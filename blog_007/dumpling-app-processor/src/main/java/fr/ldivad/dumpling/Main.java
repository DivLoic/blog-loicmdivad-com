package fr.ldivad.dumpling;

import fr.ldivad.dumpling.CommandProcessor.CartEntityMapper;
import fr.ldivad.dumpling.CommandProcessor.KeyValueCommandMapper;
import java.io.IOException;
import org.apache.beam.sdk.Pipeline;
import org.apache.beam.sdk.io.gcp.datastore.DatastoreIO;
import org.apache.beam.sdk.io.gcp.datastore.DatastoreV1;
import org.apache.beam.sdk.io.gcp.pubsub.PubsubIO;
import org.apache.beam.sdk.options.Description;
import org.apache.beam.sdk.options.PipelineOptions;
import org.apache.beam.sdk.options.PipelineOptionsFactory;
import org.apache.beam.sdk.options.StreamingOptions;
import org.apache.beam.sdk.options.Validation.Required;
import org.apache.beam.sdk.options.ValueProvider;
import org.apache.beam.sdk.transforms.Combine;
import org.apache.beam.sdk.transforms.ParDo;


public class Main {

  public interface PubSubToGcsOptions extends PipelineOptions, StreamingOptions {

    @Required
    @Description("The Cloud Pub/Sub topic subscription to read from.")
    ValueProvider<String> getInputTopicSubscription();

    void setInputTopicSubscription(ValueProvider<String> value);

    @Required
    @Description("The Cloud Datastore project to write to.")
    ValueProvider<String> getDatastoreProject();

    void setDatastoreProject(ValueProvider<String> value);
  }

  public static void main(String[] args) throws IOException {
    PubSubToGcsOptions options = PipelineOptionsFactory
        .fromArgs(args)
        .withValidation()
        .as(PubSubToGcsOptions.class);

    options.setStreaming(true);

    Pipeline pipeline = Pipeline.create(options);

    final PubsubIO.Read<Command> readFromPubSub = PubsubIO
        .readProtos(Command.class)
        .fromSubscription(options.getInputTopicSubscription());

    final DatastoreV1.Write writeInDatastore =
        DatastoreIO.v1().write().withProjectId(options.getDatastoreProject());

    pipeline
        .apply("Read PubSub Messages", readFromPubSub)

        .apply("Map in key/value pairs", new KeyValueCommandMapper().map())

        .apply("Group command in 3h windows", CommandProcessor.windowingCommand())

        .apply("Group command per key", Combine.perKey(new CommandAggregator()))

        .apply("Format user cart for Datastore", ParDo.of(new CartEntityMapper()))

        .apply("Write Datastore cart state", writeInDatastore);

    pipeline.run();
  }
}
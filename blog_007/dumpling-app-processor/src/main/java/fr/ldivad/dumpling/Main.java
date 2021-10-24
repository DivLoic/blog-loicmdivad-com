package fr.ldivad.dumpling;

import com.google.datastore.v1.Entity;
import java.awt.Window;
import java.io.IOException;
import org.apache.beam.sdk.Pipeline;
import org.apache.beam.sdk.io.gcp.datastore.DatastoreIO;
import org.apache.beam.sdk.io.gcp.pubsub.PubsubIO;
import org.apache.beam.sdk.options.Description;
import org.apache.beam.sdk.options.PipelineOptions;
import org.apache.beam.sdk.options.PipelineOptionsFactory;
import org.apache.beam.sdk.options.StreamingOptions;
import org.apache.beam.sdk.options.Validation.Required;
import org.apache.beam.sdk.transforms.Combine;
import org.apache.beam.sdk.transforms.DoFn;
import org.apache.beam.sdk.transforms.MapElements;
import org.apache.beam.sdk.transforms.ParDo;
import org.apache.beam.sdk.transforms.windowing.FixedWindows;
import org.apache.beam.sdk.values.KV;
import org.apache.beam.sdk.values.TypeDescriptor;
import org.apache.beam.sdk.values.TypeDescriptors;
import org.joda.time.Duration;


public class Main {

  public interface PubSubToGcsOptions extends PipelineOptions, StreamingOptions {

    @Description("The Cloud Pub/Sub topic subscription to read from.")
    @Required
    String getInputTopicSubscription();

    void setInputTopicSubscription(String value);
  }

  static class CartEntityMapper extends DoFn<KV<String, Cart>, Entity> {

    @ProcessElement
    public void processElement(@Element KV<String, Cart> cart, OutputReceiver<Entity> out) {
      final Entity entity = Entity.newBuilder().mergeFrom(cart.getValue()).build();
      out.output(entity);
    }
  }

  public static void main(String[] args) throws IOException {
    int numShards = 1;

    PubSubToGcsOptions options =
        PipelineOptionsFactory.fromArgs(args).withValidation().as(PubSubToGcsOptions.class);

    options.setStreaming(true);

    Pipeline pipeline = Pipeline.create(options);

    final MapElements<?, KV<String, Command>> idCommandPair = MapElements.into(
        TypeDescriptors.kvs(TypeDescriptors.strings(), TypeDescriptor.of(Command.class)));

    final PubsubIO.Read<Command> commandReader = PubsubIO
        .readProtos(Command.class)
        .fromSubscription(options.getInputTopicSubscription());

    pipeline
        .apply("Read PubSub Messages", commandReader)

        .apply("Break command into key value pairs (session - command)",
            idCommandPair.via((command) -> KV.of(command.getSession().getUserId(), command))
        )

        .apply(Window.into(FixedWindows.of(Duration.standardDays(1))))

        .apply(Combine.perKey(new CommandAggregator()))

        .apply(ParDo.of(new CartEntityMapper()))

        .apply(DatastoreIO.v1().write().withProjectId("loicmdivad"));

    pipeline.run().waitUntilFinish();
  }
}
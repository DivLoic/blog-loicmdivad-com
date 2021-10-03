package fr.ldivad.dumpling.pubsub;

import com.google.protobuf.InvalidProtocolBufferException;
import com.google.protobuf.Message;
import com.google.protobuf.util.JsonFormat;
import com.google.pubsub.v1.PubsubMessage;
import com.jasongoodwin.monads.Try;
import java.util.concurrent.TimeUnit;
import org.slf4j.Logger;
import org.springframework.cloud.gcp.pubsub.core.PubSubTemplate;

public abstract class MessagePublisher {

  protected abstract Logger logger();
  protected abstract String getTopic();

  private final JsonFormat.Printer printer = JsonFormat.printer().omittingInsignificantWhitespace();

  public Try<String> publish(PubSubTemplate template, Message message) {
    return Try.ofFailable(() -> template
        .publish(getTopic(), wrap(message))
        .get(10, TimeUnit.SECONDS));
  }

  public PubsubMessage wrap(Message message) throws InvalidProtocolBufferException {
    logger().debug("JSON ->" + printer.print(message));
    logger().debug("BINARY ->" + message.toByteString().toString());

    return PubsubMessage
        .newBuilder()
        .putAttributes("source", "kiosk-app")
        .putAttributes("version", "0.1.0-SNAPSHOT")
        .putAttributes("env", "prod")
        //.orderingkey
        .setData(message.toByteString())
        .build();
  }
}

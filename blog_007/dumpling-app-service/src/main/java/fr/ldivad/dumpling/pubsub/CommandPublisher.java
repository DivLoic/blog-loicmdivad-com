package fr.ldivad.dumpling.pubsub;

import fr.ldivad.dumpling.Command;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class CommandPublisher extends MessagePublisher<Command> {

  protected final Logger logger = LoggerFactory.getLogger(CommandPublisher.class);

  @Value("${gcp.pubsub.topic.command}")
  private String topic;

  @Override
  protected Logger logger() {
    return this.logger;
  }

  @Override
  protected String getTopic() {
    return this.topic;
  }

  @Override
  public String getOrderingKey(final Command command) {
    return command.getSession().getUserId();
  }
}

package fr.ldivad.dumpling.pubsub;

import fr.ldivad.dumpling.ExtraSauceRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class SaucePublisher extends MessagePublisher<ExtraSauceRequest> {

  protected final Logger logger = LoggerFactory.getLogger(CommandPublisher.class);

  @Override
  protected Logger logger() {
    return logger;
  }

  @Override
  protected String getTopic() {
    // /!\ fatal mistake /!\
    return "dumpling-command";
  }

  @Override
  public String getOrderingKey(final ExtraSauceRequest message) {
    return message.getUserId();
  }
}

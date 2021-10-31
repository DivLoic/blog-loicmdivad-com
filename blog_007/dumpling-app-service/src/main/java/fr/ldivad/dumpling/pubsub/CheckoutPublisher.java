package fr.ldivad.dumpling.pubsub;

import fr.ldivad.dumpling.Checkout;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class CheckoutPublisher extends MessagePublisher<Checkout> {
  protected final Logger logger = LoggerFactory.getLogger(CommandPublisher.class);

  @Value("${gcp.pubsub.topic.checkout}")
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
  public String getOrderingKey(final Checkout checkout) {
    return checkout.getSession().getUserId();
  }
}

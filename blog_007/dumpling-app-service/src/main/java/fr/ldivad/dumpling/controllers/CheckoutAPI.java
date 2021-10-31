package fr.ldivad.dumpling.controllers;

import fr.ldivad.dumpling.Checkout;
import fr.ldivad.dumpling.Command;
import fr.ldivad.dumpling.PubResponse;
import fr.ldivad.dumpling.pubsub.CheckoutPublisher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gcp.pubsub.core.PubSubTemplate;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin
@RestController
public class CheckoutAPI {

  private final PubSubTemplate template;
  private final Logger logger = LoggerFactory.getLogger(CommandAPI.class);

  @Autowired
  private CheckoutPublisher publisher;

  public CheckoutAPI(PubSubTemplate template) {
    this.template = template;
  }

  @PostMapping(path ="/checkout/", consumes = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<PubResponse> addCommand(@RequestBody Checkout checkout) {
    logger.info("CALL TO CHECKOUT \uD83D\uDCB8");

    return publisher
        .publish(template, checkout)
        .onFailure((failure) -> logger.error("Fail to publish a checkout ", failure))
        .onSuccess((message) -> logger.info("Successfully published: " + message))
        .map(PubSubController::ok)
        .recover(PubSubController::fail);
  }
}

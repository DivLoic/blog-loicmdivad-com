package fr.ldivad.dumpling.controllers;

import fr.ldivad.dumpling.Command;
import fr.ldivad.dumpling.ExtraSauceRequest;
import fr.ldivad.dumpling.PubResponse;
import fr.ldivad.dumpling.pubsub.CommandPublisher;
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
public class SauceAPI {

  private final PubSubTemplate template;
  private final Logger logger = LoggerFactory.getLogger(CommandAPI.class);

  @Autowired
  private CommandPublisher publisher;

  public SauceAPI(PubSubTemplate template) {
    this.template = template;
  }

  @PostMapping(path ="/sauce/toggle/", consumes = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<PubResponse> addCommand(@RequestBody ExtraSauceRequest request) {
    logger.info("CALL TO THE SAUCE TOGGLE 🥟");

    return publisher
        .publish(template, request)
        .onFailure((failure) -> logger.error("Fail to publish a command ", failure))
        .onSuccess((message) -> logger.info("Successfully published: " + message))
        .map(PubSubController::ok)
        .recover(PubSubController::fail);
  }

}


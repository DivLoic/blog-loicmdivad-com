package fr.ldivad.dumpling.controllers;

import com.jasongoodwin.monads.TryMapFunction;
import fr.ldivad.dumpling.Command;
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

@RestController
@CrossOrigin(maxAge = 3600)
public class CommandAPI {

  private final PubSubTemplate template;
  private final Logger logger = LoggerFactory.getLogger(CommandAPI.class);

  @Autowired
  private CommandPublisher publisher;

  public CommandAPI(PubSubTemplate template) {
    this.template = template;
  }

  @PostMapping(path ="/command/add/", consumes = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<PubResponse> addCommand(@RequestBody Command command) {
    logger.info("CALL TO THE ADD➕ CMD! 🛒");

    return publisher
        .publish(template, command)
        .onFailure((failure) -> logger.error("Fail to publish a command ", failure))
        .onSuccess((message) -> logger.info("Successfully published: " + message))
        .map(PubSubController::ok)
        .recover(PubSubController::fail);
  }

  @PostMapping(path = "/command/remove/", consumes = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<PubResponse> removeCommand(@RequestBody Command command) {
    logger.info("CALL TO THE REMOVE➖ CMD! 🛒");

    return publisher
        .publish(template, command)
        .onFailure((failure) -> logger.error("Fail to publish a command ", failure))
        .onSuccess((message) -> logger.info("Successfully published: " + message))
        .map(PubSubController::ok)
        .recover(PubSubController::fail);
  }
}



package fr.ldivad.dumpling.controllers;

import fr.ldivad.dumpling.PubResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public abstract class PubSubController {

  public static ResponseEntity<PubResponse> ok(String messageId) {
    PubResponse body = PubResponse
        .newBuilder()
        .setSuccess(true)
        .setMessageId(messageId)
        .build();

    return ResponseEntity.ok().body(body);
  }

  public static ResponseEntity<PubResponse> fail(Throwable error) {
    PubResponse body = PubResponse
        .newBuilder()
        .setSuccess(false)
        .setComment(error.getMessage())
        .build();

    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(body);
  }

}

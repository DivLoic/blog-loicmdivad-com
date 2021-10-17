package fr.ldivad.dumpling;

import static java.util.Collections.singletonList;

import java.util.Collections;
import java.util.List;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.http.MediaType;
import org.springframework.http.converter.protobuf.ProtobufJsonFormatHttpMessageConverter;
import org.springframework.web.bind.annotation.GetMapping;

@SpringBootApplication
public class Main {

  @Bean
  ProtobufJsonFormatHttpMessageConverter protobufHttpMessageConverter() {
    final ProtobufJsonFormatHttpMessageConverter protoConverter =
        new ProtobufJsonFormatHttpMessageConverter();

    protoConverter.setSupportedMediaTypes(singletonList(MediaType.APPLICATION_JSON));
    return protoConverter;
  }

  public static void main(String[] args) {
    SpringApplication.run(Main.class, args);
  }
}

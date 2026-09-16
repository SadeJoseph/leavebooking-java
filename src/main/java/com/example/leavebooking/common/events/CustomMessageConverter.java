package com.example.leavebooking.common.events;

import org.springframework.amqp.support.converter.JacksonJsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import tools.jackson.databind.json.JsonMapper;

@Configuration
public class CustomMessageConverter {

  @Bean
  public MessageConverter jsonMessageConverter(JsonMapper jsonMapper) {

    return new JacksonJsonMessageConverter(
        jsonMapper,
        "com.example.leavebooking.common.events" // just trust this applications package
    );
  }
}
package com.learn.rabbitmq.configuration.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "spring.rabbitmq", ignoreUnknownFields = false)
public class RabbitMqProperties {

  private String host;
  private String queue;
  private String exchange;
  private String routingKey;
  private String username;
  private String password;
  private String exchangeType;
}

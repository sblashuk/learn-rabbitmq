package com.learn.rabbitmq.service;

import static java.lang.String.format;

import com.learn.rabbitmq.configuration.properties.RabbitMqProperties;
import com.rabbitmq.client.Channel;
import java.io.IOException;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

@Service
@Profile("Producer")
@AllArgsConstructor
public class MessageQueueService {

  private static final Logger logger = LoggerFactory.getLogger(MessageQueueService.class);
  public static final String LOG_MESSAGE = "Sending Message - Exchange [%s] Routing Key [%s]: %s";

  private RabbitMqProperties properties;
  private Channel channel;

  public void sent(String routingKey, String message) throws IOException {
    logger.info(format(LOG_MESSAGE, properties.getExchange(), routingKey, message));
    channel.basicPublish(properties.getExchange(), routingKey, null, message.getBytes());
  }

  public void sent(String message) throws IOException {
    sent(properties.getRoutingKey(), message);
  }

  public void sentToQueue(String message) throws IOException {
    sent(properties.getQueue(), message);
  }
}

package com.learn.rabbitmq.service;

import static java.lang.String.format;

import com.learn.rabbitmq.configuration.RabbitMqProperties;
import com.rabbitmq.client.Channel;
import java.io.IOException;
import java.util.Random;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Profile;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
@Profile({"Routing & Producer"})
public class RoutingService {

  private static final Logger logger = LoggerFactory.getLogger(RoutingService.class);
  private static final String[] LOG_LEVELS = {"info", "debug", "error", "warning"};

  private RabbitMqProperties properties;
  private Channel channel;
  private MessageService messageService;

  @Scheduled(fixedRate = 1000)
  public void scheduleSendingMessages() throws IOException {
    String msg = messageService.generateHelloMessage();
    String routingKey = LOG_LEVELS[new Random().nextInt(LOG_LEVELS.length)];

    logger.info(format("Sending Log Message: [%s]: %s", routingKey, msg));
    channel.basicPublish(properties.getExchange(), routingKey, null, msg.getBytes());
  }
}

package com.learn.rabbitmq.service;

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
@Profile({"PublishSubscribe & Producer"})
public class PublishSubscribeService {

  private static final Logger logger = LoggerFactory.getLogger(PublishSubscribeService.class);
  private static final String[] NAMES = {"Siarhei", "World", "Bob"};

  private RabbitMqProperties properties;
  private Channel channel;

  @Scheduled(fixedRate = 1000)
  public void scheduleSendingMessages() throws IOException {
    String msg = String.format("info: Hello %s!", NAMES[new Random().nextInt(NAMES.length)]);
    logger.info("Sending logs message: " + msg);
    channel.basicPublish(properties.getExchange(), properties.getQueue(), null, msg.getBytes());
  }
}

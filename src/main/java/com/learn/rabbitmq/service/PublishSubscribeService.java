package com.learn.rabbitmq.service;

import com.learn.rabbitmq.configuration.RabbitMqProperties;
import com.rabbitmq.client.Channel;
import java.io.IOException;
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

  private RabbitMqProperties properties;
  private Channel channel;
  private MessageService messageService;

  @Scheduled(fixedRate = 1000)
  public void scheduleSendingMessages() throws IOException {
    String msg = messageService.generateHelloMessage();
    logger.info("Sending logs message: " + msg);
    channel.basicPublish(properties.getExchange(), properties.getRoutingkey(), null, msg.getBytes());
  }
}

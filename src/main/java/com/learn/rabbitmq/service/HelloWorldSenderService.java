package com.learn.rabbitmq.service;

import com.learn.rabbitmq.configuration.HelloWorldConfiguration;
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
@Profile("HelloWorld & Producer")
public class HelloWorldSenderService {

  private static final Logger logger = LoggerFactory.getLogger(HelloWorldConfiguration.class);

  private RabbitMqProperties properties;
  private Channel channel;
  private MessageService messageService;

  @Scheduled(fixedRate = 1000)
  public void scheduleSendingMessages() throws IOException {
    String msg = messageService.generateHelloMessage();
    logger.info("Sending Message: " + msg);
    channel.basicPublish(properties.getExchange(), properties.getQueue(), null, msg.getBytes());
  }
}

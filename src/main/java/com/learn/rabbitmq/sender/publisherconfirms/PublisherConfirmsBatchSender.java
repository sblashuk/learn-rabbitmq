package com.learn.rabbitmq.sender.publisherconfirms;

import static com.learn.rabbitmq.service.MessageQueueService.LOG_MESSAGE;
import static java.lang.String.format;

import com.learn.rabbitmq.configuration.properties.RabbitMqProperties;
import com.learn.rabbitmq.service.MessageService;
import com.rabbitmq.client.Channel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Profile;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
@Profile({"PublisherConfirms & Batch & Producer"})
public class PublisherConfirmsBatchSender {

  private static final Logger logger = LoggerFactory.getLogger(PublisherConfirmsBatchSender.class);
  private static final Integer BATCH_SIZE = 50;

  private Channel channel;
  private RabbitMqProperties properties;
  private MessageService messageService;

  private int outstandingMessageCount = 0;

  public PublisherConfirmsBatchSender(Channel channel, RabbitMqProperties properties, MessageService messageService) {
    this.channel = channel;
    this.properties = properties;
    this.messageService = messageService;
  }

  @Scheduled(fixedRate = 100)
  public void publishMessages() throws Exception {
    String message = messageService.generateHelloMessage();
    logger.info(format(LOG_MESSAGE, properties.getExchange(), properties.getQueue(), message));
    channel.basicPublish(properties.getExchange(), properties.getQueue(), null, message.getBytes());
    outstandingMessageCount++;

    if (outstandingMessageCount == BATCH_SIZE) {
      channel.waitForConfirmsOrDie(5_000);
      outstandingMessageCount = 0;
    }
  }
}

package com.learn.rabbitmq.sender.publisherconfirms;

import static com.learn.rabbitmq.service.MessageQueueService.LOG_MESSAGE;
import static java.lang.String.format;

import com.learn.rabbitmq.configuration.properties.RabbitMqProperties;
import com.learn.rabbitmq.service.MessageService;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.ConfirmCallback;
import java.util.concurrent.ConcurrentNavigableMap;
import java.util.concurrent.ConcurrentSkipListMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Profile;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
@Profile({"PublisherConfirms & Async & Producer"})
public class PublisherConfirmsIndAsyncSender {

  private static final Logger logger = LoggerFactory.getLogger(PublisherConfirmsBatchSender.class);

  private ConcurrentNavigableMap<Long, String> outstandingConfirms = new ConcurrentSkipListMap<>();

  private Channel channel;
  private RabbitMqProperties properties;
  private MessageService messageService;

  public PublisherConfirmsIndAsyncSender(Channel channel, RabbitMqProperties properties, MessageService messageService) {
    this.channel = channel;
    this.properties = properties;
    this.messageService = messageService;

    ConfirmCallback cleanOutstandingConfirms = (sequenceNumber, multiple) -> {
      if (multiple) {
        ConcurrentNavigableMap<Long, String> confirmed = outstandingConfirms.headMap(sequenceNumber, true);
        confirmed.clear();
      } else {
        outstandingConfirms.remove(sequenceNumber);
      }
    };

    ConfirmCallback confirmCallback = (sequenceNumber, multiple) -> {
      String body = outstandingConfirms.get(sequenceNumber);
      System.err.format("Message with body %s has been nack-ed. Sequence number: %d, multiple: %b%n", body, sequenceNumber, multiple);
      cleanOutstandingConfirms.handle(sequenceNumber, multiple);
    };

    channel.addConfirmListener(cleanOutstandingConfirms, confirmCallback);
  }

  @Scheduled(fixedRate = 100)
  public void publishMessages() throws Exception {
    String message = messageService.generateHelloMessage();
    outstandingConfirms.put(channel.getNextPublishSeqNo(), message);
    logger.info(format(LOG_MESSAGE, properties.getExchange(), properties.getQueue(), message));
    channel.basicPublish(properties.getExchange(), properties.getQueue(), null, message.getBytes());
  }
}

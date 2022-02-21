package com.learn.rabbitmq.sender;

import static java.lang.String.format;

import com.learn.rabbitmq.configuration.properties.RabbitMqProperties;
import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.AMQP.BasicProperties;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;
import java.io.IOException;
import java.util.Random;
import java.util.UUID;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Profile;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
@Profile({"RPC & Producer"})
public class RpcSender {

  private static final Logger logger = LoggerFactory.getLogger(RpcSender.class);

  private final Random rnd = new Random();
  private Channel channel;
  private RabbitMqProperties properties;

  @Scheduled(fixedRate = 1000)
  public void scheduleSendingMessages() throws IOException {
    final String correlationId = UUID.randomUUID().toString();
    String replyQueueName = declareQueue();
    AMQP.BasicProperties props = buildProperties(correlationId, replyQueueName);
    String message = Integer.toString(rnd.nextInt(32));

    logger.info(format("Sending: Correlation Id [%s]: Reply Queue Name [%s]: %s", correlationId, replyQueueName, message));
    channel.basicPublish(properties.getExchange(), properties.getQueue(), props, message.getBytes());

    DeliverCallback replyCallback = (consumerTag, delivery) -> {
      if (delivery.getProperties().getCorrelationId().equals(correlationId)) {
        logger.info(format("Reply for [%s] with result [%s]", message, new String(delivery.getBody())));
        channel.basicCancel(consumerTag);
      }
    };

    channel.basicConsume(replyQueueName, true, replyCallback, consumerTag -> {});
  }

  private String declareQueue() throws IOException {
    return channel.queueDeclare().getQueue();
  }

  private BasicProperties buildProperties(String correlationId, String replyQueueName) {
    return new BasicProperties
        .Builder()
        .correlationId(correlationId)
        .replyTo(replyQueueName)
        .build();
  }
}

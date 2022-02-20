package com.learn.rabbitmq.utils;

import com.learn.rabbitmq.configuration.properties.RabbitMqProperties;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;
import com.rabbitmq.client.Delivery;
import com.rabbitmq.client.Envelope;
import java.io.IOException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.connection.Connection;

public class RabbitMQFactory {

  private static final Logger logger = LoggerFactory.getLogger(RabbitMQFactory.class);

  private static final String SEPARATOR = ",";
  private static final String MESSAGE = "Received Log Message - Exchange [%s] Routing Key [%s]: %s";

  public static Channel simpleConsumerChannelBuilder(Connection connection, RabbitMqProperties properties, DeliverCallback callback)
      throws IOException {
    Channel channel = connection.createChannel(false);
    channel.queueDeclare(properties.getQueue(), false, false, false, null);
    channel.basicConsume(properties.getQueue(), true, callback, consumerTag -> {
    });
    return channel;
  }

  public static Channel simpleProducerChannelBuilder(Connection connection, RabbitMqProperties properties) throws IOException {
    Channel channel = connection.createChannel(false);
    channel.queueDeclare(properties.getQueue(), false, false, false, null);
    return channel;
  }

  public static Channel buildConsumerChannel(Connection connection, RabbitMqProperties properties) throws IOException {
    Channel channel = connection.createChannel(false);
    channel.exchangeDeclare(properties.getExchange(), properties.getExchangeType());
    String queueName = channel.queueDeclare().getQueue();

    for (String routingKey : properties.getRoutingKey().split(SEPARATOR)) {
      channel.queueBind(queueName, properties.getExchange(), routingKey);
    }

    channel.basicConsume(queueName, true, RabbitMQFactory::printLogsCallback, consumerTag -> {});
    return channel;
  }

  private static void printLogsCallback(String consumerTag, Delivery delivery) {
    Envelope envelope = delivery.getEnvelope();
    logger.info(String.format(MESSAGE, envelope.getExchange(), envelope.getRoutingKey(), new String(delivery.getBody())));
  }

  public static Channel buildProducerChannel(Connection connection, RabbitMqProperties properties) throws IOException {
    Channel channel = connection.createChannel(false);
    channel.exchangeDeclare(properties.getExchange(), properties.getExchangeType());
    return channel;
  }
}

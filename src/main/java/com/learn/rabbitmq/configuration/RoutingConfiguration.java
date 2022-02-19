package com.learn.rabbitmq.configuration;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Delivery;
import java.io.IOException;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.connection.Connection;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.scheduling.annotation.EnableScheduling;

@Configuration
@EnableScheduling
@AllArgsConstructor
@Profile("Routing")
public class RoutingConfiguration {

  private static final Logger logger = LoggerFactory.getLogger(RoutingConfiguration.class);

  public ConnectionFactory connectionFactory;
  public RabbitMqProperties properties;

  @Bean
  @Profile("Consumer")
  public Channel channelConsumer(Connection connection) throws IOException {
    Channel channel = connection.createChannel(false);
    channel.exchangeDeclare(properties.getExchange(), "direct");
    String queueName = channel.queueDeclare().getQueue();

    for (String routingKey : properties.getRoutingkey().split(",")) {
      channel.queueBind(queueName, properties.getExchange(), routingKey);
    }

    channel.basicConsume(queueName, true, this::printLogsCallback, consumerTag -> {
    });
    return channel;
  }

  @Bean
  @Profile("Producer")
  public Channel channelProducer(Connection connection) throws IOException {
    Channel channel = connection.createChannel(false);
    channel.exchangeDeclare(properties.getExchange(), "direct");
    return channel;
  }

  private void printLogsCallback(String consumerTag, Delivery delivery) {
    logger.info(String.format("Received Log Message: [%s]: %s", delivery.getEnvelope().getRoutingKey(), new String(delivery.getBody())));
  }
}

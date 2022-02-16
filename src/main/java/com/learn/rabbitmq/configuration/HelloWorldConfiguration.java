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
@Profile("HelloWorld")
public class HelloWorldConfiguration {

  private static final Logger logger = LoggerFactory.getLogger(HelloWorldConfiguration.class);

  public ConnectionFactory connectionFactory;
  public RabbitMqProperties properties;

  @Bean
  public Connection connection() {
    return connectionFactory.createConnection();
  }

  @Bean
  @Profile("Consumer")
  public Channel channelConsumer(Connection connection) throws IOException {
    Channel channel = connection.createChannel(false);
    channel.queueDeclare(properties.getQueue(), false, false, false, null);
    channel.basicConsume(properties.getQueue(), true, this::printMessageCallback, consumerTag -> {
    });
    return channel;
  }

  @Bean
  @Profile("Producer")
  public Channel channelProducer(Connection connection) throws IOException {
    Channel channel = connection.createChannel(false);
    channel.queueDeclare(properties.getQueue(), false, false, false, null);
    return channel;
  }

  private void printMessageCallback(String consumerTag, Delivery delivery) {
    logger.info("Received new message: " + new String(delivery.getBody()));
  }
}

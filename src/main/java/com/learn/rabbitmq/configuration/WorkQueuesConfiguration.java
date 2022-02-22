package com.learn.rabbitmq.configuration;

import com.learn.rabbitmq.configuration.properties.RabbitMqProperties;
import com.learn.rabbitmq.sender.WorkQueuesSender;
import com.learn.rabbitmq.utils.RabbitMQFactory;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Delivery;
import java.io.IOException;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.connection.Connection;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.scheduling.annotation.EnableScheduling;

@Configuration
@EnableScheduling
@AllArgsConstructor
@Profile("WorkQueues")
public class WorkQueuesConfiguration {

  private static final Logger logger = LoggerFactory.getLogger(HelloWorldConfiguration.class);

  public RabbitMqProperties properties;

  @Bean
  @Profile("Consumer")
  public Channel channelConsumer(Connection connection) throws IOException {
    return RabbitMQFactory.simpleConsumerChannelBuilder(connection, properties, this::startWorkConsumer);
  }

  @Bean
  @Profile("Producer")
  public Channel channelProducer(Connection connection) throws IOException {
    return RabbitMQFactory.simpleProducerChannelBuilder(connection, properties);
  }

  private void startWorkConsumer(String consumerTag, Delivery delivery) {
    String msg = new String(delivery.getBody());
    logger.info("Received new msg: " + msg);
    WorkQueuesSender.doWork(msg);
    logger.info("Finished processing msg: " + msg);
  }
}

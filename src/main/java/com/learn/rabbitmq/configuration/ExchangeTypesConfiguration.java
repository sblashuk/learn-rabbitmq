package com.learn.rabbitmq.configuration;

import com.learn.rabbitmq.configuration.properties.RabbitMqProperties;
import com.learn.rabbitmq.utils.RabbitMQFactory;
import com.rabbitmq.client.Channel;
import java.io.IOException;
import lombok.AllArgsConstructor;
import org.springframework.amqp.rabbit.connection.Connection;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.scheduling.annotation.EnableScheduling;

@Configuration
@EnableScheduling
@AllArgsConstructor
@Profile("PublishSubscribe | Routing | Topics")
public class ExchangeTypesConfiguration {

  public ConnectionFactory connectionFactory;
  public RabbitMqProperties properties;

  @Bean
  @Profile("Consumer")
  public Channel channelConsumer(Connection connection) throws IOException {
    return RabbitMQFactory.buildConsumerChannel(connection, properties);
  }

  @Bean
  @Profile("Producer")
  public Channel channelProducer(Connection connection) throws IOException {
    return RabbitMQFactory.buildProducerChannel(connection, properties);
  }
}

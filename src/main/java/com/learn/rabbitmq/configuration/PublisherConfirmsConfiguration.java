package com.learn.rabbitmq.configuration;

import com.learn.rabbitmq.configuration.properties.RabbitMqProperties;
import com.rabbitmq.client.Channel;
import java.io.IOException;
import lombok.AllArgsConstructor;
import org.springframework.amqp.rabbit.connection.Connection;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.scheduling.annotation.EnableScheduling;

@Configuration
@EnableScheduling
@AllArgsConstructor
@Profile("PublisherConfirms")
public class PublisherConfirmsConfiguration {

  public RabbitMqProperties properties;

  @Bean
  @Profile("Producer")
  public Channel channelProducer(Connection connection) throws IOException {
    Channel channel = connection.createChannel(false);
    channel.queueDeclare(properties.getQueue(), false, false, true, null);
    channel.confirmSelect();
    return channel;
  }
}

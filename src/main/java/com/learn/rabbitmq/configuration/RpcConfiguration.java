package com.learn.rabbitmq.configuration;

import static java.lang.String.format;

import com.learn.rabbitmq.configuration.properties.RabbitMqProperties;
import com.rabbitmq.client.AMQP.BasicProperties;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;
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
@Profile("RPC")
public class RpcConfiguration {

  private static final Logger logger = LoggerFactory.getLogger(RpcConfiguration.class);

  public ConnectionFactory connectionFactory;
  public RabbitMqProperties properties;

  @Bean
  @Profile("Producer")
  public Channel channelProducer(Connection connection) {
    return connection.createChannel(false);
  }

  @Bean
  @Profile("Consumer")
  public Channel channelConsumer(Connection connection) throws IOException {
    Channel channel = connection.createChannel(false);
    channel.queueDeclare(properties.getQueue(), false, false, false, null);
    channel.queuePurge(properties.getQueue());
    channel.basicQos(1);

    DeliverCallback deliverCallback = (consumerTag, delivery) -> {
      int requestNumber = Integer.parseInt(new String(delivery.getBody()));
      String message = Integer.toString(fib(requestNumber));
      String replyTo = delivery.getProperties().getReplyTo();

      logger.info(format("Got [%s] Reply To [%s] With [%s]", requestNumber, replyTo, message));
      channel.basicPublish(properties.getExchange(), replyTo, buildReplyProperties(delivery), message.getBytes());
      channel.basicAck(delivery.getEnvelope().getDeliveryTag(), false);
    };

    channel.basicConsume(properties.getQueue(), false, deliverCallback, consumerTag -> {});

    return channel;
  }

  private BasicProperties buildReplyProperties(Delivery delivery) {
    return new BasicProperties.Builder()
        .correlationId(delivery.getProperties().getCorrelationId())
        .build();
  }

  private static int fib(int n) {
    if (n == 0) return 0;
    if (n == 1) return 1;
    return fib(n - 1) + fib(n - 2);
  }
}

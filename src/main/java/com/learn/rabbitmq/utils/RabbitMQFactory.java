package com.learn.rabbitmq.utils;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;
import java.io.IOException;
import org.springframework.amqp.rabbit.connection.Connection;

public class RabbitMQFactory {

  public static Channel simpleConsumerChannelBuilder(Connection connection, String queue, DeliverCallback callback) throws IOException {
    Channel channel = connection.createChannel(false);
    channel.queueDeclare(queue, false, false, false, null);
    channel.basicConsume(queue, true, callback, consumerTag -> {
    });
    return channel;
  }

  public static Channel simpleProducerChannelBuilder(Connection connection, String queue) throws IOException {
    Channel channel = connection.createChannel(false);
    channel.queueDeclare(queue, false, false, false, null);
    return channel;
  }
}

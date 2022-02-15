package com.learn.rabbitmq.service;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
@Profile("Producer")
public class RabbitMqSender {

  @Value("${spring.rabbitmq.exchange}")
  private String exchange;
  @Value("${spring.rabbitmq.routingkey}")
  private String routingKey;

  private RabbitTemplate rabbitTemplate;

  public RabbitMqSender(RabbitTemplate rabbitTemplate) {
    this.rabbitTemplate = rabbitTemplate;
  }

  public void send(String message) {
    rabbitTemplate.convertAndSend(exchange, routingKey, message);
  }

  @Scheduled(fixedRate = 1000)
  public void scheduleSendingMessages() {
    send("Hello, World!");
  }
}

package com.learn.rabbitmq.service;

import com.learn.rabbitmq.configuration.RabbitMqProperties;
import com.rabbitmq.client.Channel;
import java.io.IOException;
import java.util.Random;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Profile;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
@Profile({"WorkQueues & Producer"})
public class WorkQueuesService {

  private static final Logger logger = LoggerFactory.getLogger(WorkQueuesService.class);
  private static final String[] NAMES = {"Siarhei", "World", "Bob"};
  private static final String KEY_WORD = "Process:";

  private RabbitMqProperties properties;
  private Channel channel;

  @Scheduled(fixedRate = 1000)
  public void scheduleSendingMessages() throws IOException {
    String message = KEY_WORD + NAMES[new Random().nextInt(NAMES.length)];
    logger.info("Sending message: " + message);
    channel.basicPublish("", properties.getQueue(), null, message.getBytes());
  }

  public static void doWork(String msg) {
    for (char ch : msg.replace(KEY_WORD, "").toCharArray()) {
      try {
        logger.info("Processing: " + ch);
        Thread.sleep(1000);
      } catch (InterruptedException _ignored) {
        Thread.currentThread().interrupt();
      }
    }
  }
}

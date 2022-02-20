package com.learn.rabbitmq.sender;

import com.learn.rabbitmq.service.MessageQueueService;
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
public class WorkQueuesSender {

  private static final Logger logger = LoggerFactory.getLogger(WorkQueuesSender.class);
  private static final String[] NAMES = {"Siarhei", "World", "Bob"};
  private static final String KEY_WORD = "Process:";

  private MessageQueueService messageQueueService;

  @Scheduled(fixedRate = 1000)
  public void scheduleSendingMessages() throws IOException {
    messageQueueService.sentToQueue(buildMessage());
  }

  private String buildMessage() {
    return KEY_WORD + NAMES[new Random().nextInt(NAMES.length)];
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

package com.learn.rabbitmq.sender;

import com.learn.rabbitmq.service.MessageQueueService;
import com.learn.rabbitmq.service.MessageService;
import java.io.IOException;
import java.util.Random;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
@Profile({"Topics & Producer"})
public class TopicsSender {

  private static final String[] SYSTEM = {"kern", "sys", "auth"};
  private static final String[] LEVEL = {"critical", "debug", "warn", "notice", "info"};
  private final Random rnd = new Random();

  private MessageQueueService messageQueueService;
  private MessageService messageService;

  @Scheduled(fixedRate = 1000)
  public void scheduleSendingMessages() throws IOException {
    messageQueueService.sent(getRandomRoutingKey(), messageService.generateHelloMessage());
  }

  private String getRandomRoutingKey() {
    return String.format("%s.%s", SYSTEM[rnd.nextInt(SYSTEM.length)], LEVEL[rnd.nextInt(LEVEL.length)]);
  }
}

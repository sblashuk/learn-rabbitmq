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
@Profile({"Routing & Producer"})
public class RoutingSender {

  private static final String[] LOG_LEVELS = {"info", "debug", "error", "warning"};

  private MessageQueueService messageQueueService;
  private MessageService messageService;

  @Scheduled(fixedRate = 1000)
  public void scheduleSendingMessages() throws IOException {
    messageQueueService.sent(buildRoutingKey(), messageService.generateHelloMessage());
  }

  private String buildRoutingKey() {
    return LOG_LEVELS[new Random().nextInt(LOG_LEVELS.length)];
  }
}

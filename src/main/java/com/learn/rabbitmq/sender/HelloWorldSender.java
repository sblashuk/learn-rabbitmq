package com.learn.rabbitmq.sender;

import com.learn.rabbitmq.service.MessageQueueService;
import com.learn.rabbitmq.service.MessageService;
import java.io.IOException;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
@Profile("HelloWorld & Producer")
public class HelloWorldSender {

  private MessageQueueService messageQueueService;
  private MessageService messageService;

  @Scheduled(fixedRate = 1000)
  public void scheduleSendingMessages() throws IOException {
    messageQueueService.sentToQueue(messageService.generateHelloMessage());
  }
}

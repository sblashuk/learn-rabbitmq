package com.learn.rabbitmq.sender.publisherconfirms;

import com.learn.rabbitmq.service.MessageQueueService;
import com.learn.rabbitmq.service.MessageService;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
@Profile({"PublisherConfirms & Individually & Producer"})
public class PublisherConfirmsIndividuallySender {

  private MessageQueueService messageQueueService;
  private MessageService messageService;

  @Scheduled(fixedRate = 100)
  public void publishMessages() throws Exception {
    messageQueueService.sentToQueueAndWait(messageService.generateHelloMessage());
  }
}

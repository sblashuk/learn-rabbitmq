package com.learn.rabbitmq.service;

import java.util.Random;
import org.springframework.stereotype.Service;

@Service
public class MessageService {

  private static final String[] NAMES = {"Siarhei", "World", "Bob", "James", "John", "Robert"};
  private final Random rnd = new Random();

  public String generateHelloMessage() {
    return String.format("Hello %s!", NAMES[rnd.nextInt(NAMES.length)]);
  }
}

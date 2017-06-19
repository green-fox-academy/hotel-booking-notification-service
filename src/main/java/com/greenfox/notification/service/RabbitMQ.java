package com.greenfox.notification.service;

import com.rabbitmq.client.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
@Getter
@Setter
public class RabbitMQ {
  private final static String QUEUE_NAME = "heartbeat";
  private int queueMessageSize;
  private Connection connection;

  public void receiveMessage() throws Exception {
    ConnectionFactory factory = new ConnectionFactory();
    factory.setUri("hotel-booking-notification-service.herokuapp.com");
    Connection connection = factory.newConnection();
    this.connection = connection;
    Channel channel = connection.createChannel();
    queueMessageSize = channel.queueDeclarePassive(QUEUE_NAME).getMessageCount();
    channel.queueDeclare(QUEUE_NAME, false, false, false, null);
    System.out.println(" [*] Waiting for messages. To exit press CTRL+C");

    Consumer consumer = new DefaultConsumer(channel) {
      @Override
      public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body)
              throws IOException {
        String message = new String(body, "UTF-8");
        System.out.println(" [x] Received '" + message + "'");
      }
    };
    channel.basicConsume(QUEUE_NAME, true, consumer);
  }

  public void sendMessage() throws Exception {
    ConnectionFactory factory = new ConnectionFactory();
    factory.setUri("hotel-booking-notification-service.herokuapp.com");
    Connection connection = factory.newConnection();
    Channel channel = connection.createChannel();

    channel.queueDeclare(QUEUE_NAME, false, false, false, null);
    String message = "Hello World!";
    channel.basicPublish("", QUEUE_NAME, null, message.getBytes("UTF-8"));
    System.out.println(" [x] Sent '" + message + "'");

    channel.close();
    connection.close();
  }
}

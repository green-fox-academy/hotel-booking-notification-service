package com.greenfox.notification.service;

import com.greenfox.notification.model.classes.Event;
import com.greenfox.notification.model.interfaces.MessageQueue;
import com.rabbitmq.client.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URISyntaxException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.concurrent.TimeoutException;

@Service
@Getter
@Setter
public class RabbitMQ implements MessageQueue {
  private Connection connection;
  private ConnectionFactory connectionFactory;
  private Channel channel;
  private Consumer consumer;

  public RabbitMQ()
      throws NoSuchAlgorithmException, KeyManagementException, URISyntaxException, IOException, TimeoutException {
    this.connectionFactory = new ConnectionFactory();
    this.connectionFactory.setUri(System.getenv("RABBITMQ_BIGWIG_RX_URL"));
    this.connection = connectionFactory.newConnection();
  }

  public void consume(String queue) throws Exception {
    channel = connection.createChannel();
    channel.queueDeclare(queue, false, false, false, null);
    consumer = new DefaultConsumer(channel) {
      @Override
      public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body)
              throws IOException {
        String message = new String(body, "UTF-8");
        System.out.println(" [x] Received '" + message + "'");
      }
    };
    channel.basicConsume(queue, true, consumer);
  }

  public boolean isQueueEmpty(String queue) throws IOException {
    return channel.queueDeclarePassive(queue).getMessageCount() == 0;
  }

  @Override
  public void push(Object queue, Object message) throws IOException {
    channel = connection.createChannel();
    Event event = new Event(message);
    channel.basicPublish("", String.valueOf(queue), null, Event.asJsonString(event).getBytes());
    System.out.println(" [x] Sent '" + Event.asJsonString(event) + "'");
  }
}

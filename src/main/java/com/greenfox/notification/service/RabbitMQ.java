package com.greenfox.notification.service;

import com.greenfox.notification.model.Event;
import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.Consumer;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.Envelope;
import java.io.IOException;
import java.net.URISyntaxException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.concurrent.TimeoutException;

import com.sendgrid.Mail;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Service;

@Service
@Getter
@Setter
public class RabbitMQ {
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

  public void push(String queue, String message) throws Exception {
    channel = connection.createChannel();
    Event event = new Event(message);
    channel.basicPublish("", queue, null, Event.asJsonString(event).getBytes());
    System.out.println(" [x] Sent '" + Event.asJsonString(event) + "'");
  }

  public void pushEmail(String queue, Mail mail) throws Exception {
    channel = connection.createChannel();
    Event event = new Event(mail);
    channel.basicPublish("", queue, null, Event.asJsonString(event).getBytes());
    System.out.println(" [x] Sent '" + Event.asJsonString(event) + "'");
  }

  public boolean isQueueEmpty(String queue) throws IOException {
    return channel.queueDeclarePassive(queue).getMessageCount() == 0;
  }
}

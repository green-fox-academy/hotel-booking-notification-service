package com.greenfox.notification.service;

import com.greenfox.notification.model.classes.Event;
import com.greenfox.notification.model.classes.TickEvent;
import com.greenfox.notification.model.interfaces.ConsumerQueue;
import com.rabbitmq.client.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URISyntaxException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.concurrent.TimeoutException;

@Service
  public class TickingQueueEventService implements ConsumerQueue {
  private Connection connection;
  private ConnectionFactory connectionFactory;
  private Channel channel;
  private Consumer consumer;
  private final Log log;
  private TickEvent tickEvent;

  @Autowired
  public TickingQueueEventService(Log log) throws NoSuchAlgorithmException, KeyManagementException, URISyntaxException, IOException, TimeoutException {
    this.connectionFactory = new ConnectionFactory();
    this.connectionFactory.setUri(System.getenv("RABBITMQ_BIGWIG_RX_URL"));
    this.tickEvent = new TickEvent("ServiceMessage");
    this.connection = connectionFactory.newConnection();
    this.log = log;
  }

  public void consume(String request, String queue) throws Exception {
    channel = connection.createChannel();
    channel.queueDeclare(queue, false, false, false, null);
    consumer = new DefaultConsumer(channel) {
      @Override
      public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body)
              throws IOException {
        String message = new String(body, "UTF-8");
        log.info(request," [x] Received '" + message + "'");
      }
    };
    channel.basicConsume(queue, true, consumer);
  }

  boolean isQueueEmpty(String queue) throws IOException {
    return channel.queueDeclarePassive(queue).getMessageCount() == 0;
  }

  @Override
  public void push(String request, Object queue, Object message) {
    try {
      tickEvent.setTickCounter(tickEvent.getTickCounter() + 1);
      channel = connection.createChannel();
      channel.basicPublish("", String.valueOf(queue), null, Event.asJsonString(tickEvent).getBytes());
      log.debug(request, TickEvent.asJsonString(tickEvent));
    } catch (IOException ex) {
      log.error(request, ex.getMessage());
      try {
        channel.basicRecover(false);
      } catch (IOException e) {
        log.error(request, e.getMessage());
      }
    }
  }
}

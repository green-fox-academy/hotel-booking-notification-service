package com.greenfox.notification.service;

import com.greenfox.notification.model.classes.Event;
import com.greenfox.notification.model.interfaces.MessageQueue;
import com.rabbitmq.client.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URISyntaxException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeoutException;

@Service
@Getter
@Setter
public class RabbitMQ implements MessageQueue {
  private Connection connection;
  private ConnectionFactory connectionFactory;
  private Channel channel;
  private Consumer consumer;
  private final Log log;
  private AMQP.BasicProperties.Builder props;
  private int actualDelayTime;
  private Map<String, Object> args;
  private Map<String, Object> headers;

  @Autowired
  public RabbitMQ(Log log)
          throws NoSuchAlgorithmException, KeyManagementException, URISyntaxException, IOException, TimeoutException {
    this.connectionFactory = new ConnectionFactory();
    this.connectionFactory.setUri(System.getenv("RABBITMQ_BIGWIG_RX_URL"));
    connectionFactory.setAutomaticRecoveryEnabled(true);
    this.connection = connectionFactory.newConnection();
    this.log = log;
    this.props = new AMQP.BasicProperties.Builder();
    this.actualDelayTime = Integer.valueOf(System.getenv("DELAY_TIME"));
    this.args = new HashMap<>();
    this.headers = new HashMap<>();
  }

  public void consume(String queue) throws Exception {
    channel = connection.createChannel();
    channel.queueDeclare(queue, false, false, false, null);
    consumer = new DefaultConsumer(channel) {
      @Override
      public void handleDelivery(String consumerTag, Envelope envelope,
                                 AMQP.BasicProperties properties, byte[] body)
              throws IOException {
        String message = new String(body, "UTF-8");
        log.info("consume method", " [x] Received '" + message + "'");
      }
    };
    channel.basicConsume(queue, true, consumer);
  }

  boolean isQueueEmpty(String queue) throws IOException {
    return channel.queueDeclarePassive(queue).getMessageCount() == 0;
  }

  @Override
  public void push(String request, Object queue, Object message) {
    int count = 0;
    try {
      channel = connection.createChannel();
      args.put("x-delayed-type", "direct");
      headers.put("x-delay", actualDelayTime);
      props.headers(headers);
      Event event = new Event(message);
      channel.exchangeDeclare((String) queue, "x-delayed-message", true, false, args);
      channel.basicPublish((String) queue, "", props.build(), Event.asJsonString(event).getBytes());
      count++;
      log.info(request, " [x] Sent '" + Event.asJsonString(event) + "'");
    } catch (IOException ex) {
      log.error(request, ex.getMessage());
      while (Integer.valueOf(System.getenv("TRY_NUMBERS")) != count) {
        Event event = new Event(message);
        try {
          channel.exchangeDeclare((String) queue, "x-delayed-message", true, false, args);
          channel.basicPublish((String) queue, "", props.build(), Event.asJsonString(event).getBytes());
          count++;
          channel.basicRecover(true);
        } catch (IOException e) {
          log.error(request, e.getMessage());
        }
        actualDelayTime *= 2;
      }
    } finally {
      actualDelayTime = Integer.valueOf(System.getenv("DELAY_TIME"));
    }
  }
}

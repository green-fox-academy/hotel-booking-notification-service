package com.greenfox.notification.service;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

import com.greenfox.notification.model.DatabaseResponse;
import com.greenfox.notification.repository.HeartbeatRepository;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.time.LocalDateTime;
import javax.servlet.http.HttpServletRequest;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

public class ServiceTests {
  private HeartbeatRepository heartbeatRepositoryMock;
  private TimeStampService timeStampServiceMock;
  private TimeStampService timeStampService = new TimeStampService();
  private ByteArrayOutputStream outContent = new ByteArrayOutputStream();
  private ByteArrayOutputStream errContent = new ByteArrayOutputStream();
  private RabbitMQ rabbitMQMock;
  private final String HOSTNAME = System.getenv("HOSTNAME");
  private String queueName = System.getenv("QUEUE_NAME");
  private HttpServletRequest requestMock;

  @Before
  public void setup() throws Exception {
    heartbeatRepositoryMock = Mockito.mock(HeartbeatRepository.class);
    timeStampServiceMock = Mockito.mock(TimeStampService.class);
    rabbitMQMock = Mockito.mock(RabbitMQ.class);
    requestMock = Mockito.mock(HttpServletRequest.class);
    ConnectionFactory factory = new ConnectionFactory();
    factory.setUri(System.getenv("RABBITMQ_BIGWIG_TX_URL"));
    Connection connection = factory.newConnection();
  }

  @Before
  public void setupStreams() {
    System.setOut(new PrintStream(outContent));
    System.setErr(new PrintStream(errContent));
  }

  @After
  public void cleanUpStreams() {
    System.setOut(null);
    System.setErr(null);
  }

  @Test
  public void testResponseLogicForOk() throws Exception {
    when(heartbeatRepositoryMock.count()).thenReturn(0L);
    when(rabbitMQMock.isQueueEmpty(queueName)).thenReturn(true);
    ResponseService responseService = new ResponseService(heartbeatRepositoryMock, rabbitMQMock);
    DatabaseResponse object = (DatabaseResponse) responseService.checkForResponse();
    assertEquals("ok", object.getStatus());
    assertEquals("error", object.getDatabase());
    assertEquals("ok", object.getQueue());
  }

  @Test
  public void testResponseLogicForError() throws Exception {
    when(heartbeatRepositoryMock.count()).thenReturn(1L);
    when(rabbitMQMock.isQueueEmpty(queueName)).thenReturn(false);
    ResponseService responseService = new ResponseService(heartbeatRepositoryMock, rabbitMQMock);
    DatabaseResponse object = (DatabaseResponse) responseService.checkForResponse();
    assertEquals("ok", object.getStatus());
    assertEquals("ok", object.getDatabase());
    assertEquals("error", object.getQueue());
  }

  @Test
  public void testLogWithCurrentTime() throws Exception {
    LocalDateTime newNow = LocalDateTime.now();
    when(timeStampServiceMock.getISO8601CurrentDate()).
            thenReturn(String.valueOf(LocalDateTime.now().withNano(0)) + "Z");
    Log log = new Log();
    assertEquals((newNow.withNano(0) + "Z"), log.getDateTime());
  }

  @Test
  public void testLogWithPrintOut() throws Exception {
    Log log = new Log();
    when(requestMock.getRequestURI()).thenReturn("/test");
    log.info(requestMock,"test message");
    assertEquals( "INFO " + log.getDateTime() + " " + HOSTNAME + " " +
            "test message " + "HTTP-REQUEST " + "/test", outContent.toString().trim());
  }

  @Test
  public void testLogWithErrorPrintOut() throws Exception {
    Log log = new Log();
    when(requestMock.getRequestURI()).thenReturn("/test");
    log.error(requestMock,"test message with error");
    assertEquals("ERROR " + log.getDateTime() + " " + HOSTNAME + " " +
            "test message with error " + "HTTP-ERROR " + "/test", errContent.toString().trim());
  }
}

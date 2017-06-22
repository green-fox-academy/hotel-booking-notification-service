package com.greenfox.notification.service;

import com.greenfox.notification.model.classes.Attribute;
import com.greenfox.notification.model.classes.Data;
import com.greenfox.notification.model.classes.DatabaseResponse;
import com.greenfox.notification.repository.HeartbeatRepository;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.sendgrid.Content;
import com.sendgrid.Email;
import com.sendgrid.Mail;
import com.sendgrid.Personalization;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import javax.servlet.http.HttpServletRequest;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.time.LocalDateTime;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

public class ServiceTests {
  private HeartbeatRepository heartbeatRepositoryMock;
  private TimeStampService timeStampServiceMock;
  private ByteArrayOutputStream outContent = new ByteArrayOutputStream();
  private ByteArrayOutputStream errContent = new ByteArrayOutputStream();
  private RabbitMQ rabbitMQMock;
  private final String HOSTNAME = System.getenv("HOSTNAME");
  private String queueName = System.getenv("QUEUE_NAME");
  private HttpServletRequest requestMock;
  private EmailGenerator emailGeneratorMock;
  private Mail testMailMock;

  @Before
  public void setup() throws Exception {
    testMailMock = Mockito.mock(Mail.class);
    emailGeneratorMock = Mockito.mock(EmailGenerator.class);
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
    Log log = new Log();
    when(timeStampServiceMock.getISO8601CurrentDate()).
            thenReturn(String.valueOf(LocalDateTime.now().withNano(0)) + "Z");
    log.setDateTime(timeStampServiceMock.getISO8601CurrentDate());
    assertEquals((newNow.withNano(0) + "Z"), log.getDateTime());
  }

  @Test
  public void testLogWithPrintOut() throws Exception {
    Log log = new Log();
    when(requestMock.getRequestURI()).thenReturn("/test");
    log.info(requestMock, "test message");
    assertEquals("INFO " + log.getDateTime() + " " + HOSTNAME + " " +
            "test message " + "HTTP-REQUEST " + "/test", outContent.toString().trim());
  }

  @Test
  public void testLogWithErrorPrintOut() throws Exception {
    Log log = new Log();
    when(requestMock.getRequestURI()).thenReturn("/test");
    log.error(requestMock, "test message with error");
    assertEquals("ERROR " + log.getDateTime() + " " + HOSTNAME + " " +
            "test message with error " + "HTTP-ERROR " + "/test", errContent.toString().trim());
  }

  @Test
  public void testGenerateEmail() throws Exception {
    Data testData = new Data("testType", new Attribute("testEmail@test.com", "testName", "testUrl.com"));
    EmailGenerator testGenerator = new EmailGenerator();
    Mail mail = new Mail();
    Email fromEmail = new Email();
    fromEmail.setEmail(System.getenv("EMAIL_ADDRESS"));
    mail.setFrom(fromEmail);
    Personalization personalization = new Personalization();
    Email to = new Email();
    to.setEmail(testData.getAttributes().getEmail());
    personalization.addTo(to);
    mail.addPersonalization(personalization);
    mail.setSubject("Registration process");
    mail.personalization.get(0).addSubstitution("-name-", testData.getAttributes().getName());
    mail.setTemplateId(System.getenv("TEMPLATE_ID"));
    Content content = new Content();
    content.setType("text/plain");
    content.setValue("vilmoskorte");
    mail.addContent(content);
    Assert.assertEquals(mail.build(), testGenerator.generateEmail(testData).build());
  }
}

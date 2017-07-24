package com.greenfox.notification.service;

import com.greenfox.notification.model.classes.DatabaseResponse;

import com.greenfox.notification.model.classes.heartbeat.Data;
import com.greenfox.notification.model.classes.registration.Attribute;
import com.greenfox.notification.model.classes.unsubscription.Error;
import com.greenfox.notification.model.classes.unsubscription.*;

import com.greenfox.notification.model.classes.booking.Booking;
import com.greenfox.notification.model.classes.booking.Bookings;

import com.greenfox.notification.repository.HeartbeatRepository;
import com.greenfox.notification.repository.UnsubscribeAttributeRepository;
import com.greenfox.notification.repository.UnsubscribeDataRepository;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.sendgrid.*;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import javax.servlet.http.HttpServletRequest;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.time.LocalDateTime;
import java.util.*;

import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.*;
import static org.mockito.Mockito.doAnswer;
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
  private SendGrid mockSg;
  private Request mockRequest;
  private EmailSenderService emailSenderServiceMock;
  private Mail mockMail;
  private EmailGenerator mockEmailGenerator;
  private UnsubscribeAttributeRepository unsubscribeAttributeRepositoryMock;
  private UnsubscribeDataRepository unsubscribeDataRepositoryMock;
  private UnsubscribeAttribute unsubscribeAttribute = new UnsubscribeAttribute();
  private UnsubscribeData unsubscribeData = new UnsubscribeData(unsubscribeAttribute);
  private UnsubscribeInput unsubscribeInput = new UnsubscribeInput(unsubscribeData);
  private SimpleDateService simpleDateService = new SimpleDateService();
  private SimpleDateService simpleDateServiceMock;
  private Errors errors = new Errors(new Error());
  private TimeStampGenerator timeStampGenerator = new TimeStampGenerator();
  private BookingReminderFiltering bookingReminderFiltering = new BookingReminderFiltering(timeStampGenerator);

  @Before
  public void setup() throws Exception {
    heartbeatRepositoryMock = Mockito.mock(HeartbeatRepository.class);
    timeStampServiceMock = Mockito.mock(TimeStampService.class);
    rabbitMQMock = Mockito.mock(RabbitMQ.class);
    requestMock = Mockito.mock(HttpServletRequest.class);
    mockSg = Mockito.mock(SendGrid.class);
    mockRequest = Mockito.mock(Request.class);
    emailSenderServiceMock = Mockito.mock(EmailSenderService.class);
    mockMail = Mockito.mock(Mail.class);
    mockEmailGenerator = Mockito.mock(EmailGenerator.class);
    unsubscribeAttributeRepositoryMock = Mockito.mock(UnsubscribeAttributeRepository.class);
    unsubscribeDataRepositoryMock = Mockito.mock(UnsubscribeDataRepository.class);
    simpleDateServiceMock = Mockito.mock(SimpleDateService.class);
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
    log.info("/test", "test message");
    assertEquals("INFO " + log.getDateTime() + " " + HOSTNAME + " " +
            "test message " + "HTTP-REQUEST " + "/test", outContent.toString().trim());
  }

  @Test
  public void testLogWithErrorPrintOut() throws Exception {
    Log log = new Log();
    when(requestMock.getRequestURI()).thenReturn("/test");
    log.error("/test", "test message with error");
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

  @Test
  public void testSendEmail() throws Exception {
    Data data = new Data("type", new Attribute("test@test.com", "testName", "testUrl"));
    when(mockEmailGenerator.generateEmail(data)).thenReturn(mockMail);
    doAnswer(invocation -> null).when(mockRequest).setMethod(Method.POST);
    doAnswer(invocation -> null).when(mockRequest).setEndpoint("mail/send");
    Response response = new Response(200, data.getAttributes().getEmail(), new HashMap<>());
    when(mockSg.api(mockRequest)).thenReturn(response);
    when(emailSenderServiceMock.sendConfirmationEmail("test", data)).thenReturn(mockMail);
    assertThat(mockMail, is(notNullValue()));
    assertEquals(mockEmailGenerator.generateEmail(data), emailSenderServiceMock.sendConfirmationEmail("test", data));
    assertTrue(mockSg.api(mockRequest).getBody().contains("test@test.com"));
  }

  @Test
  public void testUnsubscribeServiceInvalidFields() throws Exception {
    UnsubscriptionResponseService unsubscriptionResponseService =
            new UnsubscriptionResponseService(simpleDateService, unsubscribeAttributeRepositoryMock,
                    unsubscribeDataRepositoryMock, errors);
    Errors errors = (Errors) unsubscriptionResponseService.letUsersUnsubscribe(unsubscribeInput);
    assertNotNull(errors.getErrors());
    assertEquals("400", errors.getErrors().get(0).getStatus());
    assertEquals("Bad Request", errors.getErrors().get(0).getTitle());
    assertEquals("The attribute field: \"email\" is missing", errors.getErrors().get(0).getDetail());
  }

  @Test
  public void testUnsubscribeServiceValidFields() throws Exception {
    UnsubscriptionResponseService unsubscriptionResponseService =
            new UnsubscriptionResponseService(simpleDateService, unsubscribeAttributeRepositoryMock,
                    unsubscribeDataRepositoryMock, errors);
    unsubscribeInput.getData().getAttributes().setEmail("test@test.com");
    unsubscribeInput.getData().setId(1L);
    Unsubscription response = (Unsubscription) unsubscriptionResponseService.letUsersUnsubscribe(unsubscribeInput);
    assertEquals(System.getenv("HOSTNAME") + "/unsubscriptions/1", response.getLinks().getSelf());
    assertNotNull(response.getData().getAttributes().getCreatedAt());
    assertTrue(1L == response.getData().getId());
    assertEquals("test@test.com", response.getData().getAttributes().getEmail());
  }

  @Test
  public void testSimpleDateFormatService() throws Exception {
    UnsubscriptionResponseService unsubscriptionResponseService =
            new UnsubscriptionResponseService(simpleDateServiceMock, unsubscribeAttributeRepositoryMock,
                    unsubscribeDataRepositoryMock, errors);
    unsubscribeInput.getData().getAttributes().setEmail("test@test.com");
    UnsubscribeAttribute unsubscribeAttribute = unsubscribeInput.getData().getAttributes();
    unsubscribeAttribute.setCreatedAt(null);
    Date date = new Date();
    when(simpleDateServiceMock.getSimpleDateFormat()).thenReturn(date);
    Unsubscription response = (Unsubscription) unsubscriptionResponseService.letUsersUnsubscribe(unsubscribeInput);
    assertEquals(date, response.getData().getAttributes().getCreatedAt());
  }

  @Test
  public void testForFilteringBookingsOneDayLeft() throws InterruptedException {
    List<Booking> bookingList = new ArrayList<>();
    Bookings bookings = new Bookings();
    bookingList.add(new Booking(1L, 2, timeStampGenerator.getTimeStamp(1),
            timeStampGenerator.getTimeStampNow(), timeStampGenerator.getTimeStampNow(),
            "contactName", "test@test.com"));
    bookings.setBookingList(bookingList);
    Thread.sleep(1000L);
    List<Booking> filteredList = bookingReminderFiltering.findBookingsWithinOneDay(bookings);
    assertTrue(filteredList.size() == 1);
    assertEquals(filteredList, bookingList);
  }

  @Test
  public void testForFilteringBookingsSevenDayLeft() throws InterruptedException {
    List<Booking> bookingList = new ArrayList<>();
    Bookings bookings = new Bookings();
    bookingList.add(new Booking(1L, 2, timeStampGenerator.getTimeStamp(7),
            timeStampGenerator.getTimeStampNow(), timeStampGenerator.getTimeStampNow(),
            "contactName", "test@test.com"));
    bookings.setBookingList(bookingList);
    Thread.sleep(1000L);
    List<Booking> filteredList = bookingReminderFiltering.findBookingsWithinSevenDays(bookings);
    assertTrue(filteredList.size() == 1);
    assertEquals(filteredList, bookingList);
  }

  @Test
  public void testForFilteringBookingsFourteenDaysLeft() throws InterruptedException {
    List<Booking> bookingList = new ArrayList<>();
    Bookings bookings = new Bookings();
    bookingList.add(new Booking(1L, 2, timeStampGenerator.getTimeStamp(14),
            timeStampGenerator.getTimeStampNow(), timeStampGenerator.getTimeStampNow(),
            "contactName", "test@test.com"));
    bookings.setBookingList(bookingList);
    Thread.sleep(1000L);
    List<Booking> filteredList = bookingReminderFiltering.findBookingsWithinFourteenDays(bookings);
    assertTrue(filteredList.size() == 1);
    assertEquals(filteredList, bookingList);
  }
}



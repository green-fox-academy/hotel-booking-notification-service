package com.greenfox.notification.service;

import com.greenfox.notification.model.classes.DatabaseResponse;
import com.greenfox.notification.model.classes.booking.Booking;
import com.greenfox.notification.model.classes.booking.Bookings;
import com.greenfox.notification.model.classes.heartbeat.Data;
import com.greenfox.notification.model.classes.registration.Attribute;
import com.greenfox.notification.repository.HeartbeatRepository;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.sendgrid.*;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.mock.web.MockHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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
  private Response mockResponse;
  private EmailSenderService emailSenderServiceMock;
  private Mail mockMail;
  private EmailGenerator mockEmailGenerator;
  private Data mockData;
  private TimeStampGenerator timeStampGenerator = new TimeStampGenerator();
  private BookingReminderFiltering bookingReminderFiltering = new BookingReminderFiltering(timeStampGenerator);
//  private List<Booking> bookingList = new ArrayList<>();
//  private Bookings bookings = new Bookings();

  @Before
  public void setup() throws Exception {
    heartbeatRepositoryMock = Mockito.mock(HeartbeatRepository.class);
    timeStampServiceMock = Mockito.mock(TimeStampService.class);
    rabbitMQMock = Mockito.mock(RabbitMQ.class);
    requestMock = Mockito.mock(HttpServletRequest.class);
    mockSg = Mockito.mock(SendGrid.class);
    mockRequest = Mockito.mock(Request.class);
    mockResponse = Mockito.mock(Response.class);
    emailSenderServiceMock = Mockito.mock(EmailSenderService.class);
    mockMail = Mockito.mock(Mail.class);
    mockEmailGenerator = Mockito.mock(EmailGenerator.class);
    mockData = Mockito.mock(Data.class);
    ConnectionFactory factory = new ConnectionFactory();
    factory.setUri(System.getenv("RABBITMQ_BIGWIG_TX_URL"));
    Connection connection = factory.newConnection();
//    bookingList.add(new Booking(1L, 2, timeStampGenerator.getTimeStamp(1),
//            timeStampGenerator.getTimeStampNow(), timeStampGenerator.getTimeStampNow(),
//            "contactName", "test@test.com"));
//    bookings.setBookingList(bookingList);
    
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
    MockHttpServletRequest mockHttpServletRequest = new MockHttpServletRequest("/post", "/testendpoint");
    when(mockEmailGenerator.generateEmail(mockData)).thenReturn(mockMail);
    doAnswer(invocation -> {
      Object[] args = invocation.getArguments();
      System.out.println("called with arguments: " + Arrays.toString(args));
      return null;
    }).when(mockRequest).setEndpoint("mail/send");
    when(mockSg.api(mockRequest)).thenReturn(mockResponse);
    emailSenderServiceMock.sendConfirmationEmail("test", mockData);
    assertThat(mockMail, is(notNullValue()));
  }

  @Test
  public void testForFilteringBookingsOneDayLeft() throws InterruptedException {
    List<Booking> bookingList = new ArrayList<>();
    Bookings bookings = new Bookings();
    bookingList.add(new Booking(1L, 2, timeStampGenerator.getTimeStamp(1),
            timeStampGenerator.getTimeStampNow(), timeStampGenerator.getTimeStampNow(),
            "contactName", "test@test.com"));
    bookings.setBookingList(bookingList);
    Thread.sleep(10000L);
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
    Thread.sleep(10000L);
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
    Thread.sleep(10000L);
    List<Booking> filteredList = bookingReminderFiltering.findBookingsWithinFourteenDays(bookings);
    assertTrue(filteredList.size() == 1);
    assertEquals(filteredList, bookingList);
  }
}


package com.greenfox.notification.service;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

import com.greenfox.notification.model.DatabaseResponse;
import com.greenfox.notification.model.Heartbeat;
import com.greenfox.notification.model.Log;
import com.greenfox.notification.model.TimeStampUtil;
import com.greenfox.notification.repository.HeartbeatRepository;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.time.LocalDateTime;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

public class ServiceTest {

  private HeartbeatRepository heartbeatRepositoryMock;
  private TimeStampUtil timeStampUtilMock;
  private ByteArrayOutputStream outContent = new ByteArrayOutputStream();
  private ByteArrayOutputStream errContent = new ByteArrayOutputStream();

  @Before
  public void setup() throws Exception {
    heartbeatRepositoryMock = Mockito.mock(HeartbeatRepository.class);
    timeStampUtilMock = Mockito.mock(TimeStampUtil.class);
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
  public void testResponseLogicWithEmptyRepo() throws Exception {
    when(heartbeatRepositoryMock.count()).thenReturn(0L);
    ResponseValidator responseValidator = new ResponseValidator(heartbeatRepositoryMock);
    DatabaseResponse object = (DatabaseResponse) responseValidator.checkForResponse();
    assertEquals("ok", object.getStatus());
    assertEquals("error", object.getDatabase());
  }

  @Test
  public void testResponseLogicWithNotEmptyRepo() throws Exception {
    heartbeatRepositoryMock.save(new Heartbeat());
    when(heartbeatRepositoryMock.count()).thenReturn(1L);
    ResponseValidator responseValidator = new ResponseValidator(heartbeatRepositoryMock);
    DatabaseResponse object = (DatabaseResponse) responseValidator.checkForResponse();
    assertEquals("ok", object.getStatus());
    assertEquals("ok", object.getDatabase());
  }

  @Test
  public void testLogWithCurrentTime() throws Exception {
    LocalDateTime newNow = LocalDateTime.now();
    when(timeStampUtilMock.getISO8601CurrentDate()).
            thenReturn(String.valueOf(newNow));
    Log log = new Log("some-service.herokuapp.com", timeStampUtilMock.getISO8601CurrentDate());
    assertEquals((newNow.withNano(0) + "Z").length(), log.getDateTime().length());
  }

  @Test
  public void testForLogForSetMessage() throws Exception {
    Log log = new Log("some-service.herokuapp.com");
    log.info("test message");
    assertEquals("test message", log.getMessage());
  }

  @Test
  public void testLogWithPrintOut() throws Exception {
    Log log = new Log("some-service.herokuapp.com");
    log.info("test message");
    log.showLog();
    assertEquals(log.getLogLevel() + " " + log.getDateTime() + " " + log.getHostname() + " " +
            log.getMessage(), outContent.toString().trim());
  }

  @Test
  public void testLogWithErrorPrintOut() throws Exception {
    Log log = new Log("some-service.herokuapp.com");
    log.error("test message with error");
    log.showLog();
    assertEquals(log.getLogLevel() + " " + log.getDateTime() + " " + log.getHostname() + " " +
            log.getMessage(), errContent.toString().trim());
  }
}

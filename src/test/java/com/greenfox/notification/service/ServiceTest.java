package com.greenfox.notification.service;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

import com.greenfox.notification.model.DatabaseResponse;
import com.greenfox.notification.model.Heartbeat;
import com.greenfox.notification.model.Log;
import com.greenfox.notification.model.TimeStampUtil;
import com.greenfox.notification.repository.HeartbeatRepository;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDateTime;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

public class ServiceTest {

  private HeartbeatRepository heartbeatRepositoryMock;

  private TimeStampUtil timeStampUtilMock;

  @Before
  public void setup() throws Exception {
    heartbeatRepositoryMock = Mockito.mock(HeartbeatRepository.class);
    timeStampUtilMock = Mockito.mock(TimeStampUtil.class);
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
    Log log = new Log("blabla", timeStampUtilMock.getISO8601CurrentDate());
    assertEquals((newNow.withNano(0) + "Z").length(), log.getDateTime().length());
  }
}

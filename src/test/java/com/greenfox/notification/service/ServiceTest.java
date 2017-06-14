package com.greenfox.notification.service;

import com.greenfox.notification.model.DatabaseResponse;
import com.greenfox.notification.model.Heartbeat;
import com.greenfox.notification.repository.HeartbeatRepository;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

public class ServiceTest {

  private HeartbeatRepository heartbeatRepositoryMock;

  @Before
  public void setup() throws Exception {
    heartbeatRepositoryMock = Mockito.mock(HeartbeatRepository.class);
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
}

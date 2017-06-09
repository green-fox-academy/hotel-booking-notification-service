package com.greenfox.notification;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

import com.greenfox.notification.model.DatabaseResponse;
import com.greenfox.notification.model.Hearthbeat;
import com.greenfox.notification.repository.HeartbeatRepository;
import com.greenfox.notification.service.ResponseValidator;
import java.nio.charset.Charset;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = NotificationApplication.class)
@WebAppConfiguration
@EnableWebMvc
public class NotificationApplicationTests {
  private MediaType contentType = new MediaType(MediaType.APPLICATION_JSON.getType(),
          MediaType.APPLICATION_JSON.getSubtype(),
          Charset.forName("utf8"));
  private MockMvc mockMvc;

  @Autowired
  private WebApplicationContext webApplicationContext;

  @Autowired
  private HeartbeatRepository heartbeatRepositoryMock;

  @Before
  public void setup() throws Exception {
    heartbeatRepositoryMock = Mockito.mock(HeartbeatRepository.class);
    Mockito.reset(heartbeatRepositoryMock);
    this.mockMvc = webAppContextSetup(webApplicationContext).build();
  }

  @Test
  public void testGetWithEmptyTable() throws Exception {
    mockMvc.perform(get("/hearthbeat"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(contentType))
            .andExpect(jsonPath("$.status").value("ok"))
            .andExpect(jsonPath("$.database").value("error"));
  }

  @Test
  public void testGetWithNotEmptyTable() throws Exception {
    mockMvc.perform(get("/hearthbeat"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(contentType))
            .andExpect(jsonPath("$.status").value("ok"))
            .andExpect(jsonPath("$.database").value("ok"));
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
    heartbeatRepositoryMock.save(new Hearthbeat());
    when(heartbeatRepositoryMock.count()).thenReturn(1L);
    ResponseValidator responseValidator = new ResponseValidator(heartbeatRepositoryMock);
    DatabaseResponse object = (DatabaseResponse) responseValidator.checkForResponse();
    assertEquals("ok", object.getStatus());
    assertEquals("ok", object.getDatabase());
  }
  
}

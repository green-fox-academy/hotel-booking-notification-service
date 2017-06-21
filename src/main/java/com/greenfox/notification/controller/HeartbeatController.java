package com.greenfox.notification.controller;

import com.greenfox.notification.model.Data;
import com.greenfox.notification.model.Log;
import com.greenfox.notification.service.RabbitMQ;
import com.greenfox.notification.service.ResponseService;
import com.greenfox.notification.service.TimeStampService;
import com.sendgrid.Email;
import com.sendgrid.Mail;
import com.sendgrid.Method;
import com.sendgrid.Request;
import com.sendgrid.Response;
import com.sendgrid.SendGrid;
import java.io.IOException;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class HeartbeatController {

  private final ResponseService responseService;
  private final TimeStampService timeStampService;
  private final RabbitMQ rabbitMQ;

  @Autowired
  public HeartbeatController(ResponseService responseService, TimeStampService timeStampService,
      RabbitMQ rabbitMQ) {
    this.responseService = responseService;
    this.timeStampService = timeStampService;
    this.rabbitMQ = rabbitMQ;
  }

  @GetMapping("/heartbeat")
  public Object getHeartbeats(HttpServletRequest request) throws Exception {
    Log log = new Log(request.getRequestURI(), timeStampService.getISO8601CurrentDate());
    log.info("Endpoint was successfully called.");
    rabbitMQ.push("heartbeat", "wohooo");
    rabbitMQ.consume("heartbeat");
    return responseService.checkForResponse();
  }

  @PostMapping("/email/registration")
  public Data registration(@RequestBody Data data) throws IOException {
    Email from = new Email("test@example.com");
    String subject = "I'm replacing the subject tag";
    Email to = new Email(data.getAttributes().getEmail());
    com.sendgrid.Content content = new com.sendgrid.Content("text/plain", "vilmoskorte");
    Mail mail = new Mail(from, subject, to, content);
    mail.personalization.get(0).addSubstitution("-name-", data.getAttributes().getName());
    mail.setTemplateId("0783a75f-044a-4725-8624-425cc41b7735");
    SendGrid sg = new SendGrid(System.getenv("SENDGRID_API_KEY"));
    Request request = new Request();
    request.setMethod(Method.POST);
    request.setEndpoint("mail/send");
    request.setBody(mail.build());
    Response response = sg.api(request);
    System.out.println(response.getStatusCode());
    System.out.println(response.getBody());
    System.out.println(response.getHeaders());
    return data;
  }
}

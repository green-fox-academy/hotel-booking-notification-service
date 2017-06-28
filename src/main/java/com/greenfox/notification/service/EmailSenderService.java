package com.greenfox.notification.service;

import com.greenfox.notification.model.classes.Data;
import com.sendgrid.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class EmailSenderService {
  private final Log log;
  private Request request;
  private SendGrid sg;
  private Response response;
  private final RabbitMQ rabbitMQ;
  private final EmailGenerator emailGenerator;
  private static Long waitTime = Long.valueOf(System.getenv("DELAY_TIME"));

  @Autowired
  public EmailSenderService(Log log, RabbitMQ rabbitMQ, EmailGenerator emailGenerator) {
    this.log = log;
    this.request = new Request();
    this.sg = new SendGrid(System.getenv("SENDGRID_API_KEY"));
    this.rabbitMQ = rabbitMQ;
    this.emailGenerator = emailGenerator;
    this.response = new Response();
  }

  public Mail sendConfirmationEmail(String servletRequest, Data data) throws Exception {
    Mail mail = emailGenerator.generateEmail(data);
    request.setMethod(Method.POST);
    request.setEndpoint("mail/send");
    request.setBody(mail.build());
    retryOfMails(request);
    log.info(servletRequest, (response.getStatusCode() + " " + response.getBody() + " " + response.getHeaders()));
    return mail;
  }

  public void pushEmail(String servletRequest, Mail mail) {
    rabbitMQ.push(servletRequest, "email", mail);
  }

  public void consumeEmail(String servletRequest) {
    try {
      rabbitMQ.consume(servletRequest, "email");
    } catch (Exception e) {
      log.error(servletRequest, e.getMessage());
    }
  }

  private void retryOfMails(Request request) throws InterruptedException {
    int count = 0;
    while (count == Integer.valueOf(System.getenv("TRY_NUMBERS"))) {
      try {
        response = sg.api(request);
      } catch (IOException e) {
        log.error("retryOfMails ", e.getMessage());
      }
      count++;
      if (response.getStatusCode() == 201) {
        break;
      }
      sg.wait(waitTime);
      waitTime *= 2;
    }
  }
}
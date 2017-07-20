package com.greenfox.notification.service;

import com.greenfox.notification.model.classes.heartbeat.Data;
import com.sendgrid.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class EmailSenderService {
  private final UnsubscribeFiltering unsubscribeFiltering;
  private final Log log;
  private Request request;
  private SendGrid sg;
  private final RabbitMQ rabbitMQ;
  private final EmailGenerator emailGenerator;

  @Autowired
  public EmailSenderService(UnsubscribeFiltering unsubscribeFiltering, Log log, RabbitMQ rabbitMQ, EmailGenerator emailGenerator) {
    this.unsubscribeFiltering = unsubscribeFiltering;
    this.log = log;
    this.request = new Request();
    this.sg = new SendGrid(System.getenv("SENDGRID_API_KEY"));
    this.rabbitMQ = rabbitMQ;
    this.emailGenerator = emailGenerator;
  }

  public Mail sendConfirmationEmail(String servletRequest, Data data) throws Exception {
    if (!unsubscribeFiltering.checkIfUserUnsubscribed(data)) {
      Mail mail = emailGenerator.generateEmail(data);
      request.setMethod(Method.POST);
      request.setEndpoint("mail/send");
      request.setBody(mail.build());
      Response response = sg.api(request);
      log.info(servletRequest, (response.getStatusCode() + " " + response.getBody() + " " + response.getHeaders()));
      return mail;
    }
    return null;
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
}
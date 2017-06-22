package com.greenfox.notification.service;

import com.greenfox.notification.model.classes.Data;
import com.sendgrid.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;

@Service
public class EmailSenderService {
  private Log log;
  private Request request;
  private String subject;
  private SendGrid sg;
  private final RabbitMQ rabbitMQ;
  private Email sender;

  @Autowired
  public EmailSenderService(Log log, RabbitMQ rabbitMQ) {
    this.log = log;
    this.request = new Request();
    this.subject = "Registration process";
    this.sg = new SendGrid(System.getenv("SENDGRID_API_KEY"));
    this.rabbitMQ = rabbitMQ;
    this.sender = new Email("test@example.com");
  }

  public void sendConfirmationEmail(HttpServletRequest servletRequest, Data data) throws Exception {
    Email recipient = new Email(data.getAttributes().getEmail());
    com.sendgrid.Content content = new com.sendgrid.Content("text/plain", "vilmoskorte");
    Mail mail = new Mail(sender, subject, recipient, content);
    mail.personalization.get(0).addSubstitution("-name-", data.getAttributes().getName());
    mail.setTemplateId(System.getenv("TEMPLATE_ID"));
    rabbitMQ.push("email", mail);
    rabbitMQ.consume("email");
    request.setMethod(Method.POST);
    request.setEndpoint("mail/send");
    request.setBody(mail.build());
    Response response = sg.api(request);
    log.info(servletRequest, (response.getStatusCode() + " " + response.getBody()+ " " + response.getHeaders()));
  }
}
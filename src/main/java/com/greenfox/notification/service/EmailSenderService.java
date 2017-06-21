package com.greenfox.notification.service;

import com.greenfox.notification.model.Data;
import com.sendgrid.Email;
import com.sendgrid.Mail;
import com.sendgrid.Method;
import com.sendgrid.Request;
import com.sendgrid.Response;
import com.sendgrid.SendGrid;
import java.io.IOException;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class EmailSenderService {
  private Log log;
  private Request request;
  private String subject;
  private SendGrid sg;

  @Autowired
  public EmailSenderService(Log log) {
    this.log = log;
    this.request = new Request();
    this.subject = "Registration process";
    this.sg = new SendGrid(System.getenv("SENDGRID_API_KEY"));
  }

  public void sendConfirmationEmail(HttpServletRequest servletRequest, Data data) throws IOException {
    Email from = new Email("test@example.com");
    Email to = new Email(data.getAttributes().getEmail());
    com.sendgrid.Content content = new com.sendgrid.Content("text/plain", "vilmoskorte");
    Mail mail = new Mail(from, subject, to, content);
    mail.personalization.get(0).addSubstitution("-name-", data.getAttributes().getName());
    mail.setTemplateId(System.getenv("TEMPLATE_ID"));
    request.setMethod(Method.POST);
    request.setEndpoint("mail/send");
    request.setBody(mail.build());
    Response response = sg.api(request);
    log.info(servletRequest, (response.getStatusCode() + " " + response.getBody()+ " " + response.getHeaders()));
  }
}
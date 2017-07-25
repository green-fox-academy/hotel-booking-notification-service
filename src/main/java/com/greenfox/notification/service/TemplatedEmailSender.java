package com.greenfox.notification.service;

import com.greenfox.notification.model.classes.template.TemplateInput;
import com.greenfox.notification.repository.TemplateRepository;
import com.sendgrid.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class TemplatedEmailSender {
  private final TemplateRepository templateRepository;
  private final EmailGenerator emailGenerator;
  private final Log log;
  private SendGrid sg;
  private Request request;

  @Autowired
  public TemplatedEmailSender(TemplateRepository templateRepository, EmailGenerator emailGenerator, Log log) {
    this.templateRepository = templateRepository;
    this.emailGenerator = emailGenerator;
    this.log = log;
    this.request = new Request();
    this.sg = new SendGrid(System.getenv("SENDGRID_API_KEY"));
  }

  public ResponseEntity<String> sendTemplatedEmail(Long id, TemplateInput templateInput) {
    if (!templateRepository.exists(id)) {
      log.error("sendTemplatedEmail method", "Invalid template request.");
      return new ResponseEntity<>("Template does not exist.", HttpStatus.BAD_REQUEST);
    } else {
      Mail mail = emailGenerator.generateTemplateEmail(templateInput);
      try {
        Response response = sg.api(send(mail));
      } catch (IOException e) {
        log.error("sendTemplatedEmail method", e.getMessage());
      }
    }
    return new ResponseEntity<>("Email sent", HttpStatus.ACCEPTED);
  }

  private Request send(Mail mail) {
    request.setMethod(Method.POST);
    request.setEndpoint("mail/send");
    try {
      request.setBody(mail.build());
      return request;
    } catch (IOException e) {
      log.error("TemplateEmailSender send method", e.getMessage());
    }
    return null;
  }
}

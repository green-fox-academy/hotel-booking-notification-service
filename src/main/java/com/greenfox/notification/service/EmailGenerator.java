package com.greenfox.notification.service;

import com.greenfox.notification.model.classes.Data;
import com.sendgrid.Content;
import com.sendgrid.Email;
import com.sendgrid.Mail;
import org.springframework.stereotype.Service;

@Service
public class EmailGenerator {
  private Email sender;
  private String subject;

  public EmailGenerator(){
    this.subject = "Registration process";
    this.sender = new Email(System.getenv("EMAIL_ADDRESS"));
  }

  public Mail generateEmail(Data data){
    Email recipient = new Email(data.getAttributes().getEmail());
    Content content = new Content("text/plain", "vilmoskorte");
    Mail mail = new Mail(sender, subject, recipient, content);
    mail.personalization.get(0).addSubstitution("-name-", data.getAttributes().getName());
    mail.setTemplateId(System.getenv("TEMPLATE_ID"));
    return mail;
  }
}

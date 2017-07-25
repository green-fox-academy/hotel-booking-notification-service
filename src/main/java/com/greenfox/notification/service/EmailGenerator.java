package com.greenfox.notification.service;

import com.greenfox.notification.model.classes.booking.Booking;
import com.greenfox.notification.model.classes.heartbeat.Data;
import com.greenfox.notification.model.classes.template.TemplateInput;
import com.sendgrid.Content;
import com.sendgrid.Email;
import com.sendgrid.Mail;
import org.springframework.stereotype.Service;


@Service
public class EmailGenerator {
  private Email sender;
  private String subject;

  public EmailGenerator() {
    this.subject = "Registration process";
    this.sender = new Email(System.getenv("EMAIL_ADDRESS"));
  }

  public Mail generateEmail(Data data) {
    Email recipient = new Email(data.getAttributes().getEmail());
    Content content = new Content("text/plain", "vilmoskorte");
    Mail mail = new Mail(sender, subject, recipient, content);
    mail.personalization.get(0).addSubstitution("-name-", data.getAttributes().getName());
    mail.setTemplateId(System.getenv("TEMPLATE_ID"));
    return mail;
  }

  public Mail generateReminderEmail(Booking booking) {
    Email recipient = new Email(booking.getEmail());
    Content content = new Content("text/plain", "reminder");
    Mail mail = new Mail(sender, "Reminder", recipient, content);
    mail.personalization.get(0).addSubstitution("-name-", booking.getContactName());
    mail.personalization.get(0).addSubstitution("-day-", "day");
    mail.setTemplateId(System.getenv("TEMPLATE_ID_REMINDER_1_DAY"));
    return mail;
  }

  public Mail generateTemplateEmail(TemplateInput templateInput) {
    Email recipient = new Email(templateInput.getData().getAttributes().getEmail());
    Content content = new Content("text/plain", "template");
    Mail mail = new Mail(sender, "Information", recipient, content);
    mail.personalization.get(0).addSubstitution("-name-", templateInput.getData().getAttributes().getFields().get(0));
    mail.personalization.get(0).addSubstitution("-id-", templateInput.getData().getAttributes().getFields().get(1));
    mail.setTemplateId(System.getenv("TEMPLATE_EN"));
    return mail;
  }
}

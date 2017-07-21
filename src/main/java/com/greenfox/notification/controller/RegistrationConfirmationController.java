package com.greenfox.notification.controller;

import com.greenfox.notification.model.classes.heartbeat.Data;
import com.greenfox.notification.service.EmailSenderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
public class RegistrationConfirmationController {
  private final EmailSenderService emailSenderService;

  @Autowired
  public RegistrationConfirmationController(EmailSenderService emailSenderService) {
    this.emailSenderService = emailSenderService;
  }

  @PostMapping("/email/registration")
  public Data registration(@RequestBody Data data, HttpServletRequest httpServletRequest) throws Exception {
    emailSenderService.pushEmail(httpServletRequest.getRequestURI(),
            emailSenderService.sendConfirmationEmail(httpServletRequest.getRequestURI(), data));
    emailSenderService.consumeEmail(httpServletRequest.getRequestURI());
    return data;
  }
}

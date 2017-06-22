package com.greenfox.notification.controller;

import com.greenfox.notification.model.classes.Data;
import com.greenfox.notification.service.EmailSenderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
public class ConfirmationController {
  private final EmailSenderService emailSenderService;

  @Autowired
  public ConfirmationController(EmailSenderService emailSenderService) {
    this.emailSenderService = emailSenderService;
  }

  @PostMapping("/email/registration")
  public Data registration(@RequestBody Data data, HttpServletRequest httpServletRequest) throws Exception {
    emailSenderService.sendConfirmationEmail(httpServletRequest, data);
    return data;
  }
}

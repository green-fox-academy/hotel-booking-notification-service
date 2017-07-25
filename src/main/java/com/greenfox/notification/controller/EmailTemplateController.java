package com.greenfox.notification.controller;

import com.greenfox.notification.model.classes.template.TemplateAttribute;
import com.greenfox.notification.model.classes.template.TemplateInput;
import com.greenfox.notification.model.classes.template.TemplateResponse;
import com.greenfox.notification.model.interfaces.Response;
import com.greenfox.notification.service.TemplateResponseService;
import com.greenfox.notification.service.TemplatedEmailSender;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class EmailTemplateController {
  private final TemplateResponseService templateResponseService;
  private final TemplatedEmailSender templatedEmailSender;

  @Autowired
  public EmailTemplateController(TemplateResponseService templateResponseService, TemplatedEmailSender templatedEmailSender) {
    this.templateResponseService = templateResponseService;
    this.templatedEmailSender = templatedEmailSender;
  }

  @GetMapping("/templates/{id}")
  public TemplateResponse showTemplate(@PathVariable("id") Long id) {
    return templateResponseService.getTemplate(id);
  }

  @PostMapping("/templates/add")
  public Response addTemplate(@RequestBody TemplateAttribute templateAttribute) {
    return templateResponseService.addTemplate(templateAttribute);
  }

  @PutMapping("/templates/{id}/update")
  public Response updateTemplate(@PathVariable("id") Long id, @RequestBody TemplateAttribute templateAttribute){
    return templateResponseService.updateTemplate(id, templateAttribute);
  }

  @PostMapping("/send/{id}")
  public ResponseEntity<String> send(@PathVariable("id") Long id, @RequestBody TemplateInput templateInput){
    return templatedEmailSender.sendTemplatedEmail(id, templateInput);
  }
}
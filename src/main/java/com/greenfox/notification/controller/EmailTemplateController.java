package com.greenfox.notification.controller;

import com.greenfox.notification.model.classes.template.TemplateResponse;
import com.greenfox.notification.service.TemplateResponseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class EmailTemplateController {
  private final TemplateResponseService templateResponseService;

  @Autowired
  public EmailTemplateController(TemplateResponseService templateResponseService) {
    this.templateResponseService = templateResponseService;
  }

  @GetMapping("/templates/{id}")
  public TemplateResponse listTemplates(@PathVariable("id") Long id){
    return templateResponseService.getTemplates(id);
  }
}
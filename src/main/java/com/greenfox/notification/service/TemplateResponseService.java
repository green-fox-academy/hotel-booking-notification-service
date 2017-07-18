package com.greenfox.notification.service;

import com.greenfox.notification.model.classes.template.TemplateLanguageVersion;
import com.greenfox.notification.model.classes.template.TemplateResponse;
import com.greenfox.notification.repository.TemplateLanguageVersionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TemplateResponseService {
  private final TemplateLanguageVersionRepository templateLanguageVersionRepository;
  private final TemplateResponse templateResponse;

  @Autowired
  public TemplateResponseService(TemplateLanguageVersionRepository templateLanguageVersionRepository,
                                 TemplateResponse templateResponse) {
    this.templateLanguageVersionRepository = templateLanguageVersionRepository;
    this.templateResponse = templateResponse;
  }

  public TemplateResponse getTemplates() {
    templateResponse.setIncluded((List<TemplateLanguageVersion>) templateLanguageVersionRepository.findAll());
    return templateResponse;
  }
}

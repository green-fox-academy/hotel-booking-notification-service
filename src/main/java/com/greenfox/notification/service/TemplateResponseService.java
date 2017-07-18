package com.greenfox.notification.service;

import com.greenfox.notification.model.classes.template.Relationship;
import com.greenfox.notification.model.classes.template.TemplateLanguageVersion;
import com.greenfox.notification.model.classes.template.TemplateResponse;
import com.greenfox.notification.model.classes.unsubscription.Link;
import com.greenfox.notification.repository.TemplateLanguageVersionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.List;

@Service
public class TemplateResponseService implements Serializable {
  private final TemplateLanguageVersionRepository templateLanguageVersionRepository;
  private final TemplateResponse templateResponse;
  private final Relationship relationship;

  @Autowired
  public TemplateResponseService(TemplateLanguageVersionRepository templateLanguageVersionRepository,
                                 TemplateResponse templateResponse, Relationship relationship) {
    this.templateLanguageVersionRepository = templateLanguageVersionRepository;
    this.templateResponse = templateResponse;
    this.relationship = relationship;
  }

  public TemplateResponse getTemplates(Long id) {
    templateResponse.setLinks(new Link(System.getenv("HOSTNAME") + "/api/emails/"
                                        + id));
    templateResponse.getData().setId(id);
    templateResponse.getRelationships().getTemplates().getLinks().generateLinks(id);
    templateResponse.getRelationships().findTemplates();
    templateResponse.setIncluded((List<TemplateLanguageVersion>) templateLanguageVersionRepository.findAll());
    return templateResponse;
  }
}

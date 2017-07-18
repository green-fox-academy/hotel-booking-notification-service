package com.greenfox.notification.service;

import com.greenfox.notification.model.classes.OkResponse;
import com.greenfox.notification.model.classes.template.Relationship;
import com.greenfox.notification.model.classes.template.TemplateAttribute;
import com.greenfox.notification.model.classes.template.TemplateLanguageVersion;
import com.greenfox.notification.model.classes.template.TemplateResponse;
import com.greenfox.notification.model.classes.unsubscription.Link;
import com.greenfox.notification.model.interfaces.Response;
import com.greenfox.notification.repository.TemplateAttributesRepository;
import com.greenfox.notification.repository.TemplateLanguageVersionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.List;

@Service
public class TemplateResponseService implements Serializable {
  private final TemplateLanguageVersionRepository templateLanguageVersionRepository;
  private final TemplateAttributesRepository templateAttributesRepository;
  private final TemplateResponse templateResponse;
  private final Relationship relationship;

  @Autowired
  public TemplateResponseService(TemplateLanguageVersionRepository templateLanguageVersionRepository,
                                 TemplateResponse templateResponse, Relationship relationship,
                                 TemplateAttributesRepository templateAttributesRepository) {
    this.templateLanguageVersionRepository = templateLanguageVersionRepository;
    this.templateResponse = templateResponse;
    this.relationship = relationship;
    this.templateAttributesRepository = templateAttributesRepository;
  }

  public TemplateResponse getTemplate(Long id) {
    templateResponse.setLinks(new Link(System.getenv("HOSTNAME") + "/api/emails/"
                                        + id));
    templateResponse.getData().setId(id);
    templateResponse.getRelationships().getTemplates().getLinks().generateLinks(id);
    templateResponse.getRelationships().findTemplates();
    templateResponse.setIncluded((List<TemplateLanguageVersion>) templateLanguageVersionRepository.findAll());
    return templateResponse;
  }

  public Response addTemplate(TemplateAttribute templateAttribute) {
    templateAttributesRepository.save(templateAttribute);
    templateLanguageVersionRepository.save(new TemplateLanguageVersion(templateAttribute));
    return new OkResponse();
  }
}

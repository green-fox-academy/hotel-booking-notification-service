package com.greenfox.notification.model.classes.template;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.greenfox.notification.repository.TemplateLanguageVersionRepository;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Component
public class Relationship implements Serializable {
  private Template templates;
  private List<TemplateSubData> data;
  @JsonIgnore
  private final TemplateLanguageVersionRepository templateLanguageVersionRepository;
  @JsonIgnore
  private TemplateSubData templateSubData;

  @Autowired
  public Relationship(Template reviews, TemplateLanguageVersionRepository templateLanguageVersionRepository,
                      TemplateSubData templateSubData) {
    this.templates = reviews;
    this.data = new ArrayList<>();
    this.templateLanguageVersionRepository = templateLanguageVersionRepository;
    this.templateSubData = templateSubData;
  }
  
  public void findTemplates(){
    for (int i = 1; i <= templateLanguageVersionRepository.count(); i++) {
      templateSubData.setId((long) i);
      data.add(templateSubData);
      templateSubData = new TemplateSubData();
    }
  }  
}

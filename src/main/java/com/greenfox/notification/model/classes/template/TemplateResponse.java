package com.greenfox.notification.model.classes.template;

import com.greenfox.notification.model.classes.unsubscription.Link;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.List;

@Getter
@Setter
@Component
public class TemplateResponse implements Serializable {
  private Link links;
  private TemplateData data;
  private com.greenfox.notification.model.classes.template.Relationship relationships;
  private List<TemplateLanguageVersion> included;

  @Autowired
  public TemplateResponse(Relationship relationships){
    this.data = new TemplateData();
    this.relationships = relationships;
  }
}

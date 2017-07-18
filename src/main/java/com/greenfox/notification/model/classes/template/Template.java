package com.greenfox.notification.model.classes.template;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Getter
@Setter
@Component
public class Template {
  private TemplateLink links;

  @Autowired
  public Template(TemplateLink templateLink) {
    this.links = templateLink;
  }
}

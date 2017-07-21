package com.greenfox.notification.model.classes.template;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TemplateData {
  private String type;
  private Long id;
  private TemplateDataAttribute attributes;

  public TemplateData() {
    this.type = "emails";
    this.attributes = new TemplateDataAttribute();
  }
}

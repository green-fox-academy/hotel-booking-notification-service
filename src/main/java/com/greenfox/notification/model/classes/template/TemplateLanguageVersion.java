package com.greenfox.notification.model.classes.template;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class TemplateLanguageVersion {
  private String type;
  private Long id;
  private TemplateAttribute attributes;

}

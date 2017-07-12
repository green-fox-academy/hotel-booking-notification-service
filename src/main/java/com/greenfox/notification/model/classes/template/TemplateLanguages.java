package com.greenfox.notification.model.classes.template;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class TemplateLanguages {
  private String type;
  private Long id;
  private TemplateAttribute attributes;

}

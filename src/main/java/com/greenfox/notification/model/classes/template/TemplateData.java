package com.greenfox.notification.model.classes.template;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class TemplateData {
  private String type;
  private Long id;
  private TemplateDataAttribute attributes;
}

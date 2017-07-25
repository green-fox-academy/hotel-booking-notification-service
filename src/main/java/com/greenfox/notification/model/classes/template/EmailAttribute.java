package com.greenfox.notification.model.classes.template;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class EmailAttribute {
  private String email;
  private List<String> fields;
  private String language;
}

package com.greenfox.notification.model.classes.template;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@Entity
public class TemplateAttribute implements Serializable {
  @Id
  private String language;
  private String text;

}

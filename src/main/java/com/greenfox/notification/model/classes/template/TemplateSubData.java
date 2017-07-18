package com.greenfox.notification.model.classes.template;

import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;

import java.io.Serializable;

@Getter
@Setter
@Component
public class TemplateSubData implements Serializable {
  private Long id;
  private String type;

  public TemplateSubData(){
    this.type = "templates";
  }
}

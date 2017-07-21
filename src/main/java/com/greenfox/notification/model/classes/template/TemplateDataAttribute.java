package com.greenfox.notification.model.classes.template;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class TemplateDataAttribute {
  private List<String> fields;
  private String name;

  public TemplateDataAttribute(){
    this.fields = new ArrayList<>();
    fields.add("name");
    fields.add("token");
    this.name = "Register";
  }
}

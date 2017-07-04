package com.greenfox.notification.model.classes;

import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class Data {
  private String type;
  private Attribute attributes;

  public Data(String type, Attribute attributes) {
    this.type = type;
    this.attributes = attributes;
  }

  public Data() {
  }
}

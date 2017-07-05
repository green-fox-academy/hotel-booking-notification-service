package com.greenfox.notification.model.classes.heartbeat;

import com.greenfox.notification.model.classes.registration.Attribute;
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

  @Override
  public String toString() {
    return "Data{" +
            "type='" + type + '\'' +
            ", attributes=" + attributes +
            '}';
  }
}

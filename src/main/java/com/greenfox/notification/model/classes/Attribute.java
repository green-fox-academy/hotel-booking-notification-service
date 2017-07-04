package com.greenfox.notification.model.classes;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Attribute {
  private String email;
  private String name;
  private String url;

  public Attribute(String email, String name, String url) {
    this.email = email;
    this.name = name;
    this.url = url;
  }

  public Attribute() {
  }
}

package com.greenfox.notification.model.classes;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Link {

  private String self;

  public Link(String self) {
    this.self = self;
  }
}

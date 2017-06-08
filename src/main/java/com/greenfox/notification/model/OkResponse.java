package com.greenfox.notification.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OkResponse {
  private String status;

  public OkResponse() {
    this.status = "ok";
  }
}

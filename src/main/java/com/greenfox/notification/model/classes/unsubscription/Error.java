package com.greenfox.notification.model.classes.unsubscription;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class Error {
  private String status;
  private String title;
  private String detail;

  public Error(String status, String title, String detail) {
    this.status = "400";
    this.title = "Bad Request";
    this.detail = detail;
  }
}

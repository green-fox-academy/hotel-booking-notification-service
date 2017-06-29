package com.greenfox.notification.model.classes.unsubscription;

import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;

@Setter
@Getter
@Component
public class Error {
  private String status;
  private String title;
  private String detail;

  public Error() {
    this.status = "400";
    this.title = "Bad Request";
    this.detail = "The attribute field: \"email\" is missing";
  }
}

package com.greenfox.notification.model.classes;

import com.greenfox.notification.model.interfaces.Response;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OkResponse implements Response {
  private String status;

  public OkResponse() {
    this.status = "ok";
  }
}

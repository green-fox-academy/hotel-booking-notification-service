package com.greenfox.notification.model.classes;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UnsubscriptionResponse {
  private Link links;
  private Data data;

  public UnsubscriptionResponse(Link links, Data data) {
    this.links = links;
    this.data = data;
  }

  public UnsubscriptionResponse() {
  }
}

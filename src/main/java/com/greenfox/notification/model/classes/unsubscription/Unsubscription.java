package com.greenfox.notification.model.classes.unsubscription;

import com.greenfox.notification.model.interfaces.Response;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class Unsubscription implements Response {
  private Link links;
  private UnsubscribeData data;

  public Unsubscription() {
  }

  public Unsubscription(Link links, UnsubscribeData data) {
    this.links = links;
    this.data = data;
  }
}

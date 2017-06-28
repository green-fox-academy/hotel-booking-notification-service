package com.greenfox.notification.model.classes.output;

import com.greenfox.notification.model.classes.Link;
import com.greenfox.notification.model.classes.input.UnsubscribeData;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class Unsubscription {
  private Link links;
  private UnsubscribeData data;

  public Unsubscription() {
  }

  public Unsubscription(Link links, UnsubscribeData data) {
    this.links = links;
    this.data = data;
  }



}

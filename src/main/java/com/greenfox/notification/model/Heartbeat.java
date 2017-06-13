package com.greenfox.notification.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
@Getter
@Setter
public class Heartbeat {
  @Id
  private Boolean status;

  public Heartbeat(){
    this.status = true;
  }
}

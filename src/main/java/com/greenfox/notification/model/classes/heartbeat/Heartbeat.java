package com.greenfox.notification.model.classes.heartbeat;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Getter
@Setter
@Table(name = "heartbeat")
public class Heartbeat {
  @Id
  private Boolean status;

  public Heartbeat(){
    this.status = true;
  }
}

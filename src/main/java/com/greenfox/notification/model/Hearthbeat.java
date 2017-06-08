package com.greenfox.notification.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
@Getter
@Setter
public class Hearthbeat {
  @Id
  private Boolean status;

  public Hearthbeat(){
    this.status = true;
  }
}

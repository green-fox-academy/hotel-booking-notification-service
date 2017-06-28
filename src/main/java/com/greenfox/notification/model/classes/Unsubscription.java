package com.greenfox.notification.model.classes;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "unsubscription")
@Getter
@Setter
public class Unsubscription {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private long id;
  private String email;

  public Unsubscription() {
  }

  public Unsubscription(String email) {
    this.email = email;
  }
}

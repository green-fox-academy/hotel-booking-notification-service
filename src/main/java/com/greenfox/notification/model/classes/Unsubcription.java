package com.greenfox.notification.model.classes;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "unsubcription")
@Getter
@Setter
public class Unsubcription {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  long id;

  public Unsubcription() {
  }
}

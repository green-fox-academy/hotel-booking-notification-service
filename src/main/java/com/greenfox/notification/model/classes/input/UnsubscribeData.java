package com.greenfox.notification.model.classes.input;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class UnsubscribeData {
  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Long id;
  private String type;
  @OneToOne
  @JoinColumn(name = "email")
  private UnsubscribeAttribute attributes;

  public UnsubscribeData(UnsubscribeAttribute attributes) {
    this.attributes = attributes;
  }

  public UnsubscribeData(){

  }
}

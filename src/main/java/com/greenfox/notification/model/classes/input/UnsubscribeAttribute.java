package com.greenfox.notification.model.classes.input;

import com.fasterxml.jackson.annotation.JsonProperty;
import javax.persistence.Entity;
import javax.persistence.Id;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class UnsubscribeAttribute {
  @Id
  private String email;
  @JsonProperty(value = "created_at")
  private String createdAt;

  public UnsubscribeAttribute(String email, String createdAt) {
    this.email = email;
    this.createdAt = createdAt;
  }

  public UnsubscribeAttribute(){

  }
}

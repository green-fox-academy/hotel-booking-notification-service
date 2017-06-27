package com.greenfox.notification.model.classes;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.greenfox.notification.service.SimpleDateService;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;

@Getter
@Setter
public class Attribute {
  private String email;
  private String name;
  private String url;
  private String created_at;
  @JsonIgnore
  public SimpleDateService simpleDateService;

  public Attribute(String email, String name, String url) {
    this.email = email;
    this.name = name;
    this.url = url;
  }

  public Attribute() {
  }

  @Autowired
  public Attribute(String email, SimpleDateService simpleDateService) {
    this.email = email;
    this.simpleDateService = simpleDateService;
    this.created_at = simpleDateService.getDateInString();
  }
}

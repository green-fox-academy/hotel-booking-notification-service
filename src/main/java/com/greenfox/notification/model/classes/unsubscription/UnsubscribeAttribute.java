package com.greenfox.notification.model.classes.unsubscription;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.greenfox.notification.service.SimpleDateService;
import java.util.Date;
import javax.persistence.Entity;
import javax.persistence.Id;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.beans.factory.annotation.Autowired;

@Getter
@Setter
@Entity
public class UnsubscribeAttribute {

  @Id
  @NotEmpty
  private String email;
  @JsonProperty(value = "created_at")
  private Date createdAt;
  @JsonIgnore
  private SimpleDateService simpleDateService;

  public UnsubscribeAttribute(String email, Date createdAt) {
    this.email = email;
    this.createdAt = createdAt;
  }

  public UnsubscribeAttribute(Date createdAt) {
    this.createdAt = createdAt;
  }

  public UnsubscribeAttribute() {
  }

  @Autowired
  public UnsubscribeAttribute(SimpleDateService simpleDateService, String email) {
    this.email = email;
    this.createdAt = simpleDateService.getSimpleDateFormat();
  }
}

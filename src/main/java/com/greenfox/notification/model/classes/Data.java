package com.greenfox.notification.model.classes;

import com.greenfox.notification.repository.UnsubcriptionRepository;
import lombok.Getter;
import lombok.Setter;
import org.codehaus.jackson.annotate.JsonIgnore;
import org.springframework.beans.factory.annotation.Autowired;


@Getter
@Setter
public class Data {
  private String type;
  private String id;
  private Attribute attributes;
  @JsonIgnore
  private UnsubcriptionRepository unsubcriptionRepository;

  public Data(String type, Attribute attributes) {
    this.type = type;
    this.attributes = attributes;
  }

  @Autowired
  public Data(String type, String id, UnsubcriptionRepository unsubcriptionRepository, Attribute attributes) {
    this.type = type;
    this.id = String.valueOf(unsubcriptionRepository.findOne(Long.valueOf(id)));
    this.attributes = attributes;
    this.unsubcriptionRepository = unsubcriptionRepository;
  }

  public Data() {
  }
}

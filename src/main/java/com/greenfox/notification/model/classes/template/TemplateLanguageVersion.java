package com.greenfox.notification.model.classes.template;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;

@Getter
@Setter
@Entity
public class TemplateLanguageVersion implements Serializable {
  private String type;
  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Long id;
  @OneToOne
  @JoinColumns({
          @JoinColumn(name = "language", referencedColumnName = "language"),
          @JoinColumn(name = "text", referencedColumnName = "text")
  })
  private TemplateAttribute attributes;

  public TemplateLanguageVersion(TemplateAttribute attributes){
    this.type = "templates";
    this.attributes = attributes;
  }

  public TemplateLanguageVersion(){
  }
}

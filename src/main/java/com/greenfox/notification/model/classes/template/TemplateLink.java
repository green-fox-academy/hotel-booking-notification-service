package com.greenfox.notification.model.classes.template;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Component;

@Getter
@Setter
@NoArgsConstructor
@Component
public class TemplateLink {
  private String self;
  private String related;

  public void generateLinks(Long id){
    this.self = System.getenv("HOSTNAME") + "/api/emails/" + id + "/relationships/templates";
    this.related = System.getenv("HOSTNAME") + "/api/emails/" + id + "/templates";
  }
}

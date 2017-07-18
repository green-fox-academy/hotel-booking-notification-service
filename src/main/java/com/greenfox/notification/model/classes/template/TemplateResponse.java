package com.greenfox.notification.model.classes.template;

import com.greenfox.notification.model.classes.unsubscription.Link;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.aspectj.asm.internal.Relationship;
import org.springframework.stereotype.Component;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@Component
public class TemplateResponse {
  private Link links;
  private TemplateData data;
  private Relationship relationships;
  private List<TemplateLanguageVersion> included;


}

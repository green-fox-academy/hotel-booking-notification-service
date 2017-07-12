package com.greenfox.notification.model.classes.template;

import com.greenfox.notification.model.classes.heartbeat.Data;
import com.greenfox.notification.model.classes.unsubscription.Link;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.aspectj.asm.internal.Relationship;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class TemplateResponse {
  private Link links;
  private Data data;
  private Relationship relationships;
  private List<TemplateLanguageVersion> included;



}

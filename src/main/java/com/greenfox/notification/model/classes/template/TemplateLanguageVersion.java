package com.greenfox.notification.model.classes.template;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@NoArgsConstructor
@Entity
public class TemplateLanguageVersion {
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

}

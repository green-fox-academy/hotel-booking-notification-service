package com.greenfox.notification.repository;

import com.greenfox.notification.model.classes.template.TemplateAttribute;
import org.springframework.data.repository.CrudRepository;

public interface TemplateAttributesRepository extends CrudRepository<TemplateAttribute, String> {
}

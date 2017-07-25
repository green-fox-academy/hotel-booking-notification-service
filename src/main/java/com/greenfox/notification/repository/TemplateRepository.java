package com.greenfox.notification.repository;

import com.greenfox.notification.model.classes.template.EmailTemplate;
import org.springframework.data.repository.CrudRepository;

public interface TemplateRepository extends CrudRepository<EmailTemplate, Long> {
}

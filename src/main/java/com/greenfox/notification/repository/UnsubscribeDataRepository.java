package com.greenfox.notification.repository;


import com.greenfox.notification.model.classes.unsubscription.UnsubscribeData;
import org.springframework.data.repository.CrudRepository;

public interface UnsubscribeDataRepository extends CrudRepository<UnsubscribeData, Long> {
  UnsubscribeData findByAttributes_Email(String email);
}

package com.greenfox.notification.repository;


import com.greenfox.notification.model.classes.Unsubscription;
import org.springframework.data.repository.CrudRepository;

public interface UnsubcriptionRepository extends CrudRepository<Unsubscription, Long> {
}

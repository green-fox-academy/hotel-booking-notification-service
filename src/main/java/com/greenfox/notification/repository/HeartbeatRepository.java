package com.greenfox.notification.repository;

import com.greenfox.notification.model.Hearthbeat;
import org.springframework.data.repository.CrudRepository;

public interface HeartbeatRepository extends CrudRepository<Hearthbeat, Boolean> {
}

package com.greenfox.notification.repository;

import com.greenfox.notification.model.Heartbeat;
import org.springframework.data.repository.CrudRepository;

public interface HeartbeatRepository extends CrudRepository<Heartbeat, Boolean> {
}

package com.greenfox.notification.repository;

import com.greenfox.notification.model.classes.heartbeat.Heartbeat;
import org.springframework.data.repository.CrudRepository;

public interface HeartbeatRepository extends CrudRepository<Heartbeat, Boolean> {
}

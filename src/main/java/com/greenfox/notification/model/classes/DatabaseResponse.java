package com.greenfox.notification.model.classes;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DatabaseResponse {
  private String status;
  private String database;
  private String queue;

  public DatabaseResponse(String status, String database, String queue) {
    this.status = status;
    this.database = database;
    this.queue = queue;
  }
}


package com.greenfox.notification.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DatabaseResponse {
  private String status;
  private String database;

  public DatabaseResponse(String status, String database) {
    this.status = status;
    this.database = database;
  }
}


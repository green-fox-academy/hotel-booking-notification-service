package com.greenfox.notification.model.classes;

import com.greenfox.notification.model.interfaces.Response;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DatabaseResponse implements Response {
  private String status;
  private String database;
  private String queue;

  public DatabaseResponse(String status, String database, String queue) {
    this.status = status;
    this.database = database;
    this.queue = queue;
  }

  @Override
  public String toString() {
    return "DatabaseResponse{" +
            "status='" + status + '\'' +
            ", database='" + database + '\'' +
            ", queue='" + queue + '\'' +
            '}';
  }
}


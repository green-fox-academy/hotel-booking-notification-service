package com.greenfox.notification.model.classes.unsubscription;

import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Errors {
  private List<Error> errors;

  public Errors(Error error) {
    this.errors = new ArrayList<>();
    errors.add(error);
  }
}

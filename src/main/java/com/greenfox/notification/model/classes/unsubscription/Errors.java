package com.greenfox.notification.model.classes.unsubscription;

import com.greenfox.notification.model.interfaces.Response;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Getter
@Setter
@Component
public class Errors implements Response {
  private List<Error> errors;

  @Autowired
  public Errors(Error error) {
    this.errors = new ArrayList<>();
    errors.add(error);
  }
}

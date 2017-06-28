package com.greenfox.notification.service;

import java.text.SimpleDateFormat;
import javax.persistence.Embeddable;
import org.springframework.stereotype.Service;

@Service
@Embeddable
public class SimpleDateService {
  public String getDateInString () {
    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z");
    return String.valueOf(simpleDateFormat);
  }

}

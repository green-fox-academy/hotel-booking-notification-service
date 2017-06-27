package com.greenfox.notification.service;

import java.text.SimpleDateFormat;
import org.springframework.stereotype.Service;

@Service
public class SimpleDateService {
  public String getDateInString () {
    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z");
    return String.valueOf(simpleDateFormat);
  }

}

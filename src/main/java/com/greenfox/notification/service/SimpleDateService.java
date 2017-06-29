package com.greenfox.notification.service;

import java.text.SimpleDateFormat;
import java.util.Date;
import javax.persistence.Embeddable;
import org.springframework.stereotype.Service;

@Service
@Embeddable
public class SimpleDateService {
  public Date getSimpleDateFormat() {
    Date date = new Date();
    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ");
    simpleDateFormat.format(date);
    return date;
  }

}

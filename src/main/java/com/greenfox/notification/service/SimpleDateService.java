package com.greenfox.notification.service;

import org.springframework.stereotype.Service;

import javax.persistence.Embeddable;
import java.text.SimpleDateFormat;
import java.util.Date;

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

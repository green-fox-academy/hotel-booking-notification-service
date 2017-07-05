package com.greenfox.notification.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class BookingController {

  @GetMapping("/bookings")
  public String isReminderisDue() {
    return "wohoo";
  }

}

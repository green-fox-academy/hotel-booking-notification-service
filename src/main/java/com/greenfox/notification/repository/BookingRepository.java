package com.greenfox.notification.repository;

import com.greenfox.notification.model.classes.booking.Booking;
import org.springframework.data.repository.CrudRepository;

import java.sql.Timestamp;
import java.util.List;

public interface BookingRepository extends CrudRepository<Booking, Long> {
  List<Booking> findAllByStartDateLessThanEqual(Timestamp timeStamp);

  List<Booking> findAllByStartDateBetween(Timestamp timeStamp, Timestamp timeStamp2);
}

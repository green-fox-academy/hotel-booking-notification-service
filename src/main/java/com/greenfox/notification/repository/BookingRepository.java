package com.greenfox.notification.repository;

import com.greenfox.notification.model.classes.booking.Booking;
import org.springframework.data.repository.CrudRepository;

public interface BookingRepository extends CrudRepository<Booking, Long> {

}

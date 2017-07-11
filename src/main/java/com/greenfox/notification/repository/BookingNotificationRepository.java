package com.greenfox.notification.repository;

import com.greenfox.notification.model.classes.booking.BookingNotification;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

public interface BookingNotificationRepository extends CrudRepository<BookingNotification, String> {
}

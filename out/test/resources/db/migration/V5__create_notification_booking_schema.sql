CREATE TABLE notified_bookings (email VARCHAR(255), PRIMARY KEY (email), notified_one_day_before BINARY,
                                notified_seven_days_before BINARY, notified_fourteen_days_before BINARY);

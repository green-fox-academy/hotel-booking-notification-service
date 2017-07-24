CREATE TABLE notified_bookings (email VARCHAR(255), PRIMARY KEY (email), notified_one_day_before BOOLEAN,
                      notified_seven_days_before BOOLEAN, notified_fourteen_days_before BOOLEAN);

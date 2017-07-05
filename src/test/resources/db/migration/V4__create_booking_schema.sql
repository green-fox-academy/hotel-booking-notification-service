CREATE TABLE booking (id INTEGER NOT NULL , PRIMARY KEY (id), guest INTEGER,
  start_date TIMESTAMP, end_date TIMESTAMP, created_at TIMESTAMP, description VARCHAR(255));
ALTER TABLE `heartbeat`
  ADD COLUMN id INT AUTO_INCREMENT,
  DROP PRIMARY KEY,
  ADD PRIMARY KEY (id);
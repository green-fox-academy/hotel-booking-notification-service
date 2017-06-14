CREATE TABLE `heartbeat` (status bit not null, primary key (status));

ALTER TABLE `heartbeat`
	ADD COLUMN `id` INT NOT NULL AUTO_INCREMENT AFTER `status`,
	DROP PRIMARY KEY,
	ADD PRIMARY KEY (`id`);
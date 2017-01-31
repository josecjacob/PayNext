# PayNext

NOTES:

This is a RESTFul payment solution which will allows for the creation of an account, transfer money and checking balances. We also allow for a user to login into the system and logout of it. An admin user can find the list of all users who are in the system.

For generating the MySQL schema and tables, use this script:

DROP SCHEMA IF EXISTS `paynext` ;

CREATE SCHEMA IF NOT EXISTS `paynext` DEFAULT CHARACTER SET latin1 ;
USE `paynext` ;

DROP TABLE IF EXISTS `paynext`.`account` ;

CREATE TABLE IF NOT EXISTS `paynext`.`account` (
  `account_id` VARCHAR(255) NOT NULL,
  `account_holder_name` VARCHAR(255) NOT NULL,
  `balance` DECIMAL(22,2) NULL DEFAULT NULL,
  `tombstoned` BIT(1) NOT NULL,
  `user_name` VARCHAR(255) NOT NULL,
  PRIMARY KEY (`account_id`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = latin1;


DROP TABLE IF EXISTS `paynext`.`accountactivity` ;

CREATE TABLE IF NOT EXISTS `paynext`.`accountactivity` (
  `id` BIGINT(20) NOT NULL AUTO_INCREMENT,
  `account_id` VARCHAR(255) NULL DEFAULT NULL,
  `amount` DECIMAL(22,2) NULL DEFAULT NULL,
  `credit` BIT(1) NOT NULL,
  `timestamp` TIME NULL DEFAULT NULL,
  PRIMARY KEY (`id`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = latin1;


DROP TABLE IF EXISTS `paynext`.`session` ;

CREATE TABLE IF NOT EXISTS `paynext`.`session` (
  `session_id` VARCHAR(255) NOT NULL,
  `expired` BIT(1) NOT NULL,
  `last_access_time` BIGINT(20) NOT NULL,
  `timeout` INT(11) NOT NULL,
  `user_name` VARCHAR(255) NOT NULL,
  PRIMARY KEY (`session_id`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = latin1;


DROP TABLE IF EXISTS `paynext`.`user` ;

CREATE TABLE IF NOT EXISTS `paynext`.`user` (
  `user_name` VARCHAR(255) NOT NULL,
  `account_type` VARCHAR(255) NOT NULL,
  `current_session_id` VARCHAR(255) NULL DEFAULT NULL,
  `disabled` BIT(1) NOT NULL,
  `password` VARCHAR(255) NOT NULL,
  PRIMARY KEY (`user_name`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = latin1;
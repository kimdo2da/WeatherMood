-- MySQL Workbench Forward Engineering

SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION';

-- -----------------------------------------------------
-- Schema weathermood_db
-- -----------------------------------------------------
CREATE SCHEMA IF NOT EXISTS `weathermood_db`
DEFAULT CHARACTER SET utf8mb4
COLLATE utf8mb4_unicode_ci;

USE `weathermood_db`;

-- -----------------------------------------------------
-- Table `weathermood_db`.`users`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `weathermood_db`.`users` (
  `user_id` BIGINT NOT NULL AUTO_INCREMENT,
  `email` VARCHAR(100) NOT NULL,
  `password` VARCHAR(255) NOT NULL,
  `nickname` VARCHAR(50) NOT NULL,
  `created_at` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`user_id`),
  UNIQUE INDEX `email_UNIQUE` (`email` ASC) VISIBLE
)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `weathermood_db`.`weather_types`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `weathermood_db`.`weather_types` (
  `weather_id` BIGINT NOT NULL AUTO_INCREMENT,
  `weather_code` VARCHAR(50) NOT NULL,
  `weather_name` VARCHAR(50) NOT NULL,
  `description` VARCHAR(255) NULL,
  PRIMARY KEY (`weather_id`),
  UNIQUE INDEX `weather_code_UNIQUE` (`weather_code` ASC) VISIBLE
)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `weathermood_db`.`route_types`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `weathermood_db`.`route_types` (
  `route_id` BIGINT NOT NULL AUTO_INCREMENT,
  `route_name` VARCHAR(50) NOT NULL,
  `description` VARCHAR(255) NULL,
  PRIMARY KEY (`route_id`),
  UNIQUE INDEX `route_name_UNIQUE` (`route_name` ASC) VISIBLE
)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `weathermood_db`.`endings`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `weathermood_db`.`endings` (
  `ending_id` BIGINT NOT NULL AUTO_INCREMENT,
  `ending_name` VARCHAR(100) NOT NULL,
  `description` TEXT NULL,
  `condition_text` VARCHAR(255) NULL,
  `route_id` BIGINT NOT NULL,
  `weather_id` BIGINT NULL,
  PRIMARY KEY (`ending_id`),
  INDEX `fk_endings_route_types_idx` (`route_id` ASC) VISIBLE,
  INDEX `fk_endings_weather_types_idx` (`weather_id` ASC) VISIBLE,
  CONSTRAINT `fk_endings_route_types`
    FOREIGN KEY (`route_id`)
    REFERENCES `weathermood_db`.`route_types` (`route_id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_endings_weather_types`
    FOREIGN KEY (`weather_id`)
    REFERENCES `weathermood_db`.`weather_types` (`weather_id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION
)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `weathermood_db`.`contents`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `weathermood_db`.`contents` (
  `content_id` BIGINT NOT NULL AUTO_INCREMENT,
  `title` VARCHAR(150) NOT NULL,
  `content_type` VARCHAR(30) NOT NULL,
  `genre` VARCHAR(100) NULL,
  `mood_tag` VARCHAR(255) NULL,
  `weather_tag` VARCHAR(255) NULL,
  `description` TEXT NULL,
  `poster_url` VARCHAR(500) NULL,
  `external_api_id` VARCHAR(100) NULL,
  `created_at` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `route_id` BIGINT NOT NULL,
  PRIMARY KEY (`content_id`),
  INDEX `fk_contents_route_types_idx` (`route_id` ASC) VISIBLE,
  CONSTRAINT `fk_contents_route_types`
    FOREIGN KEY (`route_id`)
    REFERENCES `weathermood_db`.`route_types` (`route_id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION
)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `weathermood_db`.`simulation_questions`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `weathermood_db`.`simulation_questions` (
  `question_id` BIGINT NOT NULL AUTO_INCREMENT,
  `question_text` VARCHAR(255) NOT NULL,
  `question_order` INT NOT NULL,
  `is_active` TINYINT NOT NULL DEFAULT 1,
  PRIMARY KEY (`question_id`)
)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `weathermood_db`.`simulation_choices`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `weathermood_db`.`simulation_choices` (
  `choice_id` BIGINT NOT NULL AUTO_INCREMENT,
  `choice_text` VARCHAR(255) NOT NULL,
  `choice_order` INT NOT NULL,
  `route_score` INT NOT NULL,
  `emotion_name` VARCHAR(50) NOT NULL,
  `emotion_score` INT NOT NULL,
  `question_id` BIGINT NOT NULL,
  `route_id` BIGINT NOT NULL,
  PRIMARY KEY (`choice_id`),
  INDEX `fk_simulation_choices_simulation_questions_idx` (`question_id` ASC) VISIBLE,
  INDEX `fk_simulation_choices_route_types_idx` (`route_id` ASC) VISIBLE,
  CONSTRAINT `fk_simulation_choices_simulation_questions`
    FOREIGN KEY (`question_id`)
    REFERENCES `weathermood_db`.`simulation_questions` (`question_id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_simulation_choices_route_types`
    FOREIGN KEY (`route_id`)
    REFERENCES `weathermood_db`.`route_types` (`route_id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION
)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `weathermood_db`.`simulation_results`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `weathermood_db`.`simulation_results` (
  `result_id` BIGINT NOT NULL AUTO_INCREMENT,
  `main_emotion` VARCHAR(50) NOT NULL,
  `total_score` INT NOT NULL,
  `weather_text` VARCHAR(100) NULL,
  `temperature` DECIMAL(5,2) NULL,
  `recommended_contents` JSON NULL,
  `created_at` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `user_id` BIGINT NOT NULL,
  `weather_id` BIGINT NOT NULL,
  `route_id` BIGINT NOT NULL,
  `ending_id` BIGINT NOT NULL,
  PRIMARY KEY (`result_id`),
  INDEX `fk_simulation_results_users_idx` (`user_id` ASC) VISIBLE,
  INDEX `fk_simulation_results_weather_types_idx` (`weather_id` ASC) VISIBLE,
  INDEX `fk_simulation_results_route_types_idx` (`route_id` ASC) VISIBLE,
  INDEX `fk_simulation_results_endings_idx` (`ending_id` ASC) VISIBLE,
  CONSTRAINT `fk_simulation_results_users`
    FOREIGN KEY (`user_id`)
    REFERENCES `weathermood_db`.`users` (`user_id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_simulation_results_weather_types`
    FOREIGN KEY (`weather_id`)
    REFERENCES `weathermood_db`.`weather_types` (`weather_id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_simulation_results_route_types`
    FOREIGN KEY (`route_id`)
    REFERENCES `weathermood_db`.`route_types` (`route_id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_simulation_results_endings`
    FOREIGN KEY (`ending_id`)
    REFERENCES `weathermood_db`.`endings` (`ending_id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION
)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `weathermood_db`.`diaries`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `weathermood_db`.`diaries` (
  `diary_id` BIGINT NOT NULL AUTO_INCREMENT,
  `title` VARCHAR(100) NOT NULL,
  `content` TEXT NOT NULL,
  `mood_text` VARCHAR(50) NULL,
  `created_at` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` TIMESTAMP NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `user_id` BIGINT NOT NULL,
  `result_id` BIGINT NOT NULL,
  PRIMARY KEY (`diary_id`),
  INDEX `fk_diaries_users_idx` (`user_id` ASC) VISIBLE,
  INDEX `fk_diaries_simulation_results_idx` (`result_id` ASC) VISIBLE,
  CONSTRAINT `fk_diaries_users`
    FOREIGN KEY (`user_id`)
    REFERENCES `weathermood_db`.`users` (`user_id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_diaries_simulation_results`
    FOREIGN KEY (`result_id`)
    REFERENCES `weathermood_db`.`simulation_results` (`result_id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION
)
ENGINE = InnoDB;
SELECT * FROM users;
INSERT INTO weather_types (weather_code, weather_name, description)
VALUES
('CLEAR', '맑음', '맑은 날씨'),
('RAIN', '비', '비 오는 날씨'),
('CLOUDS', '흐림', '흐린 날씨'),
('SNOW', '눈', '눈 오는 날씨'),
('NIGHT', '밤/새벽', '밤 또는 새벽 분위기')
ON DUPLICATE KEY UPDATE
weather_name = VALUES(weather_name),
description = VALUES(description);
SELECT * FROM weather_types;


SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;
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


INSERT INTO route_types (route_id, route_name, description)
VALUES
(1, '감성 루트', '여운, 외로움, 잔잔함, 감정적인 분위기의 루트'),
(2, '현실도피 루트', '현실에서 벗어나 판타지, 모험, 다른 세계에 몰입하고 싶은 루트'),
(3, '몰입 루트', '깊은 서사, 미스터리, 스릴러처럼 작품에 강하게 빠져드는 루트'),
(4, '기분전환 루트', '가볍게 웃고 싶거나 밝은 분위기를 하고 싶은 루트')
ON DUPLICATE KEY UPDATE
route_name = VALUES(route_name),
description = VALUES(description);

INSERT INTO endings (ending_id, ending_name, description, condition_text, route_id, weather_id)
VALUES
(1, '비 오는 날 감성 엔딩', 
 '비 오는 날의 차분한 분위기와 감성적인 선택이 만나 완성된 엔딩입니다.',
 '감성 루트 + 비 오는 날일 때', 
 1, 2),

(2, '새벽 감성 엔딩',
 '조용한 밤이나 새벽의 분위기 속에서 감정이 깊어지는 엔딩입니다.',
 '감성 루트 + 밤/새벽 분위기일 때',
 1, 5),

(3, '현실도피 엔딩',
 '복잡한 현실에서 잠시 벗어나 다른 세계와 이야기에 빠져드는 엔딩입니다.',
 '현실도피 루트 점수가 가장 높을 때',
 2, NULL),

(4, '깊은 몰입 엔딩',
 '작품의 서사와 분위기에 깊게 빠져들고 싶은 상태를 나타내는 엔딩입니다.',
 '몰입 루트 점수가 가장 높을 때',
 3, NULL),

(5, '기분전환 엔딩',
 '무거운 기분을 내려놓고 가볍게 웃거나 밝은 분위기로 전환하는 엔딩입니다.',
 '기분전환 루트 점수가 가장 높을 때',
 4, NULL),

(6, '흐린 날의 여운 엔딩',
 '흐린 날씨와 잔잔한 감정이 어우러져 조용한 여운을 남기는 엔딩입니다.',
 '감성 루트 + 흐린 날일 때',
 1, 3),
 
 (7, '오늘도 즐거운 하루 엔딩',
 '좋은 기분을 그대로 이어가고 싶은 선택들이 모여 완성된 밝고 산뜻한 엔딩입니다.',
 '기분전환 루트 + 행복함 또는 설렘 감정이 강할 때',
 4, NULL),
(
  8,
  '잔잔한 감성 엔딩',
  '날씨와 상관없이 감성적인 선택들이 모여 조용한 여운을 남기는 엔딩입니다.',
  '감성 루트 기본 엔딩',
  1,
  NULL
)
ON DUPLICATE KEY UPDATE
ending_name = VALUES(ending_name),
description = VALUES(description),
condition_text = VALUES(condition_text),
route_id = VALUES(route_id),
weather_id = VALUES(weather_id);


INSERT INTO simulation_questions (question_id, question_text, question_order, is_active)
VALUES
(1, '오늘 날씨를 보면 어떤 기분이 들어?', 1, 1),
(2, '지금 보고 싶은 작품 분위기는?', 2, 1),
(3, '오늘 하루는 어땠어?', 3, 1),
(4, '결말은 어떤 느낌이었으면 좋겠어?', 4, 1)
ON DUPLICATE KEY UPDATE
question_text = VALUES(question_text),
question_order = VALUES(question_order),
is_active = VALUES(is_active);

INSERT INTO simulation_choices 
(choice_id, choice_text, choice_order, route_score, emotion_name, emotion_score, question_id, route_id)
VALUES

-- Q1. 오늘 날씨를 보면 어떤 기분이 들어?
(1, '괜히 감성적이야 감성을 타고싶어', 1, 3, '외로움', 2, 1, 1),
(2, '어디론가 사라지고 싶어 현실이 쉽지않아', 2, 3, '현실도피', 2, 1, 2),
(3, '집중이 잘되고 힘이 나', 3, 3, '몰입', 2, 1, 3),
(4, '웃음이 나오는 좋은 하루였어', 4, 3, '기분전환', 2, 1, 4),

-- Q2. 지금 보고 싶은 작품 분위기는?
(5, '잔잔하고 여운 남는 분위기', 1, 3, '감성', 3, 2, 1),
(6, '현실과 다른 분위기', 2, 3, '현실도피', 3, 2, 2),
(7, '처음부터 끝까지 집중하게 되는 분위기', 3, 3, '몰입', 3, 2, 3),
(8, '가볍고 편하게 볼 수 있는 분위기', 4, 3, '기분전환', 3, 2, 4),

-- Q3. 오늘 하루는 어땠어?
(9, '조금 외롭고 생각이 많았어', 1, 3, '외로움', 3, 3, 1),
(10, '현실이 좀 답답했어', 2, 3, '답답함', 3, 3, 2),
(11, '무언가에 집중하는 힘이 넘쳐', 3, 3, '몰입', 3, 3, 3),
(12, '하루가 꽤 괜찮아서 좋은 기분을 이어가고싶어', 4, 3, '즐거움', 3, 3, 4),

-- Q4. 결말은 어떤 느낌이었으면 좋겠어?
(13, '조용한 여운이 남았으면 좋겠어', 1, 3, '여운', 3, 4, 1),
(14, '현실과 다른 세계였으면 좋겠어', 2, 3, '현실도피', 3, 4, 2),
(15, '생각할 거리가 오래 남았으면 좋겠어', 3, 3, '몰입', 3, 4, 3),
(16, '보고 나서 더 행복하고 웃으면 좋겠다', 4, 3, '기분전환', 3, 4, 4)

ON DUPLICATE KEY UPDATE
choice_text = VALUES(choice_text),
choice_order = VALUES(choice_order),
route_score = VALUES(route_score),
emotion_name = VALUES(emotion_name),
emotion_score = VALUES(emotion_score),
question_id = VALUES(question_id),
route_id = VALUES(route_id);

SELECT * FROM users;
SELECT * FROM weather_types;
SELECT * FROM route_types;
SELECT * FROM endings;
SELECT * FROM simulation_questions;
SELECT * FROM simulation_choices;
SELECT * FROM contents;
SELECT * FROM diaries;
SELECT * FROM simulation_results;


SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;
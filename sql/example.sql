--
--    Shinsoo: Java-Quarkus Back End for Aria
--    Copyright (C) 2020  Brenterino <brent@zygon.dev>
--
--    This program is free software: you can redistribute it and/or modify
--    it under the terms of the GNU General Public License as published by
--    the Free Software Foundation, either version 3 of the License, or
--    (at your option) any later version.
--
--    This program is distributed in the hope that it will be useful,
--    but WITHOUT ANY WARRANTY; without even the implied warranty of
--    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
--    GNU General Public License for more details.
--
--    You should have received a copy of the GNU General Public License
--    along with this program.  If not, see <https://www.gnu.org/licenses/>.
--

USE shinsoo;

CREATE TABLE `guild` (
	`id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
	`name` VARCHAR(50) NULL DEFAULT '',
	PRIMARY KEY (`id`),
	UNIQUE INDEX `name` (`name`)
)
COLLATE='latin1_swedish_ci'
ENGINE=InnoDB
AUTO_INCREMENT=1
;

CREATE TABLE `player` (
	`id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
	`name` VARCHAR(50) NOT NULL DEFAULT '0',
	`level` INT(11) NOT NULL DEFAULT '1',
	`exp` BIGINT(20) NOT NULL DEFAULT '0',
	`fame` BIGINT(20) NOT NULL DEFAULT '0',
	`job` BIGINT(20) NOT NULL DEFAULT '0',
	`guild_id` BIGINT(20) UNSIGNED NULL DEFAULT NULL,
	`rank` BIGINT(20) UNSIGNED NOT NULL DEFAULT '0',
	PRIMARY KEY (`id`) USING BTREE,
	UNIQUE INDEX `name` (`name`) USING BTREE,
	INDEX `FK_player_guild` (`guild_id`) USING BTREE,
	INDEX `rank` (`rank`) USING BTREE,
	INDEX `job` (`job`) USING BTREE,
	INDEX `fame` (`fame`) USING BTREE,
	CONSTRAINT `guild_id` FOREIGN KEY (`guild_id`) REFERENCES `guild` (`id`) ON UPDATE NO ACTION
)
COLLATE='latin1_swedish_ci'
ENGINE=InnoDB
AUTO_INCREMENT=1
;

CREATE TABLE `post` (
	`id` INT(10) UNSIGNED NOT NULL AUTO_INCREMENT,
	`type` VARCHAR(50) NOT NULL,
	`views` BIGINT(20) NOT NULL DEFAULT '0',
	`title` VARCHAR(100) NOT NULL,
	`author` VARCHAR(50) NOT NULL,
	`created` TIMESTAMP NOT NULL DEFAULT '0000-00-00 00:00:00' ON UPDATE CURRENT_TIMESTAMP,
	`updated` TIMESTAMP NOT NULL DEFAULT '0000-00-00 00:00:00',
	`content` VARCHAR(30000) NOT NULL,
	PRIMARY KEY (`id`) USING BTREE
)
COLLATE='latin1_swedish_ci'
ENGINE=InnoDB
AUTO_INCREMENT=1
;

CREATE TABLE `server` (
	`id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
	`name` VARCHAR(50) NOT NULL,
	`ip` VARCHAR(50) NOT NULL DEFAULT '',
	`port` INT(11) NOT NULL,
	PRIMARY KEY (`id`) USING BTREE
)
COLLATE='latin1_swedish_ci'
ENGINE=InnoDB
AUTO_INCREMENT=1
;

CREATE TABLE `session` (
	`id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
	`nonce` VARCHAR(200) NOT NULL DEFAULT '',
	`expiration` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
	`user_name` VARCHAR(50) NOT NULL,
	`user_maple_id` VARCHAR(50) NOT NULL,
	`user_gm_level` INT(10) UNSIGNED NOT NULL DEFAULT '0',
	PRIMARY KEY (`id`) USING BTREE,
	UNIQUE INDEX `nonce` (`nonce`) USING BTREE
)
COLLATE='latin1_swedish_ci'
ENGINE=InnoDB
AUTO_INCREMENT=1
;

CREATE TABLE `user` (
	`id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
	`name` VARCHAR(50) NOT NULL,
	`maple_id` VARCHAR(50) NOT NULL,
	`password` VARCHAR(300) NOT NULL,
	`email` VARCHAR(300) NOT NULL,
	`gm_level` TINYINT(1) UNSIGNED NOT NULL DEFAULT '0',
	`status` TINYINT(1) UNSIGNED NOT NULL DEFAULT '0',
	`votes` INT(10) UNSIGNED NOT NULL DEFAULT '0',
	`lastVoted` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
	PRIMARY KEY (`id`) USING BTREE,
	UNIQUE INDEX `name` (`name`) USING BTREE,
	UNIQUE INDEX `maple_id` (`maple_id`) USING BTREE
)
COLLATE='latin1_swedish_ci'
ENGINE=InnoDB
AUTO_INCREMENT=1
;

DROP TABLE IF EXISTS `users`;
CREATE TABLE `users` (
  `username` varchar(255) NOT NULL,
  `password` varchar(255) NOT NULL,
  `enabled` boolean NOT NULL DEFAULT true,
  PRIMARY KEY (`username`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS `authorities`;
CREATE TABLE authorities (
    `username` varchar(255) NOT NULL,
    `authority` varchar(255) NOT NULL,
    KEY `fk_authorities_users` (`username`),
    CONSTRAINT `fk_authorities_users` FOREIGN KEY (`username`) REFERENCES `users` (`username`),
    UNIQUE KEY `uk_username_authority` (`username`, `authority`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS `groups`;
create table groups (
    `id` int(11) NOT NULL AUTO_INCREMENT,
    `group_name` varchar(255) NOT NULL,
    PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=20 DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS `group_authorities`;
create table group_authorities (
    `group_id` int(11) NOT NULL,
    `authority` varchar(255) NOT NULL,
    KEY `fk_group_authorities_group` (`group_id`),
    CONSTRAINT `fk_group_authorities_group` FOREIGN KEY (`group_id`) REFERENCES `groups` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS `group_members`;
create table group_members (
    `id` int(11) NOT NULL AUTO_INCREMENT,
    `username` varchar(255) NOT NULL,
    `group_id` int(11) NOT NULL,
    PRIMARY KEY (`id`),
    KEY `fk_group_members_group` (`group_id`),
    CONSTRAINT `fk_group_members_group` FOREIGN KEY (`group_id`) REFERENCES `groups` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=20 DEFAULT CHARSET=utf8;
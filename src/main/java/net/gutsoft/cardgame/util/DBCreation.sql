CREATE SCHEMA `cardgame_db` DEFAULT CHARACTER SET utf8 COLLATE utf8_general_ci;

USE cardgame_db;

CREATE TABLE `Account` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `email` varchar(255) DEFAULT NULL,
  `experience` int(11) DEFAULT NULL,
  `health` int(11) DEFAULT NULL,
  `level` int(11) DEFAULT NULL,
  `login` varchar(255) DEFAULT NULL,
  `money` int(11) DEFAULT NULL,
  `password` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8;

CREATE TABLE `Card` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `defence` int(11) DEFAULT NULL,
  `description` varchar(255) DEFAULT NULL,
  `maxHealth` int(11) DEFAULT NULL,
  `multiusable` bit(1) DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  `power` int(11) DEFAULT NULL,
  `price` int(11) DEFAULT NULL,
  `rank` int(11) DEFAULT NULL,
  `targetClass` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8;

CREATE TABLE `Deck` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `active` bit(1) DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  `account_id` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK6cvql2cvdbcsdnwn7jo2wvo2i` (`account_id`),
  CONSTRAINT `FK6cvql2cvdbcsdnwn7jo2wvo2i` FOREIGN KEY (`account_id`) REFERENCES `Account` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8;

CREATE TABLE `AccountCard` (
  `account_id` int(11) NOT NULL,
  `card_id` int(11) NOT NULL,
  KEY `FKatitxahsgyqg8n6rcv86bvaqk` (`card_id`),
  KEY `FKgfy1jal2h6ppnqjd42yyppkg1` (`account_id`),
  CONSTRAINT `FKatitxahsgyqg8n6rcv86bvaqk` FOREIGN KEY (`card_id`) REFERENCES `Card` (`id`),
  CONSTRAINT `FKgfy1jal2h6ppnqjd42yyppkg1` FOREIGN KEY (`account_id`) REFERENCES `Account` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `DeckCard` (
  `deck_id` int(11) NOT NULL,
  `card_id` int(11) NOT NULL,
  KEY `FKl4witnp1s6u4x0s6f105fw2kr` (`card_id`),
  KEY `FKjrg0evt8smeusw1jxqfkf82hn` (`deck_id`),
  CONSTRAINT `FKjrg0evt8smeusw1jxqfkf82hn` FOREIGN KEY (`deck_id`) REFERENCES `Deck` (`id`),
  CONSTRAINT `FKl4witnp1s6u4x0s6f105fw2kr` FOREIGN KEY (`card_id`) REFERENCES `Card` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

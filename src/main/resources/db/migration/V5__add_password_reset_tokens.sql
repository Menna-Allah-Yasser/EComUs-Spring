CREATE TABLE `password_reset_token` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `token` varchar(255) NOT NULL,
  `user_id` int NOT NULL,
  `expiry_date` datetime NOT NULL,
  `used` boolean DEFAULT false,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK_token` (`token`),
  FOREIGN KEY (`user_id`) REFERENCES `user` (`userId`)
) ENGINE=InnoDB;
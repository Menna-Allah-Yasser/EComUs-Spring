-- Drop the old table if it exists
DROP TABLE IF EXISTS `password_reset_token`;

-- Create the table with correct camelCase naming
CREATE TABLE `passwordResetToken` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `token` varchar(255) NOT NULL,
  `userId` int NOT NULL,
  `expiryDate` datetime NOT NULL,
  `used` boolean DEFAULT false,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK_token` (`token`),
  FOREIGN KEY (`userId`) REFERENCES `user` (`userId`)
) ENGINE=InnoDB;
CREATE TABLE `address` (
                           `addressId` integer NOT NULL AUTO_INCREMENT,
                           `area` varchar(255),
                           `buildingNo` varchar(255),
                           `city` varchar(255),
                           `street` varchar(255),
                           `userId` integer,
                           PRIMARY KEY (`addressId`)
) ENGINE=InnoDB;

CREATE TABLE `cart` (
                        `productId` integer NOT NULL,
                        `userId` integer NOT NULL,
                        `quantity` integer,
                        PRIMARY KEY (`productId`, `userId`)
) ENGINE=InnoDB;

CREATE TABLE `category` (
                            `categoryId` integer NOT NULL AUTO_INCREMENT,
                            `categoryName` varchar(255) NOT NULL,
                            PRIMARY KEY (`categoryId`)
) ENGINE=InnoDB;

CREATE TABLE `orderdetails` (
                                `orderDetailsId` integer NOT NULL AUTO_INCREMENT,
                                `price` integer NOT NULL,
                                `quantity` integer NOT NULL,
                                `orderId` integer NOT NULL,
                                `productId` integer NOT NULL,
                                PRIMARY KEY (`orderDetailsId`)
) ENGINE=InnoDB;

CREATE TABLE `orders` (
                          `orderId` integer NOT NULL AUTO_INCREMENT,
                          `address` varchar(255) NOT NULL,
                          `date` datetime(6) NOT NULL,
                          `payType` enum('CASH','CREDIT') NOT NULL,
                          `price` integer NOT NULL,
                          `status` enum('CANCELED','SHIPPED','COMPLETED','PROCESSING') NOT NULL,
                          `userId` integer NOT NULL,
                          PRIMARY KEY (`orderId`)
) ENGINE=InnoDB;

CREATE TABLE `product` (
                           `productId` integer NOT NULL AUTO_INCREMENT,
                           `description` varchar(255) NOT NULL,
                           `price` integer NOT NULL,
                           `productName` varchar(255) NOT NULL,
                           `quantity` integer NOT NULL,
                           PRIMARY KEY (`productId`)
) ENGINE=InnoDB;

CREATE TABLE `productcategory` (
                                   `categoryId` integer NOT NULL,
                                   `productId` integer NOT NULL,
                                   PRIMARY KEY (`categoryId`, `productId`)
) ENGINE=InnoDB;

CREATE TABLE `user` (
                        `userId` integer NOT NULL AUTO_INCREMENT,
                        `birthdate` date,
                        `creditLimit` integer,
                        `creditNo` varchar(255),
                        `email` varchar(255) NOT NULL,
                        `job` varchar(255),
                        `password` varchar(255) NOT NULL,
                        `phone` varchar(255),
                        `userName` varchar(255) NOT NULL,
                        PRIMARY KEY (`userId`)
) ENGINE=InnoDB;

CREATE TABLE `wishlist` (
                            `productId` integer NOT NULL,
                            `userId` integer NOT NULL,
                            PRIMARY KEY (`productId`, `userId`)
) ENGINE=InnoDB;

-- Add unique constraints
ALTER TABLE `category` ADD CONSTRAINT `UKlroeo5fvfdeg4hpicn4lw7x9b` UNIQUE (`categoryName`);
ALTER TABLE `user` ADD CONSTRAINT `UKob8kqyqqgmefl0aco34akdtpe` UNIQUE (`email`);

-- Add foreign key constraints
ALTER TABLE `address` ADD CONSTRAINT `FKda8tuywtf0gb6sedwk7la1pgi` FOREIGN KEY (`userId`) REFERENCES `user` (`userId`);
ALTER TABLE `cart` ADD CONSTRAINT `FK3d704slv66tw6x5hmbm6p2x3u` FOREIGN KEY (`productId`) REFERENCES `product` (`productId`);
ALTER TABLE `cart` ADD CONSTRAINT `FKl70asp4l4w0jmbm1tqyofho4o` FOREIGN KEY (`userId`) REFERENCES `user` (`userId`);
ALTER TABLE `orderdetails` ADD CONSTRAINT `FKhnsosbuy7bhpqpnt3bjr7sh8x` FOREIGN KEY (`orderId`) REFERENCES `orders` (`orderId`);
ALTER TABLE `orderdetails` ADD CONSTRAINT `FKdhs1mfl2idhy7idq8i2e3ftgb` FOREIGN KEY (`productId`) REFERENCES `product` (`productId`);
ALTER TABLE `orders` ADD CONSTRAINT `FKel9kyl84ego2otj2accfd8mr7` FOREIGN KEY (`userId`) REFERENCES `user` (`userId`);
ALTER TABLE `productcategory` ADD CONSTRAINT `FKm6cnialm1ucd3apn25ujub7mf` FOREIGN KEY (`categoryId`) REFERENCES `category` (`categoryId`);
ALTER TABLE `productcategory` ADD CONSTRAINT `FK2q9saqrv03gydcy5nq1715w23` FOREIGN KEY (`productId`) REFERENCES `product` (`productId`);
ALTER TABLE `wishlist` ADD CONSTRAINT `FKqchevbfw5wq0f4uqacns02rp7` FOREIGN KEY (`productId`) REFERENCES `product` (`productId`);
ALTER TABLE `wishlist` ADD CONSTRAINT `FKd4r80jm8s41fgoa0xv9yy5lo8` FOREIGN KEY (`userId`) REFERENCES `user` (`userId`);
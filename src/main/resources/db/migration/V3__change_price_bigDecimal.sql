ALTER TABLE `orderdetails`
    MODIFY COLUMN `price` DECIMAL(10,2) NOT NULL;

-- Change price column in orders table
ALTER TABLE `orders`
    MODIFY COLUMN `price` DECIMAL(10,2) NOT NULL;

-- Change price column in product table
ALTER TABLE `product`
    MODIFY COLUMN `price` DECIMAL(10,2) NOT NULL;

ALTER TABLE `user`
    MODIFY COLUMN `creditLimit` DECIMAL(10,2);

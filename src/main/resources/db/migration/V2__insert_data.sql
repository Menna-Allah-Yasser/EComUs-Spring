-- Insert Users
INSERT INTO `user` (`userName`, `email`, `password`, `birthdate`, `job`, `creditLimit`, `creditNo`, `phone`)
VALUES ('John Smith', 'john.smith@email.com', 'hashed_password_123', '1990-05-15', 'Software Engineer', 5000,
        '1234567890123456', '+1-555-0101'),
       ('Sarah Johnson', 'sarah.johnson@email.com', 'hashed_password_456', '1985-12-22', 'Marketing Manager', 3000,
        '2345678901234567', '+1-555-0102'),
       ('Mike Davis', 'mike.davis@email.com', 'hashed_password_789', '1992-08-10', 'Designer', 2500, '3456789012345678',
        '+1-555-0103'),
       ('Emily Wilson', 'emily.wilson@email.com', 'hashed_password_012', '1988-03-07', 'Teacher', 2000,
        '4567890123456789', '+1-555-0104'),
       ('David Brown', 'david.brown@email.com', 'hashed_password_345', '1995-11-30', 'Student', 1500,
        '5678901234567890', '+1-555-0105'),
       ('Lisa Garcia', 'lisa.garcia@email.com', 'hashed_password_678', '1983-07-18', 'Doctor', 8000, '6789012345678901',
        '+1-555-0106'),
       ('Tom Anderson', 'tom.anderson@email.com', 'hashed_password_901', '1991-09-25', 'Lawyer', 6000,
        '7890123456789012', '+1-555-0107'),
       ('Anna Miller', 'anna.miller@email.com', 'hashed_password_234', '1987-04-12', 'Nurse', 3500, '8901234567890123',
        '+1-555-0108');

-- Insert Addresses
INSERT INTO `address` (`area`, `buildingNo`, `city`, `street`, `userId`)
VALUES ('Downtown', '123A', 'New York', 'Main Street', 1),
       ('Uptown', '456B', 'Los Angeles', 'Oak Avenue', 2),
       ('Midtown', '789C', 'Chicago', 'Pine Road', 3),
       ('Westside', '101D', 'Houston', 'Maple Drive', 4),
       ('Eastside', '202E', 'Phoenix', 'Cedar Lane', 5),
       ('Northside', '303F', 'Philadelphia', 'Elm Street', 6),
       ('Southside', '404G', 'San Antonio', 'Birch Way', 7),
       ('Central', '505H', 'San Diego', 'Willow Court', 8),
       ('Downtown', '606I', 'New York', 'Second Street', 1),
       ('Uptown', '707J', 'Los Angeles', 'Third Avenue', 2);

-- Insert Categories
INSERT INTO `category` (`categoryName`)
VALUES ('Electronics'),
       ('Clothing'),
       ('Books'),
       ('Home & Garden'),
       ('Sports & Outdoors'),
       ('Beauty & Health'),
       ('Toys & Games'),
       ('Automotive'),
       ('Food & Beverages'),
       ('Music & Movies');

-- Insert Products
INSERT INTO `product` (`productName`, `description`, `quantity`, `price`)
VALUES ('iPhone 14 Pro', 'Latest Apple smartphone with advanced features', 50, 999),
       ('Samsung Galaxy S23', 'High-end Android smartphone', 45, 849),
       ('MacBook Air M2', 'Lightweight laptop with Apple M2 chip', 30, 1299),
       ('Dell XPS 13', 'Premium Windows laptop', 25, 1099),
       ('Sony WH-1000XM4', 'Noise-canceling wireless headphones', 75, 299),
       ('Nike Air Max 270', 'Comfortable running shoes', 100, 149),
       ('Adidas Ultraboost 22', 'High-performance athletic shoes', 80, 179),
       ('Levi s 501 Jeans', 'Classic straight-fit denim jeans', 120, 59),
('The Great Gatsby', 'Classic American literature novel', 200, 12),
('Harry Potter Set', 'Complete 7-book series', 50, 89),
('KitchenAid Stand Mixer', 'Professional-grade kitchen mixer', 35, 379),
('Dyson V15 Vacuum', 'Cordless stick vacuum cleaner', 40, 649),
('Fitbit Charge 5', 'Advanced fitness tracker', 90, 179),
('Nintendo Switch', 'Portable gaming console', 60, 299),
('PlayStation 5', 'Latest Sony gaming console', 25, 499),
('Xbox Series X', 'Microsoft gaming console', 30, 499),
('Canon EOS R6', 'Mirrorless digital camera', 20, 2499),
('GoPro Hero 11', 'Action camera for adventures', 65, 399),
('Instant Pot Duo', '7-in-1 electric pressure cooker', 55, 89),
('Bluetooth Speaker', 'Portable wireless speaker', 85, 79);

-- Insert Product Categories (Many-to-Many relationship)
INSERT INTO `productcategory` (`productId`, `categoryId`) VALUES
(1, 1), (2, 1), (3, 1), (4, 1), (5, 1), -- Electronics
(6, 2), (7, 2), (8, 2), -- Clothing & Sports
(6, 5), (7, 5), -- Sports & Outdoors
(9, 3), (10, 3), -- Books
(11, 4), (12, 4), (19, 4), -- Home & Garden
(13, 6), -- Beauty & Health
(14, 7), (15, 7), (16, 7), -- Toys & Games
(17, 1), (18, 1), (20, 1), -- More Electronics
(18, 5); -- Sports & Outdoors

-- Insert Orders
INSERT INTO `orders` (`address`, `date`, `payType`, `price`, `status`, `userId`) VALUES
('123A Main Street, Downtown, New York', '2024-12-01 10:30:00', 'CREDIT', 1148, 'COMPLETED', 1),
('456B Oak Avenue, Uptown, Los Angeles', '2024-12-02 14:15:00', 'CASH', 328, 'COMPLETED', 2),
('789C Pine Road, Midtown, Chicago ', '2024-12-03 09:45:00', 'CREDIT', 2498, 'PROCESSING', 3),
('101D Maple Drive, Westside, Houston ', '2024-12-04 16:20:00', 'CREDIT', 238, 'COMPLETED', 4),
('202E Cedar Lane, Eastside, Phoenix ', '2024-12-05 11:10:00', 'CASH', 798, 'PROCESSING', 5),
('303F Elm Street, Northside, Philadelphia ', '2024-12-06 13:30:00', 'CREDIT', 89, 'CANCELED', 6),
('404G Birch Way, Southside, San Antonio', '2024-12-07 15:45:00', 'CREDIT', 499, 'COMPLETED', 7),
('505H Willow Court, Central, San Diego', '2024-12-08 12:00:00', 'CASH', 468, 'PROCESSING', 8);

-- Insert Order Details
INSERT INTO `orderdetails` (`orderId`, `productId`, `quantity`, `price`) VALUES
-- Order 1: iPhone 14 Pro + Nike Shoes
(1, 1, 1, 999),
(1, 6, 1, 149),
-- Order 2: Sony Headphones + Bluetooth Speaker
(2, 5, 1, 299),
(2, 20, 1, 79),
-- Order 3: Canon Camera (single expensive item)
(3, 17, 1, 2499),
-- Order 4: Books + Fitbit
(4, 9, 2, 12),
(4, 10, 1, 89),
(4, 13, 1, 179),
-- Order 5: PlayStation 5 + Games
(5, 15, 1, 499),
(5, 14, 1, 299),
-- Order 6: Instant Pot (canceled order)
(6, 19, 1, 89),
-- Order 7: Xbox Series X
(7, 16, 1, 499),
-- Order 8: KitchenAid Mixer + Levi's Jeans
(8, 11, 1, 379),
        (8, 8, 1, 59);

-- Insert Cart Items (current shopping carts)
INSERT INTO `cart` (`userId`, `productId`, `quantity`)
VALUES (1, 12, 1), -- Dyson Vacuum
       (1, 19, 2), -- Instant Pot
       (2, 3, 1),  -- MacBook Air
       (3, 6, 2),  -- Nike Shoes
       (3, 7, 1),  -- Adidas Shoes
       (4, 10, 1), -- Harry Potter Set
       (5, 20, 3), -- Bluetooth Speakers
       (6, 4, 1),  -- Dell Laptop
       (7, 18, 1), -- GoPro
       (8, 13, 2);
-- Fitbit

-- Insert Wishlist Items
INSERT INTO `wishlist` (`userId`, `productId`)
VALUES (1, 15), -- PlayStation 5
       (1, 17), -- Canon Camera
       (2, 11), -- KitchenAid Mixer
       (2, 12), -- Dyson Vacuum
       (3, 1),  -- iPhone 14 Pro
       (3, 5),  -- Sony Headphones
       (4, 14), -- Nintendo Switch
       (4, 16), -- Xbox Series X
       (5, 2),  -- Samsung Galaxy
       (5, 4),  -- Dell Laptop
       (6, 18), -- GoPro
       (7, 3),  -- MacBook Air
       (7, 13), -- Fitbit
       (8, 6),  -- Nike Shoes
       (8, 8); -- Levi's Jeans
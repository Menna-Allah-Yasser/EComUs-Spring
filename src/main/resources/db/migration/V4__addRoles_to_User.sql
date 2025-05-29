ALTER TABLE `user`
    ADD COLUMN `role` enum('USER','ADMIN') NOT NULL DEFAULT 'USER';

# insert roles into user table

UPDATE `user`
SET `role` = 'USER';

-- Set default role for existing users to 'USER'
UPDATE `user`
SET `role` = 'ADMIN'
WHERE `userID` = 1;
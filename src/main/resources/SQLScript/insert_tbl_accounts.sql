INSERT INTO `tbl_accounts` (`username`, `email`, `hashed_password`, `role`, `provider`, `enabled`, `created_date`,
                            `last_modified_date`)
VALUES ('student', 'student@seonbistudy.com', '$2a$10$rXW0fI.O0kw5x..WKBCqb.tKgVM691ate4S50HnTlOZnQ5I8LTU8y', 'STUDENT',
        'LOCAL', 1, NOW(), NOW()),
       ('admin', 'admin@seonbistudy.com', '$2a$10$rXW0fI.O0kw5x..WKBCqb.tKgVM691ate4S50HnTlOZnQ5I8LTU8y', 'ADMIN',
        'LOCAL', 1, NOW(), NOW());
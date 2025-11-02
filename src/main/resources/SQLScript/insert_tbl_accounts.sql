-- SeonbiStudy Data Initialization Script (cleaned)
-- Lưu ý: chỉnh `USE your_database;` thành DB của bạn nếu cần.

USE your_database;  -- <- thay bằng tên database thực tế

-- Thời gian hiện tại
SET @now = NOW();

-- -------------------------
-- 1) TẠO TÀI KHOẢN ADMIN
-- -------------------------
INSERT IGNORE INTO `tbl_accounts` (
    `username`, `email`, `hashed_password`,
    `role`, `provider`, `enabled`,
    `created_date`, `last_modified_date`
) VALUES (
    'admin',
    'admin@seonbistudy.com',
    '$2a$10$rXW0fI.O0kw5x..WKBCqb.tKgVM691ate4S50HnTlOZnQ5I8LTU8y',
    'ADMIN',
    'LOCAL',
    1,
    @now,
    @now
);

-- Chèn profile cho admin nếu chưa tồn tại
INSERT INTO `tbl_users` (`account_id`, `full_name`, `created_date`, `last_modified_date`)
SELECT a.id, 'System Administrator', @now, @now
FROM `tbl_accounts` a
WHERE a.username = 'admin'
  AND NOT EXISTS (
    SELECT 1 FROM `tbl_users` u WHERE u.account_id = a.id
);

-- -------------------------
-- 2) TẠO TÀI KHOẢN STUDENT
-- -------------------------
INSERT IGNORE INTO `tbl_accounts` (
    `username`, `email`, `hashed_password`,
    `role`, `provider`, `enabled`,
    `created_date`, `last_modified_date`
) VALUES (
    'student',
    'student@seonbistudy.com',
    '$2a$10$rXW0fI.O0kw5x..WKBCqb.tKgVM691ate4S50HnTlOZnQ5I8LTU8y',
    'STUDENT',
    'LOCAL',
    1,
    @now,
    @now
);

-- Chèn profile cho student nếu chưa tồn tại
INSERT INTO `tbl_users` (`account_id`, `full_name`, `created_date`, `last_modified_date`)
SELECT a.id, 'Demo Student', @now, @now
FROM `tbl_accounts` a
WHERE a.username = 'student'
  AND NOT EXISTS (
    SELECT 1 FROM `tbl_users` u WHERE u.account_id = a.id
);

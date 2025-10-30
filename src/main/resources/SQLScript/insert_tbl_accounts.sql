-- -----------------------------------------------------------------
-- SeonbiStudy Data Initialization Script
-- Tương đương với DataInitializer.java
-- -----------------------------------------------------------------
--
-- !!
-- !! CẢNH BÁO QUAN TRỌNG VỀ MẬT KHẨU
-- !!
-- Không giống như code Java, SQL không thể tự "encode" mật khẩu.
-- Bạn PHẢI thay thế các placeholder bên dưới bằng mật khẩu đã được
-- băm bằng BCrypt (giống như Spring Security).
--
-- VÍ DỤ:
-- Mật khẩu 'admin123' sau khi băm sẽ giống như:
-- '$2a$10$8.Usf4.0S.1kU.f1/dE.5e8WtyPUPs5F1.92gP.Y.Z8.b3L.1C5/K'
--
-- Vui lòng tạo các mã băm này trong môi trường của bạn và
-- dán chúng vào các vị trí <..._REPLACE_ME_...>
-- -----------------------------------------------------------------

-- Đặt múi giờ cho phiên này (nếu cần) và lấy thời gian hiện tại
-- SET time_zone = '+07:00';
SET @now = NOW();

-- -----------------------------------------------------------------
-- 1. Khởi tạo tài khoản ADMIN
-- -----------------------------------------------------------------
-- Sử dụng INSERT IGNORE để bắt chước logic 'if (!exists...)'
-- Điều này yêu cầu 'username' và 'email' phải là các cột UNIQUE
INSERT IGNORE INTO accounts (
    username, email, hashed_password,
    role, provider, enabled,
    created_date, last_modified_date
) VALUES (
    'admin',
    'admin@seonbistudy.com',
    '<BCRYPT_HASH_FOR_admin123_REPLACE_ME>', -- !! THAY THẾ TẠI ĐÂY !!
    'ADMIN',
    'LOCAL',
    true,
    @now,
    @now
);

-- Lấy ID của tài khoản (dù là mới chèn hay đã tồn tại)
SET @admin_account_id = (SELECT id FROM accounts WHERE username = 'admin');

-- Tạo 'user' profile nếu nó chưa tồn tại cho tài khoản này
IF @admin_account_id IS NOT NULL THEN
    INSERT IGNORE INTO users (
        account_id, full_name, created_date, last_modified_date
    ) VALUES (
        @admin_account_id,
        'System Administrator',
        @now,
        @now
    );
END IF;


-- -----------------------------------------------------------------
-- 2. Khởi tạo tài khoản INSTRUCTOR
-- -----------------------------------------------------------------
INSERT IGNORE INTO accounts (
    username, email, hashed_password,
    role, provider, enabled,
    created_date, last_modified_date
) VALUES (
    'instructor',
    'instructor@seonbistudy.com',
    '<BCRYPT_HASH_FOR_instructor123_REPLACE_ME>', -- !! THAY THẾ TẠI ĐÂY !!
    'INSTRUCTOR',
    'LOCAL',
    true,
    @now,
    @now
);

-- Lấy ID
SET @instructor_account_id = (SELECT id FROM accounts WHERE username = 'instructor');

-- Tạo 'user' profile
IF @instructor_account_id IS NOT NULL THEN
    INSERT IGNORE INTO users (
        account_id, full_name, created_date, last_modified_date
    ) VALUES (
        @instructor_account_id,
        'Demo Instructor',
        @now,
        @now
    );
END IF;


-- -----------------------------------------------------------------
-- 3. Khởi tạo tài khoản STUDENT
-- -----------------------------------------------------------------
INSERT IGNORE INTO accounts (
    username, email, hashed_password,
    role, provider, enabled,
    created_date, last_modified_date
) VALUES (
    'student',
    'student@seonbistudy.com',
    '<BCRYPT_HASH_FOR_student123_REPLACE_ME>', -- !! THAY THẾ TẠI ĐÂY !!
    'STUDENT',
    'LOCAL',
    true,
    @now,
    @now
);

-- Lấy ID
SET @student_account_id = (SELECT id FROM accounts WHERE username = 'student');

-- Tạo 'user' profile
IF @student_account_id IS NOT NULL THEN
    INSERT IGNORE INTO users (
        account_id, full_name, created_date, last_modified_date
    ) VALUES (
        @student_account_id,
        'Demo Student',
        @now,
        @now
    );
END IF;
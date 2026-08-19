DROP DATABASE IF EXISTS `husc_refind`;
CREATE DATABASE `husc_refind` CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE `husc_refind`;

CREATE TABLE `users` (
    `id` INT AUTO_INCREMENT PRIMARY KEY,
    `code` VARCHAR(20) UNIQUE NOT NULL COMMENT 'Mã người dùng',
    `name` VARCHAR(100) NOT NULL COMMENT 'Họ và tên',
    `email` VARCHAR(100) NULL UNIQUE COMMENT '[mssv]@husc.edu.vn',
    `phone` VARCHAR(15) NULL COMMENT 'SĐT',
    `password` VARCHAR(255) NOT NULL COMMENT 'Mật khẩu (mã hoá)',
    `role` VARCHAR(20) NOT NULL DEFAULT 'student' COMMENT 'student/admin',
    `status` TINYINT NOT NULL DEFAULT 1 COMMENT '1: Hoạt động, 0: Bị khóa',
    `created_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE `categories` (
    `id` INT AUTO_INCREMENT PRIMARY KEY,
    `name` VARCHAR(100) NOT NULL UNIQUE COMMENT 'Tên danh mục'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE `locations` (
    `id` INT AUTO_INCREMENT PRIMARY KEY,
    `name` VARCHAR(100) NOT NULL UNIQUE COMMENT 'Tên địa điểm'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE `items` (
    `id` INT AUTO_INCREMENT PRIMARY KEY,
    `title` VARCHAR(255) NOT NULL COMMENT 'Tiêu đề bài đăng',
    `category_id` INT NOT NULL,
    `location_id` INT NOT NULL,
    `description` TEXT NULL COMMENT 'Mô tả chi tiết (đồ vật, thời gian, đặc điểm)',
    `image` VARCHAR(255) NOT NULL COMMENT 'Ảnh minh hoạ',
    `status` INT NOT NULL DEFAULT 1 COMMENT '1: Chờ xử lý, 2: Đang tiếp nhận, 0: Đã trả đồ',
    `admin_note` VARCHAR(255) NULL COMMENT 'Ghi chú / Vị trí tủ đồ',
    `user_id` INT NULL COMMENT 'Người đăng tin (admin -> NULL)',
    `created_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT `fk_items_category` FOREIGN KEY (`category_id`) REFERENCES `categories` (`id`) ON DELETE RESTRICT ON UPDATE CASCADE,
    CONSTRAINT `fk_items_location` FOREIGN KEY (`location_id`) REFERENCES `locations` (`id`) ON DELETE RESTRICT ON UPDATE CASCADE,
    CONSTRAINT `fk_items_user` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`) ON DELETE SET NULL ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE `saved_items` (
    `id` INT AUTO_INCREMENT PRIMARY KEY,
    `user_id` INT NOT NULL,
    `item_id` INT NOT NULL,
    `created_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    UNIQUE KEY `unique_user_item` (`user_id`, `item_id`),
    CONSTRAINT `fk_saved_user` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
    CONSTRAINT `fk_saved_item` FOREIGN KEY (`item_id`) REFERENCES `items` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- INSERT INTO `categories` (`id`, `name`) VALUES
-- (1, 'Thẻ, Giấy tờ'),
-- (2, 'Ví tiền, Bóp'),
-- (3, 'Thiết bị điện tử'),
-- (4, 'Chìa, Móc khóa'),
-- (5, 'Túi, Ba lô'),
-- (6, 'Đồ dùng học tập'),
-- (7, 'Phụ kiện cá nhân'),
-- (8, 'Đồ dùng khác');

-- INSERT INTO `locations` (`id`, `name`) VALUES
-- (1, 'Dãy nhà A'),
-- (2, 'Dãy nhà B'),
-- (3, 'Dãy nhà C'),
-- (4, 'Dãy nhà D'),
-- (5, 'Dãy nhà E'),
-- (6, 'Dãy nhà F'),
-- (7, 'Thư viện trường'),
-- (8, 'Căng tin trường'),
-- (9, 'Nhà xe sinh viên'),
-- (10, 'Khuôn viên trường'),
-- (11, 'Sân thể thao'),
-- (12, 'Hội trường lớn'),
-- (13, 'Khác');

-- INSERT INTO `users` (`id`, `mssv`, `name`, `email`, `phone`, `password`, `role`) VALUES
-- (1, 'ADMIN01', 'Quản trị viên / Bảo vệ HUSC', 'baove@husc.edu.vn', '02343823290', '123456', 'admin'),
-- (2, '21T1020001', 'Nguyễn Văn An', '21t1020001@husc.edu.vn', '0905123456', '123456', 'student'),
-- (3, '22T1020045', 'Lê Thị Mai', '22t1020045@husc.edu.vn', '0914987654', '123456', 'student'),
-- (4, '20T1080012', 'Trần Quốc Bảo', '20t1080012@husc.edu.vn', '0988776655', '123456', 'student');

-- INSERT INTO `items` (`id`, `title`, `category_id`, `location_id`, `description`, `image`, `type`, `status`, `admin_note`, `user_id`) VALUES
-- (1, 'Mất ví da màu nâu tại Nhà xe khu A', 2, 8, 'Ví da nam màu nâu, bên trong có CCCD mang tên Nguyễn Văn An, bằng lái xe máy và một ít tiền mặt. Rơi vào khoảng 10h sáng thứ Hai.', 'wallet.jpg', 'LOST', 1, NULL, 2),
-- (2, 'Nhặt được Thẻ sinh viên khoa CNTT', 1, 6, 'Nhặt được tại bàn số 4 phòng đọc tầng 2 Thư viện lúc 15h chiều qua. Thẻ tên Lê Thị Mai.', 'student_card.jpg', 'FOUND', 2, 'Tủ A1 - Phòng Bảo vệ cổng số 1 (77 Nguyễn Huệ)', 1),
-- (3, 'Nhặt được chùm chìa khóa xe Smartkey Honda', 4, 7, 'Có móc khóa hình gấu bông màu hồng, nhặt tại bàn ghế đá trước Căng tin trường.', 'keys.jpg', 'FOUND', 1, NULL, 3),
-- (4, 'Tìm máy tính cầm tay Casio FX-580VN X', 3, 4, 'Bỏ quên tại phòng D203 sau giờ học môn Giải tích lúc 11h30 trưa nay. Máy có dán sticker Pikachu.', 'casio.jpg', 'LOST', 1, NULL, 4),
-- (5, 'Đã trao trả: Balo đen chứa giáo trình Vật lý', 5, 2, 'Balo đã được trao trả lại cho sinh viên chính chủ tại phòng bảo vệ.', 'backpack.jpg', 'FOUND', 0, 'Đã hoàn tất trao trả ngày 15/08', 1);

-- INSERT INTO `saved_items` (`user_id`, `item_id`) VALUES
-- (2, 2),
-- (4, 3);

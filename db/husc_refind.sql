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

INSERT INTO locations (id, `name`) VALUES
(1, 'Phòng Bảo vệ'),
(2, 'Nhà A'),
(3, 'Nhà B'),
(4, 'Nhà C'),
(5, 'Nhà D'),
(6, 'Nhà E'),
(7, 'Nhà F'),
(8, 'Nhà K'),
(9, 'Thư viện'),
(10, 'Hội trường lớn'),
(11, 'Căng tin'),
(12, 'Nhà xe (Nguyễn Huệ)'),
(13, 'Nhà xe (Đống Đa)'),
(14, 'Sân thể thao'),
(15, 'Sân trường & Ghế đá'),
(16, 'Khu vực khác');

INSERT INTO categories (id, `name`) VALUES
(1, 'Thẻ & Giấy tờ'),
(2, 'Bóp & Ví tiền'),
(3, 'Đồ Điện tử'),
(4, 'Chìa & Móc khóa'),
(5, 'Balo & Túi sách'),
(6, 'Sách & Dụng cụ'),
(7, 'Đồ cá nhân'),
(8, 'Vật dụng khác');

-- phpMyAdmin SQL Dump
-- version 5.1.1
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Generation Time: May 27, 2023 at 04:30 PM
-- Server version: 10.4.22-MariaDB
-- PHP Version: 8.1.1

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `e_commerce_springboot`
--

-- --------------------------------------------------------

--
-- Table structure for table `blogs`
--

CREATE TABLE `blogs` (
  `id` bigint(20) NOT NULL,
  `blog_content` varchar(255) DEFAULT NULL,
  `blog_image` varchar(250) DEFAULT NULL,
  `blog_title` varchar(255) DEFAULT NULL
) ENGINE=MyISAM DEFAULT CHARSET=utf8mb4;

--
-- Dumping data for table `blogs`
--

INSERT INTO `blogs` (`id`, `blog_content`, `blog_image`, `blog_title`) VALUES
(3, 'That dominion stars lights dominion divide years for fourth have don\'t stars is that he earth it first without heaven in place seed it second morning saying.', 'single_blog_1.png', 'Google inks pact for new 35-storey office'),
(4, 'That dominion stars lights dominion divide years for fourth have don\'t stars is that he earth it first without heaven in place seed it second morning saying.', 'single_blog_2.png', 'Google inks pact for new 35-storey office'),
(5, 'That dominion stars lights dominion divide years for fourth have don\'t stars is that he earth it first without heaven in place seed it second morning saying.', 'single_blog_3.png', 'Google inks pact for new 35-storey office'),
(6, 'That dominion stars lights dominion divide years for fourth have don\'t stars is that he earth it first without heaven in place seed it second morning saying.', 'single_blog_4.png', 'Google inks pact for new 35-storey office'),
(7, 'That dominion stars lights dominion divide years for fourth have don\'t stars is that he earth it first without heaven in place seed it second morning saying.', 'single_blog_5.png', 'Google inks pact for new 35-storey office');

-- --------------------------------------------------------

--
-- Table structure for table `carts`
--

CREATE TABLE `carts` (
  `id` bigint(20) NOT NULL,
  `quantity` int(11) NOT NULL,
  `product_id` bigint(20) DEFAULT NULL,
  `user_id` int(11) DEFAULT NULL
) ENGINE=MyISAM DEFAULT CHARSET=utf8mb4;

-- --------------------------------------------------------

--
-- Table structure for table `categories`
--

CREATE TABLE `categories` (
  `id` int(11) NOT NULL,
  `category_name` varchar(255) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Dumping data for table `categories`
--

INSERT INTO `categories` (`id`, `category_name`) VALUES
(2, 'Decorative pillows'),
(6, 'Massage pillow'),
(11, 'Yoga pillow'),
(12, 'Cushions'),
(13, 'Pillow'),
(17, 'Pillows for pregnant women'),
(18, 'Pillows for children');

-- --------------------------------------------------------

--
-- Table structure for table `contacts`
--

CREATE TABLE `contacts` (
  `id` bigint(20) NOT NULL,
  `email` varchar(255) DEFAULT NULL,
  `message` varchar(255) DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  `subject` varchar(255) DEFAULT NULL
) ENGINE=MyISAM DEFAULT CHARSET=utf8mb4;

--
-- Dumping data for table `contacts`
--

INSERT INTO `contacts` (`id`, `email`, `message`, `name`, `subject`) VALUES
(3, 'hoangdeptraibodoiqua4321@gmail.com', 'Hello Admin', 'Thuong Tran', 'Demo Send Email');

-- --------------------------------------------------------

--
-- Table structure for table `orders`
--

CREATE TABLE `orders` (
  `id` bigint(20) NOT NULL,
  `currency` varchar(255) DEFAULT NULL,
  `order_date` datetime DEFAULT NULL,
  `order_status` varchar(255) DEFAULT NULL,
  `payment_method` varchar(255) DEFAULT NULL,
  `product_name` varchar(255) DEFAULT NULL,
  `quantity` int(11) NOT NULL,
  `total_cost` double DEFAULT NULL,
  `product_id` bigint(20) DEFAULT NULL,
  `user_id` int(11) DEFAULT NULL
) ENGINE=MyISAM DEFAULT CHARSET=utf8mb4;

--
-- Dumping data for table `orders`
--

INSERT INTO `orders` (`id`, `currency`, `order_date`, `order_status`, `payment_method`, `product_name`, `quantity`, `total_cost`, `product_id`, `user_id`) VALUES
(16, 'USD', '2023-05-14 23:40:51', 'Paid', 'PAYPAL', 'Sleeping orthopedic sciatica Back Hip Joint Pain relief', 3, 27, 10, 7),
(18, 'USD', '2023-05-23 23:24:56', 'Paid', 'PAYPAL', 'Foam filling cotton slow rebound pillows', 1, 8, 4, 19);

-- --------------------------------------------------------

--
-- Table structure for table `products`
--

CREATE TABLE `products` (
  `id` bigint(20) NOT NULL,
  `category_name` varchar(255) DEFAULT NULL,
  `description` varchar(255) DEFAULT NULL,
  `image` varchar(250) DEFAULT NULL,
  `price` double DEFAULT NULL,
  `product_name` varchar(255) DEFAULT NULL,
  `weight` double DEFAULT NULL
) ENGINE=MyISAM DEFAULT CHARSET=utf8mb4;

--
-- Dumping data for table `products`
--

INSERT INTO `products` (`id`, `category_name`, `description`, `image`, `price`, `product_name`, `weight`) VALUES
(3, 'Pillow', 'Cervical pillow for airplane car office nap pillow', 'product_list_1.png', 40, 'Cervical pillow for airplane car office nap pillow', 15),
(2, 'Pillow', 'Geometric striped flower home classy decor', 'product_list_2.png', 30, 'Geometric striped flower home classy decor', 15),
(4, 'Pillow', 'Foam filling cotton slow rebound pillows', 'product_list_3.png', 20, 'Foam filling cotton slow rebound pillows', 14),
(5, 'Pillow', 'Memory foam filling cotton Slow rebound pillows', 'product_list_4.png', 50, 'Memory foam filling cotton Slow rebound pillows', 10),
(6, 'Pillow', 'Memory foam filling cotton Slow rebound pillows', 'product_list_5.png', 45, 'Memory foam filling cotton Slow rebound pillows', 15),
(8, 'Decorative pillows', 'Sleeping orthopedic sciatica Back Hip Joint Pain relief', 'product_list_6.png', 55, 'Sleeping orthopedic sciatica Back Hip Joint Pain relief', 10),
(9, 'Decorative pillows', 'Memory foam filling cotton Slow rebound pillows', 'product_list_7.png', 65, 'Memory foam filling cotton Slow rebound pillows', 25),
(10, 'Decorative pillows', 'Sleeping orthopedic sciatica Back Hip Joint Pain relief', 'product_list_8.png', 90, 'Sleeping orthopedic sciatica Back Hip Joint Pain relief', 20),
(11, 'Decorative pillows', 'Geometric striped flower home classy decor', 'product_list_9.png', 70, 'Geometric striped flower home classy decor', 22),
(12, 'Decorative pillows', 'Geometric striped flower home classy decor', 'product_list_10.png', 80, 'Geometric striped flower home classy decor', 18),
(14, 'Massage pillow', 'Cervical pillow for airplane car office nap pillow', 'product_list_11.png', 95, 'Cervical pillow for airplane car office nap pillow', 20),
(15, 'Yoga pillow', 'Foam filling cotton slow rebound pillows', 'product_list_12.png', 25, 'Foam filling cotton slow rebound pillows', 15),
(16, 'Massage pillow', 'Foam filling cotton slow rebound pillows', 'product_list_13.png', 35, 'Foam filling cotton slow rebound pillows', 25),
(17, 'Massage pillow', 'Cervical pillow for airplane car office nap pillow', 'product_list_14.png', 85, 'Cervical pillow for airplane car office nap pillow', 20),
(18, 'Cushions', 'Foam filling cotton slow rebound pillows', 'product_list_15.png', 15, 'Foam filling cotton slow rebound pillows', 20),
(19, 'Yoga pillow', 'Foam filling cotton slow rebound pillows', 'product_list_16.png', 100, 'Foam filling cotton slow rebound pillows', 20),
(20, 'Cushions', 'Printed memory foam brief modern throw pillow case', 'product_list_17.png', 75, 'Printed memory foam brief modern throw pillow case', 25);

-- --------------------------------------------------------

--
-- Table structure for table `users`
--

CREATE TABLE `users` (
  `id` int(11) NOT NULL,
  `email` varchar(255) DEFAULT NULL,
  `first_name` varchar(255) DEFAULT NULL,
  `last_name` varchar(255) DEFAULT NULL,
  `password` varchar(255) DEFAULT NULL,
  `role` varchar(255) DEFAULT NULL,
  `auth_provider` varchar(255) DEFAULT NULL,
  `username` varchar(255) DEFAULT NULL,
  `avatar` varchar(255) DEFAULT NULL
) ENGINE=MyISAM DEFAULT CHARSET=utf8mb4;

--
-- Dumping data for table `users`
--

INSERT INTO `users` (`id`, `email`, `first_name`, `last_name`, `password`, `role`, `auth_provider`, `username`, `avatar`) VALUES
(7, 'user@gmail.com', 'Thuong', 'Tran', '$2a$10$FeUEpMAn9HIWNMmRhe9MuuPpBupbGaf.NSOthZiZ5OXbfcST.fK22', 'ROLE_USER', NULL, 'Thuong Tran', NULL),
(8, 'admin@gmail.com', 'Admin', 'Admin', '$2a$10$9e42q642PJOU.40KxZGRDOl8M2czG8EOBbbQrCr2HBXW9WMBtT08S', 'ROLE_ADMIN', NULL, 'Admin', NULL),
(22, 'hoangdeptraibodoiqua4321@gmail.com', 'Tran Dac Thuong', 'Tran Dac Thuong', 'Password of google account', 'ROLE_USER', 'GOOGLE', '102985581484631668324', 'https://lh3.googleusercontent.com/a/AAcHTtfUAqtShFkm_17g5Tvd0mPX4qrrPTGNRLANftiC=s96-c');

--
-- Indexes for dumped tables
--

--
-- Indexes for table `blogs`
--
ALTER TABLE `blogs`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `carts`
--
ALTER TABLE `carts`
  ADD PRIMARY KEY (`id`),
  ADD KEY `FKmd2ap4oxo3wvgkf4fnaye532i` (`product_id`),
  ADD KEY `FKb5o626f86h46m4s7ms6ginnop` (`user_id`);

--
-- Indexes for table `categories`
--
ALTER TABLE `categories`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `contacts`
--
ALTER TABLE `contacts`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `orders`
--
ALTER TABLE `orders`
  ADD PRIMARY KEY (`id`),
  ADD KEY `FKkp5k52qtiygd8jkag4hayd0qg` (`product_id`),
  ADD KEY `FK32ql8ubntj5uh44ph9659tiih` (`user_id`);

--
-- Indexes for table `products`
--
ALTER TABLE `products`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `users`
--
ALTER TABLE `users`
  ADD PRIMARY KEY (`id`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `blogs`
--
ALTER TABLE `blogs`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=8;

--
-- AUTO_INCREMENT for table `carts`
--
ALTER TABLE `carts`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=8295744241869544800;

--
-- AUTO_INCREMENT for table `categories`
--
ALTER TABLE `categories`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=19;

--
-- AUTO_INCREMENT for table `contacts`
--
ALTER TABLE `contacts`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=4;

--
-- AUTO_INCREMENT for table `orders`
--
ALTER TABLE `orders`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=22;

--
-- AUTO_INCREMENT for table `products`
--
ALTER TABLE `products`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=21;

--
-- AUTO_INCREMENT for table `users`
--
ALTER TABLE `users`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=23;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;

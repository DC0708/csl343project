-- phpMyAdmin SQL Dump
-- version 4.0.10deb1
-- http://www.phpmyadmin.net
--
-- Host: localhost
-- Generation Time: Mar 10, 2015 at 12:12 AM
-- Server version: 5.5.41-0ubuntu0.14.04.1
-- PHP Version: 5.5.9-1ubuntu4.6

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;

--
-- Database: `android_api`
--
CREATE DATABASE IF NOT EXISTS `android_api` DEFAULT CHARACTER SET latin1 COLLATE latin1_swedish_ci;
USE `android_api`;

-- --------------------------------------------------------

--
-- Table structure for table `users`
--

DROP TABLE IF EXISTS `users`;
CREATE TABLE IF NOT EXISTS `users` (
  `uid` int(11) NOT NULL AUTO_INCREMENT,
  `unique_id` varchar(23) NOT NULL,
  `name` varchar(50) NOT NULL,
  `email` varchar(100) NOT NULL,
  `encrypted_password` varchar(80) NOT NULL,
  `salt` varchar(10) NOT NULL,
  `created_at` datetime DEFAULT NULL,
  `updated_at` datetime DEFAULT NULL,
  PRIMARY KEY (`uid`),
  UNIQUE KEY `unique_id` (`unique_id`),
  UNIQUE KEY `email` (`email`)
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 AUTO_INCREMENT=9 ;



DROP TABLE IF EXISTS `songs_details`;
CREATE TABLE IF NOT EXISTS `songs_details` (
  `uid` int(11) NOT NULL,   	
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `name` varchar(200) ,
  `artist_band` varchar(100) DEFAULT NULL,
  `album_movie` varchar(100) DEFAULT NULL,
  `language_song` varchar(100) DEFAULT NULL,
  `yt_link` varchar(500) DEFAULT NULL,
  `itunes_link` varchar(500) DEFAULT NULL,
  `length` varchar(10) DEFAULT NULL,
  `genre` varchar(50),
  PRIMARY KEY (`id`),
  FOREIGN KEY (`uid`) REFERENCES `users`(`uid`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 AUTO_INCREMENT=1 ;

--
-- Dumping data for table `users`
--

INSERT INTO `users` (`uid`, `unique_id`, `name`, `email`, `encrypted_password`, `salt`, `created_at`, `updated_at`) VALUES
(1, '54faa4f9c74300.24458003', 'Aditya', 'aditya@gmail.com', 'TrekZFotfAzjF3VbP2rEUQZbTr4yYmYwMjRiZDlh', '2bf024bd9a', '2015-03-07 12:42:57', NULL),
(2, '54faa5f0068a58.90907084', 'Vivi', 'vivo@gmail.com', 'wcJkDDMwWLmVZ8BKyExkNgTivak0MTZmMGYyODVi', '416f0f285b', '2015-03-07 12:47:04', NULL),
(3, '54faa6189020a5.81895578', 'Chimed', 'chimedpaldan@gmail.com', 'ADK19EqVp1SaFzPVui/DXYGQ4UAyNjkxOTg3NjVl', '269198765e', '2015-03-07 12:47:44', NULL),
(4, '54faa701c2a039.84036117', 'adi', 'a@gmail.com', 'GxmhAx1bao+gaHYTZBsgvWRgvb4wMThlZWY1NzMw', '018eef5730', '2015-03-07 12:51:37', NULL),
(5, '54fad6f13912f3.44752515', 'Xx', 'f', 'Fnth5Qfq9WEM4Gbk8ewBHK7EzBkwNTM2NTRjMzI3', '053654c327', '2015-03-07 16:16:09', NULL),
(6, '54fae18c5cb526.36507070', 'Afaf', 'awfaf', '9iK+sO9CtQVPRVcYhXey/Iirn0tiZmI4MDRlYjk3', 'bfb804eb97', '2015-03-07 17:01:24', NULL),
(7, '54fcd571c16389.63282438', 'Xyz', 'xyz@gmail.com', '0D9KFFiqs3oZQP/bSYWDJ5wHJZ1jOGNlMzg0OTg3', 'c8ce384987', '2015-03-09 04:34:17', NULL),
(8, '54fd925c973451.86359868', 'Abc', 'a@mail.com', 'twTBc2sII9OI6m6ZvFS8sTuLuA81YzMzMGYwNDFi', '5c330f041b', '2015-03-09 18:00:20', NULL);

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;

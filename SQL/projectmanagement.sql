-- phpMyAdmin SQL Dump
-- version 4.0.10deb1
-- http://www.phpmyadmin.net
--
-- Host: localhost
-- Generation Time: Oct 27, 2016 at 09:42 AM
-- Server version: 5.5.53-0ubuntu0.14.04.1
-- PHP Version: 5.5.9-1ubuntu4.20

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;

--
-- Database: `projectmanagement`
--

-- --------------------------------------------------------

--
-- Table structure for table `Assignments`
--

CREATE TABLE IF NOT EXISTS `Assignments` (
  `AssignmentID` int(11) NOT NULL AUTO_INCREMENT,
  `TaskIDFS` int(11) NOT NULL,
  `EmployeeIDFS` int(11) NOT NULL,
  PRIMARY KEY (`AssignmentID`),
  KEY `TaskIDFS` (`TaskIDFS`),
  KEY `EmployeeIDFS` (`EmployeeIDFS`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci AUTO_INCREMENT=63 ;

--
-- Dumping data for table `Assignments`
--

INSERT INTO `Assignments` (`AssignmentID`, `TaskIDFS`, `EmployeeIDFS`) VALUES
(49, 40, 11),
(50, 39, 12),
(51, 40, 12),
(52, 40, 13),
(53, 39, 14),
(54, 40, 14),
(55, 39, 15),
(56, 40, 15),
(57, 39, 16),
(58, 40, 16),
(59, 39, 17),
(60, 40, 17),
(61, 40, 18),
(62, 40, 19);

-- --------------------------------------------------------

--
-- Table structure for table `Bookings`
--

CREATE TABLE IF NOT EXISTS `Bookings` (
  `BookingID` int(11) NOT NULL AUTO_INCREMENT,
  `AssignmentIDFS` int(11) NOT NULL,
  `Month` int(11) NOT NULL,
  `Hours` float NOT NULL,
  PRIMARY KEY (`BookingID`),
  KEY `AssignmentID` (`AssignmentIDFS`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci AUTO_INCREMENT=53 ;

--
-- Dumping data for table `Bookings`
--

INSERT INTO `Bookings` (`BookingID`, `AssignmentIDFS`, `Month`, `Hours`) VALUES
(7, 50, 1, 140),
(8, 51, 3, 112),
(9, 49, 3, 131),
(10, 50, 2, 153),
(11, 53, 1, 145),
(12, 54, 3, 112),
(13, 53, 2, 133),
(14, 54, 4, 142),
(15, 51, 4, 147),
(16, 55, 1, 136),
(17, 57, 1, 136),
(19, 51, 5, 186),
(20, 51, 6, 116),
(21, 51, 7, 107),
(22, 51, 8, 114),
(23, 51, 9, 157),
(24, 51, 10, 144),
(25, 51, 11, 158),
(26, 51, 12, 161),
(27, 51, 13, 155),
(28, 51, 14, 124),
(29, 51, 15, 149),
(30, 51, 16, 189),
(31, 51, 17, 113),
(32, 51, 18, 148),
(33, 54, 5, 186),
(34, 54, 6, 116),
(35, 54, 7, 107),
(36, 54, 8, 114),
(37, 54, 9, 157),
(38, 54, 10, 144),
(39, 54, 11, 158),
(40, 54, 12, 161),
(41, 54, 13, 155),
(42, 54, 14, 124),
(43, 54, 15, 149),
(44, 54, 16, 189),
(45, 54, 17, 113),
(46, 54, 18, 148),
(47, 55, 2, 158),
(48, 57, 2, 158),
(49, 59, 2, 158),
(50, 52, 3, 159),
(51, 61, 3, 134),
(52, 62, 3, 145);

-- --------------------------------------------------------

--
-- Table structure for table `Employees`
--

CREATE TABLE IF NOT EXISTS `Employees` (
  `EmployeeID` int(11) NOT NULL AUTO_INCREMENT,
  `Firstname` varchar(255) COLLATE utf8_unicode_ci NOT NULL,
  `Lastname` varchar(255) COLLATE utf8_unicode_ci NOT NULL,
  `Kuerzel` varchar(10) COLLATE utf8_unicode_ci NOT NULL,
  `Password` varchar(255) COLLATE utf8_unicode_ci NOT NULL,
  `Mail` varchar(255) COLLATE utf8_unicode_ci NOT NULL,
  `Supervisor` int(11) DEFAULT NULL,
  PRIMARY KEY (`EmployeeID`),
  UNIQUE KEY `Kuerzel` (`Kuerzel`),
  UNIQUE KEY `Mail` (`Mail`),
  KEY `Supervisor` (`Supervisor`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci AUTO_INCREMENT=20 ;

--
-- Dumping data for table `Employees`
--

INSERT INTO `Employees` (`EmployeeID`, `Firstname`, `Lastname`, `Kuerzel`, `Password`, `Mail`, `Supervisor`) VALUES
(1, 'Admin', ' ', 'admin', '0DPiKuNIrrVmD8IUCuw1hQxNqZc=', 'nothereyet', NULL),
(11, 'Gandalf', 'the Grey', 'gand', 'hipn9Cx77kIHq9T6iRIY5FHe4CI=', 'janine.walther@zhaw.ch', 1),
(12, 'Frodo', 'Baggins', 'frod', 'hipn9Cx77kIHq9T6iRIY5FHe4CI=', 'frod@ichweissaunidwasschriebehoffentlichchunntsnienetzsahh.ch', 11),
(13, 'Legolas', 'Greenleaf', 'lego', 'hipn9Cx77kIHq9T6iRIY5FHe4CI=', 'leglesslegolegolas@leglesslegolegolasfoundation.ajksdf', 11),
(14, 'Samwise', 'Gamgee', 'samg', 'hipn9Cx77kIHq9T6iRIY5FHe4CI=', 'samwisegmagee@leglesslegolegolasfoundation.asdfadsjgf', 11),
(15, 'Peregrin', 'Took', 'pipp', 'hipn9Cx77kIHq9T6iRIY5FHe4CI=', 'peregrinpippintook@takingthehobbitstoisengard.asdfja', 11),
(16, 'Meriadoc ', 'Brandybuck', 'meri', 'hipn9Cx77kIHq9T6iRIY5FHe4CI=', 'merry@takingthehobbitstoisengard.asdfja', 11),
(17, 'Aragorn II', 'Son of Arathorn', 'arag', 'hipn9Cx77kIHq9T6iRIY5FHe4CI=', 'aragorniisonofarathorn@ilovearwen.asdfghr', 11),
(18, 'Boromir', 'Son of Denethor II', 'boro', 'hipn9Cx77kIHq9T6iRIY5FHe4CI=', 'boroporodorosonofanaho@idieearlybecauseimplayedbythisguywhodiesineverymovie.asdf', 11),
(19, 'Gimli', 'Son of Glóin', 'giml', 'hipn9Cx77kIHq9T6iRIY5FHe4CI=', 'gimlisonofgloin@youhavemyaxe.asdfagsr', 11);

-- --------------------------------------------------------

--
-- Table structure for table `Expenses`
--

CREATE TABLE IF NOT EXISTS `Expenses` (
  `ExpenseID` int(11) NOT NULL AUTO_INCREMENT,
  `ProjectIDFS` int(11) NOT NULL,
  `EmployeeIDFS` int(11) NOT NULL,
  `Costs` float NOT NULL,
  `Type` varchar(255) COLLATE utf8_unicode_ci NOT NULL,
  `Description` varchar(255) COLLATE utf8_unicode_ci NOT NULL,
  `Date` date NOT NULL,
  PRIMARY KEY (`ExpenseID`),
  KEY `EmployeeIDFS` (`EmployeeIDFS`),
  KEY `ProjectIDFS` (`ProjectIDFS`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci AUTO_INCREMENT=13 ;

--
-- Dumping data for table `Expenses`
--

INSERT INTO `Expenses` (`ExpenseID`, `ProjectIDFS`, `EmployeeIDFS`, `Costs`, `Type`, `Description`, `Date`) VALUES
(5, 13, 12, 100, 'Overnight Stay', '1 Night in the Prancing Pony ', '2015-04-20'),
(6, 13, 14, 100, 'Overnight Stay', '1 Night in the Prancing Pony ', '2015-04-20'),
(7, 13, 15, 100, 'Overnight Stay', '1 Night in the Prancing Pony', '2015-04-20'),
(8, 13, 16, 100, 'Overnight Stay', '1 Night in the Prancing Pony', '2015-04-20'),
(9, 13, 12, 10, 'Travel', 'Bucklebury ferry', '2015-04-20'),
(10, 13, 14, 10, 'Travel', 'Bucklebury ferry', '2015-04-20'),
(11, 13, 15, 10, 'Travel', 'Bucklebury ferry', '2015-04-20'),
(12, 13, 16, 10, 'Travel', 'Bucklebury ferry', '2015-04-20');

-- --------------------------------------------------------

--
-- Table structure for table `Projects`
--

CREATE TABLE IF NOT EXISTS `Projects` (
  `ProjectIDFS` int(11) NOT NULL AUTO_INCREMENT,
  `ProjectShortname` varchar(255) COLLATE utf8_unicode_ci NOT NULL,
  `ProjectName` varchar(255) COLLATE utf8_unicode_ci NOT NULL,
  `ProjectLeader` int(11) NOT NULL,
  `TotalBudget` float NOT NULL,
  `Currency` enum('CHF','EUR') COLLATE utf8_unicode_ci NOT NULL DEFAULT 'CHF',
  `ProjectStart` date NOT NULL,
  `ProjectEnd` date NOT NULL,
  `Partner` varchar(255) COLLATE utf8_unicode_ci NOT NULL DEFAULT '-',
  `Archive` tinyint(1) NOT NULL DEFAULT '0',
  PRIMARY KEY (`ProjectIDFS`),
  KEY `ProjectLeader` (`ProjectLeader`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci AUTO_INCREMENT=14 ;

--
-- Dumping data for table `Projects`
--

INSERT INTO `Projects` (`ProjectIDFS`, `ProjectShortname`, `ProjectName`, `ProjectLeader`, `TotalBudget`, `Currency`, `ProjectStart`, `ProjectEnd`, `Partner`, `Archive`) VALUES
(13, 'BtRtM', 'Bring the Ring to Mordor', 11, 1200000, 'CHF', '2015-04-01', '2017-05-31', 'Hobbit GmbH, Elves AG', 0);

-- --------------------------------------------------------

--
-- Table structure for table `Tasks`
--

CREATE TABLE IF NOT EXISTS `Tasks` (
  `TaskID` int(11) NOT NULL AUTO_INCREMENT,
  `WorkpackageIDFS` int(11) NOT NULL,
  `TaskName` varchar(255) COLLATE utf8_unicode_ci NOT NULL,
  `TaskStart` date NOT NULL,
  `TaskEnd` date NOT NULL,
  `PMs` int(11) NOT NULL,
  `Budget` float DEFAULT NULL,
  PRIMARY KEY (`TaskID`),
  KEY `WorkpackageIDFS` (`WorkpackageIDFS`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci AUTO_INCREMENT=41 ;

--
-- Dumping data for table `Tasks`
--

INSERT INTO `Tasks` (`TaskID`, `WorkpackageIDFS`, `TaskName`, `TaskStart`, `TaskEnd`, `PMs`, `Budget`) VALUES
(39, 33, 'Ring to Rivendell', '2015-04-01', '2015-05-31', 10, 50000),
(40, 33, 'Ring to Mount Doom', '2015-06-01', '2017-05-31', 92, 950000);

-- --------------------------------------------------------

--
-- Table structure for table `Wage`
--

CREATE TABLE IF NOT EXISTS `Wage` (
  `WageID` int(11) NOT NULL AUTO_INCREMENT,
  `EmployeeIDFS` int(11) NOT NULL,
  `WagePerHour` float NOT NULL,
  `ValidFrom` date NOT NULL,
  PRIMARY KEY (`WageID`),
  KEY `EmployeeIDFS` (`EmployeeIDFS`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci AUTO_INCREMENT=19 ;

--
-- Dumping data for table `Wage`
--

INSERT INTO `Wage` (`WageID`, `EmployeeIDFS`, `WagePerHour`, `ValidFrom`) VALUES
(7, 1, 25, '2016-01-01'),
(10, 11, 30, '2016-10-26'),
(11, 12, 25, '2016-10-26'),
(12, 13, 30, '2016-10-26'),
(13, 14, 24, '2016-10-26'),
(14, 15, 24, '2016-10-26'),
(15, 16, 24, '2016-10-26'),
(16, 17, 30, '2016-10-26'),
(17, 18, 28, '2016-10-26'),
(18, 19, 30, '2016-10-26');

-- --------------------------------------------------------

--
-- Table structure for table `Workpackages`
--

CREATE TABLE IF NOT EXISTS `Workpackages` (
  `WorkpackageID` int(11) NOT NULL AUTO_INCREMENT,
  `ProjectIDFS` int(11) NOT NULL,
  `WPName` varchar(255) COLLATE utf8_unicode_ci NOT NULL,
  `WPStart` date NOT NULL,
  `WPEnd` date NOT NULL,
  PRIMARY KEY (`WorkpackageID`),
  KEY `ProjectIDFS` (`ProjectIDFS`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci AUTO_INCREMENT=34 ;

--
-- Dumping data for table `Workpackages`
--

INSERT INTO `Workpackages` (`WorkpackageID`, `ProjectIDFS`, `WPName`, `WPStart`, `WPEnd`) VALUES
(33, 13, 'Bring the Ring to Mordor', '2015-04-01', '2017-05-31');

--
-- Constraints for dumped tables
--

--
-- Constraints for table `Assignments`
--
ALTER TABLE `Assignments`
  ADD CONSTRAINT `Assignments_ibfk_1` FOREIGN KEY (`TaskIDFS`) REFERENCES `Tasks` (`TaskID`) ON DELETE CASCADE ON UPDATE CASCADE,
  ADD CONSTRAINT `Assignments_ibfk_2` FOREIGN KEY (`EmployeeIDFS`) REFERENCES `Employees` (`EmployeeID`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Constraints for table `Bookings`
--
ALTER TABLE `Bookings`
  ADD CONSTRAINT `Bookings_ibfk_1` FOREIGN KEY (`AssignmentIDFS`) REFERENCES `Assignments` (`AssignmentID`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Constraints for table `Employees`
--
ALTER TABLE `Employees`
  ADD CONSTRAINT `Employees_ibfk_1` FOREIGN KEY (`Supervisor`) REFERENCES `Employees` (`EmployeeID`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Constraints for table `Expenses`
--
ALTER TABLE `Expenses`
  ADD CONSTRAINT `Expenses_ibfk_1` FOREIGN KEY (`ProjectIDFS`) REFERENCES `Projects` (`ProjectIDFS`) ON DELETE CASCADE ON UPDATE CASCADE,
  ADD CONSTRAINT `Expenses_ibfk_2` FOREIGN KEY (`EmployeeIDFS`) REFERENCES `Employees` (`EmployeeID`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Constraints for table `Projects`
--
ALTER TABLE `Projects`
  ADD CONSTRAINT `Projects_ibfk_1` FOREIGN KEY (`ProjectLeader`) REFERENCES `Employees` (`EmployeeID`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Constraints for table `Tasks`
--
ALTER TABLE `Tasks`
  ADD CONSTRAINT `Tasks_ibfk_1` FOREIGN KEY (`WorkpackageIDFS`) REFERENCES `Workpackages` (`WorkpackageID`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Constraints for table `Wage`
--
ALTER TABLE `Wage`
  ADD CONSTRAINT `Wage_ibfk_1` FOREIGN KEY (`EmployeeIDFS`) REFERENCES `Employees` (`EmployeeID`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Constraints for table `Workpackages`
--
ALTER TABLE `Workpackages`
  ADD CONSTRAINT `Workpackages_ibfk_1` FOREIGN KEY (`ProjectIDFS`) REFERENCES `Projects` (`ProjectIDFS`) ON DELETE CASCADE ON UPDATE CASCADE;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;

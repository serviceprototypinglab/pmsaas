-- phpMyAdmin SQL Dump
-- version 4.0.10deb1
-- http://www.phpmyadmin.net
--
-- Host: localhost
-- Generation Time: Jul 11, 2016 at 11:00 AM
-- Server version: 5.5.49-0ubuntu0.14.04.1
-- PHP Version: 5.5.9-1ubuntu4.17

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;

--
-- Database: `projectmanagement`
--
CREATE DATABASE IF NOT EXISTS `projectmanagement` DEFAULT CHARACTER SET utf8 COLLATE utf8_unicode_ci;
USE `projectmanagement`;

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
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci AUTO_INCREMENT=3 ;

--
-- Dumping data for table `Assignments`
--

INSERT INTO `Assignments` (`AssignmentID`, `TaskIDFS`, `EmployeeIDFS`) VALUES
(1, 1, 1),
(2, 2, 1);

-- --------------------------------------------------------

--
-- Table structure for table `Bookings`
--

CREATE TABLE IF NOT EXISTS `Bookings` (
  `BookingID` int(11) NOT NULL DEFAULT '0',
  `AssignmentIDFS` int(11) NOT NULL,
  `Month` int(11) NOT NULL,
  `Hours` float NOT NULL,
  PRIMARY KEY (`BookingID`),
  KEY `AssignmentID` (`AssignmentIDFS`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

--
-- Dumping data for table `Bookings`
--

INSERT INTO `Bookings` (`BookingID`, `AssignmentIDFS`, `Month`, `Hours`) VALUES
(0, 1, 1, 168);

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
  KEY `Supervisor` (`Supervisor`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci AUTO_INCREMENT=2 ;

--
-- Dumping data for table `Employees`
--

INSERT INTO `Employees` (`EmployeeID`, `Firstname`, `Lastname`, `Kuerzel`, `Password`, `Mail`, `Supervisor`) VALUES
(1, 'Janine', 'Walther', 'walj', 'test123', 'walj@zhaw.ch', NULL);

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
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci AUTO_INCREMENT=2 ;

--
-- Dumping data for table `Expenses`
--

INSERT INTO `Expenses` (`ExpenseID`, `ProjectIDFS`, `EmployeeIDFS`, `Costs`, `Type`, `Description`, `Date`) VALUES
(1, 1, 1, 100, 'Travel', 'Travel to very important destination', '2016-07-01');

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
  `Partner` varchar(255) COLLATE utf8_unicode_ci NOT NULL,
  PRIMARY KEY (`ProjectIDFS`),
  KEY `ProjectLeader` (`ProjectLeader`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci AUTO_INCREMENT=13 ;

--
-- Dumping data for table `Projects`
--

INSERT INTO `Projects` (`ProjectIDFS`, `ProjectShortname`, `ProjectName`, `ProjectLeader`, `TotalBudget`, `Currency`, `ProjectStart`, `ProjectEnd`, `Partner`) VALUES
(1, 'A', 'Project Alpha', 1, 25000, 'CHF', '2016-07-01', '2016-09-30', 'ABC Company, XYZ'),
(2, 'B', 'Project Beta', 1, 12334, 'EUR', '2016-07-01', '2016-07-02', 'adsf'),
(3, 'C', 'Project Gamma', 1, 500000, 'CHF', '2016-07-01', '2016-07-31', 'This&That GmbH'),
(4, 'C', 'Project Gamma', 1, 500000, 'CHF', '2016-07-01', '2016-07-31', 'This&That GmbH'),
(5, 'C', 'Project Gamma', 1, 500000, 'CHF', '2016-07-01', '2016-07-31', 'This&That GmbH'),
(6, 'C', 'Project Gamma', 1, 500000, 'CHF', '2016-07-01', '2016-07-31', 'This&That GmbH'),
(7, 'D', 'Delta', 1, 500000, 'EUR', '2016-07-01', '2017-05-31', 'YXZ Firma'),
(8, 'D', 'Delta', 1, 500000, 'EUR', '2016-07-01', '2017-05-31', 'YXZ Firma'),
(9, 'D', 'Project Delta', 1, 500000, 'EUR', '2016-07-01', '2017-05-31', 'YXZ Firma'),
(10, 'D', 'Project Delta', 1, 500000, 'EUR', '2016-07-01', '2017-05-31', 'YXZ Firma'),
(11, 'C', 'Project Gamma', 1, 500000, 'EUR', '2016-07-01', '2017-05-31', 'YXZ Firma'),
(12, 'C', 'Project Gamma', 1, 500000, 'EUR', '2016-07-01', '2017-05-31', 'This&That GmbH');

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
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci AUTO_INCREMENT=4 ;

--
-- Dumping data for table `Tasks`
--

INSERT INTO `Tasks` (`TaskID`, `WorkpackageIDFS`, `TaskName`, `TaskStart`, `TaskEnd`, `PMs`, `Budget`) VALUES
(1, 1, 'Task 1', '2016-07-01', '2016-07-31', 2, 8300),
(2, 1, 'Task 2', '2016-08-01', '2016-09-30', 4, 16600),
(3, 6, 'Ultimate Task 1', '2016-07-01', '2017-05-31', 12, 270000);

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
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci AUTO_INCREMENT=2 ;

--
-- Dumping data for table `Wage`
--

INSERT INTO `Wage` (`WageID`, `EmployeeIDFS`, `WagePerHour`, `ValidFrom`) VALUES
(1, 1, 30, '2015-06-01');

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
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci AUTO_INCREMENT=7 ;

--
-- Dumping data for table `Workpackages`
--

INSERT INTO `Workpackages` (`WorkpackageID`, `ProjectIDFS`, `WPName`, `WPStart`, `WPEnd`) VALUES
(1, 1, 'Workpackage 1', '2016-07-01', '2016-09-30'),
(2, 10, 'WP1', '2016-07-01', '2016-12-31'),
(3, 10, 'WP2', '2017-01-01', '2017-05-31'),
(4, 11, 'WP1', '2016-07-01', '2016-12-31'),
(5, 11, 'WP2', '2017-01-01', '2017-05-31'),
(6, 12, 'WP1', '2016-07-01', '2017-05-31');

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

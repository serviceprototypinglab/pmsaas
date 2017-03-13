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
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci AUTO_INCREMENT=45 ;

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
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci AUTO_INCREMENT=3 ;

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
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci AUTO_INCREMENT=9 ;

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
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci AUTO_INCREMENT=5 ;

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
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci AUTO_INCREMENT=12 ;

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
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci AUTO_INCREMENT=38 ;

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
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci AUTO_INCREMENT=10 ;

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
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci AUTO_INCREMENT=32 ;

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

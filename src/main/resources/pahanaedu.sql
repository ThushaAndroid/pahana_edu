-- MySQL dump 10.13  Distrib 8.0.33, for Win64 (x86_64)
--
-- Host: localhost    Database: pahanaedu
-- ------------------------------------------------------
-- Server version	8.0.33

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `bill_details`
--

DROP TABLE IF EXISTS `bill_details`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `bill_details` (
  `detail_id` int NOT NULL AUTO_INCREMENT,
  `invoice_no` varchar(20) NOT NULL,
  `item_code` varchar(20) NOT NULL,
  `item_name` varchar(100) NOT NULL,
  `description` varchar(100) DEFAULT NULL,
  `price` double NOT NULL,
  `quantity` int NOT NULL,
  `total` decimal(10,2) NOT NULL,
  PRIMARY KEY (`detail_id`),
  KEY `invoice_no` (`invoice_no`),
  CONSTRAINT `bill_details_ibfk_1` FOREIGN KEY (`invoice_no`) REFERENCES `invoices` (`invoice_no`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=16 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `bill_details`
--

LOCK TABLES `bill_details` WRITE;
/*!40000 ALTER TABLE `bill_details` DISABLE KEYS */;
INSERT INTO `bill_details` VALUES (1,'INV008','ITMC001','Sinhala hodiya','For Childern',1000,1,1000.00),(4,'INV010','ITMC002','English Grammer','For All',500,1,500.00),(6,'INV011','ITMC001','Sinhala hodiya','For Childern',1000,1,1000.00),(7,'INV012','ITMC003','News papers','Daily updates',70,2,140.00),(8,'INV012','ITMC002','English Grammer','For All',500,1,500.00),(9,'INV013','ITMC001','Sinhala hodiya','For Childern',1000,1,1000.00),(12,'INV014','ITMC003','News papers','Daily updates',70,1,70.00),(14,'INV015','ITMC002','English Grammer','For All',500,1,500.00),(15,'INV016','ITMC002','English Grammer','For All',500,1,500.00);
/*!40000 ALTER TABLE `bill_details` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `customers`
--

DROP TABLE IF EXISTS `customers`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `customers` (
  `account_number` varchar(20) NOT NULL,
  `name` varchar(100) NOT NULL,
  `address` varchar(200) DEFAULT NULL,
  `telephone` varchar(20) DEFAULT NULL,
  `units_consumed` int NOT NULL DEFAULT '0',
  `nic` varchar(15) NOT NULL,
  `email` varchar(100) NOT NULL,
  PRIMARY KEY (`account_number`),
  UNIQUE KEY `nic` (`nic`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `customers`
--

LOCK TABLES `customers` WRITE;
/*!40000 ALTER TABLE `customers` DISABLE KEYS */;
INSERT INTO `customers` VALUES ('CUST001','kalana','E/1/1, Happy Land, Nilwakka, Kegalle','0713731530',1,'973590286V','kalana24@gmail.com'),('CUST002','thushan','E/1/1, Happy Land, Nilwakka, Kegalle','0713731530',1,'973423121V','thushankalana24@gmail.com'),('CUST003','tharindu','Mwanella','0772312456',0,'7567854V','tharindu@gmail.com');
/*!40000 ALTER TABLE `customers` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `invoices`
--

DROP TABLE IF EXISTS `invoices`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `invoices` (
  `invoice_no` varchar(20) NOT NULL,
  `customer_name` varchar(100) NOT NULL,
  `invoice_date` date NOT NULL,
  `due_date` date DEFAULT NULL,
  `discount` decimal(10,2) DEFAULT '0.00',
  `total_amount` decimal(10,2) NOT NULL,
  `cash` decimal(10,2) DEFAULT '0.00',
  `balance` decimal(10,2) NOT NULL,
  `status` enum('Pending','Paid','Cancelled') NOT NULL DEFAULT 'Pending',
  PRIMARY KEY (`invoice_no`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `invoices`
--

LOCK TABLES `invoices` WRITE;
/*!40000 ALTER TABLE `invoices` DISABLE KEYS */;
INSERT INTO `invoices` VALUES ('INV001','John Doe','2025-08-01','2025-08-15',0.00,500.00,0.00,200.00,'Paid'),('INV002','Jane Smith','2025-08-02','2025-08-16',0.00,750.50,0.00,0.00,'Paid'),('INV003','Acme Corp','2025-08-05','2025-08-20',0.00,1200.00,0.00,1200.00,'Pending'),('INV005','Sarah Lee','2025-08-07','2025-08-22',0.00,900.75,0.00,0.00,'Cancelled'),('INV006','kalana','2025-08-17','2025-08-19',0.00,1490.00,0.00,10.00,'Pending'),('INV007','thushan','2025-08-17','2025-08-17',0.00,500.00,0.00,0.00,'Paid'),('INV008','kalana','2025-08-17','2025-08-17',0.00,1000.00,0.00,0.00,'Paid'),('INV010','kalana','2025-08-18','2025-08-18',0.00,500.00,0.00,0.00,'Paid'),('INV011','thushan','2025-08-18','2025-08-18',0.00,1000.00,500.00,0.00,'Paid'),('INV012','tharindu','2025-08-18','2025-08-18',0.00,600.00,300.00,0.00,'Paid'),('INV013','thushan','2025-08-19','2025-08-19',0.00,1000.00,500.00,-500.00,'Pending'),('INV014','kalana','2025-08-20','2025-08-20',0.00,70.00,0.00,-70.00,'Pending'),('INV015','thushan','2025-08-20','2025-08-20',0.00,500.00,0.00,-500.00,'Pending'),('INV016','thushan','2025-08-20','2025-08-20',0.00,500.00,0.00,-500.00,'Pending');
/*!40000 ALTER TABLE `invoices` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `items`
--

DROP TABLE IF EXISTS `items`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `items` (
  `item_code` varchar(20) NOT NULL,
  `item_name` varchar(100) NOT NULL,
  `description` varchar(100) DEFAULT NULL,
  `price` double DEFAULT NULL,
  `quantity` int NOT NULL,
  PRIMARY KEY (`item_code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `items`
--

LOCK TABLES `items` WRITE;
/*!40000 ALTER TABLE `items` DISABLE KEYS */;
INSERT INTO `items` VALUES ('ITMC001','Sinhala hodiya','For Childern',1000,10),('ITMC002','English Grammer','For All',500,5),('ITMC003','News papers','Daily updates',70,20);
/*!40000 ALTER TABLE `items` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `users`
--

DROP TABLE IF EXISTS `users`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `users` (
  `user_id` varchar(20) NOT NULL,
  `username` varchar(50) NOT NULL,
  `password` varchar(50) NOT NULL,
  `role` enum('admin','staff') NOT NULL,
  `status` enum('active','disabled') NOT NULL DEFAULT 'disabled',
  PRIMARY KEY (`user_id`),
  UNIQUE KEY `username` (`username`),
  UNIQUE KEY `password` (`password`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `users`
--

LOCK TABLES `users` WRITE;
/*!40000 ALTER TABLE `users` DISABLE KEYS */;
INSERT INTO `users` VALUES ('USR0001','admin','admin123','admin','active'),('USR0002','kalana','kalana123','admin','active'),('USR0003','thushan','thushan123','staff','active');
/*!40000 ALTER TABLE `users` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2025-08-20  2:15:14

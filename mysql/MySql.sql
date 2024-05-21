-- MySQL dump 10.13  Distrib 8.0.32, for Win64 (x86_64)
--
-- Host: localhost    Database: wipcontrol
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



DROP TABLE IF EXISTS `sched`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `sched` (
  `id` int NOT NULL AUTO_INCREMENT,
  `id_story` int NOT NULL,
  `date` date NOT NULL,
  `hour` time NOT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_Sched_Story_idx` (`id_story`),
  CONSTRAINT `fk_Sched_Story` FOREIGN KEY (`id_story`) REFERENCES `story` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_spanish_ci;



LOCK TABLES `sched` WRITE;
/*!40000 ALTER TABLE `sched` DISABLE KEYS */;
INSERT INTO `sched` VALUES (1,3,'2023-12-31','12:12:00'),(2,4,'2023-12-31','12:12:00'),(3,5,'2024-01-01','12:12:00');
/*!40000 ALTER TABLE `sched` ENABLE KEYS */;
UNLOCK TABLES;



--
-- Table structure for table `comment`
--

DROP TABLE IF EXISTS `comment`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `comment` (
  `id` int NOT NULL AUTO_INCREMENT,
  `id_user` int NOT NULL,
  `id_story` int NOT NULL,
  `text` varchar(750) CHARACTER SET utf8mb4 COLLATE utf8mb4_spanish_ci NOT NULL,
  `date` datetime NOT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_User_has_Story_Story1_idx` (`id_story`),
  KEY `fk_User_has_Story_User_idx` (`id_user`),
  CONSTRAINT `fk_User_has_Story_Story1` FOREIGN KEY (`id_story`) REFERENCES `story` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `fk_User_has_Story_User` FOREIGN KEY (`id_user`) REFERENCES `user` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_spanish_ci;


--
-- Dumping data for table `comment`
--

LOCK TABLES `comment` WRITE;
/*!40000 ALTER TABLE `comment` DISABLE KEYS */;
INSERT INTO `comment` VALUES (1,2,9,'lorem ipsum','2023-10-15 21:45:23'),(2,2,10,'lorem ipsum :)','2023-10-16 21:45:53'),(3,2,12,'lorem ipsum :(','2023-10-15 21:46:22'),(4,2,7,'lorem ipsum','2023-10-15 21:46:39'),(5,2,3,'lorem ipsum','2023-10-15 21:47:27'),(6,1,9,'lorem ipsum','2023-10-16 22:45:53');/*!40000 ALTER TABLE `comment` ENABLE KEYS */;
UNLOCK TABLES;


DROP TABLE IF EXISTS `task_comment`;
CREATE TABLE `task_comment` (
  `id` int NOT NULL AUTO_INCREMENT,
  `id_user` int NOT NULL,
  `id_task` int NOT NULL,
  `text` varchar(750) CHARACTER SET utf8mb4 COLLATE utf8mb4_spanish_ci NOT NULL,
  `date` datetime NOT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_User_has_Task_Task1_idx` (`id_task`),
  KEY `fk_User_has_Task_User_idx` (`id_user`),
  CONSTRAINT `fk_User_has_Task_Task1` FOREIGN KEY (`id_task`) REFERENCES `task` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `fk_User_has_Task_User` FOREIGN KEY (`id_user`) REFERENCES `user` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_spanish_ci;




--
-- Table structure for table `story`
--

DROP TABLE IF EXISTS `story`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `story` (
  `id` int NOT NULL AUTO_INCREMENT,
  `name` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_spanish_ci NOT NULL,
  `assigned` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_spanish_ci NOT NULL,
  `estimation` int NOT NULL,
  `status` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_spanish_ci DEFAULT NULL,  
  `criteria` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_spanish_ci DEFAULT NULL,
  `description` varchar(750) CHARACTER SET utf8mb4 COLLATE utf8mb4_spanish_ci NOT NULL,
  `sprint` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_spanish_ci NOT NULL,
  `points` int NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`),
  UNIQUE KEY `name_UNIQUE` (`name`)
) ENGINE=InnoDB AUTO_INCREMENT=13 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_spanish_ci;


--
-- Dumping data for table `story`
--

LOCK TABLES `story` WRITE;
/*!40000 ALTER TABLE `story` DISABLE KEYS */;
INSERT INTO `story` VALUES (3,'US1','Francesc',90,'open','lorem ipsum','lorem ipsum','1',50),(4,'US2','Francesc',170,'open','lorem ipsum','lorem ipsum','',0),(5,'US3','Francesc',120,'open','lorem ipsum','lorem ipsum','',25),(6,'US4','Francesc',95,'open','lorem ipsum','Lorem ipsum','',0),(7,'US5','Francesc ',100,'Open','lorem ipsum','lorem ipsum','',10),(8,'US6','Francesc',120,'open','lorem ipsum','lorem ipsum','',0),(9,'US7','Francesc',100,'open','lorem ipsum','lorem ipsum','',90),(10,' US8 ','Francesc',80,'open','lorem ipsum','lorem ipsum','',0),(11,'US9','Francesc',90,'open','lorem ipsum','lorem ipsum.','',2),(12,'US10','Francesc',100,'open','lorem ipsum','lorem ipsum','',0);
/*!40000 ALTER TABLE `story` ENABLE KEYS */;
UNLOCK TABLES;


--
-- Table structure for table `story`
--

DROP TABLE IF EXISTS `task`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `task` (
  `id` int NOT NULL AUTO_INCREMENT,
  `name` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_spanish_ci NOT NULL,
  `status` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_spanish_ci DEFAULT NULL,
  `criteria` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_spanish_ci DEFAULT NULL,
  `description` varchar(750) CHARACTER SET utf8mb4 COLLATE utf8mb4_spanish_ci NOT NULL,
  `id_story` int NOT NULL,
  PRIMARY KEY (`id`),
  -- UNIQUE KEY `name_UNIQUE` (`name`)
  KEY `fk_Story_has_Task_idx` (`id_story`),
  -- KEY `fk_User_has_Task_User_idx` (`id_user`),
  CONSTRAINT `fk_Story_has_Task` FOREIGN KEY (`id_story`) REFERENCES `story` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=13 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_spanish_ci;

--
-- Table structure for table `user`
--

DROP TABLE IF EXISTS `user`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `user` (
  `id` int NOT NULL AUTO_INCREMENT,
  `username` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_spanish_ci NOT NULL,
  `email` varchar(85) CHARACTER SET utf8mb4 COLLATE utf8mb4_spanish_ci NOT NULL,
  `pass` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_spanish_ci NOT NULL,
  `avatar` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_spanish_ci DEFAULT NULL,
  `rol` enum('USER','ADMIN') CHARACTER SET utf8mb4 COLLATE utf8mb4_spanish_ci NOT NULL DEFAULT 'USER',
  PRIMARY KEY (`id`),
  UNIQUE KEY `username_UNIQUE` (`username`),
  UNIQUE KEY `email_UNIQUE` (`email`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_spanish_ci;


--
-- Dumping data for table `user`
--

LOCK TABLES `user` WRITE;
/*!40000 ALTER TABLE `user` DISABLE KEYS */;
INSERT INTO `user` VALUES (1,'root','root@wipcontrol.com','97c94ebe5d767a353b77f3c0ce2d429741f2e8c99473c3c150e2faa3d14c9da6',NULL,'ADMIN'),(2,'user','user@wipcontrol.com','97c94ebe5d767a353b77f3c0ce2d429741f2e8c99473c3c150e2faa3d14c9da6',NULL,'USER');
/*!40000 ALTER TABLE `user` ENABLE KEYS */;
UNLOCK TABLES;


--
-- Table structure for table `sprint`
--

DROP TABLE IF EXISTS `sprint`;

CREATE TABLE `sprint` (
  `id` int NOT NULL AUTO_INCREMENT,
  `name` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_spanish_ci NOT NULL,
  `status` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_spanish_ci NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `name_UNIQUE` (`name`)
  
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_spanish_ci;



--
-- Dumping data for table `sprint`
--

LOCK TABLES `sprint` WRITE;
/*!40000 ALTER TABLE `sprint` DISABLE KEYS */;
INSERT INTO `sprint` VALUES (1,'Sprint1','OPEN');
/*!40000 ALTER TABLE `sprint` ENABLE KEYS */;
UNLOCK TABLES;




-- Dump completed on 2024-05-10  1:33:25

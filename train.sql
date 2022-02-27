/*
SQLyog Ultimate v12.09 (64 bit)
MySQL - 8.0.19 : Database - exp
*********************************************************************
*/

/*!40101 SET NAMES utf8 */;

/*!40101 SET SQL_MODE=''*/;

/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;
CREATE DATABASE /*!32312 IF NOT EXISTS*/`exp` /*!40100 DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci */ /*!80016 DEFAULT ENCRYPTION='N' */;

USE `exp`;

/*Table structure for table `carriage` */

DROP TABLE IF EXISTS `carriage`;

CREATE TABLE `carriage` (
  `cid` int NOT NULL,
  `ctype` int DEFAULT NULL,
  `seat_number` int DEFAULT NULL,
  PRIMARY KEY (`cid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

/*Data for the table `carriage` */

insert  into `carriage`(`cid`,`ctype`,`seat_number`) values (1,0,21),(2,0,20),(3,0,20),(4,0,20),(5,0,20),(6,1,50),(7,1,50),(8,1,50),(9,1,50),(10,1,50),(11,2,100),(12,2,100),(13,2,100),(14,2,100),(15,2,100),(16,3,50),(17,3,50),(18,3,50),(19,3,50),(20,3,50),(21,0,50);

/*Table structure for table `hibernate_sequence` */

DROP TABLE IF EXISTS `hibernate_sequence`;

CREATE TABLE `hibernate_sequence` (
  `next_val` bigint DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

/*Data for the table `hibernate_sequence` */

insert  into `hibernate_sequence`(`next_val`) values (7);

/*Table structure for table `station` */

DROP TABLE IF EXISTS `station`;

CREATE TABLE `station` (
  `sid` int NOT NULL,
  `tid` varchar(5) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `sname` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `arrive_date` datetime NOT NULL,
  `price` float NOT NULL,
  `mileage` int NOT NULL DEFAULT '0',
  PRIMARY KEY (`sid`,`tid`),
  KEY `停靠站-列车约束` (`tid`),
  CONSTRAINT `停靠站-列车约束` FOREIGN KEY (`tid`) REFERENCES `train` (`tid`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

/*Data for the table `station` */

insert  into `station`(`sid`,`tid`,`sname`,`arrive_date`,`price`,`mileage`) values (1,'D77','徐州','2021-09-14 15:00:00',0,0),(1,'G1','北京','2021-09-13 08:00:00',0,0),(1,'G2','北京','2021-09-13 11:32:05',0,0),(1,'K1153','济南','2021-09-15 19:00:00',0,0),(1,'T34','北京','2021-09-13 08:00:00',0,0),(2,'D77','上海','2021-09-15 15:00:00',200,200),(2,'G1','石家庄','2021-09-13 09:00:00',100,100),(2,'G2','邯郸','2021-09-14 10:10:00',210,150),(2,'K1153','上海','2021-09-15 23:00:00',310,300),(2,'T34','石家庄','2021-09-13 09:00:00',100,100),(3,'D77','福州','2021-09-16 15:00:00',300,200),(3,'G1','济南','2021-09-14 10:00:00',200,200),(3,'G2','济南','2021-09-15 18:10:00',300,300),(3,'K1153','芜湖','2021-09-16 01:00:00',400,400),(3,'T34','济南','2021-09-13 15:00:00',200,200),(4,'G1','徐州','2021-09-14 17:00:00',300,300),(4,'K1153','蚌埠','2021-09-16 02:00:00',450,500),(4,'T34','徐州','2021-09-14 07:00:00',400,390),(5,'G1','上海','2021-09-15 08:00:00',500,200),(5,'K1153','九江','2021-09-16 05:00:00',500,600),(5,'T34','上海','2021-09-14 20:00:00',450,450),(6,'K1153','福州','2021-09-16 10:00:00',600,700),(6,'T34','芜湖','2021-09-15 12:00:00',500,500),(7,'T34','福州','2021-09-17 15:00:00',700,600);

/*Table structure for table `ticket` */

DROP TABLE IF EXISTS `ticket`;

CREATE TABLE `ticket` (
  `usercard` char(18) NOT NULL,
  `tid` varchar(5) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `start_sid` int NOT NULL DEFAULT '0',
  `arrive_sid` int NOT NULL DEFAULT '0',
  `cid` int DEFAULT NULL,
  `seat_sequence` int DEFAULT NULL,
  `price` float DEFAULT NULL,
  `paid` int DEFAULT '0',
  PRIMARY KEY (`usercard`,`tid`,`start_sid`,`arrive_sid`),
  KEY `车票-列车约束` (`tid`),
  CONSTRAINT `用户约束` FOREIGN KEY (`usercard`) REFERENCES `user` (`usercard`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `车票-列车约束` FOREIGN KEY (`tid`) REFERENCES `train` (`tid`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

/*Data for the table `ticket` */

insert  into `ticket`(`usercard`,`tid`,`start_sid`,`arrive_sid`,`cid`,`seat_sequence`,`price`,`paid`) values ('100000000000000001','G1',1,3,1,2,600,0),('100000000000000002','G1',1,3,1,3,600,0),('100000000000000003','G1',1,5,6,1,1000,1);

/*Table structure for table `train` */

DROP TABLE IF EXISTS `train`;

CREATE TABLE `train` (
  `tid` varchar(5) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `type` char(1) DEFAULT NULL,
  `start_station` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL,
  `end_station` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL,
  `start_date` date DEFAULT NULL,
  `end_date` date DEFAULT NULL,
  `state` int DEFAULT '0',
  PRIMARY KEY (`tid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

/*Data for the table `train` */

insert  into `train`(`tid`,`type`,`start_station`,`end_station`,`start_date`,`end_date`,`state`) values ('D77','D','徐州','福州1','2021-09-14','2021-09-16',0),('G1','G','北京','上海','2021-09-13','2021-09-15',0),('G2','G','北京','济南','2021-09-13','2021-09-15',0),('K1153','K','济南','福州','2021-09-15','2021-09-16',0),('T34','T','北京','福州','2021-09-13','2021-09-17',0);

/*Table structure for table `train_carriage` */

DROP TABLE IF EXISTS `train_carriage`;

CREATE TABLE `train_carriage` (
  `cid` int NOT NULL,
  `tid` char(5) DEFAULT NULL,
  `sequence` int DEFAULT NULL,
  PRIMARY KEY (`cid`),
  KEY `列车车厢-列车约束` (`tid`),
  CONSTRAINT `列车车厢-列车约束` FOREIGN KEY (`tid`) REFERENCES `train` (`tid`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `列车车厢-车厢约束` FOREIGN KEY (`cid`) REFERENCES `carriage` (`cid`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

/*Data for the table `train_carriage` */

insert  into `train_carriage`(`cid`,`tid`,`sequence`) values (1,'G1',1),(2,'G2',1),(3,'D77',1),(4,'K1153',1),(5,'T34',1),(6,'G1',2),(7,'G2',2),(8,'D77',2),(9,'K1153',2),(10,'T34',2),(11,'G1',3),(12,'G2',3),(13,'D77',3),(14,'K1153',3),(15,'T34',3),(16,'G1',4),(17,'G2',4),(18,'D77',4),(19,'K1153',4),(20,'T34',4);

/*Table structure for table `user` */

DROP TABLE IF EXISTS `user`;

CREATE TABLE `user` (
  `usercard` char(18) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `uid` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `uname` varchar(5) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL,
  `password` varchar(15) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL,
  `email` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL,
  `phone` char(11) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL,
  `birth` date DEFAULT NULL,
  `identity` int DEFAULT '0',
  `verified` int NOT NULL DEFAULT '0',
  PRIMARY KEY (`usercard`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

/*Data for the table `user` */

insert  into `user`(`usercard`,`uid`,`uname`,`password`,`email`,`phone`,`birth`,`identity`,`verified`) values ('100000000000000000','test','张三','123456','1064658281@qq.com','10000000001','2001-02-01',0,0),('100000000000000001','test11','李四','123456','1064658281@qq.com','10000000000','2001-02-02',0,1),('100000000000000002','test2','王二','123456','1064658281@qq.com','10000000000','2001-02-03',0,1),('100000000000000003','haha','刘翔','123456','1064658281@qq.com','10000000000','1996-02-06',0,1),('100000000000000004','test4','赵五','123456','1064658281@qq.com','10000000005','1995-08-22',0,0),('100000000000000005','test5','钱一','123456','1064658281@qq.com','10000000000','1998-02-07',0,0),('340403200101142610','test','张三','123456','1064658281@qq.com','12345678911',NULL,1,1);

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

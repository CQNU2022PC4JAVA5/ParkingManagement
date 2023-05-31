/*
SQLyog 企业版 - MySQL GUI v7.14 
MySQL - 5.0.96-community-nt : Database - parkingmanagement
*********************************************************************
*/

/*!40101 SET NAMES utf8 */;

/*!40101 SET SQL_MODE=''*/;

/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;

CREATE DATABASE /*!32312 IF NOT EXISTS*/`parkingmanagement` /*!40100 DEFAULT CHARACTER SET latin1 */;

USE `parkingmanagement`;

/*Table structure for table `account` */

DROP TABLE IF EXISTS `account`;

CREATE TABLE `account` (
  `account` char(16) NOT NULL,
  `password` char(32) default NULL COMMENT '存储到数据库使用MD5加密',
  PRIMARY KEY  (`account`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

/*Data for the table `account` */

insert  into `account`(`account`,`password`) values ('admin','21232f297a57a5a743894a0e4a801fc3');

/*Table structure for table `fee` */

DROP TABLE IF EXISTS `fee`;

CREATE TABLE `fee` (
  `freetime` int(11) default NULL,
  `fisttime` int(11) default NULL,
  `firstfee` double default NULL,
  `secondtime` int(11) default NULL,
  `secondfee` double default NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

/*Data for the table `fee` */

/*Table structure for table `spots` */

DROP TABLE IF EXISTS `spots`;

CREATE TABLE `spots` (
  `id` int(8) NOT NULL,
  `status` char(4) default NULL,
  `no` char(16) default NULL,
  `time` timestamp NOT NULL default CURRENT_TIMESTAMP on update CURRENT_TIMESTAMP,
  PRIMARY KEY  (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

/*Data for the table `spots` */

/*Table structure for table `token` */

DROP TABLE IF EXISTS `token`;

CREATE TABLE `token` (
  `account` char(16) NOT NULL,
  `token` char(32) default NULL,
  `expire` timestamp NULL default NULL,
  PRIMARY KEY  (`account`),
  CONSTRAINT `FK_token` FOREIGN KEY (`account`) REFERENCES `account` (`account`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

/*Data for the table `token` */

insert  into `token`(`account`,`token`,`expire`) values ('admin','HZ7ST9ZZ5NIGNNYSUFEVNXZF3PMDKB85','2023-05-30 21:37:41');

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;

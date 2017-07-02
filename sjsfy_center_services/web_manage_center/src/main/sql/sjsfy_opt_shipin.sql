/*
Navicat MySQL Data Transfer

Source Server         : localhost
Source Server Version : 50553
Source Host           : localhost:3306
Source Database       : sjsfy_opt_shipin

Target Server Type    : MYSQL
Target Server Version : 50553
File Encoding         : 65001

Date: 2016-12-11 18:57:22
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for `sjsfy_kelu_deviceinfo`
-- ----------------------------
DROP TABLE IF EXISTS `sjsfy_kelu_deviceinfo`;
CREATE TABLE `sjsfy_kelu_deviceinfo` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `device_host` varchar(255) NOT NULL,
  `device_desc` varchar(255) NOT NULL,
  `device_type` int(11) NOT NULL,
  `parent_device_id` int(11) NOT NULL,
  `status` int(11) NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=13 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of sjsfy_kelu_deviceinfo
-- ----------------------------
INSERT INTO `sjsfy_kelu_deviceinfo` VALUES ('1', '', '石景山法院', '1', '0', '1');

-- ----------------------------
-- Table structure for `sjsfy_kelu_keluinfo`
-- ----------------------------
DROP TABLE IF EXISTS `sjsfy_kelu_keluinfo`;
CREATE TABLE `sjsfy_kelu_keluinfo` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `room_host` varchar(255) NOT NULL DEFAULT '',
  `device_host` varchar(255) NOT NULL,
  `luxiang_start` varchar(255) NOT NULL,
  `luxiang_qujian` text NOT NULL,
  `luxiang_end` varchar(255) NOT NULL DEFAULT '',
  `kelu_start` varchar(255) NOT NULL DEFAULT '',
  `kelu_end` varchar(255) NOT NULL DEFAULT '',
  `faguan` varchar(255) NOT NULL DEFAULT '',
  `anjianbianhao` text NOT NULL,
  `anyou` text NOT NULL,
  `status` int(11) NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=58 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of sjsfy_kelu_keluinfo
-- ----------------------------
INSERT INTO `sjsfy_kelu_keluinfo` VALUES ('53', '192.168.1.248', '192.168.1.64', '20161211 09:18:36', '20161211 09:18:36-20161211 09:18:38,20161211 09:18:39-20161211 09:18:45', '20161211 09:18:45', '', '', 'ly', '123', '123123', '4');

-- ----------------------------
-- Table structure for `sjsfy_kelu_roominfo`
-- ----------------------------
DROP TABLE IF EXISTS `sjsfy_kelu_roominfo`;
CREATE TABLE `sjsfy_kelu_roominfo` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `room_host` varchar(255) NOT NULL DEFAULT '',
  `room_desc` varchar(255) NOT NULL DEFAULT '',
  `room_type` int(11) NOT NULL DEFAULT '0',
  `parent_room_id` int(11) NOT NULL DEFAULT '0',
  `device_host` varchar(255) NOT NULL DEFAULT '',
  `device_username` varchar(255) NOT NULL DEFAULT '',
  `device_password` varchar(255) NOT NULL DEFAULT '',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of sjsfy_kelu_roominfo
-- ----------------------------
INSERT INTO `sjsfy_kelu_roominfo` VALUES ('1', '', '石景山法院', '1', '0', '192.168.1.64', 'admin', 'admin123');

-- ----------------------------
-- Table structure for `sjsfy_kelu_userinfo`
-- ----------------------------
DROP TABLE IF EXISTS `sjsfy_kelu_userinfo`;
CREATE TABLE `sjsfy_kelu_userinfo` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `username` varchar(255) NOT NULL DEFAULT '',
  `password` varchar(255) DEFAULT '',
  `showname` varchar(255) DEFAULT '',
  `status` int(11) NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of sjsfy_kelu_userinfo
-- ----------------------------
INSERT INTO `sjsfy_kelu_userinfo` VALUES ('1', 'admin', 'admin', ' 管理', '1');
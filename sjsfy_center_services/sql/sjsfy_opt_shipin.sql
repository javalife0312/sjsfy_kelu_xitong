/*
Navicat MySQL Data Transfer

Source Server         : localhost
Source Server Version : 50553
Source Host           : localhost:3306
Source Database       : sjsfy_opt_shipin

Target Server Type    : MYSQL
Target Server Version : 50553
File Encoding         : 65001

Date: 2017-06-24 08:38:04
*/

SET FOREIGN_KEY_CHECKS=0;

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
  `filename` varchar(500) NOT NULL DEFAULT '',
  `kelu_dir` varchar(500) NOT NULL DEFAULT '',
  `kaitingshijian` varchar(255) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=163 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of sjsfy_kelu_keluinfo
-- ----------------------------
INSERT INTO `sjsfy_kelu_keluinfo` VALUES ('151', '192.168.145.1', '192.168.1.64', '20170605 08:43:20', '20170605 08:43:20-20170605 08:44:42', '20170605 08:44:42', '', '', '李颖', '1231', '123123', '1', '', '', '2017-06-05');
INSERT INTO `sjsfy_kelu_keluinfo` VALUES ('152', '192.168.145.1', '192.168.1.64', '20170605 08:44:47', '20170605 08:44:47-20170605 08:45:12', '20170605 08:45:12', '', '', '李颖', '1231231', '23123123', '-1', '', '', '2017-06-05');
INSERT INTO `sjsfy_kelu_keluinfo` VALUES ('153', '192.168.145.1', '192.168.1.64', '20170605 08:45:19', '20170605 08:45:19-20170605 08:46:55', '20170605 08:46:55', '', '', '李颖', '123123', '2312', '-1', '', '', '2017-06-05');

-- ----------------------------
-- Table structure for `sjsfy_kelu_manageinfo`
-- ----------------------------
DROP TABLE IF EXISTS `sjsfy_kelu_manageinfo`;
CREATE TABLE `sjsfy_kelu_manageinfo` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `fating_desc` varchar(255) NOT NULL DEFAULT '',
  `faguan_code` varchar(255) NOT NULL DEFAULT '',
  `faguan_name` varchar(255) NOT NULL DEFAULT '',
  `shexiangji_ip` varchar(255) NOT NULL DEFAULT '0',
  `shexiangji_desc` varchar(255) NOT NULL,
  `shexiangji_username` varchar(255) NOT NULL,
  `shexiangji_password` varchar(255) NOT NULL,
  `shexiangji_zhuangtai` int(11) NOT NULL,
  `diannaozhuji_ip` varchar(255) NOT NULL,
  `diannaozhuji_panfu` varchar(255) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of sjsfy_kelu_manageinfo
-- ----------------------------
INSERT INTO `sjsfy_kelu_manageinfo` VALUES ('2', '石景山法院第一法庭', 'liying', '李颖', '192.168.1.64', '测试摄像机', 'admin', 'admin123456', '1', '192.168.145.1', 'D:');

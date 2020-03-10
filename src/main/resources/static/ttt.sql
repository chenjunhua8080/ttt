# Host: 127.0.0.1  (Version 5.7.26)
# Date: 2020-03-10 18:55:35
# Generator: MySQL-Front 6.1  (Build 1.26)


#
# Structure for table "dict"
#

DROP TABLE IF EXISTS `dict`;
CREATE TABLE `dict` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `type` varchar(50) DEFAULT NULL COMMENT '类型',
  `dict_key` int(11) DEFAULT NULL COMMENT '键',
  `dict_value` varchar(255) DEFAULT NULL COMMENT '值',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  `data_level` int(1) DEFAULT '1' COMMENT '数据级别 0删除 1正常',
  PRIMARY KEY (`id`)
) ENGINE=MyISAM AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4;

#
# Data for table "dict"
#

/*!40000 ALTER TABLE `dict` DISABLE KEYS */;
INSERT INTO `dict` VALUES (1,'sex',1,'男','2020-03-03 22:47:18','2020-03-03 22:47:55',1),(2,'sex',2,'女','2020-03-03 22:47:26','2020-03-03 22:47:56',1);
/*!40000 ALTER TABLE `dict` ENABLE KEYS */;

#
# Structure for table "friend"
#

DROP TABLE IF EXISTS `friend`;
CREATE TABLE `friend` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `sender` int(11) DEFAULT NULL COMMENT '发送者',
  `recipient` int(11) DEFAULT NULL COMMENT '接收者',
  `content` varchar(255) DEFAULT NULL COMMENT '打招呼内容',
  `status` int(1) DEFAULT NULL COMMENT '好友状态，字典[friend_status]',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  `data_level` int(1) DEFAULT '1' COMMENT '数据级别 0无效 1有效',
  PRIMARY KEY (`id`),
  KEY `IDX_message_sender` (`sender`) USING BTREE,
  KEY `IDX_message_recipient` (`recipient`) USING BTREE
) ENGINE=MyISAM DEFAULT CHARSET=utf8mb4;

#
# Data for table "friend"
#


#
# Structure for table "message"
#

DROP TABLE IF EXISTS `message`;
CREATE TABLE `message` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `sender` int(11) DEFAULT NULL COMMENT '发送者',
  `recipient` int(11) DEFAULT NULL COMMENT '接收者',
  `message_type` int(1) DEFAULT NULL COMMENT '消息类型，字典[message_type]',
  `content` varchar(255) DEFAULT NULL COMMENT '内容',
  `read` int(1) DEFAULT NULL COMMENT '已读',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  `data_level` int(1) DEFAULT '1' COMMENT '数据级别 0无效 1有效',
  PRIMARY KEY (`id`),
  KEY `IDX_message_sender` (`sender`) USING BTREE,
  KEY `IDX_message_recipient` (`recipient`) USING BTREE
) ENGINE=MyISAM DEFAULT CHARSET=utf8mb4;

#
# Data for table "message"
#


#
# Structure for table "user"
#

DROP TABLE IF EXISTS `user`;
CREATE TABLE `user` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `username` varchar(50) DEFAULT NULL COMMENT '用户名',
  `nickname` varchar(50) DEFAULT NULL COMMENT '昵称',
  `sex` int(1) DEFAULT NULL COMMENT '性别，字典[sex]',
  `birthday` date DEFAULT NULL COMMENT '生日',
  `avatar` varchar(255) DEFAULT NULL COMMENT '头像',
  `phone` varchar(11) DEFAULT NULL COMMENT '手机号',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  `data_level` int(1) DEFAULT '1' COMMENT '数据级别 0无效 1正常',
  PRIMARY KEY (`id`),
  KEY `IDX_user_birthday` (`birthday`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COMMENT='用户表';

#
# Data for table "user"
#

INSERT INTO `user` VALUES (1,'qqq','张三',1,'1990-08-08',NULL,'13413527000','2020-02-27 17:52:59','2020-02-28 11:03:41',1),(2,'123','123',NULL,NULL,NULL,NULL,'2020-02-28 17:57:31',NULL,1);

# Host: 127.0.0.1  (Version 5.7.26)
# Date: 2020-03-19 18:56:43
# Generator: MySQL-Front 6.1  (Build 1.26)


#
# Structure for table "address"
#

DROP TABLE IF EXISTS `address`;
CREATE TABLE `address` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `user_id` int(11) DEFAULT NULL COMMENT '用户id',
  `lng` varchar(50) DEFAULT NULL COMMENT '经度',
  `lat` varchar(50) DEFAULT NULL COMMENT '维度',
  `province` varchar(50) DEFAULT NULL COMMENT '省',
  `city` varchar(50) DEFAULT NULL COMMENT '市',
  `area` varchar(50) DEFAULT NULL COMMENT '区',
  `street` varchar(50) DEFAULT NULL COMMENT '街道',
  `detail` varchar(255) DEFAULT NULL COMMENT '详细地址',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '修改时间',
  `data_level` int(1) DEFAULT '1' COMMENT '数据级别 0删除 1正常',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC COMMENT='地址表';

#
# Data for table "address"
#


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
) ENGINE=MyISAM AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COMMENT='字典表';

#
# Data for table "dict"
#

/*!40000 ALTER TABLE `dict` DISABLE KEYS */;
INSERT INTO `dict` VALUES (1,'sex',1,'男','2020-03-03 22:47:18','2020-03-03 22:47:55',1),(2,'sex',2,'女','2020-03-03 22:47:26','2020-03-03 22:47:56',1),(3,'sex',0,'未知','2020-03-11 11:13:38',NULL,1);
/*!40000 ALTER TABLE `dict` ENABLE KEYS */;

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
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='消息表';

#
# Data for table "message"
#


#
# Structure for table "pair"
#

DROP TABLE IF EXISTS `pair`;
CREATE TABLE `pair` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `sender` int(11) DEFAULT NULL COMMENT '发送者',
  `recipient` int(11) DEFAULT NULL COMMENT '接收者',
  `content` varchar(20) DEFAULT NULL COMMENT '内容',
  `status` int(1) DEFAULT '0' COMMENT '状态：0发送1同意2拒绝',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '修改时间',
  `data_level` int(1) DEFAULT '1' COMMENT '数据级别 0无效 1有效',
  PRIMARY KEY (`id`),
  KEY `IDX_message_recipient` (`recipient`) USING BTREE,
  KEY `IDX_message_sender` (`sender`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC COMMENT='配对表';

#
# Data for table "pair"
#

INSERT INTO `pair` VALUES (1,9,10,'hello',0,'2020-03-17 20:29:26','2020-03-17 21:40:12',1);

#
# Structure for table "user"
#

DROP TABLE IF EXISTS `user`;
CREATE TABLE `user` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `open_id` varchar(255) DEFAULT NULL COMMENT 'open_id',
  `username` varchar(50) DEFAULT NULL COMMENT '用户名',
  `nickname` varchar(50) DEFAULT NULL COMMENT '昵称',
  `sex` int(1) DEFAULT '0' COMMENT '性别，字典[sex]',
  `birthday` date DEFAULT NULL COMMENT '生日',
  `avatar` varchar(255) DEFAULT NULL COMMENT '头像',
  `phone` varchar(11) DEFAULT NULL COMMENT '手机号',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  `data_level` int(1) DEFAULT '1' COMMENT '数据级别 0无效 1正常',
  PRIMARY KEY (`id`),
  KEY `IDX_user_birthday` (`birthday`) USING BTREE,
  KEY `IDX_user_sex` (`sex`)
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8mb4 COMMENT='用户表';

#
# Data for table "user"
#

INSERT INTO `user` VALUES (1,NULL,'qqq','张三',2,'1999-01-01',NULL,'13413527000','2020-02-27 17:52:59','2020-03-16 21:26:29',1),(2,NULL,'123','123',2,'2005-01-01',NULL,NULL,'2020-02-28 17:57:31','2020-03-16 22:25:19',1),(9,'FdYMLIr37LxJ5tNA','101d589da7834e2faa151402e52ff5b2','小哥哥',1,'2005-01-01','http://192.168.1.76/ttt/1584362492420.png','13413527259','2020-03-12 15:49:45','2020-03-16 22:41:19',1),(10,'7i3383EHLN9azcS9','044613f6c6ac4ce7adc997e8a0144f3a','一只可甜可甜',2,NULL,'http://192.168.1.76/ttt/1584360900387.png',NULL,'2020-03-16 20:12:42','2020-03-16 22:25:10',1);

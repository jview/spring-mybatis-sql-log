/*
Navicat MySQL Data Transfer

Source Server         : local-edu_demo
Source Server Version : 50525
Source Host           : localhost:3306
Source Database       : msgs

Target Server Type    : MYSQL
Target Server Version : 50525
File Encoding         : 65001

Date: 2016-08-01 09:51:02
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for tf_log_db
-- ----------------------------
DROP TABLE IF EXISTS `tf_log_db`;
CREATE TABLE `tf_log_db` (
  `cid` decimal(20,0) NOT NULL,
  `project_id` decimal(19,0) DEFAULT NULL COMMENT '产品id',
  `service_id` decimal(19,0) DEFAULT NULL COMMENT '服务器id',
  `user_id` decimal(19,0) DEFAULT NULL COMMENT '用户id',
  `user_name` varchar(64) DEFAULT NULL COMMENT '用户账号',
  `model_id` decimal(19,0) DEFAULT NULL COMMENT '模块名对应的id，添加日志时自动检查\nmodel表中是否没有，如果没有就自动在model表中加数据',
  `create_date` datetime DEFAULT NULL COMMENT '创建时间',
  `log_level` int(11) DEFAULT NULL COMMENT '日志级别',
  `title` varchar(1000) DEFAULT NULL COMMENT '标题',
  `json_param` varchar(2000) DEFAULT NULL COMMENT '参数json，当时操作功能的参数',
  `json_param_opt_id` decimal(19,0) DEFAULT NULL COMMENT '参数optId',
  `json_ret` varchar(4000) DEFAULT NULL COMMENT 'json返回报文',
  `json_ret_opt_id` decimal(19,0) DEFAULT NULL COMMENT '返回报文opt_id',
  `flag` varchar(100) NOT NULL COMMENT '返回标志0失败，1成功，2异常',
  `descs` varchar(2000) DEFAULT NULL COMMENT '描述信息',
  `run_time` int(11) DEFAULT NULL COMMENT '运行时间,毫秒',
  `remark` varchar(2000) DEFAULT NULL COMMENT '备注',
  `ip` varchar(200) DEFAULT NULL,
  `author` varchar(100) DEFAULT NULL,
  `oper_type` varchar(100) DEFAULT NULL,
  `thread_id` decimal(15,0) DEFAULT NULL,
  `calls` varchar(200) DEFAULT NULL,
  `rows` int(11) DEFAULT '0' COMMENT '数据行数',
  `rows1` int(11) DEFAULT '0',
  PRIMARY KEY (`cid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='用户操作记录表';

-- ----------------------------
-- Records of tf_log_db
-- ----------------------------

-- ----------------------------
-- Table structure for tf_log_msg
-- ----------------------------
DROP TABLE IF EXISTS `tf_log_msg`;
CREATE TABLE `tf_log_msg` (
  `cid` decimal(19,0) NOT NULL,
  `msg_code` varchar(100) NOT NULL COMMENT '?ʼ????룬????lookup,\n?ʼ???tf_send_mail_code??\n??????tf_send_sms_code,\n??????tf_send_push_code,\nϵͳ??Ϣ??tf_send_sys_code',
  `project_id` decimal(19,0) DEFAULT NULL COMMENT '??Ʒid',
  `user_id` decimal(19,0) DEFAULT '0' COMMENT '????û???û?????user_id=0',
  `msg_type` int(11) NOT NULL COMMENT '??Ϣ???ͣ?1???ţ?2?ʼ???3ϵͳ??Ϣ??4??????Ϣ',
  `send_time` datetime DEFAULT NULL COMMENT '????ʱ??',
  `send_flag` int(11) DEFAULT NULL COMMENT '????״̬??0???Ϳ?ʼ??1ȫ?????ͳɹ???3?????쳣',
  `send_type` int(11) DEFAULT NULL COMMENT '?ʼ?????????:1һ?????ͣ?2?ֿ?????(?ֿ????ͣ?ȫ?????ռ??˷????????ֳ??͡????ͣ?,3????',
  `mail_count` int(11) DEFAULT NULL COMMENT '??????????ȥ???ظ???',
  `send_count` int(11) DEFAULT NULL COMMENT '???ͳɹ????ʼ?????',
  `tos` varchar(4000) DEFAULT NULL COMMENT '?ռ??˻??ֻ???',
  `ccs` varchar(2000) DEFAULT NULL COMMENT '??????',
  `bccs` varchar(2000) DEFAULT NULL COMMENT '??????',
  `fail_mails` varchar(3000) DEFAULT NULL COMMENT 'ʧ?ܵ????䣬????Ҫʱ???˹???ʧ?ܵ??ʼ??????ط????ط???ʽ??????һ?????糭????Ȼ????????',
  `exceptions` varchar(3000) DEFAULT NULL COMMENT 'ʧ?ܻ??쳣??Ϣ???磺??¼??Щ?ʼ?????ʧ??',
  `title` varchar(1000) DEFAULT NULL COMMENT '?ʼ?????',
  `content` varchar(4000) DEFAULT NULL COMMENT '?ʼ????ģ?һ??ֻ??һ???ݣ???????????4000?????Զ?һЩ????????????1000',
  `attach_path` varchar(2000) DEFAULT NULL COMMENT '?ʼ?????,?????ԡ?,??????',
  `end_time` datetime DEFAULT NULL COMMENT '????ʱ??',
  `create_date` datetime DEFAULT NULL,
  `create_user` decimal(19,0) DEFAULT NULL,
  `template` varchar(100) DEFAULT NULL,
  `run_time` int(11) DEFAULT NULL,
  PRIMARY KEY (`cid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='??Ϣ???ͼ?¼??֧?ֶ??š??ʼ??????͡?ϵͳ??Ϣ\nÿ??ֻ??һ?ַ?ʽ????????Ҫ?ɷֶ??η??͡?';

-- ----------------------------
-- Records of tf_log_msg
-- ----------------------------
INSERT INTO `tf_log_msg` VALUES ('2', 'test', null, null, '2', '2016-07-10 09:21:05', '1', '2', '1', '1', 'ycstest@126.com', null, null, null, 'Mail server connection failed; nested exception is javax.mail.MessagingException: Could not connect to SMTP host: smtp.126.com, port: 465, response: 421. Failed messages: javax.mail.MessagingException: Could not connect to SMTP host: smtp.126.com, port: 465, response: 421', 'test', 'test', null, '2016-07-10 09:16:07', '2016-07-09 00:00:00', null, null, '6531');
INSERT INTO `tf_log_msg` VALUES ('3', 'test', null, null, '2', '2016-07-10 09:21:05', '1', '2', '1', '1', 'ycstest@126.com', null, null, null, null, 'test', 'test', null, '2016-07-10 09:17:07', '2016-07-09 00:00:00', null, null, '6500');
INSERT INTO `tf_log_msg` VALUES ('4', 'test', null, null, '2', '2016-07-10 09:21:05', '1', '2', '1', '1', 'ycstest@126.com', null, null, null, null, 'test', 'test', null, '2016-07-10 09:17:07', '2016-07-09 00:00:00', null, null, '6468');
INSERT INTO `tf_log_msg` VALUES ('5', 'test', null, null, '2', '2016-07-10 09:21:05', '1', '2', '1', '1', 'ycstest@126.com', null, null, null, null, 'test', 'test', null, '2016-07-10 09:17:07', '2016-07-09 00:00:00', null, null, '6453');
INSERT INTO `tf_log_msg` VALUES ('6', 'test', null, null, '2', '2016-07-10 09:21:05', '1', '2', '1', '1', 'ycstest@126.com', null, null, null, null, 'test', 'test', null, '2016-07-10 09:17:08', '2016-07-09 00:00:00', null, null, '7531');
INSERT INTO `tf_log_msg` VALUES ('7', 'test', null, null, '2', '2016-07-10 09:21:05', '1', '2', '1', '1', 'ycstest@126.com', null, null, null, null, 'test', 'test', null, '2016-07-10 09:17:07', '2016-07-09 00:00:00', null, null, '7484');
INSERT INTO `tf_log_msg` VALUES ('8', 'test', null, null, '2', '2016-07-10 09:21:05', '1', '2', '1', '1', 'ycstest@126.com', null, null, null, null, 'test', 'test', null, '2016-07-10 09:17:07', '2016-07-09 00:00:00', null, null, '6281');
INSERT INTO `tf_log_msg` VALUES ('9', 'test', null, null, '2', '2016-07-10 09:21:05', '1', '2', '1', '1', 'ycstest@126.com', null, null, null, 'Mail server connection failed; nested exception is javax.mail.MessagingException: Could not connect to SMTP host: smtp.126.com, port: 465, response: 421. Failed messages: javax.mail.MessagingException: Could not connect to SMTP host: smtp.126.com, port: 465, response: 421', 'test', 'test', null, '2016-07-10 09:16:06', '2016-07-09 00:00:00', null, null, '7375');
INSERT INTO `tf_log_msg` VALUES ('10', 'test', null, null, '2', '2016-07-10 09:21:05', '1', '2', '1', '1', 'ycstest@126.com', null, null, null, null, 'test', 'test', null, '2016-07-10 09:17:08', '2016-07-09 00:00:00', null, null, '7218');
INSERT INTO `tf_log_msg` VALUES ('11', 'test', null, null, '2', '2016-07-10 09:21:05', '1', '2', '1', '1', 'ycstest@126.com', null, null, null, null, 'test', 'test', null, '2016-07-10 09:17:07', '2016-07-09 00:00:00', null, null, '7328');

-- ----------------------------
-- Table structure for tf_log_msg_detail
-- ----------------------------
DROP TABLE IF EXISTS `tf_log_msg_detail`;
CREATE TABLE `tf_log_msg_detail` (
  `cid` decimal(19,0) NOT NULL,
  `log_msg_id` decimal(19,0) DEFAULT NULL,
  `tos` varchar(4000) DEFAULT NULL COMMENT '?ռ???',
  `send_date` datetime DEFAULT NULL COMMENT '????ʱ??',
  `remark` varchar(2000) DEFAULT NULL COMMENT '??ע',
  `run_time` int(11) DEFAULT NULL,
  PRIMARY KEY (`cid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='??Ϣ??????ϸ\nͨ?????Ʋ?????Ĭ?Ϲرգ????Ƿ???????????ܣ?????????̫??\nһ?㿪????????1??????Ӧ?øĻ?ȥ';

-- ----------------------------
-- Records of tf_log_msg_detail
-- ----------------------------
INSERT INTO `tf_log_msg_detail` VALUES ('52', '7', 'ycstest@126.com', '2016-07-10 00:00:00', null, '1031');
INSERT INTO `tf_log_msg_detail` VALUES ('53', '2', 'ycstest@126.com', '2016-07-10 00:00:00', null, '1234');
INSERT INTO `tf_log_msg_detail` VALUES ('54', '4', 'ycstest@126.com', '2016-07-10 00:00:00', null, '1156');
INSERT INTO `tf_log_msg_detail` VALUES ('55', '6', 'ycstest@126.com', '2016-07-10 00:00:00', null, '1062');
INSERT INTO `tf_log_msg_detail` VALUES ('56', '9', 'ycstest@126.com', '2016-07-10 00:00:00', null, '1609');
INSERT INTO `tf_log_msg_detail` VALUES ('57', '5', 'ycstest@126.com', '2016-07-10 00:00:00', null, '1125');
INSERT INTO `tf_log_msg_detail` VALUES ('58', '11', 'ycstest@126.com', '2016-07-10 00:00:00', null, '1609');
INSERT INTO `tf_log_msg_detail` VALUES ('59', '3', 'ycstest@126.com', '2016-07-10 00:00:00', null, '1828');
INSERT INTO `tf_log_msg_detail` VALUES ('60', '8', 'ycstest@126.com', '2016-07-10 00:00:00', null, '1844');
INSERT INTO `tf_log_msg_detail` VALUES ('61', '10', 'ycstest@126.com', '2016-07-10 00:00:00', null, '1578');
INSERT INTO `tf_log_msg_detail` VALUES ('62', '5', 'ycstest@126.com', '2016-07-10 00:00:00', null, '2656');
INSERT INTO `tf_log_msg_detail` VALUES ('63', '7', 'ycstest@126.com', '2016-07-10 00:00:00', null, '2578');
INSERT INTO `tf_log_msg_detail` VALUES ('64', '6', 'ycstest@126.com', '2016-07-10 00:00:00', null, '2860');
INSERT INTO `tf_log_msg_detail` VALUES ('65', '9', 'ycstest@126.com', '2016-07-10 00:00:00', null, '2719');
INSERT INTO `tf_log_msg_detail` VALUES ('66', '4', 'ycstest@126.com', '2016-07-10 00:00:00', null, '2922');
INSERT INTO `tf_log_msg_detail` VALUES ('67', '10', 'ycstest@126.com', '2016-07-10 00:00:00', null, '2703');
INSERT INTO `tf_log_msg_detail` VALUES ('68', '2', 'ycstest@126.com', '2016-07-10 00:00:00', null, '3141');
INSERT INTO `tf_log_msg_detail` VALUES ('69', '8', 'ycstest@126.com', '2016-07-10 00:00:00', null, '2828');
INSERT INTO `tf_log_msg_detail` VALUES ('70', '11', 'ycstest@126.com', '2016-07-10 00:00:00', null, '2750');
INSERT INTO `tf_log_msg_detail` VALUES ('71', '3', 'ycstest@126.com', '2016-07-10 00:00:00', null, '3016');
INSERT INTO `tf_log_msg_detail` VALUES ('72', '3', 'ycstest@126.com', '2016-07-10 00:00:00', null, '6390');
INSERT INTO `tf_log_msg_detail` VALUES ('73', '2', 'ycstest@126.com', '2016-07-10 00:00:00', null, '6437');
INSERT INTO `tf_log_msg_detail` VALUES ('74', '4', 'ycstest@126.com', '2016-07-10 00:00:00', null, '6375');
INSERT INTO `tf_log_msg_detail` VALUES ('75', '5', 'ycstest@126.com', '2016-07-10 00:00:00', null, '6328');
INSERT INTO `tf_log_msg_detail` VALUES ('76', '8', 'ycstest@126.com', '2016-07-10 00:00:00', null, '6172');
INSERT INTO `tf_log_msg_detail` VALUES ('77', '10', 'ycstest@126.com', '2016-07-10 00:00:00', null, '7187');
INSERT INTO `tf_log_msg_detail` VALUES ('78', '6', 'ycstest@126.com', '2016-07-10 00:00:00', null, '7469');
INSERT INTO `tf_log_msg_detail` VALUES ('79', '11', 'ycstest@126.com', '2016-07-10 00:00:00', null, '7281');
INSERT INTO `tf_log_msg_detail` VALUES ('80', '9', 'ycstest@126.com', '2016-07-10 00:00:00', null, '7344');
INSERT INTO `tf_log_msg_detail` VALUES ('81', '7', 'ycstest@126.com', '2016-07-10 00:00:00', null, '7375');

-- ----------------------------
-- Table structure for tf_model
-- ----------------------------
DROP TABLE IF EXISTS `tf_model`;
CREATE TABLE `tf_model` (
  `cid` decimal(19,0) NOT NULL,
  `func_code` varchar(100) DEFAULT NULL COMMENT '方法名',
  `class_name` varchar(100) DEFAULT NULL COMMENT '类名',
  `package_name` varchar(100) DEFAULT NULL COMMENT '包名',
  `create_date` datetime DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`cid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='类功能模块表，启动时自动将数据加载到hashmap中，供日志操作使用';

-- ----------------------------
-- Records of tf_model
-- ----------------------------
INSERT INTO `tf_model` VALUES ('3', 'selectByExample_COUNT', 'LogMsgMapper', 'ai.yc.fwork.basedata.mapper', '2016-07-19 00:00:00');
INSERT INTO `tf_model` VALUES ('4', 'selectByExample', 'ModelMapper', 'ai.yc.fwork.basedata.mapper', '2016-07-19 00:00:00');
INSERT INTO `tf_model` VALUES ('5', 'selectByExample', 'LogMsgMapper', 'ai.yc.fwork.basedata.mapper', '2016-07-19 00:00:00');
INSERT INTO `tf_model` VALUES ('6', 'findPageByLogMsg', 'LogMsgService', 'ai.yc.fwork.basedata.service.impl', '2016-07-19 00:00:00');
INSERT INTO `tf_model` VALUES ('7', 'selectByExample', 'ModelMapper', 'ai.yc.fwork.basedata.mapper', '2016-07-19 00:00:00');
INSERT INTO `tf_model` VALUES ('8', 'insertModel!selectKey', 'ModelMapper', 'ai.yc.fwork.basedata.mapper', '2016-07-19 00:00:00');
INSERT INTO `tf_model` VALUES ('9', 'insertModel!selectKey', 'ModelMapper', 'ai.yc.fwork.basedata.mapper', '2016-07-19 00:00:00');
INSERT INTO `tf_model` VALUES ('10', 'insertModel', 'ModelMapper', 'ai.yc.fwork.basedata.mapper', '2016-07-19 00:00:00');
INSERT INTO `tf_model` VALUES ('11', 'insertModel', 'ModelMapper', 'ai.yc.fwork.basedata.mapper', '2016-07-19 00:00:00');
INSERT INTO `tf_model` VALUES ('12', 'insertModel!selectKey', 'ModelMapper', 'ai.yc.fwork.basedata.mapper', '2016-07-19 00:00:00');

-- ----------------------------
-- Table structure for tf_sequence
-- ----------------------------
DROP TABLE IF EXISTS `tf_sequence`;
CREATE TABLE `tf_sequence` (
  `name` varchar(50) NOT NULL,
  `current_value` int(11) NOT NULL,
  `increment` int(11) NOT NULL DEFAULT '1'
) ENGINE=MyISAM DEFAULT CHARSET=utf8 COMMENT='???б��[table_name]';

-- ----------------------------
-- Records of tf_sequence
-- ----------------------------
INSERT INTO `tf_sequence` VALUES ('_tf_logMsgDetail_cid_seq', '81', '1');
INSERT INTO `tf_sequence` VALUES ('_tf_logMsg_cid_seq', '21', '1');
INSERT INTO `tf_sequence` VALUES ('_tf_logDb_cid_seq', '405', '1');
INSERT INTO `tf_sequence` VALUES ('_tf_model_cid_seq', '12', '1');

-- ----------------------------
-- View structure for v_log_db
-- ----------------------------
DROP VIEW IF EXISTS `v_log_db`;
CREATE ALGORITHM=UNDEFINED DEFINER=`root`@`%` SQL SECURITY DEFINER VIEW `v_log_db` AS select `log`.`cid` AS `cid`,`m`.`class_name` AS `class_name`,`m`.`func_code` AS `func_code`,`log`.`user_id` AS `user_id`,`log`.`user_name` AS `user_name`,`log`.`model_id` AS `model_id`,`log`.`create_date` AS `create_date`,`log`.`log_level` AS `log_level`,`log`.`thread_id` AS `thread_id`,`log`.`title` AS `title`,`log`.`author` AS `author`,`log`.`calls` AS `calls`,`log`.`descs` AS `descs`,`log`.`json_param` AS `json_param`,`log`.`json_param_opt_id` AS `json_param_opt_id`,`log`.`json_ret` AS `json_ret`,`log`.`json_ret_opt_id` AS `json_ret_opt_id`,`log`.`flag` AS `flag`,`log`.`run_time` AS `run_time`,`log`.`rows` AS `rows`,`log`.`remark` AS `remark`,`log`.`ip` AS `ip`,`log`.`oper_type` AS `oper_type`,`m`.`package_name` AS `package_name` from (`tf_log_db` `log` join `tf_model` `m`) where (`log`.`model_id` = `m`.`cid`) ;

-- ----------------------------
-- Function structure for currval
-- ----------------------------
DROP FUNCTION IF EXISTS `currval`;
DELIMITER ;;
CREATE DEFINER=`root`@`localhost` FUNCTION `currval`(seq_name VARCHAR(50)) RETURNS int(11)
BEGIN  
  
DECLARE _v INTEGER;  
  
SET _v = 0;  
  
SELECT current_value INTO _v FROM tf_sequence WHERE NAME = seq_name;  
  
RETURN _v;  
  
END
;;
DELIMITER ;

-- ----------------------------
-- Function structure for _nextval
-- ----------------------------
DROP FUNCTION IF EXISTS `_nextval`;
DELIMITER ;;
CREATE DEFINER=`root`@`localhost` FUNCTION `_nextval`(seq_name VARCHAR(50)) RETURNS int(11)
    DETERMINISTIC
BEGIN  
  
DECLARE  v NUMERIC;
UPDATE tf_sequence SET current_value = current_value + increment WHERE NAME = seq_name;  

set v=currval(seq_name);
/*
if v=0 then
	INSERT INTO tf_sequence (name, current_value, increment) VALUES  (seq_name, 1, 1) ;
	return 1;
ELSE 
	return v;
END if;
*/
return v;

end
;;
DELIMITER ;

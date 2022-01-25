ALTER TABLE `JARITEMPREFER`.`WFD_FLOW`
ADD COLUMN `ALLOW_URGE_TASK` integer(255) NULL COMMENT '允许催办' AFTER `CODE_LANGUAGE`,
ADD COLUMN `TIME_LIMIT_GENERAL` integer(255) NULL COMMENT '一般工作工作办理时限' AFTER `ALLOW_URGE_TASK`,
ADD COLUMN `TIME_LIMIT_UNIT_G` varchar(255) NULL COMMENT '一般工作催办时限单位' AFTER `TIME_LIMIT_GENERAL`,
ADD COLUMN `TIME_LIMIT_URGENT` integer(255) NULL COMMENT '加急工作办理时限' AFTER `TIME_LIMIT_UNIT_G`,
ADD COLUMN `TIME_LIMIT_UNIT_U` varchar(255) NULL COMMENT '加急工作催办时限单位' AFTER `TIME_LIMIT_URGENT`,
ADD COLUMN `TIME_LIMIT_EXTRA_URGENT` varchar(255) NULL COMMENT '特急工作办理时限' AFTER `TIME_LIMIT_UNIT_U`,
ADD COLUMN `TIME_LIMIT_UNIT_EU` integer(255) NULL COMMENT '特急工作催办时限单位' AFTER `TIME_LIMIT_EXTRA_URGENT`,
ADD COLUMN `OVER_TIME_MODE` varchar(255) NULL COMMENT '超时处理方式' AFTER `TIME_LIMIT_UNIT_EU`,
ADD COLUMN `OVER_TIME_REMIND_TIME` varchar(255) NULL COMMENT '催办次数' AFTER `OVER_TIME_MODE`,
ADD COLUMN `OVER_TIME_REMIND_INTV` varchar(255) NULL COMMENT '催办间隔' AFTER `OVER_TIME_REMIND_TIME`,
ADD COLUMN `OVER_TIME_MSG_TEMPLATE_CODE` varchar(255) NULL COMMENT '催办信息模板' AFTER `OVER_TIME_REMIND_INTV`,
ADD COLUMN `FLOW_IN_MSG_TEMPLATE` varchar(255) NULL COMMENT '流入消息模板' AFTER `OVER_TIME_MSG_TEMPLATE_CODE`,
ADD COLUMN `OVER_TIME_MSG_TYPE` varchar(255) NULL COMMENT '催办处理频道' AFTER `FLOW_IN_MSG_TEMPLATE`,
ADD COLUMN `OVER_TIME_VARIABLE_FIELD` datetime NULL COMMENT '超时提醒催办变量' AFTER `OVER_TIME_MSG_TYPE`;
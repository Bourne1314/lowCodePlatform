CREATE TABLE `WFD_FLOW_CATEGORY` (
`ID`  varchar(50) NOT NULL COMMENT '主键' ,
`NAME`  varchar(50) NULL COMMENT '类别名称' ,
`SORT_NO`  int NULL DEFAULT 1 COMMENT '序号' ,
`APP_ID`  varchar(50) NULL DEFAULT '' COMMENT '应用ID' ,
PRIMARY KEY (`ID`)
)
COMMENT='流程类别'
;
CREATE TABLE `WFD_FLOW` (
`ID`  varchar(50) NOT NULL COMMENT '主键' ,
`NAME`  varchar(50) NULL COMMENT '流程名称' ,
`CODE`  varchar(50) NULL COMMENT '流程标识' ,
`SORT_NO`  int NULL DEFAULT 1 COMMENT '流程序号' ,
`CATEGORY_ID`  varchar(50) NULL COMMENT '流程类别' ,
`DESCRIPTION`  varchar(4000) NULL COMMENT '描述信息' ,
`ADMIN_AUTH_ID`  varchar(50) NULL COMMENT '监控权限' ,
`QUERY_AUTH_ID`  varchar(50) NULL COMMENT '查询权限' ,
`HAS_MODIFIED`  numeric(1,0) NULL DEFAULT 0 COMMENT '发布后已经修改过' ,
`REVISE_VERSION`  int NULL DEFAULT 0 COMMENT '修订版本号' ,
`MODEL`  longtext NULL COMMENT '流程模型' ,
`FORM_DATA_TABLE`  varchar(50) NULL COMMENT '数据表名' ,
`FORM_DATA_SOURCE_ID`  varchar(50) NULL COMMENT '数据源id' ,
`IS_EDITING`  numeric(1,0) NULL DEFAULT 0 COMMENT '是否在编辑' ,
`EDITING_USER`  varchar(50) NULL COMMENT '编辑用户' ,
`LAST_EDIT_TIME`  datetime NULL COMMENT '最后编辑时间' ,
`INIT_AUTH_ID`  varchar(50) NULL COMMENT '发起权限' ,
`SEQ_NO`  int NOT NULL DEFAULT 0 COMMENT '流水号' ,
`LATEST_CREATE_TIME`  datetime NULL COMMENT '最新创建时间' ,
`APP_ID`  varchar(50) NULL COMMENT '应用id' ,
PRIMARY KEY (`ID`)
)
COMMENT='流程定义'
;

CREATE TABLE `WFD_FLOW_AGENT` (
`ID`  varchar(50) NOT NULL ,
`BEGIN_TIME`  datetime NULL COMMENT '启用时间' ,
`END_TIME`  datetime NULL COMMENT '结束时间' ,
`ENABLED`  numeric(1,0) NULL DEFAULT 1 COMMENT '是否启用0不，1启用' ,
`FLOW_ID`  varchar(50) NULL COMMENT '流程' ,
`ORIGIN_USER_ID`  varchar(50) NULL COMMENT '原用户ID' ,
`AGENT_UESR_ID`  varchar(50) NULL COMMENT '代办用户ID' ,
`AGENT_USER_NAME`  varchar(50) NULL COMMENT '代办用户名称' ,
PRIMARY KEY (`ID`)
)
COMMENT='工作代办规则'
;

CREATE TABLE `WFD_GLOBAL_RULE` (
`ID`  varchar(50) NOT NULL COMMENT '主键' ,
`NAME`  varchar(50) NULL ,
`DESCRIPTION`  varchar(2000) NULL COMMENT '规则描述' ,
`EXPRESSION`  longtext NULL COMMENT '表达式' ,
`APP_ID`  varchar(50) NULL COMMENT '应用ID' ,
PRIMARY KEY (`ID`)
)
COMMENT='全局规则表'
;

CREATE TABLE `WFD_GLOBAL_VARIANT` (
`ID`  varchar(50) NOT NULL COMMENT '主键' ,
`NAME`  varchar(50) NULL COMMENT '变量名' ,
`CAPTION`  varchar(100) NULL COMMENT '变量标题' ,
`DATA_TYPE`  varchar(50) NULL COMMENT '类型' ,
`VALUE_EXPRESSION`  varchar(100) NULL COMMENT '取值表达式' ,
`APP_ID`  varchar(50) NULL COMMENT '应用ID' ,
`DEFAULT_VALUE`  varchar(100) NULL COMMENT '初始值' ,
PRIMARY KEY (`ID`)
)
COMMENT='全局变量表'
;

CREATE TABLE `WFD_V_FLOW` (
`ID`  varchar(50) NOT NULL COMMENT '主键' ,
`FLOW_VERSION`  int NULL COMMENT '版本' ,
`NAME`  varchar(50) NOT NULL COMMENT '流程名称' ,
`CODE`  varchar(50) NULL COMMENT '流程标识' ,
`SORT_NO`  int NULL COMMENT '流程序号' ,
`CATEGORY_ID`  varchar(50) NULL COMMENT '流程类别' ,
`DESCRIPTION`  varchar(4000) NULL COMMENT '描述信息' ,
`ADMIN_AUTH_ID`  varchar(50) NULL COMMENT '监控权限' ,
`QUERY_AUTH_ID`  varchar(50) NULL COMMENT '查询权限' ,
`FLOW_ID`  varchar(50) NULL COMMENT '流程id' ,
`IS_LATEST`  numeric(1,0) NULL COMMENT '是否最新' ,
`MODEL`  longtext NULL ,
`BPMN`  longtext NULL COMMENT 'BPMN' ,
`FORM_DATA_TABLE`  varchar(50) NULL COMMENT '数据表名' ,
`FORM_DATA_SOURCE_ID`  varchar(50) NULL COMMENT '数据源id' ,
`INIT_AUTH_ID`  varchar(50) NULL COMMENT '发起权限' ,
`VERSION_BEGIN_DATE`  datetime NULL COMMENT '版本生效日期' ,
`VERSION_END_DATE`  datetime NULL COMMENT '版本失效日期' ,
`IS_USED`  numeric(1,0) NULL COMMENT '是否已使用' ,
PRIMARY KEY (`ID`)
)
COMMENT='流程定义'
;

CREATE TABLE `WFI_FLOW` (
`ID`  varchar(50) NOT NULL COMMENT '流程实例id' ,
`V_FLOW_ID`  varchar(50) NULL COMMENT '流程发布id' ,
`MODEL`  longtext NULL COMMENT '流程模型' ,
`BUSINESS_KEY`  varchar(50) NULL COMMENT '业务标识' ,
`FLOW_NO`  varchar(50) NULL COMMENT '工作文号' ,
`FLOW_ID`  varchar(50) NULL COMMENT '流程id' ,
`FLOW_CODE`  varchar(50) NULL COMMENT '流程标识' ,
PRIMARY KEY (`ID`)
)
COMMENT='流程实例'
;

CREATE TABLE `WFI_FLOW_USER` (
`ID`  varchar(50) NOT NULL COMMENT '主键' ,
`USER_ID`  varchar(50) NULL COMMENT '用户id' ,
`USER_TYPE`  int NOT NULL COMMENT '参与类型(0-主办人,1-协办人)' ,
`FLOW_ID`  varchar(50) NULL COMMENT '流程实例id' ,
`TASK_ID`  varchar(50) NULL COMMENT '任务id' ,
`CREATE_TIME`  datetime NULL COMMENT '创建时间' ,
`IS_COMPLETED`  numeric(1,0) NULL COMMENT '是否已完成' ,
`REAL_NAME`  varchar(50) NULL COMMENT '用户名称' ,
PRIMARY KEY (`ID`)
)
COMMENT='流程实例参与用户'
;

ALTER TABLE `wfd_flow` ADD CONSTRAINT `FK_WFD_FLOW_CATEGORY_ID` FOREIGN KEY (`CATEGORY_ID`) REFERENCES `wfd_flow_category` (`ID`) ON DELETE SET NULL;

alter table WFD_FLOW add constraint UK_WFD_FLOW_CODE unique(CODE,APP_ID);

ALTER TABLE `wfd_flow_agent` ADD CONSTRAINT `FK_WFD_FLOW_AGENT_FLOW_ID` FOREIGN KEY (`FLOW_ID`) REFERENCES `wfd_flow` (`ID`) ON DELETE CASCADE;

ALTER TABLE `wfd_v_flow` ADD CONSTRAINT `FK_WFD_V_FLOW_FLOW_ID` FOREIGN KEY (`FLOW_ID`) REFERENCES `wfd_flow` (`ID`) ON DELETE CASCADE;

ALTER TABLE `wfd_v_flow` ADD CONSTRAINT `FK_WFD_V_FLOW_CATEGORY` FOREIGN KEY (`CATEGORY_ID`) REFERENCES `wfd_flow_category` (`ID`) ON DELETE SET NULL;


alter table WFI_FLOW add constraint UK_WFI_FLOW_FLOW_NO unique(FLOW_NO,FLOW_ID);

alter table WFI_FLOW add constraint UK_WFI_FLOW_FLOW_NO unique(FLOW_NO,FLOW_ID);

ALTER TABLE `wfi_flow_user` ADD CONSTRAINT `FK_WFI_FLOW_USER_FLOW_ID` FOREIGN KEY (`FLOW_ID`) REFERENCES `wfi_flow` (`ID`) ON DELETE CASCADE;

alter table WFI_FLOW_USER add constraint UK_WFI_FLOW_USER_USER_ID unique(USER_ID,TASK_ID);

CREATE TABLE `WF_PROPERTY` (
`NAME`  varchar(50) NOT NULL ,
`VALUE`  varchar(50) NULL ,
`DESCRIPTION`  varchar(50) NULL ,
PRIMARY KEY (`NAME`)
)
COMMENT='流程属性'
;


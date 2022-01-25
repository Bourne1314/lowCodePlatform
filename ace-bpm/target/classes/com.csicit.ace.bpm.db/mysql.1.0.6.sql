CREATE TABLE `WFI_BACKUP` (
`ID`  varchar(50) NOT NULL COMMENT '主键' ,
`FLOW_ID`  varchar(50) NULL COMMENT '流程实例id' ,
`DESCRIPTION`  varchar(200) NULL COMMENT '描述' ,
`VERSION`  int NULL COMMENT '版本' ,
`BACKUP_TIME`  datetime NULL COMMENT '备份时间' ,
`BACKUP_DATA`  longtext NULL COMMENT '备份数据' ,
`TASK_ID`  varchar(50) NULL COMMENT '任务id' ,
`APP_ID`  varchar(50) NULL COMMENT '应用id' ,
`ENGINE_VERSION`  varchar(50) NULL COMMENT '流程引擎版本' ,
PRIMARY KEY (`ID`)
)
COMMENT='流程实例-备份信息'
;


alter table WFI_BACKUP add constraint UK_WFI_BACKUP_VERSION  unique(FLOW_ID,VERSION,APP_ID);
alter table WFI_BACKUP add constraint UK_WFI_BACKUP_TASK_ID  unique(FLOW_ID,TASK_ID,APP_ID);

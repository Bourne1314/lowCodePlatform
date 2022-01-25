create table WFI_BACKUP
(
	"ID" VARCHAR2(50),
	"FLOW_ID" VARCHAR2(50),
	"DESCRIPTION" VARCHAR2(200),
	"VERSION" INTEGER,
	"BACKUP_TIME" DATETIME(6)
);

comment on table WFI_BACKUP is '流程实例-备份信息';

comment on column WFI_BACKUP."ID" is '主键';

comment on column WFI_BACKUP."FLOW_ID" is '流程实例id';

comment on column WFI_BACKUP."DESCRIPTION" is '描述';

comment on column WFI_BACKUP."VERSION" is '版本';

comment on column WFI_BACKUP."BACKUP_TIME" is '备份时间';

alter table WFI_BACKUP add primary key("ID");

alter table "WFI_BACKUP" add column("BACKUP_DATA" CLOB);

comment on column "WFI_BACKUP"."BACKUP_DATA" is '备份数据';

alter table "WFI_BACKUP" add column("TASK_ID" VARCHAR2(50));

comment on column "WFI_BACKUP"."TASK_ID" is '任务id';

alter table "WFI_BACKUP" add column("APP_ID" VARCHAR2(50));

comment on column "WFI_BACKUP"."APP_ID" is '应用id';

alter table "WFI_BACKUP" add constraint "UK_WFI_BACKUP_VERSION"  unique("FLOW_ID","VERSION","APP_ID");
alter table "WFI_BACKUP" add constraint "UK_WFI_BACKUP_TASK_ID"  unique("FLOW_ID","TASK_ID","APP_ID");
alter table "WFI_BACKUP" add column("ENGINE_VERSION" VARCHAR2(50));

comment on column "WFI_BACKUP"."ENGINE_VERSION" is '流程引擎版本';
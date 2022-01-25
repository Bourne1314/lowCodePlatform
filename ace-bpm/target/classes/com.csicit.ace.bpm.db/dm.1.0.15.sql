create table "WFI_ROUTE_PRESET"
(
	"ID" VARCHAR2(50),
	"FLOW_ID" VARCHAR2(50),
	"TASK_ID" VARCHAR2(50),
	"PRESET_TIME" DATETIME(6),
	"PRESET_INFO" CLOB
);

comment on table "WFI_ROUTE_PRESET" is '流程实例流转预设信息';

comment on column "WFI_ROUTE_PRESET"."ID" is '主键';

comment on column "WFI_ROUTE_PRESET"."FLOW_ID" is '流程实例ID';

comment on column "WFI_ROUTE_PRESET"."TASK_ID" is '任务ID';

comment on column "WFI_ROUTE_PRESET"."PRESET_TIME" is '预设时间';

comment on column "WFI_ROUTE_PRESET"."PRESET_INFO" is '预设信息';
alter table "JARITEMPREFER"."WFD_FLOW" add column("ALLOW_URGE_TASK" INTEGER );
comment on column "JARITEMPREFER"."WFD_FLOW"."ALLOW_URGE_TASK" is '允许催办 ';
alter table "JARITEMPREFER"."WFD_FLOW" add column("TIME_LIMIT_GENERAL" INTEGER);
comment on column "JARITEMPREFER"."WFD_FLOW"."TIME_LIMIT_GENERAL" is '一般工作工作办理时限';
alter table "JARITEMPREFER"."WFD_FLOW" add column("TIME_LIMIT_UNIT_G" VARCHAR(50));
comment on column "JARITEMPREFER"."WFD_FLOW"."TIME_LIMIT_UNIT_G" is '一般工作催办时限单位';

alter table "JARITEMPREFER"."WFD_FLOW" add column("TIME_LIMIT_URGENT" INTEGER );
comment on column "JARITEMPREFER"."WFD_FLOW"."TIME_LIMIT_URGENT" is '加急工作办理时限';
alter table "JARITEMPREFER"."WFD_FLOW" add column("TIME_LIMIT_UNIT_U" VARCHAR(50));
comment on column "JARITEMPREFER"."WFD_FLOW"."TIME_LIMIT_UNIT_U" is '加急工作催办时限单位';
alter table "JARITEMPREFER"."WFD_FLOW" add column("TIME_LIMIT_EXTRA_URGENT" INTEGER );
comment on column "JARITEMPREFER"."WFD_FLOW"."TIME_LIMIT_EXTRA_URGENT" is '特急工作办理时限';
alter table "JARITEMPREFER"."WFD_FLOW" add column("TIME_LIMIT_UNIT_EU" CHAR(10));
comment on column "JARITEMPREFER"."WFD_FLOW"."TIME_LIMIT_UNIT_EU" is '特急工作催办时限单位';
alter table "JARITEMPREFER"."WFD_FLOW" add column("OVER_TIME_MODE" CHAR(10));
comment on column "JARITEMPREFER"."WFD_FLOW"."OVER_TIME_MODE" is '超时 处理方式';
alter table "JARITEMPREFER"."WFD_FLOW" add column("OVER_TIME_REMIND_TIME" VARCHAR (50));
comment on column "JARITEMPREFER"."WFD_FLOW"."OVER_TIME_REMIND_TIME" is '催办次数';
alter table "JARITEMPREFER"."WFD_FLOW" add column("OVER_TIME_REMIND_INTV" CHAR(10));
comment on column "JARITEMPREFER"."WFD_FLOW"."OVER_TIME_REMIND_INTV" is '催办间隔';
alter table "JARITEMPREFER"."WFD_FLOW" add column("OVER_TIME_MSG_TEMPLATE_CODE" CHAR(10));
comment on column "JARITEMPREFER"."WFD_FLOW"."OVER_TIME_MSG_TEMPLATE_CODE" is '催办信息模板';
alter table "JARITEMPREFER"."WFD_FLOW" add column("FLOW_IN_MSG_TEMPLATE" CHAR(10));
comment on column "JARITEMPREFER"."WFD_FLOW"."FLOW_IN_MSG_TEMPLATE" is '流入消息模板';
alter table "JARITEMPREFER"."WFD_FLOW" add column("OVER_TIME_MSG_TYPE" CHAR(10));
comment on column "JARITEMPREFER"."WFD_FLOW"."OVER_TIME_MSG_TYPE" is '催办处理频道';
alter table "JARITEMPREFER"."WFD_FLOW" add column("OVER_TIME_VARIABLE_FIELD" CHAR(10));
comment on column "JARITEMPREFER"."WFD_FLOW"."OVER_TIME_VARIABLE_FIELD" is '超时提醒催办变量';
insert into WFD_FLOW_CATEGORY (ID,NAME,SORT_NO,APP_ID) VALUES ('filereview','附件审查流程目录',1100,'dv-testapp-platform');commit
    insert into WFD_FLOW (ID,NAME,CODE,SORT_NO,CATEGORY_ID,DESCRIPTION,ADMIN_AUTH_ID,QUERY_AUTH_ID,HAS_MODIFIED,REVISE_VERSION,MODEL,FORM_DATA_TABLE,FORM_DATA_SOURCE_ID,IS_EDITING,
    EDITING_USER,LAST_EDIT_TIME,INIT_AUTH_ID,SEQ_NO,LATEST_CREATE_TIME,APP_ID) VALUES ('fid-99fd813d-a1c9-46ea-bbc9-a4f8bfc5c0cf1','附件审查目录','fileReview1','10','ea28ecfc949b4b968c853b83c1dfa565','','','',0,0,'{"adminAuthId":"","categoryId":"ea28ecfc949b4b968c853b83c1dfa565","code":"fileReview","description":"","enableSetUrgency":0,"events":[],"flowChart":"{\"class\":\"go.GraphLinksModel\",\"linkFromPortIdProperty\":\"fromPort\",\"linkToPortIdProperty\":\"toPort\",\"nodeDataArray\":[],\"linkDataArray\":[]}","formCascadeDel":1,"formDataTable":"","formDatasourceId":"","formFields":[],"formIdName":"ID","formOperations":[],"formResultField":"","formSaveOperate":"","formSecretLevelField":"","formUrl":"","hasModified":0,"id":"fid-04f13caa-d613-42a2-83b1-07e8e334244e","links":[],"msgChannel":"","msgScopeFinished":[],"msgTemplateFinished":"","msgTemplateNewWork":"","name":"附件审查流程目录","nodes":[],"queryAuthId":"","reviseVersion":0,"rules":[],"sortNo":10,"syncSettings":[],"variants":[],"workNoSeqLength":4,"workNoSeqResetRule":0,"workNoStyle":"{F}-{Y}-{M}-{D}-{N}"}',
    '','',0,null,null,'',0,null,'dv-testapp-platform');commit

CREATE TABLE "JARITEMPREFER"."SYS_REVIEW_FILE"
(
    "ID" VARCHAR2(50),
    "APPLY_USER_ID" VARCHAR2(50),
    "APPLY_USER_NAME" VARCHAR2(50),
    "REMARK" VARCHAR2(500),
    "MANAGE_USER_ID" VARCHAR2(50),
    "MANAGE_USER_NAME" VARCHAR2(50),
    "APPLY_DEP_ID" VARCHAR2(50),
    "APPLY_DEP_NAME" VARCHAR2(50),
    "APPLY_TIME" DATETIME(6),
    "FLOW_CODE" VARCHAR2(50),
    "NODE_NAME" VARCHAR2(50),
    "AUTH_ID" VARCHAR2(50),
    "AUTH_NAME" VARCHAR2(50),
    "ROLE_ID" VARCHAR2(50),
    "ROLE_NAME" VARCHAR2(50),
    "SECRET_LEVEL" INTEGER) STORAGE(ON "MAIN", CLUSTERBTR) ;
alter table "JARITEMPREFER"."WFD_FLOW" add column("EDITABLE" INTEGER);

comment on column "JARITEMPREFER"."WFD_FLOW"."EDITABLE" is '是否可编辑';
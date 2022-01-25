CREATE TABLE WFI_ROUTE_PRESET
(
  ID           VARCHAR(50),
  FLOW_ID      VARCHAR(50),
  TASK_ID      VARCHAR(50),
  PRESET_TIME  TIMESTAMP,
  PRESET_INFO  CLOB
);

ALTER TABLE WFI_ROUTE_PRESET ADD
  CONSTRAINT WFI_ROUTE_PRESET_PK
  PRIMARY KEY
  (ID)
  ENABLE VALIDATE;
alter table "WFD_FLOW" add column("ALLOW_URGE_TASK" INTEGER(50));
comment on column "WFD_FLOW"."ALLOW_URGE_TASK" is '允许催办';
alter table "WFD_FLOW" add column("TIME_LIMIT_GENERAL" INTEGER(50));
comment on column "WFD_FLOW"."TIME_LIMIT_GENERAL" is '一般工作工作办理时限';
alter table "WFD_FLOW" add column("TIME_LIMIT_UNIT_G" VARCHAR(50));
comment on column "WFD_FLOW"."TIME_LIMIT_UNIT_G" is '一般工作催办时限单位';
alter table "WFD_FLOW" add column("TIME_LIMIT_URGENT" INTEGER(50));
comment on column "WFD_FLOW"."TIME_LIMIT_URGENT" is '加急工作办理时限';
alter table "WFD_FLOW" add column("TIME_LIMIT_UNIT_U" VARCHAR(50));
comment on column "WFD_FLOW"."TIME_LIMIT_UNIT_U" is '加急工作催办时限单位';
alter table "WFD_FLOW" add column("TIME_LIMIT_EXTRA_URGENT" VARCHAR(50));
comment on column "WFD_FLOW"."TIME_LIMIT_EXTRA_URGENT" is '特急工作办理时限';
alter table "WFD_FLOW" add column("TIME_LIMIT_UNIT_EU" INTEGER(50));
comment on column "WFD_FLOW"."TIME_LIMIT_UNIT_EU" is '特急工作催办时限单位';
alter table "WFD_FLOW" add column("OVER_TIME_MODE" VARCHAR(50));
comment on column "WFD_FLOW"."OVER_TIME_MODE" is '超时处理方式';
alter table "WFD_FLOW" add column("OVER_TIME_REMIND_TIME" VARCHAR(50));
comment on column "WFD_FLOW"."OVER_TIME_REMIND_TIME" is '催办次数';
alter table "WFD_FLOW" add column("OVER_TIME_REMIND_INTV" VARCHAR(50));
comment on column "WFD_FLOW"."OVER_TIME_REMIND_INTV" is '催办间隔';
alter table "WFD_FLOW" add column("OVER_TIME_MSG_TEMPLATE_CODE" VARCHAR(50));
comment on column "WFD_FLOW"."OVER_TIME_MSG_TEMPLATE_CODE" is '催办信息模板';
alter table "WFD_FLOW" add column("FLOW_IN_MSG_TEMPLATE" VARCHAR(50));
comment on column "WFD_FLOW"."FLOW_IN_MSG_TEMPLATE" is '流入消息模板';
alter table "WFD_FLOW" add column("OVER_TIME_MSG_TYPE" VARCHAR(50));
comment on column "WFD_FLOW"."OVER_TIME_MSG_TYPE" is '催办处理频道';
alter table "WFD_FLOW" add column("OVER_TIME_VARIABLE_FIELD" VARCHAR(50));
comment on column "WFD_FLOW"."OVER_TIME_VARIABLE_FIELD" is '''超时提醒催办变量';

CREATE TABLE WFI_ROUTE_PRESET
(
    ID           VARCHAR(50),
    FLOW_ID      VARCHAR(50),
    TASK_ID      VARCHAR(50),
    PRESET_TIME  TIMESTAMP,
    PRESET_INFO  CLOB
);

CREATE TABLE "SYS_REVIEW_FILE"
(
    ID VARCHAR2(50),
    APPLY_USER_ID VARCHAR2(50),
    APPLY_USER_NAME VARCHAR2(50),
    REMARK VARCHAR2(500),
    MANAGE_USER_ID VARCHAR2(50),
    MANAGE_USER_NAME VARCHAR2(50),
    APPLY_DEP_ID VARCHAR2(50),
    APPLY_DEP_NAME VARCHAR2(50),
    APPLY_TIME DATETIME(6),
    FLOW_CODE VARCHAR2(50),
    NODE_NAME VARCHAR2(50),
    AUTH_ID VARCHAR2(50),
    AUTH_NAME VARCHAR2(50),
    ROLE_ID VARCHAR2(50),
    ROLE_NAME VARCHAR2(50),
    SECRET_LEVEL INTEGER);

ALTER TABLE WFD_FLOW
    ADD (FLOW_ID  INTEGER DEFAULT 1);
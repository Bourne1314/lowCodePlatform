alter table "JARITEMPREFER"."WFD_FLOW" add column("ALLOW_URGE_TASK" INTEGER );
comment on column "JARITEMPREFER"."WFD_FLOW"."ALLOW_URGE_TASK" is ''允许催办'';
alter table "JARITEMPREFER"."WFD_FLOW" add column("TIME_LIMIT_GENERAL" INTEGER);
comment on column "JARITEMPREFER"."WFD_FLOW"."TIME_LIMIT_GENERAL" is ''一般工作工作办理时限'';
alter table "JARITEMPREFER"."WFD_FLOW" add column("TIME_LIMIT_UNIT_G" VARCHAR(50));
comment on column "JARITEMPREFER"."WFD_FLOW"."TIME_LIMIT_UNIT_G" is ''一般工作催办时限单位'';

alter table "JARITEMPREFER"."WFD_FLOW" add column("TIME_LIMIT_URGENT" INTEGER );
comment on column "JARITEMPREFER"."WFD_FLOW"."TIME_LIMIT_URGENT" is ''加急工作办理时限'';
alter table "JARITEMPREFER"."WFD_FLOW" add column("TIME_LIMIT_UNIT_U" VARCHAR(50));
comment on column "JARITEMPREFER"."WFD_FLOW"."TIME_LIMIT_UNIT_U" is ''加急工作催办时限单位'';
alter table "JARITEMPREFER"."WFD_FLOW" add column("TIME_LIMIT_EXTRA_URGENT" INTEGER );
comment on column "JARITEMPREFER"."WFD_FLOW"."TIME_LIMIT_EXTRA_URGENT" is ''特急工作办理时限'';
alter table "JARITEMPREFER"."WFD_FLOW" add column("TIME_LIMIT_UNIT_EU" CHAR(10));
comment on column "JARITEMPREFER"."WFD_FLOW"."TIME_LIMIT_UNIT_EU" is ''特急工作催办时限单位'';
alter table "JARITEMPREFER"."WFD_FLOW" add column("OVER_TIME_MODE" CHAR(10));
comment on column "JARITEMPREFER"."WFD_FLOW"."OVER_TIME_MODE" is ''超时 处理方式'';
alter table "JARITEMPREFER"."WFD_FLOW" add column("OVER_TIME_REMIND_TIME" VARCHAR (50));
comment on column "JARITEMPREFER"."WFD_FLOW"."OVER_TIME_REMIND_TIME" is ''催办次数'';
alter table "JARITEMPREFER"."WFD_FLOW" add column("OVER_TIME_REMIND_INTV" CHAR(10));
comment on column "JARITEMPREFER"."WFD_FLOW"."OVER_TIME_REMIND_INTV" is ''催办间隔'';
alter table "JARITEMPREFER"."WFD_FLOW" add column("OVER_TIME_MSG_TEMPLATE_CODE" CHAR(10));
comment on column "JARITEMPREFER"."WFD_FLOW"."OVER_TIME_MSG_TEMPLATE_CODE" is ''催办信息模板'';
alter table "JARITEMPREFER"."WFD_FLOW" add column("FLOW_IN_MSG_TEMPLATE" CHAR(10));
comment on column "JARITEMPREFER"."WFD_FLOW"."FLOW_IN_MSG_TEMPLATE" is ''流入消息模板'';
alter table "JARITEMPREFER"."WFD_FLOW" add column("OVER_TIME_MSG_TYPE" CHAR(10));
comment on column "JARITEMPREFER"."WFD_FLOW"."OVER_TIME_MSG_TYPE" is ''催办处理频道'';
alter table "JARITEMPREFER"."WFD_FLOW" add column("OVER_TIME_VARIABLE_FIELD" CHAR(10));
comment on column "JARITEMPREFER"."WFD_FLOW"."OVER_TIME_VARIABLE_FIELD" is ''超时提醒催办变量'';

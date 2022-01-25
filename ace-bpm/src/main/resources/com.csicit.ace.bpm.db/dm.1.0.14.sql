create table "WFI_V_FLOW"
(
	"ID" VARCHAR2(50),
	"FLOW_ID" VARCHAR2(50),
	"MODEL" CLOB,
	"BPMN" CLOB,
	"FLOW_VERSION" INT,
	"VERSION_END_DATE" DATETIME(6)
);

comment on table "WFI_V_FLOW" is '流程实例版本';

comment on column "WFI_V_FLOW"."ID" is '主键';

comment on column "WFI_V_FLOW"."FLOW_ID" is '流程实例ID';

comment on column "WFI_V_FLOW"."MODEL" is '流程定义模型';

comment on column "WFI_V_FLOW"."BPMN" is 'BPMN模型';

comment on column "WFI_V_FLOW"."FLOW_VERSION" is '流程版本';

comment on column "WFI_V_FLOW"."VERSION_END_DATE" is '版本失效时间';

alter table "WFI_V_FLOW" add primary key("ID");

alter table "WFI_V_FLOW" add constraint "UK_WFI_V_FLOW_VERSION"  unique("FLOW_ID","FLOW_VERSION");

alter table "WFI_V_FLOW" add constraint "FK_WFI_V_FLOW_FLOW_ID" foreign key("FLOW_ID") references "WFI_FLOW"("ID") on delete cascade;


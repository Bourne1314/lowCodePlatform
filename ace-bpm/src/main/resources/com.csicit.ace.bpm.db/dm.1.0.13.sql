create table "WFI_TASK_PENDING"
(
	"ID" VARCHAR2(50),
	"TASK_ID" VARCHAR2(50),
	"FLOW_ID" VARCHAR2(50),
	"APP_ID" VARCHAR2(50),
	"USER_TYPE" INT,
	"CREATE_TIME" DATETIME(6),
	"USER_ID" VARCHAR2(50),
	"NODE_ID" VARCHAR2(50),
	"FLOW_CODE" VARCHAR2(50)
)
storage(initial 1, next 1, minextents 1, fillfactor 0)
;

comment on table "WFI_TASK_PENDING" is '待办任务';

comment on column "WFI_TASK_PENDING"."ID" is 'id';

comment on column "WFI_TASK_PENDING"."TASK_ID" is '任务id';

comment on column "WFI_TASK_PENDING"."FLOW_ID" is '流程实例id';

comment on column "WFI_TASK_PENDING"."APP_ID" is '应用id';

comment on column "WFI_TASK_PENDING"."USER_TYPE" is '用户身份';

comment on column "WFI_TASK_PENDING"."CREATE_TIME" is '创建时间';

comment on column "WFI_TASK_PENDING"."USER_ID" is '办理人ID';

comment on column "WFI_TASK_PENDING"."NODE_ID" is '节点ID';

comment on column "WFI_TASK_PENDING"."FLOW_CODE" is '流程标识';

alter table "WFI_TASK_PENDING" add primary key("ID");

alter table "WFI_FOCUSED_WORK" drop constraint "FK_WFI_FOCUSED_WORK_FLOW_ID";

alter table "WFI_FOCUSED_WORK" modify "WORK_NO" VARCHAR2(200);
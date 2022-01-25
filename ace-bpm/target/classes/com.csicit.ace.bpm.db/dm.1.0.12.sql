alter table "WFI_USER_TASK_STATE" add column("FLOW_ID" VARCHAR2(50));

comment on column "WFI_USER_TASK_STATE"."FLOW_ID" is '流程实例ID';

DELETE FROM "WFI_FOCUSED_WORK" WHERE FLOW_INSTANCE_ID NOT IN (SELECT ID FROM "WFI_FLOW");

alter table "WFI_FOCUSED_WORK" add constraint "FK_WFI_FOCUSED_WORK_FLOW_ID" foreign key("FLOW_INSTANCE_ID") references "WFI_FLOW"("ID") on delete cascade;

DELETE FROM "WFI_USER_TASK_STATE" WHERE FLOW_ID NOT IN (SELECT ID FROM "WFI_FLOW");

alter table "WFI_USER_TASK_STATE" add constraint "FK_WFI_USER_TASK_STATE_FLOW_ID" foreign key("FLOW_ID") references "WFI_FLOW"("ID") on delete cascade;

alter table "WFI_FOCUSED_WORK" add column("LATEST_READ_TIME" DATETIME(6));

comment on column "WFI_FOCUSED_WORK"."LATEST_READ_TIME" is '用户最新一次浏览流程的时间';

alter table "WFI_FOCUSED_WORK" add column("LATEST_READ_FLAG" INTEGER NOT NULL  DEFAULT 1);

comment on column "WFI_FOCUSED_WORK"."LATEST_READ_FLAG" is '最新一次浏览后是否有更新  0 否 1 是';
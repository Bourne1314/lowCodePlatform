create table "WFI_COMMENT"
(
	"ID" VARCHAR2(50),
	"COMMENT_USER_ID" VARCHAR2(50),
	"COMMENT_USER" VARCHAR2(30),
	"COMMENT_ID" VARCHAR2(50),
	"FLOW_ID" VARCHAR2(50),
	"COMMENT_TIME" DATETIME(6),
	"TASK_ID" VARCHAR2(50),
	"COMMENT_TEXT" VARCHAR2(400)
);

comment on table "WFI_COMMENT" is '流程实例-意见评论';

comment on column "WFI_COMMENT"."ID" is '主键';

comment on column "WFI_COMMENT"."COMMENT_USER_ID" is '评论人id';

comment on column "WFI_COMMENT"."COMMENT_USER" is '评论人';

comment on column "WFI_COMMENT"."COMMENT_ID" is '回复评论ID';

comment on column "WFI_COMMENT"."FLOW_ID" is '流程实例ID';

comment on column "WFI_COMMENT"."COMMENT_TIME" is '评论时间';

comment on column "WFI_COMMENT"."TASK_ID" is '任务ID';

comment on column "WFI_COMMENT"."COMMENT_TEXT" is '评论内容';

alter table "WFI_COMMENT" alter column "ID" set not null;

alter table "WFI_COMMENT" add primary key("ID");

alter table "WFI_COMMENT" add column("APP_ID" VARCHAR2(50));

comment on column "WFI_COMMENT"."APP_ID" is '应用标识';

alter table "WFI_COMMENT" alter column "COMMENT_ID" rename to "REPLY_COMMENT_ID";

alter table "WFI_COMMENT" add column("REPLY_USER_ID" VARCHAR2(50));

alter table "WFI_COMMENT" add column("REPLY_USER" VARCHAR2(30));

comment on column "WFI_COMMENT"."REPLY_USER_ID" is '被回复人ID';

comment on column "WFI_COMMENT"."REPLY_USER" is '被回复人';

alter table "WFI_COMMENT" add column("USER_TYPE" INTEGER);

comment on column "WFI_COMMENT"."USER_TYPE" is '评论人身份(-1无关人员,0主办人,1协办人)';

alter table "WFI_COMMENT" add constraint "FK_WFI_COMMENT_FLOW_ID" foreign key("FLOW_ID") references "WFI_FLOW"("ID") on delete cascade;

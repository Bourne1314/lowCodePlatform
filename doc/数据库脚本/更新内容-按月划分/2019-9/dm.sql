/**
  2019/09/27:修改集团三员管理
 */
COMMENT ON COLUMN "JARIACEPLAT"."SYS_USER"."USER_TYPE" IS '用户类型（0租户管理员,1集团管理员,11待激活的集团管理员,111未分配的集团管理员,2应用管理员,22待激活的应用管理员,222未分配的应用管理员,3普通用户）';

alter table "JARIACEPLAT"."SYS_USER_ADMIN_ORG" add column("IS_ACTIVATED" NUMBER(1,0) DEFAULT 0);
alter table "JARIACEPLAT"."SYS_USER_ADMIN_ORG" add column("ROLE_TYPE" NUMBER(10,0));
comment on column "JARIACEPLAT"."SYS_USER_ADMIN_ORG"."IS_ACTIVATED" is '是否被激活，0没有，1是';
comment on column "JARIACEPLAT"."SYS_USER_ADMIN_ORG"."ROLE_TYPE" is '角色类型（11集团系统管理员,22集团安全保密员,33集团安全审计员）';

delete from "JARIACEPLAT"."SYS_AUTH" where ID in('5fc1b8842fa04faea8f39beb866fd111','5fc1b8842fa04faea8f39beb866fd112');
delete from "JARIACEPLAT"."SYS_AUTH_ROLE_V" where AUTH_ID in ('5fc1b8842fa04faea8f39beb866fd111','5fc1b8842fa04faea8f39beb866fd112');

insert into "JARIACEPLAT"."SYS_AUTH_ROLE" ("ID","AUTH_ID","ROLE_ID","SIGN","IS_REVOKE","CREATE_USER","CREATE_TIME","UPDATE_TIME","REMARK") values ('12323fe2ef32403dbf9dc989d0651231', '5fc1b8842fa04faea8f39beb866fd511', 'sec', null, 0, null, null, null, null);
insert into "JARIACEPLAT"."SYS_AUTH_ROLE_V" ("ID","AUTH_ID","ROLE_ID","SIGN","IS_REVOKE","CREATE_USER","CREATE_TIME","UPDATE_TIME","REMARK","LV_ID") values ('67870485-0733-0000-B229-5ED50026EA82', '5fc1b8842fa04faea8f39beb866fd511', 'sec', null, 0, null, null, null, null, 'sec');

/**
  2019/09/28:修改应用三员管理
 */
alter table "JARIACEPLAT"."SYS_AUTH_SCOPE_ORG" add column("IS_ACTIVATED" NUMBER(1,0) DEFAULT 0);
alter table "JARIACEPLAT"."SYS_AUTH_SCOPE_ORG" add column("ROLE_TYPE" NUMBER(10,0));
comment on column "JARIACEPLAT"."SYS_AUTH_SCOPE_ORG"."IS_ACTIVATED" is '是否被激活，0没有，1是';
comment on column "JARIACEPLAT"."SYS_AUTH_SCOPE_ORG"."ROLE_TYPE" is '角色类型（111应用系统管理员,222应用安全保密员,333应用安全审计员）';

alter table "JARIACEPLAT"."SYS_AUTH_SCOPE_APP" add column("IS_ACTIVATED" NUMBER(1,0) DEFAULT 0);
alter table "JARIACEPLAT"."SYS_AUTH_SCOPE_APP" add column("ROLE_TYPE" NUMBER(10,0));
comment on column "JARIACEPLAT"."SYS_AUTH_SCOPE_APP"."IS_ACTIVATED" is '是否被激活，0没有，1是';
comment on column "JARIACEPLAT"."SYS_AUTH_SCOPE_APP"."ROLE_TYPE" is '角色类型（111应用系统管理员,222应用安全保密员,333应用安全审计员）';

alter table "JARIACEPLAT"."SYS_AUTH_SCOPE_USER_GROUP" add column("IS_ACTIVATED" NUMBER(1,0) DEFAULT 0);
alter table "JARIACEPLAT"."SYS_AUTH_SCOPE_USER_GROUP" add column("ROLE_TYPE" NUMBER(10,0));
comment on column "JARIACEPLAT"."SYS_AUTH_SCOPE_USER_GROUP"."IS_ACTIVATED" is '是否被激活，0没有，1是';
comment on column "JARIACEPLAT"."SYS_AUTH_SCOPE_USER_GROUP"."ROLE_TYPE" is '角色类型（111应用系统管理员,222应用安全保密员,333应用安全审计员）';

delete from "JARIACEPLAT"."SYS_AUTH" where ID in('67e1114e83214aeb8a3ec0a6822fd111','67e1114e83214aeb8a3ec0a6822fd112');
delete from "JARIACEPLAT"."SYS_AUTH_ROLE_V" where AUTH_ID in ('67e1114e83214aeb8a3ec0a6822fd111','67e1114e83214aeb8a3ec0a6822fd112');

insert into "JARIACEPLAT"."SYS_AUTH_ROLE" ("ID","AUTH_ID","ROLE_ID","SIGN","IS_REVOKE","CREATE_USER","CREATE_TIME","UPDATE_TIME","REMARK") values ('12323fe2ef32403dbf9dc989d065rt78', '67e1114e83214aeb8a3ec0a6822fdd30', 'groupsec', null, 0, null, null, null, null);
insert into "JARIACEPLAT"."SYS_AUTH_ROLE_V" ("ID","AUTH_ID","ROLE_ID","SIGN","IS_REVOKE","CREATE_USER","CREATE_TIME","UPDATE_TIME","REMARK","LV_ID") values ('67870485-0733-0000-ghuy-5ED50026EA82', '67e1114e83214aeb8a3ec0a6822fdd30', 'groupsec', null, 0, null, null, null, null, 'groupsec');

alter table "JARIACEPLAT"."SYS_AUDIT_LOG" add column("GROUP_ID" VARCHAR2(50));
comment on column "JARIACEPLAT"."SYS_AUDIT_LOG"."GROUP_ID" is '操作针对的集团';

alter table "JARIACEPLAT"."SYS_AUDIT_LOG" add column("APP_ID" VARCHAR2(50));
comment on column "JARIACEPLAT"."SYS_AUDIT_LOG"."APP_ID" is '操作针对的应用';

/**
   用户角色历史数据主表
 */
CREATE TABLE "JARIACEPLAT"."SYS_USER_ROLE_LV"
(
"ID" VARCHAR2(50) NOT NULL,
"USER_ID" VARCHAR2(50),
"VERSION" INTEGER,
"CREATE_USER" VARCHAR2(50),
"CREATE_TIME" DATETIME(6),
"IS_LATEST" NUMBER(1,0),
NOT CLUSTER PRIMARY KEY("ID"),
CONSTRAINT "FK_SYS_USER_ROLE_LV_USER" FOREIGN KEY("USER_ID") REFERENCES "JARIACEPLAT"."SYS_USER"("ID") ON DELETE CASCADE  WITH INDEX ) STORAGE(ON "JARIACEPLAT", CLUSTERBTR) ;
COMMENT ON TABLE "JARIACEPLAT"."SYS_USER_ROLE_LV" IS '用户角色历史数据主表';
COMMENT ON COLUMN "JARIACEPLAT"."SYS_USER_ROLE_LV"."CREATE_TIME" IS '创建时间';
COMMENT ON COLUMN "JARIACEPLAT"."SYS_USER_ROLE_LV"."CREATE_USER" IS '创建用户';
COMMENT ON COLUMN "JARIACEPLAT"."SYS_USER_ROLE_LV"."ID" IS '主键';
COMMENT ON COLUMN "JARIACEPLAT"."SYS_USER_ROLE_LV"."IS_LATEST" IS '是否最新';
COMMENT ON COLUMN "JARIACEPLAT"."SYS_USER_ROLE_LV"."USER_ID" IS '用户ID';
COMMENT ON COLUMN "JARIACEPLAT"."SYS_USER_ROLE_LV"."VERSION" IS '版本号';

/**
   用户角色历史版本数据
 */
CREATE TABLE "JARIACEPLAT"."SYS_USER_ROLE_V"
(
"ID" VARCHAR2(50) NOT NULL,
"ROLE_ID" VARCHAR2(50),
"LV_ID" VARCHAR2(50),
NOT CLUSTER PRIMARY KEY("ID"),
CONSTRAINT "FK_SYS_USER_ROLE_V_LV" FOREIGN KEY("LV_ID") REFERENCES "JARIACEPLAT"."SYS_USER_ROLE_LV"("ID") ON DELETE CASCADE ) STORAGE(ON "JARIACEPLAT", CLUSTERBTR) ;
COMMENT ON TABLE "JARIACEPLAT"."SYS_USER_ROLE_V" IS '用户角色历史版本数据';
COMMENT ON COLUMN "JARIACEPLAT"."SYS_USER_ROLE_V"."ID" IS '主键';
COMMENT ON COLUMN "JARIACEPLAT"."SYS_USER_ROLE_V"."LV_ID" IS '主表ID';
COMMENT ON COLUMN "JARIACEPLAT"."SYS_USER_ROLE_V"."ROLE_ID" IS '角色ID';


CREATE TABLE "JARIACEPLAT"."BD_JOB"
(
"ID" VARCHAR2(50) NOT NULL,
"NAME" VARCHAR2(50),
"SORT_INDEX" INTEGER,
"DATA_VERSION" VARCHAR2(50) DEFAULT 0,
"GROUP_ID" VARCHAR2(50),
CLUSTER PRIMARY KEY("ID"),
CONSTRAINT "FK_BD_JOB_GROUP_ID" FOREIGN KEY("GROUP_ID") REFERENCES "JARIACEPLAT"."ORG_GROUP"("ID") ON DELETE CASCADE ) STORAGE(ON "JARIACEPLAT", CLUSTERBTR) ;
COMMENT ON TABLE "JARIACEPLAT"."BD_JOB" IS '职务表';
COMMENT ON COLUMN "JARIACEPLAT"."BD_JOB"."DATA_VERSION" IS '数据版本';
COMMENT ON COLUMN "JARIACEPLAT"."BD_JOB"."GROUP_ID" IS '集团ID';
COMMENT ON COLUMN "JARIACEPLAT"."BD_JOB"."ID" IS '主键';
COMMENT ON COLUMN "JARIACEPLAT"."BD_JOB"."NAME" IS '职务名称';
COMMENT ON COLUMN "JARIACEPLAT"."BD_JOB"."SORT_INDEX" IS '排序号';

CREATE TABLE "JARIACEPLAT"."BD_POST"
(
"ID" VARCHAR2(50) NOT NULL,
"NAME" VARCHAR2(50),
"DEPARTMENT_ID" VARCHAR2(50),
"GROUP_ID" VARCHAR2(50),
"ORGANIZATION_ID" VARCHAR2(50),
"DATA_VERSION" VARCHAR2(50) DEFAULT 0,
"SORT_INDEX" INTEGER DEFAULT 0,
NOT CLUSTER PRIMARY KEY("ID"),
CONSTRAINT "FK_BD_POST_GROUP_ID" FOREIGN KEY("GROUP_ID") REFERENCES "JARIACEPLAT"."ORG_GROUP"("ID") ON DELETE CASCADE ,
CONSTRAINT "FK_BD_POST_ORG_ID" FOREIGN KEY("ORGANIZATION_ID") REFERENCES "JARIACEPLAT"."ORG_ORGANIZATION"("ID") ON DELETE CASCADE ,
CONSTRAINT "FK_BD_POST_DEP_ID" FOREIGN KEY("DEPARTMENT_ID") REFERENCES "JARIACEPLAT"."ORG_DEPARTMENT"("ID") ON DELETE CASCADE ) STORAGE(ON "JARIACEPLAT", CLUSTERBTR) ;
COMMENT ON TABLE "JARIACEPLAT"."BD_POST" IS '部门岗位表';
COMMENT ON COLUMN "JARIACEPLAT"."BD_POST"."DATA_VERSION" IS '数据版本';
COMMENT ON COLUMN "JARIACEPLAT"."BD_POST"."DEPARTMENT_ID" IS '所属部门主键';
COMMENT ON COLUMN "JARIACEPLAT"."BD_POST"."GROUP_ID" IS '所属集团主键';
COMMENT ON COLUMN "JARIACEPLAT"."BD_POST"."ID" IS '主键';
COMMENT ON COLUMN "JARIACEPLAT"."BD_POST"."NAME" IS '岗位名称';
COMMENT ON COLUMN "JARIACEPLAT"."BD_POST"."ORGANIZATION_ID" IS '所属业务单元主键';
COMMENT ON COLUMN "JARIACEPLAT"."BD_POST"."SORT_INDEX" IS '排序号';

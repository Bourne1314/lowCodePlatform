ALTER TABLE "SYS_ROLE" DROP COLUMN "DEP_ID";

CREATE TABLE "SYS_ROLE_DEP"
(
"ID" VARCHAR2(200) NOT NULL,
"ROLE_ID" VARCHAR2(200),
"DEP_ID" VARCHAR2(200),
"CREATE_TIME" DATETIME(6),
NOT CLUSTER PRIMARY KEY("ID"))  ;
COMMENT ON TABLE "SYS_ROLE_DEP" IS '角色部门关联表';
COMMENT ON COLUMN "SYS_ROLE_DEP"."ID" IS '主键';
COMMENT ON COLUMN "SYS_ROLE_DEP"."DEP_ID" IS '部门主键';
COMMENT ON COLUMN "SYS_ROLE_DEP"."ROLE_ID" IS '角色主键';
COMMENT ON COLUMN "SYS_ROLE_DEP"."CREATE_TIME" IS '创建时间';

-- alter table "SYS_ROLE" add column("ROLE_SCOPE" NUMBER(10,0));

/*
添加回收人ID
2021.8.19
 */
alter table "FILE_INFO" add column("RECYCLER_ID" VARCHAR2(50));
comment on column "FILE_INFO"."RECYCLER_ID" is '回收人ID';

/**
 *  roleScope添加
 * 2021.8.31
 */
alter table "SYS_ROLE" add column("ROLE_SCOPE" NUMBER(10,0) DEFAULT 0);
comment on column "SYS_ROLE"."ROLE_SCOPE" is '角色范围 0：本地角色 1：全局角色';
/**
 *  集团管理员添加角色管理菜单
 * 2021.8.31
 */
insert into "SYS_AUTH_ROLE" ("ID","AUTH_ID","ROLE_ID","SIGN","IS_REVOKE","CREATE_USER","CREATE_TIME","UPDATE_TIME","REMARK") values ('48753fe2ef32403dbf9dc989d0651236', '8d4d2caee7f548889988da2747dfaa4a', 'groupadmin', null, 0, null, null, null, null);
insert into "SYS_AUTH_ROLE_V" ("ID","AUTH_ID","ROLE_ID","SIGN","IS_REVOKE","CREATE_USER","CREATE_TIME","UPDATE_TIME","REMARK","LV_ID") values ('48453c7b6ba04f1fa8e4cc36be871232', '8d4d2caee7f548889988da2747dfaa4a', 'groupadmin', null, 0, null, null, null, null, 'groupadmin');
insert into "SYS_AUTH_ROLE" ("ID","AUTH_ID","ROLE_ID","SIGN","IS_REVOKE","CREATE_USER","CREATE_TIME","UPDATE_TIME","REMARK") values ('12753fe2ef32403dbf9dc989d0651236', '8d4d2caee7f548889988da2747dfaa4a', 'groupsec', null, 0, null, null, null, null);
insert into "SYS_AUTH_ROLE_V" ("ID","AUTH_ID","ROLE_ID","SIGN","IS_REVOKE","CREATE_USER","CREATE_TIME","UPDATE_TIME","REMARK","LV_ID") values ('yu453c7b6ba04f1fa8e4cc36be871232', '8d4d2caee7f548889988da2747dfaa4a', 'groupsec', null, 0, null, null, null, null, 'groupsec');


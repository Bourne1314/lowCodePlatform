ALTER TABLE SYS_ROLE DROP COLUMN DEP_ID;

CREATE TABLE SYS_ROLE_DEP
(
ID VARCHAR2(200) NOT NULL,
ROLE_ID VARCHAR2(200),
DEP_ID VARCHAR2(200),
CREATE_TIME DATE,
CONSTRAINT FILE_CONFIGURATION_PK PRIMARY KEY (ID))  ;
COMMENT ON TABLE SYS_ROLE_DEP IS '角色部门关联表';
COMMENT ON COLUMN SYS_ROLE_DEP.ID IS '主键';
COMMENT ON COLUMN SYS_ROLE_DEP.DEP_ID IS '部门主键';
COMMENT ON COLUMN SYS_ROLE_DEP.ROLE_ID IS '角色主键';
COMMENT ON COLUMN SYS_ROLE_DEP.CREATE_TIME IS '创建时间';


-- ALTER TABLE SYS_ROLE ADD (ROLE_TYPE  INT);

/**
 * 增加文件回收站
 * @author JonnyJiang
 * @date 2020/7/8 10:59
 */
alter table "FILE_INFO" add column("IS_IN_RECYCLE_BIN" NUMBER(1, 0));
alter table "FILE_INFO" add column("RECYCLE_TIME" DATE);
alter table "FILE_INFO" add column("RECYCLE_FILE_ID" VARCHAR2(50));

comment on column "FILE_INFO"."IS_IN_RECYCLE_BIN" is '是否在回收站';
comment on column "FILE_INFO"."RECYCLE_TIME" is '回收时间';
comment on column "FILE_INFO"."RECYCLE_FILE_ID" is '回收文件ID';

/*
添加回收人ID
2021/8/19
 */
alter table "FILE_INFO" add column("RECYCLER_ID" VARCHAR2(50));
comment on column "FILE_INFO"."RECYCLER_ID" is '回收人ID';

/**
 *  roleScope添加
 * 2021.8.31
 */
alter table "SYS_ROLE" add column("ROLE_SCOPE" NUMBER(1, 0));
comment on column "SYS_ROLE"."ROLE_SCOPE" is '角色范围 0：本地角色 1：全局角色';
/**
 *  集团管理员添加角色管理菜单
 * 2021.8.31
 */
Insert into SYS_AUTH_ROLE
   (ID, AUTH_ID, ROLE_ID, IS_REVOKE, AUTH_NAME,
    ROLE_NAME, TRACE_ID)
 Values
   ('48753fe2ef32403dbf9dc989d0651236', '8d4d2caee7f548889988da2747dfaa4a', 'groupadmin', 0, '角色管理',
    '集团系统管理员', '');
Insert into SYS_AUTH_ROLE_V
   (ID, AUTH_ID, ROLE_ID, IS_REVOKE, LV_ID,
    TRACE_ID)
 Values
   ('48453c7b6ba04f1fa8e4cc36be871232', '8d4d2caee7f548889988da2747dfaa4a', 'groupadmin', 0, 'groupadmin',
    '');
Insert into SYS_AUTH_ROLE
   (ID, AUTH_ID, ROLE_ID, IS_REVOKE, AUTH_NAME,
    ROLE_NAME, TRACE_ID)
 Values
   ('12753fe2ef32403dbf9dc989d0651236', '8d4d2caee7f548889988da2747dfaa4a', 'groupsec', 0, '角色管理',
    '集团系统管理员', '');
Insert into SYS_AUTH_ROLE_V
   (ID, AUTH_ID, ROLE_ID, IS_REVOKE, LV_ID,
    TRACE_ID)
 Values
   ('yu453c7b6ba04f1fa8e4cc36be871232', '8d4d2caee7f548889988da2747dfaa4a', 'groupsec', 0, 'groupsec',
    '');
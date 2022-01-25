/**
  2019/09/27:修改集团三员管理
 */
comment on table "SYS_AUTH_SCOPE_APP" is '系统管理-应用三员-授权应用';

comment on table "SYS_AUTH_SCOPE_ORG" is '系统管理-应用三员-授权组织';

comment on table "SYS_AUTH_SCOPE_USER_GROUP" is '系统管理-应用三员-授权用户组';
/**
2019/12/5
 */
alter table SYS_AUDIT_LOG add ("TYPE" VARCHAR2(10 CHAR));
comment on column "SYS_AUDIT_LOG"."TYPE" is '操作类别';

alter table SYS_AUDIT_LOG add ("TITLE" VARCHAR2(300 CHAR));
comment on column "SYS_AUDIT_LOG"."TITLE" is '操作标题';

alter table SYS_AUDIT_LOG_BACKUP add ("TYPE" VARCHAR2(200 CHAR));
comment on column "SYS_AUDIT_LOG_BACKUP"."TYPE" is '操作类别';

alter table SYS_AUDIT_LOG_BACKUP add ("TITLE" VARCHAR2(600 CHAR));
comment on column "SYS_AUDIT_LOG_BACKUP"."TITLE" is '操作标题';

TRUNCATE TABLE SYS_AUDIT_LOG;
alter table SYS_AUDIT_LOG  MODIFY (OP_CONTENT LONG) ;
alter table SYS_AUDIT_LOG  MODIFY (OP_CONTENT CLOB) ;
TRUNCATE TABLE SYS_AUDIT_LOG_BACKUP;
alter table SYS_AUDIT_LOG_BACKUP  MODIFY (OP_CONTENT LONG) ;
alter table SYS_AUDIT_LOG_BACKUP  MODIFY (OP_CONTENT CLOB) ;
TRUNCATE TABLE SYS_AUDIT_LOG_COUNT;

INSERT INTO SYS_MENU ("ID", "NAME", "PARENT_ID", "APP_ID", "PARENT_NAME", "URL", "TYPE", "ICON", "AUTH_ID", "BTN_AUTH", "IS_LEAF", "SORT_INDEX", "ALL_ORDER", "SORT_PATH", "OPEN_STYLE", "CLOSE_NOTICE", "CREATE_USER", "CREATE_TIME", "UPDATE_TIME", "LEAF", "MENU_AUTH", "REMARK", "DATA_VERSION", "IS_IFRAME")
VALUES ('19ea2ad7ea4a4e54acdaff4c84bb8370', '应用审计日志', 'e3b15ac90286450fbe5a721c9f542451', 'platform', '系统管理',
'auditLogManage/views/AppAuditLog', '1', 'fa fa-window-maximize', '19ea2ad7ea4a4e54acdaff4c84bb8370', NULL, NULL, 5, NULL, '00020005', NULL, NULL, 'dde891f1a0c34a12add515b3b3ab7763', NULL, NULL, NULL, NULL, NULL, '0', '0');


insert into "SYS_AUTH" ("ID","NAME","APP_ID","REMARK","SORT_INDEX","PARENT_ID","CREATE_USER","CREATE_TIME","UPDATE_TIME","SORT_PATH","IS_ORG_ADMIN","IS_USER_GROUP_ADMIN","CODE","DATA_VERSION") values ('19ea2ad7ea4a4e54acdaff4c84bb8370', '应用运行日志', 'platform', null,5, 'e3b15ac90286450fbe5a721c9f542451', null, null, null, '00020005', 0, 0, 'workingLogManage.views.aceWorkingLog', 0);

insert into "SYS_AUTH_ROLE" ("ID","AUTH_ID","ROLE_ID","SIGN","IS_REVOKE","CREATE_USER","CREATE_TIME","UPDATE_TIME","REMARK") values ('29ea2ad7ea4a4e54acdaff4c84bb8370', '19ea2ad7ea4a4e54acdaff4c84bb8370', 'sec', null, 0, null, null, null, null);

insert into "SYS_AUTH_ROLE_V" ("ID","AUTH_ID","ROLE_ID","SIGN","IS_REVOKE","CREATE_USER","CREATE_TIME","UPDATE_TIME","REMARK","LV_ID") values ('29ea2ad7ea4a4e54acdaff4c84bb8370', '19ea2ad7ea4a4e54acdaff4c84bb8370', 'sec', null, 0, null, null, null, null, 'sec');

insert into "SYS_AUTH_ROLE" ("ID","AUTH_ID","ROLE_ID","SIGN","IS_REVOKE","CREATE_USER","CREATE_TIME","UPDATE_TIME","REMARK") values ('59ea2ad7ea4a4e54acdaff4c84bb8370', '19ea2ad7ea4a4e54acdaff4c84bb8370', 'groupsec', null, 0, null, null, null, null);

insert into "SYS_AUTH_ROLE_V" ("ID","AUTH_ID","ROLE_ID","SIGN","IS_REVOKE","CREATE_USER","CREATE_TIME","UPDATE_TIME","REMARK","LV_ID") values ('59ea2ad7ea4a4e54acdaff4c84bb8370', '19ea2ad7ea4a4e54acdaff4c84bb8370', 'groupsec', null, 0, null, null, null, null, 'groupsec');

insert into "SYS_AUTH_ROLE" ("ID","AUTH_ID","ROLE_ID","SIGN","IS_REVOKE","CREATE_USER","CREATE_TIME","UPDATE_TIME","REMARK") values ('19ea2ad7ea4a4e54acdaff4c84bb8370', '19ea2ad7ea4a4e54acdaff4c84bb8370', 'appsec', null, 0, null, null, null, null);

insert into "SYS_AUTH_ROLE_V" ("ID","AUTH_ID","ROLE_ID","SIGN","IS_REVOKE","CREATE_USER","CREATE_TIME","UPDATE_TIME","REMARK","LV_ID") values ('19ea2ad7ea4a4e54acdaff4c84bb8370', '19ea2ad7ea4a4e54acdaff4c84bb8370', 'appsec', null, 0, null, null, null, null, 'appsec');

/**
2019/12/6
 */
alter table SYS_AUDIT_LOG add ("TYPE_TAG" VARCHAR2(50 CHAR));
comment on column "SYS_AUDIT_LOG"."TYPE_TAG" is '操作类别标签';
alter table SYS_AUDIT_LOG  MODIFY ("TYPE" VARCHAR2(50 CHAR)) ;
alter table SYS_AUDIT_LOG_BACKUP add ("TYPE_TAG" VARCHAR2(200 CHAR));
comment on column "SYS_AUDIT_LOG_BACKUP"."TYPE_TAG" is '操作类别标签';

/**
2019/12/9
 */
--
-- SYS_APP_MENU_DISPLAY  (Table)
--
CREATE TABLE SYS_APP_MENU_DISPLAY
(
  ID               VARCHAR2(50 BYTE)            NOT NULL,
  APP_ID           VARCHAR2(50 BYTE),
  MENU_ID          VARCHAR2(50 BYTE),
  USER_ID          VARCHAR2(50 BYTE),
  MENU_ICON        VARCHAR2(200 BYTE),
  MENU_URL         VARCHAR2(200 BYTE),
  MENU_NAME        VARCHAR2(50 BYTE),
  MENU_SORT_INDEX  INTEGER
);

COMMENT ON TABLE SYS_APP_MENU_DISPLAY IS '应用下菜单显示在平台管控台的信息表';

COMMENT ON COLUMN SYS_APP_MENU_DISPLAY.ID IS '主键';

COMMENT ON COLUMN SYS_APP_MENU_DISPLAY.APP_ID IS '应用ID';

COMMENT ON COLUMN SYS_APP_MENU_DISPLAY.MENU_ID IS '菜单ID';

COMMENT ON COLUMN SYS_APP_MENU_DISPLAY.USER_ID IS '用户ID';

COMMENT ON COLUMN SYS_APP_MENU_DISPLAY.MENU_ICON IS '菜单图标';

COMMENT ON COLUMN SYS_APP_MENU_DISPLAY.MENU_URL IS '菜单URL';

COMMENT ON COLUMN SYS_APP_MENU_DISPLAY.MENU_NAME IS '菜单名称';

COMMENT ON COLUMN SYS_APP_MENU_DISPLAY.MENU_SORT_INDEX IS '菜单序号';



--
-- SYS_APP_MENU_DISPLAY_PK  (Index)
--
CREATE UNIQUE INDEX SYS_APP_MENU_DISPLAY_PK ON SYS_APP_MENU_DISPLAY
(ID)
LOGGING;


--
-- Non Foreign Key Constraints for Table SYS_APP_MENU_DISPLAY
--
ALTER TABLE SYS_APP_MENU_DISPLAY ADD (
  CONSTRAINT SYS_APP_MENU_DISPLAY_PK
  PRIMARY KEY
  (ID)
  USING INDEX SYS_APP_MENU_DISPLAY_PK
  ENABLE VALIDATE);

--
-- Foreign Key Constraints for Table SYS_APP_MENU_DISPLAY
--
ALTER TABLE SYS_APP_MENU_DISPLAY ADD (
  CONSTRAINT FK_DISPLAY_APP_ID
  FOREIGN KEY (APP_ID)
  REFERENCES SYS_GROUP_APP (ID)
  ON DELETE CASCADE
  ENABLE VALIDATE,
  CONSTRAINT FK_DISPLAY_MENU_ID
  FOREIGN KEY (MENU_ID)
  REFERENCES SYS_MENU (ID)
  ON DELETE CASCADE
  ENABLE VALIDATE,
  CONSTRAINT FK_DISPLAY_USER_ID
  FOREIGN KEY (USER_ID)
  REFERENCES SYS_USER (ID)
  ON DELETE CASCADE
  ENABLE VALIDATE);

/**
2019/12/12
 */
--
-- SYS_WAIT_GRANT_USER  (Table)
--
CREATE TABLE SYS_WAIT_GRANT_USER
(
  ID       VARCHAR2(50 BYTE)                    NOT NULL,
  USER_ID  VARCHAR2(50 BYTE),
  APP_ID   VARCHAR2(50 BYTE)
);

COMMENT ON TABLE SYS_WAIT_GRANT_USER IS '角色用户授予—待激活的用户表';

COMMENT ON COLUMN SYS_WAIT_GRANT_USER.ID IS '主键';

COMMENT ON COLUMN SYS_WAIT_GRANT_USER.USER_ID IS '用户ID';

COMMENT ON COLUMN SYS_WAIT_GRANT_USER.APP_ID IS '应用ID';



--
-- SYS_WAIT_GRANT_USER_PK  (Index)
--
CREATE UNIQUE INDEX SYS_WAIT_GRANT_USER_PK ON SYS_WAIT_GRANT_USER
(ID)
LOGGING;


--
-- Non Foreign Key Constraints for Table SYS_WAIT_GRANT_USER
--
ALTER TABLE SYS_WAIT_GRANT_USER ADD (
  CONSTRAINT SYS_WAIT_GRANT_USER_PK
  PRIMARY KEY
  (ID)
  USING INDEX SYS_WAIT_GRANT_USER_PK
  ENABLE VALIDATE);

--
-- Foreign Key Constraints for Table SYS_WAIT_GRANT_USER
--
ALTER TABLE SYS_WAIT_GRANT_USER ADD (
  CONSTRAINT FK_WAIT_GRANT_APP_ID
  FOREIGN KEY (APP_ID)
  REFERENCES SYS_GROUP_APP (ID)
  ON DELETE CASCADE
  ENABLE VALIDATE,
  CONSTRAINT FK_WAIT_GRANT_USER_ID
  FOREIGN KEY (USER_ID)
  REFERENCES SYS_USER (ID)
  ON DELETE CASCADE
  ENABLE VALIDATE);

ALTER TABLE "SYS_USER_ROLE" DROP COLUMN "IS_ACTIVATED";

alter table SYS_API_MIX add ("AUTH_ID" VARCHAR2(50 CHAR));
comment on column "SYS_API_MIX"."AUTH_ID" is '权限ID';

alter table SYS_API_MIX add constraint "FK_API_MIX_AUTH_ID" foreign key("AUTH_ID") references "SYS_AUTH"("ID") on delete cascade;

/**
2019/12/18
 */
alter table "SYS_APP_MENU_DISPLAY" DROP CONSTRAINT FK_DISPLAY_MENU_ID;

alter table "SYS_AUTH_SCOPE_APP" DROP CONSTRAINT FK_SYS_AUTH_SCOPE_APP_AUTH;

alter table "SYS_AUTH_SCOPE_ORG" DROP CONSTRAINT FK_SYS_AUTH_SCOPE_ORG_AUTH_ID;

alter table "SYS_AUTH_SCOPE_USER_GROUP" DROP CONSTRAINT FK_SYS_AUTH_SCOPE_UG_AUTH_ID;

alter table SYS_AUTH_MIX add ("APP_ID" VARCHAR2(50 CHAR));
comment on column "SYS_AUTH_MIX"."APP_ID" is '应用ID';

update sys_auth_mix a set a.app_id = (select b.app_id from sys_auth b where b.id = a.auth_id ) , a.data_version=0;

alter table SYS_AUTH_MIX modify  data_version default 0;

/**
2019/12/19
 */
alter table SYS_API_MIX add ("APP_ID" VARCHAR2(50 CHAR));
comment on column "SYS_API_MIX"."APP_ID" is '应用ID';

update SYS_API_MIX a set a.app_id = (select b.app_id from sys_auth b where b.id = a.auth_id ) , a.data_version=0;

alter table SYS_API_MIX modify  data_version default 0;
/**
2019/12/24
 */
alter table SYS_GROUP_APP add ("IS_LOCK" INTEGER DEFAULT 0);
comment on column "SYS_GROUP_APP"."IS_LOCK" is '应用是否被锁住 1是 0不是';
alter table SYS_GROUP_APP add ("LOCK_TIME" DATE);
comment on column "SYS_GROUP_APP"."LOCK_TIME" is '应用锁 操作时间';
alter table SYS_CONFIG add ("UPDATE_TYPE" INTEGER DEFAULT 0);
comment on column "SYS_CONFIG"."UPDATE_TYPE" is '配置项更新策略 0立即更新 1重启更新';

/**
2019/12/25
 */
alter table SYS_USER modify  IP_ADDRESS varchar2(1000);
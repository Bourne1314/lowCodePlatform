/**
  2019/11/1:修改菜单名称
 */
UPDATE SYS_MENU SET NAME='组织管理' WHERE ID='bc316279a4a9416a8e20019821e640af';
UPDATE SYS_MENU SET NAME='集团三员' WHERE ID='5fc1b8842fa04faea8f39beb866fd511';
UPDATE SYS_MENU SET NAME='系统配置' WHERE ID='ac2a8ce16a614324b5ed4e9c24e7e904';
UPDATE SYS_MENU SET NAME='附件配置' WHERE ID='ffc03579b06a4b7fae6c51c9ff48b311';
UPDATE SYS_MENU SET NAME='附件存储' WHERE ID='ffc03579b06a4b7fae6c51c9ff48b312';
UPDATE SYS_MENU SET NAME='通道扩展' WHERE ID='yut76279a4a9416a8e20019821e6g45t';
UPDATE SYS_MENU SET NAME='服务配置' WHERE ID='12315ac90286450fbe5a721c9f542344';
UPDATE SYS_MENU SET NAME='命名空间' WHERE ID='23315ac90286450fbe5a721c9f542344';
UPDATE SYS_MENU SET NAME='服务列表' WHERE ID='34415ac90286450fbe5a721c9f542344';

UPDATE SYS_MENU SET NAME='证件管理' WHERE ID='a16344b1790e41e98615f6cd19587412';
UPDATE SYS_MENU SET NAME='应用三员' WHERE ID='67e1114e83214aeb8a3ec0a6822fdd30';
/**
2019/11/5
 */
alter table "SYS_CONFIG" add column("LONG_VALUE" CLOB);
comment on column "SYS_CONFIG"."LONG_VALUE" is '长型值，储存背景图片等';

/**
2019/11/6
 */
UPDATE SYS_MENU SET PARENT_ID='11316279a4a9416a8e20019821e640af' , PARENT_NAME='配置管理' , SORT_INDEX=11 , SORT_PATH='00030011' WHERE ID='131e140bc2bd4361af5ee488f92b22ad';
UPDATE SYS_AUTH SET PARENT_ID='11316279a4a9416a8e20019821e640af' ,  SORT_INDEX=11 , SORT_PATH='00030011' WHERE ID='131e140bc2bd4361af5ee488f92b22ad';

alter table "SYS_DICT" add column("GROUP_ID" VARCHAR2(50));
alter table "SYS_DICT" add column("SCOPE" INTEGER);
comment on column "SYS_DICT"."GROUP_ID" is '集团ID';
comment on column "SYS_DICT"."SCOPE" is '配置范围 1租户 2集团 3应用';

alter table "SYS_DICT_VALUE" add column("GROUP_ID" VARCHAR2(50));
alter table "SYS_DICT_VALUE" add column("SCOPE" INTEGER);
comment on column "SYS_DICT_VALUE"."GROUP_ID" is '集团ID';
comment on column "SYS_DICT_VALUE"."SCOPE" is '配置范围 1租户 2集团 3应用';

insert into "SYS_AUTH_ROLE" ("ID","AUTH_ID","ROLE_ID","SIGN","IS_REVOKE","CREATE_USER","CREATE_TIME","UPDATE_TIME","REMARK") values ('12323fe2ef32403dbf9dc989d0651252', '131e140bc2bd4361af5ee488f92b22ad', 'admin', null, 0, null, null, null, null);
insert into "SYS_AUTH_ROLE" ("ID","AUTH_ID","ROLE_ID","SIGN","IS_REVOKE","CREATE_USER","CREATE_TIME","UPDATE_TIME","REMARK") values ('12323fe2ef32403dbf9dc989d0651253', '131e140bc2bd4361af5ee488f92b22ad', 'groupadmin', null, 0, null, null, null, null);

insert into "SYS_AUTH_ROLE_V" ("ID","AUTH_ID","ROLE_ID","SIGN","IS_REVOKE","CREATE_USER","CREATE_TIME","UPDATE_TIME","REMARK","LV_ID") values ('12323fe2ef32403dbf9dc989d0651252', '131e140bc2bd4361af5ee488f92b22ad', 'admin', null, 0, null, null, null, null, 'admin');
insert into "SYS_AUTH_ROLE_V" ("ID","AUTH_ID","ROLE_ID","SIGN","IS_REVOKE","CREATE_USER","CREATE_TIME","UPDATE_TIME","REMARK","LV_ID") values ('12323fe2ef32403dbf9dc989d0651253', '131e140bc2bd4361af5ee488f92b22ad', 'groupadmin', null, 0, null, null, null, null, 'groupadmin');


DELETE FROM SYS_AUTH WHERE ID = '131e140bc2bd4361af5ee488f92b2111';

insert into "SYS_AUTH" ("ID","NAME","APP_ID","REMARK","SORT_INDEX","PARENT_ID","CREATE_USER","CREATE_TIME","UPDATE_TIME","SORT_PATH","IS_ORG_ADMIN","IS_USER_GROUP_ADMIN","CODE","DATA_VERSION") values ('1160858109884c5095eed968472c0234', '租户级字典维护', 'platform', null, 1, '131e140bc2bd4361af5ee488f92b22ad', null, null, null, '000300110001', 0, 0, 'platForm.dictManage.Dict.TenantMaintain', 0);
insert into "SYS_AUTH" ("ID","NAME","APP_ID","REMARK","SORT_INDEX","PARENT_ID","CREATE_USER","CREATE_TIME","UPDATE_TIME","SORT_PATH","IS_ORG_ADMIN","IS_USER_GROUP_ADMIN","CODE","DATA_VERSION") values ('1160858109884c5095eed968472c0235', '集团级字典维护', 'platform', null, 2, '131e140bc2bd4361af5ee488f92b22ad', null, null, null, '000300110002', 0, 0, 'platForm.dictManage.Dict.GroupMaintain', 0);
insert into "SYS_AUTH" ("ID","NAME","APP_ID","REMARK","SORT_INDEX","PARENT_ID","CREATE_USER","CREATE_TIME","UPDATE_TIME","SORT_PATH","IS_ORG_ADMIN","IS_USER_GROUP_ADMIN","CODE","DATA_VERSION") values ('1160858109884c5095eed968472c0236', '应用级字典维护', 'platform', null, 3, '131e140bc2bd4361af5ee488f92b22ad', null, null, null, '000300110003', 0, 0, 'platForm.dictManage.Dict.AppMaintain', 0);

insert into "SYS_AUTH_ROLE" ("ID","AUTH_ID","ROLE_ID","SIGN","IS_REVOKE","CREATE_USER","CREATE_TIME","UPDATE_TIME","REMARK") values ('134757e07a3249d6ad180da0b4681111', '1160858109884c5095eed968472c0234', 'admin', null, 0, null, null, null, null);
insert into "SYS_AUTH_ROLE" ("ID","AUTH_ID","ROLE_ID","SIGN","IS_REVOKE","CREATE_USER","CREATE_TIME","UPDATE_TIME","REMARK") values ('134757e07a3249d6ad180da0b4681112', '1160858109884c5095eed968472c0235', 'groupadmin', null, 0, null, null, null, null);
insert into "SYS_AUTH_ROLE" ("ID","AUTH_ID","ROLE_ID","SIGN","IS_REVOKE","CREATE_USER","CREATE_TIME","UPDATE_TIME","REMARK") values ('134757e07a3249d6ad180da0b4681113', '1160858109884c5095eed968472c0236', 'appadmin', null, 0, null, null, null, null);

insert into "SYS_AUTH_ROLE_V" ("ID","AUTH_ID","ROLE_ID","SIGN","IS_REVOKE","CREATE_USER","CREATE_TIME","UPDATE_TIME","REMARK","LV_ID") values ('134757e07a3249d6ad180da0b4681111', '1160858109884c5095eed968472c0234', 'admin', null, 0, null, null, null, null, 'admin');
insert into "SYS_AUTH_ROLE_V" ("ID","AUTH_ID","ROLE_ID","SIGN","IS_REVOKE","CREATE_USER","CREATE_TIME","UPDATE_TIME","REMARK","LV_ID") values ('134757e07a3249d6ad180da0b4681112', '1160858109884c5095eed968472c0235', 'groupadmin', null, 0, null, null, null, null, 'groupadmin');
insert into "SYS_AUTH_ROLE_V" ("ID","AUTH_ID","ROLE_ID","SIGN","IS_REVOKE","CREATE_USER","CREATE_TIME","UPDATE_TIME","REMARK","LV_ID") values ('134757e07a3249d6ad180da0b4681113', '1160858109884c5095eed968472c0236', 'appadmin', null, 0, null, null, null, null, 'appadmin');

alter table "SYS_DICT" DROP CONSTRAINT FK_SYS_DICT_APP_ID;
alter table "SYS_MENU" DROP CONSTRAINT FK_SYS_MENU_AUTH_ID;
/**
2019/11/7
 */

alter table BD_POST add column("TYPE_ID" VARCHAR2(50));
comment on column "BD_POST"."TYPE_ID" is '岗位类别主键';

/**
2019/11/8
 */
DELETE FROM SYS_AUTH WHERE ID IN ('ffc03579b06a4b7fae6c51c9ff481111','ffc03579b06a4b7fae6c51c9ff481112','ffc03579b06a4b7fae6c51c9ff481113');
DELETE FROM SYS_AUTH_ROLE_V WHERE AUTH_ID='ffc03579b06a4b7fae6c51c9ff481111' AND ROLE_ID='admin';
DELETE FROM SYS_AUTH_ROLE_V WHERE AUTH_ID='ffc03579b06a4b7fae6c51c9ff481112' AND ROLE_ID='groupadmin';
DELETE FROM SYS_AUTH_ROLE_V WHERE AUTH_ID='ffc03579b06a4b7fae6c51c9ff481113' AND ROLE_ID='appadmin';
DELETE FROM SYS_AUTH_ROLE_V WHERE AUTH_ID='ffc03579b06a4b7fae6c51c9ff48b311' AND ROLE_ID='admin';
DELETE FROM SYS_AUTH_ROLE_V WHERE AUTH_ID='ffc03579b06a4b7fae6c51c9ff48b311' AND ROLE_ID='groupadmin';

/**
2019/11/11
 */
alter table "SYS_GROUP_DATASOURCE" add column("NAME" VARCHAR2(50));
comment on column "SYS_GROUP_DATASOURCE"."NAME" is '数据源名称';
/**
2019/11/13
 */
insert into "SYS_CONFIG" ("NAME","VALUE","REMARK","CREATE_TIME","UPDATE_TIME","SORT_INDEX","ID","CREATE_USER","APP_ID","GROUP_ID","DATA_VERSION","SCOPE","TYPE","LONG_VALUE") values ('platformName', '企业管理平台', '平台名称',null, null, 2, 'dadasdasddadsadasdasdasd', null, '', '', 0, 1, null, null);

alter table "SYS_WAIT_GRANT_AUTH" add column("APP_ID" VARCHAR2(50));
comment on column "SYS_WAIT_GRANT_AUTH"."APP_ID" is '应用ID';

alter table SYS_USER modify  PINYIN varchar2(100);

alter table SYS_AUDIT_LOG add ("USER_TYPE" VARCHAR2(50 CHAR));
comment on column "SYS_AUDIT_LOG"."USER_TYPE" is '用户类型（0租户管理员,1集团管理员,2应用管理员,3普通用户）';

alter table SYS_AUDIT_LOG_BACKUP add ("USER_TYPE" VARCHAR2(100 CHAR));
comment on column "SYS_AUDIT_LOG_BACKUP"."USER_TYPE" is '用户类型（0租户管理员,1集团管理员,2应用管理员,3普通用户）';

update
sys_audit_log a set
a.user_type =  (select b.user_type from sys_user b where a.op_name = b.real_name and rownum=1);


/**
2019/11/19
 */
CREATE TABLE "REPORT_TYPE"
(
"ID" VARCHAR2(50) NOT NULL,
"NAME" VARCHAR2(500),
"APP_ID" VARCHAR2(500),
"SORT" INTEGER DEFAULT 9999,
"DATA_VERSION" INTEGER DEFAULT 0,
"TYPE" INTEGER DEFAULT 1,
"PARENT_ID" VARCHAR2(50),
NOT CLUSTER PRIMARY KEY("ID"),
CONSTRAINT "FK_REPORT_TYPE_APP_ID" FOREIGN KEY("APP_ID") REFERENCES "SYS_GROUP_APP"("ID") ON DELETE CASCADE  WITH INDEX ) STORAGE(ON "JARIACEPLAT", CLUSTERBTR) ;
COMMENT ON TABLE "REPORT_TYPE" IS '报表类别';
COMMENT ON COLUMN "REPORT_TYPE"."APP_ID" IS '应用id';
COMMENT ON COLUMN "REPORT_TYPE"."DATA_VERSION" IS '数据版本';
COMMENT ON COLUMN "REPORT_TYPE"."ID" IS '主键';
COMMENT ON COLUMN "REPORT_TYPE"."NAME" IS '类别名称';
COMMENT ON COLUMN "REPORT_TYPE"."PARENT_ID" IS '父节点ID';
COMMENT ON COLUMN "REPORT_TYPE"."SORT" IS '排序';
COMMENT ON COLUMN "REPORT_TYPE"."TYPE" IS '类别类型 1报表2仪表盘';

CREATE TABLE "REPORT_INFO"
(
"ID" VARCHAR2(50) NOT NULL,
"APP_ID" VARCHAR2(50),
"TYPE_ID" VARCHAR2(50),
"NAME" VARCHAR2(500),
"AUTH" VARCHAR2(2000),
"IS_PUBLIC" NUMBER(1,0) DEFAULT 0,
"REFRESH_TYPE" INTEGER DEFAULT 0,
"SHOW_ITEM" VARCHAR2(2000),
"MRT_STR" CLOB,
"DATA_VERSION" INTEGER DEFAULT 0,
"IS_AUTO_FLIP" NUMBER(1,0),
"REMARKS" VARCHAR2(2000),
NOT CLUSTER PRIMARY KEY("ID"),
CONSTRAINT "FK_REPORT_INFO_APP_ID" FOREIGN KEY("APP_ID") REFERENCES "SYS_GROUP_APP"("ID") ON DELETE CASCADE  WITH INDEX ,
CONSTRAINT "FK_REPORT_INFO_TYPE_ID" FOREIGN KEY("TYPE_ID") REFERENCES "REPORT_TYPE"("ID") ON DELETE CASCADE  WITH INDEX ) STORAGE(ON "JARIACEPLAT", CLUSTERBTR) ;
COMMENT ON TABLE "REPORT_INFO" IS '报表信息';
COMMENT ON COLUMN "REPORT_INFO"."APP_ID" IS '应用id';
COMMENT ON COLUMN "REPORT_INFO"."AUTH" IS '查看权限集合';
COMMENT ON COLUMN "REPORT_INFO"."DATA_VERSION" IS '数据版本';
COMMENT ON COLUMN "REPORT_INFO"."ID" IS '主键';
COMMENT ON COLUMN "REPORT_INFO"."IS_AUTO_FLIP" IS '自动翻页';
COMMENT ON COLUMN "REPORT_INFO"."IS_PUBLIC" IS '公开报表（0不公开1公开）开放报表，无需登录查看';
COMMENT ON COLUMN "REPORT_INFO"."MRT_STR" IS '报表信息';
COMMENT ON COLUMN "REPORT_INFO"."NAME" IS '报表名称';
COMMENT ON COLUMN "REPORT_INFO"."REFRESH_TYPE" IS '自动刷新类型（0不自动刷新，1定时刷新，2推送事假定时刷新）';
COMMENT ON COLUMN "REPORT_INFO"."REMARKS" IS '描述';
COMMENT ON COLUMN "REPORT_INFO"."SHOW_ITEM" IS '查看选项';
COMMENT ON COLUMN "REPORT_INFO"."TYPE_ID" IS '报表类型id';

/**
2019/11/21
 */
--删除管理员针对审计日志的菜单管理
delete from sys_auth_role where auth_id='04d7893c4a4d4345b18b0789af13753f' and role_id in ('admin','groupadmin','appadmin');
delete from sys_auth_role_v where auth_id='04d7893c4a4d4345b18b0789af13753f' and role_id in ('admin','groupadmin','appadmin');

--对已存在管理员的有效权限删除掉审计日志的菜单管理
delete from sys_auth_mix where user_id in (select user_id from sys_user_role where role_id in ('admin','groupadmin','appadmin') and is_activated=1) and auth_id='04d7893c4a4d4345b18b0789af13753f';


--添加日志表role_id
alter table SYS_AUDIT_LOG add ("ROLE_ID" VARCHAR2(50));
comment on column "SYS_AUDIT_LOG"."ROLE_ID" is '该操作人对应的管理员角色';

alter table SYS_AUDIT_LOG_BACKUP add ("ROLE_ID" VARCHAR2(200));
comment on column "SYS_AUDIT_LOG_BACKUP"."ROLE_ID" is '该操作人对应的管理员角色';

alter table SYS_AUDIT_LOG_BACKUP add ("GROUP_ID" VARCHAR2(200));
comment on column "SYS_AUDIT_LOG_BACKUP"."GROUP_ID" is '集团ID';

alter table SYS_AUDIT_LOG_BACKUP add ("APP_ID" VARCHAR2(200));
comment on column "SYS_AUDIT_LOG_BACKUP"."APP_ID" is '应用ID';

--对当前的日志表数据的role_id添加值
update
sys_audit_log a set
a.role_id = (select c.role_id from sys_user_role c where c.user_id = (select b.id from sys_user b where a.op_name=b.real_name and rownum=1) and rownum=1) where a.user_type!=3;


/**
2019-11-21
 */
update org_organization_v set is_business_unit=2 where organization_id in (select id from org_department);
update org_organization set is_business_unit=2 where id in (select id from org_department);
alter table "BD_PERSON_ID_TYPE" DROP CONSTRAINT UK_BD_PERSON_ID_TYPE_CODE;
alter table "ORG_ORGANIZATION" DROP CONSTRAINT UK_ORG_ORG_PID_SORT_INDEX;

/**
2019/11/22
 */
--应用审计员添加角色审计菜单，授权审计菜单
insert into "SYS_AUTH" ("ID","NAME","APP_ID","REMARK","SORT_INDEX","PARENT_ID","CREATE_USER","CREATE_TIME","UPDATE_TIME","SORT_PATH","IS_ORG_ADMIN","IS_USER_GROUP_ADMIN","CODE","DATA_VERSION") values ('frggb3ec3d6e483b86c4923ea64cvbnu', '角色审计', 'platform', null,6, '11erb3ec3d6e483b86c4923ea64cfdd0', null, null, null, '000200090006', 0, 0, 'roleAuditManage.roleAudit', 0);
insert into "SYS_AUTH" ("ID","NAME","APP_ID","REMARK","SORT_INDEX","PARENT_ID","CREATE_USER","CREATE_TIME","UPDATE_TIME","SORT_PATH","IS_ORG_ADMIN","IS_USER_GROUP_ADMIN","CODE","DATA_VERSION") values ('hjuub3ec3d6e483b86c4923ea64cnmui', '授权审计', 'platform', null,7, '11erb3ec3d6e483b86c4923ea64cfdd0', null, null, null, '000200090007', 0, 0, 'grantAuthAuditManage.grantAuthAudit', 0);

insert into "SYS_MENU" ("ID","NAME","PARENT_ID","APP_ID","PARENT_NAME","URL","TYPE","ICON","AUTH_ID","BTN_AUTH","IS_LEAF","SORT_INDEX","ALL_ORDER","SORT_PATH","OPEN_STYLE","CLOSE_NOTICE","CREATE_USER","CREATE_TIME","UPDATE_TIME","LEAF","MENU_AUTH","REMARK","DATA_VERSION","IS_IFRAME") values ('frggb3ec3d6e483b86c4923ea64cvbnu', '角色审计', '11erb3ec3d6e483b86c4923ea64cfdd0', 'platform', '授权管理', 'roleAuditManage/views/RoleAudit', 1, 'fa fa-wpforms', 'frggb3ec3d6e483b86c4923ea64cvbnu', null, null, 6, null, '000200090006', null, null, null, null, null, null, null, null, 0, 0);
insert into "SYS_MENU" ("ID","NAME","PARENT_ID","APP_ID","PARENT_NAME","URL","TYPE","ICON","AUTH_ID","BTN_AUTH","IS_LEAF","SORT_INDEX","ALL_ORDER","SORT_PATH","OPEN_STYLE","CLOSE_NOTICE","CREATE_USER","CREATE_TIME","UPDATE_TIME","LEAF","MENU_AUTH","REMARK","DATA_VERSION","IS_IFRAME") values ('hjuub3ec3d6e483b86c4923ea64cnmui', '授权审计', '11erb3ec3d6e483b86c4923ea64cfdd0', 'platform', '授权管理', 'grantAuthAuditManage/views/GrantAuthAudit', 1, 'fa fa-calendar-o', 'hjuub3ec3d6e483b86c4923ea64cnmui', null, null, 7, null, '000200090007', null, null, null, null, null, null, null, null, 0, 0);

insert into "SYS_AUTH_ROLE" ("ID","AUTH_ID","ROLE_ID","SIGN","IS_REVOKE","CREATE_USER","CREATE_TIME","UPDATE_TIME","REMARK") values ('12323fe2ef32403dbf9dc989d0651254', 'frggb3ec3d6e483b86c4923ea64cvbnu', 'appauditor', null, 0, null, null, null, null);
insert into "SYS_AUTH_ROLE" ("ID","AUTH_ID","ROLE_ID","SIGN","IS_REVOKE","CREATE_USER","CREATE_TIME","UPDATE_TIME","REMARK") values ('12323fe2ef32403dbf9dc989d0651255', 'hjuub3ec3d6e483b86c4923ea64cnmui', 'appauditor', null, 0, null, null, null, null);
insert into "SYS_AUTH_ROLE_V" ("ID","AUTH_ID","ROLE_ID","SIGN","IS_REVOKE","CREATE_USER","CREATE_TIME","UPDATE_TIME","REMARK","LV_ID") values ('12323fe2ef32403dbf9dc989d0651254', 'frggb3ec3d6e483b86c4923ea64cvbnu', 'appauditor', null, 0, null, null, null, null, 'appauditor');
insert into "SYS_AUTH_ROLE_V" ("ID","AUTH_ID","ROLE_ID","SIGN","IS_REVOKE","CREATE_USER","CREATE_TIME","UPDATE_TIME","REMARK","LV_ID") values ('12323fe2ef32403dbf9dc989d0651255', 'hjuub3ec3d6e483b86c4923ea64cnmui', 'appauditor', null, 0, null, null, null, null, 'appauditor');


insert into "SYS_AUTH_ROLE" ("ID","AUTH_ID","ROLE_ID","SIGN","IS_REVOKE","CREATE_USER","CREATE_TIME","UPDATE_TIME","REMARK") values ('12323fe2ef32403dbf9dc989d0651256', '11erb3ec3d6e483b86c4923ea64cfdd0', 'appauditor', null, 0, null, null, null, null);
insert into "SYS_AUTH_ROLE_V" ("ID","AUTH_ID","ROLE_ID","SIGN","IS_REVOKE","CREATE_USER","CREATE_TIME","UPDATE_TIME","REMARK","LV_ID") values ('12323fe2ef32403dbf9dc989d0651256', '11erb3ec3d6e483b86c4923ea64cfdd0', 'appauditor', null, 0, null, null, null, null, 'appauditor');


/**
2019/11/25
 */
alter table SYS_USER_ROLE_V add ("ROLE_NAME" VARCHAR2(50));
comment on column "SYS_USER_ROLE_V"."ROLE_NAME" is '角色名称';

alter table SYS_AUTH_ROLE_V add ("AUTH_NAME" VARCHAR2(50));
comment on column "SYS_AUTH_ROLE_V"."AUTH_NAME" is '权限名称';

alter table SYS_AUTH_USER_V add ("AUTH_NAME" VARCHAR2(50));
comment on column "SYS_AUTH_USER_V"."AUTH_NAME" is '权限名称';

update sys_user_role_v a set a.role_name = (select b.name from sys_role b where b.id=a.role_id);

update sys_auth_role_v a set a.auth_name = (select b.name from sys_auth b where b.id=a.auth_id);

update sys_auth_user_v a set a.auth_name = (select b.name from sys_auth b where b.id=a.auth_id);

alter table "BD_PERSON_DOC" DROP CONSTRAINT UK_BD_PERSON_DOC_NAME;

alter table SYS_AUTH_USER_LV add ("APP_ID" VARCHAR2(50));
comment on column "SYS_AUTH_USER_LV"."APP_ID" is '应用ID';

alter table SYS_AUTH_ROLE_LV add ("APP_ID" VARCHAR2(50));
comment on column "SYS_AUTH_ROLE_LV"."APP_ID" is '应用ID';

update sys_auth_user_lv a set a.app_id = ( select b.app_id from sys_auth_user_v b where b.lv_id =a.id and rownum=1);

update sys_auth_role_lv a set a.app_id = (select c.app_id from sys_role c where c.id=a.role_id);

alter table SYS_USER add ("IS_IP_BIND" NUMBER(1,0) DEFAULT 0);
comment on column "SYS_USER"."IS_IP_BIND" is '是否激活IP绑定0否，1是';

alter table SYS_USER add ("IP_ADDRESS" VARCHAR2(50));
comment on column "SYS_USER"."IP_ADDRESS" is 'IP地址';



CREATE TABLE "SYS_USER_THIRD_PARTY"
(
  "ID"            VARCHAR2(50 BYTE)               NOT NULL,
  "USER_ID"       VARCHAR2(50 BYTE)               NOT NULL,
  "TYPE"          VARCHAR2(50 BYTE)               NOT NULL,
  "TYPE_NAME"     VARCHAR2(50 BYTE)               NOT NULL,
  "ACCOUNT"       VARCHAR2(200 BYTE)              NOT NULL,
  "KEY_ONE"       VARCHAR2(200 BYTE),
  "KEY_TWO"       VARCHAR2(200 BYTE),
  "KEY_THREE"     VARCHAR2(200 BYTE),
  "KEY_FOUR"      VARCHAR2(200 BYTE),
  "DATA_VERSION"  INTEGER                         DEFAULT 0,
  "CREATE_USER"      VARCHAR2(50 CHAR),
  "CREATE_TIME"     DATE,
  "UPDATE_TIME"      DATE,
  NOT CLUSTER PRIMARY KEY("ID"),
CONSTRAINT "FK_USER_THIRD_PARTY_USERID" FOREIGN KEY("USER_ID") REFERENCES "SYS_USER"("ID") ON DELETE CASCADE  WITH
INDEX
);

COMMENT ON TABLE "SYS_USER_THIRD_PARTY" IS '用户绑定第三账号信息';

COMMENT ON COLUMN "SYS_USER_THIRD_PARTY"."ID" IS '主键';

COMMENT ON COLUMN "SYS_USER_THIRD_PARTY"."USER_ID" IS '用户主键';

COMMENT ON COLUMN "SYS_USER_THIRD_PARTY"."TYPE" IS '第三方账号类型';

COMMENT ON COLUMN "SYS_USER_THIRD_PARTY"."TYPE_NAME" IS '第三方账号类型名称';

COMMENT ON COLUMN "SYS_USER_THIRD_PARTY"."ACCOUNT" IS '第三方账号详情';

COMMENT ON COLUMN "SYS_USER_THIRD_PARTY"."CREATE_USER" IS '创建人ID';

COMMENT ON COLUMN "SYS_USER_THIRD_PARTY"."CREATE_TIME" IS '创建时间';

COMMENT ON COLUMN "SYS_USER_THIRD_PARTY"."UPDATE_TIME" IS '更新时间';

COMMENT ON COLUMN "SYS_USER_THIRD_PARTY"."KEY_ONE" IS '预留字段1';

COMMENT ON COLUMN "SYS_USER_THIRD_PARTY"."KEY_TWO" IS '预留字段2';

COMMENT ON COLUMN "SYS_USER_THIRD_PARTY"."KEY_THREE" IS '预留字段3';

COMMENT ON COLUMN "SYS_USER_THIRD_PARTY"."KEY_FOUR" IS '预留字段4';

/**
2019-11-29 运行日志管理
 */
INSERT INTO SYS_MENU ("ID", "NAME", "PARENT_ID", "APP_ID", "PARENT_NAME", "URL", "TYPE", "ICON", "AUTH_ID",
"BTN_AUTH", "IS_LEAF", "SORT_INDEX", "ALL_ORDER", "SORT_PATH", "OPEN_STYLE", "CLOSE_NOTICE", "CREATE_USER", "CREATE_TIME", "UPDATE_TIME", "LEAF", "MENU_AUTH", "REMARK", "DATA_VERSION", "IS_IFRAME")
VALUES ('99ea2ad7ea4a4e54acdaff4c84bb8371', '运行日志', 'e3b15ac90286450fbe5a721c9f542451', 'platform', '系统管理',
'workingLogManage/views/workingLog', '1', 'fa fa-window-maximize', '99ea2ad7ea4a4e54acdaff4c84bb8371', NULL, NULL, 29, NULL, '00020029', NULL, NULL, 'dde891f1a0c34a12add515b3b3ab7763', NULL, NULL, NULL, NULL, NULL, '0', '0');

insert into "SYS_AUTH" ("ID","NAME","APP_ID","REMARK","SORT_INDEX","PARENT_ID","CREATE_USER","CREATE_TIME","UPDATE_TIME","SORT_PATH","IS_ORG_ADMIN","IS_USER_GROUP_ADMIN","CODE","DATA_VERSION") values ('99ea2ad7ea4a4e54acdaff4c84bb8371', '运行日志', 'platform', null,29, 'e3b15ac90286450fbe5a721c9f542451', null, null, null, '00020029', 0, 0, 'workingLogManage.views.workingLog', 0);


insert into "SYS_AUTH_ROLE" ("ID","AUTH_ID","ROLE_ID","SIGN","IS_REVOKE","CREATE_USER","CREATE_TIME","UPDATE_TIME","REMARK") values ('99ea2ad7ea4a4e54acdaff4c84bb8371', '99ea2ad7ea4a4e54acdaff4c84bb8371', 'groupadmin', null, 0, null, null, null, null);
insert into "SYS_AUTH_ROLE" ("ID","AUTH_ID","ROLE_ID","SIGN","IS_REVOKE","CREATE_USER","CREATE_TIME","UPDATE_TIME","REMARK") values ('99ea2ad7ea4a4e54acdaff4c84bb8372', '99ea2ad7ea4a4e54acdaff4c84bb8371', 'admin', null, 0, null, null, null, null);

insert into "SYS_AUTH_ROLE_V" ("ID","AUTH_ID","ROLE_ID","SIGN","IS_REVOKE","CREATE_USER","CREATE_TIME","UPDATE_TIME","REMARK","LV_ID") values ('99ea2ad7ea4a4e54acdaff4c84bb8371', '99ea2ad7ea4a4e54acdaff4c84bb8371', 'groupadmin', null, 0, null, null, null, null, 'groupadmin');

insert into "SYS_AUTH_ROLE_V" ("ID","AUTH_ID","ROLE_ID","SIGN","IS_REVOKE","CREATE_USER","CREATE_TIME","UPDATE_TIME","REMARK","LV_ID") values ('99ea2ad7ea4a4e54acdaff4c84bb8372', '99ea2ad7ea4a4e54acdaff4c84bb8371', 'admin', null, 0, null, null, null, null, 'admin');


INSERT INTO SYS_MENU ("ID", "NAME", "PARENT_ID", "APP_ID", "PARENT_NAME", "URL", "TYPE", "ICON", "AUTH_ID", "BTN_AUTH", "IS_LEAF", "SORT_INDEX", "ALL_ORDER", "SORT_PATH", "OPEN_STYLE", "CLOSE_NOTICE", "CREATE_USER", "CREATE_TIME", "UPDATE_TIME", "LEAF", "MENU_AUTH", "REMARK", "DATA_VERSION", "IS_IFRAME")
VALUES ('99ea2ad7ea4a4e54acdaff4c84bb8370', '平台运行日志', 'e3b15ac90286450fbe5a721c9f542451', 'platform', '系统管理', 'workingLogManage/views/aceWorkingLog', '1', 'fa fa-window-maximize', '99ea2ad7ea4a4e54acdaff4c84bb8370', NULL, NULL, 19, NULL, '00020019', NULL, NULL, 'dde891f1a0c34a12add515b3b3ab7763', NULL, NULL, NULL, NULL, NULL, '0', '0');


insert into "SYS_AUTH" ("ID","NAME","APP_ID","REMARK","SORT_INDEX","PARENT_ID","CREATE_USER","CREATE_TIME","UPDATE_TIME","SORT_PATH","IS_ORG_ADMIN","IS_USER_GROUP_ADMIN","CODE","DATA_VERSION") values ('99ea2ad7ea4a4e54acdaff4c84bb8370', '平台运行日志', 'platform', null,19, 'e3b15ac90286450fbe5a721c9f542451', null, null, null, '00020019', 0, 0, 'workingLogManage.views.aceWorkingLog', 0);

insert into "SYS_AUTH_ROLE" ("ID","AUTH_ID","ROLE_ID","SIGN","IS_REVOKE","CREATE_USER","CREATE_TIME","UPDATE_TIME","REMARK") values ('99ea2ad7ea4a4e54acdaff4c84bb8375', '99ea2ad7ea4a4e54acdaff4c84bb8370', 'admin', null, 0, null, null, null, null);

insert into "SYS_AUTH_ROLE_V" ("ID","AUTH_ID","ROLE_ID","SIGN","IS_REVOKE","CREATE_USER","CREATE_TIME","UPDATE_TIME","REMARK","LV_ID") values ('99ea2ad7ea4a4e54acdaff4c84bb8375', '99ea2ad7ea4a4e54acdaff4c84bb8370', 'admin', null, 0, null, null, null, null, 'admin');

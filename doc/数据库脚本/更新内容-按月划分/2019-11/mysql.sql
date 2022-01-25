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
ALTER TABLE `SYS_CONFIG` add (`LONG_VALUE` LONGTEXT COMMENT '长型值，储存背景图片等');

/**
2019/11/6
 */
UPDATE SYS_MENU SET PARENT_ID='11316279a4a9416a8e20019821e640af' , PARENT_NAME='配置管理' , SORT_INDEX=11 , SORT_PATH='00030011' WHERE ID='131e140bc2bd4361af5ee488f92b22ad';
UPDATE SYS_AUTH SET PARENT_ID='11316279a4a9416a8e20019821e640af' ,  SORT_INDEX=11 , SORT_PATH='00030011' WHERE ID='131e140bc2bd4361af5ee488f92b22ad';

ALTER TABLE `SYS_DICT` add (`GROUP_ID` VARCHAR(50) COMMENT '集团ID');
ALTER TABLE `SYS_DICT` add (`SCOPE` INT(1) COMMENT '配置范围 1租户 2集团 3应用');

ALTER TABLE `SYS_DICT_VALUE` add (`GROUP_ID` VARCHAR(50) COMMENT '集团ID');
ALTER TABLE `SYS_DICT_VALUE` add (`SCOPE` INT(1) COMMENT '配置范围 1租户 2集团 3应用');

insert into `SYS_AUTH_ROLE` (`ID`,`AUTH_ID`,`ROLE_ID`,`SIGN`,`IS_REVOKE`,`CREATE_USER`,`CREATE_TIME`,`UPDATE_TIME`,`REMARK`) values ('12323fe2ef32403dbf9dc989d0651252', '131e140bc2bd4361af5ee488f92b22ad', 'admin', null, 0, null, null, null, null);
insert into `SYS_AUTH_ROLE` (`ID`,`AUTH_ID`,`ROLE_ID`,`SIGN`,`IS_REVOKE`,`CREATE_USER`,`CREATE_TIME`,`UPDATE_TIME`,`REMARK`) values ('12323fe2ef32403dbf9dc989d0651253', '131e140bc2bd4361af5ee488f92b22ad', 'groupadmin', null, 0, null, null, null, null);

insert into `SYS_AUTH_ROLE_V` (`ID`,`AUTH_ID`,`ROLE_ID`,`SIGN`,`IS_REVOKE`,`CREATE_USER`,`CREATE_TIME`,`UPDATE_TIME`,`REMARK`,`LV_ID`) values ('12323fe2ef32403dbf9dc989d0651252', '131e140bc2bd4361af5ee488f92b22ad', 'admin', null, 0, null, null, null, null, 'admin');
insert into `SYS_AUTH_ROLE_V` (`ID`,`AUTH_ID`,`ROLE_ID`,`SIGN`,`IS_REVOKE`,`CREATE_USER`,`CREATE_TIME`,`UPDATE_TIME`,`REMARK`,`LV_ID`) values ('12323fe2ef32403dbf9dc989d0651253', '131e140bc2bd4361af5ee488f92b22ad', 'groupadmin', null, 0, null, null, null, null, 'groupadmin');

DELETE FROM SYS_AUTH WHERE ID = '131e140bc2bd4361af5ee488f92b2111';

insert into `SYS_AUTH` (`ID`,`NAME`,`APP_ID`,`REMARK`,`SORT_INDEX`,`PARENT_ID`,`CREATE_USER`,`CREATE_TIME`,`UPDATE_TIME`,`SORT_PATH`,`IS_ORG_ADMIN`,`IS_USER_GROUP_ADMIN`,`CODE`,`DATA_VERSION`) values ('1160858109884c5095eed968472c0234', '租户级字典维护', 'platform', null, 1, '131e140bc2bd4361af5ee488f92b22ad', null, null, null, '000300110001', 0, 0, 'platForm.dictManage.Dict.TenantMaintain', 0);
insert into `SYS_AUTH` (`ID`,`NAME`,`APP_ID`,`REMARK`,`SORT_INDEX`,`PARENT_ID`,`CREATE_USER`,`CREATE_TIME`,`UPDATE_TIME`,`SORT_PATH`,`IS_ORG_ADMIN`,`IS_USER_GROUP_ADMIN`,`CODE`,`DATA_VERSION`) values ('1160858109884c5095eed968472c0235', '集团级字典维护', 'platform', null, 2, '131e140bc2bd4361af5ee488f92b22ad', null, null, null, '000300110002', 0, 0, 'platForm.dictManage.Dict.GroupMaintain', 0);
insert into `SYS_AUTH` (`ID`,`NAME`,`APP_ID`,`REMARK`,`SORT_INDEX`,`PARENT_ID`,`CREATE_USER`,`CREATE_TIME`,`UPDATE_TIME`,`SORT_PATH`,`IS_ORG_ADMIN`,`IS_USER_GROUP_ADMIN`,`CODE`,`DATA_VERSION`) values ('1160858109884c5095eed968472c0236', '应用级字典维护', 'platform', null, 3, '131e140bc2bd4361af5ee488f92b22ad', null, null, null, '000300110003', 0, 0, 'platForm.dictManage.Dict.AppMaintain', 0);

insert into `SYS_AUTH_ROLE` (`ID`,`AUTH_ID`,`ROLE_ID`,`SIGN`,`IS_REVOKE`,`CREATE_USER`,`CREATE_TIME`,`UPDATE_TIME`,`REMARK`) values ('134757e07a3249d6ad180da0b4681111', '1160858109884c5095eed968472c0234', 'admin', null, 0, null, null, null, null);
insert into `SYS_AUTH_ROLE` (`ID`,`AUTH_ID`,`ROLE_ID`,`SIGN`,`IS_REVOKE`,`CREATE_USER`,`CREATE_TIME`,`UPDATE_TIME`,`REMARK`) values ('134757e07a3249d6ad180da0b4681112', '1160858109884c5095eed968472c0235', 'groupadmin', null, 0, null, null, null, null);
insert into `SYS_AUTH_ROLE` (`ID`,`AUTH_ID`,`ROLE_ID`,`SIGN`,`IS_REVOKE`,`CREATE_USER`,`CREATE_TIME`,`UPDATE_TIME`,`REMARK`) values ('134757e07a3249d6ad180da0b4681113', '1160858109884c5095eed968472c0236', 'appadmin', null, 0, null, null, null, null);

insert into `SYS_AUTH_ROLE_V` (`ID`,`AUTH_ID`,`ROLE_ID`,`SIGN`,`IS_REVOKE`,`CREATE_USER`,`CREATE_TIME`,`UPDATE_TIME`,`REMARK`,`LV_ID`) values ('134757e07a3249d6ad180da0b4681111', '1160858109884c5095eed968472c0234', 'admin', null, 0, null, null, null, null, 'admin');
insert into `SYS_AUTH_ROLE_V` (`ID`,`AUTH_ID`,`ROLE_ID`,`SIGN`,`IS_REVOKE`,`CREATE_USER`,`CREATE_TIME`,`UPDATE_TIME`,`REMARK`,`LV_ID`) values ('134757e07a3249d6ad180da0b4681112', '1160858109884c5095eed968472c0235', 'groupadmin', null, 0, null, null, null, null, 'groupadmin');
insert into `SYS_AUTH_ROLE_V` (`ID`,`AUTH_ID`,`ROLE_ID`,`SIGN`,`IS_REVOKE`,`CREATE_USER`,`CREATE_TIME`,`UPDATE_TIME`,`REMARK`,`LV_ID`) values ('134757e07a3249d6ad180da0b4681113', '1160858109884c5095eed968472c0236', 'appadmin', null, 0, null, null, null, null, 'appadmin');

alter table `SYS_DICT` DROP foreign key FK_SYS_DICT_APP_ID;
alter table `SYS_MENU` DROP foreign key FK_SYS_MENU_AUTH_ID;


/**
2019/11/7
 */

alter table `BD_POST` add (`TYPE_ID` VARCHAR(50) COMMENT '岗位类别主键');

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
alter table `SYS_GROUP_DATASOURCE` add (`NAME` VARCHAR(50) COMMENT '数据源名称');
/**
2019/11/13
 */
insert into `SYS_CONFIG` (`NAME`,`VALUE`,`REMARK`,`CREATE_TIME`,`UPDATE_TIME`,`SORT_INDEX`,`ID`,`CREATE_USER`,`APP_ID`,`GROUP_ID`,`DATA_VERSION`,`SCOPE`,`TYPE`,`LONG_VALUE`) values ('platformName', '企业管理平台', '平台名称', null, null, 2, 'dadasdasddadsadasdasdasd', null, '', '', 0, 1, null, null);
alter table `SYS_USER` modify  `PINYIN` varchar(100);

alter table `SYS_WAIT_GRANT_AUTH` add (`APP_ID` VARCHAR(50) COMMENT '应用ID');

alter table `SYS_AUDIT_LOG` add (`USER_TYPE` VARCHAR(50) COMMENT '用户类型（0租户管理员,1集团管理员,2应用管理员,3普通用户）');

alter table `SYS_AUDIT_LOG_BACKUP` add (`USER_TYPE` VARCHAR(100) COMMENT '用户类型（0租户管理员,1集团管理员,2应用管理员,3普通用户）');

update
sys_audit_log a set
a.user_type =  (select b.user_type from sys_user b where a.op_name = b.real_name limit 1);

/**
2019/11/19
 */
CREATE TABLE `REPORT_TYPE`
(
`ID` VARCHAR(50) NOT NULL COMMENT '主键',
`NAME` VARCHAR(500) COMMENT '类别名称',
`APP_ID` VARCHAR(500) COMMENT '应用id',
`SORT` INT(10) DEFAULT 9999 COMMENT '排序',
`DATA_VERSION` INT(10) DEFAULT 0 COMMENT '数据版本',
`TYPE` INT(10) DEFAULT 1 COMMENT '类别类型 1报表2仪表盘',
`PARENT_ID` VARCHAR(50) COMMENT '父节点ID',
PRIMARY KEY(`ID`),
CONSTRAINT `FK_REPORT_TYPE_APP_ID` FOREIGN KEY(`APP_ID`) REFERENCES `SYS_GROUP_APP`(`ID`) ON DELETE CASCADE
)
COMMENT = '报表类别';

CREATE TABLE `REPORT_INFO`
(
`ID` VARCHAR(50) NOT NULL COMMENT '主键',
`APP_ID` VARCHAR(50) COMMENT '应用id',
`TYPE_ID` VARCHAR(50) COMMENT '报表类型id',
`NAME` VARCHAR(500) COMMENT '报表名称',
`AUTH` VARCHAR(2000) COMMENT '查看权限集合',
`IS_PUBLIC` INT(10) DEFAULT 0 COMMENT '公开报表（0不公开1公开）开放报表，无需登录查看',
`REFRESH_TYPE` INT(10) DEFAULT 0 COMMENT '自动刷新类型（0不自动刷新，1定时刷新，2推送事假定时刷新）',
`SHOW_ITEM` VARCHAR(2000) COMMENT '查看选项',
`MRT_STR` LONGTEXT COMMENT '报表信息',
`DATA_VERSION` INT(10) DEFAULT 0 COMMENT '数据版本',
`IS_AUTO_FLIP` INT(10) COMMENT '自动翻页',
`REMARKS` VARCHAR(2000) COMMENT '描述',
PRIMARY KEY(`ID`),
CONSTRAINT `FK_REPORT_INFO_APP_ID` FOREIGN KEY(`APP_ID`) REFERENCES `SYS_GROUP_APP`(`ID`) ON DELETE CASCADE,
CONSTRAINT `FK_REPORT_INFO_TYPE_ID` FOREIGN KEY(`TYPE_ID`) REFERENCES `REPORT_TYPE`(`ID`) ON DELETE CASCADE
)
COMMENT = '报表信息';


/**
2019/11/21
 */
--删除管理员针对审计日志的菜单管理
delete from sys_auth_role where auth_id='04d7893c4a4d4345b18b0789af13753f' and role_id in ('admin','groupadmin','appadmin');
delete from sys_auth_role_v where auth_id='04d7893c4a4d4345b18b0789af13753f' and role_id in ('admin','groupadmin','appadmin');

--对已存在管理员的有效权限删除掉审计日志的菜单管理
delete from sys_auth_mix where user_id in (select user_id from sys_user_role where role_id in ('admin','groupadmin','appadmin') and is_activated=1) and auth_id='04d7893c4a4d4345b18b0789af13753f';



ALTER TABLE `SYS_AUDIT_LOG` ADD (`ROLE_ID` VARCHAR(50) COMMENT '该操作人对应的管理员角色');

ALTER TABLE `SYS_AUDIT_LOG_BACKUP` ADD (`ROLE_ID` VARCHAR(200) COMMENT '该操作人对应的管理员角色');

ALTER TABLE `SYS_AUDIT_LOG_BACKUP` ADD (`GROUP_ID` VARCHAR(200) COMMENT '集团ID');

ALTER TABLE `SYS_AUDIT_LOG_BACKUP` ADD (`APP_ID` VARCHAR(200) COMMENT '应用ID');

UPDATE
sys_audit_log a SET
a.role_id = (SELECT c.role_id FROM sys_user_role c WHERE c.user_id = (SELECT b.id FROM sys_user b WHERE a.op_name=b.real_name LIMIT 1) LIMIT 1) WHERE a.user_type!=3;
alter table "BD_PERSON_ID_TYPE" DROP CONSTRAINT UK_BD_PERSON_ID_TYPE_CODE;
alter table "ORG_ORGANIZATION" DROP CONSTRAINT UK_ORG_ORG_PID_SORT_INDEX;

/**
2019/11/22
 */
--应用审计员添加角色审计菜单，授权审计菜单
insert into `SYS_AUTH` (`ID`,`NAME`,`APP_ID`,`REMARK`,`SORT_INDEX`,`PARENT_ID`,`CREATE_USER`,`CREATE_TIME`,`UPDATE_TIME`,`SORT_PATH`,`IS_ORG_ADMIN`,`IS_USER_GROUP_ADMIN`,`CODE`,`DATA_VERSION`) values ('frggb3ec3d6e483b86c4923ea64cvbnu', '角色审计', 'platform', null,6, '11erb3ec3d6e483b86c4923ea64cfdd0', null, null, null, '000200090006', 0, 0, 'roleAuditManage.roleAudit', 0);
insert into `SYS_AUTH` (`ID`,`NAME`,`APP_ID`,`REMARK`,`SORT_INDEX`,`PARENT_ID`,`CREATE_USER`,`CREATE_TIME`,`UPDATE_TIME`,`SORT_PATH`,`IS_ORG_ADMIN`,`IS_USER_GROUP_ADMIN`,`CODE`,`DATA_VERSION`) values ('hjuub3ec3d6e483b86c4923ea64cnmui', '授权审计', 'platform', null,7, '11erb3ec3d6e483b86c4923ea64cfdd0', null, null, null, '000200090007', 0, 0, 'grantAuthAuditManage.grantAuthAudit', 0);

insert into `SYS_MENU` (`ID`,`NAME`,`PARENT_ID`,`APP_ID`,`PARENT_NAME`,`URL`,`TYPE`,`ICON`,`AUTH_ID`,`BTN_AUTH`,`IS_LEAF`,`SORT_INDEX`,`ALL_ORDER`,`SORT_PATH`,`OPEN_STYLE`,`CLOSE_NOTICE`,`CREATE_USER`,`CREATE_TIME`,`UPDATE_TIME`,`LEAF`,`MENU_AUTH`,`REMARK`,`DATA_VERSION`,`IS_IFRAME`) values ('frggb3ec3d6e483b86c4923ea64cvbnu', '角色审计', '11erb3ec3d6e483b86c4923ea64cfdd0', 'platform', '授权管理', 'roleAuditManage/views/RoleAudit', 1, 'fa fa-wpforms', 'frggb3ec3d6e483b86c4923ea64cvbnu', null, null, 6, null, '000200090006', null, null, null, null, null, null, null, null, 0, 0);
insert into `SYS_MENU` (`ID`,`NAME`,`PARENT_ID`,`APP_ID`,`PARENT_NAME`,`URL`,`TYPE`,`ICON`,`AUTH_ID`,`BTN_AUTH`,`IS_LEAF`,`SORT_INDEX`,`ALL_ORDER`,`SORT_PATH`,`OPEN_STYLE`,`CLOSE_NOTICE`,`CREATE_USER`,`CREATE_TIME`,`UPDATE_TIME`,`LEAF`,`MENU_AUTH`,`REMARK`,`DATA_VERSION`,`IS_IFRAME`) values ('hjuub3ec3d6e483b86c4923ea64cnmui', '授权审计', '11erb3ec3d6e483b86c4923ea64cfdd0', 'platform', '授权管理', 'grantAuthAuditManage/views/GrantAuthAudit', 1, 'fa fa-calendar-o', 'hjuub3ec3d6e483b86c4923ea64cnmui', null, null, 7, null, '000200090007', null, null, null, null, null, null, null, null, 0, 0);

insert into `SYS_AUTH_ROLE` (`ID`,`AUTH_ID`,`ROLE_ID`,`SIGN`,`IS_REVOKE`,`CREATE_USER`,`CREATE_TIME`,`UPDATE_TIME`,`REMARK`) values ('12323fe2ef32403dbf9dc989d0651254', 'frggb3ec3d6e483b86c4923ea64cvbnu', 'appauditor', null, 0, null, null, null, null);
insert into `SYS_AUTH_ROLE` (`ID`,`AUTH_ID`,`ROLE_ID`,`SIGN`,`IS_REVOKE`,`CREATE_USER`,`CREATE_TIME`,`UPDATE_TIME`,`REMARK`) values ('12323fe2ef32403dbf9dc989d0651255', 'hjuub3ec3d6e483b86c4923ea64cnmui', 'appauditor', null, 0, null, null, null, null);
insert into `SYS_AUTH_ROLE_V` (`ID`,`AUTH_ID`,`ROLE_ID`,`SIGN`,`IS_REVOKE`,`CREATE_USER`,`CREATE_TIME`,`UPDATE_TIME`,`REMARK`,`LV_ID`) values ('12323fe2ef32403dbf9dc989d0651254', 'frggb3ec3d6e483b86c4923ea64cvbnu', 'appauditor', null, 0, null, null, null, null, 'appauditor');
insert into `SYS_AUTH_ROLE_V` (`ID`,`AUTH_ID`,`ROLE_ID`,`SIGN`,`IS_REVOKE`,`CREATE_USER`,`CREATE_TIME`,`UPDATE_TIME`,`REMARK`,`LV_ID`) values ('12323fe2ef32403dbf9dc989d0651255', 'hjuub3ec3d6e483b86c4923ea64cnmui', 'appauditor', null, 0, null, null, null, null, 'appauditor');


insert into `SYS_AUTH_ROLE` (`ID`,`AUTH_ID`,`ROLE_ID`,`SIGN`,`IS_REVOKE`,`CREATE_USER`,`CREATE_TIME`,`UPDATE_TIME`,`REMARK`) values ('12323fe2ef32403dbf9dc989d0651256', '11erb3ec3d6e483b86c4923ea64cfdd0', 'appauditor', null, 0, null, null, null, null);
insert into `SYS_AUTH_ROLE_V` (`ID`,`AUTH_ID`,`ROLE_ID`,`SIGN`,`IS_REVOKE`,`CREATE_USER`,`CREATE_TIME`,`UPDATE_TIME`,`REMARK`,`LV_ID`) values ('12323fe2ef32403dbf9dc989d0651256', '11erb3ec3d6e483b86c4923ea64cfdd0', 'appauditor', null, 0, null, null, null, null, 'appauditor');


/**
2019/11/25
 */
ALTER TABLE `SYS_USER_ROLE_V` ADD (`ROLE_NAME` VARCHAR(50) COMMENT '角色名称');

ALTER TABLE `SYS_AUTH_ROLE_V` ADD (`AUTH_NAME` VARCHAR(50) COMMENT '权限名称');

ALTER TABLE `SYS_AUTH_USER_V` ADD (`AUTH_NAME` VARCHAR(50) COMMENT '权限名称');

ALTER TABLE `SYS_AUTH_USER_LV` ADD (`APP_ID` VARCHAR(50) COMMENT '应用ID');

ALTER TABLE `SYS_AUTH_ROLE_LV` ADD (`APP_ID` VARCHAR(50) COMMENT '应用ID');

ALTER TABLE `SYS_USER` ADD (`IS_IP_BIND` INT(1) DEFAULT 0 COMMENT '是否激活IP绑定0否，1是');

ALTER TABLE `SYS_USER` ADD (`IP_ADDRESS` VARCHAR(50) COMMENT 'IP地址');

CREATE TABLE `SYS_USER_THIRD_PARTY`(
  `ID` VARCHAR(50) NOT NULL COMMENT '主键',
  `USER_ID` VARCHAR(50) NOT NULL COMMENT '用户主键',
  `TYPE` VARCHAR(50) NOT NULL COMMENT '第三方账号类型',
  `TYPE_NAME` VARCHAR(50) NOT NULL COMMENT '第三方账号类型名称',
  `ACCOUNT` VARCHAR(200) NOT NULL COMMENT '第三方账号详情',
  `KEY_ONE` VARCHAR(200) COMMENT '预留字段1',
  `KEY_TWO` VARCHAR(200) COMMENT '预留字段2',
  `KEY_THREE` VARCHAR(200) COMMENT '预留字段3',
  `KEY_FOUR` VARCHAR(200) COMMENT '预留字段4',
  `DATA_VERSION` INT(1) DEFAULT 1 COMMENT '数据版本',
  `CREATE_USER` VARCHAR(50) COMMENT '创建人ID',
  `CREATE_TIME` DATE COMMENT '创建时间',
  `UPDATE_TIME` DATE COMMENT '更新时间',
  PRIMARY KEY (`ID`),
  CONSTRAINT `FK_USER_THIRD_PARTY_USERID` FOREIGN KEY(`USER_ID`) REFERENCES `SYS_USER`(`ID`) ON DELETE CASCADE
)
COMMENT = '用户绑定第三账号信息';

/**
2019-11-29 运行日志管理
 */

INSERT INTO `SYS_MENU` (`ID`, `NAME`, `PARENT_ID`, `APP_ID`, `PARENT_NAME`, `URL`, `TYPE`, `ICON`, `AUTH_ID`,
`BTN_AUTH`, `IS_LEAF`, `SORT_INDEX`, `ALL_ORDER`, `SORT_PATH`, `OPEN_STYLE`, `CLOSE_NOTICE`, `CREATE_USER`, `CREATE_TIME`, `UPDATE_TIME`, `LEAF`, `MENU_AUTH`, `REMARK`, `DATA_VERSION`, `IS_IFRAME`)
VALUES ('99ea2ad7ea4a4e54acdaff4c84bb8371', '运行日志', 'e3b15ac90286450fbe5a721c9f542451', 'platform', '系统管理',
'workingLogManage/views/workingLog', '1', 'fa fa-window-maximize', '99ea2ad7ea4a4e54acdaff4c84bb8371', NULL, NULL, 29, NULL, '00020029', NULL, NULL, 'dde891f1a0c34a12add515b3b3ab7763', NULL, NULL, NULL, NULL, NULL, '0', '0');

insert into `SYS_AUTH` (`ID`,`NAME`,`APP_ID`,`REMARK`,`SORT_INDEX`,`PARENT_ID`,`CREATE_USER`,`CREATE_TIME`,`UPDATE_TIME`,`SORT_PATH`,`IS_ORG_ADMIN`,`IS_USER_GROUP_ADMIN`,`CODE`,`DATA_VERSION`) values ('99ea2ad7ea4a4e54acdaff4c84bb8371', '运行日志', 'platform', null,29, 'e3b15ac90286450fbe5a721c9f542451', null, null, null, '00020029', 0, 0, 'workingLogManage.views.workingLog', 0);


insert into `SYS_AUTH_ROLE` (`ID`,`AUTH_ID`,`ROLE_ID`,`SIGN`,`IS_REVOKE`,`CREATE_USER`,`CREATE_TIME`,`UPDATE_TIME`,`REMARK`) values ('99ea2ad7ea4a4e54acdaff4c84bb8371', '99ea2ad7ea4a4e54acdaff4c84bb8371', 'groupadmin', null, 0, null, null, null, null);
insert into `SYS_AUTH_ROLE` (`ID`,`AUTH_ID`,`ROLE_ID`,`SIGN`,`IS_REVOKE`,`CREATE_USER`,`CREATE_TIME`,`UPDATE_TIME`,`REMARK`) values ('99ea2ad7ea4a4e54acdaff4c84bb8372', '99ea2ad7ea4a4e54acdaff4c84bb8371', 'admin', null, 0, null, null, null, null);

insert into `SYS_AUTH_ROLE_V` (`ID`,`AUTH_ID`,`ROLE_ID`,`SIGN`,`IS_REVOKE`,`CREATE_USER`,`CREATE_TIME`,`UPDATE_TIME`,`REMARK`,`LV_ID`) values ('99ea2ad7ea4a4e54acdaff4c84bb8371', '99ea2ad7ea4a4e54acdaff4c84bb8371', 'groupadmin', null, 0, null, null, null, null, 'groupadmin');

insert into `SYS_AUTH_ROLE_V` (`ID`,`AUTH_ID`,`ROLE_ID`,`SIGN`,`IS_REVOKE`,`CREATE_USER`,`CREATE_TIME`,`UPDATE_TIME`,`REMARK`,`LV_ID`) values ('99ea2ad7ea4a4e54acdaff4c84bb8372', '99ea2ad7ea4a4e54acdaff4c84bb8371', 'admin', null, 0, null, null, null, null, 'admin');


INSERT INTO `SYS_MENU` (`ID`, `NAME`, `PARENT_ID`, `APP_ID`, `PARENT_NAME`, `URL`, `TYPE`, `ICON`, `AUTH_ID`, `BTN_AUTH`, `IS_LEAF`, `SORT_INDEX`, `ALL_ORDER`, `SORT_PATH`, `OPEN_STYLE`, `CLOSE_NOTICE`, `CREATE_USER`, `CREATE_TIME`, `UPDATE_TIME`, `LEAF`, `MENU_AUTH`, `REMARK`, `DATA_VERSION`, `IS_IFRAME`)
VALUES ('99ea2ad7ea4a4e54acdaff4c84bb8370', '平台运行日志', 'e3b15ac90286450fbe5a721c9f542451', 'platform', '系统管理', 'workingLogManage/views/aceWorkingLog', '1', 'fa fa-window-maximize', '99ea2ad7ea4a4e54acdaff4c84bb8370', NULL, NULL, 19, NULL, '00020019', NULL, NULL, 'dde891f1a0c34a12add515b3b3ab7763', NULL, NULL, NULL, NULL, NULL, '0', '0');


insert into `SYS_AUTH` (`ID`,`NAME`,`APP_ID`,`REMARK`,`SORT_INDEX`,`PARENT_ID`,`CREATE_USER`,`CREATE_TIME`,`UPDATE_TIME`,`SORT_PATH`,`IS_ORG_ADMIN`,`IS_USER_GROUP_ADMIN`,`CODE`,`DATA_VERSION`) values ('99ea2ad7ea4a4e54acdaff4c84bb8370', '平台运行日志', 'platform', null,19, 'e3b15ac90286450fbe5a721c9f542451', null, null, null, '00020019', 0, 0, 'workingLogManage.views.aceWorkingLog', 0);

insert into `SYS_AUTH_ROLE` (`ID`,`AUTH_ID`,`ROLE_ID`,`SIGN`,`IS_REVOKE`,`CREATE_USER`,`CREATE_TIME`,`UPDATE_TIME`,`REMARK`) values ('99ea2ad7ea4a4e54acdaff4c84bb8375', '99ea2ad7ea4a4e54acdaff4c84bb8370', 'admin', null, 0, null, null, null, null);

insert into `SYS_AUTH_ROLE_V` (`ID`,`AUTH_ID`,`ROLE_ID`,`SIGN`,`IS_REVOKE`,`CREATE_USER`,`CREATE_TIME`,`UPDATE_TIME`,`REMARK`,`LV_ID`) values ('99ea2ad7ea4a4e54acdaff4c84bb8375', '99ea2ad7ea4a4e54acdaff4c84bb8370', 'admin', null, 0, null, null, null, null, 'admin');

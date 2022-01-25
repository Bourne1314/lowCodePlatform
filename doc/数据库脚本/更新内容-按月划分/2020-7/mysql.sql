/**
 * 地图删除APPID
 * @param null
 * @return
 * @author zuogang
 * @date 2020/7/6 17:58
 */
alter table `blade_visual_map` DROP foreign key FK_VISUAL_MAP_APP_ID;
ALTER TABLE `blade_visual_map` DROP COLUMN `app_id`;
alter table `BD_PERSON_JOB` MODIFY  COLUMN `PERSON_CODE` VARCHAR(50)  NULL;
update `sys_menu` set `url`='dataScreen/components/list/map' where `name`='地图管理';

/**
 * 增加文件回收站
 * @author JonnyJiang
 * @date 2020/7/8 10:59
 */
ALTER TABLE `file_info`
ADD COLUMN `IS_IN_RECYCLE_BIN` INT(1) NULL COMMENT '是否在回收站' AFTER `FILE_REPOSITORY_ID`,
ADD COLUMN `RECRCLE_TIME` DATETIME NULL COMMENT '回收时间' AFTER `IS_IN_RECYCLE_BIN`;
ADD COLUMN `RECRCLE_FILE_ID` VARCHAR(50) NULL COMMENT '回收文件ID' AFTER `RECRCLE_TIME`;

/**
 * 开发平台
 * @param null
 * @return
 * @author zuogang
 * @date 2020/7/13 9:26
 */
/**
app
*/
insert into `SYS_GROUP_APP` (`ID`,`NAME`,`VERSION`,`ICON`,`SORT_INDEX`,`REMARK`,`CREATE_USER`,`CREATE_TIME`,`UPDATE_TIME`,`SECRET_LEVEL`,`GROUP_ID`,`IS_3_ADMIN`,`DATA_VERSION`,`APP_LIB_ID`,`IS_MAIN_APP`,`HAS_UI`,`UI_NAME`,`IS_LOCK`,`LOCK_TIME`,`DATASOURCE_ID`) values ('dev-platform', '快速开发平台', '1.0', null, null, null, null, null, null, null, null, 1, 1060, null, 0, 0, null, 0, null, null);
/**
role
*/
comment on column `SYS_ROLE`.`ROLE_TYPE` is '角色类型(1租户系统管理员,2租户安全保密员,3租户安全审计员)(11集团系统管理员,22集团安全保密员,33集团安全审计员)(111应用系统管理员,222应用安全保密员,333应用安全审计员)(1111开发平台超级管理员,2222项目管理人员,3333项目开发人员)';
insert into `SYS_ROLE` (`ID`,`NAME`,`ROLE_EXPLAIN`,`ROLE_TYPE`,`CREATE_TIME`,`CREATE_USER`,`UPDATE_TIME`,`REMARK`,`APP_ID`,`DATA_VERSION`) values ('devadmin', '开发平台超级管理员', null,1111, null, null, null, null, 'dev-platform', 0);
insert into `SYS_ROLE` (`ID`,`NAME`,`ROLE_EXPLAIN`,`ROLE_TYPE`,`CREATE_TIME`,`CREATE_USER`,`UPDATE_TIME`,`REMARK`,`APP_ID`,`DATA_VERSION`) values ('devmaintainer', '开发平台项目管理人员', null,2222, null, null, null, null, 'dev-platform', 0);
insert into `SYS_ROLE` (`ID`,`NAME`,`ROLE_EXPLAIN`,`ROLE_TYPE`,`CREATE_TIME`,`CREATE_USER`,`UPDATE_TIME`,`REMARK`,`APP_ID`,`DATA_VERSION`) values ('devdeveloper', '开发平台项目开发人员', null,3333, null, null, null, null, 'dev-platform', 0);
/**
auth
*/
insert into `SYS_AUTH` (`ID`,`NAME`,`APP_ID`,`REMARK`,`SORT_INDEX`,`PARENT_ID`,`CREATE_USER`,`CREATE_TIME`,`UPDATE_TIME`,`SORT_PATH`,`IS_ORG_ADMIN`,`IS_USER_GROUP_ADMIN`,`CODE`,`DATA_VERSION`) values ('227f8578d72c4cd6b78125ab97c2f17d', '项目管理', 'dev-platform', '', 1, '0', 'e924956d25d94c099430748a777a6b16', null, null, '000001', 0, 0, null, 0);
insert into `SYS_AUTH` (`ID`,`NAME`,`APP_ID`,`REMARK`,`SORT_INDEX`,`PARENT_ID`,`CREATE_USER`,`CREATE_TIME`,`UPDATE_TIME`,`SORT_PATH`,`IS_ORG_ADMIN`,`IS_USER_GROUP_ADMIN`,`CODE`,`DATA_VERSION`) values ('a2c96add3b7744c3939083d5351f3828', '项目信息', 'dev-platform', '', 1, '227f8578d72c4cd6b78125ab97c2f17d', 'e924956d25d94c099430748a777a6b16', null, null, '000001000001', 0, 0, 'devPlatForm.ProInfoManage.ProInfo', 0);
insert into `SYS_AUTH` (`ID`,`NAME`,`APP_ID`,`REMARK`,`SORT_INDEX`,`PARENT_ID`,`CREATE_USER`,`CREATE_TIME`,`UPDATE_TIME`,`SORT_PATH`,`IS_ORG_ADMIN`,`IS_USER_GROUP_ADMIN`,`CODE`,`DATA_VERSION`) values ('8f9dec2ca33040e199f9e922f906dc7f', '数据源管理', 'dev-platform', '', 2, '227f8578d72c4cd6b78125ab97c2f17d', 'e924956d25d94c099430748a777a6b16', null, null, '000001000002', 0, 0, 'devPlatForm.ProDatasourceManage.ProDatasource', 0);
insert into `SYS_AUTH` (`ID`,`NAME`,`APP_ID`,`REMARK`,`SORT_INDEX`,`PARENT_ID`,`CREATE_USER`,`CREATE_TIME`,`UPDATE_TIME`,`SORT_PATH`,`IS_ORG_ADMIN`,`IS_USER_GROUP_ADMIN`,`CODE`,`DATA_VERSION`) values ('ad2b809bf7084d2db69dd56793cb5e1c', '服务管理', 'dev-platform', '', 3, '227f8578d72c4cd6b78125ab97c2f17d', 'e924956d25d94c099430748a777a6b16', null, null, '000001000003', 0, 0, 'devPlatForm.serviceMange', 0);
insert into `SYS_AUTH` (`ID`,`NAME`,`APP_ID`,`REMARK`,`SORT_INDEX`,`PARENT_ID`,`CREATE_USER`,`CREATE_TIME`,`UPDATE_TIME`,`SORT_PATH`,`IS_ORG_ADMIN`,`IS_USER_GROUP_ADMIN`,`CODE`,`DATA_VERSION`) values ('3860126718034cc2a7dbd8311bfb3ff1', '应用信息', 'dev-platform', '', 1, 'ad2b809bf7084d2db69dd56793cb5e1c', 'e924956d25d94c099430748a777a6b16', null, null, '000001000003000001', 0, 0, 'devPlatForm.ProServiceManage.ProService', 0);
insert into `SYS_AUTH` (`ID`,`NAME`,`APP_ID`,`REMARK`,`SORT_INDEX`,`PARENT_ID`,`CREATE_USER`,`CREATE_TIME`,`UPDATE_TIME`,`SORT_PATH`,`IS_ORG_ADMIN`,`IS_USER_GROUP_ADMIN`,`CODE`,`DATA_VERSION`) values ('2a3c459a0c814a098bdc41cb0e4b830e', '模型管理', 'dev-platform', '', 2, 'ad2b809bf7084d2db69dd56793cb5e1c', 'e924956d25d94c099430748a777a6b16', null, null, '000001000003000002', 0, 0, 'devPlatForm.ProModelManage.ProModel', 0);
insert into `SYS_AUTH` (`ID`,`NAME`,`APP_ID`,`REMARK`,`SORT_INDEX`,`PARENT_ID`,`CREATE_USER`,`CREATE_TIME`,`UPDATE_TIME`,`SORT_PATH`,`IS_ORG_ADMIN`,`IS_USER_GROUP_ADMIN`,`CODE`,`DATA_VERSION`) values ('e0c7623e577d4811af97dc6eab9c14d6', '接口管理', 'dev-platform', '', 3, 'ad2b809bf7084d2db69dd56793cb5e1c', 'e924956d25d94c099430748a777a6b16', null, null, '000001000003000003', 0, 0, 'devPlatForm.InterfaceManage.Interface', 0);
insert into `SYS_AUTH` (`ID`,`NAME`,`APP_ID`,`REMARK`,`SORT_INDEX`,`PARENT_ID`,`CREATE_USER`,`CREATE_TIME`,`UPDATE_TIME`,`SORT_PATH`,`IS_ORG_ADMIN`,`IS_USER_GROUP_ADMIN`,`CODE`,`DATA_VERSION`) values ('c6d75746103e4dc99d9d0ce27f0e1af1', '页面设计', 'dev-platform', '', 4, 'ad2b809bf7084d2db69dd56793cb5e1c', 'e924956d25d94c099430748a777a6b16', null, null, '000001000003000004', 0, 0, 'devPlatForm.PageDesignManage.PageDesign', 0);
insert into `SYS_AUTH` (`ID`,`NAME`,`APP_ID`,`REMARK`,`SORT_INDEX`,`PARENT_ID`,`CREATE_USER`,`CREATE_TIME`,`UPDATE_TIME`,`SORT_PATH`,`IS_ORG_ADMIN`,`IS_USER_GROUP_ADMIN`,`CODE`,`DATA_VERSION`) values ('09a55900eb974483b599b4343afb7c52', '系统管理', 'dev-platform', '', 2, '0', 'e924956d25d94c099430748a777a6b16', null, null, '000002', 0, 0, 'devplatform.configMange', 0);
insert into `SYS_AUTH` (`ID`,`NAME`,`APP_ID`,`REMARK`,`SORT_INDEX`,`PARENT_ID`,`CREATE_USER`,`CREATE_TIME`,`UPDATE_TIME`,`SORT_PATH`,`IS_ORG_ADMIN`,`IS_USER_GROUP_ADMIN`,`CODE`,`DATA_VERSION`) values ('e668d7fddbac4aa3a3fdb6502d00ac7d', '用户管理', 'dev-platform', '', 1, '09a55900eb974483b599b4343afb7c52', 'e924956d25d94c099430748a777a6b16', null, null, '000002000001', 0, 0, 'devPlatForm.UserManage.DevUser', 0);
insert into `SYS_AUTH` (`ID`,`NAME`,`APP_ID`,`REMARK`,`SORT_INDEX`,`PARENT_ID`,`CREATE_USER`,`CREATE_TIME`,`UPDATE_TIME`,`SORT_PATH`,`IS_ORG_ADMIN`,`IS_USER_GROUP_ADMIN`,`CODE`,`DATA_VERSION`) values ('74b709f180014414831c66d0ff290a49', '增删改', 'dev-platform', '', 10, 'a2c96add3b7744c3939083d5351f3828', 'e924956d25d94c099430748a777a6b16', null, null, '000001000001000010', 0, 0, 'ProInfo.addOrUpdOrDel', 0);
insert into `SYS_AUTH` (`ID`,`NAME`,`APP_ID`,`REMARK`,`SORT_INDEX`,`PARENT_ID`,`CREATE_USER`,`CREATE_TIME`,`UPDATE_TIME`,`SORT_PATH`,`IS_ORG_ADMIN`,`IS_USER_GROUP_ADMIN`,`CODE`,`DATA_VERSION`) values ('7416563936d84eefafc65325402b380e', '增删改', 'dev-platform', '', 10, '8f9dec2ca33040e199f9e922f906dc7f', 'e924956d25d94c099430748a777a6b16', null, null, '000001000002000010', 0, 0, 'ProDatasource.addOrUpdOrDel', 0);
insert into `SYS_AUTH` (`ID`,`NAME`,`APP_ID`,`REMARK`,`SORT_INDEX`,`PARENT_ID`,`CREATE_USER`,`CREATE_TIME`,`UPDATE_TIME`,`SORT_PATH`,`IS_ORG_ADMIN`,`IS_USER_GROUP_ADMIN`,`CODE`,`DATA_VERSION`) values ('9c844c3686904ccb8ed6c51bd5a7bf5d', '增删改', 'dev-platform', '', 10, '3860126718034cc2a7dbd8311bfb3ff1', 'e924956d25d94c099430748a777a6b16', null, null, '000001000003000001000010', 0, 0, 'service.addOrUpdOrDel', 0);
insert into `SYS_AUTH` (`ID`,`NAME`,`APP_ID`,`REMARK`,`SORT_INDEX`,`PARENT_ID`,`CREATE_USER`,`CREATE_TIME`,`UPDATE_TIME`,`SORT_PATH`,`IS_ORG_ADMIN`,`IS_USER_GROUP_ADMIN`,`CODE`,`DATA_VERSION`) values ('1ea55832fa9c40979e015f8c12c420b3', '增删改', 'dev-platform', '', 10, '2a3c459a0c814a098bdc41cb0e4b830e', 'e924956d25d94c099430748a777a6b16', null, null, '000001000003000002000010', 0, 0, 'proModel.addOrUpdOrDel', 0);
insert into `SYS_AUTH` (`ID`,`NAME`,`APP_ID`,`REMARK`,`SORT_INDEX`,`PARENT_ID`,`CREATE_USER`,`CREATE_TIME`,`UPDATE_TIME`,`SORT_PATH`,`IS_ORG_ADMIN`,`IS_USER_GROUP_ADMIN`,`CODE`,`DATA_VERSION`) values ('5373fd0c912b4c0fa4c46474199f9858', '增删改', 'dev-platform', '', 10, 'e0c7623e577d4811af97dc6eab9c14d6', 'e924956d25d94c099430748a777a6b16', null, null, '000001000003000003000010', 0, 0, 'interface.addOrUpdOrDel', 0);
insert into `SYS_AUTH` (`ID`,`NAME`,`APP_ID`,`REMARK`,`SORT_INDEX`,`PARENT_ID`,`CREATE_USER`,`CREATE_TIME`,`UPDATE_TIME`,`SORT_PATH`,`IS_ORG_ADMIN`,`IS_USER_GROUP_ADMIN`,`CODE`,`DATA_VERSION`) values ('d9ec0e8823c44aab931666f72bee0965', '增删改', 'dev-platform', '', 10, 'c6d75746103e4dc99d9d0ce27f0e1af1', 'e924956d25d94c099430748a777a6b16', null, null, '000001000003000004000010', 0, 0, 'pageDesign.addOrUpdeOrDel', 0);
/**
menu
*/
insert into `SYS_MENU` (`ID`,`NAME`,`PARENT_ID`,`APP_ID`,`PARENT_NAME`,`URL`,`TYPE`,`ICON`,`AUTH_ID`,`BTN_AUTH`,`IS_LEAF`,`SORT_INDEX`,`ALL_ORDER`,`SORT_PATH`,`OPEN_STYLE`,`CLOSE_NOTICE`,`CREATE_USER`,`CREATE_TIME`,`UPDATE_TIME`,`LEAF`,`MENU_AUTH`,`REMARK`,`DATA_VERSION`,`IS_IFRAME`) values ('227f8578d72c4cd6b78125ab97c2f17d', '项目管理', '0', 'dev-platform', '一级菜单', '', 0, 'fa fa-cubes', '227f8578d72c4cd6b78125ab97c2f17d', null, null, 1, null, '000001', null, null, 'e924956d25d94c099430748a777a6b16', null, null, null, null, '', 0, 0);
insert into `SYS_MENU` (`ID`,`NAME`,`PARENT_ID`,`APP_ID`,`PARENT_NAME`,`URL`,`TYPE`,`ICON`,`AUTH_ID`,`BTN_AUTH`,`IS_LEAF`,`SORT_INDEX`,`ALL_ORDER`,`SORT_PATH`,`OPEN_STYLE`,`CLOSE_NOTICE`,`CREATE_USER`,`CREATE_TIME`,`UPDATE_TIME`,`LEAF`,`MENU_AUTH`,`REMARK`,`DATA_VERSION`,`IS_IFRAME`) values ('a2c96add3b7744c3939083d5351f3828', '项目信息', '227f8578d72c4cd6b78125ab97c2f17d', 'dev-platform', '项目管理', 'devplatform/ProInfoManage/views/ProInfo', 1, 'fa fa-align-justify', 'a2c96add3b7744c3939083d5351f3828', null, null, 1, null, '000001000001', null, null, 'e924956d25d94c099430748a777a6b16', null, null, null, null, '', 0, 0);
insert into `SYS_MENU` (`ID`,`NAME`,`PARENT_ID`,`APP_ID`,`PARENT_NAME`,`URL`,`TYPE`,`ICON`,`AUTH_ID`,`BTN_AUTH`,`IS_LEAF`,`SORT_INDEX`,`ALL_ORDER`,`SORT_PATH`,`OPEN_STYLE`,`CLOSE_NOTICE`,`CREATE_USER`,`CREATE_TIME`,`UPDATE_TIME`,`LEAF`,`MENU_AUTH`,`REMARK`,`DATA_VERSION`,`IS_IFRAME`) values ('8f9dec2ca33040e199f9e922f906dc7f', '数据源管理', '227f8578d72c4cd6b78125ab97c2f17d', 'dev-platform', '项目管理', 'devplatform/ProDatasourceManage/views/ProDatasource', 1, 'fa fa-database', '8f9dec2ca33040e199f9e922f906dc7f', null, null, 2, null, '000001000002', null, null, 'e924956d25d94c099430748a777a6b16', null, null, null, null, '', 0, 0);
insert into `SYS_MENU` (`ID`,`NAME`,`PARENT_ID`,`APP_ID`,`PARENT_NAME`,`URL`,`TYPE`,`ICON`,`AUTH_ID`,`BTN_AUTH`,`IS_LEAF`,`SORT_INDEX`,`ALL_ORDER`,`SORT_PATH`,`OPEN_STYLE`,`CLOSE_NOTICE`,`CREATE_USER`,`CREATE_TIME`,`UPDATE_TIME`,`LEAF`,`MENU_AUTH`,`REMARK`,`DATA_VERSION`,`IS_IFRAME`) values ('ad2b809bf7084d2db69dd56793cb5e1c', '服务管理', '227f8578d72c4cd6b78125ab97c2f17d', 'dev-platform', '项目管理', '', 0, 'fa fa-binoculars', 'ad2b809bf7084d2db69dd56793cb5e1c', null, null, 3, null, '000001000003', null, null, 'e924956d25d94c099430748a777a6b16', null, null, null, null, '', 0, 0);
insert into `SYS_MENU` (`ID`,`NAME`,`PARENT_ID`,`APP_ID`,`PARENT_NAME`,`URL`,`TYPE`,`ICON`,`AUTH_ID`,`BTN_AUTH`,`IS_LEAF`,`SORT_INDEX`,`ALL_ORDER`,`SORT_PATH`,`OPEN_STYLE`,`CLOSE_NOTICE`,`CREATE_USER`,`CREATE_TIME`,`UPDATE_TIME`,`LEAF`,`MENU_AUTH`,`REMARK`,`DATA_VERSION`,`IS_IFRAME`) values ('3860126718034cc2a7dbd8311bfb3ff1', '应用信息', 'ad2b809bf7084d2db69dd56793cb5e1c', 'dev-platform', '服务管理', 'devplatform/ProServiceManage/views/ProService', 1, 'fa fa-gears', '3860126718034cc2a7dbd8311bfb3ff1', null, null, 1, null, '000001000003000001', null, null, 'e924956d25d94c099430748a777a6b16', null, null, null, null, '', 0, 0);
insert into `SYS_MENU` (`ID`,`NAME`,`PARENT_ID`,`APP_ID`,`PARENT_NAME`,`URL`,`TYPE`,`ICON`,`AUTH_ID`,`BTN_AUTH`,`IS_LEAF`,`SORT_INDEX`,`ALL_ORDER`,`SORT_PATH`,`OPEN_STYLE`,`CLOSE_NOTICE`,`CREATE_USER`,`CREATE_TIME`,`UPDATE_TIME`,`LEAF`,`MENU_AUTH`,`REMARK`,`DATA_VERSION`,`IS_IFRAME`) values ('2a3c459a0c814a098bdc41cb0e4b830e', '模型管理', 'ad2b809bf7084d2db69dd56793cb5e1c', 'dev-platform', '服务管理', 'devplatform/ProModelManage/views/ProModel', 1, 'fa fa-briefcase', '2a3c459a0c814a098bdc41cb0e4b830e', null, null, 2, null, '000001000003000002', null, null, 'e924956d25d94c099430748a777a6b16', null, null, null, null, '', 0, 0);
insert into `SYS_MENU` (`ID`,`NAME`,`PARENT_ID`,`APP_ID`,`PARENT_NAME`,`URL`,`TYPE`,`ICON`,`AUTH_ID`,`BTN_AUTH`,`IS_LEAF`,`SORT_INDEX`,`ALL_ORDER`,`SORT_PATH`,`OPEN_STYLE`,`CLOSE_NOTICE`,`CREATE_USER`,`CREATE_TIME`,`UPDATE_TIME`,`LEAF`,`MENU_AUTH`,`REMARK`,`DATA_VERSION`,`IS_IFRAME`) values ('e0c7623e577d4811af97dc6eab9c14d6', '接口管理', 'ad2b809bf7084d2db69dd56793cb5e1c', 'dev-platform', '服务管理', 'devplatform/InterfaceManage/views/Interface', 1, 'fa fa-window-restore', 'e0c7623e577d4811af97dc6eab9c14d6', null, null, 3, null, '000001000003000003', null, null, 'e924956d25d94c099430748a777a6b16', null, null, null, null, '', 0, 0);
insert into `SYS_MENU` (`ID`,`NAME`,`PARENT_ID`,`APP_ID`,`PARENT_NAME`,`URL`,`TYPE`,`ICON`,`AUTH_ID`,`BTN_AUTH`,`IS_LEAF`,`SORT_INDEX`,`ALL_ORDER`,`SORT_PATH`,`OPEN_STYLE`,`CLOSE_NOTICE`,`CREATE_USER`,`CREATE_TIME`,`UPDATE_TIME`,`LEAF`,`MENU_AUTH`,`REMARK`,`DATA_VERSION`,`IS_IFRAME`) values ('c6d75746103e4dc99d9d0ce27f0e1af1', '页面设计', 'ad2b809bf7084d2db69dd56793cb5e1c', 'dev-platform', '服务管理', 'devplatform/PageDesignManage/views/PageDesign', 1, 'fa fa-window-maximize', 'c6d75746103e4dc99d9d0ce27f0e1af1', null, null, 4, null, '000001000003000004', null, null, 'e924956d25d94c099430748a777a6b16', null, null, null, null, '', 0, 0);
insert into `SYS_MENU` (`ID`,`NAME`,`PARENT_ID`,`APP_ID`,`PARENT_NAME`,`URL`,`TYPE`,`ICON`,`AUTH_ID`,`BTN_AUTH`,`IS_LEAF`,`SORT_INDEX`,`ALL_ORDER`,`SORT_PATH`,`OPEN_STYLE`,`CLOSE_NOTICE`,`CREATE_USER`,`CREATE_TIME`,`UPDATE_TIME`,`LEAF`,`MENU_AUTH`,`REMARK`,`DATA_VERSION`,`IS_IFRAME`) values ('09a55900eb974483b599b4343afb7c52', '系统管理', '0', 'dev-platform', '一级菜单', '', 0, 'fa fa-archive', '09a55900eb974483b599b4343afb7c52', null, null, 2, null, '000002', null, null, 'e924956d25d94c099430748a777a6b16', null, null, null, null, '', 0, 0);
insert into `SYS_MENU` (`ID`,`NAME`,`PARENT_ID`,`APP_ID`,`PARENT_NAME`,`URL`,`TYPE`,`ICON`,`AUTH_ID`,`BTN_AUTH`,`IS_LEAF`,`SORT_INDEX`,`ALL_ORDER`,`SORT_PATH`,`OPEN_STYLE`,`CLOSE_NOTICE`,`CREATE_USER`,`CREATE_TIME`,`UPDATE_TIME`,`LEAF`,`MENU_AUTH`,`REMARK`,`DATA_VERSION`,`IS_IFRAME`) values ('e668d7fddbac4aa3a3fdb6502d00ac7d', '用户管理', '09a55900eb974483b599b4343afb7c52', 'dev-platform', '系统管理', 'devplatform/UserManage/views/DevUser', 1, 'fa fa-user', 'e668d7fddbac4aa3a3fdb6502d00ac7d', null, null, 1, null, '000002000001', null, null, 'e924956d25d94c099430748a777a6b16', null, null, null, null, '', 0, 0);
/**
authRole
*/
insert into `SYS_AUTH_ROLE` (`ID`,`AUTH_ID`,`ROLE_ID`,`SIGN`,`IS_REVOKE`,`CREATE_USER`,`CREATE_TIME`,`UPDATE_TIME`,`REMARK`,`AUTH_NAME`,`ROLE_NAME`) values ('04cf67c89b9b4fcca2387096ea809ace', '227f8578d72c4cd6b78125ab97c2f17d', 'devadmin', null, 0, '1d25e7e5ce164c95bd283f7d6c9b644a', null, null, null, '项目管理', 'devadmin');
insert into `SYS_AUTH_ROLE` (`ID`,`AUTH_ID`,`ROLE_ID`,`SIGN`,`IS_REVOKE`,`CREATE_USER`,`CREATE_TIME`,`UPDATE_TIME`,`REMARK`,`AUTH_NAME`,`ROLE_NAME`) values ('de0be48b7de54f438270d57d2189c7b4', 'a2c96add3b7744c3939083d5351f3828', 'devadmin', null, 0, '1d25e7e5ce164c95bd283f7d6c9b644a', null, null, null, '项目信息', 'devadmin');
insert into `SYS_AUTH_ROLE` (`ID`,`AUTH_ID`,`ROLE_ID`,`SIGN`,`IS_REVOKE`,`CREATE_USER`,`CREATE_TIME`,`UPDATE_TIME`,`REMARK`,`AUTH_NAME`,`ROLE_NAME`) values ('9f4676acbf954ba3a6954c2cb636d3ce', '8f9dec2ca33040e199f9e922f906dc7f', 'devadmin', null, 0, '1d25e7e5ce164c95bd283f7d6c9b644a', null, null, null, '数据源管理', 'devadmin');
insert into `SYS_AUTH_ROLE` (`ID`,`AUTH_ID`,`ROLE_ID`,`SIGN`,`IS_REVOKE`,`CREATE_USER`,`CREATE_TIME`,`UPDATE_TIME`,`REMARK`,`AUTH_NAME`,`ROLE_NAME`) values ('e6199f9a57e743f1a9a9bcc62e182152', 'ad2b809bf7084d2db69dd56793cb5e1c', 'devadmin', null, 0, '1d25e7e5ce164c95bd283f7d6c9b644a', null, null, null, '服务管理', 'devadmin');
insert into `SYS_AUTH_ROLE` (`ID`,`AUTH_ID`,`ROLE_ID`,`SIGN`,`IS_REVOKE`,`CREATE_USER`,`CREATE_TIME`,`UPDATE_TIME`,`REMARK`,`AUTH_NAME`,`ROLE_NAME`) values ('51c7be6e3dd44e94aff6323b0b5c51a6', '3860126718034cc2a7dbd8311bfb3ff1', 'devadmin', null, 0, '1d25e7e5ce164c95bd283f7d6c9b644a', null, null, null, '应用信息', 'devadmin');
insert into `SYS_AUTH_ROLE` (`ID`,`AUTH_ID`,`ROLE_ID`,`SIGN`,`IS_REVOKE`,`CREATE_USER`,`CREATE_TIME`,`UPDATE_TIME`,`REMARK`,`AUTH_NAME`,`ROLE_NAME`) values ('2dfe00218f0241aeb08910db93df297b', '2a3c459a0c814a098bdc41cb0e4b830e', 'devadmin', null, 0, '1d25e7e5ce164c95bd283f7d6c9b644a', null, null, null, '模型管理', 'devadmin');
insert into `SYS_AUTH_ROLE` (`ID`,`AUTH_ID`,`ROLE_ID`,`SIGN`,`IS_REVOKE`,`CREATE_USER`,`CREATE_TIME`,`UPDATE_TIME`,`REMARK`,`AUTH_NAME`,`ROLE_NAME`) values ('08b8f310fab643a5ba5eb1b6eba750fa', 'e0c7623e577d4811af97dc6eab9c14d6', 'devadmin', null, 0, '1d25e7e5ce164c95bd283f7d6c9b644a', null, null, null, '接口管理', 'devadmin');
insert into `SYS_AUTH_ROLE` (`ID`,`AUTH_ID`,`ROLE_ID`,`SIGN`,`IS_REVOKE`,`CREATE_USER`,`CREATE_TIME`,`UPDATE_TIME`,`REMARK`,`AUTH_NAME`,`ROLE_NAME`) values ('e8bcac4e780e4519a3366cf17315f8f0', 'c6d75746103e4dc99d9d0ce27f0e1af1', 'devadmin', null, 0, '1d25e7e5ce164c95bd283f7d6c9b644a', null, null, null, '页面设计', 'devadmin');
insert into `SYS_AUTH_ROLE` (`ID`,`AUTH_ID`,`ROLE_ID`,`SIGN`,`IS_REVOKE`,`CREATE_USER`,`CREATE_TIME`,`UPDATE_TIME`,`REMARK`,`AUTH_NAME`,`ROLE_NAME`) values ('da10a934b1594f0186632e4648a6be11', '09a55900eb974483b599b4343afb7c52', 'devadmin', null, 0, '1d25e7e5ce164c95bd283f7d6c9b644a', null, null, null, '系统管理', 'devadmin');
insert into `SYS_AUTH_ROLE` (`ID`,`AUTH_ID`,`ROLE_ID`,`SIGN`,`IS_REVOKE`,`CREATE_USER`,`CREATE_TIME`,`UPDATE_TIME`,`REMARK`,`AUTH_NAME`,`ROLE_NAME`) values ('0a20ddf3dbc34d54a3dda4f29bfe76e4', 'e668d7fddbac4aa3a3fdb6502d00ac7d', 'devadmin', null, 0, '1d25e7e5ce164c95bd283f7d6c9b644a', null, null, null, '用户管理', 'devadmin');
insert into `SYS_AUTH_ROLE` (`ID`,`AUTH_ID`,`ROLE_ID`,`SIGN`,`IS_REVOKE`,`CREATE_USER`,`CREATE_TIME`,`UPDATE_TIME`,`REMARK`,`AUTH_NAME`,`ROLE_NAME`) values ('63fcfef7a9474548bb0646c9b4d00a57', '8f9dec2ca33040e199f9e922f906dc7f', 'devdeveloper', null, 0, '1d25e7e5ce164c95bd283f7d6c9b644a', null, null, null, '数据源管理', 'devdeveloper');
insert into `SYS_AUTH_ROLE` (`ID`,`AUTH_ID`,`ROLE_ID`,`SIGN`,`IS_REVOKE`,`CREATE_USER`,`CREATE_TIME`,`UPDATE_TIME`,`REMARK`,`AUTH_NAME`,`ROLE_NAME`) values ('9bcf56b499a6434598c12ac59b0a7a7d', '7416563936d84eefafc65325402b380e', 'devdeveloper', null, 0, '1d25e7e5ce164c95bd283f7d6c9b644a', null, null, null, '增删改', 'devdeveloper');
insert into `SYS_AUTH_ROLE` (`ID`,`AUTH_ID`,`ROLE_ID`,`SIGN`,`IS_REVOKE`,`CREATE_USER`,`CREATE_TIME`,`UPDATE_TIME`,`REMARK`,`AUTH_NAME`,`ROLE_NAME`) values ('f7255dc72c594baa8f57a201cfa3e4e6', 'ad2b809bf7084d2db69dd56793cb5e1c', 'devdeveloper', null, 0, '1d25e7e5ce164c95bd283f7d6c9b644a', null, null, null, '服务管理', 'devdeveloper');
insert into `SYS_AUTH_ROLE` (`ID`,`AUTH_ID`,`ROLE_ID`,`SIGN`,`IS_REVOKE`,`CREATE_USER`,`CREATE_TIME`,`UPDATE_TIME`,`REMARK`,`AUTH_NAME`,`ROLE_NAME`) values ('56336e15844a4e3291380467f75923c4', '3860126718034cc2a7dbd8311bfb3ff1', 'devdeveloper', null, 0, '1d25e7e5ce164c95bd283f7d6c9b644a', null, null, null, '应用信息', 'devdeveloper');
insert into `SYS_AUTH_ROLE` (`ID`,`AUTH_ID`,`ROLE_ID`,`SIGN`,`IS_REVOKE`,`CREATE_USER`,`CREATE_TIME`,`UPDATE_TIME`,`REMARK`,`AUTH_NAME`,`ROLE_NAME`) values ('913e1e09cc9f43ac8990a36a493e2071', '9c844c3686904ccb8ed6c51bd5a7bf5d', 'devdeveloper', null, 0, '1d25e7e5ce164c95bd283f7d6c9b644a', null, null, null, '增删改', 'devdeveloper');
insert into `SYS_AUTH_ROLE` (`ID`,`AUTH_ID`,`ROLE_ID`,`SIGN`,`IS_REVOKE`,`CREATE_USER`,`CREATE_TIME`,`UPDATE_TIME`,`REMARK`,`AUTH_NAME`,`ROLE_NAME`) values ('2177ca2503ef42f4a545212619e82e09', '2a3c459a0c814a098bdc41cb0e4b830e', 'devdeveloper', null, 0, '1d25e7e5ce164c95bd283f7d6c9b644a', null, null, null, '模型管理', 'devdeveloper');
insert into `SYS_AUTH_ROLE` (`ID`,`AUTH_ID`,`ROLE_ID`,`SIGN`,`IS_REVOKE`,`CREATE_USER`,`CREATE_TIME`,`UPDATE_TIME`,`REMARK`,`AUTH_NAME`,`ROLE_NAME`) values ('bba9df1c426d4ca5843d050a692438ed', '1ea55832fa9c40979e015f8c12c420b3', 'devdeveloper', null, 0, '1d25e7e5ce164c95bd283f7d6c9b644a', null, null, null, '增删改', 'devdeveloper');
insert into `SYS_AUTH_ROLE` (`ID`,`AUTH_ID`,`ROLE_ID`,`SIGN`,`IS_REVOKE`,`CREATE_USER`,`CREATE_TIME`,`UPDATE_TIME`,`REMARK`,`AUTH_NAME`,`ROLE_NAME`) values ('32f524b1016e4720a81e2ebb9d010385', 'e0c7623e577d4811af97dc6eab9c14d6', 'devdeveloper', null, 0, '1d25e7e5ce164c95bd283f7d6c9b644a', null, null, null, '接口管理', 'devdeveloper');
insert into `SYS_AUTH_ROLE` (`ID`,`AUTH_ID`,`ROLE_ID`,`SIGN`,`IS_REVOKE`,`CREATE_USER`,`CREATE_TIME`,`UPDATE_TIME`,`REMARK`,`AUTH_NAME`,`ROLE_NAME`) values ('39449e153dad40dead0ec02bab2a65b1', '5373fd0c912b4c0fa4c46474199f9858', 'devdeveloper', null, 0, '1d25e7e5ce164c95bd283f7d6c9b644a', null, null, null, '增删改', 'devdeveloper');
insert into `SYS_AUTH_ROLE` (`ID`,`AUTH_ID`,`ROLE_ID`,`SIGN`,`IS_REVOKE`,`CREATE_USER`,`CREATE_TIME`,`UPDATE_TIME`,`REMARK`,`AUTH_NAME`,`ROLE_NAME`) values ('4772d682ebc547c8ac7706b799cd7493', 'c6d75746103e4dc99d9d0ce27f0e1af1', 'devdeveloper', null, 0, '1d25e7e5ce164c95bd283f7d6c9b644a', null, null, null, '页面设计', 'devdeveloper');
insert into `SYS_AUTH_ROLE` (`ID`,`AUTH_ID`,`ROLE_ID`,`SIGN`,`IS_REVOKE`,`CREATE_USER`,`CREATE_TIME`,`UPDATE_TIME`,`REMARK`,`AUTH_NAME`,`ROLE_NAME`) values ('2e9fba4f85f34f019e9e079758dc17a1', 'd9ec0e8823c44aab931666f72bee0965', 'devdeveloper', null, 0, '1d25e7e5ce164c95bd283f7d6c9b644a', null, null, null, '增删改', 'devdeveloper');
insert into `SYS_AUTH_ROLE` (`ID`,`AUTH_ID`,`ROLE_ID`,`SIGN`,`IS_REVOKE`,`CREATE_USER`,`CREATE_TIME`,`UPDATE_TIME`,`REMARK`,`AUTH_NAME`,`ROLE_NAME`) values ('201787a581b44b9d955f6c09ed5d3e41', '227f8578d72c4cd6b78125ab97c2f17d', 'devdeveloper', null, 0, '1d25e7e5ce164c95bd283f7d6c9b644a', null, null, null, '项目管理', 'devdeveloper');
insert into `SYS_AUTH_ROLE` (`ID`,`AUTH_ID`,`ROLE_ID`,`SIGN`,`IS_REVOKE`,`CREATE_USER`,`CREATE_TIME`,`UPDATE_TIME`,`REMARK`,`AUTH_NAME`,`ROLE_NAME`) values ('237153192b1b4a11b850bad035bda80e', 'a2c96add3b7744c3939083d5351f3828', 'devmaintainer', null, 0, '1d25e7e5ce164c95bd283f7d6c9b644a', null, null, null, '项目信息', 'devmaintainer');
insert into `SYS_AUTH_ROLE` (`ID`,`AUTH_ID`,`ROLE_ID`,`SIGN`,`IS_REVOKE`,`CREATE_USER`,`CREATE_TIME`,`UPDATE_TIME`,`REMARK`,`AUTH_NAME`,`ROLE_NAME`) values ('8856daf09bce48a2924ad53d923ecef2', '74b709f180014414831c66d0ff290a49', 'devmaintainer', null, 0, '1d25e7e5ce164c95bd283f7d6c9b644a', null, null, null, '增删改', 'devmaintainer');
insert into `SYS_AUTH_ROLE` (`ID`,`AUTH_ID`,`ROLE_ID`,`SIGN`,`IS_REVOKE`,`CREATE_USER`,`CREATE_TIME`,`UPDATE_TIME`,`REMARK`,`AUTH_NAME`,`ROLE_NAME`) values ('a58946f890d24cab8bf26e4ed1ee8a2f', '8f9dec2ca33040e199f9e922f906dc7f', 'devmaintainer', null, 0, '1d25e7e5ce164c95bd283f7d6c9b644a', null, null, null, '数据源管理', 'devmaintainer');
insert into `SYS_AUTH_ROLE` (`ID`,`AUTH_ID`,`ROLE_ID`,`SIGN`,`IS_REVOKE`,`CREATE_USER`,`CREATE_TIME`,`UPDATE_TIME`,`REMARK`,`AUTH_NAME`,`ROLE_NAME`) values ('c75d7aa985ea496ba2a8784adfe40f09', 'ad2b809bf7084d2db69dd56793cb5e1c', 'devmaintainer', null, 0, '1d25e7e5ce164c95bd283f7d6c9b644a', null, null, null, '服务管理', 'devmaintainer');
insert into `SYS_AUTH_ROLE` (`ID`,`AUTH_ID`,`ROLE_ID`,`SIGN`,`IS_REVOKE`,`CREATE_USER`,`CREATE_TIME`,`UPDATE_TIME`,`REMARK`,`AUTH_NAME`,`ROLE_NAME`) values ('b172fdac4cce47c89516a337d523e938', '3860126718034cc2a7dbd8311bfb3ff1', 'devmaintainer', null, 0, '1d25e7e5ce164c95bd283f7d6c9b644a', null, null, null, '应用信息', 'devmaintainer');
insert into `SYS_AUTH_ROLE` (`ID`,`AUTH_ID`,`ROLE_ID`,`SIGN`,`IS_REVOKE`,`CREATE_USER`,`CREATE_TIME`,`UPDATE_TIME`,`REMARK`,`AUTH_NAME`,`ROLE_NAME`) values ('cbcfae2bec734c3ea6c6fc78c7552c0c', '2a3c459a0c814a098bdc41cb0e4b830e', 'devmaintainer', null, 0, '1d25e7e5ce164c95bd283f7d6c9b644a', null, null, null, '模型管理', 'devmaintainer');
insert into `SYS_AUTH_ROLE` (`ID`,`AUTH_ID`,`ROLE_ID`,`SIGN`,`IS_REVOKE`,`CREATE_USER`,`CREATE_TIME`,`UPDATE_TIME`,`REMARK`,`AUTH_NAME`,`ROLE_NAME`) values ('cdbd5a12a6d7475698a69edb108294f1', 'e0c7623e577d4811af97dc6eab9c14d6', 'devmaintainer', null, 0, '1d25e7e5ce164c95bd283f7d6c9b644a', null, null, null, '接口管理', 'devmaintainer');
insert into `SYS_AUTH_ROLE` (`ID`,`AUTH_ID`,`ROLE_ID`,`SIGN`,`IS_REVOKE`,`CREATE_USER`,`CREATE_TIME`,`UPDATE_TIME`,`REMARK`,`AUTH_NAME`,`ROLE_NAME`) values ('3dc152ab58124b81bd68cbd249944644', 'c6d75746103e4dc99d9d0ce27f0e1af1', 'devmaintainer', null, 0, '1d25e7e5ce164c95bd283f7d6c9b644a', null, null, null, '页面设计', 'devmaintainer');
insert into `SYS_AUTH_ROLE` (`ID`,`AUTH_ID`,`ROLE_ID`,`SIGN`,`IS_REVOKE`,`CREATE_USER`,`CREATE_TIME`,`UPDATE_TIME`,`REMARK`,`AUTH_NAME`,`ROLE_NAME`) values ('07d034a530944d2c92e2a01db9ebaa42', '227f8578d72c4cd6b78125ab97c2f17d', 'devmaintainer', null, 0, '1d25e7e5ce164c95bd283f7d6c9b644a', null, null, null, '项目管理', 'devmaintainer');
insert into `SYS_AUTH_ROLE` (`ID`,`AUTH_ID`,`ROLE_ID`,`SIGN`,`IS_REVOKE`,`CREATE_USER`,`CREATE_TIME`,`UPDATE_TIME`,`REMARK`,`AUTH_NAME`,`ROLE_NAME`) values ('shuju3192b1b4a11b850bad035bda80e', '7416563936d84eefafc65325402b380e', 'devmaintainer', null, 0, '1d25e7e5ce164c95bd283f7d6c9b644a', null, null, null, '增删改', 'devmaintainer');
insert into `SYS_AUTH_ROLE` (`ID`,`AUTH_ID`,`ROLE_ID`,`SIGN`,`IS_REVOKE`,`CREATE_USER`,`CREATE_TIME`,`UPDATE_TIME`,`REMARK`,`AUTH_NAME`,`ROLE_NAME`) values ('fuwu1e09cc9f43ac8990a36a493e2071', '9c844c3686904ccb8ed6c51bd5a7bf5d', 'devmaintainer', null, 0, '1d25e7e5ce164c95bd283f7d6c9b644a', null, null, null, '增删改', 'devmaintainer');
insert into `SYS_AUTH_ROLE` (`ID`,`AUTH_ID`,`ROLE_ID`,`SIGN`,`IS_REVOKE`,`CREATE_USER`,`CREATE_TIME`,`UPDATE_TIME`,`REMARK`,`AUTH_NAME`,`ROLE_NAME`) values ('moxing1c426d4ca5843d050a692438ed', '1ea55832fa9c40979e015f8c12c420b3', 'devmaintainer', null, 0, '1d25e7e5ce164c95bd283f7d6c9b644a', null, null, null, '增删改', 'devmaintainer');
insert into `SYS_AUTH_ROLE` (`ID`,`AUTH_ID`,`ROLE_ID`,`SIGN`,`IS_REVOKE`,`CREATE_USER`,`CREATE_TIME`,`UPDATE_TIME`,`REMARK`,`AUTH_NAME`,`ROLE_NAME`) values ('jiekoue53dad40dead0ec02bab2a65b1', '5373fd0c912b4c0fa4c46474199f9858', 'devmaintainer', null, 0, '1d25e7e5ce164c95bd283f7d6c9b644a', null, null, null, '增删改', 'devmaintainer');
insert into `SYS_AUTH_ROLE` (`ID`,`AUTH_ID`,`ROLE_ID`,`SIGN`,`IS_REVOKE`,`CREATE_USER`,`CREATE_TIME`,`UPDATE_TIME`,`REMARK`,`AUTH_NAME`,`ROLE_NAME`) values ('yemian4f85f34f019e9e079758dc17a1', 'd9ec0e8823c44aab931666f72bee0965', 'devmaintainer', null, 0, '1d25e7e5ce164c95bd283f7d6c9b644a', null, null, null, '增删改', 'devmaintainer');
/**
authRole_LV
*/
insert into `SYS_AUTH_ROLE_LV` (`ID`,`VERSION_NO`,`IS_LAST_VERSION`,`VERSION_BEGIN_TIME`,`VERSION_END_TIME`,`VERSION_BEGIN_USER_ID`,`VERSION_END_USER_ID`,`ROLE_ID`,`APP_ID`) values ('a7290384e3f84287bedb968f1c38ad21', 0, 1, '2020-07-13 08:06:17.994000', null, 'e924956d25d94c099430748a777a6b16', null, 'devadmin', 'dev-platform');
insert into `SYS_AUTH_ROLE_LV` (`ID`,`VERSION_NO`,`IS_LAST_VERSION`,`VERSION_BEGIN_TIME`,`VERSION_END_TIME`,`VERSION_BEGIN_USER_ID`,`VERSION_END_USER_ID`,`ROLE_ID`,`APP_ID`) values ('ec0818a993014e8e9f036a0dfce2905d', 0, 1, '2020-07-13 08:06:18.091000', null, 'e924956d25d94c099430748a777a6b16', null, 'devdeveloper', 'dev-platform');
insert into `SYS_AUTH_ROLE_LV` (`ID`,`VERSION_NO`,`IS_LAST_VERSION`,`VERSION_BEGIN_TIME`,`VERSION_END_TIME`,`VERSION_BEGIN_USER_ID`,`VERSION_END_USER_ID`,`ROLE_ID`,`APP_ID`) values ('ee1fe11928804140839926be7ac23c23', 0, 1, '2020-07-13 08:06:17.883000', null, 'e924956d25d94c099430748a777a6b16', null, 'devmaintainer', 'dev-platform');

/**
authRole_V
*/
insert into `SYS_AUTH_ROLE_V` (`ID`,`AUTH_ID`,`ROLE_ID`,`SIGN`,`IS_REVOKE`,`CREATE_USER`,`CREATE_TIME`,`UPDATE_TIME`,`REMARK`,`LV_ID`,`AUTH_NAME`) values ('shuju3192b1b4a11b850bad035bda80e', '7416563936d84eefafc65325402b380e', 'devmaintainer', null, 0, '1d25e7e5ce164c95bd283f7d6c9b644a', null, null, null, 'ee1fe11928804140839926be7ac23c23', '增删改');
insert into `SYS_AUTH_ROLE_V` (`ID`,`AUTH_ID`,`ROLE_ID`,`SIGN`,`IS_REVOKE`,`CREATE_USER`,`CREATE_TIME`,`UPDATE_TIME`,`REMARK`,`LV_ID`,`AUTH_NAME`) values ('fuwu1e09cc9f43ac8990a36a493e2071', '9c844c3686904ccb8ed6c51bd5a7bf5d', 'devmaintainer', null, 0, '1d25e7e5ce164c95bd283f7d6c9b644a', null, null, null, 'ee1fe11928804140839926be7ac23c23', '增删改');
insert into `SYS_AUTH_ROLE_V` (`ID`,`AUTH_ID`,`ROLE_ID`,`SIGN`,`IS_REVOKE`,`CREATE_USER`,`CREATE_TIME`,`UPDATE_TIME`,`REMARK`,`LV_ID`,`AUTH_NAME`) values ('moxing1c426d4ca5843d050a692438ed', '1ea55832fa9c40979e015f8c12c420b3', 'devmaintainer', null, 0, '1d25e7e5ce164c95bd283f7d6c9b644a', null, null, null, 'ee1fe11928804140839926be7ac23c23', '增删改');
insert into `SYS_AUTH_ROLE_V` (`ID`,`AUTH_ID`,`ROLE_ID`,`SIGN`,`IS_REVOKE`,`CREATE_USER`,`CREATE_TIME`,`UPDATE_TIME`,`REMARK`,`LV_ID`,`AUTH_NAME`) values ('jiekoue53dad40dead0ec02bab2a65b1', '5373fd0c912b4c0fa4c46474199f9858', 'devmaintainer', null, 0, '1d25e7e5ce164c95bd283f7d6c9b644a', null, null, null, 'ee1fe11928804140839926be7ac23c23', '增删改');
insert into `SYS_AUTH_ROLE_V` (`ID`,`AUTH_ID`,`ROLE_ID`,`SIGN`,`IS_REVOKE`,`CREATE_USER`,`CREATE_TIME`,`UPDATE_TIME`,`REMARK`,`LV_ID`,`AUTH_NAME`) values ('yemian4f85f34f019e9e079758dc17a1', 'd9ec0e8823c44aab931666f72bee0965', 'devmaintainer', null, 0, '1d25e7e5ce164c95bd283f7d6c9b644a', null, null, null, 'ee1fe11928804140839926be7ac23c23', '增删改');

insert into `SYS_AUTH_ROLE_V` (`ID`,`AUTH_ID`,`ROLE_ID`,`SIGN`,`IS_REVOKE`,`CREATE_USER`,`CREATE_TIME`,`UPDATE_TIME`,`REMARK`,`LV_ID`,`AUTH_NAME`) values ('bb7f28b20a584c35925e5ef416863d5c', 'a2c96add3b7744c3939083d5351f3828', 'devmaintainer', null, 0, '1d25e7e5ce164c95bd283f7d6c9b644a', null, null, null, 'ee1fe11928804140839926be7ac23c23', '项目信息');
insert into `SYS_AUTH_ROLE_V` (`ID`,`AUTH_ID`,`ROLE_ID`,`SIGN`,`IS_REVOKE`,`CREATE_USER`,`CREATE_TIME`,`UPDATE_TIME`,`REMARK`,`LV_ID`,`AUTH_NAME`) values ('810e9c32112d47c78f66133f584241dd', '74b709f180014414831c66d0ff290a49', 'devmaintainer', null, 0, '1d25e7e5ce164c95bd283f7d6c9b644a', null, null, null, 'ee1fe11928804140839926be7ac23c23', '增删改');
insert into `SYS_AUTH_ROLE_V` (`ID`,`AUTH_ID`,`ROLE_ID`,`SIGN`,`IS_REVOKE`,`CREATE_USER`,`CREATE_TIME`,`UPDATE_TIME`,`REMARK`,`LV_ID`,`AUTH_NAME`) values ('ac36d037ec944375b4d09c9afd395b3a', '8f9dec2ca33040e199f9e922f906dc7f', 'devmaintainer', null, 0, '1d25e7e5ce164c95bd283f7d6c9b644a', null, null, null, 'ee1fe11928804140839926be7ac23c23', '数据源管理');
insert into `SYS_AUTH_ROLE_V` (`ID`,`AUTH_ID`,`ROLE_ID`,`SIGN`,`IS_REVOKE`,`CREATE_USER`,`CREATE_TIME`,`UPDATE_TIME`,`REMARK`,`LV_ID`,`AUTH_NAME`) values ('02140d71ecbc4cbc9912e778822f1e8a', 'ad2b809bf7084d2db69dd56793cb5e1c', 'devmaintainer', null, 0, '1d25e7e5ce164c95bd283f7d6c9b644a', null, null, null, 'ee1fe11928804140839926be7ac23c23', '服务管理');
insert into `SYS_AUTH_ROLE_V` (`ID`,`AUTH_ID`,`ROLE_ID`,`SIGN`,`IS_REVOKE`,`CREATE_USER`,`CREATE_TIME`,`UPDATE_TIME`,`REMARK`,`LV_ID`,`AUTH_NAME`) values ('06d9cfe93bae4144a2dbc37b6b97f44b', '3860126718034cc2a7dbd8311bfb3ff1', 'devmaintainer', null, 0, '1d25e7e5ce164c95bd283f7d6c9b644a', null, null, null, 'ee1fe11928804140839926be7ac23c23', '应用信息');
insert into `SYS_AUTH_ROLE_V` (`ID`,`AUTH_ID`,`ROLE_ID`,`SIGN`,`IS_REVOKE`,`CREATE_USER`,`CREATE_TIME`,`UPDATE_TIME`,`REMARK`,`LV_ID`,`AUTH_NAME`) values ('35b506fa12974bc58f69070d72d1e07f', '2a3c459a0c814a098bdc41cb0e4b830e', 'devmaintainer', null, 0, '1d25e7e5ce164c95bd283f7d6c9b644a', null, null, null, 'ee1fe11928804140839926be7ac23c23', '模型管理');
insert into `SYS_AUTH_ROLE_V` (`ID`,`AUTH_ID`,`ROLE_ID`,`SIGN`,`IS_REVOKE`,`CREATE_USER`,`CREATE_TIME`,`UPDATE_TIME`,`REMARK`,`LV_ID`,`AUTH_NAME`) values ('057698259eb4475c9de5877709380f85', 'e0c7623e577d4811af97dc6eab9c14d6', 'devmaintainer', null, 0, '1d25e7e5ce164c95bd283f7d6c9b644a', null, null, null, 'ee1fe11928804140839926be7ac23c23', '接口管理');
insert into `SYS_AUTH_ROLE_V` (`ID`,`AUTH_ID`,`ROLE_ID`,`SIGN`,`IS_REVOKE`,`CREATE_USER`,`CREATE_TIME`,`UPDATE_TIME`,`REMARK`,`LV_ID`,`AUTH_NAME`) values ('b35270a92b3f466c92ee8134e64602d3', 'c6d75746103e4dc99d9d0ce27f0e1af1', 'devmaintainer', null, 0, '1d25e7e5ce164c95bd283f7d6c9b644a', null, null, null, 'ee1fe11928804140839926be7ac23c23', '页面设计');
insert into `SYS_AUTH_ROLE_V` (`ID`,`AUTH_ID`,`ROLE_ID`,`SIGN`,`IS_REVOKE`,`CREATE_USER`,`CREATE_TIME`,`UPDATE_TIME`,`REMARK`,`LV_ID`,`AUTH_NAME`) values ('e1a3b9e7247c4243a8f6942d6c2abfff', '227f8578d72c4cd6b78125ab97c2f17d', 'devmaintainer', null, 0, '1d25e7e5ce164c95bd283f7d6c9b644a', null, null, null, 'ee1fe11928804140839926be7ac23c23', '项目管理');
insert into `SYS_AUTH_ROLE_V` (`ID`,`AUTH_ID`,`ROLE_ID`,`SIGN`,`IS_REVOKE`,`CREATE_USER`,`CREATE_TIME`,`UPDATE_TIME`,`REMARK`,`LV_ID`,`AUTH_NAME`) values ('c8f237f26efa4f3f974c0f880ca3f5fc', '227f8578d72c4cd6b78125ab97c2f17d', 'devadmin', null, 0, '1d25e7e5ce164c95bd283f7d6c9b644a', null, null, null, 'a7290384e3f84287bedb968f1c38ad21', '项目管理');
insert into `SYS_AUTH_ROLE_V` (`ID`,`AUTH_ID`,`ROLE_ID`,`SIGN`,`IS_REVOKE`,`CREATE_USER`,`CREATE_TIME`,`UPDATE_TIME`,`REMARK`,`LV_ID`,`AUTH_NAME`) values ('0a09978928a446d983def544237198f2', 'a2c96add3b7744c3939083d5351f3828', 'devadmin', null, 0, '1d25e7e5ce164c95bd283f7d6c9b644a', null, null, null, 'a7290384e3f84287bedb968f1c38ad21', '项目信息');
insert into `SYS_AUTH_ROLE_V` (`ID`,`AUTH_ID`,`ROLE_ID`,`SIGN`,`IS_REVOKE`,`CREATE_USER`,`CREATE_TIME`,`UPDATE_TIME`,`REMARK`,`LV_ID`,`AUTH_NAME`) values ('8e540b8dffa544369cd4e3253accf213', '8f9dec2ca33040e199f9e922f906dc7f', 'devadmin', null, 0, '1d25e7e5ce164c95bd283f7d6c9b644a', null, null, null, 'a7290384e3f84287bedb968f1c38ad21', '数据源管理');
insert into `SYS_AUTH_ROLE_V` (`ID`,`AUTH_ID`,`ROLE_ID`,`SIGN`,`IS_REVOKE`,`CREATE_USER`,`CREATE_TIME`,`UPDATE_TIME`,`REMARK`,`LV_ID`,`AUTH_NAME`) values ('f569b4bded3644f7ac6b0fb8b95afc65', 'ad2b809bf7084d2db69dd56793cb5e1c', 'devadmin', null, 0, '1d25e7e5ce164c95bd283f7d6c9b644a', null, null, null, 'a7290384e3f84287bedb968f1c38ad21', '服务管理');
insert into `SYS_AUTH_ROLE_V` (`ID`,`AUTH_ID`,`ROLE_ID`,`SIGN`,`IS_REVOKE`,`CREATE_USER`,`CREATE_TIME`,`UPDATE_TIME`,`REMARK`,`LV_ID`,`AUTH_NAME`) values ('90bc0a6c442c4a1780fec75f73dd6ea6', '3860126718034cc2a7dbd8311bfb3ff1', 'devadmin', null, 0, '1d25e7e5ce164c95bd283f7d6c9b644a', null, null, null, 'a7290384e3f84287bedb968f1c38ad21', '应用信息');
insert into `SYS_AUTH_ROLE_V` (`ID`,`AUTH_ID`,`ROLE_ID`,`SIGN`,`IS_REVOKE`,`CREATE_USER`,`CREATE_TIME`,`UPDATE_TIME`,`REMARK`,`LV_ID`,`AUTH_NAME`) values ('ae2228ed1fc14c1398ccbb9cf99484d5', '2a3c459a0c814a098bdc41cb0e4b830e', 'devadmin', null, 0, '1d25e7e5ce164c95bd283f7d6c9b644a', null, null, null, 'a7290384e3f84287bedb968f1c38ad21', '模型管理');
insert into `SYS_AUTH_ROLE_V` (`ID`,`AUTH_ID`,`ROLE_ID`,`SIGN`,`IS_REVOKE`,`CREATE_USER`,`CREATE_TIME`,`UPDATE_TIME`,`REMARK`,`LV_ID`,`AUTH_NAME`) values ('1da1394937134d48a65b36969d10e946', 'e0c7623e577d4811af97dc6eab9c14d6', 'devadmin', null, 0, '1d25e7e5ce164c95bd283f7d6c9b644a', null, null, null, 'a7290384e3f84287bedb968f1c38ad21', '接口管理');
insert into `SYS_AUTH_ROLE_V` (`ID`,`AUTH_ID`,`ROLE_ID`,`SIGN`,`IS_REVOKE`,`CREATE_USER`,`CREATE_TIME`,`UPDATE_TIME`,`REMARK`,`LV_ID`,`AUTH_NAME`) values ('7253b0d2b1a540ce919655a4e33918f0', 'c6d75746103e4dc99d9d0ce27f0e1af1', 'devadmin', null, 0, '1d25e7e5ce164c95bd283f7d6c9b644a', null, null, null, 'a7290384e3f84287bedb968f1c38ad21', '页面设计');
insert into `SYS_AUTH_ROLE_V` (`ID`,`AUTH_ID`,`ROLE_ID`,`SIGN`,`IS_REVOKE`,`CREATE_USER`,`CREATE_TIME`,`UPDATE_TIME`,`REMARK`,`LV_ID`,`AUTH_NAME`) values ('92ba1fa375534e5891ec12a854cd584d', '09a55900eb974483b599b4343afb7c52', 'devadmin', null, 0, '1d25e7e5ce164c95bd283f7d6c9b644a', null, null, null, 'a7290384e3f84287bedb968f1c38ad21', '系统管理');
insert into `SYS_AUTH_ROLE_V` (`ID`,`AUTH_ID`,`ROLE_ID`,`SIGN`,`IS_REVOKE`,`CREATE_USER`,`CREATE_TIME`,`UPDATE_TIME`,`REMARK`,`LV_ID`,`AUTH_NAME`) values ('2cc3a95856534b4aa5135807842f1284', 'e668d7fddbac4aa3a3fdb6502d00ac7d', 'devadmin', null, 0, '1d25e7e5ce164c95bd283f7d6c9b644a', null, null, null, 'a7290384e3f84287bedb968f1c38ad21', '用户管理');
insert into `SYS_AUTH_ROLE_V` (`ID`,`AUTH_ID`,`ROLE_ID`,`SIGN`,`IS_REVOKE`,`CREATE_USER`,`CREATE_TIME`,`UPDATE_TIME`,`REMARK`,`LV_ID`,`AUTH_NAME`) values ('4cac209116d341a5a09d889d754c155a', '8f9dec2ca33040e199f9e922f906dc7f', 'devdeveloper', null, 0, '1d25e7e5ce164c95bd283f7d6c9b644a', null, null, null, 'ec0818a993014e8e9f036a0dfce2905d', '数据源管理');
insert into `SYS_AUTH_ROLE_V` (`ID`,`AUTH_ID`,`ROLE_ID`,`SIGN`,`IS_REVOKE`,`CREATE_USER`,`CREATE_TIME`,`UPDATE_TIME`,`REMARK`,`LV_ID`,`AUTH_NAME`) values ('ef0b7f9119804dcebb30244ad08f5100', '7416563936d84eefafc65325402b380e', 'devdeveloper', null, 0, '1d25e7e5ce164c95bd283f7d6c9b644a', null, null, null, 'ec0818a993014e8e9f036a0dfce2905d', '增删改');
insert into `SYS_AUTH_ROLE_V` (`ID`,`AUTH_ID`,`ROLE_ID`,`SIGN`,`IS_REVOKE`,`CREATE_USER`,`CREATE_TIME`,`UPDATE_TIME`,`REMARK`,`LV_ID`,`AUTH_NAME`) values ('35468f0ad4904564b9b0b3770294202f', 'ad2b809bf7084d2db69dd56793cb5e1c', 'devdeveloper', null, 0, '1d25e7e5ce164c95bd283f7d6c9b644a', null, null, null, 'ec0818a993014e8e9f036a0dfce2905d', '服务管理');
insert into `SYS_AUTH_ROLE_V` (`ID`,`AUTH_ID`,`ROLE_ID`,`SIGN`,`IS_REVOKE`,`CREATE_USER`,`CREATE_TIME`,`UPDATE_TIME`,`REMARK`,`LV_ID`,`AUTH_NAME`) values ('92d2d2b8bcf3408eb5395f1b661e9583', '3860126718034cc2a7dbd8311bfb3ff1', 'devdeveloper', null, 0, '1d25e7e5ce164c95bd283f7d6c9b644a', null, null, null, 'ec0818a993014e8e9f036a0dfce2905d', '应用信息');
insert into `SYS_AUTH_ROLE_V` (`ID`,`AUTH_ID`,`ROLE_ID`,`SIGN`,`IS_REVOKE`,`CREATE_USER`,`CREATE_TIME`,`UPDATE_TIME`,`REMARK`,`LV_ID`,`AUTH_NAME`) values ('6abce43f575c4831b08f3ea30cbf4b2e', '9c844c3686904ccb8ed6c51bd5a7bf5d', 'devdeveloper', null, 0, '1d25e7e5ce164c95bd283f7d6c9b644a', null, null, null, 'ec0818a993014e8e9f036a0dfce2905d', '增删改');
insert into `SYS_AUTH_ROLE_V` (`ID`,`AUTH_ID`,`ROLE_ID`,`SIGN`,`IS_REVOKE`,`CREATE_USER`,`CREATE_TIME`,`UPDATE_TIME`,`REMARK`,`LV_ID`,`AUTH_NAME`) values ('e651a44810bc47c8941af46025cbe4cf', '2a3c459a0c814a098bdc41cb0e4b830e', 'devdeveloper', null, 0, '1d25e7e5ce164c95bd283f7d6c9b644a', null, null, null, 'ec0818a993014e8e9f036a0dfce2905d', '模型管理');
insert into `SYS_AUTH_ROLE_V` (`ID`,`AUTH_ID`,`ROLE_ID`,`SIGN`,`IS_REVOKE`,`CREATE_USER`,`CREATE_TIME`,`UPDATE_TIME`,`REMARK`,`LV_ID`,`AUTH_NAME`) values ('8a04a50c7bb8451ba15f1961cbb171da', '1ea55832fa9c40979e015f8c12c420b3', 'devdeveloper', null, 0, '1d25e7e5ce164c95bd283f7d6c9b644a', null, null, null, 'ec0818a993014e8e9f036a0dfce2905d', '增删改');
insert into `SYS_AUTH_ROLE_V` (`ID`,`AUTH_ID`,`ROLE_ID`,`SIGN`,`IS_REVOKE`,`CREATE_USER`,`CREATE_TIME`,`UPDATE_TIME`,`REMARK`,`LV_ID`,`AUTH_NAME`) values ('b29fe8d9a17f4ed990f688bc76a0b068', 'e0c7623e577d4811af97dc6eab9c14d6', 'devdeveloper', null, 0, '1d25e7e5ce164c95bd283f7d6c9b644a', null, null, null, 'ec0818a993014e8e9f036a0dfce2905d', '接口管理');
insert into `SYS_AUTH_ROLE_V` (`ID`,`AUTH_ID`,`ROLE_ID`,`SIGN`,`IS_REVOKE`,`CREATE_USER`,`CREATE_TIME`,`UPDATE_TIME`,`REMARK`,`LV_ID`,`AUTH_NAME`) values ('90cea634d13844a4af119f1307c1ff2d', '5373fd0c912b4c0fa4c46474199f9858', 'devdeveloper', null, 0, '1d25e7e5ce164c95bd283f7d6c9b644a', null, null, null, 'ec0818a993014e8e9f036a0dfce2905d', '增删改');
insert into `SYS_AUTH_ROLE_V` (`ID`,`AUTH_ID`,`ROLE_ID`,`SIGN`,`IS_REVOKE`,`CREATE_USER`,`CREATE_TIME`,`UPDATE_TIME`,`REMARK`,`LV_ID`,`AUTH_NAME`) values ('bcb2ad89f29a433aba5720adf8fb99af', 'c6d75746103e4dc99d9d0ce27f0e1af1', 'devdeveloper', null, 0, '1d25e7e5ce164c95bd283f7d6c9b644a', null, null, null, 'ec0818a993014e8e9f036a0dfce2905d', '页面设计');
insert into `SYS_AUTH_ROLE_V` (`ID`,`AUTH_ID`,`ROLE_ID`,`SIGN`,`IS_REVOKE`,`CREATE_USER`,`CREATE_TIME`,`UPDATE_TIME`,`REMARK`,`LV_ID`,`AUTH_NAME`) values ('be50571590e145c0a7f55e707d55bf65', 'd9ec0e8823c44aab931666f72bee0965', 'devdeveloper', null, 0, '1d25e7e5ce164c95bd283f7d6c9b644a', null, null, null, 'ec0818a993014e8e9f036a0dfce2905d', '增删改');
insert into `SYS_AUTH_ROLE_V` (`ID`,`AUTH_ID`,`ROLE_ID`,`SIGN`,`IS_REVOKE`,`CREATE_USER`,`CREATE_TIME`,`UPDATE_TIME`,`REMARK`,`LV_ID`,`AUTH_NAME`) values ('80452d237e44470994d0d7d5c1f98d89', '227f8578d72c4cd6b78125ab97c2f17d', 'devdeveloper', null, 0, '1d25e7e5ce164c95bd283f7d6c9b644a', null, null, null, 'ec0818a993014e8e9f036a0dfce2905d', '项目管理');

insert into `SYS_AUTH_ROLE` (`ID`,`AUTH_ID`,`ROLE_ID`,`SIGN`,`IS_REVOKE`,`CREATE_USER`,`CREATE_TIME`,`UPDATE_TIME`,`REMARK`,`AUTH_NAME`,`ROLE_NAME`) values ('741787a581b44b9d955f6c09ed5d3e41', 'a2c96add3b7744c3939083d5351f3828', 'devdeveloper', null, 0, '1d25e7e5ce164c95bd283f7d6c9b644a', null, null, null, '项目信息', 'devdeveloper');
insert into `SYS_AUTH_ROLE_V` (`ID`,`AUTH_ID`,`ROLE_ID`,`SIGN`,`IS_REVOKE`,`CREATE_USER`,`CREATE_TIME`,`UPDATE_TIME`,`REMARK`,`LV_ID`,`AUTH_NAME`) values ('34442d237e44470994d0d7d5c1f98d89', 'a2c96add3b7744c3939083d5351f3828', 'devdeveloper', null, 0, '1d25e7e5ce164c95bd283f7d6c9b644a', null, null, null, 'ec0818a993014e8e9f036a0dfce2905d', '项目信息');


/**
 * 增加不登录查看功能
 * @author JonnyJiang
 * @date 2020/7/13 10:27
 */

ALTER TABLE `file_configuration`
ADD COLUMN `IS_ALLOW_DLD_WITHOUT_LOGIN` INT(1) NULL COMMENT '是否允许不登录下载';
/**
建表
 */
CREATE TABLE `pro_info` (
  `ID` varchar(50) NOT NULL COMMENT '主键',
  `NAME` varchar(50) DEFAULT NULL COMMENT '项目名称',
  `CREATE_TIME` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `MAINTAIN_STAFFS` varchar(500) DEFAULT NULL COMMENT '维护人员ID(多人使用,分割)',
  `CODE` varchar(50) DEFAULT NULL COMMENT '标识',
  `REMARK` varchar(2000) DEFAULT NULL COMMENT '备注',
  `IS_DELETE` int(11) DEFAULT '0' COMMENT '是否删除，0否，1是',
  PRIMARY KEY (`ID`)
) COMMENT='项目信息表';

CREATE TABLE `pro_datasource` (
  `ID` varchar(50) NOT NULL COMMENT '主键',
  `NAME` varchar(500) DEFAULT NULL COMMENT '数据源名称',
  `URL` varchar(200) DEFAULT NULL COMMENT '路径',
  `USER_NAME` varchar(200) DEFAULT NULL COMMENT '数据库用户',
  `PASSWORD` varchar(50) DEFAULT NULL COMMENT '密码',
  `DRIVER` varchar(100) DEFAULT NULL COMMENT '驱动名称',
  `SCHEME` varchar(50) DEFAULT NULL COMMENT 'SCHEME名称',
  `REMARK` varchar(2000) DEFAULT NULL COMMENT '备注',
  `TYPE` varchar(50) DEFAULT NULL COMMENT '数据库类型',
  PRIMARY KEY (`ID`)
)  COMMENT='数据源';

CREATE TABLE `pro_service` (
  `ID` varchar(50) NOT NULL COMMENT '主键',
  `NAME` varchar(50) DEFAULT NULL COMMENT '应用名称',
  `IS_ENABLE_AUTH` int(11) DEFAULT '1' COMMENT '启用权限 1启用0不启用',
  `IS_ENABLE_LOG` int(11) DEFAULT '1' COMMENT '启用日志1启用0不启用',
  `DS_ID` varchar(50) DEFAULT NULL COMMENT '数据源id',
  `APP_ID` varchar(200) DEFAULT NULL COMMENT '应用标识',
  `PACKAGE_NAME` varchar(200) DEFAULT NULL COMMENT '包名',
  `CREATE_SERVICE_FLG` int(11) DEFAULT '0' COMMENT '是否生成过服务，0否，1是',
  `IP_PORT` varchar(50) DEFAULT NULL COMMENT '应用发布服务端口',
  `NACOS_SERVER_ADDR` varchar(50) DEFAULT NULL COMMENT 'Nacos服务地址',
  `REDIS_DATA_BASE` int(11) DEFAULT NULL COMMENT 'Redis数据库索引',
  `REDIS_HOST` varchar(50) DEFAULT NULL COMMENT 'Redis服务器地址',
  `REDIS_PORT` varchar(50) DEFAULT NULL COMMENT 'Redis连接端口',
  `REDIS_PASSWORD` varchar(50) DEFAULT NULL COMMENT 'Redis连接密码',
  `PRO_INFO_ID` varchar(50) NOT NULL COMMENT '项目ID',
  `IS_DELETE` int(11) DEFAULT '0' COMMENT '是否删除，0否，1是',
  PRIMARY KEY (`ID`),
  KEY `IDX_PRO_SERVICE_DS_ID` (`DS_ID`),
  KEY `FK_PRO_SERVIE_INFO_ID` (`PRO_INFO_ID`),
  CONSTRAINT `FK_PRO_SERVICE_DS_ID` FOREIGN KEY (`DS_ID`) REFERENCES `pro_datasource` (`ID`) ON DELETE CASCADE ON UPDATE NO ACTION,
  CONSTRAINT `FK_PRO_SERVIE_INFO_ID` FOREIGN KEY (`PRO_INFO_ID`) REFERENCES `pro_info` (`ID`) ON DELETE CASCADE
)  COMMENT='应用管理';

CREATE TABLE `pro_page_info` (
  `ID` varchar(50) NOT NULL COMMENT '主键',
  `NAME` varchar(50) DEFAULT NULL COMMENT '名称',
  `DES` varchar(2000) DEFAULT NULL COMMENT '描述',
  `JS_CODE` longtext COMMENT 'js对象',
  `HTML_CODE` longtext COMMENT 'html对象',
  `MODEL_IDS` varchar(2000) DEFAULT NULL COMMENT '已选数据模型对象ID(英文逗号分隔)',
  `CREATE_TIME` datetime DEFAULT NULL COMMENT '创建时间',
  `SERVICE_ID` varchar(50) DEFAULT NULL COMMENT '服务ID',
  PRIMARY KEY (`ID`),
  KEY `FK_PAGEiNFO_SERVICE_ID` (`SERVICE_ID`),
  CONSTRAINT `FK_PAGEiNFO_SERVICE_ID` FOREIGN KEY (`SERVICE_ID`) REFERENCES `pro_service` (`ID`) ON DELETE CASCADE
)  COMMENT='页面基本信息';

CREATE TABLE `pro_model` (
  `ID` varchar(50) NOT NULL COMMENT '主键',
  `MODEL_NAME` varchar(100) DEFAULT NULL COMMENT '模型名称',
  `TABLE_NAME` varchar(100) DEFAULT NULL COMMENT '数据库表表名',
  `OBJECT_NAME` varchar(100) DEFAULT NULL COMMENT '数据库表实体名',
  `SERVICE_ID` varchar(50) DEFAULT NULL COMMENT '服务ID',
  `REMARK` varchar(200) DEFAULT NULL COMMENT '备注',
  `CREATE_TIME` datetime(6) DEFAULT NULL COMMENT '创建时间',
  `PK_NAME` varchar(50) DEFAULT NULL COMMENT '主键名称',
  PRIMARY KEY (`ID`),
  KEY `IDX_PRO_MODEL_SERVICE_ID` (`SERVICE_ID`),
  CONSTRAINT `FK_ENTITY_MODEL_SERVICE_ID` FOREIGN KEY (`SERVICE_ID`) REFERENCES `pro_service` (`ID`) ON DELETE CASCADE ON UPDATE NO ACTION
)  COMMENT='实体模型信息表';

CREATE TABLE `pro_model_association` (
  `ID` varchar(50) NOT NULL COMMENT '主键',
  `NAME` varchar(50) DEFAULT NULL COMMENT '关联名',
  `MODEL_ID` varchar(50) DEFAULT NULL COMMENT '模型ID',
  `COL_NAMES` varchar(2000) DEFAULT NULL COMMENT '数据列名S',
  `REF_COL_NAMES` varchar(2000) DEFAULT NULL COMMENT '引用外键列名S',
  `REF_MODEL_ID` varchar(50) DEFAULT NULL COMMENT '引用表id',
  `CREATE_TIME` datetime(6) DEFAULT NULL COMMENT '创建时间',
  `DEL_ACTION` varchar(50) DEFAULT '0' COMMENT '删除动作',
  `UPD_ACTION` varchar(50) DEFAULT '0' COMMENT '更新动作',
  `CREATE_IDX` int(11) DEFAULT '0' COMMENT '是否同步创建索引 0否1是',
  `INDEX_NAME` varchar(50) DEFAULT NULL COMMENT '索引名',
  `INDEX_ID` varchar(50) DEFAULT NULL COMMENT '索引列',
  PRIMARY KEY (`ID`),
  KEY `IDX_MODEL_ASS_REF_MODEL_ID` (`REF_MODEL_ID`),
  KEY `IDX_MODEL_ASS_MODEL_ID` (`MODEL_ID`),
  CONSTRAINT `FK_MODEL_ASS_MODEL_ID` FOREIGN KEY (`MODEL_ID`) REFERENCES `pro_model` (`ID`) ON DELETE CASCADE ON UPDATE NO ACTION,
  CONSTRAINT `FK_MODEL_ASS_REF_MODEL_ID` FOREIGN KEY (`REF_MODEL_ID`) REFERENCES `pro_model` (`ID`) ON DELETE CASCADE ON UPDATE NO ACTION
)  COMMENT='数据关联定义';

CREATE TABLE `pro_model_col` (
  `ID` varchar(50) NOT NULL COMMENT '主键',
  `MODEL_ID` varchar(50) DEFAULT NULL COMMENT '模型ID',
  `TAB_COL_NAME` varchar(200) DEFAULT NULL COMMENT '表列名',
  `OBJ_COL_NAME` varchar(200) DEFAULT NULL COMMENT '实体对象列名',
  `CAPTION` varchar(2000) DEFAULT NULL COMMENT '列标题',
  `DATA_TYPE` varchar(50) DEFAULT NULL COMMENT '数据类型',
  `NULLABLE` int(11) DEFAULT '1' COMMENT '能否为空',
  `DEFAULT_VALUE` varchar(100) DEFAULT NULL COMMENT '缺省值',
  `IS_SYSCOL` int(11) DEFAULT '0' COMMENT '是否系统字段',
  `CREATE_TIME` datetime(6) DEFAULT NULL COMMENT '创建时间',
  `PK_FLG` int(11) DEFAULT '0' COMMENT '是否是主键，0否,1是',
  `DATA_PRECISION` int(11) DEFAULT NULL COMMENT '精度',
  `DATA_SCALE` int(11) DEFAULT '0' COMMENT '标度',
  PRIMARY KEY (`ID`),
  KEY `IDX_COL_MODEL_ID` (`MODEL_ID`),
  CONSTRAINT `FK_PRO_MODEL_COL_MODEL_ID` FOREIGN KEY (`MODEL_ID`) REFERENCES `pro_model` (`ID`) ON DELETE CASCADE ON UPDATE NO ACTION
)  COMMENT='数据列';

CREATE TABLE `pro_model_index` (
  `ID` varchar(50) NOT NULL COMMENT '主键',
  `MODEL_ID` varchar(50) DEFAULT NULL COMMENT '模型ID',
  `NAME` varchar(200) DEFAULT NULL COMMENT '名称',
  `COLS` varchar(2000) DEFAULT NULL COMMENT '包含列',
  `IS_UNIQUE` int(11) DEFAULT NULL COMMENT '是否唯一',
  `CREATE_TIME` datetime(6) DEFAULT NULL COMMENT '创建时间',
  `ASS_ACTION` int(11) DEFAULT '0' COMMENT '是否是外键添加的索引，0否1是',
  PRIMARY KEY (`ID`),
  KEY `IDX_MODEL_INDEX_MODEL_ID` (`MODEL_ID`),
  CONSTRAINT `FK_MODEL_INDEX_MODEL_ID` FOREIGN KEY (`MODEL_ID`) REFERENCES `pro_model` (`ID`) ON DELETE CASCADE ON UPDATE NO ACTION
)  COMMENT='索引信息';

CREATE TABLE `pro_interface_category` (
  `ID` varchar(50) NOT NULL COMMENT '主键',
  `NAME` varchar(50) DEFAULT NULL COMMENT '类别名称',
  `REMARK` varchar(2000) DEFAULT NULL COMMENT '备注',
  `CREATE_TIME` datetime DEFAULT NULL COMMENT '创建时间',
  `SERVICE_ID` varchar(50) DEFAULT NULL COMMENT '服务ID',
  PRIMARY KEY (`ID`),
  KEY `FK_CATEGORY_SERVICE_ID` (`SERVICE_ID`),
  CONSTRAINT `FK_CATEGORY_SERVICE_ID` FOREIGN KEY (`SERVICE_ID`) REFERENCES `pro_service` (`ID`) ON DELETE CASCADE
)  COMMENT='接口类别';

CREATE TABLE `pro_interface` (
  `ID` varchar(50) NOT NULL COMMENT '主键',
  `NAME` varchar(50) DEFAULT NULL COMMENT '接口名',
  `IS_PAGE_GET` int(10) DEFAULT '0' COMMENT '是否分页方法,0否1是',
  `SQL_CONTENT` longtext COMMENT 'sql语句',
  `CREATE_TIME` date DEFAULT NULL COMMENT '创建时间',
  `DESCRIPTION` varchar(2000) DEFAULT NULL COMMENT '描述',
  `CODE` varchar(50) DEFAULT NULL COMMENT '标识',
  `CATEGORY_ID` varchar(50) DEFAULT NULL COMMENT '接口类别ID',
  PRIMARY KEY (`ID`),
  KEY `FK_INTERFACE_CATEGORY_ID` (`CATEGORY_ID`),
  CONSTRAINT `FK_INTERFACE_CATEGORY_ID` FOREIGN KEY (`CATEGORY_ID`) REFERENCES `pro_interface_category` (`ID`) ON DELETE CASCADE
)  COMMENT='应用接口';

CREATE TABLE `pro_input_params` (
  `ID` varchar(50) NOT NULL COMMENT '主键',
  `PARAM_KEY` varchar(50) DEFAULT NULL COMMENT '键',
  `PARAM_DEFAULT_VALUE` varchar(100) DEFAULT NULL COMMENT '值',
  `PARAM_TYPE` varchar(50) DEFAULT NULL COMMENT '参数类型',
  `INTERFACE_ID` varchar(50) DEFAULT NULL COMMENT '接口主键',
  PRIMARY KEY (`ID`),
  KEY `FK_INPUT_INTEFACE_ID` (`INTERFACE_ID`),
  CONSTRAINT `FK_INPUT_INTEFACE_ID` FOREIGN KEY (`INTERFACE_ID`) REFERENCES `pro_interface` (`ID`) ON DELETE CASCADE
)  COMMENT='接口sql入参信息表';

CREATE TABLE `pro_output_params` (
  `ID` varchar(50) NOT NULL COMMENT '主键',
  `PARAM_KEY` varchar(50) DEFAULT NULL COMMENT '键',
  `PARAM_TYPE` varchar(50) DEFAULT NULL COMMENT '参数类型',
  `INTERFACE_ID` varchar(50) DEFAULT NULL COMMENT '接口主键',
  `PARAM_LABEL` varchar(100) DEFAULT NULL COMMENT '字段注释',
  PRIMARY KEY (`ID`),
  KEY `FK_OUTPUT_INTERFACE_ID` (`INTERFACE_ID`),
  CONSTRAINT `FK_OUTPUT_INTERFACE_ID` FOREIGN KEY (`INTERFACE_ID`) REFERENCES `pro_interface` (`ID`) ON DELETE CASCADE
)  COMMENT='接口sql出参信息表';

CREATE TABLE `pro_changelog_history` (
  `ID` varchar(255) NOT NULL COMMENT '主键',
  `LOG_VALUE` longtext COMMENT 'changeset对象',
  `PUBLISH_TAG` int(11) DEFAULT NULL COMMENT '是否发布过，0否1是',
  `SERVICE_ID` varchar(50) DEFAULT NULL COMMENT '服务ID',
  `PUBLISH_TIME` datetime(6) DEFAULT NULL COMMENT '发布时间',
  `CREATE_TIME` datetime(6) DEFAULT NULL COMMENT '创建时间',
  `PUBLISH_VERSION` int(10) DEFAULT NULL COMMENT '发布的版本',
  PRIMARY KEY (`ID`),
  KEY `FK_LOG_HISTORY_SERVICE_ID` (`SERVICE_ID`),
  CONSTRAINT `FK_LOG_HISTORY_SERVICE_ID` FOREIGN KEY (`SERVICE_ID`) REFERENCES `pro_service` (`ID`) ON DELETE CASCADE ON UPDATE CASCADE
)  COMMENT='changlog历史信息表';

/**
 * 大屏展示
 * @param null
 * @return
 * @author zuogang
 * @date 2020/7/15 10:17
 */
CREATE TABLE `BLADE_VISUAL_SHOW`(
  `ID` VARCHAR(50) NOT NULL COMMENT '主键',
  `VISUAL_ID` VARCHAR(50) COMMENT '大屏ID',
  `TYPE` INT COMMENT '类型(0为看版,1为项)',
  `NAME` VARCHAR(50) COMMENT '名称',
  `PARENT_ID` VARCHAR(50) COMMENT '父节点',
  `CONTENT` LONGTEXT COMMENT 'json对象',
  `APP_ID` VARCHAR(50) COMMENT '应用ID',
  `EXHIBITION_TYPE` VARCHAR(50) COMMENT '显示类型',
  PRIMARY KEY (`ID`),
  CONSTRAINT `FK_VISUAL_SHOW_APP_ID` FOREIGN KEY (`APP_ID`) REFERENCES `SYS_GROUP_APP`(`ID`) ON DELETE CASCADE
);


/**
 * 数字序列，服务编码
 * @param null
 * @return
 * @author zuogang
 * @date 2020/7/17 9:40
 */
insert into `SYS_AUTH` (`ID`,`NAME`,`APP_ID`,`REMARK`,`SORT_INDEX`,`PARENT_ID`,`CREATE_USER`,`CREATE_TIME`,`UPDATE_TIME`,`SORT_PATH`,`IS_ORG_ADMIN`,`IS_USER_GROUP_ADMIN`,`CODE`,`DATA_VERSION`) values ('1275967ea4a4e54acdaff4yhmzq8370', '数字序列', 'platform', null,16, '11316279a4a9416a8e20019821e640af', null, null, null, '00030016', 0, 0, 'platForm.codeSequenceManage.codeSequence', 0);

insert into `SYS_MENU` (`ID`,`NAME`,`PARENT_ID`,`APP_ID`,`PARENT_NAME`,`URL`,`TYPE`,`ICON`,`AUTH_ID`,`BTN_AUTH`,`IS_LEAF`,`SORT_INDEX`,`ALL_ORDER`,`SORT_PATH`,`OPEN_STYLE`,`CLOSE_NOTICE`,`CREATE_USER`,`CREATE_TIME`,`UPDATE_TIME`,`LEAF`,`MENU_AUTH`,`REMARK`,`DATA_VERSION`,`IS_IFRAME`) values ('1275967ea4a4e54acdaff4yhmzq8370', '数字序列', '11316279a4a9416a8e20019821e640af', 'platform', '配置管理', 'codeSequenceManage/views/codeSequence', 1, 'fa fa-sort-numeric-desc', '1275967ea4a4e54acdaff4yhmzq8370', null, null, 16, null, '00030016', null, null, null, null, null, null, null, null, 0, 0);

insert into `SYS_AUTH_ROLE` (`ID`,`AUTH_ID`,`ROLE_ID`,`SIGN`,`IS_REVOKE`,`CREATE_USER`,`CREATE_TIME`,`UPDATE_TIME`,`REMARK`) values ('1275967ea4a4e54acdaff4yhmzq8370', '1275967ea4a4e54acdaff4yhmzq8370', 'appadmin', null, 0, null, null, null, null);

insert into `SYS_AUTH_ROLE_V` (`ID`,`AUTH_ID`,`ROLE_ID`,`SIGN`,`IS_REVOKE`,`CREATE_USER`,`CREATE_TIME`,`UPDATE_TIME`,`REMARK`,`LV_ID`) values ('1275967ea4a4e54acdaff4yhmzq8370', '1275967ea4a4e54acdaff4yhmzq8370', 'appadmin', null, 0, null, null, null, null, 'appadmin');


insert into `SYS_AUTH` (`ID`,`NAME`,`APP_ID`,`REMARK`,`SORT_INDEX`,`PARENT_ID`,`CREATE_USER`,`CREATE_TIME`,`UPDATE_TIME`,`SORT_PATH`,`IS_ORG_ADMIN`,`IS_USER_GROUP_ADMIN`,`CODE`,`DATA_VERSION`) values ('0745867ea4a4e54acdaff4yhmzq8370', '服务编码', 'platform', null,17, '11316279a4a9416a8e20019821e640af', null, null, null, '00030017', 0, 0, 'platForm.codeTemplateManage.codeTemplate', 0);

insert into `SYS_MENU` (`ID`,`NAME`,`PARENT_ID`,`APP_ID`,`PARENT_NAME`,`URL`,`TYPE`,`ICON`,`AUTH_ID`,`BTN_AUTH`,`IS_LEAF`,`SORT_INDEX`,`ALL_ORDER`,`SORT_PATH`,`OPEN_STYLE`,`CLOSE_NOTICE`,`CREATE_USER`,`CREATE_TIME`,`UPDATE_TIME`,`LEAF`,`MENU_AUTH`,`REMARK`,`DATA_VERSION`,`IS_IFRAME`) values ('0745867ea4a4e54acdaff4yhmzq8370', '服务编码', '11316279a4a9416a8e20019821e640af', 'platform', '配置管理', 'codeTemplateManage/views/codeTemplate', 1, 'fa fa-codepen', '0745867ea4a4e54acdaff4yhmzq8370', null, null, 17, null, '00030017', null, null, null, null, null, null, null, null, 0, 0);

insert into `SYS_AUTH_ROLE` (`ID`,`AUTH_ID`,`ROLE_ID`,`SIGN`,`IS_REVOKE`,`CREATE_USER`,`CREATE_TIME`,`UPDATE_TIME`,`REMARK`) values ('0745867ea4a4e54acdaff4yhmzq8370', '0745867ea4a4e54acdaff4yhmzq8370', 'appadmin', null, 0, null, null, null, null);

insert into `SYS_AUTH_ROLE_V` (`ID`,`AUTH_ID`,`ROLE_ID`,`SIGN`,`IS_REVOKE`,`CREATE_USER`,`CREATE_TIME`,`UPDATE_TIME`,`REMARK`,`LV_ID`) values ('0745867ea4a4e54acdaff4yhmzq8370', '0745867ea4a4e54acdaff4yhmzq8370', 'appadmin', null, 0, null, null, null, null, 'appadmin');


CREATE TABLE `CODE_SEQUENCE`(
  `ID` VARCHAR(50) NOT NULL COMMENT '主键',
  `APP_ID` VARCHAR(50) NOT NULL COMMENT '应用id',
  `BIZ_TAG` VARCHAR(100) NOT NULL COMMENT '序列主要标识',
  `PART_VALUE_TAG` VARCHAR(100) DEFAULT 'ACE_NULL_PART_VALUE_TAG' COMMENT '序列次要标识',
  `MAX_NUM` NUMERIC(22) NOT NULL DEFAULT '0' COMMENT '已发放的最大编号',
  `STEP` INT(10) NOT NULL DEFAULT 1 COMMENT '号段长度',
  `REMARK` VARCHAR(2000) COMMENT '数字序列描述',
  `RESET_MODE` INT NOT NULL DEFAULT 0 COMMENT '序列重置模式 “0 不重置”、 “1 按年重置”、“2 按月重置”、“3 按天重置”',
  `LAST_RESET_TIME` DATE COMMENT '上次重置序列时间',
  `DATA_VERSION` INT(10) DEFAULT 0 COMMENT '数据版本',
  `ENABLE_FIX_NUM_LEN` INT DEFAULT 1 COMMENT '是否启用固定数字位数 0否1是',
  `NUM_LENGTH` INT COMMENT '序列固定位数长度',
  PRIMARY KEY (`ID`),
  CONSTRAINT `FK_CODE_SEQUENCE_APP_ID` FOREIGN KEY (`APP_ID`) REFERENCES `sys_group_app`(`ID`) ON DELETE CASCADE
)
COMMENT='数字序列';

CREATE TABLE `CODE_TEMPLATE`(
  `ID` VARCHAR(50) NOT NULL COMMENT '主键',
  `APP_ID` VARCHAR(50) COMMENT '应用id',
  `TEMPLATE_KEY` VARCHAR(100) COMMENT '模板标识',
  `REMARK` VARCHAR(2000) COMMENT '模板描述',
  `DATA_VERSION` INT DEFAULT 0 COMMENT '数据版本',
  PRIMARY KEY (`ID`),
  CONSTRAINT `FK_CODE_TEMPLATE_APP_ID` FOREIGN KEY (`APP_ID`) REFERENCES `sys_group_app`(`ID`) ON DELETE CASCADE
)
COMMENT='编码模板';

CREATE TABLE `CODE_TEMPLATE_PART`(
  `ID` VARCHAR(50) NOT NULL COMMENT '主键',
  `SORT_INDEX` INT(10) NOT NULL DEFAULT 0 COMMENT '部件排序',
  `CODE_TYPE` INT NOT NULL COMMENT '部件编码类型 0静态文本1el表达式2参数值3数字序列',
  `EL_EXPRESS` VARCHAR(100) COMMENT 'el表达式',
  `STATIC_TEXT` VARCHAR(100) COMMENT '静态文本',
  `PARAM_KEY` VARCHAR(100) COMMENT '参数键名',
  `AUTO_CREATE_SEQ_DEF` INT NOT NULL DEFAULT 0 COMMENT '是否自动创建数字序列',
  `SEQ_STEP` INT(10) COMMENT '序列步长',
  `SEQ_RESET_MODE` INT COMMENT '序列重置模式',
  `ENABLE_FIX_NUM_LEN` INT NOT NULL DEFAULT 0 COMMENT '是否启用固定数字位数 0否1是',
  `SEQ_NUM_LENGTH` INT(10) COMMENT '规定数字位数',
  `SEQUENCE_BIZ_TAG` VARCHAR(100) COMMENT '关联数字学列主标识',
  `REMARK` VARCHAR(2000) COMMENT '模板部件描述',
  `TEMPLATE_ID` VARCHAR(50) NOT NULL COMMENT '模板主键',
  `DYN_PART` VARCHAR(500) COMMENT '数字序列部件关联部件',
  PRIMARY KEY (`ID`),
  CONSTRAINT `FK_CODE_TEMPLATE_PART_TMP_ID` FOREIGN KEY (`TEMPLATE_ID`) REFERENCES `code_template`(`ID`) ON DELETE CASCADE
)
COMMENT='模板部件';

/**
 * 大屏展示
 * @param null
 * @return
 * @author zuogang
 * @date 2020/7/17 9:40
 */
insert into `SYS_AUTH` (`ID`,`NAME`,`APP_ID`,`REMARK`,`SORT_INDEX`,`PARENT_ID`,`CREATE_USER`,`CREATE_TIME`,`UPDATE_TIME`,`SORT_PATH`,`IS_ORG_ADMIN`,`IS_USER_GROUP_ADMIN`,`CODE`,`DATA_VERSION`) values ('8234ba3204344d37929abd4ad9a1dgth', '大屏展示', 'platform', null, 8, 'twerba3204344d37929abd4ad9a1dfgh', null, null, null, '00060008', 0, 0, 'screenDisplay.views.ScreenDisplayManage', 0);
insert into `SYS_MENU` (`ID`,`NAME`,`PARENT_ID`,`APP_ID`,`PARENT_NAME`,`URL`,`TYPE`,`ICON`,`AUTH_ID`,`BTN_AUTH`,`IS_LEAF`,`SORT_INDEX`,`ALL_ORDER`,`SORT_PATH`,`OPEN_STYLE`,`CLOSE_NOTICE`,`CREATE_USER`,`CREATE_TIME`,`UPDATE_TIME`,`LEAF`,`MENU_AUTH`,`REMARK`,`DATA_VERSION`,`IS_IFRAME`) values ('8234ba3204344d37929abd4ad9a1dgth', '大屏展示', 'twerba3204344d37929abd4ad9a1dfgh', 'platform', '数据展示', 'screenDisplay/views/ScreenDisplayManage', 1, 'iconfont icon-Report', '8234ba3204344d37929abd4ad9a1dgth', null, null, 8, null, '00060008', null, null, null, null, null, null, null, null, 0, 0);
insert into `SYS_AUTH_ROLE` (`ID`,`AUTH_ID`,`ROLE_ID`,`SIGN`,`IS_REVOKE`,`CREATE_USER`,`CREATE_TIME`,`UPDATE_TIME`,`REMARK`) values ('8234ba3204344d37929abd4ad9a1dgth', '8234ba3204344d37929abd4ad9a1dgth', 'appadmin', null, 0, null, null, null, null);
insert into `SYS_AUTH_ROLE_V` (`ID`,`AUTH_ID`,`ROLE_ID`,`SIGN`,`IS_REVOKE`,`CREATE_USER`,`CREATE_TIME`,`UPDATE_TIME`,`REMARK`,`LV_ID`) values ('8234ba3204344d37929abd4ad9a1dgth', '8234ba3204344d37929abd4ad9a1dgth', 'appadmin', null, 0, null, null, null, null, 'appadmin');

/**
 * 模型管理
 * @param null
 * @return
 * @author zuogang
 * @date 2020/7/17 9:40
 */
insert into `SYS_AUTH` (`ID`,`NAME`,`APP_ID`,`REMARK`,`SORT_INDEX`,`PARENT_ID`,`CREATE_USER`,`CREATE_TIME`,`UPDATE_TIME`,`SORT_PATH`,`IS_ORG_ADMIN`,`IS_USER_GROUP_ADMIN`,`CODE`,`DATA_VERSION`) values ('c789567ea4a4e54acdaff4yhmzq8370', '模型管理', 'platform', null,18, '11316279a4a9416a8e20019821e640af', null, null, null, '00030018', 0, 0, 'platForm.ModelManage.model', 0);

insert into `SYS_MENU` (`ID`,`NAME`,`PARENT_ID`,`APP_ID`,`PARENT_NAME`,`URL`,`TYPE`,`ICON`,`AUTH_ID`,`BTN_AUTH`,`IS_LEAF`,`SORT_INDEX`,`ALL_ORDER`,`SORT_PATH`,`OPEN_STYLE`,`CLOSE_NOTICE`,`CREATE_USER`,`CREATE_TIME`,`UPDATE_TIME`,`LEAF`,`MENU_AUTH`,`REMARK`,`DATA_VERSION`,`IS_IFRAME`) values ('c789567ea4a4e54acdaff4yhmzq8370', '模型管理', '11316279a4a9416a8e20019821e640af', 'platform', '配置管理', 'ModelManage/views/Model', 1, 'fa fa-briefcase', 'c789567ea4a4e54acdaff4yhmzq8370', null, null, 18, null, '00030018', null, null, null, null, null, null, null, null, 0, 0);

insert into `SYS_AUTH_ROLE` (`ID`,`AUTH_ID`,`ROLE_ID`,`SIGN`,`IS_REVOKE`,`CREATE_USER`,`CREATE_TIME`,`UPDATE_TIME`,`REMARK`) values ('c789567ea4a4e54acdaff4yhmzq8370', 'c789567ea4a4e54acdaff4yhmzq8370', 'appadmin', null, 0, null, null, null, null);

insert into `SYS_AUTH_ROLE_V` (`ID`,`AUTH_ID`,`ROLE_ID`,`SIGN`,`IS_REVOKE`,`CREATE_USER`,`CREATE_TIME`,`UPDATE_TIME`,`REMARK`,`LV_ID`) values ('c789567ea4a4e54acdaff4yhmzq8370', 'c789567ea4a4e54acdaff4yhmzq8370', 'appadmin', null, 0, null, null, null, null, 'appadmin');


/**
 * 添加 trace_id
 * @param null
 * @return
 * @author zuogang
 * @date 2020/7/20 16:48
 */
-- 数据源
ALTER TABLE `SYS_GROUP_DATASOURCE`
  ADD COLUMN `TRACE_ID` VARCHAR(50)  COMMENT '跟踪ID';
update `SYS_GROUP_DATASOURCE` set `TRACE_ID`=`ID`;
-- 权限定义
ALTER TABLE `SYS_AUTH`
  ADD COLUMN `TRACE_ID` VARCHAR(50)  COMMENT '跟踪ID';
update `SYS_AUTH` set `TRACE_ID`=`ID`;
-- API
ALTER TABLE `SYS_API_RESOURCE`
  ADD COLUMN `TRACE_ID` VARCHAR(200)  COMMENT '跟踪ID';
update `SYS_API_RESOURCE` set `TRACE_ID`=`ID`;
-- 权限API
ALTER TABLE `SYS_AUTH_API`
  ADD COLUMN `TRACE_ID` VARCHAR(50)  COMMENT '跟踪ID';
update `SYS_AUTH_API` set `TRACE_ID`=`ID`;
-- 菜单定义
ALTER TABLE `SYS_MENU`
  ADD COLUMN `TRACE_ID` VARCHAR(50)  COMMENT '跟踪ID';
update `SYS_MENU` set `TRACE_ID`=`ID`;
-- 角色定义
ALTER TABLE `SYS_ROLE`
  ADD COLUMN `TRACE_ID` VARCHAR(50)  COMMENT '跟踪ID';
update `SYS_ROLE` set `TRACE_ID`=`ID`;
-- 上下级角色
ALTER TABLE `SYS_ROLE_RELATION`
  ADD COLUMN `TRACE_ID` VARCHAR(50)  COMMENT '跟踪ID';
update `SYS_ROLE_RELATION` set `TRACE_ID`=`ID`;
-- 角色互斥
ALTER TABLE `SYS_ROLE_MUTEX`
  ADD COLUMN `TRACE_ID` VARCHAR(50)  COMMENT '跟踪ID';
update `SYS_ROLE_MUTEX` set `TRACE_ID`=`ID`;
-- 角色授权
ALTER TABLE `SYS_AUTH_ROLE`
  ADD COLUMN `TRACE_ID` VARCHAR(50)  COMMENT '跟踪ID';
update `SYS_AUTH_ROLE` set `TRACE_ID`=`ID`;

ALTER TABLE `SYS_AUTH_ROLE_LV`
  ADD COLUMN `TRACE_ID` VARCHAR(50)  COMMENT '跟踪ID';
update `SYS_AUTH_ROLE_LV` set `TRACE_ID`=`ID`;

ALTER TABLE `SYS_AUTH_ROLE_V`
  ADD COLUMN `TRACE_ID` VARCHAR(50)  COMMENT '跟踪ID';
update `SYS_AUTH_ROLE_V` set `TRACE_ID`=`ID`;
-- 附件配置
ALTER TABLE `FILE_CONFIGURATION`
  ADD COLUMN `TRACE_ID` VARCHAR(50)  COMMENT '跟踪ID';
update `FILE_CONFIGURATION` set `TRACE_ID`=`ID`;
-- 定时任务
ALTER TABLE `QRTZ_CONFIG`
  ADD COLUMN `TRACE_ID` VARCHAR(50)  COMMENT '跟踪ID';
update `QRTZ_CONFIG` set `TRACE_ID`=`ID`;
-- 消息通道
ALTER TABLE `SYS_MSG_SEND_TYPE`
  ADD COLUMN `TRACE_ID` VARCHAR(50)  COMMENT '跟踪ID';
update `SYS_MSG_SEND_TYPE` set `TRACE_ID`=`ID`;
-- 消息模板
ALTER TABLE `SYS_MSG_TEMPLATE`
  ADD COLUMN `TRACE_ID` VARCHAR(50)  COMMENT '跟踪ID';
update `SYS_MSG_TEMPLATE` set `TRACE_ID`=`ID`;
-- 信使消息模板
ALTER TABLE `SYS_MSG_TEMPLATE_CONFIG`
  ADD COLUMN `TRACE_ID` VARCHAR(50)  COMMENT '跟踪ID';
update `SYS_MSG_TEMPLATE_CONFIG` set `TRACE_ID`=`ID`;
-- 消息拓展
ALTER TABLE `SYS_MSG_TYPE_EXTEND`
  ADD COLUMN `TRACE_ID` VARCHAR(50)  COMMENT '跟踪ID';
update `SYS_MSG_TYPE_EXTEND` set `TRACE_ID`=`ID`;
-- 数据字典
ALTER TABLE `SYS_DICT`
  ADD COLUMN `TRACE_ID` VARCHAR(50)  COMMENT '跟踪ID';
update `SYS_MSG_TYPE_EXTEND` set `TRACE_ID`=`ID`;
update `SYS_DICT` set `TRACE_ID`=`ID`;
ALTER TABLE `SYS_DICT_VALUE`
  ADD COLUMN `TRACE_ID` VARCHAR(50)  COMMENT '跟踪ID';
update `SYS_DICT_VALUE` set `TRACE_ID`=`ID`;
--数据模型
ALTER TABLE `PRO_SERVICE`
  ADD COLUMN `TRACE_ID` VARCHAR(50)  COMMENT '跟踪ID';
update `PRO_SERVICE` set `TRACE_ID`=`ID`;

ALTER TABLE `PRO_MODEL`
  ADD COLUMN `TRACE_ID` VARCHAR(50)  COMMENT '跟踪ID';
update `PRO_MODEL` set `TRACE_ID`=`ID`;

ALTER TABLE `PRO_MODEL_ASSOCIATION`
  ADD COLUMN `TRACE_ID` VARCHAR(50)  COMMENT '跟踪ID';
update `PRO_MODEL_ASSOCIATION` set `TRACE_ID`=`ID`;

ALTER TABLE `PRO_MODEL_COL`
  ADD COLUMN `TRACE_ID` VARCHAR(50)  COMMENT '跟踪ID';
update `PRO_MODEL_COL` set `TRACE_ID`=`ID`;

ALTER TABLE `PRO_MODEL_INDEX`
  ADD COLUMN `TRACE_ID` VARCHAR(50)  COMMENT '跟踪ID';
update `PRO_MODEL_INDEX` set `TRACE_ID`=`ID`;
-- 组件注册
ALTER TABLE `SYS_COMPONENT_REGISTER`
  ADD COLUMN `TRACE_ID` VARCHAR(50)  COMMENT '跟踪ID';
update `SYS_COMPONENT_REGISTER` set `TRACE_ID`=`ID`;
-- 系统配置
ALTER TABLE `SYS_CONFIG`
  ADD COLUMN `TRACE_ID` VARCHAR(50)  COMMENT '跟踪ID';
update `SYS_CONFIG` set `TRACE_ID`=`ID`;
-- 业务类型
ALTER TABLE `REPORT_TYPE`
  ADD COLUMN `TRACE_ID` VARCHAR(50)  COMMENT '跟踪ID';
update `REPORT_TYPE` set `TRACE_ID`=`ID`;
-- 报表/仪表盘信息
ALTER TABLE `REPORT_INFO`
  ADD COLUMN `TRACE_ID` VARCHAR(50)  COMMENT '跟踪ID';
update `REPORT_INFO` set `TRACE_ID`=`ID`;
-- 接口
ALTER TABLE `SYS_APP_INTERFACE`
  ADD COLUMN `TRACE_ID` VARCHAR(50)  COMMENT '跟踪ID';
update `SYS_APP_INTERFACE` set `TRACE_ID`=`ID`;
ALTER TABLE `SYS_APP_INTERFACE_INPUT`
  ADD COLUMN `TRACE_ID` VARCHAR(50)  COMMENT '跟踪ID';
update `SYS_APP_INTERFACE_INPUT` set `TRACE_ID`=`ID`;
ALTER TABLE `SYS_APP_INTERFACE_OUTPUT`
  ADD COLUMN `TRACE_ID` VARCHAR(50)  COMMENT '跟踪ID';
update `SYS_APP_INTERFACE_OUTPUT` set `TRACE_ID`=`ID`;
-- 大屏
ALTER TABLE `BLADE_VISUAL`
  ADD COLUMN `TRACE_ID` VARCHAR(50)  COMMENT '跟踪ID';
update `BLADE_VISUAL` set `TRACE_ID`=`ID`;
-- 大屏展示
ALTER TABLE `BLADE_VISUAL_SHOW`
  ADD COLUMN `TRACE_ID` VARCHAR(50)  COMMENT '跟踪ID';
update `BLADE_VISUAL_SHOW` set `TRACE_ID`=`ID`;
-- 数字序列
ALTER TABLE `CODE_SEQUENCE`
  ADD COLUMN `TRACE_ID` VARCHAR(50)  COMMENT '跟踪ID';
update `CODE_SEQUENCE` set `TRACE_ID`=`ID`;
-- 编码模板
ALTER TABLE `CODE_TEMPLATE`
  ADD COLUMN `TRACE_ID` VARCHAR(50)  COMMENT '跟踪ID';
update `CODE_TEMPLATE` set `TRACE_ID`=`ID`;
ALTER TABLE `CODE_TEMPLATE_PART`
  ADD COLUMN `TRACE_ID` VARCHAR(50)  COMMENT '跟踪ID';
update `CODE_TEMPLATE_PART` set `TRACE_ID`=`ID`;

/**
 * 模型添加字段
 * @param null
 * @return
 * @author zuogang
 * @date 2020/7/20 17:33
 */
ALTER TABLE `PRO_MODEL` add (`IS_FILE_EXIST` INT(1) DEFAULT 0 COMMENT '表单是否存在附件');
ALTER TABLE `PRO_MODEL` add (`IS_FLOW_EXIST` INT(1) DEFAULT 0 COMMENT '是否是工作流表单');

/**
 *  审计日志菜单显示修复
 */
update sys_menu set type=1 where id='04d7893c4a4d4345b18b0789af13753f';

/**
 * 开发服务信息添加字段
 * @param null
 * @return
 * @author zuogang
 * @date 2020/7/24 17:33
 */
ALTER TABLE `PRO_INFO`
  ADD COLUMN `CREATE_USER` VARCHAR(50)  COMMENT '创建人';

ALTER TABLE `PRO_INFO`
  ADD COLUMN `GROUP_ID` VARCHAR(50)  COMMENT '集团ID';

update `sys_menu` set  `sort_index`=0,`sort_path`='000001000003000000' where `id`='3860126718034cc2a7dbd8311bfb3ff1';
update `sys_auth` set  `sort_index`=0,`sort_path`='000001000003000000' where `id`='3860126718034cc2a7dbd8311bfb3ff1';

update `sys_menu` set `parent_id`='ad2b809bf7084d2db69dd56793cb5e1c',`parent_name`='服务管理',`sort_index`=1,`sort_path`='000001000003000001' where `id`='8f9dec2ca33040e199f9e922f906dc7f';
update `sys_auth` set `parent_id`='ad2b809bf7084d2db69dd56793cb5e1c',`sort_index`=1,`sort_path`='000001000003000001' where `id`='8f9dec2ca33040e199f9e922f906dc7f';

ALTER TABLE `PRO_SERVICE`
  ADD COLUMN `MAINTAIN_STAFFS` VARCHAR(2000)  COMMENT '维护人员ID(多人使用,分割)';

/**
 * 开发平台
 * @param null
 * @return
 * @author zuogang
 * @date 2020/7/28 9:29
 */

 delete from `sys_auth_role` where `id` = '913e1e09cc9f43ac8990a36a493e2071';
 delete from `sys_auth_role_v` where `id` ='6abce43f575c4831b08f3ea30cbf4b2e';

 ALTER TABLE `PRO_INFO` DROP COLUMN `MAINTAIN_STAFFS`;

 ALTER TABLE `PRO_DATASOURCE` add (`IS_MAJOR` INT(1) COMMENT '是否为主数据源 1是0不是');

ALTER TABLE `PRO_SERVICE` DROP COLUMN `DS_ID`;
  ALTER TABLE `PRO_DATASOURCE`
  ADD COLUMN `SERVICE_ID` VARCHAR(50)  COMMENT '服务ID';
  ALTER TABLE `PRO_DATASOURCE`
  ADD CONSTRAINT `FK_DATASOURCE_SERVICE_ID` FOREIGN KEY (`SERVICE_ID`) REFERENCES `PRO_SERVICE`(`ID`) ON DELETE CASCADE;

 ALTER TABLE `PRO_CHANGELOG_HISTORY` add (`IS_USE_LESS` INT(1) COMMENT '替换主数据源后，变为无用数据，0无效，1有效');
 ALTER TABLE `PRO_CHANGELOG_HISTORY`
  ADD COLUMN `DS_ID` VARCHAR(50)  COMMENT '数据源ID';

 ALTER TABLE `PRO_INTERFACE`
  ADD COLUMN `DS_ID` VARCHAR(50)  COMMENT '数据源ID';
  ALTER TABLE `PRO_INTERFACE`
  ADD CONSTRAINT `FK_INTERFACE_DS_ID` FOREIGN KEY (`DS_ID`) REFERENCES `PRO_DATASOURCE`(`ID`) ON DELETE CASCADE;

/**
 * 大屏消息
 * @param null
 * @return
 * @author zuogang
 * @date 2020/7/29 17:54
 */
CREATE TABLE `BLADE_VISUAL_MSG`(
  `ID` VARCHAR(50) NOT NULL COMMENT '主键',
  `NAME` VARCHAR(50) COMMENT '名称',
  `CODE` VARCHAR(50) COMMENT '标识',
  `EVENT_NAME` VARCHAR(50) COMMENT '事件名',
  `DISPLAY_CONTENT` VARCHAR(2000) COMMENT '显示内容',
  `DISPLAY_MODE` INT(1) COMMENT '显示模式（0即时触发，1定时触发）',
  `PERIOD_MODE` VARCHAR(50) COMMENT '周期模式',
  `DISPLAY_DURATION` INT(10) COMMENT '显示时长',
  `VISUAL_IDS` VARCHAR(2000) COMMENT '大屏Ids(以英文逗号分隔)',
  `NOTICE_MODE` INT(1) COMMENT '通知模式(0通知，1警告)',
  `APP_ID` VARCHAR(50) COMMENT '应用ID',
  `TRACE_ID` VARCHAR(50) COMMENT '跟踪ID',
  `PERIOD_DATA` VARCHAR(100) COMMENT '周期数据',
  PRIMARY KEY (`ID`),
  CONSTRAINT `FK_VISUAL_MSG_APP_ID` FOREIGN KEY (`APP_ID`) REFERENCES `sys_group_app`(`ID`) ON DELETE CASCADE
)
COMMENT='大屏消息';


/**
 * 大屏通知菜单添加
 * @param null
 * @return
 * @author zuogang
 * @date 2020/7/31 9:30
 */
insert into `SYS_AUTH` (`ID`,`NAME`,`APP_ID`,`REMARK`,`SORT_INDEX`,`PARENT_ID`,`CREATE_USER`,`CREATE_TIME`,`UPDATE_TIME`,`SORT_PATH`,`IS_ORG_ADMIN`,`IS_USER_GROUP_ADMIN`,`CODE`,`DATA_VERSION`) values ('dapingxiaoxi4d37929abd4ad9a1dgth', '大屏通知', 'platform', null, 9, 'twerba3204344d37929abd4ad9a1dfgh', null, null, null, '00060009', 0, 0, 'screenNotification.views.ScreenNotification', 0);
insert into `SYS_MENU` (`ID`,`NAME`,`PARENT_ID`,`APP_ID`,`PARENT_NAME`,`URL`,`TYPE`,`ICON`,`AUTH_ID`,`BTN_AUTH`,`IS_LEAF`,`SORT_INDEX`,`ALL_ORDER`,`SORT_PATH`,`OPEN_STYLE`,`CLOSE_NOTICE`,`CREATE_USER`,`CREATE_TIME`,`UPDATE_TIME`,`LEAF`,`MENU_AUTH`,`REMARK`,`DATA_VERSION`,`IS_IFRAME`) values ('dapingxiaoxi4d37929abd4ad9a1dgth', '大屏通知', 'twerba3204344d37929abd4ad9a1dfgh', 'platform', '数据展示', 'screenNotification/views/ScreenNotification', 1, 'iconfont icon-message', 'dapingxiaoxi4d37929abd4ad9a1dgth', null, null, 9, null, '00060009', null, null, null, null, null, null, null, null, 0, 0);
insert into `SYS_AUTH_ROLE` (`ID`,`AUTH_ID`,`ROLE_ID`,`SIGN`,`IS_REVOKE`,`CREATE_USER`,`CREATE_TIME`,`UPDATE_TIME`,`REMARK`) values ('dapingxiaoxi4d37929abd4ad9a1dgth', 'dapingxiaoxi4d37929abd4ad9a1dgth', 'appadmin', null, 0, null, null, null, null);
insert into `SYS_AUTH_ROLE_V` (`ID`,`AUTH_ID`,`ROLE_ID`,`SIGN`,`IS_REVOKE`,`CREATE_USER`,`CREATE_TIME`,`UPDATE_TIME`,`REMARK`,`LV_ID`) values ('dapingxiaoxi4d37929abd4ad9a1dgth', 'dapingxiaoxi4d37929abd4ad9a1dgth', 'appadmin', null, 0, null, null, null, null, 'appadmin');


/**
 * 开发平台 接口管理 出参 键 实体
 * @param null
 * @return
 * @author zuogang
 * @date 2020/8/3 8:04
 */
 ALTER TABLE `PRO_OUTPUT_PARAMS`
  ADD COLUMN `PARAM_ITEM` VARCHAR(50)  COMMENT '键 实体';

 ALTER TABLE `SYS_APP_INTERFACE_OUTPUT`
  ADD COLUMN `PARAM_ITEM` VARCHAR(50)  COMMENT '键 实体';
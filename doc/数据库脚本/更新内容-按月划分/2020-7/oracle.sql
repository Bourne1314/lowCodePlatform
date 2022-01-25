/**
 * 地图删除APPID
 * @param null
 * @return
 * @author zuogang
 * @date 2020/7/6 17:59
 */
alter table blade_visual_map DROP CONSTRAINT FK_VISUAL_MAP_APP_ID;
alter table blade_visual_map drop column app_id;
alter table "BD_PERSON_JOB" modify  "PERSON_CODE" NULL;
update sys_menu set url='dataScreen/components/list/map' where name='地图管理';

/**
 * 增加文件回收站
 * @author JonnyJiang
 * @date 2020/7/8 10:59
 */
ALTER TABLE FILE_INFO
 ADD (IS_IN_RECYCLE_BIN  NUMBER(1));
ALTER TABLE FILE_INFO
 ADD (RECYCLE_TIME  DATE);
ALTER TABLE FILE_INFO
 ADD (RECYCLE_FILE_ID  VARCHAR2(50 CHAR));
COMMENT ON COLUMN FILE_INFO.IS_IN_RECYCLE_BIN IS '是否在回收站';
COMMENT ON COLUMN FILE_INFO.RECYCLE_TIME IS '回收时间';
COMMENT ON COLUMN FILE_INFO.RECYCLE_FILE_ID IS '回收文件ID';


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
insert into "SYS_GROUP_APP" ("ID","NAME","VERSION","ICON","SORT_INDEX","REMARK","CREATE_USER","CREATE_TIME","UPDATE_TIME","SECRET_LEVEL","GROUP_ID","IS_3_ADMIN","DATA_VERSION","IS_MAIN_APP","HAS_UI","UI_NAME","IS_LOCK","LOCK_TIME","DATASOURCE_ID") values ('dev-platform', '快速开发平台', '1.0', null, null, null, null, null, null, null, null, 1, 1060, 0, 0, null, 0, null, null);
/**
role
*/
comment on column "SYS_ROLE"."ROLE_TYPE" is '角色类型(1租户系统管理员,2租户安全保密员,3租户安全审计员)(11集团系统管理员,22集团安全保密员,33集团安全审计员)(111应用系统管理员,222应用安全保密员,333应用安全审计员)(1111开发平台超级管理员,2222项目管理人员,3333项目开发人员)';
insert into "SYS_ROLE" ("ID","NAME","ROLE_EXPLAIN","ROLE_TYPE","CREATE_TIME","CREATE_USER","UPDATE_TIME","REMARK","APP_ID","DATA_VERSION") values ('devadmin', '开发平台超级管理员', null,1111, null, null, null, null, 'dev-platform', 0);
insert into "SYS_ROLE" ("ID","NAME","ROLE_EXPLAIN","ROLE_TYPE","CREATE_TIME","CREATE_USER","UPDATE_TIME","REMARK","APP_ID","DATA_VERSION") values ('devmaintainer', '开发平台项目管理人员', null,2222, null, null, null, null, 'dev-platform', 0);
insert into "SYS_ROLE" ("ID","NAME","ROLE_EXPLAIN","ROLE_TYPE","CREATE_TIME","CREATE_USER","UPDATE_TIME","REMARK","APP_ID","DATA_VERSION") values ('devdeveloper', '开发平台项目开发人员', null,3333, null, null, null, null, 'dev-platform', 0);
/**
auth
*/
insert into "SYS_AUTH" ("ID","NAME","APP_ID","REMARK","SORT_INDEX","PARENT_ID","CREATE_USER","CREATE_TIME","UPDATE_TIME","SORT_PATH","IS_ORG_ADMIN","IS_USER_GROUP_ADMIN","CODE","DATA_VERSION") values ('227f8578d72c4cd6b78125ab97c2f17d', '项目管理', 'dev-platform', '', 1, '0', 'e924956d25d94c099430748a777a6b16', null, null, '000001', 0, 0, null, 0);
insert into "SYS_AUTH" ("ID","NAME","APP_ID","REMARK","SORT_INDEX","PARENT_ID","CREATE_USER","CREATE_TIME","UPDATE_TIME","SORT_PATH","IS_ORG_ADMIN","IS_USER_GROUP_ADMIN","CODE","DATA_VERSION") values ('a2c96add3b7744c3939083d5351f3828', '项目信息', 'dev-platform', '', 1, '227f8578d72c4cd6b78125ab97c2f17d', 'e924956d25d94c099430748a777a6b16', null, null, '000001000001', 0, 0, 'devPlatForm.ProInfoManage.ProInfo', 0);
insert into "SYS_AUTH" ("ID","NAME","APP_ID","REMARK","SORT_INDEX","PARENT_ID","CREATE_USER","CREATE_TIME","UPDATE_TIME","SORT_PATH","IS_ORG_ADMIN","IS_USER_GROUP_ADMIN","CODE","DATA_VERSION") values ('8f9dec2ca33040e199f9e922f906dc7f', '数据源管理', 'dev-platform', '', 2, '227f8578d72c4cd6b78125ab97c2f17d', 'e924956d25d94c099430748a777a6b16', null, null, '000001000002', 0, 0, 'devPlatForm.ProDatasourceManage.ProDatasource', 0);
insert into "SYS_AUTH" ("ID","NAME","APP_ID","REMARK","SORT_INDEX","PARENT_ID","CREATE_USER","CREATE_TIME","UPDATE_TIME","SORT_PATH","IS_ORG_ADMIN","IS_USER_GROUP_ADMIN","CODE","DATA_VERSION") values ('ad2b809bf7084d2db69dd56793cb5e1c', '服务管理', 'dev-platform', '', 3, '227f8578d72c4cd6b78125ab97c2f17d', 'e924956d25d94c099430748a777a6b16', null, null, '000001000003', 0, 0, 'devPlatForm.serviceMange', 0);
insert into "SYS_AUTH" ("ID","NAME","APP_ID","REMARK","SORT_INDEX","PARENT_ID","CREATE_USER","CREATE_TIME","UPDATE_TIME","SORT_PATH","IS_ORG_ADMIN","IS_USER_GROUP_ADMIN","CODE","DATA_VERSION") values ('3860126718034cc2a7dbd8311bfb3ff1', '应用信息', 'dev-platform', '', 1, 'ad2b809bf7084d2db69dd56793cb5e1c', 'e924956d25d94c099430748a777a6b16', null, null, '000001000003000001', 0, 0, 'devPlatForm.ProServiceManage.ProService', 0);
insert into "SYS_AUTH" ("ID","NAME","APP_ID","REMARK","SORT_INDEX","PARENT_ID","CREATE_USER","CREATE_TIME","UPDATE_TIME","SORT_PATH","IS_ORG_ADMIN","IS_USER_GROUP_ADMIN","CODE","DATA_VERSION") values ('2a3c459a0c814a098bdc41cb0e4b830e', '模型管理', 'dev-platform', '', 2, 'ad2b809bf7084d2db69dd56793cb5e1c', 'e924956d25d94c099430748a777a6b16', null, null, '000001000003000002', 0, 0, 'devPlatForm.ProModelManage.ProModel', 0);
insert into "SYS_AUTH" ("ID","NAME","APP_ID","REMARK","SORT_INDEX","PARENT_ID","CREATE_USER","CREATE_TIME","UPDATE_TIME","SORT_PATH","IS_ORG_ADMIN","IS_USER_GROUP_ADMIN","CODE","DATA_VERSION") values ('e0c7623e577d4811af97dc6eab9c14d6', '接口管理', 'dev-platform', '', 3, 'ad2b809bf7084d2db69dd56793cb5e1c', 'e924956d25d94c099430748a777a6b16', null, null, '000001000003000003', 0, 0, 'devPlatForm.InterfaceManage.Interface', 0);
insert into "SYS_AUTH" ("ID","NAME","APP_ID","REMARK","SORT_INDEX","PARENT_ID","CREATE_USER","CREATE_TIME","UPDATE_TIME","SORT_PATH","IS_ORG_ADMIN","IS_USER_GROUP_ADMIN","CODE","DATA_VERSION") values ('c6d75746103e4dc99d9d0ce27f0e1af1', '页面设计', 'dev-platform', '', 4, 'ad2b809bf7084d2db69dd56793cb5e1c', 'e924956d25d94c099430748a777a6b16', null, null, '000001000003000004', 0, 0, 'devPlatForm.PageDesignManage.PageDesign', 0);
insert into "SYS_AUTH" ("ID","NAME","APP_ID","REMARK","SORT_INDEX","PARENT_ID","CREATE_USER","CREATE_TIME","UPDATE_TIME","SORT_PATH","IS_ORG_ADMIN","IS_USER_GROUP_ADMIN","CODE","DATA_VERSION") values ('09a55900eb974483b599b4343afb7c52', '系统管理', 'dev-platform', '', 2, '0', 'e924956d25d94c099430748a777a6b16', null, null, '000002', 0, 0, 'devplatform.configMange', 0);
insert into "SYS_AUTH" ("ID","NAME","APP_ID","REMARK","SORT_INDEX","PARENT_ID","CREATE_USER","CREATE_TIME","UPDATE_TIME","SORT_PATH","IS_ORG_ADMIN","IS_USER_GROUP_ADMIN","CODE","DATA_VERSION") values ('e668d7fddbac4aa3a3fdb6502d00ac7d', '用户管理', 'dev-platform', '', 1, '09a55900eb974483b599b4343afb7c52', 'e924956d25d94c099430748a777a6b16', null, null, '000002000001', 0, 0, 'devPlatForm.UserManage.DevUser', 0);
insert into "SYS_AUTH" ("ID","NAME","APP_ID","REMARK","SORT_INDEX","PARENT_ID","CREATE_USER","CREATE_TIME","UPDATE_TIME","SORT_PATH","IS_ORG_ADMIN","IS_USER_GROUP_ADMIN","CODE","DATA_VERSION") values ('74b709f180014414831c66d0ff290a49', '增删改', 'dev-platform', '', 10, 'a2c96add3b7744c3939083d5351f3828', 'e924956d25d94c099430748a777a6b16', null, null, '000001000001000010', 0, 0, 'ProInfo.addOrUpdOrDel', 0);
insert into "SYS_AUTH" ("ID","NAME","APP_ID","REMARK","SORT_INDEX","PARENT_ID","CREATE_USER","CREATE_TIME","UPDATE_TIME","SORT_PATH","IS_ORG_ADMIN","IS_USER_GROUP_ADMIN","CODE","DATA_VERSION") values ('7416563936d84eefafc65325402b380e', '增删改', 'dev-platform', '', 10, '8f9dec2ca33040e199f9e922f906dc7f', 'e924956d25d94c099430748a777a6b16', null, null, '000001000002000010', 0, 0, 'ProDatasource.addOrUpdOrDel', 0);
insert into "SYS_AUTH" ("ID","NAME","APP_ID","REMARK","SORT_INDEX","PARENT_ID","CREATE_USER","CREATE_TIME","UPDATE_TIME","SORT_PATH","IS_ORG_ADMIN","IS_USER_GROUP_ADMIN","CODE","DATA_VERSION") values ('9c844c3686904ccb8ed6c51bd5a7bf5d', '增删改', 'dev-platform', '', 10, '3860126718034cc2a7dbd8311bfb3ff1', 'e924956d25d94c099430748a777a6b16', null, null, '000001000003000001000010', 0, 0, 'service.addOrUpdOrDel', 0);
insert into "SYS_AUTH" ("ID","NAME","APP_ID","REMARK","SORT_INDEX","PARENT_ID","CREATE_USER","CREATE_TIME","UPDATE_TIME","SORT_PATH","IS_ORG_ADMIN","IS_USER_GROUP_ADMIN","CODE","DATA_VERSION") values ('1ea55832fa9c40979e015f8c12c420b3', '增删改', 'dev-platform', '', 10, '2a3c459a0c814a098bdc41cb0e4b830e', 'e924956d25d94c099430748a777a6b16', null, null, '000001000003000002000010', 0, 0, 'proModel.addOrUpdOrDel', 0);
insert into "SYS_AUTH" ("ID","NAME","APP_ID","REMARK","SORT_INDEX","PARENT_ID","CREATE_USER","CREATE_TIME","UPDATE_TIME","SORT_PATH","IS_ORG_ADMIN","IS_USER_GROUP_ADMIN","CODE","DATA_VERSION") values ('5373fd0c912b4c0fa4c46474199f9858', '增删改', 'dev-platform', '', 10, 'e0c7623e577d4811af97dc6eab9c14d6', 'e924956d25d94c099430748a777a6b16', null, null, '000001000003000003000010', 0, 0, 'interface.addOrUpdOrDel', 0);
insert into "SYS_AUTH" ("ID","NAME","APP_ID","REMARK","SORT_INDEX","PARENT_ID","CREATE_USER","CREATE_TIME","UPDATE_TIME","SORT_PATH","IS_ORG_ADMIN","IS_USER_GROUP_ADMIN","CODE","DATA_VERSION") values ('d9ec0e8823c44aab931666f72bee0965', '增删改', 'dev-platform', '', 10, 'c6d75746103e4dc99d9d0ce27f0e1af1', 'e924956d25d94c099430748a777a6b16', null, null, '000001000003000004000010', 0, 0, 'pageDesign.addOrUpdeOrDel', 0);
/**
menu
*/
insert into "SYS_MENU" ("ID","NAME","PARENT_ID","APP_ID","PARENT_NAME","URL","TYPE","ICON","AUTH_ID","BTN_AUTH","IS_LEAF","SORT_INDEX","ALL_ORDER","SORT_PATH","OPEN_STYLE","CLOSE_NOTICE","CREATE_USER","CREATE_TIME","UPDATE_TIME","LEAF","MENU_AUTH","REMARK","DATA_VERSION","IS_IFRAME") values ('227f8578d72c4cd6b78125ab97c2f17d', '项目管理', '0', 'dev-platform', '一级菜单', '', 0, 'fa fa-cubes', '227f8578d72c4cd6b78125ab97c2f17d', null, null, 1, null, '000001', null, null, 'e924956d25d94c099430748a777a6b16', null, null, null, null, '', 0, 0);
insert into "SYS_MENU" ("ID","NAME","PARENT_ID","APP_ID","PARENT_NAME","URL","TYPE","ICON","AUTH_ID","BTN_AUTH","IS_LEAF","SORT_INDEX","ALL_ORDER","SORT_PATH","OPEN_STYLE","CLOSE_NOTICE","CREATE_USER","CREATE_TIME","UPDATE_TIME","LEAF","MENU_AUTH","REMARK","DATA_VERSION","IS_IFRAME") values ('a2c96add3b7744c3939083d5351f3828', '项目信息', '227f8578d72c4cd6b78125ab97c2f17d', 'dev-platform', '项目管理', 'devplatform/ProInfoManage/views/ProInfo', 1, 'fa fa-align-justify', 'a2c96add3b7744c3939083d5351f3828', null, null, 1, null, '000001000001', null, null, 'e924956d25d94c099430748a777a6b16', null, null, null, null, '', 0, 0);
insert into "SYS_MENU" ("ID","NAME","PARENT_ID","APP_ID","PARENT_NAME","URL","TYPE","ICON","AUTH_ID","BTN_AUTH","IS_LEAF","SORT_INDEX","ALL_ORDER","SORT_PATH","OPEN_STYLE","CLOSE_NOTICE","CREATE_USER","CREATE_TIME","UPDATE_TIME","LEAF","MENU_AUTH","REMARK","DATA_VERSION","IS_IFRAME") values ('8f9dec2ca33040e199f9e922f906dc7f', '数据源管理', '227f8578d72c4cd6b78125ab97c2f17d', 'dev-platform', '项目管理', 'devplatform/ProDatasourceManage/views/ProDatasource', 1, 'fa fa-database', '8f9dec2ca33040e199f9e922f906dc7f', null, null, 2, null, '000001000002', null, null, 'e924956d25d94c099430748a777a6b16', null, null, null, null, '', 0, 0);
insert into "SYS_MENU" ("ID","NAME","PARENT_ID","APP_ID","PARENT_NAME","URL","TYPE","ICON","AUTH_ID","BTN_AUTH","IS_LEAF","SORT_INDEX","ALL_ORDER","SORT_PATH","OPEN_STYLE","CLOSE_NOTICE","CREATE_USER","CREATE_TIME","UPDATE_TIME","LEAF","MENU_AUTH","REMARK","DATA_VERSION","IS_IFRAME") values ('ad2b809bf7084d2db69dd56793cb5e1c', '服务管理', '227f8578d72c4cd6b78125ab97c2f17d', 'dev-platform', '项目管理', '', 0, 'fa fa-binoculars', 'ad2b809bf7084d2db69dd56793cb5e1c', null, null, 3, null, '000001000003', null, null, 'e924956d25d94c099430748a777a6b16', null, null, null, null, '', 0, 0);
insert into "SYS_MENU" ("ID","NAME","PARENT_ID","APP_ID","PARENT_NAME","URL","TYPE","ICON","AUTH_ID","BTN_AUTH","IS_LEAF","SORT_INDEX","ALL_ORDER","SORT_PATH","OPEN_STYLE","CLOSE_NOTICE","CREATE_USER","CREATE_TIME","UPDATE_TIME","LEAF","MENU_AUTH","REMARK","DATA_VERSION","IS_IFRAME") values ('3860126718034cc2a7dbd8311bfb3ff1', '应用信息', 'ad2b809bf7084d2db69dd56793cb5e1c', 'dev-platform', '服务管理', 'devplatform/ProServiceManage/views/ProService', 1, 'fa fa-gears', '3860126718034cc2a7dbd8311bfb3ff1', null, null, 1, null, '000001000003000001', null, null, 'e924956d25d94c099430748a777a6b16', null, null, null, null, '', 0, 0);
insert into "SYS_MENU" ("ID","NAME","PARENT_ID","APP_ID","PARENT_NAME","URL","TYPE","ICON","AUTH_ID","BTN_AUTH","IS_LEAF","SORT_INDEX","ALL_ORDER","SORT_PATH","OPEN_STYLE","CLOSE_NOTICE","CREATE_USER","CREATE_TIME","UPDATE_TIME","LEAF","MENU_AUTH","REMARK","DATA_VERSION","IS_IFRAME") values ('2a3c459a0c814a098bdc41cb0e4b830e', '模型管理', 'ad2b809bf7084d2db69dd56793cb5e1c', 'dev-platform', '服务管理', 'devplatform/ProModelManage/views/ProModel', 1, 'fa fa-briefcase', '2a3c459a0c814a098bdc41cb0e4b830e', null, null, 2, null, '000001000003000002', null, null, 'e924956d25d94c099430748a777a6b16', null, null, null, null, '', 0, 0);
insert into "SYS_MENU" ("ID","NAME","PARENT_ID","APP_ID","PARENT_NAME","URL","TYPE","ICON","AUTH_ID","BTN_AUTH","IS_LEAF","SORT_INDEX","ALL_ORDER","SORT_PATH","OPEN_STYLE","CLOSE_NOTICE","CREATE_USER","CREATE_TIME","UPDATE_TIME","LEAF","MENU_AUTH","REMARK","DATA_VERSION","IS_IFRAME") values ('e0c7623e577d4811af97dc6eab9c14d6', '接口管理', 'ad2b809bf7084d2db69dd56793cb5e1c', 'dev-platform', '服务管理', 'devplatform/InterfaceManage/views/Interface', 1, 'fa fa-window-restore', 'e0c7623e577d4811af97dc6eab9c14d6', null, null, 3, null, '000001000003000003', null, null, 'e924956d25d94c099430748a777a6b16', null, null, null, null, '', 0, 0);
insert into "SYS_MENU" ("ID","NAME","PARENT_ID","APP_ID","PARENT_NAME","URL","TYPE","ICON","AUTH_ID","BTN_AUTH","IS_LEAF","SORT_INDEX","ALL_ORDER","SORT_PATH","OPEN_STYLE","CLOSE_NOTICE","CREATE_USER","CREATE_TIME","UPDATE_TIME","LEAF","MENU_AUTH","REMARK","DATA_VERSION","IS_IFRAME") values ('c6d75746103e4dc99d9d0ce27f0e1af1', '页面设计', 'ad2b809bf7084d2db69dd56793cb5e1c', 'dev-platform', '服务管理', 'devplatform/PageDesignManage/views/PageDesign', 1, 'fa fa-window-maximize', 'c6d75746103e4dc99d9d0ce27f0e1af1', null, null, 4, null, '000001000003000004', null, null, 'e924956d25d94c099430748a777a6b16', null, null, null, null, '', 0, 0);
insert into "SYS_MENU" ("ID","NAME","PARENT_ID","APP_ID","PARENT_NAME","URL","TYPE","ICON","AUTH_ID","BTN_AUTH","IS_LEAF","SORT_INDEX","ALL_ORDER","SORT_PATH","OPEN_STYLE","CLOSE_NOTICE","CREATE_USER","CREATE_TIME","UPDATE_TIME","LEAF","MENU_AUTH","REMARK","DATA_VERSION","IS_IFRAME") values ('09a55900eb974483b599b4343afb7c52', '系统管理', '0', 'dev-platform', '一级菜单', '', 0, 'fa fa-archive', '09a55900eb974483b599b4343afb7c52', null, null, 2, null, '000002', null, null, 'e924956d25d94c099430748a777a6b16', null, null, null, null, '', 0, 0);
insert into "SYS_MENU" ("ID","NAME","PARENT_ID","APP_ID","PARENT_NAME","URL","TYPE","ICON","AUTH_ID","BTN_AUTH","IS_LEAF","SORT_INDEX","ALL_ORDER","SORT_PATH","OPEN_STYLE","CLOSE_NOTICE","CREATE_USER","CREATE_TIME","UPDATE_TIME","LEAF","MENU_AUTH","REMARK","DATA_VERSION","IS_IFRAME") values ('e668d7fddbac4aa3a3fdb6502d00ac7d', '用户管理', '09a55900eb974483b599b4343afb7c52', 'dev-platform', '系统管理', 'devplatform/UserManage/views/DevUser', 1, 'fa fa-user', 'e668d7fddbac4aa3a3fdb6502d00ac7d', null, null, 1, null, '000002000001', null, null, 'e924956d25d94c099430748a777a6b16', null, null, null, null, '', 0, 0);
/**
authRole
*/
insert into "SYS_AUTH_ROLE" ("ID","AUTH_ID","ROLE_ID","SIGN","IS_REVOKE","CREATE_USER","CREATE_TIME","UPDATE_TIME","REMARK","AUTH_NAME","ROLE_NAME") values ('04cf67c89b9b4fcca2387096ea809ace', '227f8578d72c4cd6b78125ab97c2f17d', 'devadmin', null, 0, '1d25e7e5ce164c95bd283f7d6c9b644a', null, null, null, '项目管理', 'devadmin');
insert into "SYS_AUTH_ROLE" ("ID","AUTH_ID","ROLE_ID","SIGN","IS_REVOKE","CREATE_USER","CREATE_TIME","UPDATE_TIME","REMARK","AUTH_NAME","ROLE_NAME") values ('de0be48b7de54f438270d57d2189c7b4', 'a2c96add3b7744c3939083d5351f3828', 'devadmin', null, 0, '1d25e7e5ce164c95bd283f7d6c9b644a', null, null, null, '项目信息', 'devadmin');
insert into "SYS_AUTH_ROLE" ("ID","AUTH_ID","ROLE_ID","SIGN","IS_REVOKE","CREATE_USER","CREATE_TIME","UPDATE_TIME","REMARK","AUTH_NAME","ROLE_NAME") values ('9f4676acbf954ba3a6954c2cb636d3ce', '8f9dec2ca33040e199f9e922f906dc7f', 'devadmin', null, 0, '1d25e7e5ce164c95bd283f7d6c9b644a', null, null, null, '数据源管理', 'devadmin');
insert into "SYS_AUTH_ROLE" ("ID","AUTH_ID","ROLE_ID","SIGN","IS_REVOKE","CREATE_USER","CREATE_TIME","UPDATE_TIME","REMARK","AUTH_NAME","ROLE_NAME") values ('e6199f9a57e743f1a9a9bcc62e182152', 'ad2b809bf7084d2db69dd56793cb5e1c', 'devadmin', null, 0, '1d25e7e5ce164c95bd283f7d6c9b644a', null, null, null, '服务管理', 'devadmin');
insert into "SYS_AUTH_ROLE" ("ID","AUTH_ID","ROLE_ID","SIGN","IS_REVOKE","CREATE_USER","CREATE_TIME","UPDATE_TIME","REMARK","AUTH_NAME","ROLE_NAME") values ('51c7be6e3dd44e94aff6323b0b5c51a6', '3860126718034cc2a7dbd8311bfb3ff1', 'devadmin', null, 0, '1d25e7e5ce164c95bd283f7d6c9b644a', null, null, null, '应用信息', 'devadmin');
insert into "SYS_AUTH_ROLE" ("ID","AUTH_ID","ROLE_ID","SIGN","IS_REVOKE","CREATE_USER","CREATE_TIME","UPDATE_TIME","REMARK","AUTH_NAME","ROLE_NAME") values ('2dfe00218f0241aeb08910db93df297b', '2a3c459a0c814a098bdc41cb0e4b830e', 'devadmin', null, 0, '1d25e7e5ce164c95bd283f7d6c9b644a', null, null, null, '模型管理', 'devadmin');
insert into "SYS_AUTH_ROLE" ("ID","AUTH_ID","ROLE_ID","SIGN","IS_REVOKE","CREATE_USER","CREATE_TIME","UPDATE_TIME","REMARK","AUTH_NAME","ROLE_NAME") values ('08b8f310fab643a5ba5eb1b6eba750fa', 'e0c7623e577d4811af97dc6eab9c14d6', 'devadmin', null, 0, '1d25e7e5ce164c95bd283f7d6c9b644a', null, null, null, '接口管理', 'devadmin');
insert into "SYS_AUTH_ROLE" ("ID","AUTH_ID","ROLE_ID","SIGN","IS_REVOKE","CREATE_USER","CREATE_TIME","UPDATE_TIME","REMARK","AUTH_NAME","ROLE_NAME") values ('e8bcac4e780e4519a3366cf17315f8f0', 'c6d75746103e4dc99d9d0ce27f0e1af1', 'devadmin', null, 0, '1d25e7e5ce164c95bd283f7d6c9b644a', null, null, null, '页面设计', 'devadmin');
insert into "SYS_AUTH_ROLE" ("ID","AUTH_ID","ROLE_ID","SIGN","IS_REVOKE","CREATE_USER","CREATE_TIME","UPDATE_TIME","REMARK","AUTH_NAME","ROLE_NAME") values ('da10a934b1594f0186632e4648a6be11', '09a55900eb974483b599b4343afb7c52', 'devadmin', null, 0, '1d25e7e5ce164c95bd283f7d6c9b644a', null, null, null, '系统管理', 'devadmin');
insert into "SYS_AUTH_ROLE" ("ID","AUTH_ID","ROLE_ID","SIGN","IS_REVOKE","CREATE_USER","CREATE_TIME","UPDATE_TIME","REMARK","AUTH_NAME","ROLE_NAME") values ('0a20ddf3dbc34d54a3dda4f29bfe76e4', 'e668d7fddbac4aa3a3fdb6502d00ac7d', 'devadmin', null, 0, '1d25e7e5ce164c95bd283f7d6c9b644a', null, null, null, '用户管理', 'devadmin');
insert into "SYS_AUTH_ROLE" ("ID","AUTH_ID","ROLE_ID","SIGN","IS_REVOKE","CREATE_USER","CREATE_TIME","UPDATE_TIME","REMARK","AUTH_NAME","ROLE_NAME") values ('63fcfef7a9474548bb0646c9b4d00a57', '8f9dec2ca33040e199f9e922f906dc7f', 'devdeveloper', null, 0, '1d25e7e5ce164c95bd283f7d6c9b644a', null, null, null, '数据源管理', 'devdeveloper');
insert into "SYS_AUTH_ROLE" ("ID","AUTH_ID","ROLE_ID","SIGN","IS_REVOKE","CREATE_USER","CREATE_TIME","UPDATE_TIME","REMARK","AUTH_NAME","ROLE_NAME") values ('9bcf56b499a6434598c12ac59b0a7a7d', '7416563936d84eefafc65325402b380e', 'devdeveloper', null, 0, '1d25e7e5ce164c95bd283f7d6c9b644a', null, null, null, '增删改', 'devdeveloper');
insert into "SYS_AUTH_ROLE" ("ID","AUTH_ID","ROLE_ID","SIGN","IS_REVOKE","CREATE_USER","CREATE_TIME","UPDATE_TIME","REMARK","AUTH_NAME","ROLE_NAME") values ('f7255dc72c594baa8f57a201cfa3e4e6', 'ad2b809bf7084d2db69dd56793cb5e1c', 'devdeveloper', null, 0, '1d25e7e5ce164c95bd283f7d6c9b644a', null, null, null, '服务管理', 'devdeveloper');
insert into "SYS_AUTH_ROLE" ("ID","AUTH_ID","ROLE_ID","SIGN","IS_REVOKE","CREATE_USER","CREATE_TIME","UPDATE_TIME","REMARK","AUTH_NAME","ROLE_NAME") values ('56336e15844a4e3291380467f75923c4', '3860126718034cc2a7dbd8311bfb3ff1', 'devdeveloper', null, 0, '1d25e7e5ce164c95bd283f7d6c9b644a', null, null, null, '应用信息', 'devdeveloper');
insert into "SYS_AUTH_ROLE" ("ID","AUTH_ID","ROLE_ID","SIGN","IS_REVOKE","CREATE_USER","CREATE_TIME","UPDATE_TIME","REMARK","AUTH_NAME","ROLE_NAME") values ('913e1e09cc9f43ac8990a36a493e2071', '9c844c3686904ccb8ed6c51bd5a7bf5d', 'devdeveloper', null, 0, '1d25e7e5ce164c95bd283f7d6c9b644a', null, null, null, '增删改', 'devdeveloper');
insert into "SYS_AUTH_ROLE" ("ID","AUTH_ID","ROLE_ID","SIGN","IS_REVOKE","CREATE_USER","CREATE_TIME","UPDATE_TIME","REMARK","AUTH_NAME","ROLE_NAME") values ('2177ca2503ef42f4a545212619e82e09', '2a3c459a0c814a098bdc41cb0e4b830e', 'devdeveloper', null, 0, '1d25e7e5ce164c95bd283f7d6c9b644a', null, null, null, '模型管理', 'devdeveloper');
insert into "SYS_AUTH_ROLE" ("ID","AUTH_ID","ROLE_ID","SIGN","IS_REVOKE","CREATE_USER","CREATE_TIME","UPDATE_TIME","REMARK","AUTH_NAME","ROLE_NAME") values ('bba9df1c426d4ca5843d050a692438ed', '1ea55832fa9c40979e015f8c12c420b3', 'devdeveloper', null, 0, '1d25e7e5ce164c95bd283f7d6c9b644a', null, null, null, '增删改', 'devdeveloper');
insert into "SYS_AUTH_ROLE" ("ID","AUTH_ID","ROLE_ID","SIGN","IS_REVOKE","CREATE_USER","CREATE_TIME","UPDATE_TIME","REMARK","AUTH_NAME","ROLE_NAME") values ('32f524b1016e4720a81e2ebb9d010385', 'e0c7623e577d4811af97dc6eab9c14d6', 'devdeveloper', null, 0, '1d25e7e5ce164c95bd283f7d6c9b644a', null, null, null, '接口管理', 'devdeveloper');
insert into "SYS_AUTH_ROLE" ("ID","AUTH_ID","ROLE_ID","SIGN","IS_REVOKE","CREATE_USER","CREATE_TIME","UPDATE_TIME","REMARK","AUTH_NAME","ROLE_NAME") values ('39449e153dad40dead0ec02bab2a65b1', '5373fd0c912b4c0fa4c46474199f9858', 'devdeveloper', null, 0, '1d25e7e5ce164c95bd283f7d6c9b644a', null, null, null, '增删改', 'devdeveloper');
insert into "SYS_AUTH_ROLE" ("ID","AUTH_ID","ROLE_ID","SIGN","IS_REVOKE","CREATE_USER","CREATE_TIME","UPDATE_TIME","REMARK","AUTH_NAME","ROLE_NAME") values ('4772d682ebc547c8ac7706b799cd7493', 'c6d75746103e4dc99d9d0ce27f0e1af1', 'devdeveloper', null, 0, '1d25e7e5ce164c95bd283f7d6c9b644a', null, null, null, '页面设计', 'devdeveloper');
insert into "SYS_AUTH_ROLE" ("ID","AUTH_ID","ROLE_ID","SIGN","IS_REVOKE","CREATE_USER","CREATE_TIME","UPDATE_TIME","REMARK","AUTH_NAME","ROLE_NAME") values ('2e9fba4f85f34f019e9e079758dc17a1', 'd9ec0e8823c44aab931666f72bee0965', 'devdeveloper', null, 0, '1d25e7e5ce164c95bd283f7d6c9b644a', null, null, null, '增删改', 'devdeveloper');
insert into "SYS_AUTH_ROLE" ("ID","AUTH_ID","ROLE_ID","SIGN","IS_REVOKE","CREATE_USER","CREATE_TIME","UPDATE_TIME","REMARK","AUTH_NAME","ROLE_NAME") values ('201787a581b44b9d955f6c09ed5d3e41', '227f8578d72c4cd6b78125ab97c2f17d', 'devdeveloper', null, 0, '1d25e7e5ce164c95bd283f7d6c9b644a', null, null, null, '项目管理', 'devdeveloper');
insert into "SYS_AUTH_ROLE" ("ID","AUTH_ID","ROLE_ID","SIGN","IS_REVOKE","CREATE_USER","CREATE_TIME","UPDATE_TIME","REMARK","AUTH_NAME","ROLE_NAME") values ('237153192b1b4a11b850bad035bda80e', 'a2c96add3b7744c3939083d5351f3828', 'devmaintainer', null, 0, '1d25e7e5ce164c95bd283f7d6c9b644a', null, null, null, '项目信息', 'devmaintainer');
insert into "SYS_AUTH_ROLE" ("ID","AUTH_ID","ROLE_ID","SIGN","IS_REVOKE","CREATE_USER","CREATE_TIME","UPDATE_TIME","REMARK","AUTH_NAME","ROLE_NAME") values ('8856daf09bce48a2924ad53d923ecef2', '74b709f180014414831c66d0ff290a49', 'devmaintainer', null, 0, '1d25e7e5ce164c95bd283f7d6c9b644a', null, null, null, '增删改', 'devmaintainer');
insert into "SYS_AUTH_ROLE" ("ID","AUTH_ID","ROLE_ID","SIGN","IS_REVOKE","CREATE_USER","CREATE_TIME","UPDATE_TIME","REMARK","AUTH_NAME","ROLE_NAME") values ('a58946f890d24cab8bf26e4ed1ee8a2f', '8f9dec2ca33040e199f9e922f906dc7f', 'devmaintainer', null, 0, '1d25e7e5ce164c95bd283f7d6c9b644a', null, null, null, '数据源管理', 'devmaintainer');
insert into "SYS_AUTH_ROLE" ("ID","AUTH_ID","ROLE_ID","SIGN","IS_REVOKE","CREATE_USER","CREATE_TIME","UPDATE_TIME","REMARK","AUTH_NAME","ROLE_NAME") values ('c75d7aa985ea496ba2a8784adfe40f09', 'ad2b809bf7084d2db69dd56793cb5e1c', 'devmaintainer', null, 0, '1d25e7e5ce164c95bd283f7d6c9b644a', null, null, null, '服务管理', 'devmaintainer');
insert into "SYS_AUTH_ROLE" ("ID","AUTH_ID","ROLE_ID","SIGN","IS_REVOKE","CREATE_USER","CREATE_TIME","UPDATE_TIME","REMARK","AUTH_NAME","ROLE_NAME") values ('b172fdac4cce47c89516a337d523e938', '3860126718034cc2a7dbd8311bfb3ff1', 'devmaintainer', null, 0, '1d25e7e5ce164c95bd283f7d6c9b644a', null, null, null, '应用信息', 'devmaintainer');
insert into "SYS_AUTH_ROLE" ("ID","AUTH_ID","ROLE_ID","SIGN","IS_REVOKE","CREATE_USER","CREATE_TIME","UPDATE_TIME","REMARK","AUTH_NAME","ROLE_NAME") values ('cbcfae2bec734c3ea6c6fc78c7552c0c', '2a3c459a0c814a098bdc41cb0e4b830e', 'devmaintainer', null, 0, '1d25e7e5ce164c95bd283f7d6c9b644a', null, null, null, '模型管理', 'devmaintainer');
insert into "SYS_AUTH_ROLE" ("ID","AUTH_ID","ROLE_ID","SIGN","IS_REVOKE","CREATE_USER","CREATE_TIME","UPDATE_TIME","REMARK","AUTH_NAME","ROLE_NAME") values ('cdbd5a12a6d7475698a69edb108294f1', 'e0c7623e577d4811af97dc6eab9c14d6', 'devmaintainer', null, 0, '1d25e7e5ce164c95bd283f7d6c9b644a', null, null, null, '接口管理', 'devmaintainer');
insert into "SYS_AUTH_ROLE" ("ID","AUTH_ID","ROLE_ID","SIGN","IS_REVOKE","CREATE_USER","CREATE_TIME","UPDATE_TIME","REMARK","AUTH_NAME","ROLE_NAME") values ('3dc152ab58124b81bd68cbd249944644', 'c6d75746103e4dc99d9d0ce27f0e1af1', 'devmaintainer', null, 0, '1d25e7e5ce164c95bd283f7d6c9b644a', null, null, null, '页面设计', 'devmaintainer');
insert into "SYS_AUTH_ROLE" ("ID","AUTH_ID","ROLE_ID","SIGN","IS_REVOKE","CREATE_USER","CREATE_TIME","UPDATE_TIME","REMARK","AUTH_NAME","ROLE_NAME") values ('07d034a530944d2c92e2a01db9ebaa42', '227f8578d72c4cd6b78125ab97c2f17d', 'devmaintainer', null, 0, '1d25e7e5ce164c95bd283f7d6c9b644a', null, null, null, '项目管理', 'devmaintainer');
insert into "SYS_AUTH_ROLE" ("ID","AUTH_ID","ROLE_ID","SIGN","IS_REVOKE","CREATE_USER","CREATE_TIME","UPDATE_TIME","REMARK","AUTH_NAME","ROLE_NAME") values ('shuju3192b1b4a11b850bad035bda80e', '7416563936d84eefafc65325402b380e', 'devmaintainer', null, 0, '1d25e7e5ce164c95bd283f7d6c9b644a', null, null, null, '增删改', 'devmaintainer');
insert into "SYS_AUTH_ROLE" ("ID","AUTH_ID","ROLE_ID","SIGN","IS_REVOKE","CREATE_USER","CREATE_TIME","UPDATE_TIME","REMARK","AUTH_NAME","ROLE_NAME") values ('fuwu1e09cc9f43ac8990a36a493e2071', '9c844c3686904ccb8ed6c51bd5a7bf5d', 'devmaintainer', null, 0, '1d25e7e5ce164c95bd283f7d6c9b644a', null, null, null, '增删改', 'devmaintainer');
insert into "SYS_AUTH_ROLE" ("ID","AUTH_ID","ROLE_ID","SIGN","IS_REVOKE","CREATE_USER","CREATE_TIME","UPDATE_TIME","REMARK","AUTH_NAME","ROLE_NAME") values ('moxing1c426d4ca5843d050a692438ed', '1ea55832fa9c40979e015f8c12c420b3', 'devmaintainer', null, 0, '1d25e7e5ce164c95bd283f7d6c9b644a', null, null, null, '增删改', 'devmaintainer');
insert into "SYS_AUTH_ROLE" ("ID","AUTH_ID","ROLE_ID","SIGN","IS_REVOKE","CREATE_USER","CREATE_TIME","UPDATE_TIME","REMARK","AUTH_NAME","ROLE_NAME") values ('jiekoue53dad40dead0ec02bab2a65b1', '5373fd0c912b4c0fa4c46474199f9858', 'devmaintainer', null, 0, '1d25e7e5ce164c95bd283f7d6c9b644a', null, null, null, '增删改', 'devmaintainer');
insert into "SYS_AUTH_ROLE" ("ID","AUTH_ID","ROLE_ID","SIGN","IS_REVOKE","CREATE_USER","CREATE_TIME","UPDATE_TIME","REMARK","AUTH_NAME","ROLE_NAME") values ('yemian4f85f34f019e9e079758dc17a1', 'd9ec0e8823c44aab931666f72bee0965', 'devmaintainer', null, 0, '1d25e7e5ce164c95bd283f7d6c9b644a', null, null, null, '增删改', 'devmaintainer');

/**
authRole_LV
*/
insert into "SYS_AUTH_ROLE_LV" ("ID","VERSION_NO","IS_LAST_VERSION","VERSION_BEGIN_TIME","VERSION_END_TIME","VERSION_BEGIN_USER_ID","VERSION_END_USER_ID","ROLE_ID","APP_ID") values ('a7290384e3f84287bedb968f1c38ad21', 0, 1, to_date('2020-02-14 20:47:00','yyyy-mm-dd hh24:mi:ss'), null, 'e924956d25d94c099430748a777a6b16', null, 'devadmin', 'dev-platform');
insert into "SYS_AUTH_ROLE_LV" ("ID","VERSION_NO","IS_LAST_VERSION","VERSION_BEGIN_TIME","VERSION_END_TIME","VERSION_BEGIN_USER_ID","VERSION_END_USER_ID","ROLE_ID","APP_ID") values ('ec0818a993014e8e9f036a0dfce2905d', 0, 1, to_date('2020-02-14 20:47:00','yyyy-mm-dd hh24:mi:ss'), null, 'e924956d25d94c099430748a777a6b16', null, 'devdeveloper', 'dev-platform');
insert into "SYS_AUTH_ROLE_LV" ("ID","VERSION_NO","IS_LAST_VERSION","VERSION_BEGIN_TIME","VERSION_END_TIME","VERSION_BEGIN_USER_ID","VERSION_END_USER_ID","ROLE_ID","APP_ID") values ('ee1fe11928804140839926be7ac23c23', 0, 1, to_date('2020-02-14 20:47:00','yyyy-mm-dd hh24:mi:ss'), null, 'e924956d25d94c099430748a777a6b16', null, 'devmaintainer', 'dev-platform');

/**
authRole_V
*/
insert into "SYS_AUTH_ROLE_V" ("ID","AUTH_ID","ROLE_ID","SIGN","IS_REVOKE","CREATE_USER","CREATE_TIME","UPDATE_TIME","REMARK","LV_ID","AUTH_NAME") values ('shuju3192b1b4a11b850bad035bda80e', '7416563936d84eefafc65325402b380e', 'devmaintainer', null, 0, '1d25e7e5ce164c95bd283f7d6c9b644a', null, null, null, 'ee1fe11928804140839926be7ac23c23', '增删改');
insert into "SYS_AUTH_ROLE_V" ("ID","AUTH_ID","ROLE_ID","SIGN","IS_REVOKE","CREATE_USER","CREATE_TIME","UPDATE_TIME","REMARK","LV_ID","AUTH_NAME") values ('fuwu1e09cc9f43ac8990a36a493e2071', '9c844c3686904ccb8ed6c51bd5a7bf5d', 'devmaintainer', null, 0, '1d25e7e5ce164c95bd283f7d6c9b644a', null, null, null, 'ee1fe11928804140839926be7ac23c23', '增删改');
insert into "SYS_AUTH_ROLE_V" ("ID","AUTH_ID","ROLE_ID","SIGN","IS_REVOKE","CREATE_USER","CREATE_TIME","UPDATE_TIME","REMARK","LV_ID","AUTH_NAME") values ('moxing1c426d4ca5843d050a692438ed', '1ea55832fa9c40979e015f8c12c420b3', 'devmaintainer', null, 0, '1d25e7e5ce164c95bd283f7d6c9b644a', null, null, null, 'ee1fe11928804140839926be7ac23c23', '增删改');
insert into "SYS_AUTH_ROLE_V" ("ID","AUTH_ID","ROLE_ID","SIGN","IS_REVOKE","CREATE_USER","CREATE_TIME","UPDATE_TIME","REMARK","LV_ID","AUTH_NAME") values ('jiekoue53dad40dead0ec02bab2a65b1', '5373fd0c912b4c0fa4c46474199f9858', 'devmaintainer', null, 0, '1d25e7e5ce164c95bd283f7d6c9b644a', null, null, null, 'ee1fe11928804140839926be7ac23c23', '增删改');
insert into "SYS_AUTH_ROLE_V" ("ID","AUTH_ID","ROLE_ID","SIGN","IS_REVOKE","CREATE_USER","CREATE_TIME","UPDATE_TIME","REMARK","LV_ID","AUTH_NAME") values ('yemian4f85f34f019e9e079758dc17a1', 'd9ec0e8823c44aab931666f72bee0965', 'devmaintainer', null, 0, '1d25e7e5ce164c95bd283f7d6c9b644a', null, null, null, 'ee1fe11928804140839926be7ac23c23', '增删改');

insert into "SYS_AUTH_ROLE_V" ("ID","AUTH_ID","ROLE_ID","SIGN","IS_REVOKE","CREATE_USER","CREATE_TIME","UPDATE_TIME","REMARK","LV_ID","AUTH_NAME") values ('bb7f28b20a584c35925e5ef416863d5c', 'a2c96add3b7744c3939083d5351f3828', 'devmaintainer', null, 0, '1d25e7e5ce164c95bd283f7d6c9b644a', null, null, null, 'ee1fe11928804140839926be7ac23c23', '项目信息');
insert into "SYS_AUTH_ROLE_V" ("ID","AUTH_ID","ROLE_ID","SIGN","IS_REVOKE","CREATE_USER","CREATE_TIME","UPDATE_TIME","REMARK","LV_ID","AUTH_NAME") values ('810e9c32112d47c78f66133f584241dd', '74b709f180014414831c66d0ff290a49', 'devmaintainer', null, 0, '1d25e7e5ce164c95bd283f7d6c9b644a', null, null, null, 'ee1fe11928804140839926be7ac23c23', '增删改');
insert into "SYS_AUTH_ROLE_V" ("ID","AUTH_ID","ROLE_ID","SIGN","IS_REVOKE","CREATE_USER","CREATE_TIME","UPDATE_TIME","REMARK","LV_ID","AUTH_NAME") values ('ac36d037ec944375b4d09c9afd395b3a', '8f9dec2ca33040e199f9e922f906dc7f', 'devmaintainer', null, 0, '1d25e7e5ce164c95bd283f7d6c9b644a', null, null, null, 'ee1fe11928804140839926be7ac23c23', '数据源管理');
insert into "SYS_AUTH_ROLE_V" ("ID","AUTH_ID","ROLE_ID","SIGN","IS_REVOKE","CREATE_USER","CREATE_TIME","UPDATE_TIME","REMARK","LV_ID","AUTH_NAME") values ('02140d71ecbc4cbc9912e778822f1e8a', 'ad2b809bf7084d2db69dd56793cb5e1c', 'devmaintainer', null, 0, '1d25e7e5ce164c95bd283f7d6c9b644a', null, null, null, 'ee1fe11928804140839926be7ac23c23', '服务管理');
insert into "SYS_AUTH_ROLE_V" ("ID","AUTH_ID","ROLE_ID","SIGN","IS_REVOKE","CREATE_USER","CREATE_TIME","UPDATE_TIME","REMARK","LV_ID","AUTH_NAME") values ('06d9cfe93bae4144a2dbc37b6b97f44b', '3860126718034cc2a7dbd8311bfb3ff1', 'devmaintainer', null, 0, '1d25e7e5ce164c95bd283f7d6c9b644a', null, null, null, 'ee1fe11928804140839926be7ac23c23', '应用信息');
insert into "SYS_AUTH_ROLE_V" ("ID","AUTH_ID","ROLE_ID","SIGN","IS_REVOKE","CREATE_USER","CREATE_TIME","UPDATE_TIME","REMARK","LV_ID","AUTH_NAME") values ('35b506fa12974bc58f69070d72d1e07f', '2a3c459a0c814a098bdc41cb0e4b830e', 'devmaintainer', null, 0, '1d25e7e5ce164c95bd283f7d6c9b644a', null, null, null, 'ee1fe11928804140839926be7ac23c23', '模型管理');
insert into "SYS_AUTH_ROLE_V" ("ID","AUTH_ID","ROLE_ID","SIGN","IS_REVOKE","CREATE_USER","CREATE_TIME","UPDATE_TIME","REMARK","LV_ID","AUTH_NAME") values ('057698259eb4475c9de5877709380f85', 'e0c7623e577d4811af97dc6eab9c14d6', 'devmaintainer', null, 0, '1d25e7e5ce164c95bd283f7d6c9b644a', null, null, null, 'ee1fe11928804140839926be7ac23c23', '接口管理');
insert into "SYS_AUTH_ROLE_V" ("ID","AUTH_ID","ROLE_ID","SIGN","IS_REVOKE","CREATE_USER","CREATE_TIME","UPDATE_TIME","REMARK","LV_ID","AUTH_NAME") values ('b35270a92b3f466c92ee8134e64602d3', 'c6d75746103e4dc99d9d0ce27f0e1af1', 'devmaintainer', null, 0, '1d25e7e5ce164c95bd283f7d6c9b644a', null, null, null, 'ee1fe11928804140839926be7ac23c23', '页面设计');
insert into "SYS_AUTH_ROLE_V" ("ID","AUTH_ID","ROLE_ID","SIGN","IS_REVOKE","CREATE_USER","CREATE_TIME","UPDATE_TIME","REMARK","LV_ID","AUTH_NAME") values ('e1a3b9e7247c4243a8f6942d6c2abfff', '227f8578d72c4cd6b78125ab97c2f17d', 'devmaintainer', null, 0, '1d25e7e5ce164c95bd283f7d6c9b644a', null, null, null, 'ee1fe11928804140839926be7ac23c23', '项目管理');
insert into "SYS_AUTH_ROLE_V" ("ID","AUTH_ID","ROLE_ID","SIGN","IS_REVOKE","CREATE_USER","CREATE_TIME","UPDATE_TIME","REMARK","LV_ID","AUTH_NAME") values ('c8f237f26efa4f3f974c0f880ca3f5fc', '227f8578d72c4cd6b78125ab97c2f17d', 'devadmin', null, 0, '1d25e7e5ce164c95bd283f7d6c9b644a', null, null, null, 'a7290384e3f84287bedb968f1c38ad21', '项目管理');
insert into "SYS_AUTH_ROLE_V" ("ID","AUTH_ID","ROLE_ID","SIGN","IS_REVOKE","CREATE_USER","CREATE_TIME","UPDATE_TIME","REMARK","LV_ID","AUTH_NAME") values ('0a09978928a446d983def544237198f2', 'a2c96add3b7744c3939083d5351f3828', 'devadmin', null, 0, '1d25e7e5ce164c95bd283f7d6c9b644a', null, null, null, 'a7290384e3f84287bedb968f1c38ad21', '项目信息');
insert into "SYS_AUTH_ROLE_V" ("ID","AUTH_ID","ROLE_ID","SIGN","IS_REVOKE","CREATE_USER","CREATE_TIME","UPDATE_TIME","REMARK","LV_ID","AUTH_NAME") values ('8e540b8dffa544369cd4e3253accf213', '8f9dec2ca33040e199f9e922f906dc7f', 'devadmin', null, 0, '1d25e7e5ce164c95bd283f7d6c9b644a', null, null, null, 'a7290384e3f84287bedb968f1c38ad21', '数据源管理');
insert into "SYS_AUTH_ROLE_V" ("ID","AUTH_ID","ROLE_ID","SIGN","IS_REVOKE","CREATE_USER","CREATE_TIME","UPDATE_TIME","REMARK","LV_ID","AUTH_NAME") values ('f569b4bded3644f7ac6b0fb8b95afc65', 'ad2b809bf7084d2db69dd56793cb5e1c', 'devadmin', null, 0, '1d25e7e5ce164c95bd283f7d6c9b644a', null, null, null, 'a7290384e3f84287bedb968f1c38ad21', '服务管理');
insert into "SYS_AUTH_ROLE_V" ("ID","AUTH_ID","ROLE_ID","SIGN","IS_REVOKE","CREATE_USER","CREATE_TIME","UPDATE_TIME","REMARK","LV_ID","AUTH_NAME") values ('90bc0a6c442c4a1780fec75f73dd6ea6', '3860126718034cc2a7dbd8311bfb3ff1', 'devadmin', null, 0, '1d25e7e5ce164c95bd283f7d6c9b644a', null, null, null, 'a7290384e3f84287bedb968f1c38ad21', '应用信息');
insert into "SYS_AUTH_ROLE_V" ("ID","AUTH_ID","ROLE_ID","SIGN","IS_REVOKE","CREATE_USER","CREATE_TIME","UPDATE_TIME","REMARK","LV_ID","AUTH_NAME") values ('ae2228ed1fc14c1398ccbb9cf99484d5', '2a3c459a0c814a098bdc41cb0e4b830e', 'devadmin', null, 0, '1d25e7e5ce164c95bd283f7d6c9b644a', null, null, null, 'a7290384e3f84287bedb968f1c38ad21', '模型管理');
insert into "SYS_AUTH_ROLE_V" ("ID","AUTH_ID","ROLE_ID","SIGN","IS_REVOKE","CREATE_USER","CREATE_TIME","UPDATE_TIME","REMARK","LV_ID","AUTH_NAME") values ('1da1394937134d48a65b36969d10e946', 'e0c7623e577d4811af97dc6eab9c14d6', 'devadmin', null, 0, '1d25e7e5ce164c95bd283f7d6c9b644a', null, null, null, 'a7290384e3f84287bedb968f1c38ad21', '接口管理');
insert into "SYS_AUTH_ROLE_V" ("ID","AUTH_ID","ROLE_ID","SIGN","IS_REVOKE","CREATE_USER","CREATE_TIME","UPDATE_TIME","REMARK","LV_ID","AUTH_NAME") values ('7253b0d2b1a540ce919655a4e33918f0', 'c6d75746103e4dc99d9d0ce27f0e1af1', 'devadmin', null, 0, '1d25e7e5ce164c95bd283f7d6c9b644a', null, null, null, 'a7290384e3f84287bedb968f1c38ad21', '页面设计');
insert into "SYS_AUTH_ROLE_V" ("ID","AUTH_ID","ROLE_ID","SIGN","IS_REVOKE","CREATE_USER","CREATE_TIME","UPDATE_TIME","REMARK","LV_ID","AUTH_NAME") values ('92ba1fa375534e5891ec12a854cd584d', '09a55900eb974483b599b4343afb7c52', 'devadmin', null, 0, '1d25e7e5ce164c95bd283f7d6c9b644a', null, null, null, 'a7290384e3f84287bedb968f1c38ad21', '系统管理');
insert into "SYS_AUTH_ROLE_V" ("ID","AUTH_ID","ROLE_ID","SIGN","IS_REVOKE","CREATE_USER","CREATE_TIME","UPDATE_TIME","REMARK","LV_ID","AUTH_NAME") values ('2cc3a95856534b4aa5135807842f1284', 'e668d7fddbac4aa3a3fdb6502d00ac7d', 'devadmin', null, 0, '1d25e7e5ce164c95bd283f7d6c9b644a', null, null, null, 'a7290384e3f84287bedb968f1c38ad21', '用户管理');
insert into "SYS_AUTH_ROLE_V" ("ID","AUTH_ID","ROLE_ID","SIGN","IS_REVOKE","CREATE_USER","CREATE_TIME","UPDATE_TIME","REMARK","LV_ID","AUTH_NAME") values ('4cac209116d341a5a09d889d754c155a', '8f9dec2ca33040e199f9e922f906dc7f', 'devdeveloper', null, 0, '1d25e7e5ce164c95bd283f7d6c9b644a', null, null, null, 'ec0818a993014e8e9f036a0dfce2905d', '数据源管理');
insert into "SYS_AUTH_ROLE_V" ("ID","AUTH_ID","ROLE_ID","SIGN","IS_REVOKE","CREATE_USER","CREATE_TIME","UPDATE_TIME","REMARK","LV_ID","AUTH_NAME") values ('ef0b7f9119804dcebb30244ad08f5100', '7416563936d84eefafc65325402b380e', 'devdeveloper', null, 0, '1d25e7e5ce164c95bd283f7d6c9b644a', null, null, null, 'ec0818a993014e8e9f036a0dfce2905d', '增删改');
insert into "SYS_AUTH_ROLE_V" ("ID","AUTH_ID","ROLE_ID","SIGN","IS_REVOKE","CREATE_USER","CREATE_TIME","UPDATE_TIME","REMARK","LV_ID","AUTH_NAME") values ('35468f0ad4904564b9b0b3770294202f', 'ad2b809bf7084d2db69dd56793cb5e1c', 'devdeveloper', null, 0, '1d25e7e5ce164c95bd283f7d6c9b644a', null, null, null, 'ec0818a993014e8e9f036a0dfce2905d', '服务管理');
insert into "SYS_AUTH_ROLE_V" ("ID","AUTH_ID","ROLE_ID","SIGN","IS_REVOKE","CREATE_USER","CREATE_TIME","UPDATE_TIME","REMARK","LV_ID","AUTH_NAME") values ('92d2d2b8bcf3408eb5395f1b661e9583', '3860126718034cc2a7dbd8311bfb3ff1', 'devdeveloper', null, 0, '1d25e7e5ce164c95bd283f7d6c9b644a', null, null, null, 'ec0818a993014e8e9f036a0dfce2905d', '应用信息');
insert into "SYS_AUTH_ROLE_V" ("ID","AUTH_ID","ROLE_ID","SIGN","IS_REVOKE","CREATE_USER","CREATE_TIME","UPDATE_TIME","REMARK","LV_ID","AUTH_NAME") values ('6abce43f575c4831b08f3ea30cbf4b2e', '9c844c3686904ccb8ed6c51bd5a7bf5d', 'devdeveloper', null, 0, '1d25e7e5ce164c95bd283f7d6c9b644a', null, null, null, 'ec0818a993014e8e9f036a0dfce2905d', '增删改');
insert into "SYS_AUTH_ROLE_V" ("ID","AUTH_ID","ROLE_ID","SIGN","IS_REVOKE","CREATE_USER","CREATE_TIME","UPDATE_TIME","REMARK","LV_ID","AUTH_NAME") values ('e651a44810bc47c8941af46025cbe4cf', '2a3c459a0c814a098bdc41cb0e4b830e', 'devdeveloper', null, 0, '1d25e7e5ce164c95bd283f7d6c9b644a', null, null, null, 'ec0818a993014e8e9f036a0dfce2905d', '模型管理');
insert into "SYS_AUTH_ROLE_V" ("ID","AUTH_ID","ROLE_ID","SIGN","IS_REVOKE","CREATE_USER","CREATE_TIME","UPDATE_TIME","REMARK","LV_ID","AUTH_NAME") values ('8a04a50c7bb8451ba15f1961cbb171da', '1ea55832fa9c40979e015f8c12c420b3', 'devdeveloper', null, 0, '1d25e7e5ce164c95bd283f7d6c9b644a', null, null, null, 'ec0818a993014e8e9f036a0dfce2905d', '增删改');
insert into "SYS_AUTH_ROLE_V" ("ID","AUTH_ID","ROLE_ID","SIGN","IS_REVOKE","CREATE_USER","CREATE_TIME","UPDATE_TIME","REMARK","LV_ID","AUTH_NAME") values ('b29fe8d9a17f4ed990f688bc76a0b068', 'e0c7623e577d4811af97dc6eab9c14d6', 'devdeveloper', null, 0, '1d25e7e5ce164c95bd283f7d6c9b644a', null, null, null, 'ec0818a993014e8e9f036a0dfce2905d', '接口管理');
insert into "SYS_AUTH_ROLE_V" ("ID","AUTH_ID","ROLE_ID","SIGN","IS_REVOKE","CREATE_USER","CREATE_TIME","UPDATE_TIME","REMARK","LV_ID","AUTH_NAME") values ('90cea634d13844a4af119f1307c1ff2d', '5373fd0c912b4c0fa4c46474199f9858', 'devdeveloper', null, 0, '1d25e7e5ce164c95bd283f7d6c9b644a', null, null, null, 'ec0818a993014e8e9f036a0dfce2905d', '增删改');
insert into "SYS_AUTH_ROLE_V" ("ID","AUTH_ID","ROLE_ID","SIGN","IS_REVOKE","CREATE_USER","CREATE_TIME","UPDATE_TIME","REMARK","LV_ID","AUTH_NAME") values ('bcb2ad89f29a433aba5720adf8fb99af', 'c6d75746103e4dc99d9d0ce27f0e1af1', 'devdeveloper', null, 0, '1d25e7e5ce164c95bd283f7d6c9b644a', null, null, null, 'ec0818a993014e8e9f036a0dfce2905d', '页面设计');
insert into "SYS_AUTH_ROLE_V" ("ID","AUTH_ID","ROLE_ID","SIGN","IS_REVOKE","CREATE_USER","CREATE_TIME","UPDATE_TIME","REMARK","LV_ID","AUTH_NAME") values ('be50571590e145c0a7f55e707d55bf65', 'd9ec0e8823c44aab931666f72bee0965', 'devdeveloper', null, 0, '1d25e7e5ce164c95bd283f7d6c9b644a', null, null, null, 'ec0818a993014e8e9f036a0dfce2905d', '增删改');
insert into "SYS_AUTH_ROLE_V" ("ID","AUTH_ID","ROLE_ID","SIGN","IS_REVOKE","CREATE_USER","CREATE_TIME","UPDATE_TIME","REMARK","LV_ID","AUTH_NAME") values ('80452d237e44470994d0d7d5c1f98d89', '227f8578d72c4cd6b78125ab97c2f17d', 'devdeveloper', null, 0, '1d25e7e5ce164c95bd283f7d6c9b644a', null, null, null, 'ec0818a993014e8e9f036a0dfce2905d', '项目管理');

insert into "SYS_AUTH_ROLE" ("ID","AUTH_ID","ROLE_ID","SIGN","IS_REVOKE","CREATE_USER","CREATE_TIME","UPDATE_TIME","REMARK","AUTH_NAME","ROLE_NAME") values ('741787a581b44b9d955f6c09ed5d3e41', 'a2c96add3b7744c3939083d5351f3828', 'devdeveloper', null, 0, '1d25e7e5ce164c95bd283f7d6c9b644a', null, null, null, '项目信息', 'devdeveloper');
insert into "SYS_AUTH_ROLE_V" ("ID","AUTH_ID","ROLE_ID","SIGN","IS_REVOKE","CREATE_USER","CREATE_TIME","UPDATE_TIME","REMARK","LV_ID","AUTH_NAME") values ('34442d237e44470994d0d7d5c1f98d89', 'a2c96add3b7744c3939083d5351f3828', 'devdeveloper', null, 0, '1d25e7e5ce164c95bd283f7d6c9b644a', null, null, null, 'ec0818a993014e8e9f036a0dfce2905d', '项目信息');


/**
 * 增加不登录查看功能
 * @author JonnyJiang
 * @date 2020/7/13 10:27
 */

ALTER TABLE FILE_CONFIGURATION
 ADD (IS_ALLOW_DLD_WITHOUT_LOGIN  NUMBER(1));
COMMENT ON COLUMN FILE_CONFIGURATION.IS_ALLOW_DLD_WITHOUT_LOGIN IS '是否允许不登录下载';
/**
建表
 */
--
-- PRO_DATASOURCE  (Table)
--
CREATE TABLE PRO_DATASOURCE
(
  ID         VARCHAR2(50 BYTE)                  NOT NULL,
  NAME       VARCHAR2(50 BYTE),
  URL        VARCHAR2(200 BYTE),
  USER_NAME  VARCHAR2(50 BYTE),
  PASSWORD   VARCHAR2(50 BYTE),
  DRIVER     VARCHAR2(50 BYTE),
  SCHEME     VARCHAR2(50 BYTE),
  REMARK     VARCHAR2(2000 BYTE),
  TYPE       VARCHAR2(50 BYTE)                  NOT NULL
);

COMMENT ON TABLE PRO_DATASOURCE IS '数据源';

COMMENT ON COLUMN PRO_DATASOURCE.ID IS '主键';

COMMENT ON COLUMN PRO_DATASOURCE.NAME IS '数据源名称';

COMMENT ON COLUMN PRO_DATASOURCE.URL IS '路径';

COMMENT ON COLUMN PRO_DATASOURCE.USER_NAME IS '数据库用户';

COMMENT ON COLUMN PRO_DATASOURCE.PASSWORD IS '密码';

COMMENT ON COLUMN PRO_DATASOURCE.DRIVER IS '驱动名称';

COMMENT ON COLUMN PRO_DATASOURCE.SCHEME IS 'SCHEME名称';

COMMENT ON COLUMN PRO_DATASOURCE.REMARK IS '备注';

COMMENT ON COLUMN PRO_DATASOURCE.TYPE IS '数据库类型';



--
-- PRO_INFO  (Table)
--
CREATE TABLE PRO_INFO
(
  ID               VARCHAR2(50 BYTE)            NOT NULL,
  NAME             VARCHAR2(50 BYTE),
  CREATE_TIME      DATE,
  MAINTAIN_STAFFS  VARCHAR2(2000 BYTE),
  CODE             VARCHAR2(50 BYTE),
  REMARK           VARCHAR2(2000 BYTE),
  IS_DELETE        INTEGER
);

COMMENT ON TABLE PRO_INFO IS '项目信息表';

COMMENT ON COLUMN PRO_INFO.ID IS '主键';

COMMENT ON COLUMN PRO_INFO.NAME IS '项目名称';

COMMENT ON COLUMN PRO_INFO.CREATE_TIME IS '创建时间';

COMMENT ON COLUMN PRO_INFO.MAINTAIN_STAFFS IS '维护人员ID(多人使用,分割)';

COMMENT ON COLUMN PRO_INFO.CODE IS '标识';

COMMENT ON COLUMN PRO_INFO.REMARK IS '备注';

COMMENT ON COLUMN PRO_INFO.IS_DELETE IS '是否删除，0否，1是';



--
-- PRO_SERVICE  (Table)
--
CREATE TABLE PRO_SERVICE
(
  ID                  VARCHAR2(50 BYTE)         NOT NULL,
  NAME                VARCHAR2(50 BYTE),
  IS_ENABLE_AUTH      INTEGER,
  IS_ENABLE_LOG       INTEGER,
  DS_ID               VARCHAR2(50 BYTE),
  APP_ID              VARCHAR2(50 BYTE),
  PACKAGE_NAME        VARCHAR2(50 BYTE),
  CREATE_SERVICE_FLG  INTEGER,
  IP_PORT             VARCHAR2(50 BYTE),
  NACOS_SERVER_ADDR   VARCHAR2(50 BYTE),
  REDIS_DATA_BASE     INTEGER,
  REDIS_HOST          VARCHAR2(50 BYTE),
  REDIS_PORT          VARCHAR2(50 BYTE),
  REDIS_PASSWORD      VARCHAR2(50 BYTE),
  PRO_INFO_ID         VARCHAR2(50 BYTE),
  IS_DELETE           INTEGER
);

COMMENT ON TABLE PRO_SERVICE IS '应用管理';

COMMENT ON COLUMN PRO_SERVICE.ID IS '主键';

COMMENT ON COLUMN PRO_SERVICE.NAME IS '应用名称';

COMMENT ON COLUMN PRO_SERVICE.IS_ENABLE_AUTH IS '启用权限 1启用0不启用';

COMMENT ON COLUMN PRO_SERVICE.IS_ENABLE_LOG IS '启用日志1启用0不启用';

COMMENT ON COLUMN PRO_SERVICE.DS_ID IS '数据源id';

COMMENT ON COLUMN PRO_SERVICE.APP_ID IS '应用标识';

COMMENT ON COLUMN PRO_SERVICE.PACKAGE_NAME IS '包名';

COMMENT ON COLUMN PRO_SERVICE.CREATE_SERVICE_FLG IS '是否生成过服务，0否，1是';

COMMENT ON COLUMN PRO_SERVICE.IP_PORT IS '应用发布服务端口';

COMMENT ON COLUMN PRO_SERVICE.NACOS_SERVER_ADDR IS 'Nacos服务地址';

COMMENT ON COLUMN PRO_SERVICE.REDIS_DATA_BASE IS 'Redis数据库索引';

COMMENT ON COLUMN PRO_SERVICE.REDIS_HOST IS 'Redis服务器地址';

COMMENT ON COLUMN PRO_SERVICE.REDIS_PORT IS 'Redis连接端口';

COMMENT ON COLUMN PRO_SERVICE.REDIS_PASSWORD IS 'Redis连接密码';

COMMENT ON COLUMN PRO_SERVICE.PRO_INFO_ID IS '项目ID';

COMMENT ON COLUMN PRO_SERVICE.IS_DELETE IS '是否删除，0否，1是';



--
-- PRO_DATASOURCE_PK  (Index)
--
CREATE UNIQUE INDEX PRO_DATASOURCE_PK ON PRO_DATASOURCE
(ID)
LOGGING;


--
-- PRO_INFO_PK  (Index)
--
CREATE UNIQUE INDEX PRO_INFO_PK ON PRO_INFO
(ID)
LOGGING


--
-- PRO_SERVICE_PK  (Index)
--
CREATE UNIQUE INDEX PRO_SERVICE_PK ON PRO_SERVICE
(ID)
LOGGING;


--
-- PRO_CHANGELOG_HISTORY  (Table)
--
CREATE TABLE PRO_CHANGELOG_HISTORY
(
  ID               VARCHAR2(255 BYTE)            NOT NULL,
  LOG_VALUE        CLOB,
  PUBLISH_TAG      INTEGER,
  SERVICE_ID       VARCHAR2(50 BYTE),
  PUBLISH_TIME     DATE,
  CREATE_TIME      DATE,
  PUBLISH_VERSION  INTEGER
);

COMMENT ON TABLE PRO_CHANGELOG_HISTORY IS 'changlog历史信息表';

COMMENT ON COLUMN PRO_CHANGELOG_HISTORY.ID IS '主键';

COMMENT ON COLUMN PRO_CHANGELOG_HISTORY.LOG_VALUE IS 'changeset对象';

COMMENT ON COLUMN PRO_CHANGELOG_HISTORY.PUBLISH_TAG IS '是否发布过，0否1是';

COMMENT ON COLUMN PRO_CHANGELOG_HISTORY.SERVICE_ID IS '服务ID';

COMMENT ON COLUMN PRO_CHANGELOG_HISTORY.PUBLISH_TIME IS '发布时间';

COMMENT ON COLUMN PRO_CHANGELOG_HISTORY.CREATE_TIME IS '创建时间';

COMMENT ON COLUMN PRO_CHANGELOG_HISTORY.PUBLISH_VERSION IS '发布的版本';



--
-- PRO_INTERFACE_CATEGORY  (Table)
--
CREATE TABLE PRO_INTERFACE_CATEGORY
(
  ID           VARCHAR2(50 BYTE)                NOT NULL,
  NAME         VARCHAR2(50 BYTE),
  REMARK       VARCHAR2(2000 BYTE),
  CREATE_TIME  DATE,
  SERVICE_ID   VARCHAR2(50 BYTE)
);

COMMENT ON TABLE PRO_INTERFACE_CATEGORY IS '接口类别';

COMMENT ON COLUMN PRO_INTERFACE_CATEGORY.ID IS '主键';

COMMENT ON COLUMN PRO_INTERFACE_CATEGORY.NAME IS '类别名称';

COMMENT ON COLUMN PRO_INTERFACE_CATEGORY.REMARK IS '备注';

COMMENT ON COLUMN PRO_INTERFACE_CATEGORY.CREATE_TIME IS '创建时间';

COMMENT ON COLUMN PRO_INTERFACE_CATEGORY.SERVICE_ID IS '服务ID';



--
-- PRO_MODEL  (Table)
--
CREATE TABLE PRO_MODEL
(
  ID           VARCHAR2(50 BYTE)                NOT NULL,
  MODEL_NAME   VARCHAR2(100 BYTE),
  TABLE_NAME   VARCHAR2(100 BYTE),
  OBJECT_NAME  VARCHAR2(100 BYTE),
  SERVICE_ID   VARCHAR2(50 BYTE),
  REMARK       VARCHAR2(2000 BYTE),
  CREATE_TIME  DATE,
  PK_NAME      VARCHAR2(50 BYTE)
);

COMMENT ON TABLE PRO_MODEL IS '实体模型信息表';

COMMENT ON COLUMN PRO_MODEL.ID IS '主键';

COMMENT ON COLUMN PRO_MODEL.MODEL_NAME IS '模型名称';

COMMENT ON COLUMN PRO_MODEL.TABLE_NAME IS '数据库表表名';

COMMENT ON COLUMN PRO_MODEL.OBJECT_NAME IS '数据库表实体名';

COMMENT ON COLUMN PRO_MODEL.SERVICE_ID IS '服务ID';

COMMENT ON COLUMN PRO_MODEL.REMARK IS '备注';

COMMENT ON COLUMN PRO_MODEL.CREATE_TIME IS '创建时间';

COMMENT ON COLUMN PRO_MODEL.PK_NAME IS '主键名称';



--
-- PRO_MODEL_ASSOCIATION  (Table)
--
CREATE TABLE PRO_MODEL_ASSOCIATION
(
  ID             VARCHAR2(50 BYTE)              NOT NULL,
  NAME           VARCHAR2(50 BYTE),
  MODEL_ID       VARCHAR2(50 BYTE),
  COL_NAMES      VARCHAR2(2000 BYTE),
  REF_COL_NAMES  VARCHAR2(2000 BYTE),
  REF_MODEL_ID   VARCHAR2(50 BYTE),
  CREATE_TIME    DATE,
  DEL_ACTION     VARCHAR2(50 BYTE),
  UPD_ACTION     VARCHAR2(50 BYTE),
  CREATE_IDX     INTEGER,
  INDEX_NAME     VARCHAR2(50 BYTE),
  INDEX_ID       VARCHAR2(50 BYTE)
);

COMMENT ON TABLE PRO_MODEL_ASSOCIATION IS '数据关联定义';

COMMENT ON COLUMN PRO_MODEL_ASSOCIATION.ID IS '主键';

COMMENT ON COLUMN PRO_MODEL_ASSOCIATION.NAME IS '关联名';

COMMENT ON COLUMN PRO_MODEL_ASSOCIATION.MODEL_ID IS '模型ID';

COMMENT ON COLUMN PRO_MODEL_ASSOCIATION.COL_NAMES IS '数据列名S';

COMMENT ON COLUMN PRO_MODEL_ASSOCIATION.REF_COL_NAMES IS '引用外键列名S';

COMMENT ON COLUMN PRO_MODEL_ASSOCIATION.REF_MODEL_ID IS '引用表id';

COMMENT ON COLUMN PRO_MODEL_ASSOCIATION.CREATE_TIME IS '创建时间';

COMMENT ON COLUMN PRO_MODEL_ASSOCIATION.DEL_ACTION IS '删除动作';

COMMENT ON COLUMN PRO_MODEL_ASSOCIATION.UPD_ACTION IS '更新动作';

COMMENT ON COLUMN PRO_MODEL_ASSOCIATION.CREATE_IDX IS '是否同步创建索引 0否1是';

COMMENT ON COLUMN PRO_MODEL_ASSOCIATION.INDEX_NAME IS '索引名';

COMMENT ON COLUMN PRO_MODEL_ASSOCIATION.INDEX_ID IS '索引列';



--
-- PRO_MODEL_COL  (Table)
--
CREATE TABLE PRO_MODEL_COL
(
  ID              VARCHAR2(50 BYTE)             NOT NULL,
  MODEL_ID        VARCHAR2(50 BYTE),
  TAB_COL_NAME    VARCHAR2(200 BYTE),
  OBJ_COL_NAME    VARCHAR2(200 BYTE),
  CAPTION         VARCHAR2(2000 BYTE),
  DATA_TYPE       VARCHAR2(50 BYTE),
  NULLABLE        INTEGER                       DEFAULT 1,
  DEFAULT_VALUE   VARCHAR2(100 BYTE),
  IS_SYSCOL       INTEGER                       DEFAULT 0,
  CREATE_TIME     DATE,
  PK_FLG          INTEGER                       DEFAULT 0,
  DATA_PRECISION  INTEGER,
  DATA_SCALE      INTEGER                       DEFAULT 0
);

COMMENT ON TABLE PRO_MODEL_COL IS '数据列';

COMMENT ON COLUMN PRO_MODEL_COL.ID IS '主键';

COMMENT ON COLUMN PRO_MODEL_COL.MODEL_ID IS '模型ID';

COMMENT ON COLUMN PRO_MODEL_COL.TAB_COL_NAME IS '表列名';

COMMENT ON COLUMN PRO_MODEL_COL.OBJ_COL_NAME IS '实体对象列名';

COMMENT ON COLUMN PRO_MODEL_COL.CAPTION IS '列标题';

COMMENT ON COLUMN PRO_MODEL_COL.DATA_TYPE IS '数据类型';

COMMENT ON COLUMN PRO_MODEL_COL.NULLABLE IS '能否为空';

COMMENT ON COLUMN PRO_MODEL_COL.DEFAULT_VALUE IS '缺省值';

COMMENT ON COLUMN PRO_MODEL_COL.IS_SYSCOL IS '是否系统字段';

COMMENT ON COLUMN PRO_MODEL_COL.CREATE_TIME IS '创建时间';

COMMENT ON COLUMN PRO_MODEL_COL.PK_FLG IS '是否是主键，0否,1是';

COMMENT ON COLUMN PRO_MODEL_COL.DATA_PRECISION IS '精度';

COMMENT ON COLUMN PRO_MODEL_COL.DATA_SCALE IS '标度';



--
-- PRO_MODEL_INDEX  (Table)
--
CREATE TABLE PRO_MODEL_INDEX
(
  ID           VARCHAR2(50 BYTE)                NOT NULL,
  MODEL_ID     VARCHAR2(50 BYTE),
  NAME         VARCHAR2(200 BYTE),
  COLS         VARCHAR2(2000 BYTE),
  IS_UNIQUE    INTEGER,
  CREATE_TIME  DATE,
  ASS_ACTION   INTEGER                          DEFAULT 0
);

COMMENT ON TABLE PRO_MODEL_INDEX IS '索引信息';

COMMENT ON COLUMN PRO_MODEL_INDEX.ID IS '主键';

COMMENT ON COLUMN PRO_MODEL_INDEX.MODEL_ID IS '模型ID';

COMMENT ON COLUMN PRO_MODEL_INDEX.NAME IS '名称';

COMMENT ON COLUMN PRO_MODEL_INDEX.COLS IS '包含列';

COMMENT ON COLUMN PRO_MODEL_INDEX.IS_UNIQUE IS '是否唯一';

COMMENT ON COLUMN PRO_MODEL_INDEX.CREATE_TIME IS '创建时间';

COMMENT ON COLUMN PRO_MODEL_INDEX.ASS_ACTION IS '是否是外键添加的索引，0否1是';



--
-- PRO_PAGE_INFO  (Table)
--
CREATE TABLE PRO_PAGE_INFO
(
  ID           VARCHAR2(50 BYTE)                NOT NULL,
  NAME         VARCHAR2(50 BYTE),
  DES          VARCHAR2(2000 BYTE),
  JS_CODE      CLOB,
  HTML_CODE    CLOB,
  MODEL_IDS    VARCHAR2(2000 BYTE),
  CREATE_TIME  DATE,
  SERVICE_ID   VARCHAR2(50 BYTE)
);

COMMENT ON TABLE PRO_PAGE_INFO IS '页面基本信息';

COMMENT ON COLUMN PRO_PAGE_INFO.ID IS '主键';

COMMENT ON COLUMN PRO_PAGE_INFO.NAME IS '名称';

COMMENT ON COLUMN PRO_PAGE_INFO.DES IS '描述';

COMMENT ON COLUMN PRO_PAGE_INFO.JS_CODE IS 'js对象';

COMMENT ON COLUMN PRO_PAGE_INFO.HTML_CODE IS 'html对象';

COMMENT ON COLUMN PRO_PAGE_INFO.MODEL_IDS IS '已选数据模型对象ID(英文逗号分隔)';

COMMENT ON COLUMN PRO_PAGE_INFO.CREATE_TIME IS '创建时间';

COMMENT ON COLUMN PRO_PAGE_INFO.SERVICE_ID IS '服务ID';



--
-- PRO_CHANGELOG_HISTORY_PK  (Index)
--
CREATE UNIQUE INDEX PRO_CHANGELOG_HISTORY_PK ON PRO_CHANGELOG_HISTORY
(ID)
LOGGING;


--
-- PRO_INTERFACE_CATEGORY_PK  (Index)
--
CREATE UNIQUE INDEX PRO_INTERFACE_CATEGORY_PK ON PRO_INTERFACE_CATEGORY
(ID)
LOGGING;


--
-- PRO_MODEL_ASSOCIATION_PK  (Index)
--
CREATE UNIQUE INDEX PRO_MODEL_ASSOCIATION_PK ON PRO_MODEL_ASSOCIATION
(ID)
LOGGING;


--
-- PRO_MODEL_COL_PK  (Index)
--
CREATE UNIQUE INDEX PRO_MODEL_COL_PK ON PRO_MODEL_COL
(ID)
LOGGING;


--
-- PRO_MODEL_INDEX_PK  (Index)
--
CREATE UNIQUE INDEX PRO_MODEL_INDEX_PK ON PRO_MODEL_INDEX
(ID)
LOGGING;


--
-- PRO_MODEL_PK  (Index)
--
CREATE UNIQUE INDEX PRO_MODEL_PK ON PRO_MODEL
(ID)
LOGGING;


--
-- PRO_PAGE_INFO_PK  (Index)
--
CREATE UNIQUE INDEX PRO_PAGE_INFO_PK ON PRO_PAGE_INFO
(ID)
LOGGING;


--
-- PRO_INTERFACE  (Table)
--
CREATE TABLE PRO_INTERFACE
(
  ID           VARCHAR2(50 BYTE)                NOT NULL,
  NAME         VARCHAR2(50 BYTE),
  IS_PAGE_GET  INTEGER,
  SQL_CONTENT  CLOB,
  CREATE_TIME  DATE,
  DESCRIPTION  VARCHAR2(2000 BYTE),
  CODE         VARCHAR2(50 BYTE),
  CATEGORY_ID  VARCHAR2(50 BYTE)
);

COMMENT ON TABLE PRO_INTERFACE IS '应用接口';

COMMENT ON COLUMN PRO_INTERFACE.ID IS '主键';

COMMENT ON COLUMN PRO_INTERFACE.NAME IS '接口名';

COMMENT ON COLUMN PRO_INTERFACE.IS_PAGE_GET IS '是否分页方法,0否1是';

COMMENT ON COLUMN PRO_INTERFACE.SQL_CONTENT IS 'sql语句';

COMMENT ON COLUMN PRO_INTERFACE.CREATE_TIME IS '创建时间';

COMMENT ON COLUMN PRO_INTERFACE.DESCRIPTION IS '描述';

COMMENT ON COLUMN PRO_INTERFACE.CODE IS '标识';

COMMENT ON COLUMN PRO_INTERFACE.CATEGORY_ID IS '接口类别ID';



--
-- PRO_OUTPUT_PARAMS  (Table)
--
CREATE TABLE PRO_OUTPUT_PARAMS
(
  ID            VARCHAR2(50 BYTE)               NOT NULL,
  PARAM_KEY     VARCHAR2(50 BYTE),
  PARAM_TYPE    VARCHAR2(50 BYTE),
  INTERFACE_ID  VARCHAR2(50 BYTE),
  PARAM_LABEL   VARCHAR2(50 BYTE)
);

COMMENT ON TABLE PRO_OUTPUT_PARAMS IS '接口sql出参信息表';

COMMENT ON COLUMN PRO_OUTPUT_PARAMS.ID IS '主键';

COMMENT ON COLUMN PRO_OUTPUT_PARAMS.PARAM_KEY IS '键';

COMMENT ON COLUMN PRO_OUTPUT_PARAMS.PARAM_TYPE IS '参数类型';

COMMENT ON COLUMN PRO_OUTPUT_PARAMS.INTERFACE_ID IS '接口主键';

COMMENT ON COLUMN PRO_OUTPUT_PARAMS.PARAM_LABEL IS '字段注释';



--
-- PRO_INTERFACE_PK  (Index)
--
CREATE UNIQUE INDEX PRO_INTERFACE_PK ON PRO_INTERFACE
(ID)
LOGGING;


--
-- PRO_OUTPUT_PARAMS_PK  (Index)
--
CREATE UNIQUE INDEX PRO_OUTPUT_PARAMS_PK ON PRO_OUTPUT_PARAMS
(ID)
LOGGING;


--
-- PRO_INPUT_PARAMS  (Table)
--
CREATE TABLE PRO_INPUT_PARAMS
(
  ID                   VARCHAR2(50 BYTE)        NOT NULL,
  PARAM_KEY            VARCHAR2(50 BYTE),
  PARAM_DEFAULT_VALUE  VARCHAR2(100 BYTE),
  PARAM_TYPE           VARCHAR2(50 BYTE),
  INTERFACE_ID         VARCHAR2(50 BYTE)
);

COMMENT ON TABLE PRO_INPUT_PARAMS IS '应用接口';

COMMENT ON COLUMN PRO_INPUT_PARAMS.ID IS '主键';

COMMENT ON COLUMN PRO_INPUT_PARAMS.PARAM_KEY IS '键';

COMMENT ON COLUMN PRO_INPUT_PARAMS.PARAM_DEFAULT_VALUE IS '缺省值';

COMMENT ON COLUMN PRO_INPUT_PARAMS.PARAM_TYPE IS '类别';

COMMENT ON COLUMN PRO_INPUT_PARAMS.INTERFACE_ID IS '接口ID';



--
-- PRO_INPUT_PARAMS_PK  (Index)
--
CREATE UNIQUE INDEX PRO_INPUT_PARAMS_PK ON PRO_INPUT_PARAMS
(ID)
LOGGING;


--
-- Non Foreign Key Constraints for Table PRO_DATASOURCE
--
ALTER TABLE PRO_DATASOURCE ADD (
  CONSTRAINT PRO_DATASOURCE_PK
  PRIMARY KEY
  (ID)
  USING INDEX PRO_DATASOURCE_PK
  ENABLE VALIDATE);

--
-- Non Foreign Key Constraints for Table PRO_INFO
--
ALTER TABLE PRO_INFO ADD (
  CONSTRAINT PRO_INFO_PK
  PRIMARY KEY
  (ID)
  USING INDEX PRO_INFO_PK
  ENABLE VALIDATE);

--
-- Non Foreign Key Constraints for Table PRO_SERVICE
--
ALTER TABLE PRO_SERVICE ADD (
  CONSTRAINT PRO_SERVICE_PK
  PRIMARY KEY
  (ID)
  USING INDEX PRO_SERVICE_PK
  ENABLE VALIDATE);

--
-- Non Foreign Key Constraints for Table PRO_CHANGELOG_HISTORY
--
ALTER TABLE PRO_CHANGELOG_HISTORY ADD (
  CONSTRAINT PRO_CHANGELOG_HISTORY_PK
  PRIMARY KEY
  (ID)
  USING INDEX PRO_CHANGELOG_HISTORY_PK
  ENABLE VALIDATE);

--
-- Non Foreign Key Constraints for Table PRO_INTERFACE_CATEGORY
--
ALTER TABLE PRO_INTERFACE_CATEGORY ADD (
  CONSTRAINT PRO_INTERFACE_CATEGORY_PK
  PRIMARY KEY
  (ID)
  USING INDEX PRO_INTERFACE_CATEGORY_PK
  ENABLE VALIDATE);

--
-- Non Foreign Key Constraints for Table PRO_MODEL
--
ALTER TABLE PRO_MODEL ADD (
  CONSTRAINT PRO_MODEL_PK
  PRIMARY KEY
  (ID)
  USING INDEX PRO_MODEL_PK
  ENABLE VALIDATE);

--
-- Non Foreign Key Constraints for Table PRO_MODEL_ASSOCIATION
--
ALTER TABLE PRO_MODEL_ASSOCIATION ADD (
  CONSTRAINT PRO_MODEL_ASSOCIATION_PK
  PRIMARY KEY
  (ID)
  USING INDEX PRO_MODEL_ASSOCIATION_PK
  ENABLE VALIDATE);

--
-- Non Foreign Key Constraints for Table PRO_MODEL_COL
--
ALTER TABLE PRO_MODEL_COL ADD (
  CONSTRAINT PRO_MODEL_COL_PK
  PRIMARY KEY
  (ID)
  USING INDEX PRO_MODEL_COL_PK
  ENABLE VALIDATE);

--
-- Non Foreign Key Constraints for Table PRO_MODEL_INDEX
--
ALTER TABLE PRO_MODEL_INDEX ADD (
  CONSTRAINT PRO_MODEL_INDEX_PK
  PRIMARY KEY
  (ID)
  USING INDEX PRO_MODEL_INDEX_PK
  ENABLE VALIDATE);

--
-- Non Foreign Key Constraints for Table PRO_PAGE_INFO
--
ALTER TABLE PRO_PAGE_INFO ADD (
  CONSTRAINT PRO_PAGE_INFO_PK
  PRIMARY KEY
  (ID)
  USING INDEX PRO_PAGE_INFO_PK
  ENABLE VALIDATE);

--
-- Non Foreign Key Constraints for Table PRO_INTERFACE
--
ALTER TABLE PRO_INTERFACE ADD (
  CONSTRAINT PRO_INTERFACE_PK
  PRIMARY KEY
  (ID)
  USING INDEX PRO_INTERFACE_PK
  ENABLE VALIDATE);

--
-- Non Foreign Key Constraints for Table PRO_OUTPUT_PARAMS
--
ALTER TABLE PRO_OUTPUT_PARAMS ADD (
  CONSTRAINT PRO_OUTPUT_PARAMS_PK
  PRIMARY KEY
  (ID)
  USING INDEX PRO_OUTPUT_PARAMS_PK
  ENABLE VALIDATE);

--
-- Non Foreign Key Constraints for Table PRO_INPUT_PARAMS
--
ALTER TABLE PRO_INPUT_PARAMS ADD (
  CONSTRAINT PRO_INPUT_PARAMS_PK
  PRIMARY KEY
  (ID)
  USING INDEX PRO_INPUT_PARAMS_PK
  ENABLE VALIDATE);

--
-- Foreign Key Constraints for Table PRO_SERVICE
--
ALTER TABLE PRO_SERVICE ADD (
  CONSTRAINT FK_PRO_SERVIE_INFO_ID
  FOREIGN KEY (PRO_INFO_ID)
  REFERENCES PRO_INFO (ID)
  ON DELETE CASCADE
  ENABLE VALIDATE,
  CONSTRAINT IDX_PRO_SERVICE_DS_ID
  FOREIGN KEY (DS_ID)
  REFERENCES PRO_DATASOURCE (ID)
  ON DELETE CASCADE
  ENABLE VALIDATE);

--
-- Foreign Key Constraints for Table PRO_CHANGELOG_HISTORY
--
ALTER TABLE PRO_CHANGELOG_HISTORY ADD (
  CONSTRAINT FK_LOG_HISTORY_SERVICE_ID
  FOREIGN KEY (SERVICE_ID)
  REFERENCES PRO_SERVICE (ID)
  ON DELETE CASCADE
  ENABLE VALIDATE);

--
-- Foreign Key Constraints for Table PRO_INTERFACE_CATEGORY
--
ALTER TABLE PRO_INTERFACE_CATEGORY ADD (
  CONSTRAINT FK_CATEGORY_SERVICE_ID
  FOREIGN KEY (SERVICE_ID)
  REFERENCES PRO_SERVICE (ID)
  ON DELETE CASCADE
  ENABLE VALIDATE);

--
-- Foreign Key Constraints for Table PRO_MODEL
--
ALTER TABLE PRO_MODEL ADD (
  CONSTRAINT FK_ENTITY_MODEL_SERVICE_ID
  FOREIGN KEY (SERVICE_ID)
  REFERENCES PRO_SERVICE (ID)
  ON DELETE CASCADE
  ENABLE VALIDATE);

--
-- Foreign Key Constraints for Table PRO_MODEL_ASSOCIATION
--
ALTER TABLE PRO_MODEL_ASSOCIATION ADD (
  CONSTRAINT FK_MODEL_ASS_MODEL_ID
  FOREIGN KEY (MODEL_ID)
  REFERENCES PRO_MODEL (ID)
  ON DELETE CASCADE
  ENABLE VALIDATE,
  CONSTRAINT FK_MODEL_ASS_REF_MODEL_ID
  FOREIGN KEY (REF_MODEL_ID)
  REFERENCES PRO_MODEL (ID)
  ON DELETE CASCADE
  ENABLE VALIDATE);

--
-- Foreign Key Constraints for Table PRO_MODEL_COL
--
ALTER TABLE PRO_MODEL_COL ADD (
  CONSTRAINT FK_PRO_MODEL_COL_MODEL_ID
  FOREIGN KEY (MODEL_ID)
  REFERENCES PRO_MODEL (ID)
  ON DELETE CASCADE
  ENABLE VALIDATE);

--
-- Foreign Key Constraints for Table PRO_MODEL_INDEX
--
ALTER TABLE PRO_MODEL_INDEX ADD (
  CONSTRAINT FK_MODEL_INDEX_MODEL_ID
  FOREIGN KEY (MODEL_ID)
  REFERENCES PRO_MODEL (ID)
  ON DELETE CASCADE
  ENABLE VALIDATE);

--
-- Foreign Key Constraints for Table PRO_PAGE_INFO
--
ALTER TABLE PRO_PAGE_INFO ADD (
  CONSTRAINT FK_PAGEINFO_SERVICE_ID
  FOREIGN KEY (SERVICE_ID)
  REFERENCES PRO_SERVICE (ID)
  ON DELETE CASCADE
  ENABLE VALIDATE);

--
-- Foreign Key Constraints for Table PRO_INTERFACE
--
ALTER TABLE PRO_INTERFACE ADD (
  CONSTRAINT FK_INTERFACE_CATEGORY_ID
  FOREIGN KEY (CATEGORY_ID)
  REFERENCES PRO_INTERFACE_CATEGORY (ID)
  ON DELETE CASCADE
  ENABLE VALIDATE);

--
-- Foreign Key Constraints for Table PRO_OUTPUT_PARAMS
--
ALTER TABLE PRO_OUTPUT_PARAMS ADD (
  CONSTRAINT FK_OUT_INTERFACE_ID
  FOREIGN KEY (INTERFACE_ID)
  REFERENCES PRO_INTERFACE (ID)
  ON DELETE CASCADE
  ENABLE VALIDATE);

--
-- Foreign Key Constraints for Table PRO_INPUT_PARAMS
--
ALTER TABLE PRO_INPUT_PARAMS ADD (
  CONSTRAINT FK_IN_INTERFACE_ID
  FOREIGN KEY (INTERFACE_ID)
  REFERENCES PRO_INTERFACE (ID)
  ON DELETE CASCADE
  ENABLE VALIDATE);

/**
 * 大屏展示
 * @param null
 * @return
 * @author zuogang
 * @date 2020/7/15 10:17
 */
CREATE TABLE BLADE_VISUAL_SHOW
(
  ID         VARCHAR2(50)                       NOT NULL,
  VISUAL_ID  VARCHAR2(50),
  TYPE       INTEGER,
  NAME       VARCHAR2(50),
  PARENT_ID  VARCHAR2(50),
  APP_ID     VARCHAR2(50),
  EXHIBITION_TYPE     VARCHAR2(50),
  CONTENT    CLOB
);

COMMENT ON TABLE BLADE_VISUAL_SHOW IS '大屏展示';

COMMENT ON COLUMN BLADE_VISUAL_SHOW.ID IS '主键';

COMMENT ON COLUMN BLADE_VISUAL_SHOW.VISUAL_ID IS '大屏ID';

COMMENT ON COLUMN BLADE_VISUAL_SHOW.TYPE IS '类型(0为看版,1为项)';

COMMENT ON COLUMN BLADE_VISUAL_SHOW.NAME IS '名称';

COMMENT ON COLUMN BLADE_VISUAL_SHOW.PARENT_ID IS '父节点';

COMMENT ON COLUMN BLADE_VISUAL_SHOW.CONTENT IS 'json对象';

COMMENT ON COLUMN BLADE_VISUAL_SHOW.APP_ID IS '应用ID';

COMMENT ON COLUMN BLADE_VISUAL_SHOW.EXHIBITION_TYPE IS '显示类型';



ALTER TABLE BLADE_VISUAL_SHOW ADD (
  CONSTRAINT BLADE_VISUAL_SHOW_PK
  PRIMARY KEY
  (ID)
  ENABLE VALIDATE);

ALTER TABLE BLADE_VISUAL_SHOW ADD (
  CONSTRAINT FK_VISUAL_SHOW_APP_ID
  FOREIGN KEY (APP_ID)
  REFERENCES SYS_GROUP_APP (ID)
  ON DELETE CASCADE
  ENABLE VALIDATE);


/**
 * 数字序列，服务编码
 * @param null
 * @return
 * @author zuogang
 * @date 2020/7/17 9:40
 */
insert into "SYS_AUTH" ("ID","NAME","APP_ID","REMARK","SORT_INDEX","PARENT_ID","CREATE_USER","CREATE_TIME","UPDATE_TIME","SORT_PATH","IS_ORG_ADMIN","IS_USER_GROUP_ADMIN","CODE","DATA_VERSION") values ('1275967ea4a4e54acdaff4yhmzq8370', '数字序列', 'platform', null,16, '11316279a4a9416a8e20019821e640af', null, null, null, '00030016', 0, 0, 'platForm.codeSequenceManage.codeSequence', 0);

insert into "SYS_MENU" ("ID","NAME","PARENT_ID","APP_ID","PARENT_NAME","URL","TYPE","ICON","AUTH_ID","BTN_AUTH","IS_LEAF","SORT_INDEX","ALL_ORDER","SORT_PATH","OPEN_STYLE","CLOSE_NOTICE","CREATE_USER","CREATE_TIME","UPDATE_TIME","LEAF","MENU_AUTH","REMARK","DATA_VERSION","IS_IFRAME") values ('1275967ea4a4e54acdaff4yhmzq8370', '数字序列', '11316279a4a9416a8e20019821e640af', 'platform', '配置管理', 'codeSequenceManage/views/codeSequence', 1, 'fa fa-sort-numeric-desc', '1275967ea4a4e54acdaff4yhmzq8370', null, null, 16, null, '00030016', null, null, null, null, null, null, null, null, 0, 0);

insert into "SYS_AUTH_ROLE" ("ID","AUTH_ID","ROLE_ID","SIGN","IS_REVOKE","CREATE_USER","CREATE_TIME","UPDATE_TIME","REMARK") values ('1275967ea4a4e54acdaff4yhmzq8370', '1275967ea4a4e54acdaff4yhmzq8370', 'appadmin', null, 0, null, null, null, null);

insert into "SYS_AUTH_ROLE_V" ("ID","AUTH_ID","ROLE_ID","SIGN","IS_REVOKE","CREATE_USER","CREATE_TIME","UPDATE_TIME","REMARK","LV_ID") values ('1275967ea4a4e54acdaff4yhmzq8370', '1275967ea4a4e54acdaff4yhmzq8370', 'appadmin', null, 0, null, null, null, null, 'appadmin');


insert into "SYS_AUTH" ("ID","NAME","APP_ID","REMARK","SORT_INDEX","PARENT_ID","CREATE_USER","CREATE_TIME","UPDATE_TIME","SORT_PATH","IS_ORG_ADMIN","IS_USER_GROUP_ADMIN","CODE","DATA_VERSION") values ('0745867ea4a4e54acdaff4yhmzq8370', '服务编码', 'platform', null,17, '11316279a4a9416a8e20019821e640af', null, null, null, '00030017', 0, 0, 'platForm.codeTemplateManage.codeTemplate', 0);

insert into "SYS_MENU" ("ID","NAME","PARENT_ID","APP_ID","PARENT_NAME","URL","TYPE","ICON","AUTH_ID","BTN_AUTH","IS_LEAF","SORT_INDEX","ALL_ORDER","SORT_PATH","OPEN_STYLE","CLOSE_NOTICE","CREATE_USER","CREATE_TIME","UPDATE_TIME","LEAF","MENU_AUTH","REMARK","DATA_VERSION","IS_IFRAME") values ('0745867ea4a4e54acdaff4yhmzq8370', '服务编码', '11316279a4a9416a8e20019821e640af', 'platform', '配置管理', 'codeTemplateManage/views/codeTemplate', 1, 'fa fa-codepen', '0745867ea4a4e54acdaff4yhmzq8370', null, null, 17, null, '00030017', null, null, null, null, null, null, null, null, 0, 0);

insert into "SYS_AUTH_ROLE" ("ID","AUTH_ID","ROLE_ID","SIGN","IS_REVOKE","CREATE_USER","CREATE_TIME","UPDATE_TIME","REMARK") values ('0745867ea4a4e54acdaff4yhmzq8370', '0745867ea4a4e54acdaff4yhmzq8370', 'appadmin', null, 0, null, null, null, null);

insert into "SYS_AUTH_ROLE_V" ("ID","AUTH_ID","ROLE_ID","SIGN","IS_REVOKE","CREATE_USER","CREATE_TIME","UPDATE_TIME","REMARK","LV_ID") values ('0745867ea4a4e54acdaff4yhmzq8370', '0745867ea4a4e54acdaff4yhmzq8370', 'appadmin', null, 0, null, null, null, null, 'appadmin');


CREATE TABLE CODE_SEQUENCE
(
  ID                  VARCHAR2(50)              NOT NULL,
  APP_ID              VARCHAR2(50)              NOT NULL,
  BIZ_TAG             VARCHAR2(100)             NOT NULL,
  PART_VALUE_TAG      VARCHAR2(100)             DEFAULT 'ACE_NULL_PART_VALUE_TAG',
  MAX_NUM             NUMBER(22)                DEFAULT 0                     NOT NULL,
  STEP                NUMBER(10)                DEFAULT 1                     NOT NULL,
  REMARK              VARCHAR2(2000),
  RESET_MODE          NUMBER(1)                 DEFAULT 0                     NOT NULL,
  LAST_RESET_TIME     DATE                      DEFAULT SYSDATE,
  DATA_VERSION        NUMBER(10)                DEFAULT 0,
  ENABLE_FIX_NUM_LEN  NUMBER(1)                 DEFAULT 1,
  NUM_LENGTH          NUMBER(1)
);

COMMENT ON TABLE CODE_SEQUENCE IS '数字序列';

COMMENT ON COLUMN CODE_SEQUENCE.ID IS '主键';

COMMENT ON COLUMN CODE_SEQUENCE.APP_ID IS '应用id';

COMMENT ON COLUMN CODE_SEQUENCE.BIZ_TAG IS '序列主要标识';

COMMENT ON COLUMN CODE_SEQUENCE.PART_VALUE_TAG IS '序列次要标识';

COMMENT ON COLUMN CODE_SEQUENCE.MAX_NUM IS '已发放的最大编号';

COMMENT ON COLUMN CODE_SEQUENCE.STEP IS '号段长度';

COMMENT ON COLUMN CODE_SEQUENCE.REMARK IS '数字序列描述';

COMMENT ON COLUMN CODE_SEQUENCE.RESET_MODE IS '序列重置模式 “0 不重置”、 “1 按年重置”、“2 按月重置”、“3 按天重置”';

COMMENT ON COLUMN CODE_SEQUENCE.LAST_RESET_TIME IS '上次重置序列时间';

COMMENT ON COLUMN CODE_SEQUENCE.DATA_VERSION IS '数据版本';

COMMENT ON COLUMN CODE_SEQUENCE.ENABLE_FIX_NUM_LEN IS '是否启用固定数字位数 0否1是';

COMMENT ON COLUMN CODE_SEQUENCE.NUM_LENGTH IS '序列固定位数长度';



ALTER TABLE CODE_SEQUENCE ADD (
  CONSTRAINT CODE_SEQUENCE_PK
  PRIMARY KEY
  (ID)
  ENABLE VALIDATE);

ALTER TABLE CODE_SEQUENCE ADD (
  CONSTRAINT FK_CODE_SEQUENCE_APP_ID
  FOREIGN KEY (APP_ID)
  REFERENCES SYS_GROUP_APP (ID)
  ON DELETE CASCADE
  ENABLE VALIDATE);

CREATE TABLE CODE_TEMPLATE
(
  ID            VARCHAR2(50)                    NOT NULL,
  APP_ID        VARCHAR2(50),
  TEMPLATE_KEY  VARCHAR2(100),
  REMARK        VARCHAR2(2000),
  DATA_VERSION  NUMBER(10)                      DEFAULT 0
);

COMMENT ON TABLE CODE_TEMPLATE IS '编码模板';

COMMENT ON COLUMN CODE_TEMPLATE.ID IS '主键';

COMMENT ON COLUMN CODE_TEMPLATE.APP_ID IS '应用id';

COMMENT ON COLUMN CODE_TEMPLATE.TEMPLATE_KEY IS '模板标识';

COMMENT ON COLUMN CODE_TEMPLATE.REMARK IS '模板描述';

COMMENT ON COLUMN CODE_TEMPLATE.DATA_VERSION IS '数据版本';



ALTER TABLE CODE_TEMPLATE ADD (
  CONSTRAINT CODE_TEMPLATE_PK
  PRIMARY KEY
  (ID)
  ENABLE VALIDATE);

ALTER TABLE CODE_TEMPLATE ADD (
  CONSTRAINT FK_CODE_TEMPLATE_APP_ID
  FOREIGN KEY (APP_ID)
  REFERENCES SYS_GROUP_APP (ID)
  ON DELETE CASCADE
  ENABLE VALIDATE);

CREATE TABLE CODE_TEMPLATE_PART
(
  ID                   VARCHAR2(50)             NOT NULL,
  SORT_INDEX           NUMBER(10)               DEFAULT 0                     NOT NULL,
  CODE_TYPE            NUMBER(1)                NOT NULL,
  EL_EXPRESS           VARCHAR2(100),
  STATIC_TEXT          VARCHAR2(100),
  PARAM_KEY            VARCHAR2(100),
  AUTO_CREATE_SEQ_DEF  NUMBER(1)                DEFAULT 0                     NOT NULL,
  SEQ_STEP             NUMBER(1),
  SEQ_RESET_MODE       NUMBER(1),
  ENABLE_FIX_NUM_LEN   NUMBER(1)                DEFAULT 0                     NOT NULL,
  SEQ_NUM_LENGTH       NUMBER(10),
  SEQUENCE_BIZ_TAG     VARCHAR2(100),
  REMARK               VARCHAR2(2000),
  TEMPLATE_ID          VARCHAR2(50)             NOT NULL,
  DYN_PART             VARCHAR2(500)
);

COMMENT ON TABLE CODE_TEMPLATE_PART IS '模板部件';

COMMENT ON COLUMN CODE_TEMPLATE_PART.ID IS '主键';

COMMENT ON COLUMN CODE_TEMPLATE_PART.SORT_INDEX IS '部件排序';

COMMENT ON COLUMN CODE_TEMPLATE_PART.CODE_TYPE IS '部件编码类型 0静态文本1el表达式2参数值3数字序列';

COMMENT ON COLUMN CODE_TEMPLATE_PART.EL_EXPRESS IS 'el表达式';

COMMENT ON COLUMN CODE_TEMPLATE_PART.STATIC_TEXT IS '静态文本';

COMMENT ON COLUMN CODE_TEMPLATE_PART.PARAM_KEY IS '参数键名';

COMMENT ON COLUMN CODE_TEMPLATE_PART.AUTO_CREATE_SEQ_DEF IS '是否自动创建数字序列';

COMMENT ON COLUMN CODE_TEMPLATE_PART.SEQ_STEP IS '序列步长';

COMMENT ON COLUMN CODE_TEMPLATE_PART.SEQ_RESET_MODE IS '序列重置模式';

COMMENT ON COLUMN CODE_TEMPLATE_PART.ENABLE_FIX_NUM_LEN IS '是否启用固定数字位数 0否1是';

COMMENT ON COLUMN CODE_TEMPLATE_PART.SEQ_NUM_LENGTH IS '规定数字位数';

COMMENT ON COLUMN CODE_TEMPLATE_PART.SEQUENCE_BIZ_TAG IS '关联数字学列主标识';

COMMENT ON COLUMN CODE_TEMPLATE_PART.REMARK IS '模板部件描述';

COMMENT ON COLUMN CODE_TEMPLATE_PART.TEMPLATE_ID IS '模板主键';

COMMENT ON COLUMN CODE_TEMPLATE_PART.DYN_PART IS '数字序列部件关联部件';



ALTER TABLE CODE_TEMPLATE_PART ADD (
  CONSTRAINT CODE_TEMPLATE_PART_PK
  PRIMARY KEY
  (ID)
  ENABLE VALIDATE);

ALTER TABLE CODE_TEMPLATE_PART ADD (
  CONSTRAINT FK_CODE_TEMPLATE_PART_TMP_ID
  FOREIGN KEY (TEMPLATE_ID)
  REFERENCES CODE_TEMPLATE (ID)
  ON DELETE CASCADE
  ENABLE VALIDATE);

/**
 * 大屏展示
 * @param null
 * @return
 * @author zuogang
 * @date 2020/7/17 9:40
 */
insert into "SYS_AUTH" ("ID","NAME","APP_ID","REMARK","SORT_INDEX","PARENT_ID","CREATE_USER","CREATE_TIME","UPDATE_TIME","SORT_PATH","IS_ORG_ADMIN","IS_USER_GROUP_ADMIN","CODE","DATA_VERSION") values ('8234ba3204344d37929abd4ad9a1dgth', '大屏展示', 'platform', null, 8, 'twerba3204344d37929abd4ad9a1dfgh', null, null, null, '00060008', 0, 0, 'screenDisplay.views.ScreenDisplayManage', 0);
insert into "SYS_MENU" ("ID","NAME","PARENT_ID","APP_ID","PARENT_NAME","URL","TYPE","ICON","AUTH_ID","BTN_AUTH","IS_LEAF","SORT_INDEX","ALL_ORDER","SORT_PATH","OPEN_STYLE","CLOSE_NOTICE","CREATE_USER","CREATE_TIME","UPDATE_TIME","LEAF","MENU_AUTH","REMARK","DATA_VERSION","IS_IFRAME") values ('8234ba3204344d37929abd4ad9a1dgth', '大屏展示', 'twerba3204344d37929abd4ad9a1dfgh', 'platform', '数据展示', 'screenDisplay/views/ScreenDisplayManage', 1, 'iconfont icon-Report', '8234ba3204344d37929abd4ad9a1dgth', null, null, 8, null, '00060008', null, null, null, null, null, null, null, null, 0, 0);
insert into "SYS_AUTH_ROLE" ("ID","AUTH_ID","ROLE_ID","SIGN","IS_REVOKE","CREATE_USER","CREATE_TIME","UPDATE_TIME","REMARK") values ('8234ba3204344d37929abd4ad9a1dgth', '8234ba3204344d37929abd4ad9a1dgth', 'appadmin', null, 0, null, null, null, null);
insert into "SYS_AUTH_ROLE_V" ("ID","AUTH_ID","ROLE_ID","SIGN","IS_REVOKE","CREATE_USER","CREATE_TIME","UPDATE_TIME","REMARK","LV_ID") values ('8234ba3204344d37929abd4ad9a1dgth', '8234ba3204344d37929abd4ad9a1dgth', 'appadmin', null, 0, null, null, null, null, 'appadmin');
/**
 * 模型管理
 * @param null
 * @return
 * @author zuogang
 * @date 2020/7/17 9:40
 */
insert into "SYS_AUTH" ("ID","NAME","APP_ID","REMARK","SORT_INDEX","PARENT_ID","CREATE_USER","CREATE_TIME","UPDATE_TIME","SORT_PATH","IS_ORG_ADMIN","IS_USER_GROUP_ADMIN","CODE","DATA_VERSION") values ('c789567ea4a4e54acdaff4yhmzq8370', '模型管理', 'platform', null,18, '11316279a4a9416a8e20019821e640af', null, null, null, '00030018', 0, 0, 'platForm.ModelManage.model', 0);

insert into "SYS_MENU" ("ID","NAME","PARENT_ID","APP_ID","PARENT_NAME","URL","TYPE","ICON","AUTH_ID","BTN_AUTH","IS_LEAF","SORT_INDEX","ALL_ORDER","SORT_PATH","OPEN_STYLE","CLOSE_NOTICE","CREATE_USER","CREATE_TIME","UPDATE_TIME","LEAF","MENU_AUTH","REMARK","DATA_VERSION","IS_IFRAME") values ('c789567ea4a4e54acdaff4yhmzq8370', '模型管理', '11316279a4a9416a8e20019821e640af', 'platform', '配置管理', 'ModelManage/views/Model', 1, 'fa fa-briefcase', 'c789567ea4a4e54acdaff4yhmzq8370', null, null, 18, null, '00030018', null, null, null, null, null, null, null, null, 0, 0);

insert into "SYS_AUTH_ROLE" ("ID","AUTH_ID","ROLE_ID","SIGN","IS_REVOKE","CREATE_USER","CREATE_TIME","UPDATE_TIME","REMARK") values ('c789567ea4a4e54acdaff4yhmzq8370', 'c789567ea4a4e54acdaff4yhmzq8370', 'appadmin', null, 0, null, null, null, null);

insert into "SYS_AUTH_ROLE_V" ("ID","AUTH_ID","ROLE_ID","SIGN","IS_REVOKE","CREATE_USER","CREATE_TIME","UPDATE_TIME","REMARK","LV_ID") values ('c789567ea4a4e54acdaff4yhmzq8370', 'c789567ea4a4e54acdaff4yhmzq8370', 'appadmin', null, 0, null, null, null, null, 'appadmin');


/**
 * 添加 trace_id
 * @param null
 * @return
 * @author zuogang
 * @date 2020/7/20 16:48
 */
-- 数据源
alter table "SYS_GROUP_DATASOURCE" add ("TRACE_ID" VARCHAR2(50));
comment on column "SYS_GROUP_DATASOURCE"."TRACE_ID" is '跟踪ID';
update "SYS_GROUP_DATASOURCE" set "TRACE_ID"="ID";
-- 权限定义
alter table "SYS_AUTH" add ("TRACE_ID" VARCHAR2(50));
comment on column "SYS_AUTH"."TRACE_ID" is '跟踪ID';
update "SYS_AUTH" set "TRACE_ID"="ID";
-- API
alter table "SYS_API_RESOURCE" add ("TRACE_ID" VARCHAR2(200));
comment on column "SYS_API_RESOURCE"."TRACE_ID" is '跟踪ID';
update "SYS_API_RESOURCE" set "TRACE_ID"="ID";
-- 权限API
alter table "SYS_AUTH_API" add ("TRACE_ID" VARCHAR2(50));
comment on column "SYS_AUTH_API"."TRACE_ID" is '跟踪ID';
update "SYS_AUTH_API" set "TRACE_ID"="ID";
-- 菜单定义
alter table "SYS_MENU" add ("TRACE_ID" VARCHAR2(50));
comment on column "SYS_MENU"."TRACE_ID" is '跟踪ID';
update "SYS_MENU" set "TRACE_ID"="ID";
-- 角色定义
alter table "SYS_ROLE" add ("TRACE_ID" VARCHAR2(50));
comment on column "SYS_ROLE"."TRACE_ID" is '跟踪ID';
update "SYS_ROLE" set "TRACE_ID"="ID";
-- 角色授权
alter table "SYS_AUTH_ROLE" add ("TRACE_ID" VARCHAR2(50));
comment on column "SYS_AUTH_ROLE"."TRACE_ID" is '跟踪ID';
update "SYS_AUTH_ROLE" set "TRACE_ID"="ID";
-- 上下级角色
alter table "SYS_ROLE_RELATION" add ("TRACE_ID" VARCHAR2(50));
comment on column "SYS_ROLE_RELATION"."TRACE_ID" is '跟踪ID';
update "SYS_ROLE_RELATION" set "TRACE_ID"="ID";
-- 角色互斥
alter table "SYS_ROLE_MUTEX" add ("TRACE_ID" VARCHAR2(50));
comment on column "SYS_ROLE_MUTEX"."TRACE_ID" is '跟踪ID';
update "SYS_ROLE_MUTEX" set "TRACE_ID"="ID";

alter table "SYS_AUTH_ROLE_LV" add ("TRACE_ID" VARCHAR2(50));
comment on column "SYS_AUTH_ROLE_LV"."TRACE_ID" is '跟踪ID';
update "SYS_AUTH_ROLE_LV" set "TRACE_ID"="ID";

alter table "SYS_AUTH_ROLE_V" add ("TRACE_ID" VARCHAR2(50));
comment on column "SYS_AUTH_ROLE_V"."TRACE_ID" is '跟踪ID';
update "SYS_AUTH_ROLE_V" set "TRACE_ID"="ID";
-- 附件配置
alter table "FILE_CONFIGURATION" add ("TRACE_ID" VARCHAR2(50));
comment on column "FILE_CONFIGURATION"."TRACE_ID" is '跟踪ID';
update "FILE_CONFIGURATION" set "TRACE_ID"="ID";
-- 定时任务
alter table "QRTZ_CONFIG" add ("TRACE_ID" VARCHAR2(50));
comment on column "QRTZ_CONFIG"."TRACE_ID" is '跟踪ID';
update "QRTZ_CONFIG" set "TRACE_ID"="ID";
-- 消息通道
alter table "SYS_MSG_SEND_TYPE" add ("TRACE_ID" VARCHAR2(50));
comment on column "SYS_MSG_SEND_TYPE"."TRACE_ID" is '跟踪ID';
update "SYS_MSG_SEND_TYPE" set "TRACE_ID"="ID";
-- 消息模板
alter table "SYS_MSG_TEMPLATE" add ("TRACE_ID" VARCHAR2(50));
comment on column "SYS_MSG_TEMPLATE"."TRACE_ID" is '跟踪ID';
update "SYS_MSG_TEMPLATE" set "TRACE_ID"="ID";
-- 信使消息模板
alter table "SYS_MSG_TEMPLATE_CONFIG" add ("TRACE_ID" VARCHAR2(50));
comment on column "SYS_MSG_TEMPLATE_CONFIG"."TRACE_ID" is '跟踪ID';
update "SYS_MSG_TEMPLATE_CONFIG" set "TRACE_ID"="ID";
-- 消息拓展
alter table "SYS_MSG_TYPE_EXTEND" add ("TRACE_ID" VARCHAR2(50));
comment on column "SYS_MSG_TYPE_EXTEND"."TRACE_ID" is '跟踪ID';
update "SYS_MSG_TYPE_EXTEND" set "TRACE_ID"="ID";
-- 数据字典
alter table "SYS_DICT" add ("TRACE_ID" VARCHAR2(50));
comment on column "SYS_DICT"."TRACE_ID" is '跟踪ID';
update "SYS_DICT" set "TRACE_ID"="ID";
alter table "SYS_DICT_VALUE" add ("TRACE_ID" VARCHAR2(50));
comment on column "SYS_DICT_VALUE"."TRACE_ID" is '跟踪ID';
update "SYS_DICT_VALUE" set "TRACE_ID"="ID";
--数据模型
alter table "PRO_SERVICE" add ("TRACE_ID" VARCHAR2(50));
comment on column "PRO_SERVICE"."TRACE_ID" is '跟踪ID';
update "PRO_SERVICE" set "TRACE_ID"="ID";

alter table "PRO_MODEL" add ("TRACE_ID" VARCHAR2(50));
comment on column "PRO_MODEL"."TRACE_ID" is '跟踪ID';
update "PRO_MODEL" set "TRACE_ID"="ID";

alter table "PRO_MODEL_ASSOCIATION" add ("TRACE_ID" VARCHAR2(50));
comment on column "PRO_MODEL_ASSOCIATION"."TRACE_ID" is '跟踪ID';
update "PRO_MODEL_ASSOCIATION" set "TRACE_ID"="ID";

alter table "PRO_MODEL_COL" add ("TRACE_ID" VARCHAR2(50));
comment on column "PRO_MODEL_COL"."TRACE_ID" is '跟踪ID';
update "PRO_MODEL_COL" set "TRACE_ID"="ID";

alter table "PRO_MODEL_INDEX" add ("TRACE_ID" VARCHAR2(50));
comment on column "PRO_MODEL_INDEX"."TRACE_ID" is '跟踪ID';
update "PRO_MODEL_INDEX" set "TRACE_ID"="ID";
-- 组件注册
alter table "SYS_COMPONENT_REGISTER" add ("TRACE_ID" VARCHAR2(50));
comment on column "SYS_COMPONENT_REGISTER"."TRACE_ID" is '跟踪ID';
update "SYS_COMPONENT_REGISTER" set "TRACE_ID"="ID";
-- 系统配置
alter table "SYS_CONFIG" add ("TRACE_ID" VARCHAR2(50));
comment on column "SYS_CONFIG"."TRACE_ID" is '跟踪ID';
update "SYS_CONFIG" set "TRACE_ID"="ID";
-- 业务类型
alter table "REPORT_TYPE" add ("TRACE_ID" VARCHAR2(50));
comment on column "REPORT_TYPE"."TRACE_ID" is '跟踪ID';
update "REPORT_TYPE" set "TRACE_ID"="ID";
-- 报表/仪表盘信息
alter table "REPORT_INFO" add ("TRACE_ID" VARCHAR2(50));
comment on column "REPORT_INFO"."TRACE_ID" is '跟踪ID';
update "REPORT_INFO" set "TRACE_ID"="ID";
-- 接口
alter table "SYS_APP_INTERFACE" add ("TRACE_ID" VARCHAR2(50));
comment on column "SYS_APP_INTERFACE"."TRACE_ID" is '跟踪ID';
update "SYS_APP_INTERFACE" set "TRACE_ID"="ID";
alter table "SYS_APP_INTERFACE_INPUT" add ("TRACE_ID" VARCHAR2(50));
comment on column "SYS_APP_INTERFACE_INPUT"."TRACE_ID" is '跟踪ID';
update "SYS_APP_INTERFACE_INPUT" set "TRACE_ID"="ID";
alter table "SYS_APP_INTERFACE_OUTPUT" add ("TRACE_ID" VARCHAR2(50));
comment on column "SYS_APP_INTERFACE_OUTPUT"."TRACE_ID" is '跟踪ID';
update "SYS_APP_INTERFACE_OUTPUT" set "TRACE_ID"="ID";
-- 大屏
alter table "BLADE_VISUAL" add ("TRACE_ID" VARCHAR2(50));
comment on column "BLADE_VISUAL"."TRACE_ID" is '跟踪ID';
update "BLADE_VISUAL" set "TRACE_ID"="ID";
-- 大屏展示
alter table "BLADE_VISUAL_SHOW" add ("TRACE_ID" VARCHAR2(50));
comment on column "BLADE_VISUAL_SHOW"."TRACE_ID" is '跟踪ID';
update "BLADE_VISUAL_SHOW" set "TRACE_ID"="ID";
-- 数字序列
alter table "CODE_SEQUENCE" add ("TRACE_ID" VARCHAR2(50));
comment on column "CODE_SEQUENCE"."TRACE_ID" is '跟踪ID';
update "CODE_SEQUENCE" set "TRACE_ID"="ID";
-- 编码模板
alter table "CODE_TEMPLATE" add ("TRACE_ID" VARCHAR2(50));
comment on column "CODE_TEMPLATE"."TRACE_ID" is '跟踪ID';
update "CODE_TEMPLATE" set "TRACE_ID"="ID";
alter table "CODE_TEMPLATE_PART" add ("TRACE_ID" VARCHAR2(50));
comment on column "CODE_TEMPLATE_PART"."TRACE_ID" is '跟踪ID';
update "CODE_TEMPLATE_PART" set "TRACE_ID"="ID";

/**
 * 模型添加字段
 * @param null
 * @return
 * @author zuogang
 * @date 2020/7/20 17:33
 */
alter table "PRO_MODEL" add ("IS_FILE_EXIST" NUMBER(1) DEFAULT 0);
comment on column "PRO_MODEL"."IS_FILE_EXIST" is '表单是否存在附件';
alter table "PRO_MODEL" add ("IS_FLOW_EXIST" NUMBER(1) DEFAULT 0);
comment on column "PRO_MODEL"."IS_FLOW_EXIST" is '是否是工作流表单';

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
alter table "PRO_INFO" add ("CREATE_USER" VARCHAR2(50));
comment on column "PRO_INFO"."CREATE_USER" is '创建人';

alter table "PRO_INFO" add ("GROUP_ID" VARCHAR2(50));
comment on column "PRO_INFO"."GROUP_ID" is '集团ID';

update sys_menu set  sort_index=0,sort_path='000001000003000000' where id='3860126718034cc2a7dbd8311bfb3ff1';
update sys_auth set  sort_index=0,sort_path='000001000003000000' where id='3860126718034cc2a7dbd8311bfb3ff1';

update sys_menu set parent_id='ad2b809bf7084d2db69dd56793cb5e1c',parent_name='服务管理',sort_index=1,sort_path='000001000003000001' where id='8f9dec2ca33040e199f9e922f906dc7f';
update sys_auth set parent_id='ad2b809bf7084d2db69dd56793cb5e1c',sort_index=1,sort_path='000001000003000001' where id='8f9dec2ca33040e199f9e922f906dc7f';

alter table "PRO_SERVICE" add ("MAINTAIN_STAFFS" VARCHAR2(2000));
comment on column "PRO_SERVICE"."MAINTAIN_STAFFS" is '维护人员ID(多人使用,分割)';

/**
 * 开发平台
 * @param null
 * @return
 * @author zuogang
 * @date 2020/7/28 9:29
 */

 delete from sys_auth_role where id = '913e1e09cc9f43ac8990a36a493e2071';
 delete from sys_auth_role_v where id ='6abce43f575c4831b08f3ea30cbf4b2e';

ALTER TABLE "PRO_INFO" DROP COLUMN "MAINTAIN_STAFFS";

alter table "PRO_DATASOURCE" add ("IS_MAJOR" INTEGER);
comment on column "PRO_DATASOURCE"."IS_MAJOR" is '是否为主数据源 1是0不是';
ALTER TABLE "PRO_SERVICE" DROP COLUMN "DS_ID";
alter table "PRO_DATASOURCE" add ("SERVICE_ID " VARCHAR2(50));
comment on column "PRO_DATASOURCE"."SERVICE_ID" is '服务ID';
ALTER TABLE "PRO_DATASOURCE"
   ADD CONSTRAINT "FK_DATASOURCE_SERVICE_ID" FOREIGN KEY ("SERVICE_ID") REFERENCES "PRO_SERVICE"("ID")  ON DELETE CASCADE;

alter table "PRO_CHANGELOG_HISTORY" add ("IS_USE_LESS" INTEGER);
comment on column "PRO_CHANGELOG_HISTORY"."IS_USE_LESS" is '替换主数据源后，变为无用数据，0无效，1有效';

alter table "PRO_CHANGELOG_HISTORY" add ("DS_ID" VARCHAR2(50));
comment on column "PRO_CHANGELOG_HISTORY"."DS_ID" is '数据源ID';

alter table "PRO_INTERFACE" add ("DS_ID" VARCHAR2(50));
comment on column "PRO_INTERFACE"."DS_ID" is '数据源ID';
ALTER TABLE "PRO_INTERFACE"
   ADD CONSTRAINT "FK_INTERFACE_DS_ID" FOREIGN KEY ("DS_ID") REFERENCES "PRO_DATASOURCE"("ID")  ON DELETE CASCADE;


/**
 * 大屏消息
 * @param null
 * @return
 * @author zuogang
 * @date 2020/7/29 17:54
 */
CREATE TABLE BLADE_VISUAL_MSG
(
  ID                VARCHAR2(50)                NOT NULL,
  EVENT_NAME        VARCHAR2(50),
  NAME              VARCHAR2(50),
  CODE              VARCHAR2(50),
  DISPLAY_CONTENT   VARCHAR2(2000),
  DISPLAY_MODE      INTEGER,
  PERIOD_MODE       VARCHAR2(50),
  DISPLAY_DURATION  NUMBER(10,0),
  VISUAL_IDS        VARCHAR2(2000),
  NOTICE_MODE       INTEGER,
  APP_ID            VARCHAR2(50),
  TRACE_ID            VARCHAR2(50),
  PERIOD_DATA       VARCHAR2(100)
);

COMMENT ON TABLE BLADE_VISUAL_MSG IS '大屏消息';

COMMENT ON COLUMN BLADE_VISUAL_MSG.ID IS '主键';

COMMENT ON COLUMN BLADE_VISUAL_MSG.NAME IS '名称';

COMMENT ON COLUMN BLADE_VISUAL_MSG.CODE IS '标识';

COMMENT ON COLUMN BLADE_VISUAL_MSG.EVENT_NAME IS '事件名';

COMMENT ON COLUMN BLADE_VISUAL_MSG.DISPLAY_CONTENT IS '显示内容';

COMMENT ON COLUMN BLADE_VISUAL_MSG.DISPLAY_MODE IS '显示模式（0即时触发，1定时触发）';

COMMENT ON COLUMN BLADE_VISUAL_MSG.PERIOD_MODE IS '周期模式';

COMMENT ON COLUMN BLADE_VISUAL_MSG.DISPLAY_DURATION IS '显示时长';

COMMENT ON COLUMN BLADE_VISUAL_MSG.VISUAL_IDS IS '大屏Ids(以英文逗号分隔)';

COMMENT ON COLUMN BLADE_VISUAL_MSG.NOTICE_MODE IS '通知模式(0通知，1警告)';

COMMENT ON COLUMN BLADE_VISUAL_MSG.APP_ID IS '应用ID';

COMMENT ON COLUMN BLADE_VISUAL_MSG.TRACE_ID IS '跟踪ID';

COMMENT ON COLUMN BLADE_VISUAL_MSG.PERIOD_DATA IS '周期数据';



ALTER TABLE BLADE_VISUAL_MSG ADD (
  CONSTRAINT BLADE_VISUAL_MSG_PK
  PRIMARY KEY
  (ID)
  ENABLE VALIDATE);

ALTER TABLE BLADE_VISUAL_MSG ADD (
  CONSTRAINT FK_VISUAL_MSG_APP_ID
  FOREIGN KEY (APP_ID)
  REFERENCES SYS_GROUP_APP (ID)
  ON DELETE CASCADE
  ENABLE VALIDATE);

/**
 * 大屏通知菜单添加
 * @param null
 * @return
 * @author zuogang
 * @date 2020/7/31 9:30
 */
insert into "SYS_AUTH" ("ID","NAME","APP_ID","REMARK","SORT_INDEX","PARENT_ID","CREATE_USER","CREATE_TIME","UPDATE_TIME","SORT_PATH","IS_ORG_ADMIN","IS_USER_GROUP_ADMIN","CODE","DATA_VERSION") values ('dapingxiaoxi4d37929abd4ad9a1dgth', '大屏通知', 'platform', null, 9, 'twerba3204344d37929abd4ad9a1dfgh', null, null, null, '00060009', 0, 0, 'screenNotification.views.ScreenNotification', 0);
insert into "SYS_MENU" ("ID","NAME","PARENT_ID","APP_ID","PARENT_NAME","URL","TYPE","ICON","AUTH_ID","BTN_AUTH","IS_LEAF","SORT_INDEX","ALL_ORDER","SORT_PATH","OPEN_STYLE","CLOSE_NOTICE","CREATE_USER","CREATE_TIME","UPDATE_TIME","LEAF","MENU_AUTH","REMARK","DATA_VERSION","IS_IFRAME") values ('dapingxiaoxi4d37929abd4ad9a1dgth', '大屏通知', 'twerba3204344d37929abd4ad9a1dfgh', 'platform', '数据展示', 'screenNotification/views/ScreenNotification', 1, 'iconfont icon-message', 'dapingxiaoxi4d37929abd4ad9a1dgth', null, null, 9, null, '00060009', null, null, null, null, null, null, null, null, 0, 0);
insert into "SYS_AUTH_ROLE" ("ID","AUTH_ID","ROLE_ID","SIGN","IS_REVOKE","CREATE_USER","CREATE_TIME","UPDATE_TIME","REMARK") values ('dapingxiaoxi4d37929abd4ad9a1dgth', 'dapingxiaoxi4d37929abd4ad9a1dgth', 'appadmin', null, 0, null, null, null, null);
insert into "SYS_AUTH_ROLE_V" ("ID","AUTH_ID","ROLE_ID","SIGN","IS_REVOKE","CREATE_USER","CREATE_TIME","UPDATE_TIME","REMARK","LV_ID") values ('dapingxiaoxi4d37929abd4ad9a1dgth', 'dapingxiaoxi4d37929abd4ad9a1dgth', 'appadmin', null, 0, null, null, null, null, 'appadmin');


/**
 * 开发平台 接口管理 出参 键 实体
 * @param null
 * @return
 * @author zuogang
 * @date 2020/8/3 8:04
 */
alter table "PRO_OUTPUT_PARAMS" add ("PARAM_ITEM" VARCHAR2(50));
comment on column "PRO_OUTPUT_PARAMS"."PARAM_ITEM" is '键 实体';

alter table "SYS_APP_INTERFACE_OUTPUT" add ("PARAM_ITEM" VARCHAR2(50));
comment on column "SYS_APP_INTERFACE_OUTPUT"."PARAM_ITEM" is '键 实体';
/**
  2019/10/08
 */
alter table "JARIACEPLAT"."SYS_AUTH_USER" add column("APP_ID" VARCHAR2(50));
comment on column "JARIACEPLAT"."SYS_AUTH_USER"."APP_ID" is '应用ID';
alter table "JARIACEPLAT"."SYS_AUTH_USER_V" add column("APP_ID" VARCHAR2(50));
comment on column "JARIACEPLAT"."SYS_AUTH_USER_V"."APP_ID" is '应用ID';

insert into "JARIACEPLAT"."SYS_AUTH" ("ID","NAME","APP_ID","REMARK","SORT_INDEX","PARENT_ID","CREATE_USER","CREATE_TIME","UPDATE_TIME","SORT_PATH","IS_ORG_ADMIN","IS_USER_GROUP_ADMIN","CODE","DATA_VERSION") values ('tyuub3ec3d6e483b86c4923ea64cfwe5', '角色用户授予', 'platform', null, 5, '11erb3ec3d6e483b86c4923ea64cfdd0', null, null, null, '000200090005', 0, 0, 'roleManage.roleGrant', 0);

insert into "JARIACEPLAT"."SYS_MENU" ("ID","NAME","PARENT_ID","APP_ID","PARENT_NAME","URL","TYPE","ICON","AUTH_ID","BTN_AUTH","IS_LEAF","SORT_INDEX","ALL_ORDER","SORT_PATH","OPEN_STYLE","CLOSE_NOTICE","CREATE_USER","CREATE_TIME","UPDATE_TIME","LEAF","MENU_AUTH","REMARK","DATA_VERSION","IS_IFRAME") values ('tyuub3ec3d6e483b86c4923ea64cfwe5', '角色用户授予', '11erb3ec3d6e483b86c4923ea64cfdd0', 'platform', '授权管理', 'roleManage/views/RoleGrant', 1, 'iconfont icon-setting-permissions', 'tyuub3ec3d6e483b86c4923ea64cfwe5', null, null, 5, null, '000200090005', null, null, null, null, null, null, null, null, 0, 0);

insert into "JARIACEPLAT"."SYS_AUTH_ROLE" ("ID","AUTH_ID","ROLE_ID","SIGN","IS_REVOKE","CREATE_USER","CREATE_TIME","UPDATE_TIME","REMARK") values ('12323fe2ef32403dbf9dc989d0651233', 'tyuub3ec3d6e483b86c4923ea64cfwe5', 'appadmin', null, 0, null, null, null, null);
insert into "JARIACEPLAT"."SYS_AUTH_ROLE" ("ID","AUTH_ID","ROLE_ID","SIGN","IS_REVOKE","CREATE_USER","CREATE_TIME","UPDATE_TIME","REMARK") values ('12323fe2ef32403dbf9dc989d0651234', 'tyuub3ec3d6e483b86c4923ea64cfwe5', 'appsec', null, 0, null, null, null, null);

insert into "JARIACEPLAT"."SYS_AUTH_ROLE_V" ("ID","AUTH_ID","ROLE_ID","SIGN","IS_REVOKE","CREATE_USER","CREATE_TIME","UPDATE_TIME","REMARK","LV_ID") values ('3339dc7b6ba04f1fa8e4cc36be871222', 'tyuub3ec3d6e483b86c4923ea64cfwe5', 'appadmin', null, 0, null, null, null, null, 'appadmin');
insert into "JARIACEPLAT"."SYS_AUTH_ROLE_V" ("ID","AUTH_ID","ROLE_ID","SIGN","IS_REVOKE","CREATE_USER","CREATE_TIME","UPDATE_TIME","REMARK","LV_ID") values ('4569dc7b6ba04f1fa8e4cc36be871234', 'tyuub3ec3d6e483b86c4923ea64cfwe5', 'appsec', null, 0, null, null, null, null, 'appsec');

insert into "JARIACEPLAT"."SYS_AUTH_ROLE" ("ID","AUTH_ID","ROLE_ID","SIGN","IS_REVOKE","CREATE_USER","CREATE_TIME","UPDATE_TIME","REMARK") values ('12323fe2ef32403dbf9dc989d0651235', '04d7893c4a4d4345b18b0789af13753f', 'admin', null, 0, null, null, null, null);
insert into "JARIACEPLAT"."SYS_AUTH_ROLE" ("ID","AUTH_ID","ROLE_ID","SIGN","IS_REVOKE","CREATE_USER","CREATE_TIME","UPDATE_TIME","REMARK") values ('12323fe2ef32403dbf9dc989d0651236', '04d7893c4a4d4345b18b0789af13753f', 'groupadmin', null, 0, null, null, null, null);
insert into "JARIACEPLAT"."SYS_AUTH_ROLE" ("ID","AUTH_ID","ROLE_ID","SIGN","IS_REVOKE","CREATE_USER","CREATE_TIME","UPDATE_TIME","REMARK") values ('12323fe2ef32403dbf9dc989d0651237', '04d7893c4a4d4345b18b0789af13753f', 'appadmin', null, 0, null, null, null, null);
insert into "JARIACEPLAT"."SYS_AUTH_ROLE" ("ID","AUTH_ID","ROLE_ID","SIGN","IS_REVOKE","CREATE_USER","CREATE_TIME","UPDATE_TIME","REMARK") values ('12323fe2ef32403dbf9dc989d0651238', '4c8c4bc4726b40b7a570fa1d2ba49f47', 'admin', null, 0, null, null, null, null);
insert into "JARIACEPLAT"."SYS_AUTH_ROLE" ("ID","AUTH_ID","ROLE_ID","SIGN","IS_REVOKE","CREATE_USER","CREATE_TIME","UPDATE_TIME","REMARK") values ('12323fe2ef32403dbf9dc989d0651239', '4c8c4bc4726b40b7a570fa1d2ba49f47', 'groupadmin', null, 0, null, null, null, null);
insert into "JARIACEPLAT"."SYS_AUTH_ROLE" ("ID","AUTH_ID","ROLE_ID","SIGN","IS_REVOKE","CREATE_USER","CREATE_TIME","UPDATE_TIME","REMARK") values ('12323fe2ef32403dbf9dc989d0651240', '4c8c4bc4726b40b7a570fa1d2ba49f47', 'appadmin', null, 0, null, null, null, null);


insert into "JARIACEPLAT"."SYS_AUTH_ROLE_V" ("ID","AUTH_ID","ROLE_ID","SIGN","IS_REVOKE","CREATE_USER","CREATE_TIME","UPDATE_TIME","REMARK","LV_ID") values ('1569dc7b6ba04f1fa8e4cc36be871231', '04d7893c4a4d4345b18b0789af13753f', 'admin', null, 0, null, null, null, null, 'admin');
insert into "JARIACEPLAT"."SYS_AUTH_ROLE_V" ("ID","AUTH_ID","ROLE_ID","SIGN","IS_REVOKE","CREATE_USER","CREATE_TIME","UPDATE_TIME","REMARK","LV_ID") values ('2569dc7b6ba04f1fa8e4cc36be871232', '04d7893c4a4d4345b18b0789af13753f', 'groupadmin', null, 0, null, null, null, null, 'groupadmin');
insert into "JARIACEPLAT"."SYS_AUTH_ROLE_V" ("ID","AUTH_ID","ROLE_ID","SIGN","IS_REVOKE","CREATE_USER","CREATE_TIME","UPDATE_TIME","REMARK","LV_ID") values ('3569dc7b6ba04f1fa8e4cc36be871233', '04d7893c4a4d4345b18b0789af13753f', 'appadmin', null, 0, null, null, null, null, 'appadmin');
insert into "JARIACEPLAT"."SYS_AUTH_ROLE_V" ("ID","AUTH_ID","ROLE_ID","SIGN","IS_REVOKE","CREATE_USER","CREATE_TIME","UPDATE_TIME","REMARK","LV_ID") values ('7569dc7b6ba04f1fa8e4cc36be871237', '4c8c4bc4726b40b7a570fa1d2ba49f47', 'admin', null, 0, null, null, null, null, 'admin');
insert into "JARIACEPLAT"."SYS_AUTH_ROLE_V" ("ID","AUTH_ID","ROLE_ID","SIGN","IS_REVOKE","CREATE_USER","CREATE_TIME","UPDATE_TIME","REMARK","LV_ID") values ('8569dc7b6ba04f1fa8e4cc36be871238', '4c8c4bc4726b40b7a570fa1d2ba49f47', 'groupadmin', null, 0, null, null, null, null, 'groupadmin');
insert into "JARIACEPLAT"."SYS_AUTH_ROLE_V" ("ID","AUTH_ID","ROLE_ID","SIGN","IS_REVOKE","CREATE_USER","CREATE_TIME","UPDATE_TIME","REMARK","LV_ID") values ('5569dc7b6ba04f1fa8e4cc36be871235', '4c8c4bc4726b40b7a570fa1d2ba49f47', 'appadmin', null, 0, null, null, null, null, 'appadmin');

/**
 2019/10/10:岗位管理，职务管理
 */
insert into "JARIACEPLAT"."SYS_AUTH" ("ID","NAME","APP_ID","REMARK","SORT_INDEX","PARENT_ID","CREATE_USER","CREATE_TIME","UPDATE_TIME","SORT_PATH","IS_ORG_ADMIN","IS_USER_GROUP_ADMIN","CODE","DATA_VERSION") values ('a16344b1790e41e98615f6cd19587572', '岗位管理', 'platform', null, 20, 'bc316279a4a9416a8e20019821e640af', null, null, null, '00010020', 0, 0, 'orgSructureManage.PostManage', 0);
insert into "JARIACEPLAT"."SYS_AUTH" ("ID","NAME","APP_ID","REMARK","SORT_INDEX","PARENT_ID","CREATE_USER","CREATE_TIME","UPDATE_TIME","SORT_PATH","IS_ORG_ADMIN","IS_USER_GROUP_ADMIN","CODE","DATA_VERSION") values ('a16344b1790e41e98615f6cd1958757r', '职务管理', 'platform', null, 21, 'bc316279a4a9416a8e20019821e640af', null, null, null, '00010021', 0, 0, 'orgSructureManage.JobManage', 0);

insert into "JARIACEPLAT"."SYS_MENU" ("ID","NAME","PARENT_ID","APP_ID","PARENT_NAME","URL","TYPE","ICON","AUTH_ID","BTN_AUTH","IS_LEAF","SORT_INDEX","ALL_ORDER","SORT_PATH","OPEN_STYLE","CLOSE_NOTICE","CREATE_USER","CREATE_TIME","UPDATE_TIME","LEAF","MENU_AUTH","REMARK","DATA_VERSION","IS_IFRAME") values ('a16344b1790e41e98615f6cd19587572', '岗位管理', 'bc316279a4a9416a8e20019821e640af', 'platform', '组织架构管理', 'orgSructureManage/views/PostManage', 1, 'iconfont icon-users-cog', 'a16344b1790e41e98615f6cd19587571', null, null, 20, null, '00010020', null, null, null, null, null, null, null, null, 0, 0);
insert into "JARIACEPLAT"."SYS_MENU" ("ID","NAME","PARENT_ID","APP_ID","PARENT_NAME","URL","TYPE","ICON","AUTH_ID","BTN_AUTH","IS_LEAF","SORT_INDEX","ALL_ORDER","SORT_PATH","OPEN_STYLE","CLOSE_NOTICE","CREATE_USER","CREATE_TIME","UPDATE_TIME","LEAF","MENU_AUTH","REMARK","DATA_VERSION","IS_IFRAME") values ('a16344b1790e41e98615f6cd1958757r', '职务管理', 'bc316279a4a9416a8e20019821e640af', 'platform', '组织架构管理', 'orgSructureManage/views/JobManage', 1, 'iconfont icon-users-cog', 'a16344b1790e41e98615f6cd19587571', null, null, 21, null, '00010021', null, null, null, null, null, null, null, null, 0, 0);

insert into "JARIACEPLAT"."SYS_AUTH_ROLE" ("ID","AUTH_ID","ROLE_ID","SIGN","IS_REVOKE","CREATE_USER","CREATE_TIME","UPDATE_TIME","REMARK") values ('12323fe2ef32403dbf9dc989d0651241', 'a16344b1790e41e98615f6cd19587572', 'groupadmin', null, 0, null, null, null, null);
insert into "JARIACEPLAT"."SYS_AUTH_ROLE" ("ID","AUTH_ID","ROLE_ID","SIGN","IS_REVOKE","CREATE_USER","CREATE_TIME","UPDATE_TIME","REMARK") values ('12323fe2ef32403dbf9dc989d0651242', 'a16344b1790e41e98615f6cd1958757r', 'groupadmin', null, 0, null, null, null, null);

insert into "JARIACEPLAT"."SYS_AUTH_ROLE_V" ("ID","AUTH_ID","ROLE_ID","SIGN","IS_REVOKE","CREATE_USER","CREATE_TIME","UPDATE_TIME","REMARK","LV_ID") values ('7869dc7b6ba04f1fa8e4cc36be871278', 'a16344b1790e41e98615f6cd19587572', 'groupadmin', null, 0, null, null, null, null, 'groupadmin');
insert into "JARIACEPLAT"."SYS_AUTH_ROLE_V" ("ID","AUTH_ID","ROLE_ID","SIGN","IS_REVOKE","CREATE_USER","CREATE_TIME","UPDATE_TIME","REMARK","LV_ID") values ('8969dc7b6ba04f1fa8e4cc36be871289', 'a16344b1790e41e98615f6cd1958757r', 'groupadmin', null, 0, null, null, null, null, 'groupadmin');

/**
 2019/10/11: 人员证件类型管理 删除 业务单元主键
 */
alter table bd_person_id_type DROP CONSTRAINT FK_BD_PERSON_ID_TYPE_ORG_ID;
alter table bd_person_id_type DROP CONSTRAINT UK_BD_PERSON_ID_TYPE_NAME;
alter table bd_person_id_type DROP organization_id;

/**
 2019-10-14
 */
alter table ORG_ORGANIZATION_V_TYPE add column(DATA_VERSION NUMBER(1,0) DEFAULT 0);
comment on column ORG_ORGANIZATION_V_TYPE.DATA_VERSION is '数据版本';

/**
 2019-10-15
 */
alter table "FILE_CONFIGURATION" add column("IS_ENABLE_EVT_FILE_DELETING" NUMBER(1, 0));
alter table "FILE_CONFIGURATION" add column("IS_ENABLE_EVT_FILE_DOWNLOADING" NUMBER(1, 0));
alter table "FILE_CONFIGURATION" add column("IS_ENABLE_EVT_FILE_UPLOADING" NUMBER(1, 0));
alter table "FILE_CONFIGURATION" add column("IS_ENABLE_EVT_FILE_UPLOADED" NUMBER(1, 0));
comment on column "FILE_CONFIGURATION"."IS_ENABLE_EVT_FILE_DELETING" is '是否启用事件文件删除前';
comment on column "FILE_CONFIGURATION"."IS_ENABLE_EVT_FILE_DOWNLOADING" is '是否启用事件文件下载前';
comment on column "FILE_CONFIGURATION"."IS_ENABLE_EVT_FILE_UPLOADING" is '是否启用事件文件上传前';
comment on column "FILE_CONFIGURATION"."IS_ENABLE_EVT_FILE_UPLOADED" is '是否启用事件文件上传后（不影响文件上传到服务器）';
/**
2019/10/16: 工作日历菜单放在配置管理下
 */
UPDATE SYS_MENU SET PARENT_ID='11316279a4a9416a8e20019821e640af' , PARENT_NAME='配置管理' , SORT_INDEX=10 , SORT_PATH='00030010' WHERE ID='11116279a4a9416a8e20019821e61111';
UPDATE SYS_AUTH SET PARENT_ID='11316279a4a9416a8e20019821e640af' ,  SORT_INDEX=10 , SORT_PATH='00030010' WHERE ID='11116279a4a9416a8e20019821e61111';
alter table "FILE_CONFIGURATION" add column("IS_ENABLE_EVT_FILE_DOWNLOADED" NUMBER(1, 0));
comment on column "FILE_CONFIGURATION"."IS_ENABLE_EVT_FILE_DOWNLOADED" is '是否启用文件下载后事件';

/**
  2019/10/18:审计日志菜单序号修正
 */
update sys_menu set name='审计日志',url='auditLogManage/views/AuditLog',icon='iconfont icon-clipboard-check' where id = '04d7893c4a4d4345b18b0789af13753f';

update sys_auth set name='审计日志',code='platForm.auditLogManage.AuditLog' where id = '04d7893c4a4d4345b18b0789af13753f';

delete from sys_menu where id='4c8c4bc4726b40b7a570fa1d2ba49f47';
delete from sys_auth where id='4c8c4bc4726b40b7a570fa1d2ba49f47';
delete from sys_auth_role_v where auth_id ='4c8c4bc4726b40b7a570fa1d2ba49f47';

/**
  2019/10/21:添加报表菜单及权限
 */
insert into "JARIACEPLAT"."SYS_AUTH" ("ID","NAME","APP_ID","REMARK","SORT_INDEX","PARENT_ID","CREATE_USER","CREATE_TIME","UPDATE_TIME","SORT_PATH","IS_ORG_ADMIN","IS_USER_GROUP_ADMIN","CODE","DATA_VERSION") values ('twerba3204344d37929abd4ad9a1dfgh', '数据展示', 'platform', null, 6, '0', null, null, null, '0006', 0, 0, null, 0);
insert into "JARIACEPLAT"."SYS_AUTH" ("ID","NAME","APP_ID","REMARK","SORT_INDEX","PARENT_ID","CREATE_USER","CREATE_TIME","UPDATE_TIME","SORT_PATH","IS_ORG_ADMIN","IS_USER_GROUP_ADMIN","CODE","DATA_VERSION") values ('1234ba3204344d37929abd4ad9a1dgth', '业务类型', 'platform', null, 1, 'twerba3204344d37929abd4ad9a1dfgh', null, null, null, '00060001', 0, 0, 'reportType.ReportType', 0);
insert into "JARIACEPLAT"."SYS_AUTH" ("ID","NAME","APP_ID","REMARK","SORT_INDEX","PARENT_ID","CREATE_USER","CREATE_TIME","UPDATE_TIME","SORT_PATH","IS_ORG_ADMIN","IS_USER_GROUP_ADMIN","CODE","DATA_VERSION") values ('2234ba3204344d37929abd4ad9a1dgth', '报表设计', 'platform', null, 2, 'twerba3204344d37929abd4ad9a1dfgh', null, null, null, '00060002', 0, 0, 'reportInfo.ReportList', 0);
insert into "JARIACEPLAT"."SYS_AUTH" ("ID","NAME","APP_ID","REMARK","SORT_INDEX","PARENT_ID","CREATE_USER","CREATE_TIME","UPDATE_TIME","SORT_PATH","IS_ORG_ADMIN","IS_USER_GROUP_ADMIN","CODE","DATA_VERSION") values ('3234ba3204344d37929abd4ad9a1dgth', '报表浏览', 'platform', null, 3, 'twerba3204344d37929abd4ad9a1dfgh', null, null, null, '00060003', 0, 0, 'reportShow.ReportShow', 0);
insert into "JARIACEPLAT"."SYS_AUTH" ("ID","NAME","APP_ID","REMARK","SORT_INDEX","PARENT_ID","CREATE_USER","CREATE_TIME","UPDATE_TIME","SORT_PATH","IS_ORG_ADMIN","IS_USER_GROUP_ADMIN","CODE","DATA_VERSION") values ('4234ba3204344d37929abd4ad9a1dgth', '仪表盘设计', 'platform', null, 4, 'twerba3204344d37929abd4ad9a1dfgh', null, null, null, '00060004', 0, 0, 'reportDsInfo.ReportList', 0);
insert into "JARIACEPLAT"."SYS_AUTH" ("ID","NAME","APP_ID","REMARK","SORT_INDEX","PARENT_ID","CREATE_USER","CREATE_TIME","UPDATE_TIME","SORT_PATH","IS_ORG_ADMIN","IS_USER_GROUP_ADMIN","CODE","DATA_VERSION") values ('5234ba3204344d37929abd4ad9a1dgth', '仪表盘浏览', 'platform', null, 5, 'twerba3204344d37929abd4ad9a1dfgh', null, null, null, '00060005', 0, 0, 'reportDsShow.ReportDsShow', 0);

insert into "JARIACEPLAT"."SYS_MENU" ("ID","NAME","PARENT_ID","APP_ID","PARENT_NAME","URL","TYPE","ICON","AUTH_ID","BTN_AUTH","IS_LEAF","SORT_INDEX","ALL_ORDER","SORT_PATH","OPEN_STYLE","CLOSE_NOTICE","CREATE_USER","CREATE_TIME","UPDATE_TIME","LEAF","MENU_AUTH","REMARK","DATA_VERSION","IS_IFRAME") values ('twerba3204344d37929abd4ad9a1dfgh', '数据展示', '0', 'platform', '一级菜单', null, 0, 'fa fa-bar-chart', 'twerba3204344d37929abd4ad9a1dfgh', null, null, 6, null, '0006', null, null, null, null, null, null, null, null, 0, 0);
insert into "JARIACEPLAT"."SYS_MENU" ("ID","NAME","PARENT_ID","APP_ID","PARENT_NAME","URL","TYPE","ICON","AUTH_ID","BTN_AUTH","IS_LEAF","SORT_INDEX","ALL_ORDER","SORT_PATH","OPEN_STYLE","CLOSE_NOTICE","CREATE_USER","CREATE_TIME","UPDATE_TIME","LEAF","MENU_AUTH","REMARK","DATA_VERSION","IS_IFRAME") values ('1234ba3204344d37929abd4ad9a1dgth', '业务类型', 'twerba3204344d37929abd4ad9a1dfgh', 'platform', '数据展示', 'reportType/views/ReportType', 1, 'fa fa-align-justify', '1234ba3204344d37929abd4ad9a1dgth', null, null, 1, null, '00060001', null, null, null, null, null, null, null, null, 0, 0);
insert into "JARIACEPLAT"."SYS_MENU" ("ID","NAME","PARENT_ID","APP_ID","PARENT_NAME","URL","TYPE","ICON","AUTH_ID","BTN_AUTH","IS_LEAF","SORT_INDEX","ALL_ORDER","SORT_PATH","OPEN_STYLE","CLOSE_NOTICE","CREATE_USER","CREATE_TIME","UPDATE_TIME","LEAF","MENU_AUTH","REMARK","DATA_VERSION","IS_IFRAME") values ('2234ba3204344d37929abd4ad9a1dgth', '报表设计', 'twerba3204344d37929abd4ad9a1dfgh', 'platform', '数据展示', 'reportInfo/views/ReportList', 1, 'fa fa-area-chart', '2234ba3204344d37929abd4ad9a1dgth', null, null, 2, null, '00060002', null, null, null, null, null, null, null, null, 0, 0);
insert into "JARIACEPLAT"."SYS_MENU" ("ID","NAME","PARENT_ID","APP_ID","PARENT_NAME","URL","TYPE","ICON","AUTH_ID","BTN_AUTH","IS_LEAF","SORT_INDEX","ALL_ORDER","SORT_PATH","OPEN_STYLE","CLOSE_NOTICE","CREATE_USER","CREATE_TIME","UPDATE_TIME","LEAF","MENU_AUTH","REMARK","DATA_VERSION","IS_IFRAME") values ('3234ba3204344d37929abd4ad9a1dgth', '报表浏览', 'twerba3204344d37929abd4ad9a1dfgh', 'platform', '数据展示', 'reportShow/views/ReportShow', 1, 'fa fa-list', '3234ba3204344d37929abd4ad9a1dgth', null, null, 3, null, '00060003', null, null, null, null, null, null, null, null, 0, 0);
insert into "JARIACEPLAT"."SYS_MENU" ("ID","NAME","PARENT_ID","APP_ID","PARENT_NAME","URL","TYPE","ICON","AUTH_ID","BTN_AUTH","IS_LEAF","SORT_INDEX","ALL_ORDER","SORT_PATH","OPEN_STYLE","CLOSE_NOTICE","CREATE_USER","CREATE_TIME","UPDATE_TIME","LEAF","MENU_AUTH","REMARK","DATA_VERSION","IS_IFRAME") values ('4234ba3204344d37929abd4ad9a1dgth', '仪表盘设计', 'twerba3204344d37929abd4ad9a1dfgh', 'platform', '数据展示', 'reportDsInfo/views/ReportDsList', 1, 'fa fa-pie-chart', '4234ba3204344d37929abd4ad9a1dgth', null, null, 4, null, '00060004', null, null, null, null, null, null, null, null, 0, 0);
insert into "JARIACEPLAT"."SYS_MENU" ("ID","NAME","PARENT_ID","APP_ID","PARENT_NAME","URL","TYPE","ICON","AUTH_ID","BTN_AUTH","IS_LEAF","SORT_INDEX","ALL_ORDER","SORT_PATH","OPEN_STYLE","CLOSE_NOTICE","CREATE_USER","CREATE_TIME","UPDATE_TIME","LEAF","MENU_AUTH","REMARK","DATA_VERSION","IS_IFRAME") values ('5234ba3204344d37929abd4ad9a1dgth', '仪表盘浏览', 'twerba3204344d37929abd4ad9a1dfgh', 'platform', '数据展示', 'reportDsShow/views/ReportDsShow', 1, 'fa fa-list-ul', '5234ba3204344d37929abd4ad9a1dgth', null, null, 5, null, '00060005', null, null, null, null, null, null, null, null, 0, 0);


insert into "JARIACEPLAT"."SYS_AUTH_ROLE" ("ID","AUTH_ID","ROLE_ID","SIGN","IS_REVOKE","CREATE_USER","CREATE_TIME","UPDATE_TIME","REMARK") values ('12323fe2ef32403dbf9dc989d0651243', 'twerba3204344d37929abd4ad9a1dfgh', 'appadmin', null, 0, null, null, null, null);
insert into "JARIACEPLAT"."SYS_AUTH_ROLE" ("ID","AUTH_ID","ROLE_ID","SIGN","IS_REVOKE","CREATE_USER","CREATE_TIME","UPDATE_TIME","REMARK") values ('12323fe2ef32403dbf9dc989d0651244', '1234ba3204344d37929abd4ad9a1dgth', 'appadmin', null, 0, null, null, null, null);
insert into "JARIACEPLAT"."SYS_AUTH_ROLE" ("ID","AUTH_ID","ROLE_ID","SIGN","IS_REVOKE","CREATE_USER","CREATE_TIME","UPDATE_TIME","REMARK") values ('12323fe2ef32403dbf9dc989d0651245', '2234ba3204344d37929abd4ad9a1dgth', 'appadmin', null, 0, null, null, null, null);
insert into "JARIACEPLAT"."SYS_AUTH_ROLE" ("ID","AUTH_ID","ROLE_ID","SIGN","IS_REVOKE","CREATE_USER","CREATE_TIME","UPDATE_TIME","REMARK") values ('12323fe2ef32403dbf9dc989d0651246', '3234ba3204344d37929abd4ad9a1dgth', 'appadmin', null, 0, null, null, null, null);
insert into "JARIACEPLAT"."SYS_AUTH_ROLE" ("ID","AUTH_ID","ROLE_ID","SIGN","IS_REVOKE","CREATE_USER","CREATE_TIME","UPDATE_TIME","REMARK") values ('12323fe2ef32403dbf9dc989d0651247', '4234ba3204344d37929abd4ad9a1dgth', 'appadmin', null, 0, null, null, null, null);
insert into "JARIACEPLAT"."SYS_AUTH_ROLE" ("ID","AUTH_ID","ROLE_ID","SIGN","IS_REVOKE","CREATE_USER","CREATE_TIME","UPDATE_TIME","REMARK") values ('12323fe2ef32403dbf9dc989d0651248', '5234ba3204344d37929abd4ad9a1dgth', 'appadmin', null, 0, null, null, null, null);


insert into "JARIACEPLAT"."SYS_AUTH_ROLE_V" ("ID","AUTH_ID","ROLE_ID","SIGN","IS_REVOKE","CREATE_USER","CREATE_TIME","UPDATE_TIME","REMARK","LV_ID") values ('12323fe2ef32403dbf9dc989d0651243', 'twerba3204344d37929abd4ad9a1dfgh', 'appadmin', null, 0, null, null, null, null, 'appadmin');
insert into "JARIACEPLAT"."SYS_AUTH_ROLE_V" ("ID","AUTH_ID","ROLE_ID","SIGN","IS_REVOKE","CREATE_USER","CREATE_TIME","UPDATE_TIME","REMARK","LV_ID") values ('12323fe2ef32403dbf9dc989d0651244', '1234ba3204344d37929abd4ad9a1dgth', 'appadmin', null, 0, null, null, null, null, 'appadmin');
insert into "JARIACEPLAT"."SYS_AUTH_ROLE_V" ("ID","AUTH_ID","ROLE_ID","SIGN","IS_REVOKE","CREATE_USER","CREATE_TIME","UPDATE_TIME","REMARK","LV_ID") values ('12323fe2ef32403dbf9dc989d0651245', '2234ba3204344d37929abd4ad9a1dgth', 'appadmin', null, 0, null, null, null, null, 'appadmin');
insert into "JARIACEPLAT"."SYS_AUTH_ROLE_V" ("ID","AUTH_ID","ROLE_ID","SIGN","IS_REVOKE","CREATE_USER","CREATE_TIME","UPDATE_TIME","REMARK","LV_ID") values ('12323fe2ef32403dbf9dc989d0651246', '3234ba3204344d37929abd4ad9a1dgth', 'appadmin', null, 0, null, null, null, null, 'appadmin');
insert into "JARIACEPLAT"."SYS_AUTH_ROLE_V" ("ID","AUTH_ID","ROLE_ID","SIGN","IS_REVOKE","CREATE_USER","CREATE_TIME","UPDATE_TIME","REMARK","LV_ID") values ('12323fe2ef32403dbf9dc989d0651247', '4234ba3204344d37929abd4ad9a1dgth', 'appadmin', null, 0, null, null, null, null, 'appadmin');
insert into "JARIACEPLAT"."SYS_AUTH_ROLE_V" ("ID","AUTH_ID","ROLE_ID","SIGN","IS_REVOKE","CREATE_USER","CREATE_TIME","UPDATE_TIME","REMARK","LV_ID") values ('12323fe2ef32403dbf9dc989d0651248', '5234ba3204344d37929abd4ad9a1dgth', 'appadmin', null, 0, null, null, null, null, 'appadmin');


alter table "JARIACEPLAT"."SYS_USER_ROLE" add column("APP_ID" VARCHAR2(50));
comment on column "JARIACEPLAT"."SYS_USER_ROLE"."APP_ID" is '应用ID';

/**
 2019/10/22
 */
alter table "JARIACEPLAT"."SYS_USER_ROLE" add column("IS_ACTIVATED" NUMBER(1,0) DEFAULT 0);
comment on column "JARIACEPLAT"."SYS_USER_ROLE"."IS_ACTIVATED" is '是否被激活，0没有，1是';

alter table "JARIACEPLAT"."SYS_USER_ROLE_LV" add column("APP_ID" VARCHAR2(50));
comment on column "JARIACEPLAT"."SYS_USER_ROLE_LV"."APP_ID" is '应用ID';

/**
2019/10/23
 */
delete from "JARIACEPLAT"."SYS_AUTH" where ID IN ('8d4d2caee7f548889988da2747dfa114','8d4d2caee7f548889988da2747dfa111','8d4d2caee7f548889988da2747dfa112','8d4d2caee7f548889988da2747dfa113');
delete from "JARIACEPLAT"."SYS_AUTH_ROLE_V" where AUTH_ID IN ('8d4d2caee7f548889988da2747dfa114','8d4d2caee7f548889988da2747dfa111','8d4d2caee7f548889988da2747dfa112','8d4d2caee7f548889988da2747dfa113');

insert into "JARIACEPLAT"."SYS_AUTH_ROLE" ("ID","AUTH_ID","ROLE_ID","SIGN","IS_REVOKE","CREATE_USER","CREATE_TIME","UPDATE_TIME","REMARK") values ('vbnm3fe2ef32403dbf9dc989d06vbnm1', '8d4d2caee7f548889988da2747dfaa4a', 'appsec', null, 0, null, null, null, null);
insert into "JARIACEPLAT"."SYS_AUTH_ROLE_V" ("ID","AUTH_ID","ROLE_ID","SIGN","IS_REVOKE","CREATE_USER","CREATE_TIME","UPDATE_TIME","REMARK","LV_ID") values ('6787ghjk-0733-0000-B229-5ED5rftyEA82', '8d4d2caee7f548889988da2747dfaa4a', 'appsec', null, 0, null, null, null, null, 'appsec');

/**
 2019/10/24
 */
delete from "JARIACEPLAT"."SYS_MENU" where ID = '33336279a4a9416a8e20019821e63333';
delete from "JARIACEPLAT"."SYS_AUTH" where ID = '33336279a4a9416a8e20019821e63333';
delete from "JARIACEPLAT"."SYS_AUTH_ROLE_V" where AUTH_ID = '33336279a4a9416a8e20019821e63333';

insert into "JARIACEPLAT"."SYS_AUTH" ("ID","NAME","APP_ID","REMARK","SORT_INDEX","PARENT_ID","CREATE_USER","CREATE_TIME","UPDATE_TIME","SORT_PATH","IS_ORG_ADMIN","IS_USER_GROUP_ADMIN","CODE","DATA_VERSION") values ('34116279a4a9416a8e20019821e61341', '消息配置', 'platform', null, 9, '11316279a4a9416a8e20019821e640af', null, null, null, '00030009', 0, 0, null, 0);
insert into "JARIACEPLAT"."SYS_AUTH" ("ID","NAME","APP_ID","REMARK","SORT_INDEX","PARENT_ID","CREATE_USER","CREATE_TIME","UPDATE_TIME","SORT_PATH","IS_ORG_ADMIN","IS_USER_GROUP_ADMIN","CODE","DATA_VERSION") values ('45676279a4a9416a8e20019821e6ghjk', '消息通道', 'platform', null, 1, '34116279a4a9416a8e20019821e61341', null, null, null, '000300090001', 0, 0, 'platform.msgSendManage.MsgSend', 0);
insert into "JARIACEPLAT"."SYS_AUTH" ("ID","NAME","APP_ID","REMARK","SORT_INDEX","PARENT_ID","CREATE_USER","CREATE_TIME","UPDATE_TIME","SORT_PATH","IS_ORG_ADMIN","IS_USER_GROUP_ADMIN","CODE","DATA_VERSION") values ('yut76279a4a9416a8e20019821e6g45t', '消息拓展', 'platform', null, 2, '34116279a4a9416a8e20019821e61341', null, null, null, '000300090002', 0, 0, 'platform.msgTypeExtend.MsgExtend', 0);

insert into "JARIACEPLAT"."SYS_MENU" ("ID","NAME","PARENT_ID","APP_ID","PARENT_NAME","URL","TYPE","ICON","AUTH_ID","BTN_AUTH","IS_LEAF","SORT_INDEX","ALL_ORDER","SORT_PATH","OPEN_STYLE","CLOSE_NOTICE","CREATE_USER","CREATE_TIME","UPDATE_TIME","LEAF","MENU_AUTH","REMARK","DATA_VERSION","IS_IFRAME") values ('34116279a4a9416a8e20019821e61341', '消息配置', '11316279a4a9416a8e20019821e640af', 'platform', '配置管理', null, 0, 'el-icon-message', '34116279a4a9416a8e20019821e61341', null, null, 9, null, '00030009', null, null, null, null, null, null, null, null, 0, 0);
insert into "JARIACEPLAT"."SYS_MENU" ("ID","NAME","PARENT_ID","APP_ID","PARENT_NAME","URL","TYPE","ICON","AUTH_ID","BTN_AUTH","IS_LEAF","SORT_INDEX","ALL_ORDER","SORT_PATH","OPEN_STYLE","CLOSE_NOTICE","CREATE_USER","CREATE_TIME","UPDATE_TIME","LEAF","MENU_AUTH","REMARK","DATA_VERSION","IS_IFRAME") values ('45676279a4a9416a8e20019821e6ghjk', '消息通道', '34116279a4a9416a8e20019821e61341', 'platform', '消息配置', 'msgSendManage/views/MsgSend', 1, 'fa fa-eraser', '45676279a4a9416a8e20019821e6ghjk', null, null, 1, null, '000300090001', null, null, null, null, null, null, null, null, 0, 0);
insert into "JARIACEPLAT"."SYS_MENU" ("ID","NAME","PARENT_ID","APP_ID","PARENT_NAME","URL","TYPE","ICON","AUTH_ID","BTN_AUTH","IS_LEAF","SORT_INDEX","ALL_ORDER","SORT_PATH","OPEN_STYLE","CLOSE_NOTICE","CREATE_USER","CREATE_TIME","UPDATE_TIME","LEAF","MENU_AUTH","REMARK","DATA_VERSION","IS_IFRAME") values ('yut76279a4a9416a8e20019821e6g45t', '消息拓展', '34116279a4a9416a8e20019821e61341', 'platform', '消息配置', 'msgTypeExtend/views/MsgExtend', 1, 'fa fa-arrows-alt', 'yut76279a4a9416a8e20019821e6g45t', null, null, 2, null, '000300090002', null, null, null, null, null, null, null, null, 0, 0);

insert into "JARIACEPLAT"."SYS_AUTH_ROLE" ("ID","AUTH_ID","ROLE_ID","SIGN","IS_REVOKE","CREATE_USER","CREATE_TIME","UPDATE_TIME","REMARK") values ('12323fe2ef32403dbf9dc989d0651249', '34116279a4a9416a8e20019821e61341', 'admin', null, 0, null, null, null, null);
insert into "JARIACEPLAT"."SYS_AUTH_ROLE" ("ID","AUTH_ID","ROLE_ID","SIGN","IS_REVOKE","CREATE_USER","CREATE_TIME","UPDATE_TIME","REMARK") values ('12323fe2ef32403dbf9dc989d0651250', '45676279a4a9416a8e20019821e6ghjk', 'admin', null, 0, null, null, null, null);
insert into "JARIACEPLAT"."SYS_AUTH_ROLE" ("ID","AUTH_ID","ROLE_ID","SIGN","IS_REVOKE","CREATE_USER","CREATE_TIME","UPDATE_TIME","REMARK") values ('12323fe2ef32403dbf9dc989d0651251', 'yut76279a4a9416a8e20019821e6g45t', 'admin', null, 0, null, null, null, null);

insert into "JARIACEPLAT"."SYS_AUTH_ROLE_V" ("ID","AUTH_ID","ROLE_ID","SIGN","IS_REVOKE","CREATE_USER","CREATE_TIME","UPDATE_TIME","REMARK","LV_ID") values ('12323fe2ef32403dbf9dc989d0651249', '34116279a4a9416a8e20019821e61341', 'admin', null, 0, null, null, null, null, 'admin');
insert into "JARIACEPLAT"."SYS_AUTH_ROLE_V" ("ID","AUTH_ID","ROLE_ID","SIGN","IS_REVOKE","CREATE_USER","CREATE_TIME","UPDATE_TIME","REMARK","LV_ID") values ('12323fe2ef32403dbf9dc989d0651250', '45676279a4a9416a8e20019821e6ghjk', 'admin', null, 0, null, null, null, null, 'admin');
insert into "JARIACEPLAT"."SYS_AUTH_ROLE_V" ("ID","AUTH_ID","ROLE_ID","SIGN","IS_REVOKE","CREATE_USER","CREATE_TIME","UPDATE_TIME","REMARK","LV_ID") values ('12323fe2ef32403dbf9dc989d0651251', 'yut76279a4a9416a8e20019821e6g45t', 'admin', null, 0, null, null, null, null, 'admin');

insert into "JARIACEPLAT"."SYS_MSG_SEND_TYPE" ("ID","CHANNEL_NAME","SEND_MODE") values ('83a409e3e03344b7a07eac38105ae5d5', 'default', 'msgSocket');

alter table "FILE_CONFIGURATION" add column("DOWNLOAD_AUTH_CODE" VARCHAR2(50));
alter table "FILE_CONFIGURATION" add column("UPLOAD_AUTH_CODE" VARCHAR2(50));
alter table "FILE_CONFIGURATION" add column("DELETE_AUTH_CODE" VARCHAR2(50));
comment on column "FILE_CONFIGURATION"."DOWNLOAD_AUTH_CODE" is '下载权限标识';
comment on column "FILE_CONFIGURATION"."UPLOAD_AUTH_CODE" is '上传权限标识';
comment on column "FILE_CONFIGURATION"."DELETE_AUTH_CODE" is '删除权限标识';
comment on column "JARIACEPLAT"."FILE_CONFIGURATION"."UPLOAD_OPERATION_KEY" is '上传操作标识';
comment on column "FILE_CONFIGURATION"."DELETE_OPERATION_KEY" is '删除操作标识';
comment on column "FILE_CONFIGURATION"."DOWNLOAD_OPERATION_KEY" is '下载操作标识';
/**
2019/10/24
 */
alter table bd_person_id_type DROP CONSTRAINT UK_ORG_ORGANIZATION_PARENT_ID_SORTINDEX;
/**
2019/10/27
 */
insert into "SYS_MENU" ("ID","NAME","PARENT_ID","APP_ID","PARENT_NAME","URL","TYPE","ICON","AUTH_ID","BTN_AUTH","IS_LEAF","SORT_INDEX","ALL_ORDER","SORT_PATH","OPEN_STYLE","CLOSE_NOTICE","CREATE_USER","CREATE_TIME","UPDATE_TIME","LEAF","MENU_AUTH","REMARK","DATA_VERSION","IS_IFRAME") values ('a16344b1790e41e98615f6cd19587412', '证件类型管理', 'bc316279a4a9416a8e20019821e640af', 'platform', '组织架构管理', 'orgSructureManage/views/PersonIdTypeManage', 1, 'iconfont icon-users-cog', 'a16344b1790e41e98615f6cd19587572', null, null, 22, null, '00010022', null, null, null, null, null, null, null, null, 0, 0);
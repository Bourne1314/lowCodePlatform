/**
2020/2/14
 */
alter table "SYS_AUTH_ROLE_V" DROP CONSTRAINT FK_SYS_AUTH_ROLE_LV_ID;
alter table "SYS_AUTH_ROLE_V" add constraint "FK_SYS_AUTH_ROLE_LV_ID" foreign key("LV_ID") references "SYS_AUTH_ROLE_LV"("ID") on delete cascade;

alter table "SYS_AUTH_USER_V" DROP CONSTRAINT FK_SYS_AUTH_USER_LV_ID;
alter table "SYS_AUTH_USER_V" add constraint "FK_SYS_AUTH_USER_LV_ID" foreign key("LV_ID") references "SYS_AUTH_USER_LV"("ID") on delete cascade;

/**
2020/2/18
 */
comment on table "SYS_WAIT_GRANT_USER" is '角色分配—待激活的用户表';

UPDATE SYS_MENU SET NAME='角色分配' WHERE ID='tyuub3ec3d6e483b86c4923ea64cfwe5';
UPDATE SYS_AUTH SET NAME='角色分配' WHERE ID='tyuub3ec3d6e483b86c4923ea64cfwe5';

insert into "SYS_AUTH" ("ID","NAME","APP_ID","REMARK","SORT_INDEX","PARENT_ID","CREATE_USER","CREATE_TIME","UPDATE_TIME","SORT_PATH","IS_ORG_ADMIN","IS_USER_GROUP_ADMIN","CODE","DATA_VERSION") values ('werty3ec3d6e483b86c4923ea64ccvbn', '分配角色激活', 'platform', null, 8, '11erb3ec3d6e483b86c4923ea64cfdd0', null, null, null, '000200090008', 0, 0, 'roleManage.roleActivate', 0);
insert into "SYS_MENU" ("ID","NAME","PARENT_ID","APP_ID","PARENT_NAME","URL","TYPE","ICON","AUTH_ID","BTN_AUTH","IS_LEAF","SORT_INDEX","ALL_ORDER","SORT_PATH","OPEN_STYLE","CLOSE_NOTICE","CREATE_USER","CREATE_TIME","UPDATE_TIME","LEAF","MENU_AUTH","REMARK","DATA_VERSION","IS_IFRAME") values ('werty3ec3d6e483b86c4923ea64ccvbn', '分配角色激活', '11erb3ec3d6e483b86c4923ea64cfdd0', 'platform', '授权管理', 'roleManage/views/RoleGrant', 1, 'iconfont icon-setting-permissions', 'werty3ec3d6e483b86c4923ea64ccvbn', null, null, 8, null, '000200090008', null, null, null, null, null, null, null, null, 0, 0);

insert into "SYS_AUTH_ROLE" ("ID","AUTH_ID","ROLE_ID","SIGN","IS_REVOKE","CREATE_USER","CREATE_TIME","UPDATE_TIME","REMARK") values ('3e4r5ec3d6e483b86c4923ea64cv67n', 'werty3ec3d6e483b86c4923ea64ccvbn', 'appadmin', null, 0, null, null, null, null);
insert into "SYS_AUTH_ROLE_V" ("ID","AUTH_ID","ROLE_ID","SIGN","IS_REVOKE","CREATE_USER","CREATE_TIME","UPDATE_TIME","REMARK","LV_ID") values ('3e4r5ec3d6e483b86c4923ea64cv67n', 'werty3ec3d6e483b86c4923ea64ccvbn', 'appadmin', null, 0, null, null, null, null, 'appadmin');

--对已存在应用管理员的有效权限删除掉分配角色的菜单管理
delete from sys_auth_mix where user_id in (select user_id from sys_user_role where role_id in ('appadmin')) and auth_id='tyuub3ec3d6e483b86c4923ea64cfwe5';

/**
2020/2/26
 */
delete from SYS_CONFIG where id = 'ace-sql-version';
insert into SYS_CONFIG("ID","NAME","VALUE","SCOPE") values('ace-sql-version', 'ace-sql-version', '1.0.0',1);
alter table SYS_AUDIT_LOG_BACKUP modify  IP_ADDRESS varchar2(1000);
alter table SYS_AUDIT_LOG modify  IP_ADDRESS varchar2(1000);

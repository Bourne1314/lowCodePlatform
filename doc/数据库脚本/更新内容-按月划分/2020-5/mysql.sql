
/** 
 *  添加api资源相关外键的级联更新
 * @author zuogang
 * @date 2020/5/6 10:56
 */

ALTER TABLE `sys_auth_api` ADD CONSTRAINT `FK_AUTH_API_API_ID` FOREIGN KEY (`AUTH_ID`) REFERENCES `sys_auth`(`ID`) ON UPDATE CASCADE ON DELETE CASCADE;

ALTER TABLE `sys_api_mix` ADD CONSTRAINT `FK_API_MIX_API_ID` FOREIGN KEY (`API_ID`) REFERENCES `sys_api_resource`(`ID`) ON UPDATE CASCADE ON DELETE CASCADE;


/**
 * 改善权限授权逻辑
 * @param null
 * @return
 * @author zuogang
 * @date 2020/5/25 17:32
 */
ALTER TABLE `SYS_AUTH_ROLE` ADD COLUMN `AUTH_NAME` VARCHAR2(50) COMMENT '权限名';
ALTER TABLE `SYS_AUTH_ROLE` ADD COLUMN `ROLE_NAME` VARCHAR2(50) COMMENT '角色名';
ALTER TABLE `SYS_AUTH_USER` ADD COLUMN `AUTH_NAME` VARCHAR2(50) COMMENT '权限名';
ALTER TABLE `SYS_AUTH_USER` ADD COLUMN `REAL_NAME` VARCHAR2(50) COMMENT '用户名';
ALTER TABLE `SYS_USER_ROLE` ADD COLUMN `ROLE_NAME` VARCHAR2(50) COMMENT '角色名';
ALTER TABLE `SYS_USER_ROLE` ADD COLUMN `REAL_NAME` VARCHAR2(50) COMMENT '用户名';

update sys_user_role a set a.role_name=(select b.name from sys_role b where b.id = a.role_id);
update sys_user_role a set a.real_name=(select b.real_name from sys_user b where b.id = a.user_id);

update sys_auth_role a set a.role_name=(select b.name from sys_role b where b.id = a.role_id);
update sys_auth_role a set a.auth_name=(select b.name from sys_auth b where b.id = a.auth_id);

update sys_auth_user a set a.real_name=(select b.real_name from sys_user b where b.id = a.user_id);
update sys_auth_user a set a.auth_name=(select b.name from sys_auth b where b.id = a.auth_id);

update sys_user set SECRET_LEVEL=4 where SECRET_LEVEL=5;

/**
 * 消息添加appId
 *
 * @author shanwj
 * @date 2020/5/26 15:58
 */
ALTER TABLE `SYS_MSG_UNREAD` ADD COLUMN `APP_ID` VARCHAR2(50) COMMENT '应用id';
ALTER TABLE `SYS_MSG_READ` ADD COLUMN `APP_ID` VARCHAR2(50) COMMENT '应用id';

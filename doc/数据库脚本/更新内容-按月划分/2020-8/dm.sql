/**
删除仪表盘设计，仪表盘浏览
 */
delete from SYS_MENU where id = '4234ba3204344d37929abd4ad9a1dgth';
delete from SYS_MENU where id = '5234ba3204344d37929abd4ad9a1dgth';
delete from SYS_AUTH where id = '4234ba3204344d37929abd4ad9a1dgth';
delete from SYS_AUTH where id = '5234ba3204344d37929abd4ad9a1dgth';

delete from sys_auth_user where auth_id not in (select id from sys_auth);
delete from sys_auth_user_v where auth_id not in (select id from sys_auth);
delete from sys_auth_role where auth_id not in (select id from sys_auth);
delete from sys_auth_role_v where auth_id not in (select id from sys_auth);
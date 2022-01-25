

/**
 *  添加api资源相关外键的级联更新
 * @author zuogang
 * @date 2020/5/6 10:56
 */
alter table "SYS_AUTH_API" DROP CONSTRAINT "FK_AUTH_API_API_ID";
alter table "SYS_AUTH_API" add constraint "FK_AUTH_API_API_ID" foreign key("API_ID") references "SYS_API_RESOURCE"("ID") on delete cascade;
alter table "SYS_API_MIX" DROP CONSTRAINT "FK_API_MIX_API_ID";
alter table "SYS_API_MIX" add constraint "FK_API_MIX_API_ID" foreign key("API_ID") references "SYS_API_RESOURCE"("ID") on delete cascade;


/**
 * 改善权限授权逻辑
 * @param null
 * @return
 * @author zuogang
 * @date 2020/5/25 17:32
 */
alter table "SYS_AUTH_ROLE" add ("AUTH_NAME" VARCHAR2(50 CHAR));
comment on column "SYS_AUTH_ROLE"."AUTH_NAME" is '权限名';
alter table "SYS_AUTH_ROLE" add ("ROLE_NAME" VARCHAR2(50 CHAR));
comment on column "SYS_AUTH_ROLE"."ROLE_NAME" is '角色名';
alter table "SYS_AUTH_USER" add ("AUTH_NAME" VARCHAR2(50 CHAR));
comment on column "SYS_AUTH_USER"."AUTH_NAME" is '权限名';
alter table "SYS_AUTH_USER" add ("REAL_NAME" VARCHAR2(50 CHAR));
comment on column "SYS_AUTH_USER"."REAL_NAME" is '用户名';
alter table "SYS_USER_ROLE" add ("ROLE_NAME" VARCHAR2(50 CHAR));
comment on column "SYS_USER_ROLE"."ROLE_NAME" is '角色名';
alter table "SYS_USER_ROLE" add ("REAL_NAME" VARCHAR2(50 CHAR));
comment on column "SYS_USER_ROLE"."REAL_NAME" is '用户名';

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

alter table "SYS_MSG_UNREAD" add ("APP_ID" VARCHAR2(50 CHAR));
comment on column "SYS_MSG_UNREAD"."APP_ID" is '应用id';

alter table "SYS_MSG_READ" add ("APP_ID" VARCHAR2(50 CHAR));
comment on column "SYS_MSG_READ"."APP_ID" is '应用id';

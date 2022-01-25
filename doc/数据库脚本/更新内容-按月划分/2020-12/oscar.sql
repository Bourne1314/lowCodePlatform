/**
2020/12/1
 */
alter table SYS_ROLE add column ROLE_CODE VARCHAR2(200);
comment on column "SYS_ROLE"."ROLE_CODE" is '角色标识';

/**
2020/12/1
 */
alter table SYS_ROLE add column DEP_ID VARCHAR2(200);
comment on column "SYS_ROLE"."DEP_ID" is '关联部门主键';


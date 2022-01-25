/**
2021/6/26
 */
alter table "FILE_INFO" add column("RECYCLER_ID" VARCHAR2(50));

comment on column "FILE_INFO"."RECYCLER_ID" is '回收人ID';
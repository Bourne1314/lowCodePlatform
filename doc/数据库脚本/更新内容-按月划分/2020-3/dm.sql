/**
2020/3/17
 */
alter table "BD_PERSON_DOC" DROP CONSTRAINT UK_BD_PERSON_DOC_CODE;

alter table "BD_PERSON_DOC" add column("SORT_INDEX" INTEGER DEFAULT 0);
comment on column "BD_PERSON_DOC"."SORT_INDEX" is '排序号';
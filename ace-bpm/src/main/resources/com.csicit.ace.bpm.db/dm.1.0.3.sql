alter table WFI_DELIVER add column(USER_ID VARCHAR2(50));
comment on column WFI_DELIVER.USER_ID is '用户ID';
alter table WFI_DELIVER add column(DELIVER_TIME DATETIME);
comment on column WFI_DELIVER.DELIVER_TIME is '转交时间';
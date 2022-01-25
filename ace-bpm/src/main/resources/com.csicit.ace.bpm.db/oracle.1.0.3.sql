alter table WFI_DELIVER add (USER_ID VARCHAR2(50 CHAR));
comment on column WFI_DELIVER.USER_ID is '用户ID';
alter table WFI_DELIVER add (DELIVER_TIME DATE);
comment on column WFI_DELIVER.DELIVER_TIME is '转交时间';
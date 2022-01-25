drop table WFI_FLOW_USER cascade;
create table WFI_DELIVER
(
	ID VARCHAR2(50),
	DELIVER_INFO CLOB,
	FLOW_ID VARCHAR2(50)
);
comment on table WFI_DELIVER is '流程-转交信息';
comment on column WFI_DELIVER.ID is '主键';
comment on column WFI_DELIVER.DELIVER_INFO is '转交信息';
comment on column WFI_DELIVER.FLOW_ID is '流程实例ID';
alter table WFI_DELIVER add primary key(ID);
alter table WFI_DELIVER add constraint FK_WFI_DELIVER_FLOW_ID foreign key(FLOW_ID) references WFI_FLOW(ID) on delete cascade with index;
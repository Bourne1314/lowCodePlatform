alter table WFI_FLOW add column(APP_ID VARCHAR2(50));
comment on column WFI_FLOW.APP_ID is '应用标识';
alter table WFD_V_FLOW add column(APP_ID VARCHAR2(50));
comment on column WFD_V_FLOW.APP_ID is '应用标识';
update WFI_FLOW A1 set A1.APP_ID=(SELECT B3.APP_ID FROM WFD_V_FLOW B1 LEFT JOIN WFD_FLOW B2 ON B1.FLOW_ID=B2.ID LEFT JOIN WFD_FLOW_CATEGORY B3 ON B2.CATEGORY_ID=B3.ID WHERE B1.ID=A1.V_FLOW_ID);
update WFD_V_FLOW A1 set A1.APP_ID=(SELECT B2.APP_ID FROM WFD_FLOW B1 LEFT JOIN WFD_FLOW_CATEGORY B2 ON B1.CATEGORY_ID=B2.ID WHERE B1.ID=A1.FLOW_ID);
update WFD_FLOW A1 set A1.APP_ID=(SELECT B1.APP_ID FROM WFD_FLOW_CATEGORY B1 WHERE B1.ID=A1.CATEGORY_ID);
alter table WFI_FLOW_USER add column(NODE_NAME VARCHAR2(50));
comment on column WFI_FLOW_USER.NODE_NAME is '节点名称';
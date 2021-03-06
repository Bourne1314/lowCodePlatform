CREATE TABLE WFI_FOCUSED_WORK
(
ID VARCHAR(50) NOT NULL,
USER_ID VARCHAR(50) NOT NULL,
FLOW_INSTANCE_ID VARCHAR(50) NOT NULL,
STARTER_ID VARCHAR(50),
START_TIME TIMESTAMP,
WORK_NO VARCHAR(50) NOT NULL,
WFD_ID VARCHAR(50) NOT NULL,
WFD_CATEGORY_ID VARCHAR(50) NOT NULL,
APP_ID VARCHAR(50) NOT NULL,
CREATE_TIME TIMESTAMP
);

ALTER TABLE WFI_FOCUSED_WORK ADD
  CONSTRAINT WFI_FOCUSED_WORK_PK
  PRIMARY KEY
  (ID)
  ENABLE VALIDATE;

alter table WFD_DELEGATE_WORK add (NODE_NAME  VARCHAR(50));
alter table WFD_DELEGATE_WORK add (FLOW_NO  VARCHAR(50));
alter table WFD_DELEGATE_WORK add (WFD_ID  VARCHAR(50));
alter table WFD_DELEGATE_WORK add (WFD_CATEGORY_ID  VARCHAR(50));
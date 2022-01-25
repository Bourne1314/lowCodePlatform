CREATE TABLE "WFD_FLOW_CATEGORY" (
"ID" VARCHAR(50) NOT NULL,
"NAME" VARCHAR(100),
"SORT_NO" INT,
"APP_ID" VARCHAR(50),
 CONSTRAINT "PK_WFD_FLOW_CATEGORY" PRIMARY KEY ("ID") );

CREATE TABLE "WFD_FLOW" (
"ID" VARCHAR(50) NOT NULL,
"NAME" VARCHAR(50) NOT NULL,
"CODE" VARCHAR(50),
"SORT_NO" INT DEFAULT 1,
"CATEGORY_ID" VARCHAR(50),
"DESCRIPTION" VARCHAR(4000),
"ADMIN_AUTH_ID" VARCHAR(50),
"QUERY_AUTH_ID" VARCHAR(50),
"HAS_MODIFIED" NUMERIC(1, 0) DEFAULT 0,
"REVISE_VERSION" INT DEFAULT 0,
"MODEL" CLOB,
"FORM_DATA_TABLE" VARCHAR(50),
"FORM_DATA_SOURCE_ID" VARCHAR(50),
"IS_EDITING" NUMERIC(1, 0) DEFAULT 0,
"EDITING_USER" VARCHAR(50),
"LAST_EDIT_TIME" TIMESTAMP,
"INIT_AUTH_ID" VARCHAR(50),
"SEQ_NO" INT DEFAULT 0 NOT NULL,
"LATEST_CREATE_TIME" TIMESTAMP,
"APP_ID" VARCHAR(50),
 CONSTRAINT "PK_WFD_FLOW" PRIMARY KEY ("ID") );

CREATE TABLE "WFD_FLOW_AGENT" (
"ID" VARCHAR(50) NOT NULL,
"BEGIN_TIME" TIMESTAMP,
"END_TIME" TIMESTAMP,
"ENABLED" NUMERIC(1, 0),
"FLOW_ID" VARCHAR(50),
"ORIGIN_USER_ID" VARCHAR(50),
"AGENT_USER_ID" VARCHAR(50),
"AGENT_USER_NAME" VARCHAR(50),
 CONSTRAINT "PK_WFD_FLOW_AGENT" PRIMARY KEY ("ID") );

CREATE TABLE "WFD_GLOBAL_RULE" (
"ID" VARCHAR(50) NOT NULL,
"NAME" VARCHAR(50),
"DESCRIPTION" VARCHAR(2000),
"EXPRESSION" CLOB,
"APP_ID" VARCHAR(50),
 CONSTRAINT "PK_WFD_GLOBAL_RULE" PRIMARY KEY ("ID") );

CREATE TABLE "WFD_GLOBAL_VARIANT" (
"ID" VARCHAR(50) NOT NULL,
"NAME" VARCHAR(50),
"CAPTION" VARCHAR(100),
"DATA_TYPE" VARCHAR(50),
"VALUE_EXPRESSION" VARCHAR(100),
"APP_ID" VARCHAR(50),
"DEFAULT_VALUE" VARCHAR(100),
 CONSTRAINT "PK_WFD_GLOBAL_VARIANT" PRIMARY KEY ("ID") );

CREATE TABLE "WFD_V_FLOW" (
"ID" VARCHAR(50) NOT NULL,
"FLOW_VERSION" INT,
"NAME" VARCHAR(50) NOT NULL,
"CODE" VARCHAR(50),
"SORT_NO" INT,
"CATEGORY_ID" VARCHAR(50),
"DESCRIPTION" VARCHAR(4000),
"ADMIN_AUTH_ID" VARCHAR(50),
"QUERY_AUTH_ID" VARCHAR(50),
"FLOW_ID" VARCHAR(50),
"IS_LATEST" NUMERIC(1, 0),
"MODEL" CLOB,
"BPMN" CLOB,
"FORM_DATA_TABLE" VARCHAR(50),
"FORM_DATA_SOURCE_ID" VARCHAR(50),
"INIT_AUTH_ID" VARCHAR(50),
"VERSION_BEGIN_DATE" TIMESTAMP,
"VERSION_END_DATE" TIMESTAMP,
"IS_USED" NUMERIC(1, 0),
 CONSTRAINT "PK_WFD_V_FLOW" PRIMARY KEY ("ID") );

CREATE TABLE "WFI_FLOW" (
"ID" VARCHAR(50),
"V_FLOW_ID" VARCHAR(50),
"MODEL" CLOB,
"BUSINESS_KEY" VARCHAR(50),
"FLOW_NO" VARCHAR(50),
"FLOW_ID" VARCHAR(50),
"FLOW_CODE" VARCHAR(50),
 CONSTRAINT "PK_WFI_FLOW" PRIMARY KEY ("ID") );

CREATE TABLE "WFI_FLOW_USER" (
"ID" VARCHAR(50) NOT NULL,
"USER_ID" VARCHAR(50),
"USER_TYPE" INT NOT NULL,
"FLOW_ID" VARCHAR(50),
"TASK_ID" VARCHAR(50),
"CREATE_TIME" TIMESTAMP,
"IS_COMPLETED" NUMERIC(1, 0),
"REAL_NAME" VARCHAR(50),
 CONSTRAINT "PK_WFI_FLOW_USER" PRIMARY KEY ("ID") );

ALTER TABLE WFD_FLOW
 ADD CONSTRAINT FK_WFD_FLOW_CATEGORY_ID
  FOREIGN KEY (CATEGORY_ID)
  REFERENCES WFD_FLOW_CATEGORY (ID)
  ON DELETE SET NULL
  ENABLE VALIDATE;

alter table WFD_FLOW add constraint UK_WFD_FLOW_CODE unique(CODE,APP_ID);

ALTER TABLE WFD_FLOW_AGENT
 ADD CONSTRAINT FK_WFD_FLOW_AGENT_FLOW_ID
  FOREIGN KEY (FLOW_ID)
  REFERENCES WFD_FLOW (ID)
  ON DELETE CASCADE
  ENABLE VALIDATE;

ALTER TABLE WFD_V_FLOW
 ADD CONSTRAINT FK_WFD_V_FLOW_FLOW_ID
  FOREIGN KEY (FLOW_ID)
  REFERENCES WFD_FLOW (ID)
  ON DELETE CASCADE
  ENABLE VALIDATE;

ALTER TABLE WFD_V_FLOW
 ADD CONSTRAINT FK_WFD_V_FLOW_CATEGORY
  FOREIGN KEY (CATEGORY_ID)
  REFERENCES WFD_FLOW_CATEGORY (ID)
  ON DELETE SET NULL
  ENABLE VALIDATE;

alter table WFD_V_FLOW add constraint UK_WFD_V_FLOW_FLOW_VERSION unique(FLOW_VERSION,FLOW_ID);
alter table WFI_FLOW add constraint UK_WFI_FLOW_FLOW_NO unique(FLOW_NO,FLOW_ID);

ALTER TABLE WFI_FLOW_USER
 ADD CONSTRAINT FK_WFI_FLOW_USER_FLOW_ID
  FOREIGN KEY (FLOW_ID)
  REFERENCES WFI_FLOW (ID)
  ON DELETE CASCADE
  ENABLE VALIDATE;

alter table WFI_FLOW_USER add constraint UK_WFI_FLOW_USER_USER_ID unique(USER_ID,TASK_ID);

create table WF_PROPERTY
(
	NAME VARCHAR(50),
	VALUE VARCHAR(50),
	DESCRIPTION VARCHAR(50),
 CONSTRAINT "PK_WF_PROPERTY" PRIMARY KEY ("NAME") );
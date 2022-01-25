CREATE TABLE WFD_USER_PAGE
(
  ID         VARCHAR(50),
  USER_ID    VARCHAR(50)                  NOT NULL,
  WFD_ID     VARCHAR(50)                  NOT NULL,
  PAGE_SIZE  INT                          DEFAULT 10                    NOT NULL,
  ARG1       VARCHAR(50),
  ARG2       VARCHAR(50),
  ARG3       VARCHAR(50),
  PAGE_CODE  VARCHAR(50)
);


ALTER TABLE WFD_USER_PAGE ADD
  CONSTRAINT WFD_USER_PAGE_PK
  PRIMARY KEY
  (ID)
  ENABLE VALIDATE;
CREATE TABLE WFI_USER_TASK_STATE
(
  ID       VARCHAR(50),
  TASK_ID  VARCHAR(50),
  USER_ID  VARCHAR(50),
  STATE    VARCHAR(50)
);
ALTER TABLE WFI_USER_TASK_STATE ADD
  CONSTRAINT WFI_USER_TASK_STATE_PK
  PRIMARY KEY
  (ID)
  ENABLE VALIDATE;
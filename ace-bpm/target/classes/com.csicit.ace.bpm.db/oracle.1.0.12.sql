ALTER TABLE WFI_USER_TASK_STATE
 ADD (FLOW_ID  VARCHAR2(50 CHAR));


COMMENT ON COLUMN WFI_USER_TASK_STATE.FLOW_ID IS '流程实例ID';

DELETE FROM WFI_FOCUSED_WORK WHERE FLOW_INSTANCE_ID NOT IN (SELECT ID FROM WFI_FLOW);

ALTER TABLE WFI_FOCUSED_WORK ADD (
  CONSTRAINT FK_WFI_FOCUSED_WORK_FLOW_ID
  FOREIGN KEY (FLOW_INSTANCE_ID)
  REFERENCES WFI_FLOW (ID)
  ON DELETE CASCADE
  ENABLE VALIDATE);

DELETE FROM WFI_USER_TASK_STATE WHERE FLOW_ID NOT IN (SELECT ID FROM WFI_FLOW);

ALTER TABLE WFI_USER_TASK_STATE ADD (
  CONSTRAINT FK_WFI_USER_TASK_STATE_FLOW_ID
  FOREIGN KEY (FLOW_ID)
  REFERENCES WFI_FLOW (ID)
  ON DELETE CASCADE
  ENABLE VALIDATE);

alter table WFI_FOCUSED_WORK add (LATEST_READ_TIME DATE);

comment on column WFI_FOCUSED_WORK.LATEST_READ_TIME is '用户最新一次浏览流程的时间';

alter table WFI_FOCUSED_WORK add (LATEST_READ_FLAG NUMBER DEFAULT 1 NOT NULL);

comment on column WFI_FOCUSED_WORK.LATEST_READ_FLAG is '最新一次浏览后是否有更新 0 否 1 是';
ALTER TABLE WFD_V_FLOW
 ADD (PROCESS_DEFINITION_ID  VARCHAR2(50 CHAR));


COMMENT ON COLUMN WFD_V_FLOW.PROCESS_DEFINITION_ID IS '流程引擎中的流程定义id';

alter table "WFD_V_FLOW" add column("PROCESS_DEFINITION_ID" VARCHAR2(50));

comment on column "WFD_V_FLOW"."PROCESS_DEFINITION_ID" is '流程引擎中的流程定义id';
CREATE TABLE `wfi_v_flow` (
  `ID` VARCHAR(50) NOT NULL COMMENT '主键',
  `FLOW_ID` VARCHAR(50) NULL COMMENT '流程实例ID',
  `MODEL` LONGTEXT NULL COMMENT '流程定义模型',
  `BPMN` LONGTEXT NULL COMMENT 'BPMN模型',
  `FLOW_VERSION` INT(11) NULL COMMENT '流程版本',
  `VERSION_END_DATE` DATETIME NULL COMMENT '版本失效时间',
  PRIMARY KEY (`ID`),
  INDEX `FK_WFI_V_FLOW_FLOW_ID_idx` (`FLOW_ID` ASC),
  UNIQUE INDEX `UK_WFI_V_FLOW_VERSION` (`FLOW_ID` ASC, `FLOW_VERSION` ASC),
  CONSTRAINT `FK_WFI_V_FLOW_FLOW_ID`
    FOREIGN KEY (`FLOW_ID`)
    REFERENCES `wfi_flow` (`ID`)
    ON DELETE CASCADE
    ON UPDATE NO ACTION)
COMMENT = '流程实例版本';

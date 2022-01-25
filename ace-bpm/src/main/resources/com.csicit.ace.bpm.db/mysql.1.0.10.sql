CREATE TABLE `wfi_comment` (
  `ID` VARCHAR(50) NOT NULL COMMENT '主键',
  `COMMENT_USER_ID` VARCHAR(50) NULL COMMENT '评论人id',
  `COMMENT_USER` VARCHAR(30) NULL COMMENT '评论人',
  `REPLY_COMMENT_ID` VARCHAR(50) NULL COMMENT '回复评论id',
  `FLOW_ID` VARCHAR(50) NULL COMMENT '流程实例id',
  `COMMENT_TIME` DATETIME NULL COMMENT '评论时间',
  `TASK_ID` VARCHAR(50) NULL COMMENT '任务id',
  `COMMENT_TEXT` VARCHAR(400) NULL COMMENT '评论内容',
  `APP_ID` VARCHAR(50) NULL COMMENT '应用标识',
  `REPLY_USER_ID` VARCHAR(50) NULL COMMENT '被回复人id',
  `REPLY_USER` VARCHAR(30) NULL COMMENT '被回复人',
  `USER_TYPE` INT NULL COMMENT '评论人身份(-1无关人员,0主办人,1协办人)',
  PRIMARY KEY (`ID`),
  INDEX `FK_WFI_COMMENT_FLOW_ID_idx` (`FLOW_ID` ASC),
  CONSTRAINT `FK_WFI_COMMENT_FLOW_ID`
    FOREIGN KEY (`FLOW_ID`)
    REFERENCES `wfi_flow` (`ID`)
    ON DELETE CASCADE
    ON UPDATE NO ACTION)
COMMENT = '流程实例-意见评论';

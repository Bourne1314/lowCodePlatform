/**
2021/6/26
 */
ALTER TABLE FILE_INFO
 ADD (RECYCLER_ID  VARCHAR2(50 CHAR));


COMMENT ON COLUMN FILE_INFO.RECYCLER_ID IS '回收人ID';
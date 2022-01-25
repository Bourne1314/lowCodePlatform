package com.csicit.ace.common.pojo.domain;

import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.io.Serializable;

/**
 * @author shanwj
 * @date 2019/5/31 15:53
 */
@Data
@TableName("SYS_AUDIT_LOG_COUNT")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SysAuditLogCountDO implements Serializable {

    /**
     * 主键
     */
    private String id;

    /**
     * 日志数量
     */
    private String count;
}

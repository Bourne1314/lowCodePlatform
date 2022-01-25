package com.csicit.ace.common.pojo.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import javax.persistence.Id;
import java.io.Serializable;

/**
 * @author shanwj
 * @date 2019/9/5 11:06
 */
@Data
@TableName("SYS_MSG_TYPE_EXTEND")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SysMsgTypeExtendDO implements Serializable {
    /**
     * 主键
     */
    @Id
    @TableId(type = IdType.UUID)
    private String id;

    /**
     * 类型名称
     */
    private String name;

    /**
     * 应用id
     */
    private String appId;

    /**
     * 消息实现地址
     */
    private String url;
    /**
     * 跟踪ID
     */
    private String traceId;
}

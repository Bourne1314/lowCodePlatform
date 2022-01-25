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
 * @date 2019/7/8 11:00
 */
@Data
@TableName("SYS_MSG_UNREAD")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SysMsgUnReadDO implements Serializable {
    /**
     * 主键
     */
    @Id
    @TableId(type = IdType.UUID)
    private String id;
    /**
     * 接受人
     */
    private String userId;
    /**
     * 消息主体id
     */
    private String msgId;
    /**
     * 应用id
     */
    private String appId;
}

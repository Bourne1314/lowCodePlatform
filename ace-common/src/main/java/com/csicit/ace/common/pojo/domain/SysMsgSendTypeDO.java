package com.csicit.ace.common.pojo.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import javax.persistence.Id;
import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * @author shanwj
 * @date 2019/8/26 9:47
 */
@Data
@TableName("SYS_MSG_SEND_TYPE")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SysMsgSendTypeDO implements Serializable {

    /**
     * 主键
     */
    @Id
    @TableId(type = IdType.UUID)
    private String id;

    /**
     * 频道名称
     */
    private String channelName;

    /**
     * 发送方式
     */
    private String sendMode;

    /**
     * 应用id
     */
    private String appId;

    /**
     * 发送方式列表
     */
    @TableField(exist = false)
    private List<String> modeList;
    /**
     * 跟踪ID
     */
    private String traceId;
}

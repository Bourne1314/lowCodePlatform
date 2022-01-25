package com.csicit.ace.common.pojo.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import javax.persistence.Id;
import java.util.List;

/**
 * 大屏消息 实例对象类
 *
 * @author generator
 * @version V1.0
 * @date 2020-07-29 16:41:51
 */
@Data
@TableName("BLADE_VISUAL_MSG")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class BladeVisualMsgDO {
    /**
     * 主键
     */
    @Id
    @TableId(type = IdType.UUID)
    private String id;
    /**
     * 名称
     */
    private String name;
    /**
     * 标识
     */
    private String code;
    /**
     * 事件名
     */
    private String eventName;
    /**
     * 显示模式（0即时触发，1定时触发）
     */
    private Integer displayMode;
    /**
     * 显示内容
     */
    private String displayContent;
    /**
     * 周期模式
     */
    private String periodMode;
    /**
     * 周期数据
     */
    private String periodData;
    /**
     * 显示时长
     */
    private Integer displayDuration;
    /**
     * 大屏看版Ids(以英文逗号分隔)
     */
    private String visualIds;
    /**
     * 大屏看版Names(以英文逗号分隔)
     */
    @TableField(exist = false)
    private List<String> visualNames;
    /**
     * 通知模式(0通知，1警告)
     */
    private Integer noticeMode;
    /**
     * 应用ID
     */
    private String appId;
    /**
     * 跟踪ID
     */
    private String traceId;
}

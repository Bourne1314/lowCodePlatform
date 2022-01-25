package com.csicit.ace.common.pojo.domain.dev;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import javax.persistence.Id;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 页面基本信息
 *
 * @author zuogang
 * @date 2020/7/2 9:36
 */
@Data
@TableName("PRO_PAGE_INFO")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ProPageInfoDO implements Serializable {
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
     * 描述
     */
    private String des;
    /**
     * js对象
     */
    private String jsCode;
    /**
     * html对象
     */
    private String htmlCode;
    /**
     * 已选数据模型对象ID(英文逗号分隔)
     */
    private String modelIds;
    /**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;
    /**
     * 已选数据模型对象
     */
    @TableField(exist = false)
    private List<ProModelDO> models;
    /**
     * 接口类别列表
     */
    @TableField(exist = false)
    private List<ProInterfaceCategoryDO> interfaceCategorys;
    /**
     * 服务iD
     */
    private String serviceId;

}

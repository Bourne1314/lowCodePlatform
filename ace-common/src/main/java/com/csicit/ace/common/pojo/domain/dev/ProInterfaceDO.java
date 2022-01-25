package com.csicit.ace.common.pojo.domain.dev;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import javax.persistence.Id;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 应用接口表
 *
 * @author zuogang
 * @date Created in 16:51 2019/11/27
 */
@Data
@TableName("PRO_INTERFACE")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ProInterfaceDO {

    /**
     * 主键
     */
    @Id
    @TableId(type = IdType.UUID)
    private String id;
    /**
     * 接口名
     */
    private String name;
    /**
     * 是否分页方法,0否1是
     */
    @TableField(value = "IS_PAGE_GET")
    private Integer pageGet;
    /**
     * sql语句
     */
    private String sqlContent;
    /**
     * 数据源ID
     */
    private String dsId;
    /**
     * 数据源名
     */
    @TableField(exist = false)
    private String dsName;
    /**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;
    /**
     * 描述
     */
    private String description;
    /**
     * 入参列表
     */
    @TableField(exist = false)
    private List<ProInputParamsDO> inputParams;
    /**
     * 出参列表
     */
    @TableField(exist = false)
    private List<ProOutputParamsDO> outPutParams;
    /**
     * 标识
     */
    private String code;
    /**
     * 类别
     */
    private String categoryId;
}

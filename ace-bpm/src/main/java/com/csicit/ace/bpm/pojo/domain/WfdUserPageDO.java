package com.csicit.ace.bpm.pojo.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import org.springframework.data.annotation.Id;

import java.io.Serializable;

/**
 * 用户默认流程页面
 *
 * @author FourLeaves
 * @version V1.0
 * @date 2020/4/10 8:38
 */
@Data
@TableName("WFD_USER_PAGE")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class WfdUserPageDO implements Serializable {
    /**
     * 主键
     */
    @Id
    @TableId(type = IdType.UUID)
    private String id;

    /**
     * 用户主键
     */
    private String userId;

    /**
     * 流程定义主键
     */
    private String wfdId;

    /**
     * 默认流程页面标识
     */
    private String pageCode;

    /**
     * 流程列表默认每页数量
     */
    @TableField(value = "PAGE_SIZE")
    private Integer pageSize;

    /**
     * 预留属性1
     */
    private String arg1;

    /**
     * 预留属性2
     */
    private String arg2;

    /**
     * 预留属性3
     */
    private String arg3;

}

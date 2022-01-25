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
@TableName("PRO_INTERFACE_CATEGORY")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ProInterfaceCategoryDO {

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
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;
    /**
     * 描述
     */
    private String remark;
    /**
     * 服务iD
     */
    private String serviceId;
    /**
     * 接口列表
     */
    @TableField(exist = false)
    private List<ProInterfaceDO> interfaces;
}

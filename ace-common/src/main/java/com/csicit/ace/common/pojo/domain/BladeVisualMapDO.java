package com.csicit.ace.common.pojo.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.csicit.ace.common.pojo.AbstractBaseRecordDomain;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

import javax.persistence.Id;

/**
 * 可视化地图配置表 实例对象类
 *
 * @author generator
 * @version V1.0
 * @date 2020-06-05 10:05:48
 */
@Data
@TableName("BLADE_VISUAL_MAP")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class BladeVisualMapDO {
    /**
     * 主键
     */
    @Id
    @TableId(type = IdType.UUID)
    private String id;
    /**
     * 地图名称
     */
    private String name;
    /**
     * 地图数据
     */
    private String data;

}

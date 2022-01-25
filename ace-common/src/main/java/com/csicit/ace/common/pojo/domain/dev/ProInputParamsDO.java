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

/**
 * 接口sql入参信息表
 *
 * @author zuogang
 * @date Created in 16:51 2019/11/27
 */
@Data
@TableName("PRO_INPUT_PARAMS")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ProInputParamsDO {

    /**
     * 主键
     */
    @Id
    @TableId(type = IdType.UUID)
    private String id;
    /**
     * 接口ID
     */
    private String interfaceId;
    /**
     * 键
     */
    private String paramKey;
    /**
     * 值
     */
    private String paramDefaultValue;
    /**
     * 参数类型
     */
    private String paramType;

}

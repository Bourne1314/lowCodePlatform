package com.csicit.ace.bpm.pojo.vo.v7v1v81.mysql;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

/**
 * @author JonnyJiang
 * @date 2020/2/10 16:48
 */
@Data
@TableName("ACT_GE_BYTEARRAY")
public class ActGeBytearrayDO implements Serializable {
    @TableField("ID_")
    private String id;
    @TableField("REV_")
    private Integer rev;
    @TableField("NAME_")
    private String name;
    @TableField("DEPLOYMENT_ID_")
    private String deploymentId;
    @TableField("BYTES_")
    private Byte[] bytes;
    @TableField("GENERATED_")
    private Integer generated;
}
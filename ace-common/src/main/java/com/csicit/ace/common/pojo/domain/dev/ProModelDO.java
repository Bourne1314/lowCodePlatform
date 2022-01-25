package com.csicit.ace.common.pojo.domain.dev;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import net.sf.json.JSONObject;

import javax.persistence.Id;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * 实体模型信息表
 *
 * @author zuogang
 * @date Created in 16:51 2019/11/27
 */
@Data
@TableName("PRO_MODEL")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ProModelDO {

    /**
     * 主键
     */
    @Id
    @TableId(type = IdType.UUID)
    private String id;
    /**
     * 模型名称
     */
    private String modelName;
    /**
     * 数据库表名
     */
    private String tableName;
    /**
     * 数据库表实体名
     */
    private String objectName;
    /**
     * 服务iD
     */
    private String serviceId;
//    /**
//     * 模型名称
//     */
//    private String modelType;
    /**
     * 模型名称
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;
    /**
     * 备注
     */
    private String remark;
    /**
     * 主键名称
     */
    private String pkName;
    /**
     * 数据列集合
     */
    @TableField(exist = false)
    private List<ProModelColDO> proModelColDOS;

    /**
     * 索引集合
     */
    @TableField(exist = false)
    private List<ProModelIndexDO> proModelIndexDOS;

    /**
     * 关联集合
     */
    @TableField(exist = false)
    private List<ProModelAssociationDO> proModelAssociationDOS;
    /**
     * 跟踪ID
     */
    private String traceId;
    /**
     * 表单是否存在附件
     */
    @TableField("IS_FILE_EXIST")
    private Integer fileExist;
    /**
     * 是否是工作流表单
     */
    @TableField("IS_FLOW_EXIST")
    private Integer flowExist;

    /**
     * 表单规则
     */
    @TableField(exist = false)
    private JSONObject rules;
}

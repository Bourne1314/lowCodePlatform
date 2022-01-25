package com.csicit.ace.common.pojo.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import javax.persistence.Id;
import java.io.Serializable;

/**
 * 模板部件
 *
 * @author shanwj
 * @date 2020/5/22 10:22
 */
@Data
@TableName("CODE_TEMPLATE_PART")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CodeTemplatePartDO implements Serializable {

    /**
     * 主键
     */
    @Id
    @TableId(type = IdType.UUID)
    private String id;

    /**
     * 模板主键
     */
    private String templateId;
    /**
     * 部件排序
     */
    private Integer sortIndex;
    /**
     * 部件编码类型 0静态文本1el表达式2参数值3数字序列
     */
    private Integer codeType;
    /**
     * el表达式
     */
    private String elExpress;
    /**
     * 静态文本
     */
    private String staticText;
    /**
     * 参数键名
     */
    private String paramKey;
    /**
     * 是否自动创建数字序列
     */
    private Integer autoCreateSeqDef;
    /**
     * 序列步长
     */
    private Integer seqStep;
    /**
     * 序列重置模式
     * 0 不重置、 1 按年重置、2 按月重置、3 按天重置
     */
    private Integer seqResetMode;
    /**
     * 是否启用固定数字位数 0否1是
     */
    private Integer enableFixNumLen;
    /**
     * 规定数字位数
     */
    private Integer seqNumLength;
    /**
     * 关联数字学列主标识
     */
    private String sequenceBizTag;
    /**
     * 模板部件描述
     */
    private String remark;
    /**
     * 数字序列关联部件
     */
    private String dynPart;

    @TableField(exist = false)
    private String appId;
    /**
     * 跟踪ID
     */
    private String traceId;
}

package com.csicit.ace.common.pojo.domain.dev;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import javax.persistence.Id;
import java.time.LocalDate;

/**
 * 视图 实例对象类
 *
 * @author xulei
 * @date 2020/7/7 10.15
 * @version V1.0
 */
@Data
@TableName("META_COLUMN")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class MetaColumnDo {

    /**
     * 主键
     */
    @Id
    @TableId(type = IdType.UUID)
    private String id;

    /**
     * 数据表
     */
    private String tblId;

    /**
     * 列名
     */
    private String name;

    /**
     * 列标题
     */
    private String caption;

    /**
     * 数据类型
     */
    private String dataType;

    /**
     * 最大长度
     */
    private Integer dataSize;

    /**
     * 精度
     */
    private Integer dataScale;

    /**
     * 能否为空
     */
    private Integer nullable;

    /**
     * 缺省值
     */
    private String defaultValue;

    /**
     * 备注
     */
    private String remark;

    /**
     * 是否系统字段
     */
    private Integer isSyscol;

    /**
     * 序号
     */
    private Integer sortIndex;

    /**
     * 枚举字典
     */
    private String dictId;

    /**
     * 引用字典
     */
    private String refId;

    /**
     * 空值注释
     */
    private String emptyText;

    /**
     * 最小值
     */
    private Integer minNumberValue;

    /**
     * 最大值
     */
    private Integer maxNumberValue;

    /**
     * 最小值
     */
    private LocalDate minDateValue;

    /**
     * 最大值
     */
    private LocalDate maxDateValue;

    /**
     * 正则表达式
     */
    private String stringRegex;

    /**
     * 忽略大小写
     */
    private Integer stringRegexIgnoreCase;

    /**
     * 验证失败提示
     */
    private String stringRegexFailMsg;

    /**
     * 允许多选
     */
    private Integer enableMultiSelection;

    /**
     * 强制选择
     */
    private Integer forceSelection;

    /**
     * 允许编辑
     */
    private Integer allowEdit;

    /**
     * 最短长度
     */
    private Integer minLength;

    /**
     * 比较字段
     */
    private String compareField;

    /**
     * 比较操作
     */
    private String compareOperator;

    /**
     * 验证失败消息
     */
    private String compareFailMsg;

    /**
     * 预设格式
     */
    private String stringFormat;

    /**
     * 多选值分隔符
     */
    private String multiSelSeperator;

    /**
     * 引用表
     */
    private String refTblId;

    /**
     * 显示时间
     */
    private Integer showTime;
    /**
     * 日期格式
     */
    private String dateTimeFormat;



}

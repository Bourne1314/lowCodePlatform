package com.csicit.ace.testapp.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import javax.persistence.Id;
import java.text.DecimalFormat;

import java.time.LocalDateTime;

@Data
@TableName("BOOK")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Book {
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
     * 价格
     */
    private String price;
    /**
     *
     */
    private String author;

    /**
     * 简介
     */
    private String introduction;

    /**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;

    /**
     * 序号
     */
    private Integer sortIndex;

    /**
     * 是否完结（0否1是）
     */
    private Integer isFinish;

    /**
     * 字数范围(0:50万以下;1:50万到100万之间;2:100万到200万之间;3:200万到500万之间;4:500万以上)
     */
    private Integer wordSizeDeterm;
}

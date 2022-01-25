package com.csicit.ace.demo.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import javax.persistence.Id;

/**
 * @author shanwj
 * @date 2019/10/16 15:11
 */
@TableName("BOOK_TYPE")
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class BookTypeDO{
    @Id
    @TableId(type = IdType.UUID)
    private String id;
    private String name;
    private String sort;
    private String appId;
}

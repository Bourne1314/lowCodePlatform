package com.csicit.ace.demo.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import javax.persistence.Id;
import java.time.LocalDate;

/**
 * @author shanwj
 * @date 2019/10/16 15:52
 */
@Data
@TableName("BOOK_INFO")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class BookInfoDO {
    @Id
    @TableId(type = IdType.UUID)
    private String id;
    private String name;
    private String author;
    private String press;
    private String appId;
    private String typeId;
    private LocalDate time;
}

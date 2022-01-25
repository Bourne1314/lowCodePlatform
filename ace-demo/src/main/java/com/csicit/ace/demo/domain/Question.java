package com.csicit.ace.demo.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import javax.persistence.Id;

/**
 * @author shanwj
 * @date 2020/6/16 16:19
 */
@Data
@TableName("QUESTION")
public class Question {
    @Id
    @TableId(type = IdType.UUID)
    private String id;
    private String title;
    private String an1;
    private String an2;
    private String an3;
    private String an4;
    private String an;
    private int num;
}

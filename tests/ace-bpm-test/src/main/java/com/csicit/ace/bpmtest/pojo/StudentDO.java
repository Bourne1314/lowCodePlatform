package com.csicit.ace.bpmtest.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import javax.persistence.Id;

/**
 * @author JonnyJiang
 * @date 2019/11/28 8:31
 */
@Data
@TableName("STUDENT")
public class StudentDO {
    @Id
    @TableId(type = IdType.UUID)
    private String id;
    private String studentNo;
    private String studentName;
}

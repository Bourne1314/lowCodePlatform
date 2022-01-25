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
import java.util.List;

/**
 * 应用管理
 *
 * @author shanwj
 * @date 2019/11/25 11:05
 */
@Data
@TableName("PRO_INFO")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ProInfoDO {

    /**
     * 主键
     */
    @Id
    @TableId(type = IdType.UUID)
    private String id;
    /**
     * 项目名称
     */
    private String name;
    /**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;
//    /**
//     * 项目维护人员ID(多人使用;分割)
//     */
//    private String maintainStaffs;
    /**
     * 项目标识
     */
    private String code;
    /**
     * 项目备注
     */
    private String remark;
    /**
     * 创建人
     */
    private String createUser;
    /**
     * 集团ID
     */
    private String groupId;
    /**
     * 是否删除，0否，1是
     */
    @TableField("IS_DELETE")
    private Integer isDelete;
//    /**
//     * 项目维护人员信息
//     */
//    @TableField(exist = false)
//    private List<DevUserDO> maintainUsers;
}

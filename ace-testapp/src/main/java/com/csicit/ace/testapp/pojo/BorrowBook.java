package com.csicit.ace.testapp.pojo;

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

@Data
@TableName("BORROW_BOOK")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class BorrowBook {
    /**
     * 主键
     */
    @Id
    @TableId(type = IdType.UUID)
    private String id;

    /**
     * 借书人ID
     */
    private String borrowUserId;

    /**
     * 借书人名
     */
    private String borrowUserName;

    /**
     * 备注
     */
    private String remark;

    /**
     * 接收人ID
     */
    private String manageUserId;

    /**
     * 接收人姓名
     */
    private String manageUserName;

    /**
     * 借书人部门ID
     */
    private String borrowDepId;

    /**
     * 借书人部门ID
     */
    private String borrowDepName;

    /**
     * 接收人部门ID
     */
    private String manageDepId;

    /**
     * 接收人部门名
     */
    private String manageDepName;

    /**
     * 工作流发起时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime applyTime;


    /**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;

    /**
     * 创建人
     */
    private String createUser;

    /**
     * 工作流标识
     */
    private String flowCode;

    /**
     * 当前步骤名称
     */
    private String nodeName;

    /**
     * 书籍IDs
     */
    private String bookIds;

    /**
     * 书籍名
     */
    private String bookNames;

    /**
     * 书籍列表
     */
    @TableField(exist = false)
    private List<Book> books;

    /**
     * 用途
     */
    private Integer lendingPurpose;

    /**
     * 多选Demo
     */
    private String checkboxValue;

    /**
     * 单选Demo
     */
    private Integer radioValue;

    /**
     * 数字Demo
     */
    private Integer numberValue;

    /**
     * Demo权限ID
     */
    private String authId;

    /**
     * Demo权限名
     */
    private String authName;

    /**
     * Demo角色ID
     */
    private String roleId;

    /**
     * Demo角色名
     */
    private String roleName;
    /**
     * 密级
     */
    private Integer secretLevel;

}

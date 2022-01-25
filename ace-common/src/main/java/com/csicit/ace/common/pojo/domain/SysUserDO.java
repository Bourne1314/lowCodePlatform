package com.csicit.ace.common.pojo.domain;

import com.csicit.ace.common.pojo.AbstractBaseRecordDomain;
import com.csicit.ace.common.pojo.AbstractSecretRecordDomain;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 用户表 实例对象类
 *
 * @author generator
 * @version V1.0
 * @date 2019-04-15 20:13:31
 */
@Data
@TableName("SYS_USER")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SysUserDO extends AbstractBaseRecordDomain {

    /**
     * 工号
     */
    private String staffNo;
    /**
     * 登录名
     */
    private String userName;
    /**
     * 用户名称
     */
    private String realName;
    /**
     * 电子邮箱
     */
    private String email;
    /**
     * 联系电话
     */
    private String phoneNumber;
    /**
     * 用户类型（0租户管理员,1集团管理员,2应用管理员,3普通用户）
     */
    private Integer userType;
    /**
     * 启用标志 0 禁用 1 启用
     */
    private String startFlag;
    /**
     * 用户密级 1 核心 2 重要 3 一般 4 内部 5 非密
     */
    private Integer secretLevel;
    /**
     * 用户排序
     */
    private Integer sortIndex;
    /**
     * 邮编
     */
    private String userPost;
    /**
     * 用户密码
     */
    @TableField(exist = false)
    private String password;
    /**
     * 密码最近一次修改时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime passwordUpdateTime;
    /**
     * 人员档案主键
     */
    private String personDocId;
    /**
     * 人员档案
     */
    @TableField(exist = false)
    private BdPersonDocDO personDoc;
    /**
     * 主要职务
     */
    @TableField(exist = false)
    private BdPersonJobDO bdPersonJobDO;
    /**
     * 身份类型（员工/外部系统/开发者/客户/供应商/审计）
     */
    private String baseDocType;
    /**
     * 用户名称拼音
     */
    private String pinyin;
    /**
     * 密码策略id
     */
    private String pswPolicyId;
    /**
     * 用户失败登录次数
     */
    private Integer failLoginTimes;
    /**
     * 账户解锁时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime unlockTime;
    /**
     * 所属业务单元ID
     */
    private String organizationId;
    /**
     * 所属业务单元名称
     */
    @TableField(exist = false)
    private String organizationName;

    /**
     * 所属部门ID
     */
    @TableField(exist = false)
    private String departmentId;
    /**
     * 所属部门code
     */
    @TableField(exist = false)
    private String departmentCode;
    /**
     * 所属部门名称
     */
    @TableField(exist = false)
    private String departmentName;
    /**
     * 所属部门可排序路径
     */
    @TableField(exist = false)
    private String departmentSortPath;
    /**
     * 所属集团ID
     */
    private String groupId;
    /**
     * 所属集团名称
     */
    @TableField(exist = false)
    private String groupName;
    /**
     * 是否第一次登录 0 否 1是
     */
    @TableField(value = "IS_FIRST_LOGIN")
    private Integer firstLogin;

    /**
     * 逻辑删除 0 否 1是
     */
    @TableField(value = "IS_DELETE")
    private Integer beDeleted;

    /**
     * 授控域-组织
     */
    @TableField(exist = false)
    private List<OrgOrganizationDO> organizes;

    /**
     * 授控域-集团
     */
    @TableField(exist = false)
    private List<OrgGroupDO> groups;

    /**
     * 授控域-应用
     */
    @TableField(exist = false)
    private List<SysGroupAppDO> apps;

    /**
     * 授控域-用户组
     */
    @TableField(exist = false)
    private List<SysUserGroupDO> userGroups;

    /**
     * 管理员角色
     */
    @TableField(exist = false)
    private Integer roleType;

    /**
     * 是否激活IP绑定0否，1是
     */
    @TableField(value = "IS_IP_BIND")
    private Integer ipBind;

    /**
     * IP地址
     */
    private String ipAddress;

}

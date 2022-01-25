package com.csicit.ace.common.pojo.domain;

import com.baomidou.mybatisplus.annotation.TableId;
import com.csicit.ace.common.pojo.AbstractBaseRecordDomain;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

/**
 * 基础数据-人员档案 实例对象类
 *
 * @author generator
 * @version V1.0
 * @date 2019-04-15 17:25:44
 */
@Data
@TableName("BD_PERSON_DOC")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class BdPersonDocDO extends AbstractBaseRecordDomain {
    /**
     * 逻辑删除 0 否 1是
     */
    @TableField(value = "IS_DELETE")
    private Integer beDeleted;
    /**
     * 所属集团主键
     */
    private String groupId;
    /**
     * 所属集团名称
     */
    @TableField(exist = false)
    private String groupName;
    /**
     * 所属组织主键
     */
    private String organizationId;
    /**
     * 所属组织名称
     */
    @TableField(exist = false)
    private String organizationName;
    /**
     * 姓名
     */
    private String name;
    /**
     * 曾用名
     */
    private String usedName;
    /**
     * 家庭地址
     */
    private String homeAddress;
    /**
     * 出生日期
     */
    private LocalDate birthDate;
    /**
     * 编码
     */
    private String code;
    /**
     * 证件号码
     */
    private String idNumber;
    /**
     * 证件类型主键
     */
    private String personIdTypeId;
    /**
     * 证件类型
     */
    @TableField(exist = false)
    private String personIdTypeName;
    /**
     * 参加工作日期
     */
    private LocalDate joinWorkDate;
    /**
     * 手机号码
     */
    private String cellPhone;
    /**
     * 昵称
     */
    private String nickName;
    /**
     * 工作电话
     */
    private String officePhone;
    /**
     * 家庭电话
     */
    private String homePhone;
    /**
     * 性别  0男 1女
     */
    private Integer gender;
    /**
     * 姓名拼音
     */
    private String pinYin;

    /**
     * 排序号
     */
    private Integer sortIndex;
    /**
     * 职务列表
     */
    @TableField(exist = false)
    private List<BdPersonJobDO> jobList;


    /**
     * 主职务
     */
    @TableField(exist = false)
    private BdPersonJobDO mainJob;


    /**
     * 绑定的用户
     */
    @TableField(exist = false)
    private SysUserDO user;

}

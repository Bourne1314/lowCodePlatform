package com.csicit.ace.common.pojo.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import javax.persistence.Id;
import java.time.LocalDate;

@Data
@TableName("ORG_ADMIN_ORG")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class OrgAdminOrgDO {
    /**
     * 主键
     */
    @Id
    @TableId(type = IdType.UUID)
    private String id;

    /**
     * 上级行政组织主键
     */
    private String parentId;

    /**
     * 所属组织主键
     */
    private String organizationId;

    /**
     * 版本主键
     */
    private String versionId;

    /**
     * 名称
     */
    private String name;

    /**
     * 简称
     */
    private String shortName;

    /**
     * 创建人id
     */
    private String createUser;

    /**
     * 创建时间
     */
    private LocalDate createTime;

    /**
     * 更新时间
     */
    private String updateTime;

    /**
     * 所属集团主键
     */
    private String groupId;

    /**
     * 编码
     */
    private String code;

    /**
     * 数据版本
     */
    private Integer dataVersion;

}

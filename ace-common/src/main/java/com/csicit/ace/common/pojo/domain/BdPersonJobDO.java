package com.csicit.ace.common.pojo.domain;

import com.csicit.ace.common.pojo.AbstractBaseDomain;
import com.csicit.ace.common.pojo.AbstractBaseRecordDomain;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 基础数据-人员工作信息 实例对象类
 *
 * @author generator
 * @date 2019-04-15 17:27:00
 * @version V1.0
 */
@Data
@TableName("BD_PERSON_JOB")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class BdPersonJobDO extends AbstractBaseDomain {

        /**
         * 任职集团主键
         */
        private String groupId;
        /**
         * 任职集团
         */
        @TableField(exist = false)
        private String groupName;
        /**
         * 任职组织主键
         */
        private String organizationId;
        /**
         * 任职组织
         */
        @TableField(exist = false)
        private String organizationName;
        /**
         * 所在部门主键
         */
        private String departmentId;
        /**
         * 所在部门
         */
        @TableField(exist = false)
        private String departmentName;
        /**
         * 员工编号
         */
        private String personCode;
        /**
         * 排序号
         */
        private Integer sortIndex;
        /**
         * 岗位主键
         */
        private String postId;
        /**
         * 岗位称谓
         */
        @TableField(exist = false)
        private String postName;
        /**
         * 职务主键
         */
        private String jobId;
        /**
         * 职务称谓
         */
        @TableField(exist = false)
        private String jobName;
        /**
         * 集团-组织-部门-职务称谓
         */
        @TableField(exist = false)
        private String longJobName;
        /**
         * 是否主职
         */
        @TableField("IS_MAIN_JOB")
        private Integer mainJob;
        /**
         * 任职开始日期
         */
        private LocalDate inDutyDate;
        /**
         * 任职结束日期
         */
        private LocalDate endDutyDate;
        /**
         * 人员档案主键
         */
        private String personDocId;
        /**
         * 创建人id
         */
        private String createUser;
        /**
         * 创建时间
         */
        @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
        private LocalDateTime createTime;
        /**
         * 最后一次修改时间
         */
        @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
        private LocalDateTime updateTime;

}
package com.csicit.ace.common.pojo.domain;

import com.baomidou.mybatisplus.annotation.TableField;
import com.csicit.ace.common.pojo.AbstractBaseRecordDomain;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 密码安全策略表 实例对象类
 *
 * @author generator
 * @date 2019-04-15 20:12:41
 * @version V1.0
 */
@Data
@TableName("SYS_PASSWORD_POLICY")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SysPasswordPolicyDO extends AbstractBaseRecordDomain{

        /**
         * 是否启用正则表达式对密码进行校验 0 否 1 是
         */
        @TableField(exist = false)
        private boolean useEl;

        /**
         * 密码长度
         */
        private Integer len;
        /**
         * 登录失败次数
         */
        private Integer failureTimes;
        /**
         * 密码开始使用时间
         */
        @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
        private LocalDateTime startUseTime;
        /**
         * 密码更改次数
         */
        private Integer changeNum;
        /**
         * 密级
         */
        private Integer secretLevel;
        /**
         * 密码使用最长期限
         */
        private Integer useMaxTime;
        /**
         * 密码使用最短期限
         */
        private Integer useMinTime;
        /**
         * 锁定时长(分钟)
         */
        private Integer lockMinutes;
        /**
         * 重置失败次数的时长（分钟）
         */
        private Integer resetFailureTimesTime;

}

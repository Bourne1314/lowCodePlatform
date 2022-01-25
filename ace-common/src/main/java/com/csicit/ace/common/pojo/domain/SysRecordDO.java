package com.csicit.ace.common.pojo.domain;

import com.csicit.ace.common.pojo.AbstractBaseRecordDomain;
import com.baomidou.mybatisplus.annotation.TableName;
import com.csicit.ace.common.pojo.AbstractBaseRecordDomain;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.time.LocalDate;

/**
 * 系统日志 实例对象类
 *
 * @author generator
 * @date 2019-04-15 20:13:09
 * @version V1.0
 */
@Data
@TableName("SYS_RECORD")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SysRecordDO extends AbstractBaseRecordDomain {

        /**
         * 用户id
         */
        private String userId;
        /**
         * 用户名
         */
        private String userName;
        /**
         * 发生时间
         */
        private LocalDate time;
        /**
         * ip
         */
        private String ip;
        /**
         * 事件类别：0 安全审计、1 普通操作、2 工作流、3 文件访问
         */
        private String kind;
        /**
         * 日志内容
         */
        private String content;

}

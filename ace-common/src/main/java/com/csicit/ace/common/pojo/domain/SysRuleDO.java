package com.csicit.ace.common.pojo.domain;

import com.csicit.ace.common.pojo.AbstractBaseRecordDomain;
import com.baomidou.mybatisplus.annotation.TableName;
import com.csicit.ace.common.pojo.AbstractBaseRecordDomain;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

/**
 * 系统管理-规则 实例对象类
 *
 * @author generator
 * @date 2019-04-15 20:13:26
 * @version V1.0
 */
@Data
@TableName("SYS_RULE")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SysRuleDO extends AbstractBaseRecordDomain {

        /**
         * 编码
         */
        private String code;
        /**
         * 名称
         */
        private String name;
        /**
         * 规则实现类
         */
        private String className;
        /**
         * 规则类型(0授权组织规则,1授权用户组规则)
         */
        private String ruleType;

}

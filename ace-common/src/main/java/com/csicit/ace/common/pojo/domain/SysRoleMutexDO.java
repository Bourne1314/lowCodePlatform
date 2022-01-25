package com.csicit.ace.common.pojo.domain;

import com.csicit.ace.common.pojo.AbstractBaseRecordDomain;
import com.baomidou.mybatisplus.annotation.TableName;
import com.csicit.ace.common.pojo.AbstractBaseRecordDomain;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

/**
 * 角色互斥关系表 实例对象类
 *
 * @author generator
 * @date 2019-04-15 20:13:18
 * @version V1.0
 */
@Data
@TableName("SYS_ROLE_MUTEX")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SysRoleMutexDO extends AbstractBaseRecordDomain {

        /**
         * 角色id
         */
        private String roleId;
        /**
         * 互斥角色id
         */
        private String roleMutexId;
        /**
         * 跟踪ID
         */
        private String traceId;

}

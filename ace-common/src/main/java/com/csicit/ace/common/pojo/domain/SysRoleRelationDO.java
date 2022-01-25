package com.csicit.ace.common.pojo.domain;

import com.csicit.ace.common.pojo.AbstractBaseRecordDomain;
import com.baomidou.mybatisplus.annotation.TableName;
import com.csicit.ace.common.pojo.AbstractBaseRecordDomain;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

/**
 * 角色关系表 实例对象类
 *
 * @author generator
 * @date 2019-04-15 20:13:22
 * @version V1.0
 */
@Data
@TableName("SYS_ROLE_RELATION")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SysRoleRelationDO extends AbstractBaseRecordDomain {

        /**
         * 上级角色
         */
        private String pid;
        /**
         * 下级角色
         */
        private String cid;
        /**
         * 跟踪ID
         */
        private String traceId;

}

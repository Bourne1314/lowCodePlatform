package com.csicit.ace.common.pojo.domain;

import com.csicit.ace.common.pojo.AbstractBaseDomain;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

/**
 * 系统管理-用户有效权限 实例对象类
 *
 * @author generator
 * @date 2019-04-15 20:09:16
 * @version V1.0
 */
@Data
@TableName("SYS_AUTH_MIX")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SysAuthMixDO extends AbstractBaseDomain {

        /**
         * 权限id
         */
        private String authId;
        /**
         * 用户id
         */
        private String userId;
        /**
         * 签名
         */
        private String sign;
        /**
         * 应用ID
         */
        private String appId;

}

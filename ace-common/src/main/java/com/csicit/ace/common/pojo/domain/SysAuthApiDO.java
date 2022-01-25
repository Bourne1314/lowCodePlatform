package com.csicit.ace.common.pojo.domain;

import com.csicit.ace.common.pojo.AbstractBaseDomain;
import com.baomidou.mybatisplus.annotation.TableName;
import com.csicit.ace.common.pojo.AbstractBaseDomain;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

/**
 * 权限api关系表 实例对象类
 *
 * @author generator
 * @date 2019-04-15 20:09:12
 * @version V1.0
 */
@Data
@TableName("SYS_AUTH_API")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SysAuthApiDO extends AbstractBaseDomain {

        /**
         * 权限id
         */
        private String authId;
        /**
         * api资源id
         */
        private String apiId;
        /**
         * 签名
         */
        private String sign;
        /**
         * 跟踪ID
         */
        private String traceId;
}

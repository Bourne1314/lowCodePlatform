package com.csicit.ace.common.pojo.domain;

import com.csicit.ace.common.pojo.AbstractBaseDomain;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

/**
 * 有效API权限 实例对象类
 *
 * @author generator
 * @date 2019-04-15 20:05:05
 * @version V1.0
 */
@Data
@TableName("SYS_API_MIX")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SysApiMixDO extends AbstractBaseDomain {

        /**
         * 用户id
         */
        private String userId;
        /**
         * api资源id
         */
        private String apiId;
        /**
         * 请求路径
         */
        private String apiUrl;
        /**
         * 请求方式
         */
        private String apiMethod;
        /**
         * 签名
         */
        private String sign;
        /**
         * 权限ID
         */
        private String authId;
        /**
         * 应用ID
         */
        private String appId;

}

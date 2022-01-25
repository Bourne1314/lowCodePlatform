package com.csicit.ace.common.pojo.domain;

import com.csicit.ace.common.pojo.AbstractBaseDomain;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

/**
 * api资源 实例对象类
 *
 * @author generator
 * @date 2019-04-15 20:05:11
 * @version V1.0
 */
@Data
@TableName("SYS_API_RESOURCE")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SysApiResourceDO extends AbstractBaseDomain {

        /**
         * api描述
         */
        private String name;
        /**
         * 集团应用标识
         */
        private String appId;
        /**
         * 请求路径
         */
        private String apiUrl;
        /**
         * 请求方式
         */
        private String apiMethod;
        /**
         * 跟踪ID
         */
        private String traceId;

}

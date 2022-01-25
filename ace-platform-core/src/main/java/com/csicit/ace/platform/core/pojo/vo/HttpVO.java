package com.csicit.ace.platform.core.pojo.vo;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

/**
 * @author shanwj
 * @date 2019/9/12 9:40
 */
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class HttpVO {
    /**
     * 请求协议头
     */
    String Scheme;
    /**
     * 主机ip
     */
    String host;
    /**
     * 端口
     */
    int port;
    /**
     * 应用id
     */
    String appId;
    /**
     * 查询参数
     */
    String query;
}

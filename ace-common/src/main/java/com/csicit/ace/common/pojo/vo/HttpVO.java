package com.csicit.ace.common.pojo.vo;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

/**
 * @author shanwj
 * @date 2019/9/16 8:38
 */
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class HttpVO {
    private String scheme;
    private String http;
    private String host;
    private String appId;
    private int port;
}

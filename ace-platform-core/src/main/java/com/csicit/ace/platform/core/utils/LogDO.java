package com.csicit.ace.platform.core.utils;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

/**
 * @author yansiyang
 * @version V1.0
 * @date 2019/11/29 8:28
 */
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class LogDO {

    public LogDO() {

    }

    public LogDO(String time, int level, String info) {
        this.info = info;
        this.time = time;
        this.level = level;
    }
    /**
     * 时间
     */
    private String time;

    /**
     * 信息
     */
    private int level;

    /**
     * 内容
     */
    private String info;
}

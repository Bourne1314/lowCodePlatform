package com.csicit.ace.common.pojo.vo;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

/**
 * @author shanwj
 * @date 2019/9/9 16:31
 * key-value视图对象
 */
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class KeyValueVO {

    public KeyValueVO() {

    }

    public KeyValueVO(String key, String value) {
        this.key = key;
        this.value = value;
    }

    /**
     * key值
     */
    private String key;
    /**
     * Value值
     */
    private String value;

}

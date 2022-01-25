package com.csicit.ace.common.pojo.vo;

import lombok.Data;
import org.apache.commons.lang.StringUtils;

import java.util.Objects;

/**
 * 计划任务视图对象
 *
 * @author shanwj
 * @date 2020/4/14 8:10
 */
@Data
public class ScheduledVO {

    /**
     * 任务名称
     */
    private String name;
    /**
     * 执行请求路径
     */
    private String url;
    /**
     * 应用id
     */
    private String appId;
    /**
     * 任务组
     */
    private String group;

    @Override
    public boolean equals(Object obj) {
        if(this == obj){
            //地址相等
            return true;
        }
        if(obj == null){
            //非空性：对于任意非空引用x，x.equals(null)应该返回false。
            return false;
        }
        if(obj instanceof ScheduledVO){
            ScheduledVO other = (ScheduledVO) obj;
            //需要比较的字段相等，则这两个对象相等
            if(Objects.equals(this.name, other.name) && Objects.equals(this.group, other.group)
                    && Objects.equals(this.appId, other.appId)){
                return true;
            }
        }
        return false;
    }

}

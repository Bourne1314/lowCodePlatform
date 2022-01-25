package com.csicit.ace.common.annotation;

import java.lang.annotation.*;

/**
 * 配置项注解
 * 项目启动时扫描保存到数据库
 * @author FourLeaves
 * @version V1.0
 * @date 2019/12/24 15:49
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface AceConfigField {
    /**
     * 属性名，如不填写则默认为全局变量的名称
     */
    String name() default "";
    /**
     * 属性默认值，必填项
     */
    String defaultValue();
    /**
     * 配置项说明
     */
    String remark() default "";
    /**
     * 配置项更新策略 0立即更新 1重启更新
     */
    int updateType() default 0;

    /**
     * 配置范围的数组,，1租户、2集团、3应用
     * 1、平台应用后台默认添加租户级，若包含集团级，则会在每个集团下创建对应配置项
     * 2、非平台应用，后台自动添加应用级
     * 3、相同属性名的配置项，取配置范围大的
     */
    int[] scopes() default {3};
}

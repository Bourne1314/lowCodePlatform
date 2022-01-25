package com.csicit.ace.common.pojo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 日志数据记录模型
 * 如果要继承该类使用Java8 LocalDateTime，
 * 需要在项目中使用依赖
 * <dependency>
 *    <groupId>org.mybatis</groupId>
 *     artifactId>mybatis-typehandlers-jsr310</artifactId>
 * </dependency>
 * @author shanwj
 * @date 2019-03-29 10:37:46
 * @version V1.0
 */
@Data
public class AbstractBaseRecordDomain extends AbstractBaseDomain {
    /**
     * 创建人id
     */
    private String createUser;
    /**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;
    /**
     * 最后一次修改时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updateTime;

    /**
     * 备注
     */
    private String remark;
}

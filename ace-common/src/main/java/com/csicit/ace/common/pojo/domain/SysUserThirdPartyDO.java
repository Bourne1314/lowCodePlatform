package com.csicit.ace.common.pojo.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import javax.persistence.Id;
import javax.persistence.Version;
import java.time.LocalDateTime;

/**
 * @author yansiyang
 * @version V1.0
 * @date 2019/11/25 15:22
 */
@Data
@TableName("SYS_USER_THIRD_PARTY")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SysUserThirdPartyDO {
    /**
     * 主键
     */
    @Id
    @TableId(type = IdType.UUID)
    private String id;
    /**
     *  用户主键
     */
    private String userId;
    /**
     *  第三方账号类型 同小程序类型一致
     */
    private String type;
    /**
     * 第三方账号类型名称
     */
    private String typeName;
    /**
     * 第三方账号
     */
    private String account;

    /**
     * 数据版本
     */
    @Version
    private Integer dataVersion;
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
}

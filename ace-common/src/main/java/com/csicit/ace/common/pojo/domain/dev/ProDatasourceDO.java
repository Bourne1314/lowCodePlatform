package com.csicit.ace.common.pojo.domain.dev;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import javax.persistence.Id;
import java.util.List;

/**
 * 数据源 实例对象类
 *
 * @author shanwj
 * @version V1.0
 * @date 2019-11-04 14:48:24
 */
@Data
@TableName("PRO_DATASOURCE")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ProDatasourceDO {

    /**
     * 主键
     */
    @Id
    @TableId(type = IdType.UUID)
    private String id;

    /**
     * 数据源名称
     */
    private String name;
    /**
     * 路径
     */
    private String url;
    /**
     * 数据库用户
     */
    private String userName;
    /**
     * 密码
     */
    private String password;
    /**
     * 驱动名称
     */
    private String driver;
    /**
     * 数据库类型
     */
    private String type;
    /**
     * SCHEME名称
     */
    private String scheme;
    /**
     * 备注
     */
    private String remark;
    /**
     * 服务ID
     */
    private String serviceId;
    /**
     * 表单是否存在附件
     */
    @TableField("IS_MAJOR")
    private Integer major;


    /** 创建数据库所用参数 */

    /**
     * 数据库存在判断：0已存在，1需创建
     */
    @TableField(exist = false)
    private Integer existFlg;

    /**
     * 数据库名称
     */
    @TableField(exist = false)
    private String dbName;

    /**
     * 数据库端口号
     */
    @TableField(exist = false)
    private String dbPort;

    /**
     * 数据库IP地址
     */
    @TableField(exist = false)
    private String dbIpAddress;

    /**
     * 数据库管理员名
     */
    @TableField(exist = false)
    private String dbUser;

    /**
     * 数据库管理员密码
     */
    @TableField(exist = false)
    private String dbPwd;
//
//    /**
//     * 表空间文件
//     */
//    @TableField(exist = false)
//    private String datafile;
}

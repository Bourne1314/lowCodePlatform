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
 * @date 2019-11-04 14:48:24
 * @version V1.0
 */
@Data
@TableName("META_DATASOURCE")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class MetaDatasourceDO{

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
         * 数据源表
         */
        @TableField(exist = false)
        private List<MetaTableDO> tables;
        /**
         * 数据源视图
         */
        @TableField(exist = false)
        private List<MetaViewDO> views;
}

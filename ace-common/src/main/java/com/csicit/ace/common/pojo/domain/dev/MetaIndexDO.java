package com.csicit.ace.common.pojo.domain.dev;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import javax.persistence.Id;

/**
 * 索引信息 实例对象类
 *
 * @author generator
 * @date 2019-06-03 14:49:12
 * @version V1.0
 */
@Data
@TableName("META_INDEX")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class MetaIndexDO{

        /**
         * 主键
         */
        @Id
        @TableId(type = IdType.UUID)
        private String id;
        /**
         * 数据表
         */
        private String tableId;
        /**
         * 名称
         */
        private String name;
        /**
         * 包含列
         */
        private String cols;
        /**
         * 是否唯一
         */
        @TableField("IS_UNIQUE")
        private Integer onlyOne;
        /**
         * 违例消息
         */
        private String alertMsg;
        /**
         * 表名
         */
        @TableField(exist = false)
        private String tableName;
}

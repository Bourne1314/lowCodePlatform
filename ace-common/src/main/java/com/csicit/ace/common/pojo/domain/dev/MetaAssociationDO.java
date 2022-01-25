package com.csicit.ace.common.pojo.domain.dev;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import javax.persistence.Id;

/**
 * 数据关联定义 实例对象类
 *
 * @author generator
 * @date 2019-06-03 14:47:57
 * @version V1.0
 */
@Data
@TableName("META_ASSOCIATION")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class MetaAssociationDO {

        /**
         * 主键
         */
        @Id
        @TableId(type = IdType.UUID)
        private String id;
        /**
         * 关联名
         */
        private String name;
        /**
         * 表Id
         */
        private String tableId;
        /**
         * 引用索引ID
         */
        private String refIndexId;
        /**
         * 引用值删除时
         */
        private String deleteAction;
        /**
         * 外键列
         */
        private String columns;
        /**
         * 引用表id
         */
        private String refTableId;
        /**
         * 是否同步创建索引
         */
        @TableField(exist = false)
        private int createFk;
        /**
         * 唯一性索引
         */
        @TableField(exist = false)
        private int indexOne;
        /**
         * 数据源Id
         */
        @TableField(exist = false)
        private String tableName;
        /**
         * 引用索引名
         */
        @TableField(exist = false)
        private String refIndexName;
        /**
         * 引用表名
         */
        @TableField(exist = false)
        private String refTableName;
}

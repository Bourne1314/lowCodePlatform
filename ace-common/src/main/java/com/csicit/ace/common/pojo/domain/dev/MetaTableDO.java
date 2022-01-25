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
 * 数据表 实例对象类
 *
 * @author shanwj
 * @date 2019-11-04 14:49:11
 * @version V1.0
 */
@Data
@TableName("META_TABLE")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class MetaTableDO{

        /**
         * 主键
         */
        @Id
        @TableId(type = IdType.UUID)
        private String id;
        /**
         * 表名
         */
        private String tableName;
        /**
         * 实体
         */
        private String objectName;
        /**
         * 标题
         */
        private String caption;
        /**
         * 数据源ID
         */
        private String dsId;
        /**
         * 备注
         */
        private String remark;
        /**
         * 列
         */
        @TableField(exist = false)
        private List<MetaTableColDO> cols;

        @TableField(exist = false)
        private List<MetaIndexDO> indexList;

        @TableField(exist = false)
        private List<MetaAssociationDO> assList;

        /**
         * 启用树形结构（新增时使用）
         */
        @TableField(exist = false)
        private int treeFlag;
        /**
         * 启用版本控制（新增时使用）
         */
        @TableField(exist = false)
        private int dataVersionFlag;
        /**
         * 启用密级（新增时使用）
         */
        @TableField(exist = false)
        private int secretFlag;
}

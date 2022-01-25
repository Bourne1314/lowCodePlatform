package com.csicit.ace.common.pojo.domain.dev;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import javax.persistence.Id;

/**
 * 视图列 实例对象类
 *
 * @author generator
 * @date 2019-06-03 14:49:38
 * @version V1.0
 */
@Data
@TableName("META_VIEW_COL")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class MetaViewColDO {

        /**
         * 主键
         */
        @Id
        @TableId(type = IdType.UUID)
        private String id;
        /**
         * 名称
         */
        private String name;
        /**
         * 标题
         */
        private String caption;
        /**
         * 视图
         */
        private String viewId;
        /**
         * 是否系统列
         */
        @TableField("IS_SYSCOL")
        private Integer syscol;
        /**
         * 序号
         */
        private Integer sortIndex;
        /**
         * 字典
         */
        private String dictId;
        /**
         * 数据类型
         */
        private String dataType;

}

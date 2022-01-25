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
 * 视图 实例对象类
 *
 * @author generator
 * @date 2019-06-03 14:49:28
 * @version V1.0
 */
@Data
@TableName("META_VIEW")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class MetaViewDO {

        /**
         * 主键
         */
        @Id
        @TableId(type = IdType.UUID)
        private String id;
        /**
         * 视图名称
         */
        private String name;
        /**
         * 标题
         */
        private String caption;
        /**
         * 应用程序
         */
        private String dsId;
        /**
         * 视图SQL
         */
        private String sqlStr;
        /**
         * 视图列
         */
        @TableField(exist = false)
        private List<MetaViewColDO> cols;

}

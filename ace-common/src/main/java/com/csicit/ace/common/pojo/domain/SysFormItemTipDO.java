package com.csicit.ace.common.pojo.domain;

import com.csicit.ace.common.pojo.AbstractBaseRecordDomain;
import com.baomidou.mybatisplus.annotation.TableName;
import com.csicit.ace.common.pojo.AbstractBaseRecordDomain;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

/**
 * 表单字段提示信息表 实例对象类
 *
 * @author generator
 * @date 2019-04-15 20:12:28
 * @version V1.0
 */
@Data
@TableName("SYS_FORM_ITEM_TIP")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SysFormItemTipDO extends AbstractBaseRecordDomain {

        /**
         * 表单id
         */
        private String formId;
        /**
         * 表单标题
         */
        private String formTitle;
        /**
         * 字段数据name
         */
        private String dataName;
        /**
         * 字段名称
         */
        private String dataField;
        /**
         * 提示信息
         */
        private String tip;
        /**
         * 菜单编号
         */
        private String menuId;
        /**
         * 菜单名称
         */
        private String menuName;

}

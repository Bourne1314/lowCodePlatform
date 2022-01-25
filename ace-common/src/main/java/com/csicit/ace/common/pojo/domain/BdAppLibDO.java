package com.csicit.ace.common.pojo.domain;

import com.csicit.ace.common.pojo.AbstractBaseDomain;
import com.baomidou.mybatisplus.annotation.TableName;
import com.csicit.ace.common.pojo.AbstractBaseDomain;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

/**
 * 基础应用产品库 实例对象类
 *
 * @author generator
 * @date 2019-04-15 17:24:50
 * @version V1.0
 */
@Data
@TableName("BD_APP_LIB")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class BdAppLibDO extends AbstractBaseDomain {

	/**
	 * 应用名称
	 */
	private String name;
	/**
	 * 版本号
	 */
	private String version;
	/**
	 * 排序号
	 */
	private Integer sortIndex;
	/**
	 * 应用图标
	 */
	private String icon;

	/**
	 * 前后端分离 0 否 1是
	 */
	private Integer hasUi;

	/**
	 * 前端服务名称
	 */
	private String uiName;

}

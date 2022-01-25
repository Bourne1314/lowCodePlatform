package com.csicit.ace.common.pojo.domain;

import com.csicit.ace.common.pojo.AbstractBaseRecordDomain;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * 基础数据-人员证件类型 实例对象类
 *
 * @author generator
 * @date 2019-04-15 17:27:06
 * @version V1.0
 */
@Data
@TableName("BD_PERSON_ID_TYPE")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class BdPersonIdTypeDO extends AbstractBaseRecordDomain{

	/**
	 * 编码
	 */
	private String code;
	/**
	 * 名称
	 */
	private String name;
	/**
	 * 所属集团主键
	 */
	private String groupId;
	/**
	 * 校验规则
	 */
	private String regex;
	/**
	 * 国家
	 */
	private String country;
	/**
	 * 描述
	 */
	private String description;
	/**
	 * 证件号长度
	 */
	private Integer numberLength;
	/**
	 * 启用状态
	 */
	private Integer enableState;

}

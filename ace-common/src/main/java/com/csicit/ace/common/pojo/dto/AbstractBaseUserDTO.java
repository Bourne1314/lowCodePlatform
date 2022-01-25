package com.csicit.ace.common.pojo.dto;

import com.csicit.ace.common.pojo.AbstractSecretRecordDomain;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

/**
 * 用户通用领域模型
 *
 * @author shanwj
 * @date 2019-04-10 10:37:46
 * @version V1.0
 */
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public abstract class AbstractBaseUserDTO extends AbstractSecretRecordDomain {

    //姓名
    private String name;
    //用户名
    private String userName;
    //部门Id
    private String deptId;

}

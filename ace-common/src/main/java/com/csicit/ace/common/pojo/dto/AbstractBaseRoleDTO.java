package com.csicit.ace.common.pojo.dto;

import com.csicit.ace.common.pojo.AbstractSecretRecordDomain;
import lombok.Data;

/**
 * 角色通用领域模型
 *
 * @author shanwj
 * @date 2019-04-10 10:37:46
 * @version V1.0
 */
@Data
public class AbstractBaseRoleDTO extends AbstractSecretRecordDomain {
    //角色名称
    private String name;
}

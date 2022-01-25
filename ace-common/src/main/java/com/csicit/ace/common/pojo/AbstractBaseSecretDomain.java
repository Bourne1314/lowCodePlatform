package com.csicit.ace.common.pojo;

import lombok.Data;

/**
 * 涉密对象模型
 *
 * @author shanwj
 * @date 2019-03-29 10:37:46
 * @version V1.0
 */
@Data
public class AbstractBaseSecretDomain extends AbstractBaseDomain{
    /**
     * 密级
     */
    private Integer secretLevel;
}

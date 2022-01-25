package com.csicit.ace.common.pojo;

import lombok.Data;

/**
 * 涉密记录日志模型
 *
 * @author shanwj
 * @date 2019-03-29 10:37:46
 * @version V1.0
 */
@Data
public class AbstractSecretRecordDomain extends AbstractBaseRecordDomain{
    /**
     *  密级
     *  5非密
     *  4内部
     *  3秘密
     *  2机密
     *  1绝密
     */
    private Integer secretLevel;
}

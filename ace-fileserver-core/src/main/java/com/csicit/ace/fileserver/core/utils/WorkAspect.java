package com.csicit.ace.fileserver.core.utils;

import io.seata.core.context.RootContext;
import io.seata.tm.api.GlobalTransactionContext;
import org.apache.commons.lang.StringUtils;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.transaction.TransactionException;

import java.lang.reflect.Method;

/**
 * @Description TODO
 * @Author JR-zhangzhaojun
 * @DATE 2021/12/17
 * @Param
 * @return
 * @Version 1.0
 */
@Aspect
@Component
public class WorkAspect {

    private final static Logger logger = LoggerFactory.getLogger(WorkAspect.class);

    @AfterThrowing(throwing = "e", pointcut = "execution(* com.csicit.ace.fileserver.core.service.impl.*.*(..))")
    public void doRecoveryActions(Throwable e) throws TransactionException, io.seata.core.exception.TransactionException {
        logger.info("方法执行异常:{}", e.getMessage());
        if (!StringUtils.isBlank(RootContext.getXID())) {
            GlobalTransactionContext.reload(RootContext.getXID()).rollback();
        }
    }

}
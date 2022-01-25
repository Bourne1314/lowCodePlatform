package com.csicit.ace.bpm.utils;

import io.seata.core.context.RootContext;
import io.seata.core.exception.TransactionException;
import io.seata.tm.api.GlobalTransaction;
import io.seata.tm.api.GlobalTransactionContext;
import org.apache.commons.lang.StringUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

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


    @AfterThrowing(throwing = "e", pointcut = "execution(* com.csicit.ace.bpm.activiti.impl.*.*(..))")
    public void doRecoveryActions(Throwable e) throws TransactionException {
        logger.info("方法执行异常:{}", e.getMessage());
        if (!StringUtils.isBlank(RootContext.getXID())) {
            GlobalTransactionContext.reload(RootContext.getXID()).rollback();
        }
    }
    @AfterThrowing(throwing = "e", pointcut = "execution(* com.example.xltest.flowEvents.*.*(..))")
    public void doRecoveryActions1(Throwable e) throws TransactionException {
        logger.info("方法执行异常:{}", e.getMessage());
        if (!StringUtils.isBlank(RootContext.getXID())) {
            GlobalTransactionContext.reload(RootContext.getXID()).rollback();
        }
    }


}
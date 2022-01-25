package com.csicit.ace.common.aspect;

import com.alibaba.fastjson.JSONObject;
import com.csicit.ace.common.annotation.AceReqMock;
import com.csicit.ace.common.utils.JsonUtils;
import com.csicit.ace.common.utils.server.R;
import com.csicit.ace.common.utils.system.SecurityUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.Arrays;

/**
 * @author FourLeaves
 * @version V1.0
 * @date 2020/3/25 15:49
 */
@Aspect
@Component
@ConditionalOnExpression("!'${spring.application.name}'.endsWith('gateway')")
public class AceReqMockAspect {

    @Autowired
    SecurityUtils securityUtils;

    /**
     * 切点
     * 两个..代表所有子目录，最后括号里的两个..代表所有参数
     *
     * @author shanwj
     * @date 2019/4/12 14:48
     */
    @Pointcut("execution( * *..controller.WxAppController.*(..))")
    public void printPointCutController() {

    }

    @Pointcut("@annotation(com.csicit.ace.common.annotation.AceReqMock)")
    public void printPointCutAceReqMock() {

    }

    @Pointcut("printPointCutController() && printPointCutAceReqMock()")
    public void printPointCut() {

    }

    @Around("printPointCut()")
    public Object doAround(ProceedingJoinPoint pjp) throws Throwable {
        String methodName = pjp.getSignature().getName();
        try {
            Method method = pjp.getTarget().getClass().getMethod(methodName);
            AceReqMock aceReqMock = method.getDeclaredAnnotation(AceReqMock.class);
            String[] userNames = aceReqMock.userNames();
            boolean hasUserName = false;
            if (userNames.length > 0) {
                if (Arrays.asList(userNames).contains(securityUtils.getCurrentUserName())) {
                    hasUserName = true;
                }
            }
            if (aceReqMock != null && hasUserName) {
                String data = aceReqMock.data();
                JSONObject jsonObject = JsonUtils.castObject(data, JSONObject.class);
                R r = R.ok();
                for (String key : jsonObject.keySet()) {
                    r.put(key, jsonObject.get(key));
                }
                return r;
            } else {
                Object o = pjp.proceed();
                return o;
            }
        } catch (Exception e) {
            return R.ok();
        }
    }

}

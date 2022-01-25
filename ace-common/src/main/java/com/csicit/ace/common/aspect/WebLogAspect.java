package com.csicit.ace.common.aspect;

import com.alibaba.fastjson.JSONObject;
import com.csicit.ace.common.log.AceLogger;
import com.csicit.ace.common.utils.IpUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.Date;

/**
 * controller日志打印切面
 *
 * @author shanwj
 * @version V1.0
 * @date 2019-04-10 18:57:46
 */
@Aspect
@Component
@ConditionalOnExpression("!'${spring.application.name}'.endsWith('gateway')")
public class WebLogAspect {
    /**
     * 日志框架对象
     */

    @Autowired
    AceLogger logger;

    @Autowired
    HttpServletRequest request;

    /**
     * 切点
     * 两个..代表所有子目录，最后括号里的两个..代表所有参数
     *
     * @author shanwj
     * @date 2019/4/12 14:48
     */
    @Pointcut("execution( * *..controller.*.*(..))")
    public void printPointCutController() {

    }

    @Pointcut("@annotation(org.springframework.web.bind.annotation.RequestMapping)")
    public void printPointCutRequestMapping() {

    }

    @Pointcut("printPointCutController() && printPointCutRequestMapping()")
    public void printPointCut() {

    }

    /**
     * 方法执行前
     *
     * @param joinPoint 切点
     * @author shanwj
     * @date 2019/4/12 14:49
     */
    @Before("printPointCut()")
    public void doBefore(JoinPoint joinPoint) {
        // 接收到请求，记录请求内容
//        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
//        HttpServletRequest request = attributes.getRequest();

        String id = (new Date().getTime() + "").substring(8);
        request.setAttribute("aceIndex", id);
        String info;
        String url = request.getRequestURL().toString();
        // 登录密码不可见
        if (url.endsWith("login")) {
            info = " " + id + "--请求地址: " + url
                    + " ,HTTP_METHOD: " + request.getMethod()
                    + " ,IP: " + IpUtils.getFirstIpAddress(request)
                    + " ,CLASS_METHOD: " + joinPoint.getSignature().getDeclaringTypeName() + "."
                    + joinPoint.getSignature().getName()
                    + " ,参数: " + Arrays.toString(joinPoint.getArgs()).split("userName=")[1].split(",")[0];
        } else {
            info = " " + id + "--请求地址: " + url
                    + " ,HTTP_METHOD: " + request.getMethod()
                    + " ,IP: " + IpUtils.getFirstIpAddress(request)
                    + " ,CLASS_METHOD: " + joinPoint.getSignature().getDeclaringTypeName() + "."
                    + joinPoint.getSignature().getName()
                    + " ,请求参数: " + JSONObject.toJSONString(request.getParameterMap());
        }

        // 记录下请求内容
        logger.info(info);
//        logger.info("请求地址 : " + request.getRequestURL().toString());
//        logger.info("HTTP METHOD : " + request.getMethod());
//        // 获取真实的ip地址
//        logger.info("IP : " + IpUtils.getFirstIpAddress(request));
//        logger.info("CLASS_METHOD : " + joinPoint.getSignature().getDeclaringTypeName() + "."
//                + joinPoint.getSignature().getName());
//        logger.info("参数 : " + Arrays.toString(joinPoint.getArgs()));
    }

    /**
     * 方法执行后执行
     * returning的值和doAfterReturning的参数名一致
     *
     * @param ret 返回值对象
     */
    @AfterReturning(returning = "ret", pointcut = "printPointCut()")
    public void doAfterReturning(Object ret) {
        // 处理完请求，返回内容(返回值太复杂时，打印的是物理存储空间的地址)
        logger.debug("返回值 : " + ret);
    }

    /**
     * 方法中执行
     *
     * @param pjp 环绕通知
     * @return java.lang.Object
     * @author shanwj
     * @date 2019/4/12 15:01
     */
    @Around("printPointCut()")
    public Object doAround(ProceedingJoinPoint pjp) throws Throwable {
        long startTime = System.currentTimeMillis();
        // ob 为方法的返回值
        Object ob = pjp.proceed();
        String id = request.getAttribute("aceIndex").toString();
        logger.info(" " + id + "--耗时 : " + (System.currentTimeMillis() - startTime));
        return ob;
    }
}

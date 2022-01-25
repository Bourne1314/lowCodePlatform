package com.csicit.ace.common.aspect;

import com.csicit.ace.common.annotation.AceAuth;
import com.csicit.ace.common.constant.Constants;
import com.csicit.ace.common.exception.RException;
import com.csicit.ace.common.utils.CacheUtil;
import com.csicit.ace.common.utils.internationalization.InternationUtils;
import com.csicit.ace.common.utils.system.SecurityUtils;
import org.apache.commons.collections.CollectionUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Objects;


/**
 * 服务调用权限验证切面
 *
 * @author shanwj
 * @version V1.0
 * @date 2019-04-10 18:57:46
 */
@Aspect
@Component
@ConditionalOnExpression("!'${spring.application.name}'.endsWith('gateway')")
public class ServiceCallAspect {

    /**
     * 应用名称
     */
    @Value("${spring.application.name}")
    private String appName;


    @Autowired
    SecurityUtils securityUtils;

    @Autowired
    CacheUtil cacheUtil;

    /**
     * 定义切点
     *
     * @author shanwj
     * @date 2019/4/11 20:04
     */
    @Pointcut("@annotation(com.csicit.ace.common.annotation.AceAuth)")
    public void serviceCallPointCut() {

    }

    /**
     * 环绕处理验证方法权限
     *
     * @param point 切点
     * @return 执行完成对象
     * @throws Throwable 运行异常
     */
    @Around("serviceCallPointCut()")
    public Object around(ProceedingJoinPoint point) throws Throwable {
        try {
            if (Objects.equals(Constants.ACE_SERVICE_USER, securityUtils.getCurrentUser().getUserName())) {
                return point.proceed();
            }
        } catch (Exception e) {

        }
        /**
         * 是否开启 AceAuth
         */
//        String openAceAuth = cacheUtil.get("openAceAuth");
//        if (Objects.equals(openAceAuth, "yes")) {
        MethodSignature signature = (MethodSignature) point.getSignature();
        Method method = signature.getMethod();
        AceAuth aceAuth = method.getAnnotation(AceAuth.class);
        if (aceAuth != null) {
            // API
            String api = method.getDeclaringClass().getName() + "." + method.getName() + "_" + appName;
            // 缓存里面 以应用名 + apis 为key 放了SYS_AUTH_API的数据 api_id为键 auth_id为值
            if (!cacheUtil.hHasKey(appName + "-apis", api)) {
                // 如果没有auth_api关系，证明api没绑定权限，放行
                return point.proceed();
            }
            List<String> apis = securityUtils.getApis();
            if (CollectionUtils.isEmpty(apis)) {
                throw new RException(InternationUtils.getInternationalMsg("NOT_HAVE_AUTH"));
            }
            boolean isAuth = apis.stream().parallel().anyMatch(s -> Objects.equals(s, api));
            if (isAuth) {
                Object result = point.proceed();
                return result;
            } else {
                throw new RException(InternationUtils.getInternationalMsg("NOT_HAVE_AUTH"));
            }
        } else {
            throw new RException("方法未定义权限标识");
        }
//        } else {
//            return point.proceed();
//        }
    }
}

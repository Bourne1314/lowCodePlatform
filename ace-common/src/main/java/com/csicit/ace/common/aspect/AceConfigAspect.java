package com.csicit.ace.common.aspect;

import com.csicit.ace.common.annotation.AceConfigClass;
import com.csicit.ace.common.annotation.AceConfigField;
import com.csicit.ace.common.constant.Constants;
import com.csicit.ace.common.utils.StringUtils;
import com.csicit.ace.common.utils.CacheUtil;
import com.csicit.ace.common.utils.system.SecurityUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.reflections.Reflections;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * @author FourLeaves
 * @version V1.0
 * @date 2019/12/25 15:20
 */
@Aspect
@Component
@ConditionalOnExpression("!'${spring.application.name}'.endsWith('gateway')")
public class AceConfigAspect {

    /**
     * 应用名称
     */
    @Value("${spring.application.name}")
    private String appName;

    @Autowired
    CacheUtil cacheUtil;

    @Autowired
    SecurityUtils securityUtils;

    static DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    static DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

//    @Pointcut("@annotation(com.csicit.ace.common.annotation.AceConfigMethod)")
//    public void printPointCutAceConfig() {
//    }

    @Pointcut("target(com.csicit.ace.common.service.AceConfigService)")
    public void printPointCutAceConfigService() {
    }

    @Pointcut("execution(* *getConfig(..))")
    public void printPointCutAceConfigMethod() {
    }

    @Pointcut("printPointCutAceConfigService() && printPointCutAceConfigMethod()")
    public void printPointCutAceConfig() {
    }

    @Before("printPointCutAceConfig()")
    public void doBefore(JoinPoint joinPoint) throws Exception {
        Object[] getArgs = joinPoint.getArgs();
        String key = (String) getArgs[0];
        setAceConfigClass(key);
//        Class t = joinPoint.getTarget().getClass();
//        Field[] fields = t.getFields();
//        for (Field field : fields) {
//            AceConfigField aceConfigField = field.getAnnotation(AceConfigField.class);
//            if (aceConfigField != null) {
//                // 获取key名称
//                String key = aceConfigField.name();
//                if (StringUtils.isBlank(key)) {
//                    key = field.getName();
//                }
//                // 从缓存里获取值
//                /**
//                 * 需要判断服务是否是平台的服务
//                 */
//                if (Constants.AppNames.contains(appName)) {
//                    // 是平台的服务
//                    Set<Integer> scopeSet = new HashSet<>();
//                    for (int i : aceConfigField.scopes()) {
//                        if (i >= 1 && i <= 3) {
//                            scopeSet.add(i);
//                        }
//                    }
//                    if (scopeSet.contains(2)) {
//                        String groupId = securityUtils.getCurrentGroupId();
//                        String value = null;
//                        if (StringUtils.isNotBlank(groupId)) {
//                            value = (String) redisUtils.hget(groupId, appName + "-" + key);
//                        }
//                        // 取租户级的数据
//                        if (StringUtils.isBlank(value)) {
//                            value = redisUtils.get(key);
//                        }
//                        setFieldValue(value, joinPoint, aceConfigField, field);
//                    }
//
//                } else {
//                    // 不是平台的服务
//                    String value = redisUtils.getConfigValue(key);
//                    setFieldValue(value, joinPoint, aceConfigField, field);
//                }
//            }
//        }
    }

    private static Set<Class<?>> classes = new HashSet<>();
    private static Set<Field> fields = new HashSet<>();

    /**
     * 给配置类里面的静态常量赋值
     *
     * @return
     * @author FourLeaves
     * @date 2019/12/26 16:50
     */
    public void setAceConfigClass(String key) throws Exception {
        if (fields.size() == 0) {
            Reflections reflections = new Reflections(Constants.BasePackages);
            classes.addAll(reflections.getTypesAnnotatedWith(AceConfigClass.class));

            for (Class classT : classes) {
                Field[] fieldss = classT.getDeclaredFields();
                for (Field field : fieldss) {
                    fields.add(field);
                }
            }
        }
        for (Field field : fields) {
            AceConfigField aceConfigField = field.getAnnotation(AceConfigField.class);
            // 只能给 静态 变量赋值
            if (aceConfigField != null && Modifier.isStatic(field.getModifiers()) && Modifier.isPublic(field.getModifiers())) {
                // 注解若不为空
                // 注解的name和key不一致
                if (StringUtils.isNotBlank(aceConfigField.name())) {
                    if (!Objects.equals(aceConfigField.name(), key)) {
                        continue;
                    }
                } else {
                    // 注解若为空
                    // 字段name和key不一致
                    if (!Objects.equals(field.getName(), key)) {
                        continue;
                    }
                }
                // 从缓存里获取值
                /**
                 * 需要判断服务是否是平台的服务
                 */
                if (Constants.AppNames.contains(appName)) {
                    // 是平台的服务
                    Set<Integer> scopeSet = new HashSet<>();
                    for (int i : aceConfigField.scopes()) {
                        if (i >= 1 && i <= 3) {
                            scopeSet.add(i);
                        }
                    }
                    if (scopeSet.contains(2)) {
                        String groupId = securityUtils.getCurrentGroupId();
                        String value = null;
                        if (StringUtils.isNotBlank(groupId)) {
                            value = (String) cacheUtil.hget(groupId, appName + "-" + key);
                        }
                        // 取租户级的数据
                        if (StringUtils.isBlank(value)) {
                            value = cacheUtil.get(key);
                        }
                        setFieldValue(value, null, aceConfigField, field);
                    }

                } else {
                    // 不是平台的服务
                    String value = cacheUtil.getConfigValue(key);
                    setFieldValue(value, null, aceConfigField, field);
                }
            }
        }

    }


    /**
     * 给对象的属性设置值
     *
     * @param value          值
     * @param joinPoint      切点
     * @param aceConfigField 注解
     * @param field          属性
     * @return
     * @author FourLeaves
     * @date 2019/12/26 14:29
     */
    public void setFieldValue(String value, JoinPoint joinPoint, AceConfigField aceConfigField, Field field) throws Exception {
        if (StringUtils.isBlank(value)) {
            value = aceConfigField.defaultValue();
        }
        // private
//        field.setAccessible(true);

//        //去掉private修饰符
//        Field modifiersField = Field.class.getDeclaredField("modifiers");
//        modifiersField.setAccessible(true);
//        modifiersField.setInt(field, field.getModifiers() & ~Modifier.PRIVATE);

        if (field.getType() == String.class) {
            field.set(joinPoint == null ? null : joinPoint.getTarget(), StringUtils.isNotBlank(value) ? value : aceConfigField.defaultValue());
        } else if (field.getType() == Boolean.class) {
            Boolean i = true;
            if (StringUtils.isNotBlank(value)) {
                i = Boolean.parseBoolean(value);
            }
            field.set(joinPoint == null ? null : joinPoint.getTarget(), i);
        } else if (field.getType() == Integer.class) {
            Integer i = 0;
            if (StringUtils.isNotBlank(value)) {
                i = Integer.parseInt(value);
            }
            field.set(joinPoint == null ? null : joinPoint.getTarget(), i);
        } else if (field.getType() == Long.class) {
            Long i = 0L;
            if (StringUtils.isNotBlank(value)) {
                i = Long.parseLong(value);
            }
            field.set(joinPoint == null ? null : joinPoint.getTarget(), i);
        } else if (field.getType() == Double.class) {
            Double i = 0.0;
            if (StringUtils.isNotBlank(value)) {
                i = Double.parseDouble(value);
            }
            field.set(joinPoint == null ? null : joinPoint.getTarget(), i);
        } else if (field.getType() == LocalDateTime.class) {
            LocalDateTime i = LocalDateTime.now();
            if (StringUtils.isNotBlank(value)) {
                i = LocalDateTime.parse(value, dateTimeFormatter);
            }
            field.set(joinPoint == null ? null : joinPoint.getTarget(), i);
        } else if (field.getType() == LocalDate.class) {
            LocalDate i = LocalDate.now();
            if (StringUtils.isNotBlank(value)) {
                i = LocalDate.parse(value, dateFormatter);
            }
            field.set(joinPoint == null ? null : joinPoint.getTarget(), i);
        }
    }
}

package com.csicit.ace.common.service;

import com.csicit.ace.common.annotation.AceConfigClass;
import com.csicit.ace.common.annotation.AceConfigField;
import com.csicit.ace.common.constant.Constants;
import com.csicit.ace.common.utils.JsonUtils;
import com.csicit.ace.common.utils.StringUtils;
import org.reflections.Reflections;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * @author FourLeaves
 * @version V1.0
 * @date 2020/1/2 11:36
 */
@Service
public class AceConfigServiceImpl implements AceConfigService {

    private static Set<Class<?>> classes = new HashSet<>();

    @Override
    public <T> T getConfig(String key, Class<T> clazz) {
        try {
            if (classes.size() == 0) {
                Reflections reflections = new Reflections(Constants.BasePackages);
                classes.addAll(reflections.getTypesAnnotatedWith(AceConfigClass.class));
            }
            for (Class classT : classes) {
                Field[] fields = classT.getDeclaredFields();
                for (Field field : fields) {
                    AceConfigField aceConfigField = field.getAnnotation(AceConfigField.class);
                    // 只能给 静态 变量赋值
                    if (aceConfigField != null && Modifier.isStatic(field.getModifiers())) {
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
                        return JsonUtils.castObject(field.get(classT), clazz);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

}

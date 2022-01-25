package com.csicit.ace.common.utils;

import com.csicit.ace.common.annotation.AceConfigClass;
import org.reflections.Reflections;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;
import java.util.Set;

/**
 * @author shanwj
 * @date 2019/4/16 17:07
 */
public class ScanClassUtils {

    /**
     * 获取对应包下所有的Controller类以及配置项类
     * @param pkgName 包名
     * @param controllerClasses 存放Controller类
     * @param configClasses 存放配置类
     * @return
     * @author yansiyang
     * @date 2019/8/14 18:51
     */
    public static void getInitScanClass(String pkgName, Set<Class<?>> controllerClasses, Set<Class<?>> configClasses) {
        if (StringUtils.isNotBlank(pkgName)) {
            String[] pkgNames = pkgName.split(",");
            for (String name : pkgNames) {
                Reflections reflections = new Reflections(name);
                if(reflections!=null){
                    controllerClasses.addAll(reflections.getTypesAnnotatedWith(RestController.class));
                    controllerClasses.addAll(reflections.getTypesAnnotatedWith(Controller.class));
                    configClasses.addAll(reflections.getTypesAnnotatedWith(AceConfigClass.class));
                }
            }
        }
    }

    /**
     * 获取对应包下所有的Controller类以及配置项类
     * @param pkgName
     * @param classes
     * @return
     * @author yansiyang
     * @date 2019/8/14 18:51
     */
    public static void getInitScanClass(String pkgName, Set<Class<?>> classes) {
        String[] pkgNames = pkgName.split(",");
        for (String name:pkgNames){
            Reflections reflections = new Reflections(name);
            classes.addAll(reflections.getTypesAnnotatedWith(RestController.class));
            classes.addAll(reflections.getTypesAnnotatedWith(Controller.class));
        }
    }
    /**
     * 扫描包路径下的所有class文件
     *
     * @param pkgName 包名
     * @param pkgPath 包对应的绝对地址
     * @param classes 保存包路径下class的集合
     * @author shanwj
     * @date 2019/4/16 17:08
     */
    public static void setClassesSet(String pkgName, String pkgPath, Set<Class<?>> classes) {
        File dir = new File(pkgPath);
        if (!dir.exists() || !dir.isDirectory()) {
            return;
        }

        // 过滤获取目录，或者 class文件
        File[] dirfiles = dir.listFiles(pathname -> pathname.isDirectory() ||   pathname.getName().endsWith("class"));

        if (dirfiles == null || dirfiles.length == 0) {
            return;
        }

        String className;
        Class clz;
        for (File f : dirfiles) {
            if (f.isDirectory()) {
                setClassesSet(pkgName + "." + f.getName(),
                        pkgPath + "/" + f.getName(),
                        classes);
                continue;
            }

            // 获取类名，去除 ".class" 后缀
            className = f.getName();
            className = className.substring(0, className.length() - 6);

            // 加载类
            String loadclass = pkgName + "." + className;

            //调用类加载器
            clz = loadClass(loadclass);
            if (clz != null) {
                classes.add(clz);
            }
        }
    }

    //类加载器
    /**
     *
     * @param fullClzName 类全名
     * @return java.lang.Class<?> 类
     * @author shanwj
     * @date 2019/4/16 17:10
     */
    public static Class<?> loadClass(String fullClzName) {
        try {
            return Thread.currentThread().getContextClassLoader().loadClass(fullClzName);
        } catch (ClassNotFoundException e) {
            System.err.println("clz: " + fullClzName);
            System.err.println(e.getMessage());
        }
        return null;
    }
}

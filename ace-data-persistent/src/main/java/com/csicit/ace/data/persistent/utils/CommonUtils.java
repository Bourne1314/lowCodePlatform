package com.csicit.ace.data.persistent.utils;

import com.csicit.ace.common.AppUpgradeJaxb.GroupDatasourceDetail;
import com.csicit.ace.common.pojo.domain.SysGroupDatasourceDO;
import com.csicit.ace.data.persistent.service.SysGroupDatasourceService;
import org.springframework.beans.factory.annotation.Autowired;

import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.sql.Timestamp;
import java.util.*;

/**
 * 共同方法工具类
 *
 * @author zuogang
 * @return
 * @date 2020/8/7 9:20
 */
public class CommonUtils {

    /**
     * 比较两个实体属性值，如果有差分，则返回true
     *
     * @param obj1 当前业务数据库中表数据
     * @param obj2 应用升级 配置文件中的数据
     * @return boolean
     * @author zuogang
     * @date 2020/8/7 9:26
     */
    public static boolean compareFields(Object obj1, Object obj2) {
        try {
            boolean diffFlg = false;
            // 只有两个对象都是同一类型的才有可比性
            if (obj1.getClass() == obj2.getClass()) {
                Class clazz = obj2.getClass();
                // 获取object的属性描述
                PropertyDescriptor[] pds = Introspector.getBeanInfo(clazz,
                        Object.class).getPropertyDescriptors();
                // 这里就是所有的属性了
                for (PropertyDescriptor pd : pds) {
                    // 属性名
                    String name = pd.getName();
                    // 当前属性选择忽略比较，跳到下一次循环
                    if (Objects.equals(name, "traceId") || Objects.equals(name, "id")) {
                        continue;
                    }
                    // get方法
                    Method readMethod = pd.getReadMethod();
                    // 在obj1上调用get方法等同于获得obj1的属性值
                    Object o1 = readMethod.invoke(obj1);
                    // 在obj2上调用get方法等同于获得obj2的属性值
                    Object o2 = readMethod.invoke(obj2);
                    if (o1 instanceof Timestamp) {
                        o1 = new Date(((Timestamp) o1).getTime());
                    }
                    if (o2 instanceof Timestamp) {
                        o2 = new Date(((Timestamp) o2).getTime());
                    }
                    if (o1 == null && o2 == null) {
                        continue;
                    } else if (o1 == null && o2 != null) {
                        diffFlg = true;
                        break;
                    }
                    if (!o1.equals(o2)) {
                        // 比较这两个值是否相等
                        diffFlg = true;
                        break;
                    }
                }
            }
            return diffFlg;
        } catch (Exception e) {
            e.printStackTrace();
            return true;
        }
    }

}

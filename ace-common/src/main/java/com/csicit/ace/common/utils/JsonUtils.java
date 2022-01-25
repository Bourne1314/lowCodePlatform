package com.csicit.ace.common.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 对象转化工具类
 *
 * @Author: yansiyang
 * @Descruption:
 * @Date: Created in 14:36 2019/4/11
 * @Modified By:
 */
public class JsonUtils {

    /**
     * xml字符串 转JSON字符串
     * @param xml
     * @return 
     * @author FourLeaves
     * @date 2020/6/11 11:12
     */
    public static JSONObject xmlToJson(String xml) {
        JSONObject json = new JSONObject();
        try {
            Document doc = DocumentHelper.parseText(xml);
            dom4j2Json(doc.getRootElement(), json);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return json;
    }


    /**
     * xml文件 转JSON字符串
     * @param in
     * @return
     * @author FourLeaves
     * @date 2020/6/11 11:12
     */
    public static JSONObject xmlToJson(InputStream in) {
        JSONObject json = new JSONObject();
        try {
            String xml = IOUtils.toString(in);
            Document doc = DocumentHelper.parseText(xml);
            dom4j2Json(doc.getRootElement(), json);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return json;
    }

    /**
     * xml文件 转JSON字符串
     * @param file
     * @return
     * @author FourLeaves
     * @date 2020/6/11 11:12
     */
    public static JSONObject xmlToJson(File file) {
        JSONObject json = new JSONObject();
        try {
            InputStream in = new FileInputStream(file);
            String xml = IOUtils.toString(in);
            Document doc = DocumentHelper.parseText(xml);
            dom4j2Json(doc.getRootElement(), json);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return json;
    }

    /**
     * xml转json
     *
     * @param element
     * @param json
     */
    private static void dom4j2Json(Element element, JSONObject json) {
        //如果是属性
        for (Object o : element.attributes()) {
            Attribute attr = (Attribute) o;
            if (!org.springframework.util.StringUtils.isEmpty(attr.getValue())) {
                json.put("@" + attr.getName(), attr.getValue());
            }
        }
        List<Element> chdEl = element.elements();
        if (chdEl.isEmpty() && !org.springframework.util.StringUtils.isEmpty(element.getText())) {//如果没有子元素,只有一个值
            json.put(element.getName(), element.getText());
        }

        for (Element e : chdEl) {//有子元素
            if (!e.elements().isEmpty()) {//子元素也有子元素
                JSONObject chdjson = new JSONObject();
                dom4j2Json(e, chdjson);
                Object o = json.get(e.getName());
                if (o != null) {
                    JSONArray jsona = null;
                    if (o instanceof JSONObject) {//如果此元素已存在,则转为jsonArray
                        JSONObject jsono = (JSONObject) o;
                        json.remove(e.getName());
                        jsona = new JSONArray();
                        jsona.add(jsono);
                        jsona.add(chdjson);
                    }
                    if (o instanceof JSONArray) {
                        jsona = (JSONArray) o;
                        jsona.add(chdjson);
                    }
                    json.put(e.getName(), jsona);
                } else {
                    if (!chdjson.isEmpty()) {
                        json.put(e.getName(), chdjson);
                    }
                }


            } else {//子元素没有子元素
                for (Object o : element.attributes()) {
                    Attribute attr = (Attribute) o;
                    if (!org.springframework.util.StringUtils.isEmpty(attr.getValue())) {
                        json.put("@" + attr.getName(), attr.getValue());
                    }
                }
                if (!e.getText().isEmpty()) {
                    json.put(e.getName(), e.getText());
                }
            }
        }
    }

    /**
     * 判断字符串是否是Json格式
     *
     * @param content
     * @return
     * @author yansiyang
     * @date 2019/11/12 10:41
     */
    public static boolean isJson(String content) {
        try {
            JSONObject.parseObject(content);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * 转化对象
     *
     * @param object
     * @param t      目标类
     * @return
     * @author yansiyang
     * @date 2019/4/18 20:15
     */
    public static <T> T castObject(Object object, Class<T> t) {
        if (object == null) {
            return null;
        }
        String jsonStr;
        if (object instanceof String) {
            jsonStr = (String) object;
        } else {
            jsonStr = JSONObject.toJSONString(object);
        }
        if (t == String.class) {
            return (T) jsonStr;
        } else {
            return JSONObject.parseObject(jsonStr, t);
        }
    }

    /**
     * 转化对象 id置空
     *
     * @param object
     * @param t      目标类
     * @return
     * @author yansiyang
     * @date 2019/4/18 20:15
     */
    public static <T> T castObjectForSetIdNull(Object object, Class<T> t) {
        String jsonStr = JSONObject.toJSONString(object);
        JSONObject tempJsonObject = JSONObject.parseObject(jsonStr);
        if (tempJsonObject.containsKey("id")) {
            tempJsonObject.put("id", UuidUtils.createUUID());
            jsonStr = JSONObject.toJSONString(tempJsonObject);
        }
        return JSONObject.parseObject(jsonStr, t);
    }

    /**
     * Bean对象转JSON
     *
     * @param object
     * @param dataFormatString
     * @return
     */
    public static String beanToJson(Object object, String dataFormatString) {
        if (object != null) {
            if (StringUtils.isEmpty(dataFormatString)) {
                return JSONObject.toJSONString(object);
            }
            return JSON.toJSONStringWithDateFormat(object, dataFormatString);
        } else {
            return null;
        }
    }

    /**
     * Bean对象转JSON
     *
     * @param object
     * @return
     */
    public static String beanToJson(Object object) {
        if (object != null) {
            return JSON.toJSONString(object);
        } else {
            return null;
        }
    }

    /**
     * String转JSON字符串
     *
     * @param key
     * @param value
     * @return
     */
    public static String stringToJsonByFastjson(String key, String value) {
        if (StringUtils.isEmpty(key) || StringUtils.isEmpty(value)) {
            return null;
        }
        Map<String, String> map = new HashMap<String, String>(16);
        map.put(key, value);
        return beanToJson(map, null);
    }

    /**
     * 将json字符串转换成对象
     *
     * @param json
     * @param clazz
     * @return
     */
    public static Object jsonToBean(String json, Object clazz) {
        if (StringUtils.isEmpty(json) || clazz == null) {
            return null;
        }
        return JSON.parseObject(json, clazz.getClass());
    }

}

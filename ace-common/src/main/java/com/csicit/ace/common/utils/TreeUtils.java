package com.csicit.ace.common.utils;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * @author yansiyang
 * @version V1.0
 * @date 2019/5/8 14:05
 */
public class TreeUtils {
    /**
     * 将带有parentId字段的列表转化为树结构
     *
     * @param list
     * @param t    类类型
     * @return
     * @author yansiyang
     * @date 2019/5/8 14:11
     */
    public static <T> List<T> makeTree(List<T> list, Class<T> t) {
        JSONArray array = new JSONArray();
        list.forEach(tt -> {
            array.add(tt);
        });
        JSONArray arrayT = new JSONArray();
        Map<String, String> map = new HashMap<>();
        for (int i = 0; i < array.size(); i++) {
            JSONObject obj = array.getJSONObject(i);
            map.put(obj.getString("id"), obj.getString("parentId"));
        }
        for (int i = 0; i < array.size(); i++) {
            JSONObject obj = array.getJSONObject(i);
            if (map.containsKey(obj.getString("parentId"))) {
                continue;
            }
            obj.put("children", getChildren(obj.getString("id"), array, t));
            arrayT.add(JsonUtils.castObject(obj, t));
        }
        return arrayT.toJavaList(t);
    }

    /**
     * 递归获取子节点
     *
     * @param parentId
     * @param array
     * @param t
     * @return
     * @author yansiyang
     * @date 2019/5/8 15:28
     */
    private static <T> JSONArray getChildren(String parentId, JSONArray array, Class<T> t) {
        JSONArray arrayT = new JSONArray();
        for (int i = 0; i < array.size(); i++) {
            JSONObject obj = array.getJSONObject(i);
            if (Objects.equals(obj.getString("parentId"), parentId)) {
                obj.put("children", getChildren(obj.getString("id"), array, t));
                arrayT.add(JsonUtils.castObject(obj, t));
            }
        }
        return arrayT;
    }

//    public List<OrgGroupDO> makeTreeGroup(List<OrgGroupDO> list) {
//        List<OrgGroupDO> listT = new ArrayList<>();
//        for (int i = 0; i < list.size(); i++) {
//            OrgGroupDO obj = list.get(i);
//            obj.setChildren(getChildren(obj.getId(), list));
//            listT.add(obj);
//        }
//        return listT;
//    }
//
//    private List<OrgGroupDO> getChildren(String parentId, List<OrgGroupDO> array) {
//        List<OrgGroupDO> listT = new ArrayList<>();
//        for (int i = 0; i < array.size(); i++) {
//            OrgGroupDO obj = array.get(i);
//            if (Objects.equals(obj.getParentId(), parentId)) {
//                obj.setChildren(getChildren(obj.getId(), array));
//                listT.add(obj);
//            }
//
//        }
//        return listT;
//    }
}

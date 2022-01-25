package com.csicit.ace.common.utils.factory;

import com.csicit.ace.common.exception.RException;
import com.alibaba.fastjson.JSONObject;
import com.csicit.ace.common.exception.RException;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;

/**
 * @author shanwj
 * @date 2019/4/18 11:14
 */
@Data
public class TreeHelpFactory {
    private static TreeHelpFactory treeHelpFactory;
    /**
     * 父节点字段名，默认值"parent_id"
     */
    public String fieldNameParentID = "parent_id";
    /**
     * 排序路径字段名，默认值"sort_path"
     */
    public String fieldNameSortPath = "sort_path";
    /**
     * id字段名，默认值"id"
     */
    public String fieldNameID = "id";
    /**
     * 同级序号字段名，默认值"sort_index"
     */
    public String fieldNameSortIndex = "sort_index";
    /**
     * 数据版本号字段，默认值"data_version"
     */
    public String fieldNameVersion = "data_version";
    /**
     * 是否启用版本控制
     */
    public boolean enableVersionControl;
    /**
     * 排序路径规则
     */
    private String sortPathCodeRule;
    /**
     *
     */
    private String categoryField;
    /**
     * 表名
     */
    private String tableName;

    private TreeHelpFactory(){

    }

    public static TreeHelpFactory getInstance() {
        if (treeHelpFactory == null) {
            synchronized (TreeHelpFactory.class) {
                if (treeHelpFactory == null) {
                    treeHelpFactory = new TreeHelpFactory();
                }
            }
        }
        return treeHelpFactory;
    }

    public static TreeHelpFactory getInstance(String tableName, String sortPathCodeRule, String categoryField) {
        if (treeHelpFactory == null) {
            synchronized (TreeHelpFactory.class) {
                if (treeHelpFactory == null) {
                    treeHelpFactory = new TreeHelpFactory();
                }
            }
        }
        treeHelpFactory.tableName = tableName;
        treeHelpFactory.sortPathCodeRule = sortPathCodeRule;
        treeHelpFactory.categoryField = categoryField;
        return treeHelpFactory;
    }

    private int getLevel(String sortPath){
        int level = 0;
        int l = 0;
        if (StringUtils.isBlank(sortPath))
            return 0;
        for(char c : sortPathCodeRule.toCharArray())
        {
            l += Integer.parseInt(c+"");
            level++;
            if (sortPath.length() == l) return level;
        }
        throw new RException("树节点排序路径不符合编码规则：“" + sortPathCodeRule + "”");
    }

//    public boolean Save(JSONObject node, Func<JSONObject, Boolean> applyNodeUpdateMethod)
//    {
//        if (applyNodeUpdateMethod == null) throw new ArgumentNullException(nameof(applyNodeUpdateMethod));
//        db = DBHelper.GetInstance();
//        return Save(db, node, (DBHelper db, JObject obj) => { if (applyNodeUpdateMethod != null)  return applyNodeUpdateMethod(obj); return false; });
//    }

}

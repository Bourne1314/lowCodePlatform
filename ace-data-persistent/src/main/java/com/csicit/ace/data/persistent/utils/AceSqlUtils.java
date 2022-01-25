package com.csicit.ace.data.persistent.utils;

import com.csicit.ace.common.exception.RException;
import com.csicit.ace.common.utils.SortPathUtils;
import com.csicit.ace.common.utils.internationalization.InternationUtils;
import com.csicit.ace.data.persistent.mapper.AceDBHelperMapper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Objects;

/**
 * 自定义sql使用工具类
 *
 * @author yansiyang
 * @version V1.0
 * @date 2019/4/17 10:35
 */
@Component
public class AceSqlUtils {
    @Autowired
    AceDBHelperMapper aceDbHelperMapper;

    static int maxLevel = 12;

    /**
     * 新增数据
     * <p>
     * 校验树结构表格
     * 1、排序号是否重复
     * 2、排序路径是否过长
     *
     * @param tableName 表名
     * @param parentId  父节点ID
     * @param sortIndex 排序号
     * @return
     * @author yansiyang
     * @date 2019/7/8 14:25
     */
    public void validateTreeTable(String tableName, String parentId, Integer sortIndex, String sortPath) {
        // 校验排序号
        int count = aceDbHelperMapper.getCountWithParams("select count(*) from {0} where sort_index={2} and " +
                "parent_id=''{1}''", tableName, parentId, sortIndex + "");
        if (count > 0) {
            throw new RException(InternationUtils.getInternationalMsg("PARENT_NODE_EXIST_SAME_SORTINDEX"));
        }
        // 校验排序路径长度
        if (sortPath.length() > (SortPathUtils.pattern.length() * maxLevel)) {
            throw new RException(String.format(InternationUtils.getInternationalMsg("EXCEED_MAX_SORTPATH_LEVEL"),
                    maxLevel));
        }
    }

    /**
     * 新增数据
     * <p>
     * 校验树结构表格  可标识允许同样的parent_id sortIndex
     * 1、排序号是否重复
     * 2、排序路径是否过长
     *
     * @param tableName   表名
     * @param parentId    父节点ID
     * @param sortIndex   排序号
     * @param uniqueName  字段名 类似SysMenu app_id
     * @param uniqueValue
     * @return
     * @author yansiyang
     * @date 2019/7/8 14:25
     */
    public void validateTreeTableWithUnique(String tableName, String parentId, Integer sortIndex, String sortPath,
                                            String uniqueName, String uniqueValue) {
        // 校验排序号
        int count = aceDbHelperMapper.getCountWithParams("select count(*) from {0} where sort_index={2} and " +
                "parent_id=''{1}'' and {3} = ''{4}''", tableName, parentId, sortIndex + "", uniqueName, uniqueValue);
        if (count > 0) {
            throw new RException(InternationUtils.getInternationalMsg("PARENT_NODE_EXIST_SAME_SORTINDEX"));
        }
        // 校验排序路径长度
        if (sortPath.length() > (SortPathUtils.pattern.length() * maxLevel)) {
            throw new RException(String.format(InternationUtils.getInternationalMsg("EXCEED_MAX_SORTPATH_LEVEL"),
                    maxLevel));
        }
    }

    /**
     * 新增数据
     * <p>
     * 校验树结构表格  可标识允许同样的parent_id sortIndex
     * 1、排序号是否重复
     * 2、排序路径是否过长
     *
     * @param tableName      表名
     * @param parentId       父节点ID
     * @param sortIndex      排序号
     * @param uniqueName     字段名 类似SysMenu app_id
     * @param uniqueValue
     * @param isBusinessUnit 是否是业务单元
     * @return
     * @author yansiyang
     * @date 2019/7/8 14:25
     */
    public void validateTreeTableWithUniqueForOrg(String tableName, String parentId, Integer sortIndex, String
            sortPath, String uniqueName, String uniqueValue, Integer isBusinessUnit) {
        // 校验排序号
        int count = aceDbHelperMapper.getCountWithParams("select count(*) from {0} where sort_index={2} and " +
                        "parent_id=''{1}'' and {3} = ''{4}'' and is_business_unit={5}", tableName, parentId,
                sortIndex + "",
                uniqueName,
                uniqueValue, isBusinessUnit);
        if (count > 0) {
            throw new RException(InternationUtils.getInternationalMsg("PARENT_NODE_EXIST_SAME_SORTINDEX"));
        }
        // 校验排序路径长度
        if (sortPath.length() > (SortPathUtils.pattern.length() * maxLevel)) {
            throw new RException(String.format(InternationUtils.getInternationalMsg("EXCEED_MAX_SORTPATH_LEVEL"),
                    maxLevel));
        }
    }

    /**
     * 更新子节点排序路径
     *
     * @param tableName           表名
     * @param parentNewPath       父节点新的排序路径
     * @param parentOldPath       父节点旧的排序路径
     * @param parentOldPathLength 父节点旧的排序路径的长度
     * @return
     * @author zuogang
     * @date 2019/4/22 17:11
     */

    public Integer updateSonSortPath(String tableName, String parentNewPath, Integer parentOldPathLength, String
            parentOldPath) {
        return aceDbHelperMapper.updateBySqlWithParams("update {0} set sort_path=concat(''{1}'',SUBSTR(sort_path," +
                "{2} + 1)) \n" +
                "where instr(sort_path,''{3}'')=1", tableName, parentNewPath, parentOldPathLength, parentOldPath);
    }


    /**
     * 存在特殊字段 可标识允许同样的parent_id sortIndex
     * 更新子节点排序路径
     *
     * @param tableName           表名
     * @param parentNewPath       父节点新的排序路径
     * @param parentOldPath       父节点旧的排序路径
     * @param parentOldPathLength 父节点旧的排序路径的长度
     * @param uniqueName          字段名 类似SysMenu app_id
     * @param uniqueValue
     * @return
     * @author zuogang
     * @date 2019/4/22 17:11
     */

    public Integer updateSonSortPathWithUnique(String tableName, String parentNewPath, Integer parentOldPathLength,
                                               String
                                                       parentOldPath, String uniqueName, String uniqueValue) {
        return aceDbHelperMapper.updateBySqlWithParams("update {0} set sort_path=concat(''{1}'',SUBSTR(sort_path," +
                        "{2} + 1)) \n" +
                        "where instr(sort_path,''{3}'')=1 and {4} = ''{5}''", tableName, parentNewPath,
                parentOldPathLength,
                parentOldPath, uniqueName, uniqueValue);
    }

    /**
     * 校验父节点的id，生成新的sortPath
     *
     * @param tableName 表名
     * @param parentId  父节点ID
     * @param id        ID
     * @return
     * @author yansiyang
     * @date 2019/4/17 10:50
     */
    public String verifyParentId(String tableName, String parentId, String id, int sortIndex) {
        String rootId = "0";
        if (StringUtils.isBlank(parentId)) {
            return "nullParentId";
        }
        /**
         * 判断同一个父节点下是否存在同样的排序号
         */
        int sortIndexNum = aceDbHelperMapper.getCountWithParams("select count(*) from {0} where sort_index=''{3}'' and " +
                        "id!=''{1}'' and" +
                        " " +
                        "parent_id=''{2}'' "
                , tableName, id, parentId, sortIndex + "");
        if (sortIndexNum > 0) {
            throw new RException(InternationUtils.getInternationalMsg("PARENT_NODE_EXIST_SAME_SORTINDEX"));
        }
        /**
         * 判断parent_id是否改变
         */
        int num = aceDbHelperMapper.getCountWithParams("select count(*) from {0} where sort_index=''{3}'' and id=''{1}''" +
                        " and" +
                        " parent_id=''{2}''"
                , tableName, id, parentId, sortIndex + "");
        if (num == 1) {
            return "noChange";
        }
        if (Objects.equals(rootId, parentId)) {
            return "rootParent";
        }
        if (Objects.equals(parentId, id)) {
            throw new RException(InternationUtils.getInternationalMsg("PARENT_IS_SAME_WITH_SON"));
        }
        String parentPath = aceDbHelperMapper.getStringWithParams("select sort_path from {0} where id=''{1}'' ",
                tableName, parentId);
        String sonPath = aceDbHelperMapper.getStringWithParams("select sort_path from {0} where id=''{1}'' ",
                tableName, id);

        // 校验排序路径长度
        if (sonPath.length() > (SortPathUtils.pattern.length() * maxLevel)) {
            throw new RException(String.format(InternationUtils.getInternationalMsg("EXCEED_MAX_SORTPATH_LEVEL"),
                    maxLevel));
        }

        if (StringUtils.isBlank(sonPath) || StringUtils.isBlank(parentPath)) {
            return "nullParentId";
        }
        if (parentPath.startsWith(sonPath)) {
            throw new RException(InternationUtils.getInternationalMsg("PARENT_IS_SAME_WITH_SON"));
        }
        return "nullSortPath";
    }

    /**
     * 存在特殊字段 可标识允许同样的parent_id sortIndex
     * 校验父节点的id，生成新的sortPath
     *
     * @param tableName   表名
     * @param parentId    父节点ID
     * @param id          ID
     * @param uniqueName  字段名 类似SysMenu app_id
     * @param uniqueValue
     * @return
     * @author yansiyang
     * @date 2019/4/17 10:50
     */
    public String verifyParentIdWithUnique(String tableName, String parentId, String id, int sortIndex, String
            uniqueName, String uniqueValue) {
        String rootId = "0";
        if (StringUtils.isBlank(parentId) || StringUtils.isBlank(uniqueName) || StringUtils.isBlank(uniqueValue)) {
            return "nullParentId";
        }
        /**
         * 判断同一个父节点下是否存在同样的排序号
         */
        int sortIndexNum = aceDbHelperMapper.getCountWithParams("select count(*) from {0} where sort_index=''{3}'' and " +
                        "id!=''{1}'' and" +
                        " " +
                        "parent_id=''{2}'' and {4} = ''{5}'' "
                , tableName, id, parentId, sortIndex + "", uniqueName, uniqueValue);
        if (sortIndexNum > 0) {
            throw new RException(InternationUtils.getInternationalMsg("PARENT_NODE_EXIST_SAME_SORTINDEX"));
        }
        /**
         * 判断parent_id是否改变
         */
        int num = aceDbHelperMapper.getCountWithParams("select count(*) from {0} where sort_index=''{3}'' and id=''{1}''" +
                        " and" +
                        " " +
                        "parent_id=''{2}'' and {4} = ''{5}'' "
                , tableName, id, parentId, sortIndex + "", uniqueName, uniqueValue);
        if (num == 1) {
            return "noChange";
        }
        if (Objects.equals(rootId, parentId)) {
            return "rootParent";
        }
        if (Objects.equals(parentId, id)) {
            throw new RException(InternationUtils.getInternationalMsg("PARENT_IS_SAME_WITH_SON"));
        }
        String parentPath = aceDbHelperMapper.getStringWithParams("select sort_path from {0} where id=''{1}'' ",
                tableName, parentId);
        String sonPath = aceDbHelperMapper.getStringWithParams("select sort_path from {0} where id=''{1}'' ",
                tableName, id);

        // 校验排序路径长度
        if (sonPath.length() > (SortPathUtils.pattern.length() * maxLevel)) {
            throw new RException(String.format(InternationUtils.getInternationalMsg("EXCEED_MAX_SORTPATH_LEVEL"),
                    maxLevel));
        }

        if (StringUtils.isBlank(sonPath) || StringUtils.isBlank(parentPath)) {
            return "nullParentId";
        }
        if (parentPath.startsWith(sonPath)) {
            throw new RException(InternationUtils.getInternationalMsg("PARENT_IS_SAME_WITH_SON"));
        }
        return "nullSortPath";
    }

    /**
     * 存在特殊字段 可标识允许同样的parent_id sortIndex
     * 校验父节点的id，生成新的sortPath
     *
     * @param tableName      表名
     * @param parentId       父节点ID
     * @param id             ID
     * @param uniqueName     字段名 类似SysMenu app_id
     * @param uniqueValue
     * @param isBusinessUnit 是否是业务单元
     * @return
     * @author yansiyang
     * @date 2019/4/17 10:50
     */
    public String verifyParentIdWithUniqueForOrg(String tableName, String parentId, String id, int sortIndex, String
            uniqueName, String uniqueValue, Integer isBusinessUnit) {
        String rootId = "0";
        if (StringUtils.isBlank(parentId) || StringUtils.isBlank(uniqueName) || StringUtils.isBlank(uniqueValue)) {
            return "nullParentId";
        }
        /**
         * 判断同一个父节点下是否存在同样的排序号
         */
        int sortIndexNum = aceDbHelperMapper.getCountWithParams("select count(*) from {0} where sort_index=''{3}'' and " +
                        "id!=''{1}'' and" +
                        " " +
                        "parent_id=''{2}'' and {4} = ''{5}''  and is_business_unit={6}"
                , tableName, id, parentId, sortIndex + "", uniqueName, uniqueValue, isBusinessUnit);
        if (sortIndexNum > 0) {
            throw new RException(InternationUtils.getInternationalMsg("PARENT_NODE_EXIST_SAME_SORTINDEX"));
        }
        /**
         * 判断parent_id是否改变
         */
        int num = aceDbHelperMapper.getCountWithParams("select count(*) from {0} where sort_index=''{3}'' and id=''{1}''" +
                        " and" +
                        " " +
                        "parent_id=''{2}'' and {4} = ''{5}''  and is_business_unit={6}"
                , tableName, id, parentId, sortIndex + "", uniqueName, uniqueValue, isBusinessUnit);
        if (num == 1) {
            return "noChange";
        }
        if (Objects.equals(rootId, parentId)) {
            return "rootParent";
        }
        if (Objects.equals(parentId, id)) {
            throw new RException(InternationUtils.getInternationalMsg("PARENT_IS_SAME_WITH_SON"));
        }
        String parentPath = aceDbHelperMapper.getStringWithParams("select sort_path from {0} where id=''{1}'' ",
                tableName, parentId);
        String sonPath = aceDbHelperMapper.getStringWithParams("select sort_path from {0} where id=''{1}'' ",
                tableName, id);

        // 校验排序路径长度
        if (sonPath.length() > (SortPathUtils.pattern.length() * maxLevel)) {
            throw new RException(String.format(InternationUtils.getInternationalMsg("EXCEED_MAX_SORTPATH_LEVEL"),
                    maxLevel));
        }

        if (StringUtils.isBlank(sonPath) || StringUtils.isBlank(parentPath)) {
            return "nullParentId";
        }
        if (parentPath.startsWith(sonPath)) {
            throw new RException(InternationUtils.getInternationalMsg("PARENT_IS_SAME_WITH_SON"));
        }
        return "nullSortPath";
    }

    /**
     * 获取最大排序号
     *
     * @param params
     * @return
     * @author yansiyang
     * @date 2019/4/17 10:50
     */
    public Integer getMaxSort(Map<String, Object> params) {
        Integer maxIndex = 0;
        String appId = (String) params.get("appId");
        String groupId = (String) params.get("groupId");
        String orgId = (String) params.get("orgId");
        String depId = (String) params.get("depId");
        String parentId = (String) params.get("parentId");
        String tableName = (String) params.get("tableName");
        Integer scope = (Integer) params.get("scope");
        String typeId = (String) params.get("typeId");
        String personDocId = (String) params.get("personDocId");
        String categoryId = (String) params.get("categoryId");

        // 业务单元
        if (Objects.equals("org_organization", tableName)) {
            maxIndex = aceDbHelperMapper.getMaxSortNoWithParams("select max(sort_index) from {0} where " +
                            "group_id=''{1}''" +
                            " and parent_id=''{2}'' and is_business_unit=1 and is_delete=0"
                    , tableName, groupId, parentId);
        }

        // 集团
        if (Objects.equals("org_group", tableName)) {
            maxIndex = aceDbHelperMapper.getMaxSortNoWithParams("select max(sort_index) from {0} where " +
                            "parent_id=''{1}'' and is_delete=0"
                    , tableName, parentId);
        }

        // 系统配置
        if (Objects.equals("sys_config", tableName)) {
            if (Objects.equals(1, scope)) {
                // 租户
                maxIndex = aceDbHelperMapper.getMaxSortNoWithParams("select max(sort_index) from {0} where scope=1",
                        tableName
                );
            } else if (Objects.equals(2, scope)) {
                maxIndex = aceDbHelperMapper.getMaxSortNoWithParams("select max(sort_index) from {0} where scope=2 and " +
                                "group_id=''{1}''",
                        tableName, groupId
                );
            } else if (Objects.equals(3, scope)) {
                maxIndex = aceDbHelperMapper.getMaxSortNoWithParams("select max(sort_index) from {0} where scope=3 and " +
                                "group_id=''{1}'' and app_id=''{2}''",
                        tableName, groupId, appId
                );
            }
        }
        // 字典维护
        if (Objects.equals("sys_dict_value", tableName)) {
            if (Objects.equals(1, scope)) {
                // 租户
                maxIndex = aceDbHelperMapper.getMaxSortNoWithParams("select max(sort_index) from {0} where scope=1 and " +
                                "parent_id=''{1}'' and type_id=''{2}''",
                        tableName, parentId, typeId
                );
            } else if (Objects.equals(2, scope)) {
                maxIndex = aceDbHelperMapper.getMaxSortNoWithParams("select max(sort_index) from {0} where scope=2 and " +
                                "group_id=''{1}'' and parent_id=''{2}'' and type_id=''{3}''",
                        tableName, groupId, parentId, typeId
                );
            } else if (Objects.equals(3, scope)) {
                maxIndex = aceDbHelperMapper.getMaxSortNoWithParams("select max(sort_index) from {0} where scope=3 and " +
                                "group_id=''{1}'' and app_id=''{2}'' and parent_id=''{3}'' and type_id=''{4}''",
                        tableName, groupId, appId, parentId, typeId
                );
            }
        }

        // 部门
        if (Objects.equals("org_department", tableName)) {
            maxIndex = aceDbHelperMapper.getMaxSortNoWithParams("select max(sort_index) from {0} where " +
                            "group_id=''{1}'' and organization_id=''{2}'' and parent_id=''{3}'' and is_delete=0",
                    tableName, groupId, orgId, parentId
            );
        }

        // 个人档案职务信息
        if (Objects.equals("bd_person_job", tableName)) {
            maxIndex = aceDbHelperMapper.getMaxSortNoWithParams("select max(sort_index) from {0} where " +
                            "person_doc_id=''{1}''",
                    tableName, personDocId
            );
        }

        // 岗位
        if (Objects.equals("bd_post", tableName)) {
            maxIndex = aceDbHelperMapper.getMaxSortNoWithParams("select max(sort_index) from {0} where " +
                            "group_id=''{1}'' and organization_id=''{2}'' and department_id=''{3}''",
                    tableName, groupId, orgId, depId
            );
        }

        // 职务，应用
        if (Objects.equals("bd_job", tableName) || Objects.equals("sys_group_app", tableName)) {
            maxIndex = aceDbHelperMapper.getMaxSortNoWithParams("select max(sort_index) from {0} where " +
                            "group_id=''{1}''",
                    tableName, groupId
            );
        }

        // 用户
        if (Objects.equals("sys_user", tableName)) {
            maxIndex = aceDbHelperMapper.getMaxSortNoWithParams("select max(sort_index) from {0} where " +
                            "group_id=''{1}'' and organization_id=''{2}'' and user_type=3 and is_delete=0",
                    tableName, groupId, orgId
            );
        }

        // 个人档案
        if (Objects.equals("bd_person_doc", tableName)) {
            maxIndex = aceDbHelperMapper.getMaxSortNoWithParams("select max(sort_index) from {0} where " +
                            "group_id=''{1}'' and organization_id=''{2}'' and is_delete=0",
                    tableName, groupId, orgId
            );
        }

        // 菜单，权限
        if (Objects.equals("sys_menu", tableName) || Objects.equals("sys_auth", tableName)) {
            maxIndex = aceDbHelperMapper.getMaxSortNoWithParams("select max(sort_index) from {0} where " +
                            "app_id=''{1}'' and parent_id=''{2}''",
                    tableName, appId, parentId
            );
        }

        // 报表类别
        if (Objects.equals("report_type", tableName)) {
            maxIndex = aceDbHelperMapper.getMaxSortNoWithParams("select max(sort) from {0} where " +
                            "app_id=''{1}'' and parent_id=''{2}''",
                    tableName, appId, parentId
            );
        }

        // 流程类别
        if (Objects.equals("wfd_flow_category", tableName)) {
            maxIndex = aceDbHelperMapper.getMaxSortNoWithParams("select max(sort_no) from {0} where " +
                            "app_id=''{1}''",
                    tableName, appId
            );
        }

        // 流程定义
        if (Objects.equals("wfd_flow", tableName)) {
            maxIndex = aceDbHelperMapper.getMaxSortNoWithParams("select max(sort_no) from {0} where " +
                            "app_id=''{1}'' and category_id=''{2}''",
                    tableName, appId, categoryId
            );
        }

        if (maxIndex == null) {
            maxIndex = 0;
        }
        return maxIndex;
    }

    /**
     * 获取最大排序号(开发平台)
     *
     * @param params
     * @return
     * @author yansiyang
     * @date 2019/4/17 10:50
     */
    public Integer getDevMaxSort(Map<String, Object> params) {
        Integer maxIndex = 0;
        String parentId = (String) params.get("parentId");
        String tableName = (String) params.get("tableName");
        // 开发平台-菜单表
        if (Objects.equals("sys_menu", tableName)) {
            maxIndex = aceDbHelperMapper.getMaxSortNoWithParams("select max(sort_index) from {0} where " +
                            "parent_id=''{1}''",
                    tableName, parentId
            );
        }
        // 开发平台-用户表
        if (Objects.equals("sys_user", tableName)) {
            maxIndex = aceDbHelperMapper.getMaxSortNoWithParams("select max(sort_index) from {0}",
                    tableName
            );
        }
        if (maxIndex == null) {
            maxIndex = 0;
        }
        return maxIndex;
    }

}

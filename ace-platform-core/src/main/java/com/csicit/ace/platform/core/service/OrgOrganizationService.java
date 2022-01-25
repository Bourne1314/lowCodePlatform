package com.csicit.ace.platform.core.service;

import com.alibaba.fastjson.JSONObject;
import com.csicit.ace.common.pojo.domain.OrgOrganizationDO;
import com.csicit.ace.common.utils.server.R;
import com.csicit.ace.dbplus.service.IBaseService;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

/**
 * 组织-组织 实例对象访问接口
 *
 * @author shanwj
 * @version v1.0
 * @date 2019-04-11 10:36:05
 */
@Transactional
public interface OrgOrganizationService extends IBaseService<OrgOrganizationDO> {


    /**
     * 批量导入业务单元
     * @param  list
     * @return 
     * @author FourLeaves
     * @date 2020/8/26 14:02
     */
    boolean saveLoadUnits(List<OrgOrganizationDO> list);

//    /**
//     * 保存组织
//     *
//     * @param orgs 组织实体类列表
//     * @return boolean
//     * @author yansiyang
//     * @date 2019/4/15 16:52
//     */
//    boolean saveOrgs(List<JSONObject> orgs);

    /**
     * 保存组织
     *
     * @param org 组织实体
     * @return boolean
     * @author yansiyang
     * @date 2019/4/15 16:52
     */
    boolean saveOrgs(JSONObject org);

    /**
     * 保存组织
     *
     * @param org 组织实体类列表
     * @return boolean
     * @author yansiyang
     * @date 2019/4/15 16:52
     */
    boolean saveOrg(OrgOrganizationDO org);

    /**
     * 修改组织
     *
     * @param org
     * @return boolean
     * @author yansiyang
     * @date 2019/4/15 16:53
     */
    boolean updateOrg(OrgOrganizationDO org);


    /**
     *  修改子节点排序路径  包括 org 和 dep
     * @param orgId
     * @param newSortPath 新的排序路径
     * @param newGroupId 新的集团ID
     * @return
     * @author yansiyang
     * @date 2019/11/1 9:57
     */
    boolean updateSonSortPath(String orgId, String newSortPath, String newGroupId);


    /**
     * 递归获取 业务单元的所有子节点列表  包括业务单元和部门
     * @param orgId
     * @param type  1 仅业务单元 2 仅部门 3 业务单元和部门  4集团 5 全部
     * @return
     * @author yansiyang
     * @date 2019/11/1 10:01
     */
    List<OrgOrganizationDO> getSonOrgsByOrgId(String orgId, Integer type, boolean addParent);

    /**
     * 递归获取 业务单元的所有子节点列表  包括业务单元和部门  主键
     * @param orgId
     * @param type  1 仅业务单元 2 仅部门 3 业务单元和部门  4集团 5 全部
     * @return
     * @author yansiyang
     * @date 2019/11/1 10:01
     */
    List<String> getSonIdsOrgsByOrgId(String orgId, Integer type, boolean addParent);

    /**
     * 版本化组织
     *
     * @param map
     * @return
     * @author yansiyang
     * @date 2019/4/18 10:16
     */
    boolean versionOrg(Map<String, String> map);

    /**
     * 删除组织 
     * @param map
     * @return 
     * @author yansiyang
     * @date 2019/5/15 9:39
     */
    R deleteOrg(Map<String, Object> map);

    /**
     * 删除组织
     * @param ids
     * @return
     * @author yansiyang
     * @date 2019/5/14 19:42
     */
    boolean removeOrgs(List<String> ids);

    /**
     * 移动业务单元
     * @param mvToTop  是否设置为顶级业务单元
     * @param id  要移动的业务单元
     * @param groupId  集团主键
     * @param targetOrgId  目标业务单元主键
     * @return
     */
    boolean mvBusinessUnit(boolean mvToTop, String id, String groupId, String targetOrgId);

}

package com.csicit.ace.platform.core.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.aspose.cells.Cell;
import com.aspose.cells.Cells;
import com.aspose.cells.Row;
import com.aspose.cells.Workbook;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.csicit.ace.common.annotation.AceAuth;
import com.csicit.ace.common.exception.RException;
import com.csicit.ace.common.pojo.domain.OrgGroupDO;
import com.csicit.ace.common.pojo.domain.OrgOrganizationDO;
import com.csicit.ace.common.pojo.domain.SysRoleDO;
import com.csicit.ace.common.utils.StringUtils;
import com.csicit.ace.common.utils.TreeUtils;
import com.csicit.ace.common.utils.internationalization.InternationUtils;
import com.csicit.ace.common.utils.server.R;
import com.csicit.ace.platform.core.service.OrgOrganizationTypeService;
import com.csicit.ace.platform.core.service.SysAuthScopeOrgService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.*;
import java.util.stream.Collectors;


/**
 * 组织-组织 接口访问层
 *
 * @author yansiyang
 * @version V1.0
 * @date 2019-04-15 15:20:43
 */

@RestController
@RequestMapping("/orgOrganizations")
@Api("组织-组织管理")
public class OrgOrganizationController extends BaseController {

    @Autowired
    OrgOrganizationTypeService orgOrganizationTypeService;

    @Autowired
    SysAuthScopeOrgService sysAuthScopeOrgService;

    /**
     * 批量导入业务单元
     * @param groupId
     * @param file
     * @return 
     * @author FourLeaves
     * @date 2020/8/25 16:37
     */
    @ApiImplicitParam(name = "groupId", value = "groupId", dataType = "String", required = true)
    @ApiOperation(value = "批量导入业务单元", httpMethod = "POST", notes = "批量导入业务单元")
    @AceAuth("批量导入业务单元")
    @RequestMapping(value = "/action/uploadUnits", method = RequestMethod.POST)
    public R uploadUnits(@RequestParam("type") String type, @RequestParam("groupId") String groupId, @RequestParam("file") MultipartFile file) {
        if (StringUtils.isNotBlank(groupId) && StringUtils.isNotBlank(type) && !file.isEmpty()) {
            try {
                List<OrgOrganizationDO> list = new ArrayList<>();
                if (Objects.equals(type, "txt")) {
                    // Txt格式
                    BufferedReader br = new BufferedReader(new InputStreamReader(file.getInputStream(), "UTF-8"));//构造一个BufferedReader类来读取文件
                    String line;
                    while ((line = br.readLine()) != null) {
                        OrgOrganizationDO orgOrganizationDO = new OrgOrganizationDO();
                        String[] args = line.split(",");
                        if (StringUtils.isBlank(args[0]) || StringUtils.isBlank(args[2])) {
                            return R.error(InternationUtils.getInternationalMsg("EMPTY_ARG_WITH_PARAM", "业务单元主键或名称"));
                        }
                        orgOrganizationDO.setId(args[0]);
                        orgOrganizationDO.setParentId(StringUtils.isNotBlank(args[1]) ? args[1] : groupId);
                        orgOrganizationDO.setName(args[2]);
                        orgOrganizationDO.setShortName(args[3]);
                        orgOrganizationDO.setCode(args[4]);
                        orgOrganizationDO.setGroupId(groupId);
                        list.add(orgOrganizationDO);
                    }
                    br.close();
                } else {
                    // Excel格式
                    Workbook workbook = new Workbook(file.getInputStream());
                    Cells cells = workbook.getWorksheets().get(0).getCells();
                    if (cells == null)
                    {
                        return R.error(InternationUtils.getInternationalMsg("EMPTY_ARG_WITH_PARAM", "Excel"));
                    }
                    for (int i = 0; i < cells.getMaxDataRow() + 1; i++) {
                        OrgOrganizationDO orgOrganizationDO = new OrgOrganizationDO();
                        if (StringUtils.isBlank(getRowValue(cells.get(i, 0))) ||
                                StringUtils.isBlank(getRowValue(cells.get(i, 2)))) {
                            return R.error(InternationUtils.getInternationalMsg("EMPTY_ARG_WITH_PARAM", "业务单元主键或名称"));
                        }
                        orgOrganizationDO.setId(cells.get(i, 0).getValue().toString());
                        orgOrganizationDO.setParentId(getRowValue(cells.get(i, 1)) == null ? groupId : getRowValue(cells.get(i, 1)));
                        orgOrganizationDO.setName(cells.get(i, 2).getValue().toString());
                        orgOrganizationDO.setShortName(getRowValue(cells.get(i, 3)));
                        orgOrganizationDO.setCode(getRowValue(cells.get(i, 4)));
                        orgOrganizationDO.setGroupId(groupId);
                        list.add(orgOrganizationDO);
                    }
                }
                if (CollectionUtils.isNotEmpty(list)) {
                    if (orgOrganizationService.saveLoadUnits(list)) {
                        return R.ok();
                    }
                    return R.error();
                }
            }catch (RException r) {
                return R.error(r.getMsg());
            } catch (Exception e) {
                e.printStackTrace();
                return R.error();
            }
            return R.ok();
        }
        return R.error(InternationUtils.getInternationalMsg("EMPTY_ARG_WITH_PARAM", "集团主键"));
    }

    /**
     * 判断单元格是否为空
     * @param cell
     * @return 
     * @author FourLeaves
     * @date 2020/8/25 17:55
     */
    private String getRowValue(Cell cell) {
        if (Objects.nonNull(cell)) {
            if (Objects.nonNull(cell.getValue())) {
                String str = (String) cell.getValue();
                if (StringUtils.isNotBlank(str)) {
                    return str;
                }
            }
        }
        return null;
    }

    /**
     * 获取单个组织
     *
     * @param id
     * @return
     * @author yansiyang
     * @date 2019-04-15 15:20:43
     */
    @ApiImplicitParam(name = "id", value = "主键id", dataType = "String", required = true)
    @ApiOperation(value = "获取单个组织", httpMethod = "GET", notes = "获取单个")
    @AceAuth("获取单个组织")
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public R get(@PathVariable("id") String id) {
        OrgOrganizationDO instance = orgOrganizationService.getById(id);
        //获取业务单元类型
        List<String> orgIds = new ArrayList<>();
        orgIds.add(instance.getId());
        Map<String, List<String>> map = orgOrganizationTypeService.getOrgType(orgIds);
        instance.setOrgType(map.get(instance.getId()));
        instance.setOrgTypeObj(getOrgTypeObjs(instance.getOrgType()));
        return R.ok().put("instance", instance);
    }

    /**
     * 获取集团 + 业务单元列表
     *
     * @return
     * @author yansiyang
     * @date 2019-04-15 15:20:43
     */
    @ApiOperation(value = "获取集团 + 业务单元列表", httpMethod = "GET")
    @AceAuth("获取集团 + 业务单元列表")
    @RequestMapping(value = "/action/listGroupAndOrg", method = RequestMethod.GET)
    public R listGroupAndOrg() {
        List<OrgOrganizationDO> list = new ArrayList<>();
        String userId = securityUtils.getCurrentUserId();
        SysRoleDO role = sysUserRoleService.getEffectiveRoleData(userId, null).get(0);
        // 集团系统管理员 安全保密员
        List<OrgGroupDO> groups = orgGroupService.getGroupsByUserId(userId);
        List<String> groupIds = groups.stream().map(OrgGroupDO::getId).collect(Collectors.toList());
        if (Objects.equals(11, role.getRoleType()) || Objects.equals(22, role.getRoleType())) {
            list = orgOrganizationService.list(new QueryWrapper<OrgOrganizationDO>().eq("is_delete", 0)
                    .and(groupIds == null || groupIds.size() == 0, i -> i.eq("1", "2"))
                    .in("group_id", groupIds)
                    .orderByAsc("sort_path"));
        } else {
            // 应用管理员 获得其业务单元管控域
            List<String> orgIds = sysAuthScopeOrgService.getOrgIdsByUserId(userId);
            if (orgIds != null && orgIds.size() > 0) {
                orgIds.addAll(groupIds);
                list = orgOrganizationService.list(new QueryWrapper<OrgOrganizationDO>().eq("is_delete", 0)
                        .and(groupIds == null || groupIds.size() == 0, i -> i.eq("1", "2"))
                        .in("group_id", groupIds)
                        .and(orgIds == null || orgIds.size() == 0, i -> i.eq("1", "2"))
                        .in("id", orgIds)
                        .orderByAsc("sort_path"));
            }
        }
        List<OrgOrganizationDO> listT = TreeUtils.makeTree(list, OrgOrganizationDO.class);
        return R.ok().put("list", listT);
    }

    /**
     * 获取列表
     *
     * @return
     * @author yansiyang
     * @date 2019-04-15 15:20:43
     */
    @ApiOperation(value = "获取组织列表", httpMethod = "GET")
    @ApiImplicitParam(name = "params", value = "参数", required = true, dataType = "Map")
    @AceAuth("获取组织列表")
    @RequestMapping(value = "", method = RequestMethod.GET)
    public R list(@RequestParam Map<String, String> params) {
        List<OrgOrganizationDO> list = new ArrayList<>();
        String userId = securityUtils.getCurrentUserId();
        String groupId = params.get("groupId");
        SysRoleDO role = sysUserRoleService.getEffectiveRoleData(userId, null).get(0);
        // 集团系统管理员 安全保密员  集团超级管理员 单一业务管理员
        List<String> groupIds = null;
        if (StringUtils.isBlank(groupId)) {
            List<OrgGroupDO> groups = orgGroupService.getGroupsByUserId(userId);
            groupIds = groups.stream().map(OrgGroupDO::getId).collect(Collectors.toList());
        }
        if (Objects.equals(11, role.getRoleType()) || Objects.equals(22, role.getRoleType())) {
            if (StringUtils.isNotBlank(groupId)) {
                list = orgOrganizationService.list(new QueryWrapper<OrgOrganizationDO>().eq("is_delete", 0)
                        .eq("group_id", groupId)
                        .orderByAsc("sort_path").eq("IS_BUSINESS_UNIT", 1));
            } else {
                list = orgOrganizationService.list(new QueryWrapper<OrgOrganizationDO>().eq("is_delete", 0)
                        .and(groupIds == null || groupIds.size() == 0, i -> i.eq("1", "2"))
                        .in("group_id", groupIds)
                        .orderByAsc("sort_path").eq("IS_BUSINESS_UNIT", 1));
            }
        } else {
            // 应用管理员 获得其业务单元管控域
            List<String> orgIds = sysAuthScopeOrgService.getOrgIdsByUserId(userId);
            if (orgIds != null && orgIds.size() > 0) {
                if (StringUtils.isNotBlank(groupId)) {
                    list = orgOrganizationService.list(new QueryWrapper<OrgOrganizationDO>().eq("is_delete", 0)
                            .eq("group_id", groupId)
                            .and(orgIds == null || orgIds.size() == 0, i -> i.eq("1", "2"))
                            .in("id", orgIds)
                            .orderByAsc("sort_path").eq("IS_BUSINESS_UNIT", 1));
                } else {
                    list = orgOrganizationService.list(new QueryWrapper<OrgOrganizationDO>().eq("is_delete", 0)
                            .and(groupIds == null || groupIds.size() == 0, i -> i.eq("1", "2"))
                            .in("group_id", groupIds)
                            .and(orgIds == null || orgIds.size() == 0, i -> i.eq("1", "2"))
                            .in("id", orgIds)
                            .orderByAsc("sort_path").eq("IS_BUSINESS_UNIT", 1));
                }
            }
        }

        //获取业务单元类型
        List<String> orgIds = list.stream().map(OrgOrganizationDO::getId).collect(Collectors.toList());
        Map<String, List<String>> map = orgOrganizationTypeService.getOrgType(orgIds);
        // 获取业务单元上级名称
        Set<String> parentIds = list.stream().map(OrgOrganizationDO::getParentId).collect(Collectors.toSet());
        List<OrgOrganizationDO> parents = orgOrganizationService.list(new QueryWrapper<OrgOrganizationDO>().eq
                ("is_delete", 0)
                .and(parentIds == null || parentIds.size() == 0, i -> i.eq("1", "2"))
                .in("id", parentIds).select("name", "id"));
        Map<String, String> parentMap = new HashMap<>();
        parents.forEach(org -> {
            parentMap.put(org.getId(), org.getName());
        });

        list.forEach(org -> {
            org.setParentName(parentMap.get(org.getParentId()));
            org.setOrgType(map.get(org.getId()));
            org.setOrgTypeObj(getOrgTypeObjs(org.getOrgType()));
        });
        List<OrgOrganizationDO> listT = TreeUtils.makeTree(list, OrgOrganizationDO.class);
        return R.ok().put("list", listT);
    }

    /**
     * 根据集团ID获取所有业务单元列表（不判断管理员权限）
     *
     * @return 集团集合不带分页
     * @author generator
     * @date 2019-04-15 20:12:24
     */
    @ApiOperation(value = "根据集团ID获取所有业务单元列表", httpMethod = "GET", notes = "根据集团ID获取所有业务单元列表")
    @ApiImplicitParam(name = "params", value = "参数", required = true, dataType = "Map")
    @AceAuth("根据集团ID获取所有业务单元列表")
    @RequestMapping(value = "/list/all", method = RequestMethod.GET)
    public R listAll(@RequestParam Map<String, Object> params) {
        String groupId = (String) params.get("groupId");
        List<OrgOrganizationDO> list = orgOrganizationService.list(new QueryWrapper<OrgOrganizationDO>().eq
                ("is_delete", 0)
                .eq("group_id", groupId)
                .orderByAsc("sort_path").eq("IS_BUSINESS_UNIT", 1));
        //获取业务单元类型
        List<String> orgIds = list.stream().map(OrgOrganizationDO::getId).collect(Collectors.toList());
        Map<String, List<String>> map = orgOrganizationTypeService.getOrgType(orgIds);
        // 获取业务单元上级名称
        Set<String> parentIds = list.stream().map(OrgOrganizationDO::getParentId).collect(Collectors.toSet());
        List<OrgOrganizationDO> parents = orgOrganizationService.list(new QueryWrapper<OrgOrganizationDO>().eq
                ("is_delete", 0)
                .and(parentIds == null || parentIds.size() == 0, i -> i.eq("1", "2"))
                .in("id", parentIds).select("name", "id"));
        Map<String, String> parentMap = new HashMap<>();
        parents.forEach(org -> {
            parentMap.put(org.getId(), org.getName());
        });

        list.forEach(org -> {
            org.setParentName(parentMap.get(org.getId()));
            org.setOrgType(map.get(org.getId()));
            org.setOrgTypeObj(getOrgTypeObjs(org.getOrgType()));
        });
        // 不返回集团 部门 类型的业务单元
        for (int i = 0; i < list.size(); i++) {
            List<String> types = list.get(i).getOrgType();
            if (types != null && (types.contains("group") || types.contains("department"))) {
                list.remove(i);
            }
        }
        List<OrgOrganizationDO> listT = TreeUtils.makeTree(list, OrgOrganizationDO.class);
        return R.ok().put("list", listT);
    }

    /**
     * 根据集团ID获取列表
     *
     * @return
     * @author yansiyang
     * @date 2019-04-15 15:20:43
     */
    @ApiOperation(value = "根据集团ID获取列表", httpMethod = "GET")
    @ApiImplicitParam(name = "groupId", value = "集团id", required = true, dataType = "String")
    @AceAuth("根据集团ID获取业务单元列表")
    @RequestMapping(value = "/action/listByGroup/{groupId}", method = RequestMethod.GET)
    public R listByGroup(@PathVariable("groupId") String groupId) {
        List<OrgOrganizationDO> list = new ArrayList<>();
        String userId = securityUtils.getCurrentUserId();
        SysRoleDO role = sysUserRoleService.getEffectiveRoleData(userId, null).get(0);
       if (Objects.equals(1, role.getRoleType())) {
            list = orgOrganizationService.list(new QueryWrapper<OrgOrganizationDO>().eq("is_delete", 0).eq("group_id", groupId)
                    .orderByAsc("sort_path").eq("IS_BUSINESS_UNIT", 1));
        } else if (Objects.equals(11, role.getRoleType()) || Objects.equals(22, role.getRoleType()) || Objects.equals(33,
                role.getRoleType()) ||Objects.equals(44, role.getRoleType()) ||Objects.equals(4, role.getRoleType())) {
           // 集团系统管理员 安全保密员 安全审计员  集团超级管理员 单一业务管理员
           List<OrgGroupDO> groups = orgGroupService.getGroupsByUserId(userId);
           List<String> groupIds = groups.stream().map(OrgGroupDO::getId).collect(Collectors.toList());

           list = orgOrganizationService.list(new QueryWrapper<OrgOrganizationDO>().eq("is_delete", 0)
                    .and(groupIds == null || groupIds.size() == 0, i -> i.eq("1", "2"))
                    .in("group_id", groupIds).eq("group_id", groupId)
                    .orderByAsc("sort_path").eq("IS_BUSINESS_UNIT", 1));
        } else {
            // 应用管理员 获得其业务单元管控域
            List<String> orgIds = sysAuthScopeOrgService.getOrgIdsByUserId(userId);
            if (orgIds != null && orgIds.size() > 0) {
                list = orgOrganizationService.list(new QueryWrapper<OrgOrganizationDO>().eq("is_delete", 0)
                        .and(orgIds == null || orgIds.size() == 0, i -> i.eq("1", "2"))
                        .in("id", orgIds).eq("group_id", groupId)
                        .orderByAsc("sort_path").eq("IS_BUSINESS_UNIT", 1));
            }
        }
        // List<OrgOrganizationDO> list = orgOrganizationService.list(new QueryWrapper<OrgOrganizationDO>()
        //        .orderByAsc("sort_path").eq("group_id", groupId).eq("IS_BUSINESS_UNIT", 1));
        //获取业务单元类型
        List<String> orgIds = list.stream().map(OrgOrganizationDO::getId).collect(Collectors.toList());
        Map<String, List<String>> map = orgOrganizationTypeService.getOrgType(orgIds);
        // 获取业务单元上级名称
        Set<String> parentIds = list.stream().map(OrgOrganizationDO::getParentId).collect(Collectors.toSet());
        List<OrgOrganizationDO> parents = orgOrganizationService.list(new QueryWrapper<OrgOrganizationDO>().eq
                ("is_delete", 0)
                .and(parentIds == null || parentIds.size() == 0, i -> i.eq("1", "2"))
                .in("id", parentIds).select("name", "id"));
        Map<String, String> parentMap = new HashMap<>();
        parents.forEach(org -> {
            parentMap.put(org.getId(), org.getName());
        });

        list.forEach(org -> {
            org.setParentName(parentMap.get(org.getId()));
            org.setOrgType(map.get(org.getId()));
            org.setOrgTypeObj(getOrgTypeObjs(org.getOrgType()));
        });
        // 不返回集团 部门 类型的业务单元
        for (int i = 0; i < list.size(); i++) {
            List<String> types = list.get(i).getOrgType();
            if (types != null && (types.contains("group") || types.contains("department"))) {
                list.remove(i);
            }
        }
        List<OrgOrganizationDO> listT = TreeUtils.makeTree(list, OrgOrganizationDO.class);
        return R.ok().put("list", listT).put("dataList", list);
    }

//    /**
//     * 保存组织
//     *
//     * @param instances 对象列表
//     * @return 保存响应结果
//     * @author yansiyang
//     * @date 2019-04-15 15:20:43
//     */
//    @ApiOperation(value = "保存组织", httpMethod = "POST")
//    @ApiImplicitParam(name = "instance", value = "实体", required = true, dataType = "OrgOrganizationVDO")
//    @AceAuth("保存组织")
//    @RequestMapping(value = "", method = RequestMethod.POST)
//    public R save(@RequestBody List<JSONObject> instances) {
//        if (orgOrganizationService.saveOrgs(instances)) {
//            return R.ok(InternationUtils.getInternationalMsg("SAVE_SUCCESS"));
//        }
//        return R.error(InternationUtils.getInternationalMsg("SAVE_FAILED"));
//    }

    /**
     * 保存组织
     *
     * @param instance 对象列表
     * @return 保存响应结果
     * @author yansiyang
     * @date 2019-04-15 15:20:43
     */
    @ApiOperation(value = "保存组织", httpMethod = "POST")
    @ApiImplicitParam(name = "instance", value = "实体", required = true, dataType = "OrgOrganizationVDO")
    @AceAuth("保存组织")
    @RequestMapping(value = "", method = RequestMethod.POST)
    public R save(@RequestBody JSONObject instance) {
        if (orgOrganizationService.saveOrgs(instance)) {
            return R.ok(InternationUtils.getInternationalMsg("SAVE_SUCCESS"));
        }
        return R.error(InternationUtils.getInternationalMsg("SAVE_FAILED"));
    }

    /**
     * 修改组织
     *
     * @param instance 对象
     * @return
     * @author yansiyang
     * @date 2019-04-15 15:20:43
     */
    @ApiOperation(value = "修改组织", httpMethod = "PUT")
    @ApiImplicitParam(name = "instance", value = "实体", required = true, dataType = "OrgOrganizationVDO")
    @AceAuth("修改组织")
    @RequestMapping(value = "", method = RequestMethod.PUT)
    public R update(@RequestBody OrgOrganizationDO instance) {
        if (orgOrganizationService.updateOrg(instance)) {
            return R.ok(InternationUtils.getInternationalMsg("UPDATE_SUCCESS"));
        }
        return R.error(InternationUtils.getInternationalMsg("UPDATE_FAILED"));
    }

    /**
     * 删除组织
     *
     * @param params
     * @return com.csicit.ace.common.utils.server.R
     * @author yansiyang
     * @date 2019-04-15 15:20:43
     */
    @ApiOperation(value = "删除组织", httpMethod = "DELETE")
    @ApiImplicitParam(name = "params", value = "参数", required = true, dataType = "Map")
    @AceAuth("删除组织")
    @RequestMapping(value = "", method = RequestMethod.DELETE)
    public R delete(@RequestBody Map<String, Object> params) {
        return orgOrganizationService.deleteOrg(params);
    }

    /**
     * 版本化组织
     *
     * @param params {versionName,versionNo,organizationId}
     * @return
     * @author yansiyang
     * @date 2019/4/18 19:30
     */
    @ApiOperation(value = "版本化组织", httpMethod = "POST")
    @ApiImplicitParam(name = "instance", value = "实体", required = true, dataType = "Map")
    @AceAuth("版本化组织")
    @RequestMapping(value = "/action/version", method = RequestMethod.POST)
    public R version(@RequestBody Map<String, String> params) {
        if (orgOrganizationService.versionOrg(params)) {
            return R.ok(InternationUtils.getInternationalMsg("VERSION_SUCCESS"));
        }
        return R.error(InternationUtils.getInternationalMsg("VERSION_FAILED"));
    }

    /**
     * 移动业务单元
     *
     * @param params @param params {mvToTop,id,organizationId,groupId}
     * @return
     * @author yansiyang
     * @date 2019/4/18 19:30
     */
    @ApiOperation(value = "移动业务单元", httpMethod = "POST")
    @ApiImplicitParam(name = "instance", value = "实体", required = true, dataType = "Map")
    @AceAuth("移动业务单元")
    @RequestMapping(value = "/action/mvBusinessUnit", method = RequestMethod.POST)
    public R mvBusinessUnit(@RequestBody Map<String, String> params) {
        String id = params.get("id");
        String targetOrgId = params.get("organizationId");
        String groupId = params.get("groupId");
        String mvToTop = params.get("mvToTop");
        if (orgOrganizationService.mvBusinessUnit(Objects.equals(mvToTop, "1"), id, groupId, targetOrgId)) {
            return R.ok(InternationUtils.getInternationalMsg("OPERATE_SUCCESS"));
        }
        return R.error(InternationUtils.getInternationalMsg("OPERATE_FAILED"));
    }

    /**
     * 返回业务单元类型 静态 列表
     *
     * @return
     * @author yansiyang
     * @date 2019/5/21 8:14
     */
    @ApiOperation(value = "返回业务单元类型 静态 列表", httpMethod = "GET")
    @RequestMapping(value = "/action/getOrgTypes", method = RequestMethod.GET)
    @AceAuth("返回业务单元类型 静态 列表")
    public R getOrgTypes() {
        JSONArray array = new JSONArray();
        String[] types = {"corporation@法人", "administration@行政", "finance@财务",
                "factory@工厂", "project@工程", "asset@资产",
                "sales@销售", "stock@库存", "qc@质量", "hr@人力资源", "purchase@采购", "traffic@运输", "maintain@维护"};
        Arrays.asList(types).forEach(type -> {
            JSONObject json = new JSONObject();
            json.put("id", type.split("@")[0]);
            json.put("name", type.split("@")[1]);
            array.add(json);
        });
        return R.ok().put("types", array);
    }

    /**
     * 返回业务单元类型 静态 列表
     *
     * @param orgTypes
     * @return
     * @author yansiyang
     * @date 2019/5/21 10:14
     */
    public JSONArray getOrgTypeObjs(List<String> orgTypes) {
        JSONArray array = new JSONArray();
        String[] types = {"group@集团", "corporation@法人", "administration@行政", "finance@财务",
                "factory@工厂", "project@工程", "asset@资产",
                "sales@销售", "stock@库存", "qc@质量", "hr@人力资源", "purchase@采购", "traffic@运输", "maintain@维护"};
        if (orgTypes != null && orgTypes.size() > 0) {
            Arrays.asList(types).forEach(type -> {
                if (orgTypes.contains(type.split("@")[0])) {
                    JSONObject json = new JSONObject();
                    json.put("id", type.split("@")[0]);
                    json.put("name", type.split("@")[1]);
                    array.add(json);
                }
            });
        }
        return array;
    }
}

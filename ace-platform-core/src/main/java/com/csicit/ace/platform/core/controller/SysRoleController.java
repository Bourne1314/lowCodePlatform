package com.csicit.ace.platform.core.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.csicit.ace.common.annotation.AceAuth;
import com.csicit.ace.common.pojo.domain.SysRoleDO;
import com.csicit.ace.common.pojo.domain.SysRoleDepDO;
import com.csicit.ace.common.pojo.domain.SysUserRoleDO;
import com.csicit.ace.common.pojo.domain.SysUserRoleLvDO;
import com.csicit.ace.common.utils.StringUtils;
import com.csicit.ace.common.utils.internationalization.InternationUtils;
import com.csicit.ace.common.utils.server.R;
import com.csicit.ace.dbplus.MapWrapper;
import com.csicit.ace.platform.core.service.SysRoleDepService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 角色管理 接口访问层
 *
 * @author zuogang
 * @version V1.0
 * @date 2019-04-12 9:57:46
 */

@RestController
@RequestMapping("/sysRoles")
@Api("角色管理")
public class SysRoleController extends BaseController {

    @Autowired
    SysRoleDepService sysRoleDepService;


    /**
     * 根据roleId获取关联部门
     *
     * @param roleId 角色id
     * @return 角色对象
     * @author shanwj
     * @date 2019/4/11 18:25
     */
    @ApiImplicitParam(name = "roleId", value = "roleId", dataType = "String", required = true)
    @ApiOperation(value = "根据roleId获取关联部门", httpMethod = "GET", notes = "根据roleId获取关联部门")
    @AceAuth("根据roleId获取关联部门")
    @RequestMapping(value = "/roleDeps/{roleId}", method = RequestMethod.GET)
    public R getRoleDeps(@PathVariable("roleId") String roleId) {
        return R.ok().put("data", sysRoleDepService.getRoleDeps(roleId));
    }

    /**
     * 保存角色关联部门
     *
     * @return 角色对象
     * @author shanwj
     * @date 2019/4/11 18:25
     */
    @ApiOperation(value = "保存角色关联部门", httpMethod = "POST", notes = "保存角色关联部门")
    @AceAuth("保存角色关联部门")
    @RequestMapping(value = "/roleDeps", method = RequestMethod.POST)
    public R addRoleDeps(@RequestBody SysRoleDepDO sysRoleDepDO) {
        if (sysRoleDepService.addRoleDep(sysRoleDepDO)) {
            return R.ok(InternationUtils.getInternationalMsg("SAVE_SUCCESS"));
        }
        return R.error(InternationUtils.getInternationalMsg("SAVE_FAILED"));
    }

    /**
     * 删除角色关联部门
     *
     * @param id
     * @return 角色对象
     * @author shanwj
     * @date 2019/4/11 18:25
     */
    @ApiImplicitParam(name = "roleId", value = "roleId", dataType = "String", required = true)
    @ApiOperation(value = "删除角色关联部门", httpMethod = "DELETE", notes = "删除角色关联部门")
    @AceAuth("删除角色关联部门")
    @RequestMapping(value = "/roleDeps/{id}", method = RequestMethod.DELETE)
    public R deleteRoleDeps(@PathVariable("id") String id) {
        if (sysRoleDepService.deleteRoleDep(id)) {
            return R.ok(InternationUtils.getInternationalMsg("DELETE_SUCCESS"));
        }
        return R.error(InternationUtils.getInternationalMsg("DELETE_FAILED"));
    }


    /**
     * 根据id获取单个角色
     *
     * @param id 角色id
     * @return 角色对象
     * @author shanwj
     * @date 2019/4/11 18:25
     */
    @ApiImplicitParam(name = "id", value = "主键id", dataType = "String", required = true)
    @ApiOperation(value = "获取单个角色", httpMethod = "GET", notes = "根据id获取单个角色")
    @AceAuth("获取单个角色")
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public R get(@PathVariable("id") String id) {
        SysRoleDO instance = sysRoleService.infoRole(id);
        instance.setDepsName(sysRoleDepService.getDepsName(id));
        return R.ok().put("instance", instance);
    }

    /**
     * 获取列表
     *
     * @param params 请求参数
     * @return 集团应用集合不带分页
     * @author generator
     * @date 2019-04-15 20:12:24
     */
    @ApiOperation(value = "获取集团应用列表", httpMethod = "GET", notes = "根据请求参数获取集团应用列表")
    @ApiImplicitParam(name = "params", value = "参数", required = true, dataType = "Map")
    @AceAuth("获取集团应用列表")
    @RequestMapping(value = "/list/all", method = RequestMethod.GET)
    public R listAll(@RequestParam Map<String, Object> params) {
        List<SysRoleDO> list = sysRoleService.list(MapWrapper.getEqualInstance(params));
        List<SysRoleDO> list1 = sysRoleService.list(new QueryWrapper<SysRoleDO>().eq("ROLE_TYPE", 0).eq("role_scope",
                1));
        list.addAll(list1);
        return R.ok().put("list", list);
    }


    /**
     * 根据条件获取角色列表并分页(角色组件查询)
     *
     * @param params 请求参数map对象
     * @return 角色列表
     * @author yansiyang
     * @date 2019/4/11 18:21
     */
    @ApiOperation(value = "获取角色列表并分页", httpMethod = "GET", notes = "获取角色列表并分页")
    @ApiImplicitParam(name = "params", value = "参数", required = true, dataType = "Map")
    @AceAuth("获取角色列表并分页")
    @RequestMapping(value = "/list/forSelectRoleComponents", method = RequestMethod.GET)
    public R listForSelectRoleComponents(@RequestParam Map<String, Object> params) {
        String currentStr = (String) params.get("current");
        String sizeStr = (String) params.get("size");
        int current = Integer.parseInt(currentStr);
        int size = Integer.parseInt(sizeStr);
        int roleScopeContainsFlg = Integer.parseInt((String) params.get("roleScopeContainsFlg"));

        Page<SysRoleDO> page = new Page<>(current, size);
        IPage list = null;
        if (roleScopeContainsFlg == 1) {
            list = sysRoleService.page(page, new QueryWrapper<SysRoleDO>().and(i -> i
                    .and(n -> n.eq("role_scope", 1).eq("ROLE_TYPE", 0))
                    .or().eq("app_id", params.get("app_id"))));
        } else {
            list = sysRoleService.page(page, new QueryWrapper<SysRoleDO>()
                    .eq("app_id", params.get("app_id")));
        }
//        list.setRecords(sysRoleService.fillDepName(list.getRecords()));
        return R.ok().put("page", list);
    }

    /**
     * 根据条件获取角色列表并分页
     *
     * @param params 请求参数map对象
     * @return 角色列表
     * @author yansiyang
     * @date 2019/4/11 18:21
     */
    @ApiOperation(value = "获取角色列表并分页", httpMethod = "GET", notes = "获取角色列表并分页")
    @ApiImplicitParam(name = "params", value = "参数", required = true, dataType = "Map")
    @AceAuth("获取角色列表并分页")
    @RequestMapping(method = RequestMethod.GET)
    public R list(@RequestParam Map<String, Object> params) {
        String currentStr = (String) params.get("current");
        String sizeStr = (String) params.get("size");

        if (StringUtils.isBlank(currentStr) || StringUtils.isBlank(sizeStr)) {
            List<SysRoleDO> list = sysRoleService.list(MapWrapper.getEqualInstance(params));
            List<SysRoleDO> list1 = sysRoleService.list(new QueryWrapper<SysRoleDO>().eq("ROLE_TYPE", 0).eq("role_scope",
                    1));
            list.addAll(list1);
            return R.ok().put("list", list);

        }
        int current = Integer.parseInt(currentStr);
        int size = Integer.parseInt(sizeStr);
        int roleScope = Integer.parseInt((String) params.get("roleScope"));
        Page<SysRoleDO> page = new Page<>(current, size);
        IPage list;
//        Map<String, Object> equalMap = new HashMap<>();
        if (roleScope == 1) {
            list = sysRoleService.page(page, new QueryWrapper<SysRoleDO>().orderByDesc("create_time")
                    .like("name", params.get("name")).eq("ROLE_TYPE", 0).eq("role_scope", 1));
            // 判断是否存在未激活的用户角色关系
            List<SysRoleDO> roleDOS = list.getRecords();
            roleDOS.stream().forEach(role -> {
                role.setBeActivedScopeRole(0);
                // 获取当前角色的分配用户
                List<String> userIds = sysUserRoleService.list(new QueryWrapper<SysUserRoleDO>()
                        .eq("role_id", role.getId()).isNull("app_id")).stream().map(SysUserRoleDO::getUserId)
                        .collect(Collectors.toList());
                // 获取当前角色有效用户ID
                List<String> userIds2 = sysUserRoleLvService.list(new QueryWrapper<SysUserRoleLvDO>()
                        .eq("IS_LATEST", 1).isNull("app_id").inSql("id", "select lv_id from sys_user_role_v where " +
                                "role_id='" + role.getId() + "'")).stream().map(SysUserRoleLvDO::getUserId).collect
                        (Collectors.toList());
                if (userIds2.size() == userIds.size()) {
                    if (userIds2.size() > 0) {
                        if (!userIds2.containsAll(userIds)) {
                            role.setBeActivedScopeRole(1);
                        }
                    }
                } else {
                    role.setBeActivedScopeRole(1);
                }
            });
            list.setRecords(roleDOS);
//            List<String> roleIds = ((List<SysRoleDO>) list.getRecords()).stream().map(AbstractBaseDomain::getId)
//                    .collect(Collectors.toList());
        } else {
//            list = sysRoleService.page(page, new QueryWrapper<SysRoleDO>()
//                    .like("name", params.get("name")).and(i -> i.and(n -> n.eq("role_scope", 1).eq("ROLE_TYPE", 0))
//                            .or().eq("app_id", params.get("app_id"))));
            list = sysRoleService.page(page, new QueryWrapper<SysRoleDO>().orderByDesc("create_time")
                    .like("name", params.get("name")).eq("ROLE_TYPE", 0).eq("role_scope", 0)
                    .eq("app_id", params.get("app_id")));
            list.setRecords(sysRoleService.fillDepName(list.getRecords()));
        }

//        Map<String, Object> likeMap = new HashMap<>();
//        likeMap.put("name", params.get("name"));


//        IPage list = sysRoleService.page(page, MapWrapper.getEqualAndLikeInstance(equalMap, likeMap));
//        .or(true).eq(true, "ROLE_SCOPE", 1)

        return R.ok().put("page", list);
    }

    /**
     * 通过该角色ID获得可作为下级角色和互斥角色的列表数据
     *
     * @param params 请求参数map对象
     * @return 角色列表
     * @author yansiyang
     * @date 2019/4/11 18:21
     */
    @ApiOperation(value = "通过该角色ID获得可作为下级角色和互斥角色的列表数据", httpMethod = "GET", notes = "通过该角色ID获得可作为下级角色和互斥角色的列表数据")
    @ApiImplicitParam(name = "params", value = "参数", required = true, dataType = "Map")
    @AceAuth("通过该角色ID获得可作为下级角色和互斥角色的列表数据")
    @RequestMapping(value = "/action/getChildAndMutualRoleList", method = RequestMethod.GET)
    public R getChildAndMutualRoleList(@RequestParam Map<String, Object> params) {
        String appId = (String) params.get("appId");
        String roleId = (String) params.get("roleId");
        String type = (String) params.get("type");
        List<SysRoleDO> list = sysRoleService.getChildAndMutualRoleList(appId, roleId, type);
        return R.ok().put("list", list);

    }


    /**
     * 判断当前角色是否存在
     *
     * @param appId 应用id
     * @param name  角色名称
     * @return true 存在
     */
    @ApiOperation(value = "获取角色列表并分页", httpMethod = "POST", notes = "判断角色名称是否已存在")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "appId", value = "应用id", required = true, dataType = "String"),
            @ApiImplicitParam(name = "name", value = "角色名称", required = true, dataType = "String")
    })
    @AceAuth("判断角色名称是否已存在")
    @RequestMapping(value = "/action/exist-name-check/app/{appId}/name/{name}", method = RequestMethod.POST)
    public boolean nameExistCheck(@PathVariable("appId") String appId, @PathVariable("name") String name) {
        return sysRoleService.count(
                new QueryWrapper<SysRoleDO>().eq("app_id", appId).eq("name", name)) > 0;
    }

    /**
     * 新增角色保存
     *
     * @param role 角色对象
     * @return 保存响应结果
     * @author yansiyang
     * @date 2019/4/11 18:26
     */
    @ApiOperation(value = "保存角色", httpMethod = "POST", notes = "保存角色")
    @ApiImplicitParam(name = "role", value = "角色实体", required = true, dataType = "SysRoleDO")
    @AceAuth("保存角色")
    @RequestMapping(method = RequestMethod.POST)
    public R save(@RequestBody SysRoleDO role) {
        if (role.getRoleType() == 0) {
            if (role.getRoleScope() == 1) { // 全局角色
                if (sysRoleService.count(new QueryWrapper<SysRoleDO>().eq("name", role.getName())) > 0) {
                    return R.error(InternationUtils.getInternationalMsg("SAME_MODEL",
                            new String[]{InternationUtils.getInternationalMsg("ROLE_NAME"), role.getName()}
                    ));
                }
            } else { //本地角色
                // 判断角色名是否已存在
                if (nameExistCheck(role.getAppId(), role.getName())) {
                    return R.error(InternationUtils.getInternationalMsg("SAME_MODEL",
                            new String[]{InternationUtils.getInternationalMsg("ROLE_NAME"), role.getName()}
                    ));
                }
            }
        }
        if (sysRoleService.saveRole(role)) {
            return R.ok(InternationUtils.getInternationalMsg("SAVE_SUCCESS"));
        }
        return R.error(InternationUtils.getInternationalMsg("SAVE_FAILED"));
    }

    /**
     * 更新角色
     *
     * @param role 角色对象
     * @return 更新响应结果
     * @author yansiyang
     * @date 2019/4/11 18:26
     */
    @ApiOperation(value = "更新角色", httpMethod = "PUT", notes = "更新角色")
    @ApiImplicitParam(name = "role", value = "角色实体", required = true, dataType = "SysRoleDO")
    @AceAuth("更新角色")
    @RequestMapping(method = RequestMethod.PUT)
    public R update(@RequestBody SysRoleDO role) {
        SysRoleDO sysRoleDo = sysRoleService.getById(role.getId());
        if (!Objects.equals(sysRoleDo.getName(), role.getName())) {
            // 判断角色名是否已存在
            if (nameExistCheck(role.getAppId(), role.getName())) {
                return R.error(InternationUtils.getInternationalMsg("SAME_MODEL",
                        new String[]{InternationUtils.getInternationalMsg("ROLE_NAME"), role.getName()}
                ));
            }
        }
        if (sysRoleService.updateRole(role)) {
            return R.ok(InternationUtils.getInternationalMsg("UPDATE_SUCCESS"));
        }
        return R.error(InternationUtils.getInternationalMsg("UPDATE_FAILED"));
    }

    /**
     * 删除角色
     *
     * @param ids 角色ID数组
     * @return 删除响应
     * @author shanwj
     * @date 2019/4/11 18:27
     */
    @ApiOperation(value = "删除角色", httpMethod = "DELETE", notes = "根据id数组删除角色")
    @ApiImplicitParam(name = "ids", value = "角色ID数组", required = true, dataType = "String[]")
    @AceAuth("删除角色")
    @RequestMapping(method = RequestMethod.DELETE)
    public R delete(@RequestBody String[] ids) {
        if (sysRoleService.deleteByIds(Arrays.asList(ids))) {
            return R.ok(InternationUtils.getInternationalMsg("DELETE_SUCCESS"));
        }
        return R.error(InternationUtils.getInternationalMsg("DELETE_FAILED"));
    }

    /**
     * 当前角色设置下级角色
     *
     * @param map 参数对象，包含角色id，下级角色id数组集合
     * @return 响应结果
     * @author shanwj
     * @date 2019/4/18 17:29
     */
    @ApiOperation(value = "当前角色设置下级角色", httpMethod = "POST", notes = "当前角色设置下级角色")
    @ApiImplicitParam(name = "map", value = "参数对象，包含角色id，下级角色id数组集合", required = true, dataType = "Map<String, Object>")
    @AceAuth("当前角色设置下级角色")
    @RequestMapping(value = "/roleRelations/roles/juniors", method = RequestMethod.POST)
    public R saveChildRoles(@RequestBody Map<String, Object> map) {
        String id = (String) map.get("id");
        List<String> cids = (List<String>) map.get("cids");
        if (sysRoleRelationService.saveChildRoles(id, cids)) {
            return R.ok(InternationUtils.getInternationalMsg("SET_SUCCESS"));
        }
        return R.error(InternationUtils.getInternationalMsg("SET_FAILED"));
    }

    /**
     * 当前角色设置上级角色
     *
     * @param map 参数对象，包含角色id，上级角色id数组集合
     * @return 响应结果
     * @author shanwj
     * @date 2019/4/18 17:34
     */
    @ApiOperation(value = "当前角色设置上级角色", httpMethod = "POST", notes = "当前角色设置上级角色")
    @ApiImplicitParam(name = "map", value = "参数对象，包含角色id，上级角色id数组集合", required = true, dataType = "Map<String, Object>")
    @AceAuth("当前角色设置上级角色")
    @RequestMapping(value = "/roleRelations/roles/superiors", method = RequestMethod.POST)
    public R saveParentsRoles(@RequestBody Map<String, Object> map) {
        String id = (String) map.get("id");
        List<String> pids = (List<String>) map.get("pids");
        if (sysRoleRelationService.saveParentRoles(id, pids)) {
            return R.ok(InternationUtils.getInternationalMsg("SET_SUCCESS"));
        }
        return R.error(InternationUtils.getInternationalMsg("SET_FAILED"));
    }

    /**
     * 当前角色设置互斥角色
     *
     * @param map 参数对象，包含角色id，互斥角色id数组集合
     * @return 响应结果
     * @author shanwj
     * @date 2019/4/18 17:34
     */
    @ApiOperation(value = "当前角色设置互斥角色", httpMethod = "POST", notes = "当前角色设置互斥角色")
    @ApiImplicitParam(name = "map", value = "参数对象，包含角色id，互斥角色id数组集合", required = true, dataType = "Map<String, Object>")
    @AceAuth("当前角色设置互斥角色")
    @RequestMapping(value = "/roleRelations/roles/mutexes", method = RequestMethod.POST)
    public R saveMutexRoles(@RequestBody Map<String, Object> map) {
        String id = (String) map.get("id");
        List<String> mids = (List<String>) map.get("mids");
        if (sysRoleMutexService.saveMutexRoles(id, mids)) {
            return R.ok(InternationUtils.getInternationalMsg("SET_SUCCESS"));
        }
        return R.error(InternationUtils.getInternationalMsg("SET_FAILED"));
    }

}

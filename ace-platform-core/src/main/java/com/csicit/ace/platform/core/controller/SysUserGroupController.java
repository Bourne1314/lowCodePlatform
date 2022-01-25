package com.csicit.ace.platform.core.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.csicit.ace.common.annotation.AceAuth;
import com.csicit.ace.common.pojo.AbstractBaseDomain;
import com.csicit.ace.common.pojo.domain.*;
import com.csicit.ace.common.utils.TreeUtils;
import com.csicit.ace.common.utils.internationalization.InternationUtils;
import com.csicit.ace.common.utils.server.R;
import com.csicit.ace.platform.core.service.SysUserGroupService;
import com.csicit.ace.platform.core.service.SysUserGroupUserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 用户组管理 接口访问层
 *
 * @author yansiyang
 * @version V1.0
 * @date 2019/4/16 20:02
 */
@RestController
@RequestMapping("/sysUserGroups")
@Api("用户组管理")
public class SysUserGroupController extends BaseController {
    @Autowired
    SysUserGroupService sysUserGroupService;

    @Autowired
    SysUserGroupUserService sysUserGroupUserService;

    /**
     * UserGroups
     * 获取单个用户组
     *
     * @param id 用户组id
     * @return com.csicit.ace.common.utils.server.R
     * @author shanwj
     * @date 2019/4/11 18:25
     */
    @ApiImplicitParam(name = "id", value = "主键id", dataType = "String", required = true)
    @ApiOperation(value = "获取单个用户组", httpMethod = "GET", notes = "获取单个用户组")
    @AceAuth("获取单个用户组")
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public R get(@PathVariable("id") String id) {
        SysUserGroupDO instance = sysUserGroupService.getById(id);
        return R.ok().put("instance", instance);
    }

    /**
     * 获取用户组列表
     *
     * @param params 请求参数map对象
     * @return com.csicit.ace.common.utils.server.R 用户组列表
     * @author yansiyang
     * @date 2019/4/11 18:21
     */
    @ApiOperation(value = "获取用户组列表", httpMethod = "GET")
    @ApiImplicitParam(name = "params", value = "参数", required = true, dataType = "Map")
    @AceAuth("获取用户组列表")
    @RequestMapping(value = "", method = RequestMethod.GET)
    public R list(@RequestParam Map<String, Object> params) {
        String appId = (String) params.get("appId");
        if (StringUtils.isBlank(appId)) {
            return R.error(InternationUtils.getInternationalMsg("EMPTY_ARG"));
        }
        List<SysUserGroupDO> list = sysUserGroupService.list(new QueryWrapper<SysUserGroupDO>()
                .orderByAsc("sort_path").eq("app_id", appId));
        return R.ok().put("list", TreeUtils.makeTree(list, SysUserGroupDO.class));
    }

    /**
     * 根据集团ID获取用户组列表
     *
     * @return
     * @author zuogang
     * @date 2019/4/17 17:01
     */
    @ApiOperation(value = "根据集团ID获取用户组列表")
    @ApiImplicitParam(name = "sysUserDO", value = "用户对象", required = true, dataType = "SysUserDO")
    @AceAuth("根据集团ID获取用户组列表")
    @RequestMapping(value = "/query/groupApps/UserGroups", method = RequestMethod.POST)
    public R getUserGroupsByApps(@RequestBody Map<String, String> map) {
        List<SysUserGroupDO> list;
        String groupId = map.get("groupId");
        String userId = securityUtils.getCurrentUserId();
        SysRoleDO role = sysUserRoleService.getEffectiveRoleData(userId, null).get(0);
        // 集团系统管理员 安全保密员 安全审计员
        List<OrgGroupDO> groups = orgGroupService.getGroupsByUserId(userId);
        List<String> groupIds = groups.stream().map(OrgGroupDO::getId).collect(Collectors.toList());
        if (Objects.equals(11, role.getRoleType()) || Objects.equals(22, role.getRoleType()) || Objects.equals(33,
                role.getRoleType())) {
            list = sysUserGroupService.list(new QueryWrapper<SysUserGroupDO>()
                    .and(groupIds == null || groupIds.size() == 0, i -> i.eq("1", "2"))
                    .in("group_id", groupIds).eq("group_id", groupId)
                    .orderByAsc("sort_path"));
        } else {
            // 获取应用管理员用户管控的应用ID
            List<String> appIds = sysGroupAppService.listUserOrgApp().stream().map(AbstractBaseDomain::getId).collect
                    (Collectors.toList());
            list = sysUserGroupService.list(new QueryWrapper<SysUserGroupDO>()
                    .and(groupIds == null || groupIds.size() == 0, i -> i.eq("1", "2"))
                    .in("group_id", groupIds).eq("group_id", groupId)
                    .and(appIds == null || appIds.size() == 0, i -> i.eq("1", "2"))
                    .in("app_id", appIds)
                    .orderByAsc("sort_path"));
        }
//        List<SysUserGroupDO> sysUserGroupDOList = sysUserGroupService.getUserGroupsByApps(sysUserDO);
        //生成树结构的数据
        List<SysUserGroupDO> listT = TreeUtils.makeTree(list, SysUserGroupDO.class);
        return R.ok().put("list", listT);
    }

    /**
     * 保存用户组
     *
     * @param userGroup 用户组对象
     * @return com.csicit.ace.common.utils.server.R 保存响应结果
     * @author yansiyang
     * @date 2019/4/11 18:26
     */
    @ApiOperation(value = "保存用户组", httpMethod = "POST")
    @ApiImplicitParam(name = "userGroup", value = "用户组实体", required = true, dataType = "SysUserGroupDO")
    @AceAuth("保存用户组")
    @RequestMapping(value = "", method = RequestMethod.POST)
    public R save(@RequestBody SysUserGroupDO userGroup) {
        if (sysUserGroupService.saveUserGroup(userGroup)) {
            return R.ok(InternationUtils.getInternationalMsg("SAVE_SUCCESS"));
        }
        return R.error(InternationUtils.getInternationalMsg("SAVE_FAILED"));
    }

    /**
     * 修改用户组
     *
     * @param userGroup 用户组对象
     * @return com.csicit.ace.common.utils.server.R 更新结果
     * @author shanwj
     * @date 2019/4/11 18:27
     */
    @ApiOperation(value = "修改用户组", httpMethod = "PUT")
    @ApiImplicitParam(name = "userGroup", value = "用户组实体", required = true, dataType = "SysUserGroupDO")
    @AceAuth("修改用户组")
    @RequestMapping(value = "", method = RequestMethod.PUT)
    public R update(@RequestBody SysUserGroupDO userGroup) {
        userGroup.setUpdateTime(LocalDateTime.now());
        if (sysUserGroupService.update(userGroup)) {
            return R.ok(InternationUtils.getInternationalMsg("UPDATE_SUCCESS"));
        }
        return R.error(InternationUtils.getInternationalMsg("UPDATE_FAILED"));
    }

    /**
     * 删除用户组
     *
     * @param map
     * @return
     * @author yansiyang
     * @date 2019/4/12 9:12
     */
    @ApiOperation(value = "删除用户组", httpMethod = "DELETE")
    @ApiImplicitParam(name = "map", value = "删除参数", required = true, allowMultiple = true,
            dataType = "Map")
    @AceAuth("删除用户组")
    @RequestMapping(value = "", method = RequestMethod.DELETE)
    public R delete(@RequestBody Map<String, Object> map) {
        return sysUserGroupService.delete(map);
    }

    /**
     * 往用户组添加用户
     *
     * @param map
     * @return
     * @author yansiyang
     * @date 2019/4/16 12:29
     */
    @ApiOperation(value = "往用户组添加用户", httpMethod = "POST")
    @ApiImplicitParam(name = "map", value = "参数", required = true, dataType = "Map")
    @AceAuth("往用户组添加用户")
    @RequestMapping(value = "/users", method = RequestMethod.POST)
    public R addUser(@RequestBody Map<String, Object> map) {
        return sysUserGroupService.addUser(map);
    }

    /**
     * 查询用户组的用户
     *
     * @param groupId
     * @return
     * @author yansiyang
     * @date 2019/4/16 12:29
     */
    @ApiOperation(value = "查询用户组的用户", httpMethod = "GET")
    @ApiImplicitParam(name = "groupId", value = "用户组ID", required = true, dataType = "String")
    @AceAuth("查询用户组的用户")
    @RequestMapping(value = "/users/{groupId}", method = RequestMethod.GET)
    public R listUser(@PathVariable("groupId") String groupId) {
        List<SysUserDO> list = new ArrayList<>();
            list = sysUserService.list(new QueryWrapper<SysUserDO>().orderByAsc("sort_index").eq("user_type", 3).eq
                    ("is_delete", 0)
                    .inSql("id","select user_id from sys_user_group_user where  user_group_id='"+groupId+"'"));
        return R.ok().put("userList", list);
    }

    /**
     * 从用户组删除用户
     *
     * @param map
     * @return
     * @author yansiyang
     * @date 2019/4/16 12:29
     */
    @ApiOperation(value = "从用户组删除用户", httpMethod = "DELETE")
    @ApiImplicitParam(name = "map", value = "参数", required = true, dataType = "Map")
    @AceAuth("从用户组删除用户")
    @RequestMapping(value = "/users", method = RequestMethod.DELETE)
    public R deleteUser(@RequestBody Map<String, Object> map) {
        return sysUserGroupService.deleteUser(map);
    }
}

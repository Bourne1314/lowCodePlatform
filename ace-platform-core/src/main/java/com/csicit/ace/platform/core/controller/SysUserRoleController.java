package com.csicit.ace.platform.core.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.csicit.ace.common.annotation.AceAuth;
import com.csicit.ace.common.exception.RException;
import com.csicit.ace.common.pojo.domain.SysUserDO;
import com.csicit.ace.common.pojo.domain.SysUserRoleLvDO;
import com.csicit.ace.common.pojo.vo.ThreeAdminVO;
import com.csicit.ace.common.utils.internationalization.InternationUtils;
import com.csicit.ace.common.utils.server.R;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.apache.logging.log4j.util.Strings;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 用户角色管理 接口访问层
 *
 * @author shanwj
 * @version V1.0
 * @date 2019/4/16 20:02
 */

@RestController
@RequestMapping("/sysUserRoles")
@Api("用户角色管理")
public class SysUserRoleController extends BaseController {

    /**
     * 删除集团三员管理
     *
     * @param threeAdminVO
     * @return
     * @author zuogang
     * @date 2019/4/23 16:51
     */
    @ApiOperation(value = "删除集团三员管理", httpMethod = "POST", notes = "删除集团三员管理")
    @ApiImplicitParam(name = "threeAdminVO", value = "参数", required = true, dataType = "ThreeAdminVO")
    @AceAuth("删除集团三员管理")
    @RequestMapping(value = "/groupThreeRole/delete", method = RequestMethod.POST)
    public R deleteGroupThreeRole(@RequestBody ThreeAdminVO threeAdminVO) {
        if (sysUserRoleService.deleteGroupThreeRole(threeAdminVO)) {
            return R.ok(InternationUtils.getInternationalMsg("SET_SUCCESS"));
        }
        return R.error(InternationUtils.getInternationalMsg("SET_FAILED"));
    }

    /**
     * 删除应用三员管理
     *
     * @param threeAdminVO
     * @return
     * @author zuogang
     * @date 2019/4/23 16:51
     */
    @ApiOperation(value = "删除应用三员管理", httpMethod = "POST", notes = "删除应用三员管理")
    @ApiImplicitParam(name = "threeAdminVO", value = "三员对象", required = true, dataType = "ThreeAdminVO")
    @AceAuth("删除应用三员管理")
    @RequestMapping(value = "/appThreeRole/delete", method = RequestMethod.POST)
    public R deleteAppThreeRole(@RequestBody ThreeAdminVO threeAdminVO) {
        if (sysUserRoleService.deleteAppThreeRole(threeAdminVO)) {
            return R.ok(InternationUtils.getInternationalMsg("SET_SUCCESS"));
        }
        return R.error(InternationUtils.getInternationalMsg("SET_FAILED"));
    }


    /**
     * 通过UserId获取该用户所拥有的应用三员授控域
     *
     * @return
     * @author zuogang
     * @date 2019/4/23 16:51
     */
    @ApiOperation(value = "通过UserId获取角色信息", httpMethod = "GET", notes = "通过UserId获取角色信息")
    @ApiImplicitParam(name = "threeAdminVO", value = "参数", required = true, dataType = "ThreeAdminVO")
    @AceAuth("通过UserId获取角色信息")
    @RequestMapping(value = "/query/appThree/doMain", method = RequestMethod.GET)
    public R getAppThreeDoMain(@RequestParam Map<String, Object> params) {
        String userId = (String) params.get("userId");
        String status = (String) params.get("status");
        SysUserDO sysUserDO =
                sysUserRoleService.getAppThreeDoMain(userId, status);

        return R.ok().put("instance", sysUserDO);
    }


    /**
     * 获取集团级待激活人员列表
     *
     * @return 获取集团级待激活人员列表
     * @author shanwj
     * @date 2019/4/23 16:51
     */
    @ApiOperation(value = "获取集团级待激活人员列表", httpMethod = "GET", notes = "获取集团级待激活人员列表")
    @AceAuth("获取集团级待激活人员列表")
    @RequestMapping(value = "/query/group/toBeActivatedData", method = RequestMethod.GET)
    public R getGroupToBeActivatedData() {
        return R.ok().put("groupToBeActivatedData", sysUserRoleService.getGroupToBeActivatedData());
    }

    /**
     * 激活集团管理员权限
     *
     * @return 获取集团级待激活人员列表
     * @author shanwj
     * @date 2019/4/23 16:51
     */
    @ApiOperation(value = "激活集团管理员权限", httpMethod = "POST", notes = "激活集团管理员权限")
    @ApiImplicitParam(name = "params", value = "获取激活用户Id", required = true, dataType = "Map")
    @AceAuth("激活集团管理员权限")
    @RequestMapping(value = "/groupThreeRole/activated", method = RequestMethod.POST)
    public R setGroupActivated(@RequestBody Map<String, String> map) {
        if (sysUserRoleService.setGroupActivated(map)) {
            return R.ok(InternationUtils.getInternationalMsg("SET_SUCCESS"));
        }
        return R.error(InternationUtils.getInternationalMsg("SET_FAILED"));
    }

    /**
     * 获取未分配人员列表
     *
     * @return 获取未分配人员列表
     * @author shanwj
     * @date 2019/4/23 16:51
     */
    @ApiOperation(value = "获取未分配人员列表", httpMethod = "GET", notes = "获取未分配人员列表")
    @ApiImplicitParam(name = "params", value = "获取激活用户Id", required = true, dataType = "Map")
    @AceAuth("获取未分配人员列表")
    @RequestMapping(value = "/query/unallocatedData", method = RequestMethod.GET)
    public R getUnallocatedData(@RequestParam Map<String, Object> params) {
        return R.ok().put("unallocatedData", sysUserRoleService.getUnallocatedData(params));
    }

    /**
     * 获取应用管理员页面人员列表
     *
     * @return 获取应用管理员页面人员列表
     * @author shanwj
     * @date 2019/4/23 16:51
     */
    @ApiOperation(value = "获取应用管理员页面人员列表", httpMethod = "GET", notes = "获取应用管理员页面人员列表")
    @ApiImplicitParam(name = "params", value = "获取激活用户Id", required = true, dataType = "Map")
    @AceAuth("获取应用管理员页面人员列表")
    @RequestMapping(value = "/app/superadminData", method = RequestMethod.GET)
    public R getAllSuperadminData(@RequestParam Map<String, Object> params) {
        List<SysUserDO> sysUserDOS = sysUserRoleService.getUnallocatedData(params);
        sysUserDOS.addAll((List<SysUserDO>) sysUserRoleService.getAppAllThreeData(params).get("allThreeDataList"));
        return R.ok().put("sysUserDOS", sysUserDOS);
    }

    /**
     * 添加三员管理成员
     *
     * @param map
     * @return
     * @author zuogang
     * @date 2019/4/23 16:51
     */
    @ApiOperation(value = "添加三员管理成员", httpMethod = "POST", notes = "添加三员管理成员")
    @ApiImplicitParam(name = "Map", value = "参数", required = true, dataType = "map")
    @AceAuth("添加三员管理成员")
    @RequestMapping(value = "/threeRole/save", method = RequestMethod.POST)
    public R saveThreeRole(@RequestBody Map<String, Object> map) {
        if (sysUserRoleService.saveThreeRole(map)) {
            return R.ok(InternationUtils.getInternationalMsg("SAVE_SUCCESS"));
        }
        return R.error(InternationUtils.getInternationalMsg("SAVE_FAILED"));
    }

    /**
     * 修改三员管理成员
     *
     * @param map
     * @return
     * @author zuogang
     * @date 2019/4/23 16:51
     */
    @ApiOperation(value = "修改三员管理成员", httpMethod = "POST", notes = "修改三员管理成员")
    @ApiImplicitParam(name = "Map", value = "参数", required = true, dataType = "map")
    @AceAuth("修改三员管理成员")
    @RequestMapping(value = "/threeRole/update", method = RequestMethod.POST)
    public R updateThreeRole(@RequestBody Map<String, Object> map) {
        return sysUserRoleService.updateThreeRole(map);
    }


    /**
     * 获取已激活的集团三员管理员列表
     *
     * @param
     * @return
     * @author zuogang
     * @date 2019/4/23 16:51
     */
    @ApiOperation(value = "获取已激活的集团三员管理员列表", httpMethod = "GET", notes = "获取已激活的集团三员管理员列表")
    @AceAuth("获取已激活的集团三员管理员列表")
    @RequestMapping(value = "/query/group/allocatedData", method = RequestMethod.GET)
    public R getGroupAllocatedData() {
        return R.ok().put("threeUsers", sysUserRoleService.getGroupAllocatedData());
    }

    /**
     * 获取已激活和未激活的集团三员管理员列表
     *
     * @param
     * @return
     * @author zuogang
     * @date 2019/4/23 16:51
     */
    @ApiOperation(value = "获取已激活和未激活的集团三员管理员列表", httpMethod = "GET", notes = "获取已激活和未激活的集团三员管理员列表")
    @AceAuth("获取已激活和未激活的集团三员管理员列表")
    @RequestMapping(value = "/query/group/allThreeData", method = RequestMethod.GET)
    public R getGroupAllThreeData() {
        return R.ok().put("threeUsers", sysUserRoleService.getGroupAllThreeData());
    }


    /**
     * 获取应用级待激活人员列表
     *
     * @return 获取应用级待激活人员列表
     * @author shanwj
     * @date 2019/4/23 16:51
     */
    @ApiOperation(value = "获取应用级待激活人员列表", httpMethod = "GET", notes = "获取应用级待激活人员列表")
    @ApiImplicitParam(name = "params", value = "获取激活用户Id", required = true, dataType = "Map")
    @AceAuth("获取应用级待激活人员列表")
    @RequestMapping(value = "/query/app/toBeActivatedData", method = RequestMethod.GET)
    public R getAppToBeActivatedData(@RequestParam Map<String, Object> params) {
        return R.ok().put("appToBeActivatedData", sysUserRoleService.getAppToBeActivatedData(params));
    }

    /**
     * 激活应用管理员权限
     *
     * @return 激活应用管理员权限
     * @author shanwj
     * @date 2019/4/23 16:51
     */
    @ApiOperation(value = "激活应用管理员权限", httpMethod = "POST", notes = "激活应用管理员权限")
    @ApiImplicitParam(name = "params", value = "获取激活用户Id", required = true, dataType = "Map")
    @AceAuth("激活应用管理员权限")
    @RequestMapping(value = "/appThreeRole/activated", method = RequestMethod.POST)
    public R setAppActivated(@RequestBody Map<String, String> map) {
        if (sysUserRoleService.setAppActivated(map)) {
            return R.ok(InternationUtils.getInternationalMsg("SET_SUCCESS"));
        }
        return R.error(InternationUtils.getInternationalMsg("SET_FAILED"));
    }

    /**
     * 获取已激活的应用三员管理员列表
     *
     * @param
     * @return
     * @author zuogang
     * @date 2019/4/23 16:51
     */
    @ApiOperation(value = "获取已激活的应用三员管理员列表", httpMethod = "GET", notes = "获取已激活的应用三员管理员列表")
    @ApiImplicitParam(name = "params", value = "获取激活用户Id", required = true, dataType = "Map")
    @AceAuth("获取已激活的应用三员管理员列表")
    @RequestMapping(value = "/query/app/allocatedData", method = RequestMethod.GET)
    public R getAppAllocatedData(@RequestParam Map<String, Object> params) {
        return R.ok().put("threeUsers", sysUserRoleService.getAppAllocatedData(params));
    }

    /**
     * 获取已激活和未激活的应用三员管理员列表
     *
     * @param
     * @return
     * @author zuogang
     * @date 2019/4/23 16:51
     */
    @ApiOperation(value = "获取已激活和未激活的应用三员管理员列表", httpMethod = "GET", notes = "获取已激活和未激活的应用三员管理员列表")
    @ApiImplicitParam(name = "params", value = "获取激活用户Id", required = true, dataType = "Map")
    @AceAuth("获取已激活和未激活的应用三员管理员列表")
    @RequestMapping(value = "/query/app/allThreeData", method = RequestMethod.GET)
    public R getAppAllThreeData(@RequestParam Map<String, Object> params) {
        return R.ok().put("threeUsers", sysUserRoleService.getAppAllThreeData(params));
    }

    /**
     * 通过用户ID获取角色信息(激活与未激活)
     *
     * @param
     * @return
     * @author zuogang
     * @date 2019/4/23 16:51
     */
    @ApiOperation(value = "通过用户ID获取角色信息(激活与未激活)", httpMethod = "GET", notes = "通过用户ID获取角色信息(激活与未激活)")
    @ApiImplicitParam(name = "params", value = "参数", required = true, dataType = "Map")
    @AceAuth("通过用户ID获取角色信息(激活与未激活)")
    @RequestMapping(value = "/query/rolesByUserId", method = RequestMethod.GET)
    public R getRolesByUserId(@RequestParam Map<String, Object> params) {
        return R.ok().put("roles", sysUserRoleService.getRolesByUserId(params));
    }

    /**
     * 通过角色ID获取用户信息
     *
     * @param
     * @return
     * @author zuogang
     * @date 2019/4/23 16:51
     */
    @ApiOperation(value = "通过角色ID获取用户信息", httpMethod = "GET", notes = "通过角色ID获取用户信息")
    @ApiImplicitParam(name = "params", value = "参数", required = true, dataType = "Map")
    @AceAuth("通过角色ID获取用户信息")
    @RequestMapping(value = "/query/usersByRoleId", method = RequestMethod.GET)
    public R getUsersByRoleId(@RequestParam Map<String, Object> params) {
        return R.ok().put("users", sysUserRoleService.getUsersByRoleId(params));
    }

    /**
     * 通过角色ID获取有效用户信息
     *
     * @param
     * @return
     * @author zuogang
     * @date 2019/4/23 16:51
     */
    @ApiOperation(value = "通过角色ID获取有效用户信息", httpMethod = "GET", notes = "通过角色ID获取有效用户信息")
    @ApiImplicitParam(name = "params", value = "参数", required = true, dataType = "Map")
    @AceAuth("通过角色ID获取有效用户信息")
    @RequestMapping(value = "/query/activedUsersByRoleId", method = RequestMethod.GET)
    public R getActivedUsersByRoleId(@RequestParam Map<String, Object> params) {
        return R.ok().put("users", sysUserRoleService.getActivedUsersByRoleId(params));
    }

    /**
     * 用户指派角色
     *
     * @param
     * @return
     * @author zuogang
     * @date 2019/4/23 16:51
     */
    @ApiOperation(value = "用户指派角色", httpMethod = "POST", notes = "用户指派角色")
    @ApiImplicitParam(name = "params", value = "参数", required = true, dataType = "Map")
    @AceAuth("用户指派角色")
    @RequestMapping(value = "/save/userIdAndRoleIds", method = RequestMethod.POST)
    public R saveUserIdAndRoleIds(@RequestBody Map<String, Object> map) {
        return sysUserRoleService.saveUserIdAndRoleIds(map);
//        if (sysUserRoleService.saveUserIdAndRoleIds(map)) {
//            return R.ok(InternationUtils.getInternationalMsg("SET_SUCCESS"));
//        }
//        return R.error(InternationUtils.getInternationalMsg("SET_FAILED"));
    }

    /**
     * 用户指派角色（分配并激活）
     *
     * @param
     * @return
     * @author zuogang
     * @date 2019/4/23 16:51
     */
    @ApiOperation(value = "用户指派角色（分配并激活）", httpMethod = "POST", notes = "用户指派角色（分配并激活）")
    @ApiImplicitParam(name = "params", value = "参数", required = true, dataType = "Map")
    @AceAuth("用户指派角色（分配并激活）")
    @RequestMapping(value = "/saveAndActive/userIdAndRoleIds", method = RequestMethod.POST)
    public R saveAndActiveUserIdAndRoleIds(@RequestBody Map<String, Object> map) {
        // 用户指派角色（分配）
        R r = sysUserRoleService.saveUserIdAndRoleIds(map);
        if ((Integer) r.get("code") == 40000) {
            // 用户指派角色（激活）
            if (sysUserRoleService.activeByUserId(map)) {
                return R.ok(InternationUtils.getInternationalMsg("SET_SUCCESS"));
            }
            return R.error(InternationUtils.getInternationalMsg("SET_FAILED"));
        }
        return r;
    }

    /**
     * 角色指派用户
     *
     * @param
     * @return
     * @author zuogang
     * @date 2019/4/23 16:51
     */
    @ApiOperation(value = "角色指派用户", httpMethod = "POST", notes = "角色指派用户")
    @ApiImplicitParam(name = "params", value = "参数", required = true, dataType = "Map")
    @AceAuth("角色指派用户")
    @RequestMapping(value = "/save/roleIdAndUserIds", method = RequestMethod.POST)
    public R saveRoleIdAndUserIds(@RequestBody Map<String, Object> map) {
        Map<String, Object> result = sysUserRoleService.saveRoleIdAndUserIds(map);
        if (Strings.isNotBlank((String) result.get("success"))) {
            return R.ok((String) result.get("success"));
        } else {
            return R.error((String) result.get("error"));
        }
//        if (sysUserRoleService.saveRoleIdAndUserIds(map)) {
//            return R.ok(InternationUtils.getInternationalMsg("SET_SUCCESS"));
//        }
//        return R.error(InternationUtils.getInternationalMsg("SET_FAILED"));
    }


    /**
     * 角色指派用户（分配并激活）
     *
     * @param
     * @return
     * @author zuogang
     * @date 2019/4/23 16:51
     */
    @ApiOperation(value = "角色指派用户（分配并激活）", httpMethod = "POST", notes = "角色指派用户（分配并激活）")
    @ApiImplicitParam(name = "params", value = "参数", required = true, dataType = "Map")
    @AceAuth("角色指派用户（分配并激活）")
    @RequestMapping(value = "/saveAndActive/roleIdAndUserIds", method = RequestMethod.POST)
    public R saveAndActiveRoleIdAndUserIds(@RequestBody Map<String, Object> map) {
        // 分配
        Map<String, Object> result = sysUserRoleService.saveRoleIdAndUserIds(map);
        if (Strings.isNotBlank((String) result.get("success"))) {
            List<String> changeUserIds = (List<String>) result.get("changeUserIds");
            changeUserIds.stream().forEach(userId -> {
                Map<String, Object> param = new HashMap<>();
                param.put("userId", userId);
                param.put("appId", map.get("appId"));
                if (!sysUserRoleService.activeByUserId(param)) {
                    throw new RException(InternationUtils.getInternationalMsg("SET_FAILED"));
                }
            });
            return R.ok(InternationUtils.getInternationalMsg("SET_SUCCESS"));
        } else {
            return R.error((String) result.get("error"));
        }
    }

    /**
     * 全局角色激活用户
     *
     * @param
     * @return
     * @author zuogang
     * @date 2019/4/23 16:51
     */
    @ApiOperation(value = "全局角色激活用户", httpMethod = "POST", notes = "全局角色激活用户")
    @ApiImplicitParam(name = "params", value = "参数", required = true, dataType = "Map")
    @AceAuth("全局角色激活用户")
    @RequestMapping(value = "/save/activeScopeRoleIdAndUserIds", method = RequestMethod.POST)
    public R activeScopeRoleIdAndUserIds(@RequestBody Map<String, Object> map) {
        //指派用户
//        Map<String, String> result = sysUserRoleService.saveRoleIdAndUserIds(map);
//        if (Strings.isNotBlank(result.get("success"))) {
        //激活关系
        if (sysUserRoleService.activeScopeRoleIdAndUserIds(map)) {
            return R.ok(InternationUtils.getInternationalMsg("SET_SUCCESS"));
        }
        return R.error(InternationUtils.getInternationalMsg("SET_FAILED"));
//        } else {
//            return R.error(result.get("error"));
//        }

    }

    /**
     * 全局角色指派并激活用户
     *
     * @param
     * @return
     * @author zuogang
     * @date 2019/4/23 16:51
     */
    @ApiOperation(value = "全局角色激活用户", httpMethod = "POST", notes = "全局角色激活用户")
    @ApiImplicitParam(name = "params", value = "参数", required = true, dataType = "Map")
    @AceAuth("全局角色激活用户")
    @RequestMapping(value = "/save/assignAndActiveScopeRoleIdAndUserIds", method = RequestMethod.POST)
    public R assignAndActiveScopeRoleIdAndUserIds(@RequestBody Map<String, Object> map) {
        Map<String, Object> result = sysUserRoleService.saveRoleIdAndUserIds(map);
        if (Strings.isNotBlank((String) result.get("success"))) {
            //激活关系
            if (sysUserRoleService.activeScopeRoleIdAndUserIds(map)) {
                return R.ok(InternationUtils.getInternationalMsg("SET_SUCCESS"));
            }
            return R.error(InternationUtils.getInternationalMsg("SET_FAILED"));
        } else {
            return R.error((String) result.get("error"));
        }

    }

    /**
     * 获取未被激活的用户角色信息
     *
     * @param
     * @return
     * @author zuogang
     * @date 2019/4/23 16:51
     */
    @ApiOperation(value = "获取未被激活的用户角色信息", httpMethod = "POST", notes = "角色指派用户")
    @ApiImplicitParam(name = "params", value = "参数", required = true, dataType = "Map")
    @AceAuth("获取未被激活的用户角色信息")
    @RequestMapping(value = "/waitActiveUserData", method = RequestMethod.POST)
    public R getWaitActiveUserData(@RequestBody Map<String, Object> map) {
        return R.ok().put("waitActiveData", sysUserRoleService.getWaitActiveUserData(map));
    }

    /**
     * 用户角色关系激活
     *
     * @param
     * @return
     * @author zuogang
     * @date 2019/4/23 16:51
     */
    @ApiOperation(value = "用户角色关系激活", httpMethod = "POST", notes = "用户角色关系激活")
    @ApiImplicitParam(name = "params", value = "参数", required = true, dataType = "Map")
    @AceAuth("用户角色关系激活")
    @RequestMapping(value = "/active/byUserId", method = RequestMethod.POST)
    public R activeByUserId(@RequestBody Map<String, Object> map) {
        if (sysUserRoleService.activeByUserId(map)) {
            return R.ok(InternationUtils.getInternationalMsg("SET_SUCCESS"));
        }
        return R.error(InternationUtils.getInternationalMsg("SET_FAILED"));
    }

    /**
     * 通过用户ID获取有效角色信息
     *
     * @param
     * @return
     * @author zuogang
     * @date 2019/4/23 16:51
     */
    @ApiOperation(value = "通过用户ID获取有效角色信息", httpMethod = "POST", notes = "通过用户ID获取有效角色信息")
    @ApiImplicitParam(name = "params", value = "参数", required = true, dataType = "Map")
    @AceAuth("通过角色ID获取用户信息")
    @RequestMapping(value = "/getEffectiveRoleData", method = RequestMethod.POST)
    public R getEffectiveRoleData(@RequestBody Map<String, Object> params) {
        String userId = (String) params.get("userId");
        String appId = (String) params.get("appId");
        return R.ok().put("list", sysUserRoleService.getEffectiveRoleData(userId, appId));
    }

    /**
     * 一键激活用户角色
     *
     * @param
     * @return
     * @author zuogang
     * @date 2019/4/23 16:51
     */
    @ApiOperation(value = "一键激活用户角色", httpMethod = "POST", notes = "一键激活用户角色")
    @ApiImplicitParam(name = "params", value = "参数", required = true, dataType = "Map")
    @AceAuth("一键激活用户角色")
    @RequestMapping(value = "/allActive", method = RequestMethod.POST)
    public R allActive(@RequestBody Map<String, Object> params) {
        if (sysUserRoleService.allActive(params)) {
            return R.ok(InternationUtils.getInternationalMsg("SET_SUCCESS"));
        }
        return R.error(InternationUtils.getInternationalMsg("SET_FAILED"));
    }

    /**
     * 计算当前时间段下的变更过角色的用户列表
     *
     * @param
     * @return
     * @author zuogang
     * @date 2019/4/23 16:51
     */
    @ApiOperation(value = "计算当前时间段下的变更过角色的用户列表", httpMethod = "POST", notes = "计算当前时间段下的变更过角色的用户列表")
    @ApiImplicitParam(name = "params", value = "参数", required = true, dataType = "Map")
    @AceAuth("计算当前时间段下的变更过角色的用户列表")
    @RequestMapping(value = "/changeRole/getUserList", method = RequestMethod.POST)
    public R getChangeRoleUserList(@RequestBody Map<String, Object> params) {
        List<SysUserDO> sysUserDOS = sysUserRoleService.getChangeRoleUserList(params);
        return R.ok().put("sysUserDOS", sysUserDOS);
    }

    /**
     * 通过userID获取该用户的角色版本历史数据
     *
     * @param
     * @return
     * @author zuogang
     * @date 2019/4/23 16:51
     */
    @ApiOperation(value = "通过userID获取该用户的角色版本历史数据", httpMethod = "POST", notes = "通过userID获取该用户的角色版本历史数据")
    @ApiImplicitParam(name = "params", value = "参数", required = true, dataType = "Map")
    @AceAuth("通过userID获取该用户的角色版本历史数据")
    @RequestMapping(value = "/getRoleHistoryByUserId", method = RequestMethod.POST)
    public R getRoleHistoryByUserId(@RequestBody Map<String, Object> params) {
        String userId = (String) params.get("userId");
        String appId = (String) params.get("appId");
        Map<Integer, Object> roleHistoryDatas = sysUserRoleVService.getRoleHistoryByUserId(appId, userId);
        SysUserRoleLvDO sysUserRoleLvDO = sysUserRoleLvService.getOne(new QueryWrapper<SysUserRoleLvDO>()
                .eq("app_id", appId).eq("user_id", userId).eq("is_latest", 1));
        return R.ok().put("roleHistoryDatas", roleHistoryDatas).put("lastVersion", "V" + sysUserRoleLvDO.getVersion()
                .toString());
    }

    /**
     * 刷新管理员三员用户有效权限
     *
     * @return
     * @author zuogang
     * @date 2019/4/23 16:51
     */
    @ApiOperation(value = "刷新管理员三员用户有效权限", httpMethod = "POST", notes = "刷新管理员三员用户有效权限")
    @ApiImplicitParam(name = "params", value = "参数", required = true, dataType = "Map")
    @AceAuth("刷新管理员三员用户有效权限")
    @RequestMapping(value = "/refresh/threeAdmin/authMix", method = RequestMethod.POST)
    public R refreshAppThreeAuth(@RequestBody String[] roleIds) {
        if (sysUserRoleService.refreshAppThreeAuth(Arrays.asList(roleIds))) {
            return R.ok(InternationUtils.getInternationalMsg("SET_SUCCESS"));
        }
        return R.error(InternationUtils.getInternationalMsg("SET_FAILED"));
    }
}

package com.csicit.ace.platform.core.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.csicit.ace.common.annotation.AceAuth;
import com.csicit.ace.common.pojo.domain.*;
import com.csicit.ace.common.utils.StringUtils;
import com.csicit.ace.common.utils.TreeUtils;
import com.csicit.ace.common.utils.internationalization.InternationUtils;
import com.csicit.ace.common.utils.server.R;
import com.csicit.ace.platform.core.service.*;
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


/**
 * 权限管理 接口访问层
 *
 * @author shanwj
 * @version V1.0
 * @date 2019-04-10 18:57:46
 */

@RestController
@RequestMapping("/sysAuths")
@Api("权限管理")
public class SysAuthController extends BaseController {

    @Autowired
    SysAuthApiService sysAuthApiService;

    @Autowired
    SysAuthUserLvService sysAuthUserLvService;

    @Autowired
    SysAuthUserVService sysAuthUserVService;

    @Autowired
    SysAuthRoleLvService sysAuthRoleLvService;

    @Autowired
    SysAuthRoleVService sysAuthRoleVService;


    /**
     * 根据id获取单个权限
     *
     * @param id 权限id
     * @return 单个权限
     * @author shanwj
     * @date 2019/4/11 18:25
     */
    @ApiImplicitParam(name = "id", value = "主键id", dataType = "String", required = true)
    @ApiOperation(value = "获取单个权限", httpMethod = "GET", notes = "获取单个权限")
    @AceAuth("获取单个权限")
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public R get(@PathVariable("id") String id) {
        return sysAuthService.infoAuth(id);
    }

    /**
     * 获取权限列表
     *
     * @param params 请求参数map对象
     * @return 权限列表
     * @author zuogang
     * @date 2019/4/11 18:21
     */
    @ApiOperation(value = "获取权限列表", httpMethod = "GET", notes = "获取权限列表")
    @ApiImplicitParam(name = "params", value = "请求参数", required = true, dataType = "Map")
    @AceAuth("获取权限列表")
    @RequestMapping(method = RequestMethod.GET)
    public R list(@RequestParam Map<String, Object> params) {
        String appId = params.get("appId").toString();
        String name = params.get("name").toString();
        List<SysAuthDO> auths = sysAuthService.list(
                new QueryWrapper<SysAuthDO>()
                        .eq("app_id", appId)
                        .like("name", name)
                        .orderByAsc("sort_path"));
        auths.stream().forEach(auth -> {
            if (Objects.equals("0", auth.getParentId())) {
                auth.setParentName("一级权限");
            } else {
                auth.setParentName(sysAuthService.getById(auth.getParentId()).getName());
            }
        });
        //生成树结构的数据
        List<SysAuthDO> listT = TreeUtils.makeTree(auths, SysAuthDO.class);
        return R.ok().put("list", listT);
    }

    /**
     * 判断当前应用当前权限节点下排序是否是否存在
     *
     * @param appId     应用id
     * @param parentId  父节点id
     * @param sortIndex 当前节点下排序
     * @return true 存在
     */
    @ApiOperation(value = "判断当前应用当前权限节点下排序是否是否存在", httpMethod = "POST", notes = "判断当前应用当前权限节点下排序是否是否存在")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "appId", value = "应用id", required = true, dataType = "String"),
            @ApiImplicitParam(name = "parentId", value = "父节点id", required = true, dataType = "String"),
            @ApiImplicitParam(name = "sortIndex", value = "排序号", required = true, dataType = "Integer")
    })
    @AceAuth("判断当前应用当前权限节点下排序是否是否存在")
    @RequestMapping(value = "/action/exist-shortIndex-check/app/{appId}/parentId/{parentId}/shortIndex/{sortIndex}"
            , method = RequestMethod.GET)
    public boolean shortIndexExistCheck(@PathVariable("appId") String appId,
                                        @PathVariable("parentId") String parentId,
                                        @PathVariable("sortIndex") Integer sortIndex) {
        return sysAuthService.count(
                new QueryWrapper<SysAuthDO>()
                        .eq("app_id", appId)
                        .eq("parent_id", parentId)
                        .eq("sort_index", sortIndex)) > 0;
    }

    /**
     * 判断当前应用权限标识是否存在
     *
     * @param appId 应用id
     * @param code  权限标识
     * @return true 存在
     */
    @ApiOperation(value = "判断当前应用权限标识是否存在", httpMethod = "POST", notes = "判断当前应用权限标识是否存在")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "appId", value = "应用id", required = true, dataType = "String"),
            @ApiImplicitParam(name = "code", value = "权限标识", required = true, dataType = "String")
    })
    @AceAuth("判断当前应用权限标识是否存在")
    @RequestMapping(value = "/action/exist-code-check/app/{appId}/code/{code}"
            , method = RequestMethod.GET)
    public boolean codeExistCheck(@PathVariable("appId") String appId,
                                  @PathVariable("code") String code) {
        return sysAuthService.count(
                new QueryWrapper<SysAuthDO>().eq("app_id", appId).eq("code", code)) > 0;
    }

    /**
     * 新增权限保存
     *
     * @param auth 权限对象
     * @return 保存响应结果
     * @author zuogang
     * @date 2019/4/11 18:26
     */
    @ApiOperation(value = "保存权限", httpMethod = "POST", notes = "保存权限")
    @ApiImplicitParam(name = "auth", value = "权限实体", required = true, dataType = "SysAuthDO")
    @AceAuth("保存权限")
    @RequestMapping(method = RequestMethod.POST)
    public R save(@RequestBody SysAuthDO auth) {
        //判断父节点是否存在
        if (!Objects.equals("0", auth.getParentId()) &&
                sysAuthService.count(new QueryWrapper<SysAuthDO>().eq("id", auth.getParentId())) == 0) {
            return R.error(InternationUtils.getInternationalMsg("PARENT_DELETE"));
        }
        //判断权限标识是否已存在
        if (StringUtils.isNotBlank(auth.getCode())) {
            if (codeExistCheck(auth.getAppId(), auth.getCode())) {
                return R.error(InternationUtils.getInternationalMsg("SAME_MODEL",
                        new String[]{InternationUtils.getInternationalMsg("AUTH_CODE"), auth.getCode()}
                ));
            }
        }
        //判断当前节点排序号是否已存在
        if (shortIndexExistCheck(auth.getAppId(), auth.getParentId(), auth.getSortIndex())) {
            return R.error(InternationUtils.getInternationalMsg("SAME_MODEL",
                    new String[]{InternationUtils.getInternationalMsg("SHORT_INDEX"), auth.getSortIndex().toString()}
            ));
        }

        if (sysAuthService.saveAuth(auth)) {
            return R.ok(InternationUtils.getInternationalMsg("SAVE_SUCCESS"));
        }
        return R.error(InternationUtils.getInternationalMsg("SAVE_FAILED"));
    }

    /**
     * 修改权限
     *
     * @param auth 权限对象
     * @return 更新响应时结果
     * @author zuogang
     * @date 2019/4/11 18:27
     */
    @ApiOperation(value = "修改权限", httpMethod = "PUT", notes = "修改权限")
    @ApiImplicitParam(name = "auth", value = "权限实体", required = true, dataType = "SysAuthDO")
    @AceAuth("修改权限")
    @RequestMapping(method = RequestMethod.PUT)
    public R update(@RequestBody SysAuthDO auth) {
        //判断父节点是否存在
        if (!Objects.equals("0", auth.getParentId()) &&
                sysAuthService.count(new QueryWrapper<SysAuthDO>().eq("id", auth.getParentId())) == 0) {
            return R.error(InternationUtils.getInternationalMsg("PARENT_DELETE"));
        }
        // 判断排序号是否变化
        if (!Objects.equals(sysAuthService.getById(auth.getId()).getSortIndex(), auth.getSortIndex())) {
            //判断当前节点排序号是否已存在
            if (shortIndexExistCheck(auth.getAppId(), auth.getParentId(), auth.getSortIndex())) {
                return R.error(InternationUtils.getInternationalMsg("SAME_MODEL",
                        InternationUtils.getInternationalMsg("SHORT_INDEX"), auth.getSortIndex()));
            }
        }
        //  判断权限标识是否变化
        if (!Objects.equals(sysAuthService.getById(auth.getId()).getCode(), auth.getCode())) {
            //判断权限标识是否已存在
            if (StringUtils.isNotBlank(auth.getCode())) {
                if (codeExistCheck(auth.getAppId(), auth.getCode())) {
                    return R.error(InternationUtils.getInternationalMsg("SAME_MODEL",
                            new String[]{InternationUtils.getInternationalMsg("AUTH_CODE"), auth.getCode()}
                    ));
                }
            }
        }

        if (sysAuthService.updateAuth(auth)) {
            return R.ok(InternationUtils.getInternationalMsg("UPDATE_SUCCESS"));
        }
        return R.error(InternationUtils.getInternationalMsg("UPDATE_FAILED"));
    }

    /**
     * 删除权限
     *
     * @param ids 权限ID数组
     * @return 删除响应结果
     * @author shanwj
     * @date 2019/4/11 18:27
     */
    @ApiOperation(value = "删除权限", httpMethod = "DELETE", notes = "删除权限")
    @ApiImplicitParam(name = "ids", value = "权限ID数组", required = true, dataType = "String[]")
    @AceAuth("删除权限")
    @RequestMapping(method = RequestMethod.DELETE)
    public R delete(@RequestBody String[] ids) {
        if (sysAuthService.deleteByIds(Arrays.asList(ids))) {
            return R.ok(InternationUtils.getInternationalMsg("DELETE_SUCCESS"));
        }
        return R.error(InternationUtils.getInternationalMsg("DELETE_FAILED"));
    }

    /**
     * 当前权限设置API资源
     *
     * @param auth 参数对象
     * @return 响应结果
     * @author shanwj
     * @date 2019/4/18 17:34
     */
    @ApiOperation(value = "当前权限设置API资源", httpMethod = "POST", notes = "当前权限设置API资源")
    @ApiImplicitParam(name = "auth", value = "权限实体", required = true, dataType = "SysAuthDO")
    @AceAuth("当前权限设置API资源")
    @RequestMapping(value = "/authApis/auth/apis", method = RequestMethod.POST)
    public R saveAuthApis(@RequestBody SysAuthDO auth) {
        String id = auth.getId();
        List<SysApiResourceDO> apis = auth.getApis();
        if (sysAuthApiService.saveAuthApi(id, apis)) {
            return R.ok(InternationUtils.getInternationalMsg("SET_SUCCESS"));
        }
        return R.error(InternationUtils.getInternationalMsg("SET_FAILED"));
    }

    /**
     * 通过UserId获取用户有效权限列表
     *
     * @param params
     * @return List<SysAuthDO>
     * @author zuogang
     * @date 2019/4/17 17:01
     */
    @ApiOperation(value = "通过UserId获取用户有效权限列表")
    @ApiImplicitParam(name = "params", value = "参数", required = true, dataType = "Map")
    @AceAuth("通过UserId获取用户有效权限列表")
    @RequestMapping(value = "/query/user/authMix", method = RequestMethod.GET)
    public R queryUserAuthMix(@RequestParam Map<String, Object> params) {
        String userId = (String) params.get("userId");
        String appId = (String) params.get("appId");
        List<SysAuthDO> auths = sysAuthService.queryUserAuthMix(userId, appId);
        //生成树结构的数据
        List<SysAuthDO> listT = TreeUtils.makeTree(auths, SysAuthDO.class);
        return R.ok().put("list", listT);
    }

//    /**
//     * 修改有效权限组织授控域
//     *
//     * @param grantAuthReciveVO
//     * @return
//     * @author zuogang
//     * @date 2019/4/17 17:01
//     */
//    @ApiOperation(value = "修改有效权限组织授控域")
//    @ApiImplicitParam(name = "GrantAuthReciveVO", value = "参数", required = true, dataType = "GrantAuthReciveVO")
//    @AceAuth("修改有效权限组织授控域")
//    @RequestMapping(value = "/authMix/orgControlDomain", method = RequestMethod.POST)
//    public R saveAuthMixOrgControlDomain(@RequestBody GrantAuthReciveVO grantAuthReciveVO) {
//        if (sysAuthService.saveAuthMixOrgControlDomain(grantAuthReciveVO)) {
//            return R.ok(InternationUtils.getInternationalMsg("SET_SUCCESS"));
//        }
//        return R.error(InternationUtils.getInternationalMsg("SET_FAILED"));
//    }

//    /**
//     * 修改有效权限用户组授控域
//     *
//     * @param grantAuthReciveVO
//     * @return
//     * @author zuogang
//     * @date 2019/4/17 17:01
//     */
//    @ApiOperation(value = "修改有效权限用户组授控域")
//    @ApiImplicitParam(name = "GrantAuthReciveVO", value = "参数", required = true, dataType = "GrantAuthReciveVO")
//    @AceAuth("修改有效权限用户组授控域")
//    @RequestMapping(value = "/authMix/userGroupControlDomain", method = RequestMethod.POST)
//    public R saveAuthMixUserGroupControlDomain(@RequestBody GrantAuthReciveVO grantAuthReciveVO) {
//        if (sysAuthService.saveAuthMixUserGroupControlDomain(grantAuthReciveVO)) {
//            return R.ok(InternationUtils.getInternationalMsg("SET_SUCCESS"));
//        }
//        return R.error(InternationUtils.getInternationalMsg("SET_FAILED"));
//    }

    /**
     * 计算当前时间段下的变更过权限的用户角色列表
     *
     * @param
     * @return
     * @author zuogang
     * @date 2019/4/23 16:51
     */
    @ApiOperation(value = "计算当前时间段下的变更过权限的用户角色列表", httpMethod = "POST", notes = "计算当前时间段下的变更过权限的用户角色列表")
    @ApiImplicitParam(name = "params", value = "参数", required = true, dataType = "Map")
    @AceAuth("计算当前时间段下的变更过权限的用户角色列表")
    @RequestMapping(value = "/changeAuth/getUserAndRoleList", method = RequestMethod.POST)
    public R getChangeUserAndRoleList(@RequestBody Map<String, Object> params) {
        List<SysUserDO> sysUserDOS = sysAuthUserService.getChangeUserList(params);
        List<SysRoleDO> sysRoleDOS = sysAuthRoleService.getChangeRoleList(params);
        return R.ok().put("sysUserDOS", sysUserDOS).put("sysRoleDOS", sysRoleDOS);
    }

    /**
     * 根据用户ID或角色ID获取历史权限关系信息
     *
     * @param
     * @return
     * @author zuogang
     * @date 2019/4/23 16:51
     */
    @ApiOperation(value = "根据用户ID或角色ID获取历史权限关系信息", httpMethod = "POST", notes = "根据用户ID或角色ID获取历史权限关系信息")
    @ApiImplicitParam(name = "params", value = "参数", required = true, dataType = "Map")
    @AceAuth("根据用户ID或角色ID获取历史权限关系信息")
    @RequestMapping(value = "/getAuthHistory/byUserIdOrRoleId", method = RequestMethod.POST)
    public R getAuthHistory(@RequestBody Map<String, Object> params) {
        String appId = (String) params.get("appId");
        String userId = (String) params.get("userId");
        String roleId = (String) params.get("roleId");

        Map<Integer, Object> authHistoryDatas;
        if (StringUtils.isNotBlank(roleId)) {
            authHistoryDatas = sysAuthRoleVService.getAuthHistoryByRoleId(roleId);
            SysAuthRoleLvDO sysAuthRoleLvDO = sysAuthRoleLvService.getOne(new QueryWrapper<SysAuthRoleLvDO>()
                    .eq("role_id", roleId).eq("is_last_version", 1));

            return R.ok().put("authHistoryDatas", authHistoryDatas).put("lastVersion", "V" + sysAuthRoleLvDO
                    .getVersionNo()
                    .toString());
        }
        authHistoryDatas = sysAuthUserVService.getAuthHistoryByUserId(appId, userId);
        SysAuthUserLvDO sysAuthUserLvDO = sysAuthUserLvService.getOne(new QueryWrapper<SysAuthUserLvDO>()
                .eq("app_id", appId).eq("user_id", userId).eq("is_last_version", 1));

        return R.ok().put("authHistoryDatas", authHistoryDatas).put("lastVersion", "V" + sysAuthUserLvDO.getVersionNo()
                .toString());

    }

    /**
     * 移动权限
     *
     * @param params
     * @return
     * @author yansiyang
     * @date 2019/4/12 9:12
     */
    @ApiOperation(value = "移动权限", httpMethod = "POST", notes = "移动权限")
    @ApiImplicitParam(name = "params", value = "参数", required = true, dataType = "Map")
    @AceAuth("移动菜单")
    @RequestMapping(value = "/action/moveAuth", method = RequestMethod.POST)
    public R moveAuth(@RequestBody Map<String, Object> params) {
//        if (sysAuthService.moveAuth(params)) {
//            return R.ok(InternationUtils.getInternationalMsg("SET_SUCCESS"));
//        }
//        return R.error(InternationUtils.getInternationalMsg("UPDATE_FAILED"));
        return sysAuthService.moveAuth(params);
    }
}

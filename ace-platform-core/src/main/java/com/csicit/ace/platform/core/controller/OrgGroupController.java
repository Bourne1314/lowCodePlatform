package com.csicit.ace.platform.core.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.csicit.ace.common.annotation.AceAuth;
import com.csicit.ace.common.pojo.domain.OrgGroupDO;
import com.csicit.ace.common.pojo.domain.SysRoleDO;
import com.csicit.ace.common.pojo.domain.SysUserDO;
import com.csicit.ace.common.utils.StringUtils;
import com.csicit.ace.common.utils.TreeUtils;
import com.csicit.ace.common.utils.internationalization.InternationUtils;
import com.csicit.ace.common.utils.server.R;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;


/**
 * 集团管理 接口访问层
 *
 * @author shanwj
 * @version V1.0
 * @date 2019-04-10 18:57:46
 */

@RestController
@RequestMapping("/orgGroups")
@Api("集团管理")
public class OrgGroupController extends BaseController {

//    @Autowired
//    IMessage message;

    /**
     * 移动集团
     *
     * @param params {mvToTop,id,groupId}
     * @return
     * @author yansiyang
     * @date 2019/4/18 19:30
     */
    @ApiOperation(value = "移动集团", httpMethod = "POST")
    @ApiImplicitParam(name = "instance", value = "实体", required = true, dataType = "Map")
    @AceAuth("移动集团")
    @RequestMapping(value = "/action/mvGroup", method = RequestMethod.POST)
    public R mvGroup(@RequestBody Map<String, String> params) {
        String id = params.get("id");
        String targetGroupId = params.get("groupId");
        String mvToTop = params.get("mvToTop");
        if (orgGroupService.mvGroup(Objects.equals(mvToTop, "1"), id, targetGroupId)) {
            return R.ok(InternationUtils.getInternationalMsg("OPERATE_SUCCESS"));
        }

        return R.error(InternationUtils.getInternationalMsg("OPERATE_FAILED"));
    }

    /**
     * 获取单个集团
     *
     * @param id 集团id
     * @return com.csicit.ace.common.utils.server.R
     * @author shanwj
     * @date 2019/4/11 18:25
     */
    @ApiImplicitParam(name = "id", value = "主键id", dataType = "String", required = true)
    @ApiOperation(value = "获取单个集团", httpMethod = "GET", notes = "获取单个集团")
    @AceAuth("获取单个集团")
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public R get(@PathVariable("id") String id) {
        OrgGroupDO instance = orgGroupService.getById(id);
        return R.ok().put("instance", instance);
    }

    /**
     * 获取集团列表
     *
     * @return com.csicit.ace.common.utils.server.R 集团列表
     * @author yansiyang
     * @date 2019/4/11 18:21
     */
    @ApiOperation(value = "获取集团列表", httpMethod = "GET")
    @ApiImplicitParam(name = "params", value = "参数", required = true, dataType = "Map")
    @AceAuth("获取集团列表")
    @RequestMapping(value = "", method = RequestMethod.GET)
    public R list(@RequestParam Map<String, Object> params) {
        String search = (String) params.get("search");
        String newSearch = StringUtils.isNotBlank(search) ? search : "";
        List<OrgGroupDO> list;
        // 只有租户管理员可以查看所有的集团
        List<SysRoleDO> roleDOS = sysUserRoleService.getEffectiveRoleData(securityUtils.getCurrentUserId(), null);
        SysRoleDO role = null;
        if (CollectionUtils.isNotEmpty(roleDOS)) {
            role = roleDOS.get(0);
        }
        if (role == null) {
            list = orgGroupService.getGroupsByUserId(securityUtils.getCurrentUserId()).stream().filter(o -> o.getName
                    ().contains(newSearch) || o.getCode().contains(newSearch)).collect(Collectors.toList());
        } else {
            // 租户系统管理员  安全保密员
            if (Objects.equals(1, role.getRoleType()) || Objects.equals(2, role.getRoleType())) {
                list = orgGroupService.list(new QueryWrapper<OrgGroupDO>().and(StringUtils.isNotBlank
                        (search), i -> i.like("name", search).or().like("code", search)).eq("is_delete", 0)
                        .orderByAsc("sort_path"));
                // 集团系统管理员 安全保密员   集团超级管理员 单一业务管理员
            } else if (Objects.equals(11, role.getRoleType()) || Objects.equals(22, role.getRoleType()) || Objects
                    .equals(44, role.getRoleType()) || Objects
                    .equals(4, role.getRoleType())) {
                list = orgGroupService.getGroupsByUserId(securityUtils.getCurrentUserId()).stream().filter(o -> o
                        .getName
                        ().contains(newSearch) || o.getCode().contains(newSearch)).collect(Collectors.toList());
            } else {
                list = orgGroupService.getGroupsByUserId(securityUtils.getCurrentUserId()).stream().filter(o -> o
                        .getName
                        ().contains(newSearch) || o.getCode().contains(newSearch)).collect(Collectors.toList());
            }
        }
        Map<String, String> idAndName = new HashMap<>();
        if (list != null && list.size() > 0) {
            list.forEach(group -> {
                idAndName.put(group.getId(), group.getName());
            });
            list.forEach(group -> {
                // 如果 上级集团存在 且 不存在于结果中  则从数据库中查询
                if (group.getParentId() != null && !Objects.equals(group.getParentId(), "0") && !idAndName
                        .containsKey(group

                                .getParentId())) {
                    OrgGroupDO tempGroup = orgGroupService.getById(group.getParentId());
                    idAndName.put(tempGroup.getId(), tempGroup.getName());
                }
                group.setParentName(idAndName.get(group.getParentId()));
            });
        }
        /**
         * 生成树结构的数据
         *
         */
        List<OrgGroupDO> listT = TreeUtils.makeTree(list, OrgGroupDO.class);
        return R.ok().put("list", listT).put("dataList", list);
    }


    /**
     * 获取集团列表（不判断管理员权限）
     *
     * @return 集团集合不带分页
     * @author generator
     * @date 2019-04-15 20:12:24
     */
    @ApiOperation(value = "获取集团列表", httpMethod = "GET", notes = "获取集团列表")
    @AceAuth("获取集团列表")
    @RequestMapping(value = "/list/all", method = RequestMethod.GET)
    public R listAll() {
        List<OrgGroupDO> list = orgGroupService.list(new QueryWrapper<OrgGroupDO>()
                .eq("is_delete", 0).orderByAsc("sort_path"));
        Map<String, String> idAndName = new HashMap<>();
        if (list != null && list.size() > 0) {
            list.forEach(group -> {
                idAndName.put(group.getId(), group.getName());
            });
            list.forEach(group -> {
                // 如果 上级集团存在 且 不存在于结果中  则从数据库中查询
                if (group.getParentId() != null && !Objects.equals(group.getParentId(), "0") && !idAndName
                        .containsKey(group

                                .getParentId())) {
                    OrgGroupDO tempGroup = orgGroupService.getById(group.getParentId());
                    idAndName.put(tempGroup.getId(), tempGroup.getName());
                }
                group.setParentName(idAndName.get(group.getParentId()));
            });
        }
        List<OrgGroupDO> listT = TreeUtils.makeTree(list, OrgGroupDO.class);
        return R.ok().put("list", listT);
    }

    /**
     * 获取集团列表（当前登录者所在集团及下级集团）
     *
     * @return 集团集合不带分页
     * @author generator
     * @date 2019-04-15 20:12:24
     */
    @ApiOperation(value = "获取集团列表（当前登录者所在集团及下级集团）", httpMethod = "GET", notes = "获取集团列表（当前登录者所在集团及下级集团）")
    @AceAuth("获取集团列表（当前登录者所在集团及下级集团）")
    @RequestMapping(value = "/list/forApp", method = RequestMethod.GET)
    public R listForApp() {
        SysUserDO sysUserDO = sysUserService.getById(securityUtils.getCurrentUserId());
        // 应用级管理员
//        if (sysUserDO != null && Objects.equals(2, sysUserDO.getUserType())) {
        String groupId = sysUserDO.getGroupId();
        List<OrgGroupDO> list = orgGroupService.list(new QueryWrapper<OrgGroupDO>()
                .eq("is_delete", 0).eq("parent_id", groupId).orderByAsc("sort_path"));
        list.add(orgGroupService.getById(groupId));

        List<OrgGroupDO> listT = TreeUtils.makeTree(list, OrgGroupDO.class);
        return R.ok().put("list", listT);
//        }
//        return R.ok().put("list", new ArrayList<>());
    }

    /**
     * 获取集团列表（普通用户所在集团对应的最顶级集团的整个集团树）
     *
     * @return 集团集合不带分页
     * @author generator
     * @date 2019-04-15 20:12:24
     */
    @ApiOperation(value = "获取集团列表（应用级管理员所在集团及下级集团）", httpMethod = "GET", notes = "获取集团列表（应用级管理员所在集团及下级集团）")
    @AceAuth("获取集团列表（应用级管理员所在集团及下级集团）")
    @RequestMapping(value = "/list/forOrdinary/user", method = RequestMethod.GET)
    public R listForOrdinaryUser() {

        SysUserDO sysUserDO = sysUserService.getById(securityUtils.getCurrentUserId());
        if (sysUserDO != null && Objects.equals(3, sysUserDO.getUserType())) {
            String groupId = sysUserDO.getGroupId();
            return R.ok().put("list", orgGroupService.listForOrdinaryUser(groupId));
        }
        return R.ok().put("list", new ArrayList<>());
    }

    /**
     * 保存集团
     *
     * @param group 集团对象
     * @return com.csicit.ace.common.utils.server.R 保存响应结果
     * @author yansiyang
     * @date 2019/4/11 18:26
     */
    @ApiOperation(value = "保存集团", httpMethod = "POST")
    @ApiImplicitParam(name = "group", value = "集团实体", required = true, dataType = "OrgGroupDO")
    @AceAuth("保存集团")
    @RequestMapping(value = "", method = RequestMethod.POST)
    public R save(@RequestBody OrgGroupDO group) {
        return orgGroupService.saveGroupR(group);
    }

    /**
     * 修改集团
     *
     * @param group 集团对象
     * @return com.csicit.ace.common.utils.server.R 更新结果
     * @author shanwj
     * @date 2019/4/11 18:27
     */
    @ApiOperation(value = "修改集团", httpMethod = "PUT")
    @ApiImplicitParam(name = "group", value = "集团实体", required = true, dataType = "OrgGroupDO")
    @AceAuth("修改集团")
    @RequestMapping(value = "", method = RequestMethod.PUT)
    public R update(@RequestBody OrgGroupDO group) {
        if (orgGroupService.updateGroup(group)) {
            return R.ok(InternationUtils.getInternationalMsg("UPDATE_SUCCESS"));
        }
        return R.error(InternationUtils.getInternationalMsg("UPDATE_FAILED"));
    }

    /**
     * 删除集团
     *
     * @param params
     * @return
     * @author yansiyang
     * @date 2019/4/12 9:12
     */
    @ApiOperation(value = "删除集团", httpMethod = "DELETE")
    @ApiImplicitParam(name = "params", value = "参数", required = true, dataType = "Map")
    @AceAuth("删除集团")
    @RequestMapping(value = "", method = RequestMethod.DELETE)
    public R delete(@RequestBody Map<String, Object> params) {
        return orgGroupService.deleteGroup(params);
    }
}

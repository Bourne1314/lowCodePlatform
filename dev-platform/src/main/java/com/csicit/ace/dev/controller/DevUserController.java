package com.csicit.ace.dev.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.csicit.ace.common.annotation.AceAuth;
import com.csicit.ace.common.pojo.domain.dev.DevUserDO;
import com.csicit.ace.common.pojo.domain.dev.DevUserRoleDO;
import com.csicit.ace.common.utils.StringUtils;
import com.csicit.ace.common.utils.internationalization.InternationUtils;
import com.csicit.ace.common.utils.server.R;
import com.csicit.ace.dev.service.DevUserRoleService;
import com.csicit.ace.dev.service.DevUserService;
import com.csicit.ace.dev.util.ShiroUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;


/**
 * 用户管理 接口访问层
 *
 * @author zuog
 * @version V1.0
 * @date 2019-11-07 10:30:31
 */

@RestController
@RequestMapping("/devUsers")
@Api("用户管理")
public class DevUserController extends BaseController {

    @Autowired
    private DevUserService devUserService;
    @Autowired
    private DevUserRoleService devUserRoleService;

    /**
     * @param id
     * @return
     * @author zuog
     * @date 2019-11-07 10:30:31
     */
    @ApiImplicitParam(name = "id", value = "主键id", dataType = "String", required = true)
    @ApiOperation(value = "获取单个", httpMethod = "GET", notes = "获取单个")
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    @RequiresPermissions("sys:user:view")
    public R get(@PathVariable("id") String id) {
        DevUserDO instance = devUserService.getUserInfo(id);
        return R.ok().put("instance", instance);
    }

    /**
     * 获取当前登录用户信息
     *
     * @param
     * @return
     * @author yansiyang
     * @date 2019/4/12 8:39
     */
    @ApiOperation(value = "获取当前登录用户信息", httpMethod = "GET")
    @AceAuth("获取当前登录用户信息")
    @RequestMapping(value = "/action/info", method = RequestMethod.GET)
    public R info() {
        DevUserDO user = ShiroUtils.getUserinfo();
        user.setRoleIds(devUserRoleService.list(new QueryWrapper<DevUserRoleDO>()
                .eq("user_id", user.getId())).stream().map(DevUserRoleDO::getRoleId).collect(Collectors.toList()));
        return R.ok().put("user", user);
    }

    /**
     * 更新密码
     *
     * @param map
     * @return
     * @author yansiyang
     * @date 2019/4/12 8:43
     */
    @ApiOperation(value = "更新密码", httpMethod = "POST")
    @RequestMapping(value = "/action/updatePassword", method = RequestMethod.POST)
    public R updatePassword(@RequestBody Map<String, String> map) {
        String userName = map.get("userName");
        /**
         * https 传输明文密码
         */
        String oldPassword = map.get("oldPassword");
        String newPassword = map.get("newPassword");
        if (StringUtils.isBlank(userName) || StringUtils.isBlank(oldPassword) || StringUtils.isBlank(newPassword)) {
            return R.error(InternationUtils.getInternationalMsg("INPUT_OLD_OR_NEW_PASSWORD"));
        }

        /**
         * 验证旧的密码是否正确
         */
        if (!devUserService.authenticate(userName, oldPassword)) {
            return R.error(InternationUtils.getInternationalMsg("INPUT_CORRECT_OLD_PASSWORD"));
        }
        return devUserService.updatePassword(userName, newPassword, false);
    }

    /**
     * 获取列表
     *
     * @param params 请求参数map对象
     * @return xxx集合
     * @author zuog
     * @date 2019-11-07 10:30:31
     */
    @ApiOperation(value = "获取列表", httpMethod = "GET", notes = "获取列表")
    @ApiImplicitParam(name = "params", value = "参数", required = true, dataType = "Map")
    @RequestMapping(method = RequestMethod.GET)
    @RequiresPermissions("sys:user:list")
    public R list(@RequestParam Map<String, Object> params) {
        int current = Integer.parseInt((String) params.get("current"));
        int size = Integer.parseInt((String) params.get("size"));
        String searchStr = (String) params.get("searching");
        Page<DevUserDO> page = new Page<>(current, size);
        IPage list = devUserService.page(page, new QueryWrapper<DevUserDO>()
                .and(StringUtils.isNotBlank(searchStr)
                        , i -> i.like("user_name", searchStr)
                                .or().like("real_name", searchStr)).orderByAsc("user_name"));
        return R.ok().put("page", list);
    }

    /**
     * 获取列表（不分页）
     *
     * @return xxx集合
     * @author zuog
     * @date 2019-11-07 10:30:31
     */
    @ApiOperation(value = "获取列表", httpMethod = "GET", notes = "获取列表")
    @RequestMapping(value = "/action/listNoPage", method = RequestMethod.GET)
    @RequiresPermissions("sys:user:listNoPage")
    public R listNoPage() {
        List<DevUserDO> list = devUserService.list(new QueryWrapper<DevUserDO>()
                .eq("APPLY_STATUS", 1).orderByAsc("user_name"));
        return R.ok().put("list", list);
    }

    /**
     * 新增
     *
     * @param instance 对象
     * @return 保存响应结果
     * @author zuog
     * @date 2019-11-07 10:30:31
     */
    @ApiOperation(value = "保存", httpMethod = "POST", notes = "保存")
    @ApiImplicitParam(name = "instance", value = "实体", required = true, dataType = "MetaIndexDO")
    @RequestMapping(method = RequestMethod.POST)
    @RequiresPermissions("sys:user:add")
    public R save(@RequestBody DevUserDO instance) {
        Integer count = devUserService.count(new QueryWrapper<DevUserDO>()
                .eq("user_name", instance.getUserName()));
        if (count > 0) {
            return R.error("已存在相同的用户名");
        }
        if (devUserService.saveUser(instance)) {
            return R.ok(InternationUtils.getInternationalMsg("SAVE_SUCCESS"));
        }
        return R.error(InternationUtils.getInternationalMsg("SAVE_FAILED"));
    }

    /**
     * 修改
     *
     * @param instance 对象
     * @return 修改响应结果
     * @author zuog
     * @date 2019-11-07 10:30:31
     */
    @ApiOperation(value = "修改", httpMethod = "PUT", notes = "修改")
    @ApiImplicitParam(name = "instance", value = "实体", required = true, dataType = "MetaIndexDO")
    @RequestMapping(method = RequestMethod.PUT)
    @RequiresPermissions("sys:user:edit")
    public R update(@RequestBody DevUserDO instance) {
        DevUserDO oldUser = devUserService.getById(instance.getId());
        if (!Objects.equals(oldUser.getUserName(), instance.getUserName())) {
            Integer count = devUserService.count(new QueryWrapper<DevUserDO>()
                    .eq("user_name", instance.getUserName()));
            if (count > 0) {
                return R.error("已存在相同的用户名");
            }
        }
        if (devUserService.updateUser(instance)) {
            return R.ok(InternationUtils.getInternationalMsg("UPDATE_SUCCESS"));
        }
        return R.error(InternationUtils.getInternationalMsg("UPDATE_FAILED"));
    }

    /**
     * @param ids ID数组
     * @return 删除响应结果
     * @author zuog
     * @date 2019-11-07 10:30:31
     */
    @ApiOperation(value = "删除", httpMethod = "DELETE", notes = "删除")
    @ApiImplicitParam(name = "ids", value = "ID数组", required = true, allowMultiple = true, dataType = "String")
    @RequestMapping(method = RequestMethod.DELETE)
    @RequiresPermissions("sys:user:del")
    public R delete(@RequestBody String[] ids) {
        if (devUserService.removeByIds(Arrays.asList(ids))) {
            return R.ok(InternationUtils.getInternationalMsg("DELETE_SUCCESS"));
        }
        return R.error(InternationUtils.getInternationalMsg("DELETE_FAILED"));
    }

    /**
     * 解锁用户
     *
     * @param
     * @return
     * @author yansiyang
     * @date 2019/4/12 8:39
     */
    @ApiOperation(value = "解锁用户", httpMethod = "GET")
    @AceAuth("解锁用户")
    @RequestMapping(value = "/action/unlockUser/{id}", method = RequestMethod.GET)
    @RequiresPermissions("sys:user:unLock")
    public R unlockUser(@PathVariable("id") String id) {
        if (devUserService.unlockUser(id)) {
            return R.ok(InternationUtils.getInternationalMsg("OPERATE_SUCCESS"));
        }
        return R.ok(InternationUtils.getInternationalMsg("OPERATE_FAILED"));
    }

}

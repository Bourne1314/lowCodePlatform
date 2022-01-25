package com.csicit.ace.platform.core.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.csicit.ace.common.annotation.AceAuth;
import com.csicit.ace.common.pojo.domain.SysConfigDO;
import com.csicit.ace.common.pojo.domain.SysRoleDO;
import com.csicit.ace.common.pojo.domain.SysUserDO;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 服务于interfaces微服务的对外接口
 *
 * @author yansiyang
 * @version V1.0
 * @date 2019/5/8 8:38
 */
@RequestMapping("/interfaces")
@RestController
public class InterfacesController extends BaseController {

    /**
     * 根据用户id获取角色列表
     *
     * @param userId 用户id
     * @return 角色对象
     * @author shanwj
     * @date 2019/4/11 18:25
     */
    @ApiImplicitParam(name = "id", value = "主键id", dataType = "String", required = true)
    @ApiOperation(value = "根据用户id获取角色列表", httpMethod = "GET", notes = "根据用户id获取角色列表")
    @AceAuth("根据用户id获取角色列表")
    @RequestMapping(value = "/sysRoles/user/{userId}", method = RequestMethod.GET)
    public List<SysRoleDO> getRolesByUserId(@PathVariable("userId") String userId) {
        List<SysRoleDO> instances = sysUserRoleService.getEffectiveRoleData(userId, null);

//        List<SysUserRoleDO> userRoles = sysUserRoleService.list(new QueryWrapper<SysUserRoleDO>()
//                .eq("user_id", userId));
//        List<String> roleIds = userRoles.stream().map(SysUserRoleDO::getRoleId).collect(Collectors.toList());
//        List<SysRoleDO> instances = sysRoleService.list(new QueryWrapper<SysRoleDO>()
//                .and(roleIds == null || roleIds.size() == 0, i -> i.eq("1", "2"))
//                .in("id", roleIds));
        return instances;
    }


    /**
     * 获取单个配置项的值
     *
     * @param id 配置项id
     * @return
     * @author shanwj
     * @date 2019/4/11 18:25
     */
    @ApiImplicitParam(name = "id", value = "主键id", dataType = "String", required = true)
    @ApiOperation(value = "获取单个配置项的值", httpMethod = "GET", notes = "获取单个配置项的值")
    @AceAuth("获取单个配置项的值")
    @RequestMapping(value = "/sysConfigs/value/{id}", method = RequestMethod.GET)
    public String getValue(@PathVariable("id") String id) {
        SysConfigDO instance = sysConfigService.getById(id);
        return instance.getValue();
    }


    /**
     * 查询指定用户信息
     *
     * @param userId 用户主键
     * @return
     * @author yansiyang
     * @date 2019/4/12 8:37
     */
    @ApiOperation(value = "获取单个用户", httpMethod = "GET")
    @ApiImplicitParam(name = "userId", value = "用户ID", required = true,  dataType = "String",paramType = "path")
    @AceAuth("获取单个用户")
    @RequestMapping(value = "/sysUsers/{userId}", method = RequestMethod.GET)
    public SysUserDO infoById(@PathVariable("userId") String userId) {
        SysUserDO user = sysUserService.getById(userId);
        return user;
    }

    /**
     * 通过用户名称查询指定用户信息
     *
     * @param userName 用户名称
     * @return
     * @author yansiyang
     * @date 2019/4/12 8:37
     */
    @ApiOperation(value = "通过用户名称获取单个用户", httpMethod = "GET")
    @ApiImplicitParam(name = "userName", value = "用户名称", required = true, dataType = "String", paramType = "path")
    @AceAuth("通过用户名称获取单个用户")
    @RequestMapping(value = "/sysUsers/userName/{userName}", method = RequestMethod.GET)
    public SysUserDO infoByUserName(@PathVariable("userName") String userName) {
        SysUserDO user = sysUserService.getOne(new QueryWrapper<SysUserDO>().eq("is_delete", 0).eq("user_name",
                userName));
        return user;
    }
}

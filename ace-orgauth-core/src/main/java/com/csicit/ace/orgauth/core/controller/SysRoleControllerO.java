package com.csicit.ace.orgauth.core.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.csicit.ace.common.pojo.domain.SysRoleDO;
import com.csicit.ace.common.utils.StringUtils;
import com.csicit.ace.orgauth.core.service.SysRoleService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author yansiyang
 * @version V1.0
 * @date 2019/8/14 16:29
 */
@RestController
@RequestMapping("/orgauth/sysRoles")
public class SysRoleControllerO {

    @Resource(name = "sysRoleServiceO")
    SysRoleService sysRoleService;

    /**
     * 通过角色编码 判断用户是否有此角色
     * @param roleCode
     * @param appId
     * @return
     * @author FourLeaves
     * @date 2020/12/18 15:38
     */
    @RequestMapping(value = "/action/hasRoleByRoleCode", method = RequestMethod.GET)
    boolean hasRoleByRoleCode(@RequestParam("roleCode") String roleCode, @RequestParam("appId") String appId) {

        return sysRoleService.hasRoleByRoleCode(roleCode,appId);
    }

    /**
     * 通过角色主键 判断用户是否有此角色
     * @param roleId
     * @param appId
     * @return
     * @author FourLeaves
     * @date 2020/12/18 15:38
     */
    @RequestMapping(value = "/action/hasRoleByRoleId", method = RequestMethod.GET)
    boolean hasRoleByRoleId(@RequestParam("roleId") String roleId, @RequestParam("appId") String appId) {
        return sysRoleService.hasRoleByRoleId(roleId,appId);
    }

    /**
     * 通过角色标识获取角色
     *
     * @param code
     * @return
     * @author FourLeaves
     * @date 2020/12/1 15:26
     */
    @RequestMapping(value = "/action/getRoleByCode", method = RequestMethod.GET)
    public SysRoleDO getRoleByCode(@RequestParam("code") String code, @RequestParam("appId") String appId) {
        if (StringUtils.isNotBlank(code) && StringUtils.isNotBlank(appId)) {
            return sysRoleService.getOne(new QueryWrapper<SysRoleDO>()
                    .eq("role_code", code)
                    .eq("app_id", appId));
        }
        return null;
    }

    /**
     * 通过角色ID获取角色
     *
     * @param id
     * @return
     * @author FourLeaves
     * @date 2020/12/1 15:26
     */
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public SysRoleDO getRoleByCode(@PathVariable("id") String id) {
        if (StringUtils.isNotBlank(id)) {
            return sysRoleService.getById(id);
        }
        return null;
    }

    /**
     * 根据用户id获取角色列表
     *
     * @param userId 用户id
     * @return 角色对象
     * @author shanwj
     * @date 2019/4/11 18:25
     */
//    @ApiImplicitParam(name = "id", value = "主键id", dataType = "String", required = true)
//    @ApiOperation(value = "根据用户id获取角色列表", httpMethod = "GET", notes = "根据用户id获取角色列表")
    @RequestMapping(value = "/action/getRolesByUserId/{userId}", method = RequestMethod.GET)
    public List<SysRoleDO> getRolesByUserId(@PathVariable("userId") String userId) {
        return sysRoleService.getRolesByUserId(userId);
    }


    /**
     * 获取指定集团的所有角色
     *
     * @param groupId
     * @return
     * @author yansiyang
     * @date 2019/8/14 15:12
     */
    @RequestMapping(value = "/action/getRolesByGroupId/{groupId}", method = RequestMethod.GET)
    List<SysRoleDO> getRolesByGroupId(@PathVariable("groupId") String groupId) {
        return sysRoleService.getRolesByGroupId(groupId);
    }

}

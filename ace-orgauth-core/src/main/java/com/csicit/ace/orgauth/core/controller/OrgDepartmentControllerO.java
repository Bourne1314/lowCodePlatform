package com.csicit.ace.orgauth.core.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.csicit.ace.common.pojo.domain.OrgDepartmentDO;
import com.csicit.ace.common.pojo.domain.SysUserDO;
import com.csicit.ace.common.utils.StringUtils;
import com.csicit.ace.orgauth.core.service.BdPersonDocService;
import com.csicit.ace.orgauth.core.service.OrgDepartmentService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;
import java.util.Set;
/**
 * @author yansiyang
 * @version V1.0
 * @date 2019/8/14 16:05
 */
@RestController
@RequestMapping("/orgauth/orgDepartments")
public class OrgDepartmentControllerO {

    @Resource(name = "orgDepartmentServiceO")
    OrgDepartmentService orgDepartmentService;

    @Resource(name = "bdPersonDocServiceO")
    BdPersonDocService bdPersonDocService;
    /**
     * 根据用户ID获取所属部门信息
     *
     * @param userId
     * @return 部门对象
     * @author yansiyang
     * @date 16:48 2019/3/28
     */
    @RequestMapping(value = "/action/getDeptsByUserId/{userId}", method = RequestMethod.GET)
    List<OrgDepartmentDO> getDeptsByUserId(@PathVariable("userId") String userId) {
        return orgDepartmentService.getDeptsByUserId(userId);
    }

    /**
     * 根据ID获取指定部门信息
     *
     * @param id
     * @return 部门对象
     * @author yansiyang
     * @date 16:48 2019/3/28
     */
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    OrgDepartmentDO getDeptById(@PathVariable("id") String id) {
        if (StringUtils.isNotBlank(id)) {
            return orgDepartmentService.getById(id);
        }
        return new OrgDepartmentDO();
    }

    /**
     * 根据编码获取指定部门信息
     *
     * @return 部门对象
     * @author yansiyang
     * @date 16:48 2019/3/28
     */
    @RequestMapping(value = "/action/getDepartmentByCode/{code}", method = RequestMethod.GET)
    OrgDepartmentDO getDeptByCode(@PathVariable("code") String code) {
        if (StringUtils.isNotBlank(code)) {
            return orgDepartmentService.getOne(new QueryWrapper<OrgDepartmentDO>().eq("code", code));
        }
        return null;
    }


    /**
     * 获取指定用户主要部门信息
     *
     * @return 部门对象
     * @author yansiyang
     * @date 16:48 2019/3/28
     */
    @RequestMapping(value = "/action/getMainDeptByUserId/{userId}", method = RequestMethod.GET)
    OrgDepartmentDO getMainDeptByUserId(@PathVariable("userId") String userId) {
        return orgDepartmentService.getMainDeptByUserId(userId);
    }


    /**
     * @Author zhangzhaojun
     * @Description //TODO
     * @Date 8:10 2021/9/10
     * @Param List<String> userIds
     * @return List<OrgDepartmentDO>
     **/
    @RequestMapping(value = "/action/getOrgDepartmentListByUserIds", method = RequestMethod.POST)
    List<OrgDepartmentDO> getOrgDepartmentListByUserIds(@RequestBody Set<String> userIds) {
        return orgDepartmentService.getOrgDepartmentListByUserIds(userIds);
    }
    /**
     * @Author zhangzhaojun
     * @Description //TODO
     * @Date 8:10 2021/9/10
     * @Param List<String> userIds
     * @return List<OrgDepartmentDO>
     **/
    @RequestMapping(value = "/action/listdeliverDepartment", method = RequestMethod.POST)
    List<OrgDepartmentDO> listdeliverDepartment(@RequestBody Set<String> userIds) {
        return orgDepartmentService.listdeliverDepartment(userIds);
    }
    /**
     * @Author zhangzhaojun
     * @Description //TODO
     * @Date 8:10 2021/9/10
     * @Param List<String> userIds
     * @return List<OrgDepartmentDO>
     **/
    @RequestMapping(value = "/action/getUsersByDepartmentId", method = RequestMethod.GET)
    List<SysUserDO> getUsersByDepartmentId(@RequestParam("departmentId") String departmentId ) {
        System.out.println("111");
        return orgDepartmentService.getUsersByDepartmentId(departmentId);
    }
    /**
     * @Author zhangzhaojun
     * @Description //TODO
     * @Date 8:10 2021/9/10
     * @Param List<String> userIds
     * @return List<OrgDepartmentDO>
     **/
    @RequestMapping(value = "/action/getUsersByUserIds", method = RequestMethod.GET)
    List<SysUserDO> getUsersByUserIds(@RequestParam("userIds") List<String> userIds ) {
        return orgDepartmentService.getUsersByUserIds(userIds);
    }


    /**
     * 根据业务单元ID获取对应部门信息
     *
     * @param organizationId 业务单元ID
     * @return 部门对象
     * @author yansiyang
     * @date 16:49 2019/3/28
     */
    @RequestMapping(value = "/action/getDeptsByOrganizationId/{organizationId}", method = RequestMethod.GET)
    List<OrgDepartmentDO> getDeptsByOrganizationId(@PathVariable("organizationId") String organizationId) {
        return orgDepartmentService.getDeptsByOrganizationId(organizationId);
    }

    /**
     * 获取指定集团的所有部门
     *
     * @param groupId
     * @return
     * @author yansiyang
     * @date 2019/8/14 15:12
     */
    @RequestMapping(value = "/action/getDepartmentByGroupId/{groupId}", method = RequestMethod.GET)
    List<OrgDepartmentDO> getDepartmentByGroupId(@PathVariable("groupId") String groupId) {
        return orgDepartmentService.getDepartmentByGroupId(groupId);
    }
}

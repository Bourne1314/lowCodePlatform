package com.csicit.ace.orgauth.core.service;

import com.csicit.ace.common.pojo.domain.OrgDepartmentDO;
import com.csicit.ace.common.pojo.domain.SysUserDO;
import com.csicit.ace.dbplus.service.IBaseService;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;

/**
 * 组织-部门 实例对象访问接口
 *
 * @author yansiyang
 * @version V1.0
 * @date 2019-04-15 15:20:38
 */
@Transactional
public interface OrgDepartmentService extends IBaseService<OrgDepartmentDO> {
    /**
     * 根据用户ID获取所属部门信息
     *
     * @param userId
     * @return 部门对象
     * @author yansiyang
     * @date 16:48 2019/3/28
     */
    List<OrgDepartmentDO> getDeptsByUserId( String userId);

    /**
     * 获取指定用户主要部门信息
     *
     * @return 部门对象
     * @author yansiyang
     * @date 16:48 2019/3/28
     */
    OrgDepartmentDO getMainDeptByUserId(String userId);


    /**
     * @Author zhangzhaojun
     * @Description //根据用户id获取部门信息
     * @Date 9:01 2021/9/13
     * @Param Set<String> userIds</>
     * @return 部门列表
     **/
    List<OrgDepartmentDO> getOrgDepartmentListByUserIds(Set<String> userIds);
    /**
     * @Author zhangzhaojun
     * @Description //根据用户id获取部门信息
     * @Date 9:01 2021/9/15
     * @Param Set<String> userIds</>
     * @return 部门列表
     **/
    List<OrgDepartmentDO> listdeliverDepartment(Set<String> userIds);
    /**
     * @Author zhangzhaojun
     * @Description //根据用户id获取部门信息
     * @Date 9:01 2021/9/13
     * @Param Set<String> userIds</>
     * @return 部门列表
     **/
    List<SysUserDO> getUsersByDepartmentId(String departmentId);
    /**
     * @Author zhangzhaojun
     * @Description //根据用户id获取部门信息
     * @Date 9:01 2021/9/13
     * @Param Set<String> userIds</>
     * @return 部门列表
     **/
    List<SysUserDO> getUsersByUserIds(List<String> departmentId);


    /**
     * 根据业务单元ID获取对应部门信息
     *
     * @param organizationId 业务单元ID
     * @return 部门对象
     * @author yansiyang
     * @date 16:49 2019/3/28
     */
    List<OrgDepartmentDO> getDeptsByOrganizationId( String organizationId);

    /**
     * 获取指定集团的所有部门
     *
     * @param groupId
     * @return
     * @author yansiyang
     * @date 2019/8/14 15:12
     */
    List<OrgDepartmentDO> getDepartmentByGroupId( String groupId);
}

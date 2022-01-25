package com.csicit.ace.cloudImpl.service;

import com.alibaba.fastjson.JSONObject;
import com.csicit.ace.common.pojo.domain.BdPersonDocDO;
import com.csicit.ace.common.pojo.domain.SysUserDO;
import com.csicit.ace.common.pojo.vo.SysOnlineUserVO;
import com.csicit.ace.common.utils.StringUtils;
import com.csicit.ace.common.utils.server.R;
import com.csicit.ace.interfaces.service.IUser;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * @author yansiyang
 * @version V1.0
 * @date 2019/8/14 8:30
 */
@Service("user")
public class UserImpl extends BaseImpl implements IUser {

    @Override
    public Boolean bingUserIp(String userId, String ip) {
        if (StringUtils.isNotBlank(userId) && StringUtils.isNotBlank(ip)) {
            return gatewayService.bingUserIp(userId, ip);
        }
        return false;
    }

    @Override
    public List<SysUserDO> getBindUsers(String userId) {
        if (StringUtils.isNotBlank(userId)) {
            return gatewayService.getBindUsers(userId);
        }
        return new ArrayList<>();
    }

    @Override
    public List<String> getBindUserIds(String userId) {
        if (StringUtils.isNotBlank(userId)) {
            return gatewayService.getBindUserIds(userId);
        }
        return new ArrayList<>();
    }

    @Override
    public List<SysUserDO> getUserListByAuthCode(String code) {
        if (StringUtils.isNotBlank(code)) {
            return gatewayService.getUserListByAuthCode(code, appName);
        }
        return new ArrayList<>();
    }

    @Override
    public boolean AddUsers(List<SysUserDO> users) {
        if (CollectionUtils.isEmpty(users)) {
            return true;
        }
        return gatewayService.addUsers(users);
    }

    @Override
    public boolean AddPersonDocs(List<BdPersonDocDO> personDocDOS) {
        if (CollectionUtils.isEmpty(personDocDOS)) {
            return true;
        }
        return gatewayService.addPersonDocs(personDocDOS);
    }

    @Override
    public List<SysUserDO> getUserListByRoleCode(String code) {
        if (StringUtils.isNotBlank(code)) {
            return gatewayService.getUserListByRoleCode(code, appName);
        }
        return new ArrayList<>();
    }

    @Override
    public SysUserDO getUserByStaffNo(String staffNo) {
        if (StringUtils.isNotBlank(staffNo)) {
            return gatewayService.getUserByStaffNo(staffNo);
        }
        return null;
    }

    @Override
    public List<SysUserDO> getUserListByStaffNos(List<String> staffNos) {
        if (CollectionUtils.isNotEmpty(staffNos)) {
            return gatewayService.getUserListByStaffNos(staffNos);
        }
        return new ArrayList<>();
    }

    @Override
    public Integer userRepeat(String userName) {
        return gatewayService.userRepeat(userName);
    }

    @Override
    public R savePersonAndUser(BdPersonDocDO personDoc) {
        return gatewayService.savePersonAndUser(personDoc);
    }

    @Override
    public R getBdPersonDoc(String userId) {
        return gatewayService.getBdPersonDoc(userId);
    }

    @Override
    public List<String> getUserIdsByAuthId(String authId) {
        if (StringUtils.isNotBlank(authId)) {
            return gatewayService.getUserIdsByAuthId(appName, authId);
        }
        return new ArrayList<>();
    }

    @Override
    public List<SysUserDO> getUsersByAuthId(String authId) {
        if (StringUtils.isNotBlank(authId)) {
            return gatewayService.getUsersByAuthId(appName, authId);
        }
        return new ArrayList<>();
    }

    @Override
    public R listUsersBySecretLevelAndRoleOrOrg(Map<String, String> params) {
        return gatewayService.listUsersBySecretLevelAndRoleOrOrg(params);
    }

    @Override
    public List<SysOnlineUserVO> getOnlineUsers() {
        List<SysOnlineUserVO> sysOnlineUserVOS = new ArrayList<>();
        sysOnlineUserVOS.addAll(securityUtils.getOnlineUsers());
        return sysOnlineUserVOS;
    }

    @Override
    public List<SysUserDO> getAllUsersByDepAndSon(String depId) {
        if (StringUtils.isNotBlank(depId)) {
            return gatewayService.getAllUsersByDepAndSon(depId);
        }
        return new ArrayList<>();
    }

    @Override
    public List<SysUserDO> getUsersByMatchNameOrID(String str) {
        if (StringUtils.isNotBlank(str)) {
            return gatewayService.getUsersByMatchNameOrID(str);
        }
        return new ArrayList<>();
    }

    @Override
    public List<SysUserDO> getAllUsersByOrgAndSon(String organizationId) {
        if (StringUtils.isNotBlank(organizationId)) {
            return gatewayService.getAllUsersByOrgAndSon(organizationId);
        }
        return new ArrayList<>();
    }

    @Override
    public List<SysUserDO> getUsersByRoleId(String roleId) {
        if (StringUtils.isNotBlank(roleId)) {
            return gatewayService.getUsersByRoleId(roleId);
        }
        return new ArrayList<>();
    }

    @Override
    public List<SysUserDO> getUsersByDepId(String depId) {
        if (StringUtils.isNotBlank(depId)) {
            return gatewayService.getUsersDepId(depId);
        }
        return new ArrayList<>();
    }

    @Override
    public List<SysUserDO> getUsersOnlyByDepId(String depId) {
        if (StringUtils.isNotBlank(depId)) {
            return gatewayService.getUsersOnlyByDepId(depId);
        }
        return new ArrayList<>();
    }

    @Override
    public List<SysUserDO> getUsersBySecretLevel(Integer level) {
        return gatewayService.getUsersBySecretLevel(level);
    }


    @Override
    public List<SysUserDO> getUsersBySecretLevel(Integer level, boolean le) {
        return gatewayService.getUsersBySecretLevel(level, le ? 1 : 0);
    }

    @Override
    public List<SysUserDO> getUsersByIds(List<String> ids) {
        Map<String, Object> map = new HashMap<>(16);
        map.put("code", "user");
        HashSet hashSet = new HashSet(ids);
        ids.clear();
        ids.addAll(hashSet);
        map.put("ids", JSONObject.toJSONString(ids));
        return gatewayService.getSomeUsers(map);
    }

    @Override
    public SysUserDO getUserById(String id) {
        if (StringUtils.isEmpty(id)) {
            return null;
        }
        return gatewayService.getUserById(id);
    }

    @Override
    public List<SysUserDO> getUsersByGroupId(String groupId) {
        if (StringUtils.isEmpty(groupId)) {
            return new ArrayList<>();
        }
        Map<String, Object> map = new HashMap<>(16);
        map.put("code", "groupComp");
        map.put("ids", JSONObject.toJSONString(new String[]{groupId}));
        return gatewayService.getSomeUsers(map);
    }

    @Override
    public List<SysUserDO> getUsersByMatch(String str) {
        if (StringUtils.isNotBlank(str)) {
            return gatewayService.getUsersByMatch(str);
        }
        return new ArrayList<>();
    }

    @Override
    public List<SysUserDO> getUseAppUsers() {
        return gatewayService.getUseAppUsers(securityUtils.getCurrentGroupId(), securityUtils.getCurrentUser()
                .getOrganizationId(), appName);
    }
}

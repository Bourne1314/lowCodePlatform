package com.csicit.ace.platform.core.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.csicit.ace.common.exception.RException;
import com.csicit.ace.common.pojo.domain.SysRoleDO;
import com.csicit.ace.common.pojo.domain.SysRoleMutexDO;
import com.csicit.ace.common.pojo.domain.SysUserRoleDO;
import com.csicit.ace.common.pojo.domain.SysWaitGrantUserDO;
import com.csicit.ace.common.utils.internationalization.InternationUtils;
import com.csicit.ace.common.enums.AuditLogType;
import com.csicit.ace.common.enums.AuditLogTypeDO;
import com.csicit.ace.data.persistent.mapper.SysRoleMutexMapper;
import com.csicit.ace.data.persistent.service.impl.BaseServiceImpl;
import com.csicit.ace.platform.core.service.SysRoleMutexService;
import com.csicit.ace.platform.core.service.SysRoleService;
import com.csicit.ace.platform.core.service.SysUserRoleService;
import com.csicit.ace.platform.core.service.SysWaitGrantUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 角色互斥关系管理 实例对象访问接口实现
 *
 * @author zuogang
 * @version V1.0
 * @date 2019/4/12 17:22
 */
@Service("sysRoleMutexService")
public class SysRoleMutexServiceImpl extends BaseServiceImpl<SysRoleMutexMapper, SysRoleMutexDO> implements
        SysRoleMutexService {

    /**
     * 角色接口对象
     */
    @Autowired
    SysRoleService sysRoleService;
    /**
     * 用户角色接口对象
     */
    @Autowired
    SysUserRoleService sysUserRoleService;
    /**
     * 角色互斥接口对象
     */
    @Autowired
    SysRoleMutexService sysRoleMutexService;
    /**
     * 待激活的用户角色授予的用户表接口
     */
    @Autowired
    SysWaitGrantUserService sysWaitGrantUserService;

    /**
     * 保存角色互斥数据
     *
     * @param id
     * @param mids
     * @return
     * @author zuogang
     * @date 2019/4/22 15:22
     */
    @Override
    public boolean saveMutexRoles(String id, List<String> mids) {
        SysRoleDO sysRoleDO = sysRoleService.getById(id);
        sysRoleMutexService.remove(new QueryWrapper<SysRoleMutexDO>().eq("role_id", id));

        if (mids == null || mids.size() == 0) {
            return sysAuditLogService.saveLog(new AuditLogTypeDO(AuditLogType.danger, "删除"), "清空角色的互斥角色", "清空角色【" +
                            sysRoleDO.getName() +
                            "】的互斥角色",
                    securityUtils
                            .getCurrentGroupId(), sysRoleDO.getAppId());
        }

        // 判断当前用户是否有未激活的用户关系
        List<String> userIds = sysUserRoleService.list(new QueryWrapper<SysUserRoleDO>()
                .eq("role_id", id)).stream().map(SysUserRoleDO::getUserId).collect(Collectors.toList());
        if (userIds != null && userIds.size() > 0) {
            List<SysWaitGrantUserDO> sysWaitGrantUserDOS = sysWaitGrantUserService.list(new
                    QueryWrapper<SysWaitGrantUserDO>().eq("app_id", sysRoleDO.getAppId()));
            for (SysWaitGrantUserDO sysWaitGrantUserDO : sysWaitGrantUserDOS) {
                if (userIds.contains(sysWaitGrantUserDO.getUserId())) {
                    throw new RException("当前角色尚有未激活的用户关系:" + sysRoleService.getById(id).getName());
                }
            }
        }

        // 判断当前角色有拥有的有效用户关系是否与互斥角色拥有的有效用户关系同时存在
        List<String> currentUserIds = sysUserRoleService.getEffectiveUserData(id);

        // 得到当前角色所对应的有效用户
        StringBuffer mixNames = new StringBuffer();
        mids.forEach(mid -> {

            // 判断互斥角色是否有未激活的用户关系
            List<String> mixUserIds = sysUserRoleService.list(new QueryWrapper<SysUserRoleDO>()
                    .eq("role_id", mid)).stream().map(SysUserRoleDO::getUserId).collect(Collectors.toList());
            if (mixUserIds != null && mixUserIds.size() > 0) {
                List<String> waitUserIds = sysWaitGrantUserService.list(new QueryWrapper<SysWaitGrantUserDO>()
                        .eq("app_id", sysRoleDO.getAppId())).stream().map(SysWaitGrantUserDO::getUserId).collect
                        (Collectors.toList());
                for (String userId : waitUserIds) {
                    if (mixUserIds.contains(userId)) {
                        throw new RException("所选互斥角色尚有未激活的用户关系:" + sysRoleService.getById(mid).getName());
                    }
                }
            }

            List<String> mutexUserIds = sysUserRoleService.getEffectiveUserData(mid);

            mutexUserIds.stream().forEach(userId -> {
                if (currentUserIds.contains(userId)) {
                    throw new RException("当前角色与所选互斥角色为:【" + sysRoleService.getById(mid).getName() + "】存在相同用户关系");
                }
            });

            SysRoleMutexDO sysRoleMutexDO = new SysRoleMutexDO();
            sysRoleMutexDO.setRoleId(id);
            sysRoleMutexDO.setRoleMutexId(mid);
            sysRoleMutexDO.setCreateUser(securityUtils.getCurrentUserId());
            sysRoleMutexDO.setCreateTime(LocalDateTime.now());
            if (!save(sysRoleMutexDO)) {
                throw new RException(InternationUtils.getInternationalMsg("SAVE_FAILED"));
            }
            mixNames.append(sysRoleService.getById(mid).getName());
            mixNames.append(",");
        });
        return sysAuditLogService.saveLog(new AuditLogTypeDO(AuditLogType.success, "新增"), "添加角色的互斥角色","添加角色【"+sysRoleDO.getName()+"】的互斥角色："+ mixNames
                .substring(0, mixNames
                        .length() - 1), securityUtils.getCurrentGroupId(), sysRoleDO.getAppId());
    }
}

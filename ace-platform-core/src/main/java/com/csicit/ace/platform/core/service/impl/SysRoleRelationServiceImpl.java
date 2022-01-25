package com.csicit.ace.platform.core.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.csicit.ace.common.exception.RException;
import com.csicit.ace.common.pojo.AbstractBaseDomain;
import com.csicit.ace.common.pojo.domain.SysRoleDO;
import com.csicit.ace.common.pojo.domain.SysRoleRelationDO;
import com.csicit.ace.common.utils.internationalization.InternationUtils;
import com.csicit.ace.data.persistent.mapper.SysRoleRelationMapper;
import com.csicit.ace.common.enums.AuditLogType;
import com.csicit.ace.common.enums.AuditLogTypeDO;
import com.csicit.ace.data.persistent.service.impl.BaseServiceImpl;
import com.csicit.ace.platform.core.service.SysAuthMixService;
import com.csicit.ace.platform.core.service.SysRoleRelationService;
import com.csicit.ace.platform.core.service.SysRoleService;
import com.csicit.ace.platform.core.service.SysUserRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 角色关系管理 实例对象访问接口实现
 *
 * @author zuogang
 * @version V1.0
 * @date 2019/4/12 17:22
 */
@Service("sysRoleRelationService")
public class SysRoleRelationServiceImpl extends BaseServiceImpl<SysRoleRelationMapper, SysRoleRelationDO> implements
        SysRoleRelationService {

    /**
     * 角色关系接口对象
     */
    @Autowired
    SysRoleRelationService sysRoleRelationService;

    /**
     * 角色接口对象
     */
    @Autowired
    SysRoleService sysRoleService;

    @Autowired
    SysUserRoleService sysUserRoleService;

    @Autowired
    SysAuthMixService sysAuthMixService;

    /**
     * 添加下级角色
     *
     * @param id   当前角色id
     * @param cids 下级角色id集合
     * @return boolean
     * @author shanwj
     * @date 2019/4/18 18:52
     */
    @Override
    public boolean saveChildRoles(String id, List<String> cids) {
        SysRoleDO sysRoleDO = sysRoleService.getById(id);

        // 计算旧的下级角色列表
        List<String> oldCids = sysRoleRelationService.list(new QueryWrapper<SysRoleRelationDO>()
                .eq("pid", id)).stream().map(AbstractBaseDomain::getId).collect(Collectors.toList());

        // 计算旧的下级角色对应的有效用户
//        List<String> oldUserIds = sysUserRoleService.getEffectiveUserDatas(oldCids);

        sysRoleRelationService.remove(new QueryWrapper<SysRoleRelationDO>().eq("pid", id));
        if (cids == null || cids.size() == 0) {
            return sysAuditLogService.saveLog(new AuditLogTypeDO(AuditLogType.danger, "删除"), "清空角色", "清空角色【" +
                    sysRoleDO.getName() + "】的下级角色", securityUtils
                    .getCurrentGroupId(), sysRoleDO.getAppId());
        }

        cids.forEach(cid -> {
            saveRoleRelation(id, cid);
        });

        // 新加的子角色 + 被删除的子角色
        List<String> roleIds = new ArrayList<>(16);
        oldCids.forEach(oldCid -> {
            if (!cids.contains(oldCid)) {
                // 被删除的子角色
                roleIds.add(oldCid);
            }
        });
        cids.forEach(cid -> {
            if (!oldCids.contains(cid)) {
                // 新加的子角色
                roleIds.add(cid);
            }
        });

        List<String> userIds = sysUserRoleService.getEffectiveUserDatas(roleIds);
        // 重新计算有效权限
        userIds.stream().distinct().forEach(userId -> {
            sysAuthMixService.saveAuthMixForApp(userId, sysRoleDO.getAppId());
        });

        List<String> cNames = sysRoleService.list(new QueryWrapper<SysRoleDO>()
                .in("id", cids)).stream().map(SysRoleDO::getName).collect(Collectors.toList());
        StringBuffer stringBuffer = new StringBuffer();
        cNames.stream().forEach(name -> {
            stringBuffer.append(name);
            stringBuffer.append(",");
        });
        return sysAuditLogService.saveLog(new AuditLogTypeDO(AuditLogType.warning, "更新"), "添加角色的下级角色", "添加角色【" +
                sysRoleDO.getName() + "】的下级角色：" +
                stringBuffer.substring(0,
                        stringBuffer
                                .length() - 1), securityUtils.getCurrentGroupId(), sysRoleDO.getAppId());
    }

    /**
     * 添加上级角色
     *
     * @param id   当前角色id
     * @param pids 上级角色id集合
     * @return boolean
     * @author shanwj
     * @date 2019/4/18 18:52
     */
    @Override
    public boolean saveParentRoles(String id, List<String> pids) {
        sysRoleRelationService.remove(new QueryWrapper<SysRoleRelationDO>().eq("cid", id));
        if (pids == null || pids.size() == 0) {
            return true;
        }
        List<String> userIds = sysUserRoleService.getEffectiveUserData(id);
        // 重新计算有效权限
        userIds.stream().distinct().forEach(userId -> {
            sysAuthMixService.saveAuthMixForApp(userId, sysRoleService.getById(id).getAppId());
        });
        pids.forEach(pid -> {
            saveRoleRelation(pid, id);
        });
        return true;
    }

    /**
     * 设置
     *
     * @param pid 父节点id
     * @param cid 子节点id
     * @return void
     * @author shanwj
     * @date 2019/4/18 18:30
     */
    private void saveRoleRelation(String pid, String cid) {
        SysRoleRelationDO sysRoleRelationDO = new SysRoleRelationDO();
        sysRoleRelationDO.setPid(pid);
        sysRoleRelationDO.setCid(cid);
        sysRoleRelationDO.setCreateTime(LocalDateTime.now());
        sysRoleRelationDO.setCreateUser(securityUtils.getCurrentUserId());
        if (!save(sysRoleRelationDO)) {
            throw new RException(InternationUtils.getInternationalMsg("SAVE_FAILED"));
        }
    }

    /**
     * 获取当前角色的所有上级父节点至最上层
     *
     * @param id 当前角色id
     * @return 所有上级角色集合
     * @author shanwj
     * @date 2019/4/19 14:21
     */
    @Override
    public List<String> getAllSuperRoleIds(String id) {
        List<String> ids = new ArrayList<>(1);
        ids.add(id);
        return getAllSuperRoleIds(ids);
    }

    /**
     * 获取当前角色集合的所有上级父节点至最上层
     *
     * @param ids 当前角色集合ids
     * @return 所有上级角色集合
     * @author shanwj
     * @date 2019/4/19 14:21
     */
    @Override
    public List<String> getAllSuperRoleIds(List<String> ids) {
        if (ids.size() == 0) {
            return new ArrayList<>(16);
        }
        List<String> parentIds = new ArrayList<>(16);
        List<SysRoleRelationDO> sysRoleRelationDOS = list(null);
        setParentIds(sysRoleRelationDOS, parentIds, ids);
        return parentIds;
    }

    /**
     * 填充当前层级角色所有上级角色
     *
     * @param parentIds 上级角色id集合
     * @param curIds    当前层级角色id集合
     * @author shanwj
     * @date 2019/4/19 15:40
     */
    private void setParentIds(List<SysRoleRelationDO> sysRoleRelationDOS, List<String> parentIds, List<String> curIds) {
        curIds.stream().forEach(curId -> {
            List<String> pids = sysRoleRelationDOS.stream()
                    .filter(sysRoleRelationDO -> Objects.equals(sysRoleRelationDO.getCid(), curId))
                    .map(SysRoleRelationDO::getPid).collect(Collectors.toList());

//                    list(new QueryWrapper<SysRoleRelationDO>()
//                    .eq("cid", curId))
//                    .parallelStream()
//                    .map(sysRoleRelationDO -> sysRoleRelationDO.getPid())
//                    .collect(Collectors.toList());
            if (pids.size() > 0) {
                parentIds.addAll(pids);
                setParentIds(sysRoleRelationDOS, parentIds, pids);
            }
        });
    }
}

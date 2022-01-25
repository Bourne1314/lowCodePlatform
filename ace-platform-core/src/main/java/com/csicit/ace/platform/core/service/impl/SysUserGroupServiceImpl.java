package com.csicit.ace.platform.core.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.csicit.ace.common.exception.RException;
import com.csicit.ace.common.pojo.domain.SysUserDO;
import com.csicit.ace.common.pojo.domain.SysUserGroupDO;
import com.csicit.ace.common.pojo.domain.SysUserGroupUserDO;
import com.csicit.ace.common.utils.SortPathUtils;
import com.csicit.ace.common.utils.internationalization.InternationUtils;
import com.csicit.ace.common.utils.server.R;
import com.csicit.ace.data.persistent.mapper.SysUserGroupMapper;
import com.csicit.ace.data.persistent.utils.AceSqlUtils;
import com.csicit.ace.common.enums.AuditLogType;
import com.csicit.ace.common.enums.AuditLogTypeDO;
import com.csicit.ace.data.persistent.service.impl.BaseServiceImpl;
import com.csicit.ace.platform.core.service.SysUserGroupService;
import com.csicit.ace.platform.core.service.SysUserGroupUserService;
import com.csicit.ace.platform.core.service.SysUserService;
import com.csicit.ace.platform.core.utils.JDBCUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;


/**
 * 系统管理-用户组 实例对象访问接口实现
 *
 * @author generator
 * @version V1.0
 * @date 2019-04-15 20:14:36
 */
@Service("sysUserGroupService")
public class SysUserGroupServiceImpl extends BaseServiceImpl<SysUserGroupMapper, SysUserGroupDO> implements
        SysUserGroupService {

    @Autowired
    AceSqlUtils aceSqlUtils;

    @Autowired
    SysUserGroupUserService sysUserGroupUserService;

    @Autowired
    SysUserService sysUserService;

    /**
     * 更新用户组
     *
     * @param sysUserGroupDO
     * @return boolean
     * @author zuogang
     * @date 2019/4/22 15:25
     */
    @Override
    public boolean update(SysUserGroupDO sysUserGroupDO) {
        /** 验证相关数据  并更新子节点排序路径
         * *******************************************************************************
         */
        String parentId = sysUserGroupDO.getParentId();
        String sortPath = "";
        String appId = sysUserGroupDO.getAppId();
        if (StringUtils.isBlank(appId)) {
            throw new RException(InternationUtils.getInternationalMsg("EMPTY_ARG"));
        }
        String result = aceSqlUtils.verifyParentIdWithUnique("sys_user_group", parentId, sysUserGroupDO.getId(),
                sysUserGroupDO
                        .getSortIndex(), "app_id", appId);
        if (Objects.equals("rootParent", result)) {
            // 父节点为根节点
            sortPath = SortPathUtils.getSortPath("", sysUserGroupDO.getSortIndex());
            sysUserGroupDO.setSortPath(sortPath);
        } else if (Objects.equals("nullSortPath", result)) {
            // 父节点发生改变 或 sortIndex 发生变化
            SysUserGroupDO parentGroup = this.getById(parentId);
            sortPath = SortPathUtils.getSortPath(parentGroup
                    .getSortPath(), sysUserGroupDO.getSortIndex());
            sysUserGroupDO.setSortPath(sortPath);
        } else if (Objects.equals("nullParentId", result)) {
            //父节点ID为空
            return false;
        }
        sysUserGroupDO.setUpdateTime(LocalDateTime.now());
        SysUserGroupDO oldGroup = this.getById(sysUserGroupDO.getId());
        if (!this.updateById(sysUserGroupDO)) {
            return false;
        }
        // 当此节点的父节点发生变化时 才会修改其子节点的排序路径
        if (!Objects.equals("noChange", result)) {
            aceSqlUtils.updateSonSortPathWithUnique("sys_user_group", sortPath, oldGroup.getSortPath().length(), oldGroup
                    .getSortPath(), "app_id", appId);
        }
        if (!sysAuditLogService.saveLog(new AuditLogTypeDO(AuditLogType.warning, "更新"), "更新用户组", "更新用户组："+sysUserGroupDO.getName())) {
            throw new RException(InternationUtils.getInternationalMsg("UPDATE_FAILED"));
        }
        return true;
    }

    /**
     * 保存用户组
     *
     * @param group
     * @return boolean
     * @author zuogang
     * @date 2019/4/22 15:25
     */
    @Override
    public boolean saveUserGroup(SysUserGroupDO group) {
        String parentId = group.getParentId();
        String rootId = "0";
        String sortPath;
        if (StringUtils.isBlank(parentId)) {
            return false;
        } else if (Objects.equals(rootId, parentId)) {
            sortPath = SortPathUtils.getSortPath("", group.getSortIndex());
        } else {
            SysUserGroupDO parentGroup = this.getById(parentId);
            sortPath = SortPathUtils.getSortPath(parentGroup
                    .getSortPath(), group.getSortIndex());
        }
        aceSqlUtils.validateTreeTable("sys_user_group", parentId, group.getSortIndex(), sortPath);
        group.setParentId(parentId);
        group.setSortPath(sortPath);
        group.setCreateTime(LocalDateTime.now());
        group.setCreateUser(securityUtils.getCurrentUserId());
        if (this.save(group)) {
            if (!sysAuditLogService.saveLog(new AuditLogTypeDO(AuditLogType.success, "新增"), "保存用户组","保存用户组："+ group.getName())) {
                throw new RException(InternationUtils.getInternationalMsg("SAVE_FAILED"));
            }
            return true;
        }
        return false;
    }

    @Override
    public R delete(Map<String, Object> params) {
        // 是否删除子节点
        boolean deleteSons = (boolean) params.get("deleteSons");
        List<String> ids = (ArrayList) params.get("ids");
        List<SysUserGroupDO> list = list(new QueryWrapper<SysUserGroupDO>()
                .and(ids == null || ids.size() == 0, i -> i.eq("1", "2"))
                .in("id", ids)
                .select("id", "name", "sort_path", "group_id", "ORGANIZATION_ID", "app_id"));
        boolean result = false;
        if (!deleteSons) {
            int count = count(new QueryWrapper<SysUserGroupDO>()
                    .and(ids == null || ids.size() == 0, i -> i.eq("1", "2"))
                    .in("parent_id", ids));
            if (count > 0) {
                return R.error(InternationUtils.getInternationalMsg("DELETE_ERROR_FOR_EXIST_SON"));
            } else {
                removeByIds(ids);
            }
        } else {
            // 通过sort_path删除子节点
            for (int i = 0; i < list.size(); i++) {
                SysUserGroupDO userGroup = list.get(i);
                String sortPath = userGroup.getSortPath();
                if (StringUtils.isNotBlank(sortPath)) {
                    remove(new QueryWrapper<SysUserGroupDO>().eq("group_id", userGroup.getGroupId())
                            .eq("ORGANIZATION_ID", userGroup.getOrganizationId())
                            .eq("app_id", userGroup.getAppId()).likeRight("sort_path", sortPath));
                }
            }

        }
        // 判断删除是否成功
        int count = count(new QueryWrapper<SysUserGroupDO>()
                .and(ids == null || ids.size() == 0, i -> i.eq("1", "2")).in("id", ids));
        if (count == 0) {
            result = true;
        }
        if (result) {
            if (!sysAuditLogService.saveLog(new AuditLogTypeDO(AuditLogType.danger, "删除"), "删除用户组","删除用户组："+ list.parallelStream
                    ().map
                    (SysUserGroupDO::getName)
                    .collect(Collectors.toList()))) {
                throw new RException(InternationUtils.getInternationalMsg("DELETE_FAILED"));
            }
            return R.ok(InternationUtils.getInternationalMsg("DELETE_SUCCESS"));
        }

        throw new RException(InternationUtils.getInternationalMsg("DELETE_FAILED"));
    }

    @Override
    public R addUser(Map<String, Object> map) {
        String groupId = (String) map.get("groupId");
        List<String> userIds = (ArrayList) map.get("userIds");
        if (StringUtils.isBlank(groupId) || userIds == null || userIds.size() == 0) {
            return R.error(InternationUtils.getInternationalMsg("ADD_FAILED"));
        }

        List<SysUserGroupUserDO> list = new ArrayList<>();
        userIds.forEach(id -> {
            SysUserGroupUserDO sysUserGroupUserDO = new SysUserGroupUserDO();
            sysUserGroupUserDO.setUserGroupId(groupId);
            sysUserGroupUserDO.setUserId(id);
            list.add(sysUserGroupUserDO);
        });

        if (list.size() > 0 && sysUserGroupUserService.saveBatch(list)) {

            List<List<String>> list2 = JDBCUtil.getListGroupBy(userIds);
            List<SysUserDO> users = new ArrayList<>(16);
            list2.stream().forEach(ids -> {
                users.addAll(sysUserService.list(new QueryWrapper<SysUserDO>().eq("is_delete", 0)
                        .and(ids == null || ids.size() == 0, i -> i.eq("1", "2"))
                        .in("id", ids).select("User_name")));
            });
            if (!sysAuditLogService.saveLog(new AuditLogTypeDO(AuditLogType.success, "新增"), "往用户组添加用户","往用户组添加用户："+ users
                    .parallelStream
                            ().map
                            (SysUserDO::getRealName)
                    .collect(Collectors.toList()))) {
                throw new RException(InternationUtils.getInternationalMsg("ADD_FAILED"));
            }
            return R.ok(InternationUtils.getInternationalMsg("ADD_SUCCESS"));
        }
        return R.error(InternationUtils.getInternationalMsg("ADD_FAILED"));
    }

    @Override
    public R deleteUser(Map<String, Object> map) {
        String groupId = (String) map.get("groupId");
        List<String> userIds = (ArrayList) map.get("userIds");
        if (StringUtils.isBlank(groupId) || userIds == null || userIds.size() == 0) {
            return R.error(InternationUtils.getInternationalMsg("DELETE_FAILED"));
        }
        boolean result = sysUserGroupUserService.remove(new QueryWrapper<SysUserGroupUserDO>()
                .eq("user_group_id", groupId)
                .and(userIds == null || userIds.size() == 0, i -> i.eq("1", "2"))
                .in("user_id", userIds));
        if (result) {
            List<SysUserDO> users = sysUserService.list(new QueryWrapper<SysUserDO>().eq("is_delete", 0)
                    .and(userIds == null || userIds.size() == 0, i -> i.eq("1", "2"))
                    .in("id", userIds).select("user_name"));
            if (!sysAuditLogService.saveLog(new AuditLogTypeDO(AuditLogType.danger, "删除"), "从用户组删除用户", "从用户组删除用户："+users
                    .parallelStream
                            ().map
                            (SysUserDO::getUserName)
                    .collect(Collectors.toList()))) {
                throw new RException(InternationUtils.getInternationalMsg("DELETE_FAILED"));
            }
            return R.ok(InternationUtils.getInternationalMsg("DELETE_SUCCESS"));
        }
        return R.error(InternationUtils.getInternationalMsg("DELETE_FAILED"));
    }

    /**
     * 通过应用集合获取用户组列表
     *
     * @param sysUserDO
     * @return boolean
     * @author zuogang
     * @date 2019/4/22 15:27
     */
    @Override
    public List<SysUserGroupDO> getUserGroupsByApps(SysUserDO sysUserDO) {
        List<SysUserGroupDO> sysUserGroupDOList = new ArrayList<>(16);
        sysUserDO.getApps().stream().forEach(app -> {
            sysUserGroupDOList.addAll(list(new QueryWrapper<SysUserGroupDO>()
                    .eq("app_id", app.getId())));
        });
        return sysUserGroupDOList;
    }
}

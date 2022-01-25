package com.csicit.ace.orgauth.core.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.csicit.ace.common.exception.RException;
import com.csicit.ace.common.pojo.domain.BdPostDO;
import com.csicit.ace.common.utils.internationalization.InternationUtils;
import com.csicit.ace.data.persistent.mapper.BdPostMapper;
import com.csicit.ace.common.enums.AuditLogType;
import com.csicit.ace.common.enums.AuditLogTypeDO;
import com.csicit.ace.data.persistent.service.impl.BaseServiceImpl;
import com.csicit.ace.orgauth.core.service.BdPostService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 部门岗位表 实例对象访问接口实现
 *
 * @author generator
 * @version V1.0
 * @date 2019-04-15 17:24:50
 */
@Service("bdPostServiceO")
public class BdPostServiceImpl extends BaseServiceImpl<BdPostMapper, BdPostDO> implements BdPostService {

    @Override
    public boolean savePost(BdPostDO postDO) {
        //sortIndex
        if (StringUtils.isAnyBlank(postDO.getDepartmentId(), postDO.getGroupId(), postDO.getOrganizationId())) {
            throw new RException(InternationUtils.getInternationalMsg("EMPTY_ARG"));
        }
        Integer sortIndex = postDO.getSortIndex();
        int count = count(new QueryWrapper<BdPostDO>().eq("DEPARTMENT_ID", postDO.getDepartmentId()).eq("sort_index",
                sortIndex));
        if (count > 0) {
            throw new RException(InternationUtils.getInternationalMsg("SORT_INDEX_EXIST"));
        }
        if (save(postDO)) {
            //保存到其他部门
            List<String> otherDepIds = postDO.getOtherDepIds();
            Set<String> depIds = new HashSet<>();
            depIds.addAll(otherDepIds);
            if (!org.apache.commons.collections.CollectionUtils.isEmpty(depIds)) {
                depIds.remove(postDO.getDepartmentId());
                if (!org.apache.commons.collections.CollectionUtils.isEmpty(depIds)) {
                    BdPostDO newPost = new BdPostDO();
                    depIds.forEach(depId -> {
                        if (StringUtils.isNotBlank(depId)) {
                            newPost.setId(null);
                            newPost.setGroupId(postDO.getGroupId());
                            newPost.setOrganizationId(postDO.getOrganizationId());
                            newPost.setDepartmentId(depId);
                            newPost.setName(postDO.getName());
                            int newSortIndex = sortIndex;
                            while (count(new QueryWrapper<BdPostDO>().eq("DEPARTMENT_ID", depId
                            ).eq("sort_index",
                                    newSortIndex)) > 0) {
                                newSortIndex++;
                            }
                            newPost.setSortIndex(newSortIndex);
                            if (!save(newPost)) {
                                throw new RException(InternationUtils.getInternationalMsg("OPERATE_FAILED"));
                            }
                        }
                    });
                }
            }
            if (sysAuditLogService.saveLog(new AuditLogTypeDO(AuditLogType.success, "新增"), "保存岗位", "保存岗位："+postDO.getName(), postDO.getGroupId(),
                    null)) {
                return true;
            } else {
                throw new RException(InternationUtils.getInternationalMsg("UNKNOWN_EXCEPTION"));
            }
        }
        return false;
    }

    @Override
    public boolean updatePost(BdPostDO postDO) {
        BdPostDO one = getOne(new QueryWrapper<BdPostDO>().eq("id", postDO.getId()));
        if (!Objects.equals(one.getSortIndex(), postDO.getSortIndex())) {
            int count = count(new QueryWrapper<BdPostDO>().eq("DEPARTMENT_ID", postDO.getDepartmentId()).eq
                    ("sort_index", postDO.getSortIndex()));
            if (count > 0) {
                throw new RException(InternationUtils.getInternationalMsg("SORT_INDEX_EXIST"));
            }
        }
        if (updateById(postDO)) {
            if (sysAuditLogService.saveLog(new AuditLogTypeDO(AuditLogType.warning, "更新"), "修改岗位", "修改岗位："+postDO.getName(), postDO.getGroupId(), null)) {
                return true;
            } else {
                throw new RException(InternationUtils.getInternationalMsg("UNKNOWN_EXCEPTION"));
            }
        }
        return false;
    }

    @Override
    public boolean deletePost(String[] ids) {
        if (ids.length > 0) {
            List<BdPostDO> list = list(new QueryWrapper<BdPostDO>().in("id", ids));
            if (removeByIds(Arrays.asList(ids))) {
                if (sysAuditLogService.saveLog(new AuditLogTypeDO(AuditLogType.danger, "删除"), "删除岗位","删除岗位："+ list.stream()
                        .map(BdPostDO::getName)
                        .collect(Collectors.toList()), list.get(0).getGroupId(), null)) {
                    return true;
                } else {
                    throw new RException(InternationUtils.getInternationalMsg("EMPTY_ARG"));
                }
            }
            return false;
        }
        return true;
    }
}

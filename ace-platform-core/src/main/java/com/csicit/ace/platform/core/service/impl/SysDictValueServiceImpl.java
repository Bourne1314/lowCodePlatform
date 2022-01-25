package com.csicit.ace.platform.core.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.csicit.ace.common.exception.RException;
import com.csicit.ace.common.pojo.domain.OrgGroupDO;
import com.csicit.ace.common.pojo.domain.SysDictValueDO;
import com.csicit.ace.common.utils.SortPathUtils;
import com.csicit.ace.common.utils.StringUtils;
import com.csicit.ace.data.persistent.utils.AceSqlUtils;
import com.csicit.ace.common.enums.AuditLogType;
import com.csicit.ace.common.enums.AuditLogTypeDO;
import com.csicit.ace.data.persistent.mapper.SysDictValueMapper;
import com.csicit.ace.data.persistent.service.impl.BaseServiceImpl;
import com.csicit.ace.platform.core.service.OrgGroupService;
import com.csicit.ace.platform.core.service.SysDictService;
import com.csicit.ace.platform.core.service.SysDictValueService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;


/**
 * 字典数据表 实例对象访问接口实现
 *
 * @author shanwj
 * @version V1.0
 * @date 2019-05-21 8:11:27
 */
@Service("sysDictValueService")
public class SysDictValueServiceImpl extends BaseServiceImpl<SysDictValueMapper, SysDictValueDO> implements
        SysDictValueService {

    @Autowired
    AceSqlUtils aceSqlUtils;

    @Autowired
    OrgGroupService orgGroupService;

    @Autowired
    SysDictService sysDictService;

    @Override
    public List<SysDictValueDO> getDictValueByGroupId(String type, String groupId) {
        if (StringUtils.isNotBlank(type) && StringUtils.isNotBlank(groupId)) {
            List<SysDictValueDO> list = list(new QueryWrapper<SysDictValueDO>().eq("scope", 2).eq("type", type).eq
                    ("group_id",
                            groupId).orderByAsc("sort_path"));
            if (org.apache.commons.collections.CollectionUtils.isNotEmpty(list)) {
                return list;
            } else {
                OrgGroupDO group = orgGroupService.getById(groupId);
                if (group != null && StringUtils.isNotBlank(group.getParentId()) && !Objects.equals("0", group
                        .getParentId())) {
                    return getDictValueByGroupId(type, group.getParentId());
                } else {
                    return list(new QueryWrapper<SysDictValueDO>().eq("scope", 1).eq("type", type).orderByAsc
                            ("sort_path"));
                }
            }
        }
        if (StringUtils.isNotBlank(type)) {
            return list(new QueryWrapper<SysDictValueDO>().eq("scope", 1).eq("type", type).orderByAsc("sort_path"));
        }
        return null;
    }

    @Override
    public List<SysDictValueDO> getDictValueByAppId(String type, String appId) {
        if (StringUtils.isNotBlank(type) && StringUtils.isNotBlank(appId)) {
            List<SysDictValueDO> list = list(new QueryWrapper<SysDictValueDO>().eq("scope", 3).eq("type", type).eq
                    ("app_id", appId).orderByAsc("sort_path"));
            return org.apache.commons.collections.CollectionUtils.isNotEmpty(list) ? list : getDictValueByGroupId
                    (type, securityUtils
                            .getCurrentGroupId());
        }
        if (StringUtils.isNotBlank(type)) {
            return getDictValueByGroupId(type, securityUtils.getCurrentGroupId());
        }
        return new ArrayList<>(16);
    }

    /**
     * 保存数据
     *
     * @param sysDictValueDO 字典数据
     * @return boolean
     * @author shanwj
     * @date 2019/5/21 10:20
     */
    @Override
    public boolean saveDictValue(SysDictValueDO sysDictValueDO) {
        //设置排序
        String sortPath;
        if (Objects.equals("0", sysDictValueDO.getParentId())) {
            sortPath = SortPathUtils.getSortPath("", sysDictValueDO.getSortIndex());
        } else {
            SysDictValueDO parentGroup = getById(sysDictValueDO.getParentId());
            sortPath = SortPathUtils.getSortPath(parentGroup.getSortPath(), sysDictValueDO.getSortIndex());
        }
        aceSqlUtils.validateTreeTableWithUnique("sys_dict_value", sysDictValueDO.getParentId(), sysDictValueDO
                        .getSortIndex(),
                sortPath, "type_id",
                sysDictValueDO.getTypeId());
        sysDictValueDO.setSortPath(sortPath);
        if (save(sysDictValueDO)) {
            return sysAuditLogService.saveLog(new AuditLogTypeDO(AuditLogType.success, "新增"), "新增字典数据项",
                    sysDictValueDO, securityUtils
                            .getCurrentGroupId(),
                    sysDictValueDO.getAppId());
        }
        return false;
    }


    /**
     * 更新字典数据
     *
     * @param sysDictValueDO 字典数据
     * @return boolean
     * @author shanwj
     * @date 2019/5/21 10:20
     */
    @Override
    public boolean updateDictValue(SysDictValueDO sysDictValueDO) {
        SysDictValueDO old = getById(sysDictValueDO.getId());
//        if (!Objects.equals(old.getSortIndex(), sysDictValueDO.getSortIndex())) {
//            SysDictValueDO parentValue = getById(sysDictValueDO.getParentId());
//            String sortPath =
//                    parentValue == null ? SortPathUtils.getSortPath("", sysDictValueDO.getSortIndex()) :
//                            SortPathUtils.getSortPath(parentValue.getSortPath(), sysDictValueDO.getSortIndex());
//            sysDictValueDO.setSortPath(sortPath);
//            sqlUtils.updateSonSortPath(
//                    "sys_dict_value", sortPath, old.getSortPath().length(), old.getSortPath());
//        }
        if (!Objects.equals(old.getSortIndex(), sysDictValueDO.getSortIndex())) {
            SysDictValueDO parentDictValue = getById(sysDictValueDO.getParentId());
            String sortPath = "";
            if (parentDictValue != null) {
                sortPath =
                        SortPathUtils.getSortPath(parentDictValue.getSortPath(), sysDictValueDO.getSortIndex());
                sysDictValueDO.setSortPath(sortPath);

            } else {
                sortPath =
                        SortPathUtils.getSortPath(sortPath, sysDictValueDO.getSortIndex());
                sysDictValueDO.setSortPath(sortPath);
            }
            aceSqlUtils.updateSonSortPathWithUnique(
                    "sys_dict_value", sortPath, old.getSortPath().length(), old.getSortPath(), "type_id",
                    sysDictValueDO.getTypeId());
        }
        if (updateById(sysDictValueDO)) {
            return sysAuditLogService.saveLog(new AuditLogTypeDO(AuditLogType.warning, "更新"), "更新字典数据项",
                    sysDictValueDO, securityUtils
                            .getCurrentGroupId(),
                    sysDictValueDO.getAppId());
        }
        return false;
    }

    /**
     * 删除字典数据
     * 根据提供的id集合进行删除
     *
     * @param ids 字典数据对象id集合
     * @return boolean
     * @author shanwj
     * @date 2019/5/21 10:20
     */
    @Override
    public boolean deleteByIds(Collection<? extends Serializable> ids) {
        StringBuffer stringBuffer = new StringBuffer();
        String appId = getById((
                (List<String>) ids).get(0)).getAppId();
        ids.forEach(id -> {
            SysDictValueDO dictValueDO = getById(id);
            String str = dictValueDO.getDictName();
            String sortPath = dictValueDO.getSortPath();
            String typeId = dictValueDO.getTypeId();
            if (!remove(new QueryWrapper<SysDictValueDO>().eq("type_id", typeId)
                    .likeRight
                            ("sort_path", sortPath))) {
                throw new RException("删除:" + dictValueDO.getDictName() + "字典数据失败");
            }
            stringBuffer.append(str);
            stringBuffer.append(",");
        });
        return
                sysAuditLogService.saveLog(new AuditLogTypeDO(AuditLogType.danger, "删除"),
                        "删除字典数据", stringBuffer.substring(0, stringBuffer.length() - 1), securityUtils
                                .getCurrentGroupId(), appId);

    }
}

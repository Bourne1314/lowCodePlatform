package com.csicit.ace.platform.core.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.csicit.ace.common.exception.RException;
import com.csicit.ace.common.pojo.domain.BdPersonIdTypeDO;
import com.csicit.ace.common.utils.internationalization.InternationUtils;
import com.csicit.ace.common.utils.server.R;
import com.csicit.ace.data.persistent.mapper.BdPersonIdTypeMapper;
import com.csicit.ace.common.enums.AuditLogType;
import com.csicit.ace.common.enums.AuditLogTypeDO;
import com.csicit.ace.data.persistent.service.impl.BaseServiceImpl;
import com.csicit.ace.platform.core.service.BdPersonIdTypeService;
import com.csicit.ace.platform.core.service.OrgGroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;


/**
 * 基础数据-人员证件类型 实例对象访问接口实现
 *
 * @author generator
 * @version V1.0
 * @date 2019-04-15 17:27:06
 */
@Service
public class BdPersonIdTypeServiceImpl extends BaseServiceImpl<BdPersonIdTypeMapper, BdPersonIdTypeDO> implements
        BdPersonIdTypeService {

    @Autowired
    OrgGroupService orgGroupService;

    @Override
    public R insert(BdPersonIdTypeDO personDoc) {
        personDoc.setCreateTime(LocalDateTime.now());
        personDoc.setUpdateTime(LocalDateTime.now());
        personDoc.setCreateUser(securityUtils.getCurrentUserId());
        if (save(personDoc)) {
            if (!sysAuditLogService.saveLog(new AuditLogTypeDO(AuditLogType.success, "新增"), "保存人员证件类型", "保存人员证件类型："+personDoc.getName(), personDoc.getGroupId(), null)) {
                throw new RException(InternationUtils.getInternationalMsg("SAVE_FAILED"));
            }
            return R.ok(InternationUtils.getInternationalMsg("SAVE_SUCCESS"));
        }
        throw new RException(InternationUtils.getInternationalMsg("SAVE_FAILED"));
    }

    @Override
    public R update(BdPersonIdTypeDO personDoc) {
        personDoc.setUpdateTime(LocalDateTime.now());
        if (updateById(personDoc)) {
            if (!sysAuditLogService.saveLog(new AuditLogTypeDO(AuditLogType.warning, "更新"), "修改人员证件类型", "修改人员证件类型："+personDoc.getName(), personDoc.getGroupId(), null)) {
                throw new RException(InternationUtils.getInternationalMsg("UPDATE_FAILED"));
            }
            return R.ok(InternationUtils.getInternationalMsg("UPDATE_SUCCESS"));
        }
        throw new RException(InternationUtils.getInternationalMsg("UPDATE_FAILED"));
    }

    @Autowired
    PersonIdTypeConfig personIdTypeConfig;

    @Override
    public R delete(List<String> ids) {
//        Set<String > groupIds = orgGroupService.list(new QueryWrapper<OrgGroupDO>().select("id")).stream().map
//                (OrgGroupDO::getId).collect(Collectors.toSet());
//        List<BdPersonIdTypeDO> personIdTypeDOS = personIdTypeConfig.getPersonIdTypes();
//            groupIds.forEach(groupId -> {
//                personIdTypeDOS.forEach(personIdType -> {
//                    String[] strs = personIdType.getId().split("-");
//                    if (strs.length > 1) {
//                        personIdType.setId(strs[0] + "-" + UuidUtils.createUUID().substring(strs[0].length()));
//                    } else {
//                        personIdType.setId(personIdType.getId() + "-" + UuidUtils.createUUID().substring(personIdType
//                                .getId()
//                                .length()));
//                    }
//                    personIdType.setGroupId(groupId);
//                });
//                if (!saveBatch(personIdTypeDOS)) {
//                    throw new RException(InternationUtils.getInternationalMsg("OPERATE_FAILED"));
//                }
//            });
        if (org.apache.commons.collections.CollectionUtils.isNotEmpty(ids)) {
            List<BdPersonIdTypeDO> list = list(new QueryWrapper<BdPersonIdTypeDO>().select("name", "group_id").in
                    ("id", ids));
            if (org.apache.commons.collections.CollectionUtils.isNotEmpty(list)) {
                String groupId = list.get(0).getGroupId();
                if (removeByIds(ids)) {
                    if (!sysAuditLogService.saveLog(new AuditLogTypeDO(AuditLogType.danger, "删除"), "删除人员证件类型", "删除人员证件类型："+list.stream().map
                                    (BdPersonIdTypeDO::getName).collect(Collectors.toList()),
                            groupId,
                            null)) {
                        throw new RException(InternationUtils.getInternationalMsg("DELETE_FAILED"));
                    }
                    return R.ok(InternationUtils.getInternationalMsg("DELETE_SUCCESS"));
                } else {
                    throw new RException(InternationUtils.getInternationalMsg("DELETE_FAILED"));
                }
            }
        }
        throw new RException(InternationUtils.getInternationalMsg("DELETE_FAILED"));
    }
}

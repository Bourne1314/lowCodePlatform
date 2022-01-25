package com.csicit.ace.platform.core.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.csicit.ace.common.enums.AuditLogType;
import com.csicit.ace.common.enums.AuditLogTypeDO;
import com.csicit.ace.common.exception.RException;
import com.csicit.ace.common.pojo.domain.BdPersonDocDO;
import com.csicit.ace.common.pojo.domain.BdPersonIdTypeDO;
import com.csicit.ace.common.pojo.domain.BdPersonJobDO;
import com.csicit.ace.common.pojo.domain.SysUserDO;
import com.csicit.ace.common.utils.PinyinUtils;
import com.csicit.ace.common.utils.StringUtils;
import com.csicit.ace.common.utils.internationalization.InternationUtils;
import com.csicit.ace.common.utils.server.R;
import com.csicit.ace.data.persistent.mapper.BdPersonDocMapper;
import com.csicit.ace.data.persistent.service.SysAuditLogService;
import com.csicit.ace.data.persistent.service.impl.BaseServiceImpl;
import com.csicit.ace.platform.core.service.*;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;


/**
 * 基础数据-人员档案 实例对象访问接口实现
 *
 * @author yansiyang
 * @version V1.0
 * @date 2019-04-15 17:25:44
 */
@Service
public class BdPersonDocServiceImpl extends BaseServiceImpl<BdPersonDocMapper, BdPersonDocDO> implements
        BdPersonDocService {

    @Autowired
    BdPersonJobService bdPersonJobService;

    @Autowired
    BdPersonIdTypeService bdPersonIdTypeService;

    @Autowired
    SysUserService sysUserService;

    @Autowired
    SysAuditLogService sysAuditLogService;

    @Autowired
    SysRoleService sysRoleService;

    @Override
    public BdPersonDocDO getPersonById(String id) {
        BdPersonDocDO person = getById(id);
        List<BdPersonJobDO> jobs = bdPersonJobService.list(new QueryWrapper<BdPersonJobDO>()
                .eq("person_doc_id", id));
        person.setJobList(jobs);
        return person;
    }

    @Override
    public R insert(BdPersonDocDO personDoc) {
        if (count(new QueryWrapper<BdPersonDocDO>().eq("code", personDoc.getCode())) > 0) {
            return R.error(InternationUtils.getInternationalMsg("SAME_MODEL",
                    new String[]{InternationUtils.getInternationalMsg("CODE"), personDoc.getCode()}
            ));
        }
        if (StringUtils.isNotBlank(personDoc.getPersonIdTypeId())) {
            BdPersonIdTypeDO bdPersonIdType = bdPersonIdTypeService.getById(personDoc.getPersonIdTypeId());
            if (bdPersonIdType == null) {
                return R.error(InternationUtils.getInternationalMsg("PERSON_ID_TYPE_NOT_EXIST"));
            }
            if (bdPersonIdType.getNumberLength() != personDoc.getIdNumber().length()) {
                return R.error(InternationUtils.getInternationalMsg("WRONG_NUMBER_ID_TYPE"));
            }
        }
        if (personDoc.getMainJob() == null) {
            return R.error(InternationUtils.getInternationalMsg("EMPTY_ARG_WITH_PARAM", "主职务"));
        }
        if (StringUtils.isNotBlank(personDoc.getName())) {
            personDoc.setPinYin(PinyinUtils.toHanyuPinyin(personDoc.getName()));
        }
        personDoc.setCreateTime(LocalDateTime.now());
        personDoc.setUpdateTime(LocalDateTime.now());
        personDoc.setCreateUser(securityUtils.getCurrentUserId());
        if (save(personDoc)) {
            BdPersonJobDO job = personDoc.getMainJob();
            job.setPersonDocId(personDoc.getId());
            if (!bdPersonJobService.savePersonJob(job)) {
                throw new RException(InternationUtils.getInternationalMsg("SAVE_FAILED"));
            }

            // 是否同时创建 用户
            if (personDoc.getUser() != null && StringUtils.isNotBlank(personDoc.getUser().getUserName())) {
                SysUserDO user = personDoc.getUser();
                user.setPersonDocId(personDoc.getId());
                user.setOrganizationId(personDoc.getOrganizationId());
                user.setRealName(personDoc.getName());
                user.setGroupId(personDoc.getGroupId());
                user.setPhoneNumber(personDoc.getOfficePhone());
                sysUserService.saveUser(user);

                /**
                 * 保存用户和角色-部门关系
                 */
                // sysRoleService.updateRoleAndDepForUser(user.getId(), job.getDepartmentId(), null);
            }

            if (!sysAuditLogService.saveLog(new AuditLogTypeDO(AuditLogType.success, "新增"), "保存人员档案", "保存人员档案：" +
                            personDoc.getName(),
                    personDoc
                            .getGroupId(),
                    null)) {
                throw new RException(InternationUtils.getInternationalMsg("SAVE_FAILED"));
            }
            return R.ok(InternationUtils.getInternationalMsg("SAVE_SUCCESS")).put("personDocId", personDoc.getId())
                    .put("personDocName", personDoc.getName());
        }
        throw new RException(InternationUtils.getInternationalMsg("SAVE_FAILED"));
    }

    @Override
    public R update(BdPersonDocDO personDoc) {
        if (count(new QueryWrapper<BdPersonDocDO>()
                .ne("id", personDoc.getId())
                .eq("code", personDoc.getCode())) > 0) {
            return R.error(InternationUtils.getInternationalMsg("SAME_MODEL",
                    new String[]{InternationUtils.getInternationalMsg("CODE"), personDoc.getCode()}
            ));
        }
        if (StringUtils.isNotBlank(personDoc.getPersonIdTypeId())) {
            BdPersonIdTypeDO bdPersonIdType = bdPersonIdTypeService.getById(personDoc.getPersonIdTypeId());
            if (bdPersonIdType == null) {
                return R.error(InternationUtils.getInternationalMsg("PERSON_ID_TYPE_NOT_EXIST"));
            }
            if (bdPersonIdType.getNumberLength() != personDoc.getIdNumber().length()) {
                return R.error(InternationUtils.getInternationalMsg("WRONG_NUMBER_ID_TYPE"));
            }
        }
        if (StringUtils.isNotBlank(personDoc.getName())) {
            personDoc.setPinYin(PinyinUtils.toHanyuPinyin(personDoc.getName()));
        }
        personDoc.setUpdateTime(LocalDateTime.now());
        if (updateById(personDoc)) {
            if (!sysAuditLogService.saveLog(new AuditLogTypeDO(AuditLogType.warning, "更新"), "修改人员档案", "修改人员档案：" +
                            personDoc.getName(),
                    personDoc.getGroupId(), null)) {
                throw new RException(InternationUtils.getInternationalMsg("UPDATE_FAILED"));
            }
            return R.ok(InternationUtils.getInternationalMsg("UPDATE_SUCCESS"));
        }
        throw new RException(InternationUtils.getInternationalMsg("UPDATE_FAILED"));
    }

    @Override
    public R delete(Map<String, Object> map) {
        String currentDep = (String) map.get("currentDep");
        String currentOrg = (String) map.get("currentOrg");
        List<String> ids = (List<String>) map.get("ids");
        if (org.apache.commons.collections.CollectionUtils.isNotEmpty(ids)) {
            // 判断当前人员ID是否在currentOrg组织内定义的，
            // 如果不是，则删除该currentDep部门下，这个人员的职务信息
            // 如果是，则删除人员信息

            List<String> deleteJobToDocId = new ArrayList<>(16);// 删除职务的人员ID列表
            List<String> deletePersonDocId = new ArrayList<>(16);// 删除人员的ID列表
            ids.stream().forEach(id -> {
                if (count(new QueryWrapper<BdPersonDocDO>().eq("id", id).eq("organization_id", currentOrg)) > 0) {
                    deletePersonDocId.add(id);
                } else {
                    deleteJobToDocId.add(id);
                }
            });

            //  删除职务信息
            if (CollectionUtils.isNotEmpty(deleteJobToDocId)) {
                if (Objects.equals("allParentId", currentDep)) {
                    if (!bdPersonJobService.remove(new QueryWrapper<BdPersonJobDO>()
                            .eq("ORGANIZATION_ID", currentOrg).in("PERSON_DOC_ID", deleteJobToDocId))) {
                        throw new RException(InternationUtils.getInternationalMsg("DELETE_FAILED"));
                    }
                } else {
                    if (!bdPersonJobService.remove(new QueryWrapper<BdPersonJobDO>()
                            .eq("DEPARTMENT_ID", currentDep).in("PERSON_DOC_ID", deleteJobToDocId))) {
                        throw new RException(InternationUtils.getInternationalMsg("DELETE_FAILED"));
                    }
                }

            }

            // 删除人员信息
            if (CollectionUtils.isNotEmpty(deletePersonDocId)) {
                List<BdPersonDocDO> list = list(new QueryWrapper<BdPersonDocDO>().eq("is_delete", 0)
                        .in("id", deletePersonDocId)
                        .select("name", "group_id"));
                if (list.size() > 0) {
                    if (this.update(new BdPersonDocDO(), new UpdateWrapper<BdPersonDocDO>().setSql("code=CONCAT" +
                            "(CONCAT" +
                            "('del-', SUBSTR(id,1,2)),code)").set("is_delete", 1).in("id", ids))) {
                        // 删除用户关系
                        if (sysUserService.count(new QueryWrapper<SysUserDO>().eq("is_delete", 0).in("person_doc_id",
                                ids))
                                > 0) {
                            if (!sysUserService.update(new SysUserDO(), new UpdateWrapper<SysUserDO>().in
                                    ("person_doc_id",
                                    ids).setSql("person_doc_id=null"))) {
                                throw new RException(InternationUtils.getInternationalMsg("DELETE_FAILED"));
                            }
                            // 更新用户-角色-部门关联关系
                            List<SysUserDO> userDOS = sysUserService.list(new QueryWrapper<SysUserDO>().eq("is_delete",
                                    0).in("person_doc_id", ids));
                            for (SysUserDO user : userDOS) {
                                sysRoleService.updateRoleAndDepForUser(user.getId(), null, bdPersonJobService.getOne(new
                                        QueryWrapper<BdPersonJobDO>()
                                        .eq("person_doc_id", user.getPersonDocId()).eq("is_main_job", 1))
                                        .getDepartmentId
                                        ());
                            }
                        }
                        sysAuditLogService.saveLog(new AuditLogTypeDO(AuditLogType.danger, "删除"), "删除人员档案", "删除人员档案："
                                + list
                                .parallelStream().map
                                        (BdPersonDocDO::getName)
                                .collect(Collectors.toList()), list.get(0).getGroupId(), null);
                    } else {
                        throw new RException(InternationUtils.getInternationalMsg("DELETE_FAILED"));
                    }
                } else {
                    throw new RException(InternationUtils.getInternationalMsg("EMPTY_ARG"));
                }
            }
            return R.ok(InternationUtils.getInternationalMsg("DELETE_SUCCESS"));
        } else {
            throw new RException(InternationUtils.getInternationalMsg("EMPTY_ARG_WITH_PARAM", "人员主键"));
        }

    }
}

package com.csicit.ace.orgauth.core.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.csicit.ace.common.exception.RException;
import com.csicit.ace.common.pojo.domain.*;
import com.csicit.ace.common.utils.PinyinUtils;
import com.csicit.ace.common.utils.StringUtils;
import com.csicit.ace.common.utils.UuidUtils;
import com.csicit.ace.common.utils.internationalization.InternationUtils;
import com.csicit.ace.common.utils.server.R;
import com.csicit.ace.data.persistent.mapper.BdPersonDocMapper;
import com.csicit.ace.data.persistent.service.impl.BaseServiceImpl;
import com.csicit.ace.data.persistent.utils.AceSqlUtils;
import com.csicit.ace.orgauth.core.service.*;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


/**
 * 基础数据-人员档案 实例对象访问接口实现
 *
 * @author yansiyang
 * @version V1.0
 * @date 2019-04-15 17:25:44
 */
@Service("bdPersonDocServiceO")
public class BdPersonDocServiceImpl extends BaseServiceImpl<BdPersonDocMapper, BdPersonDocDO> implements
        BdPersonDocService {

    @Autowired
    AceSqlUtils aceSqlUtils;

    @Resource(name = "sysUserServiceO")
    SysUserService sysUserService;

    @Resource(name = "bdPostServiceO")
    BdPostService bdPostService;

    @Resource(name = "bdJobServiceO")
    BdJobService bdJobService;

    @Resource(name = "bdPersonJobServiceO")
    BdPersonJobService bdPersonJobService;

    @Resource(name = "bdPersonDocServiceO")
    BdPersonDocService bdPersonDocService;

    @Override
    public boolean addPersonDocs(List<BdPersonDocDO> personDocDOS) {
        int count = personDocDOS.size() / 100;
        for (int i = 0; i < (count + 1); i++) {
            List<BdPersonDocDO> addPersonDocDOS = personDocDOS.subList(100 * i, 100 * (i + 1) <= personDocDOS.size()
                    ? 100 * (i + 1) : personDocDOS
                    .size());

            List<BdPersonJobDO> jobs = new ArrayList<>(16);
            for (int j = 0; j <addPersonDocDOS.size(); j++) {
                BdPersonDocDO personDoc=addPersonDocDOS.get(j);
                if (personDoc.getMainJob() == null) {
                    throw new RException(InternationUtils.getInternationalMsg("EMPTY_ARG_WITH_PARAM", "主职务"));
                }
                Map<String, Object> map = new HashMap<>();
                map.put("tableName", "bd_person_doc");
                map.put("groupId", personDoc.getGroupId());
                map.put("orgId", personDoc.getOrganizationId());
                personDoc.setSortIndex(aceSqlUtils.getMaxSort(map) + 10*(j+1));
                personDoc.getMainJob().setSortIndex(10);

                if (StringUtils.isNotBlank(personDoc.getName())) {
                    personDoc.setPinYin(PinyinUtils.toHanyuPinyin(personDoc.getName()));
                }
                personDoc.setCreateTime(LocalDateTime.now());
                personDoc.setUpdateTime(LocalDateTime.now());
                BdPersonDocDO bdPersonDocDO = bdPersonDocService.getOne(new QueryWrapper<BdPersonDocDO>().eq("CODE",
                        personDoc.getCode()));
                if (bdPersonDocDO != null) {
                    throw new RException(InternationUtils.getInternationalMsg("SAME_MODEL",
                            new String[]{InternationUtils.getInternationalMsg("CODE"), personDoc.getCode()}
                    ));
                }
                personDoc.setId(UuidUtils.createUUID());

                //添加主职信息
                BdPersonJobDO job = personDoc.getMainJob();
                job.setGroupId(personDoc.getGroupId());
                job.setPersonDocId(personDoc.getId());
                job.setOrganizationId(personDoc.getOrganizationId());
                job.setPersonCode(personDoc.getCode());
                job.setMainJob(1);
                jobs.add(job);
            }

            if (saveBatch(addPersonDocDOS)) {
                if (!bdPersonJobService.saveBatch(jobs)) {
                    throw new RException(InternationUtils.getInternationalMsg("SAVE_FAILED"));
                }
            }else{
                throw new RException(InternationUtils.getInternationalMsg("SAVE_FAILED"));
            }
        }
        return true;
    }

    @Override
    public R getBdPersonDoc(String userId) {
        SysUserDO sysUserDO = sysUserService.getOne(new QueryWrapper<SysUserDO>().eq("ID", userId));
        if (sysUserDO.getPersonDocId() != null) {
            BdPersonDocDO bdPersonDocDO = bdPersonDocService.getOne(new QueryWrapper<BdPersonDocDO>().eq("ID",
                    sysUserDO.getPersonDocId()));
            List<BdPersonJobDO> bdPersonJobDOList = bdPersonJobService.list(new QueryWrapper<BdPersonJobDO>().eq
                    ("PERSON_DOC_ID", bdPersonDocDO.getId()));
            bdPersonDocDO.setJobList(bdPersonJobDOList);
            // BdPersonJobDO bdPersonJobDO = bdPersonJobService.getOne(new QueryWrapper<BdPersonJobDO>().eq
            // ("PERSON_DOC_ID",bdPersonDocDO.getId()));
            if (CollectionUtils.isNotEmpty(bdPersonJobDOList)) {
                bdPersonDocDO.setMainJob(bdPersonJobDOList.parallelStream().filter(job -> job.getMainJob() == 1)
                        .findFirst().get());
            }
            return R.ok().put("bdPersonDocDO", bdPersonDocDO);
        }
        return null;
    }

    @Override
    public R savePersonAndUser(BdPersonDocDO personDoc) {

        if (personDoc.getMainJob() == null) {
            return R.error(InternationUtils.getInternationalMsg("EMPTY_ARG_WITH_PARAM", "主职务"));
        }
        //默认一个排序号
        Map<String, Object> map = new HashMap<>();
        map.put("tableName", "bd_person_doc");
        map.put("groupId", personDoc.getGroupId());
        map.put("orgId", personDoc.getOrganizationId());
        personDoc.setSortIndex(aceSqlUtils.getMaxSort(map) + 10);
        personDoc.getMainJob().setSortIndex(10);
        map.put("tableName", "sys_user");
        map.put("groupId", personDoc.getGroupId());
        map.put("orgId", personDoc.getOrganizationId());
        personDoc.getUser().setSortIndex(aceSqlUtils.getMaxSort(map) + 10);

        if (StringUtils.isNotBlank(personDoc.getName())) {
            personDoc.setPinYin(PinyinUtils.toHanyuPinyin(personDoc.getName()));
        }
        personDoc.setCreateTime(LocalDateTime.now());
        personDoc.setUpdateTime(LocalDateTime.now());
        //personDoc.setCreateUser(securityUtils.getCurrentUserId());

        BdPersonDocDO bdPersonDocDO = bdPersonDocService.getOne(new QueryWrapper<BdPersonDocDO>().eq("CODE",
                personDoc.getCode()));
        if (bdPersonDocDO != null) {
            throw new RException(InternationUtils.getInternationalMsg("SAVE_FAILED"));
        }
        if (save(personDoc)) {
            BdPersonJobDO job = personDoc.getMainJob();
            job.setGroupId(personDoc.getGroupId());
            job.setPersonDocId(personDoc.getId());
            job.setOrganizationId(personDoc.getOrganizationId());
            job.setPersonCode(personDoc.getCode());
            job.setMainJob(1);
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
                user.setSecretLevel(5);
                sysUserService.saveUser(user);
            }

//            if (!sysAuditLogService.saveLog(new AuditLogTypeDO(AuditLogType.success, "新增"), "保存人员档案", personDoc,
//                    personDoc
//                            .getGroupId(),
//                    null)) {
//                throw new RException(InternationUtils.getInternationalMsg("SAVE_FAILED"));
//            }
            return R.ok(InternationUtils.getInternationalMsg("SAVE_SUCCESS")).put("personDocId", personDoc.getId())
                    .put("personDocName", personDoc.getName()).put("userId", personDoc.getUser().getId());
        }
        throw new RException(InternationUtils.getInternationalMsg("SAVE_FAILED"));
    }

    @Override
    public BdPostDO getMainPostByUserId(String userId) {
        if (StringUtils.isNotBlank(userId)) {
            SysUserDO user = sysUserService.getOne(new QueryWrapper<SysUserDO>().eq("id", userId).eq("is_delete", 0));
            if (user != null) {
                String personId = user.getPersonDocId();
                if (StringUtils.isNotBlank(personId)) {
                    BdPersonJobDO personJobDO = bdPersonJobService.getOne(new QueryWrapper<BdPersonJobDO>().eq
                            ("person_doc_id", personId).eq("is_main_job", 1));
                    if (personJobDO != null) {
                        String postId = personJobDO.getPostId();
                        if (StringUtils.isNotBlank(postId)) {
                            return bdPostService.getById(postId);
                        }
                    }
                }
            }
        }
        return new BdPostDO();
    }

    @Override
    public List<BdPostDO> getPostsByUserId(String userId) {
        if (StringUtils.isNotBlank(userId)) {
            SysUserDO user = sysUserService.getOne(new QueryWrapper<SysUserDO>().eq("id", userId).eq("is_delete", 0));
            if (user != null) {
                String personId = user.getPersonDocId();
                if (StringUtils.isNotBlank(personId)) {
                    List<BdPersonJobDO> personJobDOs = bdPersonJobService.list(new QueryWrapper<BdPersonJobDO>().eq
                            ("person_doc_id", personId));
                    if (!org.apache.commons.collections.CollectionUtils.isEmpty(personJobDOs)) {
                        List<String> postIds = personJobDOs.stream().map(BdPersonJobDO::getPostId).collect(Collectors
                                .toList());
                        if (!org.apache.commons.collections.CollectionUtils.isEmpty(postIds)) {
                            return bdPostService.list(new QueryWrapper<BdPostDO>().in("id", postIds));
                        }
                    }
                }
            }
        }
        return new ArrayList<>();
    }

    @Override
    public BdPostDO getPostByDepIdAndUserId(String depId, String userId) {
        if (StringUtils.isNotBlank(depId) && StringUtils.isNotBlank(userId)) {
            SysUserDO user = sysUserService.getById(userId);
            if (user != null) {
                if (StringUtils.isNotBlank(user.getPersonDocId())) {
                    BdPersonJobDO bdPersonJobDO = bdPersonJobService.getOne(new QueryWrapper<BdPersonJobDO>().eq
                            ("person_doc_id", user.getPersonDocId()).eq("is_main_job", 1).eq("department_id", depId));
                    if (bdPersonJobDO != null && StringUtils.isNotBlank(bdPersonJobDO.getPostId())) {
                        return bdPostService.getById(bdPersonJobDO.getPostId());
                    }
                }
            }
        }
        return new BdPostDO();
    }

    @Override
    public BdJobDO getJobByDepIdAndUserId(String depId, String userId) {
        if (StringUtils.isNotBlank(depId) && StringUtils.isNotBlank(userId)) {
            SysUserDO user = sysUserService.getById(userId);
            if (user != null) {
                if (StringUtils.isNotBlank(user.getPersonDocId())) {
                    BdPersonJobDO bdPersonJobDO = bdPersonJobService.getOne(new QueryWrapper<BdPersonJobDO>().eq
                            ("person_doc_id", user.getPersonDocId()).eq("is_main_job", 1).eq("department_id", depId));
                    if (bdPersonJobDO != null && StringUtils.isNotBlank(bdPersonJobDO.getJobId())) {
                        return bdJobService.getById(bdPersonJobDO.getJobId());
                    }
                }
            }
        }
        return new BdJobDO();
    }
}

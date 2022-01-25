package com.csicit.ace.orgauth.core.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.csicit.ace.common.exception.RException;
import com.csicit.ace.common.pojo.domain.BdPersonDocDO;
import com.csicit.ace.common.pojo.domain.BdPersonJobDO;
import com.csicit.ace.common.utils.StringUtils;
import com.csicit.ace.common.utils.UuidUtils;
import com.csicit.ace.common.utils.internationalization.InternationUtils;
import com.csicit.ace.data.persistent.mapper.BdPersonJobMapper;
import com.csicit.ace.data.persistent.service.impl.BaseServiceImpl;
import com.csicit.ace.orgauth.core.service.BdPersonDocService;
import com.csicit.ace.orgauth.core.service.BdPersonJobService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;


/**
 * 基础数据-人员工作信息 实例对象访问接口实现
 *
 * @author generator
 * @version V1.0
 * @date 2019-04-15 17:27:00
 */
@Service("bdPersonJobServiceO")
public class BdPersonJobServiceImpl extends BaseServiceImpl<BdPersonJobMapper, BdPersonJobDO> implements
        BdPersonJobService {

    @Autowired
    BdPersonDocService bdPersonDocService;

    @Override
    public boolean savePersonJob(BdPersonJobDO job) {
        if (StringUtils.isBlank(job.getGroupId()) || StringUtils.isBlank(job.getDepartmentId()) || StringUtils
                .isBlank(job.getOrganizationId()) || StringUtils.isBlank(job.getPersonDocId())) {
            throw new RException(InternationUtils.getInternationalMsg("EMPTY_ARG_WITH_PARAM", "集团、业务单元、部门、人员主键"));
        }
        // 不要重复关联岗位
        if (StringUtils.isNotBlank(job.getPostId())) {
            int count = count(new QueryWrapper<BdPersonJobDO>().eq("post_id", job.getPostId()).eq("person_doc_id", job
                    .getPersonDocId()));
            if (count > 0) {
                throw new RException(InternationUtils.getInternationalMsg("POST_HAS_ASSOCIATED"));
            }
        }
        // 校验排序号
        Integer sortIndex = job.getSortIndex();
        if (sortIndex != null) {
            int count = count(new QueryWrapper<BdPersonJobDO>()
                    .eq("sort_index", sortIndex)
                    .eq("person_doc_id", job.getPersonDocId()));
            if (count > 0) {
                throw new RException(InternationUtils.getInternationalMsg("SORT_INDEX_EXIST"));
            }
        } else {
            List<Integer> nums = getSortIndex(job.getPersonDocId());
            Integer num = 2;
            while (true) {
                if (!nums.contains(num)) {
                    break;
                }
                num++;
            }
            job.setSortIndex(num);
        }
        int count = count(new QueryWrapper<BdPersonJobDO>().eq("is_main_job", 1)
                .eq("person_doc_id", job.getPersonDocId()));


        // 职务为空 则第一个添加的职务必须为主职务

        // 新增职务时 可以勾选将职务设置为主职
        boolean setMainJob = false;
        if (Objects.equals(job.getMainJob(), 1) && count > 0) {
            setMainJob = true;
        }

        job.setMainJob(0);
        if (count == 0) {
            job.setMainJob(1);
        }

        BdPersonDocDO person = bdPersonDocService.getById(job.getPersonDocId());
        String id = UuidUtils.createUUID();
        job.setId(id);
        job.setPersonCode(person.getCode());
        job.setCreateTime(LocalDateTime.now());
        job.setUpdateTime(LocalDateTime.now());
        //job.setCreateUser(securityUtils.getCurrentUserId());
        if (save(job)) {
            if (setMainJob) {
                if (!setMainJob(job.getPersonDocId(), id)) {
                    throw new RException(InternationUtils.getInternationalMsg("SAVE_FAILED"));
                }
            }
//            sysAuditLogService.saveLog(new AuditLogTypeDO(AuditLogType.success, "新增"), "保存人员职务信息", job, job.getGroupId(),
//                    null);
            return true;
        }
        return false;
    }

    public boolean setMainJob(String personDocId, String personJobId) {
        if (StringUtils.isBlank(personDocId) || StringUtils.isBlank(personJobId)) {
            throw new RException(InternationUtils.getInternationalMsg("EMPTY_ARG_WITH_PARAM", "人员、职务主键"));
        }
        if (update(new BdPersonJobDO(), new UpdateWrapper<BdPersonJobDO>().set("is_main_job", 0).eq("person_doc_id",
                personDocId))) {
            if (update(new BdPersonJobDO(), new UpdateWrapper<BdPersonJobDO>().set("is_main_job", 1).eq("id",
                    personJobId))) {
                return true;
            }
        }
        return false;
    }

    private List<Integer> getSortIndex(String personDocId) {
        List<Integer> nums = new ArrayList<>();
        List<BdPersonJobDO> list = list(new QueryWrapper<BdPersonJobDO>().eq("person_doc_id", personDocId).select
                ("sort_index"));
        if (list != null && list.size() > 0) {
            nums = list.stream().map(BdPersonJobDO::getSortIndex).collect(Collectors.toList());
        }
        return nums;
    }
}

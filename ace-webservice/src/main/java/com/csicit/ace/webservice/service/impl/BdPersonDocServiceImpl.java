package com.csicit.ace.webservice.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.csicit.ace.common.exception.RException;
import com.csicit.ace.common.pojo.domain.BdPersonDocDO;
import com.csicit.ace.common.pojo.domain.BdPersonJobDO;
import com.csicit.ace.common.pojo.domain.SysUserDO;
import com.csicit.ace.common.utils.PinyinUtils;
import com.csicit.ace.common.utils.StringUtils;
import com.csicit.ace.common.utils.internationalization.InternationUtils;
import com.csicit.ace.common.utils.server.R;
import com.csicit.ace.webservice.mapper.BdPersonDocMapper;
import com.csicit.ace.webservice.service.BdPersonDocService;
import com.csicit.ace.webservice.service.BdPersonJobService;
import com.csicit.ace.webservice.service.BdPersonJobService;
import com.csicit.ace.webservice.service.SysUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;


/**
 * 基础数据-人员档案 实例对象访问接口实现
 *
 * @author yansiyang
 * @version V1.0
 * @date 2019-04-15 17:25:44
 */
@Service
public class BdPersonDocServiceImpl extends ServiceImpl<BdPersonDocMapper, BdPersonDocDO> implements
        BdPersonDocService {

    @Autowired
    BdPersonJobService bdPersonJobService;

    @Autowired
    SysUserService sysUserService;

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
        if (personDoc.getMainJob() == null) {
            return R.error(InternationUtils.getInternationalMsg("EMPTY_ARG_WITH_PARAM", "主职务"));
        }
        if (StringUtils.isNotBlank(personDoc.getName())) {
            personDoc.setPinYin(PinyinUtils.toHanyuPinyin(personDoc.getName()));
        }
        personDoc.setCreateTime(LocalDateTime.now());
        personDoc.setUpdateTime(LocalDateTime.now());
        personDoc.setCreateUser("webservice");
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
            }
            return R.ok(InternationUtils.getInternationalMsg("SAVE_SUCCESS")).put("personDocId", personDoc.getId())
                    .put("personDocName", personDoc.getName());
        }
        throw new RException(InternationUtils.getInternationalMsg("SAVE_FAILED"));
    }

    @Override
    public R update(BdPersonDocDO personDoc) {
        if (StringUtils.isNotBlank(personDoc.getName())) {
            personDoc.setPinYin(PinyinUtils.toHanyuPinyin(personDoc.getName()));
        }
        personDoc.setUpdateTime(LocalDateTime.now());
        if (updateById(personDoc)) {

            return R.ok(InternationUtils.getInternationalMsg("UPDATE_SUCCESS"));
        }
        throw new RException(InternationUtils.getInternationalMsg("UPDATE_FAILED"));
    }

    @Override
    public R delete(List<String> ids) {
        if (org.apache.commons.collections.CollectionUtils.isNotEmpty(ids)) {
            List<BdPersonDocDO> list = list(new QueryWrapper<BdPersonDocDO>().eq("is_delete", 0)
                    .in("id", ids)
                    .select("name", "group_id"));
            if (list.size() > 0) {
                if (this.update(new BdPersonDocDO(), new UpdateWrapper<BdPersonDocDO>().setSql("code=CONCAT(CONCAT" +
                        "('del-', SUBSTR(id,1,4)),code)").set("is_delete", 1).in("id", ids))) {
                    // 删除用户关系
                    if (sysUserService.count(new QueryWrapper<SysUserDO>().eq("is_delete", 0).in("person_doc_id", ids))
                            > 0) {
                        if (!sysUserService.update(new SysUserDO(), new UpdateWrapper<SysUserDO>().in("person_doc_id",
                                ids).setSql("person_doc_id=null"))) {
                            throw new RException(InternationUtils.getInternationalMsg("DELETE_FAILED"));
                        }
                    }
                        return R.ok(InternationUtils.getInternationalMsg("DELETE_SUCCESS"));

                }
            } else {
                throw new RException(InternationUtils.getInternationalMsg("EMPTY_ARG"));
            }
            throw new RException(InternationUtils.getInternationalMsg("DELETE_FAILED"));
        }
        throw new RException(InternationUtils.getInternationalMsg("EMPTY_ARG_WITH_PARAM", "人员主键"));
    }
}

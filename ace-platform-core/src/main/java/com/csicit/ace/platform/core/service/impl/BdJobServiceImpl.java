package com.csicit.ace.platform.core.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.csicit.ace.common.exception.RException;
import com.csicit.ace.common.pojo.domain.BdJobDO;
import com.csicit.ace.common.utils.internationalization.InternationUtils;
import com.csicit.ace.data.persistent.mapper.BdJobMappper;
import com.csicit.ace.common.enums.AuditLogType;
import com.csicit.ace.common.enums.AuditLogTypeDO;
import com.csicit.ace.data.persistent.service.impl.BaseServiceImpl;
import com.csicit.ace.platform.core.service.BdJobService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 职务称谓表 实例对象访问接口实现
 *
 * @author wangzimin
 * @version V1.0
 * @date 2019/9/28 9:08
 */

@Service("bdJobService")
public class BdJobServiceImpl extends BaseServiceImpl<BdJobMappper, BdJobDO> implements BdJobService {

    @Override
    public boolean saveJob(BdJobDO jobDO) {
        if (StringUtils.isAnyBlank(jobDO.getName(), jobDO.getGroupId())) {
            throw new RException(InternationUtils.getInternationalMsg("EMPTY_ARG"));
        }
        Integer sortIndex = jobDO.getSortIndex();
        int count = count(new QueryWrapper<BdJobDO>().eq("GROUP_ID", jobDO.getGroupId()).eq("SORT_INDEX", sortIndex));
        if (count > 0) {
            throw new RException(InternationUtils.getInternationalMsg("SORT_INDEX_EXIST"));
        }
        if (save(jobDO)) {
            if (sysAuditLogService.saveLog(new AuditLogTypeDO(AuditLogType.success, "新增"), "新增职务称谓","新增职务称谓："+ jobDO.getName())) {
                return true;
            } else {
                throw new RException(InternationUtils.getInternationalMsg("UNKNOWN_EXCEPTION"));
            }
        }
        return false;
    }

    @Override
    public boolean updateJob(BdJobDO jobDO) {
        BdJobDO one = getOne(new QueryWrapper<BdJobDO>().eq("id", jobDO.getId()));
        if (!Objects.equals(one.getSortIndex(), jobDO.getSortIndex())) {
            int count = count(new QueryWrapper<BdJobDO>().eq("GROUP_ID", jobDO.getGroupId()).eq("sort_index", jobDO.getSortIndex()));
            if (count > 0) {
                throw new RException(InternationUtils.getInternationalMsg("SORT_INDEX_EXIST"));
            }
        }
        if (updateById(jobDO)) {
            if (sysAuditLogService.saveLog(new AuditLogTypeDO(AuditLogType.warning, "更新"), "修改职务称谓", "修改职务称谓："+ jobDO.getName())) {
                return true;
            } else {
                throw new RException(InternationUtils.getInternationalMsg("UNKNOWN_EXCEPTION"));
            }
        }
        return false;
    }

    @Override
    public boolean deleteJob(String[] ids) {
        if (ids.length > 0) {
            List<BdJobDO> list = list(new QueryWrapper<BdJobDO>().in("id", ids));
            if (removeByIds(Arrays.asList(ids))) {
                if (sysAuditLogService.saveLog(new AuditLogTypeDO(AuditLogType.danger, "删除"), "删除职务称谓","删除职务称谓："+ list.stream().map
                        (BdJobDO::getName).collect(Collectors.toList()))) {
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

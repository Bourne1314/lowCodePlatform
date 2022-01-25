package com.csicit.ace.webservice.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.csicit.ace.common.exception.RException;
import com.csicit.ace.common.pojo.domain.BdJobDO;
import com.csicit.ace.common.utils.internationalization.InternationUtils;
import com.csicit.ace.webservice.mapper.BdJobMappper;
import com.csicit.ace.webservice.service.BdJobService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

/**
 * 职务称谓表 实例对象访问接口实现
 *
 * @author wangzimin
 * @version V1.0
 * @date 2019/9/28 9:08
 */

@Service("bdJobService")
public class BdJobServiceImpl extends ServiceImpl<BdJobMappper, BdJobDO> implements BdJobService {

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
            return true;

        }
        return false;
    }

    @Override
    public boolean updateJob(BdJobDO jobDO) {
        BdJobDO one = getOne(new QueryWrapper<BdJobDO>().eq("id", jobDO.getId()));
        if (!Objects.equals(one.getSortIndex(), jobDO.getSortIndex())) {
            int count = count(new QueryWrapper<BdJobDO>().eq("GROUP_ID", jobDO.getGroupId()).eq("sort_index", jobDO
                    .getSortIndex()));
            if (count > 0) {
                throw new RException(InternationUtils.getInternationalMsg("SORT_INDEX_EXIST"));
            }
        }
        if (updateById(jobDO)) {
            return true;
        }
        return false;
    }

    @Override
    public boolean deleteJob(String[] ids) {
        if (ids.length > 0) {
            List<BdJobDO> list = list(new QueryWrapper<BdJobDO>().in("id", ids));
            if (org.apache.commons.collections.CollectionUtils.isNotEmpty(list)) {
                if (removeByIds(Arrays.asList(ids))) {
                    return true;
                }
                return false;
            }
        }
        return true;
    }
}

package com.csicit.ace.dev.service.impl;

import com.csicit.ace.common.pojo.domain.dev.ProOutputParamsDO;
import com.csicit.ace.data.persistent.mapper.ProOutputParamsMapper;
import com.csicit.ace.data.persistent.service.impl.BaseServiceImpl;
import com.csicit.ace.dev.service.ProOutputParamsService;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 实体模型 实例对象访问接口实现
 *
 * @author zuog
 * @date 2019/11/25 11:12
 */
@Service("proOutputParamsService")
public class ProOutputParamsServiceImpl extends BaseServiceImpl<ProOutputParamsMapper, ProOutputParamsDO>
        implements
        ProOutputParamsService {

    /**
     * 新增出参
     *
     * @param instance
     * @return boolean
     * @author zuogang
     * @date 2019/12/23 15:22
     */
    @Override
    public boolean addOutputParams(ProOutputParamsDO instance) {
        if (!save(instance)) {
            return false;
        }
        return true;

    }

    /**
     * 修改出参
     *
     * @param instance
     * @return boolean
     * @author zuogang
     * @date 2019/12/23 15:22
     */
    @Override
    public boolean updOutputParams(ProOutputParamsDO instance) {
        if (!updateById(instance)) {
            return false;
        }
        return true;
    }

    /**
     * 删除出参
     *
     * @param ids
     * @return boolean
     * @author zuogang
     * @date 2019/12/23 15:22
     */
    @Override
    public boolean delOutputParams(List<String> ids) {
        if (CollectionUtils.isEmpty(ids)) {
            return true;
        }
        // 删除
        if (!removeByIds(ids)) {
            return false;
        }
        return true;
    }
}

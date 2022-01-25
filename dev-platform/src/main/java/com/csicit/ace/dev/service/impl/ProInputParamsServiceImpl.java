package com.csicit.ace.dev.service.impl;

import com.csicit.ace.common.pojo.domain.dev.ProInputParamsDO;
import com.csicit.ace.data.persistent.mapper.ProInputParamsMapper;
import com.csicit.ace.data.persistent.service.impl.BaseServiceImpl;
import com.csicit.ace.dev.service.ProInputParamsService;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 实体模型 实例对象访问接口实现
 *
 * @author zuog
 * @date 2019/11/25 11:12
 */
@Service("proInputParamsService")
public class ProInputParamsServiceImpl extends BaseServiceImpl<ProInputParamsMapper, ProInputParamsDO>
        implements
        ProInputParamsService {

    /**
     * 新增入参
     *
     * @param instance
     * @return boolean
     * @author zuogang
     * @date 2019/12/23 15:22
     */
    @Override
    public boolean addInputParams(ProInputParamsDO instance) {
        if (!save(instance)) {
            return false;
        }
        return true;

    }

    /**
     * 修改入参
     *
     * @param instance
     * @return boolean
     * @author zuogang
     * @date 2019/12/23 15:22
     */
    @Override
    public boolean updInputParams(ProInputParamsDO instance) {
        if (!updateById(instance)) {
            return false;
        }
        return true;
    }

    /**
     * 删除入参
     *
     * @param ids
     * @return boolean
     * @author zuogang
     * @date 2019/12/23 15:22
     */
    @Override
    public boolean delInputParams(List<String> ids) {
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

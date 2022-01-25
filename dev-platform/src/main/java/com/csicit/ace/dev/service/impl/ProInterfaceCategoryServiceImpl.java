package com.csicit.ace.dev.service.impl;

import com.csicit.ace.common.pojo.domain.dev.ProInterfaceCategoryDO;
import com.csicit.ace.common.utils.UuidUtils;
import com.csicit.ace.data.persistent.mapper.ProInterfaceCategoryMapper;
import com.csicit.ace.data.persistent.service.impl.BaseServiceImpl;
import com.csicit.ace.dev.service.ProInterfaceCategoryService;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 实体模型 实例对象访问接口实现
 *
 * @author zuog
 * @date 2019/11/25 11:12
 */
@Service("proInterfaceCategoryService")
public class ProInterfaceCategoryServiceImpl extends BaseServiceImpl<ProInterfaceCategoryMapper, ProInterfaceCategoryDO>
        implements ProInterfaceCategoryService {


    /**
     * 新增接口类别
     *
     * @param instance
     * @return boolean
     * @author zuogang
     * @date 2019/12/23 15:22
     */
    @Override
    public boolean addInterfaceCategory(ProInterfaceCategoryDO instance) {
        instance.setCreateTime(LocalDateTime.now());
        instance.setId(UuidUtils.createUUID());

        if (!save(instance)) {
            return false;
        }

        return true;

    }

    /**
     * 修改接口类别
     *
     * @param instance
     * @return boolean
     * @author zuogang
     * @date 2019/12/23 15:22
     */
    @Override
    public boolean updInterfaceCategory(ProInterfaceCategoryDO instance) {

        if (!updateById(instance)) {
            return false;
        }
        return true;
    }

    /**
     * 删除接口类别
     *
     * @param ids
     * @return boolean
     * @author zuogang
     * @date 2019/12/23 15:22
     */
    @Override
    public boolean delInterfaceCategory(List<String> ids) {
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

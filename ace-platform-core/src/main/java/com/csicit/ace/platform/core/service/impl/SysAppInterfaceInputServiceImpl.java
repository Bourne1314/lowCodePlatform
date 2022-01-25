package com.csicit.ace.platform.core.service.impl;

import com.csicit.ace.common.pojo.domain.SysAppInterfaceInputDO;
import com.csicit.ace.data.persistent.mapper.SysAppInterfaceInputMapper;
import com.csicit.ace.data.persistent.service.impl.BaseServiceImpl;
import com.csicit.ace.platform.core.service.SysAppInterfaceInputService;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Service;

import java.util.List;


/**
 * 接口sql入参信息表 实例对象访问接口实现
 *
 * @author generator
 * @version V1.0
 * @date 2020-06-03 09:05:20
 */
@Service("sysAppInterfaceInputService")
public class SysAppInterfaceInputServiceImpl extends BaseServiceImpl<SysAppInterfaceInputMapper, SysAppInterfaceInputDO> implements SysAppInterfaceInputService {
    /**
     * 新增入参
     *
     * @param instance
     * @return boolean
     * @author zuogang
     * @date 2019/12/23 15:22
     */
    @Override
    public boolean addInputParams(SysAppInterfaceInputDO instance) {
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
    public boolean updInputParams(SysAppInterfaceInputDO instance) {
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

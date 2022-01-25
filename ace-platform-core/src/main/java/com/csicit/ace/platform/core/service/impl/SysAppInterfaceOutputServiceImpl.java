package com.csicit.ace.platform.core.service.impl;

import com.csicit.ace.common.pojo.domain.SysAppInterfaceOutputDO;
import com.csicit.ace.data.persistent.mapper.SysAppInterfaceOutputMapper;
import com.csicit.ace.data.persistent.service.impl.BaseServiceImpl;
import com.csicit.ace.platform.core.service.SysAppInterfaceOutputService;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Service;

import java.util.List;


/**
 * 接口sql出参信息表 实例对象访问接口实现
 *
 * @author generator
 * @version V1.0
 * @date 2020-06-03 09:05:33
 */
@Service("sysAppInterfaceOutputService")
public class SysAppInterfaceOutputServiceImpl extends BaseServiceImpl<SysAppInterfaceOutputMapper, SysAppInterfaceOutputDO> implements SysAppInterfaceOutputService {
    /**
     * 新增出参
     *
     * @param instance
     * @return boolean
     * @author zuogang
     * @date 2019/12/23 15:22
     */
    @Override
    public boolean addOutputParams(SysAppInterfaceOutputDO instance) {
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
    public boolean updOutputParams(SysAppInterfaceOutputDO instance) {
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

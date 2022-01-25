package com.csicit.ace.platform.core.service;

import com.csicit.ace.common.pojo.domain.SysAppInterfaceInputDO;
import com.csicit.ace.dbplus.service.IBaseService;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 接口sql入参信息表 实例对象访问接口
 *
 * @author generator
 * @date 2020-06-03 09:05:20
 * @version V1.0
 */
@Transactional
public interface SysAppInterfaceInputService extends IBaseService<SysAppInterfaceInputDO> {
    /**
     * 新增入参
     *
     * @param instance
     * @return boolean
     * @author zuogang
     * @date 2019/12/23 15:22
     */
    boolean addInputParams(SysAppInterfaceInputDO instance);
    /**
     * 修改入参
     *
     * @param instance
     * @return boolean
     * @author zuogang
     * @date 2019/12/23 15:22
     */
    boolean updInputParams(SysAppInterfaceInputDO instance);
    /**
     * 删除入参
     *
     * @param ids
     * @return boolean
     * @author zuogang
     * @date 2019/12/23 15:22
     */
    boolean delInputParams(List<String> ids);
}

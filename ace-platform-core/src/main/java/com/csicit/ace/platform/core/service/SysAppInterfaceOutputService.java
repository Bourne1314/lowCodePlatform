package com.csicit.ace.platform.core.service;

import com.csicit.ace.common.pojo.domain.SysAppInterfaceOutputDO;
import com.csicit.ace.dbplus.service.IBaseService;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 接口sql出参信息表 实例对象访问接口
 *
 * @author generator
 * @date 2020-06-03 09:05:33
 * @version V1.0
 */
@Transactional
public interface SysAppInterfaceOutputService extends IBaseService<SysAppInterfaceOutputDO> {
    /**
     * 新增出参
     *
     * @param instance
     * @return boolean
     * @author zuogang
     * @date 2019/12/23 15:22
     */
    boolean addOutputParams(SysAppInterfaceOutputDO instance);
    /**
     * 修改出参
     *
     * @param instance
     * @return boolean
     * @author zuogang
     * @date 2019/12/23 15:22
     */
    boolean updOutputParams(SysAppInterfaceOutputDO instance);
    /**
     * 删除出参
     *
     * @param ids
     * @return boolean
     * @author zuogang
     * @date 2019/12/23 15:22
     */
    boolean delOutputParams(List<String> ids);
}

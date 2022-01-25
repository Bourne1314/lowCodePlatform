package com.csicit.ace.platform.core.service;

import com.csicit.ace.common.pojo.domain.SysAppInterfaceDO;
import com.csicit.ace.common.utils.server.R;
import com.csicit.ace.dbplus.service.IBaseService;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 应用接口信息表 实例对象访问接口
 *
 * @author generator
 * @date 2020-06-03 09:03:59
 * @version V1.0
 */
@Transactional
public interface SysAppInterfaceService extends IBaseService<SysAppInterfaceDO> {
    /**
     * 新增接口
     *
     * @param instance
     * @return boolean
     * @author zuogang
     * @date 2019/12/23 15:22
     */
    boolean addInterface(SysAppInterfaceDO instance);

    /**
     * 修改接口
     *
     * @param instance
     * @return boolean
     * @author zuogang
     * @date 2019/12/23 15:22
     */
    boolean updInterface(SysAppInterfaceDO instance);

    /**
     * 删除接口
     *
     * @param ids
     * @return boolean
     * @author zuogang
     * @date 2019/12/23 15:22
     */
    boolean delInterface(List<String> ids);

    /**
     * sql代码测试检查
     *
     * @param instance
     * @return R
     * @author zuogang
     * @date 2019/12/23 15:22
     */
    R sqlCodeCheck(SysAppInterfaceDO instance);
}

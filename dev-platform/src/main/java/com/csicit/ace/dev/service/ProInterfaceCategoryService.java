package com.csicit.ace.dev.service;

import com.csicit.ace.common.pojo.domain.dev.ProInterfaceCategoryDO;
import com.csicit.ace.dbplus.service.IBaseService;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 接口类别 实例对象访问接口
 *
 * @author zuog
 * @date 2019/11/25 11:10
 */
@Transactional
public interface ProInterfaceCategoryService extends IBaseService<ProInterfaceCategoryDO> {

    /**
     * 新增接口类别
     *
     * @param instance
     * @return boolean
     * @author zuogang
     * @date 2019/12/23 15:22
     */
    boolean addInterfaceCategory(ProInterfaceCategoryDO instance);

    /**
     * 修改接口类别
     *
     * @param instance
     * @return boolean
     * @author zuogang
     * @date 2019/12/23 15:22
     */
    boolean updInterfaceCategory(ProInterfaceCategoryDO instance);

    /**
     * 删除接口类别
     *
     * @param ids
     * @return boolean
     * @author zuogang
     * @date 2019/12/23 15:22
     */
    boolean delInterfaceCategory(List<String> ids);
}

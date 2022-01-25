package com.csicit.ace.dev.service;

import com.csicit.ace.common.pojo.domain.dev.ProOutputParamsDO;
import com.csicit.ace.dbplus.service.IBaseService;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 实体模型 实例对象访问接口
 *
 * @author zuog
 * @date 2019/11/25 11:10
 */
@Transactional
public interface ProOutputParamsService extends IBaseService<ProOutputParamsDO> {

    /**
     * 新增出参
     *
     * @param instance
     * @return boolean
     * @author zuogang
     * @date 2019/12/23 15:22
     */
    boolean addOutputParams(ProOutputParamsDO instance);
    /**
     * 修改出参
     *
     * @param instance
     * @return boolean
     * @author zuogang
     * @date 2019/12/23 15:22
     */
    boolean updOutputParams(ProOutputParamsDO instance);
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

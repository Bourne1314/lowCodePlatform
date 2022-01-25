package com.csicit.ace.dev.service;

import com.csicit.ace.common.pojo.domain.dev.ProInputParamsDO;
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
public interface ProInputParamsService extends IBaseService<ProInputParamsDO> {

    /**
     * 新增入参
     *
     * @param instance
     * @return boolean
     * @author zuogang
     * @date 2019/12/23 15:22
     */
    boolean addInputParams(ProInputParamsDO instance);
    /**
     * 修改入参
     *
     * @param instance
     * @return boolean
     * @author zuogang
     * @date 2019/12/23 15:22
     */
    boolean updInputParams(ProInputParamsDO instance);
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

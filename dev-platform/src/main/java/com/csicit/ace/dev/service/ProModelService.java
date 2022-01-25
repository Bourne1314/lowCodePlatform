package com.csicit.ace.dev.service;

import com.csicit.ace.common.pojo.domain.dev.ProModelDO;
import com.csicit.ace.dbplus.service.IBaseService;
import org.springframework.transaction.annotation.Transactional;

/**
 * 实体模型 实例对象访问接口
 *
 * @author zuog
 * @date 2019/11/25 11:10
 */
@Transactional
public interface ProModelService extends IBaseService<ProModelDO> {

    /**
     * 新增实体模型
     *
     * @param instance
     * @return boolean
     * @author zuogang
     * @date 2019/12/23 15:22
     */
    boolean addModel(ProModelDO instance);
    /**
     * 修改实体模型
     *
     * @param instance
     * @return boolean
     * @author zuogang
     * @date 2019/12/23 15:22
     */
    boolean updModel(ProModelDO instance);
    /**
     * 删除实体模型
     *
     * @param id
     * @return boolean
     * @author zuogang
     * @date 2019/12/23 15:22
     */
    boolean delModel(String id);
}

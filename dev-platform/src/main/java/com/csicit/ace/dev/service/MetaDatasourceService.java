package com.csicit.ace.dev.service;

import com.csicit.ace.common.pojo.domain.dev.MetaDatasourceDO;
import com.csicit.ace.dbplus.service.IBaseService;
import org.springframework.transaction.annotation.Transactional;

/**
 * 数据源 实例对象访问接口
 *
 * @author shanwj
 * @date 2019-11-04 14:48:24
 * @version V1.0
 */
@Transactional
public interface MetaDatasourceService extends IBaseService<MetaDatasourceDO> {

    /**
     * 同步数据模型
     * @param id
     * @return
     */
    boolean synDataModel(String id);

    boolean synDataModel1(String id);
}

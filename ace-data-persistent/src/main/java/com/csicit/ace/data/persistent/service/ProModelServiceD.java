package com.csicit.ace.data.persistent.service;

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
public interface ProModelServiceD extends IBaseService<ProModelDO> {

}

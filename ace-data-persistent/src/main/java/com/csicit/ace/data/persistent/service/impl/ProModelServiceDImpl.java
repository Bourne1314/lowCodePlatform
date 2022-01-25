package com.csicit.ace.data.persistent.service.impl;

import com.csicit.ace.common.pojo.domain.dev.ProModelDO;
import com.csicit.ace.data.persistent.mapper.ProModelMapper;
import com.csicit.ace.data.persistent.service.ProModelServiceD;
import org.springframework.stereotype.Service;

/**
 * 实体模型 实例对象访问接口实现
 *
 * @author zuog
 * @date 2019/11/25 11:12
 */
@Service("proModelServiceD")
public class ProModelServiceDImpl extends BaseServiceImpl<ProModelMapper, ProModelDO>
        implements ProModelServiceD {

}

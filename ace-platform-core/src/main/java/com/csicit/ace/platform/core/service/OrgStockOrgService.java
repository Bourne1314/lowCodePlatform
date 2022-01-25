package com.csicit.ace.platform.core.service;

import com.csicit.ace.common.pojo.domain.OrgStockOrgDO;
import com.csicit.ace.dbplus.service.IBaseService;
import org.springframework.transaction.annotation.Transactional;

/**
 * 组织-库存组织 实例对象访问接口
 *
 * @author generator
 * @date 2019-04-15 17:18:01
 * @version V1.0
 */
@Transactional
public interface OrgStockOrgService extends IBaseService<OrgStockOrgDO>, BaseOrgService {

}

package com.csicit.ace.platform.core.service;

import com.csicit.ace.common.pojo.domain.OrgMaintainOrgDO;
import com.csicit.ace.dbplus.service.IBaseService;
import org.springframework.transaction.annotation.Transactional;

/**
 * 组织-维修组织 实例对象访问接口
 *
 * @author generator
 * @date 2019-04-15 17:16:45
 * @version V1.0
 */
@Transactional
public interface OrgMaintainOrgService extends IBaseService<OrgMaintainOrgDO>, BaseOrgService {

}

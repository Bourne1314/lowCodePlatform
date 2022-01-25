package com.csicit.ace.dev.service;

import com.csicit.ace.common.pojo.domain.OrgOrganizationVDO;
import com.csicit.ace.dbplus.service.IBaseService;
import org.springframework.transaction.annotation.Transactional;

/**
 * 组织-组织版本 实例对象访问接口
 *
 * @author shanwj
 * @version v1.0
 * @date 2019-04-11 10:36:11
 */
@Transactional
public interface OrgOrganizationVServiceD extends IBaseService<OrgOrganizationVDO> {

}

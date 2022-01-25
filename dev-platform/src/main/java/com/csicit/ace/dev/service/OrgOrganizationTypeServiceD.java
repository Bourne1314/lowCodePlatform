package com.csicit.ace.dev.service;

import com.csicit.ace.common.pojo.domain.OrgOrganizationTypeDO;
import com.csicit.ace.dbplus.service.IBaseService;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

/**
 *  组织-组织-职能 实例对象访问接口
 * @author shanwj
 * @date 2019-04-11 10:37:15
 * @version  v1.0
 */
@Transactional
public interface OrgOrganizationTypeServiceD extends IBaseService<OrgOrganizationTypeDO> {

}

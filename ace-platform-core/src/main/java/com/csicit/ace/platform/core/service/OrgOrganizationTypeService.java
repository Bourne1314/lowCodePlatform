package com.csicit.ace.platform.core.service;

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
public interface OrgOrganizationTypeService extends IBaseService<OrgOrganizationTypeDO> {

    /**
     * 通过业务单元ID 获取对应的type
     * 集团:group  部门:deparment .....
     * @param orgIds
     * @return 
     * @author yansiyang
     * @date 2019/5/17 10:18
     */
    Map<String, List<String>> getOrgType(List<String> orgIds);
}

package com.csicit.ace.platform.core.service.impl;

import com.csicit.ace.common.pojo.domain.OrgOrganizationDO;
import com.csicit.ace.common.pojo.domain.OrgPurchaseOrgDO;
import com.csicit.ace.data.persistent.mapper.OrgPurchaseOrgMapper;
import com.csicit.ace.data.persistent.service.impl.BaseServiceImpl;
import com.csicit.ace.platform.core.service.OrgPurchaseOrgService;
import org.springframework.stereotype.Service;

import java.util.Map;


/**
 * 组织-采购组织 实例对象访问接口实现
 *
 * @author generator
 * @version V1.0
 * @date 2019-04-15 17:17:40
 */
@Service("orgPurchaseOrgService")
public class OrgPurchaseOrgServiceImpl extends BaseServiceImpl<OrgPurchaseOrgMapper, OrgPurchaseOrgDO> implements
        OrgPurchaseOrgService {

    /**
     * 保存业务单元
     *
     * @param orgOrganizationDO
     * @return boolean
     * @author zuogang
     * @date 2019/4/22 15:14
     */
    @Override
    public boolean saveOrg(OrgOrganizationDO orgOrganizationDO) {
        return false;
    }

    /**
     * 修改业务单元
     *
     * @param orgOrganizationDO
     * @return boolean
     * @author zuogang
     * @date 2019/4/22 15:14
     */
    @Override
    public boolean updateOrg(OrgOrganizationDO orgOrganizationDO) {
        return false;
    }

    /**
     * 删除业务单元
     *
     * @param map
     * @return boolean
     * @author zuogang
     * @date 2019/4/22 15:14
     */
    @Override
    public boolean deleteOrg(Map<String, Object> map) {
        return false;
    }

    /**
     * 版本化业务单元
     *
     * @param map
     * @return boolean
     * @author zuogang
     * @date 2019/4/22 15:15
     */
    @Override
    public boolean versionOrg(Map<String, String> map) {
        return false;
    }
}

package com.csicit.ace.platform.core.service.impl;

import com.csicit.ace.common.pojo.domain.OrgOrganizationDO;
import com.csicit.ace.common.pojo.domain.OrgSalesOrgDO;
import com.csicit.ace.data.persistent.mapper.OrgSalesOrgMapper;
import com.csicit.ace.data.persistent.service.impl.BaseServiceImpl;
import com.csicit.ace.platform.core.service.OrgSalesOrgService;
import org.springframework.stereotype.Service;

import java.util.Map;


/**
 * 组织-销售组织 实例对象访问接口实现
 *
 * @author generator
 * @version V1.0
 * @date 2019-04-15 17:17:51
 */
@Service("orgSalesOrgService")
public class OrgSalesOrgServiceImpl extends BaseServiceImpl<OrgSalesOrgMapper, OrgSalesOrgDO> implements
        OrgSalesOrgService {
    /**
     * 保存业务单元
     *
     * @param orgOrganizationDO
     * @return boolean
     * @author zuogang
     * @date 2019/4/22 15:15
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
     * @date 2019/4/22 15:15
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
     * @date 2019/4/22 15:15
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

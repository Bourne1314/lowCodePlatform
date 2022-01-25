package com.csicit.ace.webservice.service;

import com.csicit.ace.common.pojo.domain.OrgOrganizationDO;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

/**
 * 业务单元基础 实例对象访问接口
 *
 * @author yansiyang
 * @version V1.0
 * @date 2019/4/18 10:14
 */
@Transactional
public interface BaseOrgService {

    /**
     * 保存业务单元
     * @param orgOrganizationDO
     * @return 
     * @author yansiyang
     * @date 2019/4/18 10:16
     */
    boolean saveOrg(OrgOrganizationDO orgOrganizationDO);

    /**
     * 修改业务单元
     * @param orgOrganizationDO
     * @return
     * @author yansiyang
     * @date 2019/4/18 10:16
     */
    boolean updateOrg(OrgOrganizationDO orgOrganizationDO);

    /**
     * 删除业务单元
     * @param map
     * @return
     * @author yansiyang
     * @date 2019/4/18 10:16
     */
    boolean deleteOrg(Map<String, Object> map);

    /**
     * 版本化业务单元
     * @param map
     * @return
     * @author yansiyang
     * @date 2019/4/18 10:16
     */
    boolean versionOrg(Map<String, String> map);
}

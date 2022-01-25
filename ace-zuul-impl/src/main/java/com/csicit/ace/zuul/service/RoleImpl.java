package com.csicit.ace.zuul.service;

import com.csicit.ace.common.pojo.domain.SysRoleDO;
import com.csicit.ace.common.utils.StringUtils;
import com.csicit.ace.interfaces.service.IRole;
import org.springframework.stereotype.Service;

/**
 * @author FourLeaves
 * @version V1.0
 * @date 2020/12/3 10:16
 */
@Service("role")
public class RoleImpl extends BaseImpl implements IRole {
    @Override
    public SysRoleDO getRoleById(String id) {
        if (StringUtils.isNotBlank(id)) {
            return clientService.getRoleById(id);
        }
        return null;
    }

    @Override
    public SysRoleDO getRoleByCode(String code) {
        if (StringUtils.isNotBlank(code)) {
            return clientService.getRoleByCode(code, securityUtils.getAppName());
        }
        return null;
    }
}

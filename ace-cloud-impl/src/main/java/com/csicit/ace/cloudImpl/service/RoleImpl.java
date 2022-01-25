package com.csicit.ace.cloudImpl.service;

import com.csicit.ace.common.pojo.domain.SysRoleDO;
import com.csicit.ace.common.utils.StringUtils;
import com.csicit.ace.interfaces.service.IRole;
import org.springframework.stereotype.Service;

/**
 * 角色接口
 *
 * @author FourLeaves
 * @version V1.0
 * @date 2020/12/1 15:28
 */
@Service("role")
public class RoleImpl extends BaseImpl implements IRole {

    @Override
    public SysRoleDO getRoleById(String id) {
        if (StringUtils.isNotBlank(id)) {
            return gatewayService.getRoleById(id);
        }
        return null;
    }

    @Override
    public SysRoleDO getRoleByCode(String code) {
        if (StringUtils.isNotBlank(code)) {
            return gatewayService.getRoleByCode(code, appName);
        }
        return null;
    }
}

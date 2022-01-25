package com.csicit.ace.cloudImpl.service;

import com.csicit.ace.common.enums.AuditLogType;
import com.csicit.ace.common.enums.AuditLogTypeDO;
import com.csicit.ace.common.enums.AuditLogTypeDefault;
import com.csicit.ace.common.exception.RException;
import com.csicit.ace.common.utils.StringUtils;
import com.csicit.ace.common.utils.internationalization.InternationUtils;
import com.csicit.ace.interfaces.service.IAuditLog;
import org.springframework.stereotype.Service;

/**
 * @author FourLeaves
 * @version V1.0
 * @date 2020/7/10 17:45
 */
@Service
public class AuditLogImpl extends BaseImpl implements IAuditLog {

    @Override
    public boolean saveLog(String title, Object opContent) {
        return saveLog(new AuditLogTypeDO(), title, opContent);
    }

    @Override
    public boolean saveLog(String title, Object opContent, String type) {
        return saveLog(new AuditLogTypeDO(type), title, opContent);
    }

    @Override
    public boolean saveLog(String title, Object opContent, String type, AuditLogType tag) {
        return saveLog(new AuditLogTypeDO(tag, type), title, opContent);
    }

    @Override
    public boolean saveLog(AuditLogTypeDefault type, String title, Object opContent) {
        return saveLog(new AuditLogTypeDO(AuditLogType.getTypeByTag(type.getTag()), type.getType()), title, opContent);
    }

    @Override
    public boolean saveLog(AuditLogTypeDO type, String title, Object opContent) {
        return saveLog(type, title, opContent, securityUtils.getCurrentGroupId(), securityUtils.getAppName());
    }


    @Override
    public boolean saveLog(AuditLogTypeDO type, String title, Object opContent, String groupId, String appId) {
        if (StringUtils.isBlank(groupId)) {
            groupId = securityUtils.getCurrentGroupId();
        }
        if (StringUtils.isBlank(appId)) {
            appId = securityUtils.getAppName();
        }
        if (opContent == null || StringUtils.isBlank(title) || type == null) {
            throw new RException(InternationUtils.getInternationalMsg("EMPTY_ARG_WITH_PARAM", "内容"));
        }
        return gatewayService.saveLog(type, title, opContent, groupId, appId);
    }

}

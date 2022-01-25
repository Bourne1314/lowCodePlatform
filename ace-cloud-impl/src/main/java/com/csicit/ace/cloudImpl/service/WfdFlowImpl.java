package com.csicit.ace.cloudImpl.service;

import com.alibaba.fastjson.JSONObject;
import com.csicit.ace.common.pojo.domain.SysAuthDO;
import com.csicit.ace.common.pojo.domain.SysUserDO;
import com.csicit.ace.common.utils.StringUtils;
import com.csicit.ace.interfaces.service.IWfdFlow;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author yansiyang
 * @version V1.0
 * @date 2019/10/15 8:29
 */
@Service
public class WfdFlowImpl extends BaseImpl implements IWfdFlow {

    @Override
    public List<SysUserDO> getSomeUsers(String code, List<String> ids, Map<String, Object> map) {
        if (StringUtils.isNotBlank(code) && !org.apache.commons.collections.CollectionUtils.isEmpty(ids)) {
            map.put("code", code);
            map.put("ids", JSONObject.toJSONString(ids));
            return gatewayService.getSomeUsers(map);
        }
        return new ArrayList<>();
    }

    @Override
    public List<SysAuthDO> getMixAuth(String appId, String userId) {
        return gatewayService.getMixAuth(appId, userId);
    }

    @Override
    public boolean setAppFLowMenu(String token) {
        Map<String, String> params = new HashMap<>();
        params.put("appId", appName);
        params.put("token", token);
        try {
            return gatewayService.setAppFLowMenu(params);
        } catch (Exception e) {
            return false;
        }

    }
}

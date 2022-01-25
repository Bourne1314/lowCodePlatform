package com.csicit.ace.zuul.service;

import com.csicit.ace.common.constant.Constants;
import com.csicit.ace.interfaces.service.IAceCode;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * @author shanwj
 * @date 2020/7/3 10:43
 */
@Service("aceCode")
public class AceCodeImpl extends BaseImpl  implements IAceCode {
    @Override
    public String getNextNum(String appId, String seqBizTag) {
        return getNextNum(appId,seqBizTag,Constants.ACE_CODE_NULL_PART_VALUE_TAG);
    }

    @Override
    public String getNextNum(String appId, String seqBizTag, String partValueTag) {
        return clientService.getNextNum(appId,seqBizTag,partValueTag);
    }

    @Override
    public String getTemplateCode(String appId, String templateKey) {
        return getTemplateCode(appId,templateKey,new HashMap<>(16));
    }

    @Override
    public String getTemplateCode(String appId, String templateKey, Map<String, String> params) {
        params.put(Constants.ACE_CODE_PARAM_KEY_APPID,appId);
        params.put(Constants.PACE_CODE_ARAM_KEY_TEMPLATEKEY,templateKey);
        return clientService.getTemplateCode(params);
    }
}

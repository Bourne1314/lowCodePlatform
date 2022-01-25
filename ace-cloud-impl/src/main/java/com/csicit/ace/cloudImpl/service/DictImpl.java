package com.csicit.ace.cloudImpl.service;

import com.csicit.ace.common.pojo.domain.SysDictValueDO;
import com.csicit.ace.common.utils.StringUtils;
import com.csicit.ace.interfaces.service.IDict;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author shanwj
 * @date 2019/5/28 11:07
 */
@Service("dict")
public class DictImpl extends BaseImpl implements IDict {

    @Override
    public List<SysDictValueDO> getValue(String type) {
        if (StringUtils.isNotBlank(type)) {
            Map<String, Object> map = new HashMap<>();
            map.put("type", type);
            map.put("appId", appName);
            return gatewayService.getDictValue(map);
        }
        return new ArrayList<>();
    }
}

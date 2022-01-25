package com.csicit.ace.platform.core.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.csicit.ace.common.pojo.domain.OrgOrganizationTypeDO;
import com.csicit.ace.common.utils.JsonUtils;
import com.csicit.ace.data.persistent.mapper.OrgOrganizationTypeMapper;
import com.csicit.ace.data.persistent.service.impl.BaseServiceImpl;
import com.csicit.ace.platform.core.service.OrgOrganizationTypeService;
import org.springframework.stereotype.Service;

import java.util.*;


/**
 * 组织-组织-职能 实例对象访问接口实现
 *
 * @author generator
 * @version V1.0
 * @date 2019-04-15 17:16:53
 */
@Service("orgOrganizationTypeService")
public class OrgOrganizationTypeServiceImpl extends BaseServiceImpl<OrgOrganizationTypeMapper, OrgOrganizationTypeDO>
        implements OrgOrganizationTypeService {
    @Override
    public Map<String, List<String>> getOrgType(List<String> orgIds) {
        List<OrgOrganizationTypeDO> typeList = list(new QueryWrapper<OrgOrganizationTypeDO>()
                .and(orgIds == null || orgIds.size() == 0, i -> i.eq("1", "2"))
                .in("id", orgIds));
        Map<String, List<String>> map = new HashMap<>();
        typeList.parallelStream().forEach(type -> {
            JSONObject json = JsonUtils.castObject(type, JSONObject.class);
            Set<String> keys = json.keySet();
            List<String> types = new ArrayList<>();
            keys.parallelStream().forEach(key -> {
                String value = json.getString(key);
                if (Objects.equals(value, "1") && !Objects.equals(key, "dataVersion")) {
                    if (Objects.equals(key, "isGroup")) {
                        types.add("group");
                    } else {
                        types.add(key);
                    }
                }
            });
            map.put(type.getId(), types);
        });
        return map;
    }
}

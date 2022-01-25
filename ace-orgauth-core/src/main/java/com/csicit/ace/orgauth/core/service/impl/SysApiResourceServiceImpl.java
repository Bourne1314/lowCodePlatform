package com.csicit.ace.orgauth.core.service.impl;

import com.csicit.ace.common.pojo.domain.SysApiResourceDO;
import com.csicit.ace.data.persistent.mapper.SysApiResourceMapper;
import com.csicit.ace.data.persistent.service.impl.BaseServiceImpl;
import com.csicit.ace.orgauth.core.service.SysApiResourceService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author shanwj
 * @date 2019/5/22 9:28
 */
@Service("sysApiResourceServiceO")
public class SysApiResourceServiceImpl
        extends BaseServiceImpl<SysApiResourceMapper, SysApiResourceDO> implements SysApiResourceService {
    @Override
    public boolean saveAppApi(List<SysApiResourceDO> apis) {
        if (apis.size() > 0) {
            // remove(new QueryWrapper<SysApiResourceDO>().eq("app_id",apis.get(0).getAppId()));
            return saveOrUpdateBatch(apis);
        }
        return true;
    }
}

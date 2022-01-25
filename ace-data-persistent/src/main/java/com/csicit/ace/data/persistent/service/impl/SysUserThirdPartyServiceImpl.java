package com.csicit.ace.data.persistent.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.csicit.ace.common.pojo.domain.SysUserThirdPartyDO;
import com.csicit.ace.data.persistent.mapper.SysUserThirdPartyMapper;
import com.csicit.ace.data.persistent.service.SysUserThirdPartyService;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author yansiyang
 * @version V1.0
 * @date 2019/11/25 15:26
 */
@Service
public class SysUserThirdPartyServiceImpl extends ServiceImpl<SysUserThirdPartyMapper, SysUserThirdPartyDO>
        implements SysUserThirdPartyService {
    @Override
    public List<String> getThirdAccounts(List<String> userIds, String type) {
        if (CollectionUtils.isEmpty(userIds)) {
            return null;
        }
        return list(new QueryWrapper<SysUserThirdPartyDO>()
                .eq("type", type).in("user_id", userIds))
                .stream().map(SysUserThirdPartyDO::getAccount).collect(Collectors.toList());
    }
}

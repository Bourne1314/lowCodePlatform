package com.csicit.ace.platform.core.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.csicit.ace.common.pojo.domain.SysAuthUserLvDO;
import com.csicit.ace.common.pojo.domain.SysAuthUserVDO;
import com.csicit.ace.data.persistent.mapper.SysAuthUserVMapper;
import com.csicit.ace.data.persistent.service.impl.BaseServiceImpl;
import com.csicit.ace.platform.core.service.SysAuthUserLvService;
import com.csicit.ace.platform.core.service.SysAuthUserVService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;


/**
 * 系统管理-用户授权版本历史数据 实例对象访问接口实现
 *
 * @author generator
 * @version V1.0
 * @date 2019-04-15 20:09:50
 */
@Service("sysAuthUserVService")
public class SysAuthUserVServiceImpl extends BaseServiceImpl<SysAuthUserVMapper, SysAuthUserVDO> implements
        SysAuthUserVService {

    @Autowired
    SysAuthUserLvService sysAuthUserLvService;

    @Autowired
    SysAuthUserVService sysAuthUserVService;

    /**
     * 通过userID获取该用户的权限版本历史数据
     *
     * @return
     * @author zuogang
     * @date 2019/4/23 16:57
     */
    @Override
    public Map<Integer, Object> getAuthHistoryByUserId(String appId, String userId) {

        Map<Integer, Object> authHistoryDatas = new HashMap<>();

        List<SysAuthUserLvDO> sysAuthUserLvDOS = sysAuthUserLvService.list(new QueryWrapper<SysAuthUserLvDO>()
                .eq("app_id", appId).eq("user_id", userId).orderByAsc("is_last_version"));
        if (sysAuthUserLvDOS != null && sysAuthUserLvDOS.size() > 0) {
            sysAuthUserLvDOS.stream().forEach(sysAuthUserLvDO -> {
                String lvId = sysAuthUserLvDO.getId();
                List<SysAuthUserVDO> sysAuthUserVDOS = sysAuthUserVService.list(new QueryWrapper<SysAuthUserVDO>()
                        .eq("app_id", appId).eq("lv_id", lvId));
                authHistoryDatas.put(sysAuthUserLvDO.getVersionNo(), sysAuthUserVDOS);
            });
        }
        Map<Integer, Object> resultMap = new TreeMap<>((str1, str2) -> str1.compareTo(str2));
        resultMap.putAll(authHistoryDatas);
        return resultMap;
    }
}

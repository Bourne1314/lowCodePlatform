package com.csicit.ace.platform.core.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.csicit.ace.common.pojo.domain.SysAuthRoleLvDO;
import com.csicit.ace.common.pojo.domain.SysAuthRoleVDO;
import com.csicit.ace.data.persistent.mapper.SysAuthRoleVMapper;
import com.csicit.ace.data.persistent.service.impl.BaseServiceImpl;
import com.csicit.ace.platform.core.service.SysAuthRoleLvService;
import com.csicit.ace.platform.core.service.SysAuthRoleVService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;


/**
 * 系统管理-角色授权版本 实例对象访问接口实现
 *
 * @author generator
 * @version V1.0
 * @date 2019-04-15 20:09:24
 */
@Service("sysAuthRoleVService")
public class SysAuthRoleVServiceImpl extends BaseServiceImpl<SysAuthRoleVMapper, SysAuthRoleVDO> implements
        SysAuthRoleVService {
    @Autowired
    SysAuthRoleLvService sysAuthRoleLvService;

    @Autowired
    SysAuthRoleVService sysAuthRoleVService;

    /**
     * 通过roleId获取该用户的权限版本历史数据
     *
     * @return
     * @author zuogang
     * @date 2019/4/23 16:57
     */
    @Override
    public Map<Integer, Object> getAuthHistoryByRoleId(String roleId) {
        Map<Integer, Object> authHistoryDatas = new HashMap<>();

        List<SysAuthRoleLvDO> sysAuthRoleLvDOS = sysAuthRoleLvService.list(new QueryWrapper<SysAuthRoleLvDO>()
                .eq("role_id", roleId).orderByAsc("is_last_version"));
        if (sysAuthRoleLvDOS != null && sysAuthRoleLvDOS.size() > 0) {
            sysAuthRoleLvDOS.stream().forEach(sysAuthRoleLvDO -> {
                String lvId = sysAuthRoleLvDO.getId();
                List<SysAuthRoleVDO> sysAuthRoleVDOS = sysAuthRoleVService.list(new QueryWrapper<SysAuthRoleVDO>()
                        .eq("lv_id", lvId));
                authHistoryDatas.put(sysAuthRoleLvDO.getVersionNo(), sysAuthRoleVDOS);
            });
        }
        Map<Integer, Object> resultMap = new TreeMap<>((str1, str2) -> str1.compareTo(str2));
        resultMap.putAll(authHistoryDatas);
        return resultMap;
    }
}

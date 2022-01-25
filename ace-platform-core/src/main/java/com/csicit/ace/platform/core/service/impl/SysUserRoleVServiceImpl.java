package com.csicit.ace.platform.core.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.csicit.ace.common.pojo.domain.SysUserRoleLvDO;
import com.csicit.ace.common.pojo.domain.SysUserRoleVDO;
import com.csicit.ace.data.persistent.mapper.SysUserRoleVMapper;
import com.csicit.ace.data.persistent.service.impl.BaseServiceImpl;
import com.csicit.ace.platform.core.service.SysUserRoleLvService;
import com.csicit.ace.platform.core.service.SysUserRoleVService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;


/**
 * 用户角色历史版本数据 实例对象访问接口实现
 *
 * @author generator
 * @version V1.0
 * @date 2019-10-22 19:17:41
 */
@Service("sysUserRoleVService")
public class SysUserRoleVServiceImpl extends BaseServiceImpl<SysUserRoleVMapper, SysUserRoleVDO> implements
        SysUserRoleVService {


    @Autowired
    SysUserRoleLvService sysUserRoleLvService;

    @Autowired
    SysUserRoleVService sysUserRoleVService;

    /**
     * 通过userID获取该用户的角色版本历史数据
     *
     * @return
     * @author zuogang
     * @date 2019/4/23 16:57
     */
    @Override
    public Map<Integer, Object> getRoleHistoryByUserId(String appId, String userId) {

        Map<Integer, Object> roleHistoryDatas = new HashMap<>();

        List<SysUserRoleLvDO> sysUserRoleLvDOS = sysUserRoleLvService.list(new QueryWrapper<SysUserRoleLvDO>()
                .eq("app_id", appId).eq("user_id", userId).orderByAsc("version"));
        if (sysUserRoleLvDOS != null && sysUserRoleLvDOS.size() > 0) {
            sysUserRoleLvDOS.stream().forEach(sysUserRoleLvDO -> {
                String lvId = sysUserRoleLvDO.getId();
                List<String> roleNames = sysUserRoleVService.list(new QueryWrapper<SysUserRoleVDO>()
                        .eq("lv_id", lvId).orderByAsc("role_name"))
                        .stream().map(SysUserRoleVDO::getRoleName).collect(Collectors.toList());
                roleHistoryDatas.put(sysUserRoleLvDO.getVersion(), roleNames);
            });
        }
        Map<Integer, Object> resultMap = new TreeMap<>((str1, str2) -> str1.compareTo(str2));
        resultMap.putAll(roleHistoryDatas);
        return resultMap;
    }
}

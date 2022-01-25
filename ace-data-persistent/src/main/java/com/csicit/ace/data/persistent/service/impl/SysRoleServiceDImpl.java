package com.csicit.ace.data.persistent.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.csicit.ace.common.AppUpgradeJaxb.RoleDetail;
import com.csicit.ace.common.pojo.domain.SysMenuDO;
import com.csicit.ace.common.pojo.domain.SysRoleDO;
import com.csicit.ace.common.utils.JsonUtils;
import com.csicit.ace.data.persistent.mapper.SysRoleMapper;
import com.csicit.ace.data.persistent.service.SysRoleServiceD;
import com.csicit.ace.data.persistent.utils.CommonUtils;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * 角色管理 实例对象访问接口实现
 *
 * @author zuogang
 * @version V1.0
 * @date 2019/4/12 17:22
 */
@Service("sysRoleServiceD")
public class SysRoleServiceDImpl extends BaseServiceImpl<SysRoleMapper, SysRoleDO> implements SysRoleServiceD {

    /**
     * 应用升级时，角色更新
     *
     * @param roleDetails
     * @param appId
     * @return boolean
     * @author zuogang
     * @date 2020/8/10 15:31
     */
    @Override
    public boolean roleUpdate(List<RoleDetail> roleDetails, String appId) {
        List<SysRoleDO> add = new ArrayList<>(16);
        List<SysRoleDO> upd = new ArrayList<>(16);
        roleDetails.stream().forEach(roleDetail -> {
            SysRoleDO item = JsonUtils.castObject(roleDetail, SysRoleDO.class);
            // 判断当前业务数据库中是否存在该条数据
            SysRoleDO sysRoleDO = getOne(new QueryWrapper<SysRoleDO>()
                    .eq("trace_id", item.getTraceId()).eq("app_id", appId));
            if (sysRoleDO == null) {
                add.add(item);
            } else {
                // 判断业务数据库数据与应用更新配置文件数据是否一致
                if (CommonUtils.compareFields(sysRoleDO, item)) {
                    item.setId(sysRoleDO.getId());
                    upd.add(item);
                }
            }
        });
        if (CollectionUtils.isNotEmpty(add)) {
            if (!saveBatch(add)) {
                return false;
            }

        }
        if (CollectionUtils.isNotEmpty(upd)) {
            if (!updateBatchById(upd)) {
                return false;
            }
        }

        return true;
    }
}

package com.csicit.ace.data.persistent.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.csicit.ace.common.AppUpgradeJaxb.RoleMutexDetail;
import com.csicit.ace.common.pojo.domain.SysRoleMutexDO;
import com.csicit.ace.common.pojo.domain.SysRoleRelationDO;
import com.csicit.ace.common.utils.JsonUtils;
import com.csicit.ace.data.persistent.mapper.SysRoleMutexMapper;
import com.csicit.ace.data.persistent.service.SysRoleMutexServiceD;
import com.csicit.ace.data.persistent.utils.CommonUtils;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * 角色互斥关系管理 实例对象访问接口实现
 *
 * @author zuogang
 * @version V1.0
 * @date 2019/4/12 17:22
 */
@Service("sysRoleMutexServiceD")
public class SysRoleMutexServiceDImpl extends BaseServiceImpl<SysRoleMutexMapper, SysRoleMutexDO> implements
        SysRoleMutexServiceD {

    /**
     * 应用升级时，互斥角色更新
     *
     * @param roleMutexDetails
     * @param appId
     * @return boolean
     * @author zuogang
     * @date 2020/8/10 16:03
     */
    @Override
    public boolean roleMutexUpdate(List<RoleMutexDetail> roleMutexDetails, String appId) {
        List<SysRoleMutexDO> add = new ArrayList<>(16);
        List<SysRoleMutexDO> upd = new ArrayList<>(16);
        roleMutexDetails.stream().forEach(roleMutexDetail -> {
            SysRoleMutexDO item = JsonUtils.castObject(roleMutexDetail, SysRoleMutexDO.class);
            // 判断当前业务数据库中是否存在该条数据
            SysRoleMutexDO sysRoleMutexDO = getOne(new QueryWrapper<SysRoleMutexDO>()
                    .eq("trace_id", item.getTraceId()).inSql("role_id", "select id from sys_role where app_id='" +
                            appId + "'"));
            if (sysRoleMutexDO == null) {
                add.add(item);
            } else {
                // 判断业务数据库数据与应用更新配置文件数据是否一致
                if (CommonUtils.compareFields(sysRoleMutexDO, item)) {
                    item.setId(sysRoleMutexDO.getId());
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

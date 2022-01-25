package com.csicit.ace.data.persistent.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.csicit.ace.common.AppUpgradeJaxb.RoleRelationDetail;
import com.csicit.ace.common.pojo.domain.SysRoleDO;
import com.csicit.ace.common.pojo.domain.SysRoleRelationDO;
import com.csicit.ace.common.utils.JsonUtils;
import com.csicit.ace.data.persistent.mapper.SysRoleRelationMapper;
import com.csicit.ace.data.persistent.service.SysRoleRelationServiceD;
import com.csicit.ace.data.persistent.utils.CommonUtils;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * 角色关系管理 实例对象访问接口实现
 *
 * @author zuogang
 * @version V1.0
 * @date 2019/4/12 17:22
 */
@Service("sysRoleRelationServiceD")
public class SysRoleRelationServiceDImpl extends BaseServiceImpl<SysRoleRelationMapper, SysRoleRelationDO> implements
        SysRoleRelationServiceD {
    /**
     * 应用升级时，角色关系更新
     *
     * @return
     * @author zuogang
     * @date 2020/8/10 15:40
     */
    @Override
    public boolean roleRelationUpdate(List<RoleRelationDetail> roleRelationDetails, String appId) {
        List<SysRoleRelationDO> add = new ArrayList<>(16);
        List<SysRoleRelationDO> upd = new ArrayList<>(16);
        roleRelationDetails.stream().forEach(roleDetail -> {
            SysRoleRelationDO item = JsonUtils.castObject(roleDetail, SysRoleRelationDO.class);
            // 判断当前业务数据库中是否存在该条数据
            SysRoleRelationDO sysRoleRelationDO = getOne(new QueryWrapper<SysRoleRelationDO>()
                    .eq("trace_id", item.getTraceId()).inSql("pid", "select id from sys_role where app_id='" + appId
                            + "'"));
            if (sysRoleRelationDO == null) {
                add.add(item);
            } else {
                // 判断业务数据库数据与应用更新配置文件数据是否一致
                if (CommonUtils.compareFields(sysRoleRelationDO, item)) {
                    item.setId(sysRoleRelationDO.getId());
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

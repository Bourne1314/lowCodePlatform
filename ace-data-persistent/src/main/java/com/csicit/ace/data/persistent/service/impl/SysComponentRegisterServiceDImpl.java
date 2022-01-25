package com.csicit.ace.data.persistent.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.csicit.ace.common.AppUpgradeJaxb.ComponentRegisterDetail;
import com.csicit.ace.common.pojo.domain.SysComponentRegisterDO;
import com.csicit.ace.common.utils.JsonUtils;
import com.csicit.ace.data.persistent.mapper.SysComponentRegisterMapper;
import com.csicit.ace.data.persistent.service.SysComponentRegisterServiceD;
import com.csicit.ace.data.persistent.utils.CommonUtils;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;


/**
 * 组件注册 实例对象访问接口实现
 *
 * @author generator
 * @version V1.0
 * @date 2019-04-15 17:24:50
 */
@Service("sysComponentRegisterServiceD")
public class SysComponentRegisterServiceDImpl extends BaseServiceImpl<SysComponentRegisterMapper,
        SysComponentRegisterDO> implements
        SysComponentRegisterServiceD {
    /**
     * 应用升级， 更新组件注册
     *
     * @param componentRegisters
     * @param appId
     * @return boolean
     * @author zuogang
     * @date 2020/8/10 14:09
     */
    @Override
    public boolean componentRegisterUpdate(List<ComponentRegisterDetail> componentRegisters, String appId) {
        List<SysComponentRegisterDO> add = new ArrayList<>(16);
        List<SysComponentRegisterDO> upd = new ArrayList<>(16);

        componentRegisters.stream().forEach(componentRegister -> {
            SysComponentRegisterDO item = JsonUtils.castObject(componentRegister, SysComponentRegisterDO.class);
            // 判断当前业务数据库中是否存在该条数据
            SysComponentRegisterDO sysComponentRegisterDO = getOne(new QueryWrapper<SysComponentRegisterDO>()
                    .eq("trace_id", item.getTraceId()).eq("app_id", appId));
            if (sysComponentRegisterDO == null) {
                add.add(item);
            } else {
                // 判断业务数据库数据与应用更新配置文件数据是否一致
                if (CommonUtils.compareFields(sysComponentRegisterDO, item)) {
                    item.setId(sysComponentRegisterDO.getId());
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

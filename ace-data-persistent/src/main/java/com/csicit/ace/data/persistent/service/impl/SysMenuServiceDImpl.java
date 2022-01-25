package com.csicit.ace.data.persistent.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.csicit.ace.common.AppUpgradeJaxb.MenuDetail;
import com.csicit.ace.common.pojo.domain.SysAuthDO;
import com.csicit.ace.common.pojo.domain.SysMenuDO;
import com.csicit.ace.common.utils.JsonUtils;
import com.csicit.ace.data.persistent.mapper.SysMenuMapper;
import com.csicit.ace.data.persistent.service.SysMenuServiceD;
import com.csicit.ace.data.persistent.utils.CommonUtils;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * 系统菜单 实例对象访问接口实现
 *
 * @author generator
 * @version V1.0
 * @date 2019-04-15 20:12:31
 */
@Service("sysMenuServiceD")
public class SysMenuServiceDImpl extends BaseServiceImpl<SysMenuMapper, SysMenuDO> implements SysMenuServiceD {

    /**
     * 应用升级时，菜单更新
     *
     * @param menuDetails
     * @param appId
     * @return boolean
     * @author zuogang
     * @date 2020/8/10 15:18
     */
    @Override
    public boolean menuUpdate(List<MenuDetail> menuDetails, String appId) {
        List<SysMenuDO> add = new ArrayList<>(16);
        List<SysMenuDO> upd = new ArrayList<>(16);

        menuDetails.stream().forEach(menuDetail -> {
            SysMenuDO item = JsonUtils.castObject(menuDetail, SysMenuDO.class);
            // 判断当前业务数据库中是否存在该条数据
            SysMenuDO sysMenuDO = getOne(new QueryWrapper<SysMenuDO>()
                    .eq("trace_id", item.getTraceId()).eq("app_id", appId));
            if (sysMenuDO == null) {
                add.add(item);
            } else {
                // 判断业务数据库数据与应用更新配置文件数据是否一致
                if (CommonUtils.compareFields(sysMenuDO, item)) {
                    item.setId(sysMenuDO.getId());
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

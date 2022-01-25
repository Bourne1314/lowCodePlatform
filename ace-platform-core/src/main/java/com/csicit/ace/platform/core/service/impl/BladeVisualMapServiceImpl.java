package com.csicit.ace.platform.core.service.impl;

import com.csicit.ace.common.enums.AuditLogType;
import com.csicit.ace.common.enums.AuditLogTypeDO;
import com.csicit.ace.common.pojo.domain.BdPostDO;
import com.csicit.ace.common.pojo.domain.BladeVisualMapDO;
import com.csicit.ace.common.utils.StringUtils;
import com.csicit.ace.data.persistent.mapper.BladeVisualMapMapper;
import com.csicit.ace.data.persistent.service.impl.BaseServiceImpl;
import com.csicit.ace.platform.core.service.BladeVisualMapService;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;


/**
 * 可视化地图配置表 实例对象访问接口实现
 *
 * @author generator
 * @version V1.0
 * @date 2020-06-05 10:05:48
 */
@Service("bladeVisualMapService")
public class BladeVisualMapServiceImpl extends BaseServiceImpl<BladeVisualMapMapper, BladeVisualMapDO> implements
        BladeVisualMapService {
    /**
     * 新增
     *
     * @param instance
     * @return
     * @author zuogang
     * @date 2020/6/5 10:38
     */
    @Override
    public boolean saveBladeVisualMap(BladeVisualMapDO instance) {
        if (!save(instance)) {
            return false;
        }
        return sysAuditLogService.saveLog(new AuditLogTypeDO(AuditLogType.success, "新增"), "保存大屏地图",
                instance, securityUtils.getCurrentGroupId(), null);
    }


    /**
     * 修改
     *
     * @param instance
     * @return
     * @author zuogang
     * @date 2020/6/5 10:38
     */
    @Override
    public boolean updateBladeVisualMap(BladeVisualMapDO instance) {
        if (!updateById(instance)) {
            return false;
        }
        return sysAuditLogService.saveLog(new AuditLogTypeDO(AuditLogType.warning, "更新"),
                "修改大屏地图", instance, securityUtils.getCurrentGroupId(), null);
    }


    /**
     * 删除
     *
     * @param id
     * @return
     * @author zuogang
     * @date 2020/6/5 10:38
     */
    @Override
    public boolean deleteBladeVisualMap(String id) {

        if (StringUtils.isBlank(id)) {
            return false;
        }
        String mapName=getById(id).getName();
        if (!removeById(id)) {
            return false;
        }
        return sysAuditLogService.saveLog(new AuditLogTypeDO(AuditLogType.danger, "删除"), "删除大屏地图","删除大屏地图："+mapName, securityUtils.getCurrentGroupId(), null);
    }
}

package com.csicit.ace.platform.core.service;

import com.csicit.ace.common.pojo.domain.BladeVisualMapDO;
import com.csicit.ace.dbplus.service.IBaseService;
import org.springframework.transaction.annotation.Transactional;


/**
 * 可视化地图配置表 实例对象访问接口
 *
 * @author generator
 * @date 2020-06-05 10:05:48
 * @version V1.0
 */
@Transactional
public interface BladeVisualMapService extends IBaseService<BladeVisualMapDO> {
    /**
     * 新增
     *
     * @param instance
     * @return
     * @author zuogang
     * @date 2020/6/5 10:38
     */
    boolean saveBladeVisualMap(BladeVisualMapDO instance);

    /**
     * 修改
     *
     * @param instance
     * @return
     * @author zuogang
     * @date 2020/6/5 10:38
     */
    boolean updateBladeVisualMap(BladeVisualMapDO instance);
    /**
     * 删除
     *
     * @param id
     * @return
     * @author zuogang
     * @date 2020/6/5 10:38
     */
    boolean deleteBladeVisualMap(String id);
}

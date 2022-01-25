package com.csicit.ace.platform.core.service;

import com.csicit.ace.common.pojo.domain.BladeVisualShowDO;
import com.csicit.ace.dbplus.service.IBaseService;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


/**
 * 大屏展示 实例对象访问接口
 *
 * @author generator
 * @date 2020-06-05 10:05:48
 * @version V1.0
 */
@Transactional
public interface BladeVisualShowService extends IBaseService<BladeVisualShowDO> {
    /**
     * 获取单个
     *
     * @param id
     * @return
     * @author zuogang
     * @date 2020/6/5 10:38
     */
    BladeVisualShowDO getInfo(String id);
    /**
     * 获取树列表
     *
     * @param appId
     * @return
     * @author zuogang
     * @date 2020/6/5 10:38
     */
    List<BladeVisualShowDO> getListTree(String appId);
    /**
     * 新增
     *
     * @param instance
     * @return
     * @author zuogang
     * @date 2020/6/5 10:38
     */
    boolean saveBladeVisualShow(BladeVisualShowDO instance);

    /**
     * 修改
     *
     * @param instance
     * @return
     * @author zuogang
     * @date 2020/6/5 10:38
     */
    boolean updateBladeVisualShow(BladeVisualShowDO instance);
    /**
     * 删除
     *
     * @param id
     * @return
     * @author zuogang
     * @date 2020/6/5 10:38
     */
    boolean deleteBladeVisualShow(String id);
}

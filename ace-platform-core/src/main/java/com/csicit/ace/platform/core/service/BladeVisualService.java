package com.csicit.ace.platform.core.service;

import com.csicit.ace.common.pojo.domain.BladeVisualDO;
import com.csicit.ace.common.utils.server.R;
import com.csicit.ace.dbplus.service.IBaseService;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

/**
 * 大屏信息数据表 实例对象访问接口
 *
 * @author generator
 * @version V1.0
 * @date 2020-06-05 09:59:54
 */
@Transactional
public interface BladeVisualService extends IBaseService<BladeVisualDO> {
    /**
     * 获取单个
     *
     * @param sqlCode
     * @param queryString
     * @return
     * @author zuogang
     * @date 2020/6/5 10:38
     */
    R getSqlResult(String sqlCode, String queryString);

    /**
     * 获取单个
     *
     * @param id
     * @return
     * @author zuogang
     * @date 2020/6/5 10:38
     */
    R getInfo(String id);

    /**
     * 新增
     *
     * @param instance
     * @return
     * @author zuogang
     * @date 2020/6/5 10:38
     */
    boolean saveBladeVisual(BladeVisualDO instance);

    /**
     * 修改
     *
     * @param instance
     * @return
     * @author zuogang
     * @date 2020/6/5 10:38
     */
    boolean updateBladeVisual(BladeVisualDO instance);

    /**
     * 删除
     *
     * @param id
     * @return
     * @author zuogang
     * @date 2020/6/5 10:38
     */
    boolean deleteBladeVisual(String id);

    /**
     * 复制
     *
     * @param id
     * @return
     * @author zuogang
     * @date 2020/6/5 10:38
     */
    boolean copyBladeVisual(String id);

    /**
     * 触发消息推送方法
     *
     * @param map
     * @return
     * @author zuogang
     * @date 2020/6/5 10:38
     */
    void bladeMessagePush(Map<String, Object> map);
}

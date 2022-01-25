package com.csicit.ace.platform.core.service;

import com.csicit.ace.common.pojo.domain.SysJobCalendarDO;
import com.csicit.ace.common.utils.server.R;
import com.csicit.ace.dbplus.service.IBaseService;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

/**
 * 工作日表 实例对象访问接口
 *
 * @author generator
 * @version V1.0
 * @date 2019-08-16 08:12:02
 */
@Transactional
public interface SysJobCalendarService extends IBaseService<SysJobCalendarDO> {
    /**
     * 获取编辑过的工作日的年份列表
     *
     * @param orgId
     * @return
     * @author zuogang
     * @date 2019/8/16 16:52
     */
    List<String> getHasWorkYearList(String orgId);

    /**
     * 求出当前年月及上下月的工作日
     *
     * @param params
     * @return
     * @author zuogang
     * @date 2019/8/16 16:52
     */
    R getWorkDayDataList(Map<String, Object> params);

    /**
     * 工作日休息日点击设置
     *
     * @param params
     * @return
     * @author zuogang
     * @date 2019/8/16 16:52
     */
    boolean setWorkState(Map<String, Object> params);

    /**
     * 为该年份设置工作日
     *
     * @param params
     * @return
     * @author zuogang
     * @date 2019/8/16 16:52
     */
    boolean setWorkDay(Map<String, Object> params);
}

package com.csicit.ace.dev.service;

import com.csicit.ace.common.pojo.domain.dev.ProDatasourceDO;
import com.csicit.ace.dbplus.service.IBaseService;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 数据源 实例对象访问接口
 *
 * @author shanwj
 * @version V1.0
 * @date 2019-11-04 14:48:24
 */
@Transactional
public interface ProDatasourceService extends IBaseService<ProDatasourceDO> {

    /**
     * 新增
     *
     * @param instance
     * @return
     * @author zuogang
     * @date 2020/7/28 14:39
     */
    boolean saveDatasource(ProDatasourceDO instance);

    /**
     * 修改
     *
     * @param instance
     * @return
     * @author zuogang
     * @date 2020/7/28 14:39
     */
    boolean updateDatasouce(ProDatasourceDO instance,Integer oldMajor);

    /**
     * 删除
     *
     * @param ids
     * @return
     * @author zuogang
     * @date 2020/7/28 14:39
     */
    boolean deleteDatasouce(List<String> ids);
}

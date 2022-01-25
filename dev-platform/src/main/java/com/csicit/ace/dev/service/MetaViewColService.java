package com.csicit.ace.dev.service;

import com.csicit.ace.common.pojo.domain.dev.MetaViewColDO;
import com.csicit.ace.dbplus.service.IBaseService;
import org.springframework.transaction.annotation.Transactional;

/**
 * 视图列 实例对象访问接口
 *
 * @author shanwj
 * @date 2019-11-07 10:31:27
 * @version V1.0
 */
@Transactional
public interface MetaViewColService extends IBaseService<MetaViewColDO> {

    boolean updateTableColCaption(MetaViewColDO viewCol);

}

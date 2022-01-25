package com.csicit.ace.dev.service;

import com.csicit.ace.common.pojo.domain.dev.ProModelColDO;
import com.csicit.ace.dbplus.service.IBaseService;
import org.springframework.transaction.annotation.Transactional;

/**
 * 数据列 实例对象访问接口
 *
 * @author shanwj
 * @date 2019-11-04 14:49:22
 * @version V1.0
 */
@Transactional
public interface ProModelColService extends IBaseService<ProModelColDO> {

    boolean saveModelCol(ProModelColDO tableCol);

    boolean updateTableCol(ProModelColDO tableCol);

    boolean deleteTableCol(String id);

}

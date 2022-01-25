package com.csicit.ace.dev.service;

import com.csicit.ace.common.pojo.domain.dev.MetaTableColDO;
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
public interface MetaTableColService extends IBaseService<MetaTableColDO> {

    boolean saveCreateCol(String tableId, boolean tree, boolean dataVersion, boolean secret);

    boolean saveTableCol(MetaTableColDO tableCol);

    boolean updateTableCol(MetaTableColDO tableCol);

    boolean deleteTableCol(String id);

}

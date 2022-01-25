package com.csicit.ace.dev.service;

import com.csicit.ace.common.pojo.domain.dev.MetaViewDO;
import com.csicit.ace.dbplus.service.IBaseService;
import org.springframework.transaction.annotation.Transactional;

/**
 * 视图 实例对象访问接口
 *
 * @author shanwj
 * @date 2019-11-07 10:31:19
 * @version V1.0
 */
@Transactional
public interface MetaViewService extends IBaseService<MetaViewDO> {

    boolean saveView(MetaViewDO view);

    boolean updateView(MetaViewDO view);

    boolean deleteView(String id);

}

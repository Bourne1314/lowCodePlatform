package com.csicit.ace.dev.service;

import com.csicit.ace.common.pojo.domain.dev.MetaIndexDO;
import com.csicit.ace.dbplus.service.IBaseService;
import org.springframework.transaction.annotation.Transactional;


/**
 * 索引信息 实例对象访问接口
 *
 * @author shanwj
 * @date 2019-11-07 10:30:31
 * @version V1.0
 */
@Transactional
public interface MetaIndexService extends IBaseService<MetaIndexDO> {

    boolean saveIndex(MetaIndexDO index);

    boolean createIndex(MetaIndexDO index);

    boolean updateIndex(MetaIndexDO index);

    boolean deleteIndex(String id);
}

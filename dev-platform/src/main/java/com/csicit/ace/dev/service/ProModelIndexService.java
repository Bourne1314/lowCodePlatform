package com.csicit.ace.dev.service;

import com.csicit.ace.common.pojo.domain.dev.ProModelIndexDO;
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
public interface ProModelIndexService extends IBaseService<ProModelIndexDO> {

    boolean saveIndex(ProModelIndexDO index);

    boolean updateIndex(ProModelIndexDO index);

    boolean deleteIndex(String id);
}

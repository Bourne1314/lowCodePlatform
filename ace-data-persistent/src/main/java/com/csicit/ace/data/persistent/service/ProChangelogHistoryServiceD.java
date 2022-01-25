package com.csicit.ace.data.persistent.service;

import com.csicit.ace.common.pojo.domain.dev.ProChangelogHistoryDO;
import com.csicit.ace.dbplus.service.IBaseService;
import org.springframework.transaction.annotation.Transactional;

/**
 * changlog历史信息管理 实例对象访问接口
 *
 * @author shanwj
 * @date 2019/11/25 11:10
 */
@Transactional
public interface ProChangelogHistoryServiceD extends IBaseService<ProChangelogHistoryDO> {

}

package com.csicit.ace.orgauth.core.service;

import com.csicit.ace.common.pojo.domain.SysApiResourceDO;
import com.csicit.ace.dbplus.service.IBaseService;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author shanwj
 * @date 2019/5/22 9:28
 */
@Transactional
public interface SysApiResourceService extends IBaseService<SysApiResourceDO> {
    boolean saveAppApi(List<SysApiResourceDO> apis);
}

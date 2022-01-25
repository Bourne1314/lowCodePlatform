package com.csicit.ace.data.persistent.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.csicit.ace.common.pojo.domain.OrgGroupDO;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author FourLeaves
 * @version V1.0
 * @date 2019/12/26 14:11
 */
@Transactional
public interface OrgGroupServiceD extends IService<OrgGroupDO> {
}

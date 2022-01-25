package com.csicit.ace.data.persistent.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.csicit.ace.common.pojo.domain.OrgGroupDO;
import com.csicit.ace.data.persistent.mapper.OrgGroupMapper;
import com.csicit.ace.data.persistent.service.OrgGroupServiceD;
import org.springframework.stereotype.Service;

/**
 * @author FourLeaves
 * @version V1.0
 * @date 2019/12/26 14:11
 */
@Service("orgGroupServiceD")
public class OrgGroupServiceDImpl extends ServiceImpl<OrgGroupMapper, OrgGroupDO> implements OrgGroupServiceD {
}

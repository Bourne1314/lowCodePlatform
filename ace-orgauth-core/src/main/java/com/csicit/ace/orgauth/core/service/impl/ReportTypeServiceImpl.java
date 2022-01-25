package com.csicit.ace.orgauth.core.service.impl;

import com.csicit.ace.common.pojo.domain.ReportTypeDO;
import com.csicit.ace.data.persistent.mapper.ReportTypeMapper;
import com.csicit.ace.data.persistent.service.impl.BaseServiceImpl;
import com.csicit.ace.orgauth.core.service.ReportTypeService;
import org.springframework.stereotype.Service;

/**
 * @author shanwj
 * @date 2019/12/13 8:25
 */
@Service("reportTypeServiceO")
public class ReportTypeServiceImpl extends BaseServiceImpl<ReportTypeMapper, ReportTypeDO> implements ReportTypeService {
}

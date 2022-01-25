package com.csicit.ace.data.persistent.service.impl;

import com.csicit.ace.common.pojo.domain.SysMsgUnReadDO;
import com.csicit.ace.data.persistent.mapper.SysMsgUnReadMapper;
import com.csicit.ace.data.persistent.service.SysMsgUnReadService;
import com.csicit.ace.data.persistent.service.impl.BaseServiceImpl;
import org.springframework.stereotype.Service;

/**
 * @author shanwj
 * @date 2019/7/8 11:06
 */
@Service
public class SysMsgUnReadServiceImpl extends BaseServiceImpl<SysMsgUnReadMapper, SysMsgUnReadDO>
        implements SysMsgUnReadService {
}

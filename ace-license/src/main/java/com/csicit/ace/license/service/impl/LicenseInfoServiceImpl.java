package com.csicit.ace.license.service.impl;

import com.csicit.ace.license.entity.LicenseInfo;
import com.csicit.ace.license.mapper.LicenseInfoMapper;
import com.csicit.ace.license.service.LicenseInfoService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * @author shanwj
 * @date 2019/6/27 17:17
 */
@Service
public class LicenseInfoServiceImpl extends ServiceImpl<LicenseInfoMapper,LicenseInfo>
        implements LicenseInfoService {
}

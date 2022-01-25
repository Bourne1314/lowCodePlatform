package com.csicit.ace.license.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.csicit.ace.license.entity.LicenseAppsInfo;
import com.csicit.ace.license.mapper.LicenseAppsInfoMapper;
import com.csicit.ace.license.service.LicenseAppsInfoService;
import com.csicit.ace.license.service.LicenseInfoService;
import org.springframework.stereotype.Service;

/**
 * @author shanwj
 * @date 2019/6/27 17:17
 */
@Service
public class LicenseAppsInfoServiceImpl extends ServiceImpl<LicenseAppsInfoMapper, LicenseAppsInfo>
        implements LicenseAppsInfoService {
}

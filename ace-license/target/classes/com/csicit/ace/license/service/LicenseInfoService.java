package com.csicit.ace.license.service;

import com.csicit.ace.license.entity.LicenseInfo;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author shanwj
 * @date 2019/6/27 17:17
 */
@Transactional
public interface LicenseInfoService extends IService<LicenseInfo> {
}

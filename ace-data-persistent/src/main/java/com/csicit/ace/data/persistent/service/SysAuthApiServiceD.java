package com.csicit.ace.data.persistent.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.csicit.ace.common.AppUpgradeJaxb.AuthApiDetail;
import com.csicit.ace.common.pojo.domain.SysApiResourceDO;
import com.csicit.ace.common.pojo.domain.SysAuthApiDO;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 权限api关系管理 实例对象访问接口
 *
 * @author shanwj
 * @version V1.0
 * @date 2019/4/12 17:22
 */
@Transactional
public interface SysAuthApiServiceD extends IService<SysAuthApiDO> {
    /**
     * 应用升级时，AuthAPI更新
     *
     * @param authApiDetails
     * @param appId
     * @return boolean
     * @author zuogang
     * @date 2020/8/10 14:59
     */
    boolean authApiUpdate(List<AuthApiDetail> authApiDetails, String appId);

}

package com.csicit.ace.data.persistent.service;

import com.csicit.ace.common.AppUpgradeJaxb.AuthDetail;
import com.csicit.ace.common.pojo.domain.SysAuthDO;
import com.csicit.ace.common.utils.server.R;
import com.csicit.ace.dbplus.service.IBaseService;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import java.util.Map;


/**
 * 权限管理 实例对象访问接口
 *
 * @author shanwj
 * @version V1.0
 * @date 2019/4/12 17:22
 */
@Transactional
public interface SysAuthServiceD extends IBaseService<SysAuthDO> {
    /**
     * 应用升级，权限更新
     *
     * @param authDetails
     * @param appId
     * @return boolean
     * @author zuogang
     * @date 2020/8/10 14:21
     */
    boolean authUpdate(List<AuthDetail> authDetails, String appId);
}

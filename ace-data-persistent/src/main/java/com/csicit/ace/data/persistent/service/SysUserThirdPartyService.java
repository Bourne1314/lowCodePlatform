package com.csicit.ace.data.persistent.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.csicit.ace.common.pojo.domain.SysUserThirdPartyDO;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author yansiyang
 * @version V1.0
 * @date 2019/11/25 15:25
 */
@Transactional
public interface SysUserThirdPartyService extends IService<SysUserThirdPartyDO> {
    
    /** 
     * 根据用户id、账号类型获取第三方账号
     *
     * @param userIds	
     * @param type
     * @return java.util.List<java.lang.String>
     * @author shanwj
     * @date 2020/4/21 15:55
     */
    List<String> getThirdAccounts(List<String> userIds, String type);
}

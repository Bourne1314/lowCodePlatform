package com.csicit.ace.data.persistent.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.csicit.ace.common.pojo.domain.SysGroupAppDO;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author FourLeaves
 * @version V1.0
 * @date 2019/12/26 10:48
 */
@Transactional
public interface SysGroupAppServiceD extends IService<SysGroupAppDO> {
    
    /**
     * 获取当前应用
     * @return 
     * @author FourLeaves
     * @date 2019/12/27 16:47
     */
    SysGroupAppDO getCurrentApp();
    
    /**
     * 给App上锁
     * @param appName
     * @return
     * @author FourLeaves
     * @date 2019/12/24 11:51
     */
    boolean lockApp(String appName);


    /**
     * 解锁app
     * @param appName
     * @return
     * @author FourLeaves
     * @date 2019/12/24 11:51
     */
    boolean unLockApp(String appName);
}

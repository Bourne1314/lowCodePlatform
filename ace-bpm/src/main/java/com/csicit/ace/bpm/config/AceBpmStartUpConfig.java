package com.csicit.ace.bpm.config;

import com.csicit.ace.bpm.BpmException;
import com.csicit.ace.common.constant.Constants;
import com.csicit.ace.common.exception.RException;
import com.csicit.ace.common.pojo.domain.SysUserDO;
import com.csicit.ace.common.utils.CacheUtil;
import com.csicit.ace.common.utils.cipher.GMBaseUtil;
import com.csicit.ace.common.utils.internationalization.InternationUtils;
import com.csicit.ace.interfaces.service.IMessage;
import com.csicit.ace.interfaces.service.IWfdFlow;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;


/**
 * @author zuogang
 * @date Created in 11:04 2019/7/4
 */
@Component
@Order(-1)
public class AceBpmStartUpConfig implements ApplicationRunner {

    @Autowired
    IWfdFlow wfdFlow;
    @Autowired
    CacheUtil cacheUtil;
    @Autowired
    IMessage message;

    /**
     * 在应用启动时检查应用菜单和权限是否包含了工作流的菜单和权限，没有的话就新增相应的权限和菜单 开关
     */
    @Value("${ace.config.openCreateNpmMenu:false}")
    private Boolean openCreateNpmMenu;

    @Override
    public void run(ApplicationArguments args) {
        SysUserDO sysUserD = new SysUserDO();
        sysUserD.setId(Constants.ACE_SERVICE_USER);
        sysUserD.setUserName(Constants.ACE_SERVICE_USER);
        String token = null;
        try {
            GMBaseUtil.initSmKeyValue(cacheUtil);
            token = GMBaseUtil.getToken(Constants.ACE_SERVICE_USER, Constants.ACE_SERVICE_USER);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RException(InternationUtils.getInternationalMsg("ERROR_GET_TOKEN"));
        }
        cacheUtil.hset(token, "user", sysUserD, 10 * 60 * 1000);
        // 应用启动类
        // 在应用启动时检查应用菜单和权限是否包含了工作流的菜单和权限，没有的话就新增相应的权限和菜单
        if (openCreateNpmMenu) {
            if (!wfdFlow.setAppFLowMenu(token)) {
                new BpmException("创建应用工作流菜单项失败！");
            }
        }
    }

}

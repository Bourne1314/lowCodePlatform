package com.csicit.ace.data.persistent.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.csicit.ace.common.pojo.domain.SysGroupAppDO;
import com.csicit.ace.common.utils.StringUtils;
import com.csicit.ace.data.persistent.mapper.SysGroupAppMapper;
import com.csicit.ace.data.persistent.service.SysGroupAppServiceD;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

/**
 * @author FourLeaves
 * @version V1.0
 * @date 2019/12/26 10:48
 */
@Service("sysGroupAppServiceD")
public class SysGroupAppServiceDImpl extends ServiceImpl<SysGroupAppMapper, SysGroupAppDO> implements SysGroupAppServiceD {

    @Value("${spring.application.name}")
    private String appName;

    /**
     * 上一次操作十秒内 判断为锁住
     */
    public static int minSeconds = 10;

    /**
     * 锁住一段时间后未解锁  判定为解锁
     */
    public static int maxSeconds = 60 * 3;

    // 平台的服务列表
    public static final List<String> AppNames = Arrays.asList(new String[]{"gateway", "platform",
            "auth", "acepublic", "fileserver", "dashboards", "report", "quartz", "orgauth"});

    @Override
    public SysGroupAppDO getCurrentApp() {
        if (AppNames.contains(appName)) {
            return getById(appName);
        }
        return null;
    }

    /**
     * 给App上锁
     *
     * @param appName
     * @return
     * @author FourLeaves
     * @date 2019/12/24 11:51
     */
    @Override
    public boolean lockApp(String appName) {

        if (StringUtils.isNotBlank(appName)) {
            SysGroupAppDO app = getById(appName);
            if (app == null) {
                return true;
            }
            // 上一次操作时间
            LocalDateTime lockTime = app.getLockTime();
            int lock = app.getIslock();
            if (lockTime == null) {
                update(new SysGroupAppDO(),
                        new UpdateWrapper<SysGroupAppDO>()
                                .set("LOCK_TIME", LocalDateTime.now()).set("IS_LOCK", 1).eq("id", appName));
                return true;
            } else {
                if (lock == 0) {
                    if (LocalDateTime.now().minusSeconds(minSeconds).isBefore(lockTime)) {
                        // 解锁状态
                        // 上一次操作十秒内
                        return false;
                    } else {
                        update(new SysGroupAppDO(),
                                new UpdateWrapper<SysGroupAppDO>()
                                        .set("LOCK_TIME", LocalDateTime.now()).set("IS_LOCK", 1).eq("id", appName));
                        return true;
                    }
                } else if (lock == 1) {
                    if (LocalDateTime.now().minusSeconds(maxSeconds).isAfter(lockTime)) {
                        // 锁住状态
                        // 上一次操作大于 3分钟
                        update(new SysGroupAppDO(),
                                new UpdateWrapper<SysGroupAppDO>()
                                        .set("LOCK_TIME", LocalDateTime.now()).eq("id", appName));
                        return true;
                    } else {
                        return false;
                    }
                }
            }

        }
        return true;
    }

    /**
     * 解锁app
     *
     * @param appName
     * @return
     * @author FourLeaves
     * @date 2019/12/24 11:51
     */
    @Override
    public boolean unLockApp(String appName) {
        if (StringUtils.isNotBlank(appName)) {
            if (count(new QueryWrapper<SysGroupAppDO>().eq("id", appName)) > 0) {
                return update(new SysGroupAppDO(),
                        new UpdateWrapper<SysGroupAppDO>()
                                .set("LOCK_TIME", LocalDateTime.now()).set("IS_LOCK", 0).eq("id", appName));
            }
        }
        return true;
    }
}

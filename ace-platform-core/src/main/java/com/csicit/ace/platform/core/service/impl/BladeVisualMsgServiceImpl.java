package com.csicit.ace.platform.core.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.csicit.ace.common.constant.Constants;
import com.csicit.ace.common.enums.AuditLogType;
import com.csicit.ace.common.enums.AuditLogTypeDO;
import com.csicit.ace.common.exception.RException;
import com.csicit.ace.common.pojo.domain.BladeVisualMsgDO;
import com.csicit.ace.common.pojo.domain.BladeVisualShowDO;
import com.csicit.ace.common.pojo.domain.SysAuthScopeAppDO;
import com.csicit.ace.common.pojo.vo.SocketEventVO;
import com.csicit.ace.common.utils.StringUtils;
import com.csicit.ace.data.persistent.mapper.BladeVisualMsgMapper;
import com.csicit.ace.data.persistent.service.SysMessageService;
import com.csicit.ace.data.persistent.service.impl.BaseServiceImpl;
import com.csicit.ace.platform.core.service.BladeVisualMsgService;
import com.csicit.ace.platform.core.service.BladeVisualShowService;
import com.csicit.ace.platform.core.service.SysAuthScopeAppService;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;


/**
 * 大屏消息 实例对象访问接口实现
 *
 * @author generator
 * @version V1.0
 * @date 2020-07-29 16:49:49
 */
@Service("bladeVisualMsgService")
public class BladeVisualMsgServiceImpl extends BaseServiceImpl<BladeVisualMsgMapper, BladeVisualMsgDO> implements
        BladeVisualMsgService {

    @Autowired
    SysMessageService sysMessageService;

    @Autowired
    SysAuthScopeAppService sysAuthScopeAppService;

    @Autowired
    BladeVisualShowService bladeVisualShowService;

    /**
     * 新增
     *
     * @param id
     * @return
     * @author zuogang
     * @date 2020/7/30 8:29
     */
    @Override
    public BladeVisualMsgDO getInfo(String id) {
        BladeVisualMsgDO visualMsgDO = getById(id);
        if (StringUtils.isNotBlank(visualMsgDO.getVisualIds())) {
            List<String> visualIds = Arrays.asList(visualMsgDO.getVisualIds().split(","));
            if (CollectionUtils.isNotEmpty(visualIds)) {
                visualMsgDO.setVisualNames(bladeVisualShowService.list(new QueryWrapper<BladeVisualShowDO>()
                        .in("id", visualIds)).stream().map(BladeVisualShowDO::getName).collect(Collectors.toList()));
            }
        }
        return visualMsgDO;
    }

    /**
     * 新增
     *
     * @param instance
     * @return
     * @author zuogang
     * @date 2020/7/30 8:29
     */
    @Override
    public boolean saveBladeVisualMsg(BladeVisualMsgDO instance) {
        if (!save(instance)) {
            return false;
        }
        return sysAuditLogService.saveLog(new AuditLogTypeDO(AuditLogType.success, "新增"), "保存大屏消息",
                "保存大屏消息："+ instance.getName(), securityUtils.getCurrentGroupId(), null);
    }

    /**
     * 修改
     *
     * @param instance
     * @return
     * @author zuogang
     * @date 2020/7/30 8:29
     */
    @Override
    public boolean updBladeVisualMsg(BladeVisualMsgDO instance) {
        if (!updateById(instance)) {
            return false;
        }
        return sysAuditLogService.saveLog(new AuditLogTypeDO(AuditLogType.warning, "更新"),
                "修改大屏消息", "修改大屏消息："+ instance.getName(), securityUtils.getCurrentGroupId(), null);
    }


    /**
     * 删除
     *
     * @param id
     * @return
     * @author zuogang
     * @date 2020/7/30 8:29
     */
    @Override
    public boolean delBladeVisualMsg(String id) {
        if (StringUtils.isBlank(id)) {
            return false;
        }
        BladeVisualMsgDO bladeVisualMsgDO = getById(id);
        if (!removeById(id)) {
            return false;
        }
        return sysAuditLogService.saveLog(new AuditLogTypeDO(AuditLogType.danger, "删除"), "删除大屏消息", "删除大屏消息："+bladeVisualMsgDO
                .getName(), securityUtils.getCurrentGroupId(), null);
    }


    /**
     * 获取当前日期是星期几<br>
     *
     * @return 当前日期是星期几
     */
    private String getWeekOfDate() {
        String[] weekDays = {"7", "1", "2", "3", "4", "5", "6"};
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date());
        int w = cal.get(Calendar.DAY_OF_WEEK) - 1;
        if (w < 0)
            w = 0;
        return weekDays[w];
    }

    /**
     * 判断当前几号是否是当月的最后一天
     *
     * @return boolean
     */
    private boolean getLastDayFlg(String day) {
        //设置时间格式
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        //获得实体类
        Calendar ca = Calendar.getInstance();
        //设置最后一天
        ca.set(Calendar.DAY_OF_MONTH, ca.getActualMaximum(Calendar.DAY_OF_MONTH));
        //最后一天格式化
        String lastDay = format.format(ca.getTime());
        if (Objects.equals(day, lastDay.substring(8, 10))) {
            return true;
        }
        return false;
    }

    /**
     * 时间大小比较
     */
    private boolean timeCompare(String periodTime, String timePoints) {
        DateFormat df = new SimpleDateFormat("HH:mm:ss");
        boolean flg = false;
        try {
            Date dt1 = df.parse(periodTime);//将字符串转换为date类型
            Date dt2 = df.parse(timePoints);
            // 定时任务间隔5分钟， 触发时间需要在当前时间的前两分半和后两分半时间段内才执行
            if (dt2.getTime() - 60000 * 2.5 <= dt1.getTime() && dt1.getTime() <= dt2.getTime() + 60000 * 2.5) {
                flg = true;
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return flg;
    }


    /**
     * 业务平台内部定时任务
     * 扫描大屏消息表数据，在时间范围内向前台推送消息
     *
     * @return
     * @author zuogang
     * @date 2020/7/30 8:29
     */
    @Override
    public void bladeVisualMsgPush() {
        System.out.println("--------------业务平台内部定时任务-扫描大屏消息表数据-----------------");
        List<BladeVisualMsgDO> bladeVisualMsgDOList = list(new QueryWrapper<BladeVisualMsgDO>()
                .eq("DISPLAY_MODE", 1));

        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
        String date = df.format(new Date());// new Date()为获取当前系统时间，也可使用当前时间戳
        // 当前时间点数
        String timePoints = date.substring(11, date.length());
        // 当前星期几
        String weekDay = this.getWeekOfDate();
        // 当前几号
        String day = date.substring(8, 10);
        // 判断当前几号是否是当月的最后一天
        boolean lastDayFlg = this.getLastDayFlg(day);

        Map<String, Object> map1 = new HashMap<>();
        bladeVisualMsgDOList.stream().forEach(item -> {

            // 周期数据
            String[] periodData = item.getPeriodData().split(",");

            // 获取拥有该应用授控域的应用管理员ID集合，作为接受人ID集合
            List<String> userIds = sysAuthScopeAppService.list(new QueryWrapper<SysAuthScopeAppDO>()
                    .eq("app_id", item.getAppId()).eq("IS_ACTIVATED", 1).eq("ROLE_TYPE", 111))
                    .stream().map(SysAuthScopeAppDO::getUserId).collect(Collectors.toList());

            // 周期模式：每天
            if (Objects.equals("D", item.getPeriodMode())) {
                if (this.timeCompare(periodData[1], timePoints)) {
                    map1.put("eventName", Constants.BLADEVISUALMSGPUSH);
                    map1.put("data", item);
                    sysMessageService.fireSocketEvent(new SocketEventVO(userIds,
                            Constants.BLADEVISUALMSGPUSH, map1, Constants.PLATFORM));
                }
            } else if (Objects.equals("W", item.getPeriodMode())) {
                // 周期模式：每周
                if (Objects.equals(weekDay, periodData[0]) && this.timeCompare(periodData[1], timePoints)) {
                    map1.put("eventName", Constants.BLADEVISUALMSGPUSH);
                    map1.put("data", item);
                    sysMessageService.fireSocketEvent(new SocketEventVO(userIds,
                            Constants.BLADEVISUALMSGPUSH, map1, Constants.PLATFORM));
                }
            } else {
                // 周期模式：每月
                if (Objects.equals("last", periodData[0])) {
                    if (lastDayFlg && this.timeCompare(periodData[1], timePoints)) {
                        map1.put("eventName", Constants.BLADEVISUALMSGPUSH);
                        map1.put("data", item);
                        sysMessageService.fireSocketEvent(new SocketEventVO(userIds,
                                Constants.BLADEVISUALMSGPUSH, map1, Constants.PLATFORM));
                    }
                } else {
                    if (Objects.equals(day, periodData[0]) && this.timeCompare(periodData[1], timePoints)) {
                        map1.put("eventName", Constants.BLADEVISUALMSGPUSH);
                        map1.put("data", item);
                        sysMessageService.fireSocketEvent(new SocketEventVO(userIds,
                                Constants.BLADEVISUALMSGPUSH, map1, Constants.PLATFORM));
                    }
                }
            }
        });

    }

}

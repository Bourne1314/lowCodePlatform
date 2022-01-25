package com.csicit.ace.platform.core.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.csicit.ace.common.pojo.domain.SysJobCalendarDO;
import com.csicit.ace.common.utils.UuidUtils;
import com.csicit.ace.common.utils.server.R;
import com.csicit.ace.data.persistent.mapper.SysJobCalendarMapper;
import com.csicit.ace.data.persistent.service.impl.BaseServiceImpl;
import com.csicit.ace.platform.core.service.SysJobCalendarService;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;


/**
 * 工作日表 实例对象访问接口实现
 *
 * @author generator
 * @version V1.0
 * @date 2019-08-16 08:12:02
 */
@Service("sysJobCalendarService")
public class SysJobCalendarServiceImpl extends BaseServiceImpl<SysJobCalendarMapper, SysJobCalendarDO> implements
        SysJobCalendarService {

    /**
     * 获取编辑过的工作日的年份列表
     *
     * @param orgId
     * @return
     * @author zuogang
     * @date 2019/8/16 16:53
     */
    @Override
    public List<String> getHasWorkYearList(String orgId) {
        List<String> years = new ArrayList<>(16);
        list(new QueryWrapper<SysJobCalendarDO>()
                .eq("organization_id", orgId)).stream().map(SysJobCalendarDO::getYear).collect(Collectors
                .toList()).stream().distinct().forEach(year -> {
            years.add(year);
        });
        return years;
    }

    /**
     * 求出当前年月及上下月的工作日
     *
     * @param params
     * @return
     * @author zuogang
     * @date 2019/8/16 16:53
     */
    @Override
    public R getWorkDayDataList(Map<String, Object> params) {
        List<SysJobCalendarDO> currentMonthToDays = new ArrayList<>(16);
        List<SysJobCalendarDO> previousMonthToDays = new ArrayList<>(16);
        List<SysJobCalendarDO> nextMonthToDays = new ArrayList<>(16);
        String orgId = (String) params.get("orgId");
        Integer year = (Integer) params.get("currentYear");
        Integer month = (Integer) params.get("currentMonth");

        if (1 < month && month < 12) {

            previousMonthToDays.addAll(list(new QueryWrapper<SysJobCalendarDO>()
                    .eq("organization_id", orgId)
                    .eq("year", year)
                    .eq("month", month - 1)));
            currentMonthToDays.addAll(list(new QueryWrapper<SysJobCalendarDO>()
                    .eq("organization_id", orgId)
                    .eq("year", year)
                    .eq("month", month)));
            nextMonthToDays.addAll(list(new QueryWrapper<SysJobCalendarDO>()
                    .eq("organization_id", orgId)
                    .eq("year", year)
                    .eq("month", month + 1)));

        } else if (month == 1) {
            previousMonthToDays.addAll(list(new QueryWrapper<SysJobCalendarDO>()
                    .eq("organization_id", orgId)
                    .eq("year", year - 1)
                    .eq("month", 12)));
            currentMonthToDays.addAll(list(new QueryWrapper<SysJobCalendarDO>()
                    .eq("organization_id", orgId)
                    .eq("year", year)
                    .eq("month", month)));
            nextMonthToDays.addAll(list(new QueryWrapper<SysJobCalendarDO>()
                    .eq("organization_id", orgId)
                    .eq("year", year)
                    .eq("month", month + 1)));
        } else if (month == 12) {
            previousMonthToDays.addAll(list(new QueryWrapper<SysJobCalendarDO>()
                    .eq("organization_id", orgId)
                    .eq("year", year)
                    .eq("month", month - 1)));
            currentMonthToDays.addAll(list(new QueryWrapper<SysJobCalendarDO>()
                    .eq("organization_id", orgId)
                    .eq("year", year)
                    .eq("month", month)));
            nextMonthToDays.addAll(list(new QueryWrapper<SysJobCalendarDO>()
                    .eq("organization_id", orgId)
                    .eq("year", year + 1)
                    .eq("month", 1)));
        }

        return R.ok().put("previousMonthToDays", previousMonthToDays).put("currentMonthToDays", currentMonthToDays)
                .put("nextMonthToDays", nextMonthToDays);
    }

    /**
     * 工作日休息日点击设置
     *
     * @param params
     * @return
     * @author zuogang
     * @date 2019/8/16 16:53
     */
    @Override
    public boolean setWorkState(Map<String, Object> params) {
        String orgId = (String) params.get("orgId");
        Integer year = (Integer) params.get("currentYear");
        Integer month = (Integer) params.get("currentMonth");
        Integer clickDay = (Integer) params.get("clickDay");
        SysJobCalendarDO sysJobCalendarDO = getOne(new QueryWrapper<SysJobCalendarDO>()
                .eq("organization_id", orgId).eq("year", year).eq("month", month)
                .eq("day", clickDay));
        if (sysJobCalendarDO == null) {
            // 该日期未设置，不进行操作
            return true;
        } else {
            if (sysJobCalendarDO.getState() == 0) {
                // 设置为休息日
                sysJobCalendarDO.setState(1);
            } else {
                // 设置为工作日
                sysJobCalendarDO.setState(0);
            }
            if (!updateById(sysJobCalendarDO)) {
                return false;
            }
        }
        return true;
    }

    /**
     * 工作日休息日点击设置
     *
     * @param params
     * @return
     * @author zuogang
     * @date 2019/8/16 16:53
     */
    @Override
    public boolean setWorkDay(Map<String, Object> params) {
        String orgId = (String) params.get("orgId");
        int year = (int) params.get("year");
        int count = count(new QueryWrapper<SysJobCalendarDO>()
                .eq("organization_id", orgId).eq("year", year));
        if (count > 0) {
            // 该年份已被设置过
            // 删除老的年份日期数据
            if (!remove(new QueryWrapper<SysJobCalendarDO>()
                    .eq("organization_id", orgId).eq("year", year))) {
                return false;
            }
        }

        // 获取所有日期
        List<String> lDates = findDates(getYearFirst(year), getYearLast(year));

        // 获取要设置为休息日的日期列表
        List<String> restDateList = getRestDateList(params);

        List<SysJobCalendarDO> sysJobCalendarDOS = new ArrayList<>(16);

        lDates.stream().forEach(lDate -> {
            SysJobCalendarDO sysJobCalendarDO = new SysJobCalendarDO();
            sysJobCalendarDO.setId(UuidUtils.createUUID());
            sysJobCalendarDO.setOrganizationId(orgId);
            sysJobCalendarDO.setYear(lDate.substring(0, 4));
            sysJobCalendarDO.setMonth(lDate.substring(4, 6));
            sysJobCalendarDO.setDay(lDate.substring(6, 8));
            if (restDateList.contains(lDate)) {
                // 为休息日
                sysJobCalendarDO.setState(1);
            } else {
                // 为工作日
                sysJobCalendarDO.setState(0);
            }
            sysJobCalendarDOS.add(sysJobCalendarDO);
        });

        // 添加工作日表
        if(sysJobCalendarDOS.size()>0){
            if (!saveBatch(sysJobCalendarDOS)) {
                return false;
            }
        }

        return true;
    }

    /**
     * 获取要设置为休息日的日期列表
     *
     * @param params
     * @return
     * @author zuogang
     * @date 2019/8/19 17:04
     */
    private List<String> getRestDateList(Map<String, Object> params) {
        List<String> restDateList = new ArrayList<>(16);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        int year = (int) params.get("year");
        List<String> type = (List<String>) params.get("type");
        List<String> restValue1 = (List<String>) params.get("restValue1");
        List<LinkedHashMap> dynamicItems = (List<LinkedHashMap>) params.get("dynamicItems");
        try {
            if (restValue1 != null && restValue1.size() > 0) {
                restDateList.addAll(findDates(sdf.parse(restValue1.get(0)), sdf.parse(restValue1
                        .get(1))));
            }
            if (dynamicItems != null && dynamicItems.size() > 0) {
                dynamicItems.stream().forEach(dynamicItem -> {
                    try {
                        restDateList.addAll(findDates(sdf.parse((String) dynamicItem.get("key")), sdf.parse
                                ((String) dynamicItem.get("value"))));
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                });
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }


        List<String> sundayList = new ArrayList<>(16);
        List<String> saturdayList = new ArrayList<>(16);
        Calendar calendar = new GregorianCalendar(year, 0, 1);
        int i = 1;
        while (calendar.get(Calendar.YEAR) < year + 1) {
            calendar.set(Calendar.WEEK_OF_YEAR, i++);
            calendar.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
            if (calendar.get(Calendar.YEAR) == year) {
                sundayList.add(sdf.format(calendar.getTime()));
            }
            calendar.set(Calendar.DAY_OF_WEEK, Calendar.SATURDAY);
            if (calendar.get(Calendar.YEAR) == year) {
                saturdayList.add(sdf.format(calendar.getTime()));
            }
        }
        if (type.contains("sunday")) {
            restDateList.addAll(sundayList);
        }
        if (type.contains("saturday")) {
            restDateList.addAll(saturdayList);
        }


        return restDateList;
    }

    /**
     * 获取所有日期
     *
     * @param dBegin
     * @param dEnd
     * @return java.util.List<java.util.Date>
     * @author zuogang
     * @date 2019/8/19 14:45
     */
    private List<String> findDates(Date dBegin, Date dEnd) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        List<String> lDate = new ArrayList(16);
        lDate.add(sdf.format(dBegin));
        Calendar calBegin = Calendar.getInstance();
        // 使用给定的 Date 设置此 Calendar 的时间
        calBegin.setTime(dBegin);
        Calendar calEnd = Calendar.getInstance();
        // 使用给定的 Date 设置此 Calendar 的时间
        calEnd.setTime(dEnd);
        // 测试此日期是否在指定日期之后
        while (dEnd.after(calBegin.getTime())) {
            // 根据日历的规则，为给定的日历字段添加或减去指定的时间量
            calBegin.add(Calendar.DAY_OF_MONTH, 1);
            lDate.add(sdf.format(calBegin.getTime()));
        }
        return lDate;
    }

    /**
     * 获取某年第一天日期
     *
     * @param year
     * @return
     * @author zuogang
     * @date 2019/8/19 14:25
     */
    private Date getYearFirst(int year) {
        Calendar calendar = Calendar.getInstance();
        calendar.clear();
        calendar.set(Calendar.YEAR, year);
        Date currYearFirst = calendar.getTime();
        return currYearFirst;
    }

    /**
     * 获取某年最后一天日期
     *
     * @param year
     * @return
     * @author zuogang
     * @date 2019/8/19 14:25
     */
    private Date getYearLast(int year) {
        Calendar calendar = Calendar.getInstance();
        calendar.clear();
        calendar.set(Calendar.YEAR, year);
        calendar.roll(Calendar.DAY_OF_YEAR, -1);
        Date currYearLast = calendar.getTime();

        return currYearLast;
    }
}

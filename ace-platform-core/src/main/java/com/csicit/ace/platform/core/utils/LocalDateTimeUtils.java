package com.csicit.ace.platform.core.utils;

import java.time.Duration;
import java.time.LocalDateTime;

/**
 * LocalDateTime工具类
 *
 * @author yansiyang
 * @version V1.0
 * @date 2019/4/15 14:06
 */
public class LocalDateTimeUtils {

    /**
     * 判断目标时间是否在当前时间之前（以分钟计算）
     * @param time
     * @return 布尔值
     * @author yansiyang
     * @date 2019/4/15 14:08
     */
    public static boolean isBeforeNow(LocalDateTime time) {
        LocalDateTime now = LocalDateTime.now();
        Duration duration = Duration.between(now, time);
        long minutes = duration.toMinutes();
        if (minutes > 0) {
            return false;
        }
        return true;
    }

    /**
     * 判断目标时间是否在当前时间之前（以分钟计算）
     * @param time
     * @param type 0表示天数 1表示小时  2表示秒  其他表示分钟
     * @return 布尔值
     * @author yansiyang
     * @date 2019/4/15 14:08
     */
    public static boolean isBeforeNow(LocalDateTime time, int type) {
        LocalDateTime now = LocalDateTime.now();
        Duration duration = Duration.between(now, time);
        long interval = duration.toMinutes();
        switch (type) {
            case 0 :
                interval = duration.toDays();
                break;
            case 1:
                interval = duration.toHours();
                break;
            case 2:
                interval = duration.toMillis();
                break;
        }
        if (interval > 0) {
            return false;
        }
        return true;
    }

    /**
     * 获取两个时间的时间差（分钟）
     * @param startTime
     * @param endTime
     * @return
     * @author yansiyang
     * @date 2019/4/15 14:23
     */
    public static long getInterval(LocalDateTime startTime, LocalDateTime endTime) {
        Duration duration = Duration.between(startTime, endTime);
        return duration.toMinutes();
    }

    /**
     * 获取两个时间的时间差（分钟）
     * @param startTime
     * @param endTime
     * @param type 0表示天数 1表示小时  2表示秒  其他表示分钟
     * @return
     * @author yansiyang
     * @date 2019/4/15 14:23
     */
    public static long getInterval(LocalDateTime startTime, LocalDateTime endTime, int type) {
        Duration duration = Duration.between(startTime, endTime);
        switch (type) {
            case 0 :
                return duration.toDays();
            case 1:
                return duration.toHours();
            case 2:
                return duration.toMillis();
            default:
                return duration.toMinutes();
        }
    }
}

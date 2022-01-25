package com.csicit.ace.platform.core.controller;

import com.csicit.ace.common.annotation.AceAuth;
import com.csicit.ace.common.constant.Constants;
import com.csicit.ace.common.pojo.domain.SysUserDO;
import com.csicit.ace.common.utils.StringUtils;
import com.csicit.ace.common.utils.internationalization.InternationUtils;
import com.csicit.ace.common.utils.server.R;
import com.csicit.ace.common.utils.system.SecurityUtils;
import com.csicit.ace.platform.core.service.SysGroupAppService;
import com.csicit.ace.platform.core.utils.LogDO;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.web.bind.annotation.*;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

/**
 * @author yansiyang
 * @version V1.0
 * @date 2019/11/28 14:05
 */
@ConditionalOnProperty(prefix = "ace", name = "config.openMongoDB", havingValue = "true", matchIfMissing = false)
@RestController
@RequestMapping("/appLogs")
public class AppLogController {

    @Autowired
    MongoTemplate mongoTemplate;

    @Autowired
    SecurityUtils securityUtils;

    @Autowired
    SysGroupAppService sysGroupAppService;

    /**
     * 实时获取各应用的运行日志
     *
     * @param params 参数
     * @return
     * @author yansiyang
     * @date 2019/11/28 14:08
     */
    @ApiImplicitParam(name = "params", value = "参数", required = true, dataType = "Map")
    @ApiOperation(value = "实时获取各应用的运行日志", httpMethod = "GET", notes = "实时获取各应用的运行日志")
    @AceAuth("实时获取各应用的运行日志")
    @RequestMapping(value = "/action/getAppLogsOnline", method = RequestMethod.GET)
    public R getAppLogsOnline(@RequestParam Map<String, String> params) {
        String appName = params.get("appName");
        // 时间间隔 5  1 2 3 4 5
        String intervalStr = params.get("intervalStr");
        String lastLongtimeStr = params.get("lastLongtime");
        if (StringUtils.isBlank(appName)) {
            return R.error(InternationUtils.getInternationalMsg("EMPTY_ARG"));
        }
        int interval = 1;
        if (StringUtils.isNotBlank(intervalStr)) {
            interval = Integer.parseInt(intervalStr);
        }

        long endTime = System.currentTimeMillis();
        long startTime = endTime - 1000 * interval;

        if (StringUtils.isNotBlank(lastLongtimeStr)) {
            startTime = Long.parseLong(lastLongtimeStr);
            endTime = startTime + 1000 * interval;
        }


        LocalDate now = LocalDate.now();

        String collectionName = appName + "-" + now.format(dateTimeFormat);
        Query query = new Query(Criteria.where("times").gte(startTime).lt(endTime));

        List<Document> list = mongoTemplate.find(query, Document.class, collectionName);

        List<LogDO> res = new ArrayList<>();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        list.stream().forEach(document -> {
            res.add(new LogDO( (String)document.get("time"),
                    (int) document.get("level"),
                    (String) document.get("info")));
        });

        return R.ok().put("list", res).put("lastLongtime", endTime);
    }

    private static DateTimeFormatter dateTimeFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    /**
     * 删除各应用的运行日志
     *
     * @param params 参数
     * @return
     * @author yansiyang
     * @date 2019/11/28 14:08
     */
    @ApiImplicitParam(name = "params", value = "参数", required = true, dataType = "Map")
    @ApiOperation(value = "删除各应用的运行日志", httpMethod = "DELETE", notes = "删除各应用的运行日志")
    @AceAuth("删除各应用的运行日志")
    @RequestMapping(value = "", method = RequestMethod.DELETE)
    public R deleAppLogs(@RequestBody Map<String, String> params) {
        String appName = params.get("appName");
        // 验证权限
        // 只有租户管理员可以查看 平台运行日志
        // 租户 集团管理员 可以查看 平台 各应用
        SysUserDO user = securityUtils.getCurrentUser();
        if (user.getUserType() > 1) {
            return R.error(InternationUtils.getInternationalMsg("NOT_HAVE_AUTH"));
        } else if (user.getUserType() == 1) {
            if (Constants.AppNames.contains(appName)) {
                return R.error(InternationUtils.getInternationalMsg("NOT_HAVE_AUTH"));
            }
        }

        String dateStartStr = params.get("dateStartStr");
        String dateEndStr = params.get("dateEndStr");

        // 是否删除指定日期之前的
        boolean deleteBeforeEnd = StringUtils.isBlank(dateStartStr);

        if (StringUtils.isBlank(appName) || StringUtils.isBlank(dateEndStr)) {
            return R.error(InternationUtils.getInternationalMsg("EMPTY_ARG"));
        }

        LocalDate dateEnd = LocalDate.parse(dateEndStr, dateTimeFormat);

        Set<String> collectionNames = mongoTemplate.getCollectionNames();
        Set<String> collectionNamesToDelete = new HashSet<>();

        if (!deleteBeforeEnd) {
            LocalDate dateStart = LocalDate.parse(dateStartStr, dateTimeFormat);
            if (dateEnd.isBefore(dateStart)) {
                return R.error();
            }

            collectionNamesToDelete.add(appName + "-" + dateStartStr);
            while (dateEnd.isAfter(dateStart)) {
                collectionNamesToDelete.add(appName + "-" + dateEnd.format(dateTimeFormat));
                dateEnd = dateEnd.minusDays(1);
            }

        } else {
            final LocalDate dateEnds = dateEnd;
            collectionNamesToDelete.add(appName + "-" + dateEndStr);
            collectionNames.stream().forEach(collectionName -> {
                if (collectionName.startsWith(appName)) {
                    String dateStr = collectionName.substring(collectionName.length() - 10);
                    LocalDate date = LocalDate.parse(dateStr, dateTimeFormat);
                    if (date.isBefore(dateEnds)) {
                        collectionNamesToDelete.add(collectionName);
                    }
                }
            });

        }
        collectionNamesToDelete.stream().forEach(collectionName -> {
            if (deleteBeforeEnd) {
                if (collectionNames.contains(collectionName)) {
                    mongoTemplate.dropCollection(collectionName);
                }
            } else {
                mongoTemplate.dropCollection(collectionName);
            }
        });

        return R.ok();
    }


    /**
     * 获取各应用的运行日志
     *
     * @param params 参数
     * @return
     * @author yansiyang
     * @date 2019/11/28 14:08
     */
    @ApiImplicitParam(name = "params", value = "参数", required = true, dataType = "Map")
    @ApiOperation(value = "获取各应用的运行日志", httpMethod = "GET", notes = "获取各应用的运行日志")
    @AceAuth("获取各应用的运行日志")
    @RequestMapping(value = "", method = RequestMethod.GET)
    public R getAppLogs(@RequestParam Map<String, String> params) {
        String appName = params.get("appName");
        if (StringUtils.isBlank(appName)) {
            return R.error(InternationUtils.getInternationalMsg("EMPTY_ARG_WITH_PARAM", "应用名"));
        }
        SysUserDO user = securityUtils.getCurrentUser();
        if (user.getUserType() > 1) {
            return R.error(InternationUtils.getInternationalMsg("NOT_HAVE_AUTH"));
        } else if (user.getUserType() == 1) {
            if (Constants.AppNames.contains(appName)) {
                return R.error(InternationUtils.getInternationalMsg("NOT_HAVE_AUTH"));
            }
        }
        String dateStr = params.get("dateStr");
        if (StringUtils.isBlank(appName) || StringUtils.isBlank(dateStr)) {
            return R.error(InternationUtils.getInternationalMsg("EMPTY_ARG"));
        }
        String timeStr = params.get("timeStr");
        String type = params.get("type");
        // timeType 往前类型
        // 1 1个小时  2 半个小时  3 十分钟  4 5分钟  5 1分钟  6 30秒
        int timeType = 4;
        if (StringUtils.isNotBlank(type)) {
            timeType = Integer.parseInt(type);
        }
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = new Date();
        if (StringUtils.isNotBlank(timeStr)) {
            timeStr = dateStr + " " + timeStr;
            try {
                date = sdf.parse(timeStr);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        Date dateEnd = date;
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        switch (timeType) {
            case 1:
                c.add(Calendar.HOUR, -1);
                break;
            case 2:
                c.add(Calendar.MINUTE, -30);
                break;
            case 3:
                c.add(Calendar.MINUTE, -10);
                break;
            case 4:
                c.add(Calendar.MINUTE, -5);
                break;
            case 5:
                c.add(Calendar.MINUTE, -1);
                break;
            case 6:
                c.add(Calendar.SECOND, -30);
                break;
        }

        long times = c.getTime().getTime();
        long timesEnd = dateEnd.getTime();
        String collectionName = appName + "-" + dateStr;
        Query query = new Query(Criteria.where("times").gte(times).lte(timesEnd));

        List<Document> list = mongoTemplate.find(query, Document.class, collectionName);

        List<LogDO> res = new ArrayList<>();

        list.stream().forEach(document -> {
            // res.add(new LogDO(sdf.format(document.get("time", Date.class)),
            res.add(new LogDO((String) document.get("time"),
                    (int) document.get("level"),
                    (String) document.get("info")));
        });

        return R.ok().put("list", res);
    }
}

package com.csicit.ace.platform.core.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.csicit.ace.common.annotation.AceAuth;
import com.csicit.ace.common.constant.Constants;
import com.csicit.ace.common.pojo.domain.BdAppLibDO;
import com.csicit.ace.common.pojo.domain.SysGroupAppDO;
import com.csicit.ace.common.utils.DiscoveryUtils;
import com.csicit.ace.common.utils.internationalization.InternationUtils;
import com.csicit.ace.common.utils.server.R;
import com.csicit.ace.data.persistent.service.SysGroupDatasourceService;
import com.csicit.ace.dbplus.MapWrapper;
import com.csicit.ace.platform.core.service.SysCacheDataService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.checkerframework.checker.signedness.qual.Constant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.lang.Nullable;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;


/**
 * 集团应用库管理 接口访问层
 *
 * @author generator
 * @version V1.0
 * @date 2019-04-15 20:12:24
 */

@RestController
@RequestMapping("/sysGroupApps")
@Api("集团应用库管理")
public class SysGroupAppController extends BaseController {
    @Autowired
    SysGroupDatasourceService sysGroupDatasourceService;

    @Autowired
    SysCacheDataService sysCacheDataService;

    @Nullable
    @Autowired
    DiscoveryUtils discoveryUtils;

    /**
     * 是否是开发状态 0 否  1 是
     */
    @Value("${ace.config.isDevState:0}")
    private Integer isDevState;

    /**
     * 根据应用id获取单个应用
     *
     * @param id 对象主键
     * @return 单个应用对象
     * @author generator
     * @date 2019-04-15 20:12:24
     */
    @ApiImplicitParam(name = "id", value = "主键id", dataType = "String", required = true)
    @ApiOperation(value = "获取单个应用", httpMethod = "GET", notes = "根据应用id获取单个应用")
    @AceAuth("获取单个应用")
    @PostMapping
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public R get(@PathVariable("id") String id) {
        SysGroupAppDO instance = sysGroupAppService.getById(id);
        if (StringUtils.isNotBlank(instance.getDatasourceId())) {
            instance.setDatasourceName(sysGroupDatasourceService.getById(instance.getDatasourceId()).getName());
        }
        return R.ok().put("instance", instance);
    }

    /**
     * 获取列表
     *
     * @param params 请求参数
     * @return 集团应用集合不带分页
     * @author generator
     * @date 2019-04-15 20:12:24
     */
    @ApiOperation(value = "获取集团应用列表", httpMethod = "GET", notes = "根据请求参数获取集团应用列表")
    @ApiImplicitParam(name = "params", value = "参数", required = true, dataType = "Map")
    @AceAuth("获取集团应用列表")
    @RequestMapping(value = "/list/all", method = RequestMethod.GET)
    public R list(@RequestParam Map<String, Object> params) {
        List<SysGroupAppDO> list = sysGroupAppService.list(new QueryWrapper<SysGroupAppDO>()
        .eq("group_id",params.get("group_id")));
        return R.ok().put("list", chooseAppList(list, params));
    }

    /**
     * 获取有权限的应用列表
     *
     * @param params 请求参数
     * @return 集团应用集合不带分页
     * @author generator
     * @date 2019-04-15 20:12:24
     */
    @ApiOperation(value = "获取有权限的应用列表", httpMethod = "GET", notes = "根据请求参数获取集团应用列表")
    @ApiImplicitParam(name = "params", value = "参数", required = true, dataType = "Map")
    @AceAuth("获取有权限的应用列表")
    @RequestMapping(value = "/list/AppHaveAuth", method = RequestMethod.GET)
    public R listAppHaveAuth(@RequestParam Map<String, Object> params) {
        String groupId = (String) params.get("group_id");
//        List<SysGroupAppDO> haveAuthList = sysGroupAppService.listAppHaveAuth(groupId);
//        List<SysGroupAppDO> noAuthList = sysGroupAppService.listAppNoAuth(groupId);
        List<SysGroupAppDO> appList = sysGroupAppService.list(new QueryWrapper<SysGroupAppDO>()
                .eq("group_id", groupId));
        return R.ok().put("appList", chooseAppList(appList, params));
    }

    /**
     * 获取应用管理员所管理的应用列表
     *
     * @return 集团应用集合不带分页
     * @author generator
     * @date 2019-04-15 20:12:24
     */
    @ApiOperation(value = "获取应用管理员所管理的应用列表", httpMethod = "GET", notes = "获取应用管理员所管理的应用列表")
    @ApiImplicitParam(name = "params", value = "参数", required = true, dataType = "Map")
    @AceAuth("获取应用管理员所管理的应用列表")
    @RequestMapping(value = "/list/userOrgApp", method = RequestMethod.GET)
    public R listUserOrgApp(@RequestParam Map<String, Object> params) {
        List<SysGroupAppDO> list = sysGroupAppService.listUserOrgApp();
        return R.ok().put("list", chooseAppList(list, params));
    }

    /**
     * 获取列表
     *
     * @param params 请求参数
     * @return 集团应用集合带分页
     * @author generator
     * @date 2019-04-15 20:12:24
     */
    @ApiOperation(value = "获取集团应用列表", httpMethod = "GET", notes = "根据请求参数获取集团应用列表")
    @ApiImplicitParam(name = "params", value = "参数", required = true, dataType = "Map")
    @AceAuth("获取集团应用列表")
    @RequestMapping(method = RequestMethod.GET)
    public R page(@RequestParam Map<String, Object> params) {
        int current = Integer.parseInt(params.get("current").toString());
        int size = Integer.parseInt(params.get("size").toString());
        Page<SysGroupAppDO> page = new Page<>(current, size);
        IPage list = sysGroupAppService.page(page, new QueryWrapper<SysGroupAppDO>()
                .eq(Constants.isMonomerApp && !Constants.isZuulApp, "id", appName)
                .eq("group_id", params.get
                ("group_id")).orderByAsc("SORT_INDEX"));
        if (list.getRecords() != null && list.getRecords().size() > 0) {
            List<SysGroupAppDO> apps = list.getRecords();
            //Map<String, Object> appUseBpmFlag = cacheUtil.hmget("ace-app-use-bpm");
            Map<String, Object> appUseBpmFlag = sysCacheDataService.get("ace-app-use-bpm", Map.class);
            if (Objects.nonNull(appUseBpmFlag)) {
                for (SysGroupAppDO appDO : apps) {
                    if (appUseBpmFlag.containsKey(appDO.getId())
                            && Objects.equals(new Integer(1), appUseBpmFlag.get(appDO.getId()))) {
                        appDO.setHasBpm(1);
                    } else {
                        appDO.setHasBpm(0);
                    }
                }
            }

            SysGroupAppDO mainApp = sysGroupAppService.getOne(new QueryWrapper<SysGroupAppDO>().eq("group_id", params.get
                    ("group_id")).eq("IS_MAIN_APP", 1));
            return R.ok().put("page", Objects.equals(params.get("online"), "yes") ? getOnlieListForPage(list) : list).put("mainApp", mainApp);
        }
        return R.ok();
    }

    /**
     * 设置应用包含工作流
     *
     * @param appId
     * @return
     * @author yansiyang
     * @date 2019/7/21 15:59
     */
    @ApiOperation(value = "设置应用包含工作流", httpMethod = "GET", notes = "设置应用包含工作流")
    @ApiImplicitParam(name = "appId", value = "参数", required = true, dataType = "String")
    @AceAuth("设置应用包含工作流")
    @RequestMapping(value = "/action/setHasBpm/{appId}/{hasBpm}", method = RequestMethod.GET)
    public R setHasBpm(@PathVariable("appId") String appId, @PathVariable("hasBpm") Integer hasBpm) {
        //cacheUtil.hset("ace-app-use-bpm", appId, hasBpm);
        sysCacheDataService.setMapValue("ace-app-use-bpm", appId, hasBpm);
        return R.ok();
    }

    /**
     * 克隆应用
     *
     * @param params 参数
     * @return 保存响应结果
     * @author generator
     * @date 2019-04-15 20:12:24
     */
    @ApiOperation(value = "克隆应用", httpMethod = "POST", notes = "克隆应用")
    @ApiImplicitParam(name = "params", value = "params", required = true, dataType = "Map")
    @AceAuth("克隆应用")
    @RequestMapping(value = "/action/cloneApp", method = RequestMethod.POST)
    public R clone(@RequestParam Map<String, String> params) {
        if (isDevState != 1) {
            return R.error("仅开发模式下可克隆应用！");
        }
        String appId = params.get("appId");
        String newAppId = params.get("newAppId");
        if (StringUtils.isBlank(appId) || StringUtils.isBlank(newAppId)) {
            return R.error("应用名称为空!");
        }
        if (Objects.equals(appId, newAppId)) {
            return R.error("新的应用名称不可以和要克隆的相同!");
        }
        if (sysGroupAppService.cloneApp(appId, newAppId)) {
            return R.ok(InternationUtils.getInternationalMsg("OPERATE_SUCCESS"));
        }
        return R.error(InternationUtils.getInternationalMsg("OPERATE_FAILED"));
    }

    /**
     * 新增应用保存
     *
     * @param instance 应用对象
     * @return 保存响应结果
     * @author generator
     * @date 2019-04-15 20:12:24
     */
    @ApiOperation(value = "保存", httpMethod = "POST", notes = "保存应用")
    @ApiImplicitParam(name = "instance", value = "实体", required = true, dataType = "SysGroupAppDO")
    @AceAuth("保存应用")
    @RequestMapping(method = RequestMethod.POST)
    public R save(@RequestBody SysGroupAppDO instance) {
        if (isDevState != 1) {
            return R.error("仅开发模式下可直接新增应用！");
        }
        return sysGroupAppService.saveApp(instance);
    }

    /**
     * 修改应用对象
     *
     * @param instance 对象
     * @return 修改响应结果
     * @author generator
     * @date 2019-04-15 20:12:24
     */
    @ApiOperation(value = "修改", httpMethod = "PUT", notes = "修改应用")
    @ApiImplicitParam(name = "instance", value = "实体", required = true, dataType = "SysGroupAppDO")
    @AceAuth("修改应用")
    @RequestMapping(method = RequestMethod.PUT)
    public R update(@RequestBody SysGroupAppDO instance) {
        SysGroupAppDO old = sysGroupAppService.getById(instance.getId());
        if (!Objects.equals(old.getSortIndex(), instance.getSortIndex())) {
            if (sysGroupAppService.count(new QueryWrapper<SysGroupAppDO>().eq("group_id", instance.getGroupId()).eq
                    ("sort_index", instance.getSortIndex())) > 0) {
                return R.error(InternationUtils.getInternationalMsg("SAME_MODEL",
                        new String[]{InternationUtils.getInternationalMsg("SORT_NO"), instance.getSortIndex()
                                .toString()}
                ));
            }
        }
        // 判断是否前后端分离
        if (instance.getHasUi() != null) {
            int hasUi = instance.getHasUi();
            if (hasUi == 0) {
                instance.setUiName("");
            } else {
                if (StringUtils.isBlank(instance.getUiName())) {
                    return R.error(InternationUtils.getInternationalMsg("EMPTY_ARG"));
                }
                instance.setUiName(instance.getId().substring(0, 3) + instance.getUiName());
            }
        }
        if (sysGroupAppService.updateApp(instance)) {
            return R.ok(InternationUtils.getInternationalMsg("UPDATE_SUCCESS"));
        }
        return R.error(InternationUtils.getInternationalMsg("UPDATE_FAILED"));
    }

    /**
     * 根据id数组删除数据
     *
     * @param ids ID数组
     * @return 删除响应结果
     * @author generator
     * @date 2019-04-15 20:12:24
     */
    @ApiOperation(value = "删除", httpMethod = "DELETE", notes = "删除应用")
    @ApiImplicitParam(name = "ids", value = "ID数组", required = true, allowMultiple = true, dataType = "String []")
    @AceAuth("删除应用")
    @RequestMapping(method = RequestMethod.DELETE)
    public R delete(@RequestBody String[] ids) {
        if (sysGroupAppService.removeByIds(Arrays.asList(ids))) {
            return R.ok(InternationUtils.getInternationalMsg("DELETE_SUCCESS"));
        }
        return R.error(InternationUtils.getInternationalMsg("DELETE_FAILED"));
    }

    /**
     * 产品库导入应用
     *
     * @param map
     * @return 产品库导入应用响应结果
     * @author generator
     * @date 2019-04-15 20:12:24
     */
    @ApiOperation(value = "产品库导入应用", httpMethod = "POST", notes = "产品库导入应用")
    @ApiImplicitParam(name = "map", value = "参数", required = true, dataType = "Map")
    @AceAuth("产品库导入应用")
    @RequestMapping(value = "/import/bdApps", method = RequestMethod.POST)
    public R saveAppFromBdAppLib(@RequestBody Map<String, Object> map) {
//        String groupId = (String) map.get("groupId");
//        String checkFlg = (String) map.get("checkFlg");
//        List<String> ids = (List<String>) map.get("ids");
//        List<LinkedHashMap<String, Object>> appLibs = (List<LinkedHashMap<String, Object>>) map.get
//                ("dataListSelections");
        if (sysGroupAppService.saveAppFromBdAppLib(map)) {
            return R.ok(InternationUtils.getInternationalMsg("SAVE_SUCCESS"));
        }
        return R.error(InternationUtils.getInternationalMsg("SAVE_FAILED"));
    }

    /**
     * 设置集团的默认应用
     *
     * @param params
     * @return
     * @author yansiyang
     * @date 2019/7/21 15:59
     */
    @ApiOperation(value = "设置集团的默认应用", httpMethod = "POST", notes = "设置集团的默认应用")
    @ApiImplicitParam(name = "map", value = "参数", required = true, dataType = "Map")
    @AceAuth("设置集团的默认应用")
    @RequestMapping(value = "/action/setMainApp", method = RequestMethod.POST)
    public R setMainApp(@RequestBody Map<String, String> params) {
        if (sysGroupAppService.setMainApp(params)) {
            return R.ok(InternationUtils.getInternationalMsg("OPERATE_SUCCESS"));
        }
        return R.error(InternationUtils.getInternationalMsg("OPERATE_FAILED"));
    }

    /**
     * @param
     * @return com.csicit.ace.common.utils.server.R
     * @author shanwj
     * @date 2019/7/18 8:15
     */
    @ApiOperation(value = "获取授权应用", httpMethod = "GET", notes = "获取授权应用")
    @RequestMapping(value = "/license/apps", method = RequestMethod.GET)
    @AceAuth("获取授权应用")
    public R listAllApps(@RequestParam Map<String, Object> params) {
        String groupId = (String) params.get("groupId");
        List<String> appNames = sysGroupAppService.list(new QueryWrapper<SysGroupAppDO>()
                .eq("group_id", groupId))
                .stream().map(SysGroupAppDO::getName).collect(Collectors.toList());
        String license = cacheUtil.get("platform_secret_key");
        if (license != null) {
            JSONObject jsonObject = JSONObject.fromObject(license);
            JSONArray array = jsonObject.getJSONArray("apps");
            List<BdAppLibDO> apps = new ArrayList<>(16);
            for (int i = 0; i < array.size(); i++) {
                JSONObject object = array.getJSONObject(i);
                BdAppLibDO app = new BdAppLibDO();
                app.setId(object.getString("appId"));
                app.setName(object.getString("appName"));
                if (!appNames.contains(app.getName())) {
                    apps.add(app);
                }
            }
            return R.ok().put("page", apps);
        }
        return R.ok();
    }

    /**
     * 应用配置信息升级导出
     *
     * @param appId
     * @return
     * @author yansiyang
     * @date 2019/7/21 15:59
     */
    @ApiOperation(value = "应用配置信息升级导出", httpMethod = "GET", notes = "应用配置信息升级导出")
    @ApiImplicitParam(name = "id", value = "appId", dataType = "String", required = true)
    @RequestMapping(value = "/setApp/upgrade/{appId}", method = RequestMethod.GET)
    public void appUpgrade(@PathVariable("appId") String appId, HttpServletResponse response) throws
            IOException {
        response.reset();
        response.setHeader("Content-Disposition", "attachment; filename=\"appUpgrade.zip\"");
        sysGroupAppService.appUpgrade(appId, response.getOutputStream());

//        try {
//            byte[] data = writer.getBytes("UTF-8");
//            OutputStream out = response.getOutputStream();
//            out.write(data);
//            out.flush();
//            out.close();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }


    }

    /**
     * 根据条件参数筛选应用
     *
     * @param list
     * @param params
     * @return
     * @author FourLeaves
     * @date 2020/7/31 11:44
     */
    private List<SysGroupAppDO> chooseAppList(List<SysGroupAppDO> list, Map<String, Object> params) {
        if (CollectionUtils.isEmpty(list)) {
            return list;
        }
        if (Constants.isMonomerApp && !Constants.isZuulApp) {
            list = list.stream().filter(app-> Objects.equals(app.getId(), appName))
                    .collect(Collectors.toList());
        }
        if (Objects.equals(params.get("online"), "yes") && !Objects.equals(params.get("useBpm"), "yes")) {
            return sysGroupAppService.getOnlieList(list);
        }
        if (Objects.equals(params.get("useBpm"), "yes")) {
            return sysGroupAppService.getAppListWithBpm(sysGroupAppService.getOnlieList(list));
        }
        return list;
    }


    /**
     * 返回在线的app pag
     *
     * @param page 所有的app列表
     * @return
     * @author FourLeaves
     * @date 2020/7/31 8:38
     */
    private IPage<SysGroupAppDO> getOnlieListForPage(IPage<SysGroupAppDO> page) {
        page.setRecords(sysGroupAppService.getOnlieList(page.getRecords()));
        return page;
    }
}

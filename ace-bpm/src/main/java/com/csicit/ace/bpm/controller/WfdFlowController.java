package com.csicit.ace.bpm.controller;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.csicit.ace.bpm.BpmException;
import com.csicit.ace.bpm.BpmManager;
import com.csicit.ace.bpm.pojo.domain.WfdFlowCategoryDO;
import com.csicit.ace.bpm.pojo.domain.WfdFlowDO;
import com.csicit.ace.bpm.pojo.vo.NewJobFlowVO;
import com.csicit.ace.bpm.pojo.vo.NewJobTreeVO;
import com.csicit.ace.bpm.service.WfdFlowCategoryService;
import com.csicit.ace.bpm.service.WfdFlowService;
import com.csicit.ace.bpm.utils.LocaleUtils;
import com.csicit.ace.common.annotation.AceAuth;
import com.csicit.ace.common.pojo.domain.OrgOrganizationDO;
import com.csicit.ace.common.pojo.domain.SysRoleDO;
import com.csicit.ace.common.utils.StringUtils;
import com.csicit.ace.common.utils.internationalization.InternationUtils;
import com.csicit.ace.common.utils.server.R;
import com.csicit.ace.interfaces.service.ISecurity;
import com.csicit.ace.interfaces.service.IUser;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;


/**
 * 流程定义 接口访问层
 *
 * @author generator
 * @version V1.0
 * @date 2019-08-16 10:13:03
 */

@RestController
@RequestMapping("/wfdFlows")
@Api("流程定义")
public class WfdFlowController extends BaseController {
    @Autowired
    private WfdFlowCategoryService wfdFlowCategoryService;
    @Autowired
    private WfdFlowService wfdFlowService;
    @Autowired
    private ISecurity security;
    @Autowired
    private IUser iUser;
    @Autowired
    private BpmManager bpmManager;

    /**
     * @param id
     * @return
     * @author generator
     * @date 2019-08-16 10:13:03
     */
    @ApiImplicitParam(name = "id", value = "主键id", dataType = "String", required = true)
    @ApiOperation(value = "获取单个", httpMethod = "GET", notes = "获取单个")
    @AceAuth("获取单个")
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public R get(@PathVariable("id") String id) {
        WfdFlowDO instance = wfdFlowService.getById(id);
        JSONObject jsonObject = JSONObject.parseObject(instance.getModel(), JSONObject.class);
        return R.ok().put("instance", instance).put("jsonObject", jsonObject);
    }

    /**
     * 获取列表
     *
     * @param params 请求参数map对象
     * @return xxx集合
     * @author generator
     * @date 2019-08-16 10:13:03
     */
    @ApiOperation(value = "获取列表", httpMethod = "GET", notes = "获取列表")
    @ApiImplicitParam(name = "params", value = "参数", required = true, dataType = "Map")
    @AceAuth("获取列表")
    @RequestMapping(method = RequestMethod.GET)
    public R list(@RequestParam Map<String, Object> params) {
        int current = Integer.parseInt((String) params.get("current"));
        int size = Integer.parseInt((String) params.get("size"));
        String category_id = (String) params.get("category_id");
        Page<WfdFlowDO> page = new Page<>(current, size);
        IPage list = wfdFlowService.page(page, new QueryWrapper<WfdFlowDO>()
                .orderByAsc("sort_no").eq("category_id", category_id));
        return R.ok().put("page", list);
    }

    /**
     * 获取流程定义列表
     *
     * @param params 请求参数
     * @return 流程类别集合不带分页
     * @author generator
     * @date 2019-04-15 20:12:24
     */
    @ApiOperation(value = "获取流程定义列表", httpMethod = "GET", notes = "根据请求参数获取流程定义列表")
    @ApiImplicitParam(name = "params", value = "参数", required = true, dataType = "Map")
    @AceAuth("获取流程定义列表")
    @RequestMapping(value = "/list/all", method = RequestMethod.GET)
    public R listAll(@RequestParam Map<String, Object> params) {
        String category_id = (String) params.get("category_id");
        List<WfdFlowDO> list = wfdFlowService.list(new QueryWrapper<WfdFlowDO>()
                .orderByAsc("sort_no").eq("category_id", category_id));
        return R.ok().put("list", list);
    }

    /**
     * 获取全部流程类别定义列表
     *
     * @return 全部流程类别定义列表
     * @author generator
     * @date 2019-04-15 20:12:24
     */
    @ApiOperation(value = "获取全部流程类别定义列表", httpMethod = "GET", notes = "获取全部流程类别定义列表")
    @AceAuth("获取全部流程类别定义列表")
    @RequestMapping(value = "/listAllCategoryAndFlow", method = RequestMethod.GET)
    public R listAllCategoryAndFlow() {
        List<WfdFlowCategoryDO> categoryDOs = wfdFlowCategoryService.list(new QueryWrapper<WfdFlowCategoryDO>()
                .orderByAsc("sort_no").eq("app_id", securityUtils.getAppName()));
        JSONObject root = new JSONObject();
        root.put("id", "0");
        root.put("type", "0");
        root.put("title", "全部菜单");
        root.put("spread", true);
        List<JSONObject> categorys = new ArrayList<>();
        for (WfdFlowCategoryDO wfdFlowCategoryDO : categoryDOs) {
            List<JSONObject> flows = new ArrayList<>();
            List<WfdFlowDO> list = wfdFlowService.list(new QueryWrapper<WfdFlowDO>()
                    .orderByAsc("sort_no").eq("category_id", wfdFlowCategoryDO.getId()));
            for (WfdFlowDO flowDO : list) {
                JSONObject flow = new JSONObject();
                flow.put("id", flowDO.getId());
                flow.put("type", "2");
                flow.put("title", flowDO.getName());
                flows.add(flow);
            }
            JSONObject category = new JSONObject();
            category.put("id", wfdFlowCategoryDO.getId());
            category.put("type", "1");
            category.put("title", wfdFlowCategoryDO.getName());
            category.put("children", flows);
            category.put("spread", true);
            categorys.add(category);
        }
        root.put("children", categorys);
        return R.ok().put("treeData", root);
    }

    /**
     * 获取用户所属集团全部业务单元部门
     *
     * @return
     * @author FourLeaves
     * @date 2020/2/21 9:20
     */
    @ApiOperation(value = "获取用户所属集团全部业务单元部门", httpMethod = "GET", notes = "获取用户所属集团全部业务单元部门")
    @AceAuth("获取用户所属集团全部业务单元部门")
    @RequestMapping(value = "/listAllOrgsAndDeps", method = RequestMethod.GET)
    public R listAllOrgsAndDeps() {
        List<JSONObject> data = new ArrayList<>();
        getOrgs(security.getCurrentOrganizationsAndDeps(), data);
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("id", 0);
        jsonObject.put("title", "全部组织");
        jsonObject.put("spread", true);
        jsonObject.put("children", data);
        return R.ok().put("treeData", jsonObject);
    }


    /**
     * 转化部门数据为树结构
     *
     * @param orgs
     * @param objs
     * @return
     * @author FourLeaves
     * @date 2020/2/21 9:53
     */
    private void getOrgs(List<OrgOrganizationDO> orgs, List<JSONObject> objs) {
        for (OrgOrganizationDO org : orgs) {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("id", org.getId());
            jsonObject.put("title", org.getName());
            jsonObject.put("spread", true);
            if (CollectionUtils.isNotEmpty(org.getChildren())) {
                List<JSONObject> children = new ArrayList<>();
                getOrgs(org.getChildren(), children);
                jsonObject.put("children", children);
            }
            objs.add(jsonObject);
        }
    }

    /**
     * 转化角色数据为树结构
     *
     * @param roles
     * @param objs
     * @return
     * @author FourLeaves
     * @date 2020/2/21 9:53
     */
    private void getRoles(List<SysRoleDO> roles, List<JSONObject> objs) {
        for (SysRoleDO role : roles) {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("id", role.getId());
            jsonObject.put("title", role.getName());
            jsonObject.put("spread", true);
            if (CollectionUtils.isNotEmpty(role.getCRoles())) {
                List<JSONObject> children = new ArrayList<>();
                getRoles(role.getCRoles(), children);
                jsonObject.put("children", children);
            }
            objs.add(jsonObject);
        }
    }

    /**
     * 获取用户所属集团全部角色
     *
     * @return
     * @author FourLeaves
     * @date 2020/2/21 9:20
     */
    @ApiOperation(value = "获取用户所属集团全部角色", httpMethod = "GET", notes = "获取用户所属集团全部角色")
    @AceAuth("获取用户所属集团全部角色")
    @RequestMapping(value = "/listAllRoles", method = RequestMethod.GET)
    public R listAllRoles() {
        List<JSONObject> data = new ArrayList<>();
        getRoles(security.getRolesByCurrentGroup(), data);
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("id", 0);
        jsonObject.put("title", "全部角色");
        jsonObject.put("spread", true);
        jsonObject.put("children", data);
        return R.ok().put("treeData", jsonObject);
    }

    /**
     * 根据密级、角色或组织信息获取用户并分页
     *
     * @param params
     * @return
     * @author FourLeaves
     * @date 2020/2/21 9:20
     */
    @ApiOperation(value = "根据密级、角色或组织信息获取用户并分页", httpMethod = "GET", notes = "根据密级、角色或组织信息获取用户并分页")
    @AceAuth("根据密级、角色或组织信息获取用户并分页")
    @RequestMapping(value = "/listUsersBySecretLevelAndRoleOrOrg", method = RequestMethod.GET)
    public R listUsersBySecretLevelAndRoleOrOrg(@RequestParam Map<String, String> params) {
        return iUser.listUsersBySecretLevelAndRoleOrOrg(params);
    }

    /**
     * 保存流程定义
     *
     * @param instance 对象
     * @return 保存响应结果
     * @author generator
     * @date 2019-08-16 10:13:03
     */
    @ApiOperation(value = "保存流程定义", httpMethod = "POST", notes = "保存流程定义")
    @ApiImplicitParam(name = "instance", value = "实体", required = true, dataType = "WfdFlowDO")
    @AceAuth("保存流程定义")
    @RequestMapping(method = RequestMethod.POST)
    public R save(@RequestBody WfdFlowDO instance) {
        // 存在判断
        if (StringUtils.isNotBlank(wfdFlowService.existCheck(instance))) {
            return R.error(wfdFlowService.existCheck(instance));
        }
        WfdFlowCategoryDO wfdFlowCategory = wfdFlowCategoryService.getById(instance.getCategoryId());
        if (wfdFlowCategory == null) {
            return R.error(LocaleUtils.getWfdFlowCategoryNotFound(instance.getCategoryId()));
        }
        if (wfdFlowService.saveWfdFlow(instance)) {
            return R.ok(InternationUtils.getInternationalMsg("SAVE_SUCCESS"));
        }
        return R.error(InternationUtils.getInternationalMsg("SAVE_FAILED"));
    }

    /**
     * 修改
     *
     * @param instance 对象
     * @return 修改响应结果
     * @author generator
     * @date 2019-08-16 10:13:03
     */
    @ApiOperation(value = "修改", httpMethod = "PUT", notes = "修改")
    @ApiImplicitParam(name = "instance", value = "实体", required = true, dataType = "WfdFlowDO")
    @AceAuth("修改")
    @RequestMapping(method = RequestMethod.PUT)
    public R update(@RequestBody WfdFlowDO instance) {
        if (wfdFlowService.updateById(instance)) {
            return R.ok(InternationUtils.getInternationalMsg("UPDATE_SUCCESS"));
        }
        return R.error(InternationUtils.getInternationalMsg("UPDATE_FAILED"));
    }

    /**
     * @param ids ID数组
     * @return 删除响应结果
     * @author generator
     * @date 2019-08-16 10:13:03
     */
    @ApiOperation(value = "删除", httpMethod = "DELETE", notes = "删除")
    @ApiImplicitParam(name = "ids", value = "ID数组", required = true, allowMultiple = true, dataType = "String")
    @AceAuth("删除")
    @RequestMapping(method = RequestMethod.DELETE)
    public R delete(@RequestBody String[] ids) {
        if (wfdFlowService.removeByIds(Arrays.asList(ids))) {
            return R.ok(InternationUtils.getInternationalMsg("DELETE_SUCCESS"));
        }
        return R.error(InternationUtils.getInternationalMsg("DELETE_FAILED"));
    }

    /**
     * 前台Json信息传到后台,保存数据库
     *
     * @param map
     * @return
     * @author zuogang
     * @date 2019/4/11 18:21
     */
    @ApiOperation(value = "前台Json信息传到后台,保存数据库")
    @ApiImplicitParam(name = "map", value = "参数", required = true, dataType = "Map")
    @AceAuth("前台Json信息传到后台,保存数据库")
    @RequestMapping(value = "/commit/jsonInfo", method = RequestMethod.POST)
    public R commitJsonInfoToDB(@RequestBody Map<String, Object> map) {
        return wfdFlowService.commitJsonInfoToDB(map);
    }

    /**
     * 签出,进入编辑状态
     *
     * @param map
     * @return
     * @author zuogang
     * @date 2019/4/11 18:21
     */
    @ApiOperation(value = "签出,进入编辑状态")
    @ApiImplicitParam(name = "map", value = "参数", required = true, dataType = "Map")
    @AceAuth("签出,进入编辑状态")
    @RequestMapping(value = "/checkOut/editing", method = RequestMethod.POST)
    public R checkOutEditing(@RequestBody Map<String, String> map) {
        if (wfdFlowService.checkOutEditing(map.get("flowId"))) {
            return R.ok(InternationUtils.getInternationalMsg("SET_SUCCESS"));
        }
        return R.error(InternationUtils.getInternationalMsg("SET_FAILED"));
    }

    /**
     * 签入,退出编辑状态
     *
     * @param map
     * @return
     * @author zuogang
     * @date 2019/4/11 18:21
     */
    @ApiOperation(value = "签入,退出编辑状态")
    @ApiImplicitParam(name = "map", value = "参数", required = true, dataType = "Map")
    @AceAuth("签入,退出编辑状态")
    @RequestMapping(value = "/checkIn/editing", method = RequestMethod.POST)
    public R checkInEditing(@RequestBody Map<String, String> map) {
        if (wfdFlowService.checkInEditing(map.get("flowId"))) {
            return R.ok(InternationUtils.getInternationalMsg("SET_SUCCESS"));
        }
        return R.error(InternationUtils.getInternationalMsg("SET_FAILED"));
    }

    /**
     * 撤回某版本状态
     *
     * @param map
     * @return
     * @author zuogang
     * @date 2019/4/11 18:21
     */
    @ApiOperation(value = "撤回某版本状态")
    @ApiImplicitParam(name = "map", value = "参数", required = true, dataType = "Map")
    @AceAuth("撤回某版本状态")
    @RequestMapping(value = "/recall", method = RequestMethod.POST)
    public R recall(@RequestBody Map<String, Object> map) {
        try {
            Integer recallVersion = (Integer) map.get("recallVersion");
            String flowId = (String) map.get("flowId");
//            Integer reviseVersion = (Integer) map.get("reviseVersion");
            wfdFlowService.recall(flowId, recallVersion);
            return R.ok(InternationUtils.getInternationalMsg("SET_SUCCESS"));
        } catch (BpmException e) {
            e.printStackTrace();
            return R.error(e.getMessage());
        } catch (Exception e) {
            return R.error(InternationUtils.getInternationalMsg("SET_FAILED"));
        }
    }

    /**
     * 撤销发布
     *
     * @param map flowId、historyVersion
     * @return com.csicit.ace.common.utils.server.R
     * @author JonnyJiang
     * @date 2020/4/27 0:09
     */

    public R revokeDeploy(@RequestBody Map<String, Object> map) {
        String flowId = (String) map.get("flowId");
        Integer historyVersion = (Integer) map.get("historyVersion");
        bpmManager.revokeDeploy(flowId, historyVersion);
        return R.ok(LocaleUtils.getRevokeDeploySucceed());
    }


    /**
     * 获取用户新建工作的左侧流程列表
     *
     * @return
     * @author FourLeaves
     * @date 2020/2/21 9:20
     */
    @ApiOperation(value = "获取用户新建工作的左侧流程列表", httpMethod = "GET", notes = "获取用户新建工作的左侧流程列表")
    @AceAuth("获取用户新建工作的左侧流程列表")
    @RequestMapping(value = "/flowListForNewJob/{appId}", method = RequestMethod.GET)
    public R flowListForNewJob(@PathVariable("appId") String appId) {
        List<NewJobTreeVO> treeVOS = wfdFlowService.listFlowTreesForInitAuth(appId);
        return R.ok().put("treeVOS", treeVOS);
    }

    /**
     * 获取用户新建工作的常用流程列表,最近使用流程列表
     *
     * @return
     * @author FourLeaves
     * @date 2020/2/21 9:20
     */
    @ApiOperation(value = "获取用户新建工作的常用流程列表,最近使用流程列表", httpMethod = "GET", notes = "获取用户新建工作的常用流程列表,最近使用流程列表")
    @AceAuth("获取用户新建工作的常用流程列表,最近使用流程列表")
    @RequestMapping(value = "/latelyFlowListForNewJob/{appId}", method = RequestMethod.GET)
    public R latelyFlowListForNewJob(@PathVariable("appId") String appId) {
        return wfdFlowService.initFlowList(appId);
    }

    /**
     * 发布时获取服务端当前时间
     *
     * @return
     * @author zuogang
     * @date 2020/4/27 20:43
     */
    @ApiOperation(value = "发布时获取服务端当前时间", httpMethod = "GET", notes = "发布时获取服务端当前时间")
    @AceAuth("发布时获取服务端当前时间")
    @RequestMapping(value = "/action/getServerTime", method = RequestMethod.GET)
    public R getServerTime() {
        DateTimeFormatter DATE_FOMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime serverTime = LocalDateTime.now();
        return R.ok().put("serverTime", DATE_FOMAT.format(serverTime));
    }
}

package com.csicit.ace.platform.core.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.csicit.ace.common.annotation.AceAuth;
import com.csicit.ace.common.pojo.domain.ReportInfoDO;
import com.csicit.ace.common.pojo.domain.SysAuthDO;
import com.csicit.ace.common.pojo.domain.SysGroupDatasourceDO;
import com.csicit.ace.common.pojo.vo.TreeVO;
import com.csicit.ace.common.utils.file.FileDownUntils;
import com.csicit.ace.common.utils.internationalization.InternationUtils;
import com.csicit.ace.common.utils.server.R;
import com.csicit.ace.data.persistent.service.SysGroupDatasourceService;
import com.csicit.ace.dbplus.MapWrapper;
import com.csicit.ace.platform.core.service.ReportInfoService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.*;


/**
 * 报表信息 接口访问层
 *
 * @author generator
 * @date 2019-08-07 08:54:46
 * @version V1.0
 */
 
@RestController
@RequestMapping("/reportInfos")
@Api("报表信息")
public class ReportInfoController extends BaseController {

	@Autowired
	private ReportInfoService reportInfoService;

    @Autowired
    private SysGroupDatasourceService sysGroupDatasourceService;
    /**
     *
     * @param id
     * @return
     * @author generator
     * @date 2019-08-07 08:54:46
     */
    @ApiImplicitParam(name = "id", value = "主键id", dataType = "String", required = true)
    @ApiOperation(value = "获取单个", httpMethod = "GET", notes = "获取单个")
    @AceAuth("获取单个")
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public R get(@PathVariable("id") String id) {
        ReportInfoDO instance = reportInfoService.getById(id);
        if(StringUtils.isNotEmpty(instance.getAuth())){
            SysAuthDO auth = sysAuthService.getById(instance.getAuth());
            if(auth!=null){
                instance.setAuthName(sysAuthService.getById(instance.getAuth()).getName());
            }
        }
        if(StringUtils.isNotEmpty(instance.getDatasourceId())){
            StringJoiner sj = new StringJoiner(",");
            String[] ids = instance.getDatasourceId().split(",");
            for (String did:ids){
                SysGroupDatasourceDO ds = sysGroupDatasourceService.getById(did);
                if(Objects.nonNull(ds)){
                    sj.add(ds.getName());
                }
            }
            instance.setDatasourceName(sj.toString());
        }
        return R.ok().put("instance", instance);
    }

    /**
     * 获取列表
     *
     * @param params 请求参数map对象
     * @return xxx集合
     * @author generator
     * @date 2019-08-07 08:54:46
     */
    @ApiOperation(value = "获取列表", httpMethod = "GET", notes = "获取列表")
    @ApiImplicitParam(name = "params", value = "参数", required = true,dataType = "Map")
    @AceAuth("获取列表")
    @RequestMapping(method = RequestMethod.GET)
    public R list(@RequestParam Map<String, Object> params) {
        int current = Integer.parseInt((String) params.get("current"));
        int size = Integer.parseInt((String) params.get("size"));
        Page<ReportInfoDO> page = new Page<>(current, size);
        IPage list = reportInfoService.page(page, MapWrapper.getEqualInstance(params,"name",true));
        return R.ok().put("page", list);
    }

    /**
     * 新增
     *
     * @param instance	 对象
     * @return 保存响应结果
     * @author generator
     * @date 2019-08-07 08:54:46
     */
    @ApiOperation(value = "保存", httpMethod = "POST", notes = "保存")
    @ApiImplicitParam(name = "instance", value = "实体", required = true, dataType = "ReportInfoDO")
    @AceAuth("保存")
    @RequestMapping(method = RequestMethod.POST)
    public R save(@RequestBody ReportInfoDO instance) {
        List<ReportInfoDO> list =
                reportInfoService.list(new QueryWrapper<ReportInfoDO>()
                        .eq("app_id", instance.getAppId())
                        .eq("name", instance.getName()));
        if(list!=null&&list.size()>0){
            return R.error("存在重复的报表名称");
        }
        if (reportInfoService.saveReport(instance)) {
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
     * @date 2019-08-07 08:54:46
     */
    @ApiOperation(value = "修改", httpMethod = "PUT", notes = "修改")
    @ApiImplicitParam(name = "instance", value = "实体", required = true, dataType = "ReportInfoDO")
    @AceAuth("修改")
    @RequestMapping(method = RequestMethod.PUT)
    public R update(@RequestBody ReportInfoDO instance) {
        ReportInfoDO report = reportInfoService.getById(instance.getId());
        if(!Objects.equals(reportInfoService.getById(instance.getId()).getName(),instance.getName())){
            List<ReportInfoDO> list =
                    reportInfoService.list(new QueryWrapper<ReportInfoDO>()
                            .eq("app_id", instance.getAppId())
                            .eq("name", instance.getName()));
            if(list!=null&&list.size()>0){
                return R.error("存在重复的报表名称");
            }
        }
        instance.setMrtStr(report.getMrtStr());
        if (reportInfoService.updateReport(instance)) {
            return R.ok(InternationUtils.getInternationalMsg("UPDATE_SUCCESS"));
        }
        return R.error(InternationUtils.getInternationalMsg("UPDATE_FAILED"));
    }

    /**
     *
     * @param ids ID数组
     * @return 删除响应结果
     * @author generator
     * @date 2019-08-07 08:54:46
     */
    @ApiOperation(value = "删除", httpMethod = "DELETE", notes = "删除")
    @ApiImplicitParam(name = "ids", value = "ID数组", required = true, allowMultiple=true, dataType = "String")
    @AceAuth("删除")
    @RequestMapping(method = RequestMethod.DELETE)
    public R delete(@RequestBody String[] ids) {
        if (reportInfoService.removeByIds(Arrays.asList(ids))) {
            return R.ok(InternationUtils.getInternationalMsg("DELETE_SUCCESS"));
        }
        return R.error(InternationUtils.getInternationalMsg("DELETE_FAILED"));
    }

    /**
     * 获取应用报表列表
     *
     * @param appId	 应用id
     * @return 报表列表
     * @author shanwj
     * @date 2019/8/8 10:47
     */
    @ApiImplicitParam(name = "appId", value = "应用id", dataType = "String", required = true)
    @ApiOperation(value = "获取应用报表列表", httpMethod = "GET", notes = "获取应用报表列表")
    @AceAuth("获取应用报表列表")
    @RequestMapping(value = "/query/reportType/tree/{appId}/{type}",method = RequestMethod.GET)
    public TreeVO getReportTree(@PathVariable("appId")String appId,
                                @PathVariable("type")int type){
        return reportInfoService.getReportTree(appId,type);
    }

    /** 
     * 导出报表
     *
     * @param id 报表信息id
     * @param response	
     * @author shanwj
     * @date 2019/8/12 9:05
     */
    @ApiImplicitParam(name = "id", value = "报表id", dataType = "String", required = true)
    @ApiOperation(value = "导出报表", httpMethod = "GET", notes = "导出报表")
    @AceAuth("导出报表")
    @RequestMapping(value = "/export/{id}",method = RequestMethod.GET)
    public void exportMrt(@PathVariable("id") String id, HttpServletResponse response){
        ReportInfoDO reportInfoDO = reportInfoService.getById(id);
        FileDownUntils.download(response,reportInfoDO.getMrtStr(),reportInfoDO.getName()+".mrt");
    }

    @RequestMapping(value = "/import",method = RequestMethod.POST)
    public R importMrt(String id, String mrtStr){

        if (reportInfoService.importMrt(id,mrtStr)){
            return R.ok(InternationUtils.getInternationalMsg("OPERATE_SUCCESS"));
        }
        return R.error(InternationUtils.getInternationalMsg("OPERATE_FAILED"));
    }

    @ApiOperation(value = "上传文件", httpMethod = "POST", notes = "上传文件")
    @ApiImplicitParam(name = "id", value = "报表id", dataType = "String", required = true)
    @RequestMapping(value = "/action/upload/{id}", method = RequestMethod.POST)
    public R upload(@PathVariable("id") String id, @RequestParam("file") MultipartFile file) {
        // String str = map.get("str");
        if (file.isEmpty()) {
            return R.error(InternationUtils.getInternationalMsg("EMPTY_ARG_WITH_PARAM", "文件"));
        }
        String str;
        try {
            InputStream input = file.getInputStream();
            InputStreamReader inputStreamReader = new InputStreamReader(input, "UTF-8");
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            str = bufferedReader.readLine();
            bufferedReader.close();
            input.close();
            inputStreamReader.close();
        } catch (Exception e) {
            return R.error(InternationUtils.getInternationalMsg("UNKNOWN_EXCEPTION"));
        }
        if (StringUtils.isNotBlank(str)) {
            ReportInfoDO reportInfoDO = reportInfoService.getById(id);
            reportInfoDO.setMrtStr(str);
            if(reportInfoService.updateById(reportInfoDO)){
                return R.ok(InternationUtils.getInternationalMsg("OPERATE_SUCCESS"));
            }
            return R.error(InternationUtils.getInternationalMsg("OPERATE_FAILED"));
        }
        return R.error(InternationUtils.getInternationalMsg("OPERATE_FAILED"));
    }

}

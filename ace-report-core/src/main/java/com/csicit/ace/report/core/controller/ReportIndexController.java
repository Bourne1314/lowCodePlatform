package com.csicit.ace.report.core.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.csicit.ace.common.pojo.domain.ReportInfoDO;
import com.csicit.ace.common.pojo.domain.SysAuthMixDO;
import com.csicit.ace.common.pojo.domain.SysGroupDatasourceDO;
import com.csicit.ace.common.utils.MapUtils;
import com.csicit.ace.common.utils.StringUtils;
import com.csicit.ace.common.utils.file.FileDownUntils;
import com.csicit.ace.common.utils.system.SecurityUtils;
import com.csicit.ace.data.persistent.service.SysGroupAppServiceD;
import com.csicit.ace.data.persistent.service.SysGroupDatasourceService;
import com.csicit.ace.report.core.service.ReportService;
import com.csicit.ace.report.core.service.SysAuthMixService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * @author shanwj
 * @date 2019/8/6 9:36
 */
@ConditionalOnExpression("'${spring.application.name}'.equals('report')")
@Controller
@RequestMapping("/")
public class ReportIndexController {

    @Resource(name = "reportServiceR")
    ReportService reportService;
    @Autowired
    SecurityUtils securityUtils;
    @Resource(name = "sysAuthMixServiceR")
    SysAuthMixService sysAuthMixService;
    @Autowired
    HttpServletRequest request;
    @Autowired
    SysGroupAppServiceD sysGroupAppServiceD;
    @Autowired
    SysGroupDatasourceService sysGroupDatasourceService;
    @RequestMapping("/")
    public String index(){
        return "index";
    }

    @RequestMapping(value = "/designer",method = RequestMethod.GET)
    public String design(Model model){
        String id = request.getQueryString().split("&")[0].split("=")[1];
        ReportInfoDO reportInfoDO = reportService.getById(id);
        String dsId = sysGroupAppServiceD.getById(reportInfoDO.getAppId()).getDatasourceId();
        if(StringUtils.isEmpty(dsId)){
            model.addAttribute("show","报表所属应用未绑定数据源!");
            return "index";
        }
        SysGroupDatasourceDO ds = sysGroupDatasourceService.getById(dsId);
        if(Objects.isNull(ds)){
            model.addAttribute("show","报表所属应用绑定数据源不存在!");
            return "index";
        }
        return "designer";
    }

    @RequestMapping(value = "/viewer",method = RequestMethod.GET)
    public String view(Model model,HttpServletRequest request) throws UnsupportedEncodingException {
        String currentUserId = securityUtils.getCurrentUserId();
        String queryString = request.getQueryString();
        Map<String,Object> params = MapUtils.getUrlParams(URLDecoder.decode(queryString,"UTF-8"));
        ReportInfoDO report = null;
        if(params.get("reportId")!=null){
            String id = params.get("reportId").toString();
            report = reportService.getById(id);
        }else {
            if(params.get("reportAppId")==null||params.get("reportName")==null){
                return "error";
            }
            String reportAppId = params.get("reportAppId").toString();
            String reportName = params.get("reportName").toString();
            report =
                    reportService.getOne(
                            new QueryWrapper<ReportInfoDO>()
                                    .eq("app_id", reportAppId)
                                    .eq("name", reportName));
        }
        if (Objects.isNull(report)){
            model.addAttribute("show","当前报表尚未定义!");
            return "index";
        }
        if(!securityUtils.isAdmin()&&StringUtils.isNotEmpty(report.getAuth())){
            List<SysAuthMixDO> list = sysAuthMixService.list(
                    new QueryWrapper<SysAuthMixDO>()
                            .eq("auth_id", report.getAuth())
                            .eq("user_id", currentUserId));
            if(list==null||list.size()==0){
                return "index";
            }
        }
        params.put("reportId",report.getId());
        model.addAttribute("query",URLEncoder.encode(MapUtils.getUrlParamsByMap(params),"UTF-8"));
        return "viewerAll";
    }


    @RequestMapping(value = "/viewerh",method = RequestMethod.GET)
    public String viewh(){
        return "viewer";
    }

    @RequestMapping(value = "/designerf",method = RequestMethod.GET)
    public String designer(){
        return "designerf";
    }

    @RequestMapping(value = "/viewerf",method = RequestMethod.GET)
    public String viewer(){
        return "viewerf";
    }

    @RequestMapping(value = "/export/{id}",method = RequestMethod.GET)
    public void exportMrt(@PathVariable("id") String id, HttpServletResponse response){
        ReportInfoDO reportInfoDO = reportService.getById(id);
        FileDownUntils.download(response,reportInfoDO.getMrtStr(),reportInfoDO.getName()+".mrt");
    }

}

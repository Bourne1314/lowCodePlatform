<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">

<%@page import="com.alibaba.druid.util.StringUtils" %>
<%@ page import="com.baomidou.mybatisplus.core.conditions.query.QueryWrapper" %>
<%@ page import="com.csicit.ace.common.config.SpringContextUtils" %>
<%@ page import="com.csicit.ace.common.exception.RException" %>
<%@ page import="com.csicit.ace.common.pojo.domain.ReportInfoDO" %>
<%@ page import="com.csicit.ace.common.pojo.domain.SysGroupDatasourceDO" %>
<%@ page import="com.csicit.ace.common.utils.MapUtils" %>
<%@ page import="com.csicit.ace.report.core.config.Constants" %>
<%@ page import="com.csicit.ace.report.core.service.ReportService" %>
<%@ page import="com.csicit.ace.report.core.service.impl.ReportServiceImpl" %>
<%@ page import="com.stimulsoft.report.StiReport" %>
<%@ page import="com.stimulsoft.report.StiSerializeManager" %>
<%@ page import="com.stimulsoft.report.dictionary.StiDataParameter" %>
<%@ page import="com.stimulsoft.report.dictionary.StiDataParametersCollection" %>
<%@ page import="com.stimulsoft.report.dictionary.StiDictionary" %>
<%@ page import="com.stimulsoft.report.dictionary.dataSources.StiDataSource" %>
<%@ page import="com.stimulsoft.report.dictionary.dataSources.StiDataSourcesCollection" %>
<%@ page import="com.stimulsoft.report.dictionary.databases.StiDatabase" %>
<%@ page import="com.stimulsoft.report.dictionary.databases.StiDatabaseCollection" %>
<%@ page import="com.stimulsoft.report.dictionary.databases.StiJDBCDatabase" %>
<%@ page import="com.stimulsoft.webviewer.StiWebViewerOptions" %>
<%@ page import="java.io.ByteArrayInputStream" %>
<%@ page import="java.net.URLDecoder" %>
<%@ page import="java.util.*" %>
<%@ page import="com.csicit.ace.data.persistent.service.SysGroupAppServiceD" %>
<%@ page import="com.csicit.ace.data.persistent.service.impl.SysGroupAppServiceDImpl" %>
<%@ page import="com.csicit.ace.data.persistent.service.SysGroupDatasourceService" %>
<%@ page import="com.csicit.ace.data.persistent.service.impl.SysGroupDatasourceServiceImpl" %>
<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://stimulsoft.com/webviewer" prefix="stiwebviewer" %>
<%@ taglib uri="http://stimulsoft.com/acewebviewer" prefix="acestiwebviewer" %>
<html xmlns="http://www.w3.org/1999/xhtml">

<head>
    <title>Stimulsoft Reports for Java</title>
    <acestiwebviewer:resources/>
    <%--<stiwebviewer:resources />--%>
    <style type="text/css">
        .t1 td {
            padding-right: 30px
        }
    </style>
</head>
<body>

<%
    StiReport report = null;
    ReportService reportService = SpringContextUtils.getBean(ReportServiceImpl.class);
    SysGroupAppServiceD sysGroupAppServiceD = SpringContextUtils.getBean(SysGroupAppServiceDImpl.class);
    SysGroupDatasourceService sysGroupDatasourceService = SpringContextUtils.getBean(SysGroupDatasourceServiceImpl.class);
    String query = request.getQueryString();
    String queryString = URLDecoder.decode(query, "UTF-8");
    Map<String, Object> params = MapUtils.getUrlParams(queryString);
    String id = params.get("reportId").toString();
    ReportInfoDO reportInfo = reportService.getById(id);
    String mrtStr = reportInfo.getMrtStr();
    if (StringUtils.isEmpty(mrtStr)) {
        report = new StiReport();
    } else {
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(mrtStr.getBytes("utf-8"));
        report = StiSerializeManager.deserializeReport(byteArrayInputStream);
        StiDictionary dictionary = report.getDictionary();
        String dsId = StringUtils.isEmpty(reportInfo.getDatasourceId()) ?
                sysGroupAppServiceD.getById(reportInfo.getAppId()).getDatasourceId() : reportInfo.getDatasourceId();
        String[] dsIds = dsId.split(",");
        StiDatabaseCollection databases = dictionary.getDatabases();
        List<SysGroupDatasourceDO> datasourceList =
                sysGroupDatasourceService.list(new QueryWrapper<SysGroupDatasourceDO>().in("id", Arrays.asList(dsIds)));
        //获取报表中有数据源中没有连接数据源
        List<SysGroupDatasourceDO> adds = new ArrayList<>(16);
        //获取需要新增的连接数据源
        List<StiDatabase> dels = new ArrayList<>(16);
        for (int i = 0; i < databases.size(); i++) {
            StiJDBCDatabase db = (StiJDBCDatabase) databases.get(i);
            boolean flag = false;
            for (SysGroupDatasourceDO ds : datasourceList) {
                if (Objects.equals(db.getName(), ds.getName())) {
                    String url = sysGroupDatasourceService.getDsUrl(ds);
                    db.setConnectionString(url);
                    db.setName(ds.getName());
                    db.setAlias(ds.getName());
                    flag = true;
                }
            }
            if (flag) {
                continue;
            }
            dels.add(db);
        }
        for (SysGroupDatasourceDO ds : datasourceList) {
            boolean flag = false;
            for (StiDatabase db : databases) {
                if (Objects.equals(db.getName(), ds.getName())) {
                    flag = true;
                }
            }
            if (flag) {
                continue;
            }
            adds.add(ds);
        }
        StiDataSourcesCollection dataSources = dictionary.getDataSources();
        //报表中需要删除的数据源表集合
        List<StiDataSource> deletes = new ArrayList<>(16);
        for (StiDatabase db : dels) {
            for (StiDataSource stiDataSource : dataSources) {
                if (Objects.equals(stiDataSource.GetCategoryName(), db.getName())) {
                    deletes.add(stiDataSource);
                }
            }
        }
        dataSources.removeAll(deletes);
        databases.removeAll(dels);

        for (SysGroupDatasourceDO ds : adds) {
            String url = sysGroupDatasourceService.getDsUrl(ds);
            StiJDBCDatabase stiDatabase = new StiJDBCDatabase();
            stiDatabase.setConnectionString(url);
            stiDatabase.setName(ds.getName());
            stiDatabase.setAlias(ds.getName());
            databases.add(stiDatabase);
        }

        if (params.get("dataSource") != null) {
            String dataSourceStr = params.get("dataSource").toString();
            if (!dataSourceStr.contains(".")) {
                StiDataSource stiDataSource = dataSources.get(params.get("dataSource").toString());
                if (Objects.isNull(stiDataSource)) {
                    throw new RException("数据源:" + params.get("dataSource").toString() + "获取不到!");
                }
                StiDataParametersCollection parameters =
                        stiDataSource.getParameters();
                Set<Map.Entry<String, Object>> maps = params.entrySet();
                for (Map.Entry<String, Object> map : maps) {
                    StiDataParameter param = parameters.getByName(map.getKey());
                    if (param == null) {
                        continue;
                    }
                    int type = param.getType();
                    //类型13是字符串
                    if (type == 13) {
                        parameters.getByName(map.getKey()).setValue("{\"" + map.getValue() + "\"}");
                    } else {
                        parameters.getByName(map.getKey()).setValue("{" + map.getValue() + "}");
                    }
                }
            } else {
                String[] dataSourceStrs = dataSourceStr.split(".");
                for (int i = 0; i < dataSourceStrs.length; i++) {
                    StiDataSource stiDataSource = dataSources.get(dataSourceStrs[i]);
                    if (Objects.isNull(stiDataSource)) {
                        throw new RException("数据源:" + dataSourceStrs[i] + "获取不到!");
                    }
                    StiDataParametersCollection parameters =
                            stiDataSource.getParameters();
                    Set<Map.Entry<String, Object>> maps = params.entrySet();
                    for (Map.Entry<String, Object> map : maps) {
                        String paramName = map.getKey().replace(dataSourceStrs[i] + ".", "");
                        StiDataParameter param = parameters.getByName(paramName);
                        if (param == null) {
                            continue;
                        }
                        int type = param.getType();
                        //类型13是字符串
                        if (type == 13) {
                            parameters.getByName(paramName).setValue("{\"" + map.getValue() + "\"}");
                        } else {
                            parameters.getByName(paramName).setValue("{" + map.getValue() + "}");
                        }
                    }
                }
            }

        }

    }


    StiWebViewerOptions options = new StiWebViewerOptions();
    try {
        options.setLocalizationStream(Constants.getXmlStr());
    } catch (Exception e) {

    }
    StiWebViewerOptions.ToolbarOptions toolbar = options.getToolbar();
    toolbar.setShowAboutButton(false);
    StiWebViewerOptions.AppearanceOptions appearance = options.getAppearance();
    appearance.setFullScreenMode(true);
    appearance.setShowTooltipsHelp(false);
    pageContext.setAttribute("report", report);
    pageContext.setAttribute("options", options);
%>
<acestiwebviewer:acewebviewer report="${report}" options="${options}"/>
</body>

</html>
package com.csicit.ace.report.core.flash;

import com.alibaba.druid.util.StringUtils;
import com.csicit.ace.common.config.SpringContextUtils;
import com.csicit.ace.common.exception.RException;
import com.csicit.ace.common.pojo.domain.ReportInfoDO;
import com.csicit.ace.common.utils.MapUtils;
import com.csicit.ace.report.core.service.ReportService;
import com.csicit.ace.report.core.service.impl.ReportServiceImpl;
import com.stimulsoft.flex.StiLoadAction;
import com.stimulsoft.report.StiReport;
import com.stimulsoft.report.StiSerializeManager;
import com.stimulsoft.report.dictionary.StiDataParameter;
import com.stimulsoft.report.dictionary.StiDataParametersCollection;
import com.stimulsoft.report.dictionary.dataSources.StiDataSource;
import com.stimulsoft.report.dictionary.databases.StiJDBCDatabase;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.URLDecoder;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

/**
 * MyLoadAction.
 * flash 版本加载报表
 * @author shawnj
 */
public class MyLoadAction extends StiLoadAction {
    ReportService reportService = SpringContextUtils.getBean(ReportServiceImpl.class);
    @Override
    public InputStream load(String reportName) {
        try {
            StiReport report = null;
            String query = URLDecoder.decode(reportName, "UTF-8");
            Map<String, Object> params = MapUtils.getUrlParams(query);
            ReportInfoDO reportInfoDO = reportService.getById(params.get("reportId").toString());
            if(reportInfoDO!=null&&!StringUtils.isEmpty(reportInfoDO.getMrtStr())){
                ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(reportInfoDO.getMrtStr().getBytes("utf-8"));
                report = StiSerializeManager.deserializeReport(byteArrayInputStream);
                if(params.get("jdbcDriver")!=null){
                    String jdbcUrl = params.get("jdbcUrl").toString();
                    String user = params.get("jdbcUser").toString();
                    String password = params.get("jdbcPassword").toString();
                    String driver = params.get("jdbcDriver").toString();
                    String url;
                    if(driver.contains("oracle")){
                        url = String.format("url=jdbc:oracle:thin:@%s;user=%s;password=%s;driver=%s",
                                jdbcUrl,user,password,driver);
                    }else{
                        int i = driver.indexOf('.');
                        String type = driver.substring(0,i);
                        url = String.format("url=jdbc:%s://%s;user=%s;password=%s;driver=%s",
                                type,jdbcUrl,user,password,driver);
                    }
                    StiJDBCDatabase stiDatabase = (StiJDBCDatabase)report.getDictionary().getDatabases().get(0);
                    stiDatabase.setConnectionString(url);
                }
            }else{
                report = new StiReport();
            }
            if(params.get("dataSource")!=null){
                String dataSourceStr = params.get("dataSource").toString();
                if(!dataSourceStr.contains(".")){
                    StiDataSource stiDataSource = report.dictionary.dataSources.get(params.get("dataSource").toString());
                    if (Objects.isNull(stiDataSource)){
                        throw new RException("数据源:"+params.get("dataSource").toString()+"获取不到!");
                    }
                    StiDataParametersCollection parameters =
                            stiDataSource.getParameters();
                    Set<Map.Entry<String, Object>> maps = params.entrySet();
                    for (Map.Entry<String, Object> map:maps){
                        StiDataParameter param = parameters.getByName(map.getKey());
                        if(param==null){
                            continue;
                        }
                        int type = param.getType();
                        //类型13是字符串
                        if(type==13){
                            parameters.getByName(map.getKey()).setValue("{\""+map.getValue()+"\"}");
                        }else{
                            parameters.getByName(map.getKey()).setValue("{"+map.getValue()+"}");
                        }
                    }
                }else{
                    String[] dataSources = dataSourceStr.split(".");
                    for (int i=0;i<dataSources.length;i++){
                        StiDataSource stiDataSource = report.dictionary.dataSources.get(dataSources[i]);
                        if (Objects.isNull(stiDataSource)){
                            throw new RException("数据源:"+dataSources[i]+"获取不到!");
                        }
                        StiDataParametersCollection parameters =
                                stiDataSource.getParameters();
                        Set<Map.Entry<String, Object>> maps = params.entrySet();
                        for (Map.Entry<String, Object> map:maps){
                            String paramName = map.getKey().replace(dataSources[i]+".","");
                            StiDataParameter param = parameters.getByName(paramName);
                            if(param==null){
                                continue;
                            }
                            int type = param.getType();
                            //类型13是字符串
                            if(type==13){
                                parameters.getByName(paramName).setValue("{\""+map.getValue()+"\"}");
                            }else{
                                parameters.getByName(paramName).setValue("{"+map.getValue()+"}");
                            }
                        }
                    }
                }
            }
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            StiSerializeManager.serializeReport(report, out);
            return new ByteArrayInputStream(out.toByteArray());
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

}

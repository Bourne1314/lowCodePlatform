package com.csicit.ace.report.core.h5;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.csicit.ace.common.config.SpringContextUtils;
import com.csicit.ace.common.pojo.domain.ReportInfoDO;
import com.csicit.ace.common.pojo.domain.SysGroupDatasourceDO;
import com.csicit.ace.data.persistent.service.SysGroupAppServiceD;
import com.csicit.ace.data.persistent.service.SysGroupDatasourceService;
import com.csicit.ace.data.persistent.service.impl.SysGroupAppServiceDImpl;
import com.csicit.ace.data.persistent.service.impl.SysGroupDatasourceServiceImpl;
import com.csicit.ace.report.core.service.ReportService;
import com.csicit.ace.report.core.service.impl.ReportServiceImpl;
import com.stimulsoft.report.StiReport;
import com.stimulsoft.report.StiSerializeManager;
import com.stimulsoft.report.dictionary.StiDictionary;
import com.stimulsoft.report.dictionary.dataSources.StiDataSource;
import com.stimulsoft.report.dictionary.dataSources.StiDataSourcesCollection;
import com.stimulsoft.report.dictionary.databases.StiDatabase;
import com.stimulsoft.report.dictionary.databases.StiDatabaseCollection;
import com.stimulsoft.report.dictionary.databases.StiJDBCDatabase;
import com.stimulsoft.web.classes.StiRequestParams;
import com.stimulsoft.webdesigner.StiWebDesigerHandler;
import org.apache.commons.lang3.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

/**
 * 报表设计器处理类
 *
 * @author shanwj
 * @date 2019/8/8 15:40
 */
public class AceStiWebDesigerHandler implements StiWebDesigerHandler {

  private static final String DEFAULT_MRTSTR =
      "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?><StiSerializer application=\"StiReport\" type=\"Java\" version=\"1.2\"><CalculationMode>Interpretation</CalculationMode><Dictionary Ref=\"1\" isKey=\"true\" type=\"Dictionary\"><BusinessObjects count=\"0\" isList=\"true\"/><Databases count=\"0\" isList=\"true\"/><DataSources count=\"0\" isList=\"true\"/><Relations count=\"0\" isList=\"true\"/><Report isRef=\"0\"/><Resources count=\"0\" isList=\"true\"/><Variables count=\"0\" isList=\"true\"/></Dictionary><EngineVersion>EngineV2</EngineVersion><GlobalizationStrings count=\"0\" isList=\"true\"/><IsSubmit>False</IsSubmit><JsonReport>False</JsonReport><MetaTags count=\"0\" isList=\"true\"/><Pages count=\"1\" isList=\"true\"><Page1 Ref=\"2\" isKey=\"true\" type=\"Page\"><Anchor>Top, Left</Anchor><Border>None;Black;2.0;Solid;False;4.0;Black</Border><Brush>Transparent</Brush><Components count=\"0\" isList=\"true\"/><Conditions count=\"0\" isList=\"true\"/><Guid>09c78a0db8a44216a1227efb3d0c572f</Guid><Margins>1,1,1,1</Margins><MirrorMargins>False</MirrorMargins><Name>Page1</Name><Page isRef=\"2\"/><PageHeight>29.69</PageHeight><PageWidth>21.01</PageWidth><Report isRef=\"0\"/><Watermark Ref=\"3\" isKey=\"true\" type=\"Stimulsoft.Report.Components.StiWatermark\"><EnabledExpression/><Font>Arial,100</Font><ImageHyperlink/><TextBrush>[50:0:0:0]</TextBrush></Watermark></Page1></Pages><PrinterSettings Ref=\"4\" isKey=\"true\" type=\"Stimulsoft.Report.Print.StiPrinterSettings\"/><ReferencedAssemblies count=\"8\" isList=\"true\"><value>System.Dll</value><value>System.Drawing.Dll</value><value>System.Windows.Forms.Dll</value><value>System.Data.Dll</value><value>System.Xml.Dll</value><value>Stimulsoft.Controls.Dll</value><value>Stimulsoft.Base.Dll</value><value>Stimulsoft.Report.Dll</value></ReferencedAssemblies><RefreshTime>0</RefreshTime><ReportAlias>Report</ReportAlias><ReportChanged>05/29/2020 09:32:10 AM</ReportChanged><ReportCreated>05/29/2020 09:32:07 AM</ReportCreated><ReportFile>Report.mrt</ReportFile><ReportName>Report</ReportName><ReportResources count=\"0\" isList=\"true\"/><ReportUnit>Centimeters</ReportUnit><ReportVersion>2019.3.3 from 15 七月 2019, Java</ReportVersion><RetrieveOnlyUsedData>False</RetrieveOnlyUsedData><Script>using System;\n"
          + "using System.Drawing;\n"
          + "using System.Windows.Forms;\n"
          + "using System.Data;\n"
          + "using Stimulsoft.Controls;\n"
          + "using Stimulsoft.Base.Drawing;\n"
          + "using Stimulsoft.Report;\n"
          + "using Stimulsoft.Report.Dialogs;\n"
          + "using Stimulsoft.Report.Components;\n"
          + "\n"
          + "namespace Reports\n"
          + "{\n"
          + "    public class Report : Stimulsoft.Report.StiReport\n"
          + "    {\n"
          + "        \n"
          + "        public Report()\n"
          + "        {\n"
          + "            this.InitializeComponent();\n"
          + "        }\n"
          + "\n"
          + "        #region StiReport Designer generated code - do not modify\n"
          + "        #endregion StiReport Designer generated code - do not modify\n"
          + "    }\n"
          + "}\n"
          + "</Script><ScriptLanguage>CSharp</ScriptLanguage><Styles count=\"0\" isList=\"true\"/></StiSerializer>";
    private HttpServletRequest request;
    ReportService reportService = SpringContextUtils.getBean(ReportServiceImpl.class);
    SysGroupAppServiceD sysGroupAppServiceD = SpringContextUtils.getBean(SysGroupAppServiceDImpl.class);
    SysGroupDatasourceService sysGroupDatasourceService = SpringContextUtils.getBean(SysGroupDatasourceServiceImpl.class);
    public AceStiWebDesigerHandler(HttpServletRequest request) {
        this.request = request;
    }
    @Override
    public StiReport getEditedReport(HttpServletRequest request) {
        try {
            StiReport report = null;
            String id = request.getQueryString().split("&")[0].split("=")[1];
            if(StringUtils.isEmpty(id)){
                return report;
            }
            ReportInfoDO reportInfoDO = reportService.getById(id);
            if(reportInfoDO==null){
                return report;
            }
            //生成报表对象
            String mrtStr = StringUtils.isEmpty(reportInfoDO.getMrtStr())?DEFAULT_MRTSTR:reportInfoDO.getMrtStr();
            ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(mrtStr.getBytes("utf-8"));
            report = StiSerializeManager.deserializeReport(byteArrayInputStream);
            StiDictionary dictionary = report.getDictionary();
            StiDatabaseCollection databases = dictionary.getDatabases();
            //获取报表绑定数据源
            String dsId = StringUtils.isEmpty(reportInfoDO.getDatasourceId())?
                    sysGroupAppServiceD.getById(reportInfoDO.getAppId()).getDatasourceId():reportInfoDO.getDatasourceId();
            String [] dsIds = dsId.split(",");
            List<SysGroupDatasourceDO> datasourceList =
                    sysGroupDatasourceService.list(new QueryWrapper<SysGroupDatasourceDO>().in("id",Arrays.asList(dsIds)));
            //获取报表中有数据源中没有连接数据源
            List<SysGroupDatasourceDO> adds = new ArrayList<>(16);
            //获取需要新增的连接数据源
            List<StiDatabase> dels = new ArrayList<>(16);
            //新增报表
            if(databases.size()==0){
                adds.addAll(datasourceList);
            }else{
                for (int i = 0; i < databases.size(); i++) {
                    StiJDBCDatabase db = (StiJDBCDatabase) databases.get(i);
                    boolean flag = false;
                    for (SysGroupDatasourceDO ds:datasourceList){
                        if(Objects.equals(db.getName(),ds.getName())){
                            String url = sysGroupDatasourceService.getDsUrl(ds);
                            db.setConnectionString(url);
                            db.setName(ds.getName());
                            db.setAlias(ds.getName());
                            flag = true;
                        }
                    }
                    if(flag){
                        continue;
                    }
                    dels.add(db);
                }
                for (SysGroupDatasourceDO ds:datasourceList){
                    boolean flag = false;
                    for (StiDatabase db:databases){
                        if(Objects.equals(db.getName(),ds.getName())){
                            flag = true;
                        }
                    }
                    if(flag){
                        continue;
                    }
                    adds.add(ds);
                }
            }
            StiDataSourcesCollection dataSources = dictionary.getDataSources();
            //报表中需要删除的数据源表集合
            List<StiDataSource> deletes = new ArrayList<>(16);
            for (StiDatabase db : dels){
                for (StiDataSource stiDataSource:dataSources){
                    if(Objects.equals(stiDataSource.GetCategoryName(),db.getName())){
                        deletes.add(stiDataSource);
                    }
                }
            }
            dataSources.removeAll(deletes);
            databases.removeAll(dels);
            for (SysGroupDatasourceDO ds:adds){
                String url = sysGroupDatasourceService.getDsUrl(ds);
                StiJDBCDatabase stiDatabase = new StiJDBCDatabase();
                stiDatabase.setConnectionString(url);
                stiDatabase.setName(ds.getName());
                stiDatabase.setAlias(ds.getName());
                databases.add(stiDatabase);
            }
            return report;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    @Override
    public void onOpenReportTemplate(StiReport report, HttpServletRequest request) {

    }


    @Override
    public void onNewReportTemplate(StiReport report, HttpServletRequest request) {

    }

    @Override
    public void onSaveReportTemplate(StiReport stiReport,
                                     StiRequestParams stiRequestParams,
                                     HttpServletRequest httpServletRequest) {
        try{
            ByteArrayOutputStream bou = new ByteArrayOutputStream();
            StiSerializeManager.serializeReport(stiReport, bou);
            String id = request.getQueryString().split("&")[0].split("=")[1];
            reportService.updateReportMrt(id,new String(bou.toByteArray(), "utf-8"));
            bou.close();
        } catch (Exception e){
            e.printStackTrace();
        }
    }
}

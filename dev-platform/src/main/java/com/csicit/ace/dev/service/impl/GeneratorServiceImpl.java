package com.csicit.ace.dev.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.csicit.ace.common.pojo.domain.dev.*;
import com.csicit.ace.dev.service.*;
import com.csicit.ace.dev.util.GenCodeUtils;
import com.csicit.ace.dev.util.GenUtils;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.ServletOutputStream;
import java.util.List;
import java.util.zip.ZipOutputStream;

/**
 * 实例对象访问接口实现
 *
 * @author generator
 * @version V1.0
 * @date 2019-04-15 17:27:00
 */
@Service
public class GeneratorServiceImpl implements GeneratorService {
//    @Autowired
//    AceDBHelperMapper dbHelperMapper;

    @Autowired
    MetaTableService metaTableService;
    @Autowired
    MetaTableColService metaTableColService;
    @Autowired
    MetaDatasourceService metaDatasourceService;


    @Autowired
    ProServiceService proServiceService;
    @Autowired
    ProDatasourceService proDatasourceService;
    @Autowired
    ProModelService proModelService;
    @Autowired
    ProModelColService proModelColService;

//    @Override
//    public byte[] generatorCode(String appId, String tableId) {
//        List<String> ids = new ArrayList<>(1);
//        ids.add(tableId);
//        return generatorCodes(appId, ids);
//    }

    //    @Override
//    public byte[] generatorCodes(String appId, List<String> tableIds) {
//        ProServiceDO app = proServiceService.getById(appId);
//        List<MetaTableDO> tables = metaTableService.listTablesByIds(tableIds);
//        return getGeneratorCodes(app, tables);
//    }
    @Override
    public void generatorCodes(String appId, ServletOutputStream outputStream) {
        ProServiceDO app = proServiceService.getById(appId);
        ZipOutputStream zip = new ZipOutputStream(outputStream);
        ProDatasourceDO datasourceDO = proDatasourceService.getOne(new QueryWrapper<ProDatasourceDO>()
                .eq("IS_MAJOR", 1).eq("service_id", appId));
        List<ProModelDO> proModelDOS = proModelService.list(new QueryWrapper<ProModelDO>().eq("service_id", app.getId
                ()));
        proModelDOS.stream().forEach(table -> {
            table.setProModelColDOS(proModelColService.list(new QueryWrapper<ProModelColDO>()
                    .eq("model_id", table.getId())));
        });
        GenCodeUtils.generatorCodeByService(proModelDOS, app, zip, datasourceDO);
        IOUtils.closeQuietly(zip);
        System.out.println("---generatorCode over---");
    }

//    @Override
//    public boolean publishService(List<String> modelIdList, String appId) {
//        long start = System.currentTimeMillis();
//        List<MetaTableDO> tables = new ArrayList<>(16);
////        modelIdList.stream().forEach(modelId -> {
////            tables.add(getMetaTableCols(modelId));
////        });
//
//        ProServiceDO devAppDO = proServiceService.getById(appId);
//        // 生成代码
//        publishServiceCodes(devAppDO, tables);
//
//        String fileName = DevConstants.PUBLISH_SERVICE_LOCAL_CODE_ADDRESS + "\\" + devAppDO.getAppId();
//        String createJar = "mvn clean package";
//        String jarAddress = DevConstants.PUBLISH_SERVICE_LOCAL_CODE_ADDRESS + "\\" + devAppDO.getAppId() +
//                "\\target\\" + devAppDO.getAppId() + "-1.0.0.jar";
//        String runCommand = "start \"lock-server\" \"%JAVA_HOME%\\bin\\" + devAppDO.getAppId() + ".exe\" -Xms512m" +
//                " -Xmx512m -Dfile" +
//                ".encoding=utf-8 -jar " + jarAddress;
//        String closeCommand = "taskkill -f -t -im " + devAppDO.getAppId() + ".exe";
//        String copyFileCommand = "copy \"%JAVA_HOME%\\bin\\javaw.exe\" \"%JAVA_HOME%\\bin\\" + devAppDO.getAppId
//                () + "" +
//                ".exe\"";
//
//        // 判断是否该应用生成过服务
//        if (Objects.equals(0, devAppDO.getCreateServiceFlg())) {
//            // 没有生成过服务
//            String result = CMDUtil.excuteCMDCommand("cmd /c cd " + fileName + "&&" +
//                    createJar);
//            if (result.contains("BUILD SUCCESS")) {
//                if (CMDUtil.excuteRunCommand("cmd /c cd " + fileName + "&&" +
//                        copyFileCommand)) {
//                    if (CMDUtil.excuteRunCommand("cmd /c cd " + fileName + "&&" + runCommand)) {
//                        // 设置该应用已发布过服务
//                        devAppDO.setCreateServiceFlg(1);
//                        proServiceService.updateById(devAppDO);
//                        // 添加模型对应的URL服务
//                        proModelServiceService.addModelService(modelIdList, devAppDO.getAppId());
//                    }
//                }
//            } else {
//                throw new RException("MAVEN打包失败！");
//            }
//        } else {
//            // 生成过服务
//            // 关闭服务
//            if (CMDUtil.excuteRunCommand("cmd /c cd " + fileName + "&&" + closeCommand)) {
//                // 重新jar
//                String result = CMDUtil.excuteCMDCommand("cmd /c cd " + fileName +
//                        "&&" + createJar);
//                if (result.contains("BUILD SUCCESS")) {
//                    if (CMDUtil.excuteRunCommand("cmd /c cd " + fileName + "&&" + runCommand)) {
//                        // 添加模型对应的URL服务
//                        proModelServiceService.addModelService(modelIdList, devAppDO.getAppId());
//                    }
//                } else {
//                    throw new RException("MAVEN打包失败！");
//                }
//            }
//        }
//        long time = System.currentTimeMillis() - start;
//        System.out.println("发布时长:" + time);
//        return true;
//    }

//    @Override
//    public byte[] genService(List<String> modelIdList, String appId) {
//        List<MetaTableDO> tables = new ArrayList<>(16);
////        modelIdList.stream().forEach(modelId -> {
////            tables.add(getMetaTableCols(modelId));
////        });
//        ProServiceDO app = proServiceService.getById(appId);
//        return getGeneratorCodes(app, tables);
//    }

//    private MetaTableDO getMetaTableCols(String modelId) {
//        MetaTableDO tableDO = metaTableService.getById(proModelService.getById(modelId).getTableId());
//        List<MetaTableColDO> tableCols =
//                metaTableColService.list(new QueryWrapper<MetaTableColDO>().eq("table_id", tableDO.getId()));
//        List<ProModelFieldDO> proModelFieldDOS = proModelFieldService.list(new QueryWrapper<ProModelFieldDO>()
//                .eq("model_id", modelId).eq("db_field_flg", 0));
//        proModelFieldDOS.stream().forEach(restModelFieldDO -> {
//            MetaTableColDO metaTableColDO = new MetaTableColDO();
//            metaTableColDO.setTabColName(restModelFieldDO.getFieldName());
//            metaTableColDO.setObjColName(restModelFieldDO.getFieldObjName());
//            metaTableColDO.setCaption(restModelFieldDO.getFieldCaption());
//            metaTableColDO.setDataType(restModelFieldDO.getFieldType());
//            metaTableColDO.setDataSize(restModelFieldDO.getFieldLength());
//            metaTableColDO.setDbExistFlg("0");
//            tableCols.add(metaTableColDO);
//        });
//        tableDO.setCols(tableCols);
//        return tableDO;
//    }

//    private void publishServiceCodes(ProServiceDO app, List<MetaTableDO> tables) {
//        MetaDatasourceDO datasourceDO = metaDatasourceService.getById(app.getDsId());
//        for (MetaTableDO table : tables) {
//            GenUtils.publishServiceCodes(table, app, datasourceDO);
//        }
//    }


//    private byte[] getGeneratorCodes(ProServiceDO app, List<MetaTableDO> tables) {
//        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
//        ZipOutputStream zip = new ZipOutputStream(outputStream);
//        MetaDatasourceDO datasourceDO = metaDatasourceService.getById(app.getDsId());
//        for (MetaTableDO table : tables) {
//            GenUtils.generatorCode(table, app, zip, datasourceDO);
//        }
//        IOUtils.closeQuietly(zip);
//        return outputStream.toByteArray();
//    }
}

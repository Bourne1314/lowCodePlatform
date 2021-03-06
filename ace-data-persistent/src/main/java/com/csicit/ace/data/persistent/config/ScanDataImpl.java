package com.csicit.ace.data.persistent.config;

import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.csicit.ace.common.AppUpgradeJaxb.*;
import com.csicit.ace.common.constant.Constants;
import com.csicit.ace.common.exception.RException;
import com.csicit.ace.common.log.AceLogger;
import com.csicit.ace.common.pojo.domain.SysApiResourceDO;
import com.csicit.ace.common.pojo.domain.SysConfigDO;
import com.csicit.ace.common.pojo.domain.SysGroupAppDO;
import com.csicit.ace.common.pojo.domain.SysMsgTypeExtendDO;
import com.csicit.ace.common.pojo.vo.InitStorageDataVO;
import com.csicit.ace.common.pojo.vo.ScheduledVO;
import com.csicit.ace.data.persistent.service.*;
import com.csicit.ace.dbplus.config.IScanData;
import dm.jdbc.dbaccess.Auth;
import net.bytebuddy.asm.Advice;
import org.apache.commons.collections.CollectionUtils;
import org.checkerframework.checker.units.qual.A;
import org.checkerframework.checker.units.qual.C;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Objects;


/**
 * @author shanwj
 * @date 2020/4/14 17:23
 */
@Service
public class ScanDataImpl implements IScanData {

    @Autowired
    SysApiResourceServiceD sysApiResourceServiceD;

    @Autowired
    SysScheduledServiceD sysScheduledServiceD;

    @Autowired
    SysMsgTypeExtendService sysMsgTypeExtendService;

    @Autowired
    SysConfigServiceD sysConfigServiceD;

    @Autowired
    SysMsgSendTypeService sysMsgSendTypeService;

    @Autowired
    SysGroupAppServiceD sysGroupAppServiceD;

    @Autowired
    public AceLogger aceLogger;

    @Override
    public boolean saveInitScanData(InitStorageDataVO storageData) {
        List<SysApiResourceDO> apis = storageData.getApis();
        List<SysMsgTypeExtendDO> msgExtends = storageData.getMsgExtends();
        List<ScheduledVO> scheduleds = storageData.getScheduleds();
        List<SysConfigDO> sysConfigs = storageData.getSysConfigs();
        if (!saveApis(apis, storageData.getAppName())) {
            throw new RException("?????????API????????????!");
        }
        if (!saveMsgExtends(msgExtends, storageData.getAppName())) {
            throw new RException("??????????????????????????????????????????!");
        }
        if (!saveScheduleds(scheduleds, storageData.getAppName())) {
            throw new RException("???????????????????????????!");
        }
        if (!saveConfigs(sysConfigs)) {
            throw new RException("????????????????????????!");
        }
        if (!Constants.AppNames.contains(storageData.getAppName()) &&
                !createBpmChannelAndTemplate(storageData.getAppName())) {
            throw new RException("??????????????????????????????????????????!");
        }
        /**
         * ?????????????????????????????????????????????????????????
         */
        sysConfigServiceD.initConfig();
        return true;
    }

    private boolean saveApis(List<SysApiResourceDO> apis, String appId) {
        return sysApiResourceServiceD.saveApis(apis, appId);
    }

    private boolean saveMsgExtends(List<SysMsgTypeExtendDO> msgExtends, String appId) {
        return sysMsgTypeExtendService.saveMsgExtends(msgExtends, appId);
    }

    private boolean saveScheduleds(List<ScheduledVO> scheduleds, String appId) {
        return sysScheduledServiceD.saveScheduleds(scheduleds, appId);
    }

    private boolean saveConfigs(List<SysConfigDO> sysConfigs) {
        sysConfigServiceD.saveScanConfig(sysConfigs);
        return true;
    }

    private boolean createBpmChannelAndTemplate(String appId) {
        return sysMsgSendTypeService.createBpmChannelAndTemplate(appId);
    }

    @Override
    public boolean lockApp(Map<String, String> map) {
        String appName = map.get("appName");
        String version = map.get("version");
        // ??????????????????????????????????????????????????????????????????????????????
        SysGroupAppDO sysGroupAppDO = sysGroupAppServiceD.getById(appName);
        if (!Objects.equals(version, sysGroupAppDO.getVersion())) {
            return sysGroupAppServiceD.lockApp(appName);
//                return sysGroupAppServiceD.update(new SysGroupAppDO(),
//                        new UpdateWrapper<SysGroupAppDO>()
//                                .set("VERSION", version).eq("id", appName));
        }
        return false;
    }

    @Override
    public boolean unLockApp(Map<String, String> map) {
        return sysGroupAppServiceD.unLockApp(map.get("appName"));
    }

    @Override
    public boolean updAppVersion(Map<String, String> map) {
        return sysGroupAppServiceD.update(new SysGroupAppDO(),
                new UpdateWrapper<SysGroupAppDO>()
                        .set("VERSION", map.get("version")).eq("id", map.get("appName")));
    }

    @Override
    public boolean updateDbItem(AppUpgrade appUpgrade) {
        String appId = appUpgrade.getAppId();
        // ??????????????? ??????
        if (CollectionUtils.isNotEmpty(appUpgrade.getGroupDatasource())) {
            if (!groupDatasourceUpdate(appUpgrade.getGroupDatasource(), appId)) {
                aceLogger.error("????????????????????????????????????????????????");
                return false;
            }
        }
        // ???????????? ??????
        if (CollectionUtils.isNotEmpty(appUpgrade.getComponentRegister())) {
            if (!componentRegisterUpdate(appUpgrade.getComponentRegister(), appId)) {
                aceLogger.error("????????????????????????????????????????????????");
                return false;
            }
        }

        // ?????? ??????
        if (CollectionUtils.isNotEmpty(appUpgrade.getAuth())) {
            if (!authUpdate(appUpgrade.getAuth(), appId)) {
                aceLogger.error("??????????????????????????????????????????");
                return false;
            }
        }

        // API ??????
        if (CollectionUtils.isNotEmpty(appUpgrade.getApiResource())) {
            if (!apiResourceUpdate(appUpgrade.getApiResource(), appId)) {
                aceLogger.error("?????????????????????API???????????????");
                return false;
            }
        }

        // ??????API ??????
        if (CollectionUtils.isNotEmpty(appUpgrade.getAuthApi())) {
            if (!authApiUpdate(appUpgrade.getAuthApi(), appId)) {
                aceLogger.error("???????????????????????????API???????????????");
                return false;
            }
        }

        // ???????????? ??????
        if (CollectionUtils.isNotEmpty(appUpgrade.getMenu())) {
            if (!menuUpdate(appUpgrade.getMenu(), appId)) {
                aceLogger.error("??????????????????????????????????????????");
                return false;
            }
        }

        // ?????? ??????
        if (CollectionUtils.isNotEmpty(appUpgrade.getRole())) {
            if (!roleUpdate(appUpgrade.getRole(), appId)) {
                aceLogger.error("??????????????????????????????????????????");
                return false;
            }
        }

        //??????????????? ??????
        if (CollectionUtils.isNotEmpty(appUpgrade.getRoleRelation())) {
            if (!roleRelationUpdate(appUpgrade.getRoleRelation(), appId)) {
                aceLogger.error("????????????????????????????????????????????????");
                return false;
            }
        }

        // ???????????? ??????
        if (CollectionUtils.isNotEmpty(appUpgrade.getRoleMutex())) {
            if (!roleMutexUpdate(appUpgrade.getRoleMutex(), appId)) {
                aceLogger.error("??????????????????????????????????????????????????????");
                return false;
            }
        }

        // ???????????? ??????
        if (!authRoleUpdate(appUpgrade)) {
            aceLogger.error("????????????????????????????????????????????????");
            return false;
        }

        // ????????????
        if (CollectionUtils.isNotEmpty(appUpgrade.getFileConfiguration())) {
            if (!fileConfigurationUpdate(appUpgrade.getFileConfiguration(), appId)) {
                aceLogger.error("????????????????????????????????????????????????");
                return false;
            }
        }

        // ????????????
        if (CollectionUtils.isNotEmpty(appUpgrade.getQrtzConfig())) {
            if (!qrtzCongfigUpdate(appUpgrade.getQrtzConfig(), appId)) {
                aceLogger.error("????????????????????????????????????????????????");
                return false;
            }
        }

        // ????????????
        if (CollectionUtils.isNotEmpty(appUpgrade.getMsgSendType())) {
            if (!msgSendTypeUpdate(appUpgrade.getMsgSendType(), appId)) {
                aceLogger.error("????????????????????????????????????????????????");
                return false;
            }
        }

        // ????????????
        if (CollectionUtils.isNotEmpty(appUpgrade.getMsgTemplate())) {
            if (!msgTemplateUpdate(appUpgrade.getMsgTemplate(), appUpgrade.getMsgTemplateConfig(), appId)) {
                aceLogger.error("????????????????????????????????????????????????");
                return false;
            }
        }

        // ????????????
        if (CollectionUtils.isNotEmpty(appUpgrade.getMsgTypeExtend())) {
            if (!msgTypeExtendUpdate(appUpgrade.getMsgTypeExtend(), appId)) {
                aceLogger.error("????????????????????????????????????????????????");
                return false;
            }
        }

        // ????????????
        if (CollectionUtils.isNotEmpty(appUpgrade.getDict())) {
            if (!dictUpdate(appUpgrade.getDict(), appUpgrade.getDictValue(), appId)) {
                aceLogger.error("????????????????????????????????????????????????");
                return false;
            }
        }

        // ????????????
        if (CollectionUtils.isNotEmpty(appUpgrade.getServices())) {
            if (!modelUpdate(appUpgrade)) {
                aceLogger.error("????????????????????????????????????????????????");
                return false;
            }
        }

        // ????????????
        if (CollectionUtils.isNotEmpty(appUpgrade.getSysConfig())) {
            if (!sysConfigUpdate(appUpgrade.getSysConfig(), appId)) {
                aceLogger.error("????????????????????????????????????????????????");
                return false;
            }
        }

        // ????????????
        if (CollectionUtils.isNotEmpty(appUpgrade.getReportType())) {
            if (!reportTypeUpdate(appUpgrade.getReportType(), appId)) {
                aceLogger.error("????????????????????????????????????????????????");
                return false;
            }
        }

        //  ??????/???????????????
        if (CollectionUtils.isNotEmpty(appUpgrade.getReportInfo())) {
            if (!reportInfoUpdate(appUpgrade.getReportInfo(), appId)) {
                aceLogger.error("???????????????????????????/??????????????????????????????");
                return false;
            }
        }

        // ??????
        if (CollectionUtils.isNotEmpty(appUpgrade.getAppInterface())) {
            if (!interfaceUpdate(appUpgrade)) {
                aceLogger.error("????????????????????????????????????????????????");
                return false;
            }
        }

        // ??????
        if (CollectionUtils.isNotEmpty(appUpgrade.getBladeVisual())) {
            if (!bladeVisualUpdate(appUpgrade)) {
                aceLogger.error("????????????????????????????????????????????????");
                return false;
            }
        }

        // ????????????
        if (CollectionUtils.isNotEmpty(appUpgrade.getCodeSeq())) {
            if (!codeSeqUpdate(appUpgrade.getCodeSeq(), appId)) {
                aceLogger.error("????????????????????????????????????????????????");
                return false;
            }
        }

        // ????????????
        if (CollectionUtils.isNotEmpty(appUpgrade.getCodeTemplate())) {
            if (!codeTemplateUpdate(appUpgrade)) {
                aceLogger.error("????????????????????????????????????????????????");
                return false;
            }
        }

        return true;
    }

    @Autowired
    CodeSequenceService codeSequenceService;

    private boolean codeSeqUpdate(List<CodeSeqDetail> codeSeqDetails, String appId) {
        return codeSequenceService.codeSeqUpdate(codeSeqDetails, appId);
    }

    @Autowired
    CodeTemplateService codeTemplateService;

    private boolean codeTemplateUpdate(AppUpgrade appUpgrade) {
        return codeTemplateService.codeTemplateUpdate(appUpgrade);
    }

    @Autowired
    BladeVisualServiceD bladeVisualServiceD;

    private boolean bladeVisualUpdate(AppUpgrade appUpgrade) {
        return bladeVisualServiceD.bladeVisualUpdate(appUpgrade);
    }

    @Autowired
    SysAppInterfaceServiceD sysAppInterfaceServiceD;

    private boolean interfaceUpdate(AppUpgrade appUpgrade) {
        return sysAppInterfaceServiceD.interfaceUpdate(appUpgrade);
    }


    @Autowired
    ReportInfoServiceD reportInfoServiceD;

    private boolean reportInfoUpdate(List<ReportInfoDetail> reportInfoDetails, String appId) {
        return reportInfoServiceD.reportInfoUpdate(reportInfoDetails, appId);
    }

    @Autowired
    ReportTypeServiceD reportTypeServiceD;

    private boolean reportTypeUpdate(List<ReportTypeDetail> reportTypeDetails, String appId) {
        return reportTypeServiceD.reportTypeUpdate(reportTypeDetails, appId);
    }

    @Autowired
    SysGroupDatasourceService sysGroupDatasourceService;

    private boolean groupDatasourceUpdate(List<GroupDatasourceDetail> groupDatasources, String appId) {
        return sysGroupDatasourceService.groupDatasourceUpdate(groupDatasources, appId);
    }

    @Autowired
    SysComponentRegisterServiceD sysComponentRegisterServiceD;

    private boolean componentRegisterUpdate(List<ComponentRegisterDetail> componentRegisters, String appId) {
        return sysComponentRegisterServiceD.componentRegisterUpdate(componentRegisters, appId);
    }

    @Autowired
    SysAuthServiceD sysAuthServiceD;

    private boolean authUpdate(List<AuthDetail> authDetails, String appId) {
        return sysAuthServiceD.authUpdate(authDetails, appId);
    }

    private boolean apiResourceUpdate(List<ApiResourceDetail> apiResourceDetails, String appId) {
        return sysApiResourceServiceD.apiResourceUpdate(apiResourceDetails, appId);
    }

    @Autowired
    SysAuthApiServiceD sysAuthApiServiceD;

    private boolean authApiUpdate(List<AuthApiDetail> authApiDetails, String appId) {
        return sysAuthApiServiceD.authApiUpdate(authApiDetails, appId);
    }

    @Autowired
    SysMenuServiceD sysMenuServiceD;

    private boolean menuUpdate(List<MenuDetail> menuDetails, String appId) {
        return sysMenuServiceD.menuUpdate(menuDetails, appId);
    }

    @Autowired
    SysRoleServiceD sysRoleServiceD;

    private boolean roleUpdate(List<RoleDetail> roleDetails, String appId) {
        return sysRoleServiceD.roleUpdate(roleDetails, appId);
    }

    @Autowired
    SysRoleRelationServiceD sysRoleRelationServiceD;

    private boolean roleRelationUpdate(List<RoleRelationDetail> roleRelationDetails, String appId) {
        return sysRoleRelationServiceD.roleRelationUpdate(roleRelationDetails, appId);
    }

    @Autowired
    SysRoleMutexServiceD sysRoleMutexServiceD;

    private boolean roleMutexUpdate(List<RoleMutexDetail> roleMutexDetails, String appId) {
        return sysRoleMutexServiceD.roleMutexUpdate(roleMutexDetails, appId);
    }

    @Autowired
    SysAuthRoleLvServiceD sysAuthRoleLvServiceD;

    private boolean authRoleUpdate(AppUpgrade appUpgrade) {
        return sysAuthRoleLvServiceD.authRoleUpdate(appUpgrade);
    }

    @Autowired
    FileConfigurationServiceD fileConfigurationServiceD;

    private boolean fileConfigurationUpdate(List<FileConfigDetail> fileConfigDetails, String appId) {
        return fileConfigurationServiceD.fileConfigurationUpdate(fileConfigDetails, appId);
    }

    @Autowired
    QrtzConfigServiceD qrtzConfigServiceD;

    private boolean qrtzCongfigUpdate(List<QrtzConfigDetail> qrtzConfigDetails, String appId) {
        return qrtzConfigServiceD.qrtzCongfigUpdate(qrtzConfigDetails, appId);
    }

    private boolean msgSendTypeUpdate(List<MsgSendTypeDetail> msgSendTypeDetails, String appId) {
        return sysMsgSendTypeService.msgSendTypeUpdate(msgSendTypeDetails, appId);
    }

    @Autowired
    SysMsgTemplateService sysMsgTemplateService;

    private boolean msgTemplateUpdate(List<MsgTemplateDetail> msgTemplateDetails, List<MsgTemplateConfigDetail>
            msgTemplateConfigDetails, String appId) {
        return sysMsgTemplateService.msgTemplateUpdate(msgTemplateDetails, msgTemplateConfigDetails, appId);
    }

    private boolean msgTypeExtendUpdate(List<MsgTypeExtendDetail> msgTypeExtendDetails, String appId) {
        return sysMsgTypeExtendService.msgTypeExtendUpdate(msgTypeExtendDetails, appId);
    }

    @Autowired
    SysDictServiceD sysDictServiceD;

    private boolean dictUpdate(List<DictDetail> dictDetails, List<DictValueDetail> dictValueDetails, String appId) {
        return sysDictServiceD.dictUpdate(dictDetails, dictValueDetails, appId);
    }

    @Autowired
    ProServiceServiceD proServiceServiceD;

    private boolean modelUpdate(AppUpgrade appUpgrade) {
        return proServiceServiceD.modelUpdate(appUpgrade);
    }

    private boolean sysConfigUpdate(List<SysConfigDetail> sysConfigDetails, String appId) {
        return sysConfigServiceD.sysConfigUpdate(sysConfigDetails, appId);
    }
}

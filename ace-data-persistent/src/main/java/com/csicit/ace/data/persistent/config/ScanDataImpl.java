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
            throw new RException("初始化API资源异常!");
        }
        if (!saveMsgExtends(msgExtends, storageData.getAppName())) {
            throw new RException("初始化自定义消息发送方式异常!");
        }
        if (!saveScheduleds(scheduleds, storageData.getAppName())) {
            throw new RException("初始化工作计划异常!");
        }
        if (!saveConfigs(sysConfigs)) {
            throw new RException("初始化配置项异常!");
        }
        if (!Constants.AppNames.contains(storageData.getAppName()) &&
                !createBpmChannelAndTemplate(storageData.getAppName())) {
            throw new RException("初始化创建工作流消息模板异常!");
        }
        /**
         * 单机版保存完数据应该把配置项加载到缓存
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
        // 判断运行版本与业务平台数据库应用记录的版本号是否一致
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
        // 数据源信息 更新
        if (CollectionUtils.isNotEmpty(appUpgrade.getGroupDatasource())) {
            if (!groupDatasourceUpdate(appUpgrade.getGroupDatasource(), appId)) {
                aceLogger.error("应用版本升级中，数据源更新失败！");
                return false;
            }
        }
        // 组件注册 更新
        if (CollectionUtils.isNotEmpty(appUpgrade.getComponentRegister())) {
            if (!componentRegisterUpdate(appUpgrade.getComponentRegister(), appId)) {
                aceLogger.error("应用版本升级，组件注册更新失败！");
                return false;
            }
        }

        // 权限 更新
        if (CollectionUtils.isNotEmpty(appUpgrade.getAuth())) {
            if (!authUpdate(appUpgrade.getAuth(), appId)) {
                aceLogger.error("应用版本升级，权限更新失败！");
                return false;
            }
        }

        // API 更新
        if (CollectionUtils.isNotEmpty(appUpgrade.getApiResource())) {
            if (!apiResourceUpdate(appUpgrade.getApiResource(), appId)) {
                aceLogger.error("应用版本升级，API更新失败！");
                return false;
            }
        }

        // 权限API 更新
        if (CollectionUtils.isNotEmpty(appUpgrade.getAuthApi())) {
            if (!authApiUpdate(appUpgrade.getAuthApi(), appId)) {
                aceLogger.error("应用版本升级，权限API更新失败！");
                return false;
            }
        }

        // 菜单定义 更新
        if (CollectionUtils.isNotEmpty(appUpgrade.getMenu())) {
            if (!menuUpdate(appUpgrade.getMenu(), appId)) {
                aceLogger.error("应用版本升级，菜单更新失败！");
                return false;
            }
        }

        // 角色 更新
        if (CollectionUtils.isNotEmpty(appUpgrade.getRole())) {
            if (!roleUpdate(appUpgrade.getRole(), appId)) {
                aceLogger.error("应用版本升级，角色更新失败！");
                return false;
            }
        }

        //上下级角色 更新
        if (CollectionUtils.isNotEmpty(appUpgrade.getRoleRelation())) {
            if (!roleRelationUpdate(appUpgrade.getRoleRelation(), appId)) {
                aceLogger.error("应用版本升级，角色关系更新失败！");
                return false;
            }
        }

        // 互斥角色 更新
        if (CollectionUtils.isNotEmpty(appUpgrade.getRoleMutex())) {
            if (!roleMutexUpdate(appUpgrade.getRoleMutex(), appId)) {
                aceLogger.error("应用版本升级，互斥角色关系更新失败！");
                return false;
            }
        }

        // 角色授权 更新
        if (!authRoleUpdate(appUpgrade)) {
            aceLogger.error("应用版本升级，角色授权更新失败！");
            return false;
        }

        // 附件配置
        if (CollectionUtils.isNotEmpty(appUpgrade.getFileConfiguration())) {
            if (!fileConfigurationUpdate(appUpgrade.getFileConfiguration(), appId)) {
                aceLogger.error("应用版本升级，附件配置更新失败！");
                return false;
            }
        }

        // 定时任务
        if (CollectionUtils.isNotEmpty(appUpgrade.getQrtzConfig())) {
            if (!qrtzCongfigUpdate(appUpgrade.getQrtzConfig(), appId)) {
                aceLogger.error("应用版本升级，定时任务更新失败！");
                return false;
            }
        }

        // 消息通道
        if (CollectionUtils.isNotEmpty(appUpgrade.getMsgSendType())) {
            if (!msgSendTypeUpdate(appUpgrade.getMsgSendType(), appId)) {
                aceLogger.error("应用版本升级，消息通道更新失败！");
                return false;
            }
        }

        // 消息模板
        if (CollectionUtils.isNotEmpty(appUpgrade.getMsgTemplate())) {
            if (!msgTemplateUpdate(appUpgrade.getMsgTemplate(), appUpgrade.getMsgTemplateConfig(), appId)) {
                aceLogger.error("应用版本升级，消息模板更新失败！");
                return false;
            }
        }

        // 消息拓展
        if (CollectionUtils.isNotEmpty(appUpgrade.getMsgTypeExtend())) {
            if (!msgTypeExtendUpdate(appUpgrade.getMsgTypeExtend(), appId)) {
                aceLogger.error("应用版本升级，消息扩展更新失败！");
                return false;
            }
        }

        // 数据字典
        if (CollectionUtils.isNotEmpty(appUpgrade.getDict())) {
            if (!dictUpdate(appUpgrade.getDict(), appUpgrade.getDictValue(), appId)) {
                aceLogger.error("应用版本升级，数据字典更新失败！");
                return false;
            }
        }

        // 数据模型
        if (CollectionUtils.isNotEmpty(appUpgrade.getServices())) {
            if (!modelUpdate(appUpgrade)) {
                aceLogger.error("应用版本升级，数据模型更新失败！");
                return false;
            }
        }

        // 系统配置
        if (CollectionUtils.isNotEmpty(appUpgrade.getSysConfig())) {
            if (!sysConfigUpdate(appUpgrade.getSysConfig(), appId)) {
                aceLogger.error("应用版本升级，系统配置更新失败！");
                return false;
            }
        }

        // 业务类型
        if (CollectionUtils.isNotEmpty(appUpgrade.getReportType())) {
            if (!reportTypeUpdate(appUpgrade.getReportType(), appId)) {
                aceLogger.error("应用版本升级，业务类型更新失败！");
                return false;
            }
        }

        //  报表/仪表盘信息
        if (CollectionUtils.isNotEmpty(appUpgrade.getReportInfo())) {
            if (!reportInfoUpdate(appUpgrade.getReportInfo(), appId)) {
                aceLogger.error("应用版本升级，报表/仪表盘信息更新失败！");
                return false;
            }
        }

        // 接口
        if (CollectionUtils.isNotEmpty(appUpgrade.getAppInterface())) {
            if (!interfaceUpdate(appUpgrade)) {
                aceLogger.error("应用版本升级，接口信息更新失败！");
                return false;
            }
        }

        // 大屏
        if (CollectionUtils.isNotEmpty(appUpgrade.getBladeVisual())) {
            if (!bladeVisualUpdate(appUpgrade)) {
                aceLogger.error("应用版本升级，大屏信息更新失败！");
                return false;
            }
        }

        // 数字序列
        if (CollectionUtils.isNotEmpty(appUpgrade.getCodeSeq())) {
            if (!codeSeqUpdate(appUpgrade.getCodeSeq(), appId)) {
                aceLogger.error("应用版本升级，数字序列更新失败！");
                return false;
            }
        }

        // 编码模板
        if (CollectionUtils.isNotEmpty(appUpgrade.getCodeTemplate())) {
            if (!codeTemplateUpdate(appUpgrade)) {
                aceLogger.error("应用版本升级，编码模板更新失败！");
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

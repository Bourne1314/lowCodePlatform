package com.csicit.ace.platform.core.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.csicit.ace.common.AppUpgradeJaxb.*;
import com.csicit.ace.common.config.ZuulRouteConfig;
import com.csicit.ace.common.constant.Constants;
import com.csicit.ace.common.enums.AuditLogType;
import com.csicit.ace.common.enums.AuditLogTypeDO;
import com.csicit.ace.common.exception.RException;
import com.csicit.ace.common.log.AceLogger;
import com.csicit.ace.common.pojo.domain.*;
import com.csicit.ace.common.pojo.domain.dev.*;
import com.csicit.ace.common.utils.*;
import com.csicit.ace.common.utils.internationalization.InternationUtils;
import com.csicit.ace.common.utils.server.R;
import com.csicit.ace.data.persistent.mapper.AceDBHelperMapper;
import com.csicit.ace.data.persistent.mapper.SysGroupAppMapper;
import com.csicit.ace.data.persistent.service.*;
import com.csicit.ace.data.persistent.service.impl.BaseServiceImpl;
import com.csicit.ace.data.persistent.utils.AceSqlUtils;
import com.csicit.ace.platform.core.service.*;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.stereotype.Service;

import javax.servlet.ServletOutputStream;
import java.io.IOException;
import java.io.StringWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * 集团应用库管理 实例对象访问接口实现
 *
 * @author zuogang
 * @version V1.0
 * @date 2019/4/12 17:22
 */
@Service("sysGroupAppService")
public class SysGroupAppServiceImpl extends BaseServiceImpl<SysGroupAppMapper, SysGroupAppDO> implements
        SysGroupAppService {

    @Autowired
    BdAppLibService bdAppLibService;

    @Autowired
    OrgGroupService orgGroupService;

    @Autowired
    SysConfigService sysConfigService;

    @Autowired
    SysAuthScopeAppService sysAuthScopeAppService;

    @Autowired
    SysAuthService sysAuthService;

    @Autowired
    SysUserService sysUserService;

    @Autowired
    SysGroupDatasourceService sysGroupDatasourceService;

    @Autowired
    AceSqlUtils aceSqlUtils;

    @Autowired
    AceDBHelperMapper aceDBHelperMapper;

    @Autowired
    CacheUtil cacheUtil;

    @Autowired
    SysCacheDataService sysCacheDataService;

    @Autowired
    AceDataResolveService aceDataResolveService;

    @Autowired
    OrgOrganizationService orgOrganizationService;

    @Autowired
    SysUserRoleService sysUserRoleService;

    /**
     * 是否开启 自动创建三元
     */
    @Value("${ace.config.autoCreateThree:false}")
    private boolean autoCreateThree;

    /**
     * 设置某集团的默认APP
     *
     * @param params
     * @return
     * @author yansiyang
     * @date 2019/7/21 15:55
     */
    @Override
    public boolean setMainApp(Map<String, String> params) {
        String groupId = params.get("groupId");
        String appId = params.get("appId");
        if (StringUtils.isBlank(groupId) || StringUtils.isBlank(appId)) {
            throw new RException(InternationUtils.getInternationalMsg("EMPTY_ARG"));
        }
        if (update(new SysGroupAppDO(), new UpdateWrapper<SysGroupAppDO>().set("is_main_app", 0).eq("group_id",
                groupId))) {
            if (update(new SysGroupAppDO(), new UpdateWrapper<SysGroupAppDO>().set("is_main_app", 1).eq("id",
                    appId))) {
                return true;
            } else {
                throw new RException(InternationUtils.getInternationalMsg("OPERATE_FAILED"));
            }
        }

        return false;
    }

    /**
     * 产品库导入应用
     *
     * @param params
     * @return boolean
     * @author zuogang
     * @date 2019/5/14 11:15
     */
    @Override
    public boolean saveAppFromBdAppLib(Map<String, Object> params) {
        String groupId = (String) params.get("groupId");
        String appName = (String) params.get("appName");
        String appId = (String) params.get("appId");
        String checkFlg = (String) params.get("checkFlg");
        String adminName = (String) params.get("adminName");
        String secName = (String) params.get("secName");
        String auditorName = (String) params.get("auditorName");

        SysGroupDatasourceDO sysGroupDatasourceDO = new SysGroupDatasourceDO();
        sysGroupDatasourceDO.setId(UuidUtils.createUUID());
        sysGroupDatasourceDO.setMajor(1);
        sysGroupDatasourceDO.setDriverName((String) params.get("dsDriver"));
        sysGroupDatasourceDO.setName((String) params.get("dsName"));
        sysGroupDatasourceDO.setPassword((String) params.get("dsPassword"));
        sysGroupDatasourceDO.setType((String) params.get("dsType"));
        sysGroupDatasourceDO.setUrl((String) params.get("dsUrl"));
        sysGroupDatasourceDO.setUsername((String) params.get("dsUsername"));


        //获得相应集团信息
        OrgGroupDO orgGroupDO = orgGroupService.getById(groupId);
//        List<SysGroupAppDO> list = new ArrayList<>();
        List<SysUserDO> userDOList = new ArrayList<>();
        String newIdentify = getNewIdentify();
        StringBuffer stringBuffer = new StringBuffer();

        SysGroupAppDO sysGroupAppDO = new SysGroupAppDO();

        sysGroupAppDO.setDatasourceId(sysGroupDatasourceDO.getId());
        if (StringUtils.isNotBlank(appName)) {
            sysGroupAppDO.setName(appName);
        }

        int count = count(new QueryWrapper<SysGroupAppDO>().eq("group_id", orgGroupDO.getId()));
        if (count == 0) {
            sysGroupAppDO.setMainApp(1);
        }
        sysGroupAppDO.setVersion("1.0.0");

        // 获取当前集团下应用最大序号
        Map<String, Object> query = new HashMap<>();
        query.put("groupId", groupId);
        query.put("tableName", "sys_group_app");
        Integer maxsort = aceSqlUtils.getMaxSort(query);
        sysGroupAppDO.setSortIndex(maxsort + 10);

        if (sysGroupAppDO.getSortIndex() == null) {
            Map<String, Object> map = new HashMap<>();
            map.put("tableName", "sys_group_app");
            map.put("groupId", groupId);
            sysGroupAppDO.setSortIndex(aceSqlUtils.getMaxSort(map) + 10);
        }
        // 判断是否前后端分离
//        if (map.get("hasUi") != null) {
//            int hasUi = Integer.parseInt((String) map.get("hasUi"));
//            if (hasUi == 1) {
//                String uiName = (String) map.get("uiName");
//                if (StringUtils.isBlank(uiName)) {
//                    throw new RException(InternationUtils.getInternationalMsg("EMPTY_ARG"));
//                }
//                sysGroupAppDO.setUiName(newIdentify + uiName);
//            } else {
//                sysGroupAppDO.setUiName("");
//            }
//        }
        sysGroupAppDO.setCreateUser(securityUtils.getCurrentUserId());
        sysGroupAppDO.setCreateTime(LocalDateTime.now());
        sysGroupAppDO.setSecretLevel(orgGroupDO.getSecretLevel());
        sysGroupAppDO.setGroupId(orgGroupDO.getId());
        sysGroupAppDO.setThirdAdmin(orgGroupDO.getThreeAdmin());
        sysGroupAppDO.setId(newIdentify + "-" + appId);
        sysGroupDatasourceDO.setAppId(sysGroupAppDO.getId());
        stringBuffer.append(sysGroupAppDO.getName());
        stringBuffer.append(",");
//        list.add(sysGroupAppDO);

        // 是否自动生成三员账号
        if (Objects.equals("1", checkFlg)) {

            if (sysUserService.count(new QueryWrapper<SysUserDO>()
                    .eq("user_name", adminName)) > 0) {
                throw new RException(InternationUtils.getInternationalMsg("SAME_MODEL",
                        new String[]{InternationUtils.getInternationalMsg("USER_NAME"), adminName}
                ));
            }
            if (sysUserService.count(new QueryWrapper<SysUserDO>()
                    .eq("user_name", secName)) > 0) {
                throw new RException(InternationUtils.getInternationalMsg("SAME_MODEL",
                        new String[]{InternationUtils.getInternationalMsg("USER_NAME"), secName}
                ));
            }
            if (sysUserService.count(new QueryWrapper<SysUserDO>()
                    .eq("user_name", auditorName)) > 0) {
                throw new RException(InternationUtils.getInternationalMsg("SAME_MODEL",
                        new String[]{InternationUtils.getInternationalMsg("USER_NAME"), auditorName}
                ));
            }

            SysUserDO appadmin = new SysUserDO();
            appadmin.setId(UuidUtils.createUUID());
            appadmin.setGroupId(orgGroupDO.getId());
            appadmin.setUserName(adminName);
            appadmin.setUserType(222);
            appadmin.setRealName(sysGroupAppDO.getName() + "管理员");
            appadmin.setSecretLevel(5);
            userDOList.add(appadmin);

            SysUserDO appsec = new SysUserDO();
            appsec.setId(UuidUtils.createUUID());
            appsec.setGroupId(orgGroupDO.getId());
            appsec.setUserName(secName);
            appsec.setUserType(222);
            appsec.setRealName(sysGroupAppDO.getName() + "保密员");
            appsec.setSecretLevel(5);
            userDOList.add(appsec);

            SysUserDO appauditor = new SysUserDO();
            appauditor.setId(UuidUtils.createUUID());
            appauditor.setGroupId(orgGroupDO.getId());
            appauditor.setUserName(auditorName);
            appauditor.setUserType(222);
            appauditor.setRealName(sysGroupAppDO.getName() + "审计员");
            appauditor.setSecretLevel(5);
            userDOList.add(appauditor);

        }

        if (save(sysGroupAppDO)) {
            if (sysGroupDatasourceService.save(sysGroupDatasourceDO)) {
                userDOList.stream().forEach(sysUserDO -> {
                    sysUserService.saveUser(sysUserDO);
                    sysAuditLogService.saveLog(new AuditLogTypeDO(AuditLogType.success, "新增"), "新增应用三员账号", "新增应用三员账号："+sysUserDO
                            .getRealName(), groupId, null);
                });
                return sysAuditLogService.saveLog(new AuditLogTypeDO(AuditLogType.success, "新增"), "新增应用","新增应用："+ stringBuffer
                                .substring
                                        (0, stringBuffer.length() - 1),
                        groupId, null);
            }

        }
        return false;

    }

    /**
     * 获取当前用户当前集团下的应用范围
     *
     * @param
     * @return java.util.List<com.csicit.ace.common.pojo.domain.SysGroupAppDO>
     * @author shanwj
     * @date 2019/5/31 11:53
     */
    @Override
    public List<SysGroupAppDO> listUserOrgApp() {
        //集团下的所有应用
        String userId = securityUtils.getCurrentUserId();
        //当前用户的应用管控范围
        List<String> appIds =
                sysAuthScopeAppService.list(new QueryWrapper<SysAuthScopeAppDO>().eq("is_activated", 1).eq
                        ("user_id",
                                userId))
                        .stream().map(SysAuthScopeAppDO::getAppId).distinct().collect(Collectors.toList());
        List<SysGroupAppDO> apps = new ArrayList<>();
        if (org.apache.commons.collections.CollectionUtils.isNotEmpty(appIds)) {
            apps = list(new QueryWrapper<SysGroupAppDO>().in("id", appIds).orderByAsc("sort_index"));
        }
        return apps;
    }

    /**
     * 获取有权限的应用列表
     *
     * @param groupId
     * @return java.util.List<com.csicit.ace.common.pojo.domain.SysGroupAppDO>
     * @author shanwj
     * @date 2019/5/31 11:53
     */
    @Override
    public List<SysGroupAppDO> listAppHaveAuth(String groupId) {
        //集团下的所有应用
        List<SysGroupAppDO> list = new ArrayList<>(16);
        this.list(new QueryWrapper<SysGroupAppDO>()
                .eq("group_id", groupId)).stream().forEach(sysGroupAppDO -> {
            if (sysAuthService.count(new QueryWrapper<SysAuthDO>().eq("app_id", sysGroupAppDO.getId())) > 0) {
                list.add(sysGroupAppDO);
            }
        });
        return list;
    }

    /**
     * 获取无权限的应用列表
     *
     * @param groupId
     * @return java.util.List<com.csicit.ace.common.pojo.domain.SysGroupAppDO>
     * @author shanwj
     * @date 2019/5/31 11:53
     */
    @Override
    public List<SysGroupAppDO> listAppNoAuth(String groupId) {
        //集团下的所有应用
        List<SysGroupAppDO> list = new ArrayList<>(16);
        this.list(new QueryWrapper<SysGroupAppDO>()
                .eq("group_id", groupId)).stream().forEach(sysGroupAppDO -> {
            if (sysAuthService.count(new QueryWrapper<SysAuthDO>().eq("app_id", sysGroupAppDO.getId())) == 0) {
                list.add(sysGroupAppDO);
            }
        });
        return list;
    }

    /**
     * 根据当前标识获取下一个应用标识
     *
     * @return 下一个标识
     * @author shanwj
     * @date 2019/5/16 18:01
     */
    private String getNewIdentify() {
        SysConfigDO sysConfigDO = sysConfigService.list(
                new QueryWrapper<SysConfigDO>()
                        .eq("NAME", "app_identifying")).get(0);
        String nowValue = sysConfigDO.getValue();
        String start = nowValue.substring(0, 1);
        String end = nowValue.substring(nowValue.length() - 1);
        if (Objects.equals("z", start) && Objects.equals("z", end)) {
            throw new RException("权限标识定义超出范围!");
        }
        String returnStr;
        int snow = (int) start.toCharArray()[0];
        int enow = (int) end.toCharArray()[0];
        if (Objects.equals("z", end)) {
            start = String.valueOf((char) (snow + 1));
            returnStr = start + "a";
        } else {
            end = String.valueOf((char) (enow + 1));
            returnStr = start + end;
        }
        sysConfigDO.setValue(returnStr);
        sysConfigService.updateById(sysConfigDO);
        return returnStr;
    }

    @Override
    public boolean cloneApp(String appId, String newAppId) {
        if (count(new QueryWrapper<SysGroupAppDO>().eq("id", newAppId)) > 0) {
            throw new RException("应用标识重复！");
        }
        return aceDataResolveService.cloneApp(appId, newAppId);
    }

    /**
     * 保存应用
     *
     * @param instance
     * @return
     * @author FourLeaves
     * @date 2020/9/24 8:30
     */
    @Override
    public R saveApp(SysGroupAppDO instance) {
        int count = count(new QueryWrapper<SysGroupAppDO>().eq("id", instance.getId()));
        if (count > 0) {
            throw new RException("应用标识重复！");
        }
        if(instance.getId().contains("_")) {
            throw new RException("应用标识不可以含有下划线！");
        }
        count = count(new QueryWrapper<SysGroupAppDO>().eq("group_Id", instance.getGroupId()));
        if (count == 0) {
            instance.setMainApp(1);
        }
        instance.setCreateTime(LocalDateTime.now());
        instance.setUpdateTime(LocalDateTime.now());
        instance.setCreateUser(securityUtils.getCurrentUserId());

        List<SysUserDO> userDOList = new ArrayList<>();
        SysUserDO appadmin = new SysUserDO();
        SysUserDO appsec = new SysUserDO();
        SysUserDO appauditor = new SysUserDO();
        if (autoCreateThree) {
            String userNamePrefix = instance.getId().replace("_", "");

            appadmin.setId(UuidUtils.createUUID());
            appadmin.setGroupId(instance.getGroupId());
            appadmin.setUserName(userNamePrefix + "Admin");
            appadmin.setUserType(222);
            appadmin.setRealName(instance.getName() + "管理员");
            appadmin.setSecretLevel(5);
            userDOList.add(appadmin);

            appsec.setId(UuidUtils.createUUID());
            appsec.setGroupId(instance.getGroupId());
            appsec.setUserName(userNamePrefix + "Sec");
            appsec.setUserType(222);
            appsec.setRealName(instance.getName() + "保密员");
            appsec.setSecretLevel(5);
            userDOList.add(appsec);

            appauditor.setId(UuidUtils.createUUID());
            appauditor.setGroupId(instance.getGroupId());
            appauditor.setUserName(userNamePrefix + "Auditor");
            appauditor.setUserType(222);
            appauditor.setRealName(instance.getName() + "审计员");
            appauditor.setSecretLevel(5);
            userDOList.add(appauditor);
        }

        if (save(instance)) {
            /**
             * 应用三员保存 分配  激活
             */
            if (autoCreateThree) {
                List<SysGroupAppDO> appDOList = new ArrayList<>();
                List<OrgOrganizationDO> orgOrganizationDOS = orgOrganizationService.list(
                        new QueryWrapper<OrgOrganizationDO>().eq("group_id", instance.getGroupId())
                                .eq("is_delete", 0).eq("is_business_unit", 1)
                );
                appDOList.add(instance);
                userDOList.stream().forEach(sysUserDO -> {
                    sysUserService.saveUser(sysUserDO);
                    sysUserDO.setApps(appDOList);
                    sysUserDO.setOrganizes(orgOrganizationDOS);
                    sysUserDO.setUserGroups(new ArrayList<>());
                    if (sysUserDO.getUserName().endsWith("Admin")) {
                        sysUserDO.setRoleType(111);
                    } else if (sysUserDO.getUserName().endsWith("Sec")) {
                        sysUserDO.setRoleType(222);
                    } else {
                        sysUserDO.setRoleType(333);
                    }
                    if (!sysUserService.saveUserAppControlDomain(sysUserDO)) {
                        throw new RException(InternationUtils.getInternationalMsg("SAVE_FAILED"));
                    }
                    Map<String, String> map = new HashMap<>();
                    map.put("id", sysUserDO.getId());
                    map.put("roleType", sysUserDO.getRoleType() + "");
                    if (!sysUserRoleService.setAppActivated(map)) {
                        throw new RException(InternationUtils.getInternationalMsg("SAVE_FAILED"));
                    }
                    if (!sysAuditLogService.saveLog(new AuditLogTypeDO(AuditLogType.success, "新增"), "新增应用三员账号", "新增应用三员账号："+sysUserDO
                            .getRealName(), instance.getGroupId(), null)) {
                        throw new RException(InternationUtils.getInternationalMsg("SAVE_FAILED"));
                    }
                });
                return R.ok(InternationUtils.getInternationalMsg("SAVE_SUCCESS"))
                        .put("admin", appadmin.getUserName())
                        .put("sec", appsec.getUserName())
                        .put("auditor", appauditor.getUserName());
            }
            return R.ok(InternationUtils.getInternationalMsg("SAVE_SUCCESS"))
                    .put("show", 0);
        }
        return R.error(InternationUtils.getInternationalMsg("SAVE_FAILED"));
    }

    /**
     * 修改应用
     *
     * @return 下一个标识
     * @author shanwj
     * @date 2019/5/16 18:01
     */
    @Override
    public boolean updateApp(SysGroupAppDO instance) {
        if (updateById(instance)) {
            if (sysAuditLogService.saveLog(new AuditLogTypeDO(AuditLogType.warning, "更新"), "修改应用","修改应用："+ instance.getName(),
                    instance
                            .getGroupId(),
                    null)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 删除应用
     *
     * @return 下一个标识
     * @author shanwj
     * @date 2019/5/16 18:01
     */
    @Override
    public boolean deleteApp(List<String> ids) {
        List<SysGroupAppDO> apps = list(new QueryWrapper<SysGroupAppDO>()
                .in("id", ids));
        if (ids.size() > 0) {
            if (removeByIds(ids)) {
                StringBuffer stringBuffer = new StringBuffer();
                String groupId = apps.get(0).getGroupId();
                apps.stream().forEach(app -> {
                    stringBuffer.append(app.getName());
                    stringBuffer.append(",");
                });
                if (sysAuditLogService.saveLog(new AuditLogTypeDO(AuditLogType.danger, "删除"), "删除应用", "删除应用："+stringBuffer
                                .substring(0, stringBuffer
                                        .length
                                                () - 1),
                        groupId, null)) {
                    return true;
                }
            }
            return false;
        }
        return true;
    }

    /**
     * 数据源导出
     *
     * @param appId
     * @param appUpgrade
     * @return
     * @author zuogang
     * @date 2020/7/22 15:22
     */
    private void datasourceExport(String appId, AppUpgrade appUpgrade) {
        List<GroupDatasourceDetail> groupDatasources = new ArrayList<>(16);
        List<SysGroupDatasourceDO> sysGroupDatasourceDOS = sysGroupDatasourceService.list(new
                QueryWrapper<SysGroupDatasourceDO>().eq("app_id", appId));
        sysGroupDatasourceDOS.stream().forEach(item -> {
            item.setTraceId(item.getId());
            groupDatasources.add(JsonUtils.castObject(item, GroupDatasourceDetail.class));
        });
        if (CollectionUtils.isNotEmpty(sysGroupDatasourceDOS)) {
            if (!sysGroupDatasourceService.updateBatchById(sysGroupDatasourceDOS)) {
                throw new RException("数据源表的跟踪ID更新失败！");
            }
        }
        appUpgrade.setGroupDatasource(groupDatasources);
    }

    @Autowired
    SysComponentRegisterService sysComponentRegisterService;

    /**
     * 组件注册导出
     *
     * @param appId
     * @param appUpgrade
     * @return
     * @author zuogang
     * @date 2020/7/22 15:22
     */
    private void componentRegisterExport(String appId, AppUpgrade appUpgrade) {
        List<ComponentRegisterDetail> componentRegisters = new ArrayList<>(16);
        List<SysComponentRegisterDO> sysComponentRegisterDOs = sysComponentRegisterService.list(new
                QueryWrapper<SysComponentRegisterDO>().eq("app_id", appId));
        sysComponentRegisterDOs.stream().forEach(item -> {
            item.setTraceId(item.getId());
            componentRegisters.add(JsonUtils.castObject(item, ComponentRegisterDetail.class));
        });
        if (CollectionUtils.isNotEmpty(sysComponentRegisterDOs)) {
            if (!sysComponentRegisterService.updateBatchById(sysComponentRegisterDOs)) {
                throw new RException("组件注册的跟踪ID更新失败！");
            }
        }
        appUpgrade.setComponentRegister(componentRegisters);
    }

    @Autowired
    QrtzConfigServiceD qrtzConfigServiceD;

    /**
     * 定时任务导出
     *
     * @param appId
     * @param appUpgrade
     * @return
     * @author zuogang
     * @date 2020/7/22 15:22
     */
    private void qrtzConfigExport(String appId, AppUpgrade appUpgrade) {
        List<QrtzConfigDetail> qrtzConfigs = new ArrayList<>(16);
        QrtzConfigDO qrtzConfigDO = qrtzConfigServiceD.getById(appId);
        if (qrtzConfigDO != null) {
            qrtzConfigDO.setTraceId(appId);
            qrtzConfigs.add(JsonUtils.castObject(qrtzConfigDO, QrtzConfigDetail.class));
            if (!qrtzConfigServiceD.updateById(qrtzConfigDO)) {
                throw new RException("定时任务的跟踪ID更新失败！");
            }
        }
        appUpgrade.setQrtzConfig(qrtzConfigs);
    }

    @Autowired
    SysApiResourceService sysApiResourceService;
    @Autowired
    SysAuthApiService sysAuthApiService;

    /**
     * 权限，API,权限API导出
     *
     * @param appId
     * @param appUpgrade
     * @return
     * @author zuogang
     * @date 2020/7/22 15:22
     */
    private void authExport(String appId, AppUpgrade appUpgrade) {
        // 权限
        List<AuthDetail> authDetails = new ArrayList<>(16);
        List<SysAuthDO> sysAuthDOS = sysAuthService.list(new QueryWrapper<SysAuthDO>()
                .eq("app_id", appId));
        sysAuthDOS.stream().forEach(item -> {
            item.setTraceId(item.getId());
            authDetails.add(JsonUtils.castObject(item, AuthDetail.class));
        });
        if (CollectionUtils.isNotEmpty(sysAuthDOS)) {
            if (!sysAuthService.updateBatchById(sysAuthDOS)) {
                throw new RException("权限定义的跟踪ID更新失败！");
            }
        }
        appUpgrade.setAuth(authDetails);

        // API
        List<ApiResourceDetail> apiResourceDetails = new ArrayList<>(16);
        List<SysApiResourceDO> sysApiResourceDOS = sysApiResourceService.list(new QueryWrapper<SysApiResourceDO>()
                .eq("app_id", appId));
        sysApiResourceDOS.stream().forEach(item -> {
            item.setTraceId(item.getId());
            apiResourceDetails.add(JsonUtils.castObject(item, ApiResourceDetail.class));
        });
        if (CollectionUtils.isNotEmpty(sysApiResourceDOS)) {
            if (!sysApiResourceService.updateBatchById(sysApiResourceDOS)) {
                throw new RException("API资源的跟踪ID更新失败！");
            }
        }
        appUpgrade.setApiResource(apiResourceDetails);

        // 权限API
        List<AuthApiDetail> authApiDetails = new ArrayList<>(16);
        List<SysAuthApiDO> authApiDOS = sysAuthApiService.list(new QueryWrapper<SysAuthApiDO>()
                .inSql("auth_id", "select id from sys_auth where app_id='" + appId + "'"));
        authApiDOS.stream().forEach(item -> {
            item.setTraceId(item.getId());
            authApiDetails.add(JsonUtils.castObject(item, AuthApiDetail.class));
        });
        if (CollectionUtils.isNotEmpty(authApiDOS)) {
            if (!sysAuthApiService.updateBatchById(authApiDOS)) {
                throw new RException("权限API的跟踪ID更新失败！");
            }
        }
        appUpgrade.setAuthApi(authApiDetails);
    }

    @Autowired
    SysMenuService sysMenuService;

    /**
     * 菜单导出
     *
     * @param appId
     * @param appUpgrade
     * @return
     * @author zuogang
     * @date 2020/7/22 15:22
     */
    private void menuExport(String appId, AppUpgrade appUpgrade) {
        List<MenuDetail> menuDetails = new ArrayList<>(16);
        List<SysMenuDO> sysMenuDOS = sysMenuService.list(new QueryWrapper<SysMenuDO>()
                .eq("app_id", appId));
        sysMenuDOS.stream().forEach(item -> {
            item.setTraceId(item.getId());
            menuDetails.add(JsonUtils.castObject(item, MenuDetail.class));
        });
        if (CollectionUtils.isNotEmpty(sysMenuDOS)) {
            if (!sysMenuService.updateBatchById(sysMenuDOS)) {
                throw new RException("菜单的跟踪ID更新失败！");
            }
        }
        appUpgrade.setMenu(menuDetails);
    }

    @Autowired
    SysRoleService sysRoleService;
    @Autowired
    SysRoleMutexService sysRoleMutexService;
    @Autowired
    SysRoleRelationService sysRoleRelationService;

    /**
     * 角色导出
     *
     * @param appId
     * @param appUpgrade
     * @return
     * @author zuogang
     * @date 2020/7/22 15:22
     */
    private void roleExport(String appId, AppUpgrade appUpgrade) {
        List<RoleDetail> roleDetails = new ArrayList<>(16);
        List<SysRoleDO> sysRoleDOs = sysRoleService.list(new QueryWrapper<SysRoleDO>()
                .eq("app_id", appId));
        sysRoleDOs.stream().forEach(item -> {
            item.setTraceId(item.getId());
            roleDetails.add(JsonUtils.castObject(item, RoleDetail.class));
        });
        if (CollectionUtils.isNotEmpty(sysRoleDOs)) {
            if (!sysRoleService.updateBatchById(sysRoleDOs)) {
                throw new RException("角色的跟踪ID更新失败！");
            }
        }
        appUpgrade.setRole(roleDetails);

        List<RoleMutexDetail> roleMutexDetails = new ArrayList<>(16);
        List<SysRoleMutexDO> sysRoleMutexDOS = sysRoleMutexService.list(new QueryWrapper<SysRoleMutexDO>()
                .inSql("role_id", "select id from sys_role where app_id='" + appId + "'"));
        sysRoleMutexDOS.stream().forEach(item -> {
            item.setTraceId(item.getId());
            roleMutexDetails.add(JsonUtils.castObject(item, RoleMutexDetail.class));
        });
        if (CollectionUtils.isNotEmpty(sysRoleMutexDOS)) {
            if (!sysRoleMutexService.updateBatchById(sysRoleMutexDOS)) {
                throw new RException("互斥角色的跟踪ID更新失败！");
            }
        }
        appUpgrade.setRoleMutex(roleMutexDetails);

        List<RoleRelationDetail> roleRelationDetails = new ArrayList<>(16);
        List<SysRoleRelationDO> sysRoleRelationDOS = sysRoleRelationService.list(new QueryWrapper<SysRoleRelationDO>()
                .inSql("pid", "select id from sys_role where app_id='" + appId + "'"));
        sysRoleRelationDOS.stream().forEach(item -> {
            item.setTraceId(item.getId());
            roleRelationDetails.add(JsonUtils.castObject(item, RoleRelationDetail.class));
        });
        if (CollectionUtils.isNotEmpty(sysRoleRelationDOS)) {
            if (!sysRoleRelationService.updateBatchById(sysRoleRelationDOS)) {
                throw new RException("上下级角色的跟踪ID更新失败！");
            }
        }
        appUpgrade.setRoleRelation(roleRelationDetails);
    }

    @Autowired
    SysAuthRoleService sysAuthRoleService;
    @Autowired
    SysAuthRoleLvService sysAuthRoleLvService;
    @Autowired
    SysAuthRoleVService sysAuthRoleVService;

    /**
     * 权限角色导出
     *
     * @param appId
     * @param appUpgrade
     * @return
     * @author zuogang
     * @date 2020/7/22 15:22
     */
    private void authRoleExport(String appId, AppUpgrade appUpgrade) {
        List<AuthRoleDetail> authRoleDetails = new ArrayList<>(16);
        List<SysAuthRoleDO> sysAuthRoleDOS = sysAuthRoleService.list(new QueryWrapper<SysAuthRoleDO>()
                .inSql("auth_id", "select id from sys_auth where app_id='" + appId + "'"));
        sysAuthRoleDOS.stream().forEach(item -> {
            item.setTraceId(item.getId());
            authRoleDetails.add(JsonUtils.castObject(item, AuthRoleDetail.class));
        });
        if (CollectionUtils.isNotEmpty(sysAuthRoleDOS)) {
            if (!sysAuthRoleService.updateBatchById(sysAuthRoleDOS)) {
                throw new RException("权限角色的跟踪ID更新失败！");
            }
        }
        appUpgrade.setAuthRole(authRoleDetails);

        List<AuthRoleLvDetail> authRoleLvDetails = new ArrayList<>(16);
        List<SysAuthRoleLvDO> sysAuthRoleLvDOS = sysAuthRoleLvService.list(new QueryWrapper<SysAuthRoleLvDO>()
                .eq("app_id", appId));
        sysAuthRoleLvDOS.stream().forEach(item -> {
            item.setTraceId(item.getId());
            authRoleLvDetails.add(JsonUtils.castObject(item, AuthRoleLvDetail.class));
        });
        if (CollectionUtils.isNotEmpty(sysAuthRoleLvDOS)) {
            if (!sysAuthRoleLvService.updateBatchById(sysAuthRoleLvDOS)) {
                throw new RException("角色授权版本的跟踪ID更新失败！");
            }
        }
        appUpgrade.setAuthRoleLv(authRoleLvDetails);

        List<AuthRoleVDetail> authRoleVDetails = new ArrayList<>(16);
        List<SysAuthRoleVDO> sysAuthRoleVDOS = sysAuthRoleVService.list(new QueryWrapper<SysAuthRoleVDO>()
                .inSql("auth_id", "select id from sys_auth where app_id='" + appId + "'"));
        sysAuthRoleVDOS.stream().forEach(item -> {
            item.setTraceId(item.getId());
            authRoleVDetails.add(JsonUtils.castObject(item, AuthRoleVDetail.class));
        });
        if (CollectionUtils.isNotEmpty(sysAuthRoleVDOS)) {
            if (!sysAuthRoleVService.updateBatchById(sysAuthRoleVDOS)) {
                throw new RException("角色授权版本历史数据的跟踪ID更新失败！");
            }
        }
        appUpgrade.setAuthRoleV(authRoleVDetails);
    }

    @Autowired
    FileConfigurationServiceD fileConfigurationServiceD;

    /**
     * 附件配置导出
     *
     * @param appId
     * @param appUpgrade
     * @return
     * @author zuogang
     * @date 2020/7/22 15:22
     */
    private void fileConfigExport(String appId, AppUpgrade appUpgrade) {
        List<FileConfigDetail> fileConfigDetails = new ArrayList<>(16);
        List<FileConfigurationDO> fileConfigurationDOS = fileConfigurationServiceD.list(new
                QueryWrapper<FileConfigurationDO>()
                .eq("app_id", appId));
        fileConfigurationDOS.stream().forEach(item -> {
            item.setTraceId(item.getId());
            fileConfigDetails.add(JsonUtils.castObject(item, FileConfigDetail.class));
        });
        if (CollectionUtils.isNotEmpty(fileConfigurationDOS)) {
            if (!fileConfigurationServiceD.updateBatchById(fileConfigurationDOS)) {
                throw new RException("附件配置的跟踪ID更新失败！");
            }
        }
        appUpgrade.setFileConfiguration(fileConfigDetails);
    }

    @Autowired
    SysMsgSendTypeService sysMsgSendTypeService;

    /**
     * 消息通道导出
     *
     * @param appId
     * @param appUpgrade
     * @return
     * @author zuogang
     * @date 2020/7/22 15:22
     */
    private void msgSendTypeExport(String appId, AppUpgrade appUpgrade) {
        List<MsgSendTypeDetail> msgSendTypeDetails = new ArrayList<>(16);
        List<SysMsgSendTypeDO> sysMsgSendTypeDOS = sysMsgSendTypeService.list(new QueryWrapper<SysMsgSendTypeDO>()
                .eq("app_id", appId));
        sysMsgSendTypeDOS.stream().forEach(item -> {
            item.setTraceId(item.getId());
            msgSendTypeDetails.add(JsonUtils.castObject(item, MsgSendTypeDetail.class));
        });
        if (CollectionUtils.isNotEmpty(sysMsgSendTypeDOS)) {
            if (!sysMsgSendTypeService.updateBatchById(sysMsgSendTypeDOS)) {
                throw new RException("消息通道的跟踪ID更新失败！");
            }
        }
        appUpgrade.setMsgSendType(msgSendTypeDetails);
    }

    @Autowired
    SysMsgTemplateService sysMsgTemplateService;

    /**
     * 消息模板导出
     *
     * @param appId
     * @param appUpgrade
     * @return
     * @author zuogang
     * @date 2020/7/22 15:22
     */
    private void msgTemplateExport(String appId, AppUpgrade appUpgrade) {
        List<MsgTemplateDetail> msgTemplateDetails = new ArrayList<>(16);
        List<SysMsgTemplateDO> sysMsgTemplateDOS = sysMsgTemplateService.list(new QueryWrapper<SysMsgTemplateDO>()
                .eq("app_id", appId));
        sysMsgTemplateDOS.stream().forEach(item -> {
            item.setTraceId(item.getId());
            msgTemplateDetails.add(JsonUtils.castObject(item, MsgTemplateDetail.class));
        });
        if (CollectionUtils.isNotEmpty(sysMsgTemplateDOS)) {
            if (!sysMsgTemplateService.updateBatchById(sysMsgTemplateDOS)) {
                throw new RException("消息模板的跟踪ID更新失败！");
            }
        }
        appUpgrade.setMsgTemplate(msgTemplateDetails);
    }

    @Autowired
    SysMsgTemplateConfigService sysMsgTemplateConfigService;

    /**
     * 信使消息模板导出
     *
     * @param appId
     * @param appUpgrade
     * @return
     * @author zuogang
     * @date 2020/7/22 15:22
     */
    private void msgTemplateConfigExport(String appId, AppUpgrade appUpgrade) {
        List<MsgTemplateConfigDetail> msgTemplateConfigDetails = new ArrayList<>(16);
        List<SysMsgTemplateConfigDO> sysMsgTemplateConfigDOS = sysMsgTemplateConfigService.list(new
                QueryWrapper<SysMsgTemplateConfigDO>()
                .inSql("tid", "select id from sys_msg_template where app_id='" + appId + "'"));
        sysMsgTemplateConfigDOS.stream().forEach(item -> {
            item.setTraceId(item.getId());
            msgTemplateConfigDetails.add(JsonUtils.castObject(item, MsgTemplateConfigDetail.class));
        });
        if (CollectionUtils.isNotEmpty(sysMsgTemplateConfigDOS)) {
            if (!sysMsgTemplateConfigService.updateBatchById(sysMsgTemplateConfigDOS)) {
                throw new RException("信使消息模板的跟踪ID更新失败！");
            }
        }
        appUpgrade.setMsgTemplateConfig(msgTemplateConfigDetails);
    }

    @Autowired
    SysMsgTypeExtendService sysMsgTypeExtendService;

    /**
     * 消息拓展导出
     *
     * @param appId
     * @param appUpgrade
     * @return
     * @author zuogang
     * @date 2020/7/22 15:22
     */
    private void msgTypeExtendExport(String appId, AppUpgrade appUpgrade) {
        List<MsgTypeExtendDetail> msgTypeExtendDetails = new ArrayList<>(16);
        List<SysMsgTypeExtendDO> sysMsgTypeExtendDOS = sysMsgTypeExtendService.list(new
                QueryWrapper<SysMsgTypeExtendDO>().eq("app_id", appId));
        sysMsgTypeExtendDOS.stream().forEach(item -> {
            item.setTraceId(item.getId());
            msgTypeExtendDetails.add(JsonUtils.castObject(item, MsgTypeExtendDetail.class));
        });
        if (CollectionUtils.isNotEmpty(sysMsgTypeExtendDOS)) {
            if (!sysMsgTypeExtendService.updateBatchById(sysMsgTypeExtendDOS)) {
                throw new RException("消息拓展的跟踪ID更新失败！");
            }
        }
        appUpgrade.setMsgTypeExtend(msgTypeExtendDetails);
    }

    @Autowired
    SysDictService sysDictService;
    @Autowired
    SysDictValueService sysDictValueService;

    /**
     * 字典导出
     *
     * @param appId
     * @param appUpgrade
     * @return
     * @author zuogang
     * @date 2020/7/22 15:22
     */
    private void dictExport(String appId, AppUpgrade appUpgrade) {
        List<DictDetail> dictDetails = new ArrayList<>(16);
        List<SysDictDO> sysDictDOS = sysDictService.list(new QueryWrapper<SysDictDO>()
                .eq("app_id", appId));
        sysDictDOS.stream().forEach(item -> {
            item.setTraceId(item.getId());
            dictDetails.add(JsonUtils.castObject(item, DictDetail.class));
        });
        if (CollectionUtils.isNotEmpty(sysDictDOS)) {
            if (!sysDictService.updateBatchById(sysDictDOS)) {
                throw new RException("字典类型的跟踪ID更新失败！");
            }
        }
        appUpgrade.setDict(dictDetails);

        List<DictValueDetail> dictValueDetails = new ArrayList<>(16);
        List<SysDictValueDO> sysDictValueDOS = sysDictValueService.list(new QueryWrapper<SysDictValueDO>()
                .eq("app_id", appId));
        sysDictValueDOS.stream().forEach(item -> {
            item.setTraceId(item.getId());
            dictValueDetails.add(JsonUtils.castObject(item, DictValueDetail.class));
        });
        if (CollectionUtils.isNotEmpty(sysDictValueDOS)) {
            if (!sysDictValueService.updateBatchById(sysDictValueDOS)) {
                throw new RException("字典数据的跟踪ID更新失败！");
            }
        }
        appUpgrade.setDictValue(dictValueDetails);
    }

    @Autowired
    ProServiceServiceD proServiceServiceD;
    @Autowired
    ProModelServiceD proModelServiceD;
    @Autowired
    ProModelAssociationServiceD proModelAssociationServiceD;
    @Autowired
    ProModelColServiceD proModelColServiceD;
    @Autowired
    ProModelIndexServiceD proModelIndexServiceD;

    /**
     * 数据模型导出
     *
     * @param appId
     * @param appUpgrade
     * @return
     * @author zuogang
     * @date 2020/7/22 15:22
     */
    private void modelExport(String appId, AppUpgrade appUpgrade) {
        List<ServiceDetail> serviceDetails = new ArrayList<>(16);
        List<ProServiceDO> proServiceDOS = proServiceServiceD.list(new QueryWrapper<ProServiceDO>()
                .eq("APP_ID", appId));
        proServiceDOS.stream().forEach(item -> {
            item.setTraceId(item.getId());
            serviceDetails.add(JsonUtils.castObject(item, ServiceDetail.class));
        });
        if (CollectionUtils.isNotEmpty(proServiceDOS)) {
            if (!proServiceServiceD.updateBatchById(proServiceDOS)) {
                throw new RException("数据模型-应用信息的跟踪ID更新失败！");
            }
        }
        appUpgrade.setServices(serviceDetails);

        List<ModelDetail> modelDetails = new ArrayList<>(16);
        List<ProModelDO> proModelDOS = proModelServiceD.list(new QueryWrapper<ProModelDO>()
                .inSql("SERVICE_ID", "select id from pro_service where app_id='" + appId + "'"));
        proModelDOS.stream().forEach(item -> {
            item.setTraceId(item.getId());
            modelDetails.add(JsonUtils.castObject(item, ModelDetail.class));
        });
        if (CollectionUtils.isNotEmpty(proModelDOS)) {
            if (!proModelServiceD.updateBatchById(proModelDOS)) {
                throw new RException("数据模型-实体模型信息的跟踪ID更新失败！");
            }
        }
        appUpgrade.setModel(modelDetails);

        List<ModelAssDetail> modelAssDetails = new ArrayList<>(16);
        List<ProModelAssociationDO> proModelAssociationDOS = proModelAssociationServiceD.list(new
                QueryWrapper<ProModelAssociationDO>()
                .inSql("MODEL_ID", "select id from pro_model where service_id in (select id from pro_service where " +
                        "app_id ='" + appId + "')"));
        proModelAssociationDOS.stream().forEach(item -> {
            item.setTraceId(item.getId());
            modelAssDetails.add(JsonUtils.castObject(item, ModelAssDetail.class));
        });
        if (CollectionUtils.isNotEmpty(proModelAssociationDOS)) {
            if (!proModelAssociationServiceD.updateBatchById(proModelAssociationDOS)) {
                throw new RException("数据模型-数据关联信息的跟踪ID更新失败！");
            }
        }
        appUpgrade.setModelAss(modelAssDetails);

        List<ModelIndexDetail> modelIndexDetails = new ArrayList<>(16);
        List<ProModelIndexDO> proModelIndexDOS = proModelIndexServiceD.list(new QueryWrapper<ProModelIndexDO>()
                .inSql("MODEL_ID", "select id from pro_model where service_id in (select id from pro_service where " +
                        "app_id ='" + appId + "')"));
        proModelIndexDOS.stream().forEach(item -> {
            item.setTraceId(item.getId());
            modelIndexDetails.add(JsonUtils.castObject(item, ModelIndexDetail.class));
        });
        if (CollectionUtils.isNotEmpty(proModelIndexDOS)) {
            if (!proModelIndexServiceD.updateBatchById(proModelIndexDOS)) {
                throw new RException("数据模型-索引信息的跟踪ID更新失败！");
            }
        }
        appUpgrade.setModelIndex(modelIndexDetails);

        List<ModelColDetail> modelColDetails = new ArrayList<>(16);
        List<ProModelColDO> proModelColDOS = proModelColServiceD.list(new QueryWrapper<ProModelColDO>()
                .inSql("model_id", "select id from pro_model where service_id in (select id from pro_service where " +
                        "app_id ='" + appId + "')"));
        proModelColDOS.stream().forEach(item -> {
            item.setTraceId(item.getId());
            modelColDetails.add(JsonUtils.castObject(item, ModelColDetail.class));
        });
        if (CollectionUtils.isNotEmpty(proModelColDOS)) {
            if (!proModelColServiceD.updateBatchById(proModelColDOS)) {
                throw new RException("数据模型-数据列信息的跟踪ID更新失败！");
            }
        }
        appUpgrade.setModelCol(modelColDetails);
    }

    /**
     * 系统配置导出
     *
     * @param appId
     * @param appUpgrade
     * @return
     * @author zuogang
     * @date 2020/7/22 15:22
     */
    private void sysConfigExport(String appId, AppUpgrade appUpgrade) {
        List<SysConfigDetail> sysConfigDetails = new ArrayList<>(16);
        List<SysConfigDO> sysConfigDOS = sysConfigService.list(new QueryWrapper<SysConfigDO>()
                .eq("app_id", appId));
        sysConfigDOS.stream().forEach(item -> {
            item.setTraceId(item.getId());
            sysConfigDetails.add(JsonUtils.castObject(item, SysConfigDetail.class));
        });
        if (CollectionUtils.isNotEmpty(sysConfigDOS)) {
            if (!sysConfigService.updateBatchById(sysConfigDOS)) {
                throw new RException("系统配置的跟踪ID更新失败！");
            }
        }
        appUpgrade.setSysConfig(sysConfigDetails);
    }

    @Autowired
    ReportTypeService reportTypeService;

    /**
     * 业务类型导出
     *
     * @param appId
     * @param appUpgrade
     * @return
     * @author zuogang
     * @date 2020/7/22 15:22
     */
    private void reportTypeExport(String appId, AppUpgrade appUpgrade) {
        List<ReportTypeDetail> reportTypeDetails = new ArrayList<>(16);
        List<ReportTypeDO> reportTypeDOS = reportTypeService.list(new QueryWrapper<ReportTypeDO>()
                .eq("app_id", appId));
        reportTypeDOS.stream().forEach(item -> {
            item.setTraceId(item.getId());
            reportTypeDetails.add(JsonUtils.castObject(item, ReportTypeDetail.class));
        });
        if (CollectionUtils.isNotEmpty(reportTypeDOS)) {
            if (!reportTypeService.updateBatchById(reportTypeDOS)) {
                throw new RException("业务类型的跟踪ID更新失败！");
            }
        }
        appUpgrade.setReportType(reportTypeDetails);
    }

    @Autowired
    ReportInfoService reportInfoService;

    /**
     * 报表/仪表盘信息导出
     *
     * @param appId
     * @param appUpgrade
     * @return
     * @author zuogang
     * @date 2020/7/22 15:22
     */
    private void reportInfoExport(String appId, AppUpgrade appUpgrade) {
        List<ReportInfoDetail> reportInfoDetails = new ArrayList<>(16);
        List<ReportInfoDO> reportInfoDOS = reportInfoService.list(new QueryWrapper<ReportInfoDO>()
                .eq("app_id", appId));
        reportInfoDOS.stream().forEach(item -> {
            item.setTraceId(item.getId());
            reportInfoDetails.add(JsonUtils.castObject(item, ReportInfoDetail.class));
        });
        if (CollectionUtils.isNotEmpty(reportInfoDOS)) {
            if (!reportInfoService.updateBatchById(reportInfoDOS)) {
                throw new RException("报表/仪表盘信息的跟踪ID更新失败！");
            }
        }
        appUpgrade.setReportInfo(reportInfoDetails);
    }

    @Autowired
    SysAppInterfaceService sysAppInterfaceService;
    @Autowired
    SysAppInterfaceInputService sysAppInterfaceInputService;
    @Autowired
    SysAppInterfaceOutputService sysAppInterfaceOutputService;

    /**
     * 接口信息导出
     *
     * @param appId
     * @param appUpgrade
     * @return
     * @author zuogang
     * @date 2020/7/22 15:22
     */
    private void interfaceExport(String appId, AppUpgrade appUpgrade) {
        List<AppInterfaceDetail> appInterfaceDetails = new ArrayList<>(16);
        List<SysAppInterfaceDO> sysAppInterfaceDOS = sysAppInterfaceService.list(new QueryWrapper<SysAppInterfaceDO>()
                .eq("app_id", appId));
        sysAppInterfaceDOS.stream().forEach(item -> {
            item.setTraceId(item.getId());
            appInterfaceDetails.add(JsonUtils.castObject(item, AppInterfaceDetail.class));
        });
        if (CollectionUtils.isNotEmpty(sysAppInterfaceDOS)) {
            if (!sysAppInterfaceService.updateBatchById(sysAppInterfaceDOS)) {
                throw new RException("接口信息的跟踪ID更新失败！");
            }
        }
        appUpgrade.setAppInterface(appInterfaceDetails);

        List<AppInterfaceInDetail> appInterfaceInDetails = new ArrayList<>(16);
        List<SysAppInterfaceInputDO> sysAppInterfaceInputDOS = sysAppInterfaceInputService.list(new
                QueryWrapper<SysAppInterfaceInputDO>()
                .inSql("INTERFACE_ID", "select id from sys_app_interface where app_id='" + appId + "'"));
        sysAppInterfaceInputDOS.stream().forEach(item -> {
            item.setTraceId(item.getId());
            appInterfaceInDetails.add(JsonUtils.castObject(item, AppInterfaceInDetail.class));
        });
        if (CollectionUtils.isNotEmpty(sysAppInterfaceInputDOS)) {
            if (!sysAppInterfaceInputService.updateBatchById(sysAppInterfaceInputDOS)) {
                throw new RException("接口入参信息的跟踪ID更新失败！");
            }
        }
        appUpgrade.setAppInterfaceIn(appInterfaceInDetails);

        List<AppInterfaceOutDetail> appInterfaceOutDetails = new ArrayList<>(16);
        List<SysAppInterfaceOutputDO> sysAppInterfaceOutputDOS = sysAppInterfaceOutputService.list(new
                QueryWrapper<SysAppInterfaceOutputDO>().inSql("INTERFACE_ID", "select id from sys_app_interface where" +
                " app_id='" + appId + "'"));
        sysAppInterfaceOutputDOS.stream().forEach(item -> {
            item.setTraceId(item.getId());
            appInterfaceOutDetails.add(JsonUtils.castObject(item, AppInterfaceOutDetail.class));
        });
        if (CollectionUtils.isNotEmpty(sysAppInterfaceOutputDOS)) {
            if (!sysAppInterfaceOutputService.updateBatchById(sysAppInterfaceOutputDOS)) {
                throw new RException("接口出参信息的跟踪ID更新失败！");
            }
        }
        appUpgrade.setAppInterfaceOut(appInterfaceOutDetails);
    }

    @Autowired
    BladeVisualService bladeVisualService;
    @Autowired
    BladeVisualShowService bladeVisualShowService;
    @Autowired
    BladeVisualMsgService bladeVisualMsgService;

    /**
     * 大屏信息导出
     *
     * @param appId
     * @param appUpgrade
     * @return
     * @author zuogang
     * @date 2020/7/22 15:22
     */
    private void bladeVisualExport(String appId, AppUpgrade appUpgrade) {
        List<BladeVisualDetail> bladeVisualDetails = new ArrayList<>(16);
        List<BladeVisualDO> bladeVisualDOS = bladeVisualService.list(new QueryWrapper<BladeVisualDO>()
                .eq("app_id", appId));
        bladeVisualDOS.stream().forEach(item -> {
            item.setTraceId(item.getId());
            bladeVisualDetails.add(JsonUtils.castObject(item, BladeVisualDetail.class));
        });
        if (CollectionUtils.isNotEmpty(bladeVisualDOS)) {
            if (!bladeVisualService.updateBatchById(bladeVisualDOS)) {
                throw new RException("大屏信息的跟踪ID更新失败！");
            }
        }
        appUpgrade.setBladeVisual(bladeVisualDetails);

        List<BladeVisualShowDetail> bladeVisualShowDetails = new ArrayList<>(16);
        List<BladeVisualShowDO> bladeVisualShowDOS = bladeVisualShowService.list(new QueryWrapper<BladeVisualShowDO>()
                .eq("app_id", appId));
        bladeVisualShowDOS.stream().forEach(item -> {
            item.setTraceId(item.getId());
            bladeVisualShowDetails.add(JsonUtils.castObject(item, BladeVisualShowDetail.class));
        });
        if (CollectionUtils.isNotEmpty(bladeVisualShowDOS)) {
            if (!bladeVisualShowService.updateBatchById(bladeVisualShowDOS)) {
                throw new RException("大屏展示信息的跟踪ID更新失败！");
            }
        }
        appUpgrade.setBladeVisualShow(bladeVisualShowDetails);

        List<BladeVisualMsgDetail> msgDetails = new ArrayList<>(16);
        List<BladeVisualMsgDO> bladeVisualMsgDOS = bladeVisualMsgService.list(new QueryWrapper<BladeVisualMsgDO>()
                .eq("app_id", appId));
        bladeVisualMsgDOS.stream().forEach(item -> {
            item.setTraceId(item.getId());
            msgDetails.add(JsonUtils.castObject(item, BladeVisualMsgDetail.class));
        });
        if (CollectionUtils.isNotEmpty(bladeVisualMsgDOS)) {
            if (!bladeVisualMsgService.updateBatchById(bladeVisualMsgDOS)) {
                throw new RException("大屏消息信息的跟踪ID更新失败！");
            }
        }
        appUpgrade.setBladeVisualMsg(msgDetails);
    }

    @Autowired
    CodeSequenceService codeSequenceService;

    /**
     * 数字序列导出
     *
     * @param appId
     * @param appUpgrade
     * @return
     * @author zuogang
     * @date 2020/7/22 15:22
     */
    private void codeSeqExport(String appId, AppUpgrade appUpgrade) {
        List<CodeSeqDetail> codeSeqDetails = new ArrayList<>(16);
        List<CodeSequenceDO> codeSequenceDOS = codeSequenceService.list(new QueryWrapper<CodeSequenceDO>()
                .eq("app_id", appId));
        codeSequenceDOS.stream().forEach(item -> {
            item.setTraceId(item.getId());
            item.setLastResetTime(LocalDateTime.parse(item.getLastResetTime().format(df), df));
            codeSeqDetails.add(JsonUtils.castObject(item, CodeSeqDetail.class));
        });
        if (CollectionUtils.isNotEmpty(codeSequenceDOS)) {
            if (!codeSequenceService.updateBatchById(codeSequenceDOS)) {
                throw new RException("数字序列信息的跟踪ID更新失败！");
            }
        }
        appUpgrade.setCodeSeq(codeSeqDetails);
    }


    DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @Autowired
    CodeTemplateService codeTemplateService;
    @Autowired
    CodeTemplatePartService codeTemplatePartService;

    /**
     * 编码模板导出
     *
     * @param appId
     * @param appUpgrade
     * @return
     * @author zuogang
     * @date 2020/7/22 15:22
     */
    private void codeTemplateExport(String appId, AppUpgrade appUpgrade) {
        List<CodeTemplateDetail> codeTemplateDetails = new ArrayList<>(16);
        List<CodeTemplateDO> codeTemplateDOS = codeTemplateService.list(new QueryWrapper<CodeTemplateDO>()
                .eq("app_id", appId));
        codeTemplateDOS.stream().forEach(item -> {
            item.setTraceId(item.getId());
            codeTemplateDetails.add(JsonUtils.castObject(item, CodeTemplateDetail.class));
        });
        if (CollectionUtils.isNotEmpty(codeTemplateDOS)) {
            if (!codeTemplateService.updateBatchById(codeTemplateDOS)) {
                throw new RException("编码模板信息的跟踪ID更新失败！");
            }
        }
        appUpgrade.setCodeTemplate(codeTemplateDetails);

        List<CodeTemplatePartDetail> codeTemplatePartDetails = new ArrayList<>(16);
        List<CodeTemplatePartDO> codeTemplatePartDOS = codeTemplatePartService.list(new
                QueryWrapper<CodeTemplatePartDO>()
                .eq("TEMPLATE_ID", "select id from CODE_TEMPLATE where app_id='" + appId + "'"));
        codeTemplatePartDOS.stream().forEach(item -> {
            item.setTraceId(item.getId());
            codeTemplatePartDetails.add(JsonUtils.castObject(item, CodeTemplatePartDetail.class));
        });
        if (CollectionUtils.isNotEmpty(codeTemplatePartDOS)) {
            if (!codeTemplatePartService.updateBatchById(codeTemplatePartDOS)) {
                throw new RException("编码模板部件信息的跟踪ID更新失败！");
            }
        }
        appUpgrade.setCodeTemplatePart(codeTemplatePartDetails);
    }

    /**
     * 获取应用配置信息
     *
     * @param appId
     * @param version
     * @return java.lang.String
     * @author zuogang
     * @date 2020/7/23 16:28
     */
    private StringWriter getAppUpgrade(String appId, String version) {
        AppUpgrade appUpgrade = new AppUpgrade();
        appUpgrade.setAppId(appId);
        appUpgrade.setVersion(version);
        // 数据源
        this.datasourceExport(appId, appUpgrade);
        // 组件注册
        this.componentRegisterExport(appId, appUpgrade);
        // 权限
        // API
        // 权限API
        this.authExport(appId, appUpgrade);
        // 菜单定义
        this.menuExport(appId, appUpgrade);
        // 角色
        // 上下级角色
        // 互斥角色
        this.roleExport(appId, appUpgrade);
        // 角色授权
        this.authRoleExport(appId, appUpgrade);
        // 附件配置
        this.fileConfigExport(appId, appUpgrade);
        // 定时任务
        this.qrtzConfigExport(appId, appUpgrade);
        // 消息通道
        this.msgSendTypeExport(appId, appUpgrade);
        // 消息模板
        this.msgTemplateExport(appId, appUpgrade);
        // 信使消息模板
        this.msgTemplateConfigExport(appId, appUpgrade);
        // 消息拓展
        this.msgTypeExtendExport(appId, appUpgrade);
        // 数据字典
        this.dictExport(appId, appUpgrade);
        // 数据模型
        this.modelExport(appId, appUpgrade);
        // 系统配置
        this.sysConfigExport(appId, appUpgrade);
        // 业务类型
        this.reportTypeExport(appId, appUpgrade);
        // 报表/仪表盘信息
        this.reportInfoExport(appId, appUpgrade);
        // 接口
        this.interfaceExport(appId, appUpgrade);
        // 大屏
        // 大屏展示
        this.bladeVisualExport(appId, appUpgrade);
        // 数字序列
        this.codeSeqExport(appId, appUpgrade);
        //编码模板
        this.codeTemplateExport(appId, appUpgrade);
        // 获取新的xml
        return XmlUtils.marshalToXmlWriter(appUpgrade, AppUpgrade.class);

    }

    @Autowired
    ProChangelogHistoryServiceD proChangelogHistoryServiceD;

    /**
     * 获取应用数据库更新
     *
     * @param appId
     * @return java.lang.String
     * @author zuogang
     * @date 2020/7/23 16:28
     */
    private StringWriter getDbChangelog(String appId) {

        StringWriter dbChangelogWriter = new StringWriter();
        ProServiceDO proServiceDO = proServiceServiceD.getOne(new QueryWrapper<ProServiceDO>()
                .eq("app_id", appId));
        if (proServiceDO != null) {
            List<String> logValues = proChangelogHistoryServiceD.list(new QueryWrapper<ProChangelogHistoryDO>().eq
                    ("IS_USE_LESS", 1).eq("service_id", proServiceDO.getId())
                    .notLike("id", "FIRST-CREATE").orderByAsc("create_time")).stream().map
                    (ProChangelogHistoryDO::getLogValue).collect(Collectors.toList());
            dbChangelogWriter.append("<?xml version=\"1.0\" encoding=\"utf-8\"?>\n" +
                    "<databaseChangeLog xmlns=\"http://www.liquibase.org/xml/ns/dbchangelog\" xmlns:xsi=\"http://www" +
                    ".w3.org/2001/XMLSchema-instance\" xsi:schemaLocation=\"http://www.liquibase" +
                    ".org/xml/ns/dbchangelog http://www.liquibase.org/xml/ns/dbchangelog/dbchangelog-3.9.xsd\">\n");
            logValues.forEach(logValue -> {
                dbChangelogWriter.append(logValue.replace("<?xml version=\"1.0\" encoding=\"UTF-8\"?>", ""));
            });
            dbChangelogWriter.append("</databaseChangeLog>");
        }
        return dbChangelogWriter;
    }

    /**
     * 应用配置信息升级导出
     *
     * @param appId
     * @return
     * @author zuogang
     * @date 2020/7/20 10:21
     */
    @Override
    public void appUpgrade(String appId, ServletOutputStream outputStream) {
        ZipOutputStream zip = new ZipOutputStream(outputStream);

        SysGroupAppDO sysGroupAppDO = getById(appId);
        // 获取应用配置信息
        StringWriter appUpgradeWriter = this.getAppUpgrade(appId, sysGroupAppDO.getVersion());
        // 获取数据库变更数据 dbchangelog.xml
        StringWriter dbChangelogWriter = this.getDbChangelog(appId);

        try {
            zip.putNextEntry(new ZipEntry("appConfig.xml"));
            IOUtils.write(appUpgradeWriter.toString(), zip, "UTF-8");
            IOUtils.closeQuietly(appUpgradeWriter);
            zip.putNextEntry(new ZipEntry("dbChangelog.xml"));
            IOUtils.write(dbChangelogWriter.toString(), zip, "UTF-8");
            IOUtils.closeQuietly(dbChangelogWriter);
            zip.closeEntry();
        } catch (IOException e) {
            e.printStackTrace();
        }

        IOUtils.closeQuietly(zip);

    }


    @Autowired
    ZuulRouteConfig zuulRouteConfig;


    @Value("${ace.config.zuul.apps:#{null}}")
    private String apps;

    @Autowired
    AceLogger aceLogger;

    @Override
    public List<SysGroupAppDO> getOnlieList(List<SysGroupAppDO> list) {
        // 单体版
        if (Constants.isZuulApp) {
            //aceLogger.info("getOnlieList isZuulApp");
            Set<String> appIds = zuulRouteConfig.getRoutes().keySet();
//            aceLogger.info("getOnlieList " + JSONObject.toJSONString(zuulRouteConfig.getRoutes()));
//            aceLogger.info("getOnlieList " + JSONObject.toJSONString(apps));
//            if (StringUtils.isNotBlank(apps)) {
//                appIds.addAll(Arrays.asList(apps.split(",")));
//            }
            //aceLogger.info("getOnlieList " + JSONObject.toJSONString(appIds));
            if (CollectionUtils.isEmpty(appIds)) {
                return new ArrayList<>();
            }
            return list(new QueryWrapper<SysGroupAppDO>()
                    .in("id", appIds));
        }
        // 单机版
        if (Constants.isMonomerApp) {
            return list(new QueryWrapper<SysGroupAppDO>()
                    .eq("id", appName));
        }
        if (discoveryUtils != null) {
            List<SysGroupAppDO> onlineList = new ArrayList<>();
            for (SysGroupAppDO appDO : list) {
                List<ServiceInstance> instances = discoveryUtils.getHealthyInstances(appDO.getId());
                if (CollectionUtils.isNotEmpty(instances)) {
                    onlineList.add(appDO);
                }
            }
            return onlineList;
        }
        return list;
    }

    @Override
    public List<SysGroupAppDO> getAppListWithBpm(List<SysGroupAppDO> list) {
//        // 单机版
//        if (Constants.isMonomerApp) {
//            boolean hasBpm = false;
//            try {
//                aceDBHelperMapper.getCount("select count(*) from WFD_FLOW");
//                hasBpm = true;
//            } catch (Exception e) {
//            }
//            if (hasBpm) {
//                return list(new QueryWrapper<SysGroupAppDO>()
//                        .eq("id", appName));
//            } else {
//                return new ArrayList<>();
//            }
//        }

        Map<String, Object> appUseBpmFlag = sysCacheDataService.get("ace-app-use-bpm", Map.class);
        if (appUseBpmFlag != null) {
            List<SysGroupAppDO> onlineList = new ArrayList<>();
            for (SysGroupAppDO appDO : list) {
                if (appUseBpmFlag.containsKey(appDO.getId())
                        && Objects.equals(new Integer(1), appUseBpmFlag.get(appDO.getId()))) {
                    onlineList.add(appDO);
                }
            }
            return onlineList;
        }
        return list;
    }
}

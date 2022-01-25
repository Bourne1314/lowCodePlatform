package com.csicit.ace.platform.core.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.csicit.ace.common.enums.AuditLogType;
import com.csicit.ace.common.enums.AuditLogTypeDO;
import com.csicit.ace.common.exception.RException;
import com.csicit.ace.common.log.AceLogger;
import com.csicit.ace.common.pojo.domain.*;
import com.csicit.ace.common.utils.StringUtils;
import com.csicit.ace.common.utils.UuidUtils;
import com.csicit.ace.common.utils.cipher.GMBaseUtil;
import com.csicit.ace.common.utils.internationalization.InternationUtils;
import com.csicit.ace.data.persistent.service.*;
import com.csicit.ace.data.persistent.service.SysAuthMixService;
import com.csicit.ace.platform.core.pojo.vo.AceDataVO;
import com.csicit.ace.platform.core.service.*;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author FourLeaves
 * @version V1.0
 * @date 2020/8/27 8:51
 */
@Service
public class AceDataResolveServiceImpl implements AceDataResolveService {

    @Autowired
    AceLogger aceLogger;

    @Autowired
    OrgGroupService orgGroupService;

    @Autowired
    SysGroupAppService sysGroupAppService;

    @Autowired
    OrgOrganizationVService orgOrganizationVService;

    @Autowired
    OrgOrganizationVTypeService orgOrganizationVTypeService;

    @Autowired
    OrgOrganizationService orgOrganizationService;

    @Autowired
    OrgOrganizationTypeService orgOrganizationTypeService;

    @Autowired
    OrgDepartmentService orgDepartmentService;

    @Autowired
    OrgDepartmentVService orgDepartmentVService;

    @Autowired
    BdJobService bdJobService;

    @Autowired
    BdPostService bdPostService;

    @Autowired
    BdPersonIdTypeService bdPersonIdTypeService;

    @Autowired
    BdPersonDocService bdPersonDocService;

    @Autowired
    BdPersonJobService bdPersonJobService;

    @Autowired
    SysUserService sysUserService;

    @Autowired
    SysConfigService sysConfigService;

    @Autowired
    SysGroupDatasourceService sysGroupDatasourceService;

    @Autowired
    SysDictService sysDictService;

    @Autowired
    SysDictValueService sysDictValueService;

    @Autowired
    SysAuthService sysAuthService;

    @Autowired
    SysAuthMixService sysAuthMixService;

    @Autowired
    SysMenuService sysMenuService;

    @Autowired
    SysAppMenuDisplayService sysAppMenuDisplayService;

    @Autowired
    SysMsgSendTypeService sysMsgSendTypeService;

    @Autowired
    SysMsgTemplateService sysMsgTemplateService;

    @Autowired
    SysMsgTemplateConfigService sysMsgTemplateConfigService;

    @Autowired
    SysMsgTypeExtendService sysMsgTypeExtendService;

    @Autowired
    SysRoleService sysRoleService;

    @Autowired
    SysRoleMutexService sysRoleMutexService;

    @Autowired
    SysRoleRelationService sysRoleRelationService;

    @Autowired
    SysUserRoleService sysUserRoleService;

    @Autowired
    SysUserRoleLvService sysUserRoleLvService;

    @Autowired
    SysUserRoleVService sysUserRoleVService;

    @Autowired
    SysAuthRoleService sysAuthRoleService;

    @Autowired
    SysAuthRoleLvService sysAuthRoleLvService;

    @Autowired
    SysAuthRoleVService sysAuthRoleVService;

    @Autowired
    SysAuthUserService sysAuthUserService;

    @Autowired
    SysAuthUserLvService sysAuthUserLvService;

    @Autowired
    SysAuthUserVService sysAuthUserVService;

    @Autowired
    ReportTypeService reportTypeService;

    @Autowired
    ReportInfoService reportInfoService;

    @Autowired
    SysApiResourceService sysApiResourceService;

    @Autowired
    SysAuthApiService sysAuthApiService;

    @Autowired
    SysApiMixService sysApiMixService;

    @Autowired
    BladeVisualService bladeVisualService;

    @Autowired
    BladeVisualMapService bladeVisualMapService;

    @Autowired
    BladeVisualMsgService bladeVisualMsgService;

    @Autowired
    BladeVisualShowService bladeVisualShowService;

    @Autowired
    SysAppInterfaceService sysAppInterfaceService;

    @Autowired
    SysAppInterfaceInputService sysAppInterfaceInputService;

    @Autowired
    SysAppInterfaceOutputService sysAppInterfaceOutputService;

    @Autowired
    CodeSequenceService codeSequenceService;

    @Autowired
    CodeTemplateService codeTemplateService;

    @Autowired
    CodeTemplatePartService codeTemplatePartService;

    @Autowired
    FileConfigurationServiceD fileConfigurationServiceD;

    @Autowired
    SysAuditLogService sysAuditLogService;

    @Autowired
    SysAuthScopeAppService sysAuthScopeAppService;

    @Autowired
    SysUserAdminOrgService sysUserAdminOrgService;


    @Override
    public AceDataVO exportData(Map<String, String> params) {
        AceDataVO aceDataVO = new AceDataVO();
        String type = params.get("type");
        String groupId = params.get("groupId");
        // 集团
        OrgGroupDO groupDO = orgGroupService.getById(groupId);
        aceDataVO.setGroup(groupDO);

        /*****************************集团级*******************************************/
        // 职位列表
        aceDataVO.setJobList(bdJobService.list(new QueryWrapper<BdJobDO>()
                .eq("group_id", groupId)));

        // 证件类型列表
        aceDataVO.setPersonIdTypeList(bdPersonIdTypeService.list(new QueryWrapper<BdPersonIdTypeDO>()
                .eq("group_id", groupId)));

        // 集团配置项
        aceDataVO.setConfigList(sysConfigService.list(new QueryWrapper<SysConfigDO>()
                .eq("group_id", groupId).eq("scope", 2)));

        // 集团字典
        List<SysDictDO> dictDOS = sysDictService.list(new QueryWrapper<SysDictDO>()
                .eq("group_id", groupId).eq("scope", 2));
        if (CollectionUtils.isNotEmpty(dictDOS)) {
            aceDataVO.setDictList(dictDOS);
            List<SysDictValueDO> dictValueDOS = sysDictValueService.list(new QueryWrapper<SysDictValueDO>()
                    .in("type_id", dictDOS.stream().map(SysDictDO::getId).collect(Collectors.toList())).orderByAsc("sort_path"));
            aceDataVO.setDictValueList(dictValueDOS);
        }
        /************************************************************************/

        // 用户主键列表
        List<String> userIds = new ArrayList<>();

        if (Objects.equals(type, "org") || Objects.equals(type, "all")) {
            // 只导出组织

            // 业务单元
            List<OrgOrganizationDO> organizationDOS = new ArrayList<>();
            List<String> orgIds = new ArrayList<>();
            if (Objects.equals(type, "all")) {
                organizationDOS = orgOrganizationService.list(new QueryWrapper<OrgOrganizationDO>()
                        .eq("is_delete", 0).eq("group_id", groupId).orderByAsc("sort_path"));
            } else {
                if (StringUtils.isNotBlank(params.get("orgIds"))) {
                    orgIds.addAll(Arrays.asList(params.get("orgIds").split(",")));
                    // 集团也属于业务单元
                    orgIds.add(groupId);
                    if (CollectionUtils.isNotEmpty(orgIds)) {
                        organizationDOS = orgOrganizationService.list(new QueryWrapper<OrgOrganizationDO>()
                                .eq("is_delete", 0).in("id", orgIds).orderByAsc("sort_path"));
                    }
                }
            }

            // 若不存在业务单元 数据将无意义
            if (CollectionUtils.isNotEmpty(organizationDOS)) {
                aceDataVO.setOrganizationList(organizationDOS);
                // 业务单元主键
                orgIds = organizationDOS.stream().map(OrgOrganizationDO::getId).collect(Collectors.toList());
                // 业务单元类型
                aceDataVO.setOrganizationTypeList(orgOrganizationTypeService.list(new QueryWrapper<OrgOrganizationTypeDO>()
                        .in("id", orgIds)));
                // 业务单元固化版本
                List<OrgOrganizationVDO> orgOrganizationVDOS = orgOrganizationVService.list(new QueryWrapper<OrgOrganizationVDO>()
                        .in("organization_id", orgIds));
                if (CollectionUtils.isNotEmpty(orgOrganizationVDOS)) {
                    aceDataVO.setOrganizationVList(orgOrganizationVDOS);
                    // 业务单元类型固化版本
                    aceDataVO.setOrganizationVTypeList(orgOrganizationVTypeService.list(new QueryWrapper<OrgOrganizationVTypeDO>()
                            .in("id", orgOrganizationVDOS.stream().map(OrgOrganizationVDO::getId).collect(Collectors.toList()))));
                }
                // 部门
                List<OrgDepartmentDO> departmentDOS = orgDepartmentService.list(new QueryWrapper<OrgDepartmentDO>()
                        .eq("is_delete", 0).in("organization_id", orgIds).orderByAsc("sort_path"));
                List<String> depIds = new ArrayList<>();
                if (CollectionUtils.isNotEmpty(departmentDOS)) {
                    aceDataVO.setOrgDepartmentList(departmentDOS);
                    depIds = departmentDOS.stream().map(OrgDepartmentDO::getId).collect(Collectors.toList());
                    // 部门固化版本
                    aceDataVO.setOrgDepartmentVList(orgDepartmentVService.list(new QueryWrapper<OrgDepartmentVDO>()
                            .in("DEPARTMENT_ID", depIds)));
                    // 岗位列表
                    aceDataVO.setPostList(bdPostService.list(new QueryWrapper<BdPostDO>()
                            .in("DEPARTMENT_ID", depIds)));
                }
                // 人员
                List<BdPersonDocDO> personDocDOS = bdPersonDocService.list(new QueryWrapper<BdPersonDocDO>()
                        .eq("is_delete", 0).in("organization_id", orgIds));
                List<String> personIds = personDocDOS.stream().map(BdPersonDocDO::getId).collect(Collectors.toList());
                if (CollectionUtils.isNotEmpty(personDocDOS) && CollectionUtils.isNotEmpty(depIds)) {
                    aceDataVO.setPersonDocList(personDocDOS);
                    aceDataVO.setPersonJobList(bdPersonJobService.list(new QueryWrapper<BdPersonJobDO>()
                            .eq("group_id", groupId)
                            .in("organization_id", orgIds)
                            .in("DEPARTMENT_ID", depIds)
                            .in("person_doc_id", personIds)));
                }
                // 用户
                List<SysUserDO> sysUserDOS = sysUserService.list(new QueryWrapper<SysUserDO>()
                        .eq("is_delete", 0).in("organization_id", orgIds)
                        .and(i -> i.isNull("person_doc_id").or(CollectionUtils.isNotEmpty(personIds))
                                .in(CollectionUtils.isNotEmpty(personIds), "person_doc_id", personIds)));
                if (Objects.equals(type, "all")) {
                    List<SysUserDO> appManagers =
                            sysUserService.list(new QueryWrapper<SysUserDO>()
                                    .eq("is_delete", 0)
                                    .eq("user_type", 2)
                                    .eq("group_id", groupId));
                    if (CollectionUtils.isNotEmpty(appManagers)) {
                        aceDataVO.setAdminOrgList(sysUserAdminOrgService.list(new QueryWrapper<SysUserAdminOrgDO>()
                                .in("user_id", appManagers.stream().map(SysUserDO::getId).collect(Collectors.toList()))));
                    }
                    sysUserDOS.addAll(appManagers);
                }
                aceDataVO.setUserList(sysUserDOS);
                userIds = sysUserDOS.stream().map(SysUserDO::getId).collect(Collectors.toList());
            }
        }

        if (Objects.equals(type, "app") || Objects.equals(type, "all")) {
            // 如果只导出应用，集团也会导出，这时也需要导出集团对应的业务单元信息
            if (CollectionUtils.isNotEmpty(aceDataVO.getOrganizationList())) {
                aceDataVO.getOrganizationList().add(orgOrganizationService.getById(groupId));
                // 业务单元类型
                aceDataVO.getOrganizationTypeList().add(orgOrganizationTypeService.getById(groupId));
                // 业务单元固化版本
                List<OrgOrganizationVDO> orgOrganizationVDOS = orgOrganizationVService.list(new QueryWrapper<OrgOrganizationVDO>()
                        .eq("organization_id", groupId));
                if (CollectionUtils.isNotEmpty(orgOrganizationVDOS)) {
                    aceDataVO.setOrganizationVList(orgOrganizationVDOS);
                    // 业务单元类型固化版本
                    aceDataVO.setOrganizationVTypeList(orgOrganizationVTypeService.list(new QueryWrapper<OrgOrganizationVTypeDO>()
                            .in("id", orgOrganizationVDOS.stream().map(OrgOrganizationVDO::getId).collect(Collectors.toList()))));
                }
            }
            // 只导出应用
            // 应用列表
            List<SysGroupAppDO> appDOList = new ArrayList<>();
            List<String> appIds;
            if (Objects.equals(type, "all")) {
                appDOList = sysGroupAppService.list(new QueryWrapper<SysGroupAppDO>()
                        .eq("group_id", groupId));
            } else {
                if (StringUtils.isNotBlank(params.get("appIds"))) {
                    appIds = Arrays.asList(params.get("appIds").split(","));
                    if (CollectionUtils.isNotEmpty(appIds)) {
                        appDOList = sysGroupAppService.list(new QueryWrapper<SysGroupAppDO>()
                                .in("id", appIds));
                    }
                }
            }
            if (CollectionUtils.isNotEmpty(appDOList)) {
                aceDataVO.setAppList(appDOList);
                // 应用主键
                appIds = appDOList.stream().map(SysGroupAppDO::getId).collect(Collectors.toList());

                // 应用管理员 应用管控域
                if (Objects.equals(type, "all") && CollectionUtils.isNotEmpty(userIds)) {
                    aceDataVO.setScopeAppList(sysAuthScopeAppService.list(new QueryWrapper<SysAuthScopeAppDO>()
                            .in("app_id", appIds).in("user_id", userIds)));
                }

                // 配置项 应用  + 集团的
                List<SysConfigDO> configDOS = sysConfigService.list(new QueryWrapper<SysConfigDO>()
                        .in("app_id", appIds).eq("scope", 3));
                if (CollectionUtils.isNotEmpty(aceDataVO.getConfigList())) {
                    aceDataVO.getConfigList().addAll(configDOS);
                } else {
                    aceDataVO.setConfigList(configDOS);
                }

                // 字典 应用  + 集团的
                List<SysDictDO> dictDOST = sysDictService.list(new QueryWrapper<SysDictDO>()
                        .in("app_id", appIds).eq("scope", 3));
                if (CollectionUtils.isNotEmpty(dictDOST)) {
                    List<SysDictValueDO> dictValueDOS = sysDictValueService.list(new QueryWrapper<SysDictValueDO>()
                            .in("type_id", dictDOST.stream().map(SysDictDO::getId).collect(Collectors.toList())).orderByAsc("sort_path"));
                    if (CollectionUtils.isNotEmpty(aceDataVO.getDictList())) {
                        aceDataVO.getDictList().addAll(dictDOST);
                    } else {
                        aceDataVO.setDictList(dictDOST);
                    }
                    if (CollectionUtils.isNotEmpty(aceDataVO.getDictValueList())) {
                        aceDataVO.getDictValueList().addAll(dictValueDOS);
                    } else {
                        aceDataVO.setDictValueList(dictValueDOS);
                    }
                }

                // 权限
                List<SysAuthDO> authDOS = sysAuthService.list(new QueryWrapper<SysAuthDO>()
                        .in("app_id", appIds).orderByAsc("sort_path"));
                if (CollectionUtils.isNotEmpty(authDOS)) {
                    aceDataVO.setAuthList(authDOS);
                    List<String> authIds = authDOS.stream().map(SysAuthDO::getId).collect(Collectors.toList());

                    // 权限 角色 关系
                    List<SysAuthRoleDO> authRoleDOS = sysAuthRoleService.list(new QueryWrapper<SysAuthRoleDO>()
                            .in("auth_id", authIds));
                    aceDataVO.setAuthRoleList(authRoleDOS);
                    // auth api
                    aceDataVO.setAuthApiList(sysAuthApiService.list(new QueryWrapper<SysAuthApiDO>()
                            .in("auth_id", authIds)));
                    if (CollectionUtils.isNotEmpty(userIds)) {
                        // 用户 api
                        aceDataVO.setApiMixList(sysApiMixService.list(new QueryWrapper<SysApiMixDO>()
                                .in("auth_id", authIds).in("app_id", appIds).in("user_id", userIds)));
                        // 用户 权限
                        aceDataVO.setAuthMixList(sysAuthMixService.list(new QueryWrapper<SysAuthMixDO>()
                                .in("app_id", appIds)
                                .in("auth_id", authIds)
                                .in("user_id", userIds)));
                        // 用户 权限 关系
                        aceDataVO.setAuthUserList(sysAuthUserService.list(new QueryWrapper<SysAuthUserDO>()
                                .in("app_id", appIds).in("user_id", userIds)));
                        List<SysAuthUserLvDO> authUserLvDOS = sysAuthUserLvService.list(new QueryWrapper<SysAuthUserLvDO>()
                                .in("app_id", appIds).in("user_id", userIds));
                        aceDataVO.setAuthUserLvList(authUserLvDOS);
                        if (CollectionUtils.isNotEmpty(authUserLvDOS)) {
                            aceDataVO.setAuthUserVList(sysAuthUserVService.list(new QueryWrapper<SysAuthUserVDO>()
                                    .in("lv_id", authUserLvDOS.stream().map(SysAuthUserLvDO::getId).collect(Collectors.toList()))));
                        }
                    }
                }

                // api
                aceDataVO.setApiList(sysApiResourceService.list(new QueryWrapper<SysApiResourceDO>()
                        .in("app_id", appIds)));
                // 菜单
                List<SysMenuDO> menuDOS = sysMenuService.list(new QueryWrapper<SysMenuDO>()
                        .in("app_id", appIds).orderByAsc("sort_path"));
                aceDataVO.setMenuList(menuDOS);
                // 应用下菜单显示在平台管控台的信息表 实例对象类
                aceDataVO.setAppMenuDisplayList(sysAppMenuDisplayService.list(new QueryWrapper<SysAppMenuDisplayDO>()
                        .in("app_id", appIds)));

                // 角色
                List<SysRoleDO> roleDOS = sysRoleService.list(new QueryWrapper<SysRoleDO>()
                        .in("app_id", appIds));
                aceDataVO.setRoleList(roleDOS);
                if (CollectionUtils.isNotEmpty(roleDOS)) {
                    List<String> roleIds = roleDOS.stream().map(SysRoleDO::getId).collect(Collectors.toList());
                    // 互斥角色
                    aceDataVO.setRoleMutexList(sysRoleMutexService.list(new QueryWrapper<SysRoleMutexDO>()
                            .in("role_id", roleIds)));
                    // 上下级角色
                    aceDataVO.setRoleRelationList(sysRoleRelationService.list(new QueryWrapper<SysRoleRelationDO>()
                            .in("pid", roleIds)));
                    // 权限 角色 LV
                    List<SysAuthRoleLvDO> sysAuthRoleLvDOS = sysAuthRoleLvService.list(new QueryWrapper<SysAuthRoleLvDO>()
                            .in("role_id", roleIds));
                    if (CollectionUtils.isNotEmpty(sysAuthRoleLvDOS)) {
                        aceDataVO.setAuthRoleLvList(sysAuthRoleLvDOS);
                        // 权限 角色 V
                        aceDataVO.setAuthRoleVList(sysAuthRoleVService.list(new QueryWrapper<SysAuthRoleVDO>()
                                .in("lv_id", sysAuthRoleLvDOS.stream().map(SysAuthRoleLvDO::getId).collect(Collectors.toList()))));
                    }
                    if (CollectionUtils.isNotEmpty(userIds)) {
                        // 用户角色 关系
                        aceDataVO.setUserRoleList(sysUserRoleService.list(new QueryWrapper<SysUserRoleDO>()
                                .in("app_id", appIds).in("user_id", userIds)));
                        List<SysUserRoleLvDO> userRoleLvDOS = sysUserRoleLvService.list(new QueryWrapper<SysUserRoleLvDO>()
                                .in("app_id", appIds).in("user_id", userIds));
                        aceDataVO.setUserRoleLvList(userRoleLvDOS);
                        if (CollectionUtils.isNotEmpty(userRoleLvDOS)) {
                            aceDataVO.setUserRoleVList(sysUserRoleVService.list(new QueryWrapper<SysUserRoleVDO>()
                                    .in("lv_id", userRoleLvDOS.stream().map(SysUserRoleLvDO::getId).collect(Collectors.toList()))));
                        }
                    }
                }

                // 消息
                aceDataVO.setMsgSendTypeList(sysMsgSendTypeService.list(new QueryWrapper<SysMsgSendTypeDO>()
                        .in("app_id", appIds)));
                aceDataVO.setMsgTemplateList(sysMsgTemplateService.list(new QueryWrapper<SysMsgTemplateDO>()
                        .in("app_id", appIds)));
                if (CollectionUtils.isNotEmpty(aceDataVO.getMsgTemplateList())) {
                    aceDataVO.setMsgTemplateConfigList(sysMsgTemplateConfigService.list(new QueryWrapper<SysMsgTemplateConfigDO>()
                            .in("tid", aceDataVO.getMsgTemplateList().stream().map(SysMsgTemplateDO::getId).collect(Collectors.toList()))));
                }
                aceDataVO.setMsgTypeExtendList(sysMsgTypeExtendService.list(new QueryWrapper<SysMsgTypeExtendDO>()
                        .in("app_id", appIds)));

                // 数据源
                aceDataVO.setGroupDatasourceList(sysGroupDatasourceService.list(new QueryWrapper<SysGroupDatasourceDO>()
                        .in("app_id", appIds)));

                // 报表
                aceDataVO.setReportTypeList(reportTypeService.list(new QueryWrapper<ReportTypeDO>()
                        .in("app_id", appIds)));
                aceDataVO.setReportInfoList(reportInfoService.list(new QueryWrapper<ReportInfoDO>()
                        .in("app_id", appIds)));

                // 大屏
                aceDataVO.setBladeVisualList(bladeVisualService.list(new QueryWrapper<BladeVisualDO>()
                        .in("app_id", appIds)));
                aceDataVO.setBladeVisualMapList(bladeVisualMapService.list(null));
                aceDataVO.setBladeVisualMsgList(bladeVisualMsgService.list(new QueryWrapper<BladeVisualMsgDO>()
                        .in("app_id", appIds)));
                aceDataVO.setBladeVisualShowList(bladeVisualShowService.list(new QueryWrapper<BladeVisualShowDO>()
                        .in("app_id", appIds)));

                // app interface
                List<SysAppInterfaceDO> appInterfaceDOS = sysAppInterfaceService.list(new QueryWrapper<SysAppInterfaceDO>()
                        .in("app_id", appIds));
                aceDataVO.setAppInterfaceList(appInterfaceDOS);
                if (CollectionUtils.isNotEmpty(appInterfaceDOS)) {
                    List<String> inteIds = appInterfaceDOS.stream().map(SysAppInterfaceDO::getId).collect(Collectors.toList());
                    aceDataVO.setAppInterfaceInputList(sysAppInterfaceInputService.list(new QueryWrapper<SysAppInterfaceInputDO>()
                            .in("interface_id", inteIds)));
                    aceDataVO.setAppInterfaceOutputList(sysAppInterfaceOutputService.list(new QueryWrapper<SysAppInterfaceOutputDO>()
                            .in("interface_id", inteIds)));
                }

                // 编码
                aceDataVO.setCodeSequenceList(codeSequenceService.list(new QueryWrapper<CodeSequenceDO>()
                        .in("app_id", appIds)));
                aceDataVO.setCodeTemplateList(codeTemplateService.list(new QueryWrapper<CodeTemplateDO>()
                        .in("app_id", appIds)));
                if (CollectionUtils.isNotEmpty(aceDataVO.getCodeTemplateList())) {
                    aceDataVO.setCodeTemplatePartList(codeTemplatePartService.list(new QueryWrapper<CodeTemplatePartDO>()
                            .in("Template_id", aceDataVO.getCodeTemplateList().stream()
                                    .map(CodeTemplateDO::getId).collect(Collectors.toList()))));
                }

                // 附件配置
                aceDataVO.setFileConfigurationList(fileConfigurationServiceD.list(new QueryWrapper<FileConfigurationDO>()
                        .in("app_id", appIds)));

            }

        }


        if (!sysAuditLogService.saveLog(new AuditLogTypeDO(AuditLogType.warning, "导出"), "导出平台数据","导出平台数据："+ groupDO.getName())) {
            throw new RException(InternationUtils.getInternationalMsg("AUDIT_LOG_UNKNOWN"));
        }
        return aceDataVO;
    }


    private void throwCloneException(String info, Object o) {
        aceLogger.error(info + "数据异常");
        aceLogger.error(JSONObject.toJSONString(o));
        throw new RException(InternationUtils.getInternationalMsg("OPERATE_FAILED"));
    }

    private static String prefix = "";

    private String createNewId(String id, boolean force) {
        if (!force) {
            if (StringUtils.isBlank(id) || id.length() <= 6) {
                return id;
            }
        }
        return prefix + (id.length() <= 6 ? id : id.substring(6));
    }

    private String createNewId(String id) {
        return createNewId(id, false);
    }

    @Override
    public boolean cloneApp(String appId, String newAppId) {
        SysGroupAppDO app = sysGroupAppService.getById(appId);
        if (app != null) {
            app.setMainApp(0);
            app.setId(newAppId);
            app.setSortIndex(app.getSortIndex() + 1);
            app.setUpdateTime(LocalDateTime.now());
            if (!sysGroupAppService.save(app)) {
                throwCloneException("应用", app);
            }
            // 配置项 应用  + 集团的
            List<SysConfigDO> configDOS = sysConfigService.list(new QueryWrapper<SysConfigDO>()
                    .eq("app_id", appId).eq("scope", 3));
            if (CollectionUtils.isNotEmpty(configDOS)) {
                for (SysConfigDO config : configDOS) {
                    config.setAppId(newAppId);
                    config.setId(UuidUtils.createUUID());
                }
                if (!sysConfigService.saveBatch(configDOS)) {
                    throwCloneException("配置项", configDOS);
                }
            }

            // 新的主键前缀
            // 注意父节点ID
            prefix = UuidUtils.createUUID().substring(0, 6);

            // 应用管理员 应用管控域
            List<SysAuthScopeAppDO> sysAuthScopeAppDOS = sysAuthScopeAppService.list(new QueryWrapper<SysAuthScopeAppDO>()
                    .eq("app_id", appId));
            if (CollectionUtils.isNotEmpty(sysAuthScopeAppDOS)) {
                for (SysAuthScopeAppDO object : sysAuthScopeAppDOS) {
                    object.setAppId(newAppId);
                    object.setId(createNewId(object.getId()));
                }
                if (!sysAuthScopeAppService.saveBatch(sysAuthScopeAppDOS)) {
                    throwCloneException("应用管理员-应用管控域", sysAuthScopeAppDOS);
                }
            }


            // 字典 应用
            List<SysDictDO> dictDOST = sysDictService.list(new QueryWrapper<SysDictDO>()
                    .eq("app_id", appId).eq("scope", 3));
            if (CollectionUtils.isNotEmpty(dictDOST)) {
                List<SysDictValueDO> dictValueDOS = sysDictValueService.list(new QueryWrapper<SysDictValueDO>()
                        .in("type_id", dictDOST.stream().map(SysDictDO::getId).collect(Collectors.toList())).orderByAsc("sort_path"));

                for (SysDictDO dictDO : dictDOST) {
                    dictDO.setAppId(newAppId);
                    dictDO.setId(createNewId(dictDO.getId()));
                }
                if (!sysDictService.saveBatch(dictDOST)) {
                    throwCloneException("字典类型", dictDOST);
                }

                if (CollectionUtils.isNotEmpty(dictValueDOS)) {
                    for (SysDictValueDO dictValue : dictValueDOS) {
                        dictValue.setAppId(newAppId);
                        dictValue.setId(createNewId(dictValue.getId()));
                        dictValue.setTypeId(createNewId(dictValue.getTypeId()));
                        dictValue.setParentId(createNewId(dictValue.getParentId()));

                    }
                    if (!sysDictValueService.saveBatch(dictValueDOS)) {
                        throwCloneException("字典值", dictValueDOS);
                    }
                }

            }

            // 角色
            List<SysRoleDO> roleDOS = sysRoleService.list(new QueryWrapper<SysRoleDO>()
                    .eq("app_id", appId));
            List<String> roleIds = roleDOS.stream().map(SysRoleDO::getId).collect(Collectors.toList());
            if (CollectionUtils.isNotEmpty(roleDOS)) {
                for (SysRoleDO roleDO : roleDOS) {
                    roleDO.setAppId(newAppId);
                    roleDO.setId(createNewId(roleDO.getId()));
                }
                if (!sysRoleService.saveBatch(roleDOS)) {
                    throwCloneException("角色", roleDOS);
                }
            }


            // api
            List<SysApiResourceDO> apiResourceDOS = sysApiResourceService.list(new QueryWrapper<SysApiResourceDO>()
                    .eq("app_id", appId));
            if (CollectionUtils.isNotEmpty(apiResourceDOS)) {
                for (SysApiResourceDO apiResourceDO : apiResourceDOS) {
                    apiResourceDO.setAppId(newAppId);
                    apiResourceDO.setId(apiResourceDO.getId().substring(0,
                            apiResourceDO.getId().length() - appId.length()) + newAppId);
                }
                if (!sysApiResourceService.saveBatch(apiResourceDOS)) {
                    throwCloneException("API", apiResourceDOS);
                }
            }


            // 应用下菜单显示在平台管控台的信息表 实例对象类
            List<SysAppMenuDisplayDO> sysAppMenuDisplayDOS = sysAppMenuDisplayService.list(new QueryWrapper<SysAppMenuDisplayDO>()
                    .eq("app_id", appId));

            if (CollectionUtils.isNotEmpty(sysAppMenuDisplayDOS)) {
                for (SysAppMenuDisplayDO appMenuDisplayDO : sysAppMenuDisplayDOS) {
                    appMenuDisplayDO.setAppId(newAppId);
                    appMenuDisplayDO.setId(createNewId(appMenuDisplayDO.getId()));
                }
                if (!sysAppMenuDisplayService.saveBatch(sysAppMenuDisplayDOS)) {
                    throwCloneException(" 应用下菜单显示在平台管控台的信息表", sysAppMenuDisplayDOS);
                }
            }

            // 权限
            List<SysAuthDO> authDOS = sysAuthService.list(new QueryWrapper<SysAuthDO>()
                    .eq("app_id", appId).orderByAsc("sort_path"));
            if (CollectionUtils.isNotEmpty(authDOS)) {

                // 权限主键 需要在被修改之前
                List<String> authIds = authDOS.stream().map(SysAuthDO::getId).collect(Collectors.toList());

                for (SysAuthDO authDO : authDOS) {
                    authDO.setAppId(newAppId);
                    authDO.setId(createNewId(authDO.getId()));
                    authDO.setParentId(createNewId(authDO.getParentId()));

                }
                if (!sysAuthService.saveBatch(authDOS)) {
                    throwCloneException("权限", authDOS);
                }


                // 权限 角色 关系
                List<SysAuthRoleDO> authRoleDOS = sysAuthRoleService.list(new QueryWrapper<SysAuthRoleDO>()
                        .in("auth_id", authIds));
                if (CollectionUtils.isNotEmpty(authRoleDOS)) {
                    for (SysAuthRoleDO authRole : authRoleDOS) {
                        authRole.setAuthId(createNewId(authRole.getAuthId()));
                        authRole.setId(createNewId(authRole.getId()));
                        authRole.setRoleId(createNewId(authRole.getRoleId()));
                    }
                    if (!sysAuthRoleService.saveBatch(authRoleDOS)) {
                        throwCloneException("权限角色关系", authRoleDOS);
                    }
                }


                // auth api
                List<SysAuthApiDO> authApiDOS = sysAuthApiService.list(new QueryWrapper<SysAuthApiDO>()
                        .in("auth_id", authIds));
                if (CollectionUtils.isNotEmpty(authApiDOS)) {
                    for (SysAuthApiDO authApi : authApiDOS) {
                        authApi.setAuthId(createNewId(authApi.getAuthId()));
                        authApi.setId(createNewId(authApi.getId()));
                        // 生成新的API 主键
                        authApi.setApiId(authApi.getApiId().substring(0,
                                authApi.getApiId().length() - appId.length()) + newAppId);
                    }
                    if (!sysAuthApiService.saveBatch(authApiDOS)) {
                        throwCloneException("权限API关系", authApiDOS);
                    }
                }

                // 用户 api
                List<SysApiMixDO> apiMixDOS = sysApiMixService.list(new QueryWrapper<SysApiMixDO>()
                        .in("auth_id", authIds).eq("app_id", appId));
                if (CollectionUtils.isNotEmpty(apiMixDOS)) {
                    for (SysApiMixDO apiMixDO : apiMixDOS) {
                        apiMixDO.setAppId(newAppId);
                        apiMixDO.setAuthId(createNewId(apiMixDO.getAuthId()));
                        apiMixDO.setId(createNewId(apiMixDO.getId()));
                        // 生成新的API 主键
                        apiMixDO.setApiId(apiMixDO.getApiId().substring(0,
                                apiMixDO.getApiId().length() - appId.length()) + newAppId);
                    }
                    if (!sysApiMixService.saveBatch(apiMixDOS)) {
                        throwCloneException("API混合", apiMixDOS);
                    }
                }
                // 用户 权限
                List<SysAuthMixDO> authMixDOS = sysAuthMixService.list(new QueryWrapper<SysAuthMixDO>()
                        .eq("app_id", appId)
                        .in("auth_id", authIds));
                if (CollectionUtils.isNotEmpty(authMixDOS)) {
                    for (SysAuthMixDO authMixDO : authMixDOS) {
                        authMixDO.setAppId(newAppId);
                        authMixDO.setAuthId(createNewId(authMixDO.getAuthId()));
                        authMixDO.setId(createNewId(authMixDO.getId()));
                    }
                    if (!sysAuthMixService.saveBatch(authMixDOS)) {
                        throwCloneException("用户-权限", authMixDOS);
                    }
                }

                // 用户 权限 关系
                List<SysAuthUserDO> authUserDOS = sysAuthUserService.list(new QueryWrapper<SysAuthUserDO>()
                        .in("auth_id", authIds).eq("app_id", appId));
                if (CollectionUtils.isNotEmpty(authUserDOS)) {
                    for (SysAuthUserDO authUserDO : authUserDOS) {
                        authUserDO.setAppId(newAppId);
                        authUserDO.setAuthId(createNewId(authUserDO.getAuthId()));
                        authUserDO.setId(createNewId(authUserDO.getId()));
                    }
                    if (!sysAuthUserService.saveBatch(authUserDOS)) {
                        throwCloneException("用户-权限", authUserDOS);
                    }
                }
                List<SysAuthUserLvDO> authUserLvDOS = sysAuthUserLvService.list(new QueryWrapper<SysAuthUserLvDO>()
                        .eq("app_id", appId));
                if (CollectionUtils.isNotEmpty(authUserLvDOS)) {
                    List<SysAuthUserVDO> authUserVDOS = sysAuthUserVService.list(new QueryWrapper<SysAuthUserVDO>()
                            .inSql("lv_id", "select id from sys_auth_user_lv where app_id='" + appId + "' "));

                    for (SysAuthUserLvDO authUserLvDO : authUserLvDOS) {
                        authUserLvDO.setAppId(newAppId);
                        authUserLvDO.setId(createNewId(authUserLvDO.getId()));
                    }
                    if (!sysAuthUserLvService.saveBatch(authUserLvDOS)) {
                        throwCloneException("用户-权限LV", authUserLvDOS);
                    }

                    if (CollectionUtils.isNotEmpty(authUserVDOS)) {
                        for (SysAuthUserVDO authUserVDO : authUserVDOS) {
                            authUserVDO.setAppId(newAppId);
                            authUserVDO.setAuthId(createNewId(authUserVDO.getAuthId()));
                            authUserVDO.setId(createNewId(authUserVDO.getId()));
                            authUserVDO.setLvId(createNewId(authUserVDO.getLvId()));
                        }
                        if (!sysAuthUserVService.saveBatch(authUserVDOS)) {
                            throwCloneException("用户-权限V", authUserVDOS);
                        }
                    }
                }

            }

            // 菜单
            List<SysMenuDO> menuDOS = sysMenuService.list(new QueryWrapper<SysMenuDO>()
                    .eq("app_id", appId).orderByAsc("sort_path"));
            if (CollectionUtils.isNotEmpty(menuDOS)) {
                for (SysMenuDO menuDO : menuDOS) {
                    menuDO.setAppId(newAppId);
                    menuDO.setId(createNewId(menuDO.getId(), true));
                    menuDO.setParentId(createNewId(menuDO.getParentId()));
                    menuDO.setAuthId(createNewId(menuDO.getAuthId()));
                }
                if (!sysMenuService.saveBatch(menuDOS)) {
                    throwCloneException("菜单", menuDOS);
                }
            }


            if (CollectionUtils.isNotEmpty(roleDOS)) {
                // 互斥角色
                List<SysRoleMutexDO> sysRoleMutexDOS = sysRoleMutexService.list(new QueryWrapper<SysRoleMutexDO>()
                        .in("role_id", roleIds));
                if (CollectionUtils.isNotEmpty(sysRoleMutexDOS)) {
                    for (SysRoleMutexDO sysRoleMutexDO : sysRoleMutexDOS) {
                        sysRoleMutexDO.setId(createNewId(sysRoleMutexDO.getId()));
                        sysRoleMutexDO.setRoleMutexId(createNewId(sysRoleMutexDO.getRoleMutexId()));
                        sysRoleMutexDO.setRoleId(createNewId(sysRoleMutexDO.getRoleId()));
                    }
                    if (!sysRoleMutexService.saveBatch(sysRoleMutexDOS)) {
                        throwCloneException("互斥角色", sysRoleMutexDOS);
                    }
                }


                // 上下级角色
                List<SysRoleRelationDO> sysRoleRelationDOS = sysRoleRelationService.list(new QueryWrapper<SysRoleRelationDO>()
                        .in("pid", roleIds));
                if (CollectionUtils.isNotEmpty(sysRoleRelationDOS)) {
                    for (SysRoleRelationDO sysRoleRelationDO : sysRoleRelationDOS) {
                        sysRoleRelationDO.setId(createNewId(sysRoleRelationDO.getId()));
                        sysRoleRelationDO.setPid(createNewId(sysRoleRelationDO.getPid()));
                        sysRoleRelationDO.setCid(createNewId(sysRoleRelationDO.getCid()));
                    }
                    if (!sysRoleRelationService.saveBatch(sysRoleRelationDOS)) {
                        throwCloneException("上下级角色", sysRoleRelationDOS);
                    }
                }

                // 权限 角色 LV
                List<SysAuthRoleLvDO> sysAuthRoleLvDOS = sysAuthRoleLvService.list(new QueryWrapper<SysAuthRoleLvDO>()
                        .eq("app_id", appId));
                if (CollectionUtils.isNotEmpty(sysAuthRoleLvDOS)) {
                    // 权限 角色 V
                    List<SysAuthRoleVDO> sysAuthRoleVDOS = sysAuthRoleVService.list(new QueryWrapper<SysAuthRoleVDO>()
                            .in("role_id", roleIds)
                            .inSql("lv_id", "select id from sys_auth_role_lv where app_id='" + appId + "'"));

                    for (SysAuthRoleLvDO sysAuthRoleLvDO : sysAuthRoleLvDOS) {
                        sysAuthRoleLvDO.setId(createNewId(sysAuthRoleLvDO.getId()));
                        sysAuthRoleLvDO.setAppId(newAppId);
                        sysAuthRoleLvDO.setRoleId(createNewId(sysAuthRoleLvDO.getRoleId()));
                    }
                    if (!sysAuthRoleLvService.saveBatch(sysAuthRoleLvDOS)) {
                        throwCloneException("权限-角色LV", sysAuthRoleLvDOS);
                    }

                    if (CollectionUtils.isNotEmpty(sysAuthRoleVDOS)) {
                        for (SysAuthRoleVDO sysAuthRoleVDO : sysAuthRoleVDOS) {
                            sysAuthRoleVDO.setId(createNewId(sysAuthRoleVDO.getId()));
                            sysAuthRoleVDO.setLvId(createNewId(sysAuthRoleVDO.getLvId()));
                            sysAuthRoleVDO.setAuthId(createNewId(sysAuthRoleVDO.getAuthId()));
                            sysAuthRoleVDO.setRoleId(createNewId(sysAuthRoleVDO.getRoleId()));
                        }
                        if (!sysAuthRoleVService.saveBatch(sysAuthRoleVDOS)) {
                            throwCloneException("权限-角色V", sysAuthRoleVDOS);
                        }
                    }
                }

                // 用户角色 关系
                List<SysUserRoleDO> sysUserRoleDOS = sysUserRoleService.list(new QueryWrapper<SysUserRoleDO>()
                        .eq("app_id", appId).in("role_id", roleIds));
                if (CollectionUtils.isNotEmpty(sysUserRoleDOS)) {
                    for (SysUserRoleDO sysUserRoleDO : sysUserRoleDOS) {
                        sysUserRoleDO.setId(createNewId(sysUserRoleDO.getId()));
                        sysUserRoleDO.setAppId(newAppId);
                        sysUserRoleDO.setRoleId(createNewId(sysUserRoleDO.getRoleId()));
                    }
                    if (!sysUserRoleService.saveBatch(sysUserRoleDOS)) {
                        throwCloneException("用户角色-关系", sysUserRoleDOS);
                    }
                }
                List<SysUserRoleLvDO> userRoleLvDOS = sysUserRoleLvService.list(new QueryWrapper<SysUserRoleLvDO>()
                        .eq("app_id", appId));
                if (CollectionUtils.isNotEmpty(userRoleLvDOS)) {
                    List<SysUserRoleVDO> userRoleVDOS = sysUserRoleVService.list(new QueryWrapper<SysUserRoleVDO>()
                            .inSql("lv_id", "select id from sys_user_role_lv where app_id='" + appId + "' "));

                    for (SysUserRoleLvDO sysUserRoleLvDO : userRoleLvDOS) {
                        sysUserRoleLvDO.setId(createNewId(sysUserRoleLvDO.getId()));
                        sysUserRoleLvDO.setAppId(newAppId);
                    }
                    if (!sysUserRoleLvService.saveBatch(userRoleLvDOS)) {
                        throwCloneException("用户角色 关系V", userRoleLvDOS);
                    }


                    if (CollectionUtils.isNotEmpty(userRoleVDOS)) {
                        for (SysUserRoleVDO sysUserRoleVDO : userRoleVDOS) {
                            sysUserRoleVDO.setId(createNewId(sysUserRoleVDO.getId()));
                            sysUserRoleVDO.setLvId(createNewId(sysUserRoleVDO.getLvId()));
                            sysUserRoleVDO.setRoleId(createNewId(sysUserRoleVDO.getId()));
                        }
                        if (!sysUserRoleVService.saveBatch(userRoleVDOS)) {
                            throwCloneException("用户角色 关系V", userRoleVDOS);
                        }
                    }
                }

            }

            // 消息
            List<SysMsgSendTypeDO> sysMsgSendTypeDOS = sysMsgSendTypeService.list(new QueryWrapper<SysMsgSendTypeDO>()
                    .eq("app_id", appId));
            if (CollectionUtils.isNotEmpty(sysMsgSendTypeDOS)) {
                for (SysMsgSendTypeDO object : sysMsgSendTypeDOS) {
                    object.setId(createNewId(object.getId(), true));
                    object.setAppId(newAppId);
                }
                if (!sysMsgSendTypeService.saveBatch(sysMsgSendTypeDOS)) {
                    throwCloneException("消息", sysMsgSendTypeDOS);
                }
            }


            List<SysMsgTemplateDO> sysMsgTemplateDOS = sysMsgTemplateService.list(new QueryWrapper<SysMsgTemplateDO>()
                    .eq("app_id", appId));

            if (CollectionUtils.isNotEmpty(sysMsgTemplateDOS)) {
                List<SysMsgTemplateConfigDO> sysMsgTemplateConfigDOS = sysMsgTemplateConfigService.list(new QueryWrapper<SysMsgTemplateConfigDO>()
                        .in("tid", sysMsgTemplateDOS.stream().map(SysMsgTemplateDO::getId).collect(Collectors.toList())));

                for (SysMsgTemplateDO object : sysMsgTemplateDOS) {
                    object.setId(createNewId(object.getId()));
                    object.setAppId(newAppId);
                }
                if (!sysMsgTemplateService.saveBatch(sysMsgTemplateDOS)) {
                    throwCloneException("sysMsgTemplateDOS", sysMsgTemplateDOS);
                }

                if (CollectionUtils.isNotEmpty(sysMsgTemplateConfigDOS)) {
                    for (SysMsgTemplateConfigDO object : sysMsgTemplateConfigDOS) {
                        object.setId(createNewId(object.getId()));
                        object.setTid(createNewId(object.getTid()));
                    }
                    if (!sysMsgTemplateConfigService.saveBatch(sysMsgTemplateConfigDOS)) {
                        throwCloneException("sysMsgTemplateConfigDOS", sysMsgTemplateConfigDOS);
                    }
                }
            }
            List<SysMsgTypeExtendDO> sysMsgTypeExtendDOS = sysMsgTypeExtendService.list(new QueryWrapper<SysMsgTypeExtendDO>()
                    .eq("app_id", appId));
            if (CollectionUtils.isNotEmpty(sysMsgTypeExtendDOS)) {
                for (SysMsgTypeExtendDO object : sysMsgTypeExtendDOS) {
                    object.setId(createNewId(object.getId(), true));
                    object.setAppId(newAppId);
                }
                if (!sysMsgTypeExtendService.saveBatch(sysMsgTypeExtendDOS)) {
                    throwCloneException("sysMsgTypeExtendDOS", sysMsgTypeExtendDOS);
                }
            }

            // 数据源
            List<SysGroupDatasourceDO> sysGroupDatasourceDOS = sysGroupDatasourceService.list(new QueryWrapper<SysGroupDatasourceDO>()
                    .eq("app_id", appId));
            if (CollectionUtils.isNotEmpty(sysGroupDatasourceDOS)) {
                for (SysGroupDatasourceDO object : sysGroupDatasourceDOS) {
                    object.setId(createNewId(object.getId(), true));
                    object.setAppId(newAppId);
                }
                if (!sysGroupDatasourceService.saveBatch(sysGroupDatasourceDOS)) {
                    throwCloneException("数据源", sysGroupDatasourceDOS);
                }
            }

            // 报表
            List<ReportTypeDO> reportTypeDOS = reportTypeService.list(new QueryWrapper<ReportTypeDO>()
                    .eq("app_id", appId));
            if (CollectionUtils.isNotEmpty(reportTypeDOS)) {
                for (ReportTypeDO object : reportTypeDOS) {
                    object.setId(createNewId(object.getId(), true));
                    object.setAppId(newAppId);
                }
                if (!reportTypeService.saveBatch(reportTypeDOS)) {
                    throwCloneException("reportTypeDOS", reportTypeDOS);
                }
            }
            List<ReportInfoDO> reportInfoDOS = reportInfoService.list(new QueryWrapper<ReportInfoDO>()
                    .eq("app_id", appId));
            if (CollectionUtils.isNotEmpty(reportInfoDOS)) {
                for (ReportInfoDO object : reportInfoDOS) {
                    object.setId(createNewId(object.getId(), true));
                    object.setAppId(newAppId);
                }
                if (!reportInfoService.saveBatch(reportInfoDOS)) {
                    throwCloneException("reportInfoDOS", reportInfoDOS);
                }
            }

            // 大屏
            List<BladeVisualDO> bladeVisualDOS = bladeVisualService.list(new QueryWrapper<BladeVisualDO>()
                    .eq("app_id", appId));
            if (CollectionUtils.isNotEmpty(bladeVisualDOS)) {
                for (BladeVisualDO object : bladeVisualDOS) {
                    object.setId(createNewId(object.getId(), true));
                    object.setAppId(newAppId);
                    object.setCategory(createNewId(object.getCategory()));
                    object.setAuthId(createNewId(object.getAuthId()));
                }
                if (!bladeVisualService.saveBatch(bladeVisualDOS)) {
                    throwCloneException("bladeVisualDOS", bladeVisualDOS);
                }
            }


            List<BladeVisualMsgDO> bladeVisualMsgDOS = bladeVisualMsgService.list(new QueryWrapper<BladeVisualMsgDO>()
                    .eq("app_id", appId));
            if (CollectionUtils.isNotEmpty(bladeVisualMsgDOS)) {
                for (BladeVisualMsgDO object : bladeVisualMsgDOS) {
                    object.setId(createNewId(object.getId(), true));
                    object.setAppId(newAppId);
                }
                if (!bladeVisualMsgService.saveBatch(bladeVisualMsgDOS)) {
                    throwCloneException("bladeVisualMsgDOS", bladeVisualMsgDOS);
                }
            }

            List<BladeVisualShowDO> bladeVisualShowDOS = bladeVisualShowService.list(new QueryWrapper<BladeVisualShowDO>()
                    .eq("app_id", appId));
            if (CollectionUtils.isNotEmpty(bladeVisualShowDOS)) {
                for (BladeVisualShowDO object : bladeVisualShowDOS) {
                    object.setId(createNewId(object.getId(), true));
                    object.setAppId(newAppId);
                }
                if (!bladeVisualShowService.saveBatch(bladeVisualShowDOS)) {
                    throwCloneException("bladeVisualShowDOS", bladeVisualShowDOS);
                }
            }

            // app interface
            List<SysAppInterfaceDO> appInterfaceDOS = sysAppInterfaceService.list(new QueryWrapper<SysAppInterfaceDO>()
                    .eq("app_id", appId));
            if (CollectionUtils.isNotEmpty(appInterfaceDOS)) {
                List<String> inteIds = appInterfaceDOS.stream().map(SysAppInterfaceDO::getId).collect(Collectors.toList());
                List<SysAppInterfaceInputDO> sysAppInterfaceInputDOS = sysAppInterfaceInputService.list(new QueryWrapper<SysAppInterfaceInputDO>()
                        .in("interface_id", inteIds));
                List<SysAppInterfaceOutputDO> sysAppInterfaceOutputDOS = sysAppInterfaceOutputService.list(new QueryWrapper<SysAppInterfaceOutputDO>()
                        .in("interface_id", inteIds));

                for (SysAppInterfaceDO object : appInterfaceDOS) {
                    object.setId(createNewId(object.getId()));
                    object.setAppId(newAppId);
                }
                if (!sysAppInterfaceService.saveBatch(appInterfaceDOS)) {
                    throwCloneException("appInterface", appInterfaceDOS);
                }

                if (CollectionUtils.isNotEmpty(sysAppInterfaceInputDOS)) {
                    for (SysAppInterfaceInputDO object : sysAppInterfaceInputDOS) {
                        object.setId(createNewId(object.getId()));
                        object.setInterfaceId(createNewId(object.getInterfaceId()));
                    }
                    if (!sysAppInterfaceInputService.saveBatch(sysAppInterfaceInputDOS)) {
                        throwCloneException("appInterfaceInput", sysAppInterfaceInputDOS);
                    }
                }
                if (CollectionUtils.isNotEmpty(sysAppInterfaceOutputDOS)) {
                    for (SysAppInterfaceOutputDO object : sysAppInterfaceOutputDOS) {
                        object.setId(createNewId(object.getId()));
                        object.setInterfaceId(createNewId(object.getInterfaceId()));
                    }
                    if (!sysAppInterfaceOutputService.saveBatch(sysAppInterfaceOutputDOS)) {
                        throwCloneException("appInterfaceOutput", sysAppInterfaceOutputDOS);
                    }
                }
            }

            // 编码
            List<CodeSequenceDO> codeSequenceDOS = codeSequenceService.list(new QueryWrapper<CodeSequenceDO>()
                    .eq("app_id", appId));
            if (CollectionUtils.isNotEmpty(codeSequenceDOS)) {
                for (CodeSequenceDO object : codeSequenceDOS) {
                    object.setId(createNewId(object.getId(), true));
                    object.setAppId(newAppId);
                }
                if (!codeSequenceService.saveBatch(codeSequenceDOS)) {
                    throwCloneException("codeSequenceDOS", codeSequenceDOS);
                }
            }
            List<CodeTemplateDO> codeTemplateDOS = codeTemplateService.list(new QueryWrapper<CodeTemplateDO>()
                    .eq("app_id", appId));
            if (CollectionUtils.isNotEmpty(codeTemplateDOS)) {
                List<CodeTemplatePartDO> codeTemplatePartDOS = codeTemplatePartService.list(new QueryWrapper<CodeTemplatePartDO>()
                        .in("Template_id", codeTemplateDOS.stream()
                                .map(CodeTemplateDO::getId).collect(Collectors.toList())));
                for (CodeTemplateDO object : codeTemplateDOS) {
                    object.setId(createNewId(object.getId()));
                    object.setAppId(newAppId);
                }
                if (!codeTemplateService.saveBatch(codeTemplateDOS)) {
                    throwCloneException("codeTemplateDOS", codeTemplateDOS);
                }
                if (CollectionUtils.isNotEmpty(codeTemplatePartDOS)) {
                    for (CodeTemplatePartDO object : codeTemplatePartDOS) {
                        object.setId(createNewId(object.getId()));
                        object.setAppId(newAppId);
                        object.setTemplateId(createNewId(object.getTemplateId()));
                    }
                    if (!codeTemplatePartService.saveBatch(codeTemplatePartDOS)) {
                        throwCloneException("codeTemplatePartDOS", codeTemplatePartDOS);
                    }
                }
            }

            // 附件配置
            List<FileConfigurationDO> fileConfigurationDOS = fileConfigurationServiceD.list(new QueryWrapper<FileConfigurationDO>()
                    .eq("app_id", appId));
            if (CollectionUtils.isNotEmpty(fileConfigurationDOS)) {
                for (FileConfigurationDO object : fileConfigurationDOS) {
                    object.setId(createNewId(object.getId(), true));
                    object.setAppId(newAppId);
                }
                if (!fileConfigurationServiceD.saveBatch(fileConfigurationDOS)) {
                    throwCloneException("附件配置", fileConfigurationDOS);
                }
            }

        } else {
            throw new RException("要克隆的应用不存在！");
        }
        return true;
    }

    @Override
    public boolean importData(AceDataVO aceData) {
        // 集团
        if (aceData.getGroup() == null) {
            throwRexception("集团", "group");
        }
        OrgGroupDO group = aceData.getGroup();
        int count = orgGroupService.count(new QueryWrapper<OrgGroupDO>()
                .eq("sort_index", group.getSortIndex()).eq("parent_id", group.getParentId())
                .ne("id", group.getId()));
        if (count > 0) {
            String parentGroupName;
            if (Objects.equals(group.getParentId(), "0")) {
                parentGroupName = "空";
            } else {
                parentGroupName = orgGroupService.getById(group.getParentId()).getName();
            }
            throw new RException("数据库里存在和当前导入数据中集团一样的排序号，" +
                    "请到【集团管理】菜单修改父集团为【" + parentGroupName + "】，排序号是" + group.getSortIndex() + "的集团的排序号！");
        }
        if (!orgGroupService.saveOrUpdate(group)) {
            throwRexception("集团", "group");
        }

        // 职称
        if (CollectionUtils.isNotEmpty(aceData.getJobList())) {
            if (!bdJobService.saveOrUpdateBatch(aceData.getJobList())) {
                throwRexception("职称", "jobList");
            }
        }

        // 人员证件类型
        if (CollectionUtils.isNotEmpty(aceData.getPersonIdTypeList())) {
            if (!bdPersonIdTypeService.saveOrUpdateBatch(aceData.getPersonIdTypeList())) {
                throwRexception("人员证件类型", "personIdTypeList");
            }
        }

        boolean addUser = false;
        // 业务单元
        // 存在业务单元才可保存下面的数据
        if (CollectionUtils.isNotEmpty(aceData.getOrganizationList())) {
            if (CollectionUtils.isNotEmpty(aceData.getOrganizationVList())) {
                if (!orgOrganizationVService.saveOrUpdateBatch(aceData.getOrganizationVList())) {
                    throwRexception("业务单元固化版本", "organizationVList");
                }
                if (CollectionUtils.isNotEmpty(aceData.getOrganizationVTypeList())) {
                    if (!orgOrganizationVTypeService.saveOrUpdateBatch(aceData.getOrganizationVTypeList())) {
                        throwRexception("业务单元类型固化版本", "organizationVTypeList");
                    }
                }
            }
            if (CollectionUtils.isNotEmpty(aceData.getOrganizationList())) {
                if (!orgOrganizationService.saveOrUpdateBatch(aceData.getOrganizationList())) {
                    throwRexception("业务单元", "organizationList");
                }
                if (CollectionUtils.isNotEmpty(aceData.getOrganizationTypeList())) {
                    if (!orgOrganizationTypeService.saveOrUpdateBatch(aceData.getOrganizationTypeList())) {
                        throwRexception("业务单元固化版本", "organizationVList");
                    }
                }
            }

            // 部门
            if (CollectionUtils.isNotEmpty(aceData.getOrgDepartmentVList())) {
                if (!orgDepartmentVService.saveOrUpdateBatch(aceData.getOrgDepartmentVList())) {
                    throwRexception("部门固化版本", "orgDepartmentVList");
                }
            }
            if (CollectionUtils.isNotEmpty(aceData.getOrgDepartmentList())) {
                if (!orgDepartmentService.saveOrUpdateBatch(aceData.getOrgDepartmentList())) {
                    throwRexception("部门", "orgDepartmentList");
                }
            }

            // 岗位
            if (CollectionUtils.isNotEmpty(aceData.getPostList())) {
                if (!bdPostService.saveOrUpdateBatch(aceData.getPostList())) {
                    throwRexception("岗位", "postList");
                }
            }

            // 人员
            if (CollectionUtils.isNotEmpty(aceData.getPersonDocList())) {
                if (!bdPersonDocService.saveOrUpdateBatch(aceData.getPersonDocList())) {
                    throwRexception("人员", "personDocList");
                }
            }

            // 职务信息
            if (aceData.getPersonJobList() != null) {
                if (!bdPersonJobService.saveOrUpdateBatch(aceData.getPersonJobList())) {
                    throwRexception("职务信息", "personJobList");
                }
            }

            // 用户
            // 新增用户 需要 重置密码
            addUser = CollectionUtils.isNotEmpty(aceData.getUserList());
            if (CollectionUtils.isNotEmpty(aceData.getUserList())) {
//                List<String> orgIds = aceData.getOrganizationList().stream().map(OrgOrganizationDO::getId).collect(Collectors.toList());
//                List<SysUserDO> users = sysUserService.list(new QueryWrapper<SysUserDO>()
//                        .select("id").in("organization_id", orgIds).eq("is_delete", 0));
//                List<String> newUserIds = new ArrayList<>();
//                if (CollectionUtils.isNotEmpty(users)) {
//                    List<String> userIds = users.stream().map(SysUserDO::getId).collect(Collectors.toList());
//                    List<String> allUserIds = aceData.getUserList().stream().map(SysUserDO::getId).collect(Collectors.toList());
//                    allUserIds.removeAll(userIds);
//                    newUserIds.addAll(allUserIds);
//                }
                if (!sysUserService.saveOrUpdateBatch(aceData.getUserList())) {
                    throwRexception("用户", "userList");
                }
                //List<String> newUserIds = aceData.getUserList().stream().map(SysUserDO::getId).collect(Collectors.toList());

                // 重置全部用户密码
                //if (CollectionUtils.isNotEmpty(newUserIds)) {
                // 获取默认密码
                String defaultPassword = sysConfigService.getValue("defaultPassword");
                if (StringUtils.isBlank(defaultPassword)) {
                    throw new RException(InternationUtils.getInternationalMsg("UNKNOWN_EXCEPTION"));
                }
                String password = "";
                try {
                    password = GMBaseUtil.decryptString(defaultPassword);
                    // 默认密码初始化时被加密 解密后第7位及之后为正确的默认密码
                    password = password.substring(6);
                } catch (Exception e) {
                    e.printStackTrace();
                    throw new RException(InternationUtils.getInternationalMsg("ERROR_CIPHER_PASSWORD"));
                }

                List<SysUserDO> newUsers = aceData.getUserList();//.parallelStream().filter(user -> newUserIds.contains(user.getId())).collect(Collectors.toList());
                for (SysUserDO user : newUsers) {
                    setDefaultPassword(user.getId(), user.getUserName(), password);
                }
                //}
            }

            if (CollectionUtils.isNotEmpty(aceData.getAdminOrgList())) {
                if (!sysUserAdminOrgService.saveOrUpdateBatch(aceData.getAdminOrgList())) {
                    throwRexception("应用管理员管控域", "adminOrgList");
                }
            }

        }

        if (CollectionUtils.isNotEmpty(aceData.getAppList())) {
            // 应用
            if (!sysGroupAppService.saveOrUpdateBatch(aceData.getAppList())) {
                throwRexception("应用", "appList");
            }

            // 权限
            if (CollectionUtils.isNotEmpty(aceData.getAuthList())) {
                if (!sysAuthService.saveOrUpdateBatch(aceData.getAuthList())) {
                    throwRexception("权限", "authList");
                }
            }

            // 菜单
            if (CollectionUtils.isNotEmpty(aceData.getMenuList())) {
                if (!sysMenuService.saveOrUpdateBatch(aceData.getMenuList())) {
                    throwRexception("菜单", "menuList");
                }
            }

            // 菜单
            if (CollectionUtils.isNotEmpty(aceData.getAppMenuDisplayList())) {
                if (!sysAppMenuDisplayService.saveOrUpdateBatch(aceData.getAppMenuDisplayList())) {
                    throwRexception("应用菜单", "appMenuDisplayList");
                }
            }

            // 消息
            if (CollectionUtils.isNotEmpty(aceData.getMsgSendTypeList())) {
                if (!sysMsgSendTypeService.saveOrUpdateBatch(aceData.getMsgSendTypeList())) {
                    throwRexception("消息发送类型", "msgSendTypeList");
                }
            }
            if (CollectionUtils.isNotEmpty(aceData.getMsgTemplateList())) {
                if (!sysMsgTemplateService.saveOrUpdateBatch(aceData.getMsgTemplateList())) {
                    throwRexception("消息模板", "msgTemplateList");
                }
            }
            if (CollectionUtils.isNotEmpty(aceData.getMsgTemplateConfigList())) {
                if (!sysMsgTemplateConfigService.saveOrUpdateBatch(aceData.getMsgTemplateConfigList())) {
                    throwRexception("消息模板配置", "msgTemplateConfigList");
                }
            }
            if (CollectionUtils.isNotEmpty(aceData.getMsgTypeExtendList())) {
                if (!sysMsgTypeExtendService.saveOrUpdateBatch(aceData.getMsgTypeExtendList())) {
                    throwRexception("消息类型扩展", "msgTypeExtendList");
                }
            }

            // 角色
            if (CollectionUtils.isNotEmpty(aceData.getRoleList())) {
                if (!sysRoleService.saveOrUpdateBatch(aceData.getRoleList())) {
                    throwRexception("角色", "roleList");
                }
            }

            if (CollectionUtils.isNotEmpty(aceData.getRoleMutexList())) {
                if (!sysRoleMutexService.saveOrUpdateBatch(aceData.getRoleMutexList())) {
                    throwRexception("角色互斥", "roleMutexList");
                }
            }

            if (CollectionUtils.isNotEmpty(aceData.getRoleRelationList())) {
                if (!sysRoleRelationService.saveOrUpdateBatch(aceData.getRoleRelationList())) {
                    throwRexception("角色关联", "roleRelationList");
                }
            }

            // 权限角色
            if (CollectionUtils.isNotEmpty(aceData.getAuthRoleList())) {
                if (!sysAuthRoleService.saveOrUpdateBatch(aceData.getAuthRoleList())) {
                    throwRexception("权限-角色", "authRoleList");
                }
            }
            if (CollectionUtils.isNotEmpty(aceData.getAuthRoleLvList())) {
                if (!sysAuthRoleLvService.saveOrUpdateBatch(aceData.getAuthRoleLvList())) {
                    throwRexception("权限-角色", "authRoleLvList");
                }
            }
            if (CollectionUtils.isNotEmpty(aceData.getAuthRoleVList())) {
                if (!sysAuthRoleVService.saveOrUpdateBatch(aceData.getAuthRoleVList())) {
                    throwRexception("权限-角色", "authRoleVList");
                }
            }

            // 数据源
            if (CollectionUtils.isNotEmpty(aceData.getGroupDatasourceList())) {
                if (!sysGroupDatasourceService.saveOrUpdateBatch(aceData.getGroupDatasourceList())) {
                    throwRexception("数据源", "groupDatasourceList");
                }
            }

            // 报表
            if (CollectionUtils.isNotEmpty(aceData.getReportTypeList())) {
                if (!reportTypeService.saveOrUpdateBatch(aceData.getReportTypeList())) {
                    throwRexception("报表类型", "reportTypeList");
                }
            }

            if (CollectionUtils.isNotEmpty(aceData.getReportInfoList())) {
                if (!reportInfoService.saveOrUpdateBatch(aceData.getReportInfoList())) {
                    throwRexception("报表信息", "reportInfoList");
                }
            }

            // API
            if (CollectionUtils.isNotEmpty(aceData.getApiList())) {
                if (!sysApiResourceService.saveOrUpdateBatch(aceData.getApiList())) {
                    throwRexception("api资源", "apiList");
                }
            }

            if (CollectionUtils.isNotEmpty(aceData.getAuthApiList())) {
                if (!sysAuthApiService.saveOrUpdateBatch(aceData.getAuthApiList())) {
                    throwRexception("权限API", "authApiList");
                }
            }

            // 如果有用户
            if (addUser) {
                // 应用管理员 应用管控范围
                if (CollectionUtils.isNotEmpty(aceData.getScopeAppList())) {
                    if (!sysAuthScopeAppService.saveOrUpdateBatch(aceData.getScopeAppList())) {
                        throwRexception("应用管理员-应用管控范围", "scopeAppList");
                    }
                }
                // AuthMix
                if (CollectionUtils.isNotEmpty(aceData.getAuthMixList())) {
                    if (!sysAuthMixService.saveOrUpdateBatch(aceData.getAuthMixList())) {
                        throwRexception("权限-用户", "authMixList");
                    }
                }
                // UserRole
                if (CollectionUtils.isNotEmpty(aceData.getUserRoleList())) {
                    if (!sysUserRoleService.saveOrUpdateBatch(aceData.getUserRoleList())) {
                        throwRexception("用户-角色", "userRoleList");
                    }
                }
                if (CollectionUtils.isNotEmpty(aceData.getUserRoleLvList())) {
                    if (!sysUserRoleLvService.saveOrUpdateBatch(aceData.getUserRoleLvList())) {
                        throwRexception("用户-角色", "userRoleLvList");
                    }
                }
                if (CollectionUtils.isNotEmpty(aceData.getUserRoleVList())) {
                    if (!sysUserRoleVService.saveOrUpdateBatch(aceData.getUserRoleVList())) {
                        throwRexception("用户-角色", "userRoleVList");
                    }
                }

                // AuthUser
                if (CollectionUtils.isNotEmpty(aceData.getAuthUserList())) {
                    if (!sysAuthUserService.saveOrUpdateBatch(aceData.getAuthUserList())) {
                        throwRexception("权限-用户", "authUserList");
                    }
                }
                if (CollectionUtils.isNotEmpty(aceData.getAuthUserLvList())) {
                    if (!sysAuthUserLvService.saveOrUpdateBatch(aceData.getAuthUserLvList())) {
                        throwRexception("权限-用户", "authUserLvList");
                    }
                }
                if (CollectionUtils.isNotEmpty(aceData.getAuthUserVList())) {
                    if (!sysAuthUserVService.saveOrUpdateBatch(aceData.getAuthUserVList())) {
                        throwRexception("权限-用户", "authUserVList");
                    }
                }

                // ApiMix
                if (CollectionUtils.isNotEmpty(aceData.getApiMixList())) {
                    if (!sysApiMixService.saveOrUpdateBatch(aceData.getApiMixList())) {
                        throwRexception("api资源-用户", "apiMixList");
                    }
                }

            }

            // 大屏
            if (CollectionUtils.isNotEmpty(aceData.getBladeVisualList())) {
                if (!bladeVisualService.saveOrUpdateBatch(aceData.getBladeVisualList())) {
                    throwRexception("大屏", "bladeVisualList");
                }
            }
            if (CollectionUtils.isNotEmpty(aceData.getBladeVisualMapList())) {
                if (!bladeVisualMapService.saveOrUpdateBatch(aceData.getBladeVisualMapList())) {
                    throwRexception("大屏地图", "bladeVisualMapList");
                }
            }
            if (CollectionUtils.isNotEmpty(aceData.getBladeVisualMsgList())) {
                if (!bladeVisualMsgService.saveOrUpdateBatch(aceData.getBladeVisualMsgList())) {
                    throwRexception("大屏消息", "bladeVisualMsgList");
                }
            }
            if (CollectionUtils.isNotEmpty(aceData.getBladeVisualShowList())) {
                if (!bladeVisualShowService.saveOrUpdateBatch(aceData.getBladeVisualShowList())) {
                    throwRexception("大屏显示", "bladeVisualShowList");
                }
            }

            // 接口
            if (CollectionUtils.isNotEmpty(aceData.getAppInterfaceList())) {
                if (!sysAppInterfaceService.saveOrUpdateBatch(aceData.getAppInterfaceList())) {
                    throwRexception("接口", "appInterfaceList");
                }
            }

            if (CollectionUtils.isNotEmpty(aceData.getAppInterfaceInputList())) {
                if (!sysAppInterfaceInputService.saveOrUpdateBatch(aceData.getAppInterfaceInputList())) {
                    throwRexception("接口", "appInterfaceInputList");
                }
            }

            if (CollectionUtils.isNotEmpty(aceData.getAppInterfaceOutputList())) {
                if (!sysAppInterfaceOutputService.saveOrUpdateBatch(aceData.getAppInterfaceOutputList())) {
                    throwRexception("接口", "appInterfaceOutputList");
                }
            }

            // 编码
            if (CollectionUtils.isNotEmpty(aceData.getCodeSequenceList())) {
                if (!codeSequenceService.saveOrUpdateBatch(aceData.getCodeSequenceList())) {
                    throwRexception("编码", "codeSequenceList");
                }
            }

            if (CollectionUtils.isNotEmpty(aceData.getCodeTemplateList())) {
                if (!codeTemplateService.saveOrUpdateBatch(aceData.getCodeTemplateList())) {
                    throwRexception("编码", "codeTemplateList");
                }
            }

            if (CollectionUtils.isNotEmpty(aceData.getCodeTemplatePartList())) {
                if (!codeTemplatePartService.saveOrUpdateBatch(aceData.getCodeTemplatePartList())) {
                    throwRexception("编码", "codeTemplatePartList");
                }
            }

            // 文件配置项
            if (CollectionUtils.isNotEmpty(aceData.getFileConfigurationList())) {
                if (!fileConfigurationServiceD.saveOrUpdateBatch(aceData.getFileConfigurationList())) {
                    throwRexception("文件配置项", "fileConfigurationList");
                }
            }
        }

        // 配置项
        if (CollectionUtils.isNotEmpty(aceData.getConfigList())) {
            if (!sysConfigService.saveOrUpdateBatch(aceData.getConfigList())) {
                throwRexception("配置项", "configList");
            }
        }
        // 字典
        if (CollectionUtils.isNotEmpty(aceData.getDictList())) {
            if (!sysDictService.saveOrUpdateBatch(aceData.getDictList())) {
                throwRexception("字典", "dictList");
            }
        }
        // 字典值
        if (CollectionUtils.isNotEmpty(aceData.getDictValueList())) {
            if (!sysDictValueService.saveOrUpdateBatch(aceData.getDictValueList())) {
                throwRexception("字典值", "dictValueList");
            }
        }
        if (!sysAuditLogService.saveLog(new AuditLogTypeDO(AuditLogType.warning, "更新"), "导入平台数据", "导入平台数据："+aceData.getGroup().getName())) {
            throw new RException(InternationUtils.getInternationalMsg("AUDIT_LOG_UNKNOWN"));
        }
        return true;
    }

    /**
     * 重置新增用户密码
     *
     * @param userId
     * @param userName
     * @return
     * @author FourLeaves
     * @date 2020/9/3 9:29
     */
    private void setDefaultPassword(String userId, String userName, String defaultPassword) {
        String password;
        try {
            password = GMBaseUtil.pwToCipherPassword(userName, defaultPassword);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RException(InternationUtils.getInternationalMsg("ERROR_CIPHER_PASSWORD"));
        }
        if (!sysUserService.update(new SysUserDO(), new UpdateWrapper<SysUserDO>()
                .eq("id", userId).set("password", password).set("is_first_login", 0).set("password_update_time", LocalDateTime.now()))) {
            throw new RException("更新新用户密码失败！");
        }
    }

    /**
     * @param prefix
     * @param field
     * @return
     * @author FourLeaves
     * @date 2020/9/3 8:54
     */
    private void throwRexception(String prefix, String field) {
        throw new RException(prefix + "数据异常，请检查json文件【" + field + "】项！");
    }
}

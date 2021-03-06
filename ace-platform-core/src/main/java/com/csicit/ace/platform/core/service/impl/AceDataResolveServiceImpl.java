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
        // ??????
        OrgGroupDO groupDO = orgGroupService.getById(groupId);
        aceDataVO.setGroup(groupDO);

        /*****************************?????????*******************************************/
        // ????????????
        aceDataVO.setJobList(bdJobService.list(new QueryWrapper<BdJobDO>()
                .eq("group_id", groupId)));

        // ??????????????????
        aceDataVO.setPersonIdTypeList(bdPersonIdTypeService.list(new QueryWrapper<BdPersonIdTypeDO>()
                .eq("group_id", groupId)));

        // ???????????????
        aceDataVO.setConfigList(sysConfigService.list(new QueryWrapper<SysConfigDO>()
                .eq("group_id", groupId).eq("scope", 2)));

        // ????????????
        List<SysDictDO> dictDOS = sysDictService.list(new QueryWrapper<SysDictDO>()
                .eq("group_id", groupId).eq("scope", 2));
        if (CollectionUtils.isNotEmpty(dictDOS)) {
            aceDataVO.setDictList(dictDOS);
            List<SysDictValueDO> dictValueDOS = sysDictValueService.list(new QueryWrapper<SysDictValueDO>()
                    .in("type_id", dictDOS.stream().map(SysDictDO::getId).collect(Collectors.toList())).orderByAsc("sort_path"));
            aceDataVO.setDictValueList(dictValueDOS);
        }
        /************************************************************************/

        // ??????????????????
        List<String> userIds = new ArrayList<>();

        if (Objects.equals(type, "org") || Objects.equals(type, "all")) {
            // ???????????????

            // ????????????
            List<OrgOrganizationDO> organizationDOS = new ArrayList<>();
            List<String> orgIds = new ArrayList<>();
            if (Objects.equals(type, "all")) {
                organizationDOS = orgOrganizationService.list(new QueryWrapper<OrgOrganizationDO>()
                        .eq("is_delete", 0).eq("group_id", groupId).orderByAsc("sort_path"));
            } else {
                if (StringUtils.isNotBlank(params.get("orgIds"))) {
                    orgIds.addAll(Arrays.asList(params.get("orgIds").split(",")));
                    // ???????????????????????????
                    orgIds.add(groupId);
                    if (CollectionUtils.isNotEmpty(orgIds)) {
                        organizationDOS = orgOrganizationService.list(new QueryWrapper<OrgOrganizationDO>()
                                .eq("is_delete", 0).in("id", orgIds).orderByAsc("sort_path"));
                    }
                }
            }

            // ???????????????????????? ??????????????????
            if (CollectionUtils.isNotEmpty(organizationDOS)) {
                aceDataVO.setOrganizationList(organizationDOS);
                // ??????????????????
                orgIds = organizationDOS.stream().map(OrgOrganizationDO::getId).collect(Collectors.toList());
                // ??????????????????
                aceDataVO.setOrganizationTypeList(orgOrganizationTypeService.list(new QueryWrapper<OrgOrganizationTypeDO>()
                        .in("id", orgIds)));
                // ????????????????????????
                List<OrgOrganizationVDO> orgOrganizationVDOS = orgOrganizationVService.list(new QueryWrapper<OrgOrganizationVDO>()
                        .in("organization_id", orgIds));
                if (CollectionUtils.isNotEmpty(orgOrganizationVDOS)) {
                    aceDataVO.setOrganizationVList(orgOrganizationVDOS);
                    // ??????????????????????????????
                    aceDataVO.setOrganizationVTypeList(orgOrganizationVTypeService.list(new QueryWrapper<OrgOrganizationVTypeDO>()
                            .in("id", orgOrganizationVDOS.stream().map(OrgOrganizationVDO::getId).collect(Collectors.toList()))));
                }
                // ??????
                List<OrgDepartmentDO> departmentDOS = orgDepartmentService.list(new QueryWrapper<OrgDepartmentDO>()
                        .eq("is_delete", 0).in("organization_id", orgIds).orderByAsc("sort_path"));
                List<String> depIds = new ArrayList<>();
                if (CollectionUtils.isNotEmpty(departmentDOS)) {
                    aceDataVO.setOrgDepartmentList(departmentDOS);
                    depIds = departmentDOS.stream().map(OrgDepartmentDO::getId).collect(Collectors.toList());
                    // ??????????????????
                    aceDataVO.setOrgDepartmentVList(orgDepartmentVService.list(new QueryWrapper<OrgDepartmentVDO>()
                            .in("DEPARTMENT_ID", depIds)));
                    // ????????????
                    aceDataVO.setPostList(bdPostService.list(new QueryWrapper<BdPostDO>()
                            .in("DEPARTMENT_ID", depIds)));
                }
                // ??????
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
                // ??????
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
            // ???????????????????????????????????????????????????????????????????????????????????????????????????
            if (CollectionUtils.isNotEmpty(aceDataVO.getOrganizationList())) {
                aceDataVO.getOrganizationList().add(orgOrganizationService.getById(groupId));
                // ??????????????????
                aceDataVO.getOrganizationTypeList().add(orgOrganizationTypeService.getById(groupId));
                // ????????????????????????
                List<OrgOrganizationVDO> orgOrganizationVDOS = orgOrganizationVService.list(new QueryWrapper<OrgOrganizationVDO>()
                        .eq("organization_id", groupId));
                if (CollectionUtils.isNotEmpty(orgOrganizationVDOS)) {
                    aceDataVO.setOrganizationVList(orgOrganizationVDOS);
                    // ??????????????????????????????
                    aceDataVO.setOrganizationVTypeList(orgOrganizationVTypeService.list(new QueryWrapper<OrgOrganizationVTypeDO>()
                            .in("id", orgOrganizationVDOS.stream().map(OrgOrganizationVDO::getId).collect(Collectors.toList()))));
                }
            }
            // ???????????????
            // ????????????
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
                // ????????????
                appIds = appDOList.stream().map(SysGroupAppDO::getId).collect(Collectors.toList());

                // ??????????????? ???????????????
                if (Objects.equals(type, "all") && CollectionUtils.isNotEmpty(userIds)) {
                    aceDataVO.setScopeAppList(sysAuthScopeAppService.list(new QueryWrapper<SysAuthScopeAppDO>()
                            .in("app_id", appIds).in("user_id", userIds)));
                }

                // ????????? ??????  + ?????????
                List<SysConfigDO> configDOS = sysConfigService.list(new QueryWrapper<SysConfigDO>()
                        .in("app_id", appIds).eq("scope", 3));
                if (CollectionUtils.isNotEmpty(aceDataVO.getConfigList())) {
                    aceDataVO.getConfigList().addAll(configDOS);
                } else {
                    aceDataVO.setConfigList(configDOS);
                }

                // ?????? ??????  + ?????????
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

                // ??????
                List<SysAuthDO> authDOS = sysAuthService.list(new QueryWrapper<SysAuthDO>()
                        .in("app_id", appIds).orderByAsc("sort_path"));
                if (CollectionUtils.isNotEmpty(authDOS)) {
                    aceDataVO.setAuthList(authDOS);
                    List<String> authIds = authDOS.stream().map(SysAuthDO::getId).collect(Collectors.toList());

                    // ?????? ?????? ??????
                    List<SysAuthRoleDO> authRoleDOS = sysAuthRoleService.list(new QueryWrapper<SysAuthRoleDO>()
                            .in("auth_id", authIds));
                    aceDataVO.setAuthRoleList(authRoleDOS);
                    // auth api
                    aceDataVO.setAuthApiList(sysAuthApiService.list(new QueryWrapper<SysAuthApiDO>()
                            .in("auth_id", authIds)));
                    if (CollectionUtils.isNotEmpty(userIds)) {
                        // ?????? api
                        aceDataVO.setApiMixList(sysApiMixService.list(new QueryWrapper<SysApiMixDO>()
                                .in("auth_id", authIds).in("app_id", appIds).in("user_id", userIds)));
                        // ?????? ??????
                        aceDataVO.setAuthMixList(sysAuthMixService.list(new QueryWrapper<SysAuthMixDO>()
                                .in("app_id", appIds)
                                .in("auth_id", authIds)
                                .in("user_id", userIds)));
                        // ?????? ?????? ??????
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
                // ??????
                List<SysMenuDO> menuDOS = sysMenuService.list(new QueryWrapper<SysMenuDO>()
                        .in("app_id", appIds).orderByAsc("sort_path"));
                aceDataVO.setMenuList(menuDOS);
                // ??????????????????????????????????????????????????? ???????????????
                aceDataVO.setAppMenuDisplayList(sysAppMenuDisplayService.list(new QueryWrapper<SysAppMenuDisplayDO>()
                        .in("app_id", appIds)));

                // ??????
                List<SysRoleDO> roleDOS = sysRoleService.list(new QueryWrapper<SysRoleDO>()
                        .in("app_id", appIds));
                aceDataVO.setRoleList(roleDOS);
                if (CollectionUtils.isNotEmpty(roleDOS)) {
                    List<String> roleIds = roleDOS.stream().map(SysRoleDO::getId).collect(Collectors.toList());
                    // ????????????
                    aceDataVO.setRoleMutexList(sysRoleMutexService.list(new QueryWrapper<SysRoleMutexDO>()
                            .in("role_id", roleIds)));
                    // ???????????????
                    aceDataVO.setRoleRelationList(sysRoleRelationService.list(new QueryWrapper<SysRoleRelationDO>()
                            .in("pid", roleIds)));
                    // ?????? ?????? LV
                    List<SysAuthRoleLvDO> sysAuthRoleLvDOS = sysAuthRoleLvService.list(new QueryWrapper<SysAuthRoleLvDO>()
                            .in("role_id", roleIds));
                    if (CollectionUtils.isNotEmpty(sysAuthRoleLvDOS)) {
                        aceDataVO.setAuthRoleLvList(sysAuthRoleLvDOS);
                        // ?????? ?????? V
                        aceDataVO.setAuthRoleVList(sysAuthRoleVService.list(new QueryWrapper<SysAuthRoleVDO>()
                                .in("lv_id", sysAuthRoleLvDOS.stream().map(SysAuthRoleLvDO::getId).collect(Collectors.toList()))));
                    }
                    if (CollectionUtils.isNotEmpty(userIds)) {
                        // ???????????? ??????
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

                // ??????
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

                // ?????????
                aceDataVO.setGroupDatasourceList(sysGroupDatasourceService.list(new QueryWrapper<SysGroupDatasourceDO>()
                        .in("app_id", appIds)));

                // ??????
                aceDataVO.setReportTypeList(reportTypeService.list(new QueryWrapper<ReportTypeDO>()
                        .in("app_id", appIds)));
                aceDataVO.setReportInfoList(reportInfoService.list(new QueryWrapper<ReportInfoDO>()
                        .in("app_id", appIds)));

                // ??????
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

                // ??????
                aceDataVO.setCodeSequenceList(codeSequenceService.list(new QueryWrapper<CodeSequenceDO>()
                        .in("app_id", appIds)));
                aceDataVO.setCodeTemplateList(codeTemplateService.list(new QueryWrapper<CodeTemplateDO>()
                        .in("app_id", appIds)));
                if (CollectionUtils.isNotEmpty(aceDataVO.getCodeTemplateList())) {
                    aceDataVO.setCodeTemplatePartList(codeTemplatePartService.list(new QueryWrapper<CodeTemplatePartDO>()
                            .in("Template_id", aceDataVO.getCodeTemplateList().stream()
                                    .map(CodeTemplateDO::getId).collect(Collectors.toList()))));
                }

                // ????????????
                aceDataVO.setFileConfigurationList(fileConfigurationServiceD.list(new QueryWrapper<FileConfigurationDO>()
                        .in("app_id", appIds)));

            }

        }


        if (!sysAuditLogService.saveLog(new AuditLogTypeDO(AuditLogType.warning, "??????"), "??????????????????","?????????????????????"+ groupDO.getName())) {
            throw new RException(InternationUtils.getInternationalMsg("AUDIT_LOG_UNKNOWN"));
        }
        return aceDataVO;
    }


    private void throwCloneException(String info, Object o) {
        aceLogger.error(info + "????????????");
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
                throwCloneException("??????", app);
            }
            // ????????? ??????  + ?????????
            List<SysConfigDO> configDOS = sysConfigService.list(new QueryWrapper<SysConfigDO>()
                    .eq("app_id", appId).eq("scope", 3));
            if (CollectionUtils.isNotEmpty(configDOS)) {
                for (SysConfigDO config : configDOS) {
                    config.setAppId(newAppId);
                    config.setId(UuidUtils.createUUID());
                }
                if (!sysConfigService.saveBatch(configDOS)) {
                    throwCloneException("?????????", configDOS);
                }
            }

            // ??????????????????
            // ???????????????ID
            prefix = UuidUtils.createUUID().substring(0, 6);

            // ??????????????? ???????????????
            List<SysAuthScopeAppDO> sysAuthScopeAppDOS = sysAuthScopeAppService.list(new QueryWrapper<SysAuthScopeAppDO>()
                    .eq("app_id", appId));
            if (CollectionUtils.isNotEmpty(sysAuthScopeAppDOS)) {
                for (SysAuthScopeAppDO object : sysAuthScopeAppDOS) {
                    object.setAppId(newAppId);
                    object.setId(createNewId(object.getId()));
                }
                if (!sysAuthScopeAppService.saveBatch(sysAuthScopeAppDOS)) {
                    throwCloneException("???????????????-???????????????", sysAuthScopeAppDOS);
                }
            }


            // ?????? ??????
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
                    throwCloneException("????????????", dictDOST);
                }

                if (CollectionUtils.isNotEmpty(dictValueDOS)) {
                    for (SysDictValueDO dictValue : dictValueDOS) {
                        dictValue.setAppId(newAppId);
                        dictValue.setId(createNewId(dictValue.getId()));
                        dictValue.setTypeId(createNewId(dictValue.getTypeId()));
                        dictValue.setParentId(createNewId(dictValue.getParentId()));

                    }
                    if (!sysDictValueService.saveBatch(dictValueDOS)) {
                        throwCloneException("?????????", dictValueDOS);
                    }
                }

            }

            // ??????
            List<SysRoleDO> roleDOS = sysRoleService.list(new QueryWrapper<SysRoleDO>()
                    .eq("app_id", appId));
            List<String> roleIds = roleDOS.stream().map(SysRoleDO::getId).collect(Collectors.toList());
            if (CollectionUtils.isNotEmpty(roleDOS)) {
                for (SysRoleDO roleDO : roleDOS) {
                    roleDO.setAppId(newAppId);
                    roleDO.setId(createNewId(roleDO.getId()));
                }
                if (!sysRoleService.saveBatch(roleDOS)) {
                    throwCloneException("??????", roleDOS);
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


            // ??????????????????????????????????????????????????? ???????????????
            List<SysAppMenuDisplayDO> sysAppMenuDisplayDOS = sysAppMenuDisplayService.list(new QueryWrapper<SysAppMenuDisplayDO>()
                    .eq("app_id", appId));

            if (CollectionUtils.isNotEmpty(sysAppMenuDisplayDOS)) {
                for (SysAppMenuDisplayDO appMenuDisplayDO : sysAppMenuDisplayDOS) {
                    appMenuDisplayDO.setAppId(newAppId);
                    appMenuDisplayDO.setId(createNewId(appMenuDisplayDO.getId()));
                }
                if (!sysAppMenuDisplayService.saveBatch(sysAppMenuDisplayDOS)) {
                    throwCloneException(" ???????????????????????????????????????????????????", sysAppMenuDisplayDOS);
                }
            }

            // ??????
            List<SysAuthDO> authDOS = sysAuthService.list(new QueryWrapper<SysAuthDO>()
                    .eq("app_id", appId).orderByAsc("sort_path"));
            if (CollectionUtils.isNotEmpty(authDOS)) {

                // ???????????? ????????????????????????
                List<String> authIds = authDOS.stream().map(SysAuthDO::getId).collect(Collectors.toList());

                for (SysAuthDO authDO : authDOS) {
                    authDO.setAppId(newAppId);
                    authDO.setId(createNewId(authDO.getId()));
                    authDO.setParentId(createNewId(authDO.getParentId()));

                }
                if (!sysAuthService.saveBatch(authDOS)) {
                    throwCloneException("??????", authDOS);
                }


                // ?????? ?????? ??????
                List<SysAuthRoleDO> authRoleDOS = sysAuthRoleService.list(new QueryWrapper<SysAuthRoleDO>()
                        .in("auth_id", authIds));
                if (CollectionUtils.isNotEmpty(authRoleDOS)) {
                    for (SysAuthRoleDO authRole : authRoleDOS) {
                        authRole.setAuthId(createNewId(authRole.getAuthId()));
                        authRole.setId(createNewId(authRole.getId()));
                        authRole.setRoleId(createNewId(authRole.getRoleId()));
                    }
                    if (!sysAuthRoleService.saveBatch(authRoleDOS)) {
                        throwCloneException("??????????????????", authRoleDOS);
                    }
                }


                // auth api
                List<SysAuthApiDO> authApiDOS = sysAuthApiService.list(new QueryWrapper<SysAuthApiDO>()
                        .in("auth_id", authIds));
                if (CollectionUtils.isNotEmpty(authApiDOS)) {
                    for (SysAuthApiDO authApi : authApiDOS) {
                        authApi.setAuthId(createNewId(authApi.getAuthId()));
                        authApi.setId(createNewId(authApi.getId()));
                        // ????????????API ??????
                        authApi.setApiId(authApi.getApiId().substring(0,
                                authApi.getApiId().length() - appId.length()) + newAppId);
                    }
                    if (!sysAuthApiService.saveBatch(authApiDOS)) {
                        throwCloneException("??????API??????", authApiDOS);
                    }
                }

                // ?????? api
                List<SysApiMixDO> apiMixDOS = sysApiMixService.list(new QueryWrapper<SysApiMixDO>()
                        .in("auth_id", authIds).eq("app_id", appId));
                if (CollectionUtils.isNotEmpty(apiMixDOS)) {
                    for (SysApiMixDO apiMixDO : apiMixDOS) {
                        apiMixDO.setAppId(newAppId);
                        apiMixDO.setAuthId(createNewId(apiMixDO.getAuthId()));
                        apiMixDO.setId(createNewId(apiMixDO.getId()));
                        // ????????????API ??????
                        apiMixDO.setApiId(apiMixDO.getApiId().substring(0,
                                apiMixDO.getApiId().length() - appId.length()) + newAppId);
                    }
                    if (!sysApiMixService.saveBatch(apiMixDOS)) {
                        throwCloneException("API??????", apiMixDOS);
                    }
                }
                // ?????? ??????
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
                        throwCloneException("??????-??????", authMixDOS);
                    }
                }

                // ?????? ?????? ??????
                List<SysAuthUserDO> authUserDOS = sysAuthUserService.list(new QueryWrapper<SysAuthUserDO>()
                        .in("auth_id", authIds).eq("app_id", appId));
                if (CollectionUtils.isNotEmpty(authUserDOS)) {
                    for (SysAuthUserDO authUserDO : authUserDOS) {
                        authUserDO.setAppId(newAppId);
                        authUserDO.setAuthId(createNewId(authUserDO.getAuthId()));
                        authUserDO.setId(createNewId(authUserDO.getId()));
                    }
                    if (!sysAuthUserService.saveBatch(authUserDOS)) {
                        throwCloneException("??????-??????", authUserDOS);
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
                        throwCloneException("??????-??????LV", authUserLvDOS);
                    }

                    if (CollectionUtils.isNotEmpty(authUserVDOS)) {
                        for (SysAuthUserVDO authUserVDO : authUserVDOS) {
                            authUserVDO.setAppId(newAppId);
                            authUserVDO.setAuthId(createNewId(authUserVDO.getAuthId()));
                            authUserVDO.setId(createNewId(authUserVDO.getId()));
                            authUserVDO.setLvId(createNewId(authUserVDO.getLvId()));
                        }
                        if (!sysAuthUserVService.saveBatch(authUserVDOS)) {
                            throwCloneException("??????-??????V", authUserVDOS);
                        }
                    }
                }

            }

            // ??????
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
                    throwCloneException("??????", menuDOS);
                }
            }


            if (CollectionUtils.isNotEmpty(roleDOS)) {
                // ????????????
                List<SysRoleMutexDO> sysRoleMutexDOS = sysRoleMutexService.list(new QueryWrapper<SysRoleMutexDO>()
                        .in("role_id", roleIds));
                if (CollectionUtils.isNotEmpty(sysRoleMutexDOS)) {
                    for (SysRoleMutexDO sysRoleMutexDO : sysRoleMutexDOS) {
                        sysRoleMutexDO.setId(createNewId(sysRoleMutexDO.getId()));
                        sysRoleMutexDO.setRoleMutexId(createNewId(sysRoleMutexDO.getRoleMutexId()));
                        sysRoleMutexDO.setRoleId(createNewId(sysRoleMutexDO.getRoleId()));
                    }
                    if (!sysRoleMutexService.saveBatch(sysRoleMutexDOS)) {
                        throwCloneException("????????????", sysRoleMutexDOS);
                    }
                }


                // ???????????????
                List<SysRoleRelationDO> sysRoleRelationDOS = sysRoleRelationService.list(new QueryWrapper<SysRoleRelationDO>()
                        .in("pid", roleIds));
                if (CollectionUtils.isNotEmpty(sysRoleRelationDOS)) {
                    for (SysRoleRelationDO sysRoleRelationDO : sysRoleRelationDOS) {
                        sysRoleRelationDO.setId(createNewId(sysRoleRelationDO.getId()));
                        sysRoleRelationDO.setPid(createNewId(sysRoleRelationDO.getPid()));
                        sysRoleRelationDO.setCid(createNewId(sysRoleRelationDO.getCid()));
                    }
                    if (!sysRoleRelationService.saveBatch(sysRoleRelationDOS)) {
                        throwCloneException("???????????????", sysRoleRelationDOS);
                    }
                }

                // ?????? ?????? LV
                List<SysAuthRoleLvDO> sysAuthRoleLvDOS = sysAuthRoleLvService.list(new QueryWrapper<SysAuthRoleLvDO>()
                        .eq("app_id", appId));
                if (CollectionUtils.isNotEmpty(sysAuthRoleLvDOS)) {
                    // ?????? ?????? V
                    List<SysAuthRoleVDO> sysAuthRoleVDOS = sysAuthRoleVService.list(new QueryWrapper<SysAuthRoleVDO>()
                            .in("role_id", roleIds)
                            .inSql("lv_id", "select id from sys_auth_role_lv where app_id='" + appId + "'"));

                    for (SysAuthRoleLvDO sysAuthRoleLvDO : sysAuthRoleLvDOS) {
                        sysAuthRoleLvDO.setId(createNewId(sysAuthRoleLvDO.getId()));
                        sysAuthRoleLvDO.setAppId(newAppId);
                        sysAuthRoleLvDO.setRoleId(createNewId(sysAuthRoleLvDO.getRoleId()));
                    }
                    if (!sysAuthRoleLvService.saveBatch(sysAuthRoleLvDOS)) {
                        throwCloneException("??????-??????LV", sysAuthRoleLvDOS);
                    }

                    if (CollectionUtils.isNotEmpty(sysAuthRoleVDOS)) {
                        for (SysAuthRoleVDO sysAuthRoleVDO : sysAuthRoleVDOS) {
                            sysAuthRoleVDO.setId(createNewId(sysAuthRoleVDO.getId()));
                            sysAuthRoleVDO.setLvId(createNewId(sysAuthRoleVDO.getLvId()));
                            sysAuthRoleVDO.setAuthId(createNewId(sysAuthRoleVDO.getAuthId()));
                            sysAuthRoleVDO.setRoleId(createNewId(sysAuthRoleVDO.getRoleId()));
                        }
                        if (!sysAuthRoleVService.saveBatch(sysAuthRoleVDOS)) {
                            throwCloneException("??????-??????V", sysAuthRoleVDOS);
                        }
                    }
                }

                // ???????????? ??????
                List<SysUserRoleDO> sysUserRoleDOS = sysUserRoleService.list(new QueryWrapper<SysUserRoleDO>()
                        .eq("app_id", appId).in("role_id", roleIds));
                if (CollectionUtils.isNotEmpty(sysUserRoleDOS)) {
                    for (SysUserRoleDO sysUserRoleDO : sysUserRoleDOS) {
                        sysUserRoleDO.setId(createNewId(sysUserRoleDO.getId()));
                        sysUserRoleDO.setAppId(newAppId);
                        sysUserRoleDO.setRoleId(createNewId(sysUserRoleDO.getRoleId()));
                    }
                    if (!sysUserRoleService.saveBatch(sysUserRoleDOS)) {
                        throwCloneException("????????????-??????", sysUserRoleDOS);
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
                        throwCloneException("???????????? ??????V", userRoleLvDOS);
                    }


                    if (CollectionUtils.isNotEmpty(userRoleVDOS)) {
                        for (SysUserRoleVDO sysUserRoleVDO : userRoleVDOS) {
                            sysUserRoleVDO.setId(createNewId(sysUserRoleVDO.getId()));
                            sysUserRoleVDO.setLvId(createNewId(sysUserRoleVDO.getLvId()));
                            sysUserRoleVDO.setRoleId(createNewId(sysUserRoleVDO.getId()));
                        }
                        if (!sysUserRoleVService.saveBatch(userRoleVDOS)) {
                            throwCloneException("???????????? ??????V", userRoleVDOS);
                        }
                    }
                }

            }

            // ??????
            List<SysMsgSendTypeDO> sysMsgSendTypeDOS = sysMsgSendTypeService.list(new QueryWrapper<SysMsgSendTypeDO>()
                    .eq("app_id", appId));
            if (CollectionUtils.isNotEmpty(sysMsgSendTypeDOS)) {
                for (SysMsgSendTypeDO object : sysMsgSendTypeDOS) {
                    object.setId(createNewId(object.getId(), true));
                    object.setAppId(newAppId);
                }
                if (!sysMsgSendTypeService.saveBatch(sysMsgSendTypeDOS)) {
                    throwCloneException("??????", sysMsgSendTypeDOS);
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

            // ?????????
            List<SysGroupDatasourceDO> sysGroupDatasourceDOS = sysGroupDatasourceService.list(new QueryWrapper<SysGroupDatasourceDO>()
                    .eq("app_id", appId));
            if (CollectionUtils.isNotEmpty(sysGroupDatasourceDOS)) {
                for (SysGroupDatasourceDO object : sysGroupDatasourceDOS) {
                    object.setId(createNewId(object.getId(), true));
                    object.setAppId(newAppId);
                }
                if (!sysGroupDatasourceService.saveBatch(sysGroupDatasourceDOS)) {
                    throwCloneException("?????????", sysGroupDatasourceDOS);
                }
            }

            // ??????
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

            // ??????
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

            // ??????
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

            // ????????????
            List<FileConfigurationDO> fileConfigurationDOS = fileConfigurationServiceD.list(new QueryWrapper<FileConfigurationDO>()
                    .eq("app_id", appId));
            if (CollectionUtils.isNotEmpty(fileConfigurationDOS)) {
                for (FileConfigurationDO object : fileConfigurationDOS) {
                    object.setId(createNewId(object.getId(), true));
                    object.setAppId(newAppId);
                }
                if (!fileConfigurationServiceD.saveBatch(fileConfigurationDOS)) {
                    throwCloneException("????????????", fileConfigurationDOS);
                }
            }

        } else {
            throw new RException("??????????????????????????????");
        }
        return true;
    }

    @Override
    public boolean importData(AceDataVO aceData) {
        // ??????
        if (aceData.getGroup() == null) {
            throwRexception("??????", "group");
        }
        OrgGroupDO group = aceData.getGroup();
        int count = orgGroupService.count(new QueryWrapper<OrgGroupDO>()
                .eq("sort_index", group.getSortIndex()).eq("parent_id", group.getParentId())
                .ne("id", group.getId()));
        if (count > 0) {
            String parentGroupName;
            if (Objects.equals(group.getParentId(), "0")) {
                parentGroupName = "???";
            } else {
                parentGroupName = orgGroupService.getById(group.getParentId()).getName();
            }
            throw new RException("?????????????????????????????????????????????????????????????????????" +
                    "???????????????????????????????????????????????????" + parentGroupName + "??????????????????" + group.getSortIndex() + "????????????????????????");
        }
        if (!orgGroupService.saveOrUpdate(group)) {
            throwRexception("??????", "group");
        }

        // ??????
        if (CollectionUtils.isNotEmpty(aceData.getJobList())) {
            if (!bdJobService.saveOrUpdateBatch(aceData.getJobList())) {
                throwRexception("??????", "jobList");
            }
        }

        // ??????????????????
        if (CollectionUtils.isNotEmpty(aceData.getPersonIdTypeList())) {
            if (!bdPersonIdTypeService.saveOrUpdateBatch(aceData.getPersonIdTypeList())) {
                throwRexception("??????????????????", "personIdTypeList");
            }
        }

        boolean addUser = false;
        // ????????????
        // ?????????????????????????????????????????????
        if (CollectionUtils.isNotEmpty(aceData.getOrganizationList())) {
            if (CollectionUtils.isNotEmpty(aceData.getOrganizationVList())) {
                if (!orgOrganizationVService.saveOrUpdateBatch(aceData.getOrganizationVList())) {
                    throwRexception("????????????????????????", "organizationVList");
                }
                if (CollectionUtils.isNotEmpty(aceData.getOrganizationVTypeList())) {
                    if (!orgOrganizationVTypeService.saveOrUpdateBatch(aceData.getOrganizationVTypeList())) {
                        throwRexception("??????????????????????????????", "organizationVTypeList");
                    }
                }
            }
            if (CollectionUtils.isNotEmpty(aceData.getOrganizationList())) {
                if (!orgOrganizationService.saveOrUpdateBatch(aceData.getOrganizationList())) {
                    throwRexception("????????????", "organizationList");
                }
                if (CollectionUtils.isNotEmpty(aceData.getOrganizationTypeList())) {
                    if (!orgOrganizationTypeService.saveOrUpdateBatch(aceData.getOrganizationTypeList())) {
                        throwRexception("????????????????????????", "organizationVList");
                    }
                }
            }

            // ??????
            if (CollectionUtils.isNotEmpty(aceData.getOrgDepartmentVList())) {
                if (!orgDepartmentVService.saveOrUpdateBatch(aceData.getOrgDepartmentVList())) {
                    throwRexception("??????????????????", "orgDepartmentVList");
                }
            }
            if (CollectionUtils.isNotEmpty(aceData.getOrgDepartmentList())) {
                if (!orgDepartmentService.saveOrUpdateBatch(aceData.getOrgDepartmentList())) {
                    throwRexception("??????", "orgDepartmentList");
                }
            }

            // ??????
            if (CollectionUtils.isNotEmpty(aceData.getPostList())) {
                if (!bdPostService.saveOrUpdateBatch(aceData.getPostList())) {
                    throwRexception("??????", "postList");
                }
            }

            // ??????
            if (CollectionUtils.isNotEmpty(aceData.getPersonDocList())) {
                if (!bdPersonDocService.saveOrUpdateBatch(aceData.getPersonDocList())) {
                    throwRexception("??????", "personDocList");
                }
            }

            // ????????????
            if (aceData.getPersonJobList() != null) {
                if (!bdPersonJobService.saveOrUpdateBatch(aceData.getPersonJobList())) {
                    throwRexception("????????????", "personJobList");
                }
            }

            // ??????
            // ???????????? ?????? ????????????
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
                    throwRexception("??????", "userList");
                }
                //List<String> newUserIds = aceData.getUserList().stream().map(SysUserDO::getId).collect(Collectors.toList());

                // ????????????????????????
                //if (CollectionUtils.isNotEmpty(newUserIds)) {
                // ??????????????????
                String defaultPassword = sysConfigService.getValue("defaultPassword");
                if (StringUtils.isBlank(defaultPassword)) {
                    throw new RException(InternationUtils.getInternationalMsg("UNKNOWN_EXCEPTION"));
                }
                String password = "";
                try {
                    password = GMBaseUtil.decryptString(defaultPassword);
                    // ????????????????????????????????? ????????????7????????????????????????????????????
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
                    throwRexception("????????????????????????", "adminOrgList");
                }
            }

        }

        if (CollectionUtils.isNotEmpty(aceData.getAppList())) {
            // ??????
            if (!sysGroupAppService.saveOrUpdateBatch(aceData.getAppList())) {
                throwRexception("??????", "appList");
            }

            // ??????
            if (CollectionUtils.isNotEmpty(aceData.getAuthList())) {
                if (!sysAuthService.saveOrUpdateBatch(aceData.getAuthList())) {
                    throwRexception("??????", "authList");
                }
            }

            // ??????
            if (CollectionUtils.isNotEmpty(aceData.getMenuList())) {
                if (!sysMenuService.saveOrUpdateBatch(aceData.getMenuList())) {
                    throwRexception("??????", "menuList");
                }
            }

            // ??????
            if (CollectionUtils.isNotEmpty(aceData.getAppMenuDisplayList())) {
                if (!sysAppMenuDisplayService.saveOrUpdateBatch(aceData.getAppMenuDisplayList())) {
                    throwRexception("????????????", "appMenuDisplayList");
                }
            }

            // ??????
            if (CollectionUtils.isNotEmpty(aceData.getMsgSendTypeList())) {
                if (!sysMsgSendTypeService.saveOrUpdateBatch(aceData.getMsgSendTypeList())) {
                    throwRexception("??????????????????", "msgSendTypeList");
                }
            }
            if (CollectionUtils.isNotEmpty(aceData.getMsgTemplateList())) {
                if (!sysMsgTemplateService.saveOrUpdateBatch(aceData.getMsgTemplateList())) {
                    throwRexception("????????????", "msgTemplateList");
                }
            }
            if (CollectionUtils.isNotEmpty(aceData.getMsgTemplateConfigList())) {
                if (!sysMsgTemplateConfigService.saveOrUpdateBatch(aceData.getMsgTemplateConfigList())) {
                    throwRexception("??????????????????", "msgTemplateConfigList");
                }
            }
            if (CollectionUtils.isNotEmpty(aceData.getMsgTypeExtendList())) {
                if (!sysMsgTypeExtendService.saveOrUpdateBatch(aceData.getMsgTypeExtendList())) {
                    throwRexception("??????????????????", "msgTypeExtendList");
                }
            }

            // ??????
            if (CollectionUtils.isNotEmpty(aceData.getRoleList())) {
                if (!sysRoleService.saveOrUpdateBatch(aceData.getRoleList())) {
                    throwRexception("??????", "roleList");
                }
            }

            if (CollectionUtils.isNotEmpty(aceData.getRoleMutexList())) {
                if (!sysRoleMutexService.saveOrUpdateBatch(aceData.getRoleMutexList())) {
                    throwRexception("????????????", "roleMutexList");
                }
            }

            if (CollectionUtils.isNotEmpty(aceData.getRoleRelationList())) {
                if (!sysRoleRelationService.saveOrUpdateBatch(aceData.getRoleRelationList())) {
                    throwRexception("????????????", "roleRelationList");
                }
            }

            // ????????????
            if (CollectionUtils.isNotEmpty(aceData.getAuthRoleList())) {
                if (!sysAuthRoleService.saveOrUpdateBatch(aceData.getAuthRoleList())) {
                    throwRexception("??????-??????", "authRoleList");
                }
            }
            if (CollectionUtils.isNotEmpty(aceData.getAuthRoleLvList())) {
                if (!sysAuthRoleLvService.saveOrUpdateBatch(aceData.getAuthRoleLvList())) {
                    throwRexception("??????-??????", "authRoleLvList");
                }
            }
            if (CollectionUtils.isNotEmpty(aceData.getAuthRoleVList())) {
                if (!sysAuthRoleVService.saveOrUpdateBatch(aceData.getAuthRoleVList())) {
                    throwRexception("??????-??????", "authRoleVList");
                }
            }

            // ?????????
            if (CollectionUtils.isNotEmpty(aceData.getGroupDatasourceList())) {
                if (!sysGroupDatasourceService.saveOrUpdateBatch(aceData.getGroupDatasourceList())) {
                    throwRexception("?????????", "groupDatasourceList");
                }
            }

            // ??????
            if (CollectionUtils.isNotEmpty(aceData.getReportTypeList())) {
                if (!reportTypeService.saveOrUpdateBatch(aceData.getReportTypeList())) {
                    throwRexception("????????????", "reportTypeList");
                }
            }

            if (CollectionUtils.isNotEmpty(aceData.getReportInfoList())) {
                if (!reportInfoService.saveOrUpdateBatch(aceData.getReportInfoList())) {
                    throwRexception("????????????", "reportInfoList");
                }
            }

            // API
            if (CollectionUtils.isNotEmpty(aceData.getApiList())) {
                if (!sysApiResourceService.saveOrUpdateBatch(aceData.getApiList())) {
                    throwRexception("api??????", "apiList");
                }
            }

            if (CollectionUtils.isNotEmpty(aceData.getAuthApiList())) {
                if (!sysAuthApiService.saveOrUpdateBatch(aceData.getAuthApiList())) {
                    throwRexception("??????API", "authApiList");
                }
            }

            // ???????????????
            if (addUser) {
                // ??????????????? ??????????????????
                if (CollectionUtils.isNotEmpty(aceData.getScopeAppList())) {
                    if (!sysAuthScopeAppService.saveOrUpdateBatch(aceData.getScopeAppList())) {
                        throwRexception("???????????????-??????????????????", "scopeAppList");
                    }
                }
                // AuthMix
                if (CollectionUtils.isNotEmpty(aceData.getAuthMixList())) {
                    if (!sysAuthMixService.saveOrUpdateBatch(aceData.getAuthMixList())) {
                        throwRexception("??????-??????", "authMixList");
                    }
                }
                // UserRole
                if (CollectionUtils.isNotEmpty(aceData.getUserRoleList())) {
                    if (!sysUserRoleService.saveOrUpdateBatch(aceData.getUserRoleList())) {
                        throwRexception("??????-??????", "userRoleList");
                    }
                }
                if (CollectionUtils.isNotEmpty(aceData.getUserRoleLvList())) {
                    if (!sysUserRoleLvService.saveOrUpdateBatch(aceData.getUserRoleLvList())) {
                        throwRexception("??????-??????", "userRoleLvList");
                    }
                }
                if (CollectionUtils.isNotEmpty(aceData.getUserRoleVList())) {
                    if (!sysUserRoleVService.saveOrUpdateBatch(aceData.getUserRoleVList())) {
                        throwRexception("??????-??????", "userRoleVList");
                    }
                }

                // AuthUser
                if (CollectionUtils.isNotEmpty(aceData.getAuthUserList())) {
                    if (!sysAuthUserService.saveOrUpdateBatch(aceData.getAuthUserList())) {
                        throwRexception("??????-??????", "authUserList");
                    }
                }
                if (CollectionUtils.isNotEmpty(aceData.getAuthUserLvList())) {
                    if (!sysAuthUserLvService.saveOrUpdateBatch(aceData.getAuthUserLvList())) {
                        throwRexception("??????-??????", "authUserLvList");
                    }
                }
                if (CollectionUtils.isNotEmpty(aceData.getAuthUserVList())) {
                    if (!sysAuthUserVService.saveOrUpdateBatch(aceData.getAuthUserVList())) {
                        throwRexception("??????-??????", "authUserVList");
                    }
                }

                // ApiMix
                if (CollectionUtils.isNotEmpty(aceData.getApiMixList())) {
                    if (!sysApiMixService.saveOrUpdateBatch(aceData.getApiMixList())) {
                        throwRexception("api??????-??????", "apiMixList");
                    }
                }

            }

            // ??????
            if (CollectionUtils.isNotEmpty(aceData.getBladeVisualList())) {
                if (!bladeVisualService.saveOrUpdateBatch(aceData.getBladeVisualList())) {
                    throwRexception("??????", "bladeVisualList");
                }
            }
            if (CollectionUtils.isNotEmpty(aceData.getBladeVisualMapList())) {
                if (!bladeVisualMapService.saveOrUpdateBatch(aceData.getBladeVisualMapList())) {
                    throwRexception("????????????", "bladeVisualMapList");
                }
            }
            if (CollectionUtils.isNotEmpty(aceData.getBladeVisualMsgList())) {
                if (!bladeVisualMsgService.saveOrUpdateBatch(aceData.getBladeVisualMsgList())) {
                    throwRexception("????????????", "bladeVisualMsgList");
                }
            }
            if (CollectionUtils.isNotEmpty(aceData.getBladeVisualShowList())) {
                if (!bladeVisualShowService.saveOrUpdateBatch(aceData.getBladeVisualShowList())) {
                    throwRexception("????????????", "bladeVisualShowList");
                }
            }

            // ??????
            if (CollectionUtils.isNotEmpty(aceData.getAppInterfaceList())) {
                if (!sysAppInterfaceService.saveOrUpdateBatch(aceData.getAppInterfaceList())) {
                    throwRexception("??????", "appInterfaceList");
                }
            }

            if (CollectionUtils.isNotEmpty(aceData.getAppInterfaceInputList())) {
                if (!sysAppInterfaceInputService.saveOrUpdateBatch(aceData.getAppInterfaceInputList())) {
                    throwRexception("??????", "appInterfaceInputList");
                }
            }

            if (CollectionUtils.isNotEmpty(aceData.getAppInterfaceOutputList())) {
                if (!sysAppInterfaceOutputService.saveOrUpdateBatch(aceData.getAppInterfaceOutputList())) {
                    throwRexception("??????", "appInterfaceOutputList");
                }
            }

            // ??????
            if (CollectionUtils.isNotEmpty(aceData.getCodeSequenceList())) {
                if (!codeSequenceService.saveOrUpdateBatch(aceData.getCodeSequenceList())) {
                    throwRexception("??????", "codeSequenceList");
                }
            }

            if (CollectionUtils.isNotEmpty(aceData.getCodeTemplateList())) {
                if (!codeTemplateService.saveOrUpdateBatch(aceData.getCodeTemplateList())) {
                    throwRexception("??????", "codeTemplateList");
                }
            }

            if (CollectionUtils.isNotEmpty(aceData.getCodeTemplatePartList())) {
                if (!codeTemplatePartService.saveOrUpdateBatch(aceData.getCodeTemplatePartList())) {
                    throwRexception("??????", "codeTemplatePartList");
                }
            }

            // ???????????????
            if (CollectionUtils.isNotEmpty(aceData.getFileConfigurationList())) {
                if (!fileConfigurationServiceD.saveOrUpdateBatch(aceData.getFileConfigurationList())) {
                    throwRexception("???????????????", "fileConfigurationList");
                }
            }
        }

        // ?????????
        if (CollectionUtils.isNotEmpty(aceData.getConfigList())) {
            if (!sysConfigService.saveOrUpdateBatch(aceData.getConfigList())) {
                throwRexception("?????????", "configList");
            }
        }
        // ??????
        if (CollectionUtils.isNotEmpty(aceData.getDictList())) {
            if (!sysDictService.saveOrUpdateBatch(aceData.getDictList())) {
                throwRexception("??????", "dictList");
            }
        }
        // ?????????
        if (CollectionUtils.isNotEmpty(aceData.getDictValueList())) {
            if (!sysDictValueService.saveOrUpdateBatch(aceData.getDictValueList())) {
                throwRexception("?????????", "dictValueList");
            }
        }
        if (!sysAuditLogService.saveLog(new AuditLogTypeDO(AuditLogType.warning, "??????"), "??????????????????", "?????????????????????"+aceData.getGroup().getName())) {
            throw new RException(InternationUtils.getInternationalMsg("AUDIT_LOG_UNKNOWN"));
        }
        return true;
    }

    /**
     * ????????????????????????
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
            throw new RException("??????????????????????????????");
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
        throw new RException(prefix + "????????????????????????json?????????" + field + "?????????");
    }
}

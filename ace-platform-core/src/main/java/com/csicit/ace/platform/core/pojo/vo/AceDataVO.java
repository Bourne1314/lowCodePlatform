package com.csicit.ace.platform.core.pojo.vo;

import com.csicit.ace.common.pojo.domain.*;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.util.List;

/**
 * Ace平台数据
 *
 * @author FourLeaves
 * @version V1.0
 * @date 2020/8/27 8:32
 */
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AceDataVO {
    /**
     * 集团
     */
    private OrgGroupDO group;

    /**
     * 业务单元
     */
    private List<OrgOrganizationVDO> organizationVList;

    private List<OrgOrganizationDO> organizationList;

    private List<OrgOrganizationTypeDO> organizationTypeList;

    private List<OrgOrganizationVTypeDO> organizationVTypeList;

    /**
     * 部门
     */
    private List<OrgDepartmentVDO> orgDepartmentVList;

    private List<OrgDepartmentDO> orgDepartmentList;

    /**
     * 人员
     */
    private List<BdPersonDocDO> personDocList;

    /**
     * 职称
     */
    private List<BdJobDO> jobList;

    /**
     * 岗位
     */
    private List<BdPostDO> postList;

    /**
     * 人员证件类型
     */
    private List<BdPersonIdTypeDO> personIdTypeList;

    /**
     * 职务信息
     */
    private List<BdPersonJobDO> personJobList;

    /**
     * 用户
     */
    private List<SysUserDO> userList;

    /**
     * 应用
     */
    private List<SysGroupAppDO> appList;

    /**
     * 应用管理员-应用管控域
     */
    private List<SysAuthScopeAppDO> scopeAppList;

    /**
     * 应用管理员-业务单元管控域
     */
    private List<SysUserAdminOrgDO> adminOrgList;

    /**
     * 配置项
     */
    private List<SysConfigDO> configList;

    /**
     * 字典
     */
    private List<SysDictDO> dictList;

    private List<SysDictValueDO> dictValueList;

    /**
     * 权限
     */
    private List<SysAuthDO> authList;

    private List<SysAuthMixDO> authMixList;

    /**
     * 菜单
     */
    private List<SysMenuDO> menuList;

    private List<SysAppMenuDisplayDO> appMenuDisplayList;

    /**
     * 消息
     */
    private List<SysMsgSendTypeDO> msgSendTypeList;

    private List<SysMsgTemplateDO> msgTemplateList;

    private List<SysMsgTemplateConfigDO> msgTemplateConfigList;

    private List<SysMsgTypeExtendDO> msgTypeExtendList;

    /**
     * 角色
     */
    private List<SysRoleDO> roleList;

    private List<SysRoleMutexDO> roleMutexList;

    private List<SysRoleRelationDO> roleRelationList;

    /**
     * 用户角色
     */
    private List<SysUserRoleDO> userRoleList;

    private List<SysUserRoleLvDO> userRoleLvList;

    private List<SysUserRoleVDO> userRoleVList;

    /**
     * 权限角色
     */
    private List<SysAuthRoleDO> authRoleList;

    private List<SysAuthRoleLvDO> authRoleLvList;

    private List<SysAuthRoleVDO> authRoleVList;

    /**
     * 权限用户
     */
    private List<SysAuthUserDO> authUserList;

    private List<SysAuthUserLvDO> authUserLvList;

    private List<SysAuthUserVDO> authUserVList;
    /**
     * 数据源
     */
    private List<SysGroupDatasourceDO> groupDatasourceList;

    /**
     * 报表
     */
    private List<ReportTypeDO> reportTypeList;

    private List<ReportInfoDO> reportInfoList;

    /**
     * API
     */
    private List<SysApiResourceDO> apiList;

    private List<SysAuthApiDO> authApiList;

    private List<SysApiMixDO> apiMixList;

    /**
     * 大屏
     */

    private List<BladeVisualDO> bladeVisualList;

    private List<BladeVisualMapDO> bladeVisualMapList;

    private List<BladeVisualMsgDO> bladeVisualMsgList;

    private List<BladeVisualShowDO> bladeVisualShowList;

    /**
     * 接口
     */
    private List<SysAppInterfaceDO> appInterfaceList;

    private List<SysAppInterfaceInputDO> appInterfaceInputList;

    private List<SysAppInterfaceOutputDO> appInterfaceOutputList;

    /**
     * 编码
     */
    private List<CodeSequenceDO> codeSequenceList;

    private List<CodeTemplateDO> codeTemplateList;

    private List<CodeTemplatePartDO> codeTemplatePartList;

    /**
     * 文件配置项
     */
    private List<FileConfigurationDO> fileConfigurationList;


}

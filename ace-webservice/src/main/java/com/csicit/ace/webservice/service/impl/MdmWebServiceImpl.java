package com.csicit.ace.webservice.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.csicit.ace.common.exception.RException;
import com.csicit.ace.common.pojo.domain.*;
import com.csicit.ace.common.utils.*;
import com.csicit.ace.common.utils.internationalization.InternationUtils;
import com.csicit.ace.webservice.commonUtils.SqlUtils;
import com.csicit.ace.webservice.mapper.DBHelperMapper;
import com.csicit.ace.webservice.pojo.Employee;
import com.csicit.ace.webservice.pojo.EmployeeList;
import com.csicit.ace.webservice.pojo.Organization;
import com.csicit.ace.webservice.pojo.OrganizationList;
import com.csicit.ace.webservice.service.*;
import com.csicit.ace.webservice.utils.XmlUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.jws.WebService;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author yansiyang
 * @version V1.0
 * @date 2019/8/6 11:25
 */
@Service
@WebService(serviceName = "MdmWebService")
public class MdmWebServiceImpl implements MdmWebService {

    private static Logger logger = LoggerFactory.getLogger(MdmWebService.class);

    private static int largeNum = 100;
    @Autowired
    OrgDepartmentService orgDepartmentService;

    @Autowired
    OrgDepartmentVService orgDepartmentVService;

    @Autowired
    OrgOrganizationService orgOrganizationService;

    @Autowired
    OrgOrganizationVService orgOrganizationVService;

    @Autowired
    OrgOrganizationTypeService orgOrganizationTypeService;

    @Autowired
    OrgOrganizationVTypeService orgOrganizationVTypeService;

    @Autowired
    BdPersonDocService bdPersonDocService;

    @Autowired
    BdPersonJobService bdPersonJobService;

    @Autowired
    SysUserService sysUserService;

    @Autowired
    SqlUtils sqlUtils;

    @Autowired
    DBHelperMapper dbHelperMapper;


    @Value("${ace.config.webservice.deptCodeLength:9}")
    Integer deptCodeLength;

    private Integer getSortIndexForDep(String parentId, String groupId, String orgId) {
        Map<String, Object> params = new HashMap<>();
        params.put("tableName", "org_department");
        params.put("parentId", parentId);
        params.put("groupId", groupId);
        params.put("orgId", orgId);
        return sqlUtils.getMaxSort(params);
    }


    private void debug(String info, Object o) {
        logger.debug("------------------------Webservivce-Start---------------------");
        logger.debug(info);
        logger.debug(JSONObject.toJSONString(o));
        logger.debug("------------------------Webservivce-End-----------------------");
    }

    private boolean updateAllSortPath() {

        OrgOrganizationDO firstOrg = orgOrganizationService.getOne(new QueryWrapper<OrgOrganizationDO>().eq
                ("IS_BUSINESS_UNIT", 1).eq("is_delete", 0).orderByAsc("create_time"));

        List<OrgDepartmentDO> deps = orgDepartmentService.list(new QueryWrapper<OrgDepartmentDO>()
                .eq("parent_id", "0").eq("is_delete", 0)
                .select("id","sort_index"));//.eq("organization_Id", firstOrg.getId()));
        List<OrgDepartmentDO> depsRes = new ArrayList<>();
        for (OrgDepartmentDO dep : deps) {
            dep.setSortPath(SortPathUtils.getSortPath("", dep.getSortIndex()));
            depsRes.add(dep);
            updateDepChildSortPath(dep, depsRes);
        }

        List<OrgOrganizationDO> orgs = orgOrganizationService.list(new QueryWrapper<OrgOrganizationDO>()
                .eq("parent_id", "0").eq("is_delete", 0)
                .select("id","sort_index"));
        List<OrgOrganizationDO> orgsRes = new ArrayList<>();
        for (OrgOrganizationDO org : orgs) {
            org.setSortPath(SortPathUtils.getSortPath("", org.getSortIndex()));
            orgsRes.add(org);
            updateOrgChildSortPath(org, orgsRes);
        }

        if (!(orgDepartmentService.updateBatchById(depsRes) && orgOrganizationService.updateBatchById(orgsRes))) {
            throw new RException("更新排序路径失败");
        }

        return true;
    }

    private void updateDepChildSortPath(OrgDepartmentDO parentDep, List<OrgDepartmentDO> deps) {
        debug("parentDep", parentDep);
        if (orgDepartmentService.count(new QueryWrapper<OrgDepartmentDO>()
        .eq("parent_id", parentDep.getId()).eq("is_delete", 0)) > 0) {
            List<OrgDepartmentDO> childs = orgDepartmentService.list(new QueryWrapper<OrgDepartmentDO>()
                    .eq("parent_id", parentDep.getId()).eq("is_delete", 0)
            .select("id","sort_index"));
            debug("Dep-childs", childs);
            for (OrgDepartmentDO dep : childs) {
                dep.setSortPath(SortPathUtils.getSortPath(parentDep.getSortPath(), dep.getSortIndex()));
                deps.add(dep);
                updateDepChildSortPath(dep, deps);
            }
        }
    }

    private void updateOrgChildSortPath(OrgOrganizationDO parentOrg, List<OrgOrganizationDO> orgs) {
        debug("parentOrg", parentOrg);
        if (orgOrganizationService.count(new QueryWrapper<OrgOrganizationDO>()
                .eq("parent_id", parentOrg.getId()).eq("is_delete", 0)) > 0) {
            List<OrgOrganizationDO> childs = orgOrganizationService.list(new QueryWrapper<OrgOrganizationDO>()
                    .eq("parent_id", parentOrg.getId()).eq("is_delete", 0)
                    .select("id","sort_index"));
            debug("Org-Childs", childs);
            for (OrgOrganizationDO org : childs) {
                org.setSortPath(SortPathUtils.getSortPath(parentOrg.getSortPath(), org.getSortIndex()));
                orgs.add(org);
                updateOrgChildSortPath(org, orgs);
            }
        }
    }




    @Override
    public String opearteOrganization(String xmlStr) {
        /**
         * 取第一个业务单元
         */
        OrgOrganizationDO firstOrg = orgOrganizationService.getOne(new QueryWrapper<OrgOrganizationDO>().eq
                ("IS_BUSINESS_UNIT", 1).eq("is_delete", 0).orderByAsc("create_time"));
        debug("firstOrg", firstOrg);
        OrganizationList list = XmlUtils.convertXmlToJavaBean(OrganizationList.class, xmlStr);
        List<Organization> organizations = list.getOrganizations();
        debug("organizations", organizations);
        if (organizations != null && organizations.size() > 0) {
            List<Organization> addOrganizations = new ArrayList<>();
            List<Organization> updateOrganizations = new ArrayList<>();
            List<Organization> deleteOrganizations = new ArrayList<>();
            List<OrgDepartmentDO> deps = orgDepartmentService.list(new QueryWrapper<OrgDepartmentDO>().select("id",
                    "code").eq("is_delete", 0).eq("organization_Id", firstOrg.getId()));
            debug("deps", deps);
            Set<String> codes = deps.stream().map(OrgDepartmentDO::getCode).collect(Collectors.toSet());
            organizations.forEach(org -> {
                if (Objects.equals(org.getDataStatus(), "0")) {
                    deleteOrganizations.add(org);
                } else {
                    if (codes.contains(org.getDeptcode())) {
                        updateOrganizations.add(org);
                    } else {
                        addOrganizations.add(org);
                    }
                }
            });
            updateAllSortPath();
            if (addOrganizations.size() > 0) {
                logger.info("saveOrganization");
                logger.info(addOrganizations.size() + "");
                debug("addOrganizations", addOrganizations);
                saveOrganization(addOrganizations);
            }
            if (updateOrganizations.size() > 0) {
                logger.info("updateOrganization");
                logger.info(updateOrganizations.size() + "");
                debug("updateOrganizations", updateOrganizations);
                updateOrganization(updateOrganizations);
            }
            if (addOrganizations.size() > 0 || updateOrganizations.size() > 0) {
                updateAllSortPath();
            }
            if (deleteOrganizations.size() > 0) {
                logger.info("deleteOrganization");
                List<String> deleteCodes = deleteOrganizations.stream().map(Organization::getDeptcode).collect
                        (Collectors.toList());
                logger.info(JSONObject.toJSONString(deleteCodes));
                debug("deleteOrganizations", deleteOrganizations);
                deleteOrganization(deleteCodes);
            }
        }
        return "success";
    }

    @Override
    public String operateEmployee(String xmlStr) {
        EmployeeList list = XmlUtils.convertXmlToJavaBean(EmployeeList.class, xmlStr);
        List<Employee> employees = list.getEmployees();
        debug("employees", employees);
        if (employees != null && employees.size() > 0) {
            /**
             * 取第一个业务单元
             */
            OrgOrganizationDO firstOrg = orgOrganizationService.getOne(new QueryWrapper<OrgOrganizationDO>().eq
                    ("IS_BUSINESS_UNIT", 1).eq("is_delete", 0).orderByAsc("create_time"));
            debug("firstOrg", firstOrg);
            List<Employee> addEmployees = new ArrayList<>();
            List<Employee> updateEmployees = new ArrayList<>();
            List<Employee> deleteEmployees = new ArrayList<>();
            List<BdPersonDocDO> persons = bdPersonDocService.list(new QueryWrapper<BdPersonDocDO>().select("id",
                    "code").eq("is_delete", 0).eq("organization_Id", firstOrg.getId()));
            Set<String> codes = persons.stream().map(BdPersonDocDO::getCode).collect(Collectors.toSet());
            employees.forEach(employee -> {
                String deptCode = employee.getDepartment();
                if (StringUtils.isNotBlank(deptCode) && deptCode.length() > 9) {
                    employee.setDepartment(deptCode.substring(0, 9));
                }
                if (Objects.equals(employee.getDataStatus(), "0")) {
                    deleteEmployees.add(employee);
                } else {
                    if (codes.contains(employee.getCode())) {
                        updateEmployees.add(employee);
                    } else {
                        addEmployees.add(employee);
                    }
                }
            });
            if (addEmployees.size() > 0) {
                logger.info("saveEmployee");
                logger.info(addEmployees.size() + "");
                debug("addEmployees", addEmployees);
                if (addEmployees.size() > 500) {
                    for (int i = 0; i * 500 < addEmployees.size(); i++) {
                        saveEmployee(addEmployees.subList(i * 500, ((i + 1) * 500) > addEmployees.size() ? addEmployees.size() : ((i + 1) * 500)));
                    }
                } else {
                    saveEmployee(addEmployees);
                }
            }
            if (updateEmployees.size() > 0) {
                logger.info("updateEmployee");
                logger.info(updateEmployees.size() + "");
                debug("updateEmployees", updateEmployees);
                if (updateEmployees.size() > 500) {
                    for (int i = 0; i * 500 < updateEmployees.size(); i++) {
                        updateEmployee(updateEmployees.subList(i * 500, ((i + 1) * 500) > updateEmployees.size() ? updateEmployees.size() : ((i + 1) * 500)));
                    }
                } else {
                    updateEmployee(updateEmployees);
                }
            }
            if (deleteEmployees.size() > 0) {
                logger.info("deleteEmployee");
                List<String> deleteCodes = deleteEmployees.stream().map(Employee::getCode).collect
                        (Collectors.toList());
                logger.info(JSONObject.toJSONString(deleteCodes));
                debug("deleteEmployees", deleteEmployees);
                if (deleteCodes.size() > 500) {
                    for (int i = 0; i * 500 < deleteCodes.size(); i++) {
                        deleteEmployee(deleteCodes.subList(i * 500, ((i + 1) * 500) > deleteCodes.size() ? deleteCodes.size() : ((i + 1) * 500)));
                    }
                } else {
                    deleteEmployee(deleteCodes);
                }
            }
        }
        return "success";
    }

    /**
     * 更新部门
     *
     * @param orgs
     * @return
     * @author yansiyang
     * @date 2019/8/8 14:47
     */
    public boolean updateOrganization(List<Organization> orgs) {
        if (orgs != null && orgs.size() > 0) {
            List<String> codes = orgs.stream().map(Organization::getDeptcode).collect(Collectors.toList());
            codes.addAll(orgs.stream().map(Organization::getParentdept).collect(Collectors.toList()));
            List<OrgDepartmentDO> deps = orgDepartmentService.list(new QueryWrapper<OrgDepartmentDO>().in("code",
                    codes).eq("is_delete", 0));
            debug("deps", deps);
            Map<String, OrgDepartmentDO> codeDep = new HashMap<>();
            Map<String, OrgDepartmentDO> idDep = new HashMap<>();
            deps.forEach(dep -> {
                codeDep.put(dep.getCode(), dep);
                idDep.put(dep.getId(), dep);
            });
            orgs.forEach(org -> {
                debug("org", org);
                OrgDepartmentDO dep = codeDep.get(org.getDeptcode());
                /**
                 * 判断父节点是否改变
                 */
                OrgDepartmentDO codeParentDep = null;
                if (StringUtils.isNotBlank(org.getParentdept())) {
                    codeParentDep = codeDep.get(org.getParentdept());
                }
                if (codeParentDep == null && StringUtils.isNotBlank(org.getParentdept())) {
                    logger.warn("更新部门，此部门的父节点不存在，部门数据：" + JSONObject.toJSONString(org));
                } else {
                    OrgDepartmentDO idParentDep = null;
                    if (Objects.equals(dep.getParentId(), "0")) {
                        idParentDep = idDep.get(dep.getParentId());
                    }
                    if (codeParentDep == null && idParentDep == null) {

                    } else if (codeParentDep == null) {
                        dep.setParentId("0");
                    } else if (idParentDep == null) {
                        dep.setParentId(codeParentDep.getId());
                    } else if (Objects.equals(codeParentDep.getId(), idParentDep.getId())) {
                        /**
                         * 父节点未改变
                         */
                    } else {
                        dep.setParentId(codeParentDep.getId());
                    }
                    dep.setName(org.getDeptfullname());
                    dep.setRemark(org.getOrgdescription());
                    dep.setUpdateTime(LocalDateTime.now());
                    logger.info("更新部门:" + JSONObject.toJSONString(dep));
                    debug("dep", dep);
                    if (!orgDepartmentService.updateDep(dep)) {
                        throw new RException(InternationUtils.getInternationalMsg("UPDATE_FAILED"));
                    }
                }
            });
        }
        return true;
    }

    /**
     * 删除部门
     *
     * @param codes
     * @return
     * @author yansiyang
     * @date 2019/8/8 14:47
     */
    public boolean deleteOrganization(List<String> codes) {
        if (codes.size() > 0) {
            List<OrgDepartmentDO> deps = orgDepartmentService.list(new QueryWrapper<OrgDepartmentDO>().eq
                    ("is_delete", 0).in("code",
                    codes).select("id"));
            List<String> ids = deps.stream().map(OrgDepartmentDO::getId).collect(Collectors.toList());

            if (!CollectionUtils.isEmpty(ids)) {
                Map<String, Object> params = new HashMap<>();
                params.put("ids", ids);
                params.put("deleteSons", true);
                orgDepartmentService.deleteDep(params);
            }
        }
        return true;
    }


    /**
     * 保存主数据部门
     *
     * @param organizations
     * @return
     * @author yansiyang
     * @date 2019/8/7 8:32
     */
    public String saveOrganization(List<Organization> organizations) {
        if (organizations != null && organizations.size() > 0) {
            List<OrgDepartmentDO> deps = new ArrayList<>();
            /**
             * 取第一个业务单元
             */
            OrgOrganizationDO firstOrg = orgOrganizationService.getOne(new QueryWrapper<OrgOrganizationDO>().eq
                    ("IS_BUSINESS_UNIT", 1).eq("is_delete", 0).orderByAsc("create_time"));
            debug("firstOrg", firstOrg);
            if (firstOrg == null) {
                logger.error("不存在任何业务单元数据，请先插入业务单元");
                return "failed";
            }

            /**
             * 设置主键
             */
            Map<String, String> codeID = new HashMap<>();
            Map<String, String> iDCode = new HashMap<>();
            organizations.forEach(org -> {
                String id = UuidUtils.createUUID();
                if (!codeID.keySet().contains(org.getDeptcode())) {
                    codeID.put(org.getDeptcode(), id);
                    iDCode.put(id, org.getDeptcode());
                } else {
                    logger.warn("接收到的组织数据存在相同的Code");
                    logger.warn(JSONObject.toJSONString(org));
                }
            });

            String userId = "webservice";//"webservice";

            /**
             * 转化类
             */
            Set<String> codes = new HashSet<>();
            organizations.forEach(org -> {
                debug("org", org);
                if (!codes.contains(org.getDeptcode())) {
                    OrgDepartmentDO dep = new OrgDepartmentDO();
                    String id = codeID.get(org.getDeptcode());
                    codes.add(org.getDeptcode());
                    dep.setId(id);
                    if (StringUtils.isNotBlank(org.getParentdept())) {
                        if (StringUtils.isNotBlank(codeID.get(org.getParentdept()))) {
                            dep.setParentId(codeID.get(org.getParentdept()));
                        } else {
                            OrgDepartmentDO parentDep = orgDepartmentService.getOne(new QueryWrapper<OrgDepartmentDO>()
                                    .eq("code", org.getParentdept()).eq("is_delete", 0));
                            if (parentDep != null) {
                                dep.setParentId(parentDep.getId());
                            } else {
                                dep.setParentId(null);
                            }
                        }
                    } else {
                        dep.setParentId("0");
                    }
                    if (StringUtils.isNotBlank(dep.getParentId())) {
                        dep.setGroupId(firstOrg.getGroupId());
                        dep.setOrganizationId(firstOrg.getId());
                        dep.setParentName(org.getParentdeptDeptfullname());
                        dep.setCode(org.getDeptcode());
                        dep.setName(org.getDeptfullname());
                        dep.setRemark(org.getOrgdescription());
                        if (StringUtils.isNotBlank(org.getEstablishdate())) {
                            try {
                                DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                                LocalDate dt = LocalDate.parse(org.getEstablishdate(), df);
                                dep.setCreateTime(dt.atStartOfDay());
                            } catch (Exception e) {
                                e.printStackTrace();
                                dep.setCreateTime(LocalDateTime.now());
                            }
                        } else {
                            dep.setCreateTime(LocalDateTime.now());
                        }
                        dep.setUpdateTime(LocalDateTime.now());
                        dep.setCreateUser(userId);
                        deps.add(dep);
                    } else {
                        logger.warn("保存部门，此部门的父节点不存在，部门数据：" + JSONObject.toJSONString(org));
                    }
                }
            });

            /**
             * 列表排序
             * 先插入父部门
             */
            List<OrgDepartmentDO> depss = new ArrayList<>();

            debug("deps", deps);
            /**
             * 转化成树结构
             */
            List<OrgDepartmentDO> treeDeps = TreeUtils.makeTree(deps, OrgDepartmentDO.class);
            sortDep(treeDeps, depss);
            Map<String, String> idVersionId = new HashMap<>();

            List<OrgDepartmentVDO> depVs = new ArrayList<>();
            debug("treeDeps", treeDeps);
            debug("depss", depss);

            /**
             * 插入固化表
             */
            depss.forEach(dep -> {
                debug("dep", dep);
                OrgDepartmentVDO depV = JsonUtils.castObjectForSetIdNull(dep, OrgDepartmentVDO.class);
                depV.setDepartmentId(dep.getId());
                String versionId = UuidUtils.createUUID();
                depV.setId(versionId);
                depV.setLastVersion(1);
                idVersionId.put(dep.getId(), versionId);
                depVs.add(depV);
                debug("dep", dep);
                debug("depV", depV);
            });
            if (!orgDepartmentVService.saveBatch(depVs)) {
                throw new RException(InternationUtils.getInternationalMsg("SAVE_FAILED"));
            }
            depss.forEach(dep -> {
                dep.setVersionId(idVersionId.get(dep.getId()));
            });
            /**
             * 插入部门表
             */
            logger.info("保存部门：" + JSONObject.toJSONString(depss));
            if (!orgDepartmentService.saveBatch(depss)) {
                throw new RException(InternationUtils.getInternationalMsg("SAVE_FAILED"));
            }

            /**
             * 插入业务单元  固化版本表
             */
            List<OrgOrganizationVDO> orgVs = new ArrayList<>();
            List<OrgOrganizationVTypeDO> orgVTypes = new ArrayList<>();
            depVs.forEach(depV -> {
                OrgOrganizationVDO orgV = JsonUtils.castObject(depV, OrgOrganizationVDO.class);
                OrgOrganizationVTypeDO orgVType = new OrgOrganizationVTypeDO();
                orgVType.setDepartment(1);
                orgVType.setId(depV.getId());
                orgV.setLastVersion(1);
                orgV.setBusinessUnit(2);
                orgV.setOrganizationId(depV.getDepartmentId());
                orgV.setOrganizationCode(firstOrg.getOrganizationCode());
                orgVTypes.add(orgVType);
                if (Objects.equals("0", orgV.getParentId())) {
                    orgV.setParentId(firstOrg.getId());
                }
                orgVs.add(orgV);
            });
            if (!orgOrganizationVService.saveBatch(orgVs)) {
                throw new RException(InternationUtils.getInternationalMsg("SAVE_FAILED"));
            }
            if (!orgOrganizationVTypeService.saveBatch(orgVTypes)) {
                throw new RException(InternationUtils.getInternationalMsg("SAVE_FAILED"));
            }

            /**
             * 插入业务单元表
             */
            List<OrgOrganizationDO> orgs = new ArrayList<>();
            List<OrgOrganizationTypeDO> orgTypes = new ArrayList<>();
            String rootPath = firstOrg.getSortPath();
            depss.forEach(dep -> {
                OrgOrganizationDO org = JsonUtils.castObject(dep, OrgOrganizationDO.class);
                OrgOrganizationTypeDO orgType = new OrgOrganizationTypeDO();
                orgType.setDepartment(1);
                orgType.setId(dep.getId());
                org.setBusinessUnit(2);
                orgTypes.add(orgType);
                org.setOrganizationCode(firstOrg.getOrganizationCode());
                org.setOrganizationCode(firstOrg.getOrganizationCode());
                org.setSortPath(rootPath + org.getSortPath());
                if (Objects.equals("0", org.getParentId())) {
                    org.setParentId(firstOrg.getId());
                }
                orgs.add(org);
            });
            if (!orgOrganizationService.saveBatch(orgs)) {
                throw new RException(InternationUtils.getInternationalMsg("SAVE_FAILED"));
            }
            if (!orgOrganizationTypeService.saveBatch(orgTypes)) {
                throw new RException(InternationUtils.getInternationalMsg("SAVE_FAILED"));
            }
//            if (!sysAuditLogService.saveLog("webserive保存部门:" + JSONObject.toJSONString(depss.stream().map
//                            (OrgDepartmentDO::getName).collect(Collectors.toList())), firstOrg.getGroupId(),
//                    null)) {
//                throw new RException(InternationUtils.getInternationalMsg("SAVE_FAILED"));
//            }
            return "success";
        }
        return "failed";
    }

    /**
     * 保存主数据人员
     *
     * @param employees
     * @return
     * @author yansiyang
     * @date 2019/8/7 8:32
     */
    public String saveEmployee(List<Employee> employees) {
        List<OrgDepartmentDO> deps;
        List<String> deptCodes = employees.stream().map(Employee::getDepartment).collect(Collectors.toList());
        debug("deptCodes", deptCodes);
        if (!org.apache.commons.collections.CollectionUtils.isNotEmpty(deptCodes)) {
            logger.warn("保存主数据人员：主数据推过来的人员中的部门code列表为空");
            return "failed";
        }
        deps = orgDepartmentService.list(new QueryWrapper<OrgDepartmentDO>().eq("is_delete", 0).in("code", deptCodes));
        debug("deps", deps);
        if (!org.apache.commons.collections.CollectionUtils.isNotEmpty(deps)) {
            logger.warn("保存主数据人员：主数据推过来的人员的所有部门code，无法从数据库找到数据，需要先推送部门数据，code:" + JSONObject.toJSONString(deptCodes));
            return "failed";
        }
        Map<String, OrgDepartmentDO> codeDep = new HashMap<>();
        deps.forEach(dep -> {
            codeDep.put(dep.getCode(), dep);
        });

        List<BdPersonDocDO> persons = new ArrayList<>();
        List<BdPersonJobDO> jobs = new ArrayList<>();

        List<SysUserDO> users = new ArrayList<>();
        String userId = "webservice";//"webservice";
        Map<String, Integer> maxSortIndexMap = new HashMap<>();
        int maxSortIndex = 10;
        try {
            maxSortIndex = dbHelperMapper.getCount("select max(sort_index) from bd_person_doc where is_delete=0") + 10;
        } catch (Exception e) {

        }
        maxSortIndexMap.put("maxSortIndex", maxSortIndex);

        employees.forEach(employee -> {
            debug("employee", employee);
            BdPersonDocDO person = new BdPersonDocDO();
            OrgDepartmentDO dep = codeDep.get(employee.getDepartment());
            if (dep != null) {
                person.setGroupId(dep.getGroupId());
                person.setOrganizationId(dep.getOrganizationId());

                String id = UuidUtils.createUUID();
                person.setId(id);
                person.setSortIndex(0);
                // 排序号
                try {
                    person.setSortIndex(Integer.parseInt(employee.getCode()));
                } catch (Exception e) {
                }

                if (person.getSortIndex() == 0) {
                    person.setSortIndex(maxSortIndexMap.get("maxSortIndex"));
                    maxSortIndexMap.put("maxSortIndex", person.getSortIndex() + 10);
                }
                person.setPinYin(employee.getPinyin());
                person.setCode(employee.getCode());
                person.setName(employee.getName());
                person.setCellPhone(employee.getMobilePhone());
                person.setPersonIdTypeId("card");
                person.setIdNumber(employee.getIdCardNo());
                if (StringUtils.isNotBlank(employee.getBirthDate())) {
                    DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                    LocalDate dt = LocalDate.parse(employee.getBirthDate(), df);
                    person.setBirthDate(dt);
                }
                if (StringUtils.isNotBlank(employee.getEnterTheDate())) {
                    DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                    LocalDate dt = LocalDate.parse(employee.getEnterTheDate(), df);
                    person.setJoinWorkDate(dt);
                }
                person.setGender(employee.getSex() - 1);
                person.setCreateUser(userId);
                person.setCreateTime(LocalDateTime.now());
                person.setUpdateTime(LocalDateTime.now());
                persons.add(person);

                BdPersonJobDO job = new BdPersonJobDO();
                job.setPersonCode(person.getCode());
                job.setMainJob(1);
                job.setSortIndex(1);
                job.setDepartmentId(dep.getId());
                job.setPersonDocId(id);
                job.setJobName(employee.getTitleName());
                job.setGroupId(dep.getGroupId());
                job.setOrganizationId(dep.getOrganizationId());
                jobs.add(job);


                /**
                 * 生成用户
                 */
                SysUserDO user = new SysUserDO();
                // 普通用户
                user.setUserType(3);
                user.setSortIndex(person.getSortIndex());
                user.setUserName(person.getCode());
                user.setRealName(person.getName());
                user.setPinyin(person.getPinYin());
                user.setPersonDocId(person.getId());
                user.setPhoneNumber(person.getCellPhone());
                user.setGroupId(person.getGroupId());
                user.setOrganizationId(person.getOrganizationId());
                user.setStaffNo(person.getCode());
                try {
                    int levelT = Integer.parseInt(employee.getStaffRank());
                    /**
                     * 主数据  01 非密 02 一般  03 重要  04 核心  05 内部
                     *  平台    5       3        2        1       4
                     */
                    if (levelT == 5) {
                        levelT = 4;
                    } else if (levelT > 1) {
                        levelT = 6 - levelT - 1;
                    } else {
                        levelT = 6 - levelT;
                    }
                    user.setSecretLevel(levelT);
                } catch (Exception e) {
                    user.setSecretLevel(5);
                }
                users.add(user);
            } else {
                logger.warn("保存用户,此人对应的部门数据不存在，人员数据：" + JSONObject.toJSONString(employee));
            }
        });

        if (!CollectionUtils.isEmpty(persons)) {
            logger.info("保存人员，数量：" + persons.size());
            debug("persons", persons);
            if (persons.size() > largeNum) {
                int count = persons.size() / largeNum;
                for (int i = 0; i <= count; i++)
                    if (i == count) {
                        if (largeNum * i < persons.size()) {
                            if (!bdPersonDocService.saveBatch(persons.subList(largeNum * i, persons.size())
                            )) {
                                throw new RException(InternationUtils.getInternationalMsg("UPDATE_FAILED"));
                            }
                        }
                    } else {
                        if (!bdPersonDocService.saveBatch(persons.subList(largeNum * i, largeNum * (i + 1))
                        )) {
                            throw new RException(InternationUtils.getInternationalMsg("UPDATE_FAILED"));
                        }
                    }
            } else {
                if (!bdPersonDocService.saveBatch(persons)) {
                    throw new RException(InternationUtils.getInternationalMsg("UPDATE_FAILED"));
                }
            }
        }

        if (!CollectionUtils.isEmpty(jobs)) {
            logger.info("保存职务，数量：" + jobs.size());
            debug("jobs", jobs);
            if (jobs.size() > largeNum) {
                int count = jobs.size() / largeNum;
                for (int i = 0; i <= count; i++)
                    if (i == count) {
                        if (largeNum * i < jobs.size()) {
                            if (!bdPersonJobService.saveBatch(jobs.subList(largeNum * i, jobs.size())
                            )) {
                                throw new RException(InternationUtils.getInternationalMsg("UPDATE_FAILED"));
                            }
                        }
                    } else {
                        if (!bdPersonJobService.saveBatch(jobs.subList(largeNum * i, largeNum * (i + 1))
                        )) {
                            throw new RException(InternationUtils.getInternationalMsg("UPDATE_FAILED"));
                        }
                    }
            } else {
                if (!bdPersonJobService.saveBatch(jobs)) {
                    throw new RException(InternationUtils.getInternationalMsg("UPDATE_FAILED"));
                }
            }

        }

        debug("users", users);
        users.forEach(user -> {
            debug("user", user);
            sysUserService.saveUser(user);
        });

        return "success";
    }

    /**
     * 更新人员
     *
     * @param employees
     * @return
     * @author yansiyang
     * @date 2019/8/8 14:47
     */
    public boolean updateEmployee(List<Employee> employees) {
        if (employees != null && employees.size() > 0) {
            List<String> codes = employees.stream().map(Employee::getCode).collect(Collectors.toList());
            debug("codes", codes);
            if (!org.apache.commons.collections.CollectionUtils.isNotEmpty(codes)) {
                logger.error("更新用户，人员编码列表为空集合");
                return false;
            }
            List<BdPersonDocDO> personss = bdPersonDocService.list(new QueryWrapper<BdPersonDocDO>().in("code",
                    codes).eq("is_delete", 0));
            debug("personss", personss);
            Map<String, BdPersonDocDO> codePerson = new HashMap<>();
            personss.forEach(person -> {
                codePerson.put(person.getCode(), person);
            });

            Map<String, BdPersonJobDO> personIdAndJobs= new HashMap<>();
            if (!CollectionUtils.isEmpty(personss)) {
                List<BdPersonJobDO> jobs = bdPersonJobService.list(new QueryWrapper<BdPersonJobDO>()
                        .eq("is_main_job", 1)
                        .in("person_doc_id",
                                personss.stream().map(BdPersonDocDO::getId).collect(Collectors.toList())));
                debug("jobs", jobs);
                jobs.forEach(job -> {
                    personIdAndJobs.put(job.getPersonDocId(), job);
                });
            }

            debug("codePerson", codePerson);
            List<String> deptCodes = employees.stream().map(Employee::getDepartment).collect(Collectors.toList());
            if (!org.apache.commons.collections.CollectionUtils.isNotEmpty(deptCodes)) {
                logger.warn("更新主数据人员：主数据推过来的人员中的部门code列表为空");
                return false;
            }
            debug("deptCodes", deptCodes);
            List<OrgDepartmentDO> deps = orgDepartmentService.list(new QueryWrapper<OrgDepartmentDO>().eq
                    ("is_delete", 0).in("code", deptCodes));
            debug("deps", deps);
            if (!org.apache.commons.collections.CollectionUtils.isNotEmpty(deps)) {
                logger.warn("更新主数据人员：主数据推过来的人员的所有部门code，无法从数据库找到数据，需要先推送部门数据，code:" + JSONObject.toJSONString
                        (deptCodes));
                return false;
            }
            Map<String, OrgDepartmentDO> codeDep = new HashMap<>();
            deps.forEach(dep -> {
                codeDep.put(dep.getCode(), dep);
            });

            List<BdPersonDocDO> persons = new ArrayList<>();
            List<BdPersonDocDO> addPersons = new ArrayList<>();
            List<BdPersonJobDO> jobs = new ArrayList<>();
            // 修改用户
            List<SysUserDO> users = new ArrayList<>();
            List<SysUserDO> newUsers = new ArrayList<>();
            if (org.apache.commons.collections.CollectionUtils.isNotEmpty(personss)) {
                Set<String> personDocIds = new HashSet<>();
                personss.stream().map(BdPersonDocDO::getId).collect(Collectors.toList()).forEach(id -> {
                    if (StringUtils.isNotBlank(id)) {
                        personDocIds.add(id);
                    }
                });
                if (personDocIds.size() > 0) {
                    users = sysUserService.list(new QueryWrapper<SysUserDO>().eq("is_delete", 0)
                            .in("person_doc_id", personDocIds));
                }
            }
            debug("users", users);
            Map<String, SysUserDO> docIdAndUsers = new HashMap<>();
            users.forEach(user -> {
                docIdAndUsers.put(user.getPersonDocId(), user);
            });
            String userId = "webservice";//"webservice";

            employees.forEach(employee -> {
                debug("employee", employee);
                boolean add = true;
                BdPersonDocDO person = codePerson.get(employee.getCode());
                debug("person", person);
                OrgDepartmentDO dep = codeDep.get(employee.getDepartment());
                debug("dep", dep);
                if (dep != null) {
                    person.setGroupId(dep.getGroupId());
                    person.setOrganizationId(dep.getOrganizationId());

                    String id = UuidUtils.createUUID();
                    if (codePerson.get(employee.getCode()) != null) {
                        add = false;
                        id = codePerson.get(employee.getCode()).getId();
                    }
                    // 重新设置主键 2020-3-4
                    person.setId(id);
                    person.setPinYin(employee.getPinyin());
                    person.setName(employee.getName());
                    person.setCellPhone(employee.getMobilePhone());
                    person.setIdNumber(employee.getIdCardNo());
                    if (StringUtils.isNotBlank(employee.getBirthDate())) {
                        DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                        LocalDate dt = LocalDate.parse(employee.getBirthDate(), df);
                        person.setBirthDate(dt);
                    }
                    if (StringUtils.isNotBlank(employee.getEnterTheDate())) {
                        DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                        LocalDate dt = LocalDate.parse(employee.getEnterTheDate(), df);
                        person.setJoinWorkDate(dt);
                    }
                    person.setGender(employee.getSex() - 1);
                    person.setUpdateTime(LocalDateTime.now());
                    if (add) {
                        person.setCreateUser(userId);
                        person.setCreateTime(LocalDateTime.now());
                        addPersons.add(person);
                    } else {
                        persons.add(person);
                    }


                    BdPersonJobDO job = new BdPersonJobDO();
                    // 更新主职信息
                    if (personIdAndJobs.containsKey(id)) {
                        job = personIdAndJobs.get(id);
                    }
                    job.setPersonCode(person.getCode());
                    job.setMainJob(1);
                    job.setSortIndex(1);
                    job.setDepartmentId(dep.getId());
                    job.setPersonDocId(id);
                    job.setJobName(employee.getTitleName());
                    job.setGroupId(dep.getGroupId());
                    job.setOrganizationId(dep.getOrganizationId());
                    jobs.add(job);

                    /**
                     * 生成用户
                     */
                    SysUserDO user = docIdAndUsers.containsKey(id) ? docIdAndUsers.get(id) : new SysUserDO();
                    debug("user", user);
                    // 普通用户
                    user.setUserType(3);
                    user.setUserName(person.getCode());
                    user.setRealName(person.getName());
                    user.setPinyin(person.getPinYin());
                    user.setPersonDocId(person.getId());
                    user.setPhoneNumber(person.getCellPhone());
                    user.setGroupId(person.getGroupId());
                    user.setOrganizationId(person.getOrganizationId());
                    user.setStaffNo(person.getCode());
                    try {
                        int levelT = Integer.parseInt(employee.getStaffRank());
                        /**
                         * 主数据  01 非密 02 一般  03 重要  04 核心  05 内部
                         *  平台    5       3        2        1       4
                         */
                        if (levelT == 5) {
                            levelT = 4;
                        } else if (levelT > 1) {
                            levelT = 6 - levelT - 1;
                        } else {
                            levelT = 6 - levelT;
                        }
                        user.setSecretLevel(levelT);
                    } catch (Exception e) {
                        user.setSecretLevel(5);
                    }
                    newUsers.add(user);

                } else {
                    logger.info("更新用户," + person.getName() + ",code:" + person.getCode(), ",对应的部门尚未创建:" + employee
                            .getDepartment());
                }
            });

            if (org.apache.commons.collections.CollectionUtils.isNotEmpty(addPersons)) {
                debug("addPersons", addPersons);
                logger.info("更新人员时保存人员，数量：" + addPersons.size());
                if (addPersons.size() > largeNum) {
                    int count = addPersons.size() / largeNum;
                    for (int i = 0; i <= count; i++)
                        if (i == count) {
                            if (largeNum * i < addPersons.size()) {
                                if (!bdPersonDocService.saveBatch(addPersons.subList(largeNum * i, addPersons.size())
                                )) {
                                    throw new RException(InternationUtils.getInternationalMsg("UPDATE_FAILED"));
                                }
                            }
                        } else {
                            if (!bdPersonDocService.saveBatch(addPersons.subList(largeNum * i, largeNum * (i + 1))
                            )) {
                                throw new RException(InternationUtils.getInternationalMsg("UPDATE_FAILED"));
                            }
                        }
                } else {
                    if (!bdPersonDocService.saveBatch(addPersons)) {
                        throw new RException(InternationUtils.getInternationalMsg("UPDATE_FAILED"));
                    }
                }
            }
            if (org.apache.commons.collections.CollectionUtils.isNotEmpty(persons)) {
                debug("persons", persons);
                logger.info("更新人员，数量：" + persons.size());
                if (persons.size() > largeNum) {
                    int count = persons.size() / largeNum;
                    for (int i = 0; i <= count; i++)
                        if (i == count) {
                            if (largeNum * i < persons.size()) {
                                if (!bdPersonDocService.updateBatchById(persons.subList(largeNum * i, persons.size())
                                )) {
                                    throw new RException(InternationUtils.getInternationalMsg("UPDATE_FAILED"));
                                }
                            }
                        } else {
                            if (!bdPersonDocService.updateBatchById(persons.subList(largeNum * i, largeNum * (i +
                                    1))
                            )) {
                                throw new RException(InternationUtils.getInternationalMsg("UPDATE_FAILED"));
                            }
                        }
                } else {
                    if (!bdPersonDocService.updateBatchById(persons)) {
                        throw new RException(InternationUtils.getInternationalMsg("UPDATE_FAILED"));
                    }
                }
            }

            List<String> ids = persons.stream().map(BdPersonDocDO::getId).collect(Collectors.toList());

            debug("ids", ids);
            if (!bdPersonJobService.remove(new QueryWrapper<BdPersonJobDO>().in("person_doc_id", ids))) {
                throw new RException(InternationUtils.getInternationalMsg("SAVE_FAILED"));
            }

            if (org.apache.commons.collections.CollectionUtils.isNotEmpty(jobs)) {
                logger.info("更新人员时保存职务，数量：" + jobs.size());
                debug("jobs", jobs);
                if (jobs.size() > largeNum) {
                    int count = jobs.size() / largeNum;
                    for (int i = 0; i <= count; i++)
                        if (i == count) {
                            if (largeNum * i < jobs.size()) {
                                if (!bdPersonJobService.saveBatch(jobs.subList(largeNum * i, jobs.size())
                                )) {
                                    throw new RException(InternationUtils.getInternationalMsg("UPDATE_FAILED"));
                                }
                            }
                        } else {
                            if (!bdPersonJobService.saveBatch(jobs.subList(largeNum * i, largeNum * (i + 1))
                            )) {
                                throw new RException(InternationUtils.getInternationalMsg("UPDATE_FAILED"));
                            }
                        }
                } else {
                    if (!bdPersonJobService.saveBatch(jobs)) {
                        throw new RException(InternationUtils.getInternationalMsg("UPDATE_FAILED"));
                    }
                }
            }
            debug("newUsers", newUsers);
            newUsers.stream().forEach(user -> {
                if (StringUtils.isBlank(user.getId())) {
                    sysUserService.saveUser(user);
                } else {
                    sysUserService.updateById(user);
                }
            });
        }
        return true;
    }

    /**
     * 逻辑删除人员
     *
     * @param codes
     * @return
     * @author yansiyang
     * @date 2019/8/8 14:47
     */
    public boolean deleteEmployee(List<String> codes) {
        if (codes.size() > 0) {

//            if (bdPersonDocService.update(new BdPersonDocDO(), new UpdateWrapper<BdPersonDocDO>().in("code", codes)
//                    .set("is_delete", 1))) {
            List<BdPersonDocDO> persons = bdPersonDocService.list(new QueryWrapper<BdPersonDocDO>().in("code",
                    codes).select("id").eq("is_delete", 0));

            List<String> personIDs = persons.stream().map(BdPersonDocDO::getId).collect(Collectors.toList());

            if (!CollectionUtils.isEmpty(personIDs)) {

                List<SysUserDO> users = sysUserService.list(new QueryWrapper<SysUserDO>().in("person_doc_id",
                        personIDs).select("id"));
                List<String> userIDs = users.stream().map(SysUserDO::getId).collect(Collectors.toList());
                bdPersonDocService.delete(personIDs);
                if (!CollectionUtils.isEmpty(userIDs)) {
                    if (sysUserService.removeUsers(userIDs)) {
                        return true;
                    }
                } else {
                    return true;
                }
            } else {
                return true;
            }
        }
        throw new RException(InternationUtils.getInternationalMsg("OPERATE_FAILED"));
    }

    /**
     * 部门排序
     *
     * @param treeDeps
     * @param depss
     * @return
     * @author yansiyang
     * @date 2019/8/7 14:32
     */
    private void sortDep(List<OrgDepartmentDO> treeDeps, List<OrgDepartmentDO> depss) {
        if (treeDeps.size() > 0) {
            for (int i = 0; i < treeDeps.size(); i++) {
                OrgDepartmentDO dep = treeDeps.get(i);
                depss.add(dep);
                if (Objects.equals(dep.getParentId(), "0")) {
                    int sortIndex = getSortIndexForDep(dep.getParentId(), dep.getGroupId(), dep.getOrganizationId()) + 10 + i * 10;
                    dep.setSortIndex(sortIndex);
                    dep.setSortPath(SortPathUtils.getSortPath("", sortIndex));
                } else {
                    if (depss.stream().anyMatch(d -> Objects.equals(d.getId(), dep.getParentId()))) {
                        dep.setSortIndex(i * 10 + 10);
                        dep.setSortPath(SortPathUtils.getSortPath(depss.stream().filter(d -> Objects.equals(d.getId(), dep.getParentId()))
                                .findFirst().get().getSortPath(), i * 10 + 10));
                    } else {
                        int sortIndex = getSortIndexForDep(dep.getParentId(), dep.getGroupId(), dep.getOrganizationId()) + 10 + i * 10;
                        OrgDepartmentDO parentDep = orgDepartmentService.getById(dep.getParentId());
                        dep.setSortIndex(sortIndex);
                        dep.setSortPath(SortPathUtils.getSortPath(parentDep.getSortPath(), sortIndex));
                    }
                }
                if (dep.getChildren() != null && dep.getChildren().size() > 0) {
                    sortDep(dep.getChildren(), depss);
                }
            }

        }
    }
}

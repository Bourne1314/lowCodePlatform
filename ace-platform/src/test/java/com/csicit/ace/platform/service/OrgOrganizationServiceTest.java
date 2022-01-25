package com.csicit.ace.platform.service;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.csicit.ace.common.pojo.domain.OrgOrganizationDO;
import com.csicit.ace.common.pojo.domain.OrgOrganizationTypeDO;
import com.csicit.ace.common.pojo.domain.OrgOrganizationVDO;
import com.csicit.ace.common.pojo.domain.OrgOrganizationVTypeDO;
import com.csicit.ace.common.utils.server.R;
import com.csicit.ace.platform.BaseTest;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

public class OrgOrganizationServiceTest extends BaseTest {

    @Test
    public void saveOrgsTest1() {
        JSONObject org = new JSONObject();
        org.put("orgType", "corporation");
        org.put("groupId", "9e92cb17c7bf4af9b6072520d42ea103");
        org.put("parentId", "0");
        org.put("name", "单元测试业务单元1");
        org.put("shortName", "测试单元1");
        org.put("address", "连云港市");
        org.put("countryZone", "中国江苏");
        org.put("organizationCode", "1231");
        org.put("code", "456");
        org.put("sortIndex", 223);
        org.put("businessUnit", 1);
        boolean flg = orgOrganizationService.saveOrgs(org);
        System.out.println("结果为：" + flg);
        assertThat(flg, is(true));
        assertThat(orgOrganizationService.count(new QueryWrapper<OrgOrganizationDO>()
                .eq("name", "单元测试业务单元1")), is(1));
        System.out.println("组织数据：" + orgOrganizationService.getOne(new QueryWrapper<OrgOrganizationDO>()
                .eq("name", "单元测试业务单元1")));
        assertThat(orgOrganizationVService.count(new QueryWrapper<OrgOrganizationVDO>()
                .eq("name", "单元测试业务单元1")), is(1));
        System.out.println("组织版本数据：" + orgOrganizationVService.getOne(new QueryWrapper<OrgOrganizationVDO>()
                .eq("name", "单元测试业务单元1")));
        String id = orgOrganizationService.getOne(new QueryWrapper<OrgOrganizationDO>()
                .eq("name", "单元测试业务单元1")).getId();
        String idv = orgOrganizationVService.getOne(new QueryWrapper<OrgOrganizationVDO>()
                .eq("name", "单元测试业务单元1")).getId();
        assertThat(orgOrganizationTypeService.count(new QueryWrapper<OrgOrganizationTypeDO>()
                .eq("id", id)), is(1));
        assertThat(orgOrganizationVTypeService.count(new QueryWrapper<OrgOrganizationVTypeDO>()
                .eq("id", idv)), is(1));
        System.out.println("组织职能数据：" + orgOrganizationTypeService.getById(id));
        System.out.println("组织职能版本数据：" + orgOrganizationVTypeService.getById(idv));
    }

    @Test
    public void saveOrgsTest2() {
        JSONObject org = new JSONObject();
        List<String> types = new ArrayList<>(16);
        types.add("corporation");
        types.add("purchase");
        org.put("orgType", types);
        org.put("groupId", "9e92cb17c7bf4af9b6072520d42ea103");
        org.put("parentId", "0");
        org.put("name", "单元测试业务单元1");
        org.put("shortName", "测试单元1");
        org.put("address", "连云港市");
        org.put("countryZone", "中国江苏");
        org.put("organizationCode", "1231");
        org.put("code", "456");
        org.put("sortIndex", 223);
        org.put("businessUnit", 1);
        boolean flg = orgOrganizationService.saveOrgs(org);
        System.out.println("结果为：" + flg);
        assertThat(flg, is(true));
        assertThat(orgOrganizationService.count(new QueryWrapper<OrgOrganizationDO>()
                .eq("name", "单元测试业务单元1")), is(1));
        System.out.println("组织数据：" + orgOrganizationService.getOne(new QueryWrapper<OrgOrganizationDO>()
                .eq("name", "单元测试业务单元1")));
        assertThat(orgOrganizationVService.count(new QueryWrapper<OrgOrganizationVDO>()
                .eq("name", "单元测试业务单元1")), is(1));
        System.out.println("组织版本数据：" + orgOrganizationVService.getOne(new QueryWrapper<OrgOrganizationVDO>()
                .eq("name", "单元测试业务单元1")));
        String id = orgOrganizationService.getOne(new QueryWrapper<OrgOrganizationDO>()
                .eq("name", "单元测试业务单元1")).getId();
        String idv = orgOrganizationVService.getOne(new QueryWrapper<OrgOrganizationVDO>()
                .eq("name", "单元测试业务单元1")).getId();
        assertThat(orgOrganizationTypeService.count(new QueryWrapper<OrgOrganizationTypeDO>()
                .eq("id", id)), is(1));
        System.out.println("组织职能数据：" + orgOrganizationTypeService.getById(id));
        assertThat(orgOrganizationVTypeService.count(new QueryWrapper<OrgOrganizationVTypeDO>()
                .eq("id", idv)), is(1));
        System.out.println("组织职能版本数据：" + orgOrganizationVTypeService.getById(idv));
    }

//    @Test
//    public void saveOrg() {
//    }

//    @Test
//    public void updateOrg() {
//    }

//    @Test
//    public void updateSonSortPath() {
//    }

    @Test
    public void getSonOrgsByOrgIdTest() {
        System.out.println("当前组织：" + orgOrganizationService.getById("9e92cb17c7bf4af9b6072520d42ea103"));
        List<OrgOrganizationDO> orgs = orgOrganizationService.getSonOrgsByOrgId
                ("9e92cb17c7bf4af9b6072520d42ea103", 1, false);
        assertThat(orgs.size(), is(5));
        System.out.println("仅业务单元数据：" + orgs.toString());
        List<OrgOrganizationDO> deps = orgOrganizationService.getSonOrgsByOrgId
                ("9e92cb17c7bf4af9b6072520d42ea103", 2, false);
        assertThat(deps.size(), is(5));
        System.out.println("仅部门：" + deps.toString());
        List<OrgOrganizationDO> depAndOrgs = orgOrganizationService.getSonOrgsByOrgId
                ("9e92cb17c7bf4af9b6072520d42ea103", 3, false);
        assertThat(depAndOrgs.size(), is(10));
        System.out.println("业务单元和部门：" + depAndOrgs.toString());
        List<OrgOrganizationDO> groups = orgOrganizationService.getSonOrgsByOrgId
                ("9e92cb17c7bf4af9b6072520d42ea103", 4, true);
        assertThat(groups.size(), is(5));
        System.out.println("包括自己所在集团：" + groups.toString());
        List<OrgOrganizationDO> alls = orgOrganizationService.getSonOrgsByOrgId
                ("9e92cb17c7bf4af9b6072520d42ea103", 5, true);
        assertThat(alls.size(), is(15));
        System.out.println("包括自己所有：" + alls.toString());


    }

//    @Test
//    public void getSonIdsOrgsByOrgId() {
//    }

    @Test
    public void versionOrgTest() {
        Map<String, String> map = new HashMap<>();
        map.put("versionName", "测试单元版本1");
        map.put("versionNo", "1.0.0");
        map.put("organizationId", "7956981e38f44d0b8b57302307beacb8");
        System.out.println("----固化版本前----");
        System.out.println("业务单元数据：" + orgOrganizationService.getById("7956981e38f44d0b8b57302307beacb8"));
        String oldId = orgOrganizationService.getById
                ("7956981e38f44d0b8b57302307beacb8").getVersionId();
        System.out.println("业务单元版本数据：" + orgOrganizationVService.getById(oldId));
        boolean flg = orgOrganizationService.versionOrg(map);
        System.out.println("结果为：" + flg);
        assertThat(flg, is(true));
        System.out.println("----固化版本后----");
        System.out.println("业务单元数据：" + orgOrganizationService.getById("7956981e38f44d0b8b57302307beacb8"));
        System.out.println("旧的业务单元版本数据：" + orgOrganizationVService.getById(oldId));
        System.out.println("新的业务单元版本数据：" + orgOrganizationVService.getById(orgOrganizationService.getById
                ("7956981e38f44d0b8b57302307beacb8").getVersionId()));
        orgOrganizationVService.removeById(orgOrganizationService.getById
                ("7956981e38f44d0b8b57302307beacb8").getVersionId());
    }

    @Test
    public void deleteOrgTest() {
        List<String> ids = new ArrayList<>(16);
        ids.add("54d087404f454139a0b57b9437deb1f8");
        ids.add("7956981e38f44d0b8b57302307beacb8");
        System.out.println("删除前业务单元数据：" + orgOrganizationService.list(new QueryWrapper<OrgOrganizationDO>()
                .in("id", ids)).toString());
        Map<String, Object> map = new HashMap<>();
        map.put("ids", ids);
        R r = orgOrganizationService.deleteOrg(map);
        System.out.println(r.get("msg"));

        assertThat("删除成功!", is(r.get("msg")));
        assertThat(orgOrganizationService.getById("54d087404f454139a0b57b9437deb1f8").getName().substring(0, 3), is
                ("del"));
        assertThat(orgOrganizationService.getById("7956981e38f44d0b8b57302307beacb8").getName().substring(0, 3), is
                ("del"));
        System.out.println("删除前业务单元数据：" + orgOrganizationService.list(new QueryWrapper<OrgOrganizationDO>()
                .in("id", ids)).toString());
    }

//    @Test
//    public void removeOrgs() {
//    }

    @Test
    public void mvBusinessUnitTest1() {
        OrgOrganizationDO orgOrganizationDO = orgOrganizationService.getById("e48fd9d687f741e088e4a086fe8dbb77");
        System.out.println("变更前组织：" + orgOrganizationDO);
        System.out.println("变更前子组织：" + orgOrganizationService.list(new QueryWrapper<OrgOrganizationDO>()
                .eq("IS_BUSINESS_UNIT", 1).likeRight("sort_path", orgOrganizationDO.getSortPath())));
        boolean flg = orgOrganizationService.mvBusinessUnit(true, "e48fd9d687f741e088e4a086fe8dbb77",
                "0e4b26e45d0348088503efc8768afcdc", "0e4b26e45d0348088503efc8768afcdc");
        System.out.println("结果为：" + flg);
        assertThat(flg, is(true));
        OrgOrganizationDO neworgOrganizationDO = orgOrganizationService.getById("e48fd9d687f741e088e4a086fe8dbb77");
        assertThat(neworgOrganizationDO.getSortPath(), is("003519000010000010"));
        assertThat(orgOrganizationService.count(new QueryWrapper<OrgOrganizationDO>()
                .eq("IS_BUSINESS_UNIT", 1).likeRight("sort_path", neworgOrganizationDO.getSortPath())), is
                (3));
        System.out.println("变更后组织：" + neworgOrganizationDO);
        System.out.println("变更后子组织：" + orgOrganizationService.list(new QueryWrapper<OrgOrganizationDO>()
                .eq("IS_BUSINESS_UNIT", 1).likeRight("sort_path", neworgOrganizationDO.getSortPath())));
    }

    @Test
    public void mvBusinessUnitTest2() {
        OrgOrganizationDO orgOrganizationDO = orgOrganizationService.getById("e18d9ba2bfdb4d67ab99c642ed47655a");
        System.out.println("变更前组织：" + orgOrganizationDO);
        System.out.println("变更前子组织：" + orgOrganizationService.list(new QueryWrapper<OrgOrganizationDO>()
                .eq("IS_BUSINESS_UNIT", 1).likeRight("sort_path", orgOrganizationDO.getSortPath())));
        boolean flg = orgOrganizationService.mvBusinessUnit(false, "e18d9ba2bfdb4d67ab99c642ed47655a",
                "9e92cb17c7bf4af9b6072520d42ea103", "54d087404f454139a0b57b9437deb1f8");
        System.out.println("结果为：" + flg);
        assertThat(flg, is(true));
        OrgOrganizationDO neworgOrganizationDO = orgOrganizationService.getById("e18d9ba2bfdb4d67ab99c642ed47655a");
        assertThat(neworgOrganizationDO.getSortPath(), is("003519000200000020000010"));
        assertThat(orgOrganizationService.count(new QueryWrapper<OrgOrganizationDO>()
                .eq("IS_BUSINESS_UNIT", 1).likeRight("sort_path", neworgOrganizationDO.getSortPath())), is
                (2));
        System.out.println("变更后组织：" + neworgOrganizationDO);
        System.out.println("变更后子组织：" + orgOrganizationService.list(new QueryWrapper<OrgOrganizationDO>()
                .eq("IS_BUSINESS_UNIT", 1).likeRight("sort_path", neworgOrganizationDO.getSortPath())));
    }
}
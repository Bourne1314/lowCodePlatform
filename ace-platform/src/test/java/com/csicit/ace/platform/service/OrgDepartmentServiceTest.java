package com.csicit.ace.platform.service;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.csicit.ace.common.pojo.domain.OrgDepartmentDO;
import com.csicit.ace.common.pojo.domain.OrgOrganizationDO;
import com.csicit.ace.common.pojo.domain.OrgOrganizationVDO;
import com.csicit.ace.common.utils.server.R;
import com.csicit.ace.platform.BaseTest;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

public class OrgDepartmentServiceTest extends BaseTest {

    @Test
    public void mvDepTest1() {
        OrgDepartmentDO orgDepartmentDO = orgDepartmentService.getById("3373e43a15d74d97a4c9a2ebb4a5bb25");
        System.out.println("变更前部门：" + orgDepartmentDO);
        System.out.println("变更前子部门：" + orgDepartmentService.list(new QueryWrapper<OrgDepartmentDO>()
                .likeRight("sort_path", orgDepartmentDO.getSortPath())));
        OrgOrganizationDO orgOrganizationDO = orgOrganizationService.getById("3373e43a15d74d97a4c9a2ebb4a5bb25");
        System.out.println("变更前组织：" + orgOrganizationDO);
        System.out.println("变更前子组织：" + orgOrganizationService.list(new QueryWrapper<OrgOrganizationDO>()
                .eq("IS_BUSINESS_UNIT", 2).likeRight("sort_path", orgOrganizationDO.getSortPath())));
        boolean flg = orgDepartmentService.mvDep(true, "3373e43a15d74d97a4c9a2ebb4a5bb25",
                "e48fd9d687f741e088e4a086fe8dbb77", "0");
        System.out.println("结果为：" + flg);
        assertThat(flg, is(true));
        OrgDepartmentDO neworgDepartmentDO = orgDepartmentService.getById("3373e43a15d74d97a4c9a2ebb4a5bb25");
        assertThat(neworgDepartmentDO.getSortPath(), is("000010"));
        assertThat(neworgDepartmentDO.getOrganizationId(), is("e48fd9d687f741e088e4a086fe8dbb77"));
        System.out.println("变更后部门：" + neworgDepartmentDO);
        assertThat(orgDepartmentService.count(new QueryWrapper<OrgDepartmentDO>()
                .eq("organization_id", "e48fd9d687f741e088e4a086fe8dbb77")), is(3));
        System.out.println("变更后子部门：" + orgDepartmentService.list(new QueryWrapper<OrgDepartmentDO>().eq
                ("organization_id", "e48fd9d687f741e088e4a086fe8dbb77").likeRight("sort_path", neworgDepartmentDO
                .getSortPath())));
        OrgOrganizationDO neworgOrganizationDO = orgOrganizationService.getById("3373e43a15d74d97a4c9a2ebb4a5bb25");
        System.out.println("变更后组织：" + neworgOrganizationDO);
        assertThat(neworgOrganizationDO.getSortPath(), is("003519000200000010000010"));
        System.out.println("变更后子组织：" + orgOrganizationService.list(new QueryWrapper<OrgOrganizationDO>()
                .eq("IS_BUSINESS_UNIT", 2).likeRight("sort_path", neworgOrganizationDO.getSortPath())));
    }

    @Test
    public void mvDepTest2() {
        OrgDepartmentDO orgDepartmentDO = orgDepartmentService.getById("2eae92004a0148b39ab76d1e60012f4a");
        System.out.println("变更前部门：" + orgDepartmentDO);
        System.out.println("变更前子部门：" + orgDepartmentService.list(new QueryWrapper<OrgDepartmentDO>()
                .likeRight("sort_path", orgDepartmentDO.getSortPath())));
        OrgOrganizationDO orgOrganizationDO = orgOrganizationService.getById("2eae92004a0148b39ab76d1e60012f4a");
        System.out.println("变更前组织：" + orgOrganizationDO);
        System.out.println("变更前子组织：" + orgOrganizationService.list(new QueryWrapper<OrgOrganizationDO>()
                .eq("IS_BUSINESS_UNIT", 2).likeRight("sort_path", orgOrganizationDO.getSortPath())));
        boolean flg = orgDepartmentService.mvDep(false, "2eae92004a0148b39ab76d1e60012f4a",
                "b96daa08d59c467a9bd6cca17e46f53f", "fa08fccfe110469f87be52888b6159cd");
        System.out.println("结果为：" + flg);
        assertThat(flg, is(true));
        OrgDepartmentDO neworgDepartmentDO = orgDepartmentService.getById("2eae92004a0148b39ab76d1e60012f4a");
        assertThat(neworgDepartmentDO.getSortPath(), is("000011000020000010"));
        System.out.println("变更后部门：" + neworgDepartmentDO);
        assertThat(orgDepartmentService.count(new QueryWrapper<OrgDepartmentDO>()
                .eq("sort_path", "000011000020000010000010")), is(1));
        System.out.println("变更后子部门：" + orgDepartmentService.list(new QueryWrapper<OrgDepartmentDO>()
                .likeRight("sort_path", neworgDepartmentDO.getSortPath())));
        OrgOrganizationDO neworgOrganizationDO = orgOrganizationService.getById("2eae92004a0148b39ab76d1e60012f4a");
        assertThat(neworgOrganizationDO.getSortPath(), is("003519000200000011000020000010"));
        System.out.println("变更后组织：" + neworgOrganizationDO);
        assertThat(orgOrganizationService.count(new QueryWrapper<OrgOrganizationDO>()
                .eq("sort_path", "003519000200000011000020000010000010")), is(1));
        System.out.println("变更后子组织：" + orgOrganizationService.list(new QueryWrapper<OrgOrganizationDO>()
                .eq("IS_BUSINESS_UNIT", 2).likeRight("sort_path", neworgOrganizationDO.getSortPath())));
    }

    @Test
    public void saveDepTest() {
        OrgDepartmentDO dep = new OrgDepartmentDO();
        dep.setBeDeleted(0);
        dep.setParentId("0");
        dep.setName("单元测试部门");
        dep.setCode("dayuanceshibumen");
        dep.setSortIndex(678);
        dep.setGroupId("9e92cb17c7bf4af9b6072520d42ea103");
        dep.setOrganizationId("b96daa08d59c467a9bd6cca17e46f53f");
        boolean flg = orgDepartmentService.saveDep(dep);
        System.out.println("结果返回：" + flg);
        assertThat(flg, is(true));
        assertThat(orgDepartmentService.count(new QueryWrapper<OrgDepartmentDO>()
                .eq("name", "单元测试部门")), is(1));
        String id = orgDepartmentService.getOne(new QueryWrapper<OrgDepartmentDO>()
                .eq("name", "单元测试部门")).getId();
        System.out.println("部门表数据：" + orgDepartmentService.getOne(new QueryWrapper<OrgDepartmentDO>()
                .eq("name", "单元测试部门")).toString());
        assertThat(orgOrganizationService.count(new QueryWrapper<OrgOrganizationDO>()
                .eq("name", "单元测试部门")), is(1));
        System.out.println("组织表数据：" + orgOrganizationService.getOne(new QueryWrapper<OrgOrganizationDO>()
                .eq("name", "单元测试部门")).toString());
        assertThat(orgOrganizationVService.count(new QueryWrapper<OrgOrganizationVDO>()
                .eq("organization_id", id)), is(1));
        System.out.println("组织版本表数据：" + orgOrganizationVService.getOne(new QueryWrapper<OrgOrganizationVDO>()
                .eq("organization_id", id)).toString());


        OrgDepartmentDO orgDepartmentDOChild = new OrgDepartmentDO();
        orgDepartmentDOChild.setBeDeleted(0);
        orgDepartmentDOChild.setParentId(id);
        orgDepartmentDOChild.setName("单元测试部门下级");
        orgDepartmentDOChild.setCode("dayuanceshibumenxiaji");
        orgDepartmentDOChild.setSortIndex(222);
        orgDepartmentDOChild.setGroupId("9e92cb17c7bf4af9b6072520d42ea103");
        orgDepartmentDOChild.setOrganizationId("b96daa08d59c467a9bd6cca17e46f53f");
        boolean flgChild = orgDepartmentService.saveDep(orgDepartmentDOChild);
        System.out.println("结果返回：" + flgChild);
        assertThat(flgChild, is(true));
        assertThat(orgDepartmentService.count(new QueryWrapper<OrgDepartmentDO>()
                .eq("name", "单元测试部门下级")), is(1));
        String idChild = orgDepartmentService.getOne(new QueryWrapper<OrgDepartmentDO>()
                .eq("name", "单元测试部门下级")).getId();
        System.out.println("部门表数据：" + orgDepartmentService.getOne(new QueryWrapper<OrgDepartmentDO>()
                .eq("name", "单元测试部门下级")).toString());
        assertThat(orgOrganizationService.count(new QueryWrapper<OrgOrganizationDO>()
                .eq("name", "单元测试部门下级")), is(1));
        System.out.println("组织表数据：" + orgOrganizationService.getOne(new QueryWrapper<OrgOrganizationDO>()
                .eq("name", "单元测试部门下级")).toString());
        assertThat(orgOrganizationVService.count(new QueryWrapper<OrgOrganizationVDO>()
                .eq("organization_id", idChild)), is(1));
        System.out.println("组织版本表数据：" + orgOrganizationVService.getOne(new QueryWrapper<OrgOrganizationVDO>()
                .eq("organization_id", idChild)).toString());

    }

    @Test
    public void updateDepTest1() {
        OrgDepartmentDO orgDepartmentDO = orgDepartmentService.getById("8c22d74d433d4828bd59b05d7059a8b7");
        OrgOrganizationDO orgOrganizationDO = orgOrganizationService.getById("8c22d74d433d4828bd59b05d7059a8b7");
        System.out.println("变更前部门：" + orgDepartmentDO);
        System.out.println("变更前子部门：" + orgDepartmentService.list(new QueryWrapper<OrgDepartmentDO>()
                .likeRight("sort_path", orgDepartmentDO.getSortPath())).toString());
        System.out.println("变更前组织：" + orgOrganizationDO);
        System.out.println("变更前子组织：" + orgOrganizationService.list(new QueryWrapper<OrgOrganizationDO>()
                .eq("IS_BUSINESS_UNIT", 2).likeRight("sort_path", orgOrganizationDO.getSortPath())).toString());
        orgDepartmentDO.setName("修改后-测试部门1");
        orgDepartmentDO.setCode("xiugaihou12");
        orgDepartmentDO.setSortIndex(7766);
        boolean flg = orgDepartmentService.updateDep(orgDepartmentDO);
        System.out.println("结果为：" + flg);
        assertThat(flg, is(true));
        OrgDepartmentDO neworgDepartmentDO = orgDepartmentService.getById("8c22d74d433d4828bd59b05d7059a8b7");
        assertThat(neworgDepartmentDO.getName(), is("修改后-测试部门1"));
        assertThat(neworgDepartmentDO.getCode(), is("xiugaihou12"));
        assertThat(neworgDepartmentDO.getSortPath(), is("007766"));
        System.out.println("变更后部门:" + neworgDepartmentDO);
        OrgOrganizationDO neworgOrganizationDO = orgOrganizationService.getById("8c22d74d433d4828bd59b05d7059a8b7");
        assertThat(neworgOrganizationDO.getName(), is("修改后-测试部门1"));
        assertThat(neworgOrganizationDO.getCode(), is("xiugaihou12"));
        assertThat(neworgOrganizationDO.getSortPath(), is("003519000200007766"));
        assertThat(orgOrganizationService.count(new QueryWrapper<OrgOrganizationDO>()
                .eq("IS_BUSINESS_UNIT", 2).likeRight("sort_path", "003519000200007766")), is(5));
        System.out.println("变更后组织：" + neworgOrganizationDO);
        System.out.println("变更后子组织：" + orgOrganizationService.list(new QueryWrapper<OrgOrganizationDO>()
                .eq("IS_BUSINESS_UNIT", 2).likeRight("sort_path", neworgOrganizationDO.getSortPath())).toString());
    }

    @Test
    public void updateDepTest2() {
        OrgDepartmentDO orgDepartmentDO = orgDepartmentService.getById("3373e43a15d74d97a4c9a2ebb4a5bb25");
        OrgOrganizationDO orgOrganizationDO = orgOrganizationService.getById("3373e43a15d74d97a4c9a2ebb4a5bb25");
        System.out.println("变更前部门：" + orgDepartmentDO);
        System.out.println("变更前子部门：" + orgDepartmentService.list(new QueryWrapper<OrgDepartmentDO>()
                .likeRight("sort_path", orgDepartmentDO.getSortPath())).toString());
        System.out.println("变更前组织：" + orgOrganizationDO);
        System.out.println("变更前子组织：" + orgOrganizationService.list(new QueryWrapper<OrgOrganizationDO>()
                .eq("IS_BUSINESS_UNIT", 2).likeRight("sort_path", orgOrganizationDO.getSortPath())).toString());
        orgDepartmentDO.setName("修改后-测试部门1");
        orgDepartmentDO.setCode("xiugaihou12");
        orgDepartmentDO.setSortIndex(7766);
        boolean flg = orgDepartmentService.updateDep(orgDepartmentDO);
        System.out.println("结果为：" + flg);
        assertThat(flg, is(true));
        OrgDepartmentDO neworgDepartmentDO = orgDepartmentService.getById("3373e43a15d74d97a4c9a2ebb4a5bb25");
        assertThat(neworgDepartmentDO.getName(), is("修改后-测试部门1"));
        assertThat(neworgDepartmentDO.getCode(), is("xiugaihou12"));
        assertThat(neworgDepartmentDO.getSortPath(), is("000011007766"));
        System.out.println("变更后部门:" + neworgDepartmentDO);
        OrgOrganizationDO neworgOrganizationDO = orgOrganizationService.getById("3373e43a15d74d97a4c9a2ebb4a5bb25");
        assertThat(neworgOrganizationDO.getName(), is("修改后-测试部门1"));
        assertThat(neworgOrganizationDO.getCode(), is("xiugaihou12"));
        assertThat(neworgOrganizationDO.getSortPath(), is("003519000200000011007766"));
        assertThat(orgOrganizationService.count(new QueryWrapper<OrgOrganizationDO>()
                .eq("IS_BUSINESS_UNIT", 2).likeRight("sort_path", "003519000200000011007766")), is(3));
        System.out.println("变更后组织：" + neworgOrganizationDO);
        System.out.println("变更后子组织：" + orgOrganizationService.list(new QueryWrapper<OrgOrganizationDO>()
                .eq("IS_BUSINESS_UNIT", 2).likeRight("sort_path", neworgOrganizationDO.getSortPath())).toString());
    }

    @Test
    public void deleteDepTest() {
        List<String> ids = new ArrayList<>(16);
        ids.add("fa08fccfe110469f87be52888b6159cd");
        ids.add("37af7950533b42c6a589d38ecc06f23b");
        System.out.println("删除前部门数据：" + orgDepartmentService.list(new QueryWrapper<OrgDepartmentDO>()
                .in("id", ids)).toString());
        System.out.println("删除前组织数据：" + orgOrganizationService.list(new QueryWrapper<OrgOrganizationDO>()
                .in("id", ids)).toString());
        Map<String, Object> map = new HashMap<>();
        map.put("ids", ids);
        R r = orgDepartmentService.deleteDep(map);
        System.out.println(r.get("msg"));
        assertThat("删除成功!", is(r.get("msg")));
        System.out.println("删除后部门数据：" + orgDepartmentService.list(new QueryWrapper<OrgDepartmentDO>()
                .in("id", ids)));
        assertThat(orgDepartmentService.getById("fa08fccfe110469f87be52888b6159cd").getName().substring(0, 3), is
                ("del"));
        assertThat(orgDepartmentService.getById("37af7950533b42c6a589d38ecc06f23b").getName().substring(0, 3), is
                ("del"));
        System.out.println("删除后组织数据：" + orgOrganizationService.list(new QueryWrapper<OrgOrganizationDO>()
                .in("id", ids)).toString());
        assertThat(orgOrganizationService.getById("fa08fccfe110469f87be52888b6159cd").getName().substring(0, 3), is
                ("del"));
        assertThat(orgOrganizationService.getById("37af7950533b42c6a589d38ecc06f23b").getName().substring(0, 3), is
                ("del"));
    }
}
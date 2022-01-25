package com.csicit.ace.platform.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.csicit.ace.common.pojo.domain.BdPersonIdTypeDO;
import com.csicit.ace.common.pojo.domain.OrgGroupDO;
import com.csicit.ace.common.pojo.domain.OrgOrganizationDO;
import com.csicit.ace.common.pojo.domain.OrgOrganizationVDO;
import com.csicit.ace.common.utils.server.R;
import com.csicit.ace.platform.BaseTest;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.hamcrest.Matchers.hasItems;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

public class OrgGroupServiceTest extends BaseTest {

    @Test
    public void mvGroupTest1() {
        OrgGroupDO orgGroupDO = orgGroupService.getById("0e4b26e45d0348088503efc8768afcdc");
        System.out.println("变更前集团：" + orgGroupDO);
        System.out.println("变更前子集团：" + orgGroupService.list(new QueryWrapper<OrgGroupDO>()
                .likeRight("sort_path", orgGroupDO.getSortPath())));
        OrgOrganizationDO orgOrganizationDO = orgOrganizationService.getById("0e4b26e45d0348088503efc8768afcdc");
        System.out.println("变更前组织：" + orgOrganizationDO);
        System.out.println("变更前子组织：" + orgOrganizationService.list(new QueryWrapper<OrgOrganizationDO>()
                .likeRight("sort_path", orgOrganizationDO.getSortPath())));
        boolean flg = orgGroupService.mvGroup(true, "0e4b26e45d0348088503efc8768afcdc", null);
        System.out.println("结果为：" + flg);
        assertThat(flg, is(true));
        OrgGroupDO neworgGroupDO = orgGroupService.getById("0e4b26e45d0348088503efc8768afcdc");
        assertThat(neworgGroupDO.getSortPath(), is("000010"));
        assertThat(orgGroupService.count(new QueryWrapper<OrgGroupDO>()
                .likeRight("sort_path", neworgGroupDO.getSortPath())), is(3));
        System.out.println("变更后集团：" + neworgGroupDO);
        System.out.println("变更后子集团：" + orgGroupService.list(new QueryWrapper<OrgGroupDO>()
                .likeRight("sort_path", neworgGroupDO.getSortPath())));
        OrgOrganizationDO neworgOrganizationDO = orgOrganizationService.getById("0e4b26e45d0348088503efc8768afcdc");
        assertThat(neworgOrganizationDO.getSortPath(), is("000010"));
        assertThat(orgOrganizationService.count(new QueryWrapper<OrgOrganizationDO>()
                .eq("IS_BUSINESS_UNIT", 0).likeRight("sort_path", neworgOrganizationDO.getSortPath())), is(3));
        System.out.println("变更后组织：" + neworgOrganizationDO);
        System.out.println("变更后子组织：" + orgOrganizationService.list(new QueryWrapper<OrgOrganizationDO>()
                .likeRight("sort_path", neworgOrganizationDO.getSortPath())));
    }

    @Test
    public void mvGroupTest2() {
        OrgGroupDO orgGroupDO = orgGroupService.getById("af562c26e35d44dbb82c1cf3b56ec112");
        System.out.println("变更前集团：" + orgGroupDO);
        System.out.println("变更前子集团：" + orgGroupService.list(new QueryWrapper<OrgGroupDO>()
                .likeRight("sort_path", orgGroupDO.getSortPath())));
        OrgOrganizationDO orgOrganizationDO = orgOrganizationService.getById("af562c26e35d44dbb82c1cf3b56ec112");
        System.out.println("变更前组织：" + orgOrganizationDO);
        System.out.println("变更前子组织：" + orgOrganizationService.list(new QueryWrapper<OrgOrganizationDO>()
                .likeRight("sort_path", orgOrganizationDO.getSortPath())));
        boolean flg = orgGroupService.mvGroup(false, "af562c26e35d44dbb82c1cf3b56ec112",
                "b190c25cb0234d51ab6de0bda198891b");
        System.out.println("结果为：" + flg);
        OrgGroupDO neworgGroupDO = orgGroupService.getById("af562c26e35d44dbb82c1cf3b56ec112");
        assertThat(neworgGroupDO.getSortPath(), is("003519000020000010"));
        assertThat(orgGroupService.count(new QueryWrapper<OrgGroupDO>()
                .likeRight("sort_path", neworgGroupDO.getSortPath())), is(2));
        System.out.println("变更后集团：" + neworgGroupDO);
        System.out.println("变更后子集团：" + orgGroupService.list(new QueryWrapper<OrgGroupDO>()
                .likeRight("sort_path", neworgGroupDO.getSortPath())));
        OrgOrganizationDO neworgOrganizationDO = orgOrganizationService.getById("af562c26e35d44dbb82c1cf3b56ec112");
        assertThat(neworgOrganizationDO.getSortPath(), is("003519000020000010"));
        assertThat(orgOrganizationService.count(new QueryWrapper<OrgOrganizationDO>()
                .eq("IS_BUSINESS_UNIT", 0).likeRight("sort_path", neworgOrganizationDO.getSortPath())), is(2));
        System.out.println("变更后组织：" + neworgOrganizationDO);
        System.out.println("变更后子组织：" + orgOrganizationService.list(new QueryWrapper<OrgOrganizationDO>()
                .likeRight("sort_path", neworgOrganizationDO.getSortPath())));
        assertThat(flg, is(true));
    }

    @Test
    public void updateGroupTest1() {
        OrgGroupDO orgGroupDO = orgGroupService.getById("9e92cb17c7bf4af9b6072520d42ea103");
        OrgOrganizationDO orgOrganizationDO = orgOrganizationService.getById("9e92cb17c7bf4af9b6072520d42ea103");
        System.out.println("变更前集团：" + orgGroupDO);
        System.out.println("变更前子集团：" + orgGroupService.list(new QueryWrapper<OrgGroupDO>()
                .likeRight("sort_path", orgGroupDO.getSortPath())).toString());
        System.out.println("变更前组织：" + orgOrganizationDO);
        System.out.println("变更前子组织：" + orgOrganizationService.list(new QueryWrapper<OrgOrganizationDO>()
                .likeRight("sort_path", orgOrganizationDO.getSortPath())).toString());
        // 修改
        orgGroupDO.setCode("xiugai1");
        orgGroupDO.setSortIndex(1234);
        orgGroupDO.setName("修改后-顶级集团");
        boolean flg = orgGroupService.updateGroup(orgGroupDO);
        System.out.println("结果：" + flg);
        assertThat(flg, is(true));
        OrgGroupDO neworgGroupDO = orgGroupService.getById("9e92cb17c7bf4af9b6072520d42ea103");
        assertThat(neworgGroupDO.getName(), is("修改后-顶级集团"));
        assertThat(neworgGroupDO.getCode(), is("xiugai1"));
        assertThat(neworgGroupDO.getSortPath(), is("001234"));
        System.out.println("变更后集团：" + neworgGroupDO);
        OrgOrganizationDO neworgOrganizationDO = orgOrganizationService.getById("9e92cb17c7bf4af9b6072520d42ea103");
        assertThat(neworgOrganizationDO.getName(), is("修改后-顶级集团"));
        assertThat(neworgOrganizationDO.getCode(), is("xiugai1"));
        assertThat(neworgOrganizationDO.getSortPath(), is("001234"));
        assertThat(orgOrganizationService.count(new QueryWrapper<OrgOrganizationDO>()
                .eq("IS_BUSINESS_UNIT", 0).likeRight("sort_path", neworgOrganizationDO.getSortPath())), is(5));
        System.out.println("变更后组织：" + neworgOrganizationDO);
        System.out.println("变更后子组织：" + orgOrganizationService.list(new QueryWrapper<OrgOrganizationDO>()
                .likeRight("sort_path", neworgOrganizationDO.getSortPath())).toString());

    }

    @Test
    public void updateGroupTest2() {
        OrgGroupDO orgGroupDO = orgGroupService.getById("0e4b26e45d0348088503efc8768afcdc");
        OrgOrganizationDO orgOrganizationDO = orgOrganizationService.getById("0e4b26e45d0348088503efc8768afcdc");
        System.out.println("变更前集团：" + orgGroupDO);
        System.out.println("变更前子集团：" + orgGroupService.list(new QueryWrapper<OrgGroupDO>()
                .likeRight("sort_path", orgGroupDO.getSortPath())).toString());
        System.out.println("变更前组织：" + orgOrganizationDO);
        System.out.println("变更前子组织：" + orgOrganizationService.list(new QueryWrapper<OrgOrganizationDO>()
                .likeRight("sort_path", orgOrganizationDO.getSortPath())).toString());
        // 修改
        orgGroupDO.setCode("xiugai1");
        orgGroupDO.setSortIndex(1234);
        orgGroupDO.setName("修改后-一级集团");
        boolean flg = orgGroupService.updateGroup(orgGroupDO);
        System.out.println("结果：" + flg);
        OrgGroupDO neworgGroupDO = orgGroupService.getById("0e4b26e45d0348088503efc8768afcdc");
        assertThat(neworgGroupDO.getName(), is("修改后-一级集团"));
        assertThat(neworgGroupDO.getCode(), is("xiugai1"));
        assertThat(neworgGroupDO.getSortPath(), is("003519001234"));
        System.out.println("变更后集团：" + neworgGroupDO);
        OrgOrganizationDO neworgOrganizationDO = orgOrganizationService.getById("0e4b26e45d0348088503efc8768afcdc");
        assertThat(neworgOrganizationDO.getName(), is("修改后-一级集团"));
        assertThat(neworgOrganizationDO.getCode(), is("xiugai1"));
        assertThat(neworgOrganizationDO.getSortPath(), is("003519001234"));
        assertThat(orgOrganizationService.count(new QueryWrapper<OrgOrganizationDO>()
                .eq("IS_BUSINESS_UNIT", 0).likeRight("sort_path", neworgOrganizationDO.getSortPath())), is(3));
        System.out.println("变更后组织：" + neworgOrganizationDO);
        System.out.println("变更后子组织：" + orgOrganizationService.list(new QueryWrapper<OrgOrganizationDO>()
                .likeRight("sort_path", neworgOrganizationDO.getSortPath())).toString());
        assertThat(flg, is(true));
    }

    @Test
    public void saveGroupTest() {
        OrgGroupDO orgGroupDO = new OrgGroupDO();
        orgGroupDO.setBeDeleted(0);
        orgGroupDO.setParentId("0");
        orgGroupDO.setName("单元测试集团");
        orgGroupDO.setCode("dayuanceshijituan");
        orgGroupDO.setSortIndex(111);
        boolean flg = orgGroupService.saveGroup(orgGroupDO);
        System.out.println("结果返回：" + flg);
        assertThat(flg, is(true));
        String id = orgGroupService.getOne(new QueryWrapper<OrgGroupDO>()
                .eq("name", "单元测试集团")).getId();
        assertThat((id != null), is(true));
        System.out.println("集团表数据：" + orgGroupService.getOne(new QueryWrapper<OrgGroupDO>()
                .eq("name", "单元测试集团")).toString());
        assertThat(orgOrganizationService.count(new QueryWrapper<OrgOrganizationDO>()
                .eq("name", "单元测试集团")), is(1));
        System.out.println("组织表数据：" + orgOrganizationService.getOne(new QueryWrapper<OrgOrganizationDO>()
                .eq("name", "单元测试集团")).toString());
        assertThat(orgOrganizationVService.count(new QueryWrapper<OrgOrganizationVDO>()
                .eq("group_id", id)), is(1));
        System.out.println("组织版本表数据：" + orgOrganizationVService.getOne(new QueryWrapper<OrgOrganizationVDO>()
                .eq("group_id", id)).toString());
        assertThat(bdPersonIdTypeService.count(new QueryWrapper<BdPersonIdTypeDO>()
                .eq("group_id", id)), is(2));
        System.out.println("证件表数据：" + bdPersonIdTypeService.list(new QueryWrapper<BdPersonIdTypeDO>()
                .eq("group_id", id)).toString());


        OrgGroupDO orgGroupDOChild = new OrgGroupDO();
        orgGroupDOChild.setBeDeleted(0);
        orgGroupDOChild.setParentId(id);
        orgGroupDOChild.setName("单元测试集团下级");
        orgGroupDOChild.setCode("dayuanceshijituanxiaji");
        orgGroupDOChild.setSortIndex(222);
        boolean flgChild = orgGroupService.saveGroup(orgGroupDOChild);
        System.out.println("结果返回：" + flgChild);
        assertThat(flgChild, is(true));
        String idChild = orgGroupService.getOne(new QueryWrapper<OrgGroupDO>()
                .eq("name", "单元测试集团下级")).getId();
        assertThat((idChild != null), is(true));
        System.out.println("集团表数据：" + orgGroupService.getOne(new QueryWrapper<OrgGroupDO>()
                .eq("name", "单元测试集团下级")).toString());
        assertThat(orgOrganizationService.count(new QueryWrapper<OrgOrganizationDO>()
                .eq("name", "单元测试集团下级")), is(1));
        System.out.println("组织表数据：" + orgOrganizationService.getOne(new QueryWrapper<OrgOrganizationDO>()
                .eq("name", "单元测试集团下级")).toString());
        assertThat(orgOrganizationVService.count(new QueryWrapper<OrgOrganizationVDO>()
                .eq("group_id", idChild)), is(1));
        System.out.println("组织版本表数据：" + orgOrganizationVService.getOne(new QueryWrapper<OrgOrganizationVDO>()
                .eq("group_id", idChild)).toString());
        assertThat(bdPersonIdTypeService.count(new QueryWrapper<BdPersonIdTypeDO>()
                .eq("group_id", idChild)), is(2));
        System.out.println("证件表数据：" + bdPersonIdTypeService.list(new QueryWrapper<BdPersonIdTypeDO>()
                .eq("group_id", idChild)).toString());

    }


    @Test
    public void deleteGroupTest() {
        List<String> ids = new ArrayList<>(16);
        ids.add("89a06f1243a74a9fbe20fdf08c9f27cd");
        ids.add("b190c25cb0234d51ab6de0bda198891b");
        System.out.println("删除前集团数据：" + orgGroupService.list(new QueryWrapper<OrgGroupDO>()
                .in("id", ids)).toString());
        System.out.println("删除前组织数据：" + orgOrganizationService.list(new QueryWrapper<OrgOrganizationDO>()
                .in("id", ids)).toString());
        Map<String, Object> map = new HashMap<>();
        map.put("ids", ids);
        R r = orgGroupService.deleteGroup(map);
        System.out.println(r.get("msg"));
        assertThat("删除成功!", is(r.get("msg")));
        assertThat(orgGroupService.getById("89a06f1243a74a9fbe20fdf08c9f27cd").getName().substring(0, 3), is
                ("del"));
        assertThat(orgGroupService.getById("b190c25cb0234d51ab6de0bda198891b").getName().substring(0, 3), is
                ("del"));
        System.out.println("删除后集团数据：" + orgGroupService.list(new QueryWrapper<OrgGroupDO>()
                .in("id", ids)));
        System.out.println("删除后组织数据：" + orgOrganizationService.list(new QueryWrapper<OrgOrganizationDO>()
                .in("id", ids)).toString());
        assertThat(orgOrganizationService.getById("89a06f1243a74a9fbe20fdf08c9f27cd").getName().substring(0, 3), is
                ("del"));
        assertThat(orgOrganizationService.getById("b190c25cb0234d51ab6de0bda198891b").getName().substring(0, 3), is
                ("del"));
    }

    @Test
    public void getGroupsByUserIdTest() {
        String userId = "4c66b48a5cfa4886a4c5861cb01eae02";
        List<OrgGroupDO> orgGroupDOS = orgGroupService.getGroupsByUserId(userId);
        if (orgGroupDOS != null && orgGroupDOS.size() > 0) {
            List<String> groupNames = orgGroupDOS.stream().map(OrgGroupDO::getName).collect(Collectors.toList());
            System.out.println(groupNames.toString());
            assertThat(groupNames, hasItems("单体测试顶级集团1", "单体测试一级集团1"));
        } else {
            assertThat(1, is(2));
        }
    }
}
package com.csicit.ace.platform.service;

import com.csicit.ace.platform.BaseTest;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

public class OrgOrganizationTypeServiceTest extends BaseTest {


    @Test
    public void getOrgTypeTest() {
        List<String> orgIds = new ArrayList<>();
        orgIds.add("9e92cb17c7bf4af9b6072520d42ea103");
        orgIds.add("8c22d74d433d4828bd59b05d7059a8b7");
        Map<String, List<String>> map = orgOrganizationTypeService.getOrgType(orgIds);
        List<String> values = new ArrayList<>();
        values.add("group");
        List<String> values2 = new ArrayList<>();
        values2.add("department");
        assertThat(map.get("9e92cb17c7bf4af9b6072520d42ea103"), is(values));
        assertThat(map.get("8c22d74d433d4828bd59b05d7059a8b7"), is(values2));
        System.out.println(map.toString());
    }
}
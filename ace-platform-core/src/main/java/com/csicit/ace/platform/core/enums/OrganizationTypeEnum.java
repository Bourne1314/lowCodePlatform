package com.csicit.ace.platform.core.enums;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import java.util.Arrays;

/**
 * 组织类型枚举类
 *
 * @author yansiyang
 * @version V1.0
 * @date 2019/4/15 19:29
 */
public enum OrganizationTypeEnum {

    Group(0),
    Project(1),
    Asset(2),
    Sales(3),
    Stock(4),
    Qc(5),
    Purchase(6),
    Corporation(7),
    Traffic(8),
    Maintain(9),
    Hr(10),
    Administration(11),
    Factory(12),
    Finance(13);


    private final int type;

    OrganizationTypeEnum(int type) {
        this.type = type;
    }

    public static JSONArray getOrgTypes() {
        JSONArray array = new JSONArray();
        String[] types = {"group@集团", "corporation@法人", "administration@行政", "finance@财务", "factory@工厂", "project@工程",
                "asset@资产",
                "sales@销售", "stock@库存", "qc@质量", "hr@人力资源", "purchase@采购", "traffic@运输", "maintain@维护"};
        Arrays.asList(types).parallelStream().forEach(type -> {
            JSONObject json = new JSONObject();
            json.put(type, type);
            array.add(json);
        });
        return array;
    }

    public static void main(String[] args) {

        System.out.println(getOrgTypes().toJSONString());

    }
}

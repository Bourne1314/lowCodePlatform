package com.csicit.ace.dev.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.csicit.ace.common.pojo.domain.dev.ProModelColDO;
import com.csicit.ace.common.pojo.domain.dev.ProModelDO;
import com.csicit.ace.common.pojo.domain.dev.ProPageInfoDO;
import com.csicit.ace.common.utils.StringUtils;
import com.csicit.ace.data.persistent.mapper.ProPageInfoMapper;
import com.csicit.ace.data.persistent.service.impl.BaseServiceImpl;
import com.csicit.ace.dev.service.ProInterfaceCategoryService;
import com.csicit.ace.dev.service.ProModelColService;
import com.csicit.ace.dev.service.ProModelService;
import com.csicit.ace.dev.service.ProPageInfoService;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

/**
 * 页面基本信息 实例对象访问接口实现
 *
 * @author shanwj
 * @date 2019/11/25 11:12
 */
@Service("proPageInfoService")
public class ProPageInfoServiceImpl extends BaseServiceImpl<ProPageInfoMapper, ProPageInfoDO> implements
        ProPageInfoService {

    @Autowired
    ProInterfaceCategoryService proInterfaceCategoryService;

    @Autowired
    ProModelService proModelService;

    @Autowired
    ProModelColService proModelColService;

    /**
     * 页面基本信息
     *
     * @param id
     * @return
     * @author zuogang
     * @date 2019/11/27 17:49
     */
    @Override
    public ProPageInfoDO getProPageInfoDO(String id) {
        ProPageInfoDO instance = getById(id);
        if (instance != null) {
            List<String> modelIds = new ArrayList<>(16);
            if (StringUtils.isNotBlank(instance.getModelIds())) {
                if (instance.getModelIds().contains(",")) {
                    modelIds = Arrays.asList(instance.getModelIds().split(","));
                } else {
                    modelIds.add(instance.getModelIds());
                }
            }
            if (CollectionUtils.isNotEmpty(modelIds)) {
                List<ProModelDO> proModelDOS = (List<ProModelDO>) proModelService.listByIds(modelIds);
                proModelDOS.stream().forEach(item -> {

                    List<ProModelColDO> proModelColDOS = proModelColService.list(new QueryWrapper<ProModelColDO>()
                            .eq("model_id", item.getId()));

                    JSONObject rules = new JSONObject();

                    proModelColDOS.stream().forEach(col -> {
                        JSONArray objColName = new JSONArray();

                        // nullable 不能为空

                        if (Objects.equals(col.getNullable(), 0)) {
                            JSONObject rule1 = new JSONObject();
                            if (Objects.equals(col.getDataType(), "Varchar") || Objects.equals(col.getDataType(),
                                    "Int") || Objects.equals(col.getDataType(), "Number")) {
                                rule1.put("required", true);
                                rule1.put("message", col.getCaption() + "不能为空");
                                rule1.put("trigger", "blur");
                            } else if (Objects.equals(col.getDataType(), "Datetime") || Objects.equals(col
                                    .getDataType(), "Date") || Objects.equals(col.getDataType(), "Timestamp")) {
                                rule1.put("type", "date");
                                rule1.put("required", true);
                                rule1.put("message", col.getCaption() + "不能为空");
                                rule1.put("trigger", "change");
                            }
                            objColName.add(rule1);
                        }
                        // 长度限制
                        if (Objects.equals(col.getDataType(), "Varchar") || Objects.equals(col.getDataType(),
                                "Int") || Objects.equals(col.getDataType(), "Number")) {
                            JSONObject rule2 = new JSONObject();
                            rule2.put("max", col.getDataPrecision());
                            rule2.put("message", "长度不大于" + col.getDataPrecision());
                            rule2.put("trigger", "blur");
                        }


                        rules.put(col.getObjColName(), objColName);
                    });

                    // rules
                    item.setRules(rules);
                    item.setProModelColDOS(proModelColDOS);
                });

                instance.setModels(proModelDOS);
            } else {
                instance.setModels((new ArrayList<>(16)));
            }

        }

        return instance;
    }

    public static void main(String[] args) {
        String a = "students";
        JSONObject container1 = new JSONObject();
        JSONArray students = new JSONArray();
        JSONObject studentOne = new JSONObject();
        studentOne.put("name", "张麻子");
        studentOne.put("sex", "男");
        studentOne.put("age", 12);
        studentOne.put("hobby", "java develop");
        students.add(studentOne);
        container1.put("" + a + "", students);
        System.out.println(container1);
    }

}

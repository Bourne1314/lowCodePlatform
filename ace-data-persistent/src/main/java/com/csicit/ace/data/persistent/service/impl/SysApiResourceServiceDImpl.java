package com.csicit.ace.data.persistent.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.csicit.ace.common.AppUpgradeJaxb.ApiResourceDetail;
import com.csicit.ace.common.pojo.domain.SysApiResourceDO;
import com.csicit.ace.common.pojo.domain.SysGroupDatasourceDO;
import com.csicit.ace.common.utils.JsonUtils;
import com.csicit.ace.data.persistent.mapper.SysApiResourceMapper;
import com.csicit.ace.data.persistent.service.SysApiResourceServiceD;
import com.csicit.ace.data.persistent.utils.CommonUtils;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author FourLeaves
 * @version V1.0
 * @date 2019/12/26 11:09
 */
@Service
public class SysApiResourceServiceDImpl extends BaseServiceImpl<SysApiResourceMapper, SysApiResourceDO>
        implements SysApiResourceServiceD {

    @Override
    public void initApiResource(String appName) {
//        //删除所有api资源
//        remove(new QueryWrapper<SysApiResourceDO>().eq("app_id", appName));
//        //保存包路径下class的集合
//        Set<Class<?>> classes = new LinkedHashSet<>();
//        ScanClassUtils.getInitScanClass(BaseInitConfig.getPkgName(appName), classes);
//        List<SysApiResourceDO> apis = new ArrayList<>();
//        classes.stream().forEach(clazz -> {
//            RequestMapping rm = clazz.getAnnotation(RequestMapping.class);
//            if (rm == null || rm.value().length == 0) {
//                return;
//            }
//            Arrays.asList(clazz.getDeclaredMethods()).stream().forEach(method -> {
//                AceAuth aceAuth = method.getAnnotation(AceAuth.class);
//                RequestMapping requestMapping = method.getAnnotation(RequestMapping.class);
//                if (aceAuth != null && requestMapping != null) {
//                    String authId = clazz.getName() + "." + method.getName();
//                    String authName = aceAuth.value();
//                    SysApiResourceDO sysApiResourceDO = new SysApiResourceDO();
//                    sysApiResourceDO.setAppId(appName);
//                    sysApiResourceDO.setId(authId);
//                    sysApiResourceDO.setName(authName);
//                    sysApiResourceDO.setApiUrl(
//                            rm.value()[0] + (requestMapping.value().length > 0 ? requestMapping.value()[0] : ""));
//                    sysApiResourceDO.setApiMethod(
//                            requestMapping.method().length > 0 ? requestMapping.method()[0].toString() : "");
//                    apis.add(sysApiResourceDO);
//                }
//            });
//        });
//        if (!org.apache.commons.collections.CollectionUtils.isEmpty(apis)) {
//            saveBatch(apis);
//        }
    }

    @Override
    public boolean saveApis(List<SysApiResourceDO> apis, String appId) {
        List<SysApiResourceDO> olds = list(new QueryWrapper<SysApiResourceDO>().eq("app_id", appId));
        //获取数据库中有，扫描数据中没有的api，需要进行删除处理
        List<SysApiResourceDO> deletes =
                olds.stream().filter(
                        item -> !apis.stream().map(e ->
                                e.getId())
                                .collect(Collectors.toList())
                                .contains(item.getId())
                ).collect(Collectors.toList());
        //获取扫描数据中有，数据库中没有的api，需要进行新增处理
        List<SysApiResourceDO> adds =
                apis.stream().filter(
                        item -> !olds.stream().map(e ->
                                e.getId())
                                .collect(Collectors.toList())
                                .contains(item.getId())
                ).collect(Collectors.toList());
        if (CollectionUtils.isNotEmpty(adds)) {
            if (!saveBatch(adds)) {
                return false;
            }
            if (CollectionUtils.isNotEmpty(deletes)) {
                if (!removeByIds(deletes.stream().map(SysApiResourceDO::getId).collect
                        (Collectors.toList()))) {
                    return false;
                }
            }
        } else {
            if (CollectionUtils.isNotEmpty(deletes)) {
                if (!removeByIds(deletes.stream().map(SysApiResourceDO::getId).collect
                        (Collectors.toList()))) {
                    return false;
                }
            }
        }

        return true;
    }

    /**
     * 应用升级时，API更新
     *
     * @param apiResourceDetails
     * @param appId
     * @return boolean
     * @author zuogang
     * @date 2020/8/10 14:33
     */
    @Override
    public boolean apiResourceUpdate(List<ApiResourceDetail> apiResourceDetails, String appId) {
        List<SysApiResourceDO> add = new ArrayList<>(16);
        List<SysApiResourceDO> upd = new ArrayList<>(16);

        apiResourceDetails.stream().forEach(apiResourceDetail -> {
            SysApiResourceDO item = JsonUtils.castObject(apiResourceDetail, SysApiResourceDO.class);
            // 判断当前业务数据库中是否存在该条数据
            SysApiResourceDO sysApiResourceDO = getOne(new QueryWrapper<SysApiResourceDO>()
                    .eq("trace_id", item.getTraceId()).eq("app_id", appId));
            if (sysApiResourceDO == null) {
                add.add(item);
            } else {
                // 判断业务数据库数据与应用更新配置文件数据是否一致
                if (CommonUtils.compareFields(sysApiResourceDO, item)) {
                    item.setId(sysApiResourceDO.getId());
                    upd.add(item);
                }
            }
        });

        if (CollectionUtils.isNotEmpty(add)) {
            if (!saveBatch(add)) {
                return false;
            }

        }
        if (CollectionUtils.isNotEmpty(upd)) {
            if (!updateBatchById(upd)) {
                return false;
            }
        }
        return true;
    }
}

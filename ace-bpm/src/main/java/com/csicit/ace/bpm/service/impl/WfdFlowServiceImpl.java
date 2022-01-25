package com.csicit.ace.bpm.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.csicit.ace.bpm.BpmException;
import com.csicit.ace.bpm.BpmManager;
import com.csicit.ace.bpm.mapper.WfdFlowMapper;
import com.csicit.ace.bpm.pojo.domain.*;
import com.csicit.ace.bpm.pojo.vo.NewJobFlowVO;
import com.csicit.ace.bpm.pojo.vo.NewJobTreeVO;
import com.csicit.ace.bpm.pojo.vo.wfd.Flow;
import com.csicit.ace.bpm.service.*;
import com.csicit.ace.bpm.utils.LocaleUtils;
import com.csicit.ace.common.pojo.AbstractBaseDomain;
import com.csicit.ace.common.utils.JsonUtils;
import com.csicit.ace.common.utils.internationalization.InternationUtils;
import com.csicit.ace.common.utils.server.R;
import com.csicit.ace.dbplus.service.impl.BaseServiceImpl;
import com.csicit.ace.interfaces.service.IWfdFlow;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;
import java.util.stream.Collectors;


/**
 * 流程定义 实例对象访问接口实现
 *
 * @author generator
 * @version V1.0
 * @date 2019-08-16 10:13:03
 */
@Service("wfdFlowService")
public class WfdFlowServiceImpl extends BaseServiceImpl<WfdFlowMapper, WfdFlowDO> implements WfdFlowService {
    @Autowired
    WfdVFlowService wfdVFlowService;
    @Autowired
    BpmManager bpmManager;
    @Autowired
    IWfdFlow iWfdFlow;
    @Autowired
    WfdFlowCategoryService wfdFlowCategoryService;
    @Autowired
    WfiDeliverService wfiDeliverService;
    @Autowired
    WfiFlowService wfiFlowService;

    /**
     * 存在判断
     *
     * @param instance
     * @return
     * @author zuogang
     * @date 2019/8/30 11:53
     */
    @Override
    public String existCheck(WfdFlowDO instance) {
        // 判断序号是否存在
        int count2 = count(new QueryWrapper<WfdFlowDO>().eq("category_id", instance.getCategoryId
                ()).eq("sort_no",
                instance.getSortNo()));
        if (count2 > 0) {
            return LocaleUtils.getWfdFlowSameSortNo(instance.getSortNo());
        }
        // 判断标识是否存在
        int count3 = count(new QueryWrapper<WfdFlowDO>()
                .eq("app_id", appName)
                .eq("code", instance.getCode()));
        if (count3 > 0) {
            return LocaleUtils.getWfdFlowSameCode(instance.getCode());
        }
        return "";
    }

    /**
     * 新增流程定义
     *
     * @param instance
     * @return
     * @author zuogang
     * @date 2019/8/30 11:53
     */
    @Override
    public boolean saveWfdFlow(WfdFlowDO instance) {
        // 流程定义初期值设置
        initialFlowValue(instance);

        if (!save(instance)) {
            return false;
        }
        return true;
    }

    /**
     * 流程定义--流程属性初期值设置
     *
     * @param instance
     * @return
     * @author zuogang
     * @date 2019/8/30 14:12
     */
    private void initialFlowValue(WfdFlowDO instance) {
        instance.setId("fid-" + UUID.randomUUID().toString());
        instance.setAppId(appName);
        //描述信息
        instance.setDescription("");
        // 查询权限
        instance.setQueryAuthId("");
        // 监控权限
        instance.setAdminAuthId("");
        // 发布后已经修改过 0没有，1有
        instance.setHasModified(0);
        // 修订版本号
        instance.setReviseVersion(0);
        // 数据源id
        instance.setFormDataSourceId("");
        // 数据表名
        instance.setFormDataTable("");
        // 是否在编辑
        instance.setEditing(0);
        // 发起权限
        instance.setInitAuthId("");

        Flow flow = new Flow();
        flow.setFlowChart("{\"class\":\"go.GraphLinksModel\",\"linkFromPortIdProperty\":\"fromPort\"," +
                "\"linkToPortIdProperty\":\"toPort\",\"nodeDataArray\":[],\"linkDataArray\":[]}");
        flow.setId(instance.getId());
        flow.setName(instance.getName());
        flow.setCode(instance.getCode());
        flow.setSortNo(instance.getSortNo());
        flow.setCategoryId(instance.getCategoryId());
        flow.setDescription(instance.getDescription());
        flow.setHasModified(instance.getHasModified());
        flow.setReviseVersion(instance.getReviseVersion());
        flow.setEnableSetUrgency(0);
        flow.setWorkNoStyle("{F}-{Y}-{M}-{D}-{N}");
        flow.setWorkNoSeqResetRule(0);
        flow.setWorkNoSeqLength(4);
        flow.setFormUrl("");
        flow.setFormDatasourceId("");
        flow.setFormDataTable("");
        flow.setFormIdName("");
        flow.setFormCascadeDel(1);
        flow.setFormSecretLevelField("");
        flow.setFormResultField("");
        flow.setFormSaveOperate("");
        flow.setMsgTemplateNewWork("");
        flow.setMsgTemplateFinished("");
        flow.setMsgChannel("");
        flow.setQueryAuthId(instance.getQueryAuthId());
        flow.setAdminAuthId(instance.getAdminAuthId());
        instance.setModel(flow.toJson());
    }

    /**
     * 前台Json信息传到后台,保存数据库
     *
     * @param map
     * @return
     * @author zuogang
     * @date 2019/8/30 11:53
     */
    @Override
    public R commitJsonInfoToDB(Map<String, Object> map) {
        String model = JSON.toJSONString(map.get("model"));
        Integer reviseVersion = (Integer) map.get("reviseVersion");
        String initAuthId = (String) map.get("initAuthId");
        WfdFlowDO wfdFlowDO = JsonUtils.castObject(net.sf.json.JSONObject.fromObject(model), WfdFlowDO.class);

        WfdFlowDO oldWfdFlowDO = getById(wfdFlowDO.getId());
        if (Objects.equals(oldWfdFlowDO.getCategoryId(), wfdFlowDO.getCategoryId())) {
            if (!Objects.equals(oldWfdFlowDO.getName(), wfdFlowDO.getName())) {
                // 判断流程定义是否已存在
                int count = count(new QueryWrapper<WfdFlowDO>().eq("category_id", wfdFlowDO
                        .getCategoryId()).eq("name", wfdFlowDO.getName()));
                if (count > 0) {
                    return R.error(InternationUtils.getInternationalMsg("SAME_MODEL",
                            new Object[]{"NAME", wfdFlowDO.getName()}));
                }
            }
            if (!Objects.equals(oldWfdFlowDO.getCode(), wfdFlowDO.getCode())) {
                // 判断标识是否存在
                int count3 = count(new QueryWrapper<WfdFlowDO>().eq("category_id", wfdFlowDO.getCategoryId
                        ()).eq("code",
                        wfdFlowDO.getCode()));
                if (count3 > 0) {
                    return R.error(InternationUtils.getInternationalMsg("SAME_MODEL",
                            new Object[]{InternationUtils.getInternationalMsg("CODE"), wfdFlowDO.getCode()}));
                }
            }
            if (!Objects.equals(oldWfdFlowDO.getSortNo(), wfdFlowDO.getSortNo())) {
                // 判断序号是否存在
                int count2 = count(new QueryWrapper<WfdFlowDO>().eq("category_id", wfdFlowDO.getCategoryId
                        ()).eq("sort_no",
                        wfdFlowDO.getSortNo()));
                if (count2 > 0) {
                    return R.error(InternationUtils.getInternationalMsg("SAME_MODEL",
                            new Object[]{InternationUtils.getInternationalMsg("SORT_NO"), wfdFlowDO.getSortNo().toString
                                    ()}));
                }
            }
        }

        wfdFlowDO.setModel(model);
        wfdFlowDO.setReviseVersion(reviseVersion + 1);

        // "0"为保存，"1"为签入
        String operate = (String) map.get("operate");
        if (Objects.equals("1", operate)) {
            wfdFlowDO.setEditing(0);
        }

        if (wfdVFlowService.getOne(new QueryWrapper<WfdVFlowDO>()
                .eq("flow_id", wfdFlowDO.getId()).eq("is_latest", 1)) != null) {
            // 发布后已经修改过
            wfdFlowDO.setHasModified(1);
        }
        wfdFlowDO.setEditingUser(securityUtils.getCurrentUserId());
        wfdFlowDO.setLastEditTime(LocalDateTime.now());
        wfdFlowDO.setInitAuthId(initAuthId);

        if (!updateById(wfdFlowDO)) {
            return R.error(InternationUtils.getInternationalMsg("SET_FAILED"));
        }
        return R.ok(InternationUtils.getInternationalMsg("SET_SUCCESS"));
    }

    /**
     * 签出,进入编辑状态
     *
     * @param flowId
     * @return
     * @author zuogang
     * @date 2019/8/30 11:53
     */
    @Override
    public boolean checkOutEditing(String flowId) {
        WfdFlowDO wfdFlowDO = getById(flowId);
        if (wfdFlowDO == null) {
            return false;
        }
        // 签出前判断是否已在编辑（签出）状态
        if (wfdFlowDO != null && Objects.equals(1, wfdFlowDO.getEditing())) {
            return false;
        }
        wfdFlowDO.setEditing(1);
        wfdFlowDO.setEditingUser(securityUtils.getCurrentUserId());
        if (!updateById(wfdFlowDO)) {
            return false;
        }
        return true;
    }

    /**
     * 签入,退出编辑状态
     *
     * @param flowId
     * @return
     * @author zuogang
     * @date 2019/8/30 11:53
     */
    @Override
    public boolean checkInEditing(String flowId) {
        WfdFlowDO wfdFlowDO = getById(flowId);
        if (wfdFlowDO == null) {
            return false;
        }
        // 签入前判断是否已不在编辑（签入）状态
        if (wfdFlowDO != null && Objects.equals(0, wfdFlowDO.getEditing())) {
            return false;
        }
        wfdFlowDO.setEditing(0);
        wfdFlowDO.setEditingUser(securityUtils.getCurrentUserId());
        wfdFlowDO.setLastEditTime(LocalDateTime.now());
        if (!updateById(wfdFlowDO)) {
            return false;
        }
        return true;
    }

    @Override
    public void lockSeq(String id) {
        baseMapper.lockSeq(id);
    }

    @Override
    public void updateSeq(String id, Integer seqNo, LocalDateTime latestCreateTime) {
        baseMapper.update(new WfdFlowDO(), new UpdateWrapper<WfdFlowDO>().set("SEQ_NO", seqNo).set
                ("LATEST_CREATE_TIME", latestCreateTime).eq("ID", id));
    }

    /**
     * 获取用户新建工作的左侧流程列表
     *
     * @param appId
     * @return
     */
    @Override
    public List<NewJobTreeVO> listFlowTreesForInitAuth(String appId) {
        List<NewJobTreeVO> treeVOS = new ArrayList<>(16);

        NewJobTreeVO parent = new NewJobTreeVO();
        parent.setLabel("所有流程");
        parent.setId("0");
        parent.setType("parentNode");
        parent.setCode("");

        List<String> authIds = iWfdFlow.getMixAuth(appId, securityUtils.getCurrentUserId()).stream().map
                (AbstractBaseDomain::getId)
                .collect(Collectors.toList());

        if (CollectionUtils.isEmpty(authIds)) {
            return new ArrayList<>(16);
        }

        // 用户可以发起的流程
        List<WfdFlowDO> wfdFlowDOS = list(new QueryWrapper<WfdFlowDO>()
                .in("INIT_AUTH_ID", authIds).eq("app_id", appId).orderByAsc("SORT_NO"));
        // 所属流程类别ID
        List<String> categoryIds = wfdFlowDOS.stream().map(WfdFlowDO::getCategoryId).distinct().collect(Collectors
                .toList());

        List<NewJobTreeVO> categorys = new ArrayList<>(16);
        categoryIds.stream().forEach(categoryId -> {
            WfdFlowCategoryDO wfdFlowCategoryDO = wfdFlowCategoryService.getById(categoryId);
            NewJobTreeVO category = new NewJobTreeVO();
            category.setLabel(wfdFlowCategoryDO.getName());
            category.setId(wfdFlowCategoryDO.getId());
            category.setType("categoryNode");
            category.setCode("");

            List<NewJobTreeVO> childs = new ArrayList<>(16);
            // 获取有发起权限的流程定义
            wfdFlowDOS.stream().forEach(wfdFlowDO -> {
                if (Objects.equals(wfdFlowDO.getCategoryId(), categoryId)) {
                    NewJobTreeVO child = new NewJobTreeVO();
                    child.setLabel(wfdFlowDO.getName());
                    child.setId(wfdFlowDO.getId());
                    child.setCode(wfdFlowDO.getCode());
                    child.setType("childNode");
                    childs.add(child);
                }
            });

            category.setChildren(childs);
            categorys.add(category);

        });
        parent.setChildren(categorys);
        treeVOS.add(parent);

        return treeVOS;
    }

    /**
     * 获取用户新建工作的最近使用流程列表
     *
     * @param appId
     * @return
     */
    @Override
    public R initFlowList(String appId) {
        // 获取三个月之前的时间
        Date dNow = new Date();   //当前时间
        Date dBefore = new Date();
        Calendar calendar = Calendar.getInstance(); //得到日历
        calendar.setTime(dNow);//把当前时间赋给日历
        calendar.add(Calendar.MONTH, -3);  //设置为前3月
        dBefore = calendar.getTime();   //得到前3月的时间
//        DateTimeFormatter DATE_FOMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime localDateTime1 = dBefore.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
//        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); //设置时间格式
//        String defaultStartDate = sdf.format(dBefore);    //格式化前3月的时间
        List<NewJobFlowVO> jobFlowVOS = baseMapper.initFlowList(securityUtils.getCurrentUserId(), appId);
        jobFlowVOS.stream().forEach(jobFlowVO -> {
            WfdFlowDO flowDO = getOne(new QueryWrapper<WfdFlowDO>().eq("code", jobFlowVO.getCode()));
            if (flowDO != null) {
                jobFlowVO.setId(flowDO.getId());
                jobFlowVO.setName(flowDO.getName());
            }
        });

        List<NewJobFlowVO> latelyFlowVOS = new ArrayList<>(16);
        jobFlowVOS.stream().forEach(jobFlowVO -> {
            if (jobFlowVO.getLatelyUserTime().compareTo(localDateTime1) >= 0) {
                latelyFlowVOS.add(jobFlowVO);
            }
        });
        return R.ok().put("latelyFlowVOS", latelyFlowVOS.size() > 5 ? latelyFlowVOS
                .subList(0, 5) : latelyFlowVOS).put("commonFlowVOS", jobFlowVOS.size() > 5 ? jobFlowVOS
                .subList(0, 5) : jobFlowVOS);
    }


    @Override
    public void recall(String flowId, Integer historyVersion) {
        WfdFlowDO wfdFlowDO = getById(flowId);
        if (wfdFlowDO == null) {
            throw new BpmException(LocaleUtils.getWfdFlowNotFound(flowId));
        }
        WfdVFlowDO wfdVFlow = wfdVFlowService.getByVersion(flowId, historyVersion);
        if (wfdVFlow == null) {
            throw new BpmException(LocaleUtils.getWfdVFlowVersionNotExist(historyVersion));
        } else {
            wfdFlowDO.setName(wfdVFlow.getName());
            wfdFlowDO.setCode(wfdVFlow.getCode());
            wfdFlowDO.setSortNo(wfdVFlow.getSortNo());
            wfdFlowDO.setCategoryId(wfdVFlow.getCategoryId());
            wfdFlowDO.setDescription(wfdVFlow.getDescription());
            wfdFlowDO.setAdminAuthId(wfdVFlow.getAdminAuthId());
            wfdFlowDO.setQueryAuthId(wfdVFlow.getQueryAuthId());
            wfdFlowDO.setModel(wfdVFlow.getModel());
            wfdFlowDO.setFormDataTable(wfdVFlow.getFormDataTable());
            wfdFlowDO.setFormDataSourceId(wfdVFlow.getFormDataSourceId());
            wfdFlowDO.setInitAuthId(wfdVFlow.getInitAuthId());
            updateById(wfdFlowDO);
        }
    }
}

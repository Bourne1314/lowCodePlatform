package com.csicit.ace.bpm.controller;

import com.alibaba.fastjson.JSONArray;
import com.csicit.ace.bpm.collection.BaseCollection;
import com.csicit.ace.bpm.service.WfdFlowService;
import com.csicit.ace.bpm.utils.*;
import com.csicit.ace.common.utils.JsonUtils;
import com.csicit.ace.common.utils.server.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author yansiyang
 * @version V1.0
 * @date 2019/8/23 17:54
 */
@RestController
@RequestMapping("/collection")
public class CollectionController extends BaseController {

    @Autowired
    ExpressionUtils expressionUtils;

    @Autowired
    List<BaseCollection> allCollections;

    @Autowired
    RuleUtils ruleUtils;

    @Autowired
    WfdCollectionUtils wfdCollectionUtils;

    @Autowired
    WfdFlowService wfdFlowService;

    @RequestMapping("")
    public R getCollections() {
        return R.ok().put("list", JsonUtils.castObject(allCollections, JSONArray.class));
    }

//    @RequestMapping(value = "/action/rule", method = RequestMethod.POST)
//    public R validateRule(@RequestBody RuleDO ruleDO) throws Exception {
//        WfdFlowDO wfdFlowDO = wfdFlowService.getById("");
//        Flow flow = Flow.getWfdFlow(wfdFlowDO.getModel());
//        boolean result = ruleUtils.calculateRule(flow, ruleDO);
//        return R.ok().put("result", result);
//    }
//
//    @RequestMapping(value = "/action/user", method = RequestMethod.POST)
//    public R validateRule(@RequestBody WfdCollectionDO collectionDO) throws Exception {
//        WfdFlowDO wfdFlowDO = wfdFlowService.getById("");
//        Flow flow = Flow.getWfdFlow(wfdFlowDO.getModel());
//        List<SysUserDO> list = wfdCollectionUtils.getUsers(flow, collectionDO);
//        return R.ok().put("list", list);
//    }
//
//    @RequestMapping(value = "/action/el", method = RequestMethod.POST)
//    public R validateEl(@RequestBody String var) {
//        String result = expressionUtils.parseExpression(var);
//        return R.ok().put("result", result);
//    }
}

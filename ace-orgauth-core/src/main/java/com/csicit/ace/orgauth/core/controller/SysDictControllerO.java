package com.csicit.ace.orgauth.core.controller;

import com.csicit.ace.common.pojo.domain.SysDictValueDO;
import com.csicit.ace.orgauth.core.service.SysDictValueService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * 数据字典项内容查询
 *
 * @author shanwj
 * @date 2019/5/22 15:58
 */
@RestController
@RequestMapping("/orgauth/sysDicts")
public class SysDictControllerO {


    @Resource(name = "sysDictValueServiceO")
    SysDictValueService sysDictValueService;

    /**
     * 获取字典类型的值 tpye 类型   appID应用名 groupId集团名称
     *
     * @param params 字典类型
     * @return
     * @author shanwj
     * @date 2019/4/11 18:25
     */
//    @ApiImplicitParam(name = "params", value = "Map", dataType = "Map", required = true)
//    @ApiOperation(value = "获取字典类型的值", httpMethod = "GET", notes = "获取字典类型的值")
    @RequestMapping(method = RequestMethod.GET)
    public List<SysDictValueDO> list(@RequestParam Map<String, Object> params) {
        return sysDictValueService.list(params);
    }
}

package com.csicit.ace.orgauth.core.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.csicit.ace.common.pojo.domain.OrgGroupDO;
import com.csicit.ace.common.pojo.domain.SysConfigDO;
import com.csicit.ace.common.utils.StringUtils;
import com.csicit.ace.common.utils.CacheUtil;
import com.csicit.ace.common.utils.system.SecurityUtils;
import com.csicit.ace.orgauth.core.service.OrgGroupService;
import com.csicit.ace.orgauth.core.service.SysConfigService;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;

/**
 * @author yansiyang
 * @version V1.0
 * @date 2019/5/22 11:27
 */
@RestController
@RequestMapping("/orgauth/sysConfigs")
public class SysConfigControllerO {

    @Resource(name = "sysConfigServiceO")
    SysConfigService sysConfigService;


    /**
     * 扫描应用配置项 并写入数据库
     * @param configDOSet
     * @return
     * @author FourLeaves
     * @date 2019/12/25 17:34
     */
    @RequestMapping(value = "/action/scanConfig", method = RequestMethod.POST)
    public boolean scanConfig(@RequestBody Set<SysConfigDO> configDOSet) {
        if (!org.apache.commons.collections.CollectionUtils.isEmpty(configDOSet)) {
            List<SysConfigDO> list = new ArrayList<>();
            list.addAll(configDOSet);
            sysConfigService.saveScanConfig(list);
        }
        return true;
    }

    /**
     * 获取配置项类型的值 集团级
     * @param name 配置项类型
     * @return
     * @author shanwj
     * @date 2019/4/11 18:25
     */
//    @ApiImplicitParam(name = "name", value = "key值", dataType = "String", required = true)
//    @ApiOperation(value = "获取配置项类型的值", httpMethod = "GET", notes = "获取配置项类型的值")
    @RequestMapping(value = "/{name}", method = RequestMethod.GET)
    public String getValue(@PathVariable("name") String name) {
        return sysConfigService.getValue(name);
    }



    /**
     * 获取配置项类型的值
     * 先查找应用级的，然后是集团级，租户级
     * @param name 配置项类型
     * @return
     * @author shanwj
     * @date 2019/4/11 18:25
     */
//    @ApiImplicitParam(name = "name", value = "key值", dataType = "String", required = true)
//    @ApiOperation(value = "获取配置项类型的值", httpMethod = "GET", notes = "获取配置项类型的值")
    @RequestMapping(value = "/action/getConfigValueByApp/{appId}/{name}", method = RequestMethod.GET)
    public String getValueByApp(@PathVariable("appId") String appId, @PathVariable("name") String name) {
        return sysConfigService.getValueByApp(appId, name);
    }
}

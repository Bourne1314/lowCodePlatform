package com.csicit.ace.platform.core.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.csicit.ace.common.annotation.AceAuth;
import com.csicit.ace.common.pojo.domain.SysApiResourceDO;
import com.csicit.ace.common.utils.server.R;
import com.csicit.ace.platform.core.service.SysApiResourceService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

/**
 * api资源管理 接口访问层
 *
 * @author shanwj
 * @date 2019/5/16 14:24
 */
@RestController
@RequestMapping("/sysApiResources")
@Api("api资源管理")
public class SysApiResourceController extends BaseController {

    @Autowired
    SysApiResourceService sysApiResourceService;

    /**
     * 获取所有API资源并分页
     *
     * @param params 分页参数及查询参数
     * @return R
     * @author yansiyang
     * @date 2019/4/12 8:36
     */
    @ApiOperation(value = "获取所有用户", httpMethod = "GET")
    @ApiImplicitParam(name = "params", value = "参数", required = true, paramType = "Map", dataType = "Map")
    @AceAuth("获取所有API资源")
    @RequestMapping(method = RequestMethod.GET)
    public R list(@RequestParam Map<String, Object> params) {
        String currentStr = (String) params.get("current");
        String sizeStr = (String) params.get("size");

        String keyWord = (String) params.get("keyWord");
        String app_id = (String) params.get("app_id");

        if (StringUtils.isBlank(currentStr) || StringUtils.isBlank(sizeStr)) {
            List<SysApiResourceDO> list = sysApiResourceService.list(new QueryWrapper<SysApiResourceDO>()
                    .eq("app_id", app_id).and(StringUtils.isNotBlank(keyWord)
                            , i -> i.like("name", keyWord)
                                    .or().like("api_url", keyWord)));
            return R.ok().put("list", list);
        }
        int current = Integer.parseInt(currentStr);
        int size = Integer.parseInt(sizeStr);

        Page<SysApiResourceDO> page = new Page<>(current, size);
        IPage list = sysApiResourceService.page(page, new QueryWrapper<SysApiResourceDO>()
                .eq("app_id", app_id)
                .and(StringUtils.isNotBlank(keyWord)
                        , i -> i.like("name", keyWord)
                                .or().like("api_url", keyWord)));
        return R.ok().put("page", list);
    }
}

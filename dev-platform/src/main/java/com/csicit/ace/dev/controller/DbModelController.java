package com.csicit.ace.dev.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.csicit.ace.common.pojo.domain.dev.MetaDatasourceDO;
import com.csicit.ace.common.pojo.domain.dev.MetaTableDO;
import com.csicit.ace.common.utils.PageUtils;
import com.csicit.ace.common.utils.StringUtils;
import com.csicit.ace.dev.service.MetaDatasourceService;
import com.csicit.ace.dev.service.MetaTableService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import java.util.Map;

/**
 * @author shanwj
 * @date 2019/11/25 18:01
 */
@Controller
@RequestMapping("/model")
public class DbModelController {

    @Autowired
    MetaDatasourceService metaDatasourceService;
    @Autowired
    MetaTableService metaTableService;

    @GetMapping()
    public String list(Model model) {
        List<MetaDatasourceDO> list = metaDatasourceService.list(null);
        model.addAttribute("dsList", list);
        return "model/list";
    }

    @GetMapping("/listTable")
    @ResponseBody()
    PageUtils list(@RequestParam Map<String, Object> params) {
        String currentStr = (String) params.get("offset");
        String sizeStr = (String) params.get("limit");
        int current = Integer.parseInt(currentStr);
        int size = Integer.parseInt(sizeStr);
        String dsId = params.get("dsId").toString();
        List<MetaTableDO> tables = metaTableService.list(new QueryWrapper<MetaTableDO>().eq("ds_id", dsId));
        PageUtils pageUtils = new PageUtils(current, size, tables, tables.size());
        return pageUtils;
    }

    @GetMapping("/listTableNoPage")
    @ResponseBody()
    List<MetaTableDO> listNoPage(@RequestParam Map<String, Object> params) {
        String dsId = params.get("dsId").toString();
        String searching = params.get("searching").toString();
        List<MetaTableDO> tables = metaTableService.list(new QueryWrapper<MetaTableDO>()
                .eq("ds_id", dsId).and(StringUtils.isNotBlank(searching)
                        , i -> i.like("table_name", searching)
                                .or().like("object_name", searching)
                                .or().like("caption", searching)
                ));
        return tables;
    }


}

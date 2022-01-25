package com.csicit.ace.dbplus.controller;

import com.csicit.ace.dbplus.mapper.DBHelperMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author FourLeaves
 * @version V1.0
 * @date 2020/7/31 10:55
 */
@RestController
@RequestMapping("/app-common-methods")
public class AppCommonMethodsController {

    @Autowired
    DBHelperMapper dbHelperMapper;

    @RequestMapping("/hasBpm")
    public Integer hasBpm() {
        try {
            dbHelperMapper.getCount("select count(*) from WFD_FLOW");
            return 1;
        } catch (Exception e) {
            return 0;
        }

    }
}

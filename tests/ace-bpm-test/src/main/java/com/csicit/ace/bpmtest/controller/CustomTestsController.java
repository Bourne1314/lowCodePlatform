package com.csicit.ace.bpmtest.controller;

import com.csicit.ace.bpmtest.test.CustomTests;
import com.csicit.ace.common.utils.server.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author JonnyJiang
 * @date 2019/9/11 9:15
 */
@RestController
@RequestMapping("/customTests")
public class CustomTestsController {
    @Autowired
    CustomTests customTests;

    @CrossOrigin
    @RequestMapping(value = "/testA", method = RequestMethod.GET)
    public R testA() {
        customTests.testA();
        return R.ok();
    }

    @CrossOrigin
    @RequestMapping(value = "/testB", method = RequestMethod.GET)
    public R testB() {
        customTests.testB();
        return R.ok();
    }

    @CrossOrigin
    @RequestMapping(value = "/testC", method = RequestMethod.GET)
    public R testC() {
//        customTests.testC();
        return R.ok();
    }
}

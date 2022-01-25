package com.csicit.ace.bpmtest.controller;

import com.csicit.ace.bpmtest.test.SubflowTest;
import com.csicit.ace.common.utils.server.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author JonnyJiang
 * @date 2019/9/23 19:31
 */
@RestController
@RequestMapping("/subflowTest")
public class SubflowTestController {
    @Autowired
    SubflowTest subflowTest;

    @CrossOrigin
    @RequestMapping(value = "/testA", method = RequestMethod.GET)
    public R testA() {
        subflowTest.testA();
        return R.ok();
    }
}
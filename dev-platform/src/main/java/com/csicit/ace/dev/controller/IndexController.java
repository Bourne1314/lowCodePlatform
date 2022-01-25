package com.csicit.ace.dev.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author FourLeaves
 * @version V1.0
 * @date 2020/6/8 11:31
 */
@Controller
@RequestMapping("")
public class IndexController {
    @GetMapping()
    public String getIndex() {
        return "/index.html";
    }
}

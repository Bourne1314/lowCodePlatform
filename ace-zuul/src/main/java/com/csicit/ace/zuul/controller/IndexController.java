package com.csicit.ace.zuul.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author FourLeaves
 * @version V1.0
 * @date 2020/6/8 11:31
 */
@Controller
@RequestMapping("")
public class IndexController {
    @GetMapping("")
    public void getIndex(HttpServletRequest request, HttpServletResponse response) throws Exception {
        request.getRequestDispatcher("/static/index.html")
                .forward(request, response);
    }
}

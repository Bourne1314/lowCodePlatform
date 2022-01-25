package com.csicit.ace.license.core.controller;

import com.csicit.ace.license.core.service.LicenseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * @author FourLeaves
 * @version V1.0
 * @date 2021/9/14 10:00
 */
@ConditionalOnExpression("'${spring.application.name}'.equals('licenseserver')")
@RestController
@RequestMapping("/")
public class LicenseController {

    @Autowired
    LicenseService licenseService;

    @GetMapping("/register")
    public String register(@RequestParam Map<String, String> params) {
        return licenseService.register(params);
    }
}

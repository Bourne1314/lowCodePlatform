package com.csicit.ace.orgauth.core.controller;

import com.csicit.ace.data.persistent.service.CodeSequenceService;
import com.csicit.ace.data.persistent.service.CodeTemplateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * @author shanwj
 * @date 2020/7/14 11:14
 */
@RestController
@RequestMapping("/orgauth/codeSequences")
public class CodeSequenceControllerO {

    @Autowired
    CodeSequenceService codeSequenceService;
    @Autowired
    CodeTemplateService codeTemplateService;

    @RequestMapping(value = "/action/getNum/{appId}/{bizTag}/{partValue}", method = RequestMethod.GET)
    String getNextNum(@PathVariable("appId")String appId,
                      @PathVariable("bizTag")String bizTag,
                      @PathVariable("partValue")String partValue){
        return codeSequenceService.getNextNum(appId,bizTag,partValue);
    }

    @RequestMapping(value = "/action/getCode", method = RequestMethod.GET)
    String getTemplateCode(@RequestBody Map<String,String> map){
        return codeTemplateService.getTemplateCode(map);
    }
}

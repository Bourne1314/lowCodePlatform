package com.csicit.ace.orgauth.core.controller;

import com.csicit.ace.common.pojo.vo.InitStorageDataVO;
import com.csicit.ace.dbplus.config.IScanData;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author shanwj
 * @date 2020/4/17 9:36
 */

@RestController
@Api("初始化扫描数据处理")
@RequestMapping("/orgauth/initHandler")
public class InitScanDataHandlerController {

    @Autowired
    IScanData scanData;

    @RequestMapping(method = RequestMethod.POST)
    public boolean saveInitScanData(@RequestBody InitStorageDataVO initStorageData){
        return scanData.saveInitScanData(initStorageData);
    }

}

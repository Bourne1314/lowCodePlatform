package com.csicit.ace.filetest.controller;

import com.csicit.ace.common.pojo.domain.FileInfoDO;
import com.csicit.ace.common.utils.server.R;
import com.csicit.ace.interfaces.service.IFile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author JonnyJiang
 * @date 2019/11/13 13:52
 */
@RestController
@RequestMapping("/test")
public class TestController {
    @Autowired
    private IFile iFile;

    @CrossOrigin
    @RequestMapping("/listFilesByFormId")
    public R listFilesByFormId(@RequestParam("configurationKey") String configurationKey, @RequestParam("formId") String formId) {
        List<FileInfoDO> fileInfos = iFile.listFileByFormId(configurationKey, formId);
        return R.ok().put("files", fileInfos);
    }

    @CrossOrigin
    @RequestMapping("/deleteFileByFormId")
    public R deleteFileByFormId(@RequestParam("configurationKey") String configurationKey, @RequestParam("formId") String formId) {
        iFile.deleteFileByFormId(configurationKey, formId);
        return R.ok();
    }

    @CrossOrigin
    @RequestMapping("/shareFileByFormId")
    public R shareFileByFormId(@RequestParam("configurationKey") String configurationKey, @RequestParam("formId") String formId, @RequestParam("desConfigurationKey") String desConfigurationKey, @RequestParam("desFormId") String desFormId) {
        iFile.shareFileByFormId(configurationKey, formId, desConfigurationKey, desFormId);
        return R.ok();
    }
}

package com.csicit.ace.orgauth.core.controller;

import com.csicit.ace.orgauth.core.service.BladeVisualMsgService;
import io.swagger.annotations.Api;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;


/**
 * 大屏消息 接口访问层
 *
 * @author generator
 * @version V1.0
 * @date 2020-07-29 16:49:49
 */

@RestController
@RequestMapping("/orgauth/bladeVisualMsgs")
@Api("大屏消息")
public class BladeVisualMsgControllerO {

    @Resource(name = "bladeVisualMsgServiceO")
    BladeVisualMsgService bladeVisualMsgServiceO;

    /**
     * 大屏消息推送
     *
     * @param displayContent 显示内容
     * @param code           大屏通知标识
     * @param appName        应用标识
     * @return
     * @author zuog
     */
    @RequestMapping(value = "/action/msgPush/{displayContent}/{code}/{appName}", method = RequestMethod.GET)
    void bladeVisualMsgPush(@PathVariable("displayContent") String displayContent, @PathVariable("code") String code,
                            @PathVariable("appName") String appName) throws UnsupportedEncodingException {
        displayContent = URLDecoder.decode(displayContent, "utf-8");
        bladeVisualMsgServiceO.bladeVisualMsgPush(displayContent, code, appName);
    }
}

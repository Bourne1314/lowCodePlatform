package com.csicit.ace.platform.core.service;

import com.csicit.ace.common.pojo.domain.SysMessageDO;
import com.csicit.ace.common.pojo.vo.KeyValueVO;
//import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

/**
 * @author shanwj
 * @date 2019/7/11 8:10
 */
//@FeignClient(name = "auth")
public interface IMessage {
    /**
     * 发送消息
     *
     * @param sysMessageDO
     * @return boolean
     * @author shanwj
     * @date 2019/7/5 14:27
     */
    @RequestMapping(value = "/sysMsgs", method = RequestMethod.POST)
    boolean sendMsg(@RequestBody SysMessageDO sysMessageDO);

    @RequestMapping(value = "/sysMsgs/query/sendTypes",method = RequestMethod.GET)
    List<KeyValueVO> getSendTypes();

}

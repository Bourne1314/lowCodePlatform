package com.csicit.ace.orgauth.core.message;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.Map;

/**
 * @author shanwj
 * @date 2019/7/5 15:32
 */
//@FeignClient(name = "push")
public interface IMsgPush {
    @RequestMapping(value = "/", method = RequestMethod.POST)
    String sendMsg(@RequestBody Map<String, Object> map);
}

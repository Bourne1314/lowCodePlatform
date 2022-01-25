package com.csicit.ace.demo.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.csicit.ace.common.pojo.domain.SysMessageDO;
import com.csicit.ace.common.utils.server.R;
import com.csicit.ace.common.utils.system.SecurityUtils;
import com.csicit.ace.interfaces.service.IMenu;
import com.csicit.ace.interfaces.service.IMessage;
import org.aspectj.weaver.ast.Var;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

/**
 * @author shanwj
 * @date 2019/6/14 8:47
 */
@Controller()
@RequestMapping("/")
public class IndexController {

    @Autowired
    IMenu menu;
    @Autowired
    SecurityUtils securityUtils;

    @Autowired
    private HttpServletRequest request;

    @Autowired
    private DiscoveryClient discoveryClient;

    /**
     * 应用名称
     */
    @Value("${spring.application.name}")
    private String appName;

    @Autowired
    private IMessage message;

    @RequestMapping(value = "",method = RequestMethod.GET)
    String welcome(Model model) {
//        List<SysMenuDO> menus = menu.list();
//        model.addAttribute("menus",menus);
//        model.addAttribute("name",appName);
        return "index";
    }

    @RequestMapping(value = "/sendMsg",method = RequestMethod.POST)
    @ResponseBody
    public String sendMsg() {
        Map<String,Object> data = new HashMap<>(16);
        data.put("title","日报提醒");
        data.put("url","www.baidu.com");
        data.put("name","单文金");
        data.put("count",12);
        R r = message.sendMessage(Arrays.asList(new String[]{"4bbfa6c45ed04aa89c6a1612cca97063","eed83c38d431494b991914f95adb3eea"}), "zz", "tt", data);
        return r.toString();
    }

    @RequestMapping(value = "/sendEvent",method = RequestMethod.POST)
    @ResponseBody
    public String sendEvent() {
        Map<String,Object> data = new HashMap<>(16);
        data.put("title","日报提醒");
        data.put("url","www.baidu.com");
        data.put("name","单文金");
        data.put("count",12);
        R r = message.fireSocketEvent(Arrays.asList(new String[]{"eed83c38d431494b991914f95adb3eea"}), "event",  data);
        return r.toString();
    }

    @RequestMapping(value = "/updateOne",method = RequestMethod.POST)
    @ResponseBody
    public String updateOne() {
        message.updateUserNoReadMsg("eed83c38d431494b991914f95adb3eea","a579e557f7e04759a3db2e758bff1cb6");
        return "no";
    }

    @RequestMapping(value = "/updateMsgs",method = RequestMethod.POST)
    @ResponseBody
    public String updateMsgs() {
        message.updateUserAllNoReadMsg("eed83c38d431494b991914f95adb3eea");
        return "no";
    }

    @RequestMapping(value = "/delOne",method = RequestMethod.POST)
    @ResponseBody
    public String delOne() {
        message.deleteMsg("586ee5dfae7144af9c89c0ec5d62c81c");
        return "no";
    }

    @RequestMapping(value = "/delMsgs",method = RequestMethod.POST)
    @ResponseBody
    public String delMsgs() {
        String []ids = new String[]{"586ee5dfae7144af9c89c0ec5d62c81c","60462f8e94e14fc38ce5a9d865c785d4"};
        message.deleteMsgs(Arrays.asList(ids));
        return "no";
    }

    @RequestMapping(value = "/delAllReadMsgs",method = RequestMethod.POST)
    @ResponseBody
    public String delAllReadMsgs() {
        message.deleteAllReadMsgs("eed83c38d431494b991914f95adb3eea");
        return "no";
    }

    @RequestMapping(value = "/delAllMsgs",method = RequestMethod.POST)
    @ResponseBody
    public String delAllMsgs() {
        message.deleteAllMsgs("eed83c38d431494b991914f95adb3eea");
        return "no";
    }





    @GetMapping("/listUn")
    @ResponseBody
    public List<SysMessageDO> listUn() {
        return message.listUserNoReadMsg("eed83c38d431494b991914f95adb3eea");
    }

    @GetMapping("/listRead")
    @ResponseBody
    public List<SysMessageDO> listRead() {
        return message.listUserReadMsg("eed83c38d431494b991914f95adb3eea");
    }

    @GetMapping("/listAll")
    @ResponseBody
    public List<SysMessageDO> listAll() {
        return message.listUserAllMsg("eed83c38d431494b991914f95adb3eea");
    }


    @GetMapping("/pageUn")
    @ResponseBody
    public Page<SysMessageDO> pageUn() {
        return message.listUserNoReadMsgInPage("eed83c38d431494b991914f95adb3eea",2,1);
    }

    @GetMapping("/pageRead")
    @ResponseBody
    public Page<SysMessageDO> pageRead() {
        return message.listUserReadMsgInPage("eed83c38d431494b991914f95adb3eea",2,1);
    }

    @GetMapping("/pageAll")
    @ResponseBody
    public Page<SysMessageDO> pageAll() {
        return message.listUserAllMsgInPage("eed83c38d431494b991914f95adb3eea",2,1);
    }


    @GetMapping("/serviceurl")
    @ResponseBody
    public Map<String, List<ServiceInstance>> serviceUrl() {
        Map<String, List<ServiceInstance>> msl = new HashMap<>();
        List<String> services = discoveryClient.getServices();
        for (String service : services) {
            List<ServiceInstance> sis = discoveryClient.getInstances(service);
            msl.put(service, sis);
        }
        return msl;
    }


    @GetMapping("/serviceurl/{appId}")
    @ResponseBody
    public Map<String, List<ServiceInstance>> serviceUrl(@PathVariable("appId")String appId) {
        Map<String, List<ServiceInstance>> msl = new HashMap<>();
        List<ServiceInstance> instances = discoveryClient.getInstances(appId);
        msl.put(appId, instances);
        return msl;
    }

}

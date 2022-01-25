package com.csicit.ace.platform.core.controller;

import com.csicit.ace.common.pojo.domain.SysMessageDO;
import com.csicit.ace.common.utils.PageUtils;
import com.csicit.ace.common.utils.server.R;
import com.csicit.ace.data.persistent.service.SysMessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

/**
 * @author shanwj
 * @date 2019/7/8 8:50
 */
@RestController
@RequestMapping("/sysMsgs")
public class SysMsgController extends  BaseController {

    @Autowired
    SysMessageService sysMessageService;

    /** 
     * 查询用户未读消息列表
     *
     * @param params
     * @return com.csicit.ace.common.utils.server.R
     * @author shanwj
     * @date 2019/7/8 10:41
     */
    @RequestMapping(value = "/query/msg/noRead",method = RequestMethod.GET)
    public R listUserNoReadMsg(@RequestParam Map<String, Object> params){
        List<SysMessageDO> msgs = sysMessageService.listNoRead(params.get("userId").toString());
        String currentStr = (String) params.get("current");
        String sizeStr = (String) params.get("size");
        int current = Integer.parseInt(currentStr);
        int size = Integer.parseInt(sizeStr);
        PageUtils pageUtils = new PageUtils(current,size,msgs);
        return R.ok().put("page", pageUtils);
    }

    /** 
     * 更新用户消息阅读状态
     *
     * @param userId	
     * @param msgId	
     * @return com.csicit.ace.common.utils.server.R
     * @author shanwj
     * @date 2019/7/8 10:42
     */
    @RequestMapping(value = "/update/msgRead",method = RequestMethod.POST)
    public R updateMsgRead(String userId,String msgId){
        if(sysMessageService.updateMsgRead(userId,msgId)){
            return R.ok();
        }
        return R.error("保存失败");
    }

}

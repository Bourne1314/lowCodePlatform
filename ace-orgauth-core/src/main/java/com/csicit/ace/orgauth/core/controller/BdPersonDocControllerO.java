package com.csicit.ace.orgauth.core.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.csicit.ace.common.pojo.domain.*;
import com.csicit.ace.common.utils.StringUtils;
import com.csicit.ace.common.utils.server.R;
import com.csicit.ace.orgauth.core.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author yansiyang
 * @version V1.0
 * @date 2019/11/7 15:41
 */
@RestController
@RequestMapping("/orgauth/bdPersonDocs")
public class BdPersonDocControllerO {

    @Resource(name = "bdPersonDocServiceO")
    BdPersonDocService bdPersonDocService;

    /**
     * 批量添加个人档案
     *
     * @param personDocDOS
     * @return
     * @author FourLeaves
     * @date 2020/12/18 15:38
     */
    @RequestMapping(value = "/action/addPersonDocs", method = RequestMethod.POST)
    boolean addPersonDocs(@RequestBody List<BdPersonDocDO> personDocDOS) {
        return bdPersonDocService.addPersonDocs(personDocDOS);
    }

    /**
     *根据用户id获取人员档案
     * @return
     * @author xulei
     */
    @RequestMapping(value = "/action/getBdPersonDoc/{userId}", method =
            RequestMethod.GET)
    R getBdPersonDoc(@PathVariable("userId") String userId){
        return bdPersonDocService.getBdPersonDoc(userId);
    }

    /**
     * 存入人员档案
     * @param personDoc
     * @author xulei
     * @Date 2020/6/30 9:14
     */
    @RequestMapping(value = "/action/savePersonAndUser", method =
            RequestMethod.POST)
    R savePersonAndUser(@RequestBody BdPersonDocDO personDoc){
        return bdPersonDocService.savePersonAndUser(personDoc);
    }
    /**
     * 根据 用户Id获取主职岗位
     *
     * @param userId
     * @return
     * @author yansiyang
     * @date 2019/11/7 15:35
     */
    @RequestMapping(value = "/action/getMainPostByUserId/{userId}", method = RequestMethod.GET)
    BdPostDO getMainPostByUserId(@PathVariable("userId") String userId) {
        return bdPersonDocService.getMainPostByUserId(userId);
    }


    /**
     * 根据部门 用户Id获取所有岗位
     *
     * @param userId
     * @return
     * @author yansiyang
     * @date 2019/11/7 15:35
     */
    @RequestMapping(value = "/action/getPostsByUserId/{userId}", method = RequestMethod.GET)
    List<BdPostDO> getPostsByUserId(@PathVariable("userId") String userId) {
        return bdPersonDocService.getPostsByUserId(userId);
    }

    /**
     * 根据部门 用户Id获取岗位
     *
     * @param depId
     * @param userId
     * @return
     * @author yansiyang
     * @date 2019/11/7 15:35
     */
    @RequestMapping(value = "/action/getPostByDepIdAndUserId/{depId}/{userId}", method = RequestMethod.GET)
    BdPostDO getPostByDepIdAndUserId(@PathVariable("depId") String depId, @PathVariable("userId") String userId) {
        return bdPersonDocService.getPostByDepIdAndUserId(depId, userId);
    }

    /**
     * 根据部门 用户Id获取职务
     *
     * @param depId
     * @param userId
     * @return
     * @author yansiyang
     * @date 2019/11/7 15:35
     */
    @RequestMapping(value = "/action/getJobByDepIdAndUserId/{depId}/{userId}", method = RequestMethod.GET)
    BdJobDO getJobByDepIdAndUserId(@PathVariable("depId") String depId, @PathVariable("userId") String userId) {
        return bdPersonDocService.getJobByDepIdAndUserId(depId, userId);
    }
}

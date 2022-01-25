package com.csicit.ace.orgauth.core.service;

import com.csicit.ace.common.pojo.domain.BdJobDO;
import com.csicit.ace.common.pojo.domain.BdPersonDocDO;
import com.csicit.ace.common.pojo.domain.BdPostDO;
import com.csicit.ace.common.pojo.domain.SysUserDO;
import com.csicit.ace.common.utils.server.R;
import com.csicit.ace.dbplus.service.IBaseService;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

/**
 * 基础数据-人员档案 实例对象访问接口
 *
 * @author generator
 * @version V1.0
 * @date 2019-04-15 17:25:44
 */
@Transactional
public interface BdPersonDocService extends IBaseService<BdPersonDocDO> {

    /**
     * 批量添加用户
     * @param personDocDOS
     * @return
     * @author FourLeaves
     * @date 2021/4/19 8:19
     */
    boolean addPersonDocs(List<BdPersonDocDO> personDocDOS);

    /**
     *根据用户id获取人员档案
     * @return
     * @author xulei
     */
     R getBdPersonDoc(@RequestBody String userId);


    /**
     * 存入人员档案
     *
     * @param personDoc
     * @author xulei
     * @Date 2020/6/30 9:30
     */
    R savePersonAndUser(@RequestBody BdPersonDocDO personDoc);

    /**
     * 根据 用户Id获取主职岗位
     *
     * @param userId
     * @return
     * @author FourLeaves
     * @date 2020/4/3 15:03
     */
    BdPostDO getMainPostByUserId(String userId);


    /**
     * 根据 用户Id获取主职岗位
     *
     * @param userId
     * @return
     * @author FourLeaves
     * @date 2020/4/3 15:03
     */
    List<BdPostDO> getPostsByUserId(String userId);


    /**
     * 根据部门 用户Id获取岗位
     *
     * @param depId
     * @param userId
     * @return
     * @author yansiyang
     * @date 2019/11/7 15:35
     */
    BdPostDO getPostByDepIdAndUserId(String depId, String userId);


    /**
     * 根据部门 用户Id获取职务
     *
     * @param depId
     * @param userId
     * @return
     * @author yansiyang
     * @date 2019/11/7 15:35
     */
    BdJobDO getJobByDepIdAndUserId(String depId, String userId);
}

package com.csicit.ace.platform.core.service;


import com.csicit.ace.common.pojo.domain.BdPostDO;
import com.csicit.ace.dbplus.service.IBaseService;
import org.springframework.transaction.annotation.Transactional;

/**
 * 部门岗位表 实例对象访问接口
 *
 * @author generator
 * @version V1.0
 * @date 2019-09-23 17:24:50
 */
@Transactional
public interface BdPostService extends IBaseService<BdPostDO> {
    /**
     *  保存岗位
     * @param postDO
     * @return boolean
     * @author wangzimin
     * @date 2019/9/24 11:27
     */
    boolean savePost(BdPostDO postDO);

    /**
     * 更新岗位
     * @param postDO
     * @return
     * @author wangzimin
     * @date 2019/9/24 11:28
     */
    boolean updatePost(BdPostDO postDO);

    /**
     * 删除岗位
     * @param ids
     * @return
     * @author wangzimin
     * @date 2019/9/24 11:29
     */
    boolean deletePost(String[] ids);
}

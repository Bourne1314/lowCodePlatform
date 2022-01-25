package com.csicit.ace.bpm.service;

import com.csicit.ace.bpm.pojo.domain.WfiCommentDO;
import com.csicit.ace.dbplus.service.IBaseService;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author JonnyJiang
 * @date 2020/5/25 19:11
 */
@Transactional
public interface WfiCommentService extends IBaseService<WfiCommentDO> {
    /**
     * 删除评论
     *
     * @param ids 评论id列表
     * @author JonnyJiang
     * @date 2020/6/2 17:15
     */

    void delete(String[] ids);
}

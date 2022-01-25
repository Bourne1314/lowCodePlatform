package com.csicit.ace.bpm.service;

import com.csicit.ace.bpm.pojo.vo.v7v1v81.dm.ActGeBytearrayDO;
import com.csicit.ace.dbplus.service.IBaseService;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author JonnyJiang
 * @date 2020/2/10 17:42
 */
@Transactional
public interface ActGeBytearrayService extends IBaseService<ActGeBytearrayDO> {
    /**
     * 删除
     *
     * @param ids id列表
     * @author JonnyJiang
     * @date 2020/2/11 8:25
     */

    void deleteByIds(List<String> ids);
}
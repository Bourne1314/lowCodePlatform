package com.csicit.ace.dev.service.impl;

import com.csicit.ace.common.pojo.domain.dev.ProModelIndexDO;
import com.csicit.ace.data.persistent.mapper.ProModelIndexMapper;
import com.csicit.ace.data.persistent.service.impl.BaseServiceImpl;
import com.csicit.ace.dev.service.LiquibaseService;
import com.csicit.ace.dev.service.ProModelIndexService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;


/**
 * 索引信息 实例对象访问接口实现
 *
 * @author shanwj
 * @version V1.0
 * @date 2019-11-07 10:30:31
 */
@Service("proModelIndexService")
public class ProModelIndexServiceImpl extends BaseServiceImpl<ProModelIndexMapper, ProModelIndexDO> implements
        ProModelIndexService {
    @Autowired
    private LiquibaseService liquibaseService;

    @Override
    public boolean saveIndex(ProModelIndexDO index) {
        index.setCreateTime(LocalDateTime.now());
//        index.setAssAction(0);

        if (!liquibaseService.addLiquibaseIndex(index)) {
            return false;
        }

        if (!save(index)) {
            return false;
        }


        return true;
    }


    @Override
    public boolean updateIndex(ProModelIndexDO index) {
        if (!liquibaseService.updLiquibaseIndex(index)) {
            return false;
        }
        if (!updateById(index)) {
            return false;
        }
        return true;
    }

    @Override
    public boolean deleteIndex(String id) {

        ProModelIndexDO indexDO = getById(id);
        if (!liquibaseService.delLiquibaseIndex(indexDO)) {
            return false;
        }
        if (!removeById(id)) {
            return false;
        }
        return true;
    }
}

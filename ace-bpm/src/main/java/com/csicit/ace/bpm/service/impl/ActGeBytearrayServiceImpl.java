package com.csicit.ace.bpm.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.csicit.ace.bpm.mapper.ActGeBytearrayMapper;
import com.csicit.ace.bpm.pojo.vo.v7v1v81.dm.ActGeBytearrayDO;
import com.csicit.ace.bpm.service.ActGeBytearrayService;
import com.csicit.ace.dbplus.service.impl.BaseServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author JonnyJiang
 * @date 2020/2/10 17:43
 */
@Service
public class ActGeBytearrayServiceImpl extends BaseServiceImpl<ActGeBytearrayMapper, ActGeBytearrayDO> implements ActGeBytearrayService {
    @Override
    public void deleteByIds(List<String> ids) {
        remove(new QueryWrapper<ActGeBytearrayDO>().in("ID_", ids));
    }
}
package com.csicit.ace.platform.core.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.csicit.ace.common.pojo.domain.BladeVisualShowDO;
import com.csicit.ace.common.utils.StringUtils;
import com.csicit.ace.data.persistent.mapper.BladeVisualShowMapper;
import com.csicit.ace.data.persistent.service.impl.BaseServiceImpl;
import com.csicit.ace.platform.core.service.BladeVisualShowService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;


/**
 * 大屏展示 实例对象访问接口实现
 *
 * @author generator
 * @version V1.0
 * @date 2020-06-05 10:05:48
 */
@Service("bladeVisualShowService")
public class BladeVisualShowServiceImpl extends BaseServiceImpl<BladeVisualShowMapper, BladeVisualShowDO> implements
        BladeVisualShowService {
    /**
     * 获取单个
     *
     * @param id
     * @return
     * @author zuogang
     * @date 2020/6/5 10:38
     */
    @Override
    public BladeVisualShowDO getInfo(String id) {
        BladeVisualShowDO instance = getById(id);
        if (instance != null) {
            if(Objects.equals(0,instance.getType())){
                instance.setChilds(list(new QueryWrapper<BladeVisualShowDO>().eq("type", 1).eq("parent_id", id)));
            }
        }
        return instance;
    }

    /**
     * 获取树列表
     *
     * @param appId
     * @return
     * @author zuogang
     * @date 2020/6/5 10:38
     */
    @Override
    public List<BladeVisualShowDO> getListTree(String appId) {
        List<BladeVisualShowDO> list = list(new QueryWrapper<BladeVisualShowDO>()
                .eq("type", 0).eq("app_id", appId).orderByAsc("name"));
        list.stream().forEach(item -> {
            item.setChilds(list(new QueryWrapper<BladeVisualShowDO>().eq("type", 1).eq("parent_id", item.getId()).eq
                    ("app_id", appId).orderByAsc("name")));
        });
        return list;
    }

    /**
     * 新增
     *
     * @param instance
     * @return
     * @author zuogang
     * @date 2020/6/5 10:38
     */
    @Override
    public boolean saveBladeVisualShow(BladeVisualShowDO instance) {
        if (!save(instance)) {
            return false;
        }
        return true;
    }


    /**
     * 修改
     *
     * @param instance
     * @return
     * @author zuogang
     * @date 2020/6/5 10:38
     */
    @Override
    public boolean updateBladeVisualShow(BladeVisualShowDO instance) {
        if (!updateById(instance)) {
            return false;
        }
        return true;
    }


    /**
     * 删除
     *
     * @param id
     * @return
     * @author zuogang
     * @date 2020/6/5 10:38
     */
    @Override
    public boolean deleteBladeVisualShow(String id) {
        if (StringUtils.isBlank(id)) {
            return true;
        }
        remove(new QueryWrapper<BladeVisualShowDO>().eq("type", 1).eq("parent_id", id));
        if (!removeById(id)) {
            return false;
        }
        return true;
    }
}

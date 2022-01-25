package com.csicit.ace.platform.core.service;

import com.csicit.ace.common.pojo.domain.SysDictDO;
import com.csicit.ace.dbplus.service.IBaseService;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.util.Collection;

/**
 * 字典类型 实例对象访问接口
 *
 * @author generator
 * @version V1.0
 * @date 2019-04-15 20:09:58
 */
@Transactional
public interface SysDictService extends IBaseService<SysDictDO> {

    /**
     * 保存数据
     *
     * @param sysDictDO 字典数据
     * @return boolean
     * @author shanwj
     * @date 2019/5/21 10:20
     */
    boolean saveDict(SysDictDO sysDictDO);

    /**
     * 更新字典数据
     *
     * @param sysDictDO 字典数据
     * @return boolean
     * @author shanwj
     * @date 2019/5/21 10:20
     */
    boolean updateDict(SysDictDO sysDictDO);

    /**
     * 删除字典数据
     * 根据提供的id集合进行删除
     *
     * @param idList 字典数据对象id集合
     * @return boolean
     * @author shanwj
     * @date 2019/5/21 10:20
     */
    boolean deleteByIds(Collection<? extends Serializable> idList);

}

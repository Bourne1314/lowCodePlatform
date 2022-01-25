package com.csicit.ace.platform.core.service;

import com.csicit.ace.common.pojo.domain.SysDictValueDO;
import com.csicit.ace.dbplus.service.IBaseService;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;

/**
 * 字典数据表 实例对象访问接口
 *
 * @author generator
 * @version V1.0
 * @date 2019-04-15 20:11:27
 */
@Transactional
public interface SysDictValueService extends IBaseService<SysDictValueDO> {

    /**
     * 通过集团主键获取字典值
     * @param type
     * @param groupId
     * @return
     * @author yansiyang
     * @date 2019/11/8 11:51
     */
    List<SysDictValueDO> getDictValueByGroupId(String type, String groupId);

    /**
     * 通过应用主键获取字典值
     * @param type
     * @param appId
     * @return
     * @author yansiyang
     * @date 2019/11/8 11:52
     */
    List<SysDictValueDO> getDictValueByAppId(String type, String appId);

    /**
     * 保存数据
     *
     * @param sysDictValueDO 字典数据
     * @return boolean
     * @author shanwj
     * @date 2019/5/21 10:20
     */
    boolean saveDictValue(SysDictValueDO sysDictValueDO);

    /**
     * 更新字典数据
     *
     * @param sysDictValueDO 字典数据
     * @return boolean
     * @author shanwj
     * @date 2019/5/21 10:20
     */
    boolean updateDictValue(SysDictValueDO sysDictValueDO);

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

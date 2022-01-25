package com.csicit.ace.dev.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.csicit.ace.common.pojo.domain.dev.MetaTableDO;
import com.csicit.ace.dbplus.service.IBaseService;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


/**
 * 数据表 实例对象访问接口
 *
 * @author shanwj
 * @date 2019-11-04 14:49:11
 * @version V1.0
 */
@Transactional
public interface MetaTableService extends IBaseService<MetaTableDO> {

    boolean saveTable(MetaTableDO table);

    boolean updateTable(MetaTableDO table);

    boolean deleteTable(String id);

    List<MetaTableDO> listTables(QueryWrapper<MetaTableDO> queryWrapper);

    List<MetaTableDO> listTablesByIds(List<String> ids);

    List<MetaTableDO> listTablesByDsId(String dsId);

    MetaTableDO getTable(String id);

}

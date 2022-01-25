package com.csicit.ace.dev.service.impl;

import com.csicit.ace.common.exception.RException;
import com.csicit.ace.common.pojo.domain.dev.MetaDatasourceDO;
import com.csicit.ace.common.pojo.domain.dev.MetaIndexDO;
import com.csicit.ace.common.pojo.domain.dev.MetaTableDO;
import com.csicit.ace.data.persistent.mapper.MetaIndexMapper;
import com.csicit.ace.dbplus.service.impl.BaseServiceImpl;
import com.csicit.ace.dev.db.IDBSchema;
import com.csicit.ace.dev.service.MetaAssociationService;
import com.csicit.ace.dev.service.MetaDatasourceService;
import com.csicit.ace.dev.service.MetaIndexService;
import com.csicit.ace.dev.service.MetaTableService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Objects;


/**
 * 索引信息 实例对象访问接口实现
 *
 * @author shanwj
 * @date 2019-11-07 10:30:31
 * @version V1.0
 */
@Service
public class MetaIndexServiceImpl extends BaseServiceImpl<MetaIndexMapper, MetaIndexDO> implements MetaIndexService {

    @Autowired
    private Map<String, IDBSchema> schemaMap;
    @Autowired
    private MetaTableService metaTableService;
    @Autowired
    private MetaDatasourceService metaDatasourceService;
    @Override
    public boolean saveIndex(MetaIndexDO index) {
        MetaTableDO table = metaTableService.getById(index.getTableId());
        MetaDatasourceDO ds = metaDatasourceService.getById(table.getDsId());
        IDBSchema schema = null;
        index.setName(index.getName().toUpperCase());
        if(save(index)){
            for (Map.Entry<String, IDBSchema> entry : schemaMap.entrySet()) {
                if (Objects.equals(ds.getType() + "Schema", entry.getKey())) {
                    schema = entry.getValue();
                }
            }
            if(schema==null){
                throw new RException("保存索引失败");
            }
            if(!schema.createIndex(ds,table.getTableName(),index)){
                throw new RException("保存索引失败");
            }
            return true;
        }
        return false;
    }

    @Override
    public boolean createIndex(MetaIndexDO index) {
        MetaTableDO table = metaTableService.getById(index.getTableId());
        MetaDatasourceDO ds = metaDatasourceService.getById(table.getDsId());
        IDBSchema schema = null;
        if(save(index)){
            for (Map.Entry<String, IDBSchema> entry : schemaMap.entrySet()) {
                if (Objects.equals(ds.getType() + "Schema", entry.getKey())) {
                    schema = entry.getValue();
                }
            }
            if(schema==null){
                throw new RException("创建索引失败");
            }
            if(!schema.createIndex(ds,table.getTableName(),index.getName(),index.getCols(),index.getOnlyOne()==1?true:false)){
                throw new RException("创建索引失败");
            }
            return true;
        }
        return false;
    }

    @Override
    public boolean updateIndex(MetaIndexDO index) {
        MetaTableDO table = metaTableService.getById(index.getTableId());
        MetaDatasourceDO ds = metaDatasourceService.getById(table.getDsId());
        IDBSchema schema = null;
        String oldName = getById(index.getId()).getName();
        index.setName(index.getName().toUpperCase());
        if(updateById(index)){
            if(!Objects.equals(oldName,index.getName())){
                for (Map.Entry<String, IDBSchema> entry : schemaMap.entrySet()) {
                    if (Objects.equals(ds.getType() + "Schema", entry.getKey())) {
                        schema = entry.getValue();
                    }
                }
                if(schema==null){
                    throw new RException("更新索引失败");
                }
                if(!schema.renameIndex(ds,table.getTableName(),oldName,index.getName())){
                    throw new RException("更新索引失败");
                }
            }
            return true;
        }
        return false;
    }

    @Override
    public boolean deleteIndex(String id) {
        MetaIndexDO index = getById(id);
        MetaTableDO table = metaTableService.getById(index.getTableId());
        MetaDatasourceDO ds = metaDatasourceService.getById(table.getDsId());
        if(removeById(id)){
            IDBSchema schema = null;
            for (Map.Entry<String, IDBSchema> entry : schemaMap.entrySet()) {
                if (Objects.equals(ds.getType() + "Schema", entry.getKey())) {
                    schema = entry.getValue();
                }
            }
            if(schema==null){
                throw new RException("删除索引失败");
            }

            if(!schema.dropIndex(ds,table.getTableName(),index.getName())){
                throw new RException("删除索引失败");
            }
            return true;
        }
        return false;
    }
}

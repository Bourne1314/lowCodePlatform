package com.csicit.ace.dev.service.impl;

import com.csicit.ace.common.exception.RException;
import com.csicit.ace.common.pojo.domain.dev.MetaAssociationDO;
import com.csicit.ace.common.pojo.domain.dev.MetaDatasourceDO;
import com.csicit.ace.common.pojo.domain.dev.MetaIndexDO;
import com.csicit.ace.common.pojo.domain.dev.MetaTableDO;
import com.csicit.ace.data.persistent.mapper.MetaAssociationMapper;
import com.csicit.ace.dbplus.service.impl.BaseServiceImpl;
import com.csicit.ace.dev.db.IDBSchema;
import com.csicit.ace.dev.service.MetaAssociationService;
import com.csicit.ace.dev.service.MetaIndexService;
import com.csicit.ace.dev.service.MetaTableService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Objects;


/**
 * 数据关联定义 实例对象访问接口实现
 *
 * @author shanwj
 * @date 2019-11-07 10:31:39
 * @version V1.0
 */
@Service
public class MetaAssociationServiceImpl extends BaseServiceImpl<MetaAssociationMapper, MetaAssociationDO>
        implements MetaAssociationService {

    @Autowired
    private Map<String, IDBSchema> schemaMap;
    @Autowired
    private MetaTableService metaTableService;
    @Autowired
    private MetaIndexService metaIndexService;
    @Autowired
    private MetaDatasourceServiceImpl metaDatasourceService;

    @Override
    public boolean saveAssociation(MetaAssociationDO ass) {
        MetaTableDO table = metaTableService.getById(ass.getTableId());
        MetaDatasourceDO ds = metaDatasourceService.getById(table.getDsId());
        IDBSchema schema = null;
        ass.setName(ass.getName().toUpperCase());
        //同步创建索引
        if(save(ass)){
            if(ass.getCreateFk()==1){
                MetaIndexDO index = new MetaIndexDO();
                index.setTableId(ass.getTableId());
                index.setName(ass.getName());
                index.setCols(ass.getColumns());
                index.setOnlyOne(ass.getIndexOne());
                if(!metaIndexService.save(index)){
                    return false;
                }
            }
            for (Map.Entry<String, IDBSchema> entry : schemaMap.entrySet()) {
                if (Objects.equals(ds.getType() + "Schema", entry.getKey())) {
                    schema = entry.getValue();
                }
            }
            if(schema==null){
                throw new RException("保存关联失败!");
            }
            MetaIndexDO index = metaIndexService.getById(ass.getRefIndexId());
            ass.setRefTableName(metaTableService.getById(ass.getRefTableId()).getTableName());
            ass.setTableName(table.getTableName());
            if (!schema.createForeignKey(ds,index,ass)){
                throw new RException("保存关联失败");
            }
            return true;
        }
        return false;
    }

    @Override
    public boolean updateAssociation(MetaAssociationDO ass) {
        MetaAssociationDO oldAss = getById(ass.getId());
        MetaTableDO table = metaTableService.getById(ass.getTableId());
        MetaDatasourceDO ds = metaDatasourceService.getById(table.getDsId());
        IDBSchema schema = null;
        ass.setName(ass.getName().toUpperCase());
        if(updateById(ass)){
            if(!Objects.equals(oldAss.getName(),ass.getName())){
                for (Map.Entry<String, IDBSchema> entry : schemaMap.entrySet()) {
                    if (Objects.equals(ds.getType() + "Schema", entry.getKey())) {
                        schema = entry.getValue();
                    }
                }
                if(schema==null){
                    throw new RException("更新关联失败!");
                }
                ass.setTableName(table.getTableName());
                if (!schema.renameConstraint(ds,ass.getTableName(),oldAss.getName(),ass.getName())){
                    throw new RException("更新关联失败!");
                }
                return true;
            }
            return true;
        }
        return false;
    }

    @Override
    public boolean deleteAssociation(String id) {
        MetaAssociationDO ass = getById(id);
        MetaTableDO table = metaTableService.getById(ass.getTableId());
        MetaDatasourceDO ds = metaDatasourceService.getById(table.getDsId());
        if (removeById(id)){
            IDBSchema schema = null;
            for (Map.Entry<String, IDBSchema> entry : schemaMap.entrySet()) {
                if (Objects.equals(ds.getType() + "Schema", entry.getKey())) {
                    schema = entry.getValue();
                }
            }
            if(schema==null){
                throw new RException("删除关联失败!");
            }
            if(!schema.dropConstraint(ds,table.getTableName(),ass.getName())){
                throw new RException("删除关联失败!");
            }
            return true;
        }
        return false;
    }
}

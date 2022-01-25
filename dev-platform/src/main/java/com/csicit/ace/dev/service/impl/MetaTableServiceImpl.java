package com.csicit.ace.dev.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.csicit.ace.common.exception.RException;
import com.csicit.ace.common.pojo.domain.dev.MetaDatasourceDO;
import com.csicit.ace.common.pojo.domain.dev.MetaIndexDO;
import com.csicit.ace.common.pojo.domain.dev.MetaTableColDO;
import com.csicit.ace.common.pojo.domain.dev.MetaTableDO;
import com.csicit.ace.common.utils.StringUtils;
import com.csicit.ace.data.persistent.mapper.MetaTableMapper;
import com.csicit.ace.data.persistent.service.impl.BaseServiceImpl;
import com.csicit.ace.dev.db.IDBSchema;
import com.csicit.ace.dev.service.MetaDatasourceService;
import com.csicit.ace.dev.service.MetaIndexService;
import com.csicit.ace.dev.service.MetaTableColService;
import com.csicit.ace.dev.service.MetaTableService;
import com.csicit.ace.dev.util.GenUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.util.*;


/**
 * 数据表 实例对象访问接口实现
 *
 * @author shanwj
 * @version V1.0
 * @date 2019-11-04 14:49:11
 */
@Service
public class MetaTableServiceImpl extends BaseServiceImpl<MetaTableMapper, MetaTableDO> implements MetaTableService {

    @Autowired
    private Map<String, IDBSchema> schemaMap;
    @Autowired
    private MetaTableColService metaTableColService;
    @Autowired
    private MetaDatasourceService metaDatasourceService;
    @Autowired
    private MetaIndexService metaIndexService;
    @Override
    public boolean saveTable(MetaTableDO table) {
        String dsId = table.getDsId();
        MetaDatasourceDO ds = metaDatasourceService.getById(dsId);
        IDBSchema schema = null;
        String tableName = table.getTableName().toUpperCase();
        table.setTableName(tableName);
        if(StringUtils.isEmpty(table.getObjectName())){
            table.setObjectName(GenUtils.columnToJava(tableName));
        }
        if(save(table)){
            if(metaTableColService.saveCreateCol(table.getId(),
                    table.getTreeFlag()==1?true:false,
                    table.getDataVersionFlag()==1?true:false,
                    table.getSecretFlag()==1?true:false)){
                for (Map.Entry<String, IDBSchema> entry : schemaMap.entrySet()) {
                    if (Objects.equals(ds.getType() + "Schema", entry.getKey())) {
                        schema = entry.getValue();
                    }
                }
                if(schema==null){
                    throw new RException("创建表失败!");
                }

                if(!schema.createTable(ds, table)){
                    throw new RException("创建表失败!");
                }
                if(Objects.equals("dm",ds.getType())){
                    try {
                        List<MetaIndexDO> indexes = schema.getIndexes(ds, table);
                        indexes.forEach(index->{
                            if(!metaIndexService.save(index)){
                                throw new RException("创建表失败!");
                            }
                        });
                    } catch (SQLException e) {
                        throw new RException(e.toString());
                    }
                }
                return true;

            }
            return true;
        }
        return false;
    }

    @Override
    public boolean updateTable(MetaTableDO table) {
        String dsId = table.getDsId();
        MetaDatasourceDO ds = metaDatasourceService.getById(dsId);
        IDBSchema schema = null;
        MetaTableDO oldTable = getById(table.getId());
        table.setTableName(table.getTableName().toUpperCase());
        if(updateById(table)){
            for (Map.Entry<String, IDBSchema> entry : schemaMap.entrySet()) {
                if (Objects.equals(ds.getType() + "Schema", entry.getKey())) {
                    schema = entry.getValue();
                }
            }
            if(schema==null){
                throw new RException("更新表失败!");
            }
            if(!schema.updateTable(ds,oldTable,table)){
                throw new RException("更新表失败!");
            }
            return true;
        }
        return false;
    }

    @Override
    public boolean deleteTable(String id) {
        MetaTableDO table = getById(id);
        MetaDatasourceDO ds = metaDatasourceService.getById(table.getDsId());
        if (removeById(id)){
            IDBSchema schema = null;
            for (Map.Entry<String, IDBSchema> entry : schemaMap.entrySet()) {
                if (Objects.equals(ds.getType() + "Schema", entry.getKey())) {
                    schema = entry.getValue();
                }
            }
            if(schema==null){
                throw new RException("删除表失败!");
            }
            if(!schema.dropTable(ds,table.getTableName())){
                throw new RException("删除表失败!");
            }
            return true;
        }
        return false;
    }

    @Override
    public List<MetaTableDO> listTables(QueryWrapper<MetaTableDO> queryWrapper) {
        List<MetaTableDO> list = list(queryWrapper);
        List<MetaTableDO> tables = new ArrayList<>(16);
        list.stream().forEach(table->{
            List<MetaTableColDO> tableCols =
                    metaTableColService.list(new QueryWrapper<MetaTableColDO>().eq("table_id", table.getId()));
            table.setCols(tableCols);
            tables.add(table);
        });
        return tables;
    }

    @Override
    public List<MetaTableDO> listTablesByIds(List<String> ids) {
        Collection<MetaTableDO> list = listByIds(ids);
        return getFullTables(list);
    }

    @Override
    public List<MetaTableDO> listTablesByDsId(String dsId) {
        List<MetaTableDO> list = list(new QueryWrapper<MetaTableDO>().eq("ds_id", dsId));
        return getFullTables(list);
    }

    private List<MetaTableDO> getFullTables(Collection<MetaTableDO> list){
        List<MetaTableDO> tables = new ArrayList<>(16);
        list.stream().forEach(table->{
            List<MetaTableColDO> tableCols =
                    metaTableColService.list(new QueryWrapper<MetaTableColDO>().eq("table_id", table.getId()));
            table.setCols(tableCols);
            tables.add(table);
        });
        return tables;
    }

    @Override
    public MetaTableDO getTable(String id) {
        MetaTableDO table = getById(id);
        List<MetaTableColDO> tableCols =
                metaTableColService.list(new QueryWrapper<MetaTableColDO>().eq("table_id", id));
        table.setCols(tableCols);
        return table;
    }
}

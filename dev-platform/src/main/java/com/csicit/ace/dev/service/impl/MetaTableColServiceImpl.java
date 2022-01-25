package com.csicit.ace.dev.service.impl;

import com.csicit.ace.common.exception.RException;
import com.csicit.ace.common.pojo.domain.dev.MetaDatasourceDO;
import com.csicit.ace.common.pojo.domain.dev.MetaTableColDO;
import com.csicit.ace.common.pojo.domain.dev.MetaTableDO;
import com.csicit.ace.data.persistent.mapper.MetaTableColMapper;
import com.csicit.ace.data.persistent.service.impl.BaseServiceImpl;
import com.csicit.ace.dev.db.IDBSchema;
import com.csicit.ace.dev.service.MetaDatasourceService;
import com.csicit.ace.dev.service.MetaTableColService;
import com.csicit.ace.dev.service.MetaTableService;
import com.csicit.ace.dev.util.GenUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;


/**
 * 数据列 实例对象访问接口实现
 *
 * @author shanwj
 * @date 2019-11-04 14:49:22
 * @version V1.0
 */
@Service
public class MetaTableColServiceImpl extends BaseServiceImpl<MetaTableColMapper, MetaTableColDO>
        implements MetaTableColService {

    @Autowired
    private Map<String, IDBSchema> schemaMap;
    @Autowired
    private MetaTableService metaTableService;
    @Autowired
    private MetaDatasourceService metaDatasourceService;

    @Override
    public boolean saveCreateCol(String tableId, boolean tree, boolean dataVersion, boolean secret) {
        List<MetaTableColDO> list = new ArrayList<>(16);
        MetaTableColDO idCol = new MetaTableColDO();
        idCol.setTableId(tableId);
        idCol.setCaption("主键");
        idCol.setDataType("String");
        idCol.setNullable(0);
        idCol.setTabColName("id");
        list.add(idCol);
        if(tree){
            MetaTableColDO treeCol = new MetaTableColDO();
            treeCol.setTableId(tableId);
            treeCol.setCaption("父节点id");
            treeCol.setDataType("String");
            treeCol.setNullable(0);
            treeCol.setTabColName("parent_id");
            list.add(treeCol);
        }

        if(dataVersion){
            MetaTableColDO versionCol = new MetaTableColDO();
            versionCol.setTableId(tableId);
            versionCol.setCaption("数据版本");
            versionCol.setDataType("Int");
            versionCol.setNullable(0);
            versionCol.setTabColName("data_version");
            list.add(versionCol);
        }

        if(dataVersion){
            MetaTableColDO secretCol = new MetaTableColDO();
            secretCol.setTableId(tableId);
            secretCol.setCaption("密级 5非密4内部3秘密2机密1绝密");
            secretCol.setDataType("Int");
            secretCol.setNullable(0);
            secretCol.setTabColName("secret_level");
            list.add(secretCol);
        }
        return saveBatch(list);
    }

    @Override
    public boolean saveTableCol(MetaTableColDO tableCol) {
        MetaTableDO table = metaTableService.getById(tableCol.getTableId());
        MetaDatasourceDO ds = metaDatasourceService.getById(table.getDsId());
        IDBSchema schema = null;
        String colName = tableCol.getTabColName().toUpperCase();
        tableCol.setTabColName(colName);
        tableCol.setObjColName(StringUtils.uncapitalize(GenUtils.columnToJava(colName)));
        if(save(tableCol)){
            for (Map.Entry<String, IDBSchema> entry : schemaMap.entrySet()) {
                if (Objects.equals(ds.getType() + "Schema", entry.getKey())) {
                    schema = entry.getValue();
                }
            }
            if(schema==null){
                throw new RException("保存表列失败!");
            }
            if(!schema.addColumn(ds,table.getTableName(),tableCol.getTabColName(),
                    tableCol.getCaption(),tableCol.getDataType(),tableCol.getDataSize(),
                    tableCol.getNullable()==0?false:true,tableCol.getDefaultValue())){
                throw new RException("保存表列失败!");
            }
            return true;
        }
        return false;
    }

    @Override
    public boolean updateTableCol(MetaTableColDO tableCol) {
        MetaTableDO table = metaTableService.getById(tableCol.getTableId());
        MetaDatasourceDO ds = metaDatasourceService.getById(table.getDsId());
        IDBSchema schema = null;
        MetaTableColDO oldCol = getById(tableCol.getId());
        String colName = tableCol.getTabColName().toUpperCase();
        tableCol.setTabColName(colName);
        tableCol.setTableName(table.getTableName());
        if(updateById(tableCol)){
            for (Map.Entry<String, IDBSchema> entry : schemaMap.entrySet()) {
                if (Objects.equals(ds.getType() + "Schema", entry.getKey())) {
                    schema = entry.getValue();
                }
            }
            if(schema==null){
                throw new RException("更新表列失败!");
            }
            if(!schema.updateTableColumn(ds,oldCol,tableCol)){
                throw new RException("更新表列失败!");
            }
            return true;
        }
        return false;
    }

    @Override
    public boolean deleteTableCol(String id) {
        MetaTableColDO tableColDO = getById(id);
        MetaTableDO tableDO = metaTableService.getById(tableColDO.getTableId());
        MetaDatasourceDO ds = metaDatasourceService.getById(tableDO.getDsId());
        if (removeById(id)){
            IDBSchema schema = null;
            for (Map.Entry<String, IDBSchema> entry : schemaMap.entrySet()) {
                if (Objects.equals(ds.getType() + "Schema", entry.getKey())) {
                    schema = entry.getValue();
                }
            }
            if(schema==null){
                throw new RException("删除表列失败!");
            }
            if(!schema.dropColumn(ds,tableDO.getTableName(),tableColDO.getTabColName())){
                throw new RException("删除表列失败!");
            }
            return true;
        }
        return false;
    }
}

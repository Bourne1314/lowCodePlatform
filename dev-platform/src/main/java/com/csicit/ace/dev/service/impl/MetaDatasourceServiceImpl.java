package com.csicit.ace.dev.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.csicit.ace.common.exception.RException;
import com.csicit.ace.common.pojo.domain.dev.*;
import com.csicit.ace.data.persistent.mapper.MetaDatasourceMapper;
import com.csicit.ace.data.persistent.service.impl.BaseServiceImpl;
import com.csicit.ace.dev.db.IDBSchema;
import com.csicit.ace.dev.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.StringJoiner;


/**
 * 数据源 实例对象访问接口实现
 *
 * @author shanwj
 * @version V1.0
 * @date 2019-11-04 14:48:24
 */
@Service
public class MetaDatasourceServiceImpl extends BaseServiceImpl<MetaDatasourceMapper, MetaDatasourceDO>
        implements MetaDatasourceService {

    @Autowired
    private MetaTableService metaTableService;
    @Autowired
    private MetaTableColService metaTableColService;
    @Autowired
    private MetaViewService metaViewService;
    @Autowired
    private MetaViewColService metaViewColService;
    @Autowired
    private MetaAssociationService metaAssociationService;
    @Autowired
    private MetaIndexService metaIndexService;
    @Autowired
    private Map<String, IDBSchema> schemaMap;

    @Override
    public boolean synDataModel(String id) {
        MetaDatasourceDO ds = getById(id);
        String type = ds.getType();
        for (Map.Entry<String, IDBSchema> entry : schemaMap.entrySet()) {
            if (Objects.equals(type + "Schema", entry.getKey())) {
                try {
                    IDBSchema schema = entry.getValue();
                    //删除原有的数据模型
                    metaTableService.remove(new QueryWrapper<MetaTableDO>().eq("ds_id", id));
                    metaViewService.remove(new QueryWrapper<MetaViewDO>().eq("ds_id", id));
                    ds = schema.synDataModel(ds);
                    //同步表模型
                    List<MetaTableDO> tables = ds.getTables();
                    tables.stream().forEach(table -> {
                        if (metaTableService.save(table)) {
                            List<MetaTableColDO> cols = table.getCols();
                            if (cols != null && cols.size() > 0) {
                                metaTableColService.saveBatch(cols);
                            }
                            List<MetaIndexDO> indexes = table.getIndexList();
                            if (indexes != null && indexes.size() > 0) {
                                metaIndexService.saveBatch(indexes);
                            }

                        }
                    });
                    tables.stream().forEach(table -> {
                        List<MetaAssociationDO> assList = table.getAssList();
                        if (assList != null && assList.size() > 0) {
                            for (MetaAssociationDO ass : assList) {
                                MetaTableDO referencedTable = metaTableService.getOne(new QueryWrapper<MetaTableDO>()
                                        .eq("ds_id", id)
                                        .eq("table_name", ass.getRefTableName()));
                                MetaIndexDO index = metaIndexService.getOne(new QueryWrapper<MetaIndexDO>()
                                        .eq("table_id", referencedTable.getId())
                                        .eq("name", ass.getRefIndexName()));
                                ass.setRefIndexId(index.getId());
                                ass.setRefTableId(referencedTable.getId());
                                metaAssociationService.save(ass);
                            }
                        }
                    });

                    //同步视图模型
                    List<MetaViewDO> views = ds.getViews();
                    views.stream().forEach(view -> {
                        if (metaViewService.save(view)) {
                            List<MetaViewColDO> cols = view.getCols();
                            if (cols != null && cols.size() > 0) {
                                metaViewColService.saveBatch(cols);
                            }

                        }
                    });
                } catch (Exception e) {
                    throw new RException(e.toString());
                }
                break;
            }
        }
        return true;
    }

    @Override
    public boolean synDataModel1(String id) {
        return true;
    }


    private String getMetaName(String dbObjName) {
        if (dbObjName.contains(",")) {
            String[] names = dbObjName.split(",");
//            String metaNameStr = "";
            StringJoiner joiner = new StringJoiner(",");
            for (String name : names) {
                joiner.add(Objects.equals(name.toUpperCase(), name) ? name.toLowerCase() : name);
//                metaNameStr += (name.toUpperCase() == name ? name.toLowerCase() : name) + ",";
            }
            return com.csicit.ace.common.utils.StringUtils.trimChar(joiner.toString(), ',');
        } else
            return Objects.equals(dbObjName.toUpperCase(), dbObjName) ? dbObjName.toLowerCase() : dbObjName;
    }
}

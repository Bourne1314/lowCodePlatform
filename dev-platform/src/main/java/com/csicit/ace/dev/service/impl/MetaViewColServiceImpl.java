package com.csicit.ace.dev.service.impl;

import com.csicit.ace.common.exception.RException;
import com.csicit.ace.common.pojo.domain.dev.MetaDatasourceDO;
import com.csicit.ace.common.pojo.domain.dev.MetaViewColDO;
import com.csicit.ace.common.pojo.domain.dev.MetaViewDO;
import com.csicit.ace.data.persistent.mapper.MetaViewColMapper;
import com.csicit.ace.dbplus.service.impl.BaseServiceImpl;
import com.csicit.ace.dev.db.IDBSchema;
import com.csicit.ace.dev.service.MetaViewColService;
import com.csicit.ace.dev.service.MetaViewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Objects;


/**
 * 视图列 实例对象访问接口实现
 *
 * @author shanwj
 * @date 2019-11-07 10:31:27
 * @version V1.0
 */
@Service
public class MetaViewColServiceImpl extends BaseServiceImpl<MetaViewColMapper, MetaViewColDO>
        implements MetaViewColService {

    @Autowired
    private Map<String, IDBSchema> schemaMap;
    @Autowired
    private MetaViewService metaViewService;
    @Autowired
    private MetaDatasourceServiceImpl metaDatasourceService;
    @Override
    public boolean updateTableColCaption(MetaViewColDO viewCol) {
        MetaViewColDO oldViewCol = getById(viewCol.getId());
        MetaViewDO view = metaViewService.getById(viewCol.getViewId());
        MetaDatasourceDO ds = metaDatasourceService.getById(view.getDsId());
        IDBSchema schema = null;
        if(Objects.equals(oldViewCol.getCaption(),viewCol.getCaption())){
            return true;
        }
        if(updateById(viewCol)){
            for (Map.Entry<String, IDBSchema> entry : schemaMap.entrySet()) {
                if (Objects.equals(ds.getType() + "Schema", entry.getKey())) {
                    schema = entry.getValue();
                }
            }
            if(schema==null){
                throw new RException("更新视图列说明失败");
            }
            if(!schema.isSupportViewCommnet()){
                throw new RException("改数据源类型不支持视图模式");
            }
            if(!schema.updateViewComment(ds,view.getName(),viewCol.getCaption())){
                throw new RException("更新视图列说明失败");
            }
            return true;
        }
        return false;

    }
}

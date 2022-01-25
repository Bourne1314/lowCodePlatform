package com.csicit.ace.dev.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.csicit.ace.common.constant.HttpCode;
import com.csicit.ace.common.exception.RException;
import com.csicit.ace.common.pojo.domain.dev.MetaDatasourceDO;
import com.csicit.ace.common.pojo.domain.dev.MetaViewColDO;
import com.csicit.ace.common.pojo.domain.dev.MetaViewDO;
import com.csicit.ace.common.utils.StringUtils;
import com.csicit.ace.common.utils.server.R;
import com.csicit.ace.data.persistent.mapper.MetaViewMapper;
import com.csicit.ace.dbplus.service.impl.BaseServiceImpl;
import com.csicit.ace.dev.db.IDBSchema;
import com.csicit.ace.dev.service.MetaViewColService;
import com.csicit.ace.dev.service.MetaViewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Objects;


/**
 * 视图 实例对象访问接口实现
 *
 * @author shanwj
 * @date 2019-11-07 10:31:19
 * @version V1.0
 */
@Service
public class MetaViewServiceImpl extends BaseServiceImpl<MetaViewMapper, MetaViewDO> implements MetaViewService {

    @Autowired
    private Map<String, IDBSchema> schemaMap;
    @Autowired
    private MetaViewColService metaViewColService;
    @Autowired
    private MetaDatasourceServiceImpl metaDatasourceService;

    @Override
    public boolean saveView(MetaViewDO view) {
        view.setName(view.getName().toUpperCase());
        MetaDatasourceDO ds = metaDatasourceService.getById(view.getDsId());
        if(save(view)){
            IDBSchema schema = null;
            for (Map.Entry<String, IDBSchema> entry : schemaMap.entrySet()) {
                if (Objects.equals(ds.getType() + "Schema", entry.getKey())) {
                    schema = entry.getValue();
                }
            }
            if(schema==null){
                throw new RException("保存视图失败");
            }
            R viewR = schema.createOrReplaceView(ds, view);
            if(Objects.equals(viewR.get("code").toString(),HttpCode.SUCCESS+"")){
                List<MetaViewColDO> cols = (List<MetaViewColDO>)viewR.get("cols");
                return metaViewColService.saveBatch(cols);
            }
            return false;
        }
        return false;
    }

    @Override
    public boolean updateView(MetaViewDO view) {
        MetaViewDO oldView = getById(view.getId());
        MetaDatasourceDO ds = metaDatasourceService.getById(view.getDsId());
        view.setName(view.getName().toUpperCase());
        if(updateById(view)){
            IDBSchema schema = null;
            for (Map.Entry<String, IDBSchema> entry : schemaMap.entrySet()) {
                if (Objects.equals(ds.getType() + "Schema", entry.getKey())) {
                    schema = entry.getValue();
                }
            }
            if(schema==null){
                return false;
            }
            if(!Objects.equals(oldView.getName(),view.getName())
                    &&StringUtils.isNotBlank(view.getCaption())&&schema.isSupportViewCommnet()){
                schema.renameView(ds,oldView.getName(),view.getName());
            }
            if(!Objects.equals(oldView.getCaption(),view.getCaption())
                    &&StringUtils.isNotBlank(view.getCaption())&&schema.isSupportViewCommnet()){
                schema.updateViewComment(ds,view.getName(),view.getCaption());
            }
            return true;
        }
        return false;
    }

    @Override
    public boolean deleteView(String id) {
        MetaViewDO view = getById(id);
        MetaDatasourceDO ds = metaDatasourceService.getById(view.getDsId());
        if (removeById(id)){
            IDBSchema schema = null;
            for (Map.Entry<String, IDBSchema> entry : schemaMap.entrySet()) {
                if (Objects.equals(ds.getType() + "Schema", entry.getKey())) {
                    schema = entry.getValue();
                }
            }
            if(schema==null){
                throw new RException("删除索引失败");
            }
            if(!schema.dropView(ds,view.getName())){
                throw new RException("删除索引失败");
            }
            return metaViewColService.remove(new QueryWrapper<MetaViewColDO>().eq("view_id",id));
        }
        return false;
    }
}

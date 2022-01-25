package com.csicit.ace.bpm.collection;

import com.csicit.ace.bpm.pojo.domain.WfiFlowDO;
import com.csicit.ace.bpm.pojo.vo.wfd.Flow;
import com.csicit.ace.bpm.pojo.vo.wfd.IdName;
import com.csicit.ace.bpm.service.WfiFlowService;
import com.csicit.ace.bpm.utils.FlowUtils;
import com.csicit.ace.common.utils.StringUtils;
import com.csicit.ace.dbplus.mapper.DBHelperMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Map;

/**
 * @author FourLeaves
 * @version V1.0
 * @date 2020/3/19 16:15
 */
@Service
public class CollectionFormUtilImpl implements  CollectionFormUtil{

    private static Logger logger = LoggerFactory.getLogger(CollectionFormUtilImpl.class);

    @Autowired
    WfiFlowService wfiFlowService;

    @Autowired
    DBHelperMapper dbHelperMapper;

    @Override
    public String getIdFromForm(List<IdName> idNames, Map<String, Object> variables) {
        try {
            if (!CollectionUtils.isEmpty(idNames) && !CollectionUtils.isEmpty(variables)) {
                // 指定字段名
                String columnName = idNames.get(0).getName();

                String wfiFlowID = (String) variables.get(BaseCollection.FLOW_INSTANCE_ID);
                WfiFlowDO wfiFlow = wfiFlowService.getById(wfiFlowID);
                if (wfiFlow != null) {
                    Flow flow = FlowUtils.getFlow(wfiFlow.getModel());
                    String tableName = flow.getFormDataTable();
                    String idName = StringUtils.isNotBlank(flow.getFormIdName()) ? flow.getFormIdName() : "id";
                    return dbHelperMapper.getStringWithParams("select {0} from {1} where {2} = ''{3}'' "
                            , columnName, tableName, idName, wfiFlow.getBusinessKey());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e.getMessage(), e);
        }
        return null;
    }
}

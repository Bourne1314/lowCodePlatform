package com.csicit.ace.dev.service.impl;

import com.csicit.ace.common.pojo.domain.dev.ProModelColDO;
import com.csicit.ace.data.persistent.mapper.ProModelColMapper;
import com.csicit.ace.data.persistent.service.impl.BaseServiceImpl;
import com.csicit.ace.dev.service.LiquibaseService;
import com.csicit.ace.dev.service.ProModelColService;
import com.csicit.ace.dev.util.GenUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Objects;


/**
 * 数据列 实例对象访问接口实现
 *
 * @author shanwj
 * @version V1.0
 * @date 2019-11-04 14:49:22
 */
@Service("proModelColService")
public class ProModelColServiceImpl extends BaseServiceImpl<ProModelColMapper, ProModelColDO>
        implements ProModelColService {
    @Autowired
    private LiquibaseService liquibaseService;

    @Override
    public boolean saveModelCol(ProModelColDO tableCol) {
        String colName = tableCol.getTabColName().toUpperCase();
        tableCol.setObjColName(StringUtils.uncapitalize(GenUtils.columnToJava(colName)));
        tableCol.setCreateTime(LocalDateTime.now());
        // 是否是系统字段
        if (Objects.equals(0, tableCol.getSyscol())) {
            if (!liquibaseService.addLiquibaseCol(tableCol)) {
                return false;
            }
        }
        if (!save(tableCol)) {
            return false;
        }
        return true;
    }

    @Override
    public boolean updateTableCol(ProModelColDO tableCol) {
        String colName = tableCol.getTabColName().toUpperCase();
        tableCol.setObjColName(StringUtils.uncapitalize(GenUtils.columnToJava(colName)));
        // 是否是系统字段
        if (Objects.equals(0, tableCol.getSyscol())) {
            if (!liquibaseService.updLiquibaseCol(tableCol)) {
                return false;
            }
        }

        if (!updateById(tableCol)) {
            return false;
        }

        return true;
    }

    @Override
    public boolean deleteTableCol(String id) {
        ProModelColDO proModelColDO = getById(id);
        if (Objects.equals(0, proModelColDO.getSyscol())) {
            if (!liquibaseService.delLiquibaseCol(proModelColDO)) {
                return false;
            }
        }

        if (!removeById(id)) {
            return false;
        }


        return true;
    }
}

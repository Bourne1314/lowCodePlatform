package com.csicit.ace.platform.core.service.impl;

import com.csicit.ace.data.persistent.mapper.AceDBHelperMapper;
import com.csicit.ace.platform.core.service.GeneratorService;
import com.csicit.ace.platform.core.utils.GenUtils;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipOutputStream;

/**
 * 实例对象访问接口实现
 *
 * @author generator
 * @version V1.0
 * @date 2019-04-15 17:27:00
 */
@Service
public class GeneratorServiceImpl implements GeneratorService {
    @Autowired
    AceDBHelperMapper aceDbHelperMapper;

    @Override
    public byte[] generatorCode(String[] tableNames) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ZipOutputStream zip = new ZipOutputStream(outputStream);
        for (String tableName : tableNames) {

            Map<String, Object> table = aceDbHelperMapper.getMap("SELECT a.object_name as tableName, b.comments as " +
                    "tableComment," +
                    " a.created createTime FROM user_objects a LEFT JOIN user_tab_comments b " +
                    "on a.object_name = b.table_name WHERE a.object_name = '" + tableName.toUpperCase() + "'");

            String columnsStr = "SELECT a.COLUMN_NAME     as columnName,\n" +
                    "       a.data_type       as dataType,\n" +
                    "       b.comments        as comments,\n" +
                    "       c.constraint_type as columnKey\n" +
                    "  FROM user_tab_columns a\n" +
                    "  LEFT JOIN user_col_comments b\n" +
                    "    on a.column_name = b.column_name\n" +
                    "  left join (select con.constraint_type, col.COLUMN_NAME, col.TABLE_NAME\n" +
                    "               from user_constraints con, user_cons_columns col\n" +
                    "              where con.constraint_name = col.constraint_name\n" +
                    "                and con.constraint_type = 'P') c\n" +
                    "    on (c.table_name = a.table_name and c.COLUMN_NAME = a.column_name)\n" +
                    " WHERE a.table_name = '" + tableName.toUpperCase() + "' \n" +
                    "   AND a.table_name = b.table_name order by a.COLUMN_ID";

            //查询列信息
            List<Map<String, Object>> columns = aceDbHelperMapper.getMaps(columnsStr);
            //生成代码
            GenUtils.generatorCode(table, columns, zip);
        }
        IOUtils.closeQuietly(zip);
        return outputStream.toByteArray();
    }
}

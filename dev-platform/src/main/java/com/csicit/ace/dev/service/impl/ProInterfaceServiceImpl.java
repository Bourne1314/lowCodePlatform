package com.csicit.ace.dev.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringPool;
import com.csicit.ace.common.pojo.domain.dev.ProDatasourceDO;
import com.csicit.ace.common.pojo.domain.dev.ProInputParamsDO;
import com.csicit.ace.common.pojo.domain.dev.ProInterfaceDO;
import com.csicit.ace.common.pojo.domain.dev.ProOutputParamsDO;
import com.csicit.ace.common.utils.MapUtils;
import com.csicit.ace.common.utils.StringUtils;
import com.csicit.ace.common.utils.UuidUtils;
import com.csicit.ace.common.utils.server.R;
import com.csicit.ace.data.persistent.mapper.ProInterfaceMapper;
import com.csicit.ace.data.persistent.service.impl.BaseServiceImpl;
import com.csicit.ace.dev.service.*;
import com.csicit.ace.dev.util.DBUtil;
import com.csicit.ace.dev.util.GenCodeUtils;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

/**
 * 实体模型 实例对象访问接口实现
 *
 * @author zuog
 * @date 2019/11/25 11:12
 */
@Service("proInterfaceService")
public class ProInterfaceServiceImpl extends BaseServiceImpl<ProInterfaceMapper, ProInterfaceDO>
        implements ProInterfaceService {
    @Autowired
    ProInterfaceCategoryService proInterfaceCategoryService;
    @Autowired
    ProDatasourceService proDatasourceService;
    @Autowired
    ProServiceService proServiceService;
    @Autowired
    ProInputParamsService proInputParamsService;
    @Autowired
    ProOutputParamsService proOutputParamsService;

    /**
     * 新增接口
     *
     * @param instance
     * @return boolean
     * @author zuogang
     * @date 2019/12/23 15:22
     */
    @Override
    public boolean addInterface(ProInterfaceDO instance) {
        instance.setCreateTime(LocalDateTime.now());
        instance.setId(UuidUtils.createUUID());

        if (!save(instance)) {
            return false;
        }

        List<ProInputParamsDO> proInputParamsDOS = instance.getInputParams();
        proInputParamsDOS.stream().forEach(proInputParamsDO -> {
            proInputParamsDO.setInterfaceId(instance.getId());
        });
        List<ProOutputParamsDO> proOutputParamsDOS = instance.getOutPutParams();
        proOutputParamsDOS.stream().forEach(proOutputParamsDO -> {
            proOutputParamsDO.setInterfaceId(instance.getId());
        });
        if (CollectionUtils.isNotEmpty(proInputParamsDOS)) {
            if (!proInputParamsService.saveBatch(proInputParamsDOS)) {
                return false;
            }
        }
        if (CollectionUtils.isNotEmpty(proOutputParamsDOS)) {
            if (!proOutputParamsService.saveBatch(proOutputParamsDOS)) {
                return false;
            }
        }


        return true;

    }

    /**
     * 修改接口
     *
     * @param instance
     * @return boolean
     * @author zuogang
     * @date 2019/12/23 15:22
     */
    @Override
    public boolean updInterface(ProInterfaceDO instance) {
        List<ProInputParamsDO> proInputParamsDOS = instance.getInputParams();
        List<ProOutputParamsDO> proOutputParamsDOS = instance.getOutPutParams();
        proInputParamsDOS.stream().forEach(proInputParamsDO -> {
            proInputParamsDO.setInterfaceId(instance.getId());
        });
        proOutputParamsDOS.stream().forEach(proOutputParamsDO -> {
            proOutputParamsDO.setInterfaceId(instance.getId());
        });
        if (proInputParamsService.count(new QueryWrapper<ProInputParamsDO>()
                .eq("interface_id", instance.getId())) > 0) {
            if (!proInputParamsService.remove(new QueryWrapper<ProInputParamsDO>()
                    .eq("interface_id", instance.getId()))) {
                return false;
            }
        }
        if (CollectionUtils.isNotEmpty(proInputParamsDOS)) {
            if (!proInputParamsService.saveBatch(proInputParamsDOS)) {
                return false;
            }
        }

        if (proOutputParamsService.count(new QueryWrapper<ProOutputParamsDO>()
                .eq("interface_id", instance.getId())) > 0) {
            if (!proOutputParamsService.remove(new QueryWrapper<ProOutputParamsDO>()
                    .eq("interface_id", instance.getId()))) {
                return false;
            }
        }

        if (CollectionUtils.isNotEmpty(proOutputParamsDOS)) {
            if (!proOutputParamsService.saveBatch(proOutputParamsDOS)) {
                return false;
            }
        }


        if (!updateById(instance)) {
            return false;
        }
        return true;
    }

    /**
     * 删除接口
     *
     * @param ids
     * @return boolean
     * @author zuogang
     * @date 2019/12/23 15:22
     */
    @Override
    public boolean delInterface(List<String> ids) {
        if (CollectionUtils.isEmpty(ids)) {
            return true;
        }
        // 删除
        if (!removeByIds(ids)) {
            return false;
        }
        return true;
    }


    /**
     * 代码检查
     *
     * @param instance
     * @return R
     * @author zuogang
     * @date 2019/12/23 15:22
     */
    @Override
    public R sqlCodeCheck(ProInterfaceDO instance) {
        String categoryId = instance.getCategoryId();
        String serviceId = proInterfaceCategoryService.getById(categoryId).getServiceId();
        ProDatasourceDO datasourceDO = proDatasourceService.getOne(new QueryWrapper<ProDatasourceDO>()
                .eq("IS_MAJOR", 1).eq("service_id", serviceId));
        if (datasourceDO == null) {
            return R.error("该服务尚未配置数据源！");
        }
        String sqlContent = instance.getSqlContent();

        List<ProOutputParamsDO> proOutputParamsDOS = new ArrayList<>();

        for (ProInputParamsDO inputParam : instance.getInputParams()) {
            if (Objects.equals("String", inputParam.getParamType())) {
                if (StringUtils.isBlank(inputParam.getParamDefaultValue())) {
                    sqlContent = sqlContent.replace(":" + inputParam.getParamKey(), "' '");
                } else {
                    sqlContent = sqlContent.replace(":" + inputParam.getParamKey(), "'" + inputParam
                            .getParamDefaultValue() + "'");
                }
            } else if (Objects.equals("Date", inputParam.getParamType())) {
                if (StringUtils.isBlank(inputParam.getParamDefaultValue())) {
                    DateTimeFormatter DATE_FOMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                    LocalDateTime serverTime = LocalDateTime.now();
                    sqlContent = sqlContent.replace(":" + inputParam.getParamKey(), "'" + DATE_FOMAT.format
                            (serverTime) + "'");
                } else {
                    sqlContent = sqlContent.replace(":" + inputParam.getParamKey(), "'" + inputParam
                            .getParamDefaultValue() + "'");
                }
            } else if (Objects.equals("Number", inputParam.getParamType())) {
                if (StringUtils.isBlank(inputParam.getParamDefaultValue())) {
                    sqlContent = sqlContent.replace(":" + inputParam.getParamKey(), "0");
                } else {
                    sqlContent = sqlContent.replace(":" + inputParam.getParamKey(), inputParam
                            .getParamDefaultValue());
                }
            }
        }
        Connection conn = null;
        try {
            conn = DBUtil.getConnection(datasourceDO.getDriver(), datasourceDO.getUrl(), datasourceDO.getUserName(),
                    datasourceDO
                            .getPassword());

            PreparedStatement st = conn.prepareStatement(sqlContent);
            ResultSet rs = st.executeQuery();
            ResultSetMetaData rd = rs.getMetaData();

            for (int i = 0; i < rd.getColumnCount(); i++) {
                ProOutputParamsDO proOutputParamsDO = new ProOutputParamsDO();

                //获取列名
                String columnName = rd.getColumnLabel(i + 1);
                proOutputParamsDO.setId(UuidUtils.createUUID());
                proOutputParamsDO.setParamKey(columnName);
                proOutputParamsDO.setParamItem(StringUtils.uncapitalize(GenCodeUtils.columnToJava(columnName)));
                //获取列类型
                int columnType = rd.getColumnType(i + 1);

                switch (columnType) {
                    case Types.VARCHAR:
                    case Types.CHAR:
                        proOutputParamsDO.setParamType("String");
                        break;
                    case Types.CLOB:
                        proOutputParamsDO.setParamType("Clob");
                        break;
                    case Types.BLOB:
                        proOutputParamsDO.setParamType("Blob");
                        break;
                    case Types.INTEGER:
                    case Types.SMALLINT:
                        proOutputParamsDO.setParamType("Int");
                        break;
                    case Types.BIGINT:
                        proOutputParamsDO.setParamType("Long");
                        break;
                    case Types.DATE:
                    case Types.TIMESTAMP:
                    case Types.TIME:
                        proOutputParamsDO.setParamType("Date");
                        break;
                    case Types.DECIMAL:
                        proOutputParamsDO.setParamType("BigDecimal");
                        break;
                    case Types.DOUBLE:
                        proOutputParamsDO.setParamType("Double");
                        break;
                    case Types.NUMERIC:
                        proOutputParamsDO.setParamType("Number");
                        break;
                    case Types.BIT:
                        proOutputParamsDO.setParamType("Boolean");
                        break;
                }
                proOutputParamsDOS.add(proOutputParamsDO);
            }
            if (st != null) {
                st.close();
                st = null;
            }
            if (conn != null) {
                conn.close();
                conn = null;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return R.error(e.getMessage());
        }


        return R.ok("SQL能够正确执行！").put("proOutputParamsDO", proOutputParamsDOS);
    }

    /**
     * 获取单个
     *
     * @param sqlCode
     * @return
     * @author zuogang
     * @date 2020/6/5 10:38
     */
    @Override
    public R getSqlResult(String sqlCode, String queryString) {
        Map<String, Object> urlParams = MapUtils.getUrlParams(queryString);
        ProInterfaceDO proInterfaceDO = getOne(new QueryWrapper<ProInterfaceDO>()
                .eq("code", sqlCode));
        if (proInterfaceDO == null) {
            return R.error("接口标识不存在");
        }
        String sqlContent = proInterfaceDO.getSqlContent();

        String currentPageStr = (String) urlParams.get("currentPage");
        String pageSizeStr = (String) urlParams.get("pageSize");

        // 判断是否分页
        if (Objects.equals(proInterfaceDO.getPageGet(), 1)) {
            if (StringUtils.isBlank(currentPageStr) || StringUtils.isBlank(pageSizeStr)) {
                return R.error("该分页接口的当前页码传值未传值或页面显示条数未传值！");
            }
        }
        String dsId = proInterfaceDO.getDsId();
        ProDatasourceDO datasourceDO = proDatasourceService.getById(dsId);

        List<ProInputParamsDO> proInputParamsDOS = proInputParamsService.list(new
                QueryWrapper<ProInputParamsDO>()
                .eq("interface_id", proInterfaceDO.getId()));

        for (ProInputParamsDO intput : proInputParamsDOS) {
            if (Objects.equals("String", intput.getParamType())) {
                if (StringUtils.isBlank((String) urlParams.get(intput.getParamKey()))) {
                    if (StringUtils.isNotBlank(intput.getParamDefaultValue())) {
                        // 默认值
                        sqlContent = sqlContent.replace(":" + intput.getParamKey(), "'" + intput
                                .getParamDefaultValue() + "'");
                    } else {
                        sqlContent = sqlContent.replace(":" + intput.getParamKey(), "' '");
                    }
                } else {
                    sqlContent = sqlContent.replace(":" + intput.getParamKey(), "'" + urlParams.get(intput
                            .getParamKey()) + "'");
                }
            } else if (Objects.equals("Date", intput.getParamType())) {
                if (StringUtils.isBlank((String) urlParams.get(intput.getParamKey()))) {
                    if (StringUtils.isNotBlank(intput.getParamDefaultValue())) {
                        sqlContent = sqlContent.replace(":" + intput.getParamKey(), "'" + intput
                                .getParamDefaultValue() + "'");
                    } else {
                        DateTimeFormatter DATE_FOMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                        LocalDateTime serverTime = LocalDateTime.now();
                        sqlContent = sqlContent.replace(":" + intput.getParamKey(), "'" + DATE_FOMAT.format
                                (serverTime) + "'");
                    }
                } else {
                    sqlContent = sqlContent.replace(":" + intput.getParamKey(), "'" + urlParams.get(intput
                            .getParamKey()) + "'");
                }
            } else if (Objects.equals("Number", intput.getParamType())) {
                if (StringUtils.isBlank((String) urlParams.get(intput.getParamKey()))) {
                    if (StringUtils.isNotBlank(intput.getParamDefaultValue())) {
                        sqlContent = sqlContent.replace(":" + intput.getParamKey(), intput.getParamDefaultValue());
                    } else {
                        sqlContent = sqlContent.replace(":" + intput.getParamKey(), "0");
                    }
                } else {
                    sqlContent = sqlContent.replace(":" + intput.getParamKey(), (String) urlParams.get
                            (intput.getParamKey()));
                }
            }
        }

        // sqlContent 添加分页语句，计算总条数
        if (Objects.equals(proInterfaceDO.getPageGet(), 1)) {

            StringBuilder sql1 = new StringBuilder();
            sql1.append("SELECT * ,COUNT(*) OVER() AS total FROM (");
            sql1.append(sqlContent);
            sql1.append(") AS A");
            sqlContent = sql1.toString();

            int currentPage = Integer.parseInt(currentPageStr);
            int pageSize = Integer.parseInt(pageSizeStr);

            Integer offset = 0;
            if (currentPage > 0) {
                offset = (currentPage - 1) * pageSize;
            }
            // oracle 数据库 || DM数据库
            if (datasourceDO.getDriver().contains("oracle") || datasourceDO.getDriver().contains("dm")) {

                StringBuilder sql = new StringBuilder();
                sql.append("SELECT * FROM ( SELECT TMP.*, ROWNUM ROW_ID FROM ( ");
                sql.append(sqlContent).append(" ) TMP WHERE ROWNUM <=").append((offset >= 1) ? (offset + pageSize) :
                        pageSize);
                sql.append(") WHERE ROW_ID > ").append(offset);
                sqlContent = sql.toString();
            } else if (datasourceDO.getDriver().contains("mysql") || datasourceDO.getDriver().contains("oscar")) {//
                // mysql数据库 || oscar数据库
                StringBuilder sql = new StringBuilder(sqlContent);
                sql.append(" LIMIT ").append(offset).append(StringPool.COMMA).append(pageSize);
                sqlContent = sql.toString();
            }
        }


        List<Map<String, Object>> result = new ArrayList<>(16);
        Connection conn = null;
        try {
            conn = DBUtil.getConnection(datasourceDO.getDriver(), datasourceDO.getUrl(), datasourceDO
                            .getUserName(),
                    datasourceDO
                            .getPassword());
            PreparedStatement st = conn.prepareStatement(sqlContent);
            ResultSet rs = st.executeQuery();
            ResultSetMetaData md = rs.getMetaData();
            int columnCount = md.getColumnCount(); //Map rowData;
            while (rs.next()) {
                Map rowData = new HashMap();
                for (int i = 1; i <= columnCount; i++) {
                    rowData.put(StringUtils.uncapitalize(GenCodeUtils.columnToJava(md.getColumnName(i))), rs
                            .getObject(i));
                }
                result.add(rowData);
            }
//            System.out.println(result);
            if (st != null) {
                st.close();
                st = null;
            }
            if (conn != null) {
                conn.close();
                conn = null;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return R.error(e.getMessage());
        }
        return R.ok().put("result", result);
    }
}

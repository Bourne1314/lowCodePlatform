package com.csicit.ace.platform.core.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.csicit.ace.common.pojo.domain.*;
import com.csicit.ace.common.utils.StringUtils;
import com.csicit.ace.common.utils.UuidUtils;
import com.csicit.ace.common.utils.server.R;
import com.csicit.ace.data.persistent.mapper.SysAppInterfaceMapper;
import com.csicit.ace.data.persistent.service.SysGroupDatasourceService;
import com.csicit.ace.data.persistent.service.impl.BaseServiceImpl;
import com.csicit.ace.platform.core.service.*;
import com.csicit.ace.platform.core.utils.GenUtils;
import com.csicit.ace.platform.core.utils.JDBCUtil;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


/**
 * 应用接口信息表 实例对象访问接口实现
 *
 * @author generator
 * @version V1.0
 * @date 2020-06-03 09:03:59
 */
@Service("sysAppInterfaceService")
public class SysAppInterfaceServiceImpl extends BaseServiceImpl<SysAppInterfaceMapper, SysAppInterfaceDO> implements
        SysAppInterfaceService {
    @Autowired
    SysGroupDatasourceService sysGroupDatasourceService;
    @Autowired
    SysGroupAppService sysGroupAppService;
    @Autowired
    SysAppInterfaceInputService sysAppInterfaceInputService;
    @Autowired
    SysAppInterfaceOutputService sysAppInterfaceOutputService;

    /**
     * 新增接口
     *
     * @param instance
     * @return boolean
     * @author zuogang
     * @date 2019/12/23 15:22
     */
    @Override
    public boolean addInterface(SysAppInterfaceDO instance) {
        instance.setCreateTime(LocalDateTime.now());
        instance.setId(UuidUtils.createUUID());

        if (!save(instance)) {
            return false;
        }

        List<SysAppInterfaceInputDO> sysAppInterfaceInputDOS = instance.getInputParams();
        sysAppInterfaceInputDOS.stream().forEach(sysAppInterfaceInputDO -> {
            sysAppInterfaceInputDO.setInterfaceId(instance.getId());
        });
        List<SysAppInterfaceOutputDO> sysAppInterfaceOutputDOS = instance.getOutPutParams();
        sysAppInterfaceOutputDOS.stream().forEach(sysAppInterfaceOutputDO -> {
            sysAppInterfaceOutputDO.setInterfaceId(instance.getId());
        });
        if (CollectionUtils.isNotEmpty(sysAppInterfaceInputDOS)) {
            if (!sysAppInterfaceInputService.saveBatch(sysAppInterfaceInputDOS)) {
                return false;
            }
        }
        if (CollectionUtils.isNotEmpty(sysAppInterfaceOutputDOS)) {
            if (!sysAppInterfaceOutputService.saveBatch(sysAppInterfaceOutputDOS)) {
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
    public boolean updInterface(SysAppInterfaceDO instance) {
        List<SysAppInterfaceInputDO> sysAppInterfaceInputDOS = instance.getInputParams();
        List<SysAppInterfaceOutputDO> sysAppInterfaceOutputDOS = instance.getOutPutParams();
        sysAppInterfaceInputDOS.stream().forEach(sysAppInterfaceInputDO -> {
            sysAppInterfaceInputDO.setInterfaceId(instance.getId());
        });
        sysAppInterfaceOutputDOS.stream().forEach(sysAppInterfaceOutputDO -> {
            sysAppInterfaceOutputDO.setInterfaceId(instance.getId());
        });
        if (sysAppInterfaceInputService.count(new QueryWrapper<SysAppInterfaceInputDO>()
                .eq("interface_id", instance.getId())) > 0) {
            if (!sysAppInterfaceInputService.remove(new QueryWrapper<SysAppInterfaceInputDO>()
                    .eq("interface_id", instance.getId()))) {
                return false;
            }
        }
        if (CollectionUtils.isNotEmpty(sysAppInterfaceInputDOS)) {
            if (!sysAppInterfaceInputService.saveBatch(sysAppInterfaceInputDOS)) {
                return false;
            }
        }

        if (sysAppInterfaceOutputService.count(new QueryWrapper<SysAppInterfaceOutputDO>()
                .eq("interface_id", instance.getId())) > 0) {
            if (!sysAppInterfaceOutputService.remove(new QueryWrapper<SysAppInterfaceOutputDO>()
                    .eq("interface_id", instance.getId()))) {
                return false;
            }
        }

        if (CollectionUtils.isNotEmpty(sysAppInterfaceOutputDOS)) {
            if (!sysAppInterfaceOutputService.saveBatch(sysAppInterfaceOutputDOS)) {
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
    public R sqlCodeCheck(SysAppInterfaceDO instance) {
//        String appId = instance.getAppId();
//        SysGroupAppDO sysGroupAppDO = sysGroupAppService.getById(appId);
        if (StringUtils.isBlank(instance.getDsId())) {
            return R.error("该服务尚未配置数据源！");
        }
        SysGroupDatasourceDO datasourceDO = sysGroupDatasourceService.getById(instance.getDsId());
        String sqlContent = instance.getSqlContent();

        List<SysAppInterfaceOutputDO> sysAppInterfaceOutputDOS = new ArrayList<>();

        for (SysAppInterfaceInputDO inputParam : instance.getInputParams()) {
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
            conn = JDBCUtil.getConnection(datasourceDO.getDriverName(), datasourceDO.getUrl(), datasourceDO
                            .getUsername(),
                    datasourceDO
                            .getPassword());
            PreparedStatement st = conn.prepareStatement(sqlContent);
            ResultSet rs = st.executeQuery();
            ResultSetMetaData rd = rs.getMetaData();

            for (int i = 0; i < rd.getColumnCount(); i++) {
                SysAppInterfaceOutputDO sysAppInterfaceOutputDO = new SysAppInterfaceOutputDO();

                //获取列名
                String columnName = rd.getColumnLabel(i + 1);
                sysAppInterfaceOutputDO.setId(UuidUtils.createUUID());
                sysAppInterfaceOutputDO.setParamKey(columnName);
                sysAppInterfaceOutputDO.setParamItem(StringUtils.uncapitalize(GenUtils.columnToJava(columnName)));
                //获取列类型
                int columnType = rd.getColumnType(i + 1);

                switch (columnType) {
                    case Types.VARCHAR:
                    case Types.CHAR:
                        sysAppInterfaceOutputDO.setParamType("String");
                        break;
                    case Types.CLOB:
                        sysAppInterfaceOutputDO.setParamType("Clob");
                        break;
                    case Types.BLOB:
                        sysAppInterfaceOutputDO.setParamType("Blob");
                        break;
                    case Types.INTEGER:
                    case Types.SMALLINT:
                        sysAppInterfaceOutputDO.setParamType("Int");
                        break;
                    case Types.BIGINT:
                        sysAppInterfaceOutputDO.setParamType("Long");
                        break;
                    case Types.DATE:
                    case Types.TIMESTAMP:
                    case Types.TIME:
                        sysAppInterfaceOutputDO.setParamType("Date");
                        break;
                    case Types.DECIMAL:
                        sysAppInterfaceOutputDO.setParamType("BigDecimal");
                        break;
                    case Types.DOUBLE:
                        sysAppInterfaceOutputDO.setParamType("Double");
                        break;
                    case Types.NUMERIC:
                        sysAppInterfaceOutputDO.setParamType("Number");
                        break;
                    case Types.BIT:
                        sysAppInterfaceOutputDO.setParamType("Boolean");
                        break;
                }
                sysAppInterfaceOutputDOS.add(sysAppInterfaceOutputDO);
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


        return R.ok("SQL能够正确执行！").put("sysAppInterfaceOutputDOS", sysAppInterfaceOutputDOS);
    }
}

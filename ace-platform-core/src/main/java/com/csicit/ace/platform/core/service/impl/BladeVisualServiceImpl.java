package com.csicit.ace.platform.core.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringPool;
import com.csicit.ace.common.enums.AuditLogType;
import com.csicit.ace.common.enums.AuditLogTypeDO;
import com.csicit.ace.common.pojo.domain.BladeVisualDO;
import com.csicit.ace.common.pojo.domain.SysAppInterfaceDO;
import com.csicit.ace.common.pojo.domain.SysAppInterfaceInputDO;
import com.csicit.ace.common.pojo.domain.SysGroupDatasourceDO;
import com.csicit.ace.common.pojo.vo.BladeVusalConfigVO;
import com.csicit.ace.common.pojo.vo.SocketEventVO;
import com.csicit.ace.common.utils.MapUtils;
import com.csicit.ace.common.utils.StringUtils;
import com.csicit.ace.common.utils.UuidUtils;
import com.csicit.ace.common.utils.server.R;
import com.csicit.ace.data.persistent.mapper.BladeVisualMapper;
import com.csicit.ace.data.persistent.service.SysGroupDatasourceService;
import com.csicit.ace.data.persistent.service.SysMessageService;
import com.csicit.ace.data.persistent.service.impl.BaseServiceImpl;
import com.csicit.ace.platform.core.service.BladeVisualService;
import com.csicit.ace.platform.core.service.SysAppInterfaceInputService;
import com.csicit.ace.platform.core.service.SysAppInterfaceService;
import com.csicit.ace.platform.core.service.SysAuthService;
import com.csicit.ace.platform.core.utils.GenUtils;
import com.csicit.ace.platform.core.utils.JDBCUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;


/**
 * 大屏信息数据表 实例对象访问接口实现
 *
 * @author generator
 * @version V1.0
 * @date 2020-06-05 09:59:54
 */
@Service("bladeVisualService")
public class BladeVisualServiceImpl extends BaseServiceImpl<BladeVisualMapper, BladeVisualDO> implements
        BladeVisualService {

    @Autowired
    private SysAppInterfaceService sysAppInterfaceService;

    @Autowired
    private SysAppInterfaceInputService sysAppInterfaceInputService;

    @Autowired
    private SysGroupDatasourceService sysGroupDatasourceService;

    @Autowired
    private SysAuthService sysAuthService;

    @Autowired
    SysMessageService sysMessageService;

    /**
     * 获取单个
     *
     * @param sqlCode
     * @param queryString
     * @return
     * @author zuogang
     * @date 2020/6/5 10:38
     */
    @Override
    public R getSqlResult(String sqlCode, String queryString) {

        if (StringUtils.isBlank(queryString)) {
            return R.error("应用标识传参失败！");
        }
        Map<String, Object> urlParams = MapUtils.getUrlParams(queryString);
        SysAppInterfaceDO sysAppInterfaceDO = sysAppInterfaceService.getOne(new QueryWrapper<SysAppInterfaceDO>()
                .eq("code", sqlCode).eq("app_id", urlParams.get("appId")));
        if (sysAppInterfaceDO == null) {
            return R.error("接口标识不存在");
        }
        // 判断是否分页
        String currentPageStr = (String) urlParams.get("currentPage");
        String pageSizeStr = (String) urlParams.get("pageSize");

        if (Objects.equals(sysAppInterfaceDO.getPageGet(), 1)) {
            if (StringUtils.isBlank(currentPageStr) || StringUtils.isBlank(pageSizeStr)) {
                return R.error("该分页接口的当前页码传值未传值或页面显示条数未传值！");
            }
        }

        String sqlContent = sysAppInterfaceDO.getSqlContent();
        SysGroupDatasourceDO datasourceDO = sysGroupDatasourceService.getById(sysAppInterfaceDO.getDsId());

        List<SysAppInterfaceInputDO> sysAppInterfaceInputDOS = sysAppInterfaceInputService.list(new
                QueryWrapper<SysAppInterfaceInputDO>()
                .eq("interface_id", sysAppInterfaceDO.getId()));


        for (SysAppInterfaceInputDO intput : sysAppInterfaceInputDOS) {

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
        if (Objects.equals(sysAppInterfaceDO.getPageGet(), 1)) {

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
            if (datasourceDO.getDriverName().contains("oracle") || datasourceDO.getDriverName().contains("dm")) {

                StringBuilder sql = new StringBuilder();
                sql.append("SELECT * FROM ( SELECT TMP.*, ROWNUM ROW_ID FROM ( ");
                sql.append(sqlContent).append(" ) TMP WHERE ROWNUM <=").append((offset >= 1) ? (offset + pageSize) :
                        pageSize);
                sql.append(") WHERE ROW_ID > ").append(offset);
                sqlContent = sql.toString();
            } else if (datasourceDO.getDriverName().contains("mysql") || datasourceDO.getDriverName().contains
                    ("oscar")) {//
                // mysql数据库 || oscar数据库
                StringBuilder sql = new StringBuilder(sqlContent);
                sql.append(" LIMIT ").append(offset).append(StringPool.COMMA).append(pageSize);
                sqlContent = sql.toString();
            }
        }

        List<Map<String, Object>> result = new ArrayList<>(16);
        Connection conn = null;
        try {
            conn = JDBCUtil.getConnection(datasourceDO.getDriverName(), datasourceDO.getUrl(), datasourceDO
                            .getUsername(),
                    datasourceDO
                            .getPassword());
            PreparedStatement st = conn.prepareStatement(sqlContent);
            ResultSet rs = st.executeQuery();
            ResultSetMetaData md = rs.getMetaData();
            int columnCount = md.getColumnCount(); //Map rowData;
            while (rs.next()) {
                Map rowData = new HashMap();
                for (int i = 1; i <= columnCount; i++) {
                    rowData.put(StringUtils.uncapitalize(GenUtils.columnToJava(md.getColumnName(i))), rs.getObject(i));
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

    /**
     * 获取单个
     *
     * @param id
     * @return
     * @author zuogang
     * @date 2020/6/5 10:38
     */
    @Override
    public R getInfo(String id) {
        BladeVisualDO instance = getById(id);
        BladeVusalConfigVO bladeVusalConfigVO = new BladeVusalConfigVO();
        bladeVusalConfigVO.setVisualId(id);
        bladeVusalConfigVO.setComponent(instance.getComponent());
        bladeVusalConfigVO.setDetail(instance.getDetail());
        if (StringUtils.isNotBlank(instance.getAuthId())) {
            instance.setAuthName(sysAuthService.getById(instance.getAuthId()).getName());
        }
        return R.ok().put("visual", instance).put("config", bladeVusalConfigVO);
    }

    /**
     * 新增
     *
     * @param instance
     * @return
     * @author zuogang
     * @date 2020/6/5 10:38
     */
    @Override
    public boolean saveBladeVisual(BladeVisualDO instance) {
        instance.setCreateTime(LocalDateTime.now());
        instance.setCreateUser(securityUtils.getCurrentUserId());
        instance.setBeDeleted(0);
        if (!save(instance)) {
            return false;
        }
        return sysAuditLogService.saveLog(new AuditLogTypeDO(AuditLogType.success, "新增"), "保存大屏",
                "保存大屏："+ instance.getTitle(), securityUtils.getCurrentGroupId(), null);
    }

    /**
     * 修改
     *
     * @param instance
     * @return
     * @author zuogang
     * @date 2020/6/5 10:38
     */
    @Override
    public boolean updateBladeVisual(BladeVisualDO instance) {
        instance.setUpdateTime(LocalDateTime.now());
        instance.setUpdateUser(securityUtils.getCurrentUserId());
        if (!updateById(instance)) {
            return false;
        }
        return sysAuditLogService.saveLog(new AuditLogTypeDO(AuditLogType.warning, "更新"),
                "修改大屏", "修改大屏："+instance.getTitle(), securityUtils.getCurrentGroupId(), null);
    }

    /**
     * 删除
     *
     * @param id
     * @return
     * @author zuogang
     * @date 2020/6/5 10:38
     */
    @Override
    public boolean deleteBladeVisual(String id) {
        if (StringUtils.isBlank(id)) {
            return false;
        }
        BladeVisualDO instance = getById(id);

        instance.setBeDeleted(1);

        if (!updateById(instance)) {
            return false;
        }
        return sysAuditLogService.saveLog(new AuditLogTypeDO(AuditLogType.danger, "删除"), "删除大屏", "删除大屏："+instance.getTitle(),
                securityUtils.getCurrentGroupId(), null);
    }

    /**
     * 复制
     *
     * @param id
     * @return
     * @author zuogang
     * @date 2020/6/5 10:38
     */
    @Override
    public boolean copyBladeVisual(String id) {
        BladeVisualDO bladeVisualDO = getById(id);
        bladeVisualDO.setTitle(bladeVisualDO.getTitle() + "_" + System
                .currentTimeMillis());
        bladeVisualDO.setId(UuidUtils.createUUID());
        if (!save(bladeVisualDO)) {
            return false;
        }
        return true;
    }

    /**
     * 触发消息推送方法
     *
     * @param map
     * @return
     * @author zuogang
     * @date 2020/6/5 10:38
     */
    @Override
    public void bladeMessagePush(Map<String, Object> map) {
        LinkedHashMap data = (LinkedHashMap) map.get("data");
        String eventName = (String) map.get("eventName");
        Map<String, Object> map1 = new HashMap<>();

        map1.put("data", data);

        map1.put("eventName", eventName);
        sysMessageService.fireSocketEvent(new SocketEventVO(Arrays.asList(securityUtils.getCurrentUserId()),
                eventName, map1, appName));
    }
}

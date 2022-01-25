package com.csicit.ace.data.persistent.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.csicit.ace.common.pojo.domain.SysUserDO;
import com.csicit.ace.common.utils.DiscoveryUtils;
import com.csicit.ace.common.utils.HttpClient;
import com.csicit.ace.common.utils.system.SecurityUtils;
import com.csicit.ace.data.persistent.service.SysAuditLogService;
import com.csicit.ace.dbplus.service.IBaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.lang.Nullable;

import java.io.Serializable;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 用于涉密接口的二次封装
 * 涉及接口为原接口名称拼接"All",
 * 通过指定用户密级为条件查找<=用户密级的数据
 *
 * @author shanwj
 * @version V1.0
 * @date 2019-03-29 10:37:46
 */
public class BaseServiceImpl<M extends BaseMapper<T>, T> extends ServiceImpl<M, T> implements IBaseService<T> {

    /**
     * 安全工具类对象
     */
    @Autowired
    protected SecurityUtils securityUtils;

    @Autowired
    protected SysAuditLogService sysAuditLogService;

    /**
     * 应用名称
     */
    @Value("${spring.application.name}")
    protected String appName;

    @Nullable
    @Autowired
    protected DiscoveryUtils discoveryUtils;

    @Autowired
    protected HttpClient httpClient;

    @Override
    public Collection<T> listByMapAll(Map<String, Object> columnMap) throws Exception {
        SysUserDO sysUserDo = securityUtils.getCurrentUser();
        if (sysUserDo == null) {
            throw new Exception("当前用户session无法获取");
        }
        columnMap.put("secret_level", sysUserDo.getSecretLevel());
        return this.baseMapper.selectByMap(columnMap);
    }

    @Override
    public List<T> listAll(QueryWrapper<T> queryWrapper) throws Exception {
        queryWrapper = getQueryWrapper(queryWrapper);
        return this.baseMapper.selectList(queryWrapper);
    }

    @Override
    public IPage<T> pageAll(IPage<T> page, QueryWrapper<T> queryWrapper) throws Exception {
        queryWrapper = getQueryWrapper(queryWrapper);
        return this.baseMapper.selectPage(page, queryWrapper);
    }

    @Override
    public List<Map<String, Object>> listMapsAll(QueryWrapper<T> queryWrapper) throws Exception {
        queryWrapper = getQueryWrapper(queryWrapper);
        return this.baseMapper.selectMaps(queryWrapper);
    }

    @Override
    public List<Object> listObjsAll(QueryWrapper<T> queryWrapper) throws Exception {
        queryWrapper = getQueryWrapper(queryWrapper);
        return (List) this.baseMapper.selectObjs(queryWrapper).stream().filter(Objects::nonNull).collect(Collectors
                .toList());
    }

    @Override
    public IPage<Map<String, Object>> pageMapsAll(IPage<T> page, QueryWrapper<T> queryWrapper) throws Exception {
        queryWrapper = getQueryWrapper(queryWrapper);
        return this.baseMapper.selectMapsPage(page, queryWrapper);
    }

    /**
     * @param queryWrapper 查询对象
     * @return com.baomidou.mybatisplus.core.conditions.query.QueryWrapper<T> 带密级查询对象
     * @author shanwj
     * @date 2019/4/11 20:12
     */
    protected QueryWrapper<T> getQueryWrapper(QueryWrapper<T> queryWrapper) throws Exception {
        if (queryWrapper == null) {
            queryWrapper = new QueryWrapper<>();
        }
        SysUserDO sysUserDo = securityUtils.getCurrentUser();
        if (sysUserDo == null) {
            throw new Exception("当前用户session无法获取");
        }
        queryWrapper.le("secret_level", sysUserDo.getSecretLevel());
        return queryWrapper;
    }

    @Override
    public Collection<T> listByIds(Collection<? extends Serializable> idList) {
        if (idList != null && idList.size() > 0) {
            return super.listByIds(idList);
        } else {
            return Collections.EMPTY_LIST;
        }
    }
}
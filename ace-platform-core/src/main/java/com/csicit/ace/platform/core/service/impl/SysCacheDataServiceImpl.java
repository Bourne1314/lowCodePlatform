package com.csicit.ace.platform.core.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.csicit.ace.common.pojo.domain.SysCacheDataDO;
import com.csicit.ace.common.utils.CacheUtil;
import com.csicit.ace.common.utils.JsonUtils;
import com.csicit.ace.common.utils.StringUtils;
import com.csicit.ace.data.persistent.mapper.SysCacheDataMapper;
import com.csicit.ace.platform.core.service.SysCacheDataService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * @author FourLeaves
 * @version V1.0
 * @date 2020/11/16 15:01
 */
@Service
public class SysCacheDataServiceImpl extends ServiceImpl<SysCacheDataMapper, SysCacheDataDO> implements SysCacheDataService {
    @Override
    public String get(String id) {
        if (StringUtils.isNotBlank(id)) {
            SysCacheDataDO data = getById(id);
            return Objects.isNull(data) ? null : data.getValue();
        }
        return null;
    }

    @Override
    public <T> T get(String id, Class<T> clazz) {
        String value = get(id);
        if (StringUtils.isNotBlank(value)) {
            return JsonUtils.castObject(value, clazz);
        }
        return null;
    }

    @Override
    public boolean set(String id, Object value) {
        return set(id, value, CacheUtil.NOT_EXPIRE);
    }

    @Override
    public boolean set(String id, Object value, long seconds) {
        SysCacheDataDO data = new SysCacheDataDO();
        data.setId(id);
        data.setValue(JSONObject.toJSONString(value));
        if (seconds > 0) {
            LocalDateTime expireTime = LocalDateTime.now().plusSeconds(seconds);
            data.setExpireTime(expireTime);
        }
        return saveOrUpdate(data);
    }

    @Override
    public boolean setMapValue(String id, String key, Object value) {
        Map<String, Object> data = get(id, Map.class);
        if (Objects.isNull(data)) {
            data = new HashMap<>();
        }
        data.put(key, value);
        return set(id, data);
    }

    @Override
    public String getMapValue(String id, String key) {
        Map<String, String> data = get(id, Map.class);
        if (Objects.nonNull(data)) {
            return data.get(id);
        }
        return null;
    }

    @Override
    public <T> T getMapValue(String id, String key, Class<T> clazz) {
        String value = getMapValue(id, key);
        if (StringUtils.isNotBlank(value)) {
            return JsonUtils.castObject(value, clazz);
        }
        return null;
    }
}

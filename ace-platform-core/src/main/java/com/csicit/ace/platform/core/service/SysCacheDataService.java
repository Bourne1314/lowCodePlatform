package com.csicit.ace.platform.core.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.csicit.ace.common.pojo.domain.SysCacheDataDO;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

/**
 * @author FourLeaves
 * @version V1.0
 * @date 2020/11/16 15:01
 */
@Transactional
public interface SysCacheDataService extends IService<SysCacheDataDO> {

    /**
     * 取值
     * @param id
     * @return 
     * @author FourLeaves
     * @date 2020/11/16 15:04
     */
    String get(String id);

    /**
     * 取值
     * @param id
     * @param clazz
     * @return
     * @author FourLeaves
     * @date 2020/11/16 15:04
     */
    <T> T get(String id, Class<T> clazz);

    /**
     * 不过期地存值
     * @param id
     * @param value
     * @return 
     * @author FourLeaves
     * @date 2020/11/16 15:04
     */
    boolean set(String id, Object value);


    /**
     * 存Map的值
     * @param id
     * @param value
     * @return
     * @author FourLeaves
     * @date 2020/11/16 15:04
     */
    boolean setMapValue(String id, String key, Object value);

    /**
     * 取Map的值
     * @param id
     * @param key
     * @return
     * @author FourLeaves
     * @date 2020/11/16 15:04
     */
    String getMapValue(String id, String key);


    /**
     * 取Map的值
     * @param id
     * @param key
     * @return
     * @author FourLeaves
     * @date 2020/11/16 15:04
     */
    <T> T getMapValue(String id, String key, Class<T> clazz);

    /**
     * 设置过期时间的存值
     * @param id
     * @param value
     * @param seconds 过期时间 秒
     * @return 
     * @author FourLeaves
     * @date 2020/11/16 15:04
     */
    boolean set(String id, Object value, long seconds);
}

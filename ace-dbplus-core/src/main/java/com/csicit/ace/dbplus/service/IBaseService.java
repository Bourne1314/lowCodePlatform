package com.csicit.ace.dbplus.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;

import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * 用于涉密接口的二次封装
 *
 * @author shanwj
 * @date 2019-03-29 10:37:46
 * @version V1.0
 */
@ConditionalOnExpression("!'${spring.application.name}'.endsWith('gateway')")
public interface IBaseService<T> extends IService<T>{

    Collection<T> listByMapAll(Map<String, Object> var1) throws Exception;

    List<T> listAll(QueryWrapper<T> var1) throws Exception;

    IPage<T> pageAll(IPage<T> var1, QueryWrapper<T> var2) throws Exception;

    List<Map<String, Object>> listMapsAll(QueryWrapper<T> var1) throws Exception;

    List<Object> listObjsAll(QueryWrapper<T> var1) throws Exception;

    IPage<Map<String, Object>> pageMapsAll(IPage<T> var1, QueryWrapper<T> var2) throws Exception;
}

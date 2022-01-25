package com.csicit.ace.dbplus;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.csicit.ace.common.exception.RException;
import com.csicit.ace.common.pojo.vo.FilterInfoVO;
import com.csicit.ace.common.pojo.vo.FilterItemVO;
import com.csicit.ace.common.pojo.vo.ParamJsonVO;
import com.csicit.ace.common.utils.JsonUtils;
import com.csicit.ace.common.utils.StringUtils;
import com.csicit.ace.common.utils.internationalization.InternationUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 自定义mybatisplus Wrapper
 * 用户封装使用map参数查询集合
 *
 * @author shanwj
 * @date 2019/4/17 10:54
 */
public class MapWrapper<T> extends QueryWrapper<T> {
    private static final String CURRENT = "current";
    private static final String BEGIN_TIME = "beginTime";
    private static final String END_TIME = "endTime";
    private static final String SIZE = "size";
    private static final String T_STR = "aceTimestamp";

    private static final String COMMON_DATE = "yyyy-MM-dd HH:mm:ss";

//    private static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public static Date formDate(String date) {
        SimpleDateFormat sdf = new SimpleDateFormat(COMMON_DATE);
        if (StringUtils.isNotBlank(date)) {
            try {
                return sdf.parse(date);
            } catch (ParseException e) {
                e.printStackTrace();
                throw new RException(InternationUtils.getInternationalMsg("TIME_FORMAT_EXCEPTION"));
            }
        }
        return null;
    }


    /**
     * 通过相等去查找数据
     *
     * @param map 参数map
     * @return MapWrapper
     * @author shanwj
     * @date 2019/4/17 14:31
     */
    public static MapWrapper getInstanceByParamJson(Map<String, Object> map) {
        if (map.containsKey(T_STR)) {
            map.remove(T_STR);
        }

        MapWrapper mapWrapper = new MapWrapper();
        String paramJson = JSON.toJSONString(map.get("paramJson"));
        ParamJsonVO paramJsonVO = JsonUtils.castObject(net.sf.json.JSONObject.fromObject(paramJson), ParamJsonVO.class);
        // 模糊查询
//        mapWrapper.app
        String searchValue = paramJsonVO.getSearchValue();
        if (StringUtils.isNotBlank(paramJsonVO.getSearchField())
                && StringUtils.isNotBlank(searchValue)) {
            List<String> searchField = Arrays.asList(paramJsonVO.getSearchField().split(","));
            String[] fieldArr = new String[searchField.size()];
            StringBuffer stringBuffer = new StringBuffer();
            stringBuffer.append("( ");
            for (int t = 0; t < searchField.size(); t++) {
                stringBuffer.append(javaToColumn(searchField.get(t)) + " LIKE {" + t +"}");
                if (t != searchField.size() - 1) {
                    stringBuffer.append(" or ");
                }
                fieldArr[t] = "%" + searchValue + "%";
            }
            stringBuffer.append(" )");
            mapWrapper.apply(stringBuffer.toString(), fieldArr);
        }


        // 筛选值
        List<FilterInfoVO> filterInfo = paramJsonVO.getFilterInfo();
        if (filterInfo != null && filterInfo.size() > 0) {
            filterInfo.stream().forEach(info -> {
                List<FilterItemVO> dynamicItem = info.getDynamicItem();
                String type = info.getType();
                String field = javaToColumn(info.getField());

                dynamicItem.stream().forEach(dynamic -> {
                    String mode = dynamic.getMode();
                    Object value = dynamic.getValue();
                    if (Objects.equals("num", type) || Objects.equals("date", type)) {
                        // 字段为num型或者date型
                        setMapWrapperForNumOrDate(mapWrapper, field, mode, value);
                    } else if (Objects.equals("string", type)) {
                        // 字段为string型
                        setMapWrapperForString(mapWrapper, field, mode, value);
                    } else if (Objects.equals("boolean", type) || Objects.equals("selectOption", type)) {
                        // 字段为boolean型 或者选择项类型
                        setMapWrapperForBooleanOrOption(mapWrapper, field, mode, value);
                    }
                });

            });
        }


        // 排序
        if (Objects.equals("asc", paramJsonVO.getOrderWay())) {
            mapWrapper.orderByAsc(javaToColumn(paramJsonVO.getOrderField()));
        } else if (Objects.equals("desc", paramJsonVO.getOrderWay())) {
            mapWrapper.orderByDesc(javaToColumn(paramJsonVO.getOrderField()));
        }

        return mapWrapper;
    }

    /**
     * 为boolean型或者选择项类型的字段设置mapWrapper
     * boolean型 0否1是
     *
     * @param
     * @return void
     * @author zuogang
     * @date 2020/4/10 9:59
     */
    public static void setMapWrapperForBooleanOrOption(MapWrapper mapWrapper, String field, String mode, Object value) {
        if (Objects.equals("eq", mode)) { // 等于=
            mapWrapper.eq(field, value);
        } else if (Objects.equals("ne", mode)) { // 不等于<>
            mapWrapper.ne(field, value);
        } else if (Objects.equals("isNull", mode)) {// 为空
            mapWrapper.isNull(field);
        } else if (Objects.equals("isNotNull", mode)) {// 不为空
            mapWrapper.isNotNull(field);
        }
    }

    /**
     * 为string类型的字段设置mapWrapper
     *
     * @param
     * @return void
     * @author zuogang
     * @date 2020/4/10 9:59
     */
    public static void setMapWrapperForString(MapWrapper mapWrapper, String field, String mode, Object value) {
        if (Objects.equals("eq", mode)) { // 等于=
            mapWrapper.eq(field, value);
        } else if (Objects.equals("ne", mode)) { // 不等于<>
            mapWrapper.ne(field, value);
        } else if (Objects.equals("likeRight", mode)) { // 开头是
            mapWrapper.likeRight(field, value);
        } else if (Objects.equals("likeLeft", mode)) { // 结尾是
            mapWrapper.likeLeft(field, value);
        } else if (Objects.equals("like", mode)) { // 包含
            mapWrapper.like(field, value);
        } else if (Objects.equals("notLike", mode)) { // 不包含
            mapWrapper.notLike(field, value);
        } else if (Objects.equals("isNull", mode)) {// 为空
            mapWrapper.isNull(field);
        } else if (Objects.equals("isNotNull", mode)) {// 不为空
            mapWrapper.isNotNull(field);
        }
    }

    /**
     * 为num/date类型的字段设置mapWrapper
     *
     * @param
     * @return void
     * @author zuogang
     * @date 2020/4/10 9:59
     */
    public static void setMapWrapperForNumOrDate(MapWrapper mapWrapper, String field, String mode, Object value) {

        if (Objects.equals("eq", mode)) { // 等于=
            mapWrapper.eq(field, value);
        } else if (Objects.equals("ne", mode)) { // 不等于<>
            mapWrapper.ne(field, value);
        } else if (Objects.equals("gt", mode)) {// 大于>
            mapWrapper.gt(field, value);
        } else if (Objects.equals("ge", mode)) { // 大于等于>=
            mapWrapper.ge(field, value);
        } else if (Objects.equals("lt", mode)) {// 小于<
            mapWrapper.lt(field, value);
        } else if (Objects.equals("le", mode)) {// 小于等于<=
            mapWrapper.le(field, value);
        } else if (Objects.equals("isNull", mode)) {// 为空
            mapWrapper.isNull(field);
        } else if (Objects.equals("isNotNull", mode)) {// 不为空
            mapWrapper.isNotNull(field);
        }
    }


    /**
     * Java属性名转换成列名 例：sortIndex=>sort_index
     *
     * @param input
     * @return
     * @author zuogang
     * @date 2020/4/10 9:59
     */
    public static String javaToColumn(String input) {
        String output = "";
        char[] array = input.toCharArray();
        for (int i = 0; i < array.length; i++) {
            if (array[i] >= 'A' && array[i] <= 'Z') {
                output += "_" + array[i];
            } else {
                output += array[i];
            }
        }
        return output;
    }

    /**
     * 通过相等去查找数据
     *
     * @param map 参数map
     * @return MapWrapper
     * @author shanwj
     * @date 2019/4/17 14:31
     */
    public static MapWrapper getEqualInstance(Map<String, Object> map) {
        if (map.containsKey(T_STR)) {
            map.remove(T_STR);
        }
        MapWrapper mapWrapper = new MapWrapper();
        map.entrySet().forEach(entry -> {
            if (!Objects.equals(entry.getKey(), CURRENT) && !Objects.equals(entry.getKey(), SIZE)) {
                mapWrapper.eq(entry.getKey(), entry.getValue());
            }
        });
        return mapWrapper;
    }

    /**
     * 通过相等去查找数据
     *
     * @param map 参数map
     * @return MapWrapper
     * @author shanwj
     * @date 2019/4/17 14:31
     */
    public static MapWrapper getEqualInstance(Map<String, Object> map, String sort, boolean isAsc) {
        MapWrapper mapWrapper = new MapWrapper();
        if (map.containsKey(T_STR)) {
            map.remove(T_STR);
        }
        map.entrySet().forEach(entry -> {
            if (!Objects.equals(entry.getKey(), CURRENT) && !Objects.equals(entry.getKey(), SIZE)) {
                mapWrapper.eq(entry.getKey(), entry.getValue());
            }
        });
        if (isAsc) {
            mapWrapper.orderByAsc(sort);
        } else {
            mapWrapper.orderByDesc(sort);
        }
        return mapWrapper;
    }

    /**
     * 通过相似去查找数据
     *
     * @param map 参数map
     * @return MapWrapper
     * @author shanwj
     * @date 2019/4/17 14:32
     */
    public static MapWrapper getLikeInstance(Map<String, Object> map) {
        if (map.containsKey(T_STR)) {
            map.remove(T_STR);
        }
        MapWrapper mapWrapper = new MapWrapper();
        map.entrySet().forEach(entry -> {
            if (!Objects.equals(entry.getKey(), CURRENT) && !Objects.equals(entry.getKey(), SIZE))
                mapWrapper.like(entry.getKey(), entry.getValue());
        });
        return mapWrapper;
    }

    /**
     * 通过相似去查找数据
     *
     * @param equalMap 相等参数map
     * @param likeMap  相似参数map
     * @return MapWrapper
     * @author shanwj
     * @date 2019/4/17 14:32
     */
    public static MapWrapper getEqualAndLikeInstance(Map<String, Object> equalMap, Map<String, Object> likeMap) {
        MapWrapper mapWrapper = new MapWrapper();
        if (equalMap.containsKey(T_STR)) {
            equalMap.remove(T_STR);
        }
        if (likeMap.containsKey(T_STR)) {
            likeMap.remove(T_STR);
        }
        equalMap.entrySet().forEach(entry -> {
            if (!Objects.equals(entry.getKey(), CURRENT) && !Objects.equals(entry.getKey(), SIZE))
                mapWrapper.eq(entry.getKey(), entry.getValue());
        });
        likeMap.entrySet().forEach(entry -> {
            if (!Objects.equals(entry.getKey(), CURRENT) && !Objects.equals(entry.getKey(), SIZE))
                mapWrapper.like(entry.getKey(), entry.getValue());
        });
        return mapWrapper;
    }

    /**
     * 通过 多个相似的并集 去查找数据
     * 类似  and (t like t1 or p like p1)
     *
     * @param map 参数map
     * @return MapWrapper
     * @author shanwj
     * @date 2019/4/17 14:32
     */
    public <T> MapWrapper getOrLikeInstance(Map<String, Object> map) {
        MapWrapper<T> mapWrapper = new MapWrapper<>();
        if (map.containsKey(T_STR)) {
            map.remove(T_STR);
        }
        map.entrySet().forEach(entry -> {
            if (entry.getValue() != null) {
                if (!Objects.equals(entry.getKey(), CURRENT) && !Objects.equals(entry.getKey(), SIZE) && !Objects
                        .equals(entry.getKey(), BEGIN_TIME) && !Objects.equals(entry.getKey(), END_TIME)) {
                    mapWrapper.or().like(entry.getKey().toUpperCase(), entry.getValue());
                }
            }
        });
        return mapWrapper;
    }

    /**
     * 通过 多个相似的并集 去查找数据
     * 在一定的时间范围内
     * 类似  and (t like t1 or p like p1)
     *
     * @param map 参数map
     * @return MapWrapper
     * @author shanwj
     * @date 2019/4/17 14:32
     */
    public <T> MapWrapper getOrLikeInstanceBetweenTime(Map<String, Object> map, String timeColumn) {
        MapWrapper<T> mapWrapper = new MapWrapper<>();
        if (map.containsKey(T_STR)) {
            map.remove(T_STR);
        }
        if (map.containsKey(BEGIN_TIME)) {
            String beginTime = (String) map.get(BEGIN_TIME);
            mapWrapper.ge(StringUtils.isNotBlank(beginTime), timeColumn, formDate(beginTime));
        }
        if (map.containsKey(END_TIME)) {
            String endTime = (String) map.get(END_TIME);
            mapWrapper.le(StringUtils.isNotBlank(endTime), timeColumn, formDate(endTime));
        }
        mapWrapper.and(i -> {
            map.entrySet().forEach(entry -> {
                if (entry.getValue() != null) {
                    if (!Objects.equals(entry.getKey(), CURRENT) && !Objects.equals(entry.getKey(), SIZE) && !Objects
                            .equals(entry.getKey(), BEGIN_TIME) && !Objects.equals(entry.getKey(), END_TIME))
                        i.or().like(entry.getKey().toUpperCase(), entry.getValue());

                }
            });
            return i;
        });
        return mapWrapper;
    }

    /**
     * 通过 多个相似的并集 去查找数据
     * 在一定的时间范围内
     * 类似  and (t like t1 or p like p1)
     *
     * @param map 参数map
     * @return MapWrapper
     * @author shanwj
     * @date 2019/4/17 14:32
     */
    public <T> MapWrapper getOrLikeInstanceAndOrderBetweenTime(Map<String, Object> map, String orderColumn, boolean
            asc, String timeColumn) {
        MapWrapper<T> mapWrapper = new MapWrapper().getOrLikeInstanceBetweenTime(map, timeColumn);
        if (map.containsKey(T_STR)) {
            map.remove(T_STR);
        }
        if (asc) {
            mapWrapper.orderByAsc(orderColumn);
        } else {
            mapWrapper.orderByDesc(orderColumn);
        }
        return mapWrapper;
    }

    /**
     * 通过 多个相似的并集 去查找数据
     * 类似  and (t like t1 or p like p1)
     *
     * @param map 参数map
     * @return MapWrapper
     * @author shanwj
     * @date 2019/4/17 14:32
     */
    public <T> MapWrapper getOrLikeInstanceAndOrder(Map<String, Object> map, String orderColumn, boolean asc) {
        MapWrapper<T> mapWrapper = new MapWrapper().getOrLikeInstance(map);
        if (map.containsKey(T_STR)) {
            map.remove(T_STR);
        }
        if (asc) {
            mapWrapper.orderByAsc(orderColumn);
        } else {
            mapWrapper.orderByDesc(orderColumn);
        }
        return mapWrapper;
    }
}

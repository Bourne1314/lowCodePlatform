package com.csicit.ace.bpm.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.csicit.ace.bpm.pojo.domain.WfdFlowDO;
import com.csicit.ace.bpm.pojo.vo.NewJobFlowVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

/**
 * 流程定义 数据处理层
 *
 * @author generator
 * @version V1.0
 * @date 2019-08-16 10:13:03
 */
@Mapper
public interface WfdFlowMapper extends BaseMapper<WfdFlowDO> {
    /**
     * 锁定流水
     *
     * @param id id
     * @author JonnyJiang
     * @date 2019/10/11 16:42
     */

    @Update("UPDATE WFD_FLOW SET SEQ_NO=SEQ_NO+1 WHERE ID=#{id}")
    void lockSeq(@Param("id") String id);

    @Select("SELECT COUNT(TABLE1.NAME_) AS COUNT,TABLE1.NAME_ AS CODE,MAX(TABLE1.START_TIME_) AS LATELY_USER_TIME FROM ( SELECT ACT_HI_PROCINST.START_TIME_ ,ACT_RE_PROCDEF.NAME_ FROM ACT_HI_PROCINST INNER JOIN ACT_RE_PROCDEF ON ACT_RE_PROCDEF.ID_ = ACT_HI_PROCINST.PROC_DEF_ID_ WHERE ACT_HI_PROCINST.START_USER_ID_='' AND ACT_HI_PROCINST.TENANT_ID_ = '' ORDER BY ACT_HI_PROCINST.START_TIME_ DESC ) TABLE1 GROUP BY TABLE1.NAME_")
    List<NewJobFlowVO> initFlowList(@Param("userId") String userId,@Param("appId") String appId);
}

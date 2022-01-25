package com.csicit.ace.data.persistent.mapper;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.csicit.ace.common.pojo.domain.FileRepositoryDO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;

/**
 * @author JonnyJiang
 * @date 2019/5/21 20:13
 */
@DS("ace")
@Mapper
public interface FileRepositoryMapper extends BaseMapper<FileRepositoryDO> {
    /**
     * 更新占用空间
     *
     * @param id
     * @param size
     * @return java.lang.Integer
     * @author JonnyJiang
     * @date 2019/5/22 18:54
     */
    @Update("update FILE_REPOSITORY set USED_SIZE=(case when USED_SIZE is null then 0 else USED_SIZE end)+#{size} where id=#{id} and (case when MAX_SIZE is null then 0 else MAX_SIZE end) >= (case when USED_SIZE is null then 0 else USED_SIZE end)+#{size}")
    Integer updateUsedSizeById(@Param("id") String id, @Param("size") Long size);
}

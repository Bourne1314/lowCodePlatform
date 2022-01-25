package com.csicit.ace.data.persistent.mapper;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.csicit.ace.common.pojo.domain.FileInfoDO;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * @author JonnyJiang
 * @date 2019/5/21 20:13
 */
@DS("ace")
@Mapper
public interface FileInfoMapper extends BaseMapper<FileInfoDO> {
    /**
     * 删除无效文件
     *
     * @param configurationId 附件配置主键
     * @param formId          要清理无效文件的FormID
     * @author JonnyJiang
     * @date 2019/5/22 19:13
     */

    @Delete("DELETE FROM FILE_INFO A1 WHERE (A1.SHARED_FILE_INFO_ID IS NULL AND (case when A1.CHUNKS is null then 0 else A1.CHUNKS end)>(SELECT (case when COUNT(1) is null then 0 else COUNT(1) end) FROM FILE_CHUNK_INFO WHERE FILE_INFO_ID=A1.ID)) OR (A1.SHARED_FILE_INFO_ID IS NOT NULL AND (case when A1.CHUNKS is null then 0 else A1.CHUNKS end)>(SELECT (case when COUNT(1) is null then 0 else COUNT(1) end) FROM FILE_CHUNK_INFO WHERE FILE_INFO_ID=A1.SHARED_FILE_INFO_ID)) AND A1.FORM_ID=#{formId} AND A1.FILE_CONFIGURATION_ID=#{configurationId}")
    void deleteInvalidFilesByFormId(@Param("configurationId") String configurationId, @Param("formId") String formId);

    /**
     * 删除无效文件
     *
     * @param formId 要清理无效文件的FormID
     * @author JonnyJiang
     * @date 2019/12/23 11:56
     */

    @Delete("DELETE FROM FILE_INFO A1 WHERE (A1.SHARED_FILE_INFO_ID IS NULL AND (case when A1.CHUNKS is null then 0 else A1.CHUNKS end)>(SELECT (case when COUNT(1) is null then 0 else COUNT(1) end) FROM FILE_CHUNK_INFO WHERE FILE_INFO_ID=A1.ID)) OR (A1.SHARED_FILE_INFO_ID IS NOT NULL AND (case when A1.CHUNKS is null then 0 else A1.CHUNKS end)>(SELECT (case when COUNT(1) is null then 0 else COUNT(1) end) FROM FILE_CHUNK_INFO WHERE FILE_INFO_ID=A1.SHARED_FILE_INFO_ID)) AND A1.FORM_ID=#{formId}")
    void deleteAllInvalidFilesByFormId(String formId);
}

package com.csicit.ace.data.persistent.service;

import com.csicit.ace.common.AppUpgradeJaxb.CodeSeqDetail;
import com.csicit.ace.common.pojo.domain.CodeSequenceDO;
import com.csicit.ace.common.utils.server.R;
import com.csicit.ace.dbplus.service.IBaseService;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * @author shanwj
 * @date 2020/5/22 15:16
 */
@Transactional
public interface CodeSequenceService extends IBaseService<CodeSequenceDO> {

    ConcurrentMap<String, Object> lockMapCache = new ConcurrentHashMap<>(16);
    ConcurrentMap<String, CodeSequenceDO> sequenceMapCache = new ConcurrentHashMap<>(16);

    /**
     * 保存
     *
     * @param instance
     * @return
     */
    R saveCodeSequence(CodeSequenceDO instance);

    /**
     * 更新
     *
     * @param instance
     * @return
     */
    R updateCodeSequence(CodeSequenceDO instance);

    /**
     * 根据条件查询序列对象
     *
     * @param appId
     * @param bigTag
     * @param partValueTag
     * @return
     */
    CodeSequenceDO getCodeSequence(String appId, String bigTag, String partValueTag);


    /**
     * 获取重置序列
     *
     * @param sequence
     * @return
     */
    CodeSequenceDO getRestCodeSequence(CodeSequenceDO sequence);


    /**
     * 获取下个序列号段对象
     *
     * @param appId
     * @param bigTag
     * @return
     */
    CodeSequenceDO getNextMaxNum(String appId, String bigTag, String partValueTag);

    /**
     * 获取数字序列号
     *
     * @param appId
     * @param bigTag
     * @param partValueTag
     * @return
     */
    String getNextNum(String appId, String bigTag, String partValueTag);

    /**
     * 应用升级时，数字序列更新
     *
     * @param codeSeqDetails
     * @param appId
     * @return boolean
     * @author zuogang
     * @date 2020/8/12 10:18
     */
    boolean codeSeqUpdate(List<CodeSeqDetail> codeSeqDetails, String appId);
}

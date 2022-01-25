package com.csicit.ace.bpm.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.csicit.ace.bpm.exception.TaskCommentDeleteException;
import com.csicit.ace.bpm.mapper.WfiCommentMapper;
import com.csicit.ace.bpm.pojo.domain.WfiCommentDO;
import com.csicit.ace.bpm.service.WfiCommentService;
import com.csicit.ace.common.utils.StringUtils;
import com.csicit.ace.dbplus.service.impl.BaseServiceImpl;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author JonnyJiang
 * @date 2020/5/25 19:12
 */
@Service
public class WfiCommentServiceImpl extends BaseServiceImpl<WfiCommentMapper, WfiCommentDO> implements WfiCommentService {
    @Override
    public void delete(String[] ids) {
        List<String> deletingIds = new ArrayList<>();
        resolveDeletingIds(deletingIds, listByIds(Arrays.asList(ids)));
        removeByIds(deletingIds);
    }

    private void resolveDeletingIds(List<String> deletingIds, Collection<WfiCommentDO> wfiComments) {
        for (WfiCommentDO wfiComment : wfiComments) {
            if (StringUtils.isNotEmpty(wfiComment.getTaskId())) {
                throw new TaskCommentDeleteException(wfiComment.getId());
            }
            deletingIds.add(wfiComment.getId());
        }
        if (wfiComments.size() > 0) {
            List<String> replyCommentIds = wfiComments.stream().map(WfiCommentDO::getId).collect(Collectors.toList());
            List<WfiCommentDO> replyComments = list(new QueryWrapper<WfiCommentDO>().eq("APP_ID", appName).in("REPLY_COMMENT_ID", replyCommentIds));
            if (replyComments.size() > 0) {
                resolveDeletingIds(deletingIds, replyComments);
            }
        }
    }
}

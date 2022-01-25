package com.csicit.ace.orgauth.core.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.csicit.ace.common.constant.Constants;
import com.csicit.ace.common.exception.RException;
import com.csicit.ace.common.pojo.domain.BladeVisualMsgDO;
import com.csicit.ace.common.pojo.domain.SysAuthScopeAppDO;
import com.csicit.ace.common.pojo.vo.SocketEventVO;
import com.csicit.ace.data.persistent.mapper.BladeVisualMsgMapper;
import com.csicit.ace.data.persistent.service.SysAuthScopeAppServiceD;
import com.csicit.ace.data.persistent.service.SysMessageService;
import com.csicit.ace.data.persistent.service.impl.BaseServiceImpl;
import com.csicit.ace.orgauth.core.service.BladeVisualMsgService;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


/**
 * 大屏消息 实例对象访问接口实现
 *
 * @author generator
 * @version V1.0
 * @date 2020-07-29 16:49:49
 */
@Service("bladeVisualMsgServiceO")
public class BladeVisualMsgServiceImpl extends BaseServiceImpl<BladeVisualMsgMapper, BladeVisualMsgDO> implements
        BladeVisualMsgService {

    @Autowired
    SysMessageService sysMessageService;

    @Autowired
    SysAuthScopeAppServiceD sysAuthScopeAppServiceD;

    /**
     * 大屏消息推送
     *
     * @param displayContent 显示内容
     * @param code           大屏通知标识
     * @param appName        应用标识
     * @return
     * @author zuog
     */
    @Override
    public void bladeVisualMsgPush(String displayContent, String code, String appName) {
        BladeVisualMsgDO bladeVisualMsgDO = getOne(new QueryWrapper<BladeVisualMsgDO>()
                .eq("app_id", appName).eq("code", code));
        if (bladeVisualMsgDO == null) {
            throw new RException("当前大屏消息数据不存在！");
        }

        // 获取拥有该应用授控域的应用管理员ID集合，作为接受人ID集合
        List<String> userIds = sysAuthScopeAppServiceD.list(new QueryWrapper<SysAuthScopeAppDO>()
                .eq("app_id", appName).eq("IS_ACTIVATED", 1).eq("ROLE_TYPE", 111))
                .stream().map(SysAuthScopeAppDO::getUserId).collect(Collectors.toList());
        if (CollectionUtils.isEmpty(userIds)) {
            throw new RException("当前应用未分配管理员，消息推送无接收人！");
        }

        bladeVisualMsgDO.setDisplayContent(displayContent);
        Map<String, Object> map1 = new HashMap<>();
        map1.put("eventName", Constants.BLADEVISUALMSGPUSH);
        map1.put("data", bladeVisualMsgDO);
        sysMessageService.fireSocketEvent(new SocketEventVO(userIds,
                Constants.BLADEVISUALMSGPUSH, map1, Constants.PLATFORM));
    }

}

package com.csicit.ace.data.persistent.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.csicit.ace.common.AppUpgradeJaxb.MsgTypeExtendDetail;
import com.csicit.ace.common.pojo.domain.SysMsgSendTypeDO;
import com.csicit.ace.common.pojo.domain.SysMsgTemplateDO;
import com.csicit.ace.common.pojo.domain.SysMsgTypeExtendDO;
import com.csicit.ace.common.utils.JsonUtils;
import com.csicit.ace.data.persistent.mapper.SysMsgTypeExtendMapper;
import com.csicit.ace.data.persistent.service.SysMsgSendTypeService;
import com.csicit.ace.data.persistent.service.SysMsgTypeExtendService;
import com.csicit.ace.data.persistent.service.impl.BaseServiceImpl;
import com.csicit.ace.data.persistent.utils.CommonUtils;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author shanwj
 * @date 2019/9/5 11:15
 */
@Service
public class SysMsgTypeExtendServiceImpl extends BaseServiceImpl<SysMsgTypeExtendMapper, SysMsgTypeExtendDO>
        implements SysMsgTypeExtendService {

    @Autowired
    SysMsgSendTypeService sysMsgSendTypeService;

    @Override
    public boolean deleteByIds(Collection<? extends Serializable> idList) {
        idList.stream().forEach(id -> {
            SysMsgTypeExtendDO extendDO = getById(id);
            String name = extendDO.getName();
            int count =
                    sysMsgSendTypeService.count(new QueryWrapper<SysMsgSendTypeDO>().like("send_mode", name));
            if (count == 0) {
                removeById(id);
            }
        });
        return true;
    }

    @Override
    public boolean saveMsgExtends(List<SysMsgTypeExtendDO> msgExtends, String appId) {
        //数据库存放的消息拓展方式
        List<SysMsgTypeExtendDO> olds =
                list(new QueryWrapper<SysMsgTypeExtendDO>().eq("app_id", appId));
        //获取数据库中有，扫描数据中没有的拓展方式，需要进行删除处理
        List<SysMsgTypeExtendDO> deletes =
                olds.stream().filter(
                        item -> !msgExtends.stream().map(e ->
                                e.getName())
                                .collect(Collectors.toList())
                                .contains(item.getName())
                ).collect(Collectors.toList());
        //获取扫描数据中有，数据库中没有的拓展方式，需要进行新增处理
        List<SysMsgTypeExtendDO> adds =
                msgExtends.stream().filter(
                        item -> !olds.stream().map(e ->
                                e.getName())
                                .collect(Collectors.toList())
                                .contains(item.getName())
                ).collect(Collectors.toList());
        //保存新增的消息拓展方式
        if (CollectionUtils.isEmpty(adds)) {
            return true;
        }
        if (saveBatch(adds)) {
            //处理被拿掉的消息拓展方式
            deletes.forEach(msg -> {
                //查找启用当前消息拓展方式的消息通道
                List<SysMsgSendTypeDO> sendTypes =
                        sysMsgSendTypeService.list(
                                new QueryWrapper<SysMsgSendTypeDO>()
                                        .eq("app_id", msg.getAppId())
                                        .like("send_mode", msg.getName()));
                String name = msg.getName();
                removeById(msg.getId());
                if (CollectionUtils.isEmpty(sendTypes)) {
                    return;
                }
                sendTypes.forEach(send -> {
                    //修改消息通道中的发送方式，拿掉删除的拓展方式
                    String sendMode = send.getSendMode();
                    if (sendMode.startsWith(name)) {
                        sendMode = sendMode.replace(name, "");
                    } else {
                        sendMode = sendMode.replace("," + name, "");
                    }
                    send.setSendMode(sendMode);
                    sysMsgSendTypeService.updateById(send);
                });
            });
            return true;
        }
        return false;
    }

    /**
     * 应用升级时，消息拓展更新
     *
     * @param msgTypeExtendDetails
     * @param appId
     * @return boolean
     * @author zuogang
     * @date 2020/8/11 14:39
     */
    @Override
    public boolean msgTypeExtendUpdate(List<MsgTypeExtendDetail> msgTypeExtendDetails, String appId) {
        List<SysMsgTypeExtendDO> add = new ArrayList<>(16);
        List<SysMsgTypeExtendDO> upd = new ArrayList<>(16);

        msgTypeExtendDetails.stream().forEach(msgSendTypeDetail -> {
            SysMsgTypeExtendDO item = JsonUtils.castObject(msgSendTypeDetail, SysMsgTypeExtendDO.class);
            // 判断当前业务数据库中是否存在该条数据
            SysMsgTypeExtendDO sysMsgTypeExtendDO = getOne(new QueryWrapper<SysMsgTypeExtendDO>()
                    .eq("trace_id", item.getTraceId()).eq("app_id", appId));
            if (sysMsgTypeExtendDO == null) {
                add.add(item);
            } else {
                // 判断业务数据库数据与应用更新配置文件数据是否一致
                if (CommonUtils.compareFields(sysMsgTypeExtendDO, item)) {
                    item.setId(sysMsgTypeExtendDO.getId());
                    upd.add(item);
                }
            }
        });

        if (CollectionUtils.isNotEmpty(add)) {
            if (!saveBatch(add)) {
                return false;
            }

        }
        if (CollectionUtils.isNotEmpty(upd)) {
            if (!updateBatchById(upd)) {
                return false;
            }
        }
        return true;
    }

}

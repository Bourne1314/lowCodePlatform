package com.csicit.ace.data.persistent.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.csicit.ace.common.constant.Constants;
import com.csicit.ace.common.pojo.domain.SysMicroAppDO;
import com.csicit.ace.common.pojo.vo.KeyValueVO;
import com.csicit.ace.common.utils.internationalization.InternationUtils;
import com.csicit.ace.common.utils.server.R;
import com.csicit.ace.data.persistent.mapper.SysMicroAppMapper;
import com.csicit.ace.data.persistent.service.SysMicroAppService;
import com.csicit.ace.data.persistent.service.impl.BaseServiceImpl;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * @author shanwj
 * @date 2020/4/7 10:10
 */
@Service
public class SysMicroAppServiceImpl extends BaseServiceImpl<SysMicroAppMapper, SysMicroAppDO>
        implements SysMicroAppService {

    @Override
    public R saveSysMicroApp(SysMicroAppDO sysMicroAppDO) {
        int count = count(
                new QueryWrapper<SysMicroAppDO>()
                        .eq("app_id", sysMicroAppDO.getAppId()));
        if (count > 0) {
            return R.error("当前appId已存在");
        }
        if (save(sysMicroAppDO)) {
            return R.ok(InternationUtils.getInternationalMsg("SAVE_SUCCESS"));
        }
        return R.error(InternationUtils.getInternationalMsg("SAVE_FAILED"));
    }

    @Override
    public R updateSysMicroApp(SysMicroAppDO sysMicroAppDO) {
        SysMicroAppDO oldApp = getById(sysMicroAppDO.getId());
        if (!Objects.equals(oldApp.getAppId(), sysMicroAppDO.getAppId())) {
            int count =
                    count(
                            new QueryWrapper<SysMicroAppDO>()
                                    .eq("app_id", sysMicroAppDO.getAppId()));
            if (count > 0) {
                return R.error("当前小程序已存在");
            }
        }
        if (updateById(sysMicroAppDO)) {
            return R.ok(InternationUtils.getInternationalMsg("UPDATE_SUCCESS"));
        }
        return R.error(InternationUtils.getInternationalMsg("UPDATE_FAILED"));
    }

    @Override
    public List<KeyValueVO> getTypes() {
        List<KeyValueVO> keys = new ArrayList<>(16);
        KeyValueVO microApp = new KeyValueVO();
        microApp.setKey("微信小程序");
        microApp.setValue(Constants.WxSubscriptionMessenger);
        keys.add(microApp);
        KeyValueVO subApp = new KeyValueVO();
        microApp.setKey("微信公众号");
        microApp.setValue(Constants.WxSubscriptionMessenger);
        keys.add(subApp);
        return keys;
    }


}

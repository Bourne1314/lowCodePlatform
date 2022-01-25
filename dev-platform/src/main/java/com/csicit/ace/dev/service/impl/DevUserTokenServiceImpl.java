package com.csicit.ace.dev.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.csicit.ace.common.pojo.domain.dev.DevUserTokenDO;
import com.csicit.ace.common.utils.server.R;
import com.csicit.ace.data.persistent.mapper.DevUserTokenMapper;
import com.csicit.ace.data.persistent.service.impl.BaseServiceImpl;
import com.csicit.ace.dev.service.DevUserTokenService;
import com.csicit.ace.dev.shiro.TokenGenerator;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * 用户Token管理 实例对象访问接口实现
 *
 * @author zuog
 * @date 2019/11/25 11:12
 */
@Service("devUserTokenService")
public class DevUserTokenServiceImpl extends BaseServiceImpl<DevUserTokenMapper, DevUserTokenDO> implements
        DevUserTokenService {
    //12小时后过期
    private final static int EXPIRE = 3600 * 12;

    @Override
    public R createToken(String userId) {
        //生成一个token
        String token = TokenGenerator.generateValue();

        //当前时间
        Date now = new Date();
        //过期时间
        Date expireTime = new Date(now.getTime() + EXPIRE * 1000);

        //判断是否生成过token
        DevUserTokenDO tokenEntity = this.getOne(new QueryWrapper<DevUserTokenDO>().eq("user_id", userId));
        if (tokenEntity == null) {
            tokenEntity = new DevUserTokenDO();
            tokenEntity.setUserId(userId);
            tokenEntity.setToken(token);
            tokenEntity.setUpdateTime(now);
            tokenEntity.setExpireTime(expireTime);

            //保存token
            this.save(tokenEntity);
        } else {
            tokenEntity.setToken(token);
            tokenEntity.setUpdateTime(now);
            tokenEntity.setExpireTime(expireTime);

            //更新token
            this.updateById(tokenEntity);
        }

        R r = R.ok().put("token", token);

        return r;
    }

    @Override
    public void logout(String userId) {
        //生成一个token
        String token = TokenGenerator.generateValue();

        //修改token
        DevUserTokenDO tokenEntity = new DevUserTokenDO();
        tokenEntity.setUserId(userId);
        tokenEntity.setToken(token);
        this.updateById(tokenEntity);
    }
}

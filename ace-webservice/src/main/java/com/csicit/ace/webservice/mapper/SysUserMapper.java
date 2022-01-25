package com.csicit.ace.webservice.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.csicit.ace.common.pojo.domain.SysUserDO;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * 用户管理 数据处理层
 *
 * @author yansiyang
 * @version V1.0
 * @date 2019/3/29 11:32
 */
@Mapper
public interface SysUserMapper extends BaseMapper<SysUserDO> {

    /**
     * 将之前的密码插入密码修改历史表
     *
     * @param id     新生成的uuid
     * @param userId 用户ID
     * @return
     * @uthor yansiyang
     * @date 17:03 2019/4/4
     */
    @Insert(" insert into sys_user_password_history(id,user_id,password,create_time)\n" +
            "        select #{id},id,password,password_update_time from SYS_USER where id=#{userId}")
    Integer saveOldPassword(@Param("id") String id, @Param("userId") String userId);

}

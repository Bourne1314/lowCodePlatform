package com.csicit.ace.data.persistent.mapper;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.csicit.ace.common.pojo.domain.SysUserDO;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * 用户管理 数据处理层
 *
 * @author yansiyang
 * @version V1.0
 * @date 2019/3/29 11:32
 */
@DS("ace")
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

    /**
     * 通过部门主键获取用户
     * @param depId 部门主键
     * @return 
     * @author FourLeaves
     * @date 2020/12/31 9:21
     */
    @Select("select * from sys_user where person_doc_id in (select person_doc_id from bd_person_job where department_id=#{depId})")
    List<SysUserDO> listUsersByDepId(@Param("depId") String depId);

    /**
     * 通过部门主键获取用户
     * @param depId 部门主键
     * @return
     * @author FourLeaves
     * @date 2020/12/31 9:21
     */
    @Select("select id from sys_user where person_doc_id in (select person_doc_id from bd_person_job where department_id=#{depId})")
    List<String> listUserIdsByDepId(@Param("depId") String depId);

    /**
     * 通过部门主键列表获取用户
     * @param depIds 部门主键列表
     * @return
     * @author FourLeaves
     * @date 2020/12/31 9:21
     */
    @Select({"<script>", "select id from sys_user where person_doc_id in (select person_doc_id from bd_person_job where department_id in",
            "<foreach collection='depIds' item='id' open='(' separator=',' close=')'>",
            "#{id}",
            "</foreach>"
            , " )</script>"})
    List<String> listUserIdsByDepIds(@Param("depIds") List<String> depIds);

    /**
     * 通过部门主键列表获取用户
     * @param depIds 部门主键列表
     * @return
     * @author FourLeaves
     * @date 2020/12/31 9:21
     */
    @Select({"<script>", "select * from sys_user where person_doc_id in (select person_doc_id from bd_person_job where department_id in",
            "<foreach collection='depIds' item='id' open='(' separator=',' close=')'>",
            "#{id}",
            "</foreach>"
            , " )</script>"})
    List<SysUserDO> listUsersByDepIds(@Param("depIds") List<String> depIds);

}

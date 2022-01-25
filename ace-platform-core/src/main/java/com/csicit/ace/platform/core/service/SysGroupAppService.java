package com.csicit.ace.platform.core.service;

import com.csicit.ace.common.pojo.domain.SysGroupAppDO;
import com.csicit.ace.common.utils.server.R;
import com.csicit.ace.dbplus.service.IBaseService;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.ServletOutputStream;
import java.util.List;
import java.util.Map;

/**
 * 集团应用库管理 实例对象访问接口
 *
 * @author generator
 * @version V1.0
 * @date 2019-04-15 20:12:31
 */
@Transactional
public interface SysGroupAppService extends IBaseService<SysGroupAppDO> {

    /**
     * 产品库导入应用
     *
     * @param map
     * @author zuogang
     * @date 2019/5/14 11:24
     */
    boolean saveAppFromBdAppLib(Map<String, Object> map);

    /**
     * 获取应用管理员所管理的应用列表
     *
     * @return
     */
    List<SysGroupAppDO> listUserOrgApp();

    /**
     * 设置集团默认应用
     *
     * @param params
     * @return
     */
    boolean setMainApp(Map<String, String> params);


    /**
     * 获取有权限的应用列表
     *
     * @param groupId
     * @return
     */
    List<SysGroupAppDO> listAppHaveAuth(String groupId);

    /**
     * 获取有权限的应用列表
     *
     * @param groupId
     * @return
     */
    List<SysGroupAppDO> listAppNoAuth(String groupId);

    /**
     * 克隆应用
     *
     * @param appId
     * @param newAppId
     * @return
     */
    boolean cloneApp(String appId, String newAppId);

    /**
     * 保存应用
     *
     * @param instance
     * @return
     */
    R saveApp(SysGroupAppDO instance);

    /**
     * 修改应用
     *
     * @param instance
     * @return
     */
    boolean updateApp(SysGroupAppDO instance);

    /**
     * 删除应用
     *
     * @param ids
     * @return
     */
    boolean deleteApp(List<String> ids);

    /**
     * 应用配置信息升级导出
     *
     * @param appId
     * @return
     */
    void appUpgrade( String appId, ServletOutputStream outputStream);


    /**
     * 返回在线的app列表
     * @param list 所有的app列表
     * @return
     * @author FourLeaves
     * @date 2020/7/31 8:35
     */
    List<SysGroupAppDO> getOnlieList(List<SysGroupAppDO> list);


    /**
     * 返回使用ACE bpm工作流的app列表
     * @param list 所有的app列表
     * @return
     * @author FourLeaves
     * @date 2020/7/31 8:35
     */
    List<SysGroupAppDO> getAppListWithBpm(List<SysGroupAppDO> list);
}

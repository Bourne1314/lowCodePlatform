package com.csicit.ace.data.persistent.service;

import com.csicit.ace.common.pojo.domain.SysMicroAppDO;
import com.csicit.ace.common.pojo.vo.KeyValueVO;
import com.csicit.ace.common.utils.server.R;
import com.csicit.ace.dbplus.service.IBaseService;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 小程序配置 访问接口
 *
 * @author shanwj
 * @date 2020/4/7 9:39
 */
@Transactional
public interface SysMicroAppService extends IBaseService<SysMicroAppDO> {

    R saveSysMicroApp(SysMicroAppDO sysMicroAppDO);

    R updateSysMicroApp(SysMicroAppDO sysMicroAppDO);

    List<KeyValueVO> getTypes();
}

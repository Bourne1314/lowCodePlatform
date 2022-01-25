package com.csicit.ace.data.persistent.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.csicit.ace.common.pojo.domain.QrtzConfigDO;
import com.csicit.ace.common.pojo.vo.ScheduledVO;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author FourLeaves
 * @version V1.0
 * @date 2019/12/26 11:08
 */
@Transactional
public interface SysScheduledServiceD extends IService<QrtzConfigDO> {

    boolean saveScheduleds(List<ScheduledVO> scheduleds, String appId);
}

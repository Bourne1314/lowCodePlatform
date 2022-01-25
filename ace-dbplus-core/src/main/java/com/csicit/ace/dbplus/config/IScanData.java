package com.csicit.ace.dbplus.config;

import com.csicit.ace.common.AppUpgradeJaxb.AppUpgrade;
import com.csicit.ace.common.pojo.vo.InitStorageDataVO;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

/**
 * @author shanwj
 * @date 2020/4/14 16:27
 */
@Transactional
public interface IScanData {

    boolean saveInitScanData(InitStorageDataVO storageData);

    boolean lockApp(Map<String, String> map);

    boolean unLockApp(Map<String, String> map);

    boolean updateDbItem(AppUpgrade appUpgrade);

    boolean updAppVersion(Map<String, String> map);

}

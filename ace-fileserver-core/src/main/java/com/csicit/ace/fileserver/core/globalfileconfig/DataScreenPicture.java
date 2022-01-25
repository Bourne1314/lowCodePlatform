package com.csicit.ace.fileserver.core.globalfileconfig;

import com.csicit.ace.common.enums.GlobalFileConfiguration;
import com.csicit.ace.common.pojo.domain.SysUserDO;
import com.csicit.ace.common.utils.IntegerUtils;
import com.csicit.ace.fileserver.core.utils.ConfigurationType;
import org.apache.commons.lang3.ObjectUtils;

import java.io.File;

/**
 * 大屏图片
 *
 * @author JonnyJiang
 * @date 2020/6/12 11:23
 */
public class DataScreenPicture extends Normal {
    private static final String APP_ID = "platform";

    public DataScreenPicture(SysUserDO currentUser) {
        setId(GlobalFileConfiguration.DataScreenPicture.getId());
        setConfigurationKey(GlobalFileConfiguration.DataScreenPicture.getKey());
        setConfigurationType(ConfigurationType.Group.ordinal());
        setTableName("BLADE_VISUAL");
        setEnableEncrypt(IntegerUtils.FALSE_VALUE);
        setEnableSecretLevel(IntegerUtils.FALSE_VALUE);
        setEnablePreview(IntegerUtils.FALSE_VALUE);
        setEnableReview(IntegerUtils.FALSE_VALUE);
        // 如果是管理员，则可以编辑
        if (ObjectUtils.notEqual(currentUser.getUserType(), 1) && ObjectUtils.notEqual(currentUser.getUserType(), 2)) {
            setAllowUpload(IntegerUtils.FALSE_VALUE);
            setAllowDelete(IntegerUtils.FALSE_VALUE);
        } else {
            setAllowUpload(IntegerUtils.TRUE_VALUE);
            setAllowDelete(IntegerUtils.TRUE_VALUE);
        }
        setAccept("png,jpg,jpeg,gif");
        setEnableDownloadToken(IntegerUtils.FALSE_VALUE);
        setEnableImageCompress(IntegerUtils.FALSE_VALUE);
        setEnableEvtFileUploading(IntegerUtils.FALSE_VALUE);
        setEnableEvtFileUploaded(IntegerUtils.FALSE_VALUE);
        setEnableEvtFileDeleting(IntegerUtils.FALSE_VALUE);
        setEnableEvtFileDownloading(IntegerUtils.FALSE_VALUE);
        setEnableEvtFileDownloaded(IntegerUtils.FALSE_VALUE);
        setSubDirFormat("{yyyy}" + File.separator + "{MM}" + File.separator + "{dd}");
        setAppId(APP_ID);
    }
}

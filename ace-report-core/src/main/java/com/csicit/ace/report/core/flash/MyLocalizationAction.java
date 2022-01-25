package com.csicit.ace.report.core.flash;

import com.csicit.ace.common.constant.Constants;
import com.stimulsoft.base.exception.StiException;
import com.stimulsoft.base.localization.StiLocalizationInfo;
import com.stimulsoft.base.utils.StiXmlMarshalUtil;
import com.stimulsoft.flex.StiLocalizationAction;
import org.springframework.boot.system.ApplicationHome;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * MyLocalizationAction.
 * 
 * Copyright Stimulsoft
 * 
 */
public class MyLocalizationAction extends StiLocalizationAction {

    @Override
    public List<StiLocalizationInfo> getLocalizations() throws StiException, FileNotFoundException {
        List<StiLocalizationInfo> list = new ArrayList<>();
        ApplicationHome home = new ApplicationHome(getClass());
        String dir = home.getSource().getParentFile().toString();
        File file = new File(dir+"/Localization/zh-CHS.xml");
        if (Constants.isZuulApp) {
            file = new File(getClass().getResource("/")
                    .getPath()+"/zh-CHS.xml");
        }
        InputStream is = new BufferedInputStream(new FileInputStream(file));
        StiLocalizationInfo localization = StiXmlMarshalUtil.unmarshal(is, StiLocalizationInfo.class);
        localization.setKey("zh-CHS.xml");
        list.add(localization);
        return list;
    }

    @Override
    protected File getLocalizationDir() {
        ApplicationHome home = new ApplicationHome(getClass());
        String dir = home.getSource().getParentFile().toString();
        if (Constants.isZuulApp) {
            return  new File(getClass().getResource("/").getPath() +"/Localization");
        }
        return new File(dir+"Localization");
    }

    @Override
    public InputStream getLocalization(String key) throws StiException, FileNotFoundException {
        ApplicationHome home = new ApplicationHome(getClass());
        String dir = home.getSource().getParentFile().toString();
        File file = new File(dir+"/Localization/zh-CHS.xml");
        if (Constants.isZuulApp) {
            file = new File(getClass().getResource("/").getPath() +"/zh-CHS.xml");
        }
        return  new BufferedInputStream(new FileInputStream(file));
    }

}

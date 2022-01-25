package com.csicit.ace.report.core.config;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

/**
 * @author FourLeaves
 * @version V1.0
 * @date 2020/7/6 11:39
 */
public class Constants {

    private static ClassLoader findClassLoader(){
        ClassLoader loader = Thread.currentThread().getContextClassLoader();
        if(loader==null){
            loader = ClassLoader.getSystemClassLoader();
        }
        return loader;
    }

    public static InputStream getXmlStr() {
        InputStream stream = null;
        try {
            stream = findClassLoader().getResourceAsStream("classpath:Location/zh-CHS.xml");
            if (com.csicit.ace.common.constant.Constants.isZuulApp) {
                return  new FileInputStream(new File(Constants.class.getResource("/").getPath() +"/zh-CHS.xml"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return stream;
    }


}

package com.csicit.ace.webservice.utils;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.StringReader;


/**
 * @author yansiyang
 * @version V1.0
 * @date 2019/8/6 11:16
 */
public class XmlUtils {
    public static <T> T convertXmlToJavaBean(Class<T> clz, String xmlStr) {
        try {
            JAXBContext jaxbContext = JAXBContext.newInstance(clz);
            Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
            T t = (T) unmarshaller.unmarshal(new StringReader(xmlStr));
            return t;
        } catch (JAXBException e) {
            e.printStackTrace();
        }
        return null;
    }
}

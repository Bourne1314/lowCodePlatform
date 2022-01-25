package com.csicit.ace.common.utils;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import java.io.StringReader;
import java.io.StringWriter;

/**
 * 序列化和反序列化
 *
 * @Author: zuogang
 * @Descruption:
 * @Date: Created in 14:36 2019/4/11
 * @Modified By:
 */
public class XmlUtils {
    /**
     * XML序列化
     *
     * @param
     * @return java.lang.Object
     * @author zuogang
     * @date 2019/8/6 9:31
     */
    public static StringWriter marshalToXmlWriter(Object root, Class... classesToBeBound) {
        StringWriter stringWriter = new StringWriter();
        try {
            Marshaller jaxbMarshaller = JAXBContext.newInstance(classesToBeBound).createMarshaller();
            jaxbMarshaller.setProperty(Marshaller.JAXB_ENCODING, "UTF-8");
            jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            jaxbMarshaller.marshal(root, stringWriter);

        } catch (JAXBException e) {
            e.printStackTrace();
        }
        return stringWriter;
    }

    /**
     * XML序列化 转String
     *
     * @param root
     * @param classesToBeBound
     * @return
     * @author zuogang
     * @date 2020/7/23 16:51
     */
    public static String marshalToXml(Object root, Class... classesToBeBound) {
        StringWriter stringWriter = marshalToXmlWriter(root, classesToBeBound);
        String writer = stringWriter.toString();
        writer = writer.replace("ns2:", "");
        writer = writer.replace(":ns2", "");
        return writer;
    }

    /**
     * XML反序列化
     *
     * @param s
     * @return java.lang.Object
     * @author zuogang
     * @date 2019/8/6 9:31
     */
    public static Object unmarshalXml(String s, Class... classesToBeBound) {
        Object result = null;
        try {
            JAXBContext jaxbContext = JAXBContext.newInstance(classesToBeBound);
            Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
            StringReader sr = new StringReader(s);
            result = unmarshaller.unmarshal(sr);
        } catch (JAXBException e) {
            e.printStackTrace();
        }
        return result;
    }

}

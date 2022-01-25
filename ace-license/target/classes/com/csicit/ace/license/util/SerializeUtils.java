package com.csicit.ace.license.util;

import java.io.*;
import java.util.UUID;

/**
 * 序列化工具类
 *
 * @author shanwj
 * @date Created in 18:04 2019/4/15
 * @version V1.0
 */
public class SerializeUtils {

    public static synchronized String createUUID(){
        String uuid = UUID.randomUUID().toString();
        uuid = uuid.replace("-", "");
        return uuid;
    }
    /**
     * 将对象序列化
     *
     * @param obj	
     * @return byte[] 对象字节数组
     * @author shanwj
     * @date 2019/4/15 18:09
     */
    static public byte[] toByteArray(Object obj) {
        byte[] bytes = null;
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        try {
            ObjectOutputStream oos = new ObjectOutputStream(bos);
            oos.writeObject(obj);
            oos.flush();
            bytes = bos.toByteArray();
            oos.close();
            bos.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return bytes;
    }
    //反序列化
    static public  Object toObject(byte[] bytes) {
        Object obj = null;
        try {
            ByteArrayInputStream bis = new ByteArrayInputStream(bytes);
            ObjectInputStream ois = new ObjectInputStream(bis);
            obj = ois.readObject();
            ois.close();
            bis.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        } catch (ClassNotFoundException ex) {
            ex.printStackTrace();
        }
        return obj;
    }

}

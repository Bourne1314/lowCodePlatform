package com.csicit.ace.bpm.utils;

import com.csicit.ace.bpm.BpmException;
import com.csicit.ace.common.utils.StringUtils;

import java.io.*;
import java.util.Base64;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

/**
 * @author JonnyJiang
 * @date 2020/1/17 11:33
 */
public class SerializeUtils {
    public static String serialize(Object obj) {
        String result = "";
        if (obj != null) {
            try (ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                 ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream)) {
                objectOutputStream.writeObject(obj);
                byte[] bytes = byteArrayOutputStream.toByteArray();
                result = Base64.getEncoder().encodeToString(bytes);
            } catch (IOException e) {
                e.printStackTrace();
                throw new RuntimeException(e.getMessage());
            }


        }
        return result;
    }

    public static Object deserialize(String str) {
        Object result = null;
        if (StringUtils.isNotEmpty(str)) {
            byte[] bytes = Base64.getDecoder().decode(str);
            try (ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(bytes);
                 ObjectInputStream objectInputStream = new ObjectInputStream(byteArrayInputStream)) {
                result = objectInputStream.readObject();
            } catch (Exception ex) {
                ex.printStackTrace();
                throw new BpmException(ex.getMessage());
            }
        }
        return result;
    }

    public static String compressAfterSerialize(Object obj) {
        return compress(serialize(obj));
    }

    public static Object deserializeAfterUncompress(String result) {
        return deserialize(uncompress(result));
    }

    public static String compress(String str) {
        String result = "";
        if (StringUtils.isNotEmpty(str)) {
            try {
                ByteArrayOutputStream out = new ByteArrayOutputStream();
                GZIPOutputStream gzip = new GZIPOutputStream(out);
                gzip.write(str.getBytes());
                gzip.finish();
                result = Base64.getEncoder().encodeToString(out.toByteArray());
                gzip.close();
                out.close();
            } catch (IOException ex) {
                ex.printStackTrace();
                throw new BpmException(ex.getMessage());
            }
        }
        return result;
    }

    public static String uncompress(String str) {
        String result = "";
        if (StringUtils.isNotEmpty(str)) {
            try {
                ByteArrayOutputStream out = new ByteArrayOutputStream();
                byte[] compressed = Base64.getDecoder().decode(str);
                ByteArrayInputStream in = new ByteArrayInputStream(compressed);
                GZIPInputStream gzipInputStream = new GZIPInputStream(in);
                byte[] buffer = new byte[256];
                int n;
                while ((n = gzipInputStream.read(buffer)) >= 0) {
                    out.write(buffer, 0, n);
                }
                out.flush();
                result = out.toString();
                gzipInputStream.close();
                in.close();
                out.close();
            } catch (IOException ex) {
                ex.printStackTrace();
                throw new BpmException(ex.getMessage());
            }
        }
        return result;
    }
}
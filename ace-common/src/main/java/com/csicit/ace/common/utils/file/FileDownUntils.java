package com.csicit.ace.common.utils.file;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;

/**
 * 文件下载工具类
 *
 * @author shanwj
 * @date 2019/8/12 8:31
 */
public class FileDownUntils {
    
    /** 
     * 将字符串进行下载到文件
     *
     * @param response
     * @param data 字符串数据
     * @param fileName 文件名
     * @author shanwj
     * @date 2019/8/12 8:37
     */
    public static void download(HttpServletResponse response, String data,String fileName) {
        try {
            InputStream is = new ByteArrayInputStream(data.getBytes("utf-8"));
            byte[] buffer = new byte[is.available()];
            is.read(buffer);
            is.close();
            // 清空response
            response.reset();
            // 设置response的Header
            response.addHeader("Content-Disposition", "attachment;filename=" +
                    new String( fileName.getBytes("gb2312"), "ISO8859-1" ) );
            OutputStream toClient = new BufferedOutputStream(response.getOutputStream());
            response.addHeader("Content-Length", "" + data.getBytes("utf-8").length);
            response.setContentType("application/octet-stream");
            toClient.write(buffer);
            toClient.flush();
            toClient.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void download(HttpServletResponse response, File file,String fileName) {
        try {
            InputStream is = new BufferedInputStream(new FileInputStream(file));
            byte[] buffer = new byte[is.available()];
            is.read(buffer);
            is.close();
            // 清空response
            response.reset();
            // 设置response的Header
            response.addHeader("Content-Disposition", "attachment;filename=" +  new String(fileName.getBytes(), "utf-8"));
            OutputStream toClient = new BufferedOutputStream(response.getOutputStream());
            response.addHeader("Content-Length", "" + file.length());
            response.setContentType("application/octet-stream");
            toClient.write(buffer);
            toClient.flush();
            toClient.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}

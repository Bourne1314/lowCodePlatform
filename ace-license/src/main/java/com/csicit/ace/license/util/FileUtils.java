package com.csicit.ace.license.util;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;

/**
 * @author shanwj
 * @date 2019/7/1 19:36
 */
public class FileUtils {

    public static void createFile(String data,String fileName){
        File folder = new File("D://tmp");
        if (!folder.exists()){
            folder.mkdir();
        }
        File file = new File("D://tmp//"+fileName+".lic");
        PrintStream ps = null;
        try {
            ps = new PrintStream(new FileOutputStream(file));
            ps.println(data);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }finally {
            if(ps!=null){
                ps.close();
            }
        }
    }

    public static void download(HttpServletResponse response, File file, String filePath, String fileName) {
        try {
            // 以流的形式下载文件。
            InputStream fis = new BufferedInputStream(new FileInputStream(filePath));
            fileName = URLEncoder.encode(fileName,"UTF-8");
            byte[] buffer = new byte[fis.available()];
            fis.read(buffer);
            fis.close();
            // 清空response
            response.reset();
            // 设置response的Header
            response.addHeader("Content-Disposition", "attachment;filename=" + fileName);
            response.addHeader("Content-Length", "" + file.length());
            OutputStream toClient = new BufferedOutputStream(response.getOutputStream());
            response.setContentType("application/octet-stream");
            toClient.write(buffer);
            toClient.flush();
            toClient.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}

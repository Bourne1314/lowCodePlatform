package com.csicit.ace.dev.util;

import com.csicit.ace.common.exception.RException;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;

/**
 * 执行windows的cmd命令工具类
 *
 * @author zuogang
 * @date 2020/1/2 10:15
 */
public class CMDUtil {

    /**
     * 执行一个cmd命令(启动服务)
     *
     * @param cmdCommand cmd命令
     * @return 命令执行结果字符串，如出现异常返回null
     */
    public static boolean excuteRunCommand(String cmdCommand) {
        Process process = null;
        try {
            process = Runtime.getRuntime().exec(cmdCommand);
            process.getOutputStream().close();  // 不要忘记了一定要关
        } catch (Exception e) {
            throw new RException(e.getLocalizedMessage());
        }
        return true;
    }

    /**
     * 执行一个cmd命令
     *
     * @param cmdCommand cmd命令
     * @return 命令执行结果字符串，如出现异常返回null
     */
    public static String excuteCMDCommand(String cmdCommand) {
        Process process = null;
        // 记录dos命令的返回信息
        StringBuffer resStr = new StringBuffer();
        try {
            process = Runtime.getRuntime().exec(cmdCommand);

            // 获取返回信息的流
            InputStream in = process.getInputStream();
            Reader reader = new InputStreamReader(in, "GBK");
            BufferedReader bReader = new BufferedReader(reader);
            String res = null;
            while ((res = bReader.readLine()) != null) {
                resStr.append(res + "\n");
            }
            System.out.println(resStr.toString());
            bReader.close();
            reader.close();
            process.getOutputStream().close();  // 不要忘记了一定要关
        } catch (Exception e) {
            throw new RException(e.getLocalizedMessage());
        }

        return resStr.toString();
    }

    /**
     * 执行bat文件，
     *
     * @param file          bat文件路径
     * @param isCloseWindow 执行完毕后是否关闭cmd窗口
     * @return bat文件输出log
     */
    public static String excuteBatFile(String file, boolean isCloseWindow) {
        String cmdCommand = null;
        if (isCloseWindow) {
            cmdCommand = "cmd.exe /c " + file;
        } else {
            cmdCommand = "cmd.exe /k " + file;
        }
        Process process = null;
        try {
            process = Runtime.getRuntime().exec(cmdCommand);
            // 记录dos命令的返回信息
            StringBuffer resStr = new StringBuffer();
            // 获取返回信息的流
            InputStream in = process.getInputStream();
            Reader reader = new InputStreamReader(in, "GBK");
            BufferedReader bReader = new BufferedReader(reader);
            for (String res = ""; (res = bReader.readLine()) != null; ) {
                resStr.append(res + "\n");
            }
            System.out.println(resStr.toString());
            bReader.close();
            reader.close();
            process.getOutputStream().close();  // 不要忘记了一定要关

            return resStr.toString();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}

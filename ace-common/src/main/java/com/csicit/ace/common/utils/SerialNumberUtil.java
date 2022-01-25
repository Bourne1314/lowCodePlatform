package com.csicit.ace.common.utils;


import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.*;
import java.net.*;
import java.util.*;

/**
 * @author zuogang
 * @date Created in 15:18 2019/8/15
 */
public class SerialNumberUtil {
    static Logger log = LogManager.getLogger(SerialNumberUtil.class);

    /**
     * 获取主板序列号
     *
     * @return
     */
    public static String getMotherboardSN() {
        StringJoiner joiner = new StringJoiner("");
        try {
            File file = File.createTempFile("realhowto", ".vbs");
            file.deleteOnExit();
            FileWriter fw = new FileWriter(file);

            String vbs = "Set objWMIService = GetObject(\"winmgmts:\\\\.\\root\\cimv2\")\n"
                    + "Set colItems = objWMIService.ExecQuery _ \n"
                    + "   (\"Select * from Win32_BaseBoard\") \n"
                    + "For Each objItem in colItems \n"
                    + "    Wscript.Echo objItem.SerialNumber \n"
                    + "    exit for  ' do the first cpu only! \n" + "Next \n";

            fw.write(vbs);
            fw.close();
            String path = file.getPath().replace("%20", " ");
            Process p = Runtime.getRuntime().exec(
                    "cscript //NoLogo " + path);
            BufferedReader input = new BufferedReader(new InputStreamReader(
                    p.getInputStream(), "UTF-8"));
            String line;
            while ((line = input.readLine()) != null) {
                joiner.add(line);
            }
            input.close();
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return joiner.toString().trim();
    }

    /**
     * 获取硬盘序列号(该方法获取的是硬盘本身的序列号)
     *
     * @return
     */
    public static String getHardDiskSN() {
        StringJoiner joiner = new StringJoiner("");
        try {
            File file = File.createTempFile("realhowto", ".vbs");
            file.deleteOnExit();
            FileWriter fw = new FileWriter(file);
            String vbs = "Set objWMIService = GetObject(\"winmgmts:\\\\.\\root\\cimv2\")\n"
                    + "Set colItems = objWMIService.ExecQuery _ \n"
                    + "   (\"Select * from Win32_DiskDrive\") \n"
                    + "For Each objItem in colItems \n"
                    + "    Wscript.Echo objItem.SerialNumber \n"
                    + "    exit for  ' do the first cpu only! \n" + "Next \n";

            fw.write(vbs);
            fw.close();
            String path = file.getPath().replace("%20", " ");
            Process p = Runtime.getRuntime().exec(
                    "cscript //NoLogo " + path);
            BufferedReader input = new BufferedReader(new InputStreamReader(
                    p.getInputStream(), "UTF-8"));
            String line;
            while ((line = input.readLine()) != null) {
                joiner.add(line);
            }
            input.close();
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return joiner.toString().trim();
    }

    /**
     * 获取CPU序列号
     *
     * @return
     */
    public static String getCPUSerial() {
        String result = "";
        StringJoiner joiner = new StringJoiner("");
        try {
            File file = File.createTempFile("tmp", ".vbs");
            file.deleteOnExit();
            FileWriter fw = new FileWriter(file);
            String vbs = "Set objWMIService = GetObject(\"winmgmts:\\\\.\\root\\cimv2\")\n"
                    + "Set colItems = objWMIService.ExecQuery _ \n"
                    + "   (\"Select * from Win32_Processor\") \n"
                    + "For Each objItem in colItems \n"
                    + "    Wscript.Echo objItem.ProcessorId \n"
                    + "    exit for  ' do the first cpu only! \n" + "Next \n";

            // + "    exit for  \r\n" + "Next";
            fw.write(vbs);
            fw.close();
            String path = file.getPath().replace("%20", " ");
            Process p = Runtime.getRuntime().exec(
                    "cscript //NoLogo " + path);
            BufferedReader input = new BufferedReader(new InputStreamReader(
                    p.getInputStream(), "UTF-8"));
            String line;
            while ((line = input.readLine()) != null) {
                joiner.add(line);
            }
            input.close();
            file.delete();
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        result = joiner.toString();
        if (result.trim().length() < 1 || result == null) {
            result = "无CPU_ID被读取";
        }
        return result.trim();
    }

    private static List<String> getLocalHostLANAddress() throws UnknownHostException, SocketException {
        List<String> ips = new ArrayList<String>();
        Enumeration<NetworkInterface> interfs = NetworkInterface.getNetworkInterfaces();
        while (interfs.hasMoreElements()) {
            NetworkInterface interf = interfs.nextElement();
            Enumeration<InetAddress> addres = interf.getInetAddresses();
            while (addres.hasMoreElements()) {
                InetAddress in = addres.nextElement();
                if (in instanceof Inet4Address) {
                    System.out.println("v4:" + in.getHostAddress());
                    if (!"127.0.0.1".equals(in.getHostAddress())) {
                        ips.add(in.getHostAddress());
                    }
                }
            }
        }
        return ips;
    }

    /**
     * MAC
     * 通过jdk自带的方法,先获取本机所有的ip,然后通过NetworkInterface获取mac地址
     *
     * @return
     */
    public static String getMac() {
        try {
            StringJoiner joiner = new StringJoiner(",");
            List<String> ls = getLocalHostLANAddress();
            for (String str : ls) {
                InetAddress ia = InetAddress.getByName(str);// 获取本地IP对象
                // 获得网络接口对象（即网卡），并得到mac地址，mac地址存在于一个byte数组中。
                byte[] mac = NetworkInterface.getByInetAddress(ia)
                        .getHardwareAddress();
                // 下面代码是把mac地址拼装成String
                StringBuilder sb = new StringBuilder();
                for (int i = 0; i < mac.length; i++) {
                    if (i != 0) {
                        sb.append("-");
                    }
                    // mac[i] & 0xFF 是为了把byte转化为正整数
                    String s = Integer.toHexString(mac[i] & 0xFF);
                    sb.append(s.length() == 1 ? 0 + s : s);
                }
                // 把字符串所有小写字母改为大写成为正规的mac地址并返回
                joiner.add(sb.toString().toUpperCase());
            }
            return joiner.toString() + ",";
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return null;
    }

    /***************************linux*********************************/

    public static String executeLinuxCmd(String cmd) {
        try {
            System.out.println("got cmd job : " + cmd);
            Runtime run = Runtime.getRuntime();
            Process process;
            process = run.exec(cmd);
            InputStream in = process.getInputStream();
            BufferedReader bs = new BufferedReader(new InputStreamReader(in,"UTF-8"));
            StringBuffer out = new StringBuffer();
            byte[] b = new byte[8192];
            for (int n; (n = in.read(b)) != -1; ) {
                out.append(new String(b, 0, n,"UTF-8"));
            }
            bs.close();
            in.close();
            process.destroy();
            return out.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * @param cmd    命令语句
     * @param record 要查看的字段
     * @param symbol 分隔符
     * @return
     */
    public static String getSerialNumber(String cmd, String record, String symbol) {
        String execResult = executeLinuxCmd(cmd);
        String[] infos = execResult.split("\n");

        for (String info : infos) {
            info = info.trim();
            if (info.indexOf(record) != -1) {
                info = info.replace(" ", "");
                String[] sn = info.split(symbol);
                return sn[1];
            }
        }

        return null;
    }

    /**
     * 获取CPUID、硬盘序列号、MAC地址、主板序列号
     *
     * @return
     */
    public static Map<String, String> getAllSn() {
        String os = System.getProperty("os.name");
        Map<String, String> snVo = new HashMap<String, String>();

        if ("LINUX".equals(os)) {
            System.out.println("=============>for linux");
            String cpuid = getSerialNumber("dmidecode -t processor | grep 'ID'", "ID", ":");
            System.out.println("cpuid : " + cpuid);
            String mainboardNumber = getSerialNumber("dmidecode |grep 'Serial Number'", "Serial Number", ":");
            System.out.println("mainboardNumber : " + mainboardNumber);
            String diskNumber = getSerialNumber("fdisk -l", "Disk identifier", ":");
            System.out.println("diskNumber : " + diskNumber);
            String mac = getSerialNumber("ifconfig -a", "ether", " ");
            snVo.put("cpuid", cpuid.toUpperCase().replace(" ", ""));
            snVo.put("diskid", diskNumber.toUpperCase().replace(" ", ""));
            snVo.put("mac", mac.toUpperCase().replace(" ", ""));
            snVo.put("mainboard", mainboardNumber.toUpperCase().replace(" ", ""));
        } else {
            System.out.println("=============>for windows");
            String cpuid = SerialNumberUtil.getCPUSerial();
            String mainboard = SerialNumberUtil.getMotherboardSN();
            String disk = SerialNumberUtil.getHardDiskSN();
            String mac = SerialNumberUtil.getMac();

            System.out.println("CPU  SN:" + cpuid);
            System.out.println("主板  SN:" + mainboard);
            System.out.println("C盘   SN:" + disk);
            System.out.println("MAC  SN:" + mac);

            snVo.put("cpuid", cpuid.toUpperCase().replace(" ", ""));
            snVo.put("diskid", disk.toUpperCase().replace(" ", ""));
            snVo.put("mac", mac.toUpperCase().replace(" ", ""));
            snVo.put("mainboard", mainboard.toUpperCase().replace(" ", ""));
        }

        return snVo;
    }

    /**
     * linux
     * cpuid : dmidecode -t processor | grep 'ID'
     * mainboard : dmidecode |grep 'Serial Number'
     * disk : fdisk -l
     * mac : ifconfig -a
     *
     * @param args
     */
    public static void main(String[] args) {
        getAllSn();
    }
}
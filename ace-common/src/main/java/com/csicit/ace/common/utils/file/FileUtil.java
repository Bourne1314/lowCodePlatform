package com.csicit.ace.common.utils.file;


import org.apache.commons.lang3.StringUtils;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

/**
 * 文件操作工具类
 *
 * @author shanwj
 * @version V1.0
 * @date 2019-03-29 10:37:46
 */
public class FileUtil {

    /**
     * 获取文件后缀
     *
     * @param fileName 文件名
     * @return 文件后缀
     * @author JonnyJiang
     * @date 2020/6/22 10:12
     */

    public static String getSuffix(String fileName) {
        if (StringUtils.isNotEmpty(fileName)) {
            Integer index = fileName.indexOf(".");
            if (index > -1 && index < fileName.length() - 1) {
                return fileName.substring(index + 1);
            }
        }
        return "";
    }


    /**
     * 返回读取的每行数据记录集合
     *
     * @param fileName 文件名
     * @return 文件每行数据集合
     * @author shanwj
     * @date 2019/4/19 9:03
     */
    public static List<String> readFileReturnLines(String fileName) {
        if (StringUtils.isBlank(fileName)) {
            return null;
        }
        try {
            String charset = getFileCharset(fileName);
            if (StringUtils.isBlank(charset)) {
                return null;
            }
            return Files.readAllLines(Paths.get(fileName), Charset.forName(charset));
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 返回读取的数据字节数组
     *
     * @param fileName 文件名
     * @return byte[] 读取的数据字节数组
     * @author shanwj
     * @date 2019/4/19 9:04
     */
    public static byte[] readFileReturnBytes(String fileName) {
        if (StringUtils.isBlank(fileName)) {
            return null;
        }
        try {
            String charset = getFileCharset(fileName);
            if (StringUtils.isBlank(charset)) {
                return null;
            }
            return Files.readAllBytes(Paths.get(fileName));
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 返回读取的数据字符串
     *
     * @param fileName 文件名
     * @return java.lang.String 读取的数据字符串
     * @author shanwj
     * @date 2019/4/19 9:04
     */
    public static String readFileReturnString(String fileName) {
        if (StringUtils.isBlank(fileName)) {
            return null;
        }
        try {
            String charset = getFileCharset(fileName);
            if (StringUtils.isBlank(charset)) {
                return null;
            }
            byte[] data = Files.readAllBytes(Paths.get(fileName));
            return new String(data, StandardCharsets.UTF_8);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 写入内容
     *
     * @param fileName 文件名
     * @param content  字符串内容
     * @author shanwj
     * @date 2019/4/19 9:52
     */
    public static void writeFile(String fileName, String content) {
        byte[] bytes = content.getBytes(StandardCharsets.UTF_8);
        writeFile(fileName, bytes);
    }

    /**
     * 写入内容
     *
     * @param fileName 文件名
     * @param bytes    字节内容
     * @author shanwj
     * @date 2019/4/19 9:52
     */
    public static void writeFile(String fileName, byte[] bytes) {
        if (StringUtils.isBlank(fileName)) {
            return;
        }
        Path path = Paths.get(fileName);
        try {
            Files.write(path, bytes);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 写入内容
     *
     * @param fileName 文件名
     * @param contents 行内容集合
     * @author shanwj
     * @date 2019/4/19 9:52
     */
    public static void writeFile(String fileName, List<String> contents) {
        if (StringUtils.isBlank(fileName)) {
            return;
        }
        Path path = Paths.get(fileName);
        if (path == null) {
            return;
        }
        try {
            Files.write(path, contents);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取文件编码格式
     *
     * @param fileName 文件名 全路径
     * @return 文件编码格式
     * @author shanwj
     * @date 2019/4/19 8:43
     */
    public static String getFileCharset(String fileName) {
        if (StringUtils.isBlank(fileName)) {
            return null;
        }
        File file = new File(fileName);
        if (!file.exists()) {
            return null;
        }
        String charset = "GBK";
        FileInputStream is = null;
        BufferedInputStream bis = null;
        try {
            byte[] first3Bytes = new byte[3];
            boolean checked = false;
            is = new FileInputStream(file);
            bis = new BufferedInputStream(is);
            bis.mark(0);
            int read = bis.read(first3Bytes, 0, 3);
            if (-1 == read) {
                charset = "GBK";
            } else if (first3Bytes[0] == (byte) 0xFF
                    && first3Bytes[1] == (byte) 0xFE) {
                charset = "UTF-16LE";
                checked = true;
            } else if (first3Bytes[0] == (byte) 0xFE
                    && first3Bytes[1] == (byte) 0xFF) {
                charset = "UTF-16BE";
                checked = true;
            } else if (first3Bytes[0] == (byte) 0xEF
                    && first3Bytes[1] == (byte) 0xBB
                    && first3Bytes[2] == (byte) 0xBF) {
                charset = "UTF-8";
                checked = true;
            }
            bis.reset();
            if (!checked) {
                int loc = 0;
                while ((read = bis.read()) != -1) {
                    loc++;
                    if (read >= 0xF0) {
                        break;
                    }
                    if (0x80 <= read && read <= 0xBF) {
                        // 单独出现BF以下的,也算GBK
                        break;
                    }
                    if (0x80 <= read && read <= 0xDF) {
                        read = bis.read();
                        if (0x80 <= read && read <= 0xBF) {
                            // GBK
                            continue;
                        } else {
                            break;
                        }
                    } else if (0xE0 <= read && read <= 0xEF) {
                        read = bis.read();
                        if (0x80 <= read && read <= 0xBF) {
                            read = bis.read();
                            if (0x80 <= read && read <= 0xBF) {
                                charset = "UTF-8";
                                break;
                            } else {
                                break;
                            }
                        } else {
                            break;
                        }
                    }
                }
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            IOUtils.closeStream(bis, null);
            IOUtils.closeStream(is, null);
        }
        return charset;
    }

    /**
     * 获取contentType
     *
     * @param suffix 后缀
     * @return java.lang.String
     * @author JonnyJiang
     * @date 2020/6/22 15:46
     */

    public static String getContentType(String suffix) {
        switch (suffix) {
            case "001":
                return "application/x-001";
            case "301":
                return "application/x-301";
            case "323":
                return "text/h323";
            case "906":
                return "application/x-906";
            case "907":
                return "drawing/907";
            case "a11":
                return "application/x-a11";
            case "acp":
                return "audio/x-mei-aac";
            case "anv":
                return "application/x-anv";
            case "asa":
                return "text/asa";
            case "awf":
                return "application/vnd.adobe.workflow";
            case "biz":
                return "text/xml";
            case "bot":
                return "application/x-bot";
            case "c4t":
                return "application/x-c4t";
            case "c90":
                return "application/x-c90";
            case "cal":
                return "application/x-cals";
            case "cat":
                return "application/vnd.ms-pki.seccat";
            case "cel":
                return "application/x-cel";
            case "cg4":
                return "application/x-g4";
            case "cit":
                return "application/x-cit";
            case "cml":
                return "text/xml";
            case "cmp":
                return "application/x-cmp";
            case "cmx":
                return "application/x-cmx";
            case "cot":
                return "application/x-cot";
            case "crl":
                return "application/pkix-crl";
            case "csi":
                return "application/x-csi";
            case "cut":
                return "application/x-cut";
            case "dbm":
                return "application/x-dbm";
            case "dbx":
                return "application/x-dbx";
            case "dcd":
                return "text/xml";
            case "dcx":
                return "application/x-dcx";
            case "dgn":
                return "application/x-dgn";
            case "dib":
                return "application/x-dib";
            case "dll":
                return "application/x-msdownload";
            case "drw":
                return "application/x-drw";
            case "dwf":
                return "Model/vndcase";
            case "dxb":
                return "application/x-dxb";
            case "edn":
                return "application/vnd.adobe.edn";
            case "eml":
                return "message/rfc822";
            case "epi":
                return "application/x-epi";
            case "etd":
                return "application/x-ebx";
            case "fax":
                return "image/fax";
            case "fdf":
                return "application/vndcase";
            case "fif":
                return "application/fractals";
            case "frm":
                return "application/x-frm";
            case "g4":
                return "application/x-g4";
            case "gbr":
                return "application/x-gbr";
            case "":
                return "application/x-";
            case "gl2":
                return "application/x-gl2";
            case "gp4":
                return "application/x-gp4";
            case "hgl":
                return "application/x-hgl";
            case "hmr":
                return "application/x-hmr";
            case "hpg":
                return "application/x-hpgl";
            case "hpl":
                return "application/x-hpl";
            case "hqx":
                return "application/mac-binhex40";
            case "hrf":
                return "application/x-hrf";
            case "hta":
                return "application/hta";
            case "htc":
                return "text/x-component";
            case "htt":
                return "text/webviewhtml";
            case "htx":
                return "text/html";
            case "ig4":
                return "application/x-g4";
            case "igs":
                return "application/x-igs";
            case "iii":
                return "application/x-iphone";
            case "img":
                return "application/x-img";
            case "isp":
                return "application/x-internet-signup";
            case "IVF":
                return "video/x-ivf";
            case "jfif":
                return "image/jpeg";
            case "jsp":
                return "text/html";
            case "la1":
                return "audio/x-liquid-file";
            case "lar":
                return "application/x-laplayer-reg";
            case "lavs":
                return "audio/x-liquid-secure";
            case "lbm":
                return "application/x-lbm";
            case "lmsff":
                return "audio/x-la-lms";
            case "ls":
                return "application/x-javascript";
            case "ltr":
                return "application/x-ltr";
            case "m1v":
                return "video/x-mpeg";
            case "m2v":
                return "video/x-mpeg";
            case "m4e":
                return "video/mpeg4";
            case "mac":
                return "application/x-mac";
            case "math":
                return "text/xml";
            case "mfp":
                return "application/x-shockwave-flash";
            case "mht":
                return "message/rfc822";
            case "mhtml":
                return "message/rfc822";
            case "mi":
                return "application/x-mi";
            case "mil":
                return "application/x-mil";
            case "mnd":
                return "audio/x-musicnet-download";
            case "mns":
                return "audio/x-musicnet-stream";
            case "mocha":
                return "application/x-javascript";
            case "mp1":
                return "audio/mp1";
            case "mp2v":
                return "video/mpeg";
            case "mpa":
                return "video/x-mpg";
            case "mpd":
                return "application/vnd.ms-project";
            case "mps":
                return "video/x-mpeg";
            case "mpt":
                return "application/vnd.ms-project";
            case "mpv":
                return "video/mpg";
            case "mpv2":
                return "video/mpeg";
            case "mpw":
                return "application/vnd.ms-project";
            case "mpx":
                return "application/vnd.ms-project";
            case "mtx":
                return "text/xml";
            case "mxp":
                return "application/x-mmxp";
            case "net":
                return "image/pnetvue";
            case "nrf":
                return "application/x-nrf";
            case "nws":
                return "message/rfc822";
            case "out":
                return "application/x-out";
            case "p7c":
                return "application/pkcs7-mime";
            case "p7m":
                return "application/pkcs7-mime";
            case "p7r":
                return "application/x-pkcs7-certreqresp";
            case "pc5":
                return "application/x-pc5";
            case "pci":
                return "application/x-pci";
            case "pdx":
                return "application/vnd.adobe.pdx";
            case "pgl":
                return "application/x-pgl";
            case "pic":
                return "application/x-pic";
            case "pko":
                return "application/vnd.ms-pki.pko";
            case "plg":
                return "text/html";
            case "plt":
                return "application/x-plt";
            case "ppa":
                return "application/vnd.ms-powerpoint";
            case "pr":
                return "application/x-pr";
            case "prf":
                return "application/pics-rules";
            case "prn":
                return "application/x-prn";
            case "prt":
                return "application/x-prt";
            case "ptn":
                return "application/x-ptn";
            case "pwz":
                return "application/vnd.ms-powerpoint";
            case "r3t":
                return "text/vnd.rn-realtext3d";
            case "rat":
                return "application/rat-file";
            case "rec":
                return "application/vnd.rn-recording";
            case "red":
                return "application/x-red";
            case "rjs":
                return "application/vnd.rn-realsystem-rjs";
            case "rjt":
                return "application/vnd.rn-realsystem-rjt";
            case "rlc":
                return "application/x-rlc";
            case "rmf":
                return "application/vnd.adobe.rmf";
            case "rmi":
                return "audio/mid";
            case "rmp":
                return "application/vnd.rn-rn_music_package";
            case "rnx":
                return "application/vnd.rn-realplayer";
            case "rsml":
                return "application/vnd.rn-rsml";
            case "sat":
                return "application/x-sat";
            case "slb":
                return "application/x-slb";
            case "sld":
                return "application/x-sld";
            case "smk":
                return "application/x-smk";
            case "sol":
                return "text/plain";
            case "sor":
                return "text/plain";
            case "spp":
                return "text/xml";
            case "ssm":
                return "application/streamingmedia";
            case "sst":
                return "application/vnd.ms-pki.certstore";
            case "stl":
                return "application/vnd.ms-pki.stl";
            case "tdf":
                return "application/x-tdf";
            case "tg4":
                return "application/x-tg4";
            case "tld":
                return "text/xml";
            case "top":
                return "drawing/x-top";
            case "tsd":
                return "text/xml";
            case "uin":
                return "application/x-icq";
            case "uls":
                return "text/iuls";
            case "vdx":
                return "application/vnd.visio";
            case "vml":
                return "text/xml";
            case "vpg":
                return "application/x-vpeg005";
            case "vsd":
                return "application/vnd.visio";
            case "vss":
                return "application/vnd.visio";
            case "vsw":
                return "application/vnd.visio";
            case "vsx":
                return "application/vnd.visio";
            case "vtx":
                return "application/vnd.visio";
            case "vxml":
                return "text/xml";
            case "wiz":
                return "application/msword";
            case "wkq":
                return "application/x-wkq";
            case "wm":
                return "video/x-ms-wm";
            case "wmd":
                return "application/x-ms-wmd";
            case "wmz":
                return "application/x-ms-wmz";
            case "wq1":
                return "application/x-wq1";
            case "wr1":
                return "application/x-wr1";
            case "wrk":
                return "application/x-wrk";
            case "ws":
                return "application/x-ws";
            case "ws2":
                return "application/x-ws";
            case "wsc":
                return "text/scriptlet";
            case "wsdl":
                return "text/xml";
            case "xdp":
                return "application/vnd.adobe.xdp";
            case "xdr":
                return "text/xml";
            case "xfd":
                return "application/vnd.adobe.xfd";
            case "xfdf":
                return "application/vnd.adobe.xfdf";
            case "xpl":
                return "audio/scpls";
            case "xq":
                return "text/xml";
            case "xql":
                return "text/xml";
            case "xquery":
                return "text/xml";
            case "xsd":
                return "text/xml";
            case "x_b":
                return "application/x-x_b";
            case "x_t":
                return "application/x-x_t";
            case "ipa":
                return "application/vnd.iphone";
            case "apk":
                return "application/vnd.android.package-archive";
            case "xap":
                return "application/x-silverlight-app";
            case "load":
                return "text/html";
            case "123":
                return "application/vnd.lotus-1-2-3";
            case "3ds":
                return "image/x-3ds";
            case "3g2":
                return "video/3gpp";
            case "3ga":
                return "video/3gpp";
            case "3gp":
                return "video/3gpp";
            case "3gpp":
                return "video/3gpp";
            case "602":
                return "application/x-t602";
            case "669":
                return "audio/x-mod";
            case "7z":
                return "application/x-7z-compressed";
            case "a":
                return "application/x-archive";
            case "aac":
                return "audio/mp4";
            case "abw":
                return "application/x-abiword";
            case "abw.crashed":
                return "application/x-abiword";
            case "abw.gz":
                return "application/x-abiword";
            case "ac3":
                return "audio/ac3";
            case "ace":
                return "application/x-ace";
            case "adb":
                return "text/x-adasrc";
            case "ads":
                return "text/x-adasrc";
            case "afm":
                return "application/x-font-afm";
            case "ag":
                return "image/x-applix-graphics";
            case "ai":
                return "application/illustrator";
            case "aif":
                return "audio/x-aiff";
            case "aifc":
                return "audio/x-aiff";
            case "aiff":
                return "audio/x-aiff";
            case "al":
                return "application/x-perl";
            case "alz":
                return "application/x-alz";
            case "amr":
                return "audio/amr";
            case "ani":
                return "application/x-navi-animation";
            case "anim[1-9j]":
                return "video/x-anim";
            case "anx":
                return "application/annodex";
            case "ape":
                return "audio/x-ape";
            case "arj":
                return "application/x-arj";
            case "arw":
                return "image/x-sony-arw";
            case "as":
                return "application/x-applix-spreadsheet";
            case "asc":
                return "text/plain";
            case "asf":
                return "video/x-ms-asf";
            case "asp":
                return "application/x-asp";
            case "ass":
                return "text/x-ssa";
            case "asx":
                return "audio/x-ms-asx";
            case "atom":
                return "application/atom+xml";
            case "au":
                return "audio/basic";
            case "avi":
                return "video/x-msvideo";
            case "aw":
                return "application/x-applix-word";
            case "awb":
                return "audio/amr-wb";
            case "awk":
                return "application/x-awk";
            case "axa":
                return "audio/annodex";
            case "axv":
                return "video/annodex";
            case "bak":
                return "application/x-trash";
            case "bcpio":
                return "application/x-bcpio";
            case "bdf":
                return "application/x-font-bdf";
            case "bib":
                return "text/x-bibtex";
            case "bin":
                return "application/octet-stream";
            case "blend":
                return "application/x-blender";
            case "blender":
                return "application/x-blender";
            case "bmp":
                return "image/bmp";
            case "bz":
                return "application/x-bzip";
            case "bz2":
                return "application/x-bzip";
            case "c":
                return "text/x-csrc";
            case "c++":
                return "text/x-c++src";
            case "cab":
                return "application/vnd.ms-cab-compressed";
            case "cb7":
                return "application/x-cb7";
            case "cbr":
                return "application/x-cbr";
            case "cbt":
                return "application/x-cbt";
            case "cbz":
                return "application/x-cbz";
            case "cc":
                return "text/x-c++src";
            case "cdf":
                return "application/x-netcdf";
            case "cdr":
                return "application/vnd.corel-draw";
            case "cer":
                return "application/x-x509-ca-cert";
            case "cert":
                return "application/x-x509-ca-cert";
            case "cgm":
                return "image/cgm";
            case "chm":
                return "application/x-chm";
            case "chrt":
                return "application/x-kchart";
            case "class":
                return "application/x-java";
            case "cls":
                return "text/x-tex";
            case "cmake":
                return "text/x-cmake";
            case "cpio":
                return "application/x-cpio";
            case "cpio.gz":
                return "application/x-cpio-compressed";
            case "cpp":
                return "text/x-c++src";
            case "cr2":
                return "image/x-canon-cr2";
            case "crt":
                return "application/x-x509-ca-cert";
            case "crw":
                return "image/x-canon-crw";
            case "cs":
                return "text/x-csharp";
            case "csh":
                return "application/x-csh";
            case "css":
                return "text/css";
            case "cssl":
                return "text/css";
            case "csv":
                return "text/csv";
            case "cue":
                return "application/x-cue";
            case "cur":
                return "image/x-win-bitmap";
            case "cxx":
                return "text/x-c++src";
            case "d":
                return "text/x-dsrc";
            case "dar":
                return "application/x-dar";
            case "dbf":
                return "application/x-dbf";
            case "dc":
                return "application/x-dc-rom";
            case "dcl":
                return "text/x-dcl";
            case "dcm":
                return "application/dicom";
            case "dcr":
                return "image/x-kodak-dcr";
            case "dds":
                return "image/x-dds";
            case "deb":
                return "application/x-deb";
            case "der":
                return "application/x-x509-ca-cert";
            case "desktop":
                return "application/x-desktop";
            case "dia":
                return "application/x-dia-diagram";
            case "diff":
                return "text/x-patch";
            case "divx":
                return "video/x-msvideo";
            case "djv":
                return "image/vnd.djvu";
            case "djvu":
                return "image/vnd.djvu";
            case "dng":
                return "image/x-adobe-dng";
            case "doc":
                return "application/msword";
            case "docbook":
                return "application/docbook+xml";
            case "docm":
                return "application/vnd.openxmlformats-officedocument.wordprocessingml.document";
            case "docx":
                return "application/vnd.openxmlformats-officedocument.wordprocessingml.document";
            case "dot":
                return "text/vnd.graphviz";
            case "dsl":
                return "text/x-dsl";
            case "dtd":
                return "application/xml-dtd";
            case "dtx":
                return "text/x-tex";
            case "dv":
                return "video/dv";
            case "dvi":
                return "application/x-dvi";
            case "dvi.bz2":
                return "application/x-bzdvi";
            case "dvi.gz":
                return "application/x-gzdvi";
            case "dwg":
                return "image/vnd.dwg";
            case "dxf":
                return "image/vnd.dxf";
            case "e":
                return "text/x-eiffel";
            case "egon":
                return "application/x-egon";
            case "eif":
                return "text/x-eiffel";
            case "el":
                return "text/x-emacs-lisp";
            case "emf":
                return "image/x-emf";
            case "emp":
                return "application/vnd.emusic-emusic_package";
            case "ent":
                return "application/xml-external-parsed-entity";
            case "eps":
                return "image/x-eps";
            case "eps.bz2":
                return "image/x-bzeps";
            case "eps.gz":
                return "image/x-gzeps";
            case "epsf":
                return "image/x-eps";
            case "epsf.bz2":
                return "image/x-bzeps";
            case "epsf.gz":
                return "image/x-gzeps";
            case "epsi":
                return "image/x-eps";
            case "epsi.bz2":
                return "image/x-bzeps";
            case "epsi.gz":
                return "image/x-gzeps";
            case "epub":
                return "application/epub+zip";
            case "erl":
                return "text/x-erlang";
            case "es":
                return "application/ecmascript";
            case "etheme":
                return "application/x-e-theme";
            case "etx":
                return "text/x-setext";
            case "exe":
                return "application/x-ms-dos-executable";
            case "exr":
                return "image/x-exr";
            case "ez":
                return "application/andrew-inset";
            case "f":
                return "text/x-fortran";
            case "f90":
                return "text/x-fortran";
            case "f95":
                return "text/x-fortran";
            case "fb2":
                return "application/x-fictionbook+xml";
            case "fig":
                return "image/x-xfig";
            case "fits":
                return "image/fits";
            case "fl":
                return "application/x-fluid";
            case "flac":
                return "audio/x-flac";
            case "flc":
                return "video/x-flic";
            case "fli":
                return "video/x-flic";
            case "flv":
                return "video/x-flv";
            case "flw":
                return "application/x-kivio";
            case "fo":
                return "text/x-xslfo";
            case "for":
                return "text/x-fortran";
            case "g3":
                return "image/fax-g3";
            case "gb":
                return "application/x-gameboy-rom";
            case "gba":
                return "application/x-gba-rom";
            case "gcrd":
                return "text/directory";
            case "ged":
                return "application/x-gedcom";
            case "gedcom":
                return "application/x-gedcom";
            case "gen":
                return "application/x-genesis-rom";
            case "gf":
                return "application/x-tex-gf";
            case "gg":
                return "application/x-sms-rom";
            case "gif":
                return "image/gif";
            case "glade":
                return "application/x-glade";
            case "gmo":
                return "application/x-gettext-translation";
            case "gnc":
                return "application/x-gnucash";
            case "gnd":
                return "application/gnunet-directory";
            case "gnucash":
                return "application/x-gnucash";
            case "gnumeric":
                return "application/x-gnumeric";
            case "gnuplot":
                return "application/x-gnuplot";
            case "gp":
                return "application/x-gnuplot";
            case "gpg":
                return "application/pgp-encrypted";
            case "gplt":
                return "application/x-gnuplot";
            case "gra":
                return "application/x-graphite";
            case "gsf":
                return "application/x-font-type1";
            case "gsm":
                return "audio/x-gsm";
            case "gtar":
                return "application/x-tar";
            case "gv":
                return "text/vnd.graphviz";
            case "gvp":
                return "text/x-google-video-pointer";
            case "gz":
                return "application/x-gzip";
            case "h":
                return "text/x-chdr";
            case "h++":
                return "text/x-c++hdr";
            case "hdf":
                return "application/x-hdf";
            case "hh":
                return "text/x-c++hdr";
            case "hp":
                return "text/x-c++hdr";
            case "hpgl":
                return "application/vnd.hp-hpgl";
            case "hpp":
                return "text/x-c++hdr";
            case "hs":
                return "text/x-haskell";
            case "htm":
                return "text/html";
            case "html":
                return "text/html";
            case "hwp":
                return "application/x-hwp";
            case "hwt":
                return "application/x-hwt";
            case "hxx":
                return "text/x-c++hdr";
            case "ica":
                return "application/x-ica";
            case "icb":
                return "image/x-tga";
            case "icns":
                return "image/x-icns";
            case "ico":
                return "image/vnd.microsoft.icon";
            case "ics":
                return "text/calendar";
            case "idl":
                return "text/x-idl";
            case "ief":
                return "image/ief";
            case "iff":
                return "image/x-iff";
            case "ilbm":
                return "image/x-ilbm";
            case "ime":
                return "text/x-imelody";
            case "imy":
                return "text/x-imelody";
            case "ins":
                return "text/x-tex";
            case "iptables":
                return "text/x-iptables";
            case "iso":
                return "application/x-cd-image";
            case "iso9660":
                return "application/x-cd-image";
            case "it":
                return "audio/x-it";
            case "j2k":
                return "image/jp2";
            case "jad":
                return "text/vnd.sun.j2me.app-descriptor";
            case "jar":
                return "application/x-java-archive";
            case "java":
                return "text/x-java";
            case "jng":
                return "image/x-jng";
            case "jnlp":
                return "application/x-java-jnlp-file";
            case "jp2":
                return "image/jp2";
            case "jpc":
                return "image/jp2";
            case "jpe":
                return "image/jpeg";
            case "jpeg":
                return "image/jpeg";
            case "jpf":
                return "image/jp2";
            case "jpg":
                return "image/jpeg";
            case "jpr":
                return "application/x-jbuilder-project";
            case "jpx":
                return "image/jp2";
            case "js":
                return "application/javascript";
            case "json":
                return "application/json";
            case "jsonp":
                return "application/jsonp";
            case "k25":
                return "image/x-kodak-k25";
            case "kar":
                return "audio/midi";
            case "karbon":
                return "application/x-karbon";
            case "kdc":
                return "image/x-kodak-kdc";
            case "kdelnk":
                return "application/x-desktop";
            case "kexi":
                return "application/x-kexiproject-sqlite3";
            case "kexic":
                return "application/x-kexi-connectiondata";
            case "kexis":
                return "application/x-kexiproject-shortcut";
            case "kfo":
                return "application/x-kformula";
            case "kil":
                return "application/x-killustrator";
            case "kino":
                return "application/smil";
            case "kml":
                return "application/vnd.google-earth.kml+xml";
            case "kmz":
                return "application/vnd.google-earth.kmz";
            case "kon":
                return "application/x-kontour";
            case "kpm":
                return "application/x-kpovmodeler";
            case "kpr":
                return "application/x-kpresenter";
            case "kpt":
                return "application/x-kpresenter";
            case "kra":
                return "application/x-krita";
            case "ksp":
                return "application/x-kspread";
            case "kud":
                return "application/x-kugar";
            case "kwd":
                return "application/x-kword";
            case "kwt":
                return "application/x-kword";
            case "la":
                return "application/x-shared-library-la";
            case "latex":
                return "text/x-tex";
            case "ldif":
                return "text/x-ldif";
            case "lha":
                return "application/x-lha";
            case "lhs":
                return "text/x-literate-haskell";
            case "lhz":
                return "application/x-lhz";
            case "log":
                return "text/x-log";
            case "ltx":
                return "text/x-tex";
            case "lua":
                return "text/x-lua";
            case "lwo":
                return "image/x-lwo";
            case "lwob":
                return "image/x-lwo";
            case "lws":
                return "image/x-lws";
            case "ly":
                return "text/x-lilypond";
            case "lyx":
                return "application/x-lyx";
            case "lz":
                return "application/x-lzip";
            case "lzh":
                return "application/x-lha";
            case "lzma":
                return "application/x-lzma";
            case "lzo":
                return "application/x-lzop";
            case "m":
                return "text/x-matlab";
            case "m15":
                return "audio/x-mod";
            case "m2t":
                return "video/mpeg";
            case "m3u":
                return "audio/x-mpegurl";
            case "m3u8":
                return "audio/x-mpegurl";
            case "m4":
                return "application/x-m4";
            case "m4a":
                return "audio/mp4";
            case "m4b":
                return "audio/x-m4b";
            case "m4v":
                return "video/mp4";
            case "mab":
                return "application/x-markaby";
            case "man":
                return "application/x-troff-man";
            case "mbox":
                return "application/mbox";
            case "md":
                return "application/x-genesis-rom";
            case "mdb":
                return "application/vnd.ms-access";
            case "mdi":
                return "image/vnd.ms-modi";
            case "me":
                return "text/x-troff-me";
            case "med":
                return "audio/x-mod";
            case "metalink":
                return "application/metalink+xml";
            case "mgp":
                return "application/x-magicpoint";
            case "mid":
                return "audio/midi";
            case "midi":
                return "audio/midi";
            case "mif":
                return "application/x-mif";
            case "minipsf":
                return "audio/x-minipsf";
            case "mka":
                return "audio/x-matroska";
            case "mkv":
                return "video/x-matroska";
            case "ml":
                return "text/x-ocaml";
            case "mli":
                return "text/x-ocaml";
            case "mm":
                return "text/x-troff-mm";
            case "mmf":
                return "application/x-smaf";
            case "mml":
                return "text/mathml";
            case "mng":
                return "video/x-mng";
            case "mo":
                return "application/x-gettext-translation";
            case "mo3":
                return "audio/x-mo3";
            case "moc":
                return "text/x-moc";
            case "mod":
                return "audio/x-mod";
            case "mof":
                return "text/x-mof";
            case "moov":
                return "video/quicktime";
            case "mov":
                return "video/quicktime";
            case "movie":
                return "video/x-sgi-movie";
            case "mp+":
                return "audio/x-musepack";
            case "mp2":
                return "video/mpeg";
            case "mp3":
                return "audio/mpeg";
            case "mp4":
                return "video/mp4";
            case "mpc":
                return "audio/x-musepack";
            case "mpe":
                return "video/mpeg";
            case "mpeg":
                return "video/mpeg";
            case "mpg":
                return "video/mpeg";
            case "mpga":
                return "audio/mpeg";
            case "mpp":
                return "audio/x-musepack";
            case "mrl":
                return "text/x-mrml";
            case "mrml":
                return "text/x-mrml";
            case "mrw":
                return "image/x-minolta-mrw";
            case "ms":
                return "text/x-troff-ms";
            case "msi":
                return "application/x-msi";
            case "msod":
                return "image/x-msod";
            case "msx":
                return "application/x-msx-rom";
            case "mtm":
                return "audio/x-mod";
            case "mup":
                return "text/x-mup";
            case "mxf":
                return "application/mxf";
            case "n64":
                return "application/x-n64-rom";
            case "nb":
                return "application/mathematica";
            case "nc":
                return "application/x-netcdf";
            case "nds":
                return "application/x-nintendo-ds-rom";
            case "nef":
                return "image/x-nikon-nef";
            case "nes":
                return "application/x-nes-rom";
            case "nfo":
                return "text/x-nfo";
            case "not":
                return "text/x-mup";
            case "nsc":
                return "application/x-netshow-channel";
            case "nsv":
                return "video/x-nsv";
            case "o":
                return "application/x-object";
            case "obj":
                return "application/x-tgif";
            case "ocl":
                return "text/x-ocl";
            case "oda":
                return "application/oda";
            case "odb":
                return "application/vnd.oasis.opendocument.database";
            case "odc":
                return "application/vnd.oasis.opendocument.chart";
            case "odf":
                return "application/vnd.oasis.opendocument.formula";
            case "odg":
                return "application/vnd.oasis.opendocument.graphics";
            case "odi":
                return "application/vnd.oasis.opendocument.image";
            case "odm":
                return "application/vnd.oasis.opendocument.text-master";
            case "odp":
                return "application/vnd.oasis.opendocument.presentation";
            case "ods":
                return "application/vnd.oasis.opendocument.spreadsheet";
            case "odt":
                return "application/vnd.oasis.opendocument.text";
            case "oga":
                return "audio/ogg";
            case "ogg":
                return "video/x-theora+ogg";
            case "ogm":
                return "video/x-ogm+ogg";
            case "ogv":
                return "video/ogg";
            case "ogx":
                return "application/ogg";
            case "old":
                return "application/x-trash";
            case "oleo":
                return "application/x-oleo";
            case "opml":
                return "text/x-opml+xml";
            case "ora":
                return "image/openraster";
            case "orf":
                return "image/x-olympus-orf";
            case "otc":
                return "application/vnd.oasis.opendocument.chart-template";
            case "otf":
                return "application/x-font-otf";
            case "otg":
                return "application/vnd.oasis.opendocument.graphics-template";
            case "oth":
                return "application/vnd.oasis.opendocument.text-web";
            case "otp":
                return "application/vnd.oasis.opendocument.presentation-template";
            case "ots":
                return "application/vnd.oasis.opendocument.spreadsheet-template";
            case "ott":
                return "application/vnd.oasis.opendocument.text-template";
            case "owl":
                return "application/rdf+xml";
            case "oxt":
                return "application/vnd.openofficeorg.extension";
            case "p":
                return "text/x-pascal";
            case "p10":
                return "application/pkcs10";
            case "p12":
                return "application/x-pkcs12";
            case "p7b":
                return "application/x-pkcs7-certificates";
            case "p7s":
                return "application/pkcs7-signature";
            case "pack":
                return "application/x-java-pack200";
            case "pak":
                return "application/x-pak";
            case "par2":
                return "application/x-par2";
            case "pas":
                return "text/x-pascal";
            case "patch":
                return "text/x-patch";
            case "pbm":
                return "image/x-portable-bitmap";
            case "pcd":
                return "image/x-photo-cd";
            case "pcf":
                return "application/x-cisco-vpn-settings";
            case "pcf.gz":
                return "application/x-font-pcf";
            case "pcf.z":
                return "application/x-font-pcf";
            case "pcl":
                return "application/vnd.hp-pcl";
            case "pcx":
                return "image/x-pcx";
            case "pdb":
                return "chemical/x-pdb";
            case "pdc":
                return "application/x-aportisdoc";
            case "pdf":
                return "application/pdf";
            case "pdf.bz2":
                return "application/x-bzpdf";
            case "pdf.gz":
                return "application/x-gzpdf";
            case "pef":
                return "image/x-pentax-pef";
            case "pem":
                return "application/x-x509-ca-cert";
            case "perl":
                return "application/x-perl";
            case "pfa":
                return "application/x-font-type1";
            case "pfb":
                return "application/x-font-type1";
            case "pfx":
                return "application/x-pkcs12";
            case "pgm":
                return "image/x-portable-graymap";
            case "pgn":
                return "application/x-chess-pgn";
            case "pgp":
                return "application/pgp-encrypted";
            case "php":
                return "application/x-php";
            case "php3":
                return "application/x-php";
            case "php4":
                return "application/x-php";
            case "pict":
                return "image/x-pict";
            case "pict1":
                return "image/x-pict";
            case "pict2":
                return "image/x-pict";
            case "pickle":
                return "application/python-pickle";
            case "pk":
                return "application/x-tex-pk";
            case "pkipath":
                return "application/pkix-pkipath";
            case "pkr":
                return "application/pgp-keys";
            case "pl":
                return "application/x-perl";
            case "pla":
                return "audio/x-iriver-pla";
            case "pln":
                return "application/x-planperfect";
            case "pls":
                return "audio/x-scpls";
            case "pm":
                return "application/x-perl";
            case "png":
                return "image/png";
            case "pnm":
                return "image/x-portable-anymap";
            case "pntg":
                return "image/x-macpaint";
            case "po":
                return "text/x-gettext-translation";
            case "por":
                return "application/x-spss-por";
            case "pot":
                return "text/x-gettext-translation-template";
            case "ppm":
                return "image/x-portable-pixmap";
            case "pps":
                return "application/vnd.ms-powerpoint";
            case "ppt":
                return "application/vnd.ms-powerpoint";
            case "pptm":
                return "application/vnd.openxmlformats-officedocument.presentationml.presentation";
            case "pptx":
                return "application/vnd.openxmlformats-officedocument.presentationml.presentation";
            case "ppz":
                return "application/vnd.ms-powerpoint";
            case "prc":
                return "application/x-palm-database";
            case "ps":
                return "application/postscript";
            case "ps.bz2":
                return "application/x-bzpostscript";
            case "ps.gz":
                return "application/x-gzpostscript";
            case "psd":
                return "image/vnd.adobe.photoshop";
            case "psf":
                return "audio/x-psf";
            case "psf.gz":
                return "application/x-gz-font-linux-psf";
            case "psflib":
                return "audio/x-psflib";
            case "psid":
                return "audio/prs.sid";
            case "psw":
                return "application/x-pocket-word";
            case "pw":
                return "application/x-pw";
            case "py":
                return "text/x-python";
            case "pyc":
                return "application/x-python-bytecode";
            case "pyo":
                return "application/x-python-bytecode";
            case "qif":
                return "image/x-quicktime";
            case "qt":
                return "video/quicktime";
            case "qtif":
                return "image/x-quicktime";
            case "qtl":
                return "application/x-quicktime-media-link";
            case "qtvr":
                return "video/quicktime";
            case "ra":
                return "audio/vnd.rn-realaudio";
            case "raf":
                return "image/x-fuji-raf";
            case "ram":
                return "application/ram";
            case "rar":
                return "application/x-rar";
            case "ras":
                return "image/x-cmu-raster";
            case "raw":
                return "image/x-panasonic-raw";
            case "rax":
                return "audio/vnd.rn-realaudio";
            case "rb":
                return "application/x-ruby";
            case "rdf":
                return "application/rdf+xml";
            case "rdfs":
                return "application/rdf+xml";
            case "reg":
                return "text/x-ms-regedit";
            case "rej":
                return "application/x-reject";
            case "rgb":
                return "image/x-rgb";
            case "rle":
                return "image/rle";
            case "rm":
                return "application/vnd.rn-realmedia";
            case "rmj":
                return "application/vnd.rn-realmedia";
            case "rmm":
                return "application/vnd.rn-realmedia";
            case "rms":
                return "application/vnd.rn-realmedia";
            case "rmvb":
                return "application/vnd.rn-realmedia";
            case "rmx":
                return "application/vnd.rn-realmedia";
            case "roff":
                return "text/troff";
            case "rp":
                return "image/vnd.rn-realpix";
            case "rpm":
                return "application/x-rpm";
            case "rss":
                return "application/rss+xml";
            case "rt":
                return "text/vnd.rn-realtext";
            case "rtf":
                return "application/rtf";
            case "rtx":
                return "text/richtext";
            case "rv":
                return "video/vnd.rn-realvideo";
            case "rvx":
                return "video/vnd.rn-realvideo";
            case "s3m":
                return "audio/x-s3m";
            case "sam":
                return "application/x-amipro";
            case "sami":
                return "application/x-sami";
            case "sav":
                return "application/x-spss-sav";
            case "scm":
                return "text/x-scheme";
            case "sda":
                return "application/vnd.stardivision.draw";
            case "sdc":
                return "application/vnd.stardivision.calc";
            case "sdd":
                return "application/vnd.stardivision.impress";
            case "sdp":
                return "application/sdp";
            case "sds":
                return "application/vnd.stardivision.chart";
            case "sdw":
                return "application/vnd.stardivision.writer";
            case "sgf":
                return "application/x-go-sgf";
            case "sgi":
                return "image/x-sgi";
            case "sgl":
                return "application/vnd.stardivision.writer";
            case "sgm":
                return "text/sgml";
            case "sgml":
                return "text/sgml";
            case "sh":
                return "application/x-shellscript";
            case "shar":
                return "application/x-shar";
            case "shn":
                return "application/x-shorten";
            case "siag":
                return "application/x-siag";
            case "sid":
                return "audio/prs.sid";
            case "sik":
                return "application/x-trash";
            case "sis":
                return "application/vnd.symbian.install";
            case "sisx":
                return "x-epoc/x-sisx-app";
            case "sit":
                return "application/x-stuffit";
            case "siv":
                return "application/sieve";
            case "sk":
                return "image/x-skencil";
            case "sk1":
                return "image/x-skencil";
            case "skr":
                return "application/pgp-keys";
            case "slk":
                return "text/spreadsheet";
            case "smaf":
                return "application/x-smaf";
            case "smc":
                return "application/x-snes-rom";
            case "smd":
                return "application/vnd.stardivision.mail";
            case "smf":
                return "application/vnd.stardivision.math";
            case "smi":
                return "application/x-sami";
            case "smil":
                return "application/smil";
            case "sml":
                return "application/smil";
            case "sms":
                return "application/x-sms-rom";
            case "snd":
                return "audio/basic";
            case "so":
                return "application/x-sharedlib";
            case "spc":
                return "application/x-pkcs7-certificates";
            case "spd":
                return "application/x-font-speedo";
            case "spec":
                return "text/x-rpm-spec";
            case "spl":
                return "application/x-shockwave-flash";
            case "spx":
                return "audio/x-speex";
            case "sql":
                return "text/x-sql";
            case "sr2":
                return "image/x-sony-sr2";
            case "src":
                return "application/x-wais-source";
            case "srf":
                return "image/x-sony-srf";
            case "srt":
                return "application/x-subrip";
            case "ssa":
                return "text/x-ssa";
            case "stc":
                return "application/vnd.sun.xml.calc.template";
            case "std":
                return "application/vnd.sun.xml.draw.template";
            case "sti":
                return "application/vnd.sun.xml.impress.template";
            case "stm":
                return "audio/x-stm";
            case "stw":
                return "application/vnd.sun.xml.writer.template";
            case "sty":
                return "text/x-tex";
            case "sub":
                return "text/x-subviewer";
            case "sun":
                return "image/x-sun-raster";
            case "sv4cpio":
                return "application/x-sv4cpio";
            case "sv4crc":
                return "application/x-sv4crc";
            case "svg":
                return "image/svg+xml";
            case "svgz":
                return "image/svg+xml-compressed";
            case "swf":
                return "application/x-shockwave-flash";
            case "sxc":
                return "application/vnd.sun.xml.calc";
            case "sxd":
                return "application/vnd.sun.xml.draw";
            case "sxg":
                return "application/vnd.sun.xml.writer.global";
            case "sxi":
                return "application/vnd.sun.xml.impress";
            case "sxm":
                return "application/vnd.sun.xml.math";
            case "sxw":
                return "application/vnd.sun.xml.writer";
            case "sylk":
                return "text/spreadsheet";
            case "t":
                return "text/troff";
            case "t2t":
                return "text/x-txt2tags";
            case "tar":
                return "application/x-tar";
            case "tar.bz":
                return "application/x-bzip-compressed-tar";
            case "tar.bz2":
                return "application/x-bzip-compressed-tar";
            case "tar.gz":
                return "application/x-compressed-tar";
            case "tar.lzma":
                return "application/x-lzma-compressed-tar";
            case "tar.lzo":
                return "application/x-tzo";
            case "tar.xz":
                return "application/x-xz-compressed-tar";
            case "tar.z":
                return "application/x-tarz";
            case "tbz":
                return "application/x-bzip-compressed-tar";
            case "tbz2":
                return "application/x-bzip-compressed-tar";
            case "tcl":
                return "text/x-tcl";
            case "tex":
                return "text/x-tex";
            case "texi":
                return "text/x-texinfo";
            case "texinfo":
                return "text/x-texinfo";
            case "tga":
                return "image/x-tga";
            case "tgz":
                return "application/x-compressed-tar";
            case "theme":
                return "application/x-theme";
            case "themepack":
                return "application/x-windows-themepack";
            case "tif":
                return "image/tiff";
            case "tiff":
                return "image/tiff";
            case "tk":
                return "text/x-tcl";
            case "tlz":
                return "application/x-lzma-compressed-tar";
            case "tnef":
                return "application/vnd.ms-tnef";
            case "tnf":
                return "application/vnd.ms-tnef";
            case "toc":
                return "application/x-cdrdao-toc";
            case "torrent":
                return "application/x-bittorrent";
            case "tpic":
                return "image/x-tga";
            case "tr":
                return "text/troff";
            case "ts":
                return "application/x-linguist";
            case "tsv":
                return "text/tab-separated-values";
            case "tta":
                return "audio/x-tta";
            case "ttc":
                return "application/x-font-ttf";
            case "ttf":
                return "application/x-font-ttf";
            case "ttx":
                return "application/x-font-ttx";
            case "txt":
                return "text/plain";
            case "txz":
                return "application/x-xz-compressed-tar";
            case "tzo":
                return "application/x-tzo";
            case "ufraw":
                return "application/x-ufraw";
            case "ui":
                return "application/x-designer";
            case "uil":
                return "text/x-uil";
            case "ult":
                return "audio/x-mod";
            case "uni":
                return "audio/x-mod";
            case "uri":
                return "text/x-uri";
            case "url":
                return "text/x-uri";
            case "ustar":
                return "application/x-ustar";
            case "vala":
                return "text/x-vala";
            case "vapi":
                return "text/x-vala";
            case "vcf":
                return "text/directory";
            case "vcs":
                return "text/calendar";
            case "vct":
                return "text/directory";
            case "vda":
                return "image/x-tga";
            case "vhd":
                return "text/x-vhdl";
            case "vhdl":
                return "text/x-vhdl";
            case "viv":
                return "video/vivo";
            case "vivo":
                return "video/vivo";
            case "vlc":
                return "audio/x-mpegurl";
            case "vob":
                return "video/mpeg";
            case "voc":
                return "audio/x-voc";
            case "vor":
                return "application/vnd.stardivision.writer";
            case "vst":
                return "image/x-tga";
            case "wav":
                return "audio/x-wav";
            case "wax":
                return "audio/x-ms-asx";
            case "wb1":
                return "application/x-quattropro";
            case "wb2":
                return "application/x-quattropro";
            case "wb3":
                return "application/x-quattropro";
            case "wbmp":
                return "image/vnd.wap.wbmp";
            case "wcm":
                return "application/vnd.ms-works";
            case "wdb":
                return "application/vnd.ms-works";
            case "webm":
                return "video/webm";
            case "wk1":
                return "application/vnd.lotus-1-2-3";
            case "wk3":
                return "application/vnd.lotus-1-2-3";
            case "wk4":
                return "application/vnd.lotus-1-2-3";
            case "wks":
                return "application/vnd.ms-works";
            case "wma":
                return "audio/x-ms-wma";
            case "wmf":
                return "image/x-wmf";
            case "wml":
                return "text/vnd.wap.wml";
            case "wmls":
                return "text/vnd.wap.wmlscript";
            case "wmv":
                return "video/x-ms-wmv";
            case "wmx":
                return "audio/x-ms-asx";
            case "wp":
                return "application/vnd.wordperfect";
            case "wp4":
                return "application/vnd.wordperfect";
            case "wp5":
                return "application/vnd.wordperfect";
            case "wp6":
                return "application/vnd.wordperfect";
            case "wpd":
                return "application/vnd.wordperfect";
            case "wpg":
                return "application/x-wpg";
            case "wpl":
                return "application/vnd.ms-wpl";
            case "wpp":
                return "application/vnd.wordperfect";
            case "wps":
                return "application/vnd.ms-works";
            case "wri":
                return "application/x-mswrite";
            case "wrl":
                return "model/vrml";
            case "wv":
                return "audio/x-wavpack";
            case "wvc":
                return "audio/x-wavpack-correction";
            case "wvp":
                return "audio/x-wavpack";
            case "wvx":
                return "audio/x-ms-asx";
            case "x3f":
                return "image/x-sigma-x3f";
            case "xac":
                return "application/x-gnucash";
            case "xbel":
                return "application/x-xbel";
            case "xbl":
                return "application/xml";
            case "xbm":
                return "image/x-xbitmap";
            case "xcf":
                return "image/x-xcf";
            case "xcf.bz2":
                return "image/x-compressed-xcf";
            case "xcf.gz":
                return "image/x-compressed-xcf";
            case "xhtml":
                return "application/xhtml+xml";
            case "xi":
                return "audio/x-xi";
            case "xla":
                return "application/vnd.ms-excel";
            case "xlc":
                return "application/vnd.ms-excel";
            case "xld":
                return "application/vnd.ms-excel";
            case "xlf":
                return "application/x-xliff";
            case "xliff":
                return "application/x-xliff";
            case "xll":
                return "application/vnd.ms-excel";
            case "xlm":
                return "application/vnd.ms-excel";
            case "xls":
                return "application/vnd.ms-excel";
            case "xlsm":
                return "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";
            case "xlsx":
                return "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";
            case "xlt":
                return "application/vnd.ms-excel";
            case "xlw":
                return "application/vnd.ms-excel";
            case "xm":
                return "audio/x-xm";
            case "xmf":
                return "audio/x-xmf";
            case "xmi":
                return "text/x-xmi";
            case "xml":
                return "application/xml";
            case "xpm":
                return "image/x-xpixmap";
            case "xps":
                return "application/vnd.ms-xpsdocument";
            case "xsl":
                return "application/xml";
            case "xslfo":
                return "text/x-xslfo";
            case "xslt":
                return "application/xml";
            case "xspf":
                return "application/xspf+xml";
            case "xul":
                return "application/vnd.mozilla.xul+xml";
            case "xwd":
                return "image/x-xwindowdump";
            case "xyz":
                return "chemical/x-pdb";
            case "xz":
                return "application/x-xz";
            case "w2p":
                return "application/w2p";
            case "z":
                return "application/x-compress";
            case "zabw":
                return "application/x-abiword";
            case "zip":
                return "application/zip";
        }
        return "application/octet-stream";
    }
}

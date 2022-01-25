package com.csicit.ace.platform.core.enums;

import com.csicit.ace.common.exception.RException;
import com.csicit.ace.common.utils.StringUtils;
import com.csicit.ace.common.utils.internationalization.InternationUtils;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.CharEncoding;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author yansiyang
 * @version V1.0
 * @date 2019/11/19 10:28
 */
public class PlatformImgsEnum {

    private static boolean base64 = false;

    public final static String IMAGE_PATH = "images";

    public final static String IMG_PREFIX = "platformImg-";


    /**
     * 获取 images 目录下文件列表
     *
     * @return
     * @author FourLeaves
     * @date 2020/8/7 11:49
     */
    public static List<String> getImageList(String type) {
        File file = new File(IMAGE_PATH);
        if (!file.exists()) {
            file.mkdir();
            return new ArrayList<>();
        }
        FilenameFilter filenameFilter = new FilenameFilter() {
            @Override
            public boolean accept(File dir, String name) {
                if (org.apache.commons.lang3.StringUtils.isNotBlank(type)) {
                    return name.startsWith(IMG_PREFIX + type);
                }
                return !dir.isDirectory();
            }
        };
        String[] imageNames = file.list(filenameFilter);
        if (imageNames != null && imageNames.length > 0) {
            return Arrays.asList(imageNames);
        }
        return new ArrayList<>();
    }


    /**
     * 获取最新上传的文件的名称
     *
     * @param type
     * @return
     * @author FourLeaves
     * @date 2020/8/7 14:39
     */
    public static String getLatestImg(String type) {
        List<String> imageNameList = getImageList(type);
        if (CollectionUtils.isNotEmpty(imageNameList)) {
            int count = 0;
            int max = 0;
            for (int i = 0; i < imageNameList.size(); i++) {
                String imageName = imageNameList.get(i);
                int tcount = Integer.parseInt(imageName.split(type + "-")[1].split("\\.")[0]);
                if (tcount > count) {
                    count = tcount;
                    max = i;
                }
            }
            return imageNameList.get(max);
        }
        return null;
    }

    /**
     * 创建新的图片名称
     *
     * @param type
     * @param fileName
     * @return
     * @author FourLeaves
     * @date 2020/8/7 14:40
     */
    public static String createNewImageName(String type, String fileName) {
        String suffix = fileName.split("\\.")[fileName.split("\\.").length - 1];
        String latestImageName = getLatestImg(type);
        if (org.apache.commons.lang3.StringUtils.isBlank(latestImageName)) {
            return IMG_PREFIX + type + "-1." + suffix;
        }
        int count = Integer.parseInt(latestImageName.split(type + "-")[1].split("\\.")[0]) + 1;
        return IMG_PREFIX + type + "-" + count + "." + suffix;
    }

    public static List<String> getDefaultImgList(String type) {
        ResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
        List<String> fileNames = new ArrayList<>();
        try {
            Resource[] resources = resolver.getResources("img/*.*");
            for (Resource file : resources) {
                if (file.getFilename().startsWith(IMG_PREFIX + type)) {
                    fileNames.add(file.getFilename());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return fileNames;
    }

    private static List<String> copyDefaultImgs() {
        if (!new File(PlatformImgsEnum.IMAGE_PATH).exists()) {
            new File(PlatformImgsEnum.IMAGE_PATH).mkdir();
        }
        ResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
        try {

            Resource[] resources = resolver.getResources("img/*.*");
            //File file = new ClassPathResource("img").getFile();
            // String imgPath = file.getPath();
            List<String> fileNames = new ArrayList<>();
            if (resources != null) {
                for (Resource file : resources) {
                    if (!new File(PlatformImgsEnum.IMAGE_PATH + File.separator + file.getFilename()).exists()) {
                        InputStream inputStream = file.getInputStream();
                        BufferedOutputStream bos = new BufferedOutputStream(
                                new FileOutputStream(PlatformImgsEnum.IMAGE_PATH + File.separator + file.getFilename()));
                        byte[] bs = new byte[1024 * 100];
                        int len;
                        while ((len = inputStream.read(bs)) != -1) {
                            bos.write(bs, 0, len);
                        }
                        bos.flush();
                        bos.close();
                        inputStream.close();
                    }
                    fileNames.add(file.getFilename());

                }
                return fileNames;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        throw new RException(InternationUtils.getInternationalMsg("EXCEPTION_FOR_DATA"));
    }

    public static String getDefaultImgName(String type) {
        if (StringUtils.isNotBlank(type)) {
            List<String> fileNames = copyDefaultImgs();
            String prefix = IMG_PREFIX + type + "-0";
            for (String fileName : fileNames) {
                if (fileName.startsWith(prefix)) {
                    return fileName;
                }
            }

        }
        throw new RException(InternationUtils.getInternationalMsg("EXCEPTION_FOR_DATA"));
    }


    private static String getStrByBytes(byte[] data) {
        try {
            if (!base64) {
                return new String(data, CharEncoding.ISO_8859_1);
            } else {
                BASE64Encoder encoder = new BASE64Encoder();
                return encoder.encode(data);
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new RException(InternationUtils.getInternationalMsg("UNKNOWN_EXCEPTION"));

        }
    }


    private static byte[] getBytes(String str) {
        try {
            if (!base64) {
                return str.getBytes(CharEncoding.ISO_8859_1);
            } else {
                BASE64Decoder decoder = new BASE64Decoder();
                byte[] b = decoder.decodeBuffer(str);
                // 处理数据
                for (int i = 0; i < b.length; ++i) {
                    if (b[i] < 0) {
                        b[i] += 256;
                    }
                }
                return b;
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new RException(InternationUtils.getInternationalMsg("UNKNOWN_EXCEPTION"));

        }
    }

    private static String getImgStr(String type) {
        if (StringUtils.isNotBlank(type)) {
            byte[] data;
            InputStream inputStream;
            try {
                inputStream = PlatformImgsEnum.class.getResourceAsStream("/img/" + getDefaultImgName(type));
                //data = new byte[inputStream.available()];
                data = readInputStream(inputStream);
                Thread.sleep(1000);
                inputStream.read(data);
                String str;
                if (!base64) {
                    str = new String(data, CharEncoding.ISO_8859_1);
                } else {
                    BASE64Encoder encoder = new BASE64Encoder();
                    str = encoder.encode(data);
                }

                inputStream.close();
                return str;
            } catch (Exception e) {
                e.printStackTrace();
                throw new RException(InternationUtils.getInternationalMsg("EMPTY_ARG_WITH_PARAM", getDefaultImgName(type)));
            }
        }
        throw new RException(InternationUtils.getInternationalMsg("EXCEPTION_FOR_DATA"));
    }

    private static byte[] readInputStream(InputStream inputStream) throws Exception {
        byte[] buffer = new byte[1024];
        int len = 0;
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        while ((len = inputStream.read(buffer)) != -1) {
            bos.write(buffer, 0, len);
        }
        bos.close();
        return bos.toByteArray();
    }
}

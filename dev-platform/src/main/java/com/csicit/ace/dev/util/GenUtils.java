package com.csicit.ace.dev.util;


import com.csicit.ace.common.pojo.domain.dev.MetaDatasourceDO;
import com.csicit.ace.common.pojo.domain.dev.MetaTableColDO;
import com.csicit.ace.common.pojo.domain.dev.MetaTableDO;
import com.csicit.ace.common.pojo.domain.dev.ProServiceDO;
import com.csicit.ace.common.utils.DateUtils;
import com.csicit.ace.common.utils.UuidUtils;
import org.apache.commons.configuration.Configuration;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.WordUtils;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.StringWriter;
import java.util.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;


/**
 * 代码生成器   工具类
 *
 * @author generator
 * @version V1.0
 * @date 2019-04-15 20:14:36
 */
public class GenUtils {

    //配置信息
    public final static Configuration config = ConfigFileUtils.getConfig("generator.properties");

    public static List<String> getCodeTemplates() {
        List<String> templates = new ArrayList<String>();
        templates.add("generator/domain.java.vm");
        templates.add("generator/Mapper.java.vm");
        templates.add("generator/Service.java.vm");
        templates.add("generator/ServiceImpl.java.vm");
        templates.add("generator/Controller.java.vm");
        templates.add("generator/BaseController.java.vm");
        return templates;
    }

    public static List<String> getServiceTemplates() {
        List<String> templates = new ArrayList<String>();
        templates.add("generator/domain.java.vm");
        templates.add("generator/Mapper.java.vm");
        templates.add("generator/Service.java.vm");
        templates.add("generator/ServiceImpl.java.vm");
        templates.add("generator/Controller.java.vm");
        templates.add("generator/application.yml.vm");
        templates.add("generator/application-dev.yml.vm");
        templates.add("generator/pom.xml.vm");
        templates.add("generator/Application.java.vm");
        templates.add("generator/BaseController.java.vm");
        return templates;
    }


    /**
     * 获取表信息
     *
     * @param table
     * @return
     * @author zuogang
     * @date 2019/12/27 10:19
     */
    public static TableDO getTableInfo(MetaTableDO table) {
        //表信息
        TableDO tableDO = new TableDO();
        tableDO.setTableName(table.getTableName());
        tableDO.setComments(table.getCaption());
        tableDO.setClassName(table.getObjectName());
        tableDO.setClassname(StringUtils.uncapitalize(table.getObjectName()));
        List<MetaTableColDO> cols = table.getCols();
        //列信息
        List<ColumnDO> columsList = new ArrayList<>();
        for (MetaTableColDO col : cols) {
            ColumnDO columnDO = new ColumnDO();
            columnDO.setColumnName(col.getTabColName());
            columnDO.setDataType(col.getDataType());
            columnDO.setComments(col.getCaption());
            //列名转换成Java属性名
            String attrName = columnToJava(columnDO.getColumnName());
            columnDO.setAttrName(attrName);
            columnDO.setAttrname(StringUtils.uncapitalize(attrName));
            if (col.getTabColName().toUpperCase().startsWith("IS_")) {
                columnDO.setAttrName(attrName.substring(2, attrName.length()));
                columnDO.setAttrname(StringUtils.uncapitalize(attrName.substring(2, attrName.length())));
            }

            //列的数据类型，转换成Java类型
            String attrType = config.getString(columnDO.getDataType(), "String");
            columnDO.setAttrType(attrType);
            if (Objects.equals(col.getTabColName().toUpperCase(), "ID")) {
                tableDO.setPk(columnDO);
            }
            if (Objects.equals("0", col.getDbExistFlg())) {
                columnDO.setDbExistFlg("0");
            }
            columsList.add(columnDO);
        }
        tableDO.setColumns(columsList);
        return tableDO;
    }

    /**
     * 封装模板数据
     *
     * @param app
     * @param tableDO
     * @return
     * @author zuogang
     * @date 2019/12/27 10:23
     */
    public static VelocityContext getContextInfo(ProServiceDO app, TableDO tableDO, MetaDatasourceDO datasourceDO) {
        //封装模板数据
        Map<String, Object> map = new HashMap<>(16);
        String packageName = app.getPackageName();
        int enableAuth = app.getEnableAuth();
        int enableLog = app.getEnableLog();
        map.put("id", UuidUtils.createUUID());
        map.put("tableName", tableDO.getTableName());
        map.put("comments", StringUtils.isEmpty(tableDO.getComments()) ? "" : tableDO.getComments());
        map.put("pk", tableDO.getPk());
        map.put("className", tableDO.getClassName());
        map.put("classname", tableDO.getClassname());
        map.put("pathName", packageName.substring(packageName.lastIndexOf(".") + 1));
        map.put("columns", tableDO.getColumns());
        map.put("package", packageName);
        map.put("author", config.getString("generator"));
        map.put("email", config.getString("generator@csicit.com"));
        map.put("datetime", DateUtils.format(new Date(), DateUtils.DATE_TIME_PATTERN));
        map.put("enableAuth", enableAuth);
        map.put("enableLog", enableLog);
        map.put("appId", app.getAppId());
        map.put("appIdCapitalize", StringUtils.capitalize(app.getAppId()));
        map.put("ipPort", app.getIpPort());
        //nacos
        map.put("nacosServerAddr", app.getNacosServerAddr());
        //Redis
        map.put("redisDataBase", app.getRedisDataBase());
        map.put("redisHost", app.getRedisHost());
        map.put("redisPort", app.getRedisPort());
        map.put("redisPassword", app.getRedisPassword());
        // 连接数据库
        map.put("driverClassName", datasourceDO.getDriver());
        map.put("url", datasourceDO.getUrl());
        map.put("userName", datasourceDO.getUserName());
        map.put("password", datasourceDO.getPassword());
        VelocityContext context = new VelocityContext(map);
        return context;
    }



    /**
     * 生成代码
     *
     * @param table
     * @param app
     * @param zip
     * @return
     * @author generator
     * @date 2019/4/22 17:06
     */
    public static void generatorCode(MetaTableDO table, ProServiceDO app, ZipOutputStream zip, MetaDatasourceDO
            datasourceDO) {
        //表信息
        TableDO tableDO = getTableInfo(table);
        //设置velocity资源加载器
        Properties prop = new Properties();
        prop.put("file.resource.loader.class", "org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader");
        Velocity.init(prop);
        //获取模板列表
        VelocityContext context = getContextInfo(app, tableDO, datasourceDO);

        List<String> templates = getCodeTemplates();
        String packagePath = getPackagePath(app.getPackageName());
        for (String template : templates) {
            //渲染模板
            StringWriter sw = new StringWriter();
            Template tpl = Velocity.getTemplate(template, "UTF-8");
            tpl.merge(context, sw);
            try {
                //添加到zip
                zip.putNextEntry(new ZipEntry(packagePath + getFileName(template, tableDO.getClassName()
                )));
                IOUtils.write(sw.toString(), zip, "UTF-8");
                IOUtils.closeQuietly(sw);
                zip.closeEntry();
            } catch (IOException e) {
                try {
                    throw new Exception("渲染模板失败，表名：" + tableDO.getTableName(), e);
                } catch (Exception e1) {
                    e1.printStackTrace();
                }
            }
        }
    }

    /**
     * 列名转换成Java属性名
     *
     * @param columnName
     * @return
     * @author generator
     * @date 2019/4/22 17:06
     */
    public static String columnToJava(String columnName) {
        return WordUtils.capitalizeFully(columnName, new char[]{'_'}).replace("_", "");
    }

    /**
     * 表名转换成Java类名
     *
     * @param tableName
     * @param tablePrefix
     * @param autoRemovePre
     * @return
     * @author generator
     * @date 2019/4/22 17:06
     */
    public static String tableToJava(String tableName, String tablePrefix, String autoRemovePre) {
        if ("true".equals(autoRemovePre)) {
            tableName = tableName.substring(tableName.indexOf("_") + 1);
        }
        if (StringUtils.isNotBlank(tablePrefix)) {
            tableName = tableName.replace(tablePrefix, "");
        }

        return columnToJava(tableName);
    }

    /**
     * 获取包路径
     */
    public static String getPackagePath(String packageName) {
        String[] names = packageName.split("\\.");
        String packagePath = "main" + File.separator + "java" + File.separator;
        StringJoiner joiner = new StringJoiner(File.separator);
        for (String name : names) {
            joiner.add(name);
//            packagePath = packagePath + name + File.separator;
        }
        return packagePath + joiner.toString() + File.separator;
    }

    /**
     * 获取文件名
     *
     * @param template
     * @param className
     * @return
     * @author generator
     * @date 2019/4/22 17:06
     */
    public static String getFileName(String template, String className) {

        if (template.contains("domain.java.vm")) {
            return "pojo" + File.separator + className + ".java";
        }

        if (template.contains("Mapper.java.vm")) {
            return "mapper" + File.separator + className + "Mapper.java";
        }

        if (template.contains("Service.java.vm")) {
            return "service" + File.separator + className + "Service.java";
        }

        if (template.contains("ServiceImpl.java.vm")) {
            return "service" + File.separator + "impl" + File.separator + className + "ServiceImpl.java";
        }

        if (template.contains("BaseController.java.vm")) {
            return "controller" + File.separator + "BaseController.java";
        }

        if (template.contains("Controller.java.vm")) {
            return "controller" + File.separator + className + "Controller.java";
        }

        return null;
    }

    /**
     * 创建目录
     *
     * @param path
     * @return
     * @author zuogang
     * @date 2019/12/27 17:00
     */
    public static void createCatalog(String path) {
        File f = new File(path);
        System.out.println(f.exists());
        if (!f.exists()) {
            f.mkdirs(); //
        }
    }

    /**
     * 发布服务
     *
     * @param table
     * @param app
     * @param datasourceDO
     * @return
     * @author zuogang
     * @date 2019/12/27 10:10
     */
    public static void publishServiceCodes(MetaTableDO table, ProServiceDO app, MetaDatasourceDO datasourceDO) {
        String path = DevConstants.PUBLISH_SERVICE_LOCAL_CODE_ADDRESS + File.separator + app.getAppId() + File
                .separator + "src" + File
                .separator + "main" + File.separator;
        String javaPath = path + "java" + File.separator;
        String resourcesPath = path + "resources" + File.separator;
        String[] strs = app.getPackageName().split("\\.");
        List<String> names = Arrays.asList(strs);
        StringJoiner joiner = new StringJoiner(File.separator);
        for (String name : names) {
            joiner.add(name);
        }
        javaPath = javaPath + joiner.toString() + File.separator;
        //java文件目录
        createCatalog(javaPath);

        //添加resources目录下的static和templates文件夹
        createCatalog(resourcesPath + "static");
        createCatalog(resourcesPath + "templates");
        createCatalog(resourcesPath + "mapper");

        //表信息
        TableDO tableDO = getTableInfo(table);
        //设置velocity资源加载器
        Properties prop = new Properties();
        prop.put("file.resource.loader.class", "org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader");
        Velocity.init(prop);
        //获取模板列表
        VelocityContext context = getContextInfo(app, tableDO, datasourceDO);

        List<String> templates = getServiceTemplates();
        for (String template : templates) {
            //渲染模板
            StringWriter sw = new StringWriter();
            Template tpl = Velocity.getTemplate(template, "UTF-8");
            tpl.merge(context, sw);

            File file;
            if (template.contains("application.yml.vm")) {
                file = new File(resourcesPath, "application.yml");
            } else if (template.contains("application-dev.yml.vm")) {
                file = new File(resourcesPath, "application-dev.yml");
            } else if (template.contains("pom.xml.vm")) {
                file = new File(DevConstants.PUBLISH_SERVICE_LOCAL_CODE_ADDRESS + File.separator + app.getAppId() +
                        File.separator, "pom" +
                        ".xml");
            } else if (template.contains("Application.java.vm")) {
                file = new File(javaPath, StringUtils.capitalize(app.getAppId()) + "Application.java");
            } else {
                file = new File(javaPath, getFileName(template, tableDO.getClassName()));
            }

            //判断目标文件所在的目录是否存在
            if (!file.getParentFile().exists()) {
                //如果目标文件所在的目录不存在，则创建父目录
                file.getParentFile().mkdirs();
            }
            //创建目标文件
            try {
                if (file.exists()) {
                    file.createNewFile();
                }
                FileOutputStream in = new FileOutputStream(file);
                IOUtils.write(sw.toString(), in, "UTF-8");
                IOUtils.closeQuietly(sw);
                in.close();
            } catch (IOException e) {
                e.printStackTrace();
            }


        }
    }
}

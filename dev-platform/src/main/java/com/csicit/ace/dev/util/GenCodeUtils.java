package com.csicit.ace.dev.util;

import com.csicit.ace.common.pojo.domain.dev.ProDatasourceDO;
import com.csicit.ace.common.pojo.domain.dev.ProModelColDO;
import com.csicit.ace.common.pojo.domain.dev.ProModelDO;
import com.csicit.ace.common.pojo.domain.dev.ProServiceDO;
import com.csicit.ace.common.utils.DateUtils;
import com.csicit.ace.common.utils.StringUtils;
import com.csicit.ace.common.utils.UuidUtils;
import org.apache.commons.configuration.Configuration;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.WordUtils;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;

import java.io.File;
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
public class GenCodeUtils {
    //配置信息
    public final static Configuration config = ConfigFileUtils.getConfig("generator.properties");

    public static List<String> getCodeTemplates(Integer flowExist) {
        List<String> templates = new ArrayList<String>();
        templates.add("generator/domain.java.vm");
        templates.add("generator/Mapper.java.vm");
        templates.add("generator/Service.java.vm");
        templates.add("generator/ServiceImpl.java.vm");
        templates.add("generator/Controller.java.vm");
        templates.add("generator/frame/list.vue.vm");
        if (flowExist == null || flowExist == 0) {
            templates.add("generator/frame/add-or-update.vue.vm");
        } else if (flowExist == 1) {
            templates.add("generator/frame/flow-form.vue.vm");
        }
        return templates;
    }

    public static List<String> getServiceTemplates(Integer flowExist) {
        List<String> templates = new ArrayList<String>();
        templates.add("generator/domain.java.vm");
        templates.add("generator/Mapper.java.vm");
        templates.add("generator/Service.java.vm");
        templates.add("generator/ServiceImpl.java.vm");
        templates.add("generator/Controller.java.vm");
        templates.add("generator/application.yml.vm");
        templates.add("generator/application-dev.yml.vm");
        templates.add("generator/bootstrap.yml.vm");
        templates.add("generator/pom.xml.vm");
        templates.add("generator/Application.java.vm");
        templates.add("generator/BaseController.java.vm");
        templates.add("generator/CommonController.java.vm");
        templates.add("generator/frame/list.vue.vm");
        if (flowExist == null || flowExist == 0) {
            templates.add("generator/frame/add-or-update.vue.vm");
        } else if (flowExist == 1) {
            templates.add("generator/frame/flow-form.vue.vm");
        }
        return templates;
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
     * 创建目录
     *
     * @param path
     * @return
     * @author zuogang
     * @date 2019/12/27 17:00
     */
    public static void createCatalog(String path) {
        File f = new File(path);
        if (!f.exists()) {
            f.mkdirs(); //
        }
    }

    /**
     * 生成代码
     *
     * @param proModelDOS
     * @param app
     * @param zip
     * @return
     * @author generator
     * @date 2019/4/22 17:06
     */
    public static void generatorCodeByService(List<ProModelDO> proModelDOS, ProServiceDO app, ZipOutputStream zip,
                                              ProDatasourceDO
                                                      datasourceDO) {
        // 前端服务代码
        String servicePathFrame = app.getAppId() + "-frame" + File
                .separator + "src" + File.separator;
        // 后端服务代码
        String servicePath = app.getAppId() + File
                .separator;
        String path = servicePath + "src" + File
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

        for (int i = 0; i < proModelDOS.size(); i++) {

            //表信息
            TableDO tableDO = getTableInfoByService(proModelDOS.get(i));
            //设置velocity资源加载器
            Properties prop = new Properties();
            prop.put("file.resource.loader.class", "org.apache.velocity.runtime.resource.loader" +
                    ".ClasspathResourceLoader");
            Velocity.init(prop);
            //获取模板列表
            VelocityContext context = getContextInfo(app, tableDO, datasourceDO, proModelDOS.get(i).getFileExist()
                    , proModelDOS.get(i).getFlowExist());
            List<String> templates;
            if (i == 0) {
                templates = getServiceTemplates(proModelDOS.get(i).getFlowExist());
            } else {
                templates = getCodeTemplates(proModelDOS.get(i).getFlowExist());
            }


            for (String template : templates) {
                //渲染模板
                StringWriter sw = new StringWriter();
                Template tpl = Velocity.getTemplate(template, "UTF-8");
                tpl.merge(context, sw);
                try {
                    //添加到zip
                    if (template.contains("application.yml.vm")) {
                        zip.putNextEntry(new ZipEntry(resourcesPath + "application.yml"));
                    } else if (template.contains("application-dev.yml.vm")) {
                        zip.putNextEntry(new ZipEntry(resourcesPath + "application-dev.yml"));
                    } else if (template.contains("bootstrap.yml.vm")) {
                        zip.putNextEntry(new ZipEntry(resourcesPath + "bootstrap.yml"));
                    } else if (template.contains("pom.xml.vm")) {
                        zip.putNextEntry(new ZipEntry(servicePath + "pom.xml"));
                    } else if (template.contains("Application.java.vm")) {
                        zip.putNextEntry(new ZipEntry(javaPath + StringUtils.capitalize(app.getAppId()) +
                                "Application.java"));
                    } else if (template.contains("BaseController.java.vm")) {
                        zip.putNextEntry(new ZipEntry(javaPath + "controller" + File.separator +
                                "BaseController.java"));
                    } else if (template.contains("CommonController.java.vm")) {
                        zip.putNextEntry(new ZipEntry(javaPath + "controller" + File.separator +
                                "CommonController.java"));
                    } else if (template.contains("list.vue.vm")) {
                        zip.putNextEntry(new ZipEntry(servicePathFrame + tableDO.getClassname() + File.separator +
                                "views" + File.separator + tableDO.getClassname() + "-list.vue"));
                    } else if (template.contains("add-or-update.vue.vm")) {
                        zip.putNextEntry(new ZipEntry(servicePathFrame + tableDO.getClassname() + File.separator +
                                "components" + File.separator + "add-or-update.vue"));
                    } else if (template.contains("flow-form.vue.vm")) {
                        zip.putNextEntry(new ZipEntry(servicePathFrame + tableDO.getClassname() + File.separator +
                                "components" + File.separator + "flow-add-or-update.vue"));
                    } else {
                        zip.putNextEntry(new ZipEntry(javaPath + getFileName(template, tableDO.getClassName()
                        )));
                    }

                    IOUtils.write(sw.toString(), zip, "UTF-8");
                    IOUtils.closeQuietly(sw);
                    zip.closeEntry();
                } catch (IOException e) {
                    try {
                        System.out.println("-----------------");
                        System.out.println(e.getMessage());
                        System.out.println("-----------------");
                        throw new Exception("渲染模板失败，表名：" + tableDO.getTableName(), e);
                    } catch (Exception e1) {
                        e1.printStackTrace();
                    }
                }
            }
        }


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

        if (template.contains("Controller.java.vm")) {
            return "controller" + File.separator + className + "Controller.java";
        }

        return null;
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
    public static VelocityContext getContextInfo(ProServiceDO app, TableDO tableDO, ProDatasourceDO datasourceDO,
                                                 Integer fileExist, Integer flowExist) {
        //封装模板数据
        Map<String, Object> map = new HashMap<>(16);
        String packageName = app.getPackageName();
        int enableAuth = app.getEnableAuth();
        int enableLog = app.getEnableLog();
        map.put("fileExist", fileExist);
        map.put("flowExist", flowExist);
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

    public static TableDO getTableInfoByService(ProModelDO table) {
        TableDO tableDO = new TableDO();
        tableDO.setTableName(table.getTableName());
        tableDO.setComments(table.getRemark());
        String className = table.getObjectName().substring(0, 1).toUpperCase() + table.getObjectName().substring(1,
                table.getObjectName().length());
        tableDO.setClassName(className);
        tableDO.setClassname(StringUtils.uncapitalize(table.getObjectName()));
        List<ProModelColDO> cols = table.getProModelColDOS();
        //列信息
        List<ColumnDO> columsList = new ArrayList<>();
        for (ProModelColDO col : cols) {
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
            if (Objects.equals(1, col.getSyscol())) {
                columnDO.setDbExistFlg("1");
            }
            columsList.add(columnDO);
        }
        tableDO.setColumns(columsList);
        return tableDO;
    }
}

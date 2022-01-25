package com.csicit.ace.quartz.core.plugins;

import com.alibaba.druid.sql.visitor.functions.Substring;
import com.csicit.ace.common.pojo.domain.QrtzConfigDO;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.spi.ClassLoadHelper;
import org.quartz.spi.SchedulerPlugin;
import org.quartz.xml.ValidationException;
import org.quartz.xml.XMLSchedulingDataProcessor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPathException;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.text.ParseException;
import java.util.List;


/**
 * @author zuogang
 * @date Created in 8:28 2019/7/16
 */
@Configuration
public class AceJobConfigLoaderPlugin implements SchedulerPlugin {

    private Logger logger = LoggerFactory.getLogger(getClass());
    protected ClassLoadHelper classLoadHelper = null;
    private Scheduler scheduler;
    private String name;

    public static List<QrtzConfigDO> configs;


    public AceJobConfigLoaderPlugin() {
    }

    public Scheduler getScheduler() {
        return scheduler;
    }

    @Override
    public void initialize(String s, Scheduler scheduler, ClassLoadHelper classLoadHelper) {
        this.name = s;
        this.scheduler = scheduler;
        this.classLoadHelper = classLoadHelper;
        this.classLoadHelper.initialize();
        logger.info("Registering Quartz Job Initialization Plug-in.");
    }

    @Override
    public void start() {
        String xml = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>\n" +
                "<job-scheduling-data xmlns=\"http://www.quartz-scheduler.org/xml/JobSchedulingData\">\n" +
                "    <schedule>\n" +
                "        <job>\n" +
                "            <name>大屏消息推送</name>\n" +
                "            <group>业务平台内部定时任务</group>\n" +
                "            <description></description>\n" +
                "            <job-class>com.csicit.ace.quartz.core.job.AgentJob</job-class>\n" +
                "            <durability>true</durability>\n" +
                "            <recover>false</recover>\n" +
                "            <job-data-map>\n" +
                "                <entry>\n" +
                "                    <key>paramAppId</key>\n" +
                "                    <value>platform</value>\n" +
                "                </entry>\n" +
                "                <entry>\n" +
                "                    <key>paramUrl</key>\n" +
                "                    <value>/bladeVisualMsgs/bladeVisual/msg/push_GET</value>\n" +
                "                </entry>\n" +
                "            </job-data-map>\n" +
                "        </job>\n" +
                "        <trigger>\n" +
                "            <cron>\n" +
                "                <name>每5分钟执行一次</name>\n" +
                "                <group>大屏消息推送$业务平台内部定时任务</group>\n" +
                "                <description></description>\n" +
                "                <job-name>大屏消息推送</job-name>\n" +
                "                <job-group>业务平台内部定时任务</job-group>\n" +
                "                <cron-expression>0 */5 * * * ?</cron-expression>\n" +
                "            </cron>\n" +
                "        </trigger>\n" +
                "    </schedule>\n" +
                "</job-scheduling-data>\n";
        QrtzConfigDO qrtzConfigDO1 = new QrtzConfigDO();
        qrtzConfigDO1.setConfig(xml);
        configs.add(qrtzConfigDO1);

        configs.stream().forEach(qrtzConfigDO -> {
            XMLSchedulingDataProcessor processor = null;
            try {
                processor = new XMLSchedulingDataProcessor(classLoadHelper);
                java.io.InputStream stream = new ByteArrayInputStream(
                        qrtzConfigDO.getConfig().getBytes("UTF-8"));
                processor.processStreamAndScheduleJobs(stream,
                        " ",
                        scheduler);

            } catch (ParserConfigurationException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (XPathException e) {
                e.printStackTrace();
            } catch (SAXException e) {
                e.printStackTrace();
            } catch (SchedulerException e) {
                e.printStackTrace();
            } catch (ValidationException e) {
                e.printStackTrace();
            } catch (ParseException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });


    }

    @Override
    public void shutdown() {

    }
}

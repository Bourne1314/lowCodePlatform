package com.csicit.ace.quartz.core.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.csicit.ace.common.pojo.domain.QrtzConfigDO;
import com.csicit.ace.common.utils.JsonUtils;
import com.csicit.ace.common.utils.StringUtils;
import com.csicit.ace.common.utils.XmlUtils;
import com.csicit.ace.data.persistent.mapper.QrtzConfigMapper;
import com.csicit.ace.data.persistent.service.impl.BaseServiceImpl;
import com.csicit.ace.quartz.core.service.QrtzConfigService;
import com.csicit.ace.quartz.core.utils.*;
import com.csicit.ace.quartz.core.vo.JobDetailVO;
import org.quartz.*;
import org.quartz.impl.matchers.GroupMatcher;
import org.quartz.spi.ClassLoadHelper;
import org.quartz.xml.XMLSchedulingDataProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;
import org.springframework.stereotype.Service;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import javax.xml.parsers.ParserConfigurationException;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.ZoneId;
import java.util.*;


/**
 * 批处理任务配置 实例对象访问接口实现
 *
 * @author generator
 * @version V1.0
 * @date 2019-07-16 09:59:40
 */
@Service("qrtzConfigService")
public class QrtzConfigServiceImpl extends BaseServiceImpl<QrtzConfigMapper, QrtzConfigDO> implements
        QrtzConfigService {

    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    @Autowired
    private SchedulerFactoryBean schedulerFactoryBean;

    /**
     * 获取Scheduler容器状态
     *
     * @return
     * @author zuogang
     * @date 2019/8/6 8:46
     */
    @Override
    public SchedulerMetaData getSchedulerState() {


        Scheduler sched = schedulerFactoryBean.getScheduler();
        SchedulerMetaData meta = null;
        try {
            meta = sched.getMetaData();

        } catch (SchedulerException e) {
            e.printStackTrace();
        }

        return meta;
    }

    /**
     * 编辑运行监控的任务列表
     *
     * @return
     * @author zuogang
     * @date 2019/8/6 8:46
     */
    @Override
    public List<JobDetailVO> getOperateDataList() {

        List<JobDetailVO> list = new ArrayList<>(16);
        Scheduler sched = schedulerFactoryBean.getScheduler();

        if (sched != null) {
            try {
                List<String> grpNames = sched.getJobGroupNames();
                if (sched != null && grpNames.size() > 0) {
                    for (String groupName : sched.getJobGroupNames()) {
                        // 任务组
                        JobDetailVO groupVo = new JobDetailVO();
                        groupVo.setName(groupName);

                        List<JobDetailVO> jobDetailVOS = new ArrayList<>(16);
                        for (JobKey jobKey : sched.getJobKeys(GroupMatcher.jobGroupEquals(groupName))) {

                            String jobName = jobKey.getName();
                            String jobGroup = jobKey.getGroup();
                            // 任务
                            JobDetailVO jobDetailVO = new JobDetailVO();
                            jobDetailVO.setJobOrTriggerName(jobName);
                            jobDetailVO.setJobOrTriggerGroup(jobGroup);
                            jobDetailVO.setName(jobGroup + "_" + jobName);


                            //get job's trigger
                            List<Trigger> triggers = (List<Trigger>) sched.getTriggersOfJob(jobKey);

                            // 触发器
                            List<JobDetailVO> triggerLists = new ArrayList<>(16);
                            triggers.stream().forEach(trigger -> {

                                try {
                                    JobDetailVO trigger1 = new JobDetailVO();
                                    trigger1.setName(trigger.getKey().getGroup() + "_" + trigger.getKey()
                                            .getName());
                                    trigger1.setJobOrTriggerName(trigger.getKey()
                                            .getName());
                                    trigger1.setJobOrTriggerGroup(trigger.getKey().getGroup());
                                    if (trigger.getPreviousFireTime() != null) {
                                        trigger1.setPreviousFireTime(trigger.getPreviousFireTime().toInstant().atZone
                                                (ZoneId.systemDefault())
                                                .toLocalDateTime());
                                    }
                                    if (trigger.getNextFireTime() != null) {
                                        trigger1.setNextFireTime(trigger.getNextFireTime().toInstant().atZone(ZoneId
                                                .systemDefault())
                                                .toLocalDateTime());
                                    }
                                    Trigger.TriggerState state = sched.getTriggerState(trigger.getKey());
                                    trigger1.setStatus(state.toString());
                                    triggerLists.add(trigger1);
                                } catch (SchedulerException e) {
                                    e.printStackTrace();
                                }

                            });

                            jobDetailVO.setChildren(triggerLists);
                            jobDetailVOS.add(jobDetailVO);
                        }

                        groupVo.setChildren(jobDetailVOS);
                        list.add(groupVo);
                    }
                }

            } catch (SchedulerException e) {
                e.printStackTrace();
            }
        }

        return list;
    }


    /**
     * 编辑运行监控的任务列表
     *
     * @param id
     * @return
     * @author zuogang
     * @date 2019/8/6 8:46
     */
    @Override
    public List<JobDetailVO> getTaskConfigDataList(String id) {

        QrtzConfigDO instance = getById(id);

        List<JobDetailType> jobDetailTypes = getJobDetailType(instance);
        List<CronTriggerType> cronTriggerTypes = getCronTriggerType(instance);

        List<JobDetailVO> list = new ArrayList<>(16);

//        if (jobDetailTypes != null && jobDetailTypes.size() == 0) {
//            return list;
//        }

        JobDetailVO allJobVo = new JobDetailVO();
        allJobVo.setName("所有任务");
        allJobVo.setIcon("el-icon-s-operation");
        allJobVo.setType(4);


        //获取所有的任务组名称列表
        List<String> groups = new ArrayList<>(16);

        jobDetailTypes.stream().forEach(jobDetailType -> {
            if (!groups.contains(jobDetailType.getGroup())) {
                groups.add(jobDetailType.getGroup());
            }
        });
        List<JobDetailVO> groupList = new ArrayList<>(16);

        groups.stream().forEach(group -> {
            JobDetailVO groupVo = new JobDetailVO();
            groupVo.setName(group);
            groupVo.setIcon("el-icon-folder-opened");
            groupVo.setType(0);

            List<JobDetailVO> jobVoList = new ArrayList<>(16);
            jobDetailTypes.stream().forEach(jobDetailType -> {
                if (Objects.equals(jobDetailType.getGroup(), group)) {
                    JobDetailVO jobVo = new JobDetailVO();
                    jobVo.setJobDetailType(jobDetailType);
                    jobVo.setIcon("el-icon-s-grid");
                    jobVo.setName(jobDetailType.getName());
                    jobVo.setType(1);

                    // 触发器
                    List<JobDetailVO> list1 = new ArrayList<>();
                    JobDetailVO t1 = new JobDetailVO();
                    t1.setName("触发器");
                    t1.setIcon("el-icon-folder-opened");

                    t1.setJobOrTriggerName(jobDetailType.getName());
                    t1.setJobOrTriggerGroup(jobDetailType.getGroup());
                    List<JobDetailVO> triggerVos = new ArrayList<>(16);
                    if (cronTriggerTypes != null && cronTriggerTypes.size() > 0) {
                        cronTriggerTypes.stream().forEach(cronTriggerType -> {
                            if (Objects.equals(cronTriggerType.getJobName(), jobDetailType.getName()) && Objects
                                    .equals(cronTriggerType.getJobGroup(), jobDetailType.getGroup())) {
                                JobDetailVO triggerVo = new JobDetailVO();
                                triggerVo.setCronTriggerType(cronTriggerType);
                                triggerVo.setIcon("el-icon-time");
                                triggerVo.setName(cronTriggerType.getName());
                                triggerVo.setType(2);

                                triggerVos.add(triggerVo);
                            }
                        });
                    }
                    t1.setChildren(triggerVos);
                    list1.add(t1);
                    // 运行参数
                    JobDetailVO p1 = new JobDetailVO();
                    p1.setName("运行参数");
                    p1.setIcon("el-icon-folder-opened");
                    p1.setJobOrTriggerName(jobDetailType.getName());
                    p1.setJobOrTriggerGroup(jobDetailType.getGroup());
                    List<JobDetailVO> params = new ArrayList<>(16);
                    if (jobDetailType.getJobDataMap() != null) {
                        List<EntryType> entry = jobDetailType.getJobDataMap().getEntry();
                        if (entry != null) {
                            for (int i = 0; i < entry.size(); i++) {
                                if (!Objects.equals(entry.get(i).getKey(), "paramAppId") && !Objects.equals(entry.get
                                        (i).getKey(), "paramUrl")) {
                                    JobDetailVO param = new JobDetailVO();
                                    param.setEntryType(entry.get(i));
                                    param.setIcon("el-icon-paperclip");
                                    param.setJobOrTriggerGroup(jobDetailType.getGroup());
                                    param.setJobOrTriggerName(jobDetailType.getName());
                                    param.setName(entry.get(i).getKey());
                                    param.setType(3);
                                    params.add(param);
                                }
                            }
                        }
                    }

                    p1.setChildren(params);

                    list1.add(p1);
                    jobVo.setChildren(list1);

                    jobVoList.add(jobVo);
                }
            });
            groupVo.setChildren(jobVoList);
            groupList.add(groupVo);
        });

        allJobVo.setChildren(groupList);
        list.add(allJobVo);

        return list;
    }

    /**
     * 获取JobDetail信息
     *
     * @param instance
     * @return java.util.List<JobDetailType>
     * @author zuogang
     * @date 2019/8/6 10:14
     */
    @Override
    public List<JobDetailType> getJobDetailType(QrtzConfigDO instance) {


        List<JobDetailType> jobDetailTypes = new ArrayList<>(16);

        if (instance != null && instance.getConfig().length() != 0) {
//            InputStream stream = null;
//            try {
//                stream = new ByteArrayInputStream(instance.getConfig().getBytes("UTF-8"));
//            } catch (UnsupportedEncodingException e) {
//                e.printStackTrace();
//            }

            Object result = XmlUtils.unmarshalXml(instance.getConfig(), JobSchedulingData.class);

            Object object = ((JobSchedulingData) result)
                    .getPreProcessingCommandsAndProcessingDirectivesAndSchedule().get(0);


            JSONArray jobs = JsonUtils.castObject(object, JSONObject.class).getJSONArray("job");
            jobs.forEach(job -> {
                jobDetailTypes.add(JsonUtils.castObject(job, JobDetailType.class));
            });

        }
        return jobDetailTypes;
    }

    /**
     * 获取CronTrigger信息
     *
     * @param instance
     * @return java.util.List<JobDetailType>
     * @author zuogang
     * @date 2019/8/6 10:15
     */
    private List<CronTriggerType> getCronTriggerType(QrtzConfigDO instance) {


        List<CronTriggerType> cronTriggerTypes = new ArrayList<>(16);

        if (instance != null && instance.getConfig().length() != 0) {
//            InputStream stream = null;
//            try {
//                stream = new ByteArrayInputStream(instance.getConfig().getBytes("UTF-8"));
//            } catch (UnsupportedEncodingException e) {
//                e.printStackTrace();
//            }

            Object result = XmlUtils.unmarshalXml(instance.getConfig(), JobSchedulingData.class);

            Object object = ((JobSchedulingData) result)
                    .getPreProcessingCommandsAndProcessingDirectivesAndSchedule().get(0);


            JSONArray cronTriggers = JsonUtils.castObject(object, JSONObject.class).getJSONArray("trigger");
            cronTriggers.forEach(triggers -> {
                JSONObject jsonFirst = JSONObject.parseObject(triggers.toString());
                JSONObject jsonSecond = (JSONObject) jsonFirst.get("cron");
                cronTriggerTypes.add(JsonUtils.castObject(jsonSecond, CronTriggerType.class));
            });
        }
        return cronTriggerTypes;
    }

    /**
     * XML反序列化
     *
     * @param s
     * @return java.lang.Object
     * @author zuogang
     * @date 2019/8/6 9:31
     */
//    private Object unmarshalXml(String s) {
//        Object result = null;
//        try {
//
//            Unmarshaller unmarshaller = JAXBContext.newInstance(JobSchedulingData.class).createUnmarshaller();
//            StringReader sr = new StringReader(s);
//            result = unmarshaller.unmarshal(sr);
//        } catch (JAXBException e) {
//            e.printStackTrace();
//        }
//        return result;
//    }

    /**
     * XML序列化
     *
     * @param
     * @return java.lang.Object
     * @author zuogang
     * @date 2019/8/6 9:31
     */
//    private StringWriter marshalToXml(Object root) {
//        StringWriter writer = new StringWriter();
//        try {
//            Marshaller jaxbMarshaller = JAXBContext.newInstance(JobSchedulingData.class)
//                    .createMarshaller();
//            jaxbMarshaller.setProperty(Marshaller.JAXB_ENCODING, "UTF-8");
//            jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
//            jaxbMarshaller.marshal(root, writer);
//
//        } catch (
//                JAXBException e)
//
//        {
//            e.printStackTrace();
//        }
//        return writer;
//    }


    /**
     * 获取JobDetail信息
     *
     * @param group
     * @param appId
     * @return java.util.List<JobDetailType>
     * @author zuogang
     * @date 2019/8/6 10:14
     */
    @Override
    public List<JobDetailType> allJobByGroup(String group, String appId) {
        List<JobDetailType> jobDetailTypes = new ArrayList<>(16);
        List<JobDetailType> allJobDetailTypeList = this.getJobDetailType(getById(appId));
        allJobDetailTypeList.stream().forEach(jobDetailType -> {
            if (Objects.equals(jobDetailType.getGroup(), group)) {
                jobDetailTypes.add(jobDetailType);
            }
        });
        return jobDetailTypes;
    }

    /**
     * 暂停任务
     *
     * @param group
     * @param group
     * @return java.lang.Boolean
     * @author zuogang
     * @date 2019/8/6 19:20
     */
    @Override
    public boolean pausedJob(String group, String name) {
        Scheduler scheduler = schedulerFactoryBean.getScheduler();

        try {
            TriggerKey triggerKey = new TriggerKey(name, group);
            scheduler.pauseTrigger(triggerKey);
        } catch (SchedulerException e) {
            e.printStackTrace();
        }
        return true;
    }

    /**
     * 运行任务
     *
     * @param name
     * @param group
     * @return java.lang.Boolean
     * @author zuogang
     * @date 2019/8/6 19:20
     */
    @Override
    public boolean resumeJob(String group, String name) {
        Scheduler scheduler = schedulerFactoryBean.getScheduler();
        try {
            scheduler.resumeTrigger(new TriggerKey(name, group));
        } catch (SchedulerException e) {
            e.printStackTrace();
        }
        return true;
    }

    /**
     * 新增任务
     *
     * @param params
     * @return java.lang.Boolean
     * @author zuogang
     * @date 2019/8/6 19:20
     */
    @Override
    public boolean addJob(Map<String, Object> params) {
        String id = (String) params.get("id");
        List<JobDetailType> jobDetailTypeList = this.getJobDetailType(getById(id));
        List<CronTriggerType>
                cronTriggerTypes = this.getCronTriggerType(getById(id));

        JobDetailType newJob = new JobDetailType();
        newJob.setGroup((String) params.get("group"));
        newJob.setName((String) params.get("name"));
        newJob.setDescription((String) params.get("description"));
        newJob.setJobClass("com.csicit.ace.quartz.core.job.AgentJob");
        // 还没有触发器
        newJob.setDurability(true);
        newJob.setRecover(false);


        List<EntryType> entryTypes = new ArrayList<>(16);
        List<LinkedHashMap> entryList = (List<LinkedHashMap>) params.get("entry");
        entryList.stream().forEach(entry -> {
            EntryType entryType = new EntryType();
            entryType.setKey((String) entry.get("key"));
            entryType.setValue((String) entry.get("value"));
            entryTypes.add(entryType);
        });
        JobDataMapType jobDataMapType = new JobDataMapType();
        jobDataMapType.setEntry(entryTypes);

        newJob.setJobDataMap(jobDataMapType);

        jobDetailTypeList.add(newJob);


        // 重新执行任务并更新数据库
        if (!this.newJobStartAndDbUpdate(id, jobDetailTypeList, cronTriggerTypes))
            return false;

        return true;
    }

    /**
     * 更新任务
     *
     * @param params
     * @return java.lang.Boolean
     * @author zuogang
     * @date 2019/8/6 19:20
     */
    @Override
    public boolean updJob(Map<String, Object> params) {
        String id = (String) params.get("id");
        List<JobDetailType> jobDetailTypeList = this.getJobDetailType(getById(id));
        List<CronTriggerType>
                cronTriggerTypes = this.getCronTriggerType(getById(id));

        List<LinkedHashMap> entryList = (List<LinkedHashMap>) params.get("entry");

        jobDetailTypeList.stream().forEach(jobDetailType -> {
            if (Objects.equals(jobDetailType.getGroup(), params.get("group")) && Objects.equals(jobDetailType.getName
                    (), params.get("name"))) {
                jobDetailType.setDescription((String) params.get("description"));
                List<EntryType> entry = jobDetailType.getJobDataMap().getEntry();
                entry.stream().forEach(entryType -> {
                    entryList.stream().forEach(em -> {
                        if (Objects.equals(entryType.getKey(), em.get("key"))) {
                            entryType.setValue((String) em.get("value"));
                        }
                    });
                });
            }
        });

        // 重新执行任务并更新数据库
        if (!this.newJobStartAndDbUpdate(id, jobDetailTypeList, cronTriggerTypes))
            return false;

        return true;
    }

    /**
     * 删除任务
     *
     * @param params
     * @return java.lang.Boolean
     * @author zuogang
     * @date 2019/8/6 19:20
     */
    @Override
    public boolean delJob(Map<String, Object> params) {
        String id = (String) params.get("id");
        String name = (String) params.get("name");
        String group = (String) params.get("group");
        List<JobDetailType> jobDetailTypeList = this.getJobDetailType(getById(id));
        List<CronTriggerType>
                cronTriggerTypes = this.getCronTriggerType(getById(id));

        List<JobDetailType> newJobDetailTypeList = new ArrayList<>(16);
        jobDetailTypeList.stream().forEach(jobDetailType -> {
            if (!(Objects.equals(jobDetailType.getGroup(), group) && Objects.equals(jobDetailType.getName
                    (), name))) {
                newJobDetailTypeList.add(jobDetailType);
            }
        });

        List<CronTriggerType> newCronTriggerTypes = new ArrayList<>(16);
        cronTriggerTypes.stream().forEach(cronTriggerType -> {
            if (!(Objects.equals(cronTriggerType.getJobName(), name) && Objects.equals(cronTriggerType.getJobGroup(),
                    group))) {
                newCronTriggerTypes.add(cronTriggerType);
            }
        });


        // 删除任务
        Scheduler scheduler = schedulerFactoryBean.getScheduler();
        JobKey key = new JobKey(name, group);
        JobDetail j = null;
        try {
            j = scheduler.getJobDetail(key);
            if (j != null) scheduler.deleteJob(key);
        } catch (SchedulerException e) {
            e.printStackTrace();
        }


        // 重新执行任务并更新数据库
        if (!this.newJobStartAndDbUpdate(id, newJobDetailTypeList, newCronTriggerTypes))
            return false;

        return true;
    }

    /**
     * 根据group和name获取当前jonDetail信息
     *
     * @param params
     * @return java.lang.Boolean
     * @author zuogang
     * @date 2019/8/6 19:20
     */
    @Override
    public JobDetailType getJobDetail(Map<String, Object> params) {
        String id = (String) params.get("id");
        String jobGroup = (String) params.get("jobGroup");
        String jobName = (String) params.get("jobName");
        JobDetailType jobDetailType = null;
        List<JobDetailType> jobDetailTypes = this.getJobDetailType(getById(id));
        for (JobDetailType job : jobDetailTypes) {
            if (Objects.equals(job.getGroup(), jobGroup) && Objects.equals(job.getName(), jobName)) {
                jobDetailType = job;
            }
        }
        return jobDetailType;
    }


    /**
     * 判断该任务下是否存在同名触发器
     *
     * @param params
     * @return java.lang.Boolean
     * @author zuogang
     * @date 2019/8/6 19:20
     */
    @Override
    public boolean existTrigger(Map<String, Object> params) {
        String id = (String) params.get("id");
        String jobGroup = (String) params.get("jobGroup");
        String jobName = (String) params.get("jobName");
        String name = (String) params.get("name");
        List<CronTriggerType> cronTriggerTypes = this.getCronTriggerType(getById(id));
        List<String> existNames = new ArrayList<>(16);
        cronTriggerTypes.stream().forEach(cronTriggerType -> {
            if (Objects.equals(cronTriggerType.getJobGroup(), jobGroup) && Objects.equals(cronTriggerType.getJobName(),
                    jobName)) {
                existNames.add(cronTriggerType.getName());
            }
        });

        if (existNames.contains(name)) {
            // 判断该任务下是否存在同名触发器
            return false;
        }
        return true;
    }

    /**
     * 判断任务组下是否已经存在同名任务
     *
     * @param params
     * @return java.lang.Boolean
     * @author zuogang
     * @date 2019/8/6 19:20
     */
    @Override
    public boolean existJob(Map<String, Object> params) {
        String id = (String) params.get("id");
        String group = (String) params.get("group");
        String name = (String) params.get("name");

        // 获取所有JobDetail信息
        List<JobDetailType> jobDetailTypes = this.allJobByGroup(group, id);
        List<String> existNames = new ArrayList<>(16);
        if (jobDetailTypes != null && jobDetailTypes.size() > 0) {
            jobDetailTypes.stream().forEach(jobDetailType -> {
                existNames.add(jobDetailType.getName());
            });
        }

        if (existNames.contains(name)) {
            // 判断任务组下是否已经存在同名任务
            return false;
        }
        return true;
    }

    /**
     * 新增触发器
     *
     * @param params
     * @return java.lang.Boolean
     * @author zuogang
     * @date 2019/8/6 19:20
     */
    @Override
    public boolean addTrigger(Map<String, Object> params) {
        String id = (String) params.get("id");
        List<JobDetailType> jobDetailTypeList = this.getJobDetailType(getById(id));

        jobDetailTypeList.stream().forEach(jobDetailType -> {
            if (Objects.equals(jobDetailType.getGroup(), params.get("jobGroup")) && Objects.equals(jobDetailType
                            .getName(),
                    params.get("jobName"))) {
                jobDetailType.setDurability(false);
            }
        });

        List<CronTriggerType>
                cronTriggerTypes = this.getCronTriggerType(getById(id));

        CronTriggerType cronTriggerType = new CronTriggerType();
        cronTriggerType.setJobName((String) params.get("jobName"));
        cronTriggerType.setJobGroup((String) params.get("jobGroup"));
        cronTriggerType.setGroup(params.get("jobName") + "$" + params.get("jobGroup"));
        cronTriggerType.setName((String) params.get("name"));
        cronTriggerType.setCronExpression((String) params.get("cronExpression"));
        cronTriggerType.setDescription((String) params.get("description"));
        String endTime = (String) params.get("endTime");
        if (StringUtils.isBlank(endTime)) {
            cronTriggerType.setEndTime(null);
        } else {
            XMLGregorianCalendar xmlEndDatetime = null;
            XMLGregorianCalendar xmlStartDatetime = null;
            try {
                String end = "";
                endTime = endTime.replace("Z", " UTC");
                SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS Z");
                Date d = sdf1.parse(endTime);
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                end = simpleDateFormat.format(d);

                String start = simpleDateFormat.format(new Date());

                GregorianCalendar endGregorianCalendar = new GregorianCalendar();
                endGregorianCalendar.setTime(simpleDateFormat.parse(end));

                GregorianCalendar startGregorianCalendar = new GregorianCalendar();
                startGregorianCalendar.setTime(simpleDateFormat.parse(start));
                xmlEndDatetime = DatatypeFactory.newInstance().newXMLGregorianCalendar(endGregorianCalendar);
                xmlStartDatetime = DatatypeFactory.newInstance().newXMLGregorianCalendar(startGregorianCalendar);
            } catch (ParseException e) {
                e.printStackTrace();
            } catch (DatatypeConfigurationException e) {
                e.printStackTrace();
            }
            cronTriggerType.setEndTime(xmlEndDatetime);
            cronTriggerType.setStartTime(xmlStartDatetime);
        }

        cronTriggerTypes.add(cronTriggerType);

        // 重新执行任务并更新数据库
        if (!this.newJobStartAndDbUpdate(id, jobDetailTypeList, cronTriggerTypes))
            return false;
        return true;
    }

    /**
     * 更新触发器
     *
     * @param params
     * @return java.lang.Boolean
     * @author zuogang
     * @date 2019/8/6 19:20
     */
    @Override
    public boolean updTrigger(Map<String, Object> params) {

        String id = (String) params.get("id");
        String jobName = (String) params.get("jobName");
        String jobGroup = (String) params.get("jobGroup");
        String name = (String) params.get("name");

        List<JobDetailType> jobDetailTypeList = this.getJobDetailType(getById(id));

        List<CronTriggerType>
                cronTriggerTypes = this.getCronTriggerType(getById(id));
        cronTriggerTypes.stream().forEach(cronTriggerType -> {
            if (Objects.equals(jobName, cronTriggerType.getJobName()) && Objects.equals(jobGroup, cronTriggerType
                    .getJobGroup()) &&
                    Objects.equals(name, cronTriggerType.getName())) {

                cronTriggerType.setCronExpression((String) params.get("cronExpression"));
                cronTriggerType.setDescription((String) params.get("description"));
                String endTime = (String) params.get("endTime");
                if (StringUtils.isBlank(endTime)) {
                    cronTriggerType.setEndTime(null);
                } else {
                    XMLGregorianCalendar xmlEndDatetime = null;
                    try {
                        String end = "";
                        endTime = endTime.replace("Z", " UTC");
                        SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS Z");
                        Date d = sdf1.parse(endTime);
                        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                        end = simpleDateFormat.format(d);

                        GregorianCalendar endGregorianCalendar = new GregorianCalendar();
                        endGregorianCalendar.setTime(simpleDateFormat.parse(end));
                        xmlEndDatetime = DatatypeFactory.newInstance().newXMLGregorianCalendar(endGregorianCalendar);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    } catch (DatatypeConfigurationException e) {
                        e.printStackTrace();
                    }
                    cronTriggerType.setEndTime(xmlEndDatetime);
                }

            }
        });

        // 重新执行任务并更新数据库
        if (!this.newJobStartAndDbUpdate(id, jobDetailTypeList, cronTriggerTypes))
            return false;
        return true;
    }

    /**
     * 删除触发器
     *
     * @param params
     * @return java.lang.Boolean
     * @author zuogang
     * @date 2019/8/6 19:20
     */
    @Override
    public boolean delTrigger(Map<String, Object> params) {
        String id = (String) params.get("id");
        String jobName = (String) params.get("jobName");
        String jobGroup = (String) params.get("jobGroup");
        String name = (String) params.get("name");
        List<JobDetailType> jobDetailTypeList = this.getJobDetailType(getById(id));

        List<CronTriggerType>
                cronTriggerTypes = this.getCronTriggerType(getById(id));

        List<CronTriggerType> newCronTriggerTypes = new ArrayList<>(16);
        List<CronTriggerType> deleteCronTriggerTypes = new ArrayList<>(16);
        cronTriggerTypes.stream().forEach(cronTriggerType -> {
            if (Objects.equals(cronTriggerType.getJobName(), jobName) && Objects.equals(cronTriggerType.getJobGroup(),
                    jobGroup) && Objects.equals(cronTriggerType.getName(), name)) {
                deleteCronTriggerTypes.add(cronTriggerType);
            } else {
                newCronTriggerTypes.add(cronTriggerType);
            }
        });

        // 当触发器被删除时,该触发器对应的任务没有其他触发器时，任务的Durability设为true
        if (deleteCronTriggerTypes != null && (deleteCronTriggerTypes.size() == 1 || deleteCronTriggerTypes.size() ==
                0)) {
            jobDetailTypeList.stream().forEach(jobDetailType -> {
                if (Objects.equals(jobDetailType.getGroup(), jobGroup) && Objects.equals(jobDetailType.getName(),
                        jobName)) {
                    jobDetailType.setDurability(true);
                }
            });
        }

        // 重新执行任务并更新数据库
        if (!this.newJobStartAndDbUpdate(id, jobDetailTypeList, newCronTriggerTypes))
            return false;

        return true;
    }

    /**
     * 判断该任务下是否存在同名参数
     *
     * @param params
     * @return java.lang.Boolean
     * @author zuogang
     * @date 2019/8/6 19:20
     */
    @Override
    public boolean existParam(Map<String, Object> params) {
        List<EntryType> entry = this.getJobDetail(params).getJobDataMap().getEntry();
        List<String> keys = new ArrayList<>(16);
        String key = (String) params.get("key");
        entry.stream().forEach(entryType -> {
            keys.add(entryType.getKey());
        });
        if (keys.contains(key)) {
            return false;
        }
        return true;
    }

    /**
     * 新增参数
     *
     * @param params
     * @return java.lang.Boolean
     * @author zuogang
     * @date 2019/8/6 19:20
     */
    @Override
    public boolean addParam(Map<String, Object> params) {
        String id = (String) params.get("id");
        String jobName = (String) params.get("jobName");
        String jobGroup = (String) params.get("jobGroup");
        String key = (String) params.get("key");
        String value = (String) params.get("value");
        List<JobDetailType> jobDetailTypeList = this.getJobDetailType(getById(id));

        jobDetailTypeList.stream().forEach(jobDetailType -> {
            if (Objects.equals(jobDetailType.getName(), jobName) && Objects.equals(jobDetailType.getGroup(),
                    jobGroup)) {
                EntryType entryType = new EntryType();
                entryType.setKey(key);
                entryType.setValue(value);
                jobDetailType.getJobDataMap().getEntry().add(entryType);
            }
        });

        List<CronTriggerType>
                cronTriggerTypes = this.getCronTriggerType(getById(id));

        // 重新执行任务并更新数据库
        if (!this.newJobStartAndDbUpdate(id, jobDetailTypeList, cronTriggerTypes))
            return false;
        return true;
    }

    /**
     * 更新参数
     *
     * @param params
     * @return java.lang.Boolean
     * @author zuogang
     * @date 2019/8/6 19:20
     */
    @Override
    public boolean updParam(Map<String, Object> params) {

        String id = (String) params.get("id");
        String jobName = (String) params.get("jobName");
        String jobGroup = (String) params.get("jobGroup");
        String key = (String) params.get("key");
        String value = (String) params.get("value");
        List<JobDetailType> jobDetailTypeList = this.getJobDetailType(getById(id));

        jobDetailTypeList.stream().forEach(jobDetailType -> {
            if (Objects.equals(jobDetailType.getName(), jobName) && Objects.equals(jobDetailType.getGroup(),
                    jobGroup)) {
                jobDetailType.getJobDataMap().getEntry().stream().forEach(entryType -> {
                    if (Objects.equals(entryType.getKey(), key)) {
                        entryType.setValue(value);
                    }
                });

            }
        });

        List<CronTriggerType>
                cronTriggerTypes = this.getCronTriggerType(getById(id));

        // 重新执行任务并更新数据库
        if (!this.newJobStartAndDbUpdate(id, jobDetailTypeList, cronTriggerTypes))
            return false;

        return true;
    }

    /**
     * 删除参数
     *
     * @param params
     * @return java.lang.Boolean
     * @author zuogang
     * @date 2019/8/6 19:20
     */
    @Override
    public boolean delParam(Map<String, Object> params) {

        String id = (String) params.get("id");
        String jobName = (String) params.get("jobName");
        String jobGroup = (String) params.get("jobGroup");
        String key = (String) params.get("key");
        List<JobDetailType> jobDetailTypeList = this.getJobDetailType(getById(id));

        jobDetailTypeList.stream().forEach(jobDetailType -> {
            if (Objects.equals(jobDetailType.getName(), jobName) && Objects.equals(jobDetailType.getGroup(),
                    jobGroup)) {
                List<EntryType> oldEntry = jobDetailType.getJobDataMap().getEntry();
                List<EntryType> newEntry = new ArrayList<>(16);
                oldEntry.stream().forEach(entryType -> {
                    if (!Objects.equals(key, entryType.getKey())) {
                        newEntry.add(entryType);
                    }
                });
                jobDetailType.getJobDataMap().getEntry().clear();
                jobDetailType.getJobDataMap().setEntry(newEntry);
            }
        });

        List<CronTriggerType>
                cronTriggerTypes = this.getCronTriggerType(getById(id));

        // 重新执行任务并更新数据库
        if (!this.newJobStartAndDbUpdate(id, jobDetailTypeList, cronTriggerTypes))
            return false;

        return true;
    }

    /**
     * 运行所有服务任务
     *
     * @return java.lang.Boolean
     * @author zuogang
     * @date 2019/8/6 19:20
     */
    @Override
    public boolean clickAllRun() {
        Scheduler scheduler = schedulerFactoryBean.getScheduler();
        if (scheduler != null) {
            try {
                scheduler.resumeAll();
            } catch (SchedulerException e) {
                e.printStackTrace();
            }
        }
        return true;
    }

    /**
     * 暂停所有服务任务
     *
     * @return java.lang.Boolean
     * @author zuogang
     * @date 2019/8/6 19:20
     */
    @Override
    public boolean clickAllPause() {
        Scheduler scheduler = schedulerFactoryBean.getScheduler();
        if (scheduler != null) {
            try {
                scheduler.pauseAll();
            } catch (SchedulerException e) {
                e.printStackTrace();
            }
        }
        return true;
    }


    /**
     * 重新执行任务并更新数据库
     *
     * @param id
     * @param jobDetailTypeList
     * @param cronTriggerTypes
     * @return
     * @author zuogang
     * @date 2019/8/8 14:06
     */
    private boolean newJobStartAndDbUpdate(String id, List<JobDetailType> jobDetailTypeList, List<CronTriggerType>
            cronTriggerTypes) {
        if (jobDetailTypeList != null && jobDetailTypeList.size() == 0) {
            // 删除旧的数据
            if (!removeById(id)) {
                return false;
            }
        } else {
            JobSchedulingData jobSchedulingData = this.getJobSchedulingData(jobDetailTypeList, cronTriggerTypes);

            // 获取新的xml
            String writer = XmlUtils.marshalToXml(jobSchedulingData,JobSchedulingData.class);

            this.ProcessJobFileAndSchedule(writer);
            // 修改数据库
            if (!this.SaveXmlData(id, writer))
                return false;
        }


        return true;
    }

    /**
     * 修改数据库
     *
     * @param id
     * @param writer
     * @return boolean
     * @author zuogang
     * @date 2019/8/8 10:58
     */
    private boolean SaveXmlData(String id, String writer) {
        if (StringUtils.isBlank(writer)) {
            return false;
        }

        // 删除旧的数据
        if (getById(id) != null) {
            if (!removeById(id)) {
                return false;
            }
        }


        // 添加新的数据
        QrtzConfigDO qrtzConfigDO = new QrtzConfigDO();
        qrtzConfigDO.setId(id);
        qrtzConfigDO.setConfig(writer);
        if (!save(qrtzConfigDO)) {
            return false;
        }

        return true;
    }

    /**
     * 编辑jobSchedulingData
     *
     * @param jobDetailTypeList
     * @param cronTriggerTypes
     * @return
     * @author zuogang
     * @date 2019/8/8 9:22
     */
    private JobSchedulingData getJobSchedulingData(List<JobDetailType> jobDetailTypeList, List<CronTriggerType>
            cronTriggerTypes) {
        JobSchedulingData jobSchedulingData = new JobSchedulingData();

        JobSchedulingData.Schedule schedule = new JobSchedulingData.Schedule();
        List<Object> jobDetailObjects = new ArrayList<>(16);
        jobDetailTypeList.stream().forEach(jobDetailType -> {
            jobDetailObjects.add(jobDetailType);
        });
        List<Object> triggerObjects = new ArrayList<>(16);
        cronTriggerTypes.stream().forEach(cronTriggerType -> {
            TriggerType triggerType = new TriggerType();
            triggerType.setCron(cronTriggerType);
            triggerObjects.add(triggerType);
        });
        schedule.setJob(jobDetailObjects);
        schedule.setTrigger(triggerObjects);

        List<Object> objects = new ArrayList<>(16);
        objects.add(schedule);
        jobSchedulingData.setPreProcessingCommandsAndProcessingDirectivesAndSchedule(objects);
        return jobSchedulingData;

    }

    /**
     * 重新执行任务
     *
     * @param writer
     * @return void
     * @author zuogang
     * @date 2019/8/8 8:40
     */
    private void ProcessJobFileAndSchedule(String writer) {


        Scheduler scheduler = schedulerFactoryBean.getScheduler();

        XMLSchedulingDataProcessor processor = null;
        ClassLoadHelper typeLoadHelper = new org.quartz.simpl.SimpleClassLoadHelper();
        try {
            processor = new XMLSchedulingDataProcessor(typeLoadHelper);

            java.io.InputStream stream = new ByteArrayInputStream(writer.getBytes("UTF-8"));
            processor.processStreamAndScheduleJobs(stream,
                    " ", // systemId
                    scheduler);
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


}

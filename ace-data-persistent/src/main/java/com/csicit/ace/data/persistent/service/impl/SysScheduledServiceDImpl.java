package com.csicit.ace.data.persistent.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.csicit.ace.common.pojo.domain.QrtzConfigDO;
import com.csicit.ace.common.pojo.vo.ScheduledVO;
import com.csicit.ace.common.utils.StringUtils;
import com.csicit.ace.common.utils.XmlUtils;
import com.csicit.ace.data.persistent.mapper.SysScheduledMapper;
import com.csicit.ace.data.persistent.qrtzUtils.*;
import com.csicit.ace.data.persistent.service.SysScheduledServiceD;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author FourLeaves
 * @version V1.0
 * @date 2019/12/26 11:09
 */
@Service
public class SysScheduledServiceDImpl extends BaseServiceImpl<SysScheduledMapper, QrtzConfigDO>
        implements SysScheduledServiceD {

    @Override
    public boolean saveScheduleds(List<ScheduledVO> scheduleds, String appId) {
        if (CollectionUtils.isEmpty(scheduleds)) {
            return true;
        }
        QrtzConfigDO qrtzConfigDO = getOne(new QueryWrapper<QrtzConfigDO>().eq("id", appId));

        List<JobDetail> jobDetailTypes = new ArrayList<>(16);
        List<Trigger> cronTriggerTypes = new ArrayList<>(16);

        if (qrtzConfigDO != null && qrtzConfigDO.getConfig().length() != 0) {

            JobScheduling jobScheduling = (JobScheduling) XmlUtils.unmarshalXml(qrtzConfigDO.getConfig(),
                    JobScheduling.class);
            if (jobScheduling != null && jobScheduling.getSchedule() != null) {
                if (CollectionUtils.isNotEmpty(jobScheduling.getSchedule().getJob())) {
                    jobDetailTypes.addAll(jobScheduling.getSchedule().getJob());
                }
                if (CollectionUtils.isNotEmpty(jobScheduling.getSchedule().getTrigger())) {
                    cronTriggerTypes.addAll(jobScheduling.getSchedule().getTrigger());
                }
            }

        }

        //获取数据库中有，扫描数据中没有的api，需要进行删除处理
        if (CollectionUtils.isNotEmpty(jobDetailTypes)) {
            List<JobDetail> deleteJobs =
                    jobDetailTypes.stream().filter(
                            item -> !scheduleds.stream().map(e ->
                                    e.getGroup())
                                    .collect(Collectors.toList())
                                    .contains(item.getGroup())
                                    && !scheduleds.stream().map(e ->
                                    e.getName())
                                    .collect(Collectors.toList())
                                    .contains(item.getName())).collect(Collectors.toList());

            if (CollectionUtils.isNotEmpty(deleteJobs)) {
                jobDetailTypes.removeAll(deleteJobs);
            }
        }

        if (CollectionUtils.isNotEmpty(cronTriggerTypes)) {

            List<Trigger> deleteCrons = cronTriggerTypes.stream().filter(
                    item -> !scheduleds.stream().map(e ->
                            e.getGroup())
                            .collect(Collectors.toList())
                            .contains(item.getCron().getJobGroup())
                            && !scheduleds.stream().map(e ->
                            e.getName())
                            .collect(Collectors.toList())
                            .contains(item.getCron().getJobName())).collect(Collectors.toList());

            if (CollectionUtils.isNotEmpty(deleteCrons)) {
                cronTriggerTypes.removeAll(deleteCrons);
            }
        }


        //获取扫描数据中有，数据库中没有的api，需要进行新增处理
        List<ScheduledVO> adds =
                scheduleds.stream().filter(
                        item -> !jobDetailTypes.stream().map(e ->
                                e.getGroup())
                                .collect(Collectors.toList())
                                .contains(item.getGroup())
                                && !jobDetailTypes.stream().map(e ->
                                e.getName())
                                .collect(Collectors.toList())
                                .contains(item.getName())
                ).collect(Collectors.toList());

        // 新增任务
        if (CollectionUtils.isNotEmpty(adds)) {
            adds.stream().forEach(add -> {
                JobDetail newJob = new JobDetail();
                newJob.setGroup(add.getGroup());
                newJob.setName(add.getName());
                newJob.setDescription("");
                newJob.setJobClass("com.csicit.ace.quartz.core.job.AgentJob");
                // 还没有触发器
                newJob.setDurability(true);
                newJob.setRecover(false);

                List<Entry> entryTypes = new ArrayList<>(16);
                Entry entryType = new Entry();
                entryType.setKey("paramAppId");
                entryType.setValue(appId);
                entryTypes.add(entryType);

                Entry entryType2 = new Entry();
                entryType2.setKey("paramUrl");
                entryType2.setValue(add.getUrl());
                entryTypes.add(entryType2);

                JobDataMap jobDataMapType = new JobDataMap();
                jobDataMapType.setEntry(entryTypes);

                newJob.setJobDataMap(jobDataMapType);

                jobDetailTypes.add(newJob);
            });
        }

        // 更新数据库
        if (CollectionUtils.isEmpty(jobDetailTypes)) {
            // 删除旧的数据
            if (!removeById(appId)) {
                return false;
            }
        } else {
            JobScheduling jobSchedulingData = this.getJobSchedulingData(jobDetailTypes, cronTriggerTypes);

            // 获取新的xml
            String writer = XmlUtils.marshalToXml(jobSchedulingData, JobScheduling.class);

            // 修改数据库
            if (!this.SaveXmlData(appId, writer))
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
     * XML序列化
     *
     * @param
     * @return java.lang.Object
     * @author zuogang
     * @date 2019/8/6 9:31
     */
//    private String marshalToXml(Object root) {
//        String writer = "";
//        StringWriter stringWriter = new StringWriter();
//        try {
//            Marshaller jaxbMarshaller = JAXBContext.newInstance(JobScheduling.class)
//                    .createMarshaller();
//            jaxbMarshaller.setProperty(Marshaller.JAXB_ENCODING, "UTF-8");
//            jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
//            jaxbMarshaller.marshal(root, stringWriter);
//            writer = stringWriter.toString();
//            writer = writer.replace("ns2:", "");
//            writer = writer.replace(":ns2", "");
//        } catch (JAXBException e) {
//            e.printStackTrace();
//        }
//        return writer;
//    }

    /**
     * 编辑jobSchedulingData
     *
     * @param jobDetailTypeList
     * @param cronTriggerTypes
     * @return
     * @author zuogang
     * @date 2019/8/8 9:22
     */
    private JobScheduling getJobSchedulingData(List<JobDetail> jobDetailTypeList, List<Trigger>
            cronTriggerTypes) {

        Schedule schedule = new Schedule();
        List<JobDetail> jobDetailObjects = new ArrayList<>(16);
        jobDetailTypeList.stream().forEach(jobDetailType -> {
            jobDetailObjects.add(jobDetailType);
        });
        List<Trigger> triggerObjects = new ArrayList<>(16);
        cronTriggerTypes.stream().forEach(cronTriggerType -> {
            triggerObjects.add(cronTriggerType);
        });
        schedule.setJob(jobDetailObjects);
        schedule.setTrigger(triggerObjects);

        JobScheduling jobSchedulingData = new JobScheduling(schedule);
        return jobSchedulingData;

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
//            JAXBContext jaxbContext = JAXBContext.newInstance(JobScheduling.class);
//            Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
//            StringReader sr = new StringReader(s);
//            result = unmarshaller.unmarshal(sr);
//        } catch (JAXBException e) {
//            e.printStackTrace();
//        }
//        return result;
//    }
}

import com.csicit.ace.bpm.BpmAdapter;
import com.csicit.ace.bpm.BpmManager;
import com.csicit.ace.bpm.FlowInstance;
import com.csicit.ace.bpm.FlowNoGenerator;
import com.csicit.ace.bpm.pojo.vo.wfd.Flow;
import com.csicit.ace.bpmtest.BpmTestApplication;
import com.csicit.ace.common.pojo.domain.SysUserDO;
import com.csicit.ace.common.utils.system.SecurityUtils;
import org.activiti.bpmn.BpmnAutoLayout;
import org.activiti.bpmn.converter.BpmnXMLConverter;
import org.activiti.bpmn.model.*;
import org.activiti.bpmn.model.Process;
import org.activiti.engine.HistoryService;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.history.HistoricTaskInstance;
import org.activiti.engine.repository.DeploymentBuilder;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.UnsupportedEncodingException;
import java.util.*;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = BpmTestApplication.class)
public class BpmDemoApplicationTests {
    private static final Logger LOGGER = LoggerFactory.getLogger(BpmDemoApplicationTests.class);
    @Autowired
    private BpmManager bpmManager;
    @Autowired
    private TaskService taskService;
    @Autowired
    private RuntimeService runtimeService;
    @Autowired
    private HistoryService historyService;
    @Autowired
    private RepositoryService repositoryService;
    @Autowired
    private SecurityUtils securityUtils;
    @Autowired
    private BpmAdapter bpmAdapter;

    @Test
    public void testTwoStartEvent() throws Exception {
        String businessKey = UUID.randomUUID().toString();
        SysUserDO user = new SysUserDO();
        user.setId(UUID.randomUUID().toString());
        FlowInstance processInstance = bpmManager.createFlowInstanceById("apply-test", businessKey, user);
    }


//    @Test
//    public void test1() {
//        String businessKey = UUID.randomUUID().toString();
//        SysUserDO user = new SysUserDO();
//        user.setId(UUID.randomUUID().toString());
//        ProcessInstance processInstance = workflowManager.createWorkflowInstance("apply-test", businessKey, user);
//        List<TaskInstance> tasks = taskService.createTaskQuery().processInstanceId(processInstance.getId()).list();
//        LOGGER.info("tasks:" + tasks.size());
//        List<String> assigneeList = new ArrayList<>(); //分配任务的人员
//        assigneeList.add("tom");
//        assigneeList.add("jack");
//        assigneeList.add("mary");
//        Map<String, Object> vars = new HashMap<>(); //参数
//        vars.put("assigneeList", assigneeList);
//        vars.put("submitType", "y");
//        for (TaskInstance task :
//                tasks) {
//            taskService.complete(task.getId(), vars);
//            LOGGER.info("task completed:" + task);
//        }
//        tasks = taskService.createTaskQuery().processInstanceId(processInstance.getId()).list();
//        for (TaskInstance task :
//                tasks) {
//            taskService.claim(task.getId(), user.getId());
//            LOGGER.info("task:" + task);
//        }
//
//        runtimeService.signalEventReceivedWithTenantId("withdrawSignal", processInstance.getTenantId());
//
//        tasks = taskService.createTaskQuery().processInstanceId(processInstance.getId()).list();
//        for (TaskInstance task :
//                tasks) {
//            taskService.complete(task.getId(), vars);
//            LOGGER.info("task completed:" + task);
//        }
//    }
//
//    @Test
//    public void test2() {
//        String businessKey = UUID.randomUUID().toString();
//        ProcessInstance processInstance = workflowManager.createWorkflowInstance("task-test", businessKey, new SysUserDO());
//        List<TaskInstance> tasks = taskService.createTaskQuery().processInstanceId(processInstance.getId()).list();
//        LOGGER.info("tasks:" + tasks.size());
//        List<String> assigneeList = new ArrayList<>(); //分配任务的人员
//        assigneeList.add("tom");
//        assigneeList.add("jack");
//        assigneeList.add("mary");
//        Map<String, Object> vars = new HashMap<>(); //参数
//        vars.put("assigneeList", assigneeList);
//        vars.put("submitType", "y");
//        for (TaskInstance task :
//                tasks) {
//            taskService.complete(task.getId(), vars);
//            LOGGER.info("task completed:" + task);
//        }
//        runtimeService.signalEventReceivedWithTenantId("withdrawSignal", processInstance.getTenantId());
//        tasks = taskService.createTaskQuery().processInstanceId(processInstance.getId()).list();
//        for (TaskInstance task :
//                tasks) {
//            LOGGER.info("current task:" + task);
//        }
//
//        ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery().processDefinitionId(processInstance.getProcessDefinitionId()).singleResult();
//        BpmnModel model = repositoryService.getBpmnModel(processDefinition.getId());
//
////        List<String> currentActs = runtimeService.getActiveActivityIds(processInstance.getId());
////        ProcessDiagramGenerator processDiagramGenerator = new DefaultProcessDiagramGenerator();
////        String fontName = "宋体";
////        InputStream inputStream = processDiagramGenerator.generateDiagram(model, currentActs, Collections.EMPTY_LIST, fontName, fontName, fontName);
////        RandomAccessFile raFile = null;
////        try {
////            File dirFile = new File("d:\\temp\\" + processInstance.getId() + ".svg");
////            //以读写的方式打开目标文件
////            raFile = new RandomAccessFile(dirFile, "rw");
////            raFile.seek(raFile.length());
////            byte[] bytes = new byte[1024];
////            int length;
////            while ((length = inputStream.read(bytes)) != -1) {
////                raFile.write(bytes, 0, length);
////            }
////        } catch (Exception e) {
////            e.printStackTrace();
////        } finally {
////            if (inputStream != null) {
////                inputStream.close();
////            }
////            if (raFile != null) {
////                raFile.close();
////            }
////        }
//    }
//
//    @Test
//    public void test3(){
//        String businessKey = UUID.randomUUID().toString();
//        SysUserDO user = new SysUserDO();
//        user.setId(UUID.randomUUID().toString());
//        ProcessInstance processInstance = workflowManager.createWorkflowInstance("task-test", businessKey, user);
//        List<TaskInstance> tasks = taskService.createTaskQuery().processInstanceId(processInstance.getId()).list();
//        LOGGER.info("tasks:" + tasks.size());
//        List<String> assigneeList = new ArrayList<>(); //分配任务的人员
//        assigneeList.add("tom");
//        assigneeList.add("jack");
//        assigneeList.add("mary");
//        Map<String, Object> vars = new HashMap<>(); //参数
//        vars.put("assigneeList", assigneeList);
//        vars.put("submitType", "y");
//        for (TaskInstance task :
//                tasks) {
//            taskService.complete(task.getId(), vars);
//            LOGGER.info("task completed:" + task);
//        }
//        tasks = taskService.createTaskQuery().processInstanceId(processInstance.getId()).list();
//        for (TaskInstance task :
//                tasks) {
//            taskService.claim(task.getId(), user.getId());
//            LOGGER.info("task:" + task);
//        }
//
//        runtimeService.signalEventReceivedWithTenantId("withdrawSignal", processInstance.getTenantId());
//
//        tasks = taskService.createTaskQuery().processInstanceId(processInstance.getId()).list();
//        for (TaskInstance task :
//                tasks) {
//            taskService.complete(task.getId(), vars);
//            LOGGER.info("task completed:" + task);
//        }
//        // 某一次流程执行了多少步
//        List<HistoricActivityInstance> historicActivityInstances = historyService.createHistoricActivityInstanceQuery().processInstanceId(processInstance.getId()).list();
//        for (HistoricActivityInstance activiti :
//                historicActivityInstances) {
//            LOGGER.info(activiti.getId());
//            LOGGER.info("步骤ID：" + activiti.getActivityId());
//            LOGGER.info("步骤名称：" + activiti.getActivityName());
//            LOGGER.info("执行人：" + activiti.getAssignee());
//            LOGGER.info("====================================");
//        }
//        // 某一次流程的执行经历的多少任务
//        List<HistoricTaskInstance> historicTaskInstances = historyService.createHistoricTaskInstanceQuery().processInstanceId(processInstance.getId()).list();
//        for (HistoricTaskInstance historicTaskInstance :
//                historicTaskInstances) {
//            LOGGER.info("taskId:" + historicTaskInstance.getId() + "，");
//            LOGGER.info("name:" + historicTaskInstance.getName() + "，");
//            LOGGER.info("pdId:" + historicTaskInstance.getProcessDefinitionId() + "，");
//            LOGGER.info("assignee:" + historicTaskInstance.getAssignee() + "，");
//            LOGGER.info("====================================");
//        }
//    }
//
//    @Test
//    public void testCreateWorkflowInstance() {
//        workflowManager.createWorkflowInstance("task-test", UUID.randomUUID().toString(), new SysUserDO());
//    }


    private SequenceFlow addSequenceFlow(Process process, FlowElement sourceFlowElement, FlowElement targetFlowElement) {
        SequenceFlow sequenceFlow = new SequenceFlow();
        sequenceFlow.setId(generateId());
        sequenceFlow.setSourceRef(sourceFlowElement.getId());
        sequenceFlow.setTargetRef(targetFlowElement.getId());
        process.addFlowElement(sequenceFlow);
        return sequenceFlow;
    }

    private BpmnModel getForTestA() throws Exception {
        BpmnModel model = new BpmnModel();
        Process process = new Process();
        process.setId("testA");
        process.setName("testA");
        model.addProcess(process);

        StartEvent startEvent = new StartEvent();
        startEvent.setId("startEvent");
        startEvent.setName("开始");
        process.addFlowElement(startEvent);

        UserTask userTask1 = new UserTask();
        userTask1.setId("userTask1");
        userTask1.setName("编制");
        process.addFlowElement(userTask1);

        UserTask userTask2 = new UserTask();
        userTask2.setId("userTask2");
        userTask2.setName("专家审批");
        process.addFlowElement(userTask2);

        MultiInstanceLoopCharacteristics multiInstanceLoopCharacteristics = new MultiInstanceLoopCharacteristics();
        multiInstanceLoopCharacteristics.setSequential(false);
        multiInstanceLoopCharacteristics.setInputDataItem("assigneeList");
        multiInstanceLoopCharacteristics.setElementVariable("assignee");
        multiInstanceLoopCharacteristics.setCompletionCondition("${pass == true}");
        userTask2.setLoopCharacteristics(multiInstanceLoopCharacteristics);

        ExclusiveGateway exclusiveGateway = new ExclusiveGateway();
        exclusiveGateway.setId(generateId());
        process.addFlowElement(exclusiveGateway);

        UserTask userTask3 = new UserTask();
        userTask3.setId("userTask3");
        userTask3.setName("分管领导审批");
        process.addFlowElement(userTask3);

        EndEvent endEvent = new EndEvent();
        endEvent.setId("endEvent");
        endEvent.setName("结束");
        process.addFlowElement(endEvent);


        addSequenceFlow(process, startEvent, userTask1);
        addSequenceFlow(process, userTask1, userTask2);
        addSequenceFlow(process, userTask2, exclusiveGateway);
        addSequenceFlow(process, userTask3, endEvent);


        SequenceFlow sfPass = addSequenceFlow(process, exclusiveGateway, userTask3);
        sfPass.setConditionExpression("${pass == true}");

        SequenceFlow sfNotPass = addSequenceFlow(process, exclusiveGateway, endEvent);
        sfNotPass.setConditionExpression("${pass == false}");

        printBpmn(model);
//        validateModel(model);
        new BpmnAutoLayout(model).execute();

        return model;
    }

    private BpmnModel getForTestB() throws Exception {
        BpmnModel model = new BpmnModel();
        Process process = new Process();
        process.setId("testB");
        process.setName("testB");
        model.addProcess(process);

        StartEvent startEvent = new StartEvent();
        startEvent.setId("startEvent");
        startEvent.setName("开始");
        process.addFlowElement(startEvent);

        UserTask userTask1 = new UserTask();
        userTask1.setId("userTask1");
        userTask1.setName("编制");
        process.addFlowElement(userTask1);

        UserTask userTask2 = new UserTask();
        userTask2.setId("userTask2");
        userTask2.setName("财务审批");
        process.addFlowElement(userTask2);

        UserTask userTask3 = new UserTask();
        userTask3.setId("userTask3");
        userTask3.setName("采购审批");
        process.addFlowElement(userTask3);

        EndEvent endEvent = new EndEvent();
        endEvent.setId("endEvent");
        endEvent.setName("结束");
        process.addFlowElement(endEvent);


        addSequenceFlow(process, startEvent, userTask1);
        addSequenceFlow(process, userTask1, userTask2);
        addSequenceFlow(process, userTask1, userTask3);
        addSequenceFlow(process, userTask2, endEvent);
        addSequenceFlow(process, userTask3, endEvent);


        printBpmn(model);
//        validateModel(model);
        new BpmnAutoLayout(model).execute();

        return model;
    }

//    private void validateModel(BpmnModel model) throws Exception {
//        ProcessValidatorFactory processValidatorFactory = new ProcessValidatorFactory();
//        ProcessValidator processValidator = processValidatorFactory.createDefaultProcessValidator();
//        List<ValidationError> validationErrors = processValidator.validate(model);
//        if (validationErrors.size() > 0) {
//            System.out.println(validationErrors);
//            throw new Exception("validation error:" + model);
//        }
//    }

    private String generateId() {
        return "_" + UUID.randomUUID().toString();
    }

    private void printBpmn(BpmnModel bpmnModel) {
        BpmnXMLConverter bpmnXMLConverter = new BpmnXMLConverter();

        byte[] convertToXML = bpmnXMLConverter.convertToXML(bpmnModel);

        String bytes = null;
        try {
            bytes = new String(convertToXML, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        System.out.println(bytes);
    }

    /**
     * 测试是否支持指定分支达到后即向后继续流转
     *
     * @param
     * @return void
     * @author JonnyJiang
     * @date 2019/8/14 20:18
     */

    @Test
    public void testA() throws Exception {
        DeploymentBuilder deploymentBuilder = repositoryService.createDeployment();
        BpmnModel model = getForTestA();
        deploymentBuilder.addBpmnModel(model.getMainProcess().getId() + ".bpmn", model).tenantId(securityUtils.getAppName()).deploy();

        String businessKey = UUID.randomUUID().toString();
        SysUserDO user = new SysUserDO();
        user.setId("jianghoulu");
        FlowInstance processInstance = bpmManager.createFlowInstanceById("testA", businessKey, user);
        List<Task> tasks = taskService.createTaskQuery().processInstanceId(processInstance.getId()).list();
        LOGGER.info("userTask1 size:" + tasks.size());
        assert tasks.size() == 1;
        List<String> assigneeList = new ArrayList<>(); //分配任务的人员
        assigneeList.add("huchangping");
        assigneeList.add("yansiyang");
        assigneeList.add("shanwenjin");
        Map<String, Object> vars = new HashMap<>(); //参数
        vars.put("assigneeList", assigneeList);
        for (Task task :
                tasks) {
            taskService.complete(task.getId(), vars);
        }
        tasks = taskService.createTaskQuery().processInstanceId(processInstance.getId()).list();

        LOGGER.info("userTask2 size:" + tasks.size());
        assert tasks.size() == assigneeList.size();
        Task task0 = tasks.get(0);
        Task task1 = tasks.get(1);
        Task task2 = tasks.get(2);
        vars.clear();
        vars.put("pass", false);
        taskService.complete(task0.getId(), vars);
        tasks = taskService.createTaskQuery().processInstanceId(processInstance.getId()).list();
        LOGGER.info("userTask2 size:" + tasks.size());
        vars.clear();
        vars.put("pass", true);
        taskService.complete(task1.getId(), vars);
        tasks = taskService.createTaskQuery().processInstanceId(processInstance.getId()).list();

        LOGGER.info("userTask3 size:" + tasks.size());
        assert tasks.size() == 1;

        tasks = taskService.createTaskQuery().taskId(task1.getId()).list();
        LOGGER.info("task1 size:" + tasks.size());

        List<HistoricTaskInstance> historicTaskInstances = historyService.createHistoricTaskInstanceQuery().taskId(task1.getId()).list();
        LOGGER.info("task1 historicTaskInstance size:" + historicTaskInstances.size());

        HistoricTaskInstance historicTaskInstance = historicTaskInstances.get(0);
        LOGGER.info("historicTaskInstance endTime" + historicTaskInstance.getEndTime());
    }

    /**
     * 测试平行的两条分支互不影响
     *
     * @param
     * @return void
     * @author JonnyJiang
     * @date 2019/8/15 20:04
     */

    @Test
    public void testB() throws Exception {
        DeploymentBuilder deploymentBuilder = repositoryService.createDeployment();
        BpmnModel model = getForTestB();
        deploymentBuilder.addBpmnModel(model.getMainProcess().getId() + ".bpmn", model).tenantId(securityUtils.getAppName()).deploy();

        String businessKey = UUID.randomUUID().toString();
        SysUserDO user = new SysUserDO();
        user.setId("jianghoulu");
        FlowInstance processInstance = bpmManager.createFlowInstanceById(model.getMainProcess().getId(), businessKey, user);
        List<Task> tasks = taskService.createTaskQuery().processInstanceId(processInstance.getId()).list();
        LOGGER.info("userTask1 size:" + tasks.size());
        assert tasks.size() == 1;

        for (Task task :
                tasks) {
            taskService.complete(task.getId());
        }
        tasks = taskService.createTaskQuery().processInstanceId(processInstance.getId()).list();

        LOGGER.info("userTask size:" + tasks.size());

        taskService.complete(tasks.get(0).getId());
        LOGGER.info(tasks.get(0).getName() + " completed");

        tasks = taskService.createTaskQuery().processInstanceId(processInstance.getId()).list();

        LOGGER.info("userTask size:" + tasks.size());


        List<ProcessInstance> processInstances = runtimeService.createProcessInstanceQuery().processInstanceId(processInstance.getId()).list();

        LOGGER.info("processInstances size:" + processInstances.size());

        taskService.complete(tasks.get(0).getId());
        LOGGER.info(tasks.get(0).getName() + " completed");

        tasks = taskService.createTaskQuery().processInstanceId(processInstance.getId()).list();

        LOGGER.info("userTask size:" + tasks.size());

        processInstances = runtimeService.createProcessInstanceQuery().processInstanceId(processInstance.getId()).list();

        LOGGER.info("processInstances size:" + processInstances.size());
    }

    private BpmnModel getForTestC() throws Exception {
        BpmnModel model = new BpmnModel();
        Process process = new Process();
        process.setId("testB");
        process.setName("testB");
        model.addProcess(process);

        StartEvent startEvent = new StartEvent();
        startEvent.setId("startEvent");
        startEvent.setName("开始");
        process.addFlowElement(startEvent);

        UserTask userTask1 = new UserTask();
        userTask1.setId("userTask1");
        userTask1.setName("编制");
        process.addFlowElement(userTask1);

        UserTask userTask2 = new UserTask();
        userTask2.setId("userTask2");
        userTask2.setName("财务审批");
        process.addFlowElement(userTask2);

        UserTask userTask3 = new UserTask();
        userTask3.setId("userTask3");
        userTask3.setName("采购审批");
        process.addFlowElement(userTask3);

        UserTask userTask4 = new UserTask();
        userTask4.setId("userTask4");
        userTask4.setName("总经理审批");
        process.addFlowElement(userTask4);

        EndEvent endEvent = new EndEvent();
        endEvent.setId("endEvent");
        endEvent.setName("结束");
        process.addFlowElement(endEvent);


        addSequenceFlow(process, startEvent, userTask1);
        addSequenceFlow(process, userTask1, userTask2);
        addSequenceFlow(process, userTask1, userTask3);
        addSequenceFlow(process, userTask2, userTask4);
        addSequenceFlow(process, userTask3, userTask4);
        addSequenceFlow(process, userTask4, endEvent);


        printBpmn(model);
//        validateModel(model);
        new BpmnAutoLayout(model).execute();

        return model;
    }

    /**
     * 测试平行的两条分支达到人工节点后，其他分支继续
     * 经过测试发现，平行的两条分支互不影响
     *
     * @param
     * @return void
     * @author JonnyJiang
     * @date 2019/8/15 20:04
     */

    @Test
    public void testC() throws Exception {
        DeploymentBuilder deploymentBuilder = repositoryService.createDeployment();
        BpmnModel model = getForTestC();
        deploymentBuilder.addBpmnModel(model.getMainProcess().getId() + ".bpmn", model).tenantId(securityUtils.getAppName()).deploy();

        String businessKey = UUID.randomUUID().toString();
        SysUserDO user = new SysUserDO();
        user.setId("jianghoulu");
        FlowInstance processInstance = bpmManager.createFlowInstanceById(model.getMainProcess().getId(), businessKey, user);
        List<Task> tasks = taskService.createTaskQuery().processInstanceId(processInstance.getId()).list();
        LOGGER.info("userTask1 size:" + tasks.size());
        assert tasks.size() == 1;

        for (Task task :
                tasks) {
            taskService.complete(task.getId());
        }
        tasks = taskService.createTaskQuery().processInstanceId(processInstance.getId()).list();

        LOGGER.info("userTask size:" + tasks.size());

        taskService.complete(tasks.get(0).getId());
        LOGGER.info(tasks.get(0).getName() + " completed");

        tasks = taskService.createTaskQuery().processInstanceId(processInstance.getId()).list();

        LOGGER.info("userTask size:" + tasks.size());

        for (Task task :
                tasks) {
            LOGGER.info("task name:" + task.getName());
        }

        List<ProcessInstance> processInstances = runtimeService.createProcessInstanceQuery().processInstanceId(processInstance.getId()).list();

        LOGGER.info("processInstances size:" + processInstances.size());

        taskService.complete(tasks.get(0).getId());
        LOGGER.info(tasks.get(0).getName() + " completed");

        tasks = taskService.createTaskQuery().processInstanceId(processInstance.getId()).list();

        LOGGER.info("userTask size:" + tasks.size());

        for (Task task :
                tasks) {
            LOGGER.info("task name:" + task.getName());
        }

        processInstances = runtimeService.createProcessInstanceQuery().processInstanceId(processInstance.getId()).list();

        LOGGER.info("processInstances size:" + processInstances.size());

    }

    private BpmnModel getForTestD() throws Exception {
        BpmnModel model = new BpmnModel();
        Process process = new Process();
        process.setId("testB");
        process.setName("testB");
        model.addProcess(process);

        StartEvent startEvent = new StartEvent();
        startEvent.setId("startEvent");
        startEvent.setName("开始");
        process.addFlowElement(startEvent);

        UserTask userTask1 = new UserTask();
        userTask1.setId("userTask1");
        userTask1.setName("编制");
        process.addFlowElement(userTask1);

        UserTask userTask2 = new UserTask();
        userTask2.setId("userTask2");
        userTask2.setName("财务审批");
        process.addFlowElement(userTask2);

        UserTask userTask3 = new UserTask();
        userTask3.setId("userTask3");
        userTask3.setName("采购审批");
        process.addFlowElement(userTask3);


        UserTask userTask4 = new UserTask();
        userTask4.setId("userTask4");
        userTask4.setName("总经理审批");
        process.addFlowElement(userTask4);

        EndEvent endEvent = new EndEvent();
        endEvent.setId("endEvent");
        endEvent.setName("结束");
        process.addFlowElement(endEvent);


        addSequenceFlow(process, startEvent, userTask1);
        addSequenceFlow(process, userTask1, userTask2);
        addSequenceFlow(process, userTask1, userTask3);
        addSequenceFlow(process, userTask2, userTask4);
        addSequenceFlow(process, userTask3, userTask4);
        addSequenceFlow(process, userTask4, endEvent);


        printBpmn(model);
//        validateModel(model);
        new BpmnAutoLayout(model).execute();

        return model;
    }

    /**
     * 测试平行的两条分支达到人工节点后，其他分支继续
     * 经过测试发现，平行的两条分支互不影响
     *
     * @param
     * @return void
     * @author JonnyJiang
     * @date 2019/8/15 20:04
     */

    @Test
    public void testD() throws Exception {
        DeploymentBuilder deploymentBuilder = repositoryService.createDeployment();
        BpmnModel model = getForTestD();
        deploymentBuilder.addBpmnModel(model.getMainProcess().getId() + ".bpmn", model).tenantId(securityUtils.getAppName()).deploy();

        String businessKey = UUID.randomUUID().toString();
        SysUserDO user = new SysUserDO();
        user.setId("jianghoulu");
        FlowInstance processInstance = bpmManager.createFlowInstanceById(model.getMainProcess().getId(), businessKey, user);
        List<Task> tasks = taskService.createTaskQuery().processInstanceId(processInstance.getId()).list();
        LOGGER.info("userTask1 size:" + tasks.size());
        assert tasks.size() == 1;

        for (Task task :
                tasks) {
            taskService.complete(task.getId());
        }
        tasks = taskService.createTaskQuery().processInstanceId(processInstance.getId()).list();

        LOGGER.info("userTask size:" + tasks.size());

        taskService.complete(tasks.get(0).getId());
        LOGGER.info(tasks.get(0).getName() + " completed");

        tasks = taskService.createTaskQuery().processInstanceId(processInstance.getId()).list();

        LOGGER.info("userTask size:" + tasks.size());

        for (Task task :
                tasks) {
            LOGGER.info("task name:" + task.getName());
        }

        List<ProcessInstance> processInstances = runtimeService.createProcessInstanceQuery().processInstanceId(processInstance.getId()).list();

        LOGGER.info("processInstances size:" + processInstances.size());

        taskService.complete(tasks.get(0).getId());
        LOGGER.info(tasks.get(0).getName() + " completed");

        tasks = taskService.createTaskQuery().processInstanceId(processInstance.getId()).list();

        LOGGER.info("userTask size:" + tasks.size());

        for (Task task :
                tasks) {
            LOGGER.info("task name:" + task.getName());
        }

        processInstances = runtimeService.createProcessInstanceQuery().processInstanceId(processInstance.getId()).list();

        LOGGER.info("processInstances size:" + processInstances.size());

    }


    private BpmnModel getForTestE() throws Exception {
        BpmnModel model = new BpmnModel();
        Process process = new Process();
        process.setId("testE");
        process.setName("testE");
        model.addProcess(process);

        StartEvent startEvent = new StartEvent();
        startEvent.setId("startEvent");
        startEvent.setName("开始");
        process.addFlowElement(startEvent);

        UserTask userTask1 = new UserTask();
        userTask1.setId("userTask1");
        userTask1.setName("编制");
        process.addFlowElement(userTask1);

        UserTask userTask2 = new UserTask();
        userTask2.setId("userTask2");
        userTask2.setName("财务审批");
        process.addFlowElement(userTask2);

        UserTask userTask3 = new UserTask();
        userTask3.setId("userTask3");
        userTask3.setName("采购审批");
        process.addFlowElement(userTask3);


        UserTask userTask4 = new UserTask();
        userTask4.setId("userTask4");
        userTask4.setName("总经理审批");
        process.addFlowElement(userTask4);

        ParallelGateway parallelGateway = new ParallelGateway();
        parallelGateway.setId("parallelGateway");
        process.addFlowElement(parallelGateway);

        EndEvent endEvent = new EndEvent();
        endEvent.setId("endEvent");
        endEvent.setName("结束");
        process.addFlowElement(endEvent);


        addSequenceFlow(process, startEvent, userTask1);
        addSequenceFlow(process, userTask1, userTask2);
        addSequenceFlow(process, userTask1, userTask3);
        addSequenceFlow(process, userTask2, parallelGateway);
        addSequenceFlow(process, userTask3, parallelGateway);

        addSequenceFlow(process, parallelGateway, userTask4);
        addSequenceFlow(process, userTask4, endEvent);


        printBpmn(model);
//        validateModel(model);
        new BpmnAutoLayout(model).execute();

        return model;
    }

    /**
     * 测试分支到达后，取消其他分支
     *
     * @param
     * @return void
     * @author JonnyJiang
     * @date 2019/8/23 14:10
     */

    @Test
    public void testE() throws Exception {
        DeploymentBuilder deploymentBuilder = repositoryService.createDeployment();
        BpmnModel model = getForTestE();
        deploymentBuilder.addBpmnModel(model.getMainProcess().getId() + ".bpmn", model).tenantId(securityUtils.getAppName()).deploy();

        String businessKey = UUID.randomUUID().toString();
        SysUserDO user = new SysUserDO();
        user.setId("jianghoulu");
        FlowInstance processInstance = bpmManager.createFlowInstanceById(model.getMainProcess().getId(), businessKey, user);
        List<Task> tasks = taskService.createTaskQuery().processInstanceId(processInstance.getId()).list();
        LOGGER.info("created userTask1 size:" + tasks.size());
        assert tasks.size() == 1;

        for (Task task :
                tasks) {
            taskService.complete(task.getId());
        }
        tasks = taskService.createTaskQuery().processInstanceId(processInstance.getId()).list();

        LOGGER.info("completed userTask size:" + tasks.size());

        taskService.complete(tasks.get(0).getId());
        LOGGER.info(tasks.get(0).getName() + " completed");

        tasks = taskService.createTaskQuery().processInstanceId(processInstance.getId()).list();

        LOGGER.info("userTask size:" + tasks.size());

        for (Task task :
                tasks) {
            LOGGER.info("task name:" + task.getName());
        }

        List<ProcessInstance> processInstances = runtimeService.createProcessInstanceQuery().processInstanceId(processInstance.getId()).list();

        LOGGER.info("processInstances size:" + processInstances.size());

        taskService.complete(tasks.get(0).getId());
        LOGGER.info(tasks.get(0).getName() + " completed");

        tasks = taskService.createTaskQuery().processInstanceId(processInstance.getId()).list();

        LOGGER.info("userTask size:" + tasks.size());

        for (Task task :
                tasks) {
            LOGGER.info("task name:" + task.getName());
        }

        processInstances = runtimeService.createProcessInstanceQuery().processInstanceId(processInstance.getId()).list();

        LOGGER.info("processInstances size:" + processInstances.size());

    }


    private BpmnModel getForTestF() throws Exception {
        BpmnModel model = new BpmnModel();
        Process process = new Process();
        process.setId("testE");
        process.setName("testE");
        model.addProcess(process);

        StartEvent startEvent = new StartEvent();
        startEvent.setId("startEvent");
        startEvent.setName("开始");
        process.addFlowElement(startEvent);

        UserTask userTask1 = new UserTask();
        userTask1.setId("userTask1");
        userTask1.setName("编制");
        process.addFlowElement(userTask1);

        UserTask userTask2 = new UserTask();
        userTask2.setId("userTask2");
        userTask2.setName("财务审批");
        process.addFlowElement(userTask2);

        UserTask userTask3 = new UserTask();
        userTask3.setId("userTask3");
        userTask3.setName("采购审批");
        process.addFlowElement(userTask3);

        UserTask userTask4 = new UserTask();
        userTask4.setId("userTask4");
        userTask4.setName("总经理审批");
        process.addFlowElement(userTask4);

        EndEvent endEvent = new EndEvent();
        endEvent.setId("endEvent");
        endEvent.setName("结束");
        process.addFlowElement(endEvent);


        addSequenceFlow(process, startEvent, userTask1);
        addSequenceFlow(process, userTask1, userTask2);
        addSequenceFlow(process, userTask1, userTask3);
        addSequenceFlow(process, userTask2, userTask4);
        addSequenceFlow(process, userTask3, userTask4);
        addSequenceFlow(process, userTask4, endEvent);


        printBpmn(model);
//        validateModel(model);
        new BpmnAutoLayout(model).execute();

        return model;
    }

    /**
     * 测试并行分支直接流入同一节点
     *
     * @param
     * @return void
     * @author JonnyJiang
     * @date 2019/8/23 14:10
     */

    @Test
    public void testF() throws Exception {
        DeploymentBuilder deploymentBuilder = repositoryService.createDeployment();
        BpmnModel model = getForTestF();
        deploymentBuilder.addBpmnModel(model.getMainProcess().getId() + ".bpmn", model).tenantId(securityUtils.getAppName()).deploy();

        String businessKey = UUID.randomUUID().toString();
        SysUserDO user = new SysUserDO();
        user.setId("jianghoulu");
        FlowInstance processInstance = bpmManager.createFlowInstanceById(model.getMainProcess().getId(), businessKey, user);
        List<Task> tasks = taskService.createTaskQuery().processInstanceId(processInstance.getId()).list();
        LOGGER.info("created userTask1 size:" + tasks.size());
        assert tasks.size() == 1;

        for (Task task :
                tasks) {
            taskService.complete(task.getId());
        }
        tasks = taskService.createTaskQuery().processInstanceId(processInstance.getId()).list();

        LOGGER.info("completed userTask size:" + tasks.size());

        taskService.complete(tasks.get(0).getId());
        LOGGER.info(tasks.get(0).getName() + " completed");

        tasks = taskService.createTaskQuery().processInstanceId(processInstance.getId()).list();

        LOGGER.info("userTask size:" + tasks.size());

        for (Task task :
                tasks) {
            LOGGER.info("task name:" + task.getName());
        }

        List<ProcessInstance> processInstances = runtimeService.createProcessInstanceQuery().processInstanceId(processInstance.getId()).list();

        LOGGER.info("processInstances size:" + processInstances.size());

        taskService.complete(tasks.get(0).getId());
        LOGGER.info(tasks.get(0).getName() + " completed");

        tasks = taskService.createTaskQuery().processInstanceId(processInstance.getId()).list();

        LOGGER.info("userTask size:" + tasks.size());

        for (Task task :
                tasks) {
            LOGGER.info("task name:" + task.getName());
        }

        processInstances = runtimeService.createProcessInstanceQuery().processInstanceId(processInstance.getId()).list();

        LOGGER.info("processInstances size:" + processInstances.size());
    }

    private BpmnModel getTestG() {
        BpmnModel model = new BpmnModel();
        Process process = new Process();
        process.setId("testG");
        process.setName("testG");
        model.addProcess(process);

        StartEvent startEvent = new StartEvent();
        startEvent.setId("startEvent");
        startEvent.setName("开始");
        process.addFlowElement(startEvent);

        UserTask userTask1 = new UserTask();
        userTask1.setId("userTask1");
        userTask1.setName("编制");
        process.addFlowElement(userTask1);

        UserTask userTask2 = new UserTask();
        userTask2.setId("userTask2");
        userTask2.setName("财务审批");
        process.addFlowElement(userTask2);

        UserTask userTask3 = new UserTask();
        userTask3.setId("userTask3");
        userTask3.setName("采购审批");
        process.addFlowElement(userTask3);

        ParallelGateway inclusiveGateway = new ParallelGateway();
        inclusiveGateway.setId(generateId());
        inclusiveGateway.setName("测的就是你");
        process.addFlowElement(inclusiveGateway);

        UserTask userTask4 = new UserTask();
        userTask4.setId("userTask4");
        userTask4.setName("看什么看");
        process.addFlowElement(userTask4);

        UserTask userTask5 = new UserTask();
        userTask5.setId("userTask5");
        userTask5.setName("我就看，咋地");
        process.addFlowElement(userTask5);


        addSequenceFlow(process, startEvent, userTask1);
        addSequenceFlow(process, userTask1, userTask2);
        addSequenceFlow(process, userTask1, userTask3);

        SequenceFlow sequenceFlow1 = addSequenceFlow(process, userTask2, inclusiveGateway);
        sequenceFlow1.setConditionExpression("${test==false}");

        SequenceFlow sequenceFlow2 = addSequenceFlow(process, userTask3, inclusiveGateway);
        sequenceFlow2.setConditionExpression("${test==false}");

        addSequenceFlow(process, inclusiveGateway, userTask4);
        addSequenceFlow(process, inclusiveGateway, userTask5);


        EndEvent endEvent = new EndEvent();
        endEvent.setId("endEvent");
        endEvent.setName("结束");
        process.addFlowElement(endEvent);


        SequenceFlow sequenceFlow3 = addSequenceFlow(process, userTask3, endEvent);
        sequenceFlow3.setConditionExpression("${test==true}");

        SequenceFlow sequenceFlow4 = addSequenceFlow(process, userTask2, endEvent);
        sequenceFlow4.setConditionExpression("${test==true}");

        addSequenceFlow(process, userTask4, endEvent);
        addSequenceFlow(process, userTask5, endEvent);
        new BpmnAutoLayout(model).execute();
        return model;
    }

    @Test
    public void testG() {
        DeploymentBuilder deploymentBuilder = repositoryService.createDeployment();
        BpmnModel model = getTestG();
        printModel(model);
        deploymentBuilder.addBpmnModel(model.getMainProcess().getId() + ".bpmn", model).tenantId(securityUtils.getAppName()).deploy();
        ProcessInstance processInstance = runtimeService.startProcessInstanceByKeyAndTenantId(model.getMainProcess().getId(), UUID.randomUUID().toString(), securityUtils.getAppName());
        List<Task> tasks = printTask(processInstance.getId());
        taskService.complete(tasks.get(0).getId());
        tasks = printTask(processInstance.getId());
        Map<String, Object> map = new HashMap<>();
        map.put("test", true);
        taskService.complete(tasks.get(0).getId(), map);
        LOGGER.info("task1 completed");
        printTask(processInstance.getId());
        taskService.complete(tasks.get(1).getId(), map);
        LOGGER.info("task2 completed");
        printTask(processInstance.getId());
    }

    private void printModel(BpmnModel model) {

        BpmnXMLConverter bpmnXMLConverter = new BpmnXMLConverter();

        byte[] convertToXML = bpmnXMLConverter.convertToXML(model);

        String bpmn = null;
        try {
            bpmn = new String(convertToXML, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        LOGGER.info(bpmn);

    }

    private List<Task> printTask(String processInstanceId) {
        LOGGER.info("*****************************");
        List<Task> tasks = taskService.createTaskQuery().processInstanceId(processInstanceId).list();
        for (Task task :
                tasks) {
            LOGGER.info("id: " + task.getId());
            LOGGER.info("name: " + task.getName());
            LOGGER.info("assignee: " + task.getAssignee());
            LOGGER.info("owner: " + task.getOwner());
        }
        return tasks;
    }

    private BpmnModel getForTestH() {

        BpmnModel model = new BpmnModel();
        Process process = new Process();
        process.setId("testH");
        process.setName("testH");
        model.addProcess(process);

        StartEvent startEvent = new StartEvent();
        startEvent.setId("startEvent");
        startEvent.setName("开始");
        process.addFlowElement(startEvent);

        ExclusiveGateway exclusiveGateway = new ExclusiveGateway();
        exclusiveGateway.setId(generateId());
        exclusiveGateway.setName("干啥");
        process.addFlowElement(exclusiveGateway);

        UserTask userTask2 = new UserTask();
        userTask2.setId("userTask2");
        userTask2.setName("财务审批");
        process.addFlowElement(userTask2);

        UserTask userTask3 = new UserTask();
        userTask3.setId("userTask3");
        userTask3.setName("采购审批");
        process.addFlowElement(userTask3);

        InclusiveGateway parallelGateway = new InclusiveGateway();
        parallelGateway.setId(generateId());
        parallelGateway.setName("测的就是你");
        process.addFlowElement(parallelGateway);

        addSequenceFlow(process, startEvent, exclusiveGateway);

        SequenceFlow sequenceFlow1 = addSequenceFlow(process, exclusiveGateway, userTask2);
        sequenceFlow1.setConditionExpression("${test}");
        SequenceFlow sequenceFlow2 = addSequenceFlow(process, exclusiveGateway, userTask3);
        sequenceFlow2.setConditionExpression("${!test}");

        addSequenceFlow(process, userTask2, parallelGateway);

        addSequenceFlow(process, userTask3, parallelGateway);


        EndEvent endEvent = new EndEvent();
        endEvent.setId("endEvent");
        endEvent.setName("结束");
        process.addFlowElement(endEvent);

        addSequenceFlow(process, parallelGateway, endEvent);

        new BpmnAutoLayout(model).execute();
        return model;
    }

    @Test
    public void testH() {
        DeploymentBuilder deploymentBuilder = repositoryService.createDeployment();
        BpmnModel model = getForTestH();
        printModel(model);
        deploymentBuilder.addBpmnModel(model.getMainProcess().getId() + ".bpmn", model).tenantId(securityUtils.getAppName()).deploy();
        Map<String, Object> map = new HashMap<>();
        map.put("test", true);
        ProcessInstance processInstance = runtimeService.startProcessInstanceByKeyAndTenantId(model.getMainProcess().getId(), UUID.randomUUID().toString(), map, securityUtils.getAppName());

        List<Task> tasks = printTask(processInstance.getId());

        tasks.forEach(task -> {
            taskService.complete(task.getId());
            LOGGER.info("task 1th completed: " + task.getName());
        });

        tasks = printTask(processInstance.getId());

        LOGGER.info("processInstance cnt: " + runtimeService.createProcessInstanceQuery().processInstanceId(processInstance.getId()).list().size());

//        tasks.forEach(task -> {
//            taskService.complete(task.getId());
//            LOGGER.info("task 2th completed: " + task.getName());
//        });

//        taskService.complete(tasks.get(0).getId());
//        tasks = printTask(processInstance.getId());
//        Map<String, Object> map = new HashMap<>();
//        map.put("test", true);
//        taskService.complete(tasks.get(0).getId(), map);
//        LOGGER.info("task1 completed");
//        printTask(processInstance.getId());
//        taskService.complete(tasks.get(1).getId(), map);
//        LOGGER.info("task2 completed");
//        printTask(processInstance.getId());
    }

    private BpmnModel getForTestI() {

        BpmnModel model = new BpmnModel();
        Process process = new Process();
        process.setId("testI");
        process.setName("testI");
        model.addProcess(process);

        StartEvent startEvent = new StartEvent();
        startEvent.setId("startEvent");
        startEvent.setName("开始");
        process.addFlowElement(startEvent);

        UserTask userTask2 = new UserTask();
        userTask2.setId("userTask2");
        userTask2.setName("财务审批");
        process.addFlowElement(userTask2);

        UserTask userTask3 = new UserTask();
        userTask3.setId("userTask3");
        userTask3.setName("采购审批");
        process.addFlowElement(userTask3);

        EndEvent endEvent = new EndEvent();
        endEvent.setId("endEvent");
        endEvent.setName("结束");
        process.addFlowElement(endEvent);

        addSequenceFlow(process, startEvent, userTask2);
        addSequenceFlow(process, startEvent, userTask3);
        addSequenceFlow(process, userTask2, endEvent);
        addSequenceFlow(process, userTask3, endEvent);

        new BpmnAutoLayout(model).execute();
        return model;
    }

    @Test
    public void testI() {
        DeploymentBuilder deploymentBuilder = repositoryService.createDeployment();
        BpmnModel model = getForTestI();
        printModel(model);
        deploymentBuilder.addBpmnModel(model.getMainProcess().getId() + ".bpmn", model).tenantId(securityUtils.getAppName()).deploy();
        Map<String, Object> map = new HashMap<>();
        map.put("test", true);
        ProcessInstance processInstance = runtimeService.startProcessInstanceByKeyAndTenantId(model.getMainProcess().getId(), UUID.randomUUID().toString(), map, securityUtils.getAppName());

        List<Task> tasks = printTask(processInstance.getId());

    }

    @Test
    public void testFlowNo() {
        Flow flow = new Flow();
        flow.setWorkNoStyle("{V:111}-{V:222}");
        Map<String, Object> variables = new HashMap<>();
        variables.put("111", "xyz");
        variables.put("222", "opq");
        System.out.println("FlowNo: " + new FlowNoGenerator(flow, variables).generate(UUID.randomUUID().toString()));
    }

//    @Test
//    public void testBack() {
//        bpmManager.backupFlowInstance("9a2fc3a9-384d-11ea-86e8-005056c00008", "taskId", "测试");
//    }

    @Test
    public void testRecover() {
        String flowId = "826c9570-5dfb-11ea-9eb5-02156c2c2505";
        String taskId = "946a2533-5dfb-11ea-9eb5-02156c2c2505";
        bpmManager.recoverFlowInstance(flowId, taskId);
    }
}
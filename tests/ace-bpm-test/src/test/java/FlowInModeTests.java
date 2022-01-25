import com.csicit.ace.bpm.BpmManager;
import com.csicit.ace.bpm.FlowInstance;
import com.csicit.ace.bpmtest.BpmTestApplication;
import com.csicit.ace.common.pojo.domain.SysUserDO;
import com.csicit.ace.common.utils.system.SecurityUtils;
import org.activiti.engine.HistoryService;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.task.Task;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

import static com.csicit.ace.bpmtest.test.DeployTests.TEST_F;

/**
 * @author JonnyJiang
 * @date 2019/8/30 8:35
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = BpmTestApplication.class)
public class FlowInModeTests {
    private static final Logger LOGGER = LoggerFactory.getLogger(FlowInModeTests.class);
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

    /**
     * 测试系统生成的bpmn是否可以按照计划走向执行
     *
     * @param
     * @return void
     * @author JonnyJiang
     * @date 2019/8/30 8:37
     */

    @Test
    public void startTestF() throws Exception {
        SysUserDO user = new SysUserDO();
        user.setId(UUID.randomUUID().toString());
        user.setUserName("jianghoulu");
        user.setRealName("姜厚禄");
        FlowInstance processInstance = bpmManager.createFlowInstanceById(TEST_F, UUID.randomUUID().toString(), user);
        List<Task> tasks = taskService.createTaskQuery().processInstanceId(processInstance.getId()).list();
        for (Integer i = 0; i < tasks.size(); i++) {
            LOGGER.info("TaskInstance " + i + ": " + tasks.get(i).getName());
        }
        assert tasks.size() == 1;
        LOGGER.info("tasks count correct: " + tasks.size());
        Task taskApply = tasks.get(0);
        assert "apply".equals(taskApply.getName());
        LOGGER.info("apply completing");
        taskService.setOwner(taskApply.getId(), "jianghoulu");
        taskService.complete(taskApply.getId());
        LOGGER.info("apply completed");
        tasks = taskService.createTaskQuery().processInstanceId(processInstance.getId()).list();
        for (Integer i = 0; i < tasks.size(); i++) {
            LOGGER.info("TaskInstance " + i + ": " + tasks.get(i).getName());
        }
        assert tasks.size() == 2;
        LOGGER.info("tasks count correct: " + tasks.size());
        List<Task> taskMoneys = tasks.stream().filter(o -> "money".equals(o.getName())).collect(Collectors.toList());
        assert taskMoneys.size() == 1;
        Task taskMoney = taskMoneys.get(0);
        LOGGER.info("money completing");
        Map<String, Object> map = new HashMap<>();
        map.put("firstArriving", true);
        taskService.setOwner(taskMoney.getId(), "heshanshan");
        taskService.complete(taskMoney.getId(), map);
        LOGGER.info("money completed");
        tasks = taskService.createTaskQuery().processInstanceId(processInstance.getId()).list();
        for (Integer i = 0; i < tasks.size(); i++) {
            LOGGER.info("TaskInstance " + i + ": " + tasks.get(i).getName());
        }
        assert tasks.size() == 2;
        LOGGER.info("tasks count correct: " + tasks.size());
        List<Task> taskAudits = tasks.stream().filter(o -> "audit".equals(o.getName())).collect(Collectors.toList());
        assert taskAudits.size() == 1;
        Task taskAudit = taskAudits.get(0);
        map.clear();
        map.put("firstArriving", false);
        LOGGER.info("audit completing");
        taskService.setOwner(taskAudit.getId(), "shishouchuang");
        taskService.complete(taskAudit.getId(), map);
        LOGGER.info("audit completed");
        tasks = taskService.createTaskQuery().processInstanceId(processInstance.getId()).list();
        for (Integer i = 0; i < tasks.size(); i++) {
            LOGGER.info("TaskInstance " + i + ": " + tasks.get(i).getName());
        }
        assert tasks.size() == 1;
        LOGGER.info("tasks count correct: " + tasks.size());
        Task taskManager = tasks.get(0);
        taskService.setOwner(taskManager.getId(), "wumaochuan");
        assert "manager".equals(taskManager.getName());
        LOGGER.info("task name is: " + taskManager.getName());
    }
}
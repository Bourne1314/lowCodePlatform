import com.alibaba.fastjson.JSONObject;
import com.csicit.ace.bpm.enums.UserType;
import com.csicit.ace.bpm.pojo.vo.DeliverInfo;
import com.csicit.ace.bpm.pojo.vo.preset.PresetInfo;
import com.csicit.ace.bpm.pojo.vo.preset.PresetRoute;
import com.csicit.ace.bpm.pojo.vo.preset.PresetUser;
import com.csicit.ace.bpm.pojo.vo.process.DeliverVO;
import com.csicit.ace.bpm.pojo.vo.process.FlowVO;
import com.csicit.ace.bpm.pojo.vo.process.LinkVO;
import com.csicit.ace.bpm.pojo.vo.process.NodeVO;
import com.csicit.ace.bpm.service.WfiFlowService;
import com.csicit.ace.bpm.service.WfiVFlowService;
import com.csicit.ace.bpmtest.BpmTestApplication;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.UUID;

/**
 * @author JonnyJiang
 * @date 2019/9/11 8:13
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = BpmTestApplication.class)
public class CustomTests {
    @Autowired
    com.csicit.ace.bpmtest.test.CustomTests customTests;

    @Test
    public void testA() throws Exception {
        customTests.testA();
    }

    @Test
    public void testB() throws Exception {
        customTests.testB();
    }

    @Test
    public void testC() throws Exception {
//        customTests.testC();
    }

    @Test
    public void printPresetInfo() {
        PresetInfo presetInfo = new PresetInfo();
        presetInfo.setFlowId("流程实例id");
        presetInfo.setTaskId("任务id");
        presetInfo.setFlowVersion(WfiVFlowService.FIRST_FLOW_VERSION);
        PresetRoute presetRoute = new PresetRoute();
        presetRoute.setNodeId("节点id");
        PresetUser presetUser = new PresetUser();
        presetUser.setUserId("用户id");
        presetUser.setUserType(UserType.Host.getValue());
        presetRoute.getPresetUsers().add(presetUser);
        PresetUser presetUser1 = new PresetUser();
        presetUser1.setUserId("用户id");
        presetUser1.setUserType(UserType.Assistant.getValue());
        presetRoute.getPresetUsers().add(presetUser1);
        presetInfo.getPresetRoutes().add(presetRoute);
        PresetRoute presetRoute1 = new PresetRoute();
        presetRoute1.setNodeId("节点id");
        presetInfo.getPresetRoutes().add(presetRoute1);
        System.out.println(JSONObject.toJSONString(presetInfo));
    }
}

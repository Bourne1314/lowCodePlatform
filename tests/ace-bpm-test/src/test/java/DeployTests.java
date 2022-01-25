import com.csicit.ace.bpmtest.BpmTestApplication;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @author JonnyJiang
 * @date 2019/8/28 18:23
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = BpmTestApplication.class)
public class DeployTests {
    @Autowired
    com.csicit.ace.bpmtest.test.DeployTests deployTests;

    /**
     * startEvent-userTask-userTask-endEvent
     *
     * @param
     * @return void
     * @author JonnyJiang
     * @date 2019/8/28 20:46
     */

    @Test
    public void testA() throws Exception {
        deployTests.testA();
    }

    /**
     * startEvent-userTask-[userTask,userTask]-endEvent
     *
     * @param
     * @return void
     * @author JonnyJiang
     * @date 2019/8/28 20:47
     */

    @Test
    public void testB() throws Exception {
        deployTests.testB();
    }

    /**
     * startEvent-userTask-[userTask,userTask]-endEvent
     * 流入模式为0
     *
     * @param
     * @return void
     * @author JonnyJiang
     * @date 2019/8/28 20:47
     */

    @Test
    public void testC() throws Exception {
        deployTests.testC();
    }

    /**
     * startEvent-userTask-[userTask,userTask]-endEvent
     * 带转出条件
     *
     * @param
     * @return void
     * @author JonnyJiang
     * @date 2019/8/28 20:47
     */

    @Test
    public void testD() throws Exception {
        deployTests.testD();
    }

    /**
     * startEvent-userTask-[userTask,userTask]-userTask(flowInMode0)-endEvent
     * 流入模式为0
     *
     * @param
     * @return void
     * @author JonnyJiang
     * @date 2019/8/28 20:47
     */

    @Test
    public void testE() throws Exception {
        deployTests.testE();
    }

    /**
     * startEvent-userTask-[userTask,userTask]-userTask(flowInMode1)-endEvent
     * 流入模式为1
     *
     * @param
     * @return void
     * @author JonnyJiang
     * @date 2019/8/30 8:03
     */

    @Test
    public void testF() throws Exception {
        deployTests.testF();
    }

    /**
     * startEvent-userTask(flowOutMode0)-[userTask,userTask]-userTask(flowInMode1)-endEvent
     * 流出模式为0
     *
     * @param
     * @return void
     * @author JonnyJiang
     * @date 2019/8/30 8:03
     */

    @Test
    public void testG() throws Exception {
        deployTests.testG();
    }

    /**
     * 测试撤回
     * startEvent-userTask(flowOutMode0)-[userTask,userTask(允许撤回)]-userTask(flowInMode1)-endEvent
     *
     * @param
     * @return void
     * @author JonnyJiang
     * @date 2019/9/5 10:49
     */

    @Test
    public void testH() throws Exception {
        deployTests.testH();
    }

    /**
     * 测试开始事件的三种启动类型
     *
     * @author JonnyJiang
     * @date 2019/9/9 16:14
     */

    @Test
    public void testI() throws Exception {
        deployTests.testI();
    }

    @Test
    public void testJ() throws Exception {
        deployTests.testJ();
    }
}
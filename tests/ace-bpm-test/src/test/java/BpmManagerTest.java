import com.csicit.ace.bpmtest.BpmTestApplication;
import com.csicit.ace.bpmtest.test.BpmManagerTests;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @author JonnyJiang
 * @date 2019/9/5 15:49
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = BpmTestApplication.class)
public class BpmManagerTest {
    @Autowired
    BpmManagerTests bpmManagerTests;

    @Test
    public void testCreateFlowInstanceById() {
        bpmManagerTests.testCreateFlowInstanceById();
    }

    @Test
    public void testCreateFlowInstanceByCode() {
        bpmManagerTests.testCreateFlowInstanceByCode();
    }

    @Test
    public void testDeleteFlowInstanceById() {
        bpmManagerTests.testDeleteFlowInstanceById();
    }

    @Test
    public void testDeleteFlowInstanceByCode() {
        bpmManagerTests.testDeleteFlowInstanceByCode();
    }
}
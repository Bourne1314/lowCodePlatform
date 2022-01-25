package com.csicit.ace.platform;

import com.csicit.ace.common.pojo.domain.SysConfigDO;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;

/**
 * @Author: yansiyang
 * @Descruption:
 * @Date: Created in 14:43 2019/4/2
 * @Modified By:
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@WebAppConfiguration
public class TestDM {

    @Test
    public void testdm() {
        SysConfigDO configDO = new SysConfigDO();
        configDO.setName("111");
        configDO.setValue("111");
    }
}

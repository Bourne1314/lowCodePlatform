package com.csicit.ace.bpmtest.service;

import com.csicit.ace.bpm.FlowInstance;
import com.csicit.ace.bpmtest.pojo.StudentDO;
import com.csicit.ace.dbplus.service.IBaseService;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author JonnyJiang
 * @date 2019/11/28 8:36
 */
@Transactional
public interface StudentService extends IBaseService<StudentDO> {
    FlowInstance createInstance(String flowCode, String businessKey);
}
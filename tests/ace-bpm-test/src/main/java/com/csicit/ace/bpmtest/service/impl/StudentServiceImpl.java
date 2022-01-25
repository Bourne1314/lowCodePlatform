package com.csicit.ace.bpmtest.service.impl;

import com.csicit.ace.bpm.BpmManager;
import com.csicit.ace.bpm.FlowInstance;
import com.csicit.ace.bpmtest.mapper.StudentMapper;
import com.csicit.ace.bpmtest.pojo.StudentDO;
import com.csicit.ace.bpmtest.service.StudentService;
import com.csicit.ace.dbplus.service.impl.BaseServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author JonnyJiang
 * @date 2019/11/28 8:36
 */
@Service
public class StudentServiceImpl extends BaseServiceImpl<StudentMapper, StudentDO> implements StudentService {
    @Autowired
    private BpmManager bpmManager;

    @Override
    public FlowInstance createInstance(String flowCode, String businessKey) {
        StudentDO student = new StudentDO();
        student.setId(businessKey);
        save(student);
        return bpmManager.createFlowInstanceByCode(flowCode, businessKey);
    }
}

package com.csicit.ace.bpm.activiti;

import java.util.UUID;

/**
 * @author JonnyJiang
 * @date 2020/6/3 11:19
 */
public class IdGenerator implements org.activiti.engine.impl.cfg.IdGenerator {
    @Override
    public String getNextId() {
        return UUID.randomUUID().toString();
    }
}

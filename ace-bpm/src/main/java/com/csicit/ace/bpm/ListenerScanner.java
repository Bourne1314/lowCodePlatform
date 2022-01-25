package com.csicit.ace.bpm;

import com.csicit.ace.bpm.delegate.*;
import com.csicit.ace.bpm.exception.FlowListenerNotFoundException;
import com.csicit.ace.bpm.exception.TaskListenerNotFoundException;
import com.csicit.ace.common.utils.StringUtils;
import javafx.concurrent.Task;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.*;

/**
 * 事件扫描器
 * @author JonnyJiang
 * @date 2019/11/6 19:45
 */
@Component
@Order(99)
public class ListenerScanner implements ApplicationRunner {
    private static final Logger LOGGER = LoggerFactory.getLogger(ListenerScanner.class);
    /**
     * 任务监听
     */
    private static Map<TaskEventType, List<Class<? extends Listener>>> TaskListeners = new HashMap<>();
    /**
     * 流程监听
     */
    private static Map<FlowEventType, List<Class<? extends Listener>>> FlowListeners = new HashMap<>();

    @Value("${ace.bpm.listenerPackages:#{null}}")
    private String listenerPackages;

    /**
     * 判断流程监听是否存在
     * @param flowEventType
     * @param className
     * @return
     */
    public static boolean existFlowListener(FlowEventType flowEventType, String className) {
        for (Class cName : getFlowListeners().get(flowEventType)) {
            if (cName.getName().equals(className)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 判断任务监听是否存在
     * @param taskEventType
     * @param className
     * @return
     */
    public static boolean existTaskListener(TaskEventType taskEventType, String className) {
        for (Class cName : getTaskListeners().get(taskEventType)) {
            if (cName.getName().equals(className)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void run(ApplicationArguments args) {
        LOGGER.debug("listeners scanning");
        if (StringUtils.isEmpty(listenerPackages)) {
            scanListeners(new Reflections());
        } else {
            String[] arr = StringUtils.split(listenerPackages, ",");
            List<String> packages = new ArrayList<>();
            for (String p : arr) {
                if (StringUtils.isNotBlank(p)) {
                    p = p.replace(" ", "");
                    if (!packages.contains(p)) {
                        packages.add(p);
                        scanListeners(new Reflections(packages));
                    }
                }
            }
        }
        LOGGER.debug("listeners scanned");
        LOGGER.debug("flow listeners:");
        if (LOGGER.isDebugEnabled()) {
            FlowListeners.values().forEach(this::debug);
        }
        LOGGER.debug("task listeners:");
        if (LOGGER.isDebugEnabled()) {
            TaskListeners.values().forEach(this::debug);
        }
    }

    private void debug(List<Class<? extends Listener>> listeners) {
//        Collections.sort(listeners);
        listeners.forEach(o -> LOGGER.debug(o.getName()));
    }

    /**
     * 扫描监听
     * @param reflections
     */
    private void scanListeners(Reflections reflections) {
        scanFlowListeners(reflections);
        scanTaskListeners(reflections);
    }

    /**
     * 扫描流程监听
     * @param reflections
     */
    private void scanFlowListeners(Reflections reflections) {
        LOGGER.debug("flow listeners scanning");
        scanFlowListeners(reflections, FlowEventType.Creating, FlowCreatingListener.class);
        scanFlowListeners(reflections, FlowEventType.Created, FlowCreatedListener.class);
        scanFlowListeners(reflections, FlowEventType.Ended, FlowEndedListener.class);
        scanFlowListeners(reflections, FlowEventType.Deleting, FlowDeletingListener.class);
        scanFlowListeners(reflections, FlowEventType.Deleted, FlowDeletedListener.class);
        LOGGER.debug("flow listeners scanned");
    }

    /**
     * 扫描任务监听
     * @param reflections
     */
    private void scanTaskListeners(Reflections reflections) {
        LOGGER.debug("task listeners scanning");
        scanTaskListeners(reflections, TaskEventType.Create, TaskCreateListener.class);
        scanTaskListeners(reflections, TaskEventType.Assignment, TaskAssignmentListener.class);
        scanTaskListeners(reflections, TaskEventType.Completing, TaskCompletingListener.class);
        scanTaskListeners(reflections, TaskEventType.Completed, TaskCompletedListener.class);
        scanTaskListeners(reflections, TaskEventType.Delete, TaskDeleteListener.class);
        scanTaskListeners(reflections, TaskEventType.Rejecting, TaskRejectingListener.class);
        scanTaskListeners(reflections, TaskEventType.Rejected, TaskRejectedListener.class);
        scanTaskListeners(reflections, TaskEventType.Withdrawing, TaskRejectingListener.class);
        scanTaskListeners(reflections, TaskEventType.Withdrawn, TaskRejectedListener.class);
        LOGGER.debug("task listeners scanned");
    }

    private void scanFlowListeners(Reflections reflections, FlowEventType flowEventType, Class clazz) {
        FlowListeners.put(flowEventType, getClassNames(reflections, clazz));
    }

    private void scanTaskListeners(Reflections reflections, TaskEventType taskEventType, Class clazz) {
        TaskListeners.put(taskEventType, getClassNames(reflections, clazz));
    }

    /**
     * 获取监听对象
     * @param reflections
     * @param clazz
     * @return
     */
    private List<Class<? extends Listener>> getClassNames(Reflections reflections, Class clazz) {
        List<Class<? extends Listener>> classNames = new ArrayList<>();
        Set<Class<? extends Listener>> listenerClasses = reflections.getSubTypesOf(clazz);
        for (Class listener : listenerClasses) {
            if (listener.isInterface()) {
                classNames.addAll(getClassNames(reflections, listener.getClass()));
            } else {
                classNames.add(listener);
            }
        }
        return classNames;
    }

    /**
     * 获取流程监听实例
     * @param flowEventType
     * @param className
     * @return
     */
    public static Object getFlowListenerInstance(FlowEventType flowEventType, String className) {
        try {
            for (Class cName : getFlowListeners().get(flowEventType)) {
                if (cName.getName().equals(className)) {
                    return cName.newInstance();
                }
            }
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
            throw new BpmException(e.getMessage());
        }
        throw new FlowListenerNotFoundException(flowEventType, className);
    }

    /**
     * 获取任务监听实例
     * @param taskEventType
     * @param className
     * @return
     */
    public static Object getTaskListenerInstance(TaskEventType taskEventType, String className) {
        try {
            for (Class cName : getTaskListeners().get(taskEventType)) {
                if (cName.getName().equals(className)) {
                    return cName.newInstance();
                }
            }
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
            throw new BpmException(e.getMessage());
        }
        throw new TaskListenerNotFoundException(taskEventType, className);
    }

    public static Map<TaskEventType, List<Class<? extends Listener>>> getTaskListeners() {
        return TaskListeners;
    }

    public static Map<FlowEventType, List<Class<? extends Listener>>> getFlowListeners() {
        return FlowListeners;
    }
}
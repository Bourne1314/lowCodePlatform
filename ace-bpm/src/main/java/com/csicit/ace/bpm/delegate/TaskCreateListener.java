package com.csicit.ace.bpm.delegate;

import com.csicit.ace.bpm.delegate.args.TaskCreateEventArgs;

/**
 * 任务创建监听
 *
 * @author JonnyJiang
 * @date 2019/11/7 18:06
 */
public interface TaskCreateListener extends Listener {
    /**
     * 通知
     *
     * @param args 参数
     * @author JonnyJiang
     * @date 2019/11/7 20:20
     */

    void notify(TaskCreateEventArgs args);
}
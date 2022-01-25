package com.csicit.ace.bpm.delegate;

import com.csicit.ace.bpm.delegate.args.TaskCompletingEventArgs;

/**
 * 任务结束前监听
 *
 * @author JonnyJiang
 * @date 2019/11/7 18:07
 */
public interface TaskCompletingListener extends Listener {
    /**
     * 通知
     *
     * @param args 参数
     * @author JonnyJiang
     * @date 2019/11/7 20:20
     */

    void notify(TaskCompletingEventArgs args);
}
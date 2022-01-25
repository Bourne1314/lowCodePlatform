package com.csicit.ace.bpm.delegate;

import com.csicit.ace.bpm.delegate.args.TaskRejectingEventArgs;

/**
 * @author JonnyJiang
 * @date 2019/12/27 8:16
 */
public interface TaskRejectingListener extends Listener {
    /**
     * 通知
     *
     * @param args 参数
     * @author JonnyJiang
     * @date 2019/12/27 8:18
     */

    void notify(TaskRejectingEventArgs args);
}

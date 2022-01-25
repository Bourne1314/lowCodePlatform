package com.csicit.ace.bpm.delegate;

import com.csicit.ace.bpm.delegate.args.TaskRejectedEventArgs;

/**
 * @author JonnyJiang
 * @date 2019/12/27 8:17
 */
public interface TaskRejectedListener extends Listener {
    /**
     * 通知
     *
     * @param args 参数
     * @author JonnyJiang
     * @date 2019/12/27 8:18
     */

    void notify(TaskRejectedEventArgs args);
}

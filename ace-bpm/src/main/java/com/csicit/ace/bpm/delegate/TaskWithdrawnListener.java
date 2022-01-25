package com.csicit.ace.bpm.delegate;

import com.csicit.ace.bpm.delegate.args.TaskWithdrawnEventArgs;

/**
 * @author JonnyJiang
 * @date 2020/3/19 15:06
 */
public interface TaskWithdrawnListener extends Listener {
    /**
     * 通知
     *
     * @param args 参数
     * @author JonnyJiang
     * @date 2019/11/7 20:20
     */

    void notify(TaskWithdrawnEventArgs args);
}

package com.csicit.ace.bpm.delegate;

import com.csicit.ace.bpm.delegate.args.TaskDeleteEventArgs;

/**
 * 任务删除监听
 *
 * @author JonnyJiang
 * @date 2019/11/7 18:09
 */
public interface TaskDeleteListener extends Listener {
    /**
     * 通知
     *
     * @param args 参数
     * @author JonnyJiang
     * @date 2019/11/7 20:20
     */

    void notify(TaskDeleteEventArgs args);
}

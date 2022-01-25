package com.csicit.ace.bpm.delegate;

import com.csicit.ace.bpm.delegate.args.FlowCreatedEventArgs;

/**
 * 流程创建后监听
 *
 * @author JonnyJiang
 * @date 2019/11/7 17:58
 */
public interface FlowCreatedListener extends Listener {
    /**
     * 通知
     *
     * @param args 参数
     * @author JonnyJiang
     * @date 2019/11/7 20:20
     */

    void notify(FlowCreatedEventArgs args);
}

package com.csicit.ace.bpm.delegate;

import com.csicit.ace.bpm.delegate.args.FlowDeletedEventArgs;

/**
 * @author JonnyJiang
 * @date 2019/11/7 17:58
 */
public interface FlowDeletedListener extends Listener {
    /**
     * 通知
     *
     * @param args 参数
     * @author JonnyJiang
     * @date 2019/11/7 20:20
     */

    void notify(FlowDeletedEventArgs args);
}

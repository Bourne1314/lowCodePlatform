package com.csicit.ace.bpm.delegate;

import com.csicit.ace.bpm.delegate.args.FlowEndEventArgs;
import org.springframework.stereotype.Component;

/**
 * 流程结束监听
 *
 * @author JonnyJiang
 * @date 2019/11/7 17:58
 */
@Component
public interface FlowEndedListener extends Listener {
    /**
     * 通知
     *
     * @param args 参数
     * @author JonnyJiang
     * @date 2019/11/7 20:20
     */

    void notify(FlowEndEventArgs args);
}

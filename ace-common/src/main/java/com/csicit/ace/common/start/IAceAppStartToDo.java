package com.csicit.ace.common.start;

/**
 * 服务启动接口
 * 其余服务可实现此接口 在 服务 启动的时候进行一些操作
 * @author FourLeaves
 * @version V1.0
 * @date 2019/12/24 10:44
 */
public interface IAceAppStartToDo {
    /**
     * @return 
     * @author FourLeaves
     * @date 2019/12/24 14:02
     */
    void run();
}

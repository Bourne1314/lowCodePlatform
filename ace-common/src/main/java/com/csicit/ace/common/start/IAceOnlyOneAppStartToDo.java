package com.csicit.ace.common.start;

/**
 * 服务启动接口
 * 其余服务可实现此接口 在 服务 启动的时候进行一些操作
 * @author FourLeaves
 * @version V1.0
 * @date 2019/12/24 10:44
 */
public interface IAceOnlyOneAppStartToDo {
    /**
     * 注意此接口的功能是当且仅当
     *    nacos没有此服务注册
     *    数据库此应用没有锁 或 锁被释放
     *    的时候执行
     * @return 
     * @author FourLeaves
     * @date 2019/12/24 14:02
     */
    void run();
}

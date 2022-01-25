package com.csicit.ace.interfaces.service;

/**
 * app启动执行相关
 *
 * 移除放入cloudimpl
 * @author FourLeaves
 * @version V1.0
 * @date 2019/12/24 11:46
 */
public interface IStart {

    /**
     *         // 应用启动过程中执行的逻辑必须满足两个条件
     *         // 1、nacos上注册的应用应该没有健康实例
     *         // 2、数据库中的应用启动锁应该是释放状态
     * 判断是否app启动是否可以执行相关操作
     * 根据时间自动修复--应用锁住后解锁失败，这时下一个应用启动会根据上一个的启动时间去判断锁的状态，有一个阀值
     * @param
     * @return
     * @author FourLeaves
     * @date 2019/12/24 11:38
     */
    boolean canAppStartToDo();

    /**
     * app启动完成的相关操作--解锁
     * @param
     * @return
     * @author FourLeaves
     * @date 2019/12/24 11:39
     */
    boolean completeAppStartToDo();
}

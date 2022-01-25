package com.csicit.ace.bpm.pojo.vo.wfd;

/**
 * 任务状态
 *
 * @author JonnyJiang
 * @date 2019/10/14 15:33
 */
public enum TaskState {
    /**
     * 待接收
     */
    PendingClaim(0),
    /**
     * 办理中
     */
    Executing(1),
    /**
     * 已办结
     */
    Completed(2);

    private Integer value;

    TaskState(Integer value) {
        this.value = value;
    }

    public Integer getValue() {
        return value;
    }
}

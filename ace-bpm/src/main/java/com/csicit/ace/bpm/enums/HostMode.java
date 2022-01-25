package com.csicit.ace.bpm.enums;

/**
 * 主办模式
 *
 * @author JonnyJiang
 * @date 2019/9/25 20:37
 */
public enum HostMode {
    /**
     * 最先接收工作的办理人主办
     */
    FirstClaim(0),
    /**
     * 指定主办人
     */
    Specified(1),
    /**
     * 在允许结转之后办理的办理人主办
     */
    AllowDeliver(2),
    /**
     * 任何参与人都可主办
     */
    Everybody(3);

    private Integer value;

    HostMode(Integer value) {
        this.value = value;
    }

    public Integer getValue() {
        return this.value;
    }

    public Boolean isEquals(Integer hostMode) {
        return this.value.equals(hostMode);
    }
}

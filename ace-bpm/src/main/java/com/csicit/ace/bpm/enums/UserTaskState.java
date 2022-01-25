package com.csicit.ace.bpm.enums;

import java.util.Objects;

/**
 * 办理状态
 *
 * @author FourLeaves
 * @version V1.0
 * @date 2020/4/10 10:21
 */
public enum UserTaskState {
    /**
     * 未阅读
     */
    UN_READ("UN_READ", 1),

    /**
     * 已阅读
     */
    READED("READED", 10),

    /**
     * 未接收
     */
    UN_CLAIM("UN_CLAIM", 20),

    /**
     * 已接收
     */
    CLAIMED("CLAIMED", 30),

    /**
     * 办理中
     */
    WORKING("WORKING", 40),

    /**
     * 已办结
     */
    WORKED("WORKED", 50);


    /**
     * 状态名称
     */
    private String state;

    /**
     * 状态优先级
     */
    private Integer sortIndex;

    UserTaskState(String state, Integer sortIndex) {
        this.sortIndex = sortIndex;
        this.state = state;
    }

    public String getState() {
        return state;
    }

    public Integer getSortIndex() {
        return sortIndex;
    }

    /**
     * 比较状态优先级
     * @param state1
     * @param state2
     * @return 
     * @author FourLeaves
     * @date 2020/4/10 10:45
     */
    public static boolean compareByState(String state1, String state2) {
        return getSortIndexByState(state1) > getSortIndexByState(state2);
    }

    /**
     * 通过状态名称获取优先级
     * @param state
     * @return 
     * @author FourLeaves
     * @date 2020/4/10 10:45
     */
    public static Integer getSortIndexByState(String state) {
        if (Objects.equals(state, "UN_READ")) {
            return UN_READ.getSortIndex();
        }
        if (Objects.equals(state, "READED")) {
            return READED.getSortIndex();
        }
        if (Objects.equals(state, "UN_CLAIM")) {
            return UN_CLAIM.getSortIndex();
        }
        if (Objects.equals(state, "CLAIMED")) {
            return CLAIMED.getSortIndex();
        }
        if (Objects.equals(state, "WORKING")) {
            return WORKING.getSortIndex();
        }
        if (Objects.equals(state, "WORKED")) {
            return WORKED.getSortIndex();
        }
        return 1;
    }
}

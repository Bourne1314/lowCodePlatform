package com.csicit.ace.bpm.pojo.vo.free;

import com.csicit.ace.bpm.pojo.vo.DeliverUser;
import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @author JonnyJiang
 * @date 2020/12/1 8:20
 */
@Data
public class FreeStep implements Serializable {
    /**
     * 步骤编号
     */
    private Integer stepNo;
    /**
     * 主键
     */
    private String id;
    /**
     * 主办模式
     */
    private Integer hostMode;
    /**
     * 经办人
     */
    private List<DeliverUser> deliverUsers = new ArrayList<>();

    public List<DeliverUser> getDeliverUsers() {
        if (deliverUsers == null) {
            deliverUsers = new ArrayList<>();
        }
        return deliverUsers;
    }
}
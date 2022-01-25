package com.csicit.ace.bpm.exception;

import com.csicit.ace.bpm.pojo.vo.DeliverUser;
import com.csicit.ace.bpm.pojo.vo.wfd.Node;
import com.csicit.ace.bpm.utils.LocaleUtils;

import java.util.List;

public class DeliverUserDuplicateException extends BpmSystemException {
    private final Node node;
    private final List<DeliverUser> deliverUsers;

    public DeliverUserDuplicateException(Node node, List<DeliverUser> deliverUsers) {
        super(BpmErrorCode.S00032, LocaleUtils.getDeliverUserDuplicate());
        this.node = node;
        this.deliverUsers = deliverUsers;
    }
}

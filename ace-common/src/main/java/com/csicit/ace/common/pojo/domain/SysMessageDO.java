package com.csicit.ace.common.pojo.domain;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.csicit.ace.common.pojo.AbstractBaseDomain;
import com.csicit.ace.common.utils.StringUtils;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.StringJoiner;

/**
 * @author shanwj
 * @date 2019/7/4 14:06
 */
@Data
@TableName("SYS_MESSAGE")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SysMessageDO extends AbstractBaseDomain {

    /**
     * 标题
     */
    private String title;
    /**
     * 内容
     */
    private String content;
    /**
     * 链接地址
     */
    private String url;
    /**
     * 应用id
     */
    private String appId;
    /**
     * 消息实现类别（socket.io、微信、email...）
     */
    private String type;
    /**
     * 备注
     */
    private String remark;
    /**
     * 接受用户,用户id，用","隔开
     */
    private String receiveUsers;
    /**
     * 创建人
     */
    private String createUser;
    /**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;
    /**
     * 消息业务类型(通知消息、业务消息)
     */
    private String businessType;

    /**
     * 处理消息类型
     */
    private String handlerMsgType;

    /**
     * 拓展频道名称
     */
    @TableField(exist = false)
    private String channelName;
    /**
     * 消息模板填充数据
     */
    @TableField(exist = false)
    private Map<String,Object> data;
    /**
     * 平台模板id
     */
    @TableField(exist = false)
    private String templateId;
    /**
     * 消息模板
     */
    @TableField(exist = false)
    private SysMsgTemplateDO sysMsgTemplate;
    /**
     * 消息通道对象
     */
    @TableField(exist = false)
    private SysMsgSendTypeDO sysMsgSendType;


    public SysMessageDO() {

    }

    public SysMessageDO(String title, String content, String receiveUsers) {
        this.title = title;
        this.content = content;
        this.receiveUsers = receiveUsers;
    }

    public SysMessageDO(String title, String content, List<String> receiveUsers) {
        this.title = title;
        this.content = content;
        StringJoiner sj = new StringJoiner(",");
        receiveUsers.forEach(userId -> {
            sj.add(userId);
        });
        this.receiveUsers = sj.toString();
    }
    public SysMessageDO(String title, String content, String url,List<String> receiveUsers) {
        this.title = title;
        this.content = content;
        this.url = url;
        StringJoiner sj = new StringJoiner(",");
        receiveUsers.forEach(userId -> {
            sj.add(userId);
        });
        this.receiveUsers = sj.toString();
    }

    public SysMessageDO(String appId, String channelName, String templateId, Map<String,Object> data, List<String> receiveUsers) {
        this.appId = appId;
        this.channelName = channelName;
        this.templateId = templateId;
        this.data = data;
        StringJoiner sj = new StringJoiner(",");
        receiveUsers.forEach(userId -> {
            sj.add(userId);
        });
        this.receiveUsers = sj.toString();
    }

    public SysMessageDO(String title, String content) {
        this.title = title;
        this.content = content;
    }

}

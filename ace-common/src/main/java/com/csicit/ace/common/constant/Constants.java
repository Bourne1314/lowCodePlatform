package com.csicit.ace.common.constant;

import com.google.common.collect.ImmutableList;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 公共的参数
 *
 * @author shanwj
 * @date 2019/5/30 15:52
 */
public class Constants {

    public static final String REPLACE = "REPLACE";

    public static final String ACE_SERVICE_USER = "aceServiceUser";

    /*********************业务编码使用*************************/
    public static final String ACE_CODE_NULL_PART_VALUE_TAG = "ACE_CODE_NULL_PART_VALUE_TAG";

    public static final String ACE_CODE_PARAM_KEY_APPID = "ACE_CODE_PARAM_KEY_APP_ID";

    public static final String PACE_CODE_ARAM_KEY_TEMPLATEKEY = "PACE_CODE_ARAM_KEY_TEMPLATE_KEY";
    /*********************业务编码使用*************************/
    //平台服务名
    public static final String PLATFORM = "platform";

    public static final String GATEWAY = "gateway";

    public static final String FILESERVER = "fileserver";

    public static final String DEV_PLATFORM = "dev-platform";

    public static final String REPORT = "report";

    public static final String QUARTZ = "quartz";

    public static final String ORGAUTH = "orgauth";

    public static final String PUSHSERVER = "push-server";

    public static final String PUSH = "push";

    /**
     * 平台的服务列表
     */
    public static final List<String> AppNames;

    public static final String SocketMessenger = "socketMessengerImpl";

    public static final String EmailMessenger = "emailMessengerImpl";

    public static final String SmsMessenger = "smsMessengerImpl";

    public static final String WxMicroAppMessenger = "wxMicroAppMessengerImpl";

    public static final String WxSubscriptionMessenger = "wxSubscriptionMessengerImpl";

    /**
     * 转交
     */
    public static String BpmTransferNoticeTemplate = "BpmTransferNoticeTemplate";
    /**
     *
     */
    public static String BpmTransferNoticeChannelName = "BpmTransferNoticeChannelName";
    public static String BpmTransferNoticeEvent = "AceHandOver";
    /**
     * 驳回
     */
    public static String BpmRejectNoticeTemplate = "BpmRejectNoticeTemplate";
    /**
     *
     */
    public static String BpmRejectNoticeChannelName = "BpmRejectNoticeChannelName";
    public static String BpmRejectNoticeEvent = "AceReject";
    /**
     * 驳回
     */
    public static String BpmUrgeNoticeTemplate = "BpmUrgeNoticeTemplate";
    /**
     *
     */
    public static String BpmUrgeNoticeChannelName = "BpmUrgeNoticeChannelName";
    public static String BpmUrgeNoticeEvent = "AceReject";
    /**
     * 撤回
     */
    public static String BpmDrawBackNoticeTemplate = "BpmDrawBackNoticeTemplate";
    /**
     *
     */
    public static String BpmDrawBackNoticeChannelName = "BpmDrawBackNoticeChannelName";
    public static String BpmDrawBackNoticeEvent = "AceDrawBack";
    /**
     * 结束
     */
    public static String BpmFinshNoticeTemplate = "BpmFinshNoticeTemplate";
    /**
     *
     */
    public static String BpmFinshNoticeChannelName = "BpmFinshNoticeChannelName";
    public static String BpmFinshNoticeEvent = "AceCompleted";
    /**
     * 委托
     */
    public static String BpmDelegateNoticeTemplate = "BpmDelegateNoticeTemplate";
    /**
     *
     */
    public static String BpmDelegateNoticeChannelName = "BpmDelegateNoticeChannelName";
    public static String BpmDelegateNoticeEvent = "AceEntrust";
    /**
     * 委托
     */
    public static String BpmCommentNoticeTemplate = "BpmCommentNoticeTemplate";
    /**
     *
     */
    public static String BpmCommentNoticeChannelName = "BpmCommentNoticeChannelName";
    /**
     * 一键激活
     */
    public static String OneClickActivateEvent = "oneClickActivateEvent";
    /**
     * 大屏消息推送
     */
    public static String BLADEVISUALMSGPUSH="pushInfo";
    /**
     * 初始化扫描包路径
     */
    public static String BasePackages = "";

    /**
     * 是否开发应用
     */
    public static boolean isDevPlatform = false;

    /**
     * 是否单体应用
     */
    public static boolean isMonomerApp = false;

    /**
     * 是否zuul单体版应用
     */
    public static boolean isZuulApp = false;

    public static Map<String, Map<String, String>> BpmTemplateDefaultData = new HashMap<>(16);

    static {
        List<String> tempAppNames =
                ImmutableList.of(GATEWAY, PLATFORM, FILESERVER,
                        DEV_PLATFORM, REPORT, QUARTZ, ORGAUTH, PUSHSERVER, PUSH);
        AppNames = Collections.unmodifiableList(tempAppNames);
        initTemplateData();
    }

    /**
     * 定义工作流模板
     *
     * @author shanwj
     * @date 2020/4/24 14:50
     */
    private static void initTemplateData() {
        Map<String, String> transferData = new HashMap<>(16);
        transferData.put("title", "待办提醒");
        transferData.put("content", "${transferUserName}已完成${transferTaskName}任务,并将工作转交给您!");
        //transferData.put("url", "vue,processTaskPageFromMsm/?taskId=${taskId}");
        transferData.put("url", "code,openTaskWindow({taskId: '${taskId}'})");
        BpmTemplateDefaultData.put(BpmTransferNoticeTemplate, transferData);
        Map<String, String> rejectData = new HashMap<>(16);
        rejectData.put("title", "驳回提醒");
        rejectData.put("content", "您的任务:${taskName}已被${rejectUserName}驳回,驳回意见为:${rejectComment}!");
        //rejectData.put("url", "vue,processTaskPageFromMsm/?formId=${formId}&flowCode=${flowCode}");
        rejectData.put("url", "code,openTaskWindow({formId: '${formId}', flowCode: '${flowCode}'})");
        BpmTemplateDefaultData.put(BpmRejectNoticeTemplate, rejectData);
        Map<String, String> urgeData = new HashMap<>(16);
        urgeData.put("title", "催办提醒");
        urgeData.put("content", "${urgeUserName}对${urgeTaskName}进行了催办，请您尽快办理!");
        //rejectData.put("url", "vue,processTaskPageFromMsm/?formId=${formId}&flowCode=${flowCode}");
        rejectData.put("url", "code,openTaskWindow({formId: '${formId}', flowCode: '${flowCode}'})");
        BpmTemplateDefaultData.put(BpmUrgeNoticeTemplate, urgeData);
        Map<String, String> backData = new HashMap<>(16);
        backData.put("title", "撤回提醒");
        backData.put("content", "您的任务:${taskName}已被${drawBackUserName}撤回!");
        //backData.put("url", "vue,processTaskPageFromMsm/?formId=${formId}&flowCode=${flowCode}");
        backData.put("url", "code,openTaskWindow({formId: '${formId}', flowCode: '${flowCode}'})");
        BpmTemplateDefaultData.put(BpmDrawBackNoticeTemplate, backData);
        Map<String, String> finishData = new HashMap<>(16);
        finishData.put("title", "流程结束提醒");
        finishData.put("content", "您的${workFlowName}流程已办结!");
        //finishData.put("url", "vue,processTaskPageFromMsm/?formId=${formId}&flowCode=${flowCode}");
        finishData.put("url", "code,openTaskWindow({formId: '${formId}', flowCode: '${flowCode}'})");
        BpmTemplateDefaultData.put(BpmFinshNoticeTemplate, finishData);
        Map<String, String> delegateData = new HashMap<>(16);
        delegateData.put("title", "委托提醒");
        delegateData.put("content", "${delegateUserName}将${delegateTaskName}委托给您!");
        //delegateData.put("url", "vue,processTaskPageFromMsm/?taskId=${taskId}");
        delegateData.put("url", "code,openTaskWindow({taskId: '${taskId}'})");
        BpmTemplateDefaultData.put(BpmDelegateNoticeTemplate, delegateData);
        Map<String, String> commentData = new HashMap<>(16);
        commentData.put("title", "评论提醒");
        commentData.put("content", "${userName}对流程${flowNo}有新的评论!");
        commentData.put("url", "code,openTaskWindow({formId: '${formId}', flowCode: '${flowCode}'})");
        BpmTemplateDefaultData.put(BpmCommentNoticeTemplate, commentData);
    }

}

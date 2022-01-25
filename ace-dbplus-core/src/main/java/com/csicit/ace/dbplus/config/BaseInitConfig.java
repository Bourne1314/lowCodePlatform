package com.csicit.ace.dbplus.config;


/**
 * 平台的服务的启动项，除了platform、gateway
 *
 * @author FourLeaves
 * @version V1.0
 * @date 2019/12/26 10:44
 */
//@Component
//@Order(-1)
//@DependsOn("springUtilForGroupApp")
public class BaseInitConfig {//implements ApplicationRunner {

//    private static Logger logger = LoggerFactory.getLogger(BaseInitConfig.class);
//
//    // 锁数据是否成功
//    public static boolean lockDataSuccess = false;
//
//    // 平台的服务列表
//    public static final List<String> AppNames = Arrays.asList(new String[]{"gateway", "platform",
//            "auth", "acepublic", "fileserver", "dashboards", "report", "quartz", "orgauth"});
//
//    // 静态代码块进行锁数据
//    static {
//        try {
//            SysGroupAppServiceD sysGroupAppServiceDF = SpringUtilForGroupApp.getSysGroupAppServiceD();
//            SysGroupAppDO app = sysGroupAppServiceDF.getCurrentApp();
//            if (app != null && AppNames.contains(app.getId())) {
//                String appName = app.getId();
//                int dataVersion = app.getDataVersion() + 1;
//                if (sysGroupAppServiceDF.update(new SysGroupAppDO(), new UpdateWrapper<SysGroupAppDO>()
//                        .set("data_version", dataVersion).eq("id", appName).eq("data_version", app.getDataVersion()))) {
//                    BaseInitConfig.lockDataSuccess = true;
//                    logger.info("This App " + appName + " will do the thing Only One App Can Do!");
//                }
//            }
//        } catch (Exception e) {
//
//        }
//    }
//
//    @Value("${spring.application.name}")
//    private String appName;
//
//    @Value("${spring.datasource.driverClassName:driverClassName}")
//    private String driverClassName;
//    /**
//     * 启动时app进行的一些操作
//     */
//    @NonNull
//    @Autowired
//    List<IAceAppStartToDo> aceAppStartToDos;
//
//    @NonNull
//    @Autowired
//    DiscoveryUtils discoveryUtils;
//
//    /**
//     * 启动时只能在一个app上进行的某些操作
//     */
//    @NonNull
//    @Autowired
//    List<IAceOnlyOneAppStartToDo> aceOnlyOneAppStartToDos;
//
//    @Autowired
//    SysGroupAppServiceD sysGroupAppServiceD;
//
//    // 最顶级包名
//    static String basePkgName = "com.csicit.ace";
//
//    /**
//     * 获取平台应用的顶级包名
//     *
//     * @param appName
//     * @return
//     * @author FourLeaves
//     * @date 2019/12/26 11:21
//     */
//    public static String getPkgName(String appName) {
//        if (Objects.equals(appName, "acepublic")) {
//            return basePkgName + ".publics";
//        }
//        return basePkgName + "." + appName;
//    }
//
//    //@Override
//    public void run(ApplicationArguments args) {
//        // 只运行平台的服务
//        if (AppNames.contains(appName)) {
//            if (lockDataSuccess || driverClassName.contains("mysql")) {
//                // nacos上注册的应用应该没有健康实例
//                if (discoveryUtils != null) {
//                    if (discoveryUtils.getHealthyInstances(appName).size() > 1) {
//                        return;
//                    }
//                }
//                for (IAceOnlyOneAppStartToDo aceOnlyOneAppStartToDo : aceOnlyOneAppStartToDos) {
//                    aceOnlyOneAppStartToDo.run();
//                }
//            }
//            for (IAceAppStartToDo aceAppStartToDo : aceAppStartToDos) {
//                aceAppStartToDo.run();
//            }
//        }
//    }
}

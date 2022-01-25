package com.csicit.ace.dev.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.csicit.ace.common.pojo.domain.dev.ProServiceDO;
import com.csicit.ace.common.utils.HttpClient;
import com.csicit.ace.common.utils.UuidUtils;
import com.csicit.ace.data.persistent.mapper.ProServiceMapper;
import com.csicit.ace.data.persistent.service.impl.BaseServiceImpl;
import com.csicit.ace.dev.service.ProServiceService;
import com.csicit.ace.dev.util.CMDUtil;
import com.csicit.ace.dev.util.DevConstants;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * 服务管理 实例对象访问接口实现
 *
 * @author shanwj
 * @date 2019/11/25 11:12
 */
@Service("proServiceService")
public class ProServiceServiceImpl extends BaseServiceImpl<ProServiceMapper, ProServiceDO> implements
        ProServiceService {

    @Autowired
    HttpClient client;




    /**
     * 新增服务
     *
     * @param instance
     * @return
     * @author zuogang
     * @date 2020/1/3 15:21
     */
    @Override
    public boolean saveProService(ProServiceDO instance) {
        instance.setId(UuidUtils.createUUID());
        instance.setIsDelete(0);
        if (!save(instance)) {
            return false;
        }

        return true;
    }



    /**
     * 删除服务
     *
     * @param ids
     * @return
     * @author zuogang
     * @date 2020/1/3 15:21
     */
    @Override
    public boolean deleteByIds(List<String> ids) {
        if (ids.size() == 0) {
            return true;
        }
        List<ProServiceDO> serviceDOS = list(new QueryWrapper<ProServiceDO>().in("id", ids));
        serviceDOS.stream().forEach(serviceDO -> {
            serviceDO.setIsDelete(1);
        });
        if (!updateBatchById(serviceDOS)) {
            return false;
        }
        return true;
    }

    /**
     * 判断当前应用服务是否启动中
     *
     * @param id
     * @return
     * @author zuogang
     * @date 2020/1/3 15:21
     */
    @Override
    public String serverRunJudge(String id) {
        ProServiceDO serviceDO = getById(id);
        //判断nacos该应用是否启用中
        String url = "http://" + serviceDO.getNacosServerAddr() + "/nacos/v1/console/namespaces?namespaceId=";
        String resStr = this.client.client(url);
        List<String> nameSpaces = new ArrayList<>(16);
        JSONObject resNsObj = JSONObject.fromObject(resStr);
        JSONArray nsData = resNsObj.getJSONArray("data");
        for (int i = 0; i < nsData.size(); i++) {
            JSONObject obj = nsData.getJSONObject(i);
            nameSpaces.add(obj.getString("namespace"));
        }
        for (String ns : nameSpaces) {
            String address =
                    "http://" + serviceDO.getNacosServerAddr() +
                            "/nacos/v1/ns/catalog/services?withInstances=false&pageNo=1&pageSize=10000&namespaceId="
                            + ns;
            String rsStr = this.client.client(address);
            JSONObject resObj = JSONObject.fromObject(rsStr);
            JSONArray runApps = resObj.getJSONArray("serviceList");
            // 启动的应用列表
            List<String> apps = new ArrayList<>(16);
            if (runApps.size() > 0) {
                for (int i = 0; i < runApps.size(); i++) {
                    String name = runApps.getJSONObject(i).getString("name");
                    int ipCount = runApps.getJSONObject(i).getInt("ipCount");
                    if (ipCount == 1) {
                        apps.add(name);
                    }
                }
            }
            if (apps.contains(serviceDO.getAppId())) {
                // 存在
                return "1";
            }
        }
        return "0";
    }

    /**
     * 关闭服务
     *
     * @param id
     * @return
     * @author zuogang
     * @date 2020/1/3 15:21
     */
    @Override
    public boolean closeServer(String id) {
        ProServiceDO serviceDO = getById(id);
        String fileName = DevConstants.PUBLISH_SERVICE_LOCAL_CODE_ADDRESS + "\\" + serviceDO.getAppId();
        String closeCommand = "taskkill -f -t -im " + serviceDO.getAppId() + ".exe";
        // 关闭服务
        if (CMDUtil.excuteRunCommand("cmd /c cd " + fileName + "&&" + closeCommand)) {
            return true;
        }
        return false;
    }

    /**
     * 关闭服务
     *
     * @param id
     * @return
     * @author zuogang
     * @date 2020/1/3 15:21
     */
    @Override
    public boolean runServer(String id) {
        ProServiceDO serviceDO = getById(id);
        String fileName = DevConstants.PUBLISH_SERVICE_LOCAL_CODE_ADDRESS + "\\" + serviceDO.getAppId();
        String jarAddress = DevConstants.PUBLISH_SERVICE_LOCAL_CODE_ADDRESS + "\\" + serviceDO.getAppId() +
                "\\target\\" + serviceDO.getAppId() + "-1.0.0.jar";
        String runCommand = "start \"lock-server\" \"%JAVA_HOME%\\bin\\" + serviceDO.getAppId() + ".exe\" -Xms512m" +
                " -Xmx512m -Dfile" +
                ".encoding=utf-8 -jar " + jarAddress;
        // 开启服务
        if (CMDUtil.excuteRunCommand("cmd /c cd " + fileName + "&&" + runCommand)) {
            return true;
        }
        return false;
    }
}

package com.csicit.ace.license.controller;

import com.csicit.ace.license.entity.LicenseInfo;
import com.csicit.ace.license.service.LicenseInfoService;
import com.csicit.ace.license.util.FileUtils;
import com.csicit.ace.license.util.SerialNumberUtil;
import com.csicit.ace.license.util.SM2Util;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author shanwj
 * @date 2019/6/28 11:24
 */
@Controller
@RequestMapping("")
public class LicenseController {

    @Autowired
    LicenseInfoService licenseInfoService;


    @GetMapping("/")
    public String index() {
        return "index";
    }

    @RequestMapping(value = "/standAlone", method = RequestMethod.GET)
    public String aloneLicense(ModelMap map) {
        LicenseInfo licenseInfo = new LicenseInfo();
        // 获取主板序列号
        System.out.println("主板序列号:" + SerialNumberUtil.getMotherboardSN());
        licenseInfo.setMotherBoardNum(SerialNumberUtil.getMotherboardSN());
//        // 获取MAC地址
        System.out.println("MAC地址:" + SerialNumberUtil.getMac());
        licenseInfo.setMacAddress(SerialNumberUtil.getMac());
        map.addAttribute("licenseInfo", licenseInfo);
        return "standAlone/aloneLicense";
    }

    @RequestMapping("/downloadFile/{fileName}")
    public void downloadFile(@PathVariable("fileName") String fileName, HttpServletResponse response) {
        File file = new File("d://tmp//" + fileName + ".lic");
        FileUtils.download(response, file, "d://tmp//" + fileName + ".lic", fileName + ".lic");
    }

    @PostMapping("/save")
    @ResponseBody
    public void save(@RequestBody Map<String, Object> map) {
        String unitName = map.get("unitName").toString();
        String cpuNum = map.get("cpuNum").toString();
        String diskNum = map.get("diskNum").toString();
        String mbNum = map.get("mbNum").toString();
        String mac = map.get("mac").toString();
        String localIp = map.get("localIp").toString();
        List<Map<String, Object>> apps = (ArrayList<Map<String, Object>>) map.get("apps");
        JSONArray array = new JSONArray();
        apps.stream().forEach(m -> {
            try {
                JSONObject object = new JSONObject();
                object.put("appId", m.get("appId").toString());
                object.put("appName", m.get("appName").toString());
                object.put("count", m.get("count").toString());
                object.put("endDate", m.get("endDate").toString().substring(0, 10));
                array.add(object);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        });
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("unitName", unitName);
            jsonObject.put("localIp", localIp);
            jsonObject.put("cpuNum", cpuNum);
            jsonObject.put("diskNum", diskNum);
            jsonObject.put("mbNum", mbNum);
            jsonObject.put("mac", mac);
            jsonObject.put("apps", array);
            String jsonStr = jsonObject.toString();
            String sign = SM2Util.getSign(jsonStr);
            jsonObject.put("sign", sign);
            FileUtils.createFile(jsonObject.toString(), unitName);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @PostMapping("/saveAloneLicense")
    @ResponseBody
    public void saveAloneLicense(@RequestBody Map<String, Object> map) {
        String unitName = map.get("unitName").toString();
        String type = map.get("type").toString();
        JSONArray array = new JSONArray();

        JSONObject object = new JSONObject();
        try {
            object.put("appId", map.get("appId"));
            object.put("appName", map.get("appName"));
            object.put("count",1);
            object.put("endDate", map.get("endDate").toString().substring(0, 10));
            object.put("motherBoardNum",SerialNumberUtil.getMotherboardSN());
            object.put("macAddress",SerialNumberUtil.getMac());
            array.add(object);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        setLicenseInfo(unitName, type, array);
    }

    private void setLicenseInfo(String unitName, String type, JSONArray array) {
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("unitName", unitName);
            jsonObject.put("type", type);
            jsonObject.put("apps", array);
            String jsonStr = jsonObject.toString();
            String sign = SM2Util.getSign(jsonStr);
            jsonObject.put("sign", sign);
            FileUtils.createFile(jsonObject.toString(), unitName);
            LicenseInfo licenseInfo = new LicenseInfo();
            licenseInfo.setLicenseInfo(jsonObject.toString());
            licenseInfo.setUnitName(unitName);
            licenseInfo.setType(type);
            licenseInfoService.save(licenseInfo);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}

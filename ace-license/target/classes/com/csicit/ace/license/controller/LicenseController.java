package com.csicit.ace.license.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.csicit.ace.license.entity.LicenseAppsInfo;
import com.csicit.ace.license.entity.LicenseInfo;
import com.csicit.ace.license.service.LicenseAppsInfoService;
import com.csicit.ace.license.service.LicenseInfoService;
import com.csicit.ace.license.util.FileUtils;
import com.csicit.ace.license.util.SM2Util;
import com.csicit.ace.license.util.SerialNumberUtil;
import com.csicit.ace.license.util.SerializeUtils;
import org.apache.commons.codec.binary.Hex;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
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

    @Autowired
    LicenseAppsInfoService licenseAppsInfoService;


    @GetMapping("/")
    public String index() {
        return "index";
    }

    @RequestMapping(value = "/standAlone", method = RequestMethod.GET)
    public String aloneLicense(ModelMap map) {
        LicenseInfo licenseInfo = new LicenseInfo();
        // 获取主板序列号
        System.out.println("主板序列号:" + SerialNumberUtil.getMotherboardSN());
        licenseInfo.setMbNum(SerialNumberUtil.getMotherboardSN());
//        // 获取MAC地址
        System.out.println("MAC地址:" + SerialNumberUtil.getMac());
        licenseInfo.setMacAddress(SerialNumberUtil.getMac());
        map.addAttribute("licenseInfo", licenseInfo);
        return "standAlone/aloneLicense";
    }


    @PostMapping("/getLicenseInfo")
    @ResponseBody
    public LicenseInfo getLicenseInfo(@RequestBody Map<String, String> params) {
        String search = params.get("search");
        LicenseInfo licenseInfo = licenseInfoService.getOne(new QueryWrapper<LicenseInfo>()
                .orderByDesc("create_time")
                .and(StringUtils.isNotBlank(search)
                        , i -> i.like("UNIT_NAME", search)
                                .or().like("LOCAL_IP", search)
                                .or().like("DISK_NUM", search)
                                .or().like("MB_NUM", search)
                                .or().like("CPU_NUM", search)
                                .or().like("MAC_ADDRESS", search)));
        if (licenseInfo != null) {
            licenseInfo.setApps(licenseAppsInfoService.list(new QueryWrapper<LicenseAppsInfo>()
                    .eq("LICENSE_INFO_ID", licenseInfo.getId())));
        }
        return licenseInfo;
    }


    @RequestMapping("/downloadFile/{fileName}")
    public void downloadFile(@PathVariable("fileName") String fileName, HttpServletResponse response) {
        File file = new File("d://tmp//" + fileName + ".lic");
        FileUtils.download(response, file, "d://tmp//" + fileName + ".lic", fileName + ".lic");
    }

    @PostMapping("/save")
    @ResponseBody
    public void save(@RequestBody LicenseInfo licenseInfo) {
        System.out.println(licenseInfo);
        licenseInfo.setId(SerializeUtils.createUUID());
        List<LicenseAppsInfo> licenseAppsInfos = licenseInfo.getApps();
        licenseAppsInfos.forEach(licenseAppsInfo -> {
            licenseAppsInfo.setId(SerializeUtils.createUUID());
            licenseAppsInfo.setLicenseInfoId(licenseInfo.getId());
        });
        DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        JSONArray array = new JSONArray();
        // 排序
        licenseAppsInfos.stream().sorted(Comparator.comparing(LicenseAppsInfo::getAppId)).forEach(m -> {
            try {
                JSONObject object = new JSONObject();
                object.put("appId", m.getAppId());
                object.put("count", m.getLicenseNum().toString());
                object.put("endDate", df.format(m.getEndTime()).substring(0, 10));
                array.add(object);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        });

        try {
            JSONObject jsonObject = new JSONObject(true);
            jsonObject.put("localIp", licenseInfo.getLocalIp() == null ? "" : licenseInfo.getLocalIp().split(",")[0]);
            jsonObject.put("cpuNum", licenseInfo.getCpuNum() == null ? "" : licenseInfo.getCpuNum().split(",")[0]);
            jsonObject.put("diskNum", licenseInfo.getDiskNum() == null ? "" : licenseInfo.getDiskNum().split(",")[0]);
            jsonObject.put("mbNum", licenseInfo.getMbNum() == null ? "" : licenseInfo.getMbNum().split(",")[0]);
            jsonObject.put("macAddress", licenseInfo.getMacAddress() == null ? "" : licenseInfo.getMacAddress().split
                    (",")[0]);
            jsonObject.put("apps", array);

            // MD5加密
            String cipher = "";
            MessageDigest md = MessageDigest.getInstance("MD5");
            cipher = Hex.encodeHexString(md.digest(jsonObject.toString().getBytes(StandardCharsets.UTF_8)));
            licenseInfo.setCipher(cipher);
            licenseInfo.setLicenseInfo(jsonObject.toString());
            licenseInfo.setCreateTime(LocalDateTime.now());
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }


        if (licenseInfoService.save(licenseInfo)) {
            if (licenseAppsInfoService.saveBatch(licenseAppsInfos)) {
                try {
                    JSONObject jsonObject2 = new JSONObject();
                    jsonObject2.put("cipher", licenseInfo.getCipher());
                    jsonObject2.put("apps", array);
                    FileUtils.createFile(jsonObject2.toString(), licenseInfo.getUnitName());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
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
            object.put("count", 1);
            object.put("endDate", map.get("endDate").toString().substring(0, 10));
            object.put("motherBoardNum", SerialNumberUtil.getMotherboardSN());
            object.put("macAddress", SerialNumberUtil.getMac());
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
//            licenseInfo.setType(type);
            licenseInfoService.save(licenseInfo);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}

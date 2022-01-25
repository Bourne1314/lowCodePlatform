package com.csicit.ace.license.core.service;

import java.util.Map;

/**
 * @author FourLeaves
 * @version V1.0
 * @date 2021/9/14 10:31
 */
public interface LicenseService {
    void checkLicense();

    String register(Map<String, String> params);

    void macInfo();

    void fixRegisterApps();

    void checkManyServer();
}

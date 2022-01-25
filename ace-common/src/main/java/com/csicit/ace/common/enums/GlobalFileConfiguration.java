package com.csicit.ace.common.enums;

/**
 * 集团级文件配置标识
 *
 * @author JonnyJiang
 * @date 2020/6/11 16:00
 */
public enum GlobalFileConfiguration {
    /**
     * 通用配置
     */
    Normal("6907dd1b-80f8-4409-add9-7d56417579ca", "GlobalFileConfiguration.Normal"),
    DataScreenPicture("897904bf-95cc-439a-9103-e0ff62c58d9e", "GlobalFileConfiguration.DataScreenPicture");

    private String id;
    private String key;

    GlobalFileConfiguration(String id, String key) {
        this.id = id;
        this.key = key;
    }

    public boolean isKeyEquals(String key) {
        return this.key.equals(key);
    }

    public boolean isIdEquals(String id) {
        return this.id.equals(id);
    }

    public String getId() {
        return id;
    }

    public String getKey() {
        return key;
    }
}
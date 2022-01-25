package com.csicit.ace.dev.util;


/**
 * 列的属性
 *
 * @author generator
 * @version V1.0
 * @date 2019-04-15 20:14:36
 */
public class ColumnDO {
    // 列名
    private String columnName;
    // 列名类型
    private String dataType;
    // 列名备注
    private String comments;

    // 属性名称(第一个字母大写)，如：user_name => UserName
    private String attrName;
    // 属性名称(第一个字母小写)，如：user_name => userName
    private String attrname;
    // 属性类型
    private String attrType;
    // auto_increment
    private String extra;

    // 是否是数据表字段 0否，1是
    private String dbExistFlg;

    public String getColumnName() {
        return columnName;
    }

    public void setColumnName(String columnName) {
        this.columnName = columnName;
    }

    public String getDataType() {
        return dataType;
    }

    public void setDataType(String dataType) {
        this.dataType = dataType;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public String getAttrname() {
        return attrname;
    }

    public void setAttrname(String attrname) {
        this.attrname = attrname;
    }

    public String getAttrName() {
        return attrName;
    }

    public void setAttrName(String attrName) {
        this.attrName = attrName;
    }

    public String getAttrType() {
        return attrType;
    }

    public void setAttrType(String attrType) {
        this.attrType = attrType;
    }

    public String getExtra() {
        return extra;
    }

    public void setExtra(String extra) {
        this.extra = extra;
    }

    public String getDbExistFlg() {
        return dbExistFlg;
    }

    public void setDbExistFlg(String dbExistFlg) {
        this.dbExistFlg = dbExistFlg;
    }

    @Override
    public String toString() {
        return "ColumnDO{" +
                "columnName='" + columnName + '\'' +
                ", dataType='" + dataType + '\'' +
                ", comments='" + comments + '\'' +
                ", attrName='" + attrName + '\'' +
                ", attrname='" + attrname + '\'' +
                ", attrType='" + attrType + '\'' +
                ", extra='" + extra + '\'' +
                ", dbExistFlg='" + dbExistFlg + '\'' +
                '}';
    }
}

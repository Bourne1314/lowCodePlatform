package com.csicit.ace.report.core.flash.tag.base;

import com.stimulsoft.lib.utils.StiValidationUtil;
import com.stimulsoft.web.utils.StiConstants;

import javax.servlet.http.HttpServletRequest;
import java.io.ByteArrayOutputStream;
import java.net.URLEncoder;
import java.text.MessageFormat;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;

/**
 * @author shanwj
 * @date 2019/10/15 15:25
 */
public class AceStiBaseStiTag extends AceStiBaseTag {
    private static final long serialVersionUID = 7966197609409332244L;
    private static final String ATTRIBUTE = "&{0}={1}";
    private static final String ATTRIBUTE_STR = "&{0}";
    private static final String REPORT = "?{0}={1}";
    private static final String MORE_THAN_ONE_SOURCE = "Заданно два источника переменных отчета, variableMap и variableStr. Необходимо оставить только один!";
    private static final String ATTRIBUTE_NOT_FOUND = "Атрибут с именем ''{0}'' не найден.";
    private static final String ATTRIBUTE_NOT_MAP = "Атрибут с именем ''{0}'' является ''{1}'', а должен реализовывать interface Map. ";
    public static final String CUSTOM_PROPERTIES_PARAM = "properties";
    private String report;
    private String variableMap;
    private String variableStr;
    private String resourceKey;
    private Properties properties;
    private String globalization;

    public AceStiBaseStiTag() {
    }

    public void release() {
        super.release();
        this.report = null;
        this.variableMap = null;
        this.variableStr = null;
    }

    protected String generateHref() {
        this.valid();
        String src = this.getResource() + this.getReport() + this.getVariable();
        src = this.getPropertiesUrl(src);
        return src;
    }

    private String getPropertiesUrl(String src) {
        if (this.properties != null && !this.properties.isEmpty()) {
            try {
                ByteArrayOutputStream bos = new ByteArrayOutputStream();
                this.properties.store(bos, "");
                bos.close();
                return src + (StiValidationUtil.isNotEmpty(src) && src.contains("?") ? "&" : "?") + "properties" + "=" + URLEncoder.encode(new String(bos.toByteArray(), "UTF-8"), "UTF-8");
            } catch (Exception var3) {
                var3.printStackTrace();
                return "";
            }
        } else {
            return src;
        }
    }

    private String getVariable() {
        return StiValidationUtil.isNotBlank(this.report) ? this.prepareVariables() : "";
    }

    private String getReport() {
        return StiValidationUtil.isNotBlank(this.report) ? MessageFormat.format("?{0}={1}", StiConstants.STIMULSOFT_REPORT_KEY.value, this.report) : "";
    }

    private String getResource() {
        HttpServletRequest request = (HttpServletRequest)this.pageContext.getRequest();
        return request.getContextPath() + "/report/" + this.resourceKey;
    }

    private void valid() {
        if (this.variableMap != null && this.variableStr != null) {
            throw new IllegalArgumentException("Заданно два источника переменных отчета, variableMap и variableStr. Необходимо оставить только один!");
        } else {
            if (StiValidationUtil.isNotBlank(this.report) && StiValidationUtil.isNotBlank(this.variableMap)) {
                Object findAttribute = this.pageContext.findAttribute(this.variableMap);
                if (findAttribute == null) {
                    throw new IllegalArgumentException(MessageFormat.format("Атрибут с именем ''{0}'' не найден.", this.variableMap));
                }

                if (!(findAttribute instanceof Map)) {
                    throw new IllegalArgumentException(MessageFormat.format("Атрибут с именем ''{0}'' является ''{1}'', а должен реализовывать interface Map. ", this.variableMap, findAttribute.getClass().toString()));
                }
            }

        }
    }

    private String prepareVariables() {
        String result = "";
        if (this.variableStr != null) {
            result = MessageFormat.format("&{0}", this.variableStr);
        } else if (this.variableMap != null) {
            result = this.createVariableFromMap();
        }

        return result;
    }

    private String createVariableFromMap() {
        Map<String, String> map = this.findMap();
        StringBuilder sb = new StringBuilder();
        Iterator i$ = map.entrySet().iterator();

        while(i$.hasNext()) {
            Map.Entry<String, String> pair = (Map.Entry)i$.next();
            sb.append(MessageFormat.format("&{0}={1}", pair.getKey(), pair.getValue()));
        }

        if (StiValidationUtil.isNotEmpty(this.globalization)) {
            sb.append(MessageFormat.format("&{0}={1}", "globalization", this.globalization));
        }

        return sb.toString();
    }

    private Map<String, String> findMap() {
        return (Map)this.pageContext.findAttribute(this.variableMap);
    }

    protected void setResourceKey(String resourceKey) {
        this.resourceKey = resourceKey;
    }

    public String getVariableMap() {
        return this.variableMap;
    }

    public void setVariableMap(String variableMap) {
        this.variableMap = variableMap;
    }

    public String getVariableStr() {
        return this.variableStr;
    }

    public void setVariableStr(String variableStr) {
        this.variableStr = variableStr;
    }

    public void setReport(String report) {
        this.report = report;
    }

    public void setGlobalization(String globalization) {
        this.globalization = globalization;
    }

    public String getGlobalization() {
        return this.globalization;
    }

    public Properties getProperties() {
        return this.properties;
    }

    public void setProperties(Properties properties) {
        this.properties = properties;
    }
}

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">

<%@page import="com.csicit.ace.report.core.config.Constants" %>
<%@page import="com.csicit.ace.report.core.h5.AceStiWebDesigerHandler" %>
<%@ page import="com.stimulsoft.webdesigner.StiWebDesignerOptions" %>
<%@ page import="com.stimulsoft.webdesigner.enums.StiDesignerPermissions" %>
<%@page language="java" contentType="text/html; charset=utf-8" pageEncoding="UTF-8" %>
<%@taglib uri="http://stimulsoft.com/webdesigner" prefix="stiwebdesigner" %>
<%@taglib uri="http://stimulsoft.com/acewebdesigner" prefix="acestiwebdesigner" %>

<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <title>Stimulsoft Webdesigner for Java</title>
    <style type="text/css">
    </style>
</head>
<body>
<%
    StiWebDesignerOptions options = new StiWebDesignerOptions();
    options.getDictionary().setPermissionDataConnections(StiDesignerPermissions.View);
    try {
        options.setLocalizationStream(Constants.getXmlStr());
    } catch (Exception e) {

    }
    AceStiWebDesigerHandler handler = new AceStiWebDesigerHandler(request);
    pageContext.setAttribute("handler", handler);
    pageContext.setAttribute("options", options);
%>
<acestiwebdesigner:acewebdesigner
        handler="${handler}" options="${options}"/>
</body>
</html>


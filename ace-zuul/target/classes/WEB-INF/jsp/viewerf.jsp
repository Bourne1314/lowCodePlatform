<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<%@ page contentType="text/html;charset=UTF-8" import="java.util.*"%>
<%@ page import="java.net.URLEncoder" %>
<%@ taglib uri="http://stimulsoft.com/designer" prefix="stidesignerfx"%>
<%@ taglib uri="http://stimulsoft.com/aceviewerf" prefix="acestiviewerfx"%>
<%@ taglib uri="http://stimulsoft.com/viewer" prefix="stiviewerfx"%>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>Stimulsoft report</title>
</head>
<body>

<%
    String query = request.getQueryString();
    Properties props = new Properties();
    props.put("Viewer.Toolbar.ShowOpenButton","False");
    request.setAttribute("props", props);
    Map<String, String> variableMap = new HashMap<String, String>();
    variableMap.put("Variable1", "St");
    request.setAttribute("map",variableMap);
    request.setAttribute("props",props);
%>
<acestiviewerfx:iframe report="<%=query%>" variableMap="map"
                    width="100%" height="100%" align="top" scrolling="no" properties="${props}"/>
</body>
</html>
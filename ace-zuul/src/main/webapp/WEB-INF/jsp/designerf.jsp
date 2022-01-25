<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<%@ page contentType="text/html;charset=UTF-8" import="java.util.*"%>
<%@ taglib uri="http://stimulsoft.com/acedesignerf" prefix="acestidesignerfx"%>
<%@ taglib uri="http://stimulsoft.com/aceviewerf" prefix="acestiviewerfx"%>

<html>
<head>
    <title>Report</title>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
</head>

<%


    String id = request.getQueryString().split("=")[1];
    final String reportPath = id;
    Properties props = new Properties();
    props.put("Theme","Office2013");
    request.setAttribute("props", props);
    Map<String, String> variableMap = new HashMap<String, String>();
    variableMap.put("Variable1","variable");
    request.setAttribute("map",variableMap);
    request.setAttribute("props",props);
%>

<body marginheight="0" marginwidth="0">
<acestidesignerfx:iframe  report="<%=reportPath%>" width="100%" height="100%" variableStr="Parameter1=30" properties="${props}" />
</body>
</html>
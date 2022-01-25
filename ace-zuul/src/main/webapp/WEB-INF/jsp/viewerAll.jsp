<%@ page import="com.csicit.ace.common.utils.MapUtils" %>
<%@ page import="java.util.Map" %>
<%@ page import="org.apache.commons.lang3.StringUtils" %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">

</head>
<body>
<script >
    /**
     * 判断IE版本
     * return
     -1    Number  不是ie浏览器
     6 Number ie版本<=6
     7 Number ie7
     8 Number ie8
     9 Number ie9
     10    Number ie10
     11    Number ie11
     'edge'    String ie的edge浏览器
     * */
    function IEVersion() {
        var userAgent = navigator.userAgent; //取得浏览器的userAgent字符串
        var isIE = userAgent.indexOf("compatible") > -1 && userAgent.indexOf("MSIE") > -1; //判断是否IE<11浏览器
        var isEdge = userAgent.indexOf("Edge") > -1 && !isIE; //判断是否IE的Edge浏览器
        var isIE11 = userAgent.indexOf('Trident') > -1 && userAgent.indexOf("rv:11.0") > -1;
        if(isIE) {
            var reIE = new RegExp("MSIE (\\d+\\.\\d+);");
            reIE.test(userAgent);
            var fIEVersion = parseFloat(RegExp["$1"]);
            if(fIEVersion == 7) {
                return 7;
            } else if(fIEVersion == 8) {
                return 8;
            } else if(fIEVersion == 9) {
                return 9;
            } else if(fIEVersion == 10) {
                return 10;
            } else {
                return 6;//IE版本<=7
            }
        } else if(isEdge) {
            return 0;//edge
        } else if(isIE11) {
            return 11; //IE11
        }else{
            return -1;//不是ie浏览器
        }
    }
    var httpUrl = "";
    if (IEVersion()>=6&&IEVersion()<11){
        httpUrl =  window.location.protocol + "//" +
            window.location.hostname +
            (window.location.port ? ':' + window.location.port: '');
        window.location.href =+"/report/viewerf?${query}";
    }else{
        httpUrl = window.location.origin;
    }
    window.location.href = httpUrl+"/report/viewerh?${query}"+"&httpUrl="+httpUrl;

</script>
</body>
</html>
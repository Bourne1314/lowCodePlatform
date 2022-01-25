
/**
 * 初始化socketio连接
 *
 * @param gwAddress
 * @param appId
 * @param callback
 */
function initSocketConnect(appId){
    var socket = io(window.location.origin+'/'+appId, {
        path: '/push/socket.io',
        'reconnectionAttempts': 10     // 限制对于 socket 服务器的重连次数为10次
    });
    return socket;
}

/** 
 *
 * @param objName 参数名称
 * @return 
 * @author shanwj
 * @date 2019/10/28 8:44
 */
function getCookie(objName) {//获取指定名称的cookie的值
    var arrStr = document.cookie.split("; ");
    for (var i = 0; i < arrStr.length; i++) {
        var temp = arrStr[i].split("=");
        if (temp[0] == objName) return unescape(temp[1]);  //解码
    }
    return "";
}




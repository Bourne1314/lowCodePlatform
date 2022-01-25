/**
 * 初始化socketio连接
 *
 * @param gwAddress
 * @param appId
 * @param callback
 */
// function initSocketConnect(appId){
//     var socket = io(window.location.origin+'/'+appId, {
//         path: '/push/socket.io',
//         'reconnectionAttempts': 10     // 限制对于 socket 服务器的重连次数为10次
//     });
//     return socket;
// }

/**
 * 初始化socketio连接
 *
 * @param gwAddress
 * @param appId
 * @param callback
 */
/**
 * cookie中取值
 * */
function getCookie (name) {
    var arr,reg = new RegExp("(^| )" + name + "=([^;]*)(;|$)");	//匹配字段
    if (arr = document.cookie.match(reg)) {
        return unescape(arr[2]);
    } else {
        return null;
    }
};

function initSocketConnect(appId){
    var zuulFlag = getCookie('zuulFlag');
    var monoFlag = getCookie('monoFlag');
    if (zuulFlag == '1' || zuulFlag == 1 ) {
        var port = getCookie('socketPort');
        var url = 'http://' + window.location.hostname + ":" + port;
        if (window.location.origin.startsWith('https')) {
            url = 'https://' + window.location.hostname + ":" + port
        }
        return io.connect(url);
    } else if (monoFlag == '1' || monoFlag == 1 ) {
        var port = getCookie('socketPort');
        var url = 'http://' + window.location.hostname + ":" + port;
        if (window.location.origin.startsWith('https')) {
            url = 'https://' + window.location.hostname + ":" + port
        }
        return io.connect(url);
    } else {
        return io(window.location.origin + '/' + appId, {
            path: '/push/socket.io',
            'reconnectionAttempts': 10     // 限制对于 socket 服务器的重连次数为10次
        });
    }
}

function createUuid(){
    return 'xxxxxxxx-xxxx-4xxx-yxxx-xxxxxxxxxxxx'.replace(/[xy]/g, function (c) {
        var r = Math.random() * 16 | 0,
            v = c == 'x' ? r : (r & 0x3 | 0x8);
        return v.toString(16);
    });
}



